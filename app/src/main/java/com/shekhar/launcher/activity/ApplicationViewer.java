package com.shekhar.launcher.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.shekhar.launcher.R;
import com.shekhar.launcher.adapter.ApplicationAdapter;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;

import java.util.List;

/**
 * Created by Mradul on 6/27/2015.
 */
public class ApplicationViewer extends Activity {

  public static List<ApplicationDetailsModel> applicationDetailsModels;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.app_viewer);

    applicationDetailsModels = ApplicationDataBase.getSingletonInstance(this).getAllApplications();

    Log.e("size", String.valueOf(applicationDetailsModels.size()));
    if (getActionBar() != null)
      getActionBar().hide();

    String intentData = getIntent().getStringExtra("ID");
    if (intentData != null) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setMessage("Select any one application for replacement");
      dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
      });
      dialog.show();
    }
    GridView gridView = (GridView) findViewById(android.R.id.list);
    gridView.setAdapter(new ApplicationAdapter(applicationDetailsModels, intentData, this));
  }
}
