package com.example.todoapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoapp.models.Work;

@Database(entities={Work.class},version=1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WorkDao workDao();}
