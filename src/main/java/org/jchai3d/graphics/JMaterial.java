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
package org.jchai3d.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.math.JMaths;

/**
 * JMaterial provide a description for handling OpenGL graphic material
 * properties. These include: ambient color, diffuse color, specular color,
 * emissive color, and shininess. \n
 *
 * Haptic properties are also defined in this class. Properties include
 * stiffness, dynamic friction, and static friction, viscosity, vibration and
 * magnetic effects. Force rendering algorithms will lookup the material
 * properties of an object to compute the desired force rendering effect.
 *
 * @author jairo
 */
public class JMaterial {

    //-----------------------------------------------------------------------
    // MEMBERS - HAPTIC PROPERTIES:
    //-----------------------------------------------------------------------
    //! Level of viscosity.
    protected double viscosity;
    //! Stiffness [Netwons per meter].
    protected double stiffness;
    //! Static friction constant.
    protected double staticFriction;
    //! Dynamic friction constant.
    protected double dynamicFriction;
    //! Frequency of vibrations.
    protected double vibrationFrequency;
    //! Amplitude of vibrations.
    protected double vibrationAmplitude;
    //! Maximum force applied by magnet effect.
    protected double magnetMaxForce;
    //! Maximum distance from the object where the magnetic force can be perceived.
    protected double magnetMaxDistance;
    //! Force threshold for stick and slip effect.
    protected double stickSlipForceMax;
    //! Spring stiffness of stick slip model.
    protected double stickSlipStiffness;
    //-----------------------------------------------------------------------
    // MEMBERS - GRAPHICS PROPERTIES:
    //-----------------------------------------------------------------------
    //! OpenGL shininess
    public int shininess;
    //! Emissive color.
    protected JColorf ambient;
    protected JColorf diffuse;
    protected JColorf specular;
    protected JColorf emission;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    //! Constructor of cMaterial.
    public JMaterial() {
        // default graphic settings
        ambient = new JColorf();
        ambient.set(0.5f, 0.5f, 0.5f, 1.0f);

        diffuse = new JColorf();
        diffuse.set(0.7f, 0.7f, 0.7f, 1.0f);

        specular = new JColorf();
        specular.set(0.4f, 0.4f, 0.4f, 1.0f);

        emission = new JColorf();
        emission.set(0.4f, 0.4f, 0.4f, 1.0f);

        shininess = 64;

        // default haptic settings
        viscosity = 0.0;
        stiffness = 0.0;
        staticFriction = 0.0;
        dynamicFriction = 0.0;
        vibrationFrequency = 0.0;
        vibrationAmplitude = 0.0;
        stickSlipForceMax = 0.0;
        stickSlipStiffness = 0.0;
    }

    //-----------------------------------------------------------------------
    // METHODS - GRAPHIC PROPERTIES:
    //-----------------------------------------------------------------------
    /**
     * //! Set shininess (the exponent used for specular lighting).
     *
     * @param aShininess
     */
    public void setShininess(int aShininess) {
        float s = JMaths.jClamp((float) aShininess, 0.0f, 128.0f);
        shininess = (int) s;
    }

    /**
     * //! Get shininess.
     *
     * @return
     */
    public int getShininess() {
        return (shininess);
    }

    /**
     * //! set transparency level (sets the alpha value for all color
     * properties).
     *
     * @param aLevelTransparency
     */
    public void setTransparencyLevel(float aLevelTransparency) {
        // make sur value is in range [0.0 - 1.0]
        float level = JMaths.jClamp(aLevelTransparency, 0.0f, 1.0f);

        // apply new value
        ambient.setA(level);
        diffuse.setA(level);
        specular.setA(level);
        emission.setA(level);
    }

    //! tells you whether this material includes partial transparency.
    public final boolean isTransparent() {
        return (ambient.getA() < 1.0f
                || diffuse.getA() < 1.0f
                || specular.getA() < 1.0f
                || emission.getA() < 1.0f);
    }

    //-----------------------------------------------------------------------
    // METHODS - HAPTIC PROPERTIES:
    //-----------------------------------------------------------------------
    /**
     * //! Set stiffness level [N/m]
     *
     * @param aStiffness
     */
    public void setStiffness(double aStiffness) {
        stiffness = JMaths.jClamp0(aStiffness);
    }

    /**
     * //! Get stiffness level [N/m]
     *
     * @return
     */
    public final double getStiffness() {
        return (stiffness);
    }

    /**
     * //! Set static friction level
     *
     * @param aFriction
     */
    public void setStaticFriction(double aFriction) {
        staticFriction = JMaths.jClamp0(aFriction);
    }

    /**
     * //! Get static friction level
     *
     * @return
     */
    public final double getStaticFriction() {
        return (staticFriction);
    }

    /**
     * //! Set dynamic friction level
     *
     * @param a_friction
     */
    public void setDynamicFriction(double aFriction) {
        dynamicFriction = JMaths.jClamp0(aFriction);
    }

    /**
     * //! Get dynamic friction level
     *
     * @return
     */
    public final double getDynamicFriction() {
        return (dynamicFriction);
    }

    /**
     * //! Set level of viscosity
     *
     * @param aViscosity
     */
    public void setViscosity(double aViscosity) {
        viscosity = JMaths.jClamp0(aViscosity);
    }

    /**
     * //! Get level of viscosity
     *
     * @return
     */
    public final double getViscosity() {
        return (viscosity);
    }

    /**
     * //! Set vibration frequency [Hz]
     *
     * @param aVibrationFrequency
     */
    public void setVibrationFrequency(double aVibrationFrequency) {
        vibrationFrequency = JMaths.jClamp0(aVibrationFrequency);
    }

    /**
     * //! Get vibration frequency [Hz]
     *
     * @return
     */
    public final double getVibrationFrequency() {
        return (vibrationFrequency);
    }

    /**
     * //! Set vibration amplitude [max N]
     *
     * @param aVibrationAmplitude
     */
    public void setVibrationAmplitude(double aVibrationAmplitude) {
        vibrationAmplitude = JMaths.jClamp0(aVibrationAmplitude);
    }

    /**
     * //! Get vibration amplitude [max N]
     *
     * @return
     */
    public final double getVibrationAmplitude() {
        return (vibrationAmplitude);
    }

    /**
     * //! Set the maximum force applied by the magnet [N]
     *
     * @param aMagnetMaxForce
     */
    public void setMagnetMaxForce(double aMagnetMaxForce) {
        magnetMaxForce = JMaths.jClamp0(aMagnetMaxForce);
    }

    /**
     * //! Get the maximum force applied by the magnet [N]
     *
     * @return
     */
    public final double getMagnetMaxForce() {
        return (magnetMaxForce);
    }

    /**
     * //! Set the maximum distance from the object where the force can be
     * perceived [m]
     *
     * @param aMagnetMaxDistance
     */
    public void setMagnetMaxDistance(double aMagnetMaxDistance) {
        magnetMaxDistance = JMaths.jClamp0(aMagnetMaxDistance);
    }

    /**
     * //! Set the maximum distance from the object where the force can be
     * perceived [m]
     *
     * @return
     */
    public final double getMagnetMaxDistance() {
        return (magnetMaxDistance);
    }

    /**
     * //! Set the maximum force threshold for the stick and slip model [N]
     *
     * @param aStickSlipForceMax
     */
    public void setStickSlipForceMax(double aStickSlipForceMax) {
        stickSlipForceMax = JMaths.jClamp0(aStickSlipForceMax);
    }

    /**
     * //! Get the maximum force threshold for the stick and slip model [N]
     *
     * @return
     */
    public final double getStickSlipForceMax() {
        return (stickSlipForceMax);
    }

    /**
     * //! Set the stiffness for the stick and slip model [N/m]
     *
     * @param aStickSlipStiffness
     */
    public void setStickSlipStiffness(double aStickSlipStiffness) {
        stickSlipStiffness = JMaths.jClamp0(aStickSlipStiffness);
    }

    //! Get the stiffness for the stick and slip model [N/m]
    public final double getStickSlipStiffness() {
        return (stickSlipStiffness);
    }

    /**
     * Render this material in OpenGL2.
     */
    public void render() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient.getComponents(), 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse.getComponents(), 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular.getComponents(), 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emission.getComponents(), 0);
        gl.glMateriali(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
    }

    /**
     * @return the ambient
     */
    public JColorf getAmbient() {
        return ambient;
    }

    /**
     * @param ambient the ambient to set
     */
    public void setAmbient(JColorf ambient) {
        this.ambient.copyFrom(ambient);
    }

    /**
     * @return the diffuse
     */
    public JColorf getDiffuse() {
        return diffuse;
    }

    /**
     * @param diffuse the diffuse to set
     */
    public void setDiffuse(JColorf diffuse) {
        this.diffuse.copyFrom(diffuse);
    }

    /**
     * @return the specular
     */
    public JColorf getSpecular() {
        return specular;
    }

    /**
     * @param specular the specular to set
     */
    public void setSpecular(JColorf specular) {
        this.specular.copyFrom(specular);
    }

    /**
     * @return the emission
     */
    public JColorf getEmission() {
        return emission;
    }

    /**
     * @param emission the emission to set
     */
    public void setEmission(JColorf emission) {
        this.emission.copyFrom(emission);
    }

    /**
     *
     */
    public void copyFrom(JMaterial m) {
        if (m != null) {
            this.ambient.copyFrom(m.ambient);
            this.diffuse.copyFrom(m.diffuse);
            this.specular.copyFrom(m.specular);
            this.emission.copyFrom(m.emission);
            this.stiffness = m.stiffness;
            this.staticFriction = m.staticFriction;
            this.dynamicFriction = m.dynamicFriction;
            this.vibrationFrequency = m.vibrationFrequency;
            this.vibrationAmplitude = m.vibrationAmplitude;
            this.magnetMaxForce = m.magnetMaxForce;
            this.magnetMaxDistance = m.magnetMaxDistance;
            this.stickSlipForceMax = m.stickSlipForceMax;
            this.stickSlipStiffness = m.stiffness;
        }
    }
}
