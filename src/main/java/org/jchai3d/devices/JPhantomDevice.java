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

/**
 * describes an interface to the Phantom haptic devices
 * @author jairo
 */
public class JPhantomDevice extends JGenericHapticDevice
{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    /**
     * Counter.
     */ 
    private static int mDllcount;

    /**
     * Device ID number.
     */ 
    private int mDeviceID;

    /**
     * Are the Phantom drivers installed and available.
     */ 
    boolean mDriverInstalled;

    /**
     * Phantom driver installed
     */ 
    public static PhantomDriverLibrary hdPhantomDriver = null;

    /**
     * Phantom device communication
     */ 
    public static PhantomDeviceLibrary hdPhantom = null;
    
    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    
    /**
     * Constructor of JPhantomDevice.
     * No servo loop is yet created, encoders are NOT reset.
     * @param aDeviceNumber - Index number to the ith Phantom device
     */
    public JPhantomDevice(int aDeviceNumber)
    {
        // default specification setup
        mSpecifications.mManufacturerName              = "Sensable Technologies";
        mSpecifications.mModelName                     = "PHANTOM";
        mSpecifications.mMaxForce                      = 6.0;     // [N]
        mSpecifications.mMaxForceStiffness             = 1000.0;  // [N/m]
        mSpecifications.mMaxTorque                     = 0.0;     // [N*m]
        mSpecifications.mMaxTorqueStiffness            = 0.0;     // [N*m/Rad]
        mSpecifications.mMaxGripperTorque              = 0.0;     // [N]
        mSpecifications.mMaxGripperTorqueStiffness     = 0.0;     // [N*m/m]
        mSpecifications.mMaxLinearDamping              = 8.0;     // [N/(m/s)]
        mSpecifications.mWorkspaceRadius               = 0.10;    // [m];
        mSpecifications.mSensedPosition                = true;
        mSpecifications.mSensedRotation                = true;
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

        // check if Phantom drivers installed
        System.out.println("Loading 'HD' library.");
        
        if (mDllcount == 0)
        {
            try
            {
                hdPhantomDriver =
                        (PhantomDriverLibrary) Native.loadLibrary(
                        "HD",
                        PhantomDriverLibrary.class);

                if(hdPhantomDriver == null)
                    throw(new Exception("Phantom driver not installed."));
            }
            catch(Throwable e)
            {
            System.out.print(" >"+e.getMessage());
                return;
            }
        }

        // the Phantom drivers are installed
        mDriverInstalled = true;

        // load dll library
        if (mDllcount == 0)
        {
            try
            {
                // load dll library
                hdPhantom =
                    (PhantomDeviceLibrary) Native.loadLibrary(
                    "hdPhantom",
                    PhantomDeviceLibrary.class);
            }catch(Throwable e){
                e.printStackTrace();
                return;
            }
        }
        
        // check if DLL loaded correctly
        if(hdPhantom == null)
            return;

        // get the number ID of the device we wish to communicate with
        mDeviceID = aDeviceNumber;

        // get the number of Force Dimension devices connected to this computer
        int numDevices = 0;
        try{
            numDevices = hdPhantom.hdPhantomGetNumDevices();
        }catch(Exception e)
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

        // read information related to the device
        DoubleByReference workspaceRadius = new DoubleByReference(getSpecifications().mWorkspaceRadius);
        try{
            hdPhantom.hdPhantomGetWorkspaceRadius(mDeviceID, workspaceRadius);
        }catch(Exception e)
        {e.printStackTrace();}
        mSpecifications.mWorkspaceRadius = workspaceRadius.getValue();

        // read the device model
        String name = "";
        try{
            hdPhantom.hdPhantomGetType(mDeviceID, name);
        }catch(Exception e)
        {e.printStackTrace();}
        mSpecifications.mModelName = name;

        /////////////////////////////////////////////////////////////////////
        // Define specifications given the device model
        /////////////////////////////////////////////////////////////////////

        if (mSpecifications.mModelName.equals("PHANTOM Omni"))

        {
            mSpecifications.mMaxForce                      = 4.0;     // [N]
            mSpecifications.mMaxForceStiffness             = 700.0;   // [N/m]
            mSpecifications.mMaxLinearDamping              = 5.0;     // [N/(m/s)]
        }

        // increment counter
        mDllcount++;

    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    
    /**
     * Open connection to phantom device.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
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
            hdPhantom.hdPhantomOpen(mDeviceID);
        }catch(Exception e)
        {e.printStackTrace();}
        
        // update device status
        mSystemReady = true;

        // success
        return (0);
    }
    
    /**
     * Close connection to phantom device.
     * @return - Return 0 is operation succeeds, -1 if an error occurs.
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
            result = hdPhantom.hdPhantomClose(mDeviceID);
        }catch(Exception e)
        {e.printStackTrace();}

         // update status
        mSystemReady = false;

        // possibly turn off servo loop
        try{
            hdPhantom.hdPhantomStopServo();
        }catch(Exception e)
        {e.printStackTrace();}

        // exit
        return (result);
    }
    
    /**
     * Initialize the phantom device.
     * For desktops and omnis, the a_resetEncoders parameter is ignored.
     * For premiums, if you specify a_resetEncoders as true, you should
     * be holding the Phantom in its rest position when this is called.
     * @return - Return 0 if operation succeeds, -1 if an error occurs.
     */    
    public int initialize()
    {
        return initialize(false);
    }
    
    /**
     * Initialize the phantom device.
     * For desktops and omnis, the a_resetEncoders parameter is ignored.
     * For premiums, if you specify a_resetEncoders as true, you should
     * be holding the Phantom in its rest position when this is called.
     * @param aResetEncoders - Should I re-zero the encoders?  (affects premiums only...)
     * @return - Return 0 if operation succeeds, -1 if an error occurs.
     */    
    @Override
    public int initialize(final boolean aResetEncoders)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        // exit
        return 0;
    }

    /**
     * Returns the number of devices available from this class of device.
     * @return - Returns the result
     */    
    @Override
    public int getNumDevices()
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (0);

        // read number of devices
        int numDevices = 0;
        try{
            numDevices = hdPhantom.hdPhantomGetNumDevices();
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

        DoubleByReference   x=new DoubleByReference(),
                            y=new DoubleByReference(),
                            z=new DoubleByReference();
        
        int error = -1;
        try{
            error = hdPhantom.hdPhantomGetPosition(mDeviceID, x, y, z);
        }catch(Exception e)
        {e.printStackTrace();}

        aPosition.set(x.getValue(), y.getValue(), z.getValue());
        estimateLinearVelocity(aPosition);
        return (error);
    }
    
    /**
     * Read the linear velocity of the device. Units are in [m/s].
     * @param aLinearVelocity - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getLinearVelocity(JVector3d aLinearVelocity)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        int error = -1;

        /*
        int error = -1;

        Double  vx=new Double(0),
                vy=new Double(0),
                vz=new Double(0);

        error = hdPhantom.hdPhantomGetLinearVelocity(mDeviceID, vx, vy, vz);

        mLinearVelocity.set(vx, vy, vz);
        aLinearVelocity = mLinearVelocity;
         */

        aLinearVelocity = getLinearVelocity();

        return (error);
    }
    
    /**
     * Read the orientation frame of the device end-effector
     * @param aRotation - Return value.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int getRotation(JMatrix3d aRotation)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        //Pre-alocation memory
        //DoubleByReference [][] rot = new DoubleByReference [3][3];
        
        DoubleByReference rot00 = new DoubleByReference();
        DoubleByReference rot01 = new DoubleByReference();
        DoubleByReference rot02 = new DoubleByReference();
        DoubleByReference rot10 = new DoubleByReference();
        DoubleByReference rot11 = new DoubleByReference();
        DoubleByReference rot12 = new DoubleByReference();
        DoubleByReference rot20 = new DoubleByReference();
        DoubleByReference rot21 = new DoubleByReference();
        DoubleByReference rot22 = new DoubleByReference();
        
        int error = -1;
        try{
            error = hdPhantom.hdPhantomGetRotation(mDeviceID,
                                         rot00,
                                         rot01,
                                         rot02,
                                         rot10,
                                         rot11,
                                         rot12,
                                         rot20,
                                         rot21,
                                         rot22);
        }catch(Exception e)
        {e.printStackTrace();}
        
        aRotation.set(rot00.getValue(),rot01.getValue(), rot02.getValue(),
                      rot10.getValue(),rot11.getValue(), rot12.getValue(),
                      rot20.getValue(),rot21.getValue(), rot22.getValue());

        estimateAngularVelocity(aRotation);
        return (error);
    }
    
    /**
     * Send a force [N] to the haptic device
     * @param aForce - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setForce(JVector3d aForce)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);


        DoubleByReference   x=new DoubleByReference(aForce.getX()),
                            y=new DoubleByReference(aForce.getY()),
                            z=new DoubleByReference(aForce.getZ());

        int error = -1;
        try{
            error = hdPhantom.hdPhantomSetForce(mDeviceID, x, y, z);
        }catch(Exception e)
        {e.printStackTrace();}

        aForce.setX(x.getValue());
        aForce.setY(y.getValue());
        aForce.setZ(z.getValue());

        setPrevForce(aForce);
        return (error);
    }
    
    /**
     * Send a torque [N*m] to the haptic device
     * @param aTorque - Force command to be applied to device.
     * @return - Return 0 if no error occurred.
     */    
    @Override
    public int setTorque(JVector3d aTorque)
    {
        // check if drivers are installed
        if (!mDriverInstalled) return (-1);

        DoubleByReference   x=new DoubleByReference(aTorque.getX()),
                            y=new DoubleByReference(aTorque.getY()),
                            z=new DoubleByReference(aTorque.getZ());

        int error = -1;
        try{
            error = hdPhantom.hdPhantomSetTorque(mDeviceID, x, y, z);
        }catch(Exception e)
        {e.printStackTrace();}

        aTorque.setX(x.getValue());
        aTorque.setY(y.getValue());
        aTorque.setZ(z.getValue());

        setPrevTorque(aTorque);
        return (error);
    }
    
    /**
     * Read the status of the user switch [\b true = \b ON / \b false = \b OFF].
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
            button = hdPhantom.hdPhantomGetButtons(mDeviceID);
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

    interface PhantomDriverLibrary extends Library
    {

    }

    interface PhantomDeviceLibrary extends Library
    {

        public int hdPhantomGetNumDevices ();

        public int hdPhantomOpen          (int aDeviceID);

        public int hdPhantomClose         (int aDeviceID);

        public int hdPhantomGetPosition   (int aDeviceID,
                                                DoubleByReference aPosX,
                                                DoubleByReference aPosY,
                                                DoubleByReference aPosZ);

        public int hdPhantomGetLinearVelocity(int aDeviceID,
                                                DoubleByReference aVelX,
                                                DoubleByReference aVelY,
                                                DoubleByReference aVelZ);

        public int hdPhantomGetRotation   (int aDeviceID,
                                                DoubleByReference aRot00,
                                                DoubleByReference aRot01,
                                                DoubleByReference aRot02,
                                                DoubleByReference aRot10,
                                                DoubleByReference aRot11,
                                                DoubleByReference aRot12,
                                                DoubleByReference aRot20,
                                                DoubleByReference aRot21,
                                                DoubleByReference aRot22);

        public int hdPhantomGetButtons    (int aDeviceID);

        public int hdPhantomSetForce      (int aDeviceID,
                                                DoubleByReference aForceX,
                                                DoubleByReference aForceY,
                                                DoubleByReference aForceZ);

        public int hdPhantomSetTorque     (int aDeviceID,
                                                DoubleByReference aTorqueX,
                                                DoubleByReference aTorqueY,
                                                DoubleByReference aTorqueZ);

        public int hdPhantomGetWorkspaceRadius(int aDeviceID,
                                                DoubleByReference aWorkspaceRadius);

        public int hdPhantomGetType       (int aDeviceID,
                                                String aTypeName);

        // initialize servo controller
        public void hdPhantomStartServo();

        // stop servo controller
        public void hdPhantomStopServo();
    }

}
