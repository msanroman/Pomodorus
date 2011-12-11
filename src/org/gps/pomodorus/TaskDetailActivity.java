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
	private Cursor CatTask;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail);  
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
        
        dbHelper = new TaskDbAdapter(this);
        dbHelper.open();
        task = dbHelper.fetchTask(id);
        dbHelper.close();
        
        dbCatTask = new CatTaskDbAdapter(this);
        dbCatTask.open();
        CatTask = dbCatTask.fetchTask(id);
        long[] category = new long[CatTask.getCount()];
        CatTask.moveToFirst();
        for (int i = 0; !CatTask.isAfterLast(); ++i) {
        	category[i] = CatTask.getLong(1);
        	CatTask.moveToNext();
        }
        dbCatTask.close();
  
	    String Categories_aux = "";;
        dbCat = new CategoryDbAdapter(this);
        dbCat.open();
        for (int i = 0; i < category.length; ++i) {
        	Categories_aux += dbCat.fetchCategory(category[i]).getString(1);
        	if (i != category.length - 1) Categories_aux += ", ";
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
    }
}
