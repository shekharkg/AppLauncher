package com.shekhar.launcher;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shekhar.launcher.async.AddApplicationDetailsAsync;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;
import com.shekhar.launcher.intrface.CallBack;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity implements CallBack {

  private List<ApplicationDetailsModel> applicationDetailsModels;
  private final String TAG = "<<MainActivity>>";
  ApplicationDataBase applicationDataBase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    applicationDataBase = ApplicationDataBase.getSingletonInstance(this);
    applicationDetailsModels = applicationDataBase.getAllApplications();
    if (applicationDetailsModels.size() < 1)
      new AddApplicationDetailsAsync(this, this).execute();
    else
      showList();
  }

  private void showList() {
    ListView listView = (ListView) findViewById(R.id.listView);
    String[] appNames = new String[applicationDetailsModels.size()];
    for (int index = 0; index < applicationDetailsModels.size(); index++) {
      appNames[index] = applicationDetailsModels.get(index).getAppName();
    }

//    listView.setAdapter(new ArrayAdapter<String>(this,
//        android.R.layout.simple_list_item_1, appNames));
//
//    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ApplicationDetailsModel model = applicationDetailsModels.get(position);
//        Calendar calendar = Calendar.getInstance();
//        int frequencyCount = model.getFrequencyCount();
//        applicationDataBase.updateAppDetails(model.getId(),
//            calendar.getTimeInMillis(), ++frequencyCount);
//
//        model.setFrequencyCount(frequencyCount);
//        model.setLastUsedOn(calendar.getTimeInMillis());
//
//        Toast.makeText(MainActivity.this, model.getFrequencyCount() + "", Toast.LENGTH_SHORT).show();
//      }
//    });
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
      showList();

  }


}
