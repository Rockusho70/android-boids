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
package se.uncle.boidsdemo.example;

import java.util.ArrayList;

import se.uncle.boids.Boid;
import se.uncle.boids.Vector3f;
import se.uncle.renderview.RenderView.RenderEngine;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class MetaBallsRenderEngine implements RenderEngine {
	private ArrayList<Boid> mAllBoids = new ArrayList<Boid>();
	private ArrayList<Boid> mGhostBoids = new ArrayList<Boid>();

	private Vector3f mTarget;

	private Paint mWhite = new Paint();
	private Paint mBlack = new Paint();
	
	
	public void initialize(Resources resources) {
		mTarget = new Vector3f(100, 100, 20);

		Boid boid;
		for (int i = 0; i < 10; i++) {
			boid = new Boid(90, 40, null, mGhostBoids, mAllBoids);
			boid.setTarget(mTarget);
		}
		
		mWhite.setColor(0xFFFFFFFF);
		mBlack.setColor(0xFF000000);
		
	}

	public void Update() {
		for (int i = 0; i < mAllBoids.size(); i++) {
			mAllBoids.get(i).update();
		}
	}

	public void render(Canvas c) {
		Boid boid;
		for (int i = 0; i < mAllBoids.size(); i++) {
			boid = mAllBoids.get(i);
			c.drawCircle(boid.position.x, boid.position.y, 30+boid.position.z, mWhite);
		}
		
		for (int i = 0; i < mAllBoids.size(); i++) {
			boid = mAllBoids.get(i);
			c.drawCircle(boid.position.x, boid.position.y, (float) (25+boid.position.z), mBlack);
		}
	}

	public void setDimention(int width, int height) {
		for (int i = 0; i < mAllBoids.size(); i++) {
			mAllBoids.get(i).setBoundery(width, height);
		}
	}

	/** BOIDS **/

	public boolean onTouch(View v, MotionEvent event) {
		mTarget.x = event.getX();
		mTarget.y = event.getY();
		return true;
	}
}
