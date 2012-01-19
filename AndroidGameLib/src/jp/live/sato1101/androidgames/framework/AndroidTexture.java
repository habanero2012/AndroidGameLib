package jp.live.sato1101.androidgames.framework;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
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
	
	public AndroidTexture(GLGame glGame, int resId) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = (AndroidFileIO) glGame.getFileIO();
		this.resId = resId;
		load();
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
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
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
