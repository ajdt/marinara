package com.itsaunixsystem.marinara;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author: ajdt on 8/6/16.
 * @description: class used with DBFlow ORM to save/query tasks in database
 */
@Table(databaseName = AppDatabase.NAME)
public class Task extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public long id ;

    @Column
    public String name ;

    @Column
    public TaskStatus status ;
}
