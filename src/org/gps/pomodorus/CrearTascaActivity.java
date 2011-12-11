package org.gps.pomodorus;

import org.gps.databases.CatTaskDbAdapter;
import org.gps.databases.CategoryDbAdapter;
import org.gps.databases.TaskDbAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
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
    
    private String[] options;
    private long[] id_options;
    private boolean[] selections;
    private CategoryDbAdapter database2;
    private CatTaskDbAdapter database3;
    
    private class DialogSelectionClickHandler implements OnMultiChoiceClickListener {
		public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
		}
    }
    
    private class DialogButtonClickHandler implements android.content.DialogInterface.OnClickListener {
		public void onClick(DialogInterface arg0, int arg1) {
		}
    }
    
	@Override
	protected Dialog onCreateDialog( int id ) 
	{
		return 
		new AlertDialog.Builder( this )
        	.setTitle( "Categories" )
        	.setMultiChoiceItems( options, selections, new DialogSelectionClickHandler() )
        	.setPositiveButton( "OK", new DialogButtonClickHandler() )
        	.create();
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creartasca);
        database = new TaskDbAdapter(this);  
		database2 = new CategoryDbAdapter(this);
		database3 = new CatTaskDbAdapter(this);
		database2.open();
		fillCategory();
		database2.close();
    }
	
	private void fillCategory() {
		Cursor cat = database2.fetchAllCategories();
		options = new String[cat.getCount()];
		id_options = new long[options.length];
		selections = new boolean[options.length];
		cat.moveToFirst();
		for (int i = 0; !cat.isAfterLast(); ++i) {
			id_options[i] = cat.getLong(0);
			options[i] = cat.getString(1);
			cat.moveToNext();
		}
	}
	
	public void onCategoryClick(View button) {
		showDialog(1);
	}

	public void crearTasca(View currentView){
        database.open();
		String nomTasca = ((EditText) findViewById(formFields[NOM])).getText().toString();
		String descripcioTasca = ((EditText) findViewById(formFields[DESCRIPCIO])).getText().toString();
		int pomodoros = Integer.valueOf(((EditText) findViewById(formFields[NUMERO])).getText().toString());
		long id_act = database.createTask(nomTasca, descripcioTasca, pomodoros);
		database.close();
		database3.open();
		for(int i = 0; i < selections.length; ++i) if (selections[i]) database3.createCatTask(id_options[i], id_act);
		database3.close();
		clearAllFields();
	}

	private void clearAllFields() {
		finish();
	}
}