package jp.live.sato1101.androidgames.framework.gl;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ScreenCapture {
	
	public static Bitmap capture(GL10 gl, int x, int y, int width, int height) {
    	gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);
		// Read Pixels
		ByteBuffer pixels = ByteBuffer.allocateDirect(width * height * 4);
		gl.glReadPixels(x, y, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixels);

		gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 4);
		
		// Create Bitmap
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmp.copyPixelsFromBuffer(pixels);
		
		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1);
		matrix.postRotate(180.0f);
		Bitmap screen = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		bmp.recycle();
		
		return screen;
	}
}
