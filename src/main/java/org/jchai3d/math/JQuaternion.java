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
 *   of our support services, please contact CHAI 3D about aJQuiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */

package org.jchai3d.math;

/**
 *
 * @author Marcos da Silva Ramos <marcos.9306@gmail.com>
 */
class JQuaternion {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    //! Component w of quaternion
    public double w;
    //! Component x of quaternion
    public double x;
    //! Component y of quaternion
    public double y;
    //! Component z of quaternion
    public double z;

    //-----------------------------------------------------------------------
    // RUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    //! ructor of JQuaternion.
    public JQuaternion() {
    }

    //! ructor of JQuaternion.
    public JQuaternion(double nw, double nx, double ny, double nz) {
        w = nw;
        x = nx;
        y = ny;
        z = nz;
    }

    //! ructor of JQuaternion.
    public JQuaternion(final double[] in) {
        w = in[0];
        x = in[1];
        y = in[2];
        z = in[3];
    }

    //-----------------------------------------------------------------------
    // OVERLOADED CAST OPERATORS:
    //-----------------------------------------------------------------------
    //! Cast quaternion to a double*
    public double[] toArray() {
        return new double[]{w, x, y, z};
    }

    //! Clear quaternion with zeros.
    public void zero() {
        w = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    //! Negate current quaternion. \n Result is stored in current quaternion.
    public void negate() {
        w = -w;
        x = -x;
        y = -y;
        z = -z;
    }

    //! Returns quaternion magnitude squared.
    public double magsq() {
        return (w * w) + (x * x) + (y * y) + (z * z);
    }

    //! Returns quaternion magnitude squared.
    public double lengthsq() {
        return magsq();
    }

    //! Returns quaternion magnitude.
    public double mag() {
        return Math.sqrt(magsq());
    }

    //! Returns quaternion magnitude.
    public double length() {
        return mag();
    }

    //! Normalize quaternion.
    public void normalize() {
        double m = mag();
        w /= m;
        x /= m;
        y /= m;
        z /= m;
    }

    //---------------------------------------------------------------
    //! Convert quaternion to rotation matrix.
    /*!
    \param   a_mat The matrix to store the result into.
     */
    //---------------------------------------------------------------
    public void toRotMat(JMatrix3d a_mat) {
        double x2 = 2.0 * x * x;
        double y2 = 2.0 * y * y;
        double z2 = 2.0 * z * z;
        double xy = 2.0 * x * y;
        double wz = 2.0 * w * z;
        double xz = 2.0 * x * z;
        double wy = 2.0 * w * y;
        double yz = 2.0 * y * z;
        double wx = 2.0 * w * x;

        a_mat.m[0][0] = 1.0 - y2 - z2;
        a_mat.m[0][1] = xy - wz;
        a_mat.m[0][2] = xz + wy;
        a_mat.m[1][0] = xy + wz;
        a_mat.m[1][1] = 1.0 - x2 - z2;
        a_mat.m[1][2] = yz - wx;
        a_mat.m[2][0] = xz - wy;
        a_mat.m[2][1] = yz + wx;
        a_mat.m[2][2] = 1.0 - x2 - y2;
    }

    //---------------------------------------------------------------
    /*!
    Convert rotation matrix to quaternion.

    \param   a_mat The rotation matrix to convert.
     */
    //---------------------------------------------------------------
    public void fromRotMat(JMatrix3d a_mat) {
        double trace = 1.0 + a_mat.m[0][0] + a_mat.m[1][1] + a_mat.m[2][2];

        if (trace > 0.00000001) {
            double s = 2.0 * Math.sqrt(trace);
            x = (a_mat.m[2][1] - a_mat.m[1][2]) / s;
            y = (a_mat.m[0][2] - a_mat.m[2][0]) / s;
            z = (a_mat.m[1][0] - a_mat.m[0][1]) / s;
            w = 0.25 * s;
        } else if ((a_mat.m[0][0] > a_mat.m[1][1]) && (a_mat.m[0][0] > a_mat.m[2][2])) {
            // column 1 has largest diagonal
            double s = 2.0 * Math.sqrt(1.0 + a_mat.m[0][0] - a_mat.m[1][1] - a_mat.m[2][2]);
            x = 0.25 * s;
            y = (a_mat.m[1][0] + a_mat.m[0][1]) / s;
            z = (a_mat.m[0][2] + a_mat.m[2][0]) / s;
            w = (a_mat.m[2][1] - a_mat.m[1][2]) / s;
        } else if (a_mat.m[1][1] > a_mat.m[2][2]) {
            // column 2 has largest diagonal
            double s = 2.0 * Math.sqrt(1.0 + a_mat.m[1][1] - a_mat.m[0][0] - a_mat.m[2][2]);
            x = (a_mat.m[1][0] + a_mat.m[0][1]) / s;
            y = 0.25 * s;
            z = (a_mat.m[2][1] + a_mat.m[1][2]) / s;
            w = (a_mat.m[0][2] - a_mat.m[2][0]) / s;
        } else {
            // column 3 has largest diagonal
            double s = 2.0 * Math.sqrt(1.0 + a_mat.m[2][2] - a_mat.m[0][0] - a_mat.m[1][1]);
            x = (a_mat.m[0][2] + a_mat.m[2][0]) / s;
            y = (a_mat.m[2][1] + a_mat.m[1][2]) / s;
            z = 0.25 * s;
            w = (a_mat.m[1][0] - a_mat.m[0][1]) / s;
        }
    }

    //---------------------------------------------------------------
    /*!
    Convert from axis and angle (in radians).

    \param  a_axis  The axis.
    \param  a_angle The angle in radians.
     */
    //---------------------------------------------------------------
    public void fromAxisAngle(JVector3d a_axis, double a_angle) {
        // not that axis is passed by value so that we can normalize it
        a_axis.normalize();
        double sina = Math.sin(a_angle / 2.0);
        double cosa = Math.cos(a_angle / 2.0);
        w = cosa;
        x = a_axis.getX() * sina;
        y = a_axis.getY() * sina;
        z = a_axis.getZ() * sina;
    }

    //---------------------------------------------------------------
    /*!
    Convert to axis (not normalized) and angle.

    \param a_axis  Where to store the axis.
    \param a_angle Where to store the angle.
     */
    //---------------------------------------------------------------
    public void toAxisAngle(JVector3d a_axis, double a_angle) {
        double cosa = w / mag();
        a_angle = Math.acos(cosa);
        a_axis.setX(x);
        a_axis.setY(y);
        a_axis.setZ(z);
    }

    //! Conjugate of quaternion
    public void conj() {
        x = -x;
        y = -y;
        z = -z;
    }

    //! Invert quaternion ( inverse is conjugate/magsq )
    public void invert() {
        double m2 = magsq();
        w = w / m2;
        x = -x / m2;
        y = -y / m2;
        z = -z / m2;
    }

    //! Multiply operator (grassman product)
    public JQuaternion operatorMul(JQuaternion a_otherQ) {
        double neww = w * a_otherQ.w - x * a_otherQ.x - y * a_otherQ.y - z * a_otherQ.z;
        double newx = w * a_otherQ.x + x * a_otherQ.w + y * a_otherQ.z - z * a_otherQ.y;
        double newy = w * a_otherQ.y - x * a_otherQ.z + y * a_otherQ.w + z * a_otherQ.x;
        double newz = w * a_otherQ.z + x * a_otherQ.y - y * a_otherQ.x + z * a_otherQ.w;
        w = neww;
        x = newx;
        y = newy;
        z = newz;

        return this;
    }

    //---------------------------------------------------------------
    /*!
    Quaternion multiplication \n
    Multiply this quaternion with another and store result here.

    \param  a_otherQ The other quaternion.
     */
    //---------------------------------------------------------------
    public void mul(JQuaternion a_otherQ) {
        operatorMul(a_otherQ);
    }

    //! Scale operator
    public JQuaternion operatorMul(double a_scale) {
        w *= a_scale;
        x *= a_scale;
        y *= a_scale;
        z *= a_scale;
        return this;
    }

    //! Scale this quaternion by a scalar
    public void mul(double s) {
        operatorMul(s);
    }

    //! Equality operator
    public boolean operatorEquals(JQuaternion a_otherQ) {
        return (w == a_otherQ.w && x == a_otherQ.x && y == a_otherQ.y && z == a_otherQ.z);
    }

    //---------------------------------------------------------------
    /*!
    Dot product. \n
    Take the dot product with another quaternion and store the result here.

    \param  a_otherQ The other quaternion.
    \return The result of the dot product.
     */
    //---------------------------------------------------------------
    public double dot(JQuaternion a_otherQ) {
        return (w * a_otherQ.w + x * a_otherQ.x + y * a_otherQ.y + z * a_otherQ.z);
    }

    //! Addition
    public JQuaternion operatorAdd(JQuaternion a_otherQ) {
        w += a_otherQ.w;
        x += a_otherQ.x;
        y += a_otherQ.y;
        z += a_otherQ.z;
        return this;
    }

    //---------------------------------------------------------------
    /*!
    Addition \n
    Add another quaternion to this one and store here.

    \param a_otherQ The other quaternion.
     */
    //---------------------------------------------------------------
    public void add(JQuaternion a_otherQ) {
        operatorAdd(a_otherQ);
    }

    //---------------------------------------------------------------
    /*!
    Spherical linear interpolation. \n
    Spherically linearly interpolate between quaternions and store
    the result here.

    \param a_level Parameter between 0 (fully at a_q1) and 1.0 (fully at a_q2).
    \param a_q1    Starting quaternion.
    \param a_q2    Ending quaternion.
     */
    //---------------------------------------------------------------
    public void slerp(double a_level, JQuaternion a_q1, JQuaternion a_q2) {
        // a_q2 is passed by value so that we can scale it, etc.
        // compute angle between a_q1 and a_q2
        double costheta = a_q1.dot(a_q2);
        if ((costheta - 1.0) < 1e-4 && (costheta - 1.0) > -1e-4) {
            // quarternions are parallel
            // linearly interpolate and normalize
            copyFrom(a_q1);
            this.mul(1.0 - a_level);
            a_q2.mul(a_level);
            this.operatorAdd(a_q2);
            this.normalize();
        } else {
            double ratio1, ratio2;
            if ((costheta + 1.0) > -1e-4 && (costheta + 1.0) < 1e-4) {
                // a_q1 and a_q2 are 180 degrees apart
                // there is no unique path between them
                a_q2.w = a_q1.z;
                a_q2.x = -a_q1.y;
                a_q2.y = a_q1.x;
                a_q2.z = -a_q1.w;
                ratio1 = Math.sin(Math.PI * (0.5 - a_level));
                ratio2 = Math.sin(Math.PI * a_level);
            } else {
                if (costheta < 0.0) {
                    costheta = -costheta;
                    a_q2.negate();
                }
                double theta = Math.acos(costheta);
                double sintheta = Math.sin(theta);

                ratio1 = Math.sin(theta * (1.0 - a_level)) / sintheta;
                ratio2 = Math.sin(theta * a_level) / sintheta;
            }
            this.copyFrom(a_q1);
            this.mul(ratio1);
            a_q2.mul(ratio2);
            this.operatorAdd(a_q2);
        }
    }

    public void copyFrom(JQuaternion q) {
        x = q.x;
        y = q.y;
        z = q.z;
        w = q.w;
    }
}
