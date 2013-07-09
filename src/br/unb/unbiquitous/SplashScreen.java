package br.unb.unbiquitous;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import br.unb.unbiquitous.ubiquitos.DroidMouseActivity;

public class SplashScreen extends Activity implements Runnable {

	private final int DELAY = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_screen);
		Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
		
		Handler handle = new Handler();
		handle.postDelayed(this, DELAY);
	}
	
	public void run() {
		startActivity(new Intent(this, DroidMouseActivity.class));
		finish();
	}
}
