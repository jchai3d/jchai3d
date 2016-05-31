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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.collisions.JGenericCollision;
import org.jchai3d.effects.JGenericEffect;
import org.jchai3d.extras.JGenericType;
import org.jchai3d.forces.JInteractionEvent;
import org.jchai3d.forces.JInteractionRecorder;
import org.jchai3d.forces.JInteractionSettings;
import org.jchai3d.graphics.*;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * <p>This class is the root of basically every render-able object in JCHAI. It
 * defines a reference frame (position and rotation) and virtual methods for
 * rendering, which are overloaded by useful subclasses. </p>
 *
 * <p>This class also defines basic methods for maintaining a scene graph, and
 * propagating rendering passes and reference frame changes through a hierarchy
 * of JGenericObject objects. </p>
 *
 * <p>Besides subclassing, a useful way to extend JGenericObject is to store
 * custom data in the tag and userData member fields, which are not used by
 * JCHAI.</p>
 *
 * <p>The most important methods to look at here are probably the virtual
 * methods, which are listed last in CGenericObject.h. These methods will be
 * called on each cGenericObject as operations propagate through the scene
 * graph.</p>
 *
 * @author Francois Conti (original author)
 * @author Dan Morris (original author)
 * @author Jairo Melo (java implementation)
 * @author Marcos Ramos (java implementation)
 */
public class JGenericObject extends JGenericType {

    /**
     * My list of children.
     */
    protected JVector3d interactionProjectedPoint;
    /**
     * Was the last interaction point located inside the object?
     */
    protected boolean interactionInside;
    /**
     * list of haptic effects.
     */
    protected ArrayList<JGenericEffect> effects;
    /**
     * Material properties.
     */
    protected JMaterial material;
    /**
     * Texture property.
     */
    protected JTexture2D texture;
    /**
     * An arbitrary tag, not used by CHAI.
     */
    protected int tag;
    /**
     * An arbitrary data pointer, not used by CHAI.
     */
    protected Object userData;
    /**
     * A name for this object, automatically assigned by mesh loaders (for
     * example).
     */
    protected String objectName;
    /**
     * Parent object.
     */
    protected JGenericObject parent;
    /**
     * My list of children.
     */
    protected ArrayList<JGenericObject> childrens;
    /**
     * Ghost status of current object.
     */
    protected boolean ghost;
    /**
     * A pointer to an external parent located outside of the scenegraph. This
     * parameter can typically be used if you want to attach an generic object
     * to some other object outside of CHAI3D of to an external representation
     * such as a dynamics engine model. See the ODE examples to understand how a
     * generic object can be attached to an ODE object.
     */
    protected JGenericType externalParent;
    /**
     *
     * A super parent points to another object generally located higher up in
     * the scene graph. When a mesh is created, the super parent of its children
     * will generally point towards the root of the mesh. This parameter is
     * automatically set by the 3D object file loader.
     */
    protected JGenericObject superParent;
    //-----------------------------------------------------------------------
    // MEMBERS - POSITION & ORIENTATION:
    //-----------------------------------------------------------------------
    /**
     * The position of this object in my parent's reference frame.
     */
    protected JVector3d localPosition;
    /**
     * The position of this object in the world's reference frame.
     */
    protected JVector3d globalPosition;
    /**
     * The color of the bounding box.
     */
    protected JMatrix3d localRotation;
    /**
     * The rotation matrix that rotates my reference frame into the world's
     * reference frame.
     */
    protected JMatrix3d globalRotation;
    /**
     * The previous position of this of this object in the parent's reference
     * frame.
     */
    protected JVector3d previousLocalPosition;
    /**
     * The previous position of this of this object in the parent's reference
     * frame.
     */
    protected JMatrix3d previousLocalRotation;
    /**
     * A previous position; exact interpretation up to user.
     */
    protected JVector3d previousGlobalPosition;
    /**
     * A previous rotation; exact interpretation up to user.
     */
    protected JMatrix3d previousGlobalRotation;
    /**
     * Minimum position of boundary box.
     */
    protected JVector3d boundaryBoxMin;
    /**
     * Maximum position of boundary box.
     */
    protected JVector3d boundaryBoxMax;
    /**
     * Size of graphical representation of frame (X-Y-Z).
     */
    protected double frameSize;
    /**
     * Pen thickness of graphical representation of frame (X-Y-Z).
     */
    protected double frameThicknessScale;
    /**
     * If \b true, this object is rendered.
     */
    protected boolean visible;
    /**
     * IF \b true, this object can be felt.
     */
    protected boolean hapticEnabled;
    /**
     * If \b true, this object's reference frame is rendered as a set of arrows.
     */
    protected boolean frameVisible;
    /**
     * If \b true, this object's boundary box is displayed as a set of lines.
     */
    protected boolean boxVisible;
    /**
     * If \b true, the skeleton of the scene graph is rendered at this node.
     */
    protected boolean treeVisible;
    /**
     * If \b true, the collision tree is displayed (if available) at this node.
     */
    protected boolean collisionTreeVisible;
    /**
     * The color of the collision tree.
     */
    protected JColorf treeColor;
    /**
     * The color of the bounding box.
     */
    protected JColorf boundaryBoxColor;
    /**
     * Should texture mapping be used?
     */
    protected boolean textureMappingEnabled;
    /**
     * Should material properties be used?
     */
    protected boolean materialEnabled;
    /**
     * Should per-vertex colors be used?
     */
    protected boolean vertexColorsEnabled;
    /**
     * The polygon rendering mode (GL_FILL or GL_LINE).
     */
    protected int triangleMode;
    /**
     *
     * If true, transparency is enabled... this turns alpha on when the mesh is
     * rendered, and - if multipass transparency is enabled in the rendering
     * camera - uses the camera's multiple rendering passes to approximate
     * back-to-front sorting via culling.
     */
    protected boolean transparencyEnabled;
    /**
     *
     * If true, multipass transparency is permitted for this mesh... this means
     * that if the rendering camera is using multipass transparency, this mesh
     * will render back and front faces separately. \n
     *
     * Note that m_useTransparency also has to be \b true for this variable to
     * be meaningful.
     */
    protected boolean multipassTransparencyEnabled;
    /**
     *
     * Should culling be used when rendering triangles? \n
     *
     * Note that this option only applies when multipass transparency is
     * disabled or during the non-transparent rendering pass when multipass
     * transparency is enabled... \n
     *
     * Also note that currently only back-faces are culled during
     * non-transparent rendering; you can't cull front-faces.
     */
    protected boolean cullingEnabled;
    //-----------------------------------------------------------------------
    // MEMBERS - COLLISION DETECTION:
    //-----------------------------------------------------------------------
    /**
     * The collision detector used to test for contact with this object.
     */
    protected JGenericCollision collisionDetector;
    //-----------------------------------------------------------------------
    // MEMBERS - OPEN GL:
    //-----------------------------------------------------------------------
    /**
     * OpenGL matrix describing my position and orientation transformation.
     */
    protected JMatrixGL frameGL;
    /**
     * transparency level
     */
    protected float transparencyLevel;
    /**
     * world information. For debug purposes
     */
    protected String info;
    /**
     * We need a constant to determine if an object has already been assigned a
     * 'real' bounding box
     */
    public static double BOUNDARY_BOX_EPSILON = 1e-15;
    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------

    /**
     * Constructor of cGenericObject.
     */
    public JGenericObject() {

        parent = null;
        localPosition = new JVector3d(0.0, 0.0, 0.0);
        globalPosition = new JVector3d(0.0, 0.0, 0.0);
        visible = true;
        frameVisible = false;
        frameSize = (1.0);
        frameThicknessScale = (2.0);
        boundaryBoxMin = new JVector3d(0.0, 0.0, 0.0);
        boundaryBoxMax = new JVector3d(0.0, 0.0, 0.0);
        boxVisible = (false);
        boundaryBoxColor = new JColorf(0.5f, 0.5f, 0.0f, 0.0f);
        treeVisible = (false);
        treeColor = new JColorf(0.5f, 0.0f, 0.0f, 0.0f);
        collisionDetector = (null);
        collisionTreeVisible = (false);
        hapticEnabled = (true);
        tag = (-1);
        userData = (0);

        // initialize local position and orientation
        localRotation = new JMatrix3d();
        localRotation.identity();

        // initialize global position and orientation
        globalRotation = new JMatrix3d();
        globalRotation.identity();

        // initialize openGL matrix with position ArrayList and orientation matrix
        frameGL = new JMatrixGL();
        frameGL.set(globalPosition, globalRotation);

        // custom user information
        objectName = "";

        // should we use the material property?
        materialEnabled = true;

        // Are vertex colors used during rendering process?
        vertexColorsEnabled = false;

        // Should transparency be used?
        transparencyEnabled = false;

        // Should texture mapping be used if a texture is defined?
        textureMappingEnabled = false;

        // How are triangles displayed; FILL or LINE ?
        triangleMode = GL2.GL_FILL;

        // initialize texture
        texture = new JTexture2D();

        // turn culling on by default
        cullingEnabled = true;

        // by default, if transparency is enabled, use the multi-pass approach
        multipassTransparencyEnabled = true;

        // disable ghost setting
        ghost = false;

        // no external parent defined
        externalParent = null;

        // by default, the object is the super parent of itself
        superParent = this;

        childrens = new ArrayList<JGenericObject>();

        material = new JMaterial();

        interactionProjectedPoint = new JVector3d();

        effects = new ArrayList<JGenericEffect>();

        previousGlobalPosition = new JVector3d();

        previousGlobalRotation = new JMatrix3d();
        previousGlobalRotation.identity();
    }

    //-----------------------------------------------------------------------
    // METHODS - TRANSLATION AND ORIENTATION:
    //-----------------------------------------------------------------------
    /**
     * Set the local position of this object.
     */
    public void setPosition(final JVector3d aPos) {
        localPosition.copyFrom(aPos);
    }

    /**
     * Set the local position of this object.
     */
    public void setPosition(final double aX, final double aY, final double aZ) {
        localPosition.set(aX, aY, aZ);
    }

    /**
     * Get the local position of this object.
     */
    public final JVector3d getPosition() {
        return (localPosition);
    }

    /**
     * Get the global position of this object.
     */
    public final JVector3d getGlobalPosition() {
        return (globalPosition);
    }

    /**
     * Set the local rotation matrix for this object.
     */
    public void setRotation(final JMatrix3d aRot) {
        localRotation.copyFrom(aRot);
    }

    /**
     * Get the local rotation matrix of this object.
     */
    public final JMatrix3d getRotation() {
        return (localRotation);
    }

    /**
     * Get the global rotation matrix of this object.
     */
    public final JMatrix3d getGlobalRotation() {
        return (globalRotation);
    }

    /**
     * Translate this object by a specified offset.
     *
     * @param aTranslation
     */
    public void translate(final JVector3d aTranslation) {
        // apply the translation to this object
        JVector3d newPosition = JMaths.jAdd(localPosition, aTranslation);
        localPosition.copyFrom(newPosition);
    }

    /**
     * Translate an object by a specified offset.
     *
     * @param aX
     * @param aY
     * @param aZ
     */
    public void translate(final double aX, final double aY, final double aZ) {
        translate(new JVector3d(aX, aY, aZ));
    }

    /**
     * Rotate this object by multiplying with a specified rotation matrix.
     *
     * @param aRotation
     */
    public void rotate(final JMatrix3d aRotation) {
        JMatrix3d newRot = localRotation;
        newRot.mul(aRotation);
        setRotation(newRot);
    }

    /**
     * Rotate this object around axis a_axis by angle a_angle (radians). a_axis
     * is not normalized, so unless you know what you're doing, normalize your
     * axis before supplying it to this function.
     *
     * @param aAxis
     * @param aAngle
     */
    public void rotate(final JVector3d aAxis, final double aAngle) {
        JMatrix3d newRot = localRotation;
        newRot.rotate(aAxis, aAngle);
        setRotation(newRot);
    }

    //-----------------------------------------------------------------------
    // METHODS - GLOBAL / LOCAL POSITIONS:
    //-----------------------------------------------------------------------
    /**
     * Compute globalPos and globalRot given the localPos and localRot of this
     * object and its parents. Optionally propagates to children. \n If \e
     * a_frameOnly is set to \b false, additional global positions such as
     * vertex positions are computed (which can be time-consuming). \n Call this
     * method any time you've moved an object and will need to access to
     * globalPos and globalRot in this object or its children. For performance
     * reasons, these values are not kept up-to-date by default, since almost
     * all operations use local positions and rotations.?
     *
     * @param aFrameOnly
     * @param aGlobalPos
     * @param aGlobalRot
     */
    public void computeGlobalPositions(final boolean aFrameOnly,
            final JVector3d aGlobalPos,
            final JMatrix3d aGlobalRot) {
        // check if node is a ghost. If yes, then ignore call
        if (ghost) {
            return;
        }

        // current values become previous values
        previousGlobalPosition.copyFrom(globalPosition);
        previousGlobalRotation.copyFrom(globalRotation);

        // update global position ArrayList and global rotation matrix
        if (aGlobalPos != null) {
            //globalPosition.copyfrom(JMaths.jAdd(aGlobalPos, JMaths.jMul(aGlobalRot, localPosition)));
            globalPosition.copyFrom(JMaths.jAdd(aGlobalPos, JMaths.jMul(aGlobalRot, localPosition)));
        }
        if (aGlobalRot != null) {
            globalRotation.copyFrom(JMaths.jMul(aGlobalRot, localRotation));
        }

        // update any positions within the current object that need to be
        // updated (e.g. vertex positions)
        updateGlobalPositions(aFrameOnly);


        // propagate this method to my children
        for (int i = 0; i < childrens.size(); i++) {
            childrens.get(i).computeGlobalPositions(aFrameOnly, globalPosition, globalRotation);
        }
    }

    public void computeGlobalPositions(final boolean aFrameOnly) {
        JMatrix3d rot = new JMatrix3d();
        rot.identity();
        computeGlobalPositions(aFrameOnly, new JVector3d(), rot);
    }

    public ArrayList<JGenericObject> getChildrens() {
        return childrens;
    }

    public void computeGlobalCurrentObjectOnly() {
        computeGlobalCurrentObjectOnly(true);
    }

    /**
     * Compute globalPos and globalRot for this object only, by recursively
     * climbing up the scene graph tree until the root is reached.
     *
     * @param aFrameOnly
     */
    public void computeGlobalCurrentObjectOnly(final boolean aFrameOnly) {

        JMatrix3d globalRot = new JMatrix3d();
        JVector3d globalPos = new JVector3d();
        globalRot.identity();
        globalPos.zero();

        // get a pointer to current object
        JGenericObject curObject = this;

        // walk up the scene graph until we reach the root, updating
        // my global position and rotation at each step
        do {
            curObject.getRotation().mul(globalPos);
            globalPos.add(curObject.getPosition());
            JMatrix3d rot = new JMatrix3d();
            curObject.getRotation().mulr(globalRot, rot);
            rot.copyTo(globalRot);
            curObject = curObject.getParent();
        } while (curObject != null);

        // update values
        globalPosition.copyFrom(globalPos);
        globalRotation.copyFrom(globalRot);

        // update any positions within the current object that need to be
        // updated (e.g. vertex positions)
        updateGlobalPositions(aFrameOnly);
    }

    public void computeGlobalPositionsAndMotion() {
        computeGlobalPositionsAndMotion(true, new JVector3d(), JMaths.jIdentity3d());
    }

    /**
     * Compute the global position and rotation with relative motion of this
     * object and its children.
     */
    public void computeGlobalPositionsAndMotion(final boolean aFrameOnly,
            final JVector3d aGlobalPos,
            final JMatrix3d aGlobalRot) {
    }

    //-----------------------------------------------------------------------
    // METHODS - INTERACTIONS, FORCES AND EFFECTS:
    //-----------------------------------------------------------------------
    /**
     * Descend through child objects to compute interactions for all
     * cGenericEffects.
     */
    public JVector3d computeInteractions(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int a_IDN,
            JInteractionRecorder aInteractions,
            JInteractionSettings aInteractionSettings) {
        // check if node is a ghost. If yes, then ignore call
        if (ghost) {
            return (new JVector3d(0, 0, 0));
        }

        JMatrix3d localRotTrans = new JMatrix3d();
        localRotation.transr(localRotTrans);

        // compute local position of tool and velocity ArrayList
        JVector3d toolPosLocal = JMaths.jMul(localRotTrans, JMaths.jSub(aToolPos, localPosition));

        // compute interaction between tool and current object
        JVector3d toolVelLocal = JMaths.jMul(localRotTrans, aToolVel);

        // compute local interaction with current object
        computeLocalInteraction(toolPosLocal,
                toolVelLocal,
                a_IDN);

        // compute forces based on the effects programmed for this object
        JVector3d localForce = new JVector3d(0, 0, 0);

        if (hapticEnabled) {
            // compute each force effect
            boolean interactionEvent = false;
            for (JGenericEffect nextEffect : effects) {

                if (nextEffect.isEnabled()) {
                    JVector3d force = new JVector3d(0, 0, 0);

                    interactionEvent = interactionEvent
                            || nextEffect.computeForce(toolPosLocal,
                            toolVelLocal,
                            a_IDN,
                            force);
                    localForce.add(force);
                }
            }

            // report any interaction
            if (interactionEvent) {
                JInteractionEvent newInteractionEvent = new JInteractionEvent();
                newInteractionEvent.setObject(this);
                newInteractionEvent.setInside(interactionInside);
                newInteractionEvent.setLocalPosition(toolPosLocal);
                newInteractionEvent.setLocalSurfacePosition(interactionProjectedPoint);
                newInteractionEvent.setLocalForce(localForce);
                aInteractions.add(newInteractionEvent);
            }

            // compute any other force interactions
            JVector3d force = computeOtherInteractions(toolPosLocal,
                    toolVelLocal,
                    a_IDN,
                    aInteractions,
                    aInteractionSettings);

            localForce.add(force);
        }

        // descend through the children
        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);

            JVector3d force = nextObject.computeInteractions(toolPosLocal,
                    toolVelLocal,
                    a_IDN,
                    aInteractions,
                    aInteractionSettings);
            localForce.add(force);
        }

        // convert the reaction force into my parent coordinates
        JVector3d m_globalForce = JMaths.jMul(localRotation, localForce);

        // return resulting force
        return (m_globalForce);
    }

    /**
     * Adds a haptic effect to the current object
     *
     * @param aEffect
     */
    public void addEffect(JGenericEffect aEffect) {
        // update the effect object's parent pointer
        aEffect.parent = this;

        // add this child to my list of children
        effects.add(aEffect);
    }

    //-----------------------------------------------------------------------
    // METHODS - HAPTIC PROPERTIES:
    //-----------------------------------------------------------------------
    /**
     * Set the haptic stiffness, possibly recursively affecting children.
     */
    public void setStiffness(double aStiffness) {
        setStiffness(aStiffness, false);
    }

    public void setStiffness(double aStiffness, final boolean aAffectChildren) {
        material.setStiffness(aStiffness);

        // propagate changes to children
        if (aAffectChildren) {
            for (JGenericObject children : childrens) {
                children.setStiffness(aStiffness, aAffectChildren);
            }
        }
    }

    /**
     * Set the static and dynamic friction for this mesh, not affect children.
     */
    public void setFriction(double aStaticFriction, double aDynamicFriction) {
        setFriction(aStaticFriction, aDynamicFriction, false);
    }

    /**
     * Set the static and dynamic friction for this mesh, possibly recursively
     * affecting children.
     */
    public void setFriction(double aStaticFriction, double aDynamicFriction, final boolean aAffectChildren) {
        material.setStaticFriction(aStaticFriction);
        material.setDynamicFriction(aDynamicFriction);

        // propagate changes to children
        if (aAffectChildren) {
            for (JGenericObject children : childrens) {
                children.setFriction(aStaticFriction, aDynamicFriction, aAffectChildren);
            }
        }
    }
    //-----------------------------------------------------------------------
    // METHODS - GRAPHICS:
    //-----------------------------------------------------------------------

    /**
     * Show or hide this object. Do not propagate chages to its children.
     *
     *
     * @param aShow
     */
    public void setVisible(final boolean aShow) {
        setVisible(aShow, false);
    }

    /**
     * Show or hide this object. \n
     *
     * If \e a_affectChildren is set to \b true then all children are updated
     * with the new value.
     *
     * @param aShow
     * @param aAffectChildren
     */
    public void setVisible(final boolean aShow, final boolean aAffectChildren) {
        // update current object
        visible = aShow;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setVisible(aShow, true);
            }
        }
    }

    /**
     * Read the display status of object (true means it's visible).
     */
    public final boolean isVisible() {
        return (visible);
    }

    /**
     * Allow or disallow the object to be felt (when visible). \n
     *
     * @param aHapticEnabled
     * @param aAffectChildren
     */
    public void setHapticEnabled(final boolean aHapticEnabled, final boolean aAffectChildren) {
        // update current object
        hapticEnabled = aHapticEnabled;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setHapticEnabled(aHapticEnabled, true);
            }
        }
    }

    /**
     * Read the haptic status of object (true means it can be felt when
     * visible).
     */
    public final boolean isHapticEnabled() {
        return (hapticEnabled);
    }

    /**
     * Show or hide the graphic representation of the scene graph at this node.
     * Do not affect children.
     *
     * @param aShowTree
     * @param aAffectChildren
     */
    public void setTreeVisible(final boolean aShowTree) {
        setTreeVisible(aShowTree, false);
    }

    /**
     * Show or hide the graphic representation of the scene graph at this node.
     * \n
     *
     * If \e a_affectChildren is set to \b true then all children are updated
     * with the new value.
     *
     * @param aShowTree
     * @param aAffectChildren
     */
    public void setTreeVisible(final boolean aShowTree, final boolean aAffectChildren) {

        // update current object
        treeVisible = aShowTree;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setTreeVisible(aShowTree, true);
            }
        }
    }

    /**
     * Read the display status of the tree (true means it's visible).
     */
    public final boolean isTreeVisible() {
        return (treeVisible);
    }

    public void setTreeColor(final JColorf aTreeColor) {
        setTreeColor(aTreeColor, false);
    }

    /**
     * Set the tree color, optionally propagating the change to children.
     */
    public void setTreeColor(final JColorf aTreeColor, final boolean aAffectChildren) {
        // update current object
        treeColor = aTreeColor;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setTreeColor(aTreeColor, true);
            }
        }
    }

    /**
     * Read the tree color.
     */
    public final JColorf getTreeColor() {
        return (treeColor);
    }

    /**
     * Show or hide the set of arrows that represent this object's reference
     * frame.
     *
     * Do not affect children.
     *
     * @param aShowFrame
     * @param aAffectChildren
     */
    public final void setFrameVisible(final boolean aShowFrame) {
        setFrameVisible(aShowFrame, false);
    }

    /**
     * Show or hide the set of arrows that represent this object's reference
     * frame.
     *
     * If \e a_affectChildren is set to \b true then all children are updated
     * with the new value.
     *
     * @param aShowFrame
     * @param aAffectChildren
     */
    public final void setFrameVisible(final boolean aShowFrame, final boolean aAffectChildren) {

        // update current object
        frameVisible = aShowFrame;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setFrameVisible(aShowFrame, aAffectChildren);
            }
        }
    }

    /**
     * Read the display status of the reference frame (true means it's visible).
     */
    public final boolean isFrameVisible() {
        return (frameVisible);
    }

    public void setBoundaryBoxVisible(final boolean iShowBox) {
        setBoundaryBoxVisible(iShowBox, false);
    }

    /**
     * Show or hide the boundary box for this object, optionally propagating the
     * change to children.
     */
    public void setBoundaryBoxVisible(final boolean iShowBox, final boolean iAffectChildren) {
        // update current object
        boxVisible = iShowBox;

        // update children
        if (iAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setBoundaryBoxVisible(iShowBox, true);
            }
        }
    }

    /**
     * Read the display status of boundary box. (true means it's visible).
     */
    public final boolean isBoundaryBoxVisible() {
        return (boxVisible);
    }

    public void setBoxColor(final JColorf aBoxColor) {
        setBoxColor(aBoxColor, false);
    }

    /**
     * Set the color of boundary box for this object, optionally propagating the
     * change to children.
     */
    public void setBoxColor(final JColorf aBoxColor, final boolean aAffectChildren) {
        // update current object
        boundaryBoxColor = aBoxColor;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setBoxColor(aBoxColor, true);
            }
        }
    }

    /**
     * Read the color of boundary box.
     */
    public final JColorf getBoxColor() {
        return (boundaryBoxColor);
    }

    public void setCollisionTreeVisible(final boolean aShowCollisionTree) {
        setCollisionTreeVisible(aShowCollisionTree, false);
    }

    /**
     * Show or hide the collision tree for this object, optionally propagating
     * the change to children.
     */
    public void setCollisionTreeVisible(final boolean aShowCollisionTree, final boolean aAffectChildren) {
        // update current object
        collisionTreeVisible = aShowCollisionTree;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setCollisionTreeVisible(aShowCollisionTree, true);
            }
        }
    }

    /**
     * Read the display status of of the collision tree for this object.
     */
    public boolean isCollisionTreeVisible() {
        return (collisionTreeVisible);
    }

    public void onDisplayReset() {
        onDisplayReset(false);
    }

    /**
     * Users should call this function when it's necessary to re-initialize the
     * OpenGL context; e.g. re-initialize textures and display lists. Subclasses
     * should perform whatever re-initialization they need to do. \n Note that
     * this is not an event CHAI can easily detect, so it's up to the
     * application-writer to call this function on the root of the scenegraph.
     *
     * @param aAffectChildren
     */
    public void onDisplayReset(final boolean aAffectChildren) {
        // Since I don't have any display context to update, I don't do anything here...

        // We _don't_ call this method on the current object, which allows subclasses
        // to do their business in this method, then call the cGenericObject version
        // to propagate the call through the scene graph

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).onDisplayReset(true);
            }
        }
    }

    /**
     * This call tells an object that you're not going to modify him any more.
     * For example, a mesh-like object might optimize his vertex arrangement
     * when he gets this call. Always optional; just for performance...
     *
     * @param aAffectChildren
     */
    public void finalizeObject() {
        finalizeObject(true);
    }

    public void finalizeObject(final boolean aAffectChildren) {
        // We _don't_ call this method on the current object, which allows subclasses
        // to do their business in this method, then call the cGenericObject version
        // to propagate the call through the scene graph

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).finalizeObject(true);
            }
        }
    }

    public void unfinalizeObject() {
        unfinalizeObject(true);
    }

    /**
     * This call tells an object that you may modify his contents. See
     * finalize() for more information.
     *
     * @param aAffectChildren
     */
    public void unfinalizeObject(final boolean aAffectChildren) {
        // We _don't_ call this method on the current object, which allows subclasses
        // to do their business in this method, then call the cGenericObject version
        // to propagate the call through the scene graph

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).unfinalizeObject(true);
            }
        }
    }

    public void renderSceneGraph() {
        render(JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL);
    }

    /**
     * Render the entire scene graph, starting from this object.
     */
    public void renderSceneGraph(final JChaiRenderMode aRenderMode) {
        //-----------------------------------------------------------------------
        // Initialize rendering
        //-----------------------------------------------------------------------

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        // rotate the current reference frame to match this object's
        // reference frame
        frameGL.set(localPosition, localRotation);
        //frameGL2.setGL(gl);
        frameGL.glMatrixPushMultiply();

        // Handle rendering meta-object components, e.g. collision trees,
        // bounding boxes, scenegraph tree, etc.
        // set up useful rendering state
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_BLEND);
        gl.glDepthMask(true);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        //-----------------------------------------------------------------------
        // Render non transparent components of JGenericObject
        //-----------------------------------------------------------------------

        if (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY
                || aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL) {
            // disable lighting
            gl.glDisable(GL2.GL_LIGHTING);

            // render tree
            if (treeVisible) {

                // set size on lines
                gl.glLineWidth(1.0f);

                // set color of tree
                gl.glColor4fv(FloatBuffer.wrap(treeColor.getComponents()));

                // render tree
                for (int i = 0; i < childrens.size(); i++) {
                    JGenericObject nextChild = childrens.get(i);

                    // draw a line from origin of current frame to origin  of child frame
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex3d(0.0, 0.0, 0.0);
                    gl.glVertex3dv(DoubleBuffer.wrap(nextChild.getPosition().toArray()));
                    gl.glEnd();
                }
            }

            // render boundary box
            if (boxVisible) {
                // set size on lines
                gl.glLineWidth(1.0f);

                // set color of boundary box
                gl.glColor4fv(FloatBuffer.wrap(boundaryBoxColor.getComponents()));

                // draw box line
                JDraw3D.jDrawWireBox(boundaryBoxMin.getX(), boundaryBoxMax.getX(),
                        boundaryBoxMin.getY(), boundaryBoxMax.getY(),
                        boundaryBoxMin.getZ(), boundaryBoxMax.getZ());
            }

            // render collision tree
            if (collisionTreeVisible && (collisionDetector != null)) {
                collisionDetector.render();
            }

            // enable lighting
            gl.glEnable(GL2.GL_LIGHTING);

            // render frame
            if (frameVisible) {
                // set rendering properties
                gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);

                // draw frame
                JDraw3D.jDrawFrame(frameSize, frameThicknessScale, true);
            }
        }

        //-----------------------------------------------------------------------
        // Render graphical representation of object
        //-----------------------------------------------------------------------
        if (visible) {
            // set polygon and face mode
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, triangleMode);


            /////////////////////////////////////////////////////////////////////
            // SINGLE PASS RENDERING
            /////////////////////////////////////////////////////////////////////

            if (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL) {
                if (cullingEnabled) {
                    gl.glEnable(GL2.GL_CULL_FACE);
                } else {
                    gl.glDisable(GL2.GL_CULL_FACE);
                }

                if (transparencyEnabled) {
                    gl.glEnable(GL2.GL_BLEND);
                    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
                    gl.glDepthMask(false);
                } else {
                    gl.glDisable(GL2.GL_BLEND);
                    gl.glDepthMask(true);
                }
                render(aRenderMode);
            } /////////////////////////////////////////////////////////////////////
            // MULTI PASS RENDERING
            /////////////////////////////////////////////////////////////////////
            // opaque objects
            else if (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY) {
                if (cullingEnabled) {
                    gl.glEnable(GL2.GL_CULL_FACE);
                } else {
                    gl.glDisable(GL2.GL_CULL_FACE);
                }

                render(aRenderMode);
            } // render transparent front triangles
            else if (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY) {
                if (transparencyEnabled) {
                    gl.glEnable(GL2.GL_BLEND);
                    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
                    gl.glDepthMask(false);
                } else {
                    gl.glDisable(GL2.GL_BLEND);
                    gl.glDepthMask(true);
                }

                gl.glEnable(GL2.GL_CULL_FACE);
                gl.glCullFace(GL2.GL_BACK);
                render(aRenderMode);
            } else if (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY) {
                if (transparencyEnabled) {
                    gl.glEnable(GL2.GL_BLEND);
                    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
                    gl.glDepthMask(false);
                } else {
                    gl.glDisable(GL2.GL_BLEND);
                    gl.glDepthMask(true);
                }

                gl.glEnable(GL2.GL_CULL_FACE);
                gl.glCullFace(GL2.GL_FRONT);
                render(aRenderMode);
            }
        }

        // render children
        for (int i = 0; i < childrens.size(); i++) {
            childrens.get(i).renderSceneGraph(aRenderMode);
        }

        // pop current matrix
        frameGL.glMatrixPop();

        // restore settings
        gl.glDisable(GL2.GL_CULL_FACE);
    }

    //-----------------------------------------------------------------------
    // METHODS - GRAPHIC RENDERING:
    //-----------------------------------------------------------------------
    /**
     * Set the material for this mesh, not propagate tho children.
     */
    public void setMaterial(JMaterial aMat) {
        setMaterial(aMat, true, false);
    }

    /**
     * Set the material for this mesh, and optionally pass it on to my children.
     */
    public void setMaterial(JMaterial aMat, final boolean aAffectChildren) {
        setMaterial(aMat, aAffectChildren, false);
    }

    public void setMaterial(JMaterial aMat, final boolean aAffectChildren, final boolean aApplyPhysicalParmetersOnly) {
        
        if (aApplyPhysicalParmetersOnly) {
            material.setDynamicFriction(aMat.getDynamicFriction());
            material.setMagnetMaxDistance(aMat.getMagnetMaxDistance());
            material.setMagnetMaxForce(aMat.getMagnetMaxForce());
            material.setStaticFriction(aMat.getStaticFriction());
            material.setStickSlipForceMax(aMat.getStickSlipForceMax());
            material.setStickSlipStiffness(aMat.getStickSlipStiffness());
            material.setVibrationAmplitude(aMat.getVibrationAmplitude());
            material.setVibrationFrequency(aMat.getVibrationFrequency());
            material.setViscosity(aMat.getViscosity());
        } else {
            material.copyFrom(aMat);
        }

        // propagate changes to children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setMaterial(aMat, true, aApplyPhysicalParmetersOnly);
            }
        }
    }

    public void setTransparencyLevel(final float aLevel) {
        setTransparencyLevel(aLevel, false, true);
    }

    /**
     * Set the alpha value at each vertex and in all of my material colors.
     */
    public void setTransparencyLevel(final float aLevel,
            final boolean aApplyToTextures,
            final boolean aAffectChildren) {

        transparencyLevel = aLevel;

        // if the transparency level is equal to 1.0, then do not apply transparency
        // otherwise enable it.
        if (aLevel < 1.0) {
            setTransparencyEnabled(true, true);
        } else {
            setTransparencyEnabled(false, true);
        }

        // apply new value to material
        material.setTransparencyLevel(aLevel);

        // apply changes to texture if required
        /*
         * if(aApplyToTextures && texture != null) {
         * texture.setTransparency(255.0f * aLevel); }
         *
         */

        // propagate the operation to my children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setTransparencyLevel(aLevel,
                        aApplyToTextures,
                        aAffectChildren);
            }
        }
    }

    public void setMultipassTransparencyEnabled(final boolean aUseMultipassTransparency) {
        setMultipassTransparencyEnabled(aUseMultipassTransparency, false);
    }

    /**
     * Specify whether this mesh should use multipass transparency (see
     * cCamera).
     */
    public void setMultipassTransparencyEnabled(final boolean aUseMultipassTransparency, final boolean aAffectChildren) {
        // update changes to object
        multipassTransparencyEnabled = aUseMultipassTransparency;

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setMultipassTransparencyEnabled(aUseMultipassTransparency, aAffectChildren);
            }
        }
    }

    /**
     * Is multipass transparency used for this mesh?
     */
    public final boolean isMultipassTransparencyEnabled() {
        return multipassTransparencyEnabled;
    }

    /**
     * Enable or disable transparency (also see setTransparencyRenderMode)...
     * turns the depth mask _off_!
     */
    public void setTransparencyEnabled(final boolean aUseTransparency) {
        setTransparencyEnabled(aUseTransparency, true);
    }

    /**
     * Enable or disable transparency (also see setTransparencyRenderMode)...
     * turns the depth mask _off_!
     */
    public void setTransparencyEnabled(final boolean aUseTransparency, final boolean aAffectChildren) {
        // update changes to object
        transparencyEnabled = aUseTransparency;

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setTransparencyEnabled(aUseTransparency, aAffectChildren);
            }
        }
    }

    /**
     * Is transparency enabled for this mesh?
     */
    public final boolean isTransparencyEnabled() {
        return transparencyEnabled;
    }

    /**
     * Enable or disable wireframe rendering, optionally propagating the
     * operation to my children.
     */
    public void setWireMode(final boolean aShowWireMode, final boolean aAffectChildren) {
        // update changes to object
        if (aShowWireMode) {
            triangleMode = GL2.GL_LINE;
        } else {
            triangleMode = GL2.GL_FILL;
        }

        // update changes to children
        if (aAffectChildren) {
            int i, numItems;
            numItems = childrens.size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setWireMode(aShowWireMode, aAffectChildren);
            }
        }
    }

    /**
     * Return whether wireframe rendering is enabled.
     */
    public final boolean isWireMode() {
        return triangleMode == GL2.GL_LINE;
    }

    /**
     * Enable or disabling face-culling, propagating the operation to my
     * children.
     */
    public void setCullingEnabled(final boolean aUseCulling) {
        setCullingEnabled(aUseCulling, true);
    }

    /**
     * Enable or disabling face-culling, optionally propagating the operation to
     * my children.
     */
    public void setCullingEnabled(final boolean aUseCulling, final boolean aAffectChildren) {
        // apply changes to this object
        cullingEnabled = aUseCulling;

        // propagate changes to children
        if (aAffectChildren) {
            int i, numItems;
            numItems = childrens.size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setCullingEnabled(aUseCulling, aAffectChildren);
            }
        }
    }

    /**
     * Is face-culling currently enabled?
     */
    public final boolean isCullingEnabled() {
        return cullingEnabled;
    }

    /**
     * Enable or disable the use of per-vertex colors, propagating the operation
     * to my children.
     *
     * @param aUseColors
     * @param aAffectChildren
     */
    public void setVertexColorsEnabled(final boolean aUseColors) {
        setVertexColorsEnabled(aUseColors, true);
    }

    public void setVertexColorsEnabled(final boolean aUseColors, final boolean aAffectChildren) {
        // update changes to object
        vertexColorsEnabled = aUseColors;

        // update changes to children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setVertexColorsEnabled(aUseColors, aAffectChildren);
            }
        }
    }

    /**
     * Are per-vertex properties currently enabled?
     */
    public final boolean isVertexColorsEnabled() {
        return vertexColorsEnabled;
    }

    /**
     * Enable or disable the use of material properties, optionally propagating
     * the operation to my children.
     *
     * @param aUseMaterial
     * @param aAffectChildren
     */
    public void setMaterialEnabled(final boolean aUseMaterial) {
        setMaterial(material, true);
    }

    public void setMaterialEnabled(final boolean aUseMaterial, final boolean aAffectChildren) {
        // update changes to object
        materialEnabled = aUseMaterial;

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setMaterialEnabled(aUseMaterial, aAffectChildren);
            }
        }
    }

    /**
     * Are material properties currently enabled?
     */
    public final boolean isMaterialEnabled() {
        return materialEnabled;
    }

    /**
     * Enable or disable the use of texture-mapping, not propagate to children.
     */
    public void setTextureMappingEnabled(final boolean aUseTexture) {
        setTextureMappingEnabled(aUseTexture, false);
    }

    /**
     * Enable or disable the use of texture-mapping, optionally propagating the
     * operation to my children.
     */
    public void setTextureMappingEnabled(final boolean aUseTexture, final boolean aAffectChildren) {
        textureMappingEnabled = aUseTexture;

        // propagate changes to children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setTextureMappingEnabled(aUseTexture, aAffectChildren);
            }
        }
    }

    /**
     * Is texture-mapping enabled?
     */
    public final boolean isTextureMappingEnabled() {
        return textureMappingEnabled;
    }

    /**
     * Set my texture, but not affecting children.
     */
    public void setTexture(JTexture2D aTexture) {
        setTexture(aTexture, false);
    }

    /**
     * Set my texture, possibly recursively affecting children.
     */
    public void setTexture(JTexture2D aTexture, final boolean aAffectChildren) {
        this.texture = aTexture;
        // propagate changes to children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JGenericObject nextObject = childrens.get(i);
                nextObject.setTexture(aTexture, aAffectChildren);
            }
        }
    }

    /**
     * Access my texture.
     */
    public final JTexture2D getTexture() {
        return (texture);
    }

    //-----------------------------------------------------------------------
    // METHODS - BOUNDARY BOX:
    //-----------------------------------------------------------------------
    /**
     * Read the minimum point of this object's boundary box.
     */
    public final JVector3d getBoundaryBoxMin() {
        return (boundaryBoxMin);
    }

    /**
     * Read the maximum point of this object's boundary box.
     */
    public final JVector3d getBoundaryBoxMax() {
        return (boundaryBoxMax);
    }

    /**
     * Compute the center of this object's boundary box.
     */
    public final JVector3d getBoundaryCenter() {
        JVector3d tmp = boundaryBoxMax.operatorSub(boundaryBoxMin);
        tmp.div(2.0);
        return tmp;
    }

    /**
     * Re-compute this object's bounding box, optionally forcing it to bound
     * child objects.
     */
    public void computeBoundaryBox() {
        computeBoundaryBox(true);
    }

    /**
     * Re-compute this object's bounding box, optionally forcing it to bound
     * child objects.
     */
    public void computeBoundaryBox(final boolean aIncludeChildren) {
        // check if node is a ghost. If yes, then ignore call
        if (ghost) {
            return;
        }

        // compute the bounding box of this object
        updateBoundaryBox();

        if (aIncludeChildren == false) {
            return;
        }


        // compute the bounding box of all my children
        for (JGenericObject children : childrens) {
            children.computeBoundaryBox(aIncludeChildren);

            // see if this child has a _valid_ boundary box
            boolean childBoxValid = (JMaths.jAbs(JMaths.jDistance(children.getBoundaryBoxMax(),
                    children.getBoundaryBoxMin()))
                    > BOUNDARY_BOX_EPSILON);

            // don't include invalid boxes in my bounding box
            if (!childBoxValid) {
                continue;
            }

            // get position and rotation of child frame
            JMatrix3d rot = children.getRotation();
            JVector3d pos = children.getPosition();

            // enumerate each corner of the child's bounding box
            boolean[] xshifts = {false, false, false, false, true, true, true, true};
            boolean[] yshifts = {false, false, true, true, false, false, true, true};
            boolean[] zshifts = {false, true, false, true, false, true, false, true};

            JVector3d childBoxMin = new JVector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
            JVector3d childBoxMax = new JVector3d(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);

            JVector3d localmin = children.getBoundaryBoxMin();
            JVector3d localmax = children.getBoundaryBoxMax();

            // for each corner...
            for (int corner = 0; corner < 8; corner++) {
                // grab this corner of the child's bounding box
                JVector3d cornerLocationChild = new JVector3d();
                cornerLocationChild.setX((xshifts[corner]) ? localmin.getX() : localmax.getX());
                cornerLocationChild.setY((yshifts[corner]) ? localmin.getY() : localmax.getY());
                cornerLocationChild.setZ((zshifts[corner]) ? localmin.getZ() : localmax.getZ());

                // convert this point into the parent reference frame
                JVector3d cornerLocationParent = new JVector3d();
                rot.mulr(cornerLocationChild, cornerLocationParent);
                cornerLocationParent.add(pos);

                // is this a max or min on any axis?
                for (int k = 0; k < 3; k++) {
                    if (cornerLocationParent.get(k) < childBoxMin.get(k)) {
                        childBoxMin.set(k, cornerLocationParent.get(k));
                    }
                    if (cornerLocationParent.get(k) > childBoxMax.get(k)) {

                        childBoxMax.set(k, cornerLocationParent.get(k));
                    }

                }
            }

            // see if _I_ have a valid boundary box
            boolean currentBoxValid = (JMaths.jAbs(JMaths.jDistance(boundaryBoxMax, boundaryBoxMin)) > BOUNDARY_BOX_EPSILON);

            // if I don't, take my child's boundary box, which is valid...
            if (currentBoxValid == false) {
                boundaryBoxMin.copyFrom(childBoxMin);
                boundaryBoxMax.copyFrom(childBoxMax);
            } else {
                // compute new boundary
                boundaryBoxMin.setX(JMaths.jMin(boundaryBoxMin.getX(), childBoxMin.getX()));
                boundaryBoxMin.setY(JMaths.jMin(boundaryBoxMin.getY(), childBoxMin.getY()));
                boundaryBoxMin.setZ(JMaths.jMin(boundaryBoxMin.getZ(), childBoxMin.getZ()));

                // compute new boundary
                boundaryBoxMax.setX(JMaths.jMax(boundaryBoxMax.getX(), childBoxMax.getX()));
                boundaryBoxMax.setY(JMaths.jMax(boundaryBoxMax.getY(), childBoxMax.getY()));
                boundaryBoxMax.setZ(JMaths.jMax(boundaryBoxMax.getZ(), childBoxMax.getZ()));
            }
        }
    }

    //-----------------------------------------------------------------------
    // METHODS - REFERENCE FRAME REPRESENTATION:
    //-----------------------------------------------------------------------
    /**
     * Set the display size of the arrows representing my reference frame. The
     * size corresponds to the length of each displayed axis (X-Y-Z). \n
     *
     * If \e a_affectChildren is set to \b true then all children are updated
     * with the new value.
     *
     * @param aSize
     * @param aThickness
     * @param aAffectChildren
     * @return
     */
    public boolean setFrameSize(final double aSize, final double aThickness, final boolean aAffectChildren) {
        // check value of size
        if (aSize <= 0) {
            return (false);
        }

        // update current object
        frameSize = aSize;
        frameThicknessScale = aThickness;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setFrameSize(aSize, aThickness, aAffectChildren);
            }
        }

        // operation succeeded
        return (true);
    }

    /**
     * Read the size of the rendered reference frame.
     */
    public final double getFrameSize() {
        return (frameSize);
    }

    //-----------------------------------------------------------------------
    // METHODS - COLLISION DETECTION:
    //-----------------------------------------------------------------------
    /**
     * Set a collision detector for current object.
     */
    public void setCollisionDetector(JGenericCollision aCollisionDetector) {
        collisionDetector = aCollisionDetector;
    }

    /**
     * Get pointer to this object's current collision detector.
     */
    public final JGenericCollision getCollisionDetector() {
        return (collisionDetector);
    }

    /**
     * Set collision rendering properties.
     */
    public void setCollisionDetectorProperties(int aDisplayDepth, JColorf aColor, final boolean aAffectChildren) {
        // update current collision detector
        if (collisionDetector != null) {
            collisionDetector.setTreeColor(aColor);
            collisionDetector.setDisplayDepth(aDisplayDepth);
        }

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setCollisionDetectorProperties(aDisplayDepth,
                        aColor, aAffectChildren);
            }
        }
    }

    /**
     * Delete any existing collision detector and set the current cd to null (no
     * collisions).
     */
    public void deleteCollisionDetector(final boolean aAffectChildren) {
        if (collisionDetector != null) {
            collisionDetector = null;
        }

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).deleteCollisionDetector(true);
            }
        }
    }

    /**
     * Compute collision detection using collision trees
     */
    public boolean computeCollisionDetection(JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JCollisionRecorder aRecorder,
            JCollisionSettings aSettings) {
        // check if node is a ghost. If yes, then ignore call
        if (ghost) {
            return (false);
        }

        // temp variable
        boolean hit = false;

        // get the transpose of the local rotation matrix
        JMatrix3d transLocalRot = new JMatrix3d();
        localRotation.transr(transLocalRot);

        // convert first endpoint of the segment into local coordinate frame
        JVector3d localSegmentPointA = new JVector3d(aSegmentPointA);
        localSegmentPointA.sub(localPosition);
        transLocalRot.mul(localSegmentPointA);

        // convert second endpoint of the segment into local coordinate frame
        JVector3d localSegmentPointB = new JVector3d(aSegmentPointB);
        localSegmentPointB.sub(localPosition);
        transLocalRot.mul(localSegmentPointB);

        // check for a collision with this object if:
        // (1) it has a collision detector
        // (2) if other settings (visible and haptic enabled) are activated
        if ((collisionDetector != null)
                && (!aSettings.isCheckVisibleObjectsOnly() || visible)
                && (!aSettings.isCheckHapticObjectsOnly() || hapticEnabled)) {
            // adjust the first segment endpoint so that it is in the same position
            // relative to the moving object as it was at the previous haptic iteration
            JVector3d localSegmentPointAadjusted = new JVector3d();
            if (aSettings.isAdjustObjectMotion()) {
                adjustCollisionSegment(localSegmentPointA, localSegmentPointAadjusted);
                //System.out.println("adjust");
            } else {
                localSegmentPointAadjusted.copyFrom(localSegmentPointA);
                //System.out.println("original");
            }


            // call the collision detector's collision detection function
            if (collisionDetector.computeCollision(localSegmentPointAadjusted,
                    localSegmentPointB,
                    aRecorder,
                    aSettings)) {

                // record that there has been a collision
                hit = true;
                //System.out.println("Collision");
            }
        }

        // compute any other collisions. This is a virtual function that can be extended for
        // classes that may contain other objects (sibbling) for wich collision detection may
        // need to be computed.
        hit = hit || computeOtherCollisionDetection(localSegmentPointA,
                localSegmentPointB,
                aRecorder,
                aSettings);

        // check for collisions with all children of this object
        for (int i = 0; i < childrens.size(); i++) {
            // call this child's collision detection function to see if it (or any
            // of its descendants) are intersected by the segment
            boolean hitChild = childrens.get(i).computeCollisionDetection(localSegmentPointA,
                    localSegmentPointB,
                    aRecorder,
                    aSettings);

            // update if a hit ocured
            hit = hit || hitChild;
        }


        // return whether there was a collision between the segment and this world
        return (hit);
    }

    /**
     * Adjust the given segment such that it tests for intersection of the ray
     * with objects at their previous positions at the last haptic loop so that
     * collision detection will work in a dynamic environment.
     *
     * @param aSegmentPointA Start point of segment.
     * @param aSegmentPointAadjusted Same segment, adjusted to be in local space.
     */
    public void adjustCollisionSegment(final JVector3d aSegmentPointA,
            JVector3d aSegmentPointAadjusted) {
        // convert point from local to global coordinates by using
        // the previous object position and orientation
        JVector3d point = JMaths.jAdd(globalPosition, JMaths.jMul(globalRotation, aSegmentPointA));
        
        // compute the new position of the point based on
        // the new object position and orientation
        aSegmentPointAadjusted.copyFrom(
                JMaths.jMul(
                    JMaths.jTrans(previousGlobalRotation),
                    JMaths.jSub(point, previousGlobalPosition)));
        
    }

    //-----------------------------------------------------------------------
    // METHODS - SCENE GRAPH:
    //-----------------------------------------------------------------------
    /**
     * Set parent of current object.
     */
    public void setParent(JGenericObject aParent) {
        parent = aParent;
    }

    /**
     * Read parent of current object.
     */
    public final JGenericObject getParent() {
        return (parent);
    }

    /**
     * Read an object from my list of children.
     */
    public final JGenericObject getChild(final int aIndex) {
        return (childrens.get(aIndex));
    }

    public final void setChildAt(JGenericObject child, int index) {
        childrens.set(index, child);
    }

    /**
     * Adds an object to the scene graph below this object.
     *
     * @param aObject
     */
    public void addChild(JGenericObject aObject) {
        if (aObject != null) {
            // update the child object's parent pointer
            aObject.setParent(this);

            // add this child to my list of children
            childrens.add(aObject);
        }
    }

    /**
     * Removes an object from my list of children, without deleting the child
     * object from memory.
     *
     * This method assigns the child object's parent point to null, so if you're
     * moving someone around in your scene graph, make sure you call this
     * function _before_ you add the child to another node in the scene graph.
     *
     * @param aObject
     * @return
     */
    public boolean removeChild(JGenericObject aObject) {
        //std::ArrayList<cGenericObject*>::iterator nextObject;

        Iterator<JGenericObject> iter = childrens.iterator();
        JGenericObject nextObject = null;

        while (iter.hasNext()) {
            nextObject = iter.next();
            // Did we find the object we're trying to delete?
            if ((nextObject) == aObject) {
                // he doesn't have a parent any more
                aObject.setParent(null);

                // remove this object from my list of children
                childrens.remove(nextObject);

                // return success
                return (true);
            }

        }

        // operation failed
        return (false);
    }

    /**
     * Does this object have the specified object as a child?
     *
     * @param aObject
     * @param aIncludeChildren
     * @return
     */
    public boolean containsChild(JGenericObject aObject, boolean aIncludeChildren) {
        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);
            if (nextObject == aObject) {
                return (true);
            }

            if (aIncludeChildren) {
                boolean result = nextObject.containsChild(aObject, true);
                if (result) {
                    return (true);
                }
            }
        }
        return (false);
    }

    /**
     * Removes an object from my list of children, and deletes the child object
     * from memory.
     *
     * @param aObject
     * @return
     */
    public boolean deleteChild(JGenericObject aObject) {
        // remove object from list
        boolean result = removeChild(aObject);

        // if operation succeeds, delete the object
        if (result) {
            aObject = null;
        }

        // return result
        return result;
    }

    /**
     * Clear all objects from my list of children, without deleting them.
     */
    public void clearAllChildrens() {
        // clear children list
        childrens.clear();
    }

    /**
     * Delete and clear all objects from my list of children.
     */
    public void deleteAllChildren() {
        // clear my list of children
        childrens.clear();
    }

    /**
     * Return the number of children on my list of children.
     */
    public int getNumChildren() {
        return ((int) childrens.size());
    }

    public int getDescendantCount() {
        return getDescendantCount(false);
    }

    /**
     * Return my total number of descendants, optionally including this object.
     *
     * @param aIncludeCurrentObject
     * @return
     */
    public int getDescendantCount(boolean aIncludeCurrentObject) {
        int numDescendants = aIncludeCurrentObject ? 1 : 0;

        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);
            numDescendants += nextObject.getDescendantCount(true);
        }

        return numDescendants;
    }

    /**
     * Fill this list with all of my descendants. The current object is
     * optionally included in this list. Does not clear the list before
     * appending to it.
     *
     * @param aChildList
     * @param aIncludeCurrentObject
     */
    public void enumerateChildren(ArrayList<JGenericObject> aChildList, boolean aIncludeCurrentObject) {
        if (aIncludeCurrentObject) {
            aChildList.add(this);
        }

        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);
            nextObject.enumerateChildren(aChildList, true);
        }
    }

    /**
     *
     * Remove me from my parent's CHAI scene graph.
     *
     * @return
     */
    public boolean removeFromParent() {
        if (parent != null) {
            return getParent().removeChild(this);
        } else {
            return false;
        }
    }

    /**
     * It can be sometimes useful to add an object twice in a scenegraph to
     * create for instance a reflexion of a model over a ground. In these cases
     * there only one model for which all the interactions should be computed.
     * The second one is only there for display purposes. By enabling the ghost
     * status, we disable all collision detection, force interaction further
     * down the scene graph. An example of this feature can be found in the
     * example "ODE-cube".
     *
     * @param aGhostStatus
     */
    public void setGhost(boolean aGhostStatus) {
        ghost = aGhostStatus;
    }

    /**
     * Read the ghost status of this object.
     */
    public boolean isGhost() {
        return (ghost);
    }

    public void setExternalParent(JGenericType aExternalParent) {
        setExternalParent(aExternalParent, false);
    }
    //-----------------------------------------------------------------------
    // METHODS - LINKS TO EXTERNALS:
    //-----------------------------------------------------------------------

    /**
     * Sets a pointer to an external parent of the current object. Optionally
     * propagating the change to children. A pointer to an external parent
     * located outside of the scenegraph. This parameter can typically be used
     * if you want to attach an generic object to some other object outside of
     * CHAI3D of to an external representation such as a dynamics engine model.
     * See the ODE examples to understand how a generic object can be attached
     * to an ODE object.
     *
     * @param aExternalParent
     * @param aAffectChildren
     */
    public void setExternalParent(JGenericType aExternalParent, final boolean aAffectChildren) {

        // set external parent
        externalParent = aExternalParent;

        // apply change to children
        if (aAffectChildren == false) {
            return;
        }
        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);
            nextObject.setExternalParent(aExternalParent, true);
        }
    }

    /**
     * Get the external parent of current object.
     */
    public JGenericType getExternalParent() {
        return (externalParent);
    }

    public void setSuperParent(JGenericObject aSuperParent) {
        setSuperParent(aSuperParent, false);
    }

    /**
     * Sets the super parent of the current object. Optionally propagating the
     * change to children. A super parent points to another object generally
     * located higher up in the scene graph. When a mesh is created, the super
     * parent of its children will generally point towards the root of the mesh.
     * This parameter is automatically set by the 3D object file loader.
     *
     * @param aSuperParent
     * @param aAffectChildren
     */
    public void setSuperParent(JGenericObject aSuperParent, final boolean aAffectChildren) {

        // set super parent
        superParent = aSuperParent;

        // apply change to children
        if (aAffectChildren == false) {
            return;
        }
        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);
            nextObject.setSuperParent(aSuperParent, true);
        }
    }

    /**
     * Get the super parent of current object.
     */
    public JGenericObject getSuperParent() {
        return (superParent);
    }

    /**
     * Uniform scale, not include children. Not necessarily implemented in all
     * subclasses. Does nothing at the cGenericObject level. Subclasses should
     * scale themselves, then call the superclass method.
     *
     * @param aScaleFactor
     * @param aIncludeChildren
     */
    public void scale(final double aScaleFactor) {
        scale(aScaleFactor, true);
    }

    //-----------------------------------------------------------------------
    // METHODS - SCALING:
    //-----------------------------------------------------------------------
    /**
     * Uniform scale, optionally include children. Not necessarily implemented
     * in all subclasses. Does nothing at the cGenericObject level. Subclasses
     * should scale themselves, then call the superclass method.
     *
     * @param aScaleFactor
     * @param aIncludeChildren
     */
    public void scale(final double aScaleFactor, final boolean aIncludeChildren) {
        scale(new JVector3d(aScaleFactor, aScaleFactor, aScaleFactor), aIncludeChildren);
    }

    /**
     * Non-uniform scale, not include children. Not necessarily implemented in
     * all subclasses. Does nothing at the cGenericObject level; subclasses
     * should scale themselves, then call the superclass method.
     *
     * @param aScaleFactors
     * @param aIncludeChildren
     */
    public void scale(final JVector3d aScaleFactors) {
        scale(aScaleFactors, true);
    }

    /**
     * Non-uniform scale, optionally include children. Not necessarily
     * implemented in all subclasses. Does nothing at the cGenericObject level;
     * subclasses should scale themselves, then call the superclass method.
     *
     * @param aScaleFactors
     * @param aIncludeChildren
     */
    public void scale(final JVector3d aScaleFactors, final boolean aIncludeChildren) {

        // scale current object
        scaleObject(aScaleFactors);

        // scale children
        if (!aIncludeChildren) {
            return;
        }
        for (int i = 0; i < childrens.size(); i++) {
            JGenericObject nextObject = childrens.get(i);

            // scale the position of this child
            nextObject.scale(aScaleFactors, aIncludeChildren);
        }

    }

    /**
     * Set the tag for this object but not for childrens.
     *
     * @param aTag
     */
    public void setTag(final int aTag) {
        setTag(aTag, false);
    }

    /**
     * Set the tag for this object and - optionally - for my children.
     *
     * @param aTag
     * @param aAffectChildren
     */
    public void setTag(final int aTag, final boolean aAffectChildren) {
        setTag(aTag);

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setTag(aTag, true);
            }
        }
    }

    public void setUserData(Object aData) {
        setUserData(aData, false);
    }

    /**
     * Set the m_userData pointer for this object and - optionally - for my
     * children.
     */
    public void setUserData(Object aData, final boolean aAffectChildren) {

        setUserData(aData);

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setUserData(aData, true);
            }
        }
    }

    /**
     * Set the name for this object but not for my children.
     *
     * @param aName
     */
    public void setObjectName(String aName) {
        setObjectName(aName, false);
    }

    /**
     * Set the name for this object and - optionally - for my children.
     *
     * @param aName
     * @param aAffectChildren
     */
    public void setObjectName(String aName, final boolean aAffectChildren) {

        objectName = aName;

        // update children
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                childrens.get(i).setObjectName(aName, true);
            }
        }
    }

    //-----------------------------------------------------------------------
    // GENERAL VIRTUAL METHODS::
    //-----------------------------------------------------------------------
    /**
     * Render this object in OpenGL2.
     */
    public void render(final JChaiRenderMode aRenderMode) {
    }

    /**
     * Update the m_globalPos and m_globalRot properties of any members of this
     * object (e.g. all triangles).
     */
    public void updateGlobalPositions(boolean aFrameOnly) {
    }

    /**
     * Update the bounding box of this object, based on object-specific data
     * (e.g. triangle positions).
     */
    public void updateBoundaryBox() {
    }

    /**
     * Scale current object with scale factors along x, y and z.
     */
    public void scaleObject(final JVector3d aScaleFactors) {
    }

    /**
     * From the position of the tool, search for the nearest point located at
     * the surface of the current object. Decide if the point is located inside
     * or outside of the object.
     *
     * @param aToolPos
     * @param aToolVel
     * @param a_IDN
     */
    public void computeLocalInteraction(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int a_IDN) {
        // no surface limits defined, so we simply return the same position of the tool
        interactionProjectedPoint.copyFrom(aToolPos);

        // no surface limits, so we consider that we are inside the object
        interactionInside = true;
    }

    /**
     * computes any other interaction with object.
     */
    public JVector3d computeOtherInteractions(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int a_IDN,
            JInteractionRecorder aInteractions,
            JInteractionSettings aInteractionSettings) {
        return new JVector3d(0, 0, 0);
    }

    /**
     * Compute any collisions other than the default collision detector.
     */
    public boolean computeOtherCollisionDetection(JVector3d a_segmentPointA,
            JVector3d a_segmentPointB,
            JCollisionRecorder a_recorder,
            JCollisionSettings a_settings) {
        return (false);
    }

    /**
     * @return the mLevel
     */
    public float getTransparencyLevel() {
        return transparencyLevel;
    }

    /**
     * @return the infoWord
     */
    public String getInfo() {

        info = new String();

        info += "FrameSize: " + getFrameSize();
        info += "\n";

        info += "Nº Childs: " + getNumChildren();
        info += "\n";

        info += "Interface Haptica: " + isHapticEnabled();
        info += "\n";

        info += "Wire Mode: " + isWireMode();
        info += "\n";

        info += "Vertex Color: " + isVertexColorsEnabled();
        info += "\n";

        info += "Material: " + isMaterialEnabled();
        info += "\n";

        info += "Texture: " + isTextureMappingEnabled();
        info += "\n";

        info += "Transparency: " + isTransparencyEnabled();
        info += "\n";

        info += "Multi Transparency: " + isMultipassTransparencyEnabled();
        info += "\n";

        info += "Transparency Level: " + getTransparencyLevel();
        info += "\n";

        info += "Culling: " + isCullingEnabled();
        info += "\n";

        info += "ShowTree: " + isTreeVisible();
        info += "\n";

        info += "CollisionTree: " + isCollisionTreeVisible();
        info += "\n";

        info += "ShowEnabled: " + isVisible();
        info += "\n";

        info += "ShowFrame: " + isFrameVisible();
        info += "\n";

        info += "ShowBox: " + isBoundaryBoxVisible();
        info += "\n";

        info += "ShowCollisionTree: " + isCollisionTreeVisible();
        info += "\n";

        info += "World POS X: " + getPosition().getX() + "Y: " + getPosition().getY() + "Z: " + getPosition().getZ();
        info += "\n";

        info += "World GlobalPos X: " + getGlobalPosition().getX() + "Y: " + getGlobalPosition().getY() + "Z: " + getGlobalPosition().getZ();
        info += "\n";

        return info;
    }

    /**
     * @return the interactionProjectedPoint
     */
    public JVector3d getInteractionProjectedPoint() {
        return interactionProjectedPoint;
    }

    /**
     * @return the interactionInside
     */
    public boolean isInteractionInside() {
        return interactionInside;
    }

    /**
     * @return the effects
     */
    public ArrayList<JGenericEffect> getEffects() {
        return effects;
    }

    /**
     * @return the material
     */
    public JMaterial getMaterial() {
        return material;
    }

    /**
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @return the userData
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * @return the previousLocalPosition
     */
    public JVector3d getPreviousLocalPosition() {
        return previousLocalPosition;
    }

    /**
     * @return the previousLocalRotation
     */
    public JMatrix3d getPreviousLocalRotation() {
        return previousLocalRotation;
    }

    /**
     * @return the previousGlobalPosition
     */
    public JVector3d getPreviousGlobalPosition() {
        return previousGlobalPosition;
    }

    /**
     * @return the previousGlobalRotation
     */
    public JMatrix3d getPreviousGlobalRotation() {
        return previousGlobalRotation;
    }

    /**
     * @return the frameThicknessScale
     */
    public double getFrameThicknessScale() {
        return frameThicknessScale;
    }

    /**
     * @return the boxVisible
     */
    public boolean isBoxVisible() {
        return boxVisible;
    }

    /**
     * @return the boundaryBoxColor
     */
    public JColorf getBoundaryBoxColor() {
        return boundaryBoxColor;
    }

    /**
     * @return the frameGL
     */
    public JMatrixGL getFrameGL() {
        return frameGL;
    }
}
