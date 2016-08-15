package com.itsaunixsystem.marinara.util;

/**
 * @author: ajdt on 8/14/16.
 * @description: utility function providing static methods to deal with time conversions
 * (e.g. millisec to minutes/seconds)
 */
public class TimeConversionHelper {

    /**
     *
     * @param millisec
     * @return the time-value as a string of the form "<minutes>:<seconds>"
     * NOTE: My requirements are simple, so I opted out of using DateFormat or related classes
     */
    public static String millisecToTimeString(long millisec) {
        long seconds = (millisec / 1000) % 60 ;
        long minutes = (millisec / (1000 * 60)) % 60 ;

        return String.format("%02d:%02d", minutes, seconds) ;
    }

    /****************************** millisec <--> seconds, minutes ******************************/

    public static long millisecToMinutes(long millisec) { return millisec / (1000 * 60) ;}

    public static long millisecToSeconds(long millisec) { return millisec / 1000  ; }

    /**
     * compute number of seconds displayed if given time was in HH:MM:SS format
     * @param millisec
     */
    public static long secondsRemainder(long millisec) { return (millisec / 1000) % 60 ; }

    /**
     *
     * @param minutes
     * @param seconds
     * @return convert minutes and seconds each to millisec and return their sum
     */
    public static long minAndSecToMillisec(long minutes, long seconds) {
        return minutes * 60 * 1000 + seconds * 1000 ;
    }

}
