package org.gps.pomodorus;

import org.gps.databases.CategoryDbAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UpdateCategory extends Activity {
	
	private long id;
	private CategoryDbAdapter database;
	private int formFields[] = {R.id.NomActualitzarCategoria};
    private int NOM = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.updatecategory);
	    
	    CategoryDbAdapter BaseDades = new CategoryDbAdapter(this);
	    BaseDades.open();
	    
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) id = extras.getLong("id");
	    
	    Cursor cursor = BaseDades.fetchCategory(id);
	    
	    EditText NomCategory = (EditText)findViewById(R.id.NomActualitzarCategoria);
	    NomCategory.setText(cursor.getString(1));
	    
	    BaseDades.close();
    }
	
	public void actualitzarCategoria(View currentView){
		database = new CategoryDbAdapter(this);
		database.open();
		String nomCategoria = ((EditText) findViewById(formFields[NOM])).getText().toString();
		database.updateCategory(id, nomCategoria);
		database.close();
		clearAllFields();
	}
	
private void clearAllFields() {		
		finish();
	}
	
}
