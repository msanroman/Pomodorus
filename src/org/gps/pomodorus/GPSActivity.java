package org.gps.pomodorus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GPSActivity extends Activity implements OnClickListener{

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View crearTascaMain = findViewById(R.id.CrearTascaMain);
        crearTascaMain.setOnClickListener(this);
    }

    
	public void onClick(View currentView) {
		
		if(currentView.getId() == R.id.CrearTascaMain){
			Intent newGameIntent = new Intent(this, CrearTascaActivity.class);
			startActivity(newGameIntent);
		}
	}
    
    
}