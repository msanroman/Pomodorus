package org.gps.pomodorus;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TasksTabActivity extends TabActivity {
    static private TabHost mTabHost;
    private Resources mResources;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskstabs);
        mTabHost = getTabHost();       
        mResources = getResources();
        addTabTotes();
        addTabPendents();
        addTabCompletades();
        mTabHost.setCurrentTab(0);  
    }
    
    private void addTabTotes() {
        Intent intent = new Intent(this, TasksOverviewActivity.class);
        TabSpec spec = mTabHost.newTabSpec("Totes");
        spec.setIndicator("Totes", mResources
                          .getDrawable(android.R.drawable.ic_menu_agenda));
        spec.setContent(intent);
        mTabHost.addTab(spec);
     }
    
    private void addTabPendents() {
        Intent intent = new Intent(this, NotFinishedTasksOverviewActivity.class);
        TabSpec spec = mTabHost.newTabSpec("Pendents");
        spec.setIndicator("Pendents", mResources
                          .getDrawable(android.R.drawable.ic_menu_agenda));
        spec.setContent(intent);
        mTabHost.addTab(spec);
     } 
    
    private void addTabCompletades() {
         Intent intent = new Intent(this, FinishedTasksOverviewActivity.class);
         TabSpec spec = mTabHost.newTabSpec("Completades");
         spec.setIndicator("Completades", mResources
                           .getDrawable(android.R.drawable.ic_menu_agenda));
         spec.setContent(intent);
         mTabHost.addTab(spec);
     }
}