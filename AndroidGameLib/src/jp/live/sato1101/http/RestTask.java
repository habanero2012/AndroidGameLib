/**
 * 非同期にhttpリクエストをするクラス
 * 処理の完了はブロードキャストで通知される
 * レスポンスを受け取るにはRestTaskReceiverを継承したクラスを作成する
 * 
 * 
 * 
 * 使い方
 * 
 * 1.RestTaskReceiverを継承したクラスを作成
 * 
 * private RestTaskReceiver mReceiver = new RestTaskReceiver() {
 *     @Override
 *	   public void onReceive(String response) {
 *	       if(mProgress != null) {
 *		       mProgress.dismiss();
 *		   }
 *		   mTextView.setText(response);
 *     }
 * };
 * 
 * 
 * 2.onResumeにRestTaskReceiverを登録、onPauseに登録を解除する
 * 
 * public void onResume() {
　*   	super.onResume();
 *      mReceiver.registerReceiver(this, "action_name"); // Intentのaction名を指定
 * }
 * 
 * public void onPause() {
 *     super.onPause();
 *     mReceiver.unregisterReceiver(this);
 * }
 * 
 * 
 * 3.RestTaskを実行する
 * 
 * String url = "http://www.google.co.jp";
 * HttpGet request = new HttpGet(new URI(url));
 * RestTask task = new RestTask(this, "action_name"); // Intentのaction名を指定
 * task.execute(request);
 */
package jp.live.sato1101.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;


public class RestTask extends AsyncTask<HttpUriRequest, Void, String>{

	public static final String HTTP_RESPONSE = "httpResponse";
	
	private Context mContext;
	private String mIntentActionName;
	private HttpClient mClient;
	
	/**
	 * 新規にHttpClientを作成するコンストラクタ
	 * @param context
	 * @param intentActionName Intentのactionに使用される文字列
	 */
	public RestTask(Context context, String intentActionName) {
		this(context, intentActionName, new DefaultHttpClient());
	}
	
    /**
     * 既存のHttpClientを使いまわせるコンストラクタ
     * @param context
     * @param intentActionName Intentのactionに使用される文字列
     * @param client
     */
    public RestTask(Context context, String intentActionName, HttpClient client) {
		mContext = context;
		mIntentActionName = intentActionName;
		mClient = client;
	}
    
    /**
     * Basic認証をする
     * @param username
     * @param password
     */
    public void setBasicAuth(String userName, String password) {
    	UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
    	((DefaultHttpClient)mClient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    }
	
	@Override
	protected String doInBackground(HttpUriRequest... params) {
		HttpUriRequest request = params[0];
		try {
			HttpResponse serverResponse = mClient.execute(request);
			
			BasicResponseHandler handler = new BasicResponseHandler();
			String response = handler.handleResponse(serverResponse);
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		Intent intent = new Intent(mIntentActionName);
		intent.putExtra(HTTP_RESPONSE, result);
		// 処理の完了をブロードキャストする
		mContext.sendBroadcast(intent);
	}

	
	
	/**
	 * RestTaskの処理完了の通知をうけるBroadcastReciver
	 */
	public abstract static class RestTaskReceiver extends BroadcastReceiver {

		/**
		 * このBroadcastReciverを登録する
		 * @param context
		 * @param intentActionName 通知を受け取るRestTaskと同じintentActionNameを指定する
		 */
		public void registerReceiver(Context context, String intentActionName) {
			context.registerReceiver(this, new IntentFilter(intentActionName));
		}
		
		
		/**
		 * このBroadcastReciverの登録を解除する
		 * @param context
		 */
		public void unregisterReceiver(Context context) {
			context.unregisterReceiver(this);
		}
		
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String response = intent.getStringExtra(RestTask.HTTP_RESPONSE);
			onReceive(response);
		}

		/**
		 * RestTaskが取得したHTTPレスポンスのbodyを受け取る
		 * @param response HTTPレスポンスのbody
		 */
		abstract public void onReceive(String response);
	}
}
