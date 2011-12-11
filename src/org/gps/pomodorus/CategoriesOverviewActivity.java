package org.gps.pomodorus;

import org.gps.databases.CatTaskDbAdapter;
import org.gps.databases.CategoryDbAdapter;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

public class CategoriesOverviewActivity extends ListActivity implements OnItemClickListener{

	private CategoryDbAdapter dbHelper;
	private CatTaskDbAdapter dbCatTaskHelper;
	private static final CharSequence[] items = { "Editar categoria", "Borrar categoria", "Llistar tasques assignades a la categoria" };
	private Cursor cursor;
	protected Cursor category;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);
		this.getListView().setDividerHeight(2);
		dbHelper = new CategoryDbAdapter(this);
		dbCatTaskHelper = new CatTaskDbAdapter(this);
		dbHelper.open();
		fillData();
		dbHelper.close();
		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
				dbHelper.open();
				category = dbHelper.fetchCategory(id);
				dbHelper.close();

				dialogBuilder.setTitle("Selecciona una de les següents opcions");
				dialogBuilder.setItems(items, new OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int item) {
                    	
                        Intent intent = null;
                        Bundle bundle = new Bundle();
                        bundle.putLong("id", category.getLong(0));
                        bundle.putCharSequence("name", category.getString(1));
                        switch(item){
                            case 0:
                              intent = new Intent(getBaseContext(), UpdateCategory.class);
                              intent.putExtra("id", id);
                              startActivity(intent);
                              finish();
                              break;
                            case 1:
                            	AlertDialog.Builder builderBorrar = new AlertDialog.Builder(CategoriesOverviewActivity.this);
                            	builderBorrar.setIcon(R.drawable.alert_dialog_icon)
                            	.setTitle("Segur que desitges eliminar la categoria?")   
                            	.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            		
                            		public void onClick(DialogInterface dialog, int whichButton) {
                            			dbHelper.open();
                            			dbHelper.deleteCategory(id);
                        				dbHelper.close();
                        				dbCatTaskHelper.open();
                        				dbCatTaskHelper.deleteCat(id);
                        				dbCatTaskHelper.close();
                            			finish();
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
                            case 2:
//                                intent = new Intent(getBaseContext(), TaskDetailActivity.class);
//                                intent.putExtra("id", id);
//                                startActivity(intent);
//                                break;
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

		cursor = dbHelper.fetchAllCategories();
		startManagingCursor(cursor);
		String[] from = new String[] { CategoryDbAdapter.KEY_NAME };
		int[] to = new int[] { R.id.categoryName };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter categories = new SimpleCursorAdapter(this,
				R.layout.category_row, cursor, from, to);
		
		setListAdapter(categories);
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Dialog dialog = new Dialog(this);
		dialog.setTitle(dbHelper.fetchCategory(id).getString(1));
		dialog.setContentView(R.layout.help);
	}
}
