package com.shekhar.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shekhar.launcher.R;
import com.shekhar.launcher.dao.ApplicationDetailsModel;

import java.util.List;

/**
 * Created by Mradul on 6/27/2015.
 */
public class ApplicationAdapter extends BaseAdapter {
  Context context;
  List<ApplicationDetailsModel> applicationDetailsModels;
  String parse;

  public ApplicationAdapter(List<ApplicationDetailsModel> applicationDetailsModels, String parse, Context context) {
    this.parse = parse;
    this.applicationDetailsModels = applicationDetailsModels;
    this.context = context;
  }

  @Override
  public int getCount() {
    return applicationDetailsModels.size();
  }

  @Override
  public Object getItem(int i) {
    return applicationDetailsModels.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public class ViewHolder {
    TextView appName;
    ImageView appIcon;

    ViewHolder(View view) {
      appName = (TextView) view.findViewById(R.id.appName);
      appIcon = (ImageView) view.findViewById(R.id.appIcon);
    }
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    View row = view;
    ViewHolder holder = null;
    if (row == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = inflater.inflate(R.layout.single_application, viewGroup, false);
      holder = new ViewHolder(row);
      row.setTag(holder);
    } else {
      holder = (ViewHolder) row.getTag();
    }

    final ApplicationDetailsModel data = applicationDetailsModels.get(i);

    holder.appName.setText(data.getAppName());
    Log.e("APP NAME", data.getAppName());
    try {
      Drawable icon = context.getPackageManager().getApplicationIcon(data.getAppPackage());
      holder.appIcon.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    row.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        uninstallApplication(data.getAppPackage());

        return false;
      }
    });
    if (parse != null) {

      row.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Toast.makeText(context, "Long click item" + parse, Toast.LENGTH_LONG).show();

          Intent intent = new Intent(context, MediaActionSound.class);
          context.startActivity(intent);

        }
      });

    }

    return row;
  }

  public void uninstallApplication(String applicationPackageName) {
    if (applicationPackageName != null) {
      try {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + applicationPackageName));
        context.startActivity(intent);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
