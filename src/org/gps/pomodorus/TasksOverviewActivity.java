package org.gps.pomodorus;

import org.gps.databases.TaskDbAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
	private static final CharSequence[] items = { "Veure detall", "Modificar", "Esborrar", "Començar Pomodoro" };
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
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
				dbHelper.open();
				String name = dbHelper.fetchTask(id).getString(1);
				dbHelper.close();
				dialogBuilder.setTitle("Selecciona una de les següents opcions");
				dialogBuilder.setSingleChoiceItems(items, 0, new OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int item) {
                        
                        Intent intent;
                        switch(item){
//                            case 0:
//                                intent = new Intent(getBaseContext(), TaskDetail.class);
//                                break;
                            default:
                                intent = new Intent(getBaseContext(), UpdateTask.class);
                                intent.putExtra("id", id);
                                break;
//                            case 2:
//                                intent = new Intent(getBaseContext(), DeleteTask.class);
//                                break;
//                            case 3:
//                                intent = new Intent(getBaseContext(), StartPomodoro.class);
//                                break;
                        }
                        startActivity(intent);
                    }
                });
				AlertDialog ad = dialogBuilder.create();
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
