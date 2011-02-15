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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RenderView extends SurfaceView implements SurfaceHolder.Callback {

	private RenderEngine mRenderEngine;

	private Context mContext;

	private RenderThread mRenderThread;

	public RenderView(Context context) {
		super(context);
		mContext = context;
	}

	public RenderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public RenderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public void setRenderEngine(RenderEngine renderEngine) {
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mRenderEngine = renderEngine;
		mRenderEngine.initialize(mContext.getResources());
		mRenderThread = new RenderThread(holder, mContext, new Handler(),
				mRenderEngine);
		setFocusable(true);
		setOnTouchListener(renderEngine);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mRenderEngine != null) {
			mRenderEngine.setDimention(width, height);
		}
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		mRenderThread.setRunning(false);
		while (retry) {
			try {
				mRenderThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		if (!mRenderThread.isRunning()) {
			mRenderThread = new RenderThread(getHolder(), mContext,
					new Handler(), mRenderEngine);
			mRenderThread.start();
		} else {
			mRenderThread.start();
		}
	}

	public interface RenderEngine extends OnTouchListener {

		public void initialize(Resources resources);

		public void Update();

		public void render(Canvas c);

		public void setDimention(int width, int height);

	}
}
