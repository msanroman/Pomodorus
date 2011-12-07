package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TasksOverviewActivity extends ListActivity implements OnItemClickListener{

	private TaskDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		this.getListView().setDividerHeight(2);
		dbHelper = new TaskDbAdapter(this);
		dbHelper.open();
		fillData();
		dbHelper.close();
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
				dbHelper.open();
				String name = dbHelper.fetchTask(id).getString(1);
				dialog.setTitle(name);
				dbHelper.close();
				dialog.setMessage("Item selected is: " + name);
				AlertDialog ad = dialog.create();
				ad.show();
		    }
		});
		registerForContextMenu(getListView());
	}
	
	// Create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}
	
	private void fillData() {

		cursor = dbHelper.fetchAllTasks();
		startManagingCursor(cursor);
		String[] from = new String[] { TaskDbAdapter.KEY_NAME, TaskDbAdapter.KEY_REMAINING_POMODOROS, TaskDbAdapter.KEY_TOTAL_POMODOROS };
		int[] to = new int[] { R.id.taskName, R.id.taskRemainingPomodoros, R.id.taskTotalPomodoros };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter tasks = new SimpleCursorAdapter(this,
				R.layout.task_row, cursor, from, to);
		setListAdapter(tasks);
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Dialog dialog = new Dialog(this);
		dialog.setTitle(dbHelper.fetchTask(id).getString(1));
		dialog.setContentView(R.layout.help);
	}

}
