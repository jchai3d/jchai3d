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
 * provides a class to manage threads.
 *
 * @author Jairo Melo
 */
public class JThread {

    /**
     * Thread handle
     */
    protected Thread handler;
    /**
     * Pointer to thread function.
     */
    protected Object function;
    /**
     * Thread priority level.
     */
    protected JThreadPriority priorityLevel;

    /**
     * Constructor of JThread.
     */
    public JThread() {

        // no thread function has been defined yet
        function = 0;

        // default value for priority level
        priorityLevel = JThreadPriority.CHAI_THREAD_PRIORITY_GRAPHICS;
    }

    /**
     * Adjust the priority level of the thread.
     *
     * @param aLevel
     */
    private void setPriority(JThreadPriority aLevel) {
        priorityLevel = aLevel;
        switch (priorityLevel) {
            case CHAI_THREAD_PRIORITY_GRAPHICS:
                handler.setPriority(Thread.NORM_PRIORITY);
                break;

            case CHAI_THREAD_PRIORITY_HAPTICS:
                handler.setPriority(Thread.MAX_PRIORITY);
                break;
        }
    }

    /**
     * Creates a thread to execute within the address space of the calling
     * process. Parameters include a pointer to the function and its priority
     * level.
     *
     * @param aFunction
     * @param aLevel
     */
    public void set(Runnable aFunction, JThreadPriority aLevel) {

        handler = new Thread(aFunction);

        // set thread priority level
        setPriority(aLevel);

    }

    /**
     * Start execution thread
     */
    public void start() {
        if (handler != null) {
            handler.start();
        }
    }

    /**
     * Define time to wait process
     *
     * @param milliseconds
     * @param s
     */
    public static void safeSleep(long milliseconds, String s) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
