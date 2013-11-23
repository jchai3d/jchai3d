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
import com.sun.jna.Pointer;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * describes an interface to the Freedom6S haptic
 * @author jairo
 */
public class JFreedom6SDevice extends JGenericHapticDevice
{
    
    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    /**
     * Reference count used to control access to the DLL.
     */ 
    protected static int mActiveFreedom6SDevices;

    /**
     * Handle to device.
     */ 
    protected Pointer mHf6s;

    /**
     * Freedom6S device communication
     */ 
    public static Freedom6sDeviceLibrary hf6s = null;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    
    /**
     * Constructor of JFreedom6SDevice.
     * Loads interface DLL.
     */
    public JFreedom6SDevice()
    {
        super();

        mSystemReady = false;
        mSystemAvailable = false;
        mHf6s = null;

        mActiveFreedom6SDevices++;

        System.out.println("Loading 'freedom6s' library.");
        if (hf6s==null)
        {
            try
            {
                hf6s = 
                        (Freedom6sDeviceLibrary) Native.loadLibrary(
                        "freedom6s",
                        Freedom6sDeviceLibrary.class);

                    
            }
            catch(Throwable e)
            {
            System.out.print(" >"+e.getMessage());
                return;
            }
            
        }

        // setup information about device
        mSpecifications.mManufacturerName              = "MPB Technologies";
        mSpecifications.mModelName                     = "freedom 6S";
        mSpecifications.mMaxForce                      = 2.500;   // [N]
        mSpecifications.mMaxForceStiffness             = 2000.0;  // [N/m]
        mSpecifications.mMaxTorque                     = 0.250;   // [N*m]
        mSpecifications.mMaxTorqueStiffness            = 3.000;   // [N*m/Rad]
        mSpecifications.mMaxGripperTorque              = 0.000;   // [N*m]
        mSpecifications.mMaxGripperTorqueStiffness     = 0.000;   // [N/m]
        mSpecifications.mMaxLinearDamping              = 3.5;     // [N/(m/s)]
        mSpecifications.mWorkspaceRadius               = 0.100;   // [m]
        mSpecifications.mSensedPosition                = true;
        mSpecifications.mActuatedGripper               = true;
        mSpecifications.mSensedGripper                 = false;
        mSpecifications.mActuatedPosition              = true;
        mSpecifications.mActuatedRotation              = true;
        mSpecifications.mActuatedGripper               = false;
        mSpecifications.mLeftHand                      = true;
        mSpecifications.mRightHand                     = true;

        mSystemAvailable = true;

    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    
    /**
     * Open connection to Freedom6S device.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    @Override
    public int open()
    {
        return 0;
    }
    
    /**
     * Close connection to Freedom6S device.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    @Override
    public int close()
    {
        return 0;
    }
    
    /**
     * Calibrate Freedom6S device. Initializes the driver, loading appropriate
     * settings according to current Freedom6S configuration.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */
    public int initialize()
    {
        return initialize(false);
    }
    
    /**
     * Calibrate Freedom6S device. Initializes the driver, loading appropriate
     * settings according to current Freedom6S configuration.
     * @param aResetEncoders - Ignored; exists for forward compatibility.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
     */    
    @Override
    public int initialize(final boolean aResetEncoders)
    {
        if (mHf6s != null)
            return -1;

        int rc = F6SRC.F6SRC_FAILURE;
        try{
            rc = hf6s.f6s_Initialize(mHf6s);
        }catch(Exception e)
        {e.printStackTrace();}
        
        if (mHf6s != null && rc == F6SRC.F6SRC_NOERROR)
        {
            // Joint velocity computation:
            //   timestep = 1ms
            //   sample buffer size = 15
            try{
                hf6s.f6s_ComputeJointVel(mHf6s, 0.001f, 15);
            }catch(Exception e)
            {e.printStackTrace();}
            
            return 0;
        }

        mHf6s = null;
        return -1;
    }
    
    /**
     * Returns the number of devices available from this class of device.
     * @return - Returns the result
     */    
    @Override
    public int getNumDevices()
    {
        int result = 0;

        if (mSystemAvailable == true)
        {
            result = 1;
        }

        return (result);
    }
    
    /**
     * Read the position of the device. Units are meters [m].
     * @param aPosition - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getPosition(JVector3d aPosition)
    {
        // temp variables
        double [] kinemat = new double[16];

        // read position information from device
        try{
            hf6s.f6s_UpdateKinematics(mHf6s);
            hf6s.f6s_GetPositionMatrixGL(mHf6s, kinemat);
        }catch(Exception e)
        {e.printStackTrace();}

        // kinemat is a row-major 4x4 rotation/translation matrix
        JVector3d result = new JVector3d();
        
        result.setX(kinemat[14]);
        result.setY(kinemat[12]);
        result.setZ(kinemat[13]);

        // return result
        aPosition = result;

        // success
        return (0);
    }
    
    /**
     * Read the linear velocity of the device. Units are in meters per second [m/s].
     * @param aLinearVelocity - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getLinearVelocity(JVector3d aLinearVelocity)
    {
        // temp variables
        double [] velLinear = new double[3], velAngular = new double[3];

        // read velocities from device
        try{
            hf6s.f6s_GetVelocityGL(mHf6s, velLinear, velAngular);
        }catch(Exception e)
        {e.printStackTrace();}
        
        getLinearVelocity().setX(velLinear[2]);
        getLinearVelocity().setY(velLinear[0]);
        getLinearVelocity().setZ(velLinear[1]);

        getAngularVelocity().setX(velAngular[2]);
        getAngularVelocity().setY(velAngular[0]);
        getAngularVelocity().setZ(velAngular[1]);

        // return result
        aLinearVelocity = getLinearVelocity();

        // success
        return (0);
    }
    
    /**
     * Read the orientation frame of the device end-effector
     * @param aRotation - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getRotation(JMatrix3d aRotation)
    {
        // success
        return (0);
    }
    
    /**
     * Read the angular velocity of the device. Units are in radians per
     * second [m/s].
     * @param aAngularVelocity - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getAngularVelocity(JVector3d aAngularVelocity)
    {
        // temp variables
        double [] velLinear = new double[3], velAngular = new double[3];

        // read velocities from device
        try{
            hf6s.f6s_GetVelocityGL(mHf6s, velLinear, velAngular);
        }catch(Exception e)
        {e.printStackTrace();}

        getLinearVelocity().setX(velLinear[2]);
        getLinearVelocity().setY(velLinear[0]);
        getLinearVelocity().setZ(velLinear[1]);

        getAngularVelocity().setX(velAngular[2]);
        getAngularVelocity().setY(velAngular[0]);
        getAngularVelocity().setZ(velAngular[1]);

        // return result
        aAngularVelocity = getAngularVelocity();

        // success
        return (0);
    }
    
    /**
     * Read the angular velocity of the device. Units are in radians per
     * second [m/s].
     * @param aAngularVelocity - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getGripperAngleRad(Double aAngle)
    {
        // success
        return (0);
    }
    
    /**
     * Send a force [N] to the haptic device
     * @param aForce - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setForce(JVector3d aForce)
    {
        // temp variables
        double [] force = new double[3], torque = new double[3];

        // store the new force
        setPrevForce(aForce);

        // convert force and torque in local coordinates of device
        force[0] = getPrevForce().getY();
        force[1] = -mPrevForce.getX();
        force[2] = getPrevForce().getZ();

        torque[0] = getPrevTorque().getY();
        torque[1] = -mPrevTorque.getX();
        torque[2] = getPrevTorque().getZ();

        // write values to device
        try{
            hf6s.f6s_SetForceTorque(mHf6s, force, torque);
        }catch(Exception e)
        {e.printStackTrace();}
        
        // success
        return (0);
    }
    
    /**
     * Send a torque [N*m] to the haptic device
     * @param aTorque - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setTorque(JVector3d aTorque)
    {
        // temp variables
        double [] force = new double[3], torque = new double[3];

        // store the new torque
        setPrevTorque(aTorque);

        // convert force and torque in local coordinates of device
        force[0] = getPrevForce().getY();
        force[1] = -mPrevForce.getX();
        force[2] = getPrevForce().getZ();

        torque[0] = getPrevTorque().getY();
        torque[1] = -mPrevTorque.getX();
        torque[2] = getPrevTorque().getZ();

        // write values to device
        try{
            hf6s.f6s_SetForceTorque(mHf6s, force, torque);
        }catch(Exception e)
        {e.printStackTrace();}

        // success
        return (0);
    }
    
    /**
     * Send a torque [N*m] to the gripper
     * @param aGripperTorque - Torque command to be sent to gripper.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setGripperTorque(double aGripperTorque)
    {
        // success
        return (0);
    }
    
    /**
     * Send a force [N] and a torque [N*m] and gripper torque [N*m] to the haptic device.
     * @param aForce - Force command.
     * @param aTorque - Torque command.
     * @param aGripperTorque - Gripper torque command.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setForceAndTorqueAndGripper(JVector3d aForce, JVector3d aTorque, double aGripperTorque)
    {
        // temp variables
        double [] force = new double[3], torque = new double[3];

        // store the new force, torque and gripper torque
        setPrevTorque(aTorque);
        setPrevForce(aForce);
        setPrevGripperTorque(aGripperTorque);

        // convert force and torque in local coordinates of device
        force[0] = getPrevForce().getY();
        force[1] = -mPrevForce.getX();
        force[2] = getPrevForce().getZ();

        torque[0] = getPrevTorque().getY();
        torque[1] = -mPrevTorque.getX();
        torque[2] = getPrevTorque().getZ();

        // write values to device
        try{
            hf6s.f6s_SetForceTorque(mHf6s, force, torque);
        }catch(Exception e)
        {e.printStackTrace();}
        

        // success
        return (0);
    }
    
    /**
     * Read the status of the user switch [1 = ON / 0 = OFF].
     * @param aSwitchIndex - index number of the switch.
     * @param aStatus - result value from reading the selected input switch.
     * @return - Return 0 if no error occurred.
     */    
    public int getUserSwitch(int aSwitchIndex, Double aStatus)
    {
	// no switch implemented
	aStatus = new Double(0);

        // success
        return (0);
    }

    public static interface F6SRC
    {
        public static final int   F6SRC_NOERROR         =  0;
        public static final int   F6SRC_ALREADYEXIST    = -1;   // A Freedom6S device is already open in the system
        public static final int   F6SRC_BADVALUE        = -2;   // Value out of range
        public static final int   F6SRC_BADPOINTER      = -3;   // Bad pointer passed to function
        public static final int   F6SRC_MEMORY          = -4;   // Out of memory
        public static final int   F6SRC_REGISTRY        = -5;   // Error reading registry values (will user defaults)
        public static final int   F6SRC_INIFILE_READ    = -6;   // Error reading ini file (settings)
        public static final int   F6SRC_INIFILE_WRITE   = -7;   // Error writing ini file (settings)
        public static final int   F6SRC_NOTINITIALIZED  = -8;   // Attempt to call a function before f6s_Initialize()
        public static final int   F6SRC_BADHANDLE       = -9;   // A function received a bad HF6S value
        public static final int   F6SRC_BADMOTORTEMP    = -10;  // Motor temperatures were out of range or not read correctly (warning only)
        public static final int   F6SRC_JOINTVELINIT    = -11;  // Attempt to read velocity without joint velocity computation enabled
        public static final int   F6SRC_CALIBRATION     = -12;  // Unable to calibrate; require mechanical calibration
        public static final int   F6SRC_ROLLANGLE       = -13;  // Unable to calculate roll angle; sensors 4 & 5 require mechanical re-calibration
        public static final int   F6SRC_DRIVERINIT      = -14;  // Unable to initialize the drivers for ADC or DAC hardware
        public static final int   F6SRC_IOERROR         = -15;  // Error returned from ADC or DAC drivers
        public static final int   F6SRC_DAQCONFIG       = -16;  // Unknown DAQ configuration
        public static final int   F6SRC_HOTMOTOR        = -17;  // One or more motors have been flagged hot; causing the max current to decrease
        public static final int   F6SRC_FAILURE         = -18;   // Operation failed
    }

    interface Freedom6sDeviceLibrary extends Library
    {
        int f6s_Initialize( Pointer phf6s );
        int f6s_ComputeJointVel( Pointer hf6s, float ftimeStep, int inewBufferSize );
        int f6s_Cleanup( Pointer hf6s );
        int f6s_SetHoldDist( Pointer hf6s, float fdist );
        int f6s_SetForceTorque( Pointer hf6s, final double [] force, final double [] torque );
        int f6s_GetPositionMatrixGL( Pointer hf6s, double [] kineMat );
        int f6s_UpdateKinematics( Pointer hf6s );
        int f6s_GetVelocityGL( Pointer hf6s, double [] linearVel,  double [] angularVel );
    }
    
}
