package com.shekhar.launcher.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.GridView;

import com.shekhar.launcher.R;
import com.shekhar.launcher.adapter.ApplicationsGridAdapter;

/**
 * Created by ShekharKG on 6/28/2015.
 */
public class ApplicationGridActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.app_viewer);

    Log.e("size", String.valueOf(MainActivity.applicationDetailsModels.size()));
    if (getActionBar() != null)
      getActionBar().hide();

    GridView gridView = (GridView) findViewById(android.R.id.list);
    gridView.setAdapter(new ApplicationsGridAdapter(this));
  }

}
