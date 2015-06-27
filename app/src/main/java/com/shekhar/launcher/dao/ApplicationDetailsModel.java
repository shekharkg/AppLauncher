package com.shekhar.launcher.dao;

/**
 * Created by ShekharKG on 6/27/2015.
 */
public class ApplicationDetailsModel {

  private int _id;
  private String appPackage;
  private String appName;
  private long installedOn;
  private long lastUsedOn;
  private int frequencyCount;

  /**
   * Called to create object using data from database
   */
  public ApplicationDetailsModel(int _id, String appPackage, String appName,
                                 long installedOn, long lastUsedOn, int frequencyCount) {
    this._id = _id;
    this.appPackage = appPackage;
    this.appName = appName;
    this.installedOn = installedOn;
    this.lastUsedOn = lastUsedOn;
    this.frequencyCount = frequencyCount;
  }

  /**
   * Called to create object to insert into database
   */
  public ApplicationDetailsModel(String appPackage, String appName,
                                 long installedOn, long lastUsedOn, int frequencyCount) {
    this.appPackage = appPackage;
    this.appName = appName;
    this.installedOn = installedOn;
    this.lastUsedOn = lastUsedOn;
    this.frequencyCount = frequencyCount;
  }

  public int getId() {
    return _id;
  }

  public String getAppPackage() {
    return appPackage;
  }

  public String getAppName() {
    return appName;
  }

  public long getInstalledOn() {
    return installedOn;
  }

  public long getLastUsedOn() {
    return lastUsedOn;
  }

  public void setLastUsedOn(long lastUsedOn) {
    this.lastUsedOn = lastUsedOn;
  }

  public int getFrequencyCount() {
    return frequencyCount;
  }

  public void setFrequencyCount(int frequencyCount) {
    this.frequencyCount = frequencyCount;
  }
}
