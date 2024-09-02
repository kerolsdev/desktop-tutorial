package com.kerolsmm.incognitopro.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark")
public class BookMark {


    private String Title;
    private String Url;
    //private byte[] image;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    public BookMark () {}


    @Ignore
    public BookMark (String Title , String Url) {
        this.Title = Title;
        this.Url = Url;


    }

    public BookMark (int id, String Title , String Url) {
        this.Title = Title;
        this.Url = Url;
        this.id=id;
    }

    public String getTitle() {
        return Title;
    }


    public String getUrl() {
        return Url;
    }

    public int getId() {
        return id;
    }
}
