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
public class JMatrix3d {

    public double[][] m;

    /**
     * Constructor of cMatrix3d.
     */
    public JMatrix3d() {
        m = new double[3][3];
    }
    
    public JMatrix3d(JMatrix3d o) {
        copyFrom(o);
    }

    /**
     * Initialize a matrix with a scalar which is copied to each cell of
    the matrix.
     *
     * @param a_value
     */
    public void set(final double a_value) {
        m[0][0] = a_value;
        m[0][1] = a_value;
        m[0][2] = a_value;
        m[1][0] = a_value;
        m[1][1] = a_value;
        m[1][2] = a_value;
        m[2][0] = a_value;
        m[2][1] = a_value;
        m[2][2] = a_value;

    }

    /**
     * Initialize a matrix bypassing as parameter values for each cell.
     *
     * @param a_m00
     * @param a_m01
     * @param a_m02
     * @param a_m10
     * @param a_m11
     * @param a_m12
     * @param a_m20
     * @param a_m21
     * @param a_m22
     */
    public void set(final double a_m00, final double a_m01, final double a_m02,
            final double a_m10, final double a_m11, final double a_m12,
            final double a_m20, final double a_m21, final double a_m22) {
        m[0][0] = a_m00;
        m[0][1] = a_m01;
        m[0][2] = a_m02;
        m[1][0] = a_m10;
        m[1][1] = a_m11;
        m[1][2] = a_m12;
        m[2][0] = a_m20;
        m[2][1] = a_m21;
        m[2][2] = a_m22;

    }

    /**
     * Initialize a matrix by passing as parameter 3 column vectors. \n
    M = (V0,V1,V2).
     *
     * @param a_vectCol0
     * @param a_vectCol1
     * @param a_vectCol2
     */
    public void setCol(final JVector3d a_vectCol0, final JVector3d a_vectCol1,
            final JVector3d a_vectCol2) {
        m[0][0] = a_vectCol0.getX();
        m[0][1] = a_vectCol1.getX();
        m[0][2] = a_vectCol2.getX();
        m[1][0] = a_vectCol0.getY();
        m[1][1] = a_vectCol1.getY();
        m[1][2] = a_vectCol2.getY();
        m[2][0] = a_vectCol0.getZ();
        m[2][1] = a_vectCol1.getZ();
        m[2][2] = a_vectCol2.getZ();

    }

    /**
     * Set column 0 of matrix with vector passed as parameter.
     *
     * @param a_vectCol
     */
    public void setCol0(final JVector3d a_vectCol) {
        m[0][0] = a_vectCol.getX();
        m[1][0] = a_vectCol.getY();
        m[2][0] = a_vectCol.getZ();



    }

    /**
     * Set column 1 of matrix with vector passed as parameter.
     *
     * @param a_vectCol
     */
    public void setCol1(final JVector3d a_vectCol) {
        m[0][1] = a_vectCol.getX();
        m[1][1] = a_vectCol.getY();
        m[2][1] = a_vectCol.getZ();



    }

    /**
     * Set column 2 of matrix with vector passed as parameter.
     *
     * @param a_vectCol
     */
    public void setCol2(final JVector3d a_vectCol) {
        m[0][2] = a_vectCol.getX();
        m[1][2] = a_vectCol.getY();
        m[2][2] = a_vectCol.getZ();



    }

    /**
     * Read column vector 0 of matrix.
     *
     * @return
     */
    public final JVector3d getCol0() {
        JVector3d result = new JVector3d();
        result.setX(m[0][0]);
        result.setY(m[1][0]);
        result.setZ(m[2][0]);



        return (result);
    }

    /**
     * Read column vector 1 of matrix.
     *
     * @return
     */
    public final JVector3d getCol1() {
        JVector3d result = new JVector3d();
        result.setX(m[0][1]);
        result.setY(m[1][1]);
        result.setZ(m[2][1]);



        return (result);
    }

    /**
     * Read column vector 2 of matrix.
     *
     * @return
     */
    public final JVector3d getCol2() {
        JVector3d result = new JVector3d();
        result.setX(m[0][2]);
        result.setY(m[1][2]);
        result.setZ(m[2][2]);



        return (result);
    }

    /**
     * Read a row of this matrix.
     *
     * @param index
     * @return
     */
    public final JVector3d getRow(final int index) {
        JVector3d result = new JVector3d();
        result.setX(m[index][0]);
        result.setY(m[index][1]);
        result.setZ(m[index][2]);



        return (result);
    }

    /**
     * Copy current matrix values to an external matrix passed as parameter.
     *
     * @param a_destination
     */
    public final void copyTo(JMatrix3d a_destination) {
        a_destination.m[0][0] = m[0][0];
        a_destination.m[0][1] = m[0][1];
        a_destination.m[0][2] = m[0][2];
        a_destination.m[1][0] = m[1][0];
        a_destination.m[1][1] = m[1][1];
        a_destination.m[1][2] = m[1][2];
        a_destination.m[2][0] = m[2][0];
        a_destination.m[2][1] = m[2][1];
        a_destination.m[2][2] = m[2][2];

    }

    /**
     * Copy values from an external matrix passed as parameter to current
    matrix.
     * @param a_source
     */
    public void copyFrom(final JMatrix3d a_source) {
        m[0][0] = a_source.m[0][0];
        m[0][1] = a_source.m[0][1];
        m[0][2] = a_source.m[0][2];
        m[1][0] = a_source.m[1][0];
        m[1][1] = a_source.m[1][1];
        m[1][2] = a_source.m[1][2];
        m[2][0] = a_source.m[2][0];
        m[2][1] = a_source.m[2][1];
        m[2][2] = a_source.m[2][2];

    }

    /**
     * Set the identity matrix.
     */
    public void identity() {
        m[0][0] = 1.0;
        m[0][1] = 0.0;
        m[0][2] = 0.0;
        m[1][0] = 0.0;
        m[1][1] = 1.0;
        m[1][2] = 0.0;
        m[2][0] = 0.0;
        m[2][1] = 0.0;
        m[2][2] = 1.0;

    }

    /**
     * Multiply current matrix with an external matrix. M = M * a_matrix.
    Result is stored in current matrix.
     * @param a_matrix
     */
    public void mul(final JMatrix3d a_matrix) {
        // compute multiplication between both matrices
        double m00 = m[0][0] * a_matrix.m[0][0] + m[0][1] * a_matrix.m[1][0] + m[0][2] * a_matrix.m[2][0];
        double m01 = m[0][0] * a_matrix.m[0][1] + m[0][1] * a_matrix.m[1][1] + m[0][2] * a_matrix.m[2][1];
        double m02 = m[0][0] * a_matrix.m[0][2] + m[0][1] * a_matrix.m[1][2] + m[0][2] * a_matrix.m[2][2];
        double m10 = m[1][0] * a_matrix.m[0][0] + m[1][1] * a_matrix.m[1][0] + m[1][2] * a_matrix.m[2][0];
        double m11 = m[1][0] * a_matrix.m[0][1] + m[1][1] * a_matrix.m[1][1] + m[1][2] * a_matrix.m[2][1];
        double m12 = m[1][0] * a_matrix.m[0][2] + m[1][1] * a_matrix.m[1][2] + m[1][2] * a_matrix.m[2][2];
        double m20 = m[2][0] * a_matrix.m[0][0] + m[2][1] * a_matrix.m[1][0] + m[2][2] * a_matrix.m[2][0];
        double m21 = m[2][0] * a_matrix.m[0][1] + m[2][1] * a_matrix.m[1][1] + m[2][2] * a_matrix.m[2][1];
        double m22 = m[2][0] * a_matrix.m[0][2] + m[2][1] * a_matrix.m[1][2] + m[2][2] * a_matrix.m[2][2];

        // return values to current matrix
        m[0][0] = m00;
        m[0][1] = m01;
        m[0][2] = m02;
        m[1][0] = m10;
        m[1][1] = m11;
        m[1][2] = m12;
        m[2][0] = m20;
        m[2][1] = m21;
        m[2][2] = m22;

    }

    /**
     * Multiply current matrix with an external matrix.\n
    \e result = \e M * \e matrix. \n
    Result is stored in \e result matrix.
     * @param a_matrix
     * @param a_result
     */
    public final void mulr(final JMatrix3d a_matrix, JMatrix3d a_result) {
        // compute multiplication between both matrices
        a_result.m[0][0] = m[0][0] * a_matrix.m[0][0] + m[0][1] * a_matrix.m[1][0] + m[0][2] * a_matrix.m[2][0];
        a_result.m[0][1] = m[0][0] * a_matrix.m[0][1] + m[0][1] * a_matrix.m[1][1] + m[0][2] * a_matrix.m[2][1];
        a_result.m[0][2] = m[0][0] * a_matrix.m[0][2] + m[0][1] * a_matrix.m[1][2] + m[0][2] * a_matrix.m[2][2];
        a_result.m[1][0] = m[1][0] * a_matrix.m[0][0] + m[1][1] * a_matrix.m[1][0] + m[1][2] * a_matrix.m[2][0];
        a_result.m[1][1] = m[1][0] * a_matrix.m[0][1] + m[1][1] * a_matrix.m[1][1] + m[1][2] * a_matrix.m[2][1];
        a_result.m[1][2] = m[1][0] * a_matrix.m[0][2] + m[1][1] * a_matrix.m[1][2] + m[1][2] * a_matrix.m[2][2];
        a_result.m[2][0] = m[2][0] * a_matrix.m[0][0] + m[2][1] * a_matrix.m[1][0] + m[2][2] * a_matrix.m[2][0];
        a_result.m[2][1] = m[2][0] * a_matrix.m[0][1] + m[2][1] * a_matrix.m[1][1] + m[2][2] * a_matrix.m[2][1];
        a_result.m[2][2] = m[2][0] * a_matrix.m[0][2] + m[2][1] * a_matrix.m[1][2] + m[2][2] * a_matrix.m[2][2];
    }

    /**
     * Multiply current matrix with an external vector passed as parameter. \n
    \e vector = \e M * \e vector. \n
    Result is stored in same vector.
     * @param a_vector
     */
    public final void mul(JVector3d a_vector) {
        // compute multiplication
        double x = m[0][0] * a_vector.getX() + m[0][1] * a_vector.getY() + m[0][2] * a_vector.getZ();
        double y = m[1][0] * a_vector.getX() + m[1][1] * a_vector.getY() + m[1][2] * a_vector.getZ();
        double z = m[2][0] * a_vector.getX() + m[2][1] * a_vector.getY() + m[2][2] * a_vector.getZ();

        // store result
        a_vector.setX(x);
        a_vector.setY(y);
        a_vector.setZ(z);
    }

    /**
     * Multiply current matrix with a vector. \n
    \e result = \e M * \e vector. \n
    Result is stored in result vector \e result.
     *
     * @param a_vector
     * @param a_result
     */
    public final void mulr(final JVector3d a_vector, JVector3d a_result) {
        // compute multiplication
        a_result.setX(m[0][0] * a_vector.getX() + m[0][1] * a_vector.getY() + m[0][2] * a_vector.getZ());
        a_result.setY(m[1][0] * a_vector.getX() + m[1][1] * a_vector.getY() + m[1][2] * a_vector.getZ());
        a_result.setZ(m[2][0] * a_vector.getX() + m[2][1] * a_vector.getY() + m[2][2] * a_vector.getZ());
    }

    /**
     * Compute the determinant of  current matrix.
     *
     * @return
     */
    public final double det() {
        return (+m[0][0] * m[1][1] * m[2][2]
                + m[0][1] * m[1][2] * m[2][0]
                + m[0][2] * m[1][0] * m[2][1]
                - m[2][0] * m[1][1] * m[0][2]
                - m[2][1] * m[1][2] * m[0][0]
                - m[2][2] * m[1][0] * m[0][1]);
    }

    /**
     * Compute the transpose of current matrix.
     */
    public void trans() {
        double t;
        t = m[0][1];
        m[0][1] = m[1][0];
        m[1][0] = t;
        t = m[0][2];
        m[0][2] = m[2][0];
        m[2][0] = t;
        t = m[1][2];
        m[1][2] = m[2][1];
        m[2][1] = t;
    }

    /**
     * Compute the transpose of current matrix. \n
    Result is stored in \e result matrix.


     * @param a_result
     */
    public final void transr(JMatrix3d a_result) {
        a_result.m[0][0] = m[0][0];
        a_result.m[0][1] = m[1][0];
        a_result.m[0][2] = m[2][0];

        a_result.m[1][0] = m[0][1];
        a_result.m[1][1] = m[1][1];
        a_result.m[1][2] = m[2][1];

        a_result.m[2][0] = m[0][2];
        a_result.m[2][1] = m[1][2];
        a_result.m[2][2] = m[2][2];
    }

    /**
     * Compute the inverse of current matrix. \n
    If the operation succeeds, result is stored in current matrix.
     *
     * @return
     */
    public boolean invert() {
        // compute determinant
        double det = (+m[0][0] * m[1][1] * m[2][2]
                + m[0][1] * m[1][2] * m[2][0]
                + m[0][2] * m[1][0] * m[2][1]
                - m[2][0] * m[1][1] * m[0][2]
                - m[2][1] * m[1][2] * m[0][0]
                - m[2][2] * m[1][0] * m[0][1]);

        // check if determinant null
        if ((det < JConstants.CHAI_TINY) && (det > -JConstants.CHAI_TINY)) {
            // determinant null, matrix inversion could not be performed
            return (false);
        } else {

            // compute inverted matrix
            double m00 = (m[1][1] * m[2][2] - m[2][1] * m[1][2]) / det;
            double m01 = -(m[0][1] * m[2][2] - m[2][1] * m[0][2]) / det;
            double m02 = (m[0][1] * m[1][2] - m[1][1] * m[0][2]) / det;

            double m10 = -(m[1][0] * m[2][2] - m[2][0] * m[1][2]) / det;
            double m11 = (m[0][0] * m[2][2] - m[2][0] * m[0][2]) / det;
            double m12 = -(m[0][0] * m[1][2] - m[1][0] * m[0][2]) / det;

            double m20 = (m[1][0] * m[2][1] - m[2][0] * m[1][1]) / det;
            double m21 = -(m[0][0] * m[2][1] - m[2][0] * m[0][1]) / det;
            double m22 = (m[0][0] * m[1][1] - m[1][0] * m[0][1]) / det;

            // return values to current matrix
            m[0][0] = m00;
            m[0][1] = m01;
            m[0][2] = m02;
            m[1][0] = m10;
            m[1][1] = m11;
            m[1][2] = m12;
            m[2][0] = m20;
            m[2][1] = m21;
            m[2][2] = m22;

            // return success
            return (true);
        }
    }

    /**
     *
     * @param a_result
     * @return
     */
    public final JMatrix3d inv(boolean a_result) {
        JMatrix3d result = new JMatrix3d();
        boolean status = invertr(result);
        if (a_result) {
            a_result = status;
        }

        return result;
    }

    /**
     * Compute the inverse of current matrix.
    If the operation succeeds, result is stored in \e result matrix passed
    as parameter.
     * @param a_result
     * @return
     */
    public final boolean invertr(JMatrix3d a_result) {
        // compute determinant
        double det = (+m[0][0] * m[1][1] * m[2][2]
                + m[0][1] * m[1][2] * m[2][0]
                + m[0][2] * m[1][0] * m[2][1]
                - m[2][0] * m[1][1] * m[0][2]
                - m[2][1] * m[1][2] * m[0][0]
                - m[2][2] * m[1][0] * m[0][1]);

        // check if determinant null.
        if ((det < JConstants.CHAI_TINY) && (det > -JConstants.CHAI_TINY)) {
            // determinant null, matrix inversion can not be performed
            return (false);
        } else {

            // compute inverted matrix
            a_result.m[0][0] = (m[1][1] * m[2][2] - m[2][1] * m[1][2]) / det;
            a_result.m[0][1] = -(m[0][1] * m[2][2] - m[2][1] * m[0][2]) / det;
            a_result.m[0][2] = (m[0][1] * m[1][2] - m[1][1] * m[0][2]) / det;

            a_result.m[1][0] = -(m[1][0] * m[2][2] - m[2][0] * m[1][2]) / det;
            a_result.m[1][1] = (m[0][0] * m[2][2] - m[2][0] * m[0][2]) / det;
            a_result.m[1][2] = -(m[0][0] * m[1][2] - m[1][0] * m[0][2]) / det;

            a_result.m[2][0] = (m[1][0] * m[2][1] - m[2][0] * m[1][1]) / det;
            a_result.m[2][1] = -(m[0][0] * m[2][1] - m[2][0] * m[0][1]) / det;
            a_result.m[2][2] = (m[0][0] * m[1][1] - m[1][0] * m[0][1]) / det;

            // return success
            return (true);
        }
    }

    /**
     * Build a rotation matrix defined by a rotation axis and rotation
    angle given in radian. These values are passed as parameters. \n
    Result is stored in current matrix.
     * @param a_axis
     * @param a_angleRad
     * @return
     */
    public boolean set(final JVector3d a_axis, final double a_angleRad) {
        // compute length of axis vector
        double length = a_axis.length();

        // check length of axis vector
        if (length < JConstants.CHAI_TINY) {
            // rotation matrix could not be computed because axis vector is not defined
            return (false);
        }

        // normalize axis vector
        double x = a_axis.getX() / length;
        double y = a_axis.getY() / length;
        double z = a_axis.getZ() / length;

        // compute rotation matrix
        double c = Math.cos(a_angleRad);
        double s = Math.sin(a_angleRad);
        double v = 1 - c;

        m[0][0] = x * x * v + c;
        m[0][1] = x * y * v - z * s;
        m[0][2] = x * z * v + y * s;
        m[1][0] = x * y * v + z * s;
        m[1][1] = y * y * v + c;
        m[1][2] = y * z * v - x * s;
        m[2][0] = x * z * v - y * s;
        m[2][1] = y * z * v + x * s;
        m[2][2] = z * z * v + c;

        // return success
        return (true);
    }

    /**
     * Rotate current matrix around an axis an angle defined as parameters.
     *
     * @param a_axis
     * @param a_angleRad
     * @return
     */
    public boolean rotate(final JVector3d a_axis, final double a_angleRad) {
        // compute length of axis vector
        double length = a_axis.length();

        // check length of axis vector
        if (length < JConstants.CHAI_TINY) {
            // rotation matrix could not be computed because axis vector is not defined
            return (false);
        }

        // normalize axis vector
        double x = a_axis.getX() / length;
        double y = a_axis.getY() / length;
        double z = a_axis.getZ() / length;

        // compute rotation matrix
        double c = Math.cos(a_angleRad);
        double s = Math.sin(a_angleRad);
        double v = 1 - c;

        double m00 = x * x * v + c;
        double m01 = x * y * v - z * s;
        double m02 = x * z * v + y * s;
        double m10 = x * y * v + z * s;
        double m11 = y * y * v + c;
        double m12 = y * z * v - x * s;
        double m20 = x * z * v - y * s;
        double m21 = y * z * v + x * s;
        double m22 = z * z * v + c;

        // compute multiplication between both matrices
        double tm00 = m00 * m[0][0] + m01 * m[1][0] + m02 * m[2][0];
        double tm01 = m00 * m[0][1] + m01 * m[1][1] + m02 * m[2][1];
        double tm02 = m00 * m[0][2] + m01 * m[1][2] + m02 * m[2][2];
        double tm10 = m10 * m[0][0] + m11 * m[1][0] + m12 * m[2][0];
        double tm11 = m10 * m[0][1] + m11 * m[1][1] + m12 * m[2][1];
        double tm12 = m10 * m[0][2] + m11 * m[1][2] + m12 * m[2][2];
        double tm20 = m20 * m[0][0] + m21 * m[1][0] + m22 * m[2][0];
        double tm21 = m20 * m[0][1] + m21 * m[1][1] + m22 * m[2][1];
        double tm22 = m20 * m[0][2] + m21 * m[1][2] + m22 * m[2][2];

        // store new values to current matrix
        m[0][0] = tm00;
        m[0][1] = tm01;
        m[0][2] = tm02;
        m[1][0] = tm10;
        m[1][1] = tm11;
        m[1][2] = tm12;
        m[2][0] = tm20;
        m[2][1] = tm21;
        m[2][2] = tm22;

        // return success
        return (true);
    }

    /**
     * Rotate current matrix around an axis an angle defined as parameters. \n
    Result is stored in \e result matrix.


     * @param a_axis
     * @param a_angleRad
     * @param a_result
     * @return
     */
    public final boolean rotater(final JVector3d a_axis, final double a_angleRad, JMatrix3d a_result) {
        // compute length of axis vector
        double length = a_axis.length();

        // check length of axis vector
        if (length < JConstants.CHAI_TINY) {
            // rotation matrix could not be computed because axis vector is not defined
            return (false);
        }

        // normalize axis vector
        double x = a_axis.getX() / length;
        double y = a_axis.getY() / length;
        double z = a_axis.getZ() / length;

        // compute rotation matrix
        double c = Math.cos(a_angleRad);
        double s = Math.sin(a_angleRad);
        double v = 1 - c;

        double m00 = x * x * v + c;
        double m01 = x * y * v - z * s;
        double m02 = x * z * v + y * s;
        double m10 = x * y * v + z * s;
        double m11 = y * y * v + c;
        double m12 = y * z * v - x * s;
        double m20 = x * z * v - y * s;
        double m21 = y * z * v + x * s;
        double m22 = z * z * v + c;

        // compute multiplication between both matrices
        a_result.m[0][0] = m00 * m[0][0] + m01 * m[1][0] + m02 * m[2][0];
        a_result.m[0][1] = m00 * m[0][1] + m01 * m[1][1] + m02 * m[2][1];
        a_result.m[0][2] = m00 * m[0][2] + m01 * m[1][2] + m02 * m[2][2];
        a_result.m[1][0] = m10 * m[0][0] + m11 * m[1][0] + m12 * m[2][0];
        a_result.m[1][1] = m10 * m[0][1] + m11 * m[1][1] + m12 * m[2][1];
        a_result.m[1][2] = m10 * m[0][2] + m11 * m[1][2] + m12 * m[2][2];
        a_result.m[2][0] = m20 * m[0][0] + m21 * m[1][0] + m22 * m[2][0];
        a_result.m[2][1] = m20 * m[0][1] + m21 * m[1][1] + m22 * m[2][1];
        a_result.m[2][2] = m20 * m[0][2] + m21 * m[1][2] + m22 * m[2][2];

        // return success
        return (true);
    }

    /**
     * Convert current matrix into a string.


     * @param a_string
     * @param a_precision
     */
    public final void str(String a_string, final int a_precision) {
        a_string.concat("[ ");
        for (int i = 0; i < 3; i++) {
            a_string.concat("( ");
            for (int j = 0; j < 3; j++) {
                JString.jStr(a_string, m[j][i], a_precision);
                if (j < 2) {
                    a_string.concat(", ");

                }
            }
            a_string.concat(" ) ");
        }
        a_string.concat("]");
    }

    /**
     * Convert current matrix into a string, which is returned on the stack.
     *
     * @param a_precision
     * @return
     */
    public final String str(final int a_precision) {
        String a_string = new String();
        str(a_string, a_precision);
        return a_string;
    }

    /**
     * Print the current matrix using the CHAI_DEBUG_PRINT macro.
     *
     * @param a_precision
     */
    public final void print(final int a_precision) {
        String s = new String();
        str(s, a_precision);
        JGlobals.CHAI_DEBUG_PRINT("%s\n", s);
    }

    /**
     * Compare two matrices. Return \b true if both matrices are equal,
    otherwise return \b false.
     *
     * @param a_matrix
     * @return
     */
    public final boolean equals(JMatrix3d a_matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (a_matrix.m[i][j] != m[i][j]) {
                    return (false);
                }
            }
        }

        return (true);
    }

    /**
     * Convert the rotation to an angle axis
     *
     * @param a_angle
     * @param a_axis
     * @return
     */
    public boolean toAngleAxis(double a_angle, JVector3d a_axis) {
        final double epsilon = 0.01;
        if ((m[0][1] - m[1][0] < epsilon) && (m[0][1] - m[1][0] > -epsilon)
                && (m[0][2] - m[2][0] < epsilon) && (m[0][2] - m[2][0] > -epsilon)
                && (m[1][2] - m[2][1] < epsilon) && (m[1][2] - m[2][1] > -epsilon)) {
            // rotation of 0 or pi
            if ((m[0][1] + m[1][0] < epsilon) && (m[0][1] + m[1][0] > -epsilon)
                    && (m[0][2] + m[2][0] < epsilon) && (m[0][2] + m[2][0] > -epsilon)
                    && (m[1][2] + m[2][1] < epsilon) && (m[1][2] + m[2][1] > -epsilon)) {
                // Matrix is identity matrix
                a_angle = 0;
                // axis is arbitrary
                a_axis.setX(1.0);
                a_axis.setY(0.0);
                a_axis.setZ(0.0);
            } else {
                // angle is pi
                a_angle = JConstants.CHAI_PI;

                a_axis.setX((m[0][0] + 1.0) / 2.0);

                if (a_axis.getX() > 0) {
                    a_axis.setX(Math.sqrt(a_axis.getX()));
                } else {

                    // invalid matrix
                    return false;
                }


                a_axis.setY((m[1][1] + 1.0) / 2.0);
                if (a_axis.getY() > 0) {
                    a_axis.setY(Math.sqrt(a_axis.getY()));
                } else {

                    // invalid matrix
                    return false;
                }

                a_axis.setZ((m[2][2] + 1.0) / 2.0);
                if (a_axis.getZ() > 0) {
                    a_axis.setZ(Math.sqrt(a_axis.getZ()));
                } else {

                    // invalid matrix
                    return false;
                }

                boolean xIsZero = (a_axis.getX() < epsilon && a_axis.getX() > -epsilon);
                boolean yIsZero = (a_axis.getY() < epsilon && a_axis.getY() > -epsilon);
                boolean zIsZero = (a_axis.getZ() < epsilon && a_axis.getZ() > -epsilon);
                boolean xyIsPositive = (m[0][1] > 0.0);
                boolean xzIsPositive = (m[0][2] > 0.0);
                boolean yzIsPositive = (m[1][2] > 0.0);
                if (xIsZero && !yIsZero && !zIsZero) {

                    if (!yzIsPositive) {
                        a_axis.setY(-a_axis.getY());
                    }

                } else if (yIsZero && !zIsZero) {

                    if (!xzIsPositive) {
                        a_axis.setZ(-a_axis.getZ());
                    }

                } else if (zIsZero) {

                    if (!xyIsPositive) {
                        a_axis.setX(-a_axis.getX());
                    }
                }


            }
        } else {
            double sinthetamag = Math.sqrt((m[2][1] - m[1][2]) * (m[2][1] - m[1][2])
                    + (m[0][2] - m[2][0]) * (m[0][2] - m[2][0])
                    + (m[1][0] - m[0][1]) * (m[1][0] - m[0][1]));

            a_angle = Math.acos((m[0][0] + m[1][1] + m[2][2] - 1.0) / 2.0);
            a_axis.setX((m[2][1] - m[1][2]) / sinthetamag);
            a_axis.setY((m[0][2] - m[2][0]) / sinthetamag);
            a_axis.setZ((m[1][0] - m[0][1]) / sinthetamag);

        }
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 3; i++) {
            s += "[";
            for (int j = 0; j < 3; j++) {
                s += m[i][j];
                if (j < 2) {
                    s += ",";
                }

            }
            s += "]\n";

        }
        return s;
    }
}
