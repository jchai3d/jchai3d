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

import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;
import org.jchai3d.timers.JPrecisionClock;

/**
 * JGenericHapticDevice describes a virtual class from which all
 *   2D or 3D point contact haptic devices are derived. These include
 *   for instance the delta.x, omega.x or Phantom haptic devices.
 * @author jairo
 */
public class JGenericHapticDevice extends JGenericDevice {
    /**
     * General clock when the device was started
     */
    protected JHapticDeviceInfo mSpecifications;
    protected JVector3d mPrevForce;
    protected JVector3d mPrevTorque;
    protected double mPrevGripperTorque;
    protected JVector3d mLinearVelocity;
    protected JVector3d mAngularVelocity;
    protected double mGripperVelocity;
    protected JTimestampPos[] mHistoryPos = new JTimestampPos[CHAI_DEVICE_HISTORY_SIZE];
    protected JTimestampRot[] mHistoryRot = new JTimestampRot[CHAI_DEVICE_HISTORY_SIZE];
    protected JTimestampValue[] mHistoryGripper = new JTimestampValue[CHAI_DEVICE_HISTORY_SIZE];
    protected int mIndexHistoryPos;
    protected int mIndexHistoryRot;
    protected int mIndexHistoryGripper;
    protected int mIndexHistoryPosWin;
    protected int mIndexHistoryRotWin;
    protected int mIndexHistoryGripperWin;
    protected double mLinearVelocityWindowSize;
    protected double mAngularVelocityWindowSize;
    protected double mGripperVelocityWindowSize;
    protected JPrecisionClock mClockGeneral;
    /**
     * Filter property used for velocity estimator
     */
    public static final int CHAI_DEVICE_HISTORY_SIZE = 200;
    /**
     * Minimum time between two devioce status acquisitions
     */
    public static double CHAI_DEVICE_MIN_ACQUISITION_TIME = 0.0001;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    /**
     * Constructor of JGenericHapticDevice.
     * Initialize basic parameters of generic haptic device.
     */
    public JGenericHapticDevice() {
        mSpecifications = new JHapticDeviceInfo();
        mSpecifications.mManufacturerName = "not defined";
        mSpecifications.mModelName = "not defined";
        mSpecifications.mMaxForce = 0.1; // [N]
        mSpecifications.mMaxForceStiffness = 0.1; // [N/m]
        mSpecifications.mMaxTorque = 0.1; // [N*m]
        mSpecifications.mMaxTorqueStiffness = 0.1; // [N*m/Rad]
        mSpecifications.mMaxGripperTorque = 0.1; // [N]
        mSpecifications.mMaxGripperTorqueStiffness = 0.1; // [N*m/m]
        mSpecifications.mMaxLinearDamping = 0.0; // [N/(m/s)]
        mSpecifications.mWorkspaceRadius = 0.1; // [m]
        mSpecifications.mSensedPosition = false;
        mSpecifications.mSensedRotation = false;
        mSpecifications.mSensedGripper = false;
        mSpecifications.mActuatedPosition = false;
        mSpecifications.mActuatedRotation = false;
        mSpecifications.mActuatedGripper = false;
        mSpecifications.mLeftHand = false;
        mSpecifications.mRightHand = false;

        mPrevForce = new JVector3d();
        mPrevForce.zero();
        mPrevTorque = new JVector3d();
        mPrevTorque.zero();
        mPrevGripperTorque = 0.0;

        mAngularVelocity = new JVector3d();
        mAngularVelocity.zero();
        mGripperVelocity = 0.0;

        // start the general clock of the device
        mClockGeneral = new JPrecisionClock();
        mClockGeneral.reset();
        mClockGeneral.start();

        // reset history tables
        double time = mClockGeneral.getCurrentTimeSeconds();

        mIndexHistoryPos = 0;
        mIndexHistoryPosWin = CHAI_DEVICE_HISTORY_SIZE - 1;

        //create data
        for(int i = 0; i  < mHistoryPos.length; i++)
            mHistoryPos[i] = new JTimestampPos();
        
        for (int i = 0; i < CHAI_DEVICE_HISTORY_SIZE; i++) {
            mHistoryPos[i].position.zero();
            mHistoryPos[i].time = time;
        }

        mIndexHistoryRot = 0;
        mIndexHistoryPosWin = CHAI_DEVICE_HISTORY_SIZE - 1;

        //create data
        for(int i = 0; i  < mHistoryRot.length; i++)
            mHistoryRot[i] = new JTimestampRot();

        for (int i = 0; i < CHAI_DEVICE_HISTORY_SIZE; i++) {
            mHistoryRot[i].rotation.identity();
            mHistoryRot[i].time = time;
        }

        mIndexHistoryGripper = 0;
        mIndexHistoryPosWin = CHAI_DEVICE_HISTORY_SIZE - 1;

        //create data
        for(int i = 0; i  < mHistoryGripper.length; i++)
            mHistoryGripper[i] = new JTimestampValue();

        for (int i = 0; i < CHAI_DEVICE_HISTORY_SIZE; i++) {
            mHistoryGripper[i].value = 0.0;
            mHistoryGripper[i].time = time;
        }

        // Window time interval for measuring linear velocity
        mLinearVelocityWindowSize = 0.015; // [s]

        // Window time interval for measuring angular velocity
        mAngularVelocityWindowSize = 0.015; // [s]

        // Window time interval for measuring gripper velocity
        mGripperVelocityWindowSize = 0.015; // [s]

        mLinearVelocity = new JVector3d();
    }

    //-----------------------------------------------------------------------
    // METHODS - GENERAL COMMANDS:
    //-----------------------------------------------------------------------
    /**
     * Open connection to haptic device (0 indicates success)
     * @return
     */
    @Override
    public int open() {
        return -1;
    }

    /**
     * Close connection to haptic device (0 indicates success)
     * @return
     */
    @Override
    public int close() {
        return -1;
    }

    /**
     * Initialize or calibrate haptic device (0 indicates success)
     * @param aResetEncoders
     * @return
     */
    @Override
    public int initialize(final boolean aResetEncoders) {
        return -1;
    }

    /**
     * Set command for the haptic device
     * @param aCommand - Selected command.
     * @param aData - Object that contains the corresponding data structure
     * @return - Return status of command.
     */
    @Override
    public int command(int aCommand, Object aData) {
        // temp variables
        int result;

        // check if the device is open
        if (mSystemReady) {
            switch (aCommand) {
                // read position of end-effector
                case CHAI_CMD_GET_POS_3D: {
                    JVector3d temp = new JVector3d();
                    result = getPosition(temp);
                    JVector3d position = (JVector3d) aData;
                    position = temp;
                }
                break;

                // read normalized position of end-effector
                case CHAI_CMD_GET_POS_NORM_3D: {
                    JVector3d temp = new JVector3d();
                    result = getPosition(temp);
                    temp.div(getSpecifications().mWorkspaceRadius);
                    JVector3d position = (JVector3d) aData;
                    position = temp;
                }

                // read orientation of end-effector
                case CHAI_CMD_GET_ROT_MATRIX: {
                    JMatrix3d temp = new JMatrix3d();
                    result = getRotation(temp);
                    JMatrix3d rotation = (JMatrix3d) aData;
                    rotation = temp;
                }
                break;

                // set force to end-effector
                case CHAI_CMD_SET_FORCE_3D: {
                    JVector3d force = (JVector3d) aData;
                    result = setForce(force);
                }
                break;

                // set torque to end-effector
                case CHAI_CMD_SET_TORQUE_3D: {
                    JVector3d torque = (JVector3d) aData;
                    result = setTorque(torque);
                }
                break;

                // read user switch from end-effector
                case CHAI_CMD_GET_SWITCH_0: {
                    Boolean status = (Boolean) aData;
                    Boolean temp = false;
                    result = getUserSwitch(0, temp);
                    status = temp;
                }
                break;

                // read user switch from end-effector
                case CHAI_CMD_GET_SWITCH_MASK: {
                    // Force the result to be 0 or 1, since bit 0 should carry button 0's value
                    Boolean status = (Boolean) aData;
                    Boolean temp = false;
                    result = getUserSwitch(0, temp);
                    status = temp;
                }
                break;

                // function is not implemented
                default:
                    result = CHAI_MSG_NOT_IMPLEMENTED;
            }
        } else {
            result = CHAI_MSG_SYSTEM_NOT_READY;
        }
        return (result);
    }

    /**
     * Read the position of the device. Units are meters [m]
     * @param aPosition
     * @return
     */
    public int getPosition(JVector3d aPosition) {
        aPosition.zero();
        return (0);
    }

    /**
     * Read the linear velocity of the device. Units are meters per second [m/s]
     */
    public int getLinearVelocity(JVector3d aLinearVelocity) {
        aLinearVelocity.copyFrom(mLinearVelocity);
        return (0);
    }

    /*
     * Read the orientation frame of the device end-effector.
     */
    public int getRotation(JMatrix3d aRotation) {
        aRotation.identity();
        return (0);
    }

    /**
     * Read the angular velocity of the device. Units are in radians per second [m/s]
     * @param aAngularVelocity
     * @return
     */
    public int getAngularVelocity(JVector3d aAngularVelocity) {
        aAngularVelocity = getAngularVelocity();
        return (0);
    }

    /**
     * Read the gripper angle in radian.
     * @param aAngle
     * @return
     */
    public int getGripperAngleRad(Double aAngle) {
        aAngle = 0.0;
        return (0);
    }

    /**
     * Read the angular velocity of the gripper. Units are in radians per second [m/s]
     * @param aGripperVelocity
     * @return
     */
    public int getGripperVelocity(Double aGripperVelocity) {
        aGripperVelocity = getGripperVelocity();
        return (0);
    }

    /**
     * Send a force [N] to the haptic device
     * @param aForce
     * @return
     */
    public int setForce(JVector3d aForce) {
        return (0);
    }

    /**
     * Read a sensed force [N] from the haptic device.
     * @param aForce
     * @return
     */
    public int getForce(JVector3d aForce) {
        aForce = getPrevForce();
        return (0);
    }

    /**
     * Send a torque [N*m] to the haptic device.
     * @param aTorque
     * @return
     */
    public int setTorque(JVector3d aTorque) {
        return (0);
    }

    /**
     * Read a sensed torque [N*m] from the haptic device.
     * @param aTorque
     * @return
     */
    public int getTorque(JVector3d aTorque) {
        aTorque = getPrevTorque();
        return (0);
    }

    /**
     * Send a torque [N*m] to the gripper.
     * @param aGripperTorque
     * @return
     */
    public int setGripperTorque(double aGripperTorque) {
        return (0);
    }

    /**
     * Read a sensed torque [N*m] from the gripper.
     * @param aGripperTorque
     * @return
     */
    public int getGripperTorque(double aGripperTorque) {
        aGripperTorque = getPrevGripperTorque();
        return (0);
    }

    /**
     * Send a force [N], a torque [N*m] and a gripper torque [N*m] to the haptic device.
     * @param aForce - Force command.
     * @param aTorque - Torque command.
     * @param aGripperTorque - Gripper torque command.
     * @return - Return 0 if no error occurred.
     */
    public int setForceAndTorqueAndGripper(JVector3d aForce, JVector3d aTorque, double aGripperTorque) {
        int error0, error1, error2;

        // send force command
        error0 = setForce(aForce);

        // send torque command
        error1 = setTorque(aForce);

        // send gripper command
        error2 = setGripperTorque(aGripperTorque);

        // return status
        if ((error0 != 0) || (error1 != 0) || (error2 != 0)) {
            // an error has occurred
            return (-1);
        } else {
            // success
            return (0);
        }
    }

    /**
     * Read the status of the user switch [\b true = \b ON / \b false = \b OFF].
     * @param aSwitchIndex
     * @param aStatus
     * @return
     */
    public int getUserSwitch(int aSwitchIndex, Boolean aStatus) {
        aStatus = false;
        return (0);
    }

    /**
     * Get the specifications of the current device.
     * @return
     */
    public JHapticDeviceInfo getSpecifications() {
        return (mSpecifications);
    }

    /**
     * Estimate the linear velocity by passing the latest position.
     * @param aNewPosition - New position of the device.
     * @return - Return 0 if no error occurred.
     */
    protected void estimateLinearVelocity(JVector3d aNewPosition) {
        // get current time
        double time = mClockGeneral.getCurrentTimeSeconds();

        // check the time interval between the current and previous sample
        if ((time - mHistoryPos[mIndexHistoryPos].time) < CHAI_DEVICE_MIN_ACQUISITION_TIME) {
            return;
        }

        // store new value
        mIndexHistoryPos = (mIndexHistoryPos + 1) % CHAI_DEVICE_HISTORY_SIZE;
        mHistoryPos[mIndexHistoryPos].time = time;
        mHistoryPos[mIndexHistoryPos].position.copyFrom(aNewPosition);

        // search table to find a sample that occurred before current time
        // minus time window interval
        int i = 0;
        boolean completed = false;
        while ((i < CHAI_DEVICE_HISTORY_SIZE) && (!completed)) {
            double interval = time - mHistoryPos[mIndexHistoryPosWin].time;
            if ((interval < mLinearVelocityWindowSize) || (i == (CHAI_DEVICE_HISTORY_SIZE - 1))) {
                // compute result
                JVector3d result = new JVector3d();
                mHistoryPos[mIndexHistoryPos].position.subr(mHistoryPos[mIndexHistoryPosWin].position, result);
                if (interval > 0) {
                    result.divr(interval, mLinearVelocity);
                    completed = true;
                } else {
                    completed = true;
                }
            } else {
                mIndexHistoryPosWin = (mIndexHistoryPosWin + 1) % CHAI_DEVICE_HISTORY_SIZE;
            }
        }
    }

    /**
     * Estimate the angular velocity by passing the latest orientation frame.
     * @param aNewRotation - New orientation frame of the device.
     */
    protected void estimateAngularVelocity(JMatrix3d aNewRotation) {
        // TODO: TO BE COMPLETED!
        getAngularVelocity().zero();
    }

    /**
     * Estimate the velocity of the gripper by passing the latest gripper position.
     * @param aNewGripperPosition - New position of the gripper.
     */
    protected void estimateGripperVelocity(double aNewGripperPosition) {
        // get current time
        double time = mClockGeneral.getCurrentTimeSeconds();

        // check the time interval between the current and previous sample
        if ((time - mHistoryGripper[mIndexHistoryGripper].time) < CHAI_DEVICE_MIN_ACQUISITION_TIME) {
            return;
        }

        // store new value
        mIndexHistoryGripper = (mIndexHistoryGripper + 1) % CHAI_DEVICE_HISTORY_SIZE;
        mHistoryGripper[mIndexHistoryGripper].time = time;
        mHistoryGripper[mIndexHistoryGripper].value = aNewGripperPosition;

        // search table to find a sample that occurred before current time
        // minus time window interval
        int i = 0;
        boolean completed = false;
        while ((i < CHAI_DEVICE_HISTORY_SIZE) && (!completed)) {
            double interval = time - mHistoryGripper[mIndexHistoryGripperWin].time;
            if ((interval < mGripperVelocityWindowSize) || (i == (CHAI_DEVICE_HISTORY_SIZE - 1))) {
                // compute result
                if (interval > 0) {
                    mGripperVelocity = (mHistoryGripper[mIndexHistoryGripper].value - mHistoryGripper[mIndexHistoryGripperWin].value) / interval;
                    completed = true;
                } else {
                    completed = true;
                }
            } else {
                mIndexHistoryGripperWin = ((mIndexHistoryGripperWin + 1) % CHAI_DEVICE_HISTORY_SIZE);
            }
        }
    }


    /**
     * @param mSpecifications the mSpecifications to set
     */
    public void setSpecifications(JHapticDeviceInfo mSpecifications) {
        this.mSpecifications = mSpecifications;
    }

    /**
     * @return the mPrevForce
     */
    public JVector3d getPrevForce() {
        return mPrevForce;
    }

    /**
     * @param mPrevForce the mPrevForce to set
     */
    public void setPrevForce(JVector3d mPrevForce) {
        this.mPrevForce = mPrevForce;
    }

    /**
     * @return the mPrevTorque
     */
    public JVector3d getPrevTorque() {
        return mPrevTorque;
    }

    /**
     * @param mPrevTorque the mPrevTorque to set
     */
    public void setPrevTorque(JVector3d mPrevTorque) {
        this.mPrevTorque = mPrevTorque;
    }

    /**
     * @return the mPrevGripperTorque
     */
    public double getPrevGripperTorque() {
        return mPrevGripperTorque;
    }

    /**
     * @param mPrevGripperTorque the mPrevGripperTorque to set
     */
    public void setPrevGripperTorque(double mPrevGripperTorque) {
        this.mPrevGripperTorque = mPrevGripperTorque;
    }

    /**
     * @return the mLinearVelocity
     */
    public JVector3d getLinearVelocity() {
        return mLinearVelocity;
    }

    /**
     * @param mLinearVelocity the mLinearVelocity to set
     */
    public void setLinearVelocity(JVector3d mLinearVelocity) {
        this.mLinearVelocity = mLinearVelocity;
    }

    /**
     * @return the mAngularVelocity
     */
    public JVector3d getAngularVelocity() {
        return mAngularVelocity;
    }

    /**
     * @param mAngularVelocity the mAngularVelocity to set
     */
    public void setAngularVelocity(JVector3d mAngularVelocity) {
        this.mAngularVelocity = mAngularVelocity;
    }

    /**
     * @return the mGripperVelocity
     */
    public double getGripperVelocity() {
        return mGripperVelocity;
    }

    /**
     * @param mGripperVelocity the mGripperVelocity to set
     */
    public void setGripperVelocity(double mGripperVelocity) {
        this.mGripperVelocity = mGripperVelocity;
    }

    /**
     * @return the mHistoryPos
     */
    public JTimestampPos[] getHistoryPos() {
        return mHistoryPos;
    }

    /**
     * @param mHistoryPos the mHistoryPos to set
     */
    public void setHistoryPos(JTimestampPos[] mHistoryPos) {
        this.mHistoryPos = mHistoryPos;
    }

    /**
     * @return the mHistoryRot
     */
    public JTimestampRot[] getHistoryRot() {
        return mHistoryRot;
    }

    /**
     * @param mHistoryRot the mHistoryRot to set
     */
    public void setHistoryRot(JTimestampRot[] mHistoryRot) {
        this.mHistoryRot = mHistoryRot;
    }

    /**
     * @return the mHistoryGripper
     */
    public JTimestampValue[] getHistoryGripper() {
        return mHistoryGripper;
    }

    /**
     * @param mHistoryGripper the mHistoryGripper to set
     */
    public void setHistoryGripper(JTimestampValue[] mHistoryGripper) {
        this.mHistoryGripper = mHistoryGripper;
    }

    /**
     * @return the mIndexHistoryPos
     */
    public int getIndexHistoryPos() {
        return mIndexHistoryPos;
    }

    /**
     * @param mIndexHistoryPos the mIndexHistoryPos to set
     */
    public void setIndexHistoryPos(int mIndexHistoryPos) {
        this.mIndexHistoryPos = mIndexHistoryPos;
    }

    /**
     * @return the mIndexHistoryRot
     */
    public int getIndexHistoryRot() {
        return mIndexHistoryRot;
    }

    /**
     * @param mIndexHistoryRot the mIndexHistoryRot to set
     */
    public void setIndexHistoryRot(int mIndexHistoryRot) {
        this.mIndexHistoryRot = mIndexHistoryRot;
    }

    /**
     * @return the mIndexHistoryGripper
     */
    public int getIndexHistoryGripper() {
        return mIndexHistoryGripper;
    }

    /**
     * @param mIndexHistoryGripper the mIndexHistoryGripper to set
     */
    public void setIndexHistoryGripper(int mIndexHistoryGripper) {
        this.mIndexHistoryGripper = mIndexHistoryGripper;
    }

    /**
     * @return the mIndexHistoryPosWin
     */
    public int getIndexHistoryPosWin() {
        return mIndexHistoryPosWin;
    }

    /**
     * @param mIndexHistoryPosWin the mIndexHistoryPosWin to set
     */
    public void setIndexHistoryPosWin(int mIndexHistoryPosWin) {
        this.mIndexHistoryPosWin = mIndexHistoryPosWin;
    }

    /**
     * @return the mIndexHistoryRotWin
     */
    public int getIndexHistoryRotWin() {
        return mIndexHistoryRotWin;
    }

    /**
     * @param mIndexHistoryRotWin the mIndexHistoryRotWin to set
     */
    public void setIndexHistoryRotWin(int mIndexHistoryRotWin) {
        this.mIndexHistoryRotWin = mIndexHistoryRotWin;
    }

    /**
     * @return the mIndexHistoryGripperWin
     */
    public int getIndexHistoryGripperWin() {
        return mIndexHistoryGripperWin;
    }

    /**
     * @param mIndexHistoryGripperWin the mIndexHistoryGripperWin to set
     */
    public void setIndexHistoryGripperWin(int mIndexHistoryGripperWin) {
        this.mIndexHistoryGripperWin = mIndexHistoryGripperWin;
    }

    /**
     * @return the mLinearVelocityWindowSize
     */
    public double getLinearVelocityWindowSize() {
        return mLinearVelocityWindowSize;
    }

    /**
     * @param mLinearVelocityWindowSize the mLinearVelocityWindowSize to set
     */
    public void setLinearVelocityWindowSize(double mLinearVelocityWindowSize) {
        this.mLinearVelocityWindowSize = mLinearVelocityWindowSize;
    }

    /**
     * @return the mAngularVelocityWindowSize
     */
    public double getAngularVelocityWindowSize() {
        return mAngularVelocityWindowSize;
    }

    /**
     * @param mAngularVelocityWindowSize the mAngularVelocityWindowSize to set
     */
    public void setAngularVelocityWindowSize(double mAngularVelocityWindowSize) {
        this.mAngularVelocityWindowSize = mAngularVelocityWindowSize;
    }

    /**
     * @return the mGripperVelocityWindowSize
     */
    public double getGripperVelocityWindowSize() {
        return mGripperVelocityWindowSize;
    }

    /**
     * @param mGripperVelocityWindowSize the mGripperVelocityWindowSize to set
     */
    public void setGripperVelocityWindowSize(double mGripperVelocityWindowSize) {
        this.mGripperVelocityWindowSize = mGripperVelocityWindowSize;
    }

    /**
     * @return the mClockGeneral
     */
    public JPrecisionClock getClockGeneral() {
        return mClockGeneral;
    }

    /**
     * @param mClockGeneral the mClockGeneral to set
     */
    public void setClockGeneral(JPrecisionClock mClockGeneral) {
        this.mClockGeneral = mClockGeneral;
    }
}


/**
 * Provides a structure to store an orientation frame with a time stamp
 *
 * @author jairo
 */
class JTimestampRot {

    /**
     * Time when the following data was acquired
     */
    public double time;
    /**
     * Rotation information
     */
    public JMatrix3d rotation;

    public JTimestampRot() {
        this.rotation = new JMatrix3d();
    }
}


/**
 * Provides a structure to store a position with a time stamp
 *
 * @author jairo
 */
class JTimestampPos {

    /**
     * Time when the following data was acquired
     */
    public double time;
    /**
     * Position information
     */
    public JVector3d position;

    public JTimestampPos() {
        position = new JVector3d();
    }
    
    
}

/**
 * Provides a structure to store a double value with a time
 * stamp
 * @author jairo
 */
class JTimestampValue {

    /**
     * Time when the following data was acquired
     */
    public double time;
    /**
     * Gripper position
     */
    public double value;
}
