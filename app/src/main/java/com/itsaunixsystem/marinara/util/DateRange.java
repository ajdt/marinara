package com.itsaunixsystem.marinara.util;

import java.util.Date;

/**
 * @author: ajdt on 8/28/16.
 * @description: simple class for date ranges. Like a primitive tuple class.
 */
public class DateRange {
    private Date _start ;
    private Date _stop ;

    public DateRange(Date min, Date max) {
        _start = min ;
        _stop = max ;
    }

    public Date start() { return _start ; }
    public Date stop() { return _stop ; }
}
