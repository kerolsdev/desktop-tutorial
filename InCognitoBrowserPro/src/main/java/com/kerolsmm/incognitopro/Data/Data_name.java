package com.kerolsmm.incognitopro.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {BookMark.class,FileModel.class}, version = 1,exportSchema = false)
public abstract class Data_name extends RoomDatabase {

    private static String DataBase_Name = "IncognitoProApp";
    private static Data_name data_name;
    private static final Object Lock = new Object();


    public static Data_name getInstance(Context context) {

        if (data_name == null) {
            synchronized (Lock) {
                data_name = Room.databaseBuilder(context.getApplicationContext(), Data_name.class, Data_name.DataBase_Name)
                        .addMigrations()
                        .build();
            }
        }
        return data_name;
    }

    public abstract mDao kerols();

     static final Migration MIGRATION_1_2 = new Migration(1, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users "
                    + "ADD COLUMN address TEXT");

        }
    };
}