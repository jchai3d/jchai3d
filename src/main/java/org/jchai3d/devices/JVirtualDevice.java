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

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * Class which interfaces with the virtual device
 * 
 * @author jairo
 */
public class JVirtualDevice extends JGenericHapticDevice {

	// -----------------------------------------------------------------------
	// MEMBERS:
	// -----------------------------------------------------------------------
	/**
	 * Shared memory connection to virtual haptic device.
	 */
	private VirtualDeviceLibrary virtualLib;

	// -----------------------------------------------------------------------
	// CONSTRUCTOR:
	// -----------------------------------------------------------------------
	public JVirtualDevice() {
		// settings:
		mSpecifications = new JHapticDeviceInfo();
		mSpecifications.mManufacturerName = "CHAI 3D";
		mSpecifications.mModelName = "virtual";
		mSpecifications.mMaxForce = 10.0; // [N]
		mSpecifications.mMaxForceStiffness = 2000.0; // [N/m]
		mSpecifications.mMaxTorque = 0.0; // [N*m]
		mSpecifications.mMaxTorqueStiffness = 0.0; // [N*m/Rad]
		mSpecifications.mMaxGripperTorque = 0.0; // [N]
		mSpecifications.mMaxGripperTorqueStiffness = 0.0; // [N/m]
		mSpecifications.mWorkspaceRadius = 0.15; // [m]
		mSpecifications.mSensedPosition = true;
		mSpecifications.mSensedRotation = false;
		mSpecifications.mSensedGripper = false;
		mSpecifications.mActuatedPosition = true;
		mSpecifications.mActuatedRotation = false;
		mSpecifications.mActuatedGripper = false;
		mSpecifications.mLeftHand = true;
		mSpecifications.mRightHand = true;

		mSystemAvailable = false;
		mSystemReady = false;

		try {
			virtualLib = (VirtualDeviceLibrary) Native.loadLibrary(
					"VirtualDevice", VirtualDeviceLibrary.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		if (virtualLib.mapMemory("dhdVirtual") == 0) {
			mSystemReady = false;
			mSystemAvailable = false;
			return;
		}

		// virtual device is available
		mSystemAvailable = true;
	}

	// -----------------------------------------------------------------------
	// METHODS:
	// -----------------------------------------------------------------------
	/**
	 * Open connection to virtual device.
	 * 
	 * @return - Return 0 is operation succeeds, -1 if an error occurs.
	 */
	@Override
	public int open() {
		if (mSystemAvailable) {
			mSystemReady = true;
		}
		return (0);
	}

	/**
	 * Close connection to virtual device
	 * 
	 * @return - Return 0 is operation succeeds, -1 if an error occurs
	 */
	@Override
	public int close() {
		mSystemReady = false;

		return (0);
	}

	/**
	 * Initialize virtual device. a_resetEncoders is ignored
	 * 
	 * @param aResetEncoders
	 *            - ignored
	 * @return - Return 0 is operation succeeds, -1 if an error occurs
	 */
	@Override
	public int initialize(final boolean aResetEncoders) {
		if (mSystemReady) {
			return (0);
		} else {
			return (-1);
		}
	}

	/**
	 * Returns the number of devices available from this class of device
	 * 
	 * @return - Returns the number result of devices available
	 */
	@Override
	public int getNumDevices() {
		// only one device can be enabled
		int result;
		if (mSystemAvailable) {
			result = 1;
		} else {
			result = 0;
		}
		return (result);
	}

	/**
	 * Read the position of the device. Units are meters [m]
	 * 
	 * @param aPosition
	 *            - Return value.
	 * @return
	 */
	@Override
	public int getPosition(JVector3d aPosition) {
		if (!mSystemReady) {
			aPosition.set(0, 0, 0);
			return (-1);
		}

		double x = 0, y = 0, z = 0;

		try {
			JVirtualDeviceData.ByValue mPDevice = virtualLib.getValue();

			x = (double) (mPDevice).PosX;
			y = (double) (mPDevice).PosY;
			z = (double) (mPDevice).PosZ;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		aPosition.set(x, y, z);

		return (0);
	}

	/**
	 * Read the orientation frame of the device end-effector.
	 * 
	 * @param aRotation
	 *            - Return value.
	 * @return
	 */
	@Override
	public int getRotation(JMatrix3d aRotation) {
		if (!mSystemReady) {
			aRotation.identity();
			return (-1);
		}

		aRotation.identity();

		return (0);
	}

	/**
	 * Send a force [N] to the haptic device.
	 * 
	 * @param aForce
	 *            - Force command to be applied to device.
	 * @return
	 */
	@Override
	public int setForce(JVector3d aForce) {
		if (!mSystemReady) {
			return (-1);
		}

		try {
			JVirtualDeviceData mPDevice = virtualLib.getValue();

			((mPDevice).ForceX) = aForce.getX();
			((mPDevice).ForceY) = aForce.getY();
			((mPDevice).ForceZ) = aForce.getZ();

			virtualLib.setValue((JVirtualDeviceData.ByValue) mPDevice);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (0);
	}

	/**
	 * Return the last force sent to the device.
	 * 
	 * @param aForce
	 *            - Return value.
	 * @return
	 */
	@Override
	public int getForce(JVector3d aForce) {
		if (!mSystemReady) {
			aForce.set(0, 0, 0);
			return (-1);
		}

		try {
			JVirtualDeviceData mPDevice = virtualLib.getValue();

			aForce.setX(((mPDevice).ForceX));
			aForce.setY(((mPDevice).ForceY));
			aForce.setZ(((mPDevice).ForceZ));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (0);
	}

	/**
	 * Read the status of the user switch [ true = ON / false = OFF].
	 * 
	 * @param aSwitchIndex
	 *            - index number of the switch.
	 * @param aStatus
	 *            - result value from reading the selected input switch.
	 * @return
	 */
	@Override
	public int getUserSwitch(int aSwitchIndex, Boolean aStatus) {
		if (!mSystemReady) {
			aStatus = false;
			return (-1);
		}

		try {
			JVirtualDeviceData mPDevice = virtualLib.getValue();

			aStatus = ((boolean) (mPDevice).Button0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (0);
	}

	public static class JVirtualDeviceData extends Structure {

		public static class ByValue extends JVirtualDeviceData implements
				Structure.ByValue {
		}

		public double ForceX; // Force component X.
		public double ForceY; // Force component Y.
		public double ForceZ; // Force component Z.
		public double TorqueA; // Torque alpha.
		public double TorqueB; // Torque beta.
		public double TorqueG; // Torque gamma.
		public double PosX; // Position X.
		public double PosY; // Position Y.
		public double PosZ; // Position Z.
		public double AngleA; // Angle alpha.
		public double AngleB; // Angle beta.
		public double AngleG; // Angle gamma.
		public boolean Button0; // Button 0 status.
		public boolean AckMsg; // Acknowledge Message
		public boolean CmdReset; // Command Reset

		@Override
		protected List getFieldOrder() {
			return Arrays.asList(new String[] { "ForceX", "ForceY", "ForceZ",
					"TorqueA", "TorqueB", "TorqueG", "PosX", "PosY", "PosZ",
					"AngleA", "AngleB", "AngleG" });
		}
	};

	public interface VirtualDeviceLibrary extends Library {

		public int mapMemory(String memoryName);

		public void setValue(JVirtualDeviceData.ByValue pDevice);

		public JVirtualDeviceData.ByValue getValue();
	}
}
