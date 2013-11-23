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


package org.jchai3d.devices;

/**
 * JGenericDevice Provides an general interface to communicate with hardware
 *   devices. A number of constants define a set of generic commands supported
 *   by the JGenericDevice:command method. For each generic command, we describe
 *   the data type and information that must be passed by parameter for
 *   index and  data.  command contains of course the command number
 *   corresponding to the following list of command constants.
 * @author jairo
 */
public class JGenericDevice
{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    //---------------------------------------------------------------------------
    // GENERIC DEVICE or BOARD:
    //---------------------------------------------------------------------------

    /**
     * Purpose: Query device to check if is operating correctly.
     * iData: integer type value. 1 means device is ok
     * 0 means device is not ready.
     */
    public static final int CHAI_CMD_GET_DEVICE_STATE = 1010;

    //---------------------------------------------------------------------------
    // GENERIC I/O BOARDS:
    //---------------------------------------------------------------------------

    /**
     * Purpose: Read the value of an encoder N.
     * Data: Integer type value.
     */

    /**
     * Reference to encoder 0.
     */
    public static final int CHAI_CMD_GET_ENCODER_0 = 1020;

    /**
     * Reference to encoder 1.
     */
    public static final int CHAI_CMD_GET_ENCODER_1 = 1021;

    /**
     * Reference to encoder 2.
     */
    public static final int CHAI_CMD_GET_ENCODER_2 = 1022;

    /**
     * Reference to encoder 3.
     */
    public static final int CHAI_CMD_GET_ENCODER_3 = 1023;

    /**
     * Reference to encoder 4.
     */
    public static final int CHAI_CMD_GET_ENCODER_4 = 1024;

    /**
     * Reference to encoder 5.
     */
    public static final int CHAI_CMD_GET_ENCODER_5 = 1025;

    /**
     * Reference to encoder 6.
     */
    public static final int CHAI_CMD_GET_ENCODER_6 = 1026;

    /**
     * Reference to encoder 7.
     */
    public static final int CHAI_CMD_GET_ENCODER_7 = 1027;


    /**
     * Purpose: Reset the value of an encoder N.
     * Data: Integer type value.
     */

    /**
     * Reference to reset signal encoder 0.
     */
    public static final int CHAI_CMD_RESET_ENCODER_0 = 1040;

    /**
     * Reference to reset signal encoder 1.
     */
    public static final int CHAI_CMD_RESET_ENCODER_1 = 1041;

    /**
     * Reference to reset signal encoder 2.
     */
    public static final int CHAI_CMD_RESET_ENCODER_2 = 1042;

    /**
     * Reference to reset signal encoder 3.
     */
    public static final int CHAI_CMD_RESET_ENCODER_3 = 1043;

    /**
     * Reference to reset signal encoder 4.
     */
    public static final int CHAI_CMD_RESET_ENCODER_4 = 1044;

    /**
     * Reference to reset signal encoder 5.
     */
    public static final int CHAI_CMD_RESET_ENCODER_5 = 1045;

    /**
     * Reference to reset signal encoder 6.
     */
    public static final int CHAI_CMD_RESET_ENCODER_6 = 1046;

    /**
     * Reference to reset signal encoder 7.
     */
    public static final int CHAI_CMD_RESET_ENCODER_7 = 1047;

    /**
     * Purpose: Set value to a DAC.
     * Data: Integer type value.
     */

    /**
     * Reference to DAC 0.
     */
    public static final int CHAI_CMD_SET_DAC_0 = 1030;

    /**
     * Reference to DAC 1.
     */
    public static final int CHAI_CMD_SET_DAC_1 = 1031;

    /**
     * Reference to DAC 2.
     */
    public static final int CHAI_CMD_SET_DAC_2 = 1032;

    /**
     * Reference to DAC 3.
     */
    public static final int CHAI_CMD_SET_DAC_3 = 1033;

    /**
     * Reference to DAC 4.
     */
    public static final int CHAI_CMD_SET_DAC_4 = 1034;

    /**
     * Reference to DAC 5.
     */
    public static final int CHAI_CMD_SET_DAC_5 = 1035;

    /**
     * Reference to DAC 6.
     */
    public static final int CHAI_CMD_SET_DAC_6 = 1036;

    /**
     * Reference to DAC 7.
     */
    public static final int CHAI_CMD_SET_DAC_7 = 1037;


    //---------------------------------------------------------------------------
    // GENERIC POINT CONTACT 3/6 DOF HAPTIC DEVICES:
    //---------------------------------------------------------------------------

    /**
     * Purpose: Read position (px, py, pz) in meters [m] of 3d point contact device.
     * Data: JVector3d type value.
     */
    public static final int CHAI_CMD_GET_POS_3D = 2000;

    /**
     * Purpose: Read normalized position (px, py, pz) of 3d point contact device.
     *          typically the value of each component of the vector position will
     *          be included in the interval [-1,1], accounting for the maximum
     *          usable workspace of the device.
     * Data: JVector3d type value.
     */
    public static final int CHAI_CMD_GET_POS_NORM_3D = 2001;

    /**
     * Purpose: Read velocity (vx, vy, vz) of 3d point contact device in [m/s].
     * Data:    JVector3d type value.
     */
    public static final int CHAI_CMD_GET_VEL_3D = 2002;

    /**
     * Purpose: Set a force (fx, fy, fz) to a 3d point contact device (in Newtons [N]).
     * Data:    cVector3d type value.
     */
    public static final int CHAI_CMD_SET_FORCE_3D = 2010;

    /**
     * Purpose: Set a normalized force (fx, fy, fz) to a 3d point contact device.
     *           A normalized force has a maximum length of 1.0 corresponding
     *           to the highest force that the device can generate.
     * Data:    JVector3d type value.
     */
    public static final int CHAI_CMD_SET_FORCE_NORM_3D = 2011;

    /**
     * Purpose: Set a force (fx, fy, fz) and a torque (tx, ty, tz) to a 6d point contact device.
     * Data:    array of 2 JVector3d type value.  Units are [N] and [N*mm]
     */
    public static final int CHAI_CMD_SET_FORCE_TORQUE_3D = 2012;

    /**
     * Purpose: Read orientation angles (ax, ay, az) of a 3d wrist or stylus.
     * Data:    JVector3d type value.
     */
    public static final int CHAI_CMD_GET_ROT_ANGLES = 2020;

    /**
     * Purpose: Read orientation matrix of a 3d wrist or stylus.
     * Data:    JMatrix3d type value.
     */
    public static final int CHAI_CMD_GET_ROT_MATRIX = 2021;

    /**
     * Purpose: Set a torque (tx, ty, tz) to a 3d wrist or stylus.
     * Data:    cVector3d type value.  Units are N*mm.
     */
    public static final int CHAI_CMD_SET_TORQUE_3D = 2030;

    /**
     * Purpose: Read status of user switch 0.
     * Data:    Integer type value.
     */
    public static final int CHAI_CMD_GET_SWITCH_0 = 2041;

    /**
     * Purpose: Read status of user switch 1.
     * Data:    Integer type value.
     */
    public static final int CHAI_CMD_GET_SWITCH_1 = 2042;

    /**
     * Purpose: Read status of user switch 2.
     * Data:    Integer type value.
     */
    public static final int CHAI_CMD_GET_SWITCH_2 = 2043;

    /**
     * Purpose: Reads all switches into a bit mask with bit 0 = button 0, etc.
     * Data:    Integer type value.
     */
    public static final int CHAI_CMD_GET_SWITCH_MASK = 2044;

    /**
     * Purpose: Get the scale factor from normalized coordinates to mm
     * Data:    double scale factor... mm = scale * normalized_coords
     */
    public static final int CHAI_CMD_GET_NORMALIZED_SCALE_FACTOR = 2045;

    /**
     * The following constants define the possible return values
     * of the method JGenericDevice:: command()
     */

    /**
     * Error message - no error occurred.
     */
    public static final int CHAI_MSG_OK = 0;

    /**
     * Error message - and error has occurred
     */
    public static final int CHAI_MSG_ERROR = -1;

    /**
     * Error message - this command is not supported
     */
    public static final int CHAI_MSG_NOT_IMPLEMENTED = -2;

    /**
     * Error message - system is not ready
     */
    public static final int CHAI_MSG_SYSTEM_NOT_READY = -3;

    /**
     * Flag that indicates if the hardware device is available to the computer.
     */
    protected boolean mSystemAvailable;

    /**
     * Flag that indicates if connection to system was opened successfully.
     */
    protected boolean mSystemReady;

    /**
     * A callback method for this device (or zero if none has been registered).
     */
    protected JCallback mCallback;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    public JGenericDevice() {
        // the device is not yet available
        mSystemAvailable = false;

        // the system is not yet ready to receive commands
        mSystemReady = false;

        // No call back has been defined
        mCallback = null;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    
    /**
     * Ask the device to call me back periodically.  If this device supports
     * timed callbacks, this function will return 'true' and will call the
     * supplied m_callback method at haptic rates.  If not, this function will
     * return 'false', and you should create your own haptic thread.
     * @param aCallback - The callback to trigger periodically, or 0 to cancel
     *                   an existing callback.
     * @return - Returns true if this device supports callbacks, false
     *       otherwise.
     */
    public boolean setCallback(JCallback aCallback) {
        this.mCallback = aCallback;
        return (true);
    }

    /**
     * Open connection to device (0 indicates success).
     * @return 
     */
    public int open() { return -1; }

    /**
     * Close connection to device (0 indicates success).
     * @return 
     */
    public int close() { return -1; }

    /**
     * Initialize or calibrate device (0 indicates success).
     * @return 
     */
    public int initialize(final boolean aResetEncoders) { return -1; }

    /**
     * Send a command to the device (0 indicates success).
     * @param aCommand
     * @param aData
     * @return 
     */
    public int command(int aCommand, Object aData) { return (CHAI_MSG_NOT_IMPLEMENTED); }

    /**
     * Returns the number of devices available from this class of device.
     * @return 
     */
    public int getNumDevices() { return (0); }

    /**
     * Returns true if the device is available for communication.
     * @return 
     */
    public boolean isSystemAvailable() { return (this.mSystemAvailable); }

    /**
     * Returns true if the connection to the device has been created.
     * @return 
     */
    boolean isSystemReady() { return (this.mSystemReady); }

}
