package jp.live.sato1101.androidgames.framework.gl;


import com.badlogic.androidgames.framework.gl.TextureRegion;


public class ParticleSystem {
	
	public final int mCapacity;
	private Particle[] mParticles;
	
	public ParticleSystem(int capacity, float particleLifeSpan) {
		mCapacity = capacity;
		mParticles = new Particle[mCapacity];
		for(int i=0; i<mCapacity; i++) {
			mParticles[i] = new Particle();
			mParticles[i].mLifeSpan = particleLifeSpan;
		}
	}
	
	public void add(float x, float y, float size, float moveX, float moveY) {
		for(Particle p : mParticles) {
			if(!p.mIsActive) {
				p.mIsActive = true;
				p.mX = x;
				p.mY = y;
				p.mMoveX = moveX;
				p.mMoveY = moveY;
				p.mSize = size;
				p.mStateTime = 0;
				break;
			}
		}
	}
	
	public void update(float deltaTime) {
		for(Particle p : mParticles) {
			if(p.mIsActive) {
			    p.update(deltaTime);
			}
		}		
	}
	
	public void draw(ColorableSpriteBatcher batcher, TextureRegion region) {
		for(int i=0; i<mCapacity; i++) {
			Particle p = mParticles[i];
			if(!p.mIsActive) {
				continue;
			}
			
			float lifePercentage =  p.mStateTime /  p.mLifeSpan;
			float alpha = 1.0f;
			if(lifePercentage <= 0.5f) {
				alpha = lifePercentage * 2.0f;
			} else {
				alpha = 1.0f - (lifePercentage - 0.5f) * 2.0f;
			}
			batcher.drawSprite(p.mX, p.mY, p.mSize, p.mSize, region,
					1.0f, 1.0f, 1.0f, alpha);
		}
	}
	
}
