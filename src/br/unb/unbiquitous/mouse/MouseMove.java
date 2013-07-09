//package br.unb.unbiquitous.mouse;
//
//import android.graphics.Point;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.View.OnTouchListener;
//import br.unb.unbiquitous.R;
//import br.unb.unbiquitous.ubiquitos.DroidMouseActivity;
//
//public class MouseMove {
//
//	private Point mousePosition;
//	private Point scrollPosition;
//	private int eventAction;
//	private boolean eventWasAMouseMove;
//	final private MouseDriver mouseDriver;
//
//	public MouseMove(DroidMouseActivity activity, MouseDriver mouseDriver) {
//		this.mousePosition = new Point();
//		this.scrollPosition = new Point();
//		this.mouseDriver = mouseDriver;
//		addLeftButtonClick(activity);
//		addRightButtonClick(activity);
//		addMoveArea(activity);
//		addScroll(activity);
//	}
//
//	private void addScroll(final DroidMouseActivity activity) {
//		View scroll = (MouseScroll) activity.findViewById(R.id.scroll);
//
//		scroll.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				eventAction = event.getAction();
//
//				switch (eventAction) {
//
//				case MotionEvent.ACTION_DOWN:
//					scrollPosition.y = (int) event.getY();
//					break;
//
//				case MotionEvent.ACTION_MOVE:
//					int axisY = (int) event.getY() - scrollPosition.y;
//					scrollPosition.y = (int) event.getY();
//					Log.d("DROID MOUSE", "Scrollou: " + axisY);
//					mouseDriver.scroll(axisY);
//					break;
//				}
//				return false;
//			}
//		});
//
//	}
//
//	private void addMoveArea(final DroidMouseActivity activity) {
//		final View touchPad = (MousePointer) activity
//				.findViewById(R.id.mouse_pointer);
//
//		touchPad.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				Log.d("DROID MOUSE", "DEDos: " + Integer.toString(event.getPointerCount()));
//				eventAction = event.getAction();
//				switch (eventAction) {
//				case MotionEvent.ACTION_DOWN:
//					eventWasAMouseMove = false;
//					touchPad.setSoundEffectsEnabled(true);
//					mousePosition.x = (int) event.getX();
//					mousePosition.y = (int) event.getY();
//
//					Log.d("DROID MOUSE", "Action Down: " + event.getAction());
//					Log.d("DROID MOUSE", "Começou a mover: " + mousePosition.x
//							+ ", " + mousePosition.y);
//					break;
//				case MotionEvent.ACTION_MOVE:
//					eventWasAMouseMove = true;
//					touchPad.setSoundEffectsEnabled(false);
//					int axisX = (int) event.getX() - mousePosition.x;
//					int axisY = (int) event.getY() - mousePosition.y;
//
//					mouseDriver.move(axisX, axisY);
//
//					mousePosition.x = (int) event.getX();
//					mousePosition.y = (int) event.getY();
//
//					Log.d("DROID MOUSE", "Action move: " + event.getAction());
//					Log.d("DROID MOUSE", "Mouse moveu para: " + axisX + ", "
//							+ axisY);
//					break;
//				}
//
//				return false;
//			}
//		});
//	}
//
//	private void addLeftButtonClick(final DroidMouseActivity activity) {
//		View touchPad = (MousePointer) activity
//				.findViewById(R.id.mouse_pointer);
//
//		touchPad.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!eventWasAMouseMove) {
//					Log.d("DROID MOUSE", "CLICOU");
//					mouseDriver.leftButtonClick();
//				}
//			}
//		});
//	}
//
//	private void addRightButtonClick(final DroidMouseActivity activity) {
//		View touchPad = (MousePointer) activity
//				.findViewById(R.id.mouse_pointer);
//
//		touchPad.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				if (eventAction != MotionEvent.ACTION_MOVE) {
//					Log.d("DROID MOUSE", "CLICOU LONGO");
//					mouseDriver.rightButtonClick();
//					return true;
//				}
//				return false;
//			}
//		});
//	}
//}
