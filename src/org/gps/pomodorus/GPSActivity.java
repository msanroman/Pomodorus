package org.gps.pomodorus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GPSActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	public void onCrearTascaClick(View button) {
			Intent newIntent = new Intent(this, CrearTascaActivity.class);
			startActivity(newIntent);
	}
	
	public void onLogInTwitterClick(View botton) {
		Intent newIntent = new Intent(this, LogInTwitterActivity.class);
		startActivity(newIntent);		
	}
}