package com.shekhar.launcher.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.shekhar.launcher.R;
import com.shekhar.launcher.adapter.ApplicationsGridAdapter;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.shakelistener.ShakeListener;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ShekharKG on 6/28/2015.
 */
public class ApplicationGridActivity extends ActionBarActivity {

  private ApplicationsGridAdapter applicationsGridAdapter;
  private GridView gridView;
  private int sortPosition;
  private ShakeListener mShaker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.app_viewer);

    gridView = (GridView) findViewById(android.R.id.list);
    applicationsGridAdapter = new ApplicationsGridAdapter(this);
    gridView.setAdapter(applicationsGridAdapter);


    final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    mShaker = new ShakeListener(this);
    mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
      public void onShake() {
        vibe.vibrate(100);
        decideSort(sortPosition);
        applicationsGridAdapter.notifyDataSetChanged();
        gridView.setSelection(0);
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_filter) {
      showSortingDialog();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Show filter dialog
   */
  private void showSortingDialog() {

    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.filter_dialog);
    ListView filterListView = (ListView) dialog.findViewById(R.id.filterLV);
    dialog.setTitle("Select filter!!!");
    String[] filterArray = new String[]{"Alphabetic", "Recently used first", "Most used first", "Older first", "New installed first", "Least used first"};
    filterListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterArray));

    filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        decideSort(position);
        dialog.dismiss();
        applicationsGridAdapter.notifyDataSetChanged();
        gridView.setSelection(0);

      }
    });

    dialog.show();
  }

  private void decideSort(int position) {
    switch (position) {
      case 0:
        sortAlphabetic();
        break;
      case 1:
        sortCurrentlyUsed();
        break;
      case 2:
        sortMostUsed();
        break;
      case 3:
        sortInstalledOnAsc();
        break;
      case 4:
        sortInstalledOnDesc();
        break;
      case 5:
        sortLeastUsed();
        break;
    }
  }

  private void sortLeastUsed() {
    sortPosition = 4;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return (int) (lhs.getLastUsedOn() - rhs.getLastUsedOn());
      }
    });
  }

  private void sortInstalledOnDesc() {
    sortPosition = 3;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return (int) (lhs.getInstalledOn() - rhs.getInstalledOn());
      }
    });
  }

  private void sortInstalledOnAsc() {
    sortPosition = 2;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return (int) (rhs.getInstalledOn() - lhs.getInstalledOn());
      }
    });
  }

  private void sortMostUsed() {
    sortPosition = 1;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return rhs.getFrequencyCount() - lhs.getFrequencyCount();
      }
    });
  }

  private void sortCurrentlyUsed() {
    sortPosition = 0;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return (int) (rhs.getLastUsedOn() - lhs.getLastUsedOn());
      }
    });
  }
  private void sortAlphabetic(){
    sortPosition = 4;
    Collections.sort(MainActivity.applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return lhs.getAppName().compareToIgnoreCase(rhs.getAppName());
      }
    });
  }
}
