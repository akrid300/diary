package com.example.agenda.ui.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.model.Location;


@Database(entities = {Event.class, Location.class}, version = 1, exportSchema = false)
public abstract class DatabaseInstance extends RoomDatabase {
    private static volatile DatabaseInstance dbInstance; //saved on data segment, can be accessed from any part of the code.
    //volatile - ACCESSED AND VISIBLE FROM ANY THREADS OF THE PROCESS. Exactly like static, but at the memory level.

    public abstract EventDAO eventDAO();
    public abstract LocationDAO locationDAO();

    public static DatabaseInstance getInstance(Context context) {

        if(dbInstance == null){
            synchronized (DatabaseInstance.class){

                if(dbInstance == null){ //we sync after checking if it was null. Another thread could have created one. So we check again.

                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseInstance.class,
                            "events.db")
                            .allowMainThreadQueries()
                            .build();
                }

            }
        }

        return dbInstance;
    }
}

