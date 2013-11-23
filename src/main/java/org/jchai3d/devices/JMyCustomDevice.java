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

/**
 * JMyCustomDevice provides the structure in which you can very easily
 * interface CHAI 3D to your custom haptic device. \n
 * 
 * Simply follow the comments and complete the gaps with your code.
 * Depending of the numbers of degrees of freedom of your device, not
 * all methods will need to be completed. For instance, if your device
 * does not provide any rotation degrees of freedom, simply ignore
 * the getRotation() method. Default values will be returned correctly
 * if these are not implemented on your device
 * @author felipe
 */
public class JMyCustomDevice extends JGenericHapticDevice
{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

   /**
    * If you need to declare any local variables or methods,
    * you can do it here.
    */

   /**
    * a short description of my variable
    */
    protected int mMyVariable;

    //-----------------------------------------------------------------------
    // CONSTRUCTORS:
    //-----------------------------------------------------------------------

   /**
    * Constructor of JMyCustomDevice.
    * @param aDeviceNumber - Number of the device
    */
    public JMyCustomDevice( int aDeviceNumber )
    {
        // the connection to your device has not yet been established.
        mSystemReady = false;


        /************************************************************************
            STEP 1:
            Complete the specifications of your device.
            These values are estimates. Since haptic devices often perform
            differently depending of their configuration with their workspace,
            simply use average values.
        *************************************************************************/

        // name of the device manufacturer
        mSpecifications.mManufacturerName              = "My Name";

        // name of your device
        mSpecifications.mModelName                     = "My Custom Device";

        // the maximum force in [N] along the x,y,z axis.
        mSpecifications.mMaxForce                      = 5.0; // [N]

        // the maximum stiffness in [N/m] along the x,y,z axis
        mSpecifications.mMaxForceStiffness             = 1000.0; // [N/m]

        // the maximum amount of torque your device can provide on the
        // rotation degrees of freedom.
        mSpecifications.mMaxTorque                     = 0.2;  // [N*m]

        // the maximum amount of angular stiffness
        mSpecifications.mMaxTorqueStiffness            = 1.0;  // [N*m/Rad]

        // the maximum amount of torque which can be provided my your gripper
        mSpecifications.mMaxGripperTorque              = 0.4;  // [N]

        // the maximum stiffness for the gripper
        mSpecifications.mMaxGripperTorqueStiffness     = 1.0;  // [N*m/m]

        // the radius of the physiqual workspace of the device (x,y,z axis)
        mSpecifications.mWorkspaceRadius               = 0.2; // [m]

        // does your device provide sensed position (x,y,z axis)?
        mSpecifications.mSensedPosition                = true;

        // does your device provide sensed rotations (i.e stylus)?
        mSpecifications.mSensedRotation                = true;

        // does your device provide a gripper which can be sensed?
        mSpecifications.mSensedGripper                 = true;

        // is you device actuated on the translation degrees of freedom?
        mSpecifications.mActuatedPosition              = true;

        // is your device actuated on the rotation degrees of freedom?
        mSpecifications.mActuatedRotation              = true;

        // is the gripper of your device actuated?
        mSpecifications.mActuatedGripper               = true;

        // can the device be used with the left hand?
        mSpecifications.mLeftHand                      = true;

        // can the device be used with the right hand?
        mSpecifications.mRightHand                     = true;


        /************************************************************************
            STEP 2:
            Here, you need to implement code which tells the application if your
            device is connected to your computer and can be accessed.
            In practice this may be consist in checking if your I/O board
            is active or if a USB connection is available.

            If your device can be accessed, set:
            m_systemAvailable = true;

            If your device cannot be accessed set:
            m_systemAvailable = false;
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***

        mSystemAvailable = false; // default value.
    }

   /**
    * Constructor of JMyCustomDevice.
    */
    public JMyCustomDevice()
    {
        this(0);
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------

   /**
    * Open connection to haptic device (0 indicates success).
    */
    @Override
    public int open()
    {
        // check if the system is available
        if (!mSystemAvailable) return (-1);

        // if system is already opened then return
        if (mSystemReady) return (0);

        /************************************************************************
            STEP 3:
            Here you need to implement code which open the connection to your
            device. This may include opening a connection to an interface board
            for instance.

            If the connection fails, simply set the variable 'result' to false.
            If the connection succeeds, set the variable 'result' to true.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***

        boolean result = false;

        // update device status
        if (result)
        {
            mSystemReady = true;
            return (0);
        }
        else
        {
            mSystemReady = false;
            return (-1);
        }
    }

   /**
    * Close connection to your device.
    */
    @Override
    public int close()
    {
        // check if the system has been opened previously
        if (!mSystemReady) return (-1);

        /************************************************************************
            STEP 4:
            Here you need to implement code which closes the connection to your
            device.

            If the operation fails, simply set the variable 'result' to 0.
            If the connection succeeds, set the variable 'result' to any
            negative error value you may want to return. By default: -1.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***
        int result = 0;

        // update status
        mSystemReady = false;

        return (result);
    }

   /**
    * Calibrate your device.
    * @param aResetEncoders - Ignored; exists for forward compatibility.
    * @return - Always 0
    */
    @Override
    public int initialize(final boolean aResetEncoders)
    {
        /************************************************************************
            STEP 5:
            Here you may implement any code which is called after opening
            a connection to your device. This could for instance be some
            commands to reset the encoders of your device.

            If the operation fails, you can return a negative error code.
            Otherwise return 0 if the operation succeeds.

            Please ignore the  a_resetEncoders value passed by parameter.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***
        int error = 0;
        return (error);
    }

   /**
    * Calibrate your device.
    * @return - Always 0
    */
    public int initialize()
    {
        return initialize(false);
    }

   /**
    * Returns the number of devices available from this class of device.
    * @return - Returns the result
    */
    @Override
    public int getNumDevices()
    {
        /************************************************************************
            STEP 6:
            Here you may implement code which returns the number of available
            haptic devices of type "JMyCustomDevice" which are currently connected
            to your computer.

            In practice you will often have either 0 or 1 device. In which case
            the code here bellow is already implemented for you.

            If you have more than 1 of your devices that can be connected,
            simply modify the code accordingly so that "numberOfDevices" takes
            the correct value.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE, MODIFY CODE BELLOW ACCORDINGLY ***

        int numberOfDevices;
        if (mSystemAvailable)
        {
            // at least one device is available!
            numberOfDevices = 1;
        }
        else
        {
            // no devices are available
            numberOfDevices = 0;
        }

        return (numberOfDevices);
    }

   /**
    * Read the position of your device. Units are meters [m].
    * @param aPosition - Return value.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int getPosition(JVector3d aPosition)
    {
        // temp variables
        int error = 0;
        double x,y,z;

        /************************************************************************
            STEP 7:
            Here you may implement code which reads the position (X,Y,Z) from
            your haptic device. Read the values from your device and modify
            the local variable (x,y,z) accordingly.
            If the operation fails return an error code.

            Note:
            For consistency, units must be in meters.
            If your device is located in front of you, the x-axis is pointing
            towards you (the operator). The y-axis points towards your right
            hand side and the z-axis points up towards the sky.

        *************************************************************************/

        // *** INSERT YOUR CODE HERE, MODIFY CODE BELLOW ACCORDINGLY ***

        x = 0.0;
        y = 0.0;
        z = 0.0;

        // store new position values
        aPosition.set(x, y, z);

        // estimate linear velocity
        estimateLinearVelocity(aPosition);

        // exit
        return (error);
    }

   /**
    * Read the orientation frame of your device end-effector
    * @param aRotation - Return value.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int getRotation(JMatrix3d aRotation)
    {
        // temp variables
        int error = -1;
        double r00, r01, r02, r10, r11, r12, r20, r21, r22;
        JMatrix3d frame = new JMatrix3d();
        frame.identity();

        /************************************************************************
            STEP 7:
            Here you may implement code which reads the orientation frame from
            your haptic device. The orientation frame is expressed by a 3x3
            rotation matrix. The 1st column of this matrix corresponds to the
            x-axis, the 2nd column to the y-axis and the 3rd column to the z-axis.
            The length of each column vector should be of length 1 and vectors need
            to be perpendicular to each other.
            If the operation fails return an error code.

            Note:
            If your device is located in front of you, the x-axis is pointing
            towards you (the operator). The y-axis points towards your right
            hand side and the z-axis points up towards the sky.

            If your device has a stylus, make sure that you set the reference frame
            so that the x-axis corresponds to the axis of the stylus.

        *************************************************************************/

        // *** INSERT YOUR CODE HERE, MODIFY CODE BELLOW ACCORDINGLY ***

        // by default we set the rotation matrix equal to the identiy matrix.
        r00 = 1.0;  r01 = 0.0;  r02 = 0.0;
        r10 = 0.0;  r11 = 1.0;  r12 = 0.0;
        r20 = 0.0;  r21 = 0.0;  r22 = 1.0;

        frame.set(r00, r01, r02, r10, r11, r12, r20, r21, r22);

        // store new rotation matrix
        aRotation = frame;

        // estimate angular velocity
        estimateAngularVelocity(aRotation);

        // exit
        return (error);
    }

   /**
    * Read the gripper angle in radian.
    * @param aAngle - Return value.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int getGripperAngleRad(Double aAngle)
    {
        // temp variables
        int error = 0;

        /************************************************************************
            STEP 8:
            Here you may implement code which reads the position angle of your
            gripper. The result must be returned in radian.

            If the operation fails return an error code.

        *************************************************************************/

        // *** INSERT YOUR CODE HERE, MODIFY CODE BELLOW ACCORDINGLY ***

        // return gripper angle
        aAngle = 0.0;

        // estimate gripper velocity
        estimateGripperVelocity(aAngle);

        // exit
        return (error);
    }

   /**
    * Send a force [N] to your haptic device
    * @param aForce - Force command to be applied to device.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int setForce(JVector3d aForce)
    {
        // temp variable
        int error = 0;

        // store new force value.
        setPrevForce(aForce);

        /************************************************************************
            STEP 9:
            Here you may implement code which sends a force command (fx,fy,fz)
            to your haptic device.
            If the operation fails return an error code.

            Note:
            For consistency, units must be in Newtons.
            If your device is located in front of you, the x-axis is pointing
            towards you (the operator). The y-axis points towards your right
            hand side and the z-axis points up towards the sky.

            For instance: if the force = (1,0,0), the device will move towards
            the operator, if the force = (0,0,1), the device will move upwards.

        *************************************************************************/


        // *** INSERT YOUR CODE HERE ***

        //double fx = a_force.x;
        //double fy = a_force.y;
        //double fz = a_force.z;

        // exit
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
        // temp variable
        int error = 0;

        // store new torque values
        setPrevTorque(aTorque);

        /************************************************************************
            STEP 10:
            Here you may implement code which sends a torque command (Tx,Ty,Tz)
            to your haptic device. This would be implemented if you have
            a haptic device with an active stylus for instance.
            If the operation fails return an error code.

            Note:
            For consistency, units must be in Newton meters.
            A torque (1,0,0) would rotate counter clock-wise around the x-axis.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***

        // double tx = a_torque.x;
        // double ty = a_torque.y;
        // double tz = a_torque.z;

        // exit
        return (error);
    }

   /**
    * Send a torque [N*m] to the gripper
    * @param aGripperTorque - Torque command to be sent to gripper.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int setGripperTorque(double aGripperTorque)
    {
        // temp variable
        int error = 0;

        // store new gripper torque value
        setPrevGripperTorque(aGripperTorque);

        /************************************************************************
            STEP 11:
            Here you may implement code which sends a torque command to the
            gripper of your haptic device.
            If the operation fails return an error code.

            Note:
            For consistency, units must be in Newton meters.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***

        // double torque = a_gripperTorque;

        // exit
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
    public int setForceAndTorqueAndGripper(JVector3d aForce, JVector3d aTorque, double aGripperTorque)
    {
        // temp variables
        int error = 0;
        int result = 0;

        /************************************************************************
            STEP 11:
            Here you may implement code which send a force, torque and gripper
            command at once. For some haptic devices, this is a more optimal
            way of proceeding which limits the amount of data being transfered
            over to the device.

            You may ignore this section in which case the individuals methods
            are called consecutively.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE, MODIFY CODE BELLOW ACCORDINGLY ***

        // send a force command
        setPrevForce(aForce);
        error = setForce(aForce);
        if (error != 0) { result = error; }

        // send a torque command
        setPrevTorque(aTorque);
        error = setTorque(aTorque);
        if (error != 0) { result = error; }

        // send a gripper torque command
        setPrevGripperTorque(aGripperTorque);
        error = setGripperTorque(aGripperTorque);
        if (error != 0) { result = error; }

        // exit
        return (result);
    }

   /**
    * Read the status of the user switch [\b true = \e ON / \b false = \e OFF].
    * @param aSwitchIndex - index number of the switch.
    * @param aStatus - result value from reading the selected input switch.
    * @return - Return 0 if no error occurred.
    */
    @Override
    public int getUserSwitch(int aSwitchIndex, Boolean aStatus)
    {
        int error = 0;

        /************************************************************************
            STEP 12:
            Here you may implement code which reads the status of one or
            more user switches. An application may request to read the status
            of a switch by passing its index number. The primary user switch mounted
            on the stylus of a haptic device will receive the index number 0.

            The return value of a switch (a_status) shall be equal to \b true if the button
            is pressed or \b false otherwise.
        *************************************************************************/

        // *** INSERT YOUR CODE HERE ***

        aStatus = false;

        return (error);
    }
    
}
