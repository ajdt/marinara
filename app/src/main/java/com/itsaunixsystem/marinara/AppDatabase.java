package com.itsaunixsystem.marinara;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ajdt on 8/06/16.
 */
@Database(name=AppDatabase.NAME, version=AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase" ;
    public static final int VERSION = 1 ;
}
