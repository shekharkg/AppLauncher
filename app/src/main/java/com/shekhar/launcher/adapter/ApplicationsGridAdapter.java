package com.shekhar.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shekhar.launcher.R;
import com.shekhar.launcher.activity.MainActivity;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;

import java.util.Calendar;

/**
 * Created by ShekharKG on 6/28/2015.
 */
public class ApplicationsGridAdapter extends BaseAdapter {

  private Context context;
  private ApplicationDataBase applicationDataBase;

  public ApplicationsGridAdapter(Context context) {
    this.context = context;
    applicationDataBase = ApplicationDataBase.getSingletonInstance(context);
  }

  @Override
  public int getCount() {
    return MainActivity.applicationDetailsModels.size();
  }

  @Override
  public Object getItem(int position) {
    return MainActivity.applicationDetailsModels.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    View row = convertView;
    ViewHolder holder = null;
    if (row == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.single_application, parent, false);
      holder = new ViewHolder();
      holder.appName = (TextView) row.findViewById(R.id.appName);
      holder.appIcon = (ImageView) row.findViewById(R.id.appIcon);
      row.setTag(holder);
    } else {
      holder = (ViewHolder) row.getTag();
    }

    final ApplicationDetailsModel model = MainActivity.applicationDetailsModels.get(position);

    holder.appName.setText(model.getAppName());
    try {
      Drawable icon = context.getPackageManager().getApplicationIcon(model.getAppPackage());
      holder.appIcon.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    row.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int count = model.getFrequencyCount();
        long time = Calendar.getInstance().getTimeInMillis();
        applicationDataBase.updateAppDetails(model.getId(), time, ++count);
        model.setLastUsedOn(time);
        model.setFrequencyCount(count);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(model.getAppPackage());
        context.startActivity(intent);
      }
    });

    row.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        uninstallApplication(model);

        return false;
      }
    });

    Animation anim = AnimationUtils.loadAnimation(context, R.anim.fly_in);
    row.setAnimation(anim);
    anim.start();

    return row;
  }

  public void uninstallApplication(ApplicationDetailsModel model) {
    if (model.getAppPackage() != null) {
      try {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + model.getAppPackage()));
        context.startActivity(intent);
        applicationDataBase.deleteApp(model.getId());
        MainActivity.applicationDetailsModels.remove(model);
        this.notifyDataSetChanged();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private class ViewHolder {
    TextView appName;
    ImageView appIcon;
  }
}
