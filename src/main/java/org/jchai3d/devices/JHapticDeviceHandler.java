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
 * This class implements a manager which lists the different devices available
 * on your computer and provides handles to them.
 *
 * @author felipe
 */
public class JHapticDeviceHandler {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    /**
     * Number of devices.
     */
    private int mNumDevices;
    /**
     * Array of available haptic devices.
     */
    private JGenericHapticDevice[] mDevices = new JGenericHapticDevice[CHAI_MAX_HAPTIC_DEVICES];
    /**
     * A default device with no functionalities.
     */
    private JGenericHapticDevice mNullHapticDevice;
    /**
     * Maximum number of devices that can be connected at the same time.
     */
    public static final int CHAI_MAX_HAPTIC_DEVICES = 16;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    public JHapticDeviceHandler() {
        // clear number of devices
        mNumDevices = 0;

        // create a null haptic device. a pointer to this device is returned
        // if no device is found. this insures that applications which forget
        // to address the case when no device is connected start sending commands
        // to a null pointer...
        mNullHapticDevice = new JGenericHapticDevice();

        // clear device table
        int i;
        for (i = 0; i < CHAI_MAX_HAPTIC_DEVICES; i++) {
            mDevices[i] = null;
        }

        //System.getProperty("path");
        //System.setProperty("jna.library.path", "C:/lib");
        //System.setProperty("java.library.path", "C:/lib");

    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Returns the number of devices connected to your computer
     *
     * @return
     */
    public int getNumDevices() {
        return (mNumDevices);
    }

    /**
     * Updates information regarding the devices that are connected to your
     * computer.
     */
    public void update() {
        System.out.println("Searching devices...");
        // temp variables
        int index, count = 0, i;
        JGenericHapticDevice device = null;

        // clear current list of devices
        mNumDevices = 0;
        for (i = 0; i < CHAI_MAX_HAPTIC_DEVICES; i++) {
            mDevices[i] = null;
        }

        //-----------------------------------------------------------------------
        // search for Force Dimension devices
        //-----------------------------------------------------------------------

        // reset index number
        index = 0;

        // create a first device of this class
        device = new JDeltaDevice(index);

        // check for how many devices of this type that are available
        count = device.getNumDevices();
        System.out.println(" >Found " + count + " Force Dimension devices.");

        // if there are one or more devices available, then store them in the device table
        if (count > 0) {
            // store first device
            mDevices[mNumDevices] = device;
            mNumDevices++;

            // search for other devices
            if (count > 1) {
                for (i = 1; i < count; i++) {
                    index++;
                    device = new JDeltaDevice(index);
                    mDevices[mNumDevices] = device;
                    mNumDevices++;
                }
            }
        }

        //-----------------------------------------------------------------------
        // search for Novint Falcon device
        //-----------------------------------------------------------------------

        // reset index number
        index = 0;

        // create a first device of this class
        device = new JFalconDevice(index);

        // check for how many devices of this type that are available
        count = device.getNumDevices();
        System.out.println(" >Found " + count + " Falcon devices.");

        // if there are one or more devices available, then store them in the device table
        if (count > 0) {
            // store first device
            mDevices[mNumDevices] = device;
            device.open();
            mNumDevices++;

            // search for other devices
            if (count > 1) {
                for (i = 1; i < count; i++) {
                    index++;
                    device = new JFalconDevice(index);
                    device.open();
                    mDevices[mNumDevices] = device;
                    mNumDevices++;
                }
            }
        }

        //-----------------------------------------------------------------------
        // search for MPB Technologies devices
        //-----------------------------------------------------------------------

        // reset index number
        index = 0;

        // create a first device of this class
        device = new JFreedom6SDevice();

        // check for how many devices of this type that are available
        count = device.getNumDevices();
        System.out.println(" >Found " + count + " MPB Technologies devices.");

        // if there is one device available, then store it in the device table
        if (count > 0) {
            // store first device
            mDevices[mNumDevices] = device;
            mNumDevices++;
        }

        //-----------------------------------------------------------------------
        // search for Sensable Technologies devices
        //-----------------------------------------------------------------------

        // reset index number
        index = 0;

        // create a first device of this class
        device = new JPhantomDevice(index);

        // check for how many devices of this type that are available
        count = device.getNumDevices();

        System.out.println(" >Found " + count + " Sensable devices.");

        // if there are one or more devices available, then store them in the device table
        if (count > 0) {
            // store first device
            mDevices[mNumDevices] = device;
            mNumDevices++;

            // search for other devices
            if (count > 1) {
                for (i = 1; i < count; i++) {
                    index++;
                    device = new JPhantomDevice(index);
                    mDevices[mNumDevices] = device;
                    mNumDevices++;
                }
            }
        }

        //-----------------------------------------------------------------------
        // search for MyCustom device
        //-----------------------------------------------------------------------

        System.out.println("Loading custom devices.");
        // reset index number
        index = 0;

        // create a first device of this class
        device = new JMyCustomDevice(index);

        // check for how many devices of this type that are available
        count = device.getNumDevices();

        System.out.println(" >Found " + count + " custom devices.");

        // if there are one or more devices available, then store them in the device table
        if (count > 0) {
            // store first device
            mDevices[mNumDevices] = device;
            mNumDevices++;

            // search for other devices
            if (count > 1) {
                for (i = 1; i < count; i++) {
                    index++;
                    device = new JMyCustomDevice(index);
                    mDevices[mNumDevices] = device;
                    mNumDevices++;
                }
            }
        }

        //-----------------------------------------------------------------------
        // search for JCHAI 3D Virtual Device
        // Note:
        // Virtual devices should always be listed last. The desired behavior
        // is that an application first searches for physical devices. If none
        // are found, it may launch a virtual device
        //-----------------------------------------------------------------------

        if (mNumDevices == 0) {
            System.out.println("Loading virtual devices.");
            // reset index number
            index = 0;

            // create a first device of this class
            device = new JVirtualDevice();

            // check for how many devices of this type that are available
            count = device.getNumDevices();

            // if there are one or more devices available, then store it in the device table
            if (count > 0) {
                // store first device
                mDevices[mNumDevices] = device;
                System.out.println(" > VirtualDevice already running.");
                mNumDevices++;
                return;
            } // if no devices have been found then we try to launch a virtual haptic device
            else if (mNumDevices == 0) {
                // we try to launch the virtual device.
                try {
                    Runtime.getRuntime().exec("VirtualDevice.exe");
                    Thread.sleep(750);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // create again a first device of this class
                device = new JVirtualDevice();

                // check for how many devices of this type that are available
                count = device.getNumDevices();

                // if there are one or more devices available, then store it in the device table
                if (count > 0) {
                    // store first device
                    mDevices[mNumDevices] = device;
                    System.out.println(" > VirtualDevice sucessfuly launched.");
                    mNumDevices++;
                }
            }
        }

    }

    /**
     * Returns the specifications of the ith device.
     *
     * @param aDeviceSpecifications - Returned result
     * @return - Return 0 if no error occurred.
     */
    public int getDeviceSpecifications(JHapticDeviceInfo aDeviceSpecifications) {
        return getDeviceSpecifications(aDeviceSpecifications, 0);
    }

    /**
     * Returns the specifications of the ith device.
     *
     * @param aDeviceSpecifications - Returned result
     * @param aIndex - Index number of the device.
     * @return - Return 0 if no error occurred.
     */
    public int getDeviceSpecifications(JHapticDeviceInfo aDeviceSpecifications, int aIndex) {
        if (aIndex < mNumDevices) {
            aDeviceSpecifications.copyFrom(mDevices[aIndex].getSpecifications());
            return (0);
        } else {
            return (-1);
        }
    }

    /**
     * Returns a handle to the ith device if available.
     *
     * @param aHapticDevice - Handle to device
     * @return - Return 0 if no error occurred.
     */
    public JGenericHapticDevice getDevice() {
        return getDevice(0);
    }

    /**
     * Returns a handle to the ith device if available.
     *
     * @param aIndex - Index number of the device.
     * @return - Handle to device
     */
    public JGenericHapticDevice getDevice(int aIndex) {
        JGenericHapticDevice aHapticDevice = null;

        if (aIndex < mNumDevices) {
            aHapticDevice = mDevices[aIndex];
        } else {
            aHapticDevice = mNullHapticDevice;
        }

        return aHapticDevice;
    }
}
