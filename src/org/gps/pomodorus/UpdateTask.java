package org.gps.pomodorus;

import java.util.Arrays;

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

public class UpdateTask extends Activity {
	
	private long id;
	private TaskDbAdapter database;
	private CatTaskDbAdapter CatTaskDbHelper;
	private CategoryDbAdapter CategoryDbHelper;
	private int formFields[] = {R.id.NomActualitzarTasca,
    		R.id.DescripcioActualitzarTasca, R.id.NumeroActualitzarTasca};
    private int NOM = 0;
    private int DESCRIPCIO = 1;
    private int NUMERO = 2;
    
    String[] opcions;
    long[] rowidCat;
    boolean[] marcades;
    boolean[] noves;
    
    private class DialogSelectionClickHandler implements OnMultiChoiceClickListener {
		public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
		}
    }
    
    private class DialogButtonClickHandler implements android.content.DialogInterface.OnClickListener {
		public void onClick(DialogInterface arg0, int arg1) {
		}
    }
    
    protected Dialog onCreateDialog( int id ) 
	{
		return 
		new AlertDialog.Builder( this )
        	.setTitle( "Categories" )
        	.setMultiChoiceItems( opcions, noves, new DialogSelectionClickHandler() )
        	.setPositiveButton( "OK", new DialogButtonClickHandler() )
        	.create();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.updatetask);
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) id = extras.getLong("id");
	    database = new TaskDbAdapter(this);
	    CatTaskDbHelper = new CatTaskDbAdapter(this);
	    CategoryDbHelper = new CategoryDbAdapter(this);
	    
		database.open();
		CatTaskDbHelper.open();
		CategoryDbHelper.open();
	    fillData();
	    database.close();
	    CatTaskDbHelper.close();
	    CategoryDbHelper.close();
    }
	
	public void onCategoryModifClick(View button) {
		showDialog(1);
	}
	
	public void actualitzarTasca(View currentView){
		String nomTasca = ((EditText) findViewById(formFields[NOM])).getText().toString();
		String descripcioTasca = ((EditText) findViewById(formFields[DESCRIPCIO])).getText().toString();
		int pomodoros = Integer.valueOf(((EditText) findViewById(formFields[NUMERO])).getText().toString());
		database.open();
		database.updateTask(id, nomTasca, descripcioTasca, pomodoros);
		database.close();
		CatTaskDbHelper.open();
		for(int i = 0; i < noves.length; ++i) {
			if (marcades[i] && !noves[i]) CatTaskDbHelper.deleteCatTask(rowidCat[i], id);
			else if (!marcades[i] && noves[i]) CatTaskDbHelper.createCatTask(rowidCat[i], id);
		}
		CatTaskDbHelper.close();
		finish();
	}
	
	private void fillData() {
	    Cursor cursor = database.fetchTask(id);
	    long[] CategoriesAct = CatTaskDbHelper.fetchCat(id);
	    Cursor cat = CategoryDbHelper.fetchAllCategories();
		opcions = new String[cat.getCount()];
		rowidCat = new long[opcions.length];
		marcades = new boolean[opcions.length];
		noves = new boolean[opcions.length];
		cat.moveToFirst();
		for (int i = 0; !cat.isAfterLast(); ++i) {
			rowidCat[i] = cat.getLong(0);
			opcions[i] = cat.getString(1);
			if (Arrays.binarySearch(CategoriesAct, rowidCat[i]) >= 0) {
				marcades[i] = true;
				noves[i] = true;
			}
			cat.moveToNext();
		}
	    EditText NomTask = (EditText)findViewById(R.id.NomActualitzarTasca);
	    NomTask.setText(cursor.getString(1));
	    EditText NumTask = (EditText)findViewById(R.id.NumeroActualitzarTasca);
	    NumTask.setText(cursor.getString(3));
	    EditText DescTask = (EditText)findViewById(R.id.DescripcioActualitzarTasca);
	    DescTask.setText(cursor.getString(2));   
	}
}
