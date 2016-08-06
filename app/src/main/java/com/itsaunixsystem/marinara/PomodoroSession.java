package com.itsaunixsystem.marinara;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.sql.Timestamp;

/**
 * @author: ajdt on 8/6/16.
 * @description: encapsulates data about a single pomodoro session
 */
@Table(databaseName = AppDatabase.NAME)
public class PomodoroSession extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id ;

    @Column
    public Timestamp started_at ;
    public long duration ;

    @Column
    @ForeignKey(saveForeignKeyModel = false,
            references = {@ForeignKeyReference(columnType = Task.class,
                                                    columnName = "task",
                                                    foreignColumnName = "id"
                                                )})
    public ForeignKeyContainer<Task> task ;


}
