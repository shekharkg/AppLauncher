package com.shekhar.launcher.async;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;
import com.shekhar.launcher.intrface.CallBack;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ShekharKG on 6/27/2015.
 */
public class AddApplicationDetailsAsync extends AsyncTask<Void, Void, Void> {

  private final String TAG = "<<Async>>";
  private Context context;
  private CallBack callBack;
  private ApplicationDataBase applicationDataBase;

  public AddApplicationDetailsAsync(Context context, CallBack callBack) {
    this.context = context;
    this.callBack = callBack;
    applicationDataBase = ApplicationDataBase.getSingletonInstance(context);
  }

  @Override
  protected Void doInBackground(Void... params) {

    //Get PackageInfo list of installed apps
    PackageManager packageManager = context.getPackageManager();
    List<PackageInfo> listAppInfo = packageManager
        .getInstalledPackages(PackageManager.GET_ACTIVITIES);

    Calendar calendar = Calendar.getInstance();

    //Iterating PackageInfo for package name
    for (PackageInfo packageInfo : listAppInfo) {

      //To remove the system apps details
      if (context.getPackageManager().getLaunchIntentForPackage(packageInfo.packageName) != null) {
        Log.e(TAG, "App Details: Package name : " + packageInfo.packageName
            + ", App name : " + packageInfo.applicationInfo.loadLabel(packageManager).toString()
            + ", Installed on: " + packageInfo.firstInstallTime);

        applicationDataBase.insertIntoTable(new ApplicationDetailsModel(packageInfo.packageName,
            packageInfo.applicationInfo.loadLabel(packageManager).toString(),
            packageInfo.firstInstallTime, calendar.getTimeInMillis(), 0));

      }
    }

    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    //Notify calling method about completion
    callBack.asyncTaskCompleteCallBack();
  }
}
