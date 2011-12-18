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

public class TasksOverviewActivity extends ListActivity implements OnItemClickListener{

	private TaskDbAdapter dbHelper;
	private CatTaskDbAdapter dbCatTaskHelper;
	private static final CharSequence[] items = { "Veure detall", "Modificar", "Esborrar", "Finalitzar Tasca", "Començar Pomodoro" };
	private Cursor cursor;
	protected Cursor task;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		this.getListView().setDividerHeight(2);
		dbHelper = new TaskDbAdapter(this);
		dbCatTaskHelper = new CatTaskDbAdapter(this);
		dbHelper.open();
		fillData();
		dbHelper.close();
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
                        task.close();
                        switch(item){
                            case 0:
                                intent = new Intent(getBaseContext(), TaskDetailActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(getBaseContext(), UpdateTask.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case 2:
                            	AlertDialog.Builder builderBorrar = new AlertDialog.Builder(TasksOverviewActivity.this);
                            	builderBorrar.setIcon(R.drawable.alert_dialog_icon)
                            	.setTitle("Segur que desitges eliminar la tasca?")   
                            	.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            		
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			dbHelper.open();
                            			dbHelper.deleteTask(id);
                        				fillData();
                        				dbHelper.close();
                        				dbCatTaskHelper.open();
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
                            case 3:
                            	AlertDialog.Builder builderFinalitzar = new AlertDialog.Builder(TasksOverviewActivity.this);
                            	builderFinalitzar.setIcon(R.drawable.alert_dialog_icon)
                            	.setTitle("Segur que desitges finalitzar la tasca?")   
                            	.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            		
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			dbHelper.open();
                            			dbHelper.finishTask(id);
                        				dbHelper.close();
                            		}
                            	})
                            	.setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			/* nada */
                            		}
                            	});
                            	AlertDialog FinalitzarDialog = builderFinalitzar.create();
                            	FinalitzarDialog.show();
                            	break;
                            case 4:
                            	intent = new Intent(getBaseContext(), PomodoroActivity.class);
                            	intent.putExtras(bundle);
                            	startActivity(intent);
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
	
	public void onResume() {
		
		super.onResume();
		dbHelper.open();
		fillData();
		dbHelper.close();
	}
}
