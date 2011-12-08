package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends Activity {
	private TaskDbAdapter dbHelper;
	private long id;
	private Cursor task;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail);  
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
        
        dbHelper = new TaskDbAdapter(this);
        dbHelper.open();
        task = dbHelper.fetchTask(id);
        dbHelper.close();
        
	    TextView NomTask = (TextView)findViewById(R.id.NomTasca);
	    NomTask.setText(task.getString(1));
	    TextView NumTask = (TextView)findViewById(R.id.NumeroTasca);
	    NumTask.setText(task.getString(3));
	    TextView NumRestTask = (TextView)findViewById(R.id.NumeroRestantsTasca);
	    NumRestTask.setText(task.getString(4));
	    TextView DescTask = (TextView)findViewById(R.id.DescripcioTasca);
	    DescTask.setText(task.getString(2));
    }
}
