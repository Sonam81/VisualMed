package com.example.visualmed;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Medicine.class,MedicineTime.class}, version = 2, exportSchema = false)

public abstract class MyAppDatabase extends RoomDatabase {

    public abstract MedicineDAO medicineDAO();
}

