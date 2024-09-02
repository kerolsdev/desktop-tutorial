package com.kerolsmm.incognito.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//ASC for Firest id ;;
@Dao
public interface mDao {
   /* @Insert
    void insert(Tab videoModel);
    @Update
    void update(Tab videoModel);
    @Delete
    void delete(Tab videoModel);*/
    @Query("DELETE FROM bookmark")
    void deleteAllBookmark();
    @Query("SELECT * FROM bookmark ORDER BY id DESC")
     LiveData<List<BookMark>> mQuerybyId();
    @Delete
    void deleteBookMark(BookMark bookMark);
    @Insert
    void insertBookMark(BookMark bookMark);
     @Query("SELECT * FROM bookmark WHERE Url LIKE :KYU ")
     BookMark LOLO(String KYU) ;
     //////
    @Query("DELETE FROM filemodel")
    void deleteDownload();
     @Query("SELECT * FROM filemodel ORDER BY id DESC")
    LiveData<List<FileModel>> mQuerybyIdFile();
     @Delete
     void deleteFile(FileModel fileModel);
     @Insert
     void insertFile(FileModel fileModel);
    @Query("SELECT * FROM filemodel WHERE Url LIKE :KYU ")
    FileModel LOLOFile(String KYU) ;

      @Query("DELETE FROM bookmark WHERE Url  LIKE :BookName")
    void DeleteFileHistory(String BookName);


/*
    @Query("DELETE FROM task WHERE Title  LIKE :BookName")
     void DeleteFileHistory(String BookName);*/

}