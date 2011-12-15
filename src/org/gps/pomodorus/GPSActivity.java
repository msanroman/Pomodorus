package org.gps.pomodorus;

import java.lang.reflect.Array;

import org.gps.databases.CatTaskDbAdapter;
import org.gps.databases.CategoryDbAdapter;
import org.gps.databases.TaskDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class GPSActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onCrearTascaClick(View button) {

        Intent newIntent = new Intent(this, CrearTascaActivity.class);
        startActivity(newIntent);
    }
    
    public void onCrearCategoriaClick(View button) {

        Intent newIntent = new Intent(this, CrearCategoriaActivity.class);
        startActivity(newIntent);
    }
    
    public void onLlistarTasquesClick(View button) {

        Intent newIntent = new Intent(this, TasksOverviewActivity.class);
        startActivity(newIntent);
    }
    
    public void onLlistarCategoriesClick(View button) {

        Intent newIntent = new Intent(this, CategoriesOverviewActivity.class);
        startActivity(newIntent);
    }

    public void onLogInTwitterClick(View botton) {

        Intent newIntent = new Intent(this, LogInTwitterActivity.class);
        startActivity(newIntent);
    }
    
    public void onFillDatabaseTest(View button) {

    	TaskDbAdapter TaskAdapter = new TaskDbAdapter(this);
        CategoryDbAdapter CatAdapter = new CategoryDbAdapter(this);
        CatTaskDbAdapter CatTaskAdapter = new CatTaskDbAdapter(this);
        TaskAdapter.open();
        
        long ids[] = new long[5];

		ids[0] = TaskAdapter.createTask("Tasca1", "Tasca1Descripcio", 3);
		ids[1] = TaskAdapter.createTask("Tasca2", "Tasca2Descripcio", 4);
		ids[2] = TaskAdapter.createTask("Tasca3", "Tasca3Descripcio", 5);
		ids[3] = TaskAdapter.createTask("Tasca4", "Tasca4Descripcio", 6);
		ids[4] = TaskAdapter.createTask("Tasca5", "Tasca5Descripcio", 7);
		
		TaskAdapter.close();
		
		CatAdapter.open();
        
        long idsCat[] = new long[4];

        idsCat[0] = CatAdapter.createCategory("Cat1");
        idsCat[1] = CatAdapter.createCategory("Cat2");
        idsCat[2] = CatAdapter.createCategory("Cat3");
        idsCat[3] = CatAdapter.createCategory("Cat4");
		
        CatAdapter.close();
		
        CatTaskAdapter.open();

        CatTaskAdapter.createCatTask(idsCat[0], ids[1]);
        CatTaskAdapter.createCatTask(idsCat[1], ids[1]);
        CatTaskAdapter.createCatTask(idsCat[2], ids[1]);
        CatTaskAdapter.createCatTask(idsCat[3], ids[1]);
        CatTaskAdapter.createCatTask(idsCat[1], ids[2]);
        CatTaskAdapter.createCatTask(idsCat[3], ids[2]);
        CatTaskAdapter.createCatTask(idsCat[0], ids[3]);
        CatTaskAdapter.createCatTask(idsCat[2], ids[3]);
        CatTaskAdapter.createCatTask(idsCat[2], ids[4]);
		
        CatTaskAdapter.close();
    }
}