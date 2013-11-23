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
import org.jchai3d.math.JVector3d;

/**
 * describes an interface to the Falcon haptic device
 * 
 * @author jairo
 */
public class JFalconDevice extends JGenericHapticDevice
{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    /** 
     * counter.
     */
    private static int mDllcount;

    /** 
     * Device ID number.
     */
    private int mDeviceID;

    /** 
     * Are the Falcon drivers installed and available.
     */
    private boolean mDriverInstalled;

    /** 
     * Falcon driver installed
     */
    public static FalconDriverLibrary hdFalconDriver = null;

    /** 
     * Falcon device communication
     */
    public static FalconDeviceLibrary hdFalcon = null;

    //-----------------------------------------------------------------------
    // CONSTRUCTORS:
    //-----------------------------------------------------------------------
    
    /**
     * Constructor of JFalconDevice.
     */
    public JFalconDevice()
    {
        this(0);
    }
    
    /**
     * Constructor of JFalconDevice.
     * @param aDeviceNumber 
     */
    public JFalconDevice(int aDeviceNumber)
    {
        // set specifications
        mSpecifications.mManufacturerName              = "Novint Technologies";
        mSpecifications.mModelName                     = "Falcon";
        mSpecifications.mMaxForce                      = 8.0;     // [N]
        mSpecifications.mMaxForceStiffness             = 3000.0;  // [N/m]
        mSpecifications.mMaxTorque                     = 0.0;     // [N*m]
        mSpecifications.mMaxTorqueStiffness            = 0.0;     // [N*m/Rad]
        mSpecifications.mMaxGripperTorque              = 0.0;     // [N]
        mSpecifications.mMaxLinearDamping              = 20.0;    // [N/(m/s)]
        mSpecifications.mMaxGripperTorqueStiffness     = 0.0;     // [N*m/m]
        mSpecifications.mWorkspaceRadius               = 0.04;    // [m]
        mSpecifications.mSensedPosition                = true;
        mSpecifications.mSensedRotation                = false;
        mSpecifications.mSensedGripper                 = false;
        mSpecifications.mActuatedPosition              = true;
        mSpecifications.mActuatedRotation              = false;
        mSpecifications.mActuatedGripper               = false;
        mSpecifications.mLeftHand                      = true;
        mSpecifications.mRightHand                     = true;

        // device is not yet available or ready
        mDriverInstalled = false;
        mSystemAvailable = false;
        mSystemReady = false;

        // check if Falcon drivers installed
        if (mDllcount == 0)
        {

            // load Falcon dll
            /*try
            {
                hdFalconDriver =
                        (FalconDriverLibrary) Native.loadLibrary(
                        "hdl",
                        FalconDriverLibrary.class);

                // check if file exists
                if(hdFalconDriver == null)
                    throw(new Exception("Falcon driver not installed!"));

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return;
            }*/

            // check if multi device is supported

            /* TODO:
            FARPROC testFunction = null;
            testFunction = GetProcAddress(hdFalconDriver, "hdlCountDevices@0");
            if (testFunction == null)
            {
                // failed, old version is installed.
                System.out.println("ERROR: Please update the drivers of your Novint Falcon haptic device.\n");
                return;
            }

             */
        }

        // the Falcon drivers are installed
        //mDriverInstalled = true;

        // load dll library
        
        System.out.println("Loading 'hdFalcon' library");
        if (mDllcount == 0)
        {
            try
            {
                hdFalcon =
                        (FalconDeviceLibrary) Native.loadLibrary(
                        "hdFalcon",
                        FalconDeviceLibrary.class);

            }
            catch(Throwable e)
            {
            System.out.print(" >"+e.getMessage());
                return;
            }
        }
        
        // get the number ID of the device we wish to communicate with
        mDeviceID = aDeviceNumber;

        // get the number of Force Dimension devices connected to this computer
        int numDevices = 0;
        try{
            numDevices = hdFalcon.hdFalconGetNumDevices();
        }catch(Throwable e)
        {e.printStackTrace();}

        // check if such device is available
        if ((aDeviceNumber + 1) > numDevices)
        {
            // no, such ID does not lead to an existing device
            mSystemAvailable = false;
        }
        else
        {
            // yes, this ID leads to an existing device
            mSystemAvailable = true;
        }

        // increment counter
        mDllcount++;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    
    /**
     * Open connection to Falcon haptic device.
     */
    @Override
    public int open()
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        // check if the system is available
        if (!mSystemAvailable) return (-1);

        // if system is already opened then return
        if (mSystemReady) return (0);

        // try to open the device
        try{
            hdFalcon.hdFalconOpen(mDeviceID);
        }catch(Exception e)
        {e.printStackTrace();}

        // update device status
        mSystemReady = true;

        // success
        return (0);
    }
    
    /**
     * Close connection to Falcon haptic device.
     */
    @Override
    public int close()
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        // check if the system has been opened previously
        if (!mSystemReady) return (-1);

        // yes, the device is open so let's close it
        int result = -1;
        try{
            result = hdFalcon.hdFalconClose(mDeviceID);
        }catch(Exception e)
        {e.printStackTrace();}

        // update status
        mSystemReady = false;

        // exit
        return (result);
    }
    
    /**
     * Calibrate Falcon haptic device.
     * 
     * This function does nothing right now; the a_resetEncoders parameter is ignored.
     * @return - Always 0
     */    
    public int initialize()
    {
        return initialize(false);
    }
    
    /**
     * Calibrate Falcon haptic device.
     *
     * This function does nothing right now; the a_resetEncoders parameter is ignored.
     * @param aResetEncoders - Ignored; exists for forward compatibility.
     * @return - Always 0
     */    
    @Override
    public int initialize(final boolean aResetEncoders)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        // reset encoders
        return (0);
    }
    
    /**
     * Calibrate Falcon haptic device.
     * This function does nothing right now; the a_resetEncoders parameter is ignored.
     * @param aResetEncoders - Ignored; exists for forward compatibility.
     * @return - Always 0
     */    
    @Override
    public int getNumDevices()
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (0);

        int numDevices = 0;
        try{
            numDevices = hdFalcon.hdFalconGetNumDevices();
        }catch(Exception e)
        {e.printStackTrace();}

        return (numDevices);
    }
    
    /**
     * Read the position of the device. Units are meters [m].
     * @param aPosition - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getPosition(JVector3d aPosition)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        DoubleByReference   x = new DoubleByReference(),
                            y = new DoubleByReference(),
                            z = new DoubleByReference();

        int error = 0;
        try{
            error = hdFalcon.hdFalconGetPosition(mDeviceID, x, y, z);
        }catch(Exception e)
        {e.printStackTrace();}

        // add a small offset for zero centering
        x.setValue(x.getValue() + 0.01);
        aPosition.set(x.getValue(), y.getValue(), z.getValue());
        estimateLinearVelocity(aPosition);
        return (error);
    }
    
    /**
     * Send a force [N] to the Falcon haptic device.
     * @param aForce - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setForce(JVector3d aForce)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        DoubleByReference   x = new DoubleByReference(aForce.getX()),
                            y = new DoubleByReference(aForce.getY()),
                            z = new DoubleByReference(aForce.getZ());

        int error = 0;
        try{
            error = hdFalcon.hdFalconSetForce(mDeviceID, x, y, z);
        }catch(Exception e)
        {e.printStackTrace();}

        aForce.setX(x.getValue());
        aForce.setY(y.getValue());
        aForce.setZ(z.getValue());

        setPrevForce(aForce);
        return (error);
    }
    
    /**
     * Read the status of the user switch [1 = \e ON / 0 = \e OFF].
     * @param aSwitchIndex - index number of the switch.
     * @param aStatus - result value from reading the selected input switch.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getUserSwitch(int aSwitchIndex, Boolean aStatus)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        boolean result = false;
        int button = 0;
        try{
            button = hdFalcon.hdFalconGetButtons(mDeviceID);
        }catch(Exception e)
        {e.printStackTrace();}

        switch (aSwitchIndex)
        {
            case 0:
                if ((button & 1) != 0) { result = true; }
                break;

            case 1:
                if ((button & 2) != 0) { result = true; }
                break;

            case 2:
                if ((button & 3) != 0) { result = true; }
                break;

            case 3:
                if ((button & 4) != 0) { result = true; }
                break;
        }

        // return result
        aStatus = result;
        
        return (0);
    }

    interface FalconDriverLibrary extends Library
    {

    }

    interface FalconDeviceLibrary extends Library
    {
        public int hdFalconGetNumDevices  ();
        public int hdFalconOpen           (int adeviceID);
        public int hdFalconClose          (int adeviceID);
        public int hdFalconGetPosition    (int adeviceID,
                                                DoubleByReference aPosX,
                                                DoubleByReference aposY,
                                                DoubleByReference aPosZ);
        public int hdFalconGetRotation   (int adeviceID,
                                                DoubleByReference aRot00,
                                                DoubleByReference aRot01,
                                                DoubleByReference aRot02,
                                                DoubleByReference aRot10,
                                                DoubleByReference aRot11,
                                                DoubleByReference aRot12,
                                                DoubleByReference aRot20,
                                                DoubleByReference aRot21,
                                                DoubleByReference aRot22);

        public int hdFalconGetButtons    (int aDeviceID);

        public int hdFalconSetForce      (int aDeviceID,
                                                DoubleByReference aForceX,
                                                DoubleByReference aForceY,
                                                DoubleByReference aForceZ);
    }

}
