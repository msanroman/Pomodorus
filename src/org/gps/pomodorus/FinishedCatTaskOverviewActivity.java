package org.gps.pomodorus;

import org.gps.databases.CatTaskDbAdapter;
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

public class FinishedCatTaskOverviewActivity extends ListActivity implements OnItemClickListener{

	private TaskDbAdapter dbHelper;
	private CatTaskDbAdapter dbCatTaskHelper;
	private static final CharSequence[] items = { "Veure detall", "Esborrar" };
	private Cursor cursor;
	protected Cursor task;
	
	private long id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
		
		this.getListView().setDividerHeight(2);
		dbHelper = new TaskDbAdapter(this);
		dbCatTaskHelper = new CatTaskDbAdapter(this);
		dbCatTaskHelper.open();
		dbHelper.open();
		fillData();
		dbHelper.close();
		dbCatTaskHelper.close();
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
				dbHelper.open();
				task = dbHelper.fetchTask(id);
				dbHelper.close();

				dialogBuilder.setTitle("Selecciona una de les següents opcions");
				dialogBuilder.setItems(items, new OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int item) {
                    	
                        Intent intent = null;
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", task.getLong(0));
                        bundle.putCharSequence("name", task.getString(1));
                        bundle.putCharSequence("description", task.getString(2));
                        bundle.putInt("totalPomodoros", task.getInt(3));
                        bundle.putInt("remainingPomodoros", task.getInt(4));
                        switch(item){
                            case 0:
                                intent = new Intent(getBaseContext(), TaskDetailActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case 1:
                            	AlertDialog.Builder builderBorrar = new AlertDialog.Builder(FinishedCatTaskOverviewActivity.this);
                            	builderBorrar.setIcon(R.drawable.alert_dialog_icon)
                            	.setTitle("Segur que desitges eliminar la tasca?")   
                            	.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            		
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			dbHelper.open();
                            			dbHelper.deleteTask(id);
                        				dbCatTaskHelper.open();
                        				fillData();
                        				dbHelper.close();
                        				dbCatTaskHelper.deleteTask(id);
                        				dbCatTaskHelper.close();
                            		}
                            	})
                            	.setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			/* nada */
                            		}
                            	});
                            	AlertDialog BorrarDialog = builderBorrar.create();
                            	BorrarDialog.show();
                            	break;
                        }
                        
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
		long[] rowid_tasques = dbCatTaskHelper.fetchTask(id);
		cursor = dbHelper.fetchTasksFinished(rowid_tasques);
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
	
	public void onResume() {	
		super.onResume();
		dbHelper.open();
		dbCatTaskHelper.open();
		fillData();
		dbHelper.close();
		dbCatTaskHelper.close();
	}
}
