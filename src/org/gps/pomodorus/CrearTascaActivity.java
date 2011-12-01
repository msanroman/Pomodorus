package org.gps.pomodorus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CrearTascaActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.creartasca);
        View crearTascaMain = findViewById(R.id.CrearTascaButton);
        crearTascaMain.setOnClickListener(this);
    }
	
	public void onClick(View currentView) {

	}

}
