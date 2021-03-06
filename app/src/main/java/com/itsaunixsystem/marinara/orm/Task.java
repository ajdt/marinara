package com.itsaunixsystem.marinara.orm;


import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.NotNull;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: ajdt on 8/6/16.
 * @description: an orm class to facilitate DB access of tasks users create/delete
 */
public class Task extends SugarRecord {

    // IMPORTANT: unique conflicts are handled by sugarORM with a replacement strategy.
    // For this application, replacement might break foreign key dependencies.
    // To avoid this use Task.create() instead of Task.save()
    @Unique
    @NotNull
    private String name ;

    @NotNull
    public TaskStatus status ;

    @Ignore
    public static final long INVALID_TASK_ID_FLAG = -1 ;



    /****************************** CONSTRUCTORS ******************************/

    public Task() { } // required by SugarORM

    public Task(String name, TaskStatus status) { this.name = name ; this.status = status ;}

    /****************************** OVERRIDDEN ******************************/

    /**
     * overridden deletion method to enforce having at least one task in the Task table,
     * and to avoid deleting tasks still referenced by PomodoroSessions
     * @return true if deletion is successful
     */
    @Override
    public boolean delete() {

        // don't delete task if it's the last one in the Task table that's active
        if (Task.getActiveTasks().size() <= 1)
            return false ;
        // don't delete task if its id is a foreign key to a pomodoro session
        else if (referencedBySavedPomodoroSessions()) {
            this.status = TaskStatus.DELETED ;
            this.save() ;
            return false ;
        } else
            return super.delete() ;
    }

    /**
     * check if another task exists in DB with same name as this task, if so
     * ensure other task is set to Active. If not, then save this task.
     * @return the id of created task or -1 if the task could not be saved to DB
     */
    public long create() {
        if (Task.isActiveTask(this.name)) { // task name already in use
            return Task.INVALID_TASK_ID_FLAG ;
        } else if (Task.isDeletedTask(this.name)) {
            // task existed previously, but was deleted. Restore it instead of saving this task
            Task existing_task      = Task.getByName(this.name);
            existing_task.status    = TaskStatus.ACTIVE ;
            return existing_task.save();
        } else {
            return this.save() ;
        }
    }

    /**
     * NOTE: required so TaskArrayAdapter is able to locate Tasks using
     * ArrayList<Task>::indexOf()
     * @param obj
     * @return true if argument is a Task and all its fields are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj.getClass() == Task.class))
            return false ;

        // cast to Task and compare fields
        Task other_task = (Task) obj ;
        return other_task.name.equals(this.name) &&
                other_task.status == this.status &&
                other_task.getId() == this.getId() ;

    }

    @Override
    public String toString() {
        return "Task[" + Long.toString(getId()) + ", " + name + ", " + status.name() + "]" ;
    }

    /****************************** GETTERS ******************************/

    public String getName() { return this.name ;}

    /****************************** DB LOOKUPS ******************************/

    // a single task may have multiple sessions
    // this is the only way I know to obtain those sessions
    public List<PomodoroSession> getPomodoroSessions() {
        return PomodoroSession.find(PomodoroSession.class, "task = ?", Long.toString(getId())) ;
    }

    public static ArrayList<Task> getTasks() { return new ArrayList(Task.listAll(Task.class)) ; }

    public static ArrayList<Task> getActiveTasks() {
        return new ArrayList(Task.find(Task.class, "status = ?", TaskStatus.ACTIVE.name())) ;
    }

    public static Task getById(long id) { return Task.findById(Task.class, id) ; }

    public static Task getByName(String name) {
        List<Task> results = Task.find(Task.class, "name = ?", name) ;
        if (results.isEmpty())
            return null ;
        else
            return results.get(0) ;
    }

    /****************************** BOOLEAN HELPERS ******************************/

    public static boolean isActiveTask(String name) {
        Task the_task = Task.getByName(name) ;
        if (the_task == null)
            return false ;
        else
            return (the_task.status == TaskStatus.ACTIVE) ;
    }

    public static boolean isDeletedTask(String name) {
        return Task.taskNameAlreadyExists(name) && !Task.isActiveTask(name) ;
    }

    /**
     * @return true if task's id is a foreign key to PomodoroSessions saved in DB
     */
    public boolean referencedBySavedPomodoroSessions() { return this.getPomodoroSessions().size() > 0 ; }

    public static boolean taskNameAlreadyExists(String name_to_check) {
        return Task.getByName(name_to_check) != null ;
    }

}
