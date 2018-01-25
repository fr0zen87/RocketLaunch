package com.example.fr0zen.rocketlaunch;

import android.graphics.Bitmap;

public class ListItem {

    private String rocketName;
    private long launchDate;
    private Bitmap missionImage;
    private String details;
    private String articleLink;

    public ListItem(String rocketName, long launchDate, Bitmap missionImage, String details) {
        this.rocketName = rocketName;
        this.launchDate = launchDate;
        this.missionImage = missionImage;
        this.details = details;
    }

    public String getRocketName() {
        return rocketName;
    }

    public long getLaunchDate() {
        return launchDate;
    }

    public Bitmap getMissionImage() {
        return missionImage;
    }

    public String getDetails() {
        return details;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}
