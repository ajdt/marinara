package com.itsaunixsystem.marinara;

/**
 * @author: ajdt on 8/6/16.
 * @description: indicates whether task stored in database is active or deleted. We only truly delete
 * a task if there are no sessions in DB with a foreign key on that task
 */
public enum TaskStatus {
    ACTIVE, DELETED ;
}
