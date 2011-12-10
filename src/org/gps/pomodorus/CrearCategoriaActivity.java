package org.gps.pomodorus;

import org.gps.databases.CategoryDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CrearCategoriaActivity extends Activity {

    private CategoryDbAdapter database;
    private int formFields[] = {R.id.NomCrearCategoria};
    private int NOM = 0;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {	
 
		super.onCreate(savedInstanceState);
        database = new CategoryDbAdapter(this);
        database.open();
		setContentView(R.layout.crearcategoria);
    }
	
	public void crearCategoria(View currentView){

		String nomCategoria = ((EditText) findViewById(formFields[NOM])).getText().toString();
		database.createCategory(nomCategoria);
		clearAllFields();
	}

	private void clearAllFields() {
		finish();
	}
	
	
	
	
}
