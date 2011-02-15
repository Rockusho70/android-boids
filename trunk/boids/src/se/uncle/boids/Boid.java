/**
 * Copyright 2010 Per-Erik Bergman (bergman@uncle.se)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.uncle.boids;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boid {

	private ArrayList<Boid> mFriendBoids = new ArrayList<Boid>();
	private ArrayList<Boid> mAllBoids = new ArrayList<Boid>();

	private int mWidth;

	private int mHeight;

	private float mSize = 30f;

	public Vector3f velocity;

	public Vector3f position;

	public Vector3f mTarget;

	private Bitmap mBitmap;

	private Paint mBoidPaint = new Paint();

	public Boid(float x, float y, Bitmap bitmap, ArrayList<Boid> friends,
			ArrayList<Boid> allBoids) {
		velocity = new Vector3f(0, 0, 0);
		position = new Vector3f(x, y, 0);
		mTarget = new Vector3f(x, y, 0);
		mBitmap = bitmap;
		mFriendBoids = friends;
		mAllBoids = allBoids;

		mFriendBoids.add(this);
		mAllBoids.add(this);
	}

	public void update() {
		velocity.add(rule1(this));
		velocity.add(rule2(this));
		velocity.add(rule3(this));
		velocity.add(bound_position(this));
		velocity.add(tend_to_place(this));
		limit_velocity(this, 5);
		position.add(this.velocity);

	}

	public void render(Canvas canvas) {
		canvas.drawBitmap(mBitmap, position.x, position.y, mBoidPaint);
	}

	// Rule 1: Boids try to fly towards the centre of mass of neighbouring
	// boids.
	private Vector3f rule1(Boid bj) {
		Vector3f pcJ = new Vector3f();
		if (mFriendBoids.size() == 1) {
			return pcJ;
		}

		Boid b;
		for (int i = 0; i < mFriendBoids.size(); i++) {
			b = mFriendBoids.get(i);
			if (b != bj) {
				pcJ = pcJ.add(b.position);
			}
		}
		pcJ = pcJ.divide(mFriendBoids.size() - 1);
		return pcJ.sub(bj.position).divide(100);
	}

	// Rule 2: Boids try to keep a small distance away from other objects
	// (including other boids).
	private Vector3f rule2(Boid bj) {
		Vector3f c = new Vector3f();
		if (mAllBoids.size() == 1) {
			return c;
		}

		Vector3f tmp;
		Boid b;
		for (int i = 0; i < mAllBoids.size(); i++) {
			b = mAllBoids.get(i);
			if (b != bj) {
				tmp = b.position.clone();
				tmp.sub(bj.position);
				if (tmp.length() < mSize) {
					c.sub(tmp);
				}
			}
		}
		return c;
	}

	// Rule 3: Boids try to match velocity with near boids.
	private Vector3f rule3(Boid bj) {
		Vector3f pvJ = new Vector3f();
		if (mFriendBoids.size() == 1) {
			return pvJ;
		}

		Boid b;
		for (int i = 0; i < mFriendBoids.size(); i++) {
			b = mFriendBoids.get(i);
			if (b != bj) {
				pvJ.add(b.velocity);
			}
		}

		pvJ.divide(mFriendBoids.size() - 1);
		pvJ.sub(bj.velocity).divide(8);
		return pvJ;
	}

	// Tendency towards a particular place
	private Vector3f tend_to_place(Boid b) {
		Vector3f tmp = mTarget.clone();
		return (tmp.sub(b.position)).divide(100);
	}

	// Limiting the speed
	private void limit_velocity(Boid b, int vlim) {
		if (b.velocity.length() > vlim) {
			b.velocity = (b.velocity.divide(b.velocity.length()))
					.multiply(vlim);
		}
	}

	// Bounding the position
	private Vector3f bound_position(Boid b) {
		Vector3f v = new Vector3f();

		if (b.position.x < 0) {
			v.x = 10;
		} else if (b.position.x > mWidth) {
			v.x = -10;
		}

		if (b.position.y < 0) {
			v.y = 10;
		} else if (b.position.y > mHeight) {
			v.y = -10;
		}

		if (b.position.z < 0) {
			v.z = 10;
		} else if (b.position.z > 10) {
			v.z = -10;
		}

		return v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((velocity == null) ? 0 : velocity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boid other = (Boid) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (velocity == null) {
			if (other.velocity != null)
				return false;
		} else if (!velocity.equals(other.velocity))
			return false;
		return true;
	}

	public void setBoundery(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public void setTarget(Vector3f target) {
		mTarget = target;
	}

}
