package jp.live.sato1101.androidgames.framework;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.impl.GLGraphics;
import com.badlogic.androidgames.framework.math.Vector2;

public class Camera2D {
	public final Vector2 position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	
	public Camera2D(GLGraphics glGraphics, float frustumWidth, float frustumHeight) {
		this.glGraphics = glGraphics;
		this.frustumWidth = frustumWidth;
		this.frustumHeight = frustumHeight;
		this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
		this.zoom = 1.0f;
	}

	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(Global.surfaceOffsetWidth, Global.surfaceOffsetHeight,
				Global.surfaceWidth, Global.surfaceHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustumWidth * zoom / 2,
				position.x + frustumWidth * zoom/ 2,
				position.y - frustumHeight * zoom / 2,
				position.y + frustumHeight * zoom/ 2,
				1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void touchToWorld(Vector2 touch) {
		touch.sub(Global.surfaceOffsetWidth, Global.surfaceOffsetHeight);
		touch.x = (touch.x / (float) Global.surfaceWidth) * frustumWidth * zoom;
		touch.y = (1 - touch.y / (float) Global.surfaceHeight) * frustumHeight * zoom;
		touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
	}
}
