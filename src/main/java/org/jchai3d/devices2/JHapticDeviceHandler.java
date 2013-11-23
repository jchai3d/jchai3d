package org.jchai3d.devices2;

/**
 *
 * @author Marcos da Silva Ramos
 */
public abstract class JHapticDeviceHandler {

    protected boolean currentPlatformSupported;
    protected JPlatform currentPlatform;

    public JHapticDeviceHandler(JPlatform platform) {
        this.currentPlatform = platform;
        this.currentPlatformSupported = false;
    }

    /**
     * Here is where the driver is loaded
     */
    public abstract void initialize();

    /**
     * Update devices info
     */
    public abstract void updateDevices();

    /**
     *
     * @param deviceIndex
     * @return
     */
    public abstract JHapticDevice getHapticDevice(int deviceIndex);

    /**
     *
     * @return the first available device, or null if no device is available
     */
    public abstract JHapticDevice getFirstAvailableHapticDevice();

    /**
     *
     * @return the number of available devices
     */
    public abstract int getDeviceCount();

    /**
     *
     * @return
     */
    public abstract JHapticDeviceSpecifications[] getHapticDeviceProperties();

    /**
     *
     * @param platform
     * @return
     */
    public abstract boolean isPlatformSupported(JPlatform platform);

    /**
     * Implementation of this method should verify if the driver library(or the
     * communication method) is available.
     *
     * @return
     */
    public abstract boolean isAvailable();

    /**
     *
     * @param platform
     * @return
     */
    public boolean isCurrentPlatformSupported() {
        return this.currentPlatformSupported;
    }
}
