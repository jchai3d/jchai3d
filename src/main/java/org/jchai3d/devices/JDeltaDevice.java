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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;
import org.jchai3d.timers.JPrecisionClock;

/**
 * Describes an interface to  the omega.x and delta.x
 * @author jairo
 */
public class JDeltaDevice extends JGenericHapticDevice {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    /**
     * Reference count used to control access to the dhd dll.
     */
    protected static int mActiveDeltaDevices = 0;
    /**
     * Device ID number.
     */
    protected int mDeviceID;
    /**
     * Which FD device is actually instantiated here?
     */
    protected int mDeviceType;
    /** 
     * Structure for modeling a low-pass filter user switches.
     */
    protected int[] mUserSwitchCount = new int[8];
    /**
     * Last state of user switch.
     */
    protected int[] mUserSwitchStatus = new int[8];
    /** 
     * Timeguard for user switch.
     */
    protected JPrecisionClock[] mUserSwitchClock = new JPrecisionClock[8];
    /** 
     * Have forces been enable yet since the connection to the device was opened?
     */
    protected boolean statusEnableForcesFirstTime;
    /** 
     * Delta device communication
     */
    protected DeltaDeviceLibrary dhd = null;
    /** 
     * Status of DHD API calls.
     */
    static boolean sdhdGetDeviceCount = true;
    static boolean sdhdGetDeviceID = true;
    static boolean sdhdGetSystemType = true;
    static boolean sdhdOpenID = true;
    static boolean sdhdClose = true;
    static boolean sdhdGetButton = true;
    static boolean sdhdReset = true;
    static boolean sdhdGetPosition = true;
    static boolean sdhdGetLinearVelocity = true;
    static boolean sdhdSetForce = true;
    static boolean sdhdGetOrientationRad = true;
    static boolean sdhdSetTorque = true;
    static boolean sdhdGetOrientationFrame = true;
    static boolean sdhdSetForceAndGripperForce = true;
    static boolean sdhdGetGripperThumbPos = true;
    static boolean sdhdGetGripperFingerPos = true;
    static boolean sdhdEnableExpertMode = true;
    static boolean sdhdDisableExpertMode = true;
    static boolean sdhdEnableForce = true;
    static boolean sdhdIsLeftHanded = true;
    /**
     * Devices 
     */
    private final int DHD_DEVICE_NONE = 0;
    private final int DHD_DEVICE_SIMULATOR = 11;
    private final int DHD_DEVICE_3DOF = 31;
    private final int DHD_DEVICE_6DOF = 61;
    private final int DHD_DEVICE_6DOF_500 = 62;
    private final int DHD_DEVICE_OMEGA = 32;
    private final int DHD_DEVICE_OMEGA3 = 33;
    private final int DHD_DEVICE_OMEGA33 = 34;
    private final int DHD_DEVICE_OMEGA33_LEFT = 36;
    private final int DHD_DEVICE_OMEGA331 = 35;
    private final int DHD_DEVICE_OMEGA331_LEFT = 37;
    private final int DHD_DEVICE_FALCON = 60;
    private final int DHD_DEVICE_CONTROLLER = 81;
    private final int DHD_DEVICE_CONTROLLER_HR = 82;
    private final int DHD_DEVICE_CUSTOM = 91;
    /**
     * Status 
     */
    private final int DHD_ON = 1;
    private final int DHD_OFF = 0;
    /** 
     * Device count 
     */
    private final int DHD_MAX_DEVICE = 4;
    /** 
     * TimeGuard return value 
     */
    private final int DHD_TIMEGUARD = 1;
    /**
     * Status count 
     */
    private final int DHD_MAX_STATUS = 13;
    /** 
     * Status codes 
     */
    private final int DHD_STATUS_POWER = 0;
    private final int DHD_STATUS_CONNECTED = 1;
    private final int DHD_STATUS_STARTED = 2;
    private final int DHD_STATUS_RESET = 3;
    private final int DHD_STATUS_IDLE = 4;
    private final int DHD_STATUS_FORCE = 5;
    private final int DHD_STATUS_BRAKE = 6;
    private final int DHD_STATUS_TORQUE = 7;
    private final int DHD_STATUS_WRIST_DETECTED = 8;
    private final int DHD_STATUS_ERROR = 9;
    private final int DHD_STATUS_GRAVITY = 10;
    private final int DHD_STATUS_TIMEGUARD = 11;
    private final int DHD_STATUS_ROTATOR_RESET = 12;
    /** 
     * Buttons count 
     */
    private final int DHD_MAX_BUTTONS = 8;
    /** 
     * Velocity estimator computation mode 
     */
    private final int DHD_VELOCITY_WINDOWING = 0;
    private final int DHD_VELOCITY_AVERAGING = 1;

    //-----------------------------------------------------------------------
    // CONSTRUCTORS:
    //-----------------------------------------------------------------------
    public JDeltaDevice() {
        this(0);
    }

    public JDeltaDevice(int aDeviceNumber) {
        // init variables
        statusEnableForcesFirstTime = true;

        // name of the device manufacturer
        mSpecifications.mManufacturerName = "Force Dimension";

        // device is not yet available or ready
        mSystemAvailable = false;
        mSystemReady = false;
        mDeviceType = -1;
        mActiveDeltaDevices++;

        // load dhd.dll library
        System.out.println("Loading 'dhd' library");
        try {
            //TODO para interfaces hápticas da force dimension
            // temos que decidir quando chamar o modulo
            // dhd ou dhd64
            dhd =
                    (DeltaDeviceLibrary) Native.loadLibrary(
                    "dhd64",
                    DeltaDeviceLibrary.class);

            // check if DLL loaded correctly

        } catch (Throwable e) {
            System.out.print(" >" + e.getMessage());
            return;
        }



        // get the number ID of the device we wish to communicate with
        mDeviceID = aDeviceNumber;

        // get the number of Force Dimension devices connected to this computer
        int numDevices = 0;
        try {
            numDevices = dhd.dhdGetDeviceCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if such device is available
        if ((aDeviceNumber + 1) > numDevices) {
            // no, such ID does not lead to an existing device
            mSystemAvailable = false;
        } else {
            // yes, this ID leads to an existing device
            mSystemAvailable = true;

            // open the device to read all the technical specifications about it
            open();

            // close the device
            close();
        }

        //set data
        for (int i = 0; i < mUserSwitchClock.length; i++) {
            mUserSwitchClock[i] = new JPrecisionClock();
        }

        // init code to handle buttons
        int i = 0;
        for (i = 0; i < 8; i++) {
            mUserSwitchCount[i] = 0;
            mUserSwitchStatus[i] = 0;
            mUserSwitchClock[i].reset();
            mUserSwitchClock[i].start();
        }
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Open connection to the omega.x or delta.x device.
     * @return - Returns 0 if the device is already opened
     */
    @Override
    public int open() {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // if system is already opened then return
        if (mSystemReady) {
            return (0);
        }

        // check if DHD-API call is available
        if (!sdhdOpenID) {
            return (-1);
        }

        // try to open the device
        int result = -1;
        try {
            result = dhd.dhdOpenID((char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // update device status
        if (result >= 0) {
            mSystemReady = true;
        } else {
            mSystemReady = false;
            return (-1);
        }

        // init force status
        statusEnableForcesFirstTime = true;

        // read the device type
        mDeviceType = DHD_DEVICE_OMEGA;
        if (sdhdGetSystemType) {
            try {
                mDeviceType = dhd.dhdGetSystemType((char) mDeviceID);
            } catch (Exception e) {
                mDeviceType = DHD_DEVICE_NONE;
                e.printStackTrace();
            }
        }

        // left/right hand
        boolean leftHandOnly = false;
        if (sdhdIsLeftHanded) {
            leftHandOnly = dhd.dhdIsLeftHanded((char) mDeviceID);
        }

        // default information
        mSpecifications.mModelName = "omega";
        mSpecifications.mMaxForce = 12.0;    // [N]
        mSpecifications.mMaxForceStiffness = 5000.0;  // [N/m]
        mSpecifications.mMaxTorque = 0.0;     // [N*m]
        mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
        mSpecifications.mMaxGripperTorque = 0.0;     // [N]
        mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
        mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
        mSpecifications.mWorkspaceRadius = 0.075;   // [m]
        mSpecifications.mSensedPosition = true;
        mSpecifications.mSensedRotation = false;
        mSpecifications.mSensedGripper = false;
        mSpecifications.mActuatedPosition = true;
        mSpecifications.mActuatedRotation = false;
        mSpecifications.mActuatedGripper = false;
        mSpecifications.mLeftHand = true;
        mSpecifications.mRightHand = true;

        // setup information regarding device
        switch (mDeviceType) {
            //------------------------------------------------------------------
            // delta devices
            //------------------------------------------------------------------
            case (DHD_DEVICE_3DOF): {
                mSpecifications.mModelName = "delta.3";
                mSpecifications.mMaxForce = 20.0;    // [N]
                mSpecifications.mMaxForceStiffness = 5000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.0;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 0.0;     // [N]
                mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
                mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
                mSpecifications.mWorkspaceRadius = 0.15;    // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = false;
                mSpecifications.mSensedGripper = false;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = false;
                mSpecifications.mActuatedGripper = false;
                mSpecifications.mLeftHand = true;
                mSpecifications.mRightHand = true;
            }
            break;

            case (DHD_DEVICE_6DOF):
            case (DHD_DEVICE_6DOF_500): {
                mSpecifications.mModelName = "delta.6";
                mSpecifications.mMaxForce = 20.0;    // [N]
                mSpecifications.mMaxForceStiffness = 4000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.2;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 1.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 0.0;     // [N]
                mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
                mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
                mSpecifications.mWorkspaceRadius = 0.15;    // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = true;
                mSpecifications.mSensedGripper = false;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = true;
                mSpecifications.mActuatedGripper = false;
                mSpecifications.mLeftHand = true;
                mSpecifications.mRightHand = true;
            }
            break;

            //------------------------------------------------------------------
            // omega devices
            //------------------------------------------------------------------
            case (DHD_DEVICE_OMEGA):
            case (DHD_DEVICE_OMEGA3): {
                mSpecifications.mModelName = "omega.3";
                mSpecifications.mMaxForce = 12.0;    // [N]
                mSpecifications.mMaxForceStiffness = 5000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.0;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 0.0;     // [N]
                mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
                mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
                mSpecifications.mWorkspaceRadius = 0.075;   // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = false;
                mSpecifications.mSensedGripper = false;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = false;
                mSpecifications.mActuatedGripper = false;
                mSpecifications.mLeftHand = true;
                mSpecifications.mRightHand = true;
            }
            break;

            case (DHD_DEVICE_OMEGA33):
            case (DHD_DEVICE_OMEGA33_LEFT): {
                mSpecifications.mModelName = "omega.6";
                mSpecifications.mMaxForce = 12.0;    // [N]
                mSpecifications.mMaxForceStiffness = 4000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.0;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 0.0;     // [N]
                mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
                mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
                mSpecifications.mWorkspaceRadius = 0.075;   // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = true;
                mSpecifications.mSensedGripper = false;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = false;
                mSpecifications.mActuatedGripper = false;
                mSpecifications.mLeftHand = leftHandOnly;
                mSpecifications.mRightHand = !leftHandOnly;
            }
            break;

            case (DHD_DEVICE_OMEGA331):
            case (DHD_DEVICE_OMEGA331_LEFT): {
                mSpecifications.mModelName = "omega.7";
                mSpecifications.mMaxForce = 12.0;    // [N]
                mSpecifications.mMaxForceStiffness = 4000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.0;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 8.0;     // [N]
                mSpecifications.mMaxGripperTorqueStiffness = 10.0;    // [N*m/m]
                mSpecifications.mMaxLinearDamping = 40.0;    // [N/(m/s)]
                mSpecifications.mWorkspaceRadius = 0.075;   // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = true;
                mSpecifications.mSensedGripper = true;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = false;
                mSpecifications.mActuatedGripper = true;
                mSpecifications.mLeftHand = leftHandOnly;
                mSpecifications.mRightHand = !leftHandOnly;
            }
            break;

            //------------------------------------------------------------------
            // falcon device
            //------------------------------------------------------------------
            case (DHD_DEVICE_FALCON): {
                mSpecifications.mManufacturerName = "Novint Technologies";
                mSpecifications.mModelName = "Falcon";
                mSpecifications.mMaxForce = 8.0;     // [N]
                mSpecifications.mMaxForceStiffness = 3000.0;  // [N/m]
                mSpecifications.mMaxTorque = 0.0;     // [N*m]
                mSpecifications.mMaxTorqueStiffness = 0.0;     // [N*m/Rad]
                mSpecifications.mMaxGripperTorque = 0.0;     // [N]
                mSpecifications.mMaxLinearDamping = 20.0;    // [N/(m/s)]
                mSpecifications.mMaxGripperTorqueStiffness = 0.0;     // [N*m/m]
                mSpecifications.mWorkspaceRadius = 0.04;    // [m]
                mSpecifications.mSensedPosition = true;
                mSpecifications.mSensedRotation = false;
                mSpecifications.mSensedGripper = false;
                mSpecifications.mActuatedPosition = true;
                mSpecifications.mActuatedRotation = false;
                mSpecifications.mActuatedGripper = false;
                mSpecifications.mLeftHand = true;
                mSpecifications.mRightHand = true;
            }
        }

        return (result);
    }

    /**
     * Close connection to the omega.x or delta.x device.
     */
    @Override
    public int close() {
        // check if the system has been opened previously
        if (!mSystemReady) {
            return (-1);
        }

        // check if DHD-API call is available
        if (!sdhdClose) {
            return (-1);
        }

        // yes, the device is open so let's close it
        int result = -1;
        try {
            result = dhd.dhdClose((char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // update status
        mSystemReady = false;

        // reset force status
        statusEnableForcesFirstTime = true;

        return (result);
    }

    /**
     * Calibrate the omega.x or delta.x device.
     */
    public int initialize() {
        return initialize(false);
    }

    /**
     * Calibrate the omega.x or delta.x device.
     * This function does nothing right now; the aResetEncoders parameter is ignored.
     * @param  aResetEncoders - Ignored; exists for forward compatibility.
     * @return - Always 0
     */
    @Override
    public int initialize(final boolean aResetEncoders) {
        
        return (0);
    }

    /**
     * Returns the number of devices available from this class of device.
     * @return - Returns the result
     */
    @Override
    public int getNumDevices() {
        // check if the system is available
        if (!mSystemAvailable) {
            return (0);
        }

        // check if DHD-API call is available
        if (!sdhdGetDeviceCount) {
            return (1);
        }

        int result = -1;
        try {
            result = dhd.dhdGetDeviceCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (result);
    }

    /**
     * Read the position of the device. Units are meters [m].
     * @param aPosition - Return value.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int getPosition(JVector3d aPosition) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // check if DHD-API call is available
        if (!sdhdGetPosition) {
            return (-1);
        }

        int error = -1;

        DoubleByReference x = new DoubleByReference(),
                y = new DoubleByReference(),
                z = new DoubleByReference();

        try {
            error = dhd.dhdGetPosition(x, y, z, (char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        aPosition.set(x.getValue(), y.getValue(), z.getValue());
        estimateLinearVelocity(aPosition);
        return (error);
    }

    /**
     * Read the linear velocity of the device. Units are in [m/s].
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int getLinearVelocity(JVector3d aLinearVelocity) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        int error = -1;

        DoubleByReference vx = new DoubleByReference(),
                vy = new DoubleByReference(),
                vz = new DoubleByReference();

        try {
            error = dhd.dhdGetLinearVelocity(vx, vy, vz, (char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLinearVelocity.set(vx.getValue(), vy.getValue(), vz.getValue());

        aLinearVelocity.copyFrom(mLinearVelocity);

        return (error);
    }

    /**
     * Read the orientation frame of the device end-effector
     * @param aRotation - Return value.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int getRotation(JMatrix3d aRotation) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        int error = 0;
        JMatrix3d frame = new JMatrix3d();
        frame.identity();

        switch (mDeviceType) {
            // delta devices
            case (DHD_DEVICE_3DOF):
            case (DHD_DEVICE_6DOF):
            case (DHD_DEVICE_6DOF_500):
                // read angles
                JVector3d angles = new JVector3d(0, 0, 0);

                // check if DHD-API call is available
                if (sdhdGetOrientationRad) {
                    DoubleByReference x = new DoubleByReference(),
                            y = new DoubleByReference(),
                            z = new DoubleByReference();

                    try {
                        error = dhd.dhdGetOrientationRad(x, y, z, (char) mDeviceID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    angles.set(x.getValue(), y.getValue(), z.getValue());
                }

                // compute rotation matrix
                angles.mul(1.5);
                frame.rotate(new JVector3d(1, 0, 0), angles.getX());
                frame.rotate(new JVector3d(0, 1, 0), angles.getY());
                frame.rotate(new JVector3d(0, 0, 1), angles.getZ());

                break;

            // omega devices
            case (DHD_DEVICE_OMEGA):
            case (DHD_DEVICE_OMEGA3):
            case (DHD_DEVICE_OMEGA33):
            case (DHD_DEVICE_OMEGA331): {
                // read rotation matrix
                double[] rot = new double[9];
                rot[0] = 1.0;
                rot[1] = 0.0;
                rot[2] = 0.0;
                rot[3] = 0.0;
                rot[4] = 1.0;
                rot[5] = 0.0;
                rot[6] = 0.0;
                rot[7] = 0.0;
                rot[8] = 1.0;

                if (sdhdGetOrientationFrame) {
                    try {
                        error = dhd.dhdGetOrientationFrame(rot, (char) mDeviceID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //JMatrix3d result;
                frame.m[0][0] = rot[0];
                frame.m[0][1] = rot[1];
                frame.m[0][2] = rot[2];
                frame.m[1][0] = rot[3];
                frame.m[1][1] = rot[4];
                frame.m[1][2] = rot[5];
                frame.m[2][0] = rot[6];
                frame.m[2][1] = rot[7];
                frame.m[2][2] = rot[8];
            }
            break;
        }

        // return result
        aRotation.copyFrom(frame);
        estimateAngularVelocity(aRotation);
        return (error);
    }

    /**
     * Read the gripper angle in radian.
     * @param aAngle - Return value.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int getGripperAngleRad(Double aAngle) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        int error = 0;
        aAngle = 0.0;
        estimateGripperVelocity(aAngle);
        return (error);
    }

    /**
     * Send a force [N] to the haptic device
     * @param aForce - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int setForce(JVector3d aForce) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // check if forces need to be enable (happens only once)
        if (statusEnableForcesFirstTime) {
            enableForces(true);
        }

        // check if DHD-API call is available
        if (!sdhdSetForce) {
            return (-1);
        }

        int error = -1;

        try {
            error = dhd.dhdSetForce(aForce.getX(), aForce.getY(), aForce.getZ(), (char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPrevForce(aForce);
        return (error);
    }

    /**
     * Send a torque [N*m] to the haptic device
     * @param aTorque - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int setTorque(JVector3d aTorque) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // check if forces need to be enable (happens only once)
        if (statusEnableForcesFirstTime) {
            enableForces(true);
        }

        // check if DHD-API call is available
        if (!sdhdSetTorque) {
            return (-1);
        }

        int error = -1;

        try {
            error = dhd.dhdSetTorque(aTorque.getX(), aTorque.getY(), aTorque.getZ(), (char) mDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPrevTorque(aTorque);
        return (error);
    }

    /**
     * Send a torque [N*m] to the gripper
     * @param aGripperTorque - Torque command to be sent to gripper.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int setGripperTorque(double aGripperTorque) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // check if forces need to be enable (happens only once)
        if (statusEnableForcesFirstTime) {
            enableForces(true);
        }

        int error = 0;
        setPrevGripperTorque(aGripperTorque);
        return (error);
    }

    /**
     * Send a force [N] and a torque [N*m] and gripper torque [N*m] to the haptic device.
     * @param aForce - Force command.
     * @param aTorque - Torque command.
     * @param aGripperTorque - Gripper torque command.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int setForceAndTorqueAndGripper(JVector3d aForce, JVector3d aTorque, double aGripperTorque) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        // check if forces need to be enable (happens only once)
        if (statusEnableForcesFirstTime) {
            enableForces(true);
        }

        // apply force and gripper torque

        try {
            if (!mSpecifications.mActuatedRotation) {
                if (sdhdSetForceAndGripperForce) {
                    dhd.dhdSetForceAndGripperForce(aForce.getX(), aForce.getY(), aForce.getZ(), aGripperTorque, (char) mDeviceID);
                } else if (sdhdSetForce) {
                    dhd.dhdSetForce(aForce.getX(), aForce.getY(), aForce.getZ(), (char) mDeviceID);
                }
            } else if ((sdhdSetForce) && (sdhdSetTorque) && (mSpecifications.mActuatedRotation)) {
                dhd.dhdSetForce(aForce.getX(), aForce.getY(), aForce.getZ(), (char) mDeviceID);
                dhd.dhdSetTorque(aTorque.getX(), aTorque.getY(), aTorque.getZ(), (char) mDeviceID);
            } else if (sdhdSetForce) {
                dhd.dhdSetForce(aForce.getX(), aForce.getY(), aForce.getZ(), (char) mDeviceID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int error = 0;
        setPrevForce(aForce);
        setPrevTorque(aTorque);
        setPrevGripperTorque(aGripperTorque);
        return (error);
    }
    

    /**
     * Read the status of the user switch [1 = ON / 0 = OFF].
     * @param aSwitchIndex - index number of the switch.
     * @param aStatus - result value from reading the selected input switch.
     * @return - Return 0 if no error occurred.
     */
    @Override
    public int getUserSwitch(int aSwitchIndex, Boolean aStatus) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        int error = 0;
        if (getUserSwitch(mDeviceID) == 0) {
            aStatus = false;
        } else {
            aStatus = true;
        }
        return (error);
    }

    /**
     * Read the user switch of the end-effector.
     * This function implements a small filter to avoid reading glitches.
     * @param aDeviceID - device ID.
     * @return - Return 0 if no error occurred.
     */
    protected int getUserSwitch(int aDeviceID) {
        // check if the system is available
        if (!mSystemAvailable) {
            return (-1);
        }

        final long SWITCHTIMEOUT = 10000; // [us]
        final double SWITCHTIMEOUT_SECONDS = ((double) (SWITCHTIMEOUT)) / 1e6;

        // check device id.
        if ((aDeviceID < 0) || (aDeviceID > 7)) {
            return (0);
        }

        // check time
        if (mUserSwitchClock[aDeviceID].getCurrentTimeSeconds() < SWITCHTIMEOUT_SECONDS) {
            return (mUserSwitchStatus[aDeviceID]);
        } else {
            // check if DHD-API call is available
            if (!sdhdGetButton) {
                return (mUserSwitchStatus[aDeviceID]);
            }

            // timeout has occurred, we read the status again
            int switchStatus = 0;
            try {
                switchStatus = dhd.dhdGetButton(0, (char) aDeviceID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if value equals to zero, check once more time
            if (switchStatus == 1) {
                mUserSwitchStatus[aDeviceID] = 1;
                mUserSwitchCount[aDeviceID] = 0;
            } else {
                mUserSwitchCount[aDeviceID]++;
                if (mUserSwitchCount[aDeviceID] > 6) {
                    mUserSwitchStatus[aDeviceID] = 0;
                    mUserSwitchCount[aDeviceID] = 0;
                }
            }

            mUserSwitchClock[aDeviceID].reset();
            mUserSwitchClock[aDeviceID].start();

            return (mUserSwitchStatus[aDeviceID]);
        }
    }

    //-----------------------------------------------------------------------
    // METHODS RESTRICTED TO FORCE DIMENSION DEVICES ONLY:
    //-----------------------------------------------------------------------
    /**
     * Return the Force Dimension type of the current device.
     * @return - Return the value.
     */
    public int getDeviceType() {
        return mDeviceType;
    }

    /**
     * Enable/Disable the motors of the omega.x device.
     * This function overrides the force button located at the base of the
     * device or on the controller panel.
     * @param aValue - force status.
     * @return - Return 0 if no error occurred.
     */
    public int enableForces(boolean aValue) {
        // check if DHD-API call is available
        if (!sdhdEnableExpertMode) {
            return (-1);
        }
        if (!sdhdEnableForce) {
            return (-1);
        }
        if (!sdhdDisableExpertMode) {
            return (-1);
        }

        try {
            if (aValue) {
                // enable forces
                dhd.dhdEnableExpertMode();
                dhd.dhdEnableForce((char) DHD_ON, (char) mDeviceID);
                dhd.dhdDisableExpertMode();
                statusEnableForcesFirstTime = false;
            } else {
                // disable forces
                dhd.dhdEnableExpertMode();
                dhd.dhdEnableForce((char) DHD_OFF, (char) mDeviceID);
                dhd.dhdDisableExpertMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (0);
    }

    interface DeltaDeviceLibrary extends Library {

        public int dhdGetDeviceCount();

        public int dhdGetDeviceID();

        public int dhdGetSystemType(char ID);

        public int dhdOpenID(char ID);

        public int dhdClose(char ID);

        public int dhdGetButton(int index,
                char ID);

        public int dhdReset(char ID);

        public int dhdGetPosition(DoubleByReference px,
                DoubleByReference py,
                DoubleByReference pz,
                char ID);

        public int dhdGetLinearVelocity(DoubleByReference vx,
                DoubleByReference vy,
                DoubleByReference vz,
                char ID);

        public int dhdSetForce(double fx,
                double fy,
                double fz,
                char ID);

        public int dhdGetOrientationRad(DoubleByReference oa,
                DoubleByReference ob,
                DoubleByReference og,
                char ID);

        public int dhdSetTorque(double ta,
                double tb,
                double tg,
                char ID);

        public int dhdGetOrientationFrame(double matrix[],
                char ID);

        public int dhdSetForceAndGripperForce(double fx,
                double fy,
                double fz,
                double f,
                char ID);

        public int dhdGetGripperThumbPos(DoubleByReference tx,
                DoubleByReference ty,
                DoubleByReference tz,
                char ID);

        public int dhdGetGripperFingerPos(DoubleByReference fx,
                DoubleByReference fy,
                DoubleByReference fz,
                char ID);

        public int dhdEnableExpertMode();

        public int dhdDisableExpertMode();

        public int dhdEnableForce(char val,
                char ID);

        public boolean dhdIsLeftHanded(char ID);
        
        public int dhdSetGravityCompensation(int val, char ID);
    }
}
