package com.itsaunixsystem.marinara.orm;


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

    // TODO: not sure what this does, if anything
    @Unique
    @NotNull
    private String name ;

    @NotNull
    public String status ; // TODO: change this to an enum. Don't have setters/getters. If such methods don't do any validation then member might as well be public.


    // static constants indicate Task status
    @Ignore
    public static final String ACTIVE_STATUS = "ACTIVE" ;
    @Ignore
    public static final String DELETED_STATUS = "DELETED" ;



    /****************************** CONSTRUCTORS ******************************/

    public Task() { } // required by SugarORM

    public Task(String name, String status) { this.name = name ; this.status = status ;}

    /****************************** OVERRIDDEN ******************************/

    /**
     * overridden deletion method to enforce having at least one task in the Task table,
     * and to avoid deleting tasks still referenced by PomodoroSessions
     * @return true if deletion is successful
     */
    @Override
    public boolean delete() {

        // don't delete task if it's the last one in the Task table
        if (Task.getTasks().size() <= 1)
            return false ;
        // don't delete task if its id is a foreign key to a pomodoro session
        else if (referencedBySavedPomodoroSessions()) {
            this.status = DELETED_STATUS ;
            this.save() ;
            return false ;
        } else
            return super.delete() ;
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
        return new ArrayList(Task.find(Task.class, "status = ?", ACTIVE_STATUS)) ;
    }

    public static Task getById(long id) { return Task.findById(Task.class, id) ; }

    public static Task getByName(String name) {
        List<Task> results = Task.find(Task.class, "name = ?", name) ;
        if (results.isEmpty())
            return null ;
        else
            return results.get(0) ;
    }

    public static ArrayList<String> getActiveTaskNames() {
        ArrayList<String> names = new ArrayList<String>() ;
        for (Task t : Task.getActiveTasks())
            names.add(t.getName()) ;
        return names ;
    }


    /****************************** BOOLEAN HELPERS ******************************/

    public static boolean isActiveTask(String name) {
        Task the_task = Task.getByName(name) ;
        if (the_task == null)
            return false ;
        else
            return (the_task.status == Task.ACTIVE_STATUS) ;
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
