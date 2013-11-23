/**
 * This file is part of the JCHAI 3D visualization and haptics libraries.
 * Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License("GPL") version 2 as published by
 * the Free Software Foundation.
 *
 * For using the JCHAI 3D libraries with software that can not be combined with
 * the GNU GPL, and for taking advantage of the additional benefits of our
 * support services, please contact CHAI 3D about acquiring a Professional
 * Edition License.
 *
 * project <https://sourceforge.net/projects/jchai3d>
 */
package org.jchai3d.forces;

import org.jchai3d.collisions.JCollisionEvent;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JGenericObject;
import org.jchai3d.scenegraph.JMesh;
import org.jchai3d.scenegraph.JWorld;

/**
 * Implements the finger-proxy algorithm for computing interaction forces
 * between a point force device and meshes.
 *
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (java implementation)
 */
public class JProxyPointForceAlgo extends JGenericPointForceAlgo {

    /**
     * Global position of the proxy.
     */
    JVector3d proxyGlobalPosition;
    /**
     * Global position of device.
     */
    protected JVector3d deviceGlobalPosition;
    /**
     * Last computed force (in global coordinate frame).
     */
    protected JVector3d lastGlobalForce;
    /**
     * Next best position for the proxy (in global coordinate frame).
     */
    protected JVector3d nextBestProxyGlobalPosition;
    /**
     * Are we currently in a "slip friction" state?
     */
    protected boolean slipping;
    /**
     * Normal force.
     */
    protected JVector3d normalForce;
    /**
     * Tangential force.
     */
    protected JVector3d tangentialForce;
    /**
     * Number of contacts between proxy and triangles (0, 1, 2 or 3).
     */
    protected int contactCount;
    /**
     * Radius of the proxy.
     */
    double proxyRadius;
    /**
     * Use any friction algorithm?
     */
    protected boolean frictionEnabled;
    /**
     * Use the dynamic proxy algorithm to deal with mobile objects?
     */
    protected boolean dynamicProxyEnabled;
    /**
     * Use force shading.
     */
    protected boolean forceShadingEnabled;
    
    /**
     * Dynamic friction hysteresis multiplier In CHAI's proxy, the angle
     * computed from the coefficient is multiplied by this constant to avoid
     * rapidly oscillating between slipping and sticking without having to turn
     * the dynamic friction level way down.
     */
    protected double frictionDynHysteresisMultiplier;
    /*
     * Maximum force shading angle (radians) threshold between normals of
     * triangle.
     */
    protected double forceShadingAngleThreshold;
    /*
     * Collision cettings
     */
    protected JCollisionSettings collisionSettings;
    /**
     * Collision detection recorder for searching first constraint.
     */
    protected JCollisionRecorder collisionRecorderConstraint0;
    /**
     * Collision detection recorder for searching second constraint.
     */
    protected JCollisionRecorder collisionRecorderConstraint1;
    /**
     * Collision detection recorder for searching third constraint.
     */
    protected JCollisionRecorder collisionRecorderConstraint2;

    /*
     * To address numerical errors during geometric computation, several epsilon
     * values are computed and used.
     */
    /**
     * epsilon value - used for handling numerical limits.
     */
    protected double epsilonInitialValue;
    /**
     * epsilon value - used for handling numerical limits.
     */
    protected double epsilon;
    /**
     * epsilon value - used for handling numerical limits.
     */
    protected double epsilonCollisionDetection;
    /**
     * epsilon value - used for handling numerical limits.
     */
    double epsilonBaseValue;
    /**
     * epsilon value - used for handling numerical limits.
     */
    protected double epsilonMinimalValue;
    /**
     * Value of state machine.
     */
    protected int algoCount;

    /**
     * Constructor of cProxyPointForceAlgo.
     */
    public JProxyPointForceAlgo() {
        collisionSettings = new JCollisionSettings();
        collisionRecorderConstraint0 = new JCollisionRecorder();
        collisionRecorderConstraint1 = new JCollisionRecorder();
        collisionRecorderConstraint2 = new JCollisionRecorder();

        proxyGlobalPosition = new JVector3d();
        deviceGlobalPosition = new JVector3d();
        lastGlobalForce = new JVector3d();
        nextBestProxyGlobalPosition = new JVector3d();
        normalForce = new JVector3d();
        tangentialForce = new JVector3d();

        // initialize world pointer
        parentWorld = null;

        // no contacts yet between proxy and environment
        contactCount = 0;

        // set epsilon base value
        setEpsilonBaseValue(0.00001);

        epsilonBaseValue = 0.00001;
        epsilonMinimalValue = 0.01 * epsilonBaseValue;
        epsilon = epsilonBaseValue;
        epsilonCollisionDetection = 1.0 * epsilon;

        // force model settings
        frictionDynHysteresisMultiplier = 0.6;
        forceShadingAngleThreshold = 0.75;

        // initialize device and proxy positions
        deviceGlobalPosition.zero();
        proxyGlobalPosition.zero();
        lastGlobalForce.zero();

        // this will generally be over-written by the calling pointer
        proxyRadius = 0.01;

        // by default, we do not use dynamic proxy (which handles moving objects)
        dynamicProxyEnabled = true;

        // initialize dynamic proxy members
        collisionRecorderConstraint0.getNearestCollision().clear();
        collisionRecorderConstraint1.getNearestCollision().clear();
        collisionRecorderConstraint2.getNearestCollision().clear();

        // friction properties
        slipping = true;
        frictionEnabled = true;

        // use force shading
        forceShadingEnabled = false;

        // setup collision detector seetings
        collisionSettings.setCheckForNearestCollisionOnly(true);
        collisionSettings.setReturnMinimalCollisionData(false);
        collisionSettings.setCheckVisibleObjectsOnly(true);
        collisionSettings.setCheckHapticObjectsOnly(true);
        collisionSettings.setCheckBothSidesOfTriangles(true);
        collisionSettings.setAdjustObjectMotion(dynamicProxyEnabled);

        // initialize algorithm variables
        algoCount = 0;
    }

    /**
     * Initialize the algorithm.
     */
    @Override
    public void initialize(JWorld aWorld, JVector3d aInitialGlobalPosition) {

        // reset some variables
        lastGlobalForce.zero();

        // no contacts yet between proxy and environment
        contactCount = 0;

        // the proxy can slip along surfaces
        slipping = true;

        // initialize device and proxy positions
        deviceGlobalPosition.copyFrom(aInitialGlobalPosition);
        proxyGlobalPosition.copyFrom(aInitialGlobalPosition);

        // set pointer to world in which force algorithm operates
        parentWorld = aWorld;
    }

    /**
     * Reset the algorithm. Set proxy position to device position.
     */
    public void reset() {

        // reset force
        lastGlobalForce.zero();

        // set proxy position to be equal to the device position
        proxyGlobalPosition.copyFrom(deviceGlobalPosition);

        // no contacts yet between proxy and environment
        contactCount = 0;
    }

    /**
     * Calculate interaction forces between device and meshes.
     */
    @Override
    public JVector3d computeForces(final JVector3d aToolPos, final JVector3d aToolVel) {
        
        // for each time we compute the forces applied in the haptic interface
        // we must clear all constraints because they can hold unwanted values
        clearCollisionRecorderConstraints();
        
        // update device position
        deviceGlobalPosition.copyFrom(aToolPos);

        // check if world has been defined; if so, compute forces
        if (parentWorld != null) {
            // compute next best position of proxy
            computeNextBestProxyPosition(deviceGlobalPosition);

            // update proxy to next best position
            proxyGlobalPosition.copyFrom(nextBestProxyGlobalPosition);

            // compute force vector applied to device
            updateForce();

            // return result
            return lastGlobalForce;
        } // if no world has been defined in which algorithm operates, there is no force
        else {
            return new JVector3d(0.0, 0.0, 0.0);
        }
    }

    /**
     * Set radius of proxy.
     */
    public void setProxyRadius(double aRadius) {
        proxyRadius = aRadius;
        collisionSettings.setCollisionRadius(proxyRadius);
    }

    public static JVector3d d = new JVector3d(),p = new JVector3d();
    /**
     * Implementation of the proxy algorithm - constraint 0.
     */
    boolean computeNextProxyPositionWithContraints0(final JVector3d aGoalGlobalPos) {

        // We define the goal position of the proxy.
        JVector3d goalGlobalPos = new JVector3d(aGoalGlobalPos);

        // To address numerical errors of the computer, we make sure to keep the proxy
        // slightly above any triangle and not directly on it. If we are using a radius of
        // zero, we need to define a default small value for epsilon
        epsilonInitialValue = JMaths.jAbs(0.0001 * proxyRadius);
        if (epsilonInitialValue < epsilonBaseValue) {
            epsilonInitialValue = epsilonBaseValue;
        }

        // The epsilon value is dynamic (can be reduced). We set it to its initial
        // value if the proxy is not touching any triangle.
        if (contactCount == 0) {
            epsilon = epsilonInitialValue;
            slipping = true;
        }

        // If the distance between the proxy and the goal position (device) is
        // very small then we can be considered done.
        if (!dynamicProxyEnabled) {
            if (goalAchieved(proxyGlobalPosition, goalGlobalPos)) {
                nextBestProxyGlobalPosition.copyFrom(proxyGlobalPosition);
                algoCount = 0;
                return (false);
            }
        }

        // compute the normalized form of the vector going from the
        // current proxy position to the desired goal position

        // compute the distance between the proxy and the goal positions
        double distanceProxyGoal = JMaths.jDistance(proxyGlobalPosition, goalGlobalPos);

        // A vector from the proxy to the goal
        JVector3d vProxyToGoal = new JVector3d();
        JVector3d vProxyToGoalNormalized = new JVector3d();
        boolean proxyAndDeviceEqual;

        if (distanceProxyGoal > epsilon) {
            goalGlobalPos.subr(proxyGlobalPosition, vProxyToGoal);
            vProxyToGoal.normalizer(vProxyToGoalNormalized);
            proxyAndDeviceEqual = false;
        } else {
            vProxyToGoal.zero();
            vProxyToGoalNormalized.zero();
            proxyAndDeviceEqual = true;
        }

        // Test whether the path from the proxy to the goal is obstructed.
        // For this we create a segment that goes from the proxy position to
        // the goal position plus a little extra to take into account the
        // physical radius of the proxy.
        JVector3d targetPos;
        if (dynamicProxyEnabled) {
            targetPos = new JVector3d(goalGlobalPos);
        } else {
            targetPos = JMaths.jAdd(goalGlobalPos, JMaths.jMul(epsilonCollisionDetection, vProxyToGoalNormalized));
        }

        // setup collision detector
        collisionSettings.setCollisionRadius(proxyRadius);

        // Search for a collision between the first segment (proxy-device)
        // and the environment.
        collisionSettings.setAdjustObjectMotion(dynamicProxyEnabled);
        collisionRecorderConstraint0.clear();
        p.copyFrom(proxyGlobalPosition);
        d.copyFrom(targetPos);
        boolean hit = parentWorld.computeCollisionDetection(proxyGlobalPosition,
                targetPos,
                collisionRecorderConstraint0,
                collisionSettings);

        // check if collision occurred between proxy and goal positions.
        double collisionDistance = 0.0;
        if (hit) {
            collisionDistance = Math.sqrt(collisionRecorderConstraint0.getNearestCollision().getSquareDistance());
            if (dynamicProxyEnabled) {
                // retrieve new position of proxy
                JVector3d posLocal = new JVector3d(collisionRecorderConstraint0.getNearestCollision().getAdjustedSegmentAPoint());
                JGenericObject obj = collisionRecorderConstraint0.getNearestCollision().getObject();
                JVector3d posGlobal = JMaths.jAdd(obj.getGlobalPosition(), JMaths.jMul(obj.getGlobalRotation(), posLocal));
                proxyGlobalPosition.copyFrom(posGlobal);

                distanceProxyGoal = JMaths.jDistance(proxyGlobalPosition, goalGlobalPos);
                goalGlobalPos.subr(proxyGlobalPosition, vProxyToGoal);
                vProxyToGoal.normalizer(vProxyToGoalNormalized);
            }

            if (collisionDistance > (double) (distanceProxyGoal + (JConstants.CHAI_SMALL))) {
                hit = false;
            }


            if (hit) {
                // a collision has occurred and we check if the distance from the
                // proxy to the collision is smaller than epsilon. If yes, then
                // we reduce the epsilon term in order to avoid possible "pop through"
                // effect if we suddenly push the proxy "up" again.
                if (collisionDistance < epsilon) {
                    epsilon = collisionDistance;
                    if (epsilon < epsilonMinimalValue) {
                        epsilon = epsilonMinimalValue;
                    }
                }
            }
        }

        // If no collision occurs, then we move the proxy to its goal, and we're done
        if (!hit) {
            contactCount = 0;
            algoCount = 0;
            slipping = true;
            nextBestProxyGlobalPosition.copyFrom(goalGlobalPos);
            return (false);
        }

        // a first collision has occurred
        algoCount = 1;

        //-----------------------------------------------------------------------
        // FIRST COLLISION OCCURES:
        //-----------------------------------------------------------------------

        // We want the center of the proxy to move as far toward the triangle as it can,
        // but we want it to stop when the Sphere_ representing the proxy hits the
        // triangle.  We want to compute how far the proxy center will have to
        // be pushed Away_ from the collision point - along the vector from the proxy
        // to the goal - to keep a distance mRadius between the proxy center and the
        // triangle.
        //
        // So we compute the cosine of the angle between the normal and proxy-goal vector...
        double cosAngle = vProxyToGoalNormalized.dot(collisionRecorderConstraint0.getNearestCollision().getGlobalNormal());


        // Now we compute how far away from the collision point - _backwards_
        // along vProxyGoal - we have to put the proxy to keep it from penetrating
        // the triangle.
        //
        // If only ASCII art were a little more expressive...
        double distanceTriangleProxy = epsilon / JMaths.jAbs(cosAngle);
        if (distanceTriangleProxy > collisionDistance) {
            distanceTriangleProxy = JMaths.jMax(collisionDistance, epsilon);
        }

        // We compute the projection of the vector between the proxy and the collision
        // point onto the normal of the triangle.  This is the direction in which
        // we'll move the Goal_ to "push it away" from the triangle (to account for
        // the radius of the proxy).

        JCollisionEvent contactPoint0 = getContactPoint0();


        // A vector from the most recent collision point to the proxy
        JVector3d vCollisionToProxy = new JVector3d();
        proxyGlobalPosition.subr(contactPoint0.getGlobalPosition(), vCollisionToProxy);

        // Move the proxy to the collision point, minus the distance along the
        // movement vector that we computed above.
        //
        // Note that we're adjusting the 'proxy' variable, which is just a local
        // copy of the proxy position.  We still might decide not to move the
        // 'real' proxy due to friction.
        JVector3d vColNextGoal = new JVector3d();
        vProxyToGoalNormalized.mulr(-distanceTriangleProxy, vColNextGoal);
        JVector3d nextProxyPos = new JVector3d();
        contactPoint0.getGlobalPosition().addr(vColNextGoal, nextProxyPos);

        // we can now set the next position of the proxy
        nextBestProxyGlobalPosition.copyFrom(nextProxyPos);

        // If the distance between the proxy and the goal position (device) is
        // very small then we can be considered done.
        if (goalAchieved(goalGlobalPos, nextProxyPos)) {
            contactCount = 1;
            algoCount = 0;
            return (true);
        }

        return (true);
    }

    /**
     * Implementation of the proxy algorithm - constraint 1.
     */
    boolean computeNextProxyPositionWithContraints1(final JVector3d aGoalGlobalPos) {

        // The proxy is now constrained on a plane; we now calculate the nearest
        // point to the original goal (device position) on this plane; this point
        // is computed by projecting the ideal goal onto the plane defined by the
        // intersected triangle
        JVector3d goalGlobalPos = JMaths.jProjectPointOnPlane(aGoalGlobalPos,
                proxyGlobalPosition,
                collisionRecorderConstraint0.getNearestCollision().getGlobalNormal());

        // A vector from the proxy to the goal
        JVector3d vProxyToGoal = new JVector3d();
        goalGlobalPos.subr(proxyGlobalPosition, vProxyToGoal);

        // If the distance between the proxy and the goal position (device) is
        // very small then we can be considered done.
        if (goalAchieved(proxyGlobalPosition, goalGlobalPos)) {
            nextBestProxyGlobalPosition.copyFrom(proxyGlobalPosition);
            algoCount = 0;
            contactCount = 1;
            return (false);
        }

        // compute the normalized form of the vector going from the
        // current proxy position to the desired goal position
        JVector3d vProxyToGoalNormalized = new JVector3d();
        vProxyToGoal.normalizer(vProxyToGoalNormalized);

        // Test whether the path from the proxy to the goal is obstructed.
        // For this we create a segment that goes from the proxy position to
        // the goal position plus a little extra to take into account the
        // physical radius of the proxy.
        JVector3d targetPos = JMaths.jAdd(goalGlobalPos, JMaths.jMul(epsilonCollisionDetection, vProxyToGoalNormalized));

        // setup collision detector
        collisionSettings.setCollisionRadius(proxyRadius);

        // search for collision
        collisionSettings.setAdjustObjectMotion(false);
        collisionRecorderConstraint1.clear();
        boolean hit = parentWorld.computeCollisionDetection(proxyGlobalPosition,
                targetPos,
                collisionRecorderConstraint1,
                collisionSettings);

        // check if collision occurred between proxy and goal positions.
        double collisionDistance = 0.0;
        if (hit) {
            collisionDistance = Math.sqrt(collisionRecorderConstraint1.getNearestCollision().getSquareDistance());
            if (collisionDistance > (JMaths.jDistance(proxyGlobalPosition, goalGlobalPos) + JConstants.CHAI_SMALL)) {
                hit = false;
            } else {
                // a collision has occurred and we check if the distance from the
                // proxy to the collision is smaller than epsilon. If yes, then
                // we reduce the epsilon term in order to avoid possible "pop through"
                // effect if we suddenly push the proxy "up" again.
                if (collisionDistance < epsilon) {
                    epsilon = collisionDistance;
                    if (epsilon < epsilonMinimalValue) {
                        epsilon = epsilonMinimalValue;
                    }
                }
            }
        }

        // If no collision occurs, we move the proxy to its goal, unless
        // friction prevents us from doing so.
        if (!hit) {
            testFrictionAndMoveProxy(goalGlobalPos,
                    proxyGlobalPosition,
                    collisionRecorderConstraint0.getNearestCollision().getGlobalNormal(),
                    collisionRecorderConstraint0.getNearestCollision().getObject());

            contactCount = 1;
            algoCount = 0;

            return (false);
        }

        // a second collision has occurred
        algoCount = 2;

        //-----------------------------------------------------------------------
        // SECOND COLLISION OCCURES:
        //-----------------------------------------------------------------------
        // We want the center of the proxy to move as far toward the triangle as it can,
        // but we want it to stop when the Sphere_ representing the proxy hits the
        // triangle.  We want to compute how far the proxy center will have to
        // be pushed Away_ from the collision point - along the vector from the proxy
        // to the goal - to keep a distance mRadius between the proxy center and the
        // triangle.
        //
        // So we compute the cosine of the angle between the normal and proxy-goal vector...
        double cosAngle = vProxyToGoalNormalized.dot(collisionRecorderConstraint1.getNearestCollision().getGlobalNormal());

        // Now we compute how far away from the collision point - _backwards_
        // along vProxyGoal - we have to put the proxy to keep it from penetrating
        // the triangle.
        //
        // If only ASCII art were a little more expressive...
        double distanceTriangleProxy = epsilon / JMaths.jAbs(cosAngle);
        if (distanceTriangleProxy > collisionDistance) {
            distanceTriangleProxy = JMaths.jMax(collisionDistance, epsilon);
        }

        // We compute the projection of the vector between the proxy and the collision
        // point onto the normal of the triangle.  This is the direction in which
        // we'll move the Goal_ to "push it away" from the triangle (to account for
        // the radius of the proxy).

        JCollisionEvent contactPoint1 = getContactPoint1();
        // A vector from the most recent collision point to the proxy
        JVector3d vCollisionToProxy = new JVector3d();
        proxyGlobalPosition.subr(contactPoint1.getGlobalPosition(), vCollisionToProxy);

        // Move the proxy to the collision point, minus the distance along the
        // movement vector that we computed above.
        //
        // Note that we're adjusting the 'proxy' variable, which is just a local
        // copy of the proxy position.  We still might decide not to move the
        // 'real' proxy due to friction.
        JVector3d vColNextGoal = new JVector3d();
        vProxyToGoalNormalized.mulr(-distanceTriangleProxy, vColNextGoal);
        JVector3d nextProxyPos = new JVector3d();
        contactPoint1.getGlobalPosition().addr(vColNextGoal, nextProxyPos);

        // we can now set the next position of the proxy
        nextBestProxyGlobalPosition.copyFrom(nextProxyPos);

        // If the distance between the proxy and the goal position (device) is
        // very small then we can be considered done.
        if (goalAchieved(goalGlobalPos, nextProxyPos)) {
            contactCount = 2;
            algoCount = 0;
            return (true);
        }

        return (true);
    }

    /**
     * Implementation of the proxy algorithm - constraint 2.
     */
    boolean computeNextProxyPositionWithContraints2(JVector3d aGoalGlobalPos) {

        // The proxy is now constrained by two triangles and can only move along
        // a virtual line; we now calculate the nearest point to the original
        // goal (device position) along this line by projecting the ideal
        // goal onto the line.
        //
        // The line is expressed by the cross product of both surface normals,
        // which have both been oriented to point away from the device
        JVector3d line = new JVector3d();
        collisionRecorderConstraint0.getNearestCollision().getGlobalNormal().crossr(collisionRecorderConstraint1.getNearestCollision().getGlobalNormal(), line);

        // check result.
        if (line.equals(new JVector3d(0, 0, 0))) {
            nextBestProxyGlobalPosition.copyFrom(proxyGlobalPosition);
            algoCount = 0;
            contactCount = 2;
            return (false);
        }

        line.normalize();

        // Compute the projection of the device position (goal) onto the line; this
        // gives us the new goal position.
        JVector3d goalGlobalPos = JMaths.jProjectPointOnLine(aGoalGlobalPos, proxyGlobalPosition, line);

        // A vector from the proxy to the goal
        JVector3d vProxyToGoal = new JVector3d();
        goalGlobalPos.subr(proxyGlobalPosition, vProxyToGoal);

        // If the distance between the proxy and the goal position (device) is
        // very small then we can be considered done.
        if (goalAchieved(proxyGlobalPosition, goalGlobalPos)) {
            nextBestProxyGlobalPosition.copyFrom(proxyGlobalPosition);
            algoCount = 0;
            contactCount = 2;
            return (false);
        }

        // compute the normalized form of the vector going from the
        // current proxy position to the desired goal position
        JVector3d vProxyToGoalNormalized = new JVector3d();
        vProxyToGoal.normalizer(vProxyToGoalNormalized);

        // Test whether the path from the proxy to the goal is obstructed.
        // For this we create a segment that goes from the proxy position to
        // the goal position plus a little extra to take into account the
        // physical radius of the proxy.
        JVector3d targetPos = JMaths.jAdd(goalGlobalPos, JMaths.jMul(epsilonCollisionDetection, vProxyToGoalNormalized));

        // setup collision detector
        collisionSettings.setCollisionRadius(proxyRadius);

        // search for collision
        collisionSettings.setAdjustObjectMotion(false);
        collisionRecorderConstraint2.clear();
        boolean hit = parentWorld.computeCollisionDetection(proxyGlobalPosition,
                targetPos,
                collisionRecorderConstraint2,
                collisionSettings);

        // check if collision occurred between proxy and goal positions.
        double collisionDistance = 0;
        if (hit) {
            collisionDistance = Math.sqrt(collisionRecorderConstraint2.getNearestCollision().getSquareDistance());
            if (collisionDistance > (JMaths.jDistance(proxyGlobalPosition, goalGlobalPos) + JConstants.CHAI_SMALL)) {
                hit = false;
            } else {
                // a collision has occurred and we check if the distance from the
                // proxy to the collision is smaller than epsilon. If yes, then
                // we reduce the epsilon term in order to avoid possible "pop through"
                // effect if we suddenly push the proxy "up" again.
                if (collisionDistance < epsilon) {
                    epsilon = collisionDistance;
                    if (epsilon < epsilonMinimalValue) {
                        epsilon = epsilonMinimalValue;
                    }
                }
            }
        }

        // If no collision occurs, we move the proxy to its goal, unless
        // friction prevents us from doing so
        if (!hit) {
            JVector3d normal = JMaths.jMul(0.5, JMaths.jAdd(collisionRecorderConstraint0.getNearestCollision().getGlobalNormal(),
                    collisionRecorderConstraint1.getNearestCollision().getGlobalNormal()));

            testFrictionAndMoveProxy(goalGlobalPos,
                    proxyGlobalPosition,
                    normal,
                    collisionRecorderConstraint1.getNearestCollision().getTriangle().getParent());
            contactCount = 2;
            algoCount = 0;

            return (false);
        }

        //-----------------------------------------------------------------------
        // THIRD COLLISION OCCURES:
        //-----------------------------------------------------------------------
        // We want the center of the proxy to move as far toward the triangle as it can,
        // but we want it to stop when the Sphere_ representing the proxy hits the
        // triangle.  We want to compute how far the proxy center will have to
        // be pushed Away_ from the collision point - along the vector from the proxy
        // to the goal - to keep a distance mRadius between the proxy center and the
        // triangle.
        //
        // So we compute the cosine of the angle between the normal and proxy-goal vector...
        double cosAngle = vProxyToGoalNormalized.dot(collisionRecorderConstraint2.getNearestCollision().getGlobalNormal());

        // Now we compute how far away from the collision point - _backwards_
        // along vProxyGoal - we have to put the proxy to keep it from penetrating
        // the triangle.
        //
        // If only ASCII art were a little more expressive...
        double distanceTriangleProxy = epsilon / JMaths.jAbs(cosAngle);
        if (distanceTriangleProxy > collisionDistance) {
            distanceTriangleProxy = JMaths.jMax(collisionDistance, epsilon);
        }

        // We compute the projection of the vector between the proxy and the collision
        // point onto the normal of the triangle.  This is the direction in which
        // we'll move the Goal_ to "push it away" from the triangle (to account for
        // the radius of the proxy).

        JCollisionEvent contactPoint2 = getContactPoint2();
        // A vector from the most recent collision point to the proxy
        JVector3d vCollisionToProxy = new JVector3d();
        proxyGlobalPosition.subr(contactPoint2.getGlobalPosition(), vCollisionToProxy);

        // Move the proxy to the collision point, minus the distance along the
        // movement vector that we computed above.
        //
        // Note that we're adjusting the 'proxy' variable, which is just a local
        // copy of the proxy position.  We still might decide not to move the
        // 'real' proxy due to friction.
        JVector3d vColNextGoal = new JVector3d();
        vProxyToGoalNormalized.mulr(-distanceTriangleProxy, vColNextGoal);
        JVector3d nextProxyPos = new JVector3d();
        contactPoint2.getGlobalPosition().addr(vColNextGoal, nextProxyPos);

        // we can now set the next position of the proxy
        nextBestProxyGlobalPosition.copyFrom(nextProxyPos);
        algoCount = 0;
        contactCount = 3;

        // TODO: There actually should be a third friction test to see if we
        // can make it to our new goal position, but this is generally such a
        // small movement in one iteration that it's irrelevant...

        return (true);
    }

    private void clearCollisionRecorderConstraints() {
        collisionRecorderConstraint0.clear();
        collisionRecorderConstraint1.clear();
        collisionRecorderConstraint2.clear();
    }
    /**
     * Read radius of proxy.
     */
    public double getProxyRadius() {
        return (proxyRadius);
    }

    /**
     * Get last computed position of proxy in world coordinates.
     */
    public JVector3d getProxyGlobalPosition() {
        return (proxyGlobalPosition);
    }

    /**
     * Set position of proxy in world coordinates.
     */
    public void setProxyGlobalPosition(JVector3d aPosition) {
        proxyGlobalPosition = aPosition;
    }

    /**
     * Get last specified position of device in world coordinates.
     */
    public JVector3d getDeviceGlobalPosition() {
        return (deviceGlobalPosition);
    }

    /**
     * Get last computed force vector in world coordinates.
     */
    public JVector3d getForce() {
        return (lastGlobalForce);
    }

    /**
     * Return most recently calculated normal force.
     */
    public JVector3d getNormalForce() {
        return (normalForce);
    }

    /**
     * Return most recently calculated tangential force.
     */
    public JVector3d getTangentialForce() {
        return (tangentialForce);
    }

    //----------------------------------------------------------------------
    // METHODS - COLLISION INFORMATION BETWEEN PROXY AND WORLD
    //----------------------------------------------------------------------
    /**
     * Return the number of contacts (0, 1, 2 or 3):
     */
    int getNumContacts() {
        return (contactCount);
    }

    //----------------------------------------------------------------------
    // METHODS - RESOLUTION / ERRORS
    //----------------------------------------------------------------------
    /**
     * Set epsilon base value.
     */
    void setEpsilonBaseValue(double value) {
        epsilonBaseValue = value;
        epsilonMinimalValue = 0.01 * epsilonBaseValue;
        epsilon = epsilonBaseValue;
        epsilonCollisionDetection = 1.0 * epsilon;
    }

    /**
     * Read current epsilon value.
     */
    double getEpsilonBaseValue() {
        return (epsilonBaseValue);
    }

    /**
     * Test whether the proxy has reached the goal point.
     */
    public boolean goalAchieved(JVector3d aProxy, JVector3d aGoal) {

        if (dynamicProxyEnabled) {
            return (aProxy.distance(aGoal) < 0);
        } else {
            return (aProxy.distance(aGoal) < (epsilonBaseValue));
        }
    }

    /**
     * Compute the next goal position of the proxy.
     */
    public void computeNextBestProxyPosition(final JVector3d aGoal) {

        if (dynamicProxyEnabled) {
            boolean hit0, hit1, hit2;
            hit0 = computeNextProxyPositionWithContraints0(aGoal);
            proxyGlobalPosition.copyFrom(nextBestProxyGlobalPosition);

            if (!hit0) {
                return;
            }

            hit1 = computeNextProxyPositionWithContraints1(aGoal);
            proxyGlobalPosition.copyFrom(nextBestProxyGlobalPosition);

            if (!hit1) {
                return;
            }

            hit2 = computeNextProxyPositionWithContraints2(aGoal);
            proxyGlobalPosition.copyFrom(nextBestProxyGlobalPosition);

        } else {
            // In order to keep the finger-proxy algorithm running fast, we only
            // compute collision with one constraint at the time. The next time
            // the algorithm is called, it searches for the second or
            // third obstacle.

            switch (algoCount) {
                case 0:
                    computeNextProxyPositionWithContraints0(aGoal);
                    break;

                case 1:
                    computeNextProxyPositionWithContraints1(aGoal);
                    break;

                case 2:
                    computeNextProxyPositionWithContraints2(aGoal);
                    break;
            }
        }
    }

    /**
     * Attempt to move the proxy, subject to friction constraints.
     */
    public void testFrictionAndMoveProxy(final JVector3d aGoal, final JVector3d aProxy, JVector3d aNormal, JGenericObject aParent) {


        // check if friction is enabled
        if (frictionEnabled == false) {
            nextBestProxyGlobalPosition.copyFrom(aGoal);
            return;
        }

        // Compute penetration depth; how far is the device "behind" the
        // plane of the obstructing surface
        JVector3d projectedGoal = JMaths.jProjectPointOnPlane(deviceGlobalPosition, aProxy, aNormal);
        double penetrationDepth = JMaths.jSub(deviceGlobalPosition, projectedGoal).length();

        // Find the appropriate friction coefficient


        // Right now we can only work with cMesh's
        if (aParent == null || !(aParent instanceof JMesh)) {
            nextBestProxyGlobalPosition.copyFrom(aGoal);
            return;
        }

        // Our dynamic and static coefficients...
        JMesh parentMesh = (JMesh) aParent;

        double mud = parentMesh.getMaterial().getDynamicFriction();
        double mus = parentMesh.getMaterial().getStaticFriction();

        // No friction; don't try to compute friction cones
        if ((mud == 0) && (mus == 0)) {
            nextBestProxyGlobalPosition.copyFrom(aGoal);
            return;
        }

        // The corresponding friction cone radii
        double atmd = Math.atan(mud);
        double atms = Math.atan(mus);

        // Compute a vector from the device to the proxy, for computing
        // the angle of the friction cone
        JVector3d vDeviceProxy = JMaths.jSub(aProxy, deviceGlobalPosition);
        vDeviceProxy.normalize();

        // Now compute the angle of the friction cone...
        double theta = Math.acos(vDeviceProxy.dot(aNormal));

        // Manage the "slip-friction" state machine

        // If the dynamic friction radius is for some reason larger than the
        // static friction radius, always slip
        if (mud > mus) {
            slipping = true;
        } // If we're slipping...
        else if (slipping) {
            slipping = (theta > (atmd * frictionDynHysteresisMultiplier));
        } // If we're not slipping...
        else {
            slipping = theta > atms;
        }

        // The friction coefficient we're going to use...
        double mu = slipping ? mud : mus;

        // Calculate the friction radius as the absolute value of the penetration
        // depth times the coefficient of friction
        double frictionRadius = Math.abs(penetrationDepth * mu);

        // Calculate the distance between the proxy position and the current
        // goal position.
        double r = aProxy.distance(aGoal);

        // If this distance is smaller than CHAISMALL, we consider the proxy
        // to be at the same position as the goal, and we're done...
        if (r < JConstants.CHAI_SMALL) {
            nextBestProxyGlobalPosition.copyFrom(aProxy);
        } // If the proxy is outside the friction cone, update its position to
        // be on the perimeter of the friction cone... 
        else if (r > frictionRadius) {
            nextBestProxyGlobalPosition.copyFrom(JMaths.jAdd(aGoal, JMaths.jMul(frictionRadius / r, JMaths.jSub(aProxy, aGoal))));
        } // Otherwise, if the proxy is inside the friction cone, the proxy
        // should not be moved (set next best position to current position)
        else {
            nextBestProxyGlobalPosition.copyFrom(aProxy);
        }

        // We're done; record the fact that we're still touching an object...

    }

    /**
     * Compute force to apply to device.
     */
    public void updateForce() {

        // initialize variables
        double stiffness = 0.0;
        JVector3d normal = new JVector3d();
        normal.zero();

        JCollisionEvent contactPoint0 = getContactPoint0();
        JCollisionEvent contactPoint1 = getContactPoint1();
        JCollisionEvent contactPoint2 = getContactPoint2();

        // if there are no contacts between proxy and environment, no force is applied
        if (contactCount == 0) {
            lastGlobalForce.zero();
            return;
        } //---------------------------------------------------------------------
        // stiffness and surface normal estimation
        //---------------------------------------------------------------------
        else if (contactCount == 1) {
            // compute stiffness
            stiffness = (contactPoint0.getTriangle().getParent().getMaterial().getStiffness());

            // compute surface normal
            normal.add(contactPoint0.getGlobalNormal());
        } // if there are two contact points, the stiffness is the average of the
        // stiffnesses of the two intersected triangles' meshes
        else if (contactCount == 2) {
            // compute stiffness
            stiffness = (contactPoint0.getTriangle().getParent().getMaterial().getStiffness()
                    + contactPoint1.getTriangle().getParent().getMaterial().getStiffness()) / 2.0;

            // compute surface normal
            normal.add(contactPoint0.getGlobalNormal());
            normal.add(contactPoint1.getGlobalNormal());
            normal.mul(1.0 / 2.0);
        } // if there are three contact points, the stiffness is the average of the
        // stiffnesses of the three intersected triangles' meshes
        else if (contactCount == 3) {
            // compute stiffness
            stiffness = (contactPoint0.getTriangle().getParent().getMaterial().getStiffness()
                    + contactPoint1.getTriangle().getParent().getMaterial().getStiffness()
                    + contactPoint2.getTriangle().getParent().getMaterial().getStiffness()) / 3.0;

            // compute surface normal
            normal.add(contactPoint0.getGlobalNormal());
            normal.add(contactPoint1.getGlobalNormal());
            normal.add(contactPoint2.getGlobalNormal());
            normal.mul(1.0 / 3.0);
        }

        //---------------------------------------------------------------------
        // computing a force (Hooke's law)
        //---------------------------------------------------------------------

        // compute the force by modeling a spring between the proxy and the device
        JVector3d force = new JVector3d();
        proxyGlobalPosition.subr(deviceGlobalPosition, force);
        force.mul(stiffness);
        lastGlobalForce.copyFrom(force);

        // compute tangential and normal forces
        if ((force.lengthsq() > 0) && (contactCount > 0)) {
            normalForce.copyFrom(JMaths.jProject(force, normal));
            force.subr(normalForce, tangentialForce);
        } else {
            tangentialForce.zero();
            normalForce.copyFrom(force);
        }


        //---------------------------------------------------------------------
        // force shading (optional)
        //---------------------------------------------------------------------

        if ((forceShadingEnabled) && (contactCount == 1)) {
            // get vertices and normals related to contact triangle
            JVector3d vertex0 = JMaths.jAdd(contactPoint0.getObject().getGlobalPosition(), JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex0().getPosition()));
            JVector3d vertex1 = JMaths.jAdd(contactPoint0.getObject().getGlobalPosition(), JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex1().getPosition()));
            JVector3d vertex2 = JMaths.jAdd(contactPoint0.getObject().getGlobalPosition(), JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex2().getPosition()));
            JVector3d normal0 = JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex0().getNormal());
            JVector3d normal1 = JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex1().getNormal());
            JVector3d normal2 = JMaths.jMul(contactPoint0.getObject().getGlobalRotation(), contactPoint0.getTriangle().getVertex2().getNormal());

            // compute angles between normals. If the angles are very different, then do not apply shading.
            double angle01 = JMaths.jAngle(normal0, normal1);
            double angle02 = JMaths.jAngle(normal0, normal2);
            double angle12 = JMaths.jAngle(normal1, normal2);

            if ((angle01 < forceShadingAngleThreshold) || (angle02 < forceShadingAngleThreshold) || (angle12 < forceShadingAngleThreshold)) {
                double a0 = 0;
                double a1 = 0;
                JMaths.jProjectPointOnPlane(contactPoint0.getGlobalPosition(), vertex0, vertex1, vertex2, a0, a1);

                JVector3d normalShaded = JMaths.jAdd(
                        JMaths.jMul(0.5, JMaths.jAdd(JMaths.jMul(a0, normal1), JMaths.jMul((1 - a0), normal0))),
                        JMaths.jMul(0.5, JMaths.jAdd(JMaths.jMul(a1, normal2), JMaths.jMul((1 - a1), normal0))));
                normalShaded.normalize();

                if (JMaths.jAngle(normalShaded, normal) > 1.0) {
                    normalShaded.negate();
                }

                if (JMaths.jAngle(normal, normalShaded) < forceShadingAngleThreshold) {
                    double forceMagnitude = normalForce.length();
                    force = JMaths.jAdd(JMaths.jMul(forceMagnitude, normalShaded), tangentialForce);
                    lastGlobalForce.copyFrom(force);
                    normal = normalShaded;

                    // update tangential and normal forces again
                    if ((force.lengthsq() > 0) && (contactCount > 0)) {
                        normalForce.copyFrom(JMaths.jProject(force, normal));
                        force.subr(normalForce, tangentialForce);
                    } else {
                        tangentialForce.zero();
                        normalForce.copyFrom(force);
                    }
                }
            }
        }
    }

    public JCollisionSettings getCollisionSettings() {
        return collisionSettings;
    }

    /**
     * @param deviceGlobalPosition the deviceGlobalPosition to set
     */
    public void setDeviceGlobalPosition(JVector3d deviceGlobalPosition) {
        this.deviceGlobalPosition = deviceGlobalPosition;
    }

    /**
     * @return the lastGlobalForce
     */
    public JVector3d getLastGlobalForce() {
        return lastGlobalForce;
    }

    /**
     * @param lastGlobalForce the lastGlobalForce to set
     */
    public void setLastGlobalForce(JVector3d lastGlobalForce) {
        this.lastGlobalForce = lastGlobalForce;
    }

    /**
     * @return the nextBestProxyGlobalPosition
     */
    public JVector3d getNextBestProxyGlobalPosition() {
        return nextBestProxyGlobalPosition;
    }

    /**
     * @return the slipping
     */
    public boolean isSlipping() {
        return slipping;
    }

    /**
     * @return the contactCount
     */
    public int getContactCount() {
        return contactCount;
    }

    /**
     * @return the frictionEnabled
     */
    public boolean isFrictionEnabled() {
        return frictionEnabled;
    }

    /**
     * @param frictionEnabled the frictionEnabled to set
     */
    public void setFrictionEnabled(boolean frictionEnabled) {
        this.frictionEnabled = frictionEnabled;
    }

    /**
     * @return the dynamicProxyEnabled
     */
    public boolean isDynamicProxyEnabled() {
        return dynamicProxyEnabled;
    }

    /**
     * @return the forceShadingEnabled
     */
    public boolean isForceShadingEnabled() {
        return forceShadingEnabled;
    }

    /**
     * @return the contactPoint0
     */
    public JCollisionEvent getContactPoint0() {
        return collisionRecorderConstraint0.getNearestCollision();
    }

    /**
     * @return the contactPoint1
     */
    public JCollisionEvent getContactPoint1() {
        return collisionRecorderConstraint1.getNearestCollision();
    }

    /**
     * @return the contactPoint2
     */
    public JCollisionEvent getContactPoint2() {
        return collisionRecorderConstraint2.getNearestCollision();
    }

    /**
     * @return the frictionDynHysteresisMultiplier
     */
    public double getFrictionDynHysteresisMultiplier() {
        return frictionDynHysteresisMultiplier;
    }

    /**
     * @param frictionDynHysteresisMultiplier the
     * frictionDynHysteresisMultiplier to set
     */
    public void setFrictionDynHysteresisMultiplier(double frictionDynHysteresisMultiplier) {
        this.frictionDynHysteresisMultiplier = frictionDynHysteresisMultiplier;
    }

    /**
     * @return the forceShadingAngleThreshold
     */
    public double getForceShadingAngleThreshold() {
        return forceShadingAngleThreshold;
    }

    /**
     * @param forceShadingAngleThreshold the forceShadingAngleThreshold to set
     */
    public void setForceShadingAngleThreshold(double forceShadingAngleThreshold) {
        this.forceShadingAngleThreshold = forceShadingAngleThreshold;
    }
    /**
     * @return the collisionRecorderConstraint0
     */
    public JCollisionRecorder getCollisionRecorderConstraint0() {
        return collisionRecorderConstraint0;
    }

    /**
     * @return the collisionRecorderConstraint1
     */
    public JCollisionRecorder getCollisionRecorderConstraint1() {
        return collisionRecorderConstraint1;
    }

    /**
     * @return the collisionRecorderConstraint2
     */
    public JCollisionRecorder getCollisionRecorderConstraint2() {
        return collisionRecorderConstraint2;
    }

    /**
     * @return the epsilonInitialValue
     */
    public double getEpsilonInitialValue() {
        return epsilonInitialValue;
    }

    /**
     * @param epsilonInitialValue the epsilonInitialValue to set
     */
    public void setEpsilonInitialValue(double epsilonInitialValue) {
        this.epsilonInitialValue = epsilonInitialValue;
    }

    /**
     * @return the epsilon
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * @param epsilon the epsilon to set
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * @return the epsilonCollisionDetection
     */
    public double getEpsilonCollisionDetection() {
        return epsilonCollisionDetection;
    }

    /**
     * @param epsilonCollisionDetection the epsilonCollisionDetection to set
     */
    public void setEpsilonCollisionDetection(double epsilonCollisionDetection) {
        this.epsilonCollisionDetection = epsilonCollisionDetection;
    }

    /**
     * @return the epsilonMinimalValue
     */
    public double getEpsilonMinimalValue() {
        return epsilonMinimalValue;
    }

    /**
     * @param epsilonMinimalValue the epsilonMinimalValue to set
     */
    public void setEpsilonMinimalValue(double epsilonMinimalValue) {
        this.epsilonMinimalValue = epsilonMinimalValue;
    }

    /**
     * @return the algoCount
     */
    public int getAlgoCount() {
        return algoCount;
    }

    public void setDynamicProxyEnabled(boolean mUseDynamicProxy) {
        this.dynamicProxyEnabled = mUseDynamicProxy;
    }

    public void setForceShadingEnabled(boolean mUseForceShading) {
        this.forceShadingEnabled = mUseForceShading;
    }
}
