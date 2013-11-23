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

package org.jchai3d.math;

import org.jchai3d.extras.JGlobals;

/**
 *
 * @author jairo
 */
public class JMaths {

    /**
     * Check if \e value is equal or near zero.
     * @param a_value
     * @return
     */
    public static boolean jZero(final double a_value) {
        return ((a_value < JConstants.CHAI_TINY) && (a_value > -JConstants.CHAI_TINY));
    }

    /**
     * Check if value is strictly positive and less than \e maxBound in case
    maxBound is positive.
     * @param a_value
     * @param a_boundMax
     * @return
     */
    public static boolean jPositiveBound(final double a_value, final double a_boundMax) {
        return ((a_value > JConstants.CHAI_TINY) && ((a_boundMax < 0) || (a_value < a_boundMax)));
    }

    /**
     * Compute absolute value
     *
     * @param <T>
     * @param a_value
     * @return
     */
    public static <T extends Number> T jAbs(final T a_value) {
        return (T) new Double(Math.abs(a_value.doubleValue()));
    }

    /**
     * Compute maximum between two values
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @return
     */
    public static <T extends Number> T jMax(final T a_value1, final T a_value2) {
        return (T) new Double(a_value1.doubleValue() >= a_value2.doubleValue() ? a_value1.doubleValue() : a_value2.doubleValue());
    }

    /**
     * Compute minimum between two values.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @return
     */
    public static <T extends Number> T jMin(final T a_value1, final T a_value2) {
        return (T) new Double(a_value1.doubleValue() <= a_value2.doubleValue() ? a_value1.doubleValue() : a_value2.doubleValue());
    }

    /**
     * Compute maximum of 3 values.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @param a_value3
     * @return
     */
    public static <T extends Number> T jMax3(final T a_value1, final T a_value2, final T a_value3) {
        return (jMax(a_value1, jMax(a_value2, a_value3)));
    }

    /**
     * Return minimum of 3 values
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @param a_value3
     * @return
     */
    public static <T extends Number> T jMin3(final T a_value1, final T a_value2, final T a_value3) {
        return (jMin(a_value1, jMin(a_value2, a_value3)));
    }

    /**
     * Compute maximum of absolute values of 2 numbers.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @return
     */
    public static <T extends Number> T jMaxAbs(final T a_value1, final T a_value2) {
        return (jAbs(a_value1).doubleValue() >= jAbs(a_value2).doubleValue() ? a_value1 : a_value2);
    }

    /**
     * Compute minimum of absolute values of 2 values.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @return
     */
    public static <T extends Number> T jMinAbs(final T a_value1, final T a_value2) {
        return jAbs(a_value1.doubleValue() <= jAbs(a_value2).doubleValue() ? a_value1 : a_value2);
    }

    /**
     * Compute maximum of absolute values of 3 values.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @param a_value3
     * @return
     */
    public static <T extends Number> T jMax3Abs(final T a_value1, final T a_value2, final T a_value3) {
        return jMaxAbs(a_value1, jMaxAbs(a_value2, a_value3));
    }

    /**
     * Compute minimum of absolute values of 3 values.
     *
     * @param <T>
     * @param a_value1
     * @param a_value2
     * @param a_value3
     * @return
     */
    public static <T extends Number> T jMin3Abs(final T a_value1, final T a_value2, final T a_value3) {
        return jMinAbs(a_value1, jMinAbs(a_value2, a_value3));
    }

    /**
     * Swap two elements.
     * @param <T>
     * @param a_value1
     * @param a_value2
     */
    public static <T extends Number> void jSwap(T a_value1, T a_value2) {
        T value = a_value1;
        a_value1 = a_value2;
        a_value2 = value;
    }

    /**
     * Linear interpolation from \e value0 (when a=0) to \e value1 (when a=1).
     *
     * @param <T>
     * @param a_level
     * @param a_value1
     * @param a_value2
     * @return
     */
    public static <T extends Number> T jLerp(final double a_level, final T a_value1, final T a_value2) {
        return (T) new Double((a_value2.doubleValue() * a_level + a_value1.doubleValue() * (1 - a_level)));
    }

    /**
     * Clamp the input to the specified range.
     *
     * @param <T>
     * @param a_value
     * @param a_low
     * @param a_high
     * @return
     */
    public static <T extends Number> T jClamp(final T a_value, final T a_low, final T a_high) {
        /*return (T) new Double((a_value.doubleValue() < a_low.doubleValue() ? a_low.doubleValue()
        : a_value.doubleValue() > a_high.doubleValue() ? a_high.doubleValue()
        : a_value.doubleValue()));
         *
         */

        if (a_value instanceof Float) {
            return (T) new Float((a_value.floatValue() < a_low.floatValue() ? a_low.floatValue()
                    : a_value.floatValue() > a_high.floatValue() ? a_high.floatValue()
                    : a_value.floatValue()));
        } else {
            return (T) new Double((a_value.doubleValue() < a_low.doubleValue() ? a_low.doubleValue()
                    : a_value.doubleValue() > a_high.doubleValue() ? a_high.doubleValue()
                    : a_value.doubleValue()));
        }
    }

    /**
     * Clamp the input to the range 0 - \e infinity.
     *
     * @param <T>
     * @param a_value
     * @return
     */
    public static <T extends Number> T jClamp0(T a_value) {
        return (T) jMax(0, a_value);
    }

    /**
     * Clamp the input to the range [0,1].
     *
     * @param a_value
     * @return
     */
    public static double jClamp01(double a_value) {
        return (jClamp(a_value, 0.0, 1.0));
    }

    /**
     * Check whether \e value is in the range of [low, high].
     *
     * @param <T>
     * @param <V>
     * @param a_value
     * @param a_low
     * @param a_high
     * @return
     */
    public static <T extends Number, V extends Number> boolean jContains(final T a_value, final V a_low, final V a_high) {
        return ((a_value.doubleValue() >= a_low.doubleValue()) && (a_value.doubleValue() <= a_high.doubleValue()));
    }

    /**
     * Compute the square of a scalar.
     *
     * @param a_value
     * @return
     */
    public static double jSqr(final double a_value) {
        return (a_value * a_value);
    }

    /**
     * Compute the cosine of an angle defined in degrees.
     *
     * @param a_angleDeg
     * @return
     */
    public static double jCosDeg(final double a_angleDeg) {
        return (Math.cos(a_angleDeg * JConstants.CHAI_DEG2RAD));
    }

    /**
     * Compute the sine of an angle defined in degrees.
     *
     * @param a_angleDeg
     * @return
     */
    public static double jSinDeg(final double a_angleDeg) {
        return (Math.sin(a_angleDeg * JConstants.CHAI_DEG2RAD));
    }

    /**
     * Compute the tangent of an angle defined in degrees.
     *
     * @param a_angleDeg
     * @return
     */
    public static double jTanDeg(final double a_angleDeg) {
        return (Math.tan(a_angleDeg * JConstants.CHAI_DEG2RAD));
    }

    /**
     * Return the cosine of an angle defined in radians.
     *
     * @param a_angleRad
     * @return
     */
    public static double jCosRad(final double a_angleRad) {
        return (Math.cos(a_angleRad));
    }

    /**
     * Return the sine of an angle defined in radians.
     *
     * @param a_value
     * @return
     */
    public static double jSinRad(final double a_value) {
        return (Math.sin(a_value));
    }

    /**
     * Return the tangent of an angle defined in radians.
     *
     * @param a_value
     * @return
     */
    public static double jTanRad(final double a_value) {
        return (Math.tan(a_value));
    }

    /**
     * Convert an angle from degrees to radians.
     *
     * @param a_angleDeg
     * @return
     */
    public static double jDegToRad(final double a_angleDeg) {
        return (a_angleDeg * JConstants.CHAI_DEG2RAD);
    }

    /**
     * Convert an angle from radians to degrees
     *
     * @param a_angleRad
     * @return
     */
    public static double jRadToDeg(final double a_angleRad) {
        return (a_angleRad * JConstants.CHAI_RAD2DEG);
    }

    /**
     * Compute the addition between two vectors. \n \e Result = \e Vector1 + \e Vector2
     * @param a_vector1
     * @param a_vector2
     * @return
     */
    public static JVector3d jAdd(final JVector3d a_vector1, final JVector3d a_vector2) {
        return new JVector3d(
                a_vector1.getX() + a_vector2.getX(),
                a_vector1.getY() + a_vector2.getY(),
                a_vector1.getZ() + a_vector2.getZ());
    }

    /**
     * Compute the addition between three vectors. \n \e Result = \e Vector1 + \e Vector2 + \e Vector3
     * @param a_vector1
     * @param a_vector2
     * @param a_vector3
     * @return
     */
    public static JVector3d jAdd(final JVector3d a_vector1, final JVector3d a_vector2, final JVector3d a_vector3) {
        return new JVector3d(
                a_vector1.getX() + a_vector2.getX() + a_vector3.getX(),
                a_vector1.getY() + a_vector2.getY() + a_vector3.getY(),
                a_vector1.getZ() + a_vector2.getZ() + a_vector3.getZ());
    }

    /**
     * Compute the subtraction between two vectors. \n \e Result = \e Vector1 - \e Vector2
     * @param a_vector1
     * @param a_vector2
     * @return
     */
    public static JVector3d jSub(final JVector3d a_vector1, final JVector3d a_vector2) {
        return new JVector3d(
                a_vector1.x - a_vector2.x,
                a_vector1.y - a_vector2.y,
                a_vector1.z - a_vector2.z);
    }

    /**
     * Compute the negated vector of a input vector.
     *
     * @param a_vector
     * @return
     */
    public static JVector3d jNegate(final JVector3d a_vector) {
        return new JVector3d(a_vector.getX() * -1.0, a_vector.getY() * -1.0, a_vector.getZ() * -1.0);
    }

    /**
     * Multiply a vector by a scalar.
     *
     * @param a_value
     * @param a_vector
     * @return
     */
    public static JVector3d jMul(final double a_value, final JVector3d a_vector) {
        return new JVector3d(a_vector.getX() * a_value, a_vector.getY() * a_value, a_vector.getZ() * a_value);
    }

    /**
     * Divide a vector by a scalar.
     *
     * @param a_value
     * @param a_vector
     * @return
     */
    public static JVector3d jDiv(final double a_value, final JVector3d a_vector) {
        return new JVector3d(a_vector.getX() / a_value, a_vector.getY() / a_value, a_vector.getZ() / a_value);
    }

    /**
     * Divide a scalar by components of a 3D vector and return vector
     *
     * @param a_value
     * @param a_vector
     * @return
     */
    public static JVector3d jDivVect(final double a_value, final JVector3d a_vector) {
        return new JVector3d(
                a_value / a_vector.getX(), a_value / a_vector.getY(), a_value / a_vector.getZ());
    }

    /**
     * Compute the cross product between two 3D vectors.
     *
     * @param a_vector1
     * @param a_vector2
     * @return
     */
    public static JVector3d jCross(final JVector3d a_vector1, final JVector3d a_vector2) {
        JVector3d result = new JVector3d();
        a_vector1.crossr(a_vector2, result);
        return (result);
    }

    /**
     * Compute the dot product between two vectors.
     *
     * @param a_vector1
     * @param a_vector2
     * @return
     */
    public static double jDot(final JVector3d a_vector1, final JVector3d a_vector2) {
        return (a_vector1.dot(a_vector2));
    }

    /**
     * compute the normalized vector (\e length = 1) of an input vector.
     *
     * @param a_vector
     * @return
     */
    public static JVector3d jNormalize(final JVector3d a_vector) {
        JVector3d result = new JVector3d();
        a_vector.normalizer(result);
        return (result);
    }

    /**
     * Compute the distance between two points.
     *
     * @param a_point1
     * @param a_point2
     * @return
     */
    public static double jDistance(final JVector3d a_point1, final JVector3d a_point2) {
        return (a_point1.distance(a_point2));
    }

    /**
     * Compute the squared distance between two points.
     *
     * @param a_point1
     * @param a_point2
     * @return
     */
    public static double jDistanceSq(final JVector3d a_point1, final JVector3d a_point2) {
        return (a_point1.distancesq(a_point2));
    }


    public static boolean jEqualPoints(final JVector3d v1,
            final JVector3d v2) {
        return jEqualPoints(v1, v2, JConstants.CHAI_SMALL);
    }
    /**
     * Determine whether two vectors represent the same point.
     *
     * @param v1
     * @param v2
     * @param epsilon
     * @return
     */
    public static boolean jEqualPoints(final JVector3d v1,
            final JVector3d v2,
            final double epsilon) {
        // Accelerated path for exact equality
        if (epsilon == 0.0) {
            if ((v1.getX() == v2.getX()) && (v1.getY() == v2.getY()) && (v1.getZ() == v2.getZ())) {
                return true;
            } else {
                return false;
            }
        }

        if ((Math.abs(v1.getX() - v2.getX()) < epsilon)
                && (Math.abs(v1.getY() - v2.getY()) < epsilon)
                && (Math.abs(v1.getZ() - v2.getZ()) < epsilon)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the Identity Matrix .
     * @return
     */
    public static JMatrix3d jIdentity3d() {
        JMatrix3d result = new JMatrix3d();
        result.identity();
        return (result);
    }

    /**
     * Compute the multiplication between two matrices.
     * @param aMatrix1
     * @param aMatrix2
     * @return
     */
    public static JMatrix3d jMul(final JMatrix3d aMatrix1, final JMatrix3d aMatrix2) {
        JMatrix3d result = new JMatrix3d();
        aMatrix1.mulr(aMatrix2, result);
        return (result);
    }

    /**
     * Compute the multiplication of a matrix and a vector.
     * @param aMatrix
     * @param aVector
     * @return
     */
    public static JVector3d jMul(final JMatrix3d aMatrix, final JVector3d aVector) {
        JVector3d result = new JVector3d();
        aMatrix.mulr(aVector, result);
        return (result);
    }

    /**
     * Compute the transpose of a matrix
     * @param aMatrix
     * @return
     */
    public static JMatrix3d jTrans(final JMatrix3d aMatrix) {
        JMatrix3d result = new JMatrix3d();
        aMatrix.transr(result);
        return (result);
    }

    /**
     * Compute the inverse of a matrix.
     * @param aMatrix
     * @return
     */
    public static JMatrix3d jInv(final JMatrix3d aMatrix) {
        JMatrix3d result = new JMatrix3d();
        aMatrix.invertr(result);
        return (result);
    }

    /**
     * Compute the angle in radians between two vectors.
     * @param aVector0
     * @param aVector1
     * @return
     */
    public static double jAngle(final JVector3d aVector0, final JVector3d aVector1) {
        // compute length of vectors
        double n0 = aVector0.length();
        double n1 = aVector1.length();
        double val = n0 * n1;

        // check if lengths of vectors are not zero
        if (jAbs(val) < JConstants.CHAI_SMALL) {
            return (0);
        }

        // compute angle
        double result = aVector0.dot(aVector1) / (val);
        if (result > 1.0) {
            result = 1.0;
        } else if (result < -1.0) {
            result = -1.0;
        }

        return (Math.acos(result));
    }

    /**
     * Compute the cosine of the angle between two vectors.
     * @param aVector0
     * @param aVector1
     * @return
     */
    public static double jCosAngle(final JVector3d aVector0, final JVector3d aVector1) {
        // compute length of vectors
        double n0 = aVector0.length();
        double n1 = aVector1.length();
        double val = n0 * n1;

        // check if lengths of vectors are not zero
        if (jAbs(val) < JConstants.CHAI_SMALL) {
            return (0);
        }

        // compute angle
        return (aVector0.dot(aVector1) / (val));
    }

    /**
     * Compute a rotation matrix given a rotation \e axis and an \e angle.
     * @param aAxis
     * @param aAngleRad
     * @return
     */
    public static JMatrix3d jRotMatrix(final JVector3d aAxis, final double aAngleRad) {
        JMatrix3d result = new JMatrix3d();
        result.set(aAxis, aAngleRad);
        return (result);
    }

    /**
     * Compute the rotation of a matrix around an \e axis and an \e angle.
     * @param aMatrix
     * @param aAxis
     * @param aAngleRad
     * @return
     */
    public static JMatrix3d cRotate(final JMatrix3d aMatrix,
            final JVector3d aAxis,
            final double aAngleRad) {
        JMatrix3d result = new JMatrix3d();
        aMatrix.rotater(aAxis, aAngleRad, result);
        return (result);
    }

    /**
     * Compute the projection of a point on a plane. the plane is expressed
     * by a point and a surface normal.
     * @param a_point
     * @param a_planePoint
     * @param n
     * @return
     */
    public static JVector3d jProjectPointOnPlane(final JVector3d a_point,
            final JVector3d a_planePoint,
            final JVector3d n) {
        // compute a projection matrix
        JMatrix3d projectionMatrix = new JMatrix3d();

        projectionMatrix.set(
                (n.getY() * n.getY()) + (n.getZ() * n.getZ()), -(n.getX() * n.getY()), -(n.getX() * n.getZ()),
                -(n.getY() * n.getX()), (n.getX() * n.getX()) + (n.getZ() * n.getZ()), -(n.getY() * n.getZ()),
                -(n.getZ() * n.getX()), -(n.getZ() * n.getY()), (n.getX() * n.getX()) + (n.getY() * n.getY()));

        // project point on plane and return projected point.
        JVector3d point = new JVector3d();
        a_point.subr(a_planePoint, point);
        projectionMatrix.mul(point);
        point.add(a_planePoint);

        // return result
        return (point);
    }

    /**
     * Compute the projection of a point on a plane. the plane is expressed
     * by a set of three points.
     * @param aPoint
     * @param aPlanePoint0
     * @param aPlanePoint1
     * @param aPlanePoint2
     * @return
     */
    public static JVector3d jProjectPointOnPlane(final JVector3d aPoint,
            final JVector3d aPlanePoint0, final JVector3d aPlanePoint1,
            final JVector3d aPlanePoint2) {
        // create two vectors from the three input points lying in the projection
        // plane.
        JVector3d v01 = new JVector3d();
        JVector3d v02 = new JVector3d();
        aPlanePoint1.subr(aPlanePoint0, v01);
        aPlanePoint2.subr(aPlanePoint0, v02);

        // compute the normal vector of the plane
        JVector3d n = new JVector3d();
        v01.crossr(v02, n);
        n.normalize();

        return jProjectPointOnPlane(aPoint, aPlanePoint0, n);
    }

    /**
     * Projects a 3D point on a plane composed of three points. \n
     * This function returns two parameters \e v01 and \e v02 which correspond
     * to the factord which express the following relation:
     * @param aPoint
     * @param aPlanePoint0
     * @param aPlanePoint1
     * @param aPlanePoint2
     * @param a_v01
     * @param a_v02
     */
    public static void jProjectPointOnPlane(final JVector3d aPoint,
            final JVector3d aPlanePoint0, final JVector3d aPlanePoint1,
            final JVector3d aPlanePoint2, double a_v01, double a_v02) {
        JVector3d v01 = jSub(aPlanePoint1, aPlanePoint0);
        JVector3d v02 = jSub(aPlanePoint2, aPlanePoint0);
        JVector3d point = jSub(aPoint, aPlanePoint0);

        // matrix
        double m00 = v01.x * v01.x + v01.y * v01.y + v01.z * v01.z;
        double m01 = v01.x * v02.x + v01.y * v02.y + v01.z * v02.z;
        double m10 = m01;
        double m11 = v02.x * v02.x + v02.y * v02.y + v02.z * v02.z;
        double det = m00 * m11 - m10 * m01;

        // vector
        double vm0 = v01.x * point.x + v01.y * point.y + v01.z * point.z;
        double vm1 = v02.x * point.x + v02.y * point.y + v02.z * point.z;

        // inverse
        a_v01 = (1.0 / det) * (m11 * vm0 - m01 * vm1);
        a_v02 = (1.0 / det) * (-m10 * vm0 + m00 * vm1);
    }

    /**
     * Compute the projection of a point on a line. the line is expressed
     * by a point located on the line and a direction vector
     * @param aPoint
     * @param aPointOnLine
     * @param aDirectionOfLine
     * @return
     */
    public static JVector3d jProjectPointOnLine(final JVector3d aPoint,
            final JVector3d aPointOnLine, final JVector3d aDirectionOfLine) {
        // temp variable
        JVector3d point = new JVector3d();
        JVector3d result = new JVector3d();

        // compute projection
        double lengthDirSq = aDirectionOfLine.lengthsq();
        aPoint.subr(aPointOnLine, point);

        aDirectionOfLine.mulr((point.dot(aDirectionOfLine) / (lengthDirSq)),
                result);

        result.add(aPointOnLine);

        // return result
        return (result);
    }

    /**
     * Compute the projection of a point on a segment. the segment is described
     * by its two extremity points
     * @param aPoint
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @return
     */
    public static JVector3d jProjectPointOnSegment(final JVector3d aPoint,
            final JVector3d aSegmentPointA,
            final JVector3d aSegmentPointB) {
        // if both points are equal
        if (aSegmentPointA.equals(aSegmentPointB)) {
            return (aSegmentPointA);
        }

        // compute line
        JVector3d segmentAB = new JVector3d();
        aSegmentPointB.subr(aSegmentPointA, segmentAB);

        // project tool onto segment
        JVector3d projection = jProjectPointOnLine(aPoint, aSegmentPointA, segmentAB);

        double distanceAB = segmentAB.lengthsq();
        double distanceAP = jDistanceSq(projection, aSegmentPointA);
        double distanceBP = jDistanceSq(projection, aSegmentPointB);

        if (distanceAP > distanceAB) {
            return (aSegmentPointB);
        } else if (distanceBP > distanceAB) {
            return (aSegmentPointA);
        } else {
            return (projection);
        }
    }

    /**
     * Project a vector \e V0 onto a second vector \e V1.
     * @param aVector0
     * @param aVector1
     * @return
     */
    public static JVector3d jProject(final JVector3d aVector0, final JVector3d aVector1) {
        // temp variable
        JVector3d result = new JVector3d();

        // compute projection
        double lengthSq = aVector1.lengthsq();
        aVector1.mulr((aVector0.dot(aVector1) / (lengthSq)), result);

        // return result
        return (result);
    }

    /**
     * Compute the normal of a surface defined by three point passed as
     * parameters.
     * @param aSurfacePoint0
     * @param aSurfacePoint1
     * @param aSurfacePoint2
     * @return
     */
    public static JVector3d jComputeSurfaceNormal(final JVector3d aSurfacePoint0,
            final JVector3d aSurfacePoint1, final JVector3d aSurfacePoint2) {
        // temp variable
        JVector3d v01 = new JVector3d();
        JVector3d v02 = new JVector3d();
        JVector3d result = new JVector3d();

        // compute surface normal
        aSurfacePoint1.subr(aSurfacePoint0, v01);
        aSurfacePoint2.subr(aSurfacePoint0, v02);
        v01.normalize();
        v02.normalize();
        v01.crossr(v02, result);
        result.normalize();

        // return result
        return (result);
    }

    /**
     * Returns true if \e point is contained in the bounding box defined by min and max
     * 
     * @param aPoint
     * @param boxMin
     * @param boxMax
     * @return
     */
    public static boolean jBoxContains(final JVector3d aPoint, final JVector3d boxMin, final JVector3d boxMax) {
        if ((aPoint.getX() >= boxMin.getX()) && (aPoint.getX() <= boxMax.getX())
                && (aPoint.getY() >= boxMin.getY()) && (aPoint.getY() <= boxMax.getY())
                && (aPoint.getZ() >= boxMin.getZ()) && (aPoint.getZ() <= boxMax.getZ())) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of intersection points between a segment AB and
     * a sphere of radius \e R. \n
     * The number of points can be \e 0, \e 1 or \e 2. \n
     * If two intersection points are detected, \e collisionPoint0 will be
     * the closest one to \e segmentPointA of segment AB.
     * 
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @param aSpherePos
     * @param aSphereRadius
     * @param aCollisionPoint0
     * @param aCollisionNormal0
     * @param aCollisionPoint1
     * @param aCollisionNormal1
     * @return
     */
    public static int jIntersectionSegmentSphere(JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JVector3d aSpherePos,
            double aSphereRadius,
            JVector3d aCollisionPoint0,
            JVector3d aCollisionNormal0,
            JVector3d aCollisionPoint1,
            JVector3d aCollisionNormal1) {
        
        
        // temp variables
        JVector3d AB = new JVector3d();
        JVector3d CA = new JVector3d();
        aSegmentPointB.subr(aSegmentPointA, AB);
        aSegmentPointA.subr(aSpherePos, CA);
        double radiusSq = aSphereRadius * aSphereRadius;

        double a = AB.lengthsq();
        double b = 2.0 * jDot(AB, CA);
        double c = CA.lengthsq() - radiusSq;

        // invalid segment
        if (a == 0) {
            return (0);
        }

        double d = b * b - 4 * a * c;

        // segment ray is located outside of sphere
        if (d < 0) {
            return (0);
        }

        // segment ray intersects sphere
        d = Math.sqrt(d);
        double e = 2.0 * a;

        // compute both solutions
        double u0 = (-b + d) / e;
        double u1 = (-b - d) / e;

        // check if the solutions are located along the segment AB
        boolean valid_u0 = jContains(u0, 0.0, 1.0);
        boolean valid_u1 = jContains(u1, 0.0, 1.0);

        // two intersection points are located along segment AB
        if (valid_u0 && valid_u1) {
            if (u0 > u1) {
                jSwap(u0, u1);
            }

            // compute point 0
            AB.mulr(u0, aCollisionPoint0);
            aCollisionPoint0.add(aSegmentPointA);

            aCollisionPoint0.subr(aSpherePos, aCollisionNormal0);
            aCollisionNormal0.normalize();

            // compute point 1
            AB.mulr(u1, aCollisionPoint1);
            aCollisionPoint1.add(aSegmentPointA);

            aCollisionPoint1.subr(aSpherePos, aCollisionNormal1);
            aCollisionNormal1.normalize();

            return (2);
        } // one intersection point is located along segment AB
        else if (valid_u0) {
            // compute point 0
            AB.mulr(u0, aCollisionPoint0);
            aCollisionPoint0.add(aSegmentPointA);

            aCollisionPoint0.subr(aSpherePos, aCollisionNormal0);
            aCollisionNormal0.normalize();

            return (1);
        } // one intersection point is located along segment AB
        else if (valid_u1) {
            // compute point 0
            AB.mulr(u1, aCollisionPoint0);
            aCollisionPoint0.add(aSegmentPointA);

            aCollisionPoint0.subr(aSpherePos, aCollisionNormal0);
            aCollisionNormal0.normalize();

            return (1);
        } // both points are located outside of the segment AB
        else {
            return (0);
        }

    }

    /**
     * Returns the number of intersection points between a segment AB and
     * a open topless cylinder of radius \e R. \n
     * The number of intersection points can be \e 0, \e 1 or \e 2. \n
     * If two intersection points are detected, \e collisionPoint0 will
     * correspond to the point nearest to \e segmentPointA of segment AB.
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @param aCylinderPointA
     * @param aCylinderPointB
     * @param aCylinderRadius
     * @param aCollisionPoint0
     * @param aCollisionNormal0
     * @param aCollisionPoint1
     * @param aCollisionNormal1
     * @return
     */
    public static int jIntersectionSegmentToplessCylinder(
            JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JVector3d aCylinderPointA,
            JVector3d aCylinderPointB,
            double aCylinderRadius,
            JVector3d aCollisionPoint0,
            JVector3d aCollisionNormal0,
            JVector3d aCollisionPoint1,
            JVector3d aCollisionNormal1) {
        
        
        JVector3d RC = aSegmentPointA.operatorSub(aCylinderPointA);
        JVector3d segmentAB = aSegmentPointB.operatorSub(aSegmentPointA);
        JVector3d segmentDir = jNormalize(segmentAB);
        JVector3d cylinderDir = jNormalize(aCylinderPointB.operatorSub(aCylinderPointA));
        JVector3d n = jCross(segmentDir, cylinderDir);

        // segment is parallel to cylinder
        double length = n.length();
        if (length == 0.0) {
            return (0);
        }

        n.normalize();
        double d = jAbs(jDot(RC, n));

        if (d <= aCylinderRadius) {
            JVector3d O = jCross(RC, cylinderDir);
            double t = -jDot(O, n) / length;
            O = jCross(n, cylinderDir);
            O.normalize();
            double s = jAbs(Math.sqrt(aCylinderRadius * aCylinderRadius - d * d) / jDot(segmentDir, O));
            double u0 = t - s;
            double u1 = t + s;

            // reorder solutions
            if (u0 > u1) {
                jSwap(u0, u1);
            }

            boolean valid_u0 = true;
            boolean valid_u1 = true;

            // check if solutions along segment
            double lengthAB = segmentAB.length();
            if (!jContains(u0, 0.0, lengthAB)) {
                valid_u0 = false;
            }
            if (!jContains(u1, 0.0, lengthAB)) {
                valid_u1 = false;
            }

            // check if solutions lay along cylinder
            JVector3d P0 = new JVector3d();
            JVector3d P1 = new JVector3d();

            JVector3d cylinderDirNeg = jNegate(cylinderDir);

            if (valid_u0) {
                segmentDir.operatorMul(u0);
                P0 = aSegmentPointA.operatorAdd(segmentDir);
                double cosAngleA = jCosAngle(cylinderDir, jSub(P0, aCylinderPointA));
                double cosAngleB = jCosAngle(cylinderDirNeg, jSub(P0, aCylinderPointB));

                if ((cosAngleA <= 0.0) || (cosAngleB <= 0.0)) {
                    valid_u0 = false;
                }
            }

            if (valid_u1) {
                segmentDir.operatorMul(u1);
                P1 = aSegmentPointA.operatorAdd(segmentDir);
                double cosAngleA = jCosAngle(cylinderDir, jSub(P1, aCylinderPointA));
                double cosAngleB = jCosAngle(cylinderDirNeg, jSub(P1, aCylinderPointB));

                if ((cosAngleA <= 0.0) || (cosAngleB <= 0.0)) {
                    valid_u1 = false;
                }
            }

            if (valid_u0 && valid_u1) {
                aCollisionPoint0.copyFrom(P0);
                aCollisionNormal0.copyFrom(jNormalize(jCross(cylinderDir, jCross(jSub(P0, aCylinderPointA), cylinderDir))));
                aCollisionPoint1.copyFrom(P1);
                aCollisionNormal1.copyFrom(jNormalize(jCross(cylinderDir, jCross(jSub(P1, aCylinderPointA), cylinderDir))));
                return (2);
            } else if (valid_u0) {
                aCollisionPoint0.copyFrom(P0);
                aCollisionNormal0.copyFrom(jNormalize(jCross(cylinderDir, jCross(jSub(P0, aCylinderPointA), cylinderDir))));
                return (1);
            } else if (valid_u1) {
                aCollisionPoint0.copyFrom(P1);
                aCollisionNormal0.copyFrom(jNormalize(jCross(cylinderDir, jCross(jSub(P1, aCylinderPointA), cylinderDir))));
                return (1);
            }
        }

        return (0);
    }

    /**
     * Returns true if segment AB intersects triangle defined by
     * its three vertices (\e V0, \e V1, \e V2).
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @param aTriangleVertex0
     * @param aTriangleVertex1
     * @param aTriangleVertex2
     * @param aCollisionPoint
     * @param aCollisionNormal
     * @return
     */
    public static boolean jIntersectionSegmentTriangle(
            JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JVector3d aTriangleVertex0,
            JVector3d aTriangleVertex1,
            JVector3d aTriangleVertex2,
            JVector3d aCollisionPoint,
            JVector3d aCollisionNormal) {
        // This value controls how close rays can be to parallel to the triangle
        // surface before we discard them
        final double CHAI_INTERSECT_EPSILON = 10e-14f;

        // compute a ray and check its length
        JVector3d rayDir = new JVector3d();
        aSegmentPointB.subr(aSegmentPointA, rayDir);
        double segmentLengthSquare = rayDir.lengthsq();
        if (segmentLengthSquare == 0.0) {
            return (false);
        }

        // Compute the triangle's normal
        JVector3d t_E0 = new JVector3d();
        JVector3d t_E1 = new JVector3d();
        JVector3d t_N = new JVector3d();

        aTriangleVertex1.subr(aTriangleVertex0, t_E0);
        aTriangleVertex2.subr(aTriangleVertex0, t_E1);
        t_E0.crossr(t_E1, t_N);

        // If the ray is parallel to the triangle (perpendicular to the
        // normal), there's no collision
        if (Math.abs(t_N.dot(rayDir)) < 10E-15f) {
            return (false);
        }

        double t_T = jDot(t_N, jSub(aTriangleVertex0, aSegmentPointA)) / jDot(t_N, rayDir);

        if (t_T + CHAI_INTERSECT_EPSILON < 0) {
            return (false);
        }

        JVector3d t_Q = jSub(jAdd(aSegmentPointA, jMul(t_T, rayDir)), aTriangleVertex0);
        double t_Q0 = jDot(t_E0, t_Q);
        double t_Q1 = jDot(t_E1, t_Q);
        double t_E00 = jDot(t_E0, t_E0);
        double t_E01 = jDot(t_E0, t_E1);
        double t_E11 = jDot(t_E1, t_E1);
        double t_D = (t_E00 * t_E11) - (t_E01 * t_E01);

        if ((t_D > -CHAI_INTERSECT_EPSILON) && (t_D < CHAI_INTERSECT_EPSILON)) {
            return (false);
        }

        double t_S0 = ((t_E11 * t_Q0) - (t_E01 * t_Q1)) / t_D;
        double t_S1 = ((t_E00 * t_Q1) - (t_E01 * t_Q0)) / t_D;

        // Collision has occurred. It is reported.
        if ((t_S0 >= 0.0 - CHAI_INTERSECT_EPSILON)
                && (t_S1 >= 0.0 - CHAI_INTERSECT_EPSILON)
                && ((t_S0 + t_S1) <= 1.0 + CHAI_INTERSECT_EPSILON)) {
            JVector3d t_I = jAdd(aTriangleVertex0, jMul(t_S0, t_E0), jMul(t_S1, t_E1));

            // Square distance between ray origin and collision point.
            double distanceSquare = aSegmentPointA.distancesq(t_I);

            // check if collision occurred within segment. If yes, report collision
            if (distanceSquare <= segmentLengthSquare) {
                aCollisionPoint.copyFrom(jAdd(aSegmentPointA, jMul(t_T, rayDir)));
                t_N.normalizer(aCollisionNormal);
                if (jCosAngle(aCollisionNormal, rayDir) > 0.0) {
                    aCollisionNormal.negate();
                }
                return (true);
            }
        }

        // no collision occurred
        return (false);
    }
}
