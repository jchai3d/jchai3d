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
 */
package org.jchai3d.scenegraph;

import java.util.ArrayList;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JTexture2D;
import org.jchai3d.math.JVector3d;

/**
 *
 * The JWorld object is the root for all scenegraph object in a scene.
 *
 * @author Francois Conti (original author)
 * @author Jairo Melo (java implementation)
 * @author Marcos Ramos (java implementation)
 */
public class JWorld extends JGenericObject {

    /**
     * Background color. Default color is black.
     */
    protected JColorf backgroundColor;
    /**
     * List of textures.
     */
    protected ArrayList<JTexture2D> textures;
    /**
     * List of light sources.
     */
    private ArrayList<JLight> lights;
    /**
     * Should I render my light sources, or just use the current OpenGL light
     * state?
     */
    protected boolean lightingEnabled;
    /**
     * Some apps may have multiple cameras, which would cause recursion when
     * resetting the display.
     */
    protected boolean performingDisplayReset;
    /**
     * It's useful to store the world's modelview matrix, for rendering stuff in
     * "global" coordinates.
     */
    public double worldModelView[] = new double[16];
    /**
     * The maximum number of lights that we expect OpenGL to support
     */
    public static int CHAI_MAXIMUM_OPENGL_LIGHT_COUNT = 8;

    public JWorld() {
        // set background properties
        backgroundColor = new JColorf();

        backgroundColor.set(0.0f, 0.0f, 0.0f, 1.0f);

        lightingEnabled = true;

        performingDisplayReset = false;

        textures = new ArrayList<JTexture2D>();

        lights = new ArrayList<JLight>();
    }

    /**
     * Add an OpenGL light source to the world. A maximum of eight light sources
     * can be registered. For each registered light source, an OpenGL lightID
     * number is defined
     *
     * @param aLight
     * @return
     */
    public boolean addLightSource(JLight aLight) {
        // check if number of lights already equal to 8.
        if (lights.size() >= CHAI_MAXIMUM_OPENGL_LIGHT_COUNT) {
            return (false);
        }

        // search for a free ID number
        int lightId = GL2.GL_LIGHT0;
        boolean found = false;

        while (lightId < GL2.GL_LIGHT0 + CHAI_MAXIMUM_OPENGL_LIGHT_COUNT) {

            // check if ID is not already used
            int i;
            boolean free = true;
            for (i = 0; i < lights.size(); i++) {
                JLight nextLight = lights.get(i);

                if (nextLight.getGlLightID() == lightId) {
                    free = false;
                }
            }

            // check if a free ID was found
            if (free) {
                aLight.glLightID = lightId;
                found = true;
                break;
            }

            lightId++;
        }

        // finalize
        if (found) {
            lights.add(aLight);
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Adds a new child to this world
     *
     * @param obj the new child
     */
    public void addChild(JGenericObject obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof JMesh) {
            ((JMesh) obj).setParentWorld(this);
        }
        super.addChild(obj);
    }

    /**
     * Remove a light source from world.
     *
     * @param aLight the reference of the light to remove
     * @return false, only if the specified light is not on this world light
     * list
     */
    public boolean removeLightSource(JLight aLight) {
        // set iterator
        Iterator<JLight> nextLight = lights.iterator();

        while (nextLight.hasNext()) {
            if ((nextLight.next()) == aLight) {
                // remove object from list
                lights.remove(nextLight);

                // return success
                return (true);
            }
        }
        // operation failed
        return (false);
    }

    public void removeLightSource(int index) {
        lights.remove(index);
    }

    /**
     * Set the background color used when rendering. This really belongs in
     * cCamera or cViewport; it's a historical artifact that it lives here.
     *
     * @param aRed
     * @param aGreen
     * @param aBlue
     */
    public void setBackgroundColor(final float aRed,
            final float aGreen,
            final float aBlue) {
        backgroundColor.set(aRed, aGreen, aBlue, 1.0f);
    }

    /**
     * Set the background color used when rendering. This really belongs in
     * cCamera or cViewport; it's a historical artifact that it lives here.
     *
     * @param aColor
     */
    public void setBackgroundColor(final JColorf aColor) {
        backgroundColor = aColor;
    }

    /**
     * Get the background color used when rendering.
     */
    public final JColorf getBackgroundColor() {
        return (backgroundColor);
    }

    /**
     * Enable or disable the rendering of this world's light sources.
     */
    public void setLightingEnabled(boolean enable) {
        lightingEnabled = enable;
    }

    /**
     * Returns the status of lighting(enabled or disabled)
     *
     * @return true if lighting is enabled, false otherwise
     */
    public boolean isLightingEnabled() {
        return lightingEnabled;
    }

    /**
     * Create new texture and add it to textures list.
     *
     * @return
     */
    public JTexture2D newTexture() {
        // create new texture entity
        JTexture2D newTexture = new JTexture2D();

        // add texture to list
        textures.add(newTexture);

        // return pointer to new texture
        return (newTexture);
    }

    /**
     * Get a pointer to a texture by passing an index into my texture list.
     * public JTexture2D getTexture(int aIndex) { return textures.get(aIndex); }
     *
     * /**
     * Add texture to texture list.
     *
     * @param aTexture
     */
    public void addTexture(JTexture2D aTexture) {
        // add texture to list
        textures.add(aTexture);
    }

    /**
     * Remove texture from textures list. Texture is not deleted from memory.
     *
     * @param aTexture
     * @return
     */
    public boolean removeTexture(JTexture2D aTexture) {
        // set iterator
        Iterator<JTexture2D> iter = textures.iterator();
        JTexture2D nextTexture;

        // search texture in  list and remove it
        while (iter.hasNext()) {
            nextTexture = iter.next();
            if ((nextTexture) == aTexture) {
                // remove object from list
                textures.remove(nextTexture);

                // return success
                return (true);
            }
        }
        // operation failed
        return (false);
    }

    /**
     * Delete texture from textures list and erase it from memory.
     *
     * @param aTexture
     * @return
     */
    public boolean deleteTexture(JTexture2D aTexture) {
        // remove texture from list
        boolean result = removeTexture(aTexture);

        // if operation succeeds, delete object
        if (result) {
            aTexture = null;
        }

        // return result
        return (result);
    }

    /**
     * Delete all texture from memory.
     */
    public void deleteAllTextures() {
        // delete all textures
        for (int i = 0; i < textures.size(); i++) {
            textures.set(i, null);
        }

        // clear textures list
        textures.clear();
    }

    /**
     * Determine whether the given segment intersects a triangle in this world.
     * The segment is described by a start point /e a_segmentPointA and end
     * point /e a_segmentPointB. Collision detection functions of all children
     * of the world are called, which recursively call the collision detection
     * functions for all objects in this world. If there is more than one
     * collision, the one closest to a_segmentPointA is the one returned.
     *
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @param aRecorder
     * @param aSettings
     * @return
     */
    @Override
    public boolean computeCollisionDetection(JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JCollisionRecorder aRecorder,
            JCollisionSettings aSettings) {
        // temp variable
        boolean hit = false;
        JVector3d tSegmentPointA = new JVector3d(aSegmentPointA);
        JVector3d tSegmentPointB = new JVector3d(aSegmentPointB);

        // check for collisions with all children of this world
        for (JGenericObject children : childrens) {
            hit = hit || children.computeCollisionDetection(tSegmentPointA,
                    tSegmentPointB,
                    aRecorder,
                    aSettings);
            continue;
        }

        // return whether there was a collision between the segment and this world
        return (hit);
    }

    /**
     * Render the world in OpenGL2.
     *
     * @param aRenderMode
     */
    @Override
    public void render(JChaiRenderMode renderMode) {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        // Set up the CHAI openGL defaults (see cGenericObject::render())
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Back up the "global" modelview matrix for future reference
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, worldModelView, 0);

        if (lightingEnabled) {
            // enable lighting
            gl.glEnable(GL2.GL_LIGHTING);

            // render light sources
            for (JLight light : lights) {
                light.renderLightSource();
            }
        } else {
            gl.glDisable(GL2.GL_LIGHTING);
        }

    }

    /**
     * Get access to a particular light source (between 0 and
     * MAXIMUM_OPENGL_LIGHT_COUNT-1). Returns a pointer to the requested light,
     * or zero if it's not available.
     *
     * @param index
     * @return
     */
    public JLight getLightSource(int index) {
        // Make sure this is a valid index
        if (index < 0 || (int) (index) >= getLights().size()) {
            return null;
        }

        // Return the light that we were supplied with by the creator of the world
        return lights.get(index);
    }

    /**
     * Called by the user or by the viewport when the world needs to have
     * textures and display lists reset (e.g. after a switch to or from
     * fullscreen).
     *
     * @param aAffectChildren
     */
    @Override
    public void onDisplayReset(final boolean aAffectChildren) {
        // Prevent the world from getting reset multiple times when there are multiple cameras
        if (performingDisplayReset) {
            return;
        }

        performingDisplayReset = true;

        // This will pass the call on to any children I might have...
        JGenericObject jGenericObject = new JGenericObject();
        jGenericObject.onDisplayReset(aAffectChildren);

        performingDisplayReset = false;
    }

    /**
     * @return the mLights
     */
    public ArrayList<JLight> getLights() {
        return lights;
    }

    /**
     * @param mLights the mLights to set
     */
    public void setLights(ArrayList<JLight> mLights) {
        this.lights = mLights;
    }
}
