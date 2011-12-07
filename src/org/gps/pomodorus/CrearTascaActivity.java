package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CrearTascaActivity extends Activity {

    private TaskDbAdapter database;
    private int formFields[] = {R.id.NomCrearTasca,
    		R.id.DescripcioCrearTasca, R.id.NumeroCrearTasca};
    private int NOM = 0;
    private int DESCRIPCIO = 1;
    private int NUMERO = 2;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {	
 
		super.onCreate(savedInstanceState);
        database = new TaskDbAdapter(this);
        database.open();
		setContentView(R.layout.creartasca);
    }
	
	public void crearTasca(View currentView){

		String nomTasca = ((EditText) findViewById(formFields[NOM])).getText().toString();
		String descripcioTasca = ((EditText) findViewById(formFields[DESCRIPCIO])).getText().toString();
		int pomodoros = Integer.valueOf(((EditText) findViewById(formFields[NUMERO])).getText().toString());
		database.createTask(nomTasca, descripcioTasca, pomodoros);
		clearAllFields();
	}

	private void clearAllFields() {
		
//		for(int field: formFields) {
//			EditText f = (EditText) findViewById(field);
//			f.setText("");
//		}
//		
		finish();
	}
	
	
	
	
}
