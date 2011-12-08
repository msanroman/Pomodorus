package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UpdateTask extends Activity {
	
	private long id;
	private TaskDbAdapter database;
	private int formFields[] = {R.id.NomActualitzarTasca,
    		R.id.DescripcioActualitzarTasca, R.id.NumeroActualitzarTasca};
    private int NOM = 0;
    private int DESCRIPCIO = 1;
    private int NUMERO = 2;

	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.updatetask);
	    
	    TaskDbAdapter BaseDades = new TaskDbAdapter(this);
	    BaseDades.open();
	    
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) id = extras.getLong("id");
	    
	    Cursor cursor = BaseDades.fetchTask(id);
	    
	    EditText NomTask = (EditText)findViewById(R.id.NomActualitzarTasca);
	    NomTask.setText(cursor.getString(1));
	    EditText NumTask = (EditText)findViewById(R.id.NumeroActualitzarTasca);
	    NumTask.setText(cursor.getString(3));
	    EditText DescTask = (EditText)findViewById(R.id.DescripcioActualitzarTasca);
	    DescTask.setText(cursor.getString(2));
	    
	    BaseDades.close();
    }
	
	public void actualitzarTasca(View currentView){

		database = new TaskDbAdapter(this);
		database.open();
		String nomTasca = ((EditText) findViewById(formFields[NOM])).getText().toString();
		String descripcioTasca = ((EditText) findViewById(formFields[DESCRIPCIO])).getText().toString();
		int pomodoros = Integer.valueOf(((EditText) findViewById(formFields[NUMERO])).getText().toString());
		System.out.print(id);
		database.updateTask(id, nomTasca, descripcioTasca, pomodoros);
		database.close();
		clearAllFields();
	}
	
private void clearAllFields() {		
		finish();
	}
	
}
