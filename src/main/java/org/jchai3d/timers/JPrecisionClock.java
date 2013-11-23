/*
 *   This file is part of the JCHAI 3D visualization and haptics libraries.
 *   Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License("GPL") version 2
 *   as published by the Free Software Foundation.
 *
 *   For using the JCHAI 3D libraries with software that can not be combined
 *   with the GNU GPL, and for taking advantage of the additional benefits
 *   of our support services, please contact CHAI 3D about acquiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */
package org.jchai3d.timers;

/**
 *
 * @author Jairo Melo
 */
public class JPrecisionClock {

    /**
     * Time accumulated between previous calls to "start" and "stop".
     */
    private double accumulatedTime;
    /**
     * CPU time when clock was started.
     */
    private double startTime;
    /**
     * Timeout period.
     */
    private double timeoutPeriod;
    /**
     * clock time when timer was started.
     */
    private double timeoutStart;
    /**
     * If \b true, a high precision CPU clock is available.
     */
    private boolean highresAvailable;
    /**
     * If \b true, the clock is \b on.
     */
    private boolean enabled;

    /**
     * Constructor of cPrecisionClock. Clock is initialized to zero
     */
    public JPrecisionClock() {
        // clock is currently off
        enabled = false;

        // test for high performance timer on the local machine. Some old computers
        // may not offer this feature
        //QueryPerformanceFrequency (mFreq);
        /*
         * if (mFreq.QuadPart <= 0) { mHighres = false; }
        else
         */
        {
            highresAvailable = true;
        }
        //mHighres = true;

        // initialize current time
        accumulatedTime = 0.0;

        // initialize timeout
        timeoutPeriod = 0.0;
    }

    /**
     * Reset the clock to zero.
     */
    public void reset() {
        // initialize current time of timer
        accumulatedTime = 0.0;
        startTime = getCPUTimeSeconds();
    }

    /**
     * Start the clock from its current time value. To read the latest time from
     * the clock, use method getCurrentTime.
     *
     * @return
     */
    public double start() {
        return start(false);
    }

    /**
     * Start the clock from its current time value. To read the latest time from
     * the clock, use method getCurrentTime.
     *
     * @param aResetClock
     * @return
     */
    public double start(boolean aResetClock) {
        // store cpu time when timer was started
        startTime = getCPUTimeSeconds();

        if (aResetClock) {
            accumulatedTime = 0.0;
        }

        // timer is now on
        enabled = true;

        // return time when timer was started.
        return accumulatedTime;
    }

    /**
     * Stop the timer. To resume counting call start().
     *
     * @return
     */
    public double stop() {

        // How much time has now elapsed in total running "sessions"?
        accumulatedTime += getCPUTimeSeconds() - startTime;

        // stop timer
        enabled = false;

        // return time when timer was stopped
        return getCurrentTimeSeconds();
    }

    /**
     * Set the period in \e microseconds before timeout occurs. Do not forget to
     * set the timer on by calling method \e start()
     *
     * @param aTimeoutPeriod
     */
    public void setTimeoutPeriodSeconds(double aTimeoutPeriod) {
        timeoutPeriod = aTimeoutPeriod;
    }

    /**
     * Check if timer has expired its timeout period. if so return \b true
     *
     * @return
     */
    public boolean timeoutOccurred() {
        // check if timeout has occurred
        if (getCurrentTimeSeconds() > timeoutPeriod) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Read the current time of timer. Result is returned in \e seconds
     *
     * @return
     */
    public double getCurrentTimeSeconds() {
        if (enabled) {
            return accumulatedTime + getCPUTimeSeconds() - startTime;
        } else {
            return accumulatedTime;
        }
    }

    /**
     * If all you want is something that tells you the time, this is your
     * function
     *
     * @return
     */
    public double getCPUTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    /**
     * Return <code>true</code> if timer is currently \b on, else return \b false.
     *
     * @return
     */
    public boolean isEnabled() {
        return (enabled);
    }

    /**
     * Read the programmed timeout period
     *
     * @return
     */
    public double getTimeoutPeriodSeconds() {
        return (timeoutPeriod);
    }

    /**
     * Returns \b true if high resolution timers are available on this computer.
     *
     * @return
     */
    public boolean highResolutionAvailable() {
        return (highresAvailable);
    }

    /**
     * For backwards-compatibility...
     *
     * @return
     */
    public double getCPUTime() {
        return getCPUTimeSeconds();
    }

    /**
     * For backwards-compatibility...
     *
     * @return
     */
    public double getCPUtime() {
        return getCPUTimeSeconds();
    }
}
