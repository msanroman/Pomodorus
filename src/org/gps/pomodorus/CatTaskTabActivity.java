package org.gps.pomodorus;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class CatTaskTabActivity extends TabActivity implements OnTabChangeListener {
    static private TabHost mTabHost;
    private Resources mResources;
    
    private long id;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskstabs);
        Bundle extras = getIntent().getExtras();
        if (extras != null) id = extras.getLong("id");
        mTabHost = getTabHost();       
        mResources = getResources();
        addTabTotes();
        addTabPendents();
        addTabCompletades();
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(0);
        mTabHost.setBackgroundColor(Color.parseColor("#493733"));
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(
                    Color.parseColor("#493733"));
        }
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff5224"));
    }
    
    private void addTabTotes() {
        Intent intent = new Intent(this, CatTaskOverviewActivity.class);
        intent.putExtra("id", id);
        TabSpec spec = mTabHost.newTabSpec("Totes");
        spec.setIndicator("Totes", mResources
                          .getDrawable(android.R.drawable.ic_menu_agenda));
        spec.setContent(intent);
        mTabHost.addTab(spec);
     }
    
    private void addTabPendents() {
        Intent intent = new Intent(this, NotFinishedCatTaskOverviewActivity.class);
        intent.putExtra("id", id);
        TabSpec spec = mTabHost.newTabSpec("Pendents");
        spec.setIndicator("Pendents", mResources
                          .getDrawable(android.R.drawable.ic_menu_agenda));
        spec.setContent(intent);
        mTabHost.addTab(spec);
     } 
    
    private void addTabCompletades() {
         Intent intent = new Intent(this, FinishedCatTaskOverviewActivity.class);
         intent.putExtra("id", id);
         TabSpec spec = mTabHost.newTabSpec("Completades");
         spec.setIndicator("Completades", mResources
                           .getDrawable(android.R.drawable.ic_menu_agenda));
         spec.setContent(intent);
         mTabHost.addTab(spec);
     }
    
    public void onTabChanged(String tabId) {
        
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(
                    Color.parseColor("#493733"));
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                .setBackgroundColor(Color.parseColor("#ff5224"));
     
    }
}