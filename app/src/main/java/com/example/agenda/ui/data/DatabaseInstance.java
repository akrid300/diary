package com.example.agenda.ui.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.agenda.ui.model.EventModel;
import com.example.agenda.ui.model.LocationModel;


@Database(entities = {EventModel.class, LocationModel.class}, version = 1, exportSchema = false)
public abstract class DatabaseInstance extends RoomDatabase {
    private static volatile DatabaseInstance dbInstance; //saved on data segment, can be accessed from any part of the code.
    //volatile - ACCESSED AND VISIBLE FROM ANY THREADS OF THE PROCESS. Exactly like static, but at the memory level.

    public abstract EventDAO eventDAO();
    public abstract LocationDAO locationDAO();

    public static DatabaseInstance getInstance(Context context){

        if(dbInstance == null){
            synchronized (DatabaseInstance.class){

                if(dbInstance == null){ //we sync after checking if it was null. Another thread could have created one. So we check again.

                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseInstance.class,
                            "test.db")
                            .allowMainThreadQueries()
                            .build();
                }

            }
        }

        return dbInstance;
    }
}

