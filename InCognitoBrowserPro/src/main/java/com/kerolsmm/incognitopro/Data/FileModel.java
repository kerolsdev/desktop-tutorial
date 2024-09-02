package com.kerolsmm.incognitopro.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "filemodel")
public class FileModel {

    private String Name;
    private String Url;
    private String path;

    @PrimaryKey(autoGenerate = true)
    private int id;


    @Ignore
    public FileModel () {

    }

    @Ignore
    public FileModel (String Name , String Url, String path) {
        this.Name = Name;
        this.Url = Url;
        this.path= path;
    }

    public FileModel (int id,String Name , String Url,String path) {

        this.path = path;
        this.Name = Name;
        this.Url = Url;
        this.id = id;

    }

    public String getUrl() {
        return Url;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
