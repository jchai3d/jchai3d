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
package org.jchai3d.tools;

import java.nio.FloatBuffer;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.devices.JGenericHapticDevice;
import org.jchai3d.forces.JInteractionEvent;
import org.jchai3d.forces.JPotentialFieldForceAlgo;
import org.jchai3d.forces.JProxyPointForceAlgo;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JMaterial;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.*;

/**
 * Represents a haptic tool that can apply forces in three degrees of freedom
 * and maintains three or six degrees of device pose.
 *
 * @author Jairo Melo
 * @author Marcos da Silva Ramos
 */
public class JGeneric3dofPointer extends JGenericTool {

    /**
     * Sphere representing the device.
     */
    protected JShapeSphere deviceSphere;
    /**
     * Sphere representing the proxy.
     */
    protected JShapeSphere proxySphere;
    /**
     * Mesh representing the device.
     */
    protected JMesh deviceMesh;
    /**
     * Use device mesh?
     */
    protected boolean deviceMeshEnabled;
    /**
     * Mesh representing the proxy.
     */
    protected JMesh proxyMesh;
    /**
     * Color of line connecting proxy and device position together.
     */
    protected JColorf lineColor;
    /**
     * Material properties of proxy.
     */
    protected JMaterial proxyMaterial;
    /**
     * Material properties of device sphere
     */
    protected JMaterial materialDevice;
    /**
     * Material properties of proxy when button is pressed.
     */
    protected JMaterial buttonPressedProxyMaterial;
    /**
     * Finger-proxy algorithm model to handle interactions with mesh objects.
     */
    protected JProxyPointForceAlgo proxyPointForceModel;
    /**
     * Potential fields model.
     */
    protected JPotentialFieldForceAlgo potentialFieldsForceModel;

    /*
     * <p>The last force computed for application to this tool, in the world
     * coordinate system. [N] </p>
     *
     * <p>If you want to manually send forces to a device, you can modify this
     * value before calling 'applyForces'.</p>
     */
    protected JVector3d lastComputedGlobalForce;

    /*
     * The last force computed for application to this tool, in the device
     * coordinate. system. [N]
     */
    protected JVector3d lastComputedLocalForce;
    /**
     * Radius of the workspace which can be accessed by the tool.
     */
    private double workspaceRadius;
    /**
     * Scale factor between the sizes of the tool workspace and the haptic
     * device workspace.
     */
    private double workspaceScaleFactor;
    /**
     * Position of device in device local coordinate system.
     */
    protected JVector3d deviceLocalPosition;
    /**
     * Position of device in world global coordinate system.
     */
    protected JVector3d deviceGlobalPosition;
    /**
     * Velocity of device in device local coordinate system.
     */
    protected JVector3d deviceLocalVelocity;
    /**
     * Velocity of device in world global coordinate system.
     */
    protected JVector3d deviceGlobalVelocity;
    /**
     * Orientation of wrist in local coordinates of device.
     */
    protected JMatrix3d deviceLocalRotation;
    /**
     * Orientation of wrist in global coordinates of device.
     */
    protected JMatrix3d deviceGlobalRotation;
    /**
     * World in which tool is interacting.
     */
    protected JWorld world;
    /**
     * Radius of sphere representing position of pointer.
     */
    protected double displayRadius;
    /**
     * Last status of user switch 0. This value is used by the graphical
     * rendering function.
     */
    protected boolean userSwitch0;
    /**
     * This flag records whether the user has enabled forces.
     */
    protected boolean forcesEnabled;
    /**
     * Flag to avoid initial bumps in force (has the user sent a _small_ force
     * yet?).
     */
    protected boolean forceStarted;

    /*
     * Normally this class waits for a very small force before initializing
     * forces to avoid initial "jerks" (a safety feature); you can bypass that
     * requirement with this variable.
     */
    protected boolean waitForSmallForce;
    
    /**
     * Constructor of cGeneric3dofPointer.
     */
    public JGeneric3dofPointer(JWorld aWorld) {
        // this makes sure that we do not send a huge force to the device
        // as soon as the demo starts.
        waitForSmallForce = true;

        // default radius of tip of tool
        displayRadius = 0.001;

        // set world
        world = aWorld;

        // set a default device for the moment
        hapticDevice = new JGenericHapticDevice();


        //-------------------------------------------------------------------
        // FORCE MODELS SUPPORTED BY THE TOOL
        //-------------------------------------------------------------------

        // create a default proxy algorithm force renderer
        proxyPointForceModel = new JProxyPointForceAlgo();

        // initialize proxy model
        proxyPointForceModel.initialize(world, new JVector3d(0, 0, 0));
        proxyPointForceModel.setProxyRadius(displayRadius);

        // create a default potential field force renderer
        potentialFieldsForceModel = new JPotentialFieldForceAlgo();

        // initialize potential field model
        potentialFieldsForceModel.initialize(world, new JVector3d(0, 0, 0));

        // force settings
        forcesEnabled = true;
        forceStarted = false;


        //-------------------------------------------------------------------
        // GRAPHICAL MODEL OF THE TOOL
        //-------------------------------------------------------------------

        // set the default material properties for the sphere representing  the proxy
        proxyMaterial = new JMaterial();
        proxyMaterial.getAmbient().set(1f, 0.f, 0.f, 1f);
        proxyMaterial.getDiffuse().set(1f, 0.f, 0.f, 1f);
        proxyMaterial.getSpecular().set(1.0f, 0.0f, 0.0f, 1.0f);

        // set the default material properties for proxy when the user switch is engaged
        buttonPressedProxyMaterial = new JMaterial();
        buttonPressedProxyMaterial.getAmbient().set(1.0f, 0.0f, 0.0f, 1.0f);
        buttonPressedProxyMaterial.getDiffuse().set(1.0f, 0.0f, 0.0f, 1.0f);
        buttonPressedProxyMaterial.getSpecular().set(1.0f, 0.0f, 0.0f, 1.0f);

        // set the default material properties for the sphere representing the device
        materialDevice = new JMaterial();
        materialDevice.getAmbient().set(0.0f, 1.0f, 0.0f, 1.0f);
        materialDevice.getDiffuse().set(0.0f, 1.0f, 0.0f, 1.0f);
        materialDevice.getSpecular().set(0.0f, 1.0f, 0.0f, 1.0f);

        // Sphere representing the device
        deviceSphere = new JShapeSphere(0.019);
        deviceSphere.setMaterial(materialDevice);
        deviceSphere.setHapticEnabled(false, false);
        addChild(deviceSphere);

        // Sphere representing the proxy
        proxySphere = new JShapeSphere(0.020);
        proxySphere.setHapticEnabled(false, false);
        proxySphere.setMaterial(proxyMaterial);
        addChild(proxySphere);

        // Mesh representing the device
        deviceMesh = new JMesh(world);
        deviceMesh.setHapticEnabled(false, false);
        deviceSphere.addChild(deviceMesh);

        // Mesh representing the proxy
        proxyMesh = new JMesh(world);
        proxyMesh.setHapticEnabled(false, false);
        proxySphere.addChild(proxyMesh);

        // Default color of the line which connects the device and the proxy spheres
        lineColor = new JColorf();
        lineColor.set(0.7f, 0.7f, 0.7f, 0.7f);

        // This sets both the rendering radius and the actual proxy radius
        setRadius(0.05);

        //-------------------------------------------------------------------
        // WORKSPACE AND POSITION
        //-------------------------------------------------------------------

        // set workspace size
        workspaceRadius = 1.0;
        workspaceScaleFactor = 1.0;

        // init device related variables
        deviceGlobalPosition = new JVector3d();
        deviceLocalPosition = new JVector3d();
        lastComputedLocalForce = new JVector3d();
        lastComputedGlobalForce = new JVector3d();
        deviceLocalVelocity = new JVector3d();
        deviceGlobalVelocity = new JVector3d();

        deviceGlobalRotation = new JMatrix3d();
        deviceLocalRotation = new JMatrix3d();
    }

    /**
     * Start communication with the device connected to the tool (0 indicates
     * success).
     */
    @Override
    public int start() {
        // check if device is available
        if (hapticDevice == null) {
            return -1;
        }

        int result;

        // open connection to device
        result = hapticDevice.open();

        // initialize device
        if (result == 0) {
            result = hapticDevice.initialize(false);
        }

        // resturn result
        return (result);
    }

    /**
     * Stop communication with the device connected to the tool (0 indicates
     * success).
     */
    @Override
    public int stop() {
        // check if device is available
        if (hapticDevice == null) {
            return -1;
        }

        // stop the device
        return hapticDevice.close();
    }

    /**
     * Initialize the device connected to the tool (0 indicates success).
     */
    @Override
    public int initialize(final boolean resetEncoders) {
        // check if device is available
        if (hapticDevice == null) {
            return (-1);
        }

        // initialize all force models
        proxyPointForceModel.initialize(world, deviceGlobalPosition);
        potentialFieldsForceModel.initialize(world, deviceGlobalPosition);

        // if device encoders need to be initialize, call initialize function here.
        // otherwise normal initialization is perform in the start() function
        if (resetEncoders) {
            hapticDevice.initialize(true);
        }

        // exit
        return 0;
    }

    /**
     * Toggle forces ON.
     */
    @Override
    public int setForcesON() {
        if (!forcesEnabled) {
            forceStarted = false;
            forcesEnabled = true;
        }
        return 0;
    }

    /**
     * Toggle forces OFF.
     */
    @Override
    public int setForcesOFF() {
        forcesEnabled = false;
        lastComputedLocalForce.zero();
        lastComputedGlobalForce.zero();
        applyForces();
        return 0;
    }

    JVector3d p = new JVector3d(), v = new JVector3d();
    /**
     * Update position and orientation of the device.
     */
    @Override
    public void updatePose() {

        // check if device is available
        if (hapticDevice == null) {
            return;
        }

        JVector3d pos = new JVector3d();
        JVector3d vel = new JVector3d();
        
        // read device position
        hapticDevice.getPosition(pos);

        // adjust for tool workspace scale factor
        pos.mulr(workspaceScaleFactor, deviceLocalPosition);
        

        // update global position of tool
        JVector3d tPos = new JVector3d();
        globalRotation.mulr(deviceLocalPosition, tPos);
        tPos.addr(globalPosition, deviceGlobalPosition);

        // read device orientation
        hapticDevice.getRotation(deviceLocalRotation);

        // update global orientation of tool
        deviceLocalRotation.mulr(globalRotation, deviceGlobalRotation);

        // read switches
        userSwitch0 = getUserSwitch(0);

        // read velocity of the device in local coordinates
        hapticDevice.getLinearVelocity(vel);

        // adjust for tool workspace scale factor
        vel.mulr(workspaceScaleFactor, deviceLocalVelocity);

        // update global velocity of tool
        globalRotation.mulr(deviceLocalVelocity, deviceGlobalVelocity);
        p.copyFrom(deviceGlobalPosition);
        v.copyFrom(deviceGlobalVelocity);
    }

    /**
     * Compute interaction forces with environment.
     */
    @Override
    public void computeInteractionForces() {
        // temporary variable to store forces
        JVector3d force = new JVector3d();

        // compute forces in world coordinates for each point force algorithm
        force.add(potentialFieldsForceModel.computeForces(deviceGlobalPosition, deviceGlobalVelocity));
        force.add(proxyPointForceModel.computeForces(deviceGlobalPosition, deviceGlobalVelocity));

        // copy result
        lastComputedGlobalForce.copyFrom(force);
    }

    
    /**
     * Apply latest computed forces to device.
     */
    @Override
    public void applyForces() {
        // check if device is available
        if (hapticDevice == null) {
            return;
        }

        // convert force into device local coordinates
        JMatrix3d tRot = new JMatrix3d();
        globalRotation.transr(tRot);
        tRot.mulr(lastComputedGlobalForce, lastComputedLocalForce);

        if ((!waitForSmallForce) || ((!forceStarted) && (lastComputedLocalForce.lengthsq() < 0.000001))) {
            forceStarted = true;
        }

        // send force to device
        if ((forcesEnabled) && (forceStarted)) {
            hapticDevice.setForce(lastComputedLocalForce);
        } else {
            hapticDevice.setForce(new JVector3d(0.0, 0.0, 0.0));
        }
    }

    /**
     * Check if the tool is touching a particular object.
     */
    @Override
    public boolean isInContact(JGenericObject aObject) {
        //-------------------------------------------------------------------
        // CHECK PROXY ALGORITHM
        //-------------------------------------------------------------------

        // contact 0
        if (proxyPointForceModel.getContactPoint0().getObject() == aObject) {
            return (true);
        }

        // contact 1
        if ((proxyPointForceModel.getContactPoint0().getObject() != null)
                && (proxyPointForceModel.getContactPoint1().getObject() == aObject)) {
            return (true);
        }

        // contact 2
        if ((proxyPointForceModel.getContactPoint0().getObject() != null)
                && (proxyPointForceModel.getContactPoint1().getObject() != null)
                && (proxyPointForceModel.getContactPoint2().getObject() == aObject)) {
            return (true);
        }

        //-------------------------------------------------------------------
        // CHECK POTENTIAL FIELD ALGORITHM
        //-------------------------------------------------------------------
        for (JInteractionEvent event : potentialFieldsForceModel.interactionRecorder) {
            if (event.getObject() == aObject) {
                return (true);
            }
        }

        // no object in contact
        return (false);
    }

    //-----------------------------------------------------------------------
    // METHODS - WORKSPACE SETTINGS
    //-----------------------------------------------------------------------
    /**
     * Set radius of pointer.
     */
    public void setRadius(final double aRadius) {
        // update the radius that's rendered
        displayRadius = aRadius;

        // update the radius used for collision detection
        proxyPointForceModel.setProxyRadius(aRadius);

        // update radius of display sphere.
        // make the device sphere slightly smaller to avoid
        // graphical artifacts.
        proxySphere.setRadius(1.00 * aRadius);
        deviceSphere.setRadius(0.99 * aRadius);
    }

    /**
     * Set virtual workspace dimensions in which tool will be working.
     */
    public void setWorkspaceRadius(final double aWorkspaceRadius) {
        // update new workspace size
        workspaceRadius = aWorkspaceRadius;

        // compute the new scale factor between the workspace of the tool
        // and one of the haptic device
        if (hapticDevice != null) {
            workspaceScaleFactor = workspaceRadius / hapticDevice.getSpecifications().mWorkspaceRadius;
        } else {
            workspaceScaleFactor = 1.0;
        }
    }

    /**
     * Read the radius of the workspace of the tool.
     */
    public double getWorkspaceRadius() {
        return (workspaceRadius);
    }

    /**
     * Set the scale factor between the workspace of the tool and one of the
     * haptic device.
     */
    public void setWorkspaceScaleFactor(final double aWorkspaceScaleFactor) {
        // make sure that input value is bigger than zero
        double value = JMaths.jAbs(aWorkspaceScaleFactor);

        if (value == 0) {
            return;
        }

        // update scale factor
        workspaceScaleFactor = value;

        // compute the new scale factor between the workspace of the tool
        // and one of the haptic device
        workspaceRadius = workspaceScaleFactor * hapticDevice.getSpecifications().mWorkspaceRadius;
    }

    /**
     * Read the scale factor between the workspace of the tool and one of the
     * haptic device.
     */
    public double getWorkspaceScaleFactor() {
        return (workspaceScaleFactor);
    }

    /**
     * Render the object in OpenGL2.
     */
    @Override
    public void render(JChaiRenderMode aRenderMode) {
        // If multipass transparency is enabled, only render on a single
        // pass...
        if (aRenderMode != JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY && aRenderMode != JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL) {
            return;
        }

        // compute local position of proxy
        JVector3d proxyLocalPos = new JVector3d();
        JMatrix3d tRot = new JMatrix3d();
        proxyLocalPos = proxyPointForceModel.getProxyGlobalPosition();
        proxyLocalPos.sub(globalPosition);
        globalRotation.transr(tRot);
        tRot.mul(proxyLocalPos);


        // update position information of graphic entity for the device
        deviceSphere.setPosition(deviceLocalPosition);
        deviceSphere.setRotation(deviceLocalRotation);

        // update position information of graphic entity for the proxy
        proxySphere.setPosition(proxyLocalPos);
        proxySphere.setRotation(deviceLocalRotation);

        // Button 0 determines the color of the proxy
        if (userSwitch0) {
            proxySphere.setMaterial(buttonPressedProxyMaterial);
        } else {
            proxySphere.setMaterial(proxyMaterial);
        }
        
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(proxyPointForceModel.p.x,proxyPointForceModel.p.y,proxyPointForceModel.p.z);
        gl.glVertex3d(proxyPointForceModel.d.x,proxyPointForceModel.d.y,proxyPointForceModel.d.z);
        gl.glEnd();
        


        // if proxy and device sphere are enabled, draw
        if ((proxySphere.isVisible()) && (deviceSphere.isVisible())) {
            //GL gl = GLContext.getCurrent().getGL();
            gl.glDisable(GL2.GL_LIGHTING);

            gl.glLineWidth(1.0f);

            gl.glColor4fv(lineColor.color,0);

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3d(deviceLocalPosition.getX(), deviceLocalPosition.getY(), deviceLocalPosition.getZ());
            gl.glVertex3d(proxyLocalPos.getX(), proxyLocalPos.getY(), proxyLocalPos.getZ());
            gl.glEnd();

            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    /**
     * @param mDeviceMesh the mDeviceMesh to set
     */
    public void setDeviceMesh(JMesh mDeviceMesh) {
        // remove original device mesh from the sphere
        this.deviceSphere.removeChild(this.deviceMesh);

        // assign new mesh
        this.deviceMesh = mDeviceMesh;
        this.deviceMesh.setHapticEnabled(true, true);
        this.deviceMeshEnabled = true;

        // add to sphere 
        this.deviceSphere.addChild(this.deviceMesh);
    }

    /**
     * @return the deviceSphere
     */
    public JShapeSphere getDeviceSphere() {
        return deviceSphere;
    }

    /**
     * @return the proxySphere
     */
    public JShapeSphere getProxySphere() {
        return proxySphere;
    }

    /**
     * @return the deviceMesh
     */
    public JMesh getDeviceMesh() {
        return deviceMesh;
    }

    /**
     * @return the deviceMeshEnabled
     */
    public boolean isDeviceMeshEnabled() {
        return deviceMeshEnabled;
    }

    /**
     * @param deviceMeshEnabled the deviceMeshEnabled to set
     */
    public void setDeviceMeshEnabled(boolean deviceMeshEnabled) {
        this.deviceMeshEnabled = deviceMeshEnabled;
    }

    /**
     * @return the proxyMesh
     */
    public JMesh getProxyMesh() {
        return proxyMesh;
    }

    /**
     * @param proxyMesh the proxyMesh to set
     */
    public void setProxyMesh(JMesh proxyMesh) {

        // remove from original proxy mesh from sphere
        this.proxySphere.removeChild(this.proxyMesh);

        // assign new mesh
        this.proxyMesh = proxyMesh;
        this.proxyMesh.setHapticEnabled(true, true);

        // add to sphere 
        this.proxySphere.addChild(this.proxyMesh);
    }

    /**
     * @return the lineColor
     */
    public JColorf getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(JColorf lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * @return the proxyMaterial
     */
    public JMaterial getProxyMaterial() {
        return proxyMaterial;
    }

    /**
     * @param proxyMaterial the proxyMaterial to set
     */
    public void setProxyMaterial(JMaterial proxyMaterial) {
        this.proxyMaterial = proxyMaterial;
        this.proxySphere.setMaterial(this.proxyMaterial,true);
    }

    /**
     * @return the buttonPressedProxyMaterial
     */
    public JMaterial getButtonPressedProxyMaterial() {
        return buttonPressedProxyMaterial;
    }

    /**
     * @param buttonPressedProxyMaterial the buttonPressedProxyMaterial to set
     */
    public void setButtonPressedProxyMaterial(JMaterial buttonPressedProxyMaterial) {
        this.buttonPressedProxyMaterial = buttonPressedProxyMaterial;
    }

    /**
     * @return the proxyPointForceModel
     */
    public JProxyPointForceAlgo getProxyPointForceModel() {
        return proxyPointForceModel;
    }

    /**
     * @return the potentialFieldsForceModel
     */
    public JPotentialFieldForceAlgo getPotentialFieldsForceModel() {
        return potentialFieldsForceModel;
    }

    /**
     * @return the lastComputedGlobalForce
     */
    public JVector3d getLastComputedGlobalForce() {
        return lastComputedGlobalForce;
    }

    /**
     * @return the lastComputedLocalForce
     */
    public JVector3d getLastComputedLocalForce() {
        return lastComputedLocalForce;
    }

    /**
     * @return the deviceLocalPosition
     */
    public JVector3d getDeviceLocalPosition() {
        return deviceLocalPosition;
    }

    /**
     * @return the deviceGlobalPosition
     */
    public JVector3d getDeviceGlobalPosition() {
        return deviceGlobalPosition;
    }

    /**
     * @return the deviceLocalVelocity
     */
    public JVector3d getDeviceLocalVelocity() {
        return deviceLocalVelocity;
    }

    /**
     * @return the deviceGlobalVelocity
     */
    public JVector3d getDeviceGlobalVelocity() {
        return deviceGlobalVelocity;
    }

    /**
     * @return the deviceLocalRotation
     */
    public JMatrix3d getDeviceLocalRotation() {
        return deviceLocalRotation;
    }

    /**
     * @return the deviceGlobalRotation
     */
    public JMatrix3d getDeviceGlobalRotation() {
        return deviceGlobalRotation;
    }

    /**
     * @return the world
     */
    public JWorld getWorld() {
        return world;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(JWorld world) {
        this.world = world;
        this.potentialFieldsForceModel.setParentWorld(this.world);
        this.proxyPointForceModel.setParentWorld(world);
    }

    /**
     * @return the displayRadius
     */
    public double getDisplayRadius() {
        return displayRadius;
    }

    /**
     * @param displayRadius the displayRadius to set
     */
    public void setDisplayRadius(double displayRadius) {
        this.displayRadius = displayRadius;
    }

    /**
     * @return the userSwitch0
     */
    public boolean isUserSwitch0() {
        return userSwitch0;
    }

    /**
     * @param userSwitch0 the userSwitch0 to set
     */
    public void setUserSwitch0(boolean userSwitch0) {
        this.userSwitch0 = userSwitch0;
    }

    /**
     * @return the forcesEnabled
     */
    public boolean isForcesEnabled() {
        return forcesEnabled;
    }

    /**
     * @param forcesEnabled the forcesEnabled to set
     */
    public void setForcesEnabled(boolean forcesEnabled) {
        this.forcesEnabled = forcesEnabled;

        if (this.forcesEnabled) {
            setForcesON();
        } else {
            setForcesOFF();
        }
    }

    /**
     * @return the forceStarted
     */
    public boolean isForceStarted() {
        return forceStarted;
    }

    /**
     * @param forceStarted the forceStarted to set
     */
    public void setForceStarted(boolean forceStarted) {
        this.forceStarted = forceStarted;
    }

    /**
     * @return the waitForSmallForce
     */
    public boolean isWaitForSmallForce() {
        return waitForSmallForce;
    }

    /**
     * @param waitForSmallForce the waitForSmallForce to set
     */
    public void setWaitForSmallForce(boolean waitForSmallForce) {
        this.waitForSmallForce = waitForSmallForce;
    }
}
