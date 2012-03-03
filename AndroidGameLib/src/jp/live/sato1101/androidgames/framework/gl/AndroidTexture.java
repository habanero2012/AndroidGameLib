package jp.live.sato1101.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import jp.live.sato1101.androidgames.framework.AndroidFileIO;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLUtils;

import com.badlogic.androidgames.framework.GLGame;
import com.badlogic.androidgames.framework.Texture;
import com.badlogic.androidgames.framework.impl.GLGraphics;

public class AndroidTexture implements Texture{
	GLGraphics glGraphics;
	AndroidFileIO fileIO;
	int resId;
	private int textureId;
	private int minFilter;
	private int magFilter;
    private int width;
    private int height;
	private boolean mipmapped;
    
	public AndroidTexture(GLGame glGame, int resId, boolean mipmapped) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = (AndroidFileIO) glGame.getFileIO();
		this.resId = resId;
		this.mipmapped = mipmapped;
		load();
	}
    
	public AndroidTexture(GLGame glGame, int resId) {
		this(glGame, resId, false);
	}

	private void load() {
		GL10 gl = glGraphics.getGL();
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];

		Bitmap bitmap = fileIO.getBitmap(resId);
		if(bitmap == null) {
			throw new RuntimeException("Couldn't load texture '" + resId +"'");
		}
		
		if(mipmapped) {
		    createMipmaps(gl, bitmap);	
		} else {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	        width = bitmap.getWidth();
	        height = bitmap.getHeight();
	        bitmap.recycle();
		}
	}
	
	private void createMipmaps(GL10 gl, Bitmap bitmap) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		setFilters(GL10.GL_LINEAR_MIPMAP_NEAREST, GL10.GL_LINEAR);

		int level = 0;
		int newWidth = width;
		int newHeight = height;
		while (true) {
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			newWidth = newWidth / 2;
			newHeight = newHeight / 2;
			if (newWidth <= 0)
				break;
			Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight,
					bitmap.getConfig());
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(bitmap,
					new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new Rect(0, 0, newWidth, newHeight), null);
			bitmap.recycle();
			bitmap = newBitmap;
			level++;
		}

		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		bitmap.recycle();
	}

	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.getGL();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
