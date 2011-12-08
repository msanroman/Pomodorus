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
        
        TextView nom = (TextView)findViewById(R.id.textView1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
        
        dbHelper = new TaskDbAdapter(this);
        dbHelper.open();
        task = dbHelper.fetchTask(id);
        dbHelper.close();
        nom.setText(task.getString(1));
    }
}
