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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private MainThread thread;
	
	private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();

	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		//TODO Test av soldater
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 10));
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 20));
		soldiers.add(new Soldier(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 0, 0, 30));
		
		soldiers.get(0).setThrow(0, 0, 0, 0);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
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
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			// delegating event handling to the droid
//			droid.handleActionDown((int)event.getX(), (int)event.getY());
//			
//			// check if in the lower part of the screen we exit
//			if (event.getY() > getHeight() - 50) {
//				thread.setRunning(false);
//				((Activity)getContext()).finish();
//			} else {
//				Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
//			}
//		} if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			// the gestures
//			if (droid.isTouched()) {
//				// the droid was picked up and is being dragged
//				droid.setX((int)event.getX());
//				droid.setY((int)event.getY());
//			}
//		} if (event.getAction() == MotionEvent.ACTION_UP) {
//			// touch was released
//			if (droid.isTouched()) {
//				droid.setTouched(false);
//			}
//		}
		return true;
	}

	public void render(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		
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
		
		for(Soldier s : soldiers){
			s.update(0.01);
		}
		
	}

}
