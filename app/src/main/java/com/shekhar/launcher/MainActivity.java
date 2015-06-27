package com.shekhar.launcher;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.OctogonImageView;
import com.shekhar.launcher.async.AddApplicationDetailsAsync;
import com.shekhar.launcher.dao.ApplicationDetailsModel;
import com.shekhar.launcher.database.ApplicationDataBase;
import com.shekhar.launcher.intrface.CallBack;

import java.util.List;


public class MainActivity extends Activity implements CallBack {

  private List<ApplicationDetailsModel> applicationDetailsModels;
  private final String TAG = "<<MainActivity>>";
  ApplicationDataBase applicationDataBase;

  private ImageView replace;
  public OctogonImageView[] img;
  private String selectedId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    applicationDataBase = ApplicationDataBase.getSingletonInstance(this);
    applicationDetailsModels = applicationDataBase.getAllApplications();
    if (applicationDetailsModels.size() < 1)
      new AddApplicationDetailsAsync(this, this).execute();
    else
      loadAppsInHomeScreen();

  }

  private void loadApplicationImageView() {
    for (int i = 0; i < 8; i++) {
      final ApplicationDetailsModel model = applicationDetailsModels.get(i);
      img[i].setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          replace.setVisibility(View.VISIBLE);
          selectedId = String.valueOf(model.getId());
          Dragshadow shadowBuilder = new Dragshadow(view);
          ClipData data = ClipData.newPlainText(" ", " ");
          view.startDrag(data, shadowBuilder, view, 0);
          return false;
        }
      });

      img[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          run(model.getAppPackage());
        }
      });

      try {
        img[i].setImageDrawable(getPackageManager().getApplicationIcon(model.getAppPackage()));
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public void run(String packageName) {
    Log.e("run", packageName, null);
    try {
      Intent intent = this.getPackageManager().getLaunchIntentForPackage(packageName);
      this.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class Dragshadow extends View.DragShadowBuilder {
    ColorDrawable grayShedow;

    public Dragshadow(View view) {
      super(view);
      grayShedow = new ColorDrawable(Color.LTGRAY);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
      grayShedow.draw(canvas);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
      View v = getView();
      int h = v.getHeight();
      int w = v.getWidth();
      grayShedow.setBounds(0, 0, h, w);
      shadowSize.set(h, w);
      shadowTouchPoint.set(h, w);
    }

  }

  View.OnDragListener dragListener = new View.OnDragListener() {
    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
      int event = dragEvent.getAction();
      switch (event) {
        case DragEvent.ACTION_DRAG_ENTERED:
          Log.e("event", "entered");
          break;
        case DragEvent.ACTION_DRAG_EXITED:
          Log.e("event", "exit");
          break;
        case DragEvent.ACTION_DROP:
          ImageView targetView = (ImageView) view;
          if (targetView.getId() == replace.getId()) {
            Intent intent = new Intent(MainActivity.this, ApplicationViewer.class);
            intent.putExtra("ID", selectedId);
            MainActivity.this.startActivity(intent);
          }
          break;
        case DragEvent.ACTION_DRAG_ENDED:
          Log.e("event", "ended");
          replace.setVisibility(View.GONE);
          break;
      }
      return true;
    }
  };


  private void loadAppsInHomeScreen() {
    img = new OctogonImageView[8];
    replace = (ImageView) findViewById(R.id.replace);
    img[0] = (OctogonImageView) findViewById(R.id.image1);
    img[1] = (OctogonImageView) findViewById(R.id.image2);
    img[2] = (OctogonImageView) findViewById(R.id.image3);
    img[3] = (OctogonImageView) findViewById(R.id.image4);
    img[4] = (OctogonImageView) findViewById(R.id.image5);
    img[5] = (OctogonImageView) findViewById(R.id.image6);
    img[6] = (OctogonImageView) findViewById(R.id.image7);
    img[7] = (OctogonImageView) findViewById(R.id.image8);
    replace.setOnDragListener(dragListener);

    loadApplicationImageView();
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
      loadAppsInHomeScreen();

  }

}
