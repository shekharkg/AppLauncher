package com.shekhar.launcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shekhar.launcher.dao.ApplicationDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShekharKG on 6/27/2015.
 */
public class ApplicationDataBase extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "dbAppLauncher";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_NAME = "tblAppLauncher";

  private static final String UID = "_id";
  private static final String APP_NAME = "appName";
  private static final String PACKAGE_NAME = "packageName";
  private static final String INSTALLED_ON = "installedOn";
  private static final String LAST_USED_ON = "lastUsedOn";
  private static final String FREQUENCY_COUNT = "frequencyCount";

  private SQLiteDatabase sqLiteDatabase;
  public static ApplicationDataBase applicationDataBase;

  /**
   * Create table queries
   */
  private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APP_NAME + " TEXT, " + PACKAGE_NAME + " TEXT, "
      + INSTALLED_ON + " LONG, " + LAST_USED_ON + " LONG, " + FREQUENCY_COUNT + " INTEGER);";

  /**
   * Drop table queries
   */
  private final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  /**
   * Private constructor for making class singleton
   */
  private ApplicationDataBase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    sqLiteDatabase = this.getWritableDatabase();
  }

  /**
   * Static method for getting singleton instance of class
   */
  public static ApplicationDataBase getSingletonInstance(Context context) {
    if (applicationDataBase == null)
      applicationDataBase = new ApplicationDataBase(context);
    return applicationDataBase;
  }


  /**
   * Insert application details into app database one at a time
   */
  public long insertIntoTable(ApplicationDetailsModel applicationDetailsModel) {

    ContentValues contentValues = new ContentValues();
    contentValues.put(APP_NAME, applicationDetailsModel.getAppName());
    contentValues.put(PACKAGE_NAME, applicationDetailsModel.getAppPackage());
    contentValues.put(INSTALLED_ON, applicationDetailsModel.getInstalledOn());
    contentValues.put(LAST_USED_ON, applicationDetailsModel.getLastUsedOn());
    contentValues.put(FREQUENCY_COUNT, applicationDetailsModel.getFrequencyCount());

    return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
  }

  /**
   * Get all applications details in application model
   */
  public List<ApplicationDetailsModel> getAllApplications() {
    List<ApplicationDetailsModel> applicationDetailsModels = new ArrayList<>();

    Cursor cursor = sqLiteDatabase.query(TABLE_NAME,
        new String[]{UID, APP_NAME, PACKAGE_NAME, INSTALLED_ON, LAST_USED_ON, FREQUENCY_COUNT}
        , null, null, null, null, APP_NAME, null);

    while (cursor.moveToNext()) {
      try {
        applicationDetailsModels.add(new ApplicationDetailsModel(cursor.getInt(0),
            cursor.getString(2), cursor.getString(1), cursor.getLong(3), cursor.getLong(4),
            cursor.getInt(5)));
      } catch (Exception ignored) {
      }
    }

    return applicationDetailsModels;
  }

  /**
   * Update last used time and frequency counter
   */
  public long updateAppDetails(int id, long lastUsedTime, int frequencyCount) {

    ContentValues values = new ContentValues();
    values.put(LAST_USED_ON, lastUsedTime);
    values.put(FREQUENCY_COUNT, frequencyCount);

    return sqLiteDatabase.update(TABLE_NAME, values, UID + "=?", new String[]{String.valueOf(id)});
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(DROP_TABLE);
    onCreate(db);
  }
}
