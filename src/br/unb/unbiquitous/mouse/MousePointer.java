package br.unb.unbiquitous.mouse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import br.unb.unbiquitous.R;

public class MousePointer extends View {

	public static Drawable mousePointerImage;
	private int x, y;
	private int imageWidth;
	private int imageHeight;

	public MousePointer(Context context) {
		this(context, null);
	}

	public MousePointer(Context context, AttributeSet attrs) {
		super(context, attrs);

		mousePointerImage = context.getResources().getDrawable(
				R.drawable.circulo_verde);
		imageWidth = mousePointerImage.getIntrinsicWidth();
		imageHeight = mousePointerImage.getIntrinsicHeight();

		setFocusable(true);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);

		x = width / 2 - (imageWidth / 2);
		y = height / 2 - (imageHeight / 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mousePointerImage.setBounds(x, y, x + imageWidth, y + imageHeight);
		mousePointerImage.draw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.x = (int) x - (imageWidth / 2);
			this.y = (int) y - (imageHeight / 2);
			mousePointerImage.setAlpha(100);
			break;

		case MotionEvent.ACTION_MOVE:
			this.x = (int) x - (imageWidth / 2);
			this.y = (int) y - (imageHeight / 2);

			break;

		case MotionEvent.ACTION_UP:
			mousePointerImage.setAlpha(0);

			break;
		}

		invalidate();
		
		return super.onTouchEvent(event);
	}
}
