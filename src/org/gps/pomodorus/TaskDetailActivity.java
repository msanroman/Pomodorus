package org.gps.pomodorus;

import org.gps.databases.CatTaskDbAdapter;
import org.gps.databases.CategoryDbAdapter;
import org.gps.databases.TaskDbAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends Activity {
	private TaskDbAdapter dbHelper;
	private CatTaskDbAdapter dbCatTask;
	private CategoryDbAdapter dbCat;
	private long id;
	private Cursor task;
	private Cursor cat;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail);  
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
        fillData();
    }

	private void fillData() {
        dbHelper = new TaskDbAdapter(this);
        dbCat = new CategoryDbAdapter(this);
        dbCatTask = new CatTaskDbAdapter(this);
        dbHelper.open();
        task = dbHelper.fetchTask(id);
        dbHelper.close();
        dbCatTask.open();
        long[] rowid_cat = dbCatTask.fetchCat(id);
        dbCatTask.close();
        dbCat.open();
        cat = dbCat.fetchCategories(rowid_cat);
        String Categories_aux = "";
        for (cat.moveToFirst(); !cat.isAfterLast(); cat.moveToNext()) {
        	Categories_aux += cat.getString(1);
        	if (!cat.isLast()) Categories_aux += ", ";
        }
        dbCat.close();
        
	    TextView NomTask = (TextView)findViewById(R.id.NomTasca);
	    NomTask.setText(task.getString(1));
	    TextView NumTask = (TextView)findViewById(R.id.NumeroTasca);
	    NumTask.setText(task.getString(3));
	    TextView NumRestTask = (TextView)findViewById(R.id.NumeroRestantsTasca);
	    NumRestTask.setText(task.getString(4));
	    TextView DescTask = (TextView)findViewById(R.id.DescripcioTasca);
	    DescTask.setText(task.getString(2));
	    TextView Categories = (TextView)findViewById(R.id.Categories);
	    Categories.setText(Categories_aux);
	    TextView Finalitzada = (TextView)findViewById(R.id.Finalitzada);
	    if (task.getInt(5) == 1) Finalitzada.setText("La tasca està finalitzada");
	    else Finalitzada.setText("La Tasca no està finalitzada");
	}
}
