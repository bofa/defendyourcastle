/**
 * 
 */
package com.kvantnysning.castle;

import java.util.ArrayList;

import com.kvantnysning.castle.objects.Soldier;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;

	public final ArrayList<Soldier> soldiers = new ArrayList<Soldier>();

	private Soldier markedSoldier;

	private GestureDetector gestureScanner;

	private int WIDTH;

	private int HEIGTH;

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		//TODO Test av soldater
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 60));
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 70));
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 80));

		soldiers.get(0).setThrow(100, 300, 0, -400);

		gestureScanner = new GestureDetector(context, new MyGestureDetector());

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		WIDTH = width;
		HEIGTH = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureScanner.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (markedSoldier != null ){
				markedSoldier.setPosition(event.getX(), event.getY());
			}			
		}
		return true;
	}

	public void render(Canvas canvas) {
		Paint paint  = new Paint();
		paint.setColor(Color.GREEN);
		canvas.drawColor(Color.BLACK);
		canvas.drawRect(0, 400, WIDTH, HEIGTH, paint);

		for(Soldier s : soldiers){
			s.draw(canvas);
		}

	}

	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {

		Soldier indexDead = null;
		
		for(Soldier s : soldiers){
			if(s.update(0.01)){
				indexDead = s;
			}
		}
		
		if(indexDead!=null)
			soldiers.remove(indexDead);

	}

	class MyGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			if(markedSoldier != null) {
				markedSoldier.setThrow(e2.getX(), e2.getY(), velocityX/4, velocityY/4);
				markedSoldier = null;
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent arg0) {

			for(Soldier s : soldiers){
				if(s.isClicked(arg0.getX(),arg0.getY())) {
					markedSoldier = s;
					markedSoldier.setPosition(arg0.getX(), arg0.getY());
				}
			}

			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

	}


}
