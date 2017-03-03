package com.ecash.app.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ecash.R;
import com.ecash.app.Frags.Frag_Filter;
import com.ecash.app.Frags.Frag_Home;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    // Widgets
    private BottomBar bottomBar;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_view_by_id();
        init();
        //replaceFrags(new Frag_Filter(), "Frag_Filter");
        replaceFrags(new Frag_Home(), "Frag_Home");

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_contacts) {
                    //Toast.makeText(MainActivity.this, "Contacts is selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void find_view_by_id() {
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
    }

    private void init() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.logo);

        manager = getFragmentManager();
        manager.addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_apply).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            replaceFrags(new Frag_Filter(), "Frag_Filter");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFrags(Fragment frag, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, frag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void onBackStackChanged() {
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = backStackCount - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(i);
            Log.e("BackStack Ele: ", entry.getName());
        }
        Log.e("BackStack Ele: ", "------------------");
        if (backStackCount == 0) {
            finish();
        }
    }
}