package org.jchai3d.scenegraph;

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
 *   author    João Cerqueira
 *   version   1.0.0
 */

import java.nio.FloatBuffer;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

public class JLight extends JGenericObject {
    /**
     * Parent world.
     */
    protected JColorf ambientColor;
    protected JColorf diffuseColor;
    protected JColorf specularColor;
    protected JWorld parentWorld;
    /**
     * Constant attenuation parameter.
     */
    protected float constantAttenuation;
    /**
     * Linear attenuation parameter.
     */
    protected float linearAttenuation;
    /**
     * Quadratic attenuation parameter.
     */
    protected  float quadraticAttenuation;
    /**
     * Concentration of the light.
     */
    protected float spotExponent;
    /**
     * Cut off angle (for spot lights only). Only values in the range [0, 90], and the special value 180, are accepted.
     */
    protected float spotCutoffAngle;
    /**
     * Enable light source (ON/OFF).
     */
    protected boolean enabled;
    /**
     * Is this a directional (true) or positional (false) light.
     */
    protected boolean directionalLight;
    /**
     * GL reference number of the light (0-7).
     */
    protected int glLightID;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    /**
     * Constructor of JLight.
     */
    public JLight(JWorld aWorld) {
        this();

        /**
         * read light number in current world
         */
        parentWorld = aWorld;

        /**
         * The GL light number will be modified here...
         */
        if (parentWorld != null) {
            parentWorld.addLightSource(this);
        }
    }
    /**
     * Constructor of JLight.
     */
    public JLight() {

        /**
         * Uninitialized state...
         */
        glLightID = 9999;

        /**
         * set default color parameters
         */
        ambientColor = new JColorf(.3f, .3f, .3f, 1.0f);
        diffuseColor = new JColorf(.5f, .5f, .5f, 1.0f);
        specularColor = new JColorf(.4f, .4f, .4f, 1.0f);

        /**
         * set default color parameters
         */
        spotCutoffAngle = 180.0f;

        /**
         * set default attenuation parameters  ATT = 1 / (Kc + Kl*d + Kq*d^2)
         */
        /**
         * Attenuation Constant.
         */
        constantAttenuation = 1.0f;

        /**
         * Attenuation Linear.
         */
        linearAttenuation = 0.0f;

        /**
         * Attenuation Quadratic.
         */
        quadraticAttenuation = 0.0f;

        /**
         * set default spot exponent
         */
        spotExponent = 100.0f;

        /**
         * light sources are disable by default
         */
        enabled = false;

        /**
         * lights are purely directional by default
         */
        directionalLight = true;

        //aWorld.addLightSource(this);

    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    
    /**
     * Set the direction of the light beam... only affects _positional_ lights with angular cutoffs (spotlights).
     */
    public void setDirection(final JVector3d aDirection) {

        // We arbitrarily point lights along the x axis of the stored
        // rotation matrix... this allows matrix transformations
        // to apply to lights.
        // mLocalRot.setCol0(aDirection);

        JVector3d c0 = new JVector3d();
        JVector3d c1 = new JVector3d();
        JVector3d c2 = new JVector3d();
        JVector3d t = new JVector3d();

        aDirection.copyTo(c0);

        /**
         * check vector
         */
        if (c0.lengthsq() < 0.0001) {
            return;
        }

        /**
         * normalize direction vector
         */
        c0.normalize();

        /**
         * compute 2 vector perpendicular to a_direction
         */
        t.set(aDirection.getY(), aDirection.getZ(), aDirection.getX());
        t.crossr(c0, c1);
        c1.crossr(c0, c2);

        /**
         * c0.negate();
         */
        c1.negate();
        c2.negate();

        /**
         * update rotation matrix
         */
        localRotation.setCol(c0, c1, c2);
    }

    public void setParentWorld(JWorld world) {
        parentWorld = world;
    }
    /**
     * Set the direction of the light beam... only affects _positional_ lights with angular cutoffs (spotlights).
     */
    public void setDirection(final double aX, final double aY, final double aZ) {

        setDirection(new JVector3d(aX, aY, aZ));
    }

    /**
     * Read the direction of the light beam... only affects _positional_ lights with angular cutoffs (spotlights).
     */
    public final JVector3d getDirection() {
        return (localRotation.getCol0());
    }

    /**
     * Set this light to be purely directional (true) or purely positional (false).
     */
    public void setDirectionalLight(boolean aDirectionalLight) {
        directionalLight = aDirectionalLight;
    }

    /**
     * Returns true for a directional light, false for a positional light.
     */
    public boolean isDirectionalLight() {
        return directionalLight;
    }

    /**
     * Set my constant attenuation parameter.
     */
    public void setConstantAttenuation(final float aValue) {
        constantAttenuation = JMaths.jClamp(aValue, 0.0f, 1.0f);
    }

    /**
     * Read my constant attenuation parameter.
     */
    public final float getConstantAttenuation() {
        return (constantAttenuation);
    }

    /**
     * Set my linear attenuation parameter.
     */
    public void setLinearAttenuation(final float aValue) {
        linearAttenuation = JMaths.jClamp(aValue, 0.0f, 1.0f);
    }

    /**
     * Read my linear attenuation parameter.
     */
    public final float getLinearAttenuation() {
        return (linearAttenuation);
    }

    /**
     * Set my quadratic attenuation parameter.
     */
    public void setQuadraticAttenuation(final float aValue) {
        quadraticAttenuation = ((float) JMaths.jClamp(aValue, 0.0f, 1.0f));
    }

    /**
     * Read my quadratic attenuation parameter.
     */
    public final float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    /**
     * Set concentration level of the light.
     */
    public void setSpotExponent(final float aValue) {
        spotExponent = JMaths.jClamp(aValue, 0.0f, 100.0f);
    }

    /**
     * Read concentration level of the light.
     */
    public final float getSpotExponent() {
        return (spotExponent);
    }

    /**
     * Read concentration level of the light.
     */
    public void setSpotCutoffAngle(final float aValue) {

        /**
         * temp variable
         */
        float tNewAngle;

        /**
         * check if a negative value was given in which case light is set to a non spot configuration
         */
        if (aValue < 0) {
            tNewAngle = 180.0f;
        } /**
         * check if angle is equal to 180.0 degrees. This corresponds to a non spot light.
         */
        else if (aValue == 180.0f) {
            tNewAngle = 180.0f;
        } /**
         * check if value ranges between 0 and 90. This corresponds to a spot light.
         */
        else if ((aValue >= 0.0f) && (aValue <= 90.0f)) {
            tNewAngle = aValue;
        } /**
         * value is incorrect. Light is set to a non-spot light configuration.
         */
        else {
            tNewAngle = 180.0f;
        }

        /**
         * assign new value
         */
        spotCutoffAngle = tNewAngle;
    }

    /**
     * Read Cut off angle.
     */
    public final float getSpotCutoffAngle() {
        return (spotCutoffAngle);
    }

    /**
     * Enable or disable this light source.
     */
    public void setEnabled(final boolean aEnabled) {
        enabled = aEnabled;
    }

    /**
     * Is this light source enabled?
     */
    public final boolean isEnabled() {
        return (enabled);
    }

    /**
     * Render the light in JOpenGL2.
     */
    protected void renderLightSource() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        /**
         * check if light source enabled
         */
        if (!enabled) {
            /**
             * disable JOpenGL light source
             */
            gl.glDisable(glLightID);
            return;
        }

        computeGlobalCurrentObjectOnly(true);

        /**
         * enable this light in JOpenGL
         */
        gl.glEnable(glLightID);
        
        

        /**
         * set lighting components
         */
        gl.glLightfv(glLightID, GL2.GL_AMBIENT, FloatBuffer.wrap(ambientColor.getComponents()));
        gl.glLightfv(glLightID, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseColor.getComponents()));
        gl.glLightfv(glLightID, GL2.GL_SPECULAR, FloatBuffer.wrap(specularColor.getComponents()));

        /**
         * position the light source in (global) space (because we're not rendered as part of the scene graph)
         */
        float[] position = new float[4];

        position[0] = (float) getGlobalPosition().getX();
        position[1] = (float) getGlobalPosition().getY();
        position[2] = (float) getGlobalPosition().getZ();

        /**
         * Directional light source...
         */
        if (directionalLight) {
            position[3] = 0.0f;
        } /**
         * Positional light source...
         */
        else {
            position[3] = 1.0f;
        }

        gl.glLightfv(glLightID, GL2.GL_POSITION, FloatBuffer.wrap(position));

        // set cutoff angle
        gl.glLightf(glLightID, GL2.GL_SPOT_CUTOFF, spotCutoffAngle);

        // set the direction of my light beam, if I'm a positional spotlight
        if (directionalLight == false) {
            JVector3d dir = globalRotation.getCol0();
            float[] direction = new float[4];
            direction[0] = (float) dir.getX();
            direction[1] = (float) dir.getY();
            direction[2] = (float) dir.getZ();
            direction[3] = 0.0f;
            gl.glLightfv(glLightID, GL2.GL_SPOT_DIRECTION, FloatBuffer.wrap(direction));
        }

        /**
         * set attenuation factors
         */
        gl.glLightf(glLightID, GL2.GL_CONSTANT_ATTENUATION, constantAttenuation);
        gl.glLightf(glLightID, GL2.GL_LINEAR_ATTENUATION, linearAttenuation);
        gl.glLightf(glLightID, GL2.GL_QUADRATIC_ATTENUATION, quadraticAttenuation);

        /**
         * set exponent factor
         */
        gl.glLightf(glLightID, GL2.GL_SPOT_EXPONENT, spotExponent);
    }

    /**
     * @return the ambientColor
     */
    public JColorf getAmbientColor() {
        return ambientColor;
    }

    /**
     * @param ambientColor the ambientColor to set
     */
    public void setAmbientColor(JColorf ambientColor) {
        this.ambientColor = ambientColor;
    }

    /**
     * @return the diffuseColor
     */
    public JColorf getDiffuseColor() {
        return diffuseColor;
    }

    /**
     * @param diffuseColor the diffuseColor to set
     */
    public void setDiffuseColor(JColorf diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    /**
     * @return the specularColor
     */
    public JColorf getSpecularColor() {
        return specularColor;
    }

    /**
     * @param specularColor the specularColor to set
     */
    public void setSpecularColor(JColorf specularColor) {
        this.specularColor = specularColor;
    }

    /**
     * @return the parentWorld
     */
    public JWorld getParentWorld() {
        return parentWorld;
    }

    /**
     * @return the glLightID
     */
    public int getGlLightID() {
        return glLightID;
    }
}
