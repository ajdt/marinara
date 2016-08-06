package com.itsaunixsystem.marinara;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.NotNull;
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
    @NotNull
    public Timestamp started_at ;

    @NotNull
    public long duration ;

    @NotNull
    @Column
    @ForeignKey(saveForeignKeyModel = false,
            references = {@ForeignKeyReference(columnType = Task.class,
                                                    columnName = "task",
                                                    foreignColumnName = "id"
                                                )})
    public ForeignKeyContainer<Task> task ;

    // TODO: this is incorrect. Why would you need it anyway? Documentation unclear
    public void associateTask(Task some_task) {
        this.task = new ForeignKeyContainer<Task>(Task.class) ;

    }


}
