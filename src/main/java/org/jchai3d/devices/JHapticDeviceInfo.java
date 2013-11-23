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
 * Provides a structure which can hold technical specifications about a
 * particular haptic device
 * @author jairo
 */



public class JHapticDeviceInfo {

    /**
     * Name of the device model. "delta, "omega" or "phantom" for instance
     */
    public String mModelName;

    /**
     * Name of the manufacturer of the device
     */
    public String mManufacturerName;

    /**
     * Maximum force in [N] that can be produced by the device in translation.
     */
    public double mMaxForce;

    /**
     * Maximum torque in [N*m] that can be produced by the device in orientation.
     */
    public double mMaxTorque;

    /**
     * Maximum force in [N*m] that can be produced by the gripper
     */
    public double mMaxGripperTorque;

    /**
     * Maximum closed loop force stiffness [N/m] for a simulation running at 1 KhZ.
     */
    public double mMaxForceStiffness;

    /**
     * Maximum closed loop torque stiffness [N*m/rad] for a simulation running at 1 KhZ.
     */
    public double mMaxTorqueStiffness;

    /**
     * Maximum closed loop gripper torque stiffness [N*m/rad] for a simulation running at 1 KhZ.
     */
    public double mMaxGripperTorqueStiffness;

    /**
     * Maximum recommended linear damping factor Kv when using the getVelocity() method from the device class
     */
    public double mMaxLinearDamping;

    /**
     * Radius which describes the largest sphere (3D devices) or circle (2D Devices) which can be enclosed inside the physical workspace of the device.
     */
    public double mWorkspaceRadius;

    /**
     * If \b true then device supports position sensing (x,y,z axis)
     */
    public boolean mSensedPosition;

    /**
     * If \b true thhen device supports rotation sensing. (i.e stylus, pen)
     */
    public boolean mSensedRotation;

    /**
     * If \b true then device supports a sensed gripper interface
     */
    public boolean mSensedGripper;

    /**
     * If \b true then device provides actuation capabilities on the translation degrees of freedom. (x,y,z axis).
     */
    public boolean mActuatedPosition;

    /**
     * If \b true then device provides actuation capabilities on the orientation degrees of freedom. (i.e stylus, pen)
     */
    public boolean mActuatedRotation;

    /**
     * If \b true then device provides actuation capabilities on the gripper.
     */
    public boolean mActuatedGripper;

    /**
     * If \b true then the device can used for left hands
     */
    public boolean mLeftHand;

    /**
     * If \b true then the device can used for left hands
     */
    public boolean mRightHand;

    public void copyFrom(JHapticDeviceInfo info) {

        mManufacturerName = info.mManufacturerName;
        mModelName = info.mModelName;
        mMaxForce = info.mMaxForce;    // [N]
        mMaxForceStiffness = info.mMaxForceStiffness;  // [N/m]
        mMaxTorque = info.mMaxTorque;     // [N*m]
        mMaxTorqueStiffness = info.mMaxTorqueStiffness;     // [N*m/Rad]
        mMaxGripperTorque = info.mMaxGripperTorque;     // [N]
        mMaxGripperTorqueStiffness = info.mMaxGripperTorqueStiffness;     // [N/m]
        mWorkspaceRadius = info.mWorkspaceRadius;    // [m]
        mSensedPosition = info.mSensedPosition;
        mSensedRotation = info.mSensedRotation;
        mSensedGripper = info.mSensedGripper;
        mActuatedPosition = info.mActuatedPosition;
        mActuatedRotation = info.mActuatedRotation;
        mActuatedGripper = info.mActuatedGripper;
        mLeftHand = info.mLeftHand;
        mRightHand = info.mRightHand;
        mMaxLinearDamping = info.mMaxLinearDamping;
        
    }

    @Override
    public String toString() {
        String str = "Manufacturer: " + mManufacturerName;
        str += "\nModel: " + mModelName;
        return str;
    }
}
