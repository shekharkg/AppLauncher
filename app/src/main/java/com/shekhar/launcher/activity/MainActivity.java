package com.shekhar.launcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.github.siyamed.shapeimageview.OctogonImageView;
import com.shekhar.launcher.R;
import com.shekhar.launcher.async.AddApplicationDetailsAsync;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;
import com.shekhar.launcher.intrface.CallBack;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends Activity implements CallBack {

  public static List<ApplicationDetailsModel> applicationDetailsModels;
  private final String TAG = "<<MainActivity>>";
  private ApplicationDataBase applicationDataBase;

  public OctogonImageView[] octogonImageViews;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    applicationDataBase = ApplicationDataBase.getSingletonInstance(this);
    applicationDetailsModels = applicationDataBase.getAllApplications();
    if (applicationDetailsModels.size() < 1)
      new AddApplicationDetailsAsync(this, this).execute();
    else
      loadAppsInHomeScreen(false);

  }

  private void loadApplicationImageView() {
    for (int i = 0; i < 8; i++) {
      final ApplicationDetailsModel model = applicationDetailsModels.get(i);
      octogonImageViews[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          run(model);
        }
      });

      try {
        octogonImageViews[i].setImageDrawable(getPackageManager().getApplicationIcon(model.getAppPackage()));
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public void run(ApplicationDetailsModel model) {
    try {
      int count = model.getFrequencyCount();
      long time = Calendar.getInstance().getTimeInMillis();
      applicationDataBase.updateAppDetails(model.getId(), time, ++count);
      model.setLastUsedOn(time);
      model.setFrequencyCount(count);
      Intent intent = this.getPackageManager().getLaunchIntentForPackage(model.getAppPackage());
      this.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (applicationDetailsModels.size() > 0)
      loadAppsInHomeScreen(true);
  }

  private void loadAppsInHomeScreen(boolean needToSort) {
    applicationDetailsModels = applicationDataBase.getAllApplications();

    if (needToSort)
      sortMostUsed();

    if (octogonImageViews == null) {
      octogonImageViews = new OctogonImageView[8];
      octogonImageViews[0] = (OctogonImageView) findViewById(R.id.image1);
      octogonImageViews[1] = (OctogonImageView) findViewById(R.id.image2);
      octogonImageViews[2] = (OctogonImageView) findViewById(R.id.image3);
      octogonImageViews[3] = (OctogonImageView) findViewById(R.id.image4);
      octogonImageViews[4] = (OctogonImageView) findViewById(R.id.image5);
      octogonImageViews[5] = (OctogonImageView) findViewById(R.id.image6);
      octogonImageViews[6] = (OctogonImageView) findViewById(R.id.image7);
      octogonImageViews[7] = (OctogonImageView) findViewById(R.id.image8);
    }

    loadApplicationImageView();

    findViewById(R.id.launcher).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, ApplicationGridActivity.class));
      }
    });
  }

  @Override
  public void onBackPressed() {
  }


  /**
   * Notify that async was completed
   */
  @Override
  public void asyncTaskCompleteCallBack() {
    applicationDetailsModels = ApplicationDataBase.getSingletonInstance(this).getAllApplications();
    if (applicationDetailsModels.size() > 0)
      loadAppsInHomeScreen(false);

  }

  /**
   * Sort by most frequent used first
   */
  private void sortMostUsed() {
    Collections.sort(applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return rhs.getFrequencyCount() - lhs.getFrequencyCount();
      }
    });

    Collections.sort(applicationDetailsModels, new Comparator<ApplicationDetailsModel>() {
      @Override
      public int compare(ApplicationDetailsModel lhs, ApplicationDetailsModel rhs) {
        return (int) (rhs.getLastUsedOn() - lhs.getLastUsedOn());
      }
    });
  }

}
