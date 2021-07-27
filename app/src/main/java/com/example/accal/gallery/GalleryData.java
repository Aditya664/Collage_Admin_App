package com.example.accal.gallery;

public class GalleryData
{  private String Durl,category,key,Sfilename;

    public GalleryData() {
    }

    public GalleryData(String durl, String category, String key, String sfilename) {
       this.Durl = durl;
        this.category = category;
        this.key = key;
        this.Sfilename = sfilename;
    }

    public String getDurl() {
        return Durl;
    }

    public void setDurl(String durl) {
        Durl = durl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSfilename() {
        return Sfilename;
    }

    public void setSfilename(String sfilename) {
        Sfilename = sfilename;
    }
}
