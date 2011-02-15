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
package se.uncle.renderview;

import java.util.logging.Level;
import java.util.logging.Logger;

import se.uncle.renderview.RenderView.RenderEngine;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

public class RenderThread extends Thread {
	private SurfaceHolder mSurfaceHolder;
	private Paint mBackgroundColor;
	private RenderEngine mRenderEngine;
	private boolean mRunning = true;

	// for consistent rendering
	private long mSleepTime = 0;

	// amount of time to sleep for (in milliseconds)
	private long mDelay = 20;

	public RenderThread(SurfaceHolder surfaceHolder, Context context,
			Handler handler, RenderEngine renderEngine) {

		mSurfaceHolder = surfaceHolder;

		// Clear color.
		mBackgroundColor = new Paint();
		mBackgroundColor.setARGB(255, 0, 0, 0);

		mRenderEngine = renderEngine;
	}

	@Override
	public void run() {
		while (mRunning) {
			// time before update
			long beforeTime = System.nanoTime();
			// This is where we update the game engine
			mRenderEngine.Update();

			// DRAW
			Canvas c = null;
			try {
				// lock canvas so nothing else can use it
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					// clear the screen with the black painter.
					c.drawRect(0, 0, c.getWidth(), c.getHeight(), mBackgroundColor);
					// This is where we draw the game engine.
					mRenderEngine.render(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}

			// SLEEP
			// Sleep time. Time required to sleep to keep game consistent
			// This starts with the specified delay time (in milliseconds) then
			// subtracts from that the
			// actual time it took to update and render the game. This allows
			// our game to render smoothly.
			this.mSleepTime = mDelay
					- ((System.nanoTime() - beforeTime) / 1000000L);

			try {
				// actual sleep code
				if (mSleepTime > 0) {
					Thread.sleep(mSleepTime);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(RenderThread.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}
	
	public void setRunning(boolean value){
		mRunning = value;
	}
	
	public boolean isRunning(){
		return mRunning;
	}
}
