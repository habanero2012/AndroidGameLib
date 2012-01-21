package jp.live.sato1101.androidgames.framework.gl;

public class Particle {

	public float mX;
	public float mY;
	public float mMoveX;
	public float mMoveY;
	public float mSize;
	public boolean mIsActive;
	
	public float mStateTime;
	public float mLifeSpan;
	
	public Particle() {
		mX = 0.0f;
		mY = 0.0f;
		mMoveX = 0.0f;
		mMoveY = 0.0f;
		mSize = 1.0f;
		mIsActive = false;
		
		mLifeSpan = 1;
		mStateTime = 0;
	}
	
	public void update(float deltaTime) {
		mStateTime += deltaTime;
		if(mStateTime >= mLifeSpan) {
			mIsActive = false;
		}
			
		mX += mMoveX * deltaTime;
		mY += mMoveY * deltaTime;
	}
	
}
