package jp.live.sato1101.androidgames.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.androidgames.framework.FileIO;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Environment;

public class AndroidFileIO implements FileIO {
	
	
	private AssetManager mAssetManager;
	private Resources mResources;
	private BitmapFactory.Options options;
	private String mExternalStoragePath;
	
    public AndroidFileIO(Context context) {
    	mAssetManager = context.getAssets();
    	mResources = context.getResources();
    	
    	options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inPreferredConfig = Config.ARGB_8888;
		
    	mExternalStoragePath = Environment.getExternalStorageDirectory()
    			.getAbsolutePath() + File.separator;
    }
    
    @Override
	public InputStream readAsset(String fileName) throws IOException {
		return mAssetManager.open(fileName);
	}
	
    @Override
	public InputStream readFile(String fileName) throws IOException {
		return new FileInputStream(mExternalStoragePath + fileName);
	}

    @Override
	public OutputStream writeFile(String fileName) throws IOException {
		return new FileOutputStream(mExternalStoragePath + fileName);
	}
	
	public Bitmap getBitmap(int resId) {
		return BitmapFactory.decodeResource(mResources, resId, options);
	}
}
