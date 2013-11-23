/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.devices2;

/**
 *
 * @author Usuário
 */
public class JHapticDeviceSpecifications implements Cloneable{

    /**
     *
     */
    protected String model;
    /**
     * The manufacturer
     */
    protected String manufacturer;
    /**
     * Maximum force in [N] that can be produced by the device in translation.
     */
    protected double maximumForce;
    /**
     * Maximum torque in [N*m] that can be produced by the device in
     * orientation.
     */
    protected double maximumTorque;
    /**
     * Maximum force in [N*m] that can be produced by the gripper
     */
    protected double maximumGripperTorque;
    /**
     * Maximum closed loop force stiffness [N/m] for a simulation running at 1
     * KhZ.
     */
    protected double maximumForceStiffness;
    /**
     * Maximum closed loop torque stiffness [N*m/rad] for a simulation running
     * at 1 KhZ.
     */
    protected double maximumTorqueStiffness;
    /**
     * Maximum closed loop gripper torque stiffness [N*m/rad] for a simulation
     * running at 1 KhZ.
     */
    protected double maximumGripperTorqueStiffness;
    /**
     * Maximum recommended linear damping factor Kv when using the getVelocity()
     * method from the device class
     */
    protected double maximumLinearDamping;
    /**
     * Radius which describes the largest sphere (3D devices) or circle (2D
     * Devices) which can be enclosed inside the physical workspace of the
     * device.
     */
    protected double workspaceRadius;
    /**
     * If true then device supports position sensing (x,y,z axis)
     */
    protected boolean positionSensingSupported;
    /**
     * If true thhen device supports rotation sensing. (i.e stylus, pen)
     */
    protected boolean rotationSensingSupported;
    /**
     * If true then device supports a sensed gripper interface
     */
    protected boolean sensedGripperSupported;
    /**
     * If true then device provides actuation capabilities on the translation
     * degrees of freedom. (x,y,z axis).
     */
    protected boolean positionActuationSupported;
    /**
     * If true then device provides actuation capabilities on the orientation
     * degrees of freedom. (i.e stylus, pen)
     */
    protected boolean actuatedRotationSupported;
    /**
     * If true then device provides actuation capabilities on the gripper.
     */
    protected boolean actuatedGripperSupported;
    /**
     * If true then the device can used for left hands
     */
    protected boolean leftHand;
    /**
     * If true then the device can used for left hands
     */
    protected boolean rightHand;

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return the maximumForce
     */
    public double getMaximumForce() {
        return maximumForce;
    }

    /**
     * @param maximumForce the maximumForce to set
     */
    public void setMaximumForce(double maximumForce) {
        this.maximumForce = maximumForce;
    }

    /**
     * @return the maximumTorque
     */
    public double getMaximumTorque() {
        return maximumTorque;
    }

    /**
     * @param maximumTorque the maximumTorque to set
     */
    public void setMaximumTorque(double maximumTorque) {
        this.maximumTorque = maximumTorque;
    }

    /**
     * @return the maximumGripperTorque
     */
    public double getMaximumGripperTorque() {
        return maximumGripperTorque;
    }

    /**
     * @param maximumGripperTorque the maximumGripperTorque to set
     */
    public void setMaximumGripperTorque(double maximumGripperTorque) {
        this.maximumGripperTorque = maximumGripperTorque;
    }

    /**
     * @return the maximumForceStiffness
     */
    public double getMaximumForceStiffness() {
        return maximumForceStiffness;
    }

    /**
     * @param maximumForceStiffness the maximumForceStiffness to set
     */
    public void setMaximumForceStiffness(double maximumForceStiffness) {
        this.maximumForceStiffness = maximumForceStiffness;
    }

    /**
     * @return the maximumTorqueStiffness
     */
    public double getMaximumTorqueStiffness() {
        return maximumTorqueStiffness;
    }

    /**
     * @param maximumTorqueStiffness the maximumTorqueStiffness to set
     */
    public void setMaximumTorqueStiffness(double maximumTorqueStiffness) {
        this.maximumTorqueStiffness = maximumTorqueStiffness;
    }

    /**
     * @return the maximumGripperTorqueStiffness
     */
    public double getMaximumGripperTorqueStiffness() {
        return maximumGripperTorqueStiffness;
    }

    /**
     * @param maximumGripperTorqueStiffness the maximumGripperTorqueStiffness to
     * set
     */
    public void setMaximumGripperTorqueStiffness(double maximumGripperTorqueStiffness) {
        this.maximumGripperTorqueStiffness = maximumGripperTorqueStiffness;
    }

    /**
     * @return the maximumLinearDamping
     */
    public double getMaximumLinearDamping() {
        return maximumLinearDamping;
    }

    /**
     * @param maximumLinearDamping the maximumLinearDamping to set
     */
    public void setMaximumLinearDamping(double maximumLinearDamping) {
        this.maximumLinearDamping = maximumLinearDamping;
    }

    /**
     * @return the workspaceRadius
     */
    public double getWorkspaceRadius() {
        return workspaceRadius;
    }

    /**
     * @param workspaceRadius the workspaceRadius to set
     */
    public void setWorkspaceRadius(double workspaceRadius) {
        this.workspaceRadius = workspaceRadius;
    }

    /**
     * @return the positionSensingSupported
     */
    public boolean isPositionSensingSupported() {
        return positionSensingSupported;
    }

    /**
     * @param positionSensingSupported the positionSensingSupported to set
     */
    public void setPositionSensingSupported(boolean positionSensingSupported) {
        this.positionSensingSupported = positionSensingSupported;
    }

    /**
     * @return the rotationSensingSupported
     */
    public boolean isRotationSensingSupported() {
        return rotationSensingSupported;
    }

    /**
     * @param rotationSensingSupported the rotationSensingSupported to set
     */
    public void setRotationSensingSupported(boolean rotationSensingSupported) {
        this.rotationSensingSupported = rotationSensingSupported;
    }

    /**
     * @return the sensedGripperSupported
     */
    public boolean isSensedGripperSupported() {
        return sensedGripperSupported;
    }

    /**
     * @param sensedGripperSupported the sensedGripperSupported to set
     */
    public void setSensedGripperSupported(boolean sensedGripperSupported) {
        this.sensedGripperSupported = sensedGripperSupported;
    }

    /**
     * @return the positionActuationSupported
     */
    public boolean isPositionActuationSupported() {
        return positionActuationSupported;
    }

    /**
     * @param positionActuationSupported the positionActuationSupported to set
     */
    public void setPositionActuationSupported(boolean positionActuationSupported) {
        this.positionActuationSupported = positionActuationSupported;
    }

    /**
     * @return the actuatedRotationSupported
     */
    public boolean isActuatedRotationSupported() {
        return actuatedRotationSupported;
    }

    /**
     * @param actuatedRotationSupported the actuatedRotationSupported to set
     */
    public void setActuatedRotationSupported(boolean actuatedRotationSupported) {
        this.actuatedRotationSupported = actuatedRotationSupported;
    }

    /**
     * @return the actuatedGripperSupported
     */
    public boolean isActuatedGripperSupported() {
        return actuatedGripperSupported;
    }

    /**
     * @param actuatedGripperSupported the actuatedGripperSupported to set
     */
    public void setActuatedGripperSupported(boolean actuatedGripperSupported) {
        this.actuatedGripperSupported = actuatedGripperSupported;
    }

    /**
     * @return the leftHand
     */
    public boolean isLeftHand() {
        return leftHand;
    }

    /**
     * @param leftHand the leftHand to set
     */
    public void setLeftHand(boolean leftHand) {
        this.leftHand = leftHand;
    }

    /**
     * @return the rightHand
     */
    public boolean isRightHand() {
        return rightHand;
    }

    /**
     * @param rightHand the rightHand to set
     */
    public void setRightHand(boolean rightHand) {
        this.rightHand = rightHand;
    }

    @Override
    public Object clone() {
        JHapticDeviceSpecifications specs = new JHapticDeviceSpecifications();
        
        specs.actuatedGripperSupported = this.actuatedGripperSupported;
        specs.actuatedRotationSupported = this.actuatedRotationSupported;
        specs.leftHand = this.leftHand;
        specs.manufacturer = this.manufacturer;
        specs.maximumForce = this.maximumForce;
        specs.maximumForceStiffness = this.maximumForceStiffness;
        specs.maximumGripperTorque = this.maximumGripperTorque;
        specs.maximumGripperTorqueStiffness = this.maximumGripperTorqueStiffness;
        specs.maximumLinearDamping = this.maximumLinearDamping;
        specs.maximumTorque = this.maximumTorque;
        specs.maximumTorqueStiffness = this.maximumTorqueStiffness;
        specs.model = this.model;
        specs.positionActuationSupported = this.positionActuationSupported;
        specs.positionSensingSupported = this.positionSensingSupported;
        specs.sensedGripperSupported = this.sensedGripperSupported;
        specs.workspaceRadius = this.workspaceRadius;
        return specs;
    }
}
