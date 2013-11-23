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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JString;
import org.jchai3d.math.JVector3d;

/**
 *
 *  CHAI describes rotations using 3x3 rotation matrices (cMatrix3d)
and 3D vectors (cVector3d) to express position or translation.
On the OpenGL side 4x4 matrices are required to perform all
geometric transformations. cMatrixGL provides a structure
which encapsulates all the necessary functionality to generate 4x4
OpenGL transformation matrices from 3D position vectors and rotation
matrices. \n

cMatrixGL also provides OpenGL calls to push, multiply and pop
matrices off the OpenGL stack. \n

Note that OpenGL Matrices are COLUMN major, but CHAI matrices
(and all other matrices in the universe) are ROW major.

 *
 * @author Jairo
 * @author Marcos
 */
public class JMatrixGL {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    //! array of type \e double, defining the actual transformation
    private double[][] m = new double[4][4];

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    public JMatrixGL() {
        identity();
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Returns a pointer to the matrix array in memory.
     * @return
     */
    public final double[] pMatrix() {

        return toArray();
    }

    /**
     * Creates OpenGL translation matrix from a position vector passed as
    parameter.
     * @param aPos
     */
    public void set(final JVector3d aPos) {
        m[0][0] = 1.0;
        m[0][1] = 0.0;
        m[0][2] = 0.0;
        m[0][3] = 0.0;
        m[1][0] = 0.0;
        m[1][1] = 1.0;
        m[1][2] = 0.0;
        m[1][3] = 0.0;
        m[2][0] = 0.0;
        m[2][1] = 0.0;
        m[2][2] = 1.0;
        m[2][3] = 0.0;
        m[3][0] = aPos.getX();
        m[3][1] = aPos.getY();
        m[3][2] = aPos.getZ();
        m[3][3] = 1.0;
    }

    /**
     * Extract the translational component of this matrix.
     * @return
     */
    public final JVector3d getPos() {
        return new JVector3d(m[3][0], m[3][1], m[3][2]);
    }

    /**
     * Extract the rotational component of this matrix.
     * @return
     */
    public final JMatrix3d getRot() {
        JMatrix3d mat = new JMatrix3d();
        mat.set(m[0][0], m[1][0], m[2][0],
                m[0][1], m[1][1], m[2][1],
                m[0][2], m[1][2], m[2][2]);
        return mat;
    }

    /**
     * Create an OpenGL rotation matrix from a 3x3 rotation matrix passed
    as a parameter.
     * @param aRot
     */
    public void set(final JMatrix3d aRot) {
        m[0][0] = aRot.m[0][0];
        m[0][1] = aRot.m[1][0];
        m[0][2] = aRot.m[2][0];
        m[0][3] = 0.0;
        m[1][0] = aRot.m[0][1];
        m[1][1] = aRot.m[1][1];
        m[1][2] = aRot.m[2][1];
        m[1][3] = 0.0;
        m[2][0] = aRot.m[0][2];
        m[2][1] = aRot.m[1][2];
        m[2][2] = aRot.m[2][2];
        m[2][3] = 0.0;
        m[3][0] = 0.0;
        m[3][1] = 0.0;
        m[3][2] = 0.0;
        m[3][3] = 1.0;
    }

    /**
     * Create an OpenGL translation matrix from a 3-vector and a 3x3 matrix
    passed as  a parameter.
     * @param aPos
     * @param aRot
     */
    public void set(final JVector3d aPos, final JMatrix3d aRot) {
        m[0][0] = aRot.m[0][0];
        m[0][1] = aRot.m[1][0];
        m[0][2] = aRot.m[2][0];
        m[0][3] = 0.0;
        m[1][0] = aRot.m[0][1];
        m[1][1] = aRot.m[1][1];
        m[1][2] = aRot.m[2][1];
        m[1][3] = 0.0;
        m[2][0] = aRot.m[0][2];
        m[2][1] = aRot.m[1][2];
        m[2][2] = aRot.m[2][2];
        m[2][3] = 0.0;
        m[3][0] = aPos.getX();
        m[3][1] = aPos.getY();
        m[3][2] = aPos.getZ();
        m[3][3] = 1.0;
    }

    /**
     * Copy the current matrix to an external matrix passed as a parameter.
     * @param aDestination
     */
    public final void copyto(JMatrixGL aDestination) {
        aDestination.m[0][0] = m[0][0];
        aDestination.m[0][1] = m[0][1];
        aDestination.m[0][2] = m[0][2];
        aDestination.m[0][3] = m[0][3];
        aDestination.m[1][0] = m[1][0];
        aDestination.m[1][1] = m[1][1];
        aDestination.m[1][2] = m[1][2];
        aDestination.m[1][3] = m[1][3];
        aDestination.m[2][0] = m[2][0];
        aDestination.m[2][1] = m[2][1];
        aDestination.m[2][2] = m[2][2];
        aDestination.m[2][3] = m[2][3];
        aDestination.m[3][0] = m[3][0];
        aDestination.m[3][1] = m[3][1];
        aDestination.m[3][2] = m[3][2];
        aDestination.m[3][3] = m[3][3];
    }

    /**
     * copy values from an external matrix passed as parameter to this matrix.
     * @param aSource
     */
    public void copyfrom(final JMatrixGL aSource) {
        m[0][0] = aSource.m[0][0];
        m[0][1] = aSource.m[0][1];
        m[0][2] = aSource.m[0][2];
        m[0][3] = aSource.m[0][3];
        m[1][0] = aSource.m[1][0];
        m[1][1] = aSource.m[1][1];
        m[1][2] = aSource.m[1][2];
        m[1][3] = aSource.m[1][3];
        m[2][0] = aSource.m[2][0];
        m[2][1] = aSource.m[2][1];
        m[2][2] = aSource.m[2][2];
        m[2][3] = aSource.m[2][3];
        m[3][0] = aSource.m[3][0];
        m[3][1] = aSource.m[3][1];
        m[3][2] = aSource.m[3][2];
        m[3][3] = aSource.m[3][3];
    }

    /**
     * Set this matrix to be equal to the identity matrix.
     */
    public void identity() {
        m[0][0] = 1.0;
        m[0][1] = 0.0;
        m[0][2] = 0.0;
        m[0][3] = 0.0;
        m[1][0] = 0.0;
        m[1][1] = 1.0;
        m[1][2] = 0.0;
        m[1][3] = 0.0;
        m[2][0] = 0.0;
        m[2][1] = 0.0;
        m[2][2] = 1.0;
        m[2][3] = 0.0;
        m[3][0] = 0.0;
        m[3][1] = 0.0;
        m[3][2] = 0.0;
        m[3][3] = 1.0;
    }

    /**
     * Left-multiply the current matrix by an external matrix passed as
    a parameter.  That is, compute: \n

    \e this = \e aMatrix * \e this \n

    Remember that all matrices are column-major.  That's why the following
    code looks like right-multiplication...

     * @param aMatrix
     */
    public void mul(final JMatrixGL aMatrix) {
        // compute multiplication between both matrices
        double m00 = m[0][0] * aMatrix.m[0][0] + m[0][1] * aMatrix.m[1][0]
                + m[0][2] * aMatrix.m[2][0] + m[0][3] * aMatrix.m[3][0];
        double m01 = m[0][0] * aMatrix.m[0][1] + m[0][1] * aMatrix.m[1][1]
                + m[0][2] * aMatrix.m[2][1] + m[0][3] * aMatrix.m[3][1];
        double m02 = m[0][0] * aMatrix.m[0][2] + m[0][1] * aMatrix.m[1][2]
                + m[0][2] * aMatrix.m[2][2] + m[0][3] * aMatrix.m[3][2];
        double m03 = m[0][0] * aMatrix.m[0][3] + m[0][1] * aMatrix.m[1][3]
                + m[0][2] * aMatrix.m[2][3] + m[0][3] * aMatrix.m[3][3];

        double m10 = m[1][0] * aMatrix.m[0][0] + m[1][1] * aMatrix.m[1][0]
                + m[1][2] * aMatrix.m[2][0] + m[1][3] * aMatrix.m[3][0];
        double m11 = m[1][0] * aMatrix.m[0][1] + m[1][1] * aMatrix.m[1][1]
                + m[1][2] * aMatrix.m[2][1] + m[1][3] * aMatrix.m[3][1];
        double m12 = m[1][0] * aMatrix.m[0][2] + m[1][1] * aMatrix.m[1][2]
                + m[1][2] * aMatrix.m[2][2] + m[1][3] * aMatrix.m[3][2];
        double m13 = m[1][0] * aMatrix.m[0][3] + m[1][1] * aMatrix.m[1][3]
                + m[1][2] * aMatrix.m[2][3] + m[1][3] * aMatrix.m[3][3];

        double m20 = m[2][0] * aMatrix.m[0][0] + m[2][1] * aMatrix.m[1][0]
                + m[2][2] * aMatrix.m[2][0] + m[2][3] * aMatrix.m[3][0];
        double m21 = m[2][0] * aMatrix.m[0][1] + m[2][1] * aMatrix.m[1][1]
                + m[2][2] * aMatrix.m[2][1] + m[2][3] * aMatrix.m[3][1];
        double m22 = m[2][0] * aMatrix.m[0][2] + m[2][1] * aMatrix.m[1][2]
                + m[2][2] * aMatrix.m[2][2] + m[2][3] * aMatrix.m[3][2];
        double m23 = m[2][0] * aMatrix.m[0][3] + m[2][1] * aMatrix.m[1][3]
                + m[2][2] * aMatrix.m[2][3] + m[2][3] * aMatrix.m[3][3];

        double m30 = m[3][0] * aMatrix.m[0][0] + m[3][1] * aMatrix.m[1][0]
                + m[3][2] * aMatrix.m[2][0] + m[3][3] * aMatrix.m[3][0];
        double m31 = m[3][0] * aMatrix.m[0][1] + m[3][1] * aMatrix.m[1][1]
                + m[3][2] * aMatrix.m[2][1] + m[3][3] * aMatrix.m[3][1];
        double m32 = m[3][0] * aMatrix.m[0][2] + m[3][1] * aMatrix.m[1][2]
                + m[3][2] * aMatrix.m[2][2] + m[3][3] * aMatrix.m[3][2];
        double m33 = m[3][0] * aMatrix.m[0][3] + m[3][1] * aMatrix.m[1][3]
                + m[3][2] * aMatrix.m[2][3] + m[3][3] * aMatrix.m[3][3];

        // return values to current matrix
        m[0][0] = m00;
        m[0][1] = m01;
        m[0][2] = m02;
        m[0][3] = m03;
        m[1][0] = m10;
        m[1][1] = m11;
        m[1][2] = m12;
        m[1][3] = m13;
        m[2][0] = m20;
        m[2][1] = m21;
        m[2][2] = m22;
        m[2][3] = m23;
        m[3][0] = m30;
        m[3][1] = m31;
        m[3][2] = m32;
        m[3][3] = m33;
    }

    /**
     * Left-multiply the current matrix by an external matrix passed as
    a parameter, storing the result externally.  That is, compute: \n

    \e aResult = \e a_matrix * \e this;

    Remember that all matrices are column-major.  That's why the following
    code looks like right-multiplication...
     * @param aMatrix
     * @param aResult
     */
    public final void mulr(final JMatrix3d aMatrix, JMatrix3d aResult) {
        // compute multiplication between both matrices
        aResult.m[0][0] = m[0][0] * aMatrix.m[0][0] + m[0][1] * aMatrix.m[1][0] + m[0][2] * aMatrix.m[2][0];
        aResult.m[0][1] = m[0][0] * aMatrix.m[0][1] + m[0][1] * aMatrix.m[1][1] + m[0][2] * aMatrix.m[2][1];
        aResult.m[0][2] = m[0][0] * aMatrix.m[0][2] + m[0][1] * aMatrix.m[1][2] + m[0][2] * aMatrix.m[2][2];
        aResult.m[1][0] = m[1][0] * aMatrix.m[0][0] + m[1][1] * aMatrix.m[1][0] + m[1][2] * aMatrix.m[2][0];
        aResult.m[1][1] = m[1][0] * aMatrix.m[0][1] + m[1][1] * aMatrix.m[1][1] + m[1][2] * aMatrix.m[2][1];
        aResult.m[1][2] = m[1][0] * aMatrix.m[0][2] + m[1][1] * aMatrix.m[1][2] + m[1][2] * aMatrix.m[2][2];
        aResult.m[2][0] = m[2][0] * aMatrix.m[0][0] + m[2][1] * aMatrix.m[1][0] + m[2][2] * aMatrix.m[2][0];
        aResult.m[2][1] = m[2][0] * aMatrix.m[0][1] + m[2][1] * aMatrix.m[1][1] + m[2][2] * aMatrix.m[2][1];
        aResult.m[2][2] = m[2][0] * aMatrix.m[0][2] + m[2][1] * aMatrix.m[1][2] + m[2][2] * aMatrix.m[2][2];
    }

    /**
     * Transpose this matrix.
     */
    public void trans() {
        double t;

        t = m[0][1];
        m[0][1] = m[1][0];
        m[1][0] = t;
        t = m[0][2];
        m[0][2] = m[2][0];
        m[2][0] = t;
        t = m[0][3];
        m[0][3] = m[3][0];
        m[3][0] = t;
        t = m[1][2];
        m[1][2] = m[2][1];
        m[2][1] = t;
        t = m[1][3];
        m[1][3] = m[3][1];
        m[3][1] = t;
        t = m[2][3];
        m[2][3] = m[3][2];
        m[3][2] = t;
    }

    /**
     * Transpose this matrix and store the result in a_result.
     * @param aResult
     */
    public final void transr(JMatrixGL aResult) {
        aResult.m[0][0] = m[0][0];
        aResult.m[0][1] = m[1][0];
        aResult.m[0][2] = m[2][0];
        aResult.m[0][3] = m[3][0];

        aResult.m[1][0] = m[0][1];
        aResult.m[1][1] = m[1][1];
        aResult.m[1][2] = m[2][1];
        aResult.m[1][3] = m[3][1];

        aResult.m[2][0] = m[0][2];
        aResult.m[2][1] = m[1][2];
        aResult.m[2][2] = m[2][2];
        aResult.m[2][3] = m[3][2];

        aResult.m[3][0] = m[0][3];
        aResult.m[3][1] = m[1][3];
        aResult.m[3][2] = m[2][3];
        aResult.m[3][3] = m[3][3];
    }

    /**
     * Create a frustum matrix, as defined by the glFrustum function.
     *
     * @param l
     * @param r
     * @param b
     * @param t
     * @param n
     * @param f
     */
    public void buildFrustumMatrix(double l, double r, double b, double t,
            double n, double f) {
        m[0][0] = (2.0 * n) / (r - l);
        m[0][1] = 0.0;
        m[0][2] = 0.0;
        m[0][3] = 0.0;

        m[1][0] = 0.0;
        m[1][1] = (2.0 * n) / (t - b);
        m[1][2] = 0.0;
        m[1][3] = 0.0;

        m[2][0] = (r + l) / (r - l);
        m[2][1] = (t + b) / (t - b);
        m[2][2] = -(f + n) / (f - n);
        m[2][3] = -1.0;

        m[3][0] = 0.0;
        m[3][1] = 0.0;
        m[3][2] = -(2.0 * f * n) / (f - n);
        m[3][3] = 0.0;
    }

    private static void SWAP_ROWS(double[] a, double[] b) {
        double[] tmp = a;
        a = b;
        b = tmp;
    }

    /**
     * Invert this matrix.
     *
     * @return
     */
    public boolean invert() {

        double[] mat = toArray();

        double[][] wtmp = new double[4][8];
        double m0, m1, m2, m3, s;
        double[] r0 = new double[3];
        double[] r1 = new double[3];
        double[] r2 = new double[3];
        double[] r3 = new double[3];

        r0 = wtmp[0];
        r1 = wtmp[1];
        r2 = wtmp[2];
        r3 = wtmp[3];




        r0[0] = m[0][0];
        r0[1] = m[0][1];
        r0[2] = m[0][2];
        r0[3] = m[0][3];
        r0[4] = 1.0;
        r0[5] = 0.0;
        r0[6] = 0.0;
        r0[7] = 0.0;








        r1[0] = m[1][0];
        r1[1] = m[1][1];
        r1[2] = m[1][2];
        r1[3] = m[1][3];
        r1[4] = 0.0;
        r1[5] = 1.0;
        r1[6] = 0.0;
        r1[7] = 0.0;




        r2[0] = m[2][0];
        r2[1] = m[2][1];
        r2[2] = m[2][2];
        r2[3] = m[2][3];
        r2[4] = 0.0;
        r2[5] = 0.0;
        r2[6] = 1.0;
        r2[7] = 0.0;

        r3[0] = m[3][0];
        r3[1] = m[3][1];
        r3[2] = m[3][2];
        r3[3] = m[3][3];
        r3[4] = 0.0;
        r3[5] = 0.0;
        r3[6] = 0.0;
        r3[7] = 1.0;


        // choose pivot






        if (Math.abs(r3[0]) > Math.abs(r2[0])) {
            SWAP_ROWS(r3, r2);
        }
        if (Math.abs(r2[0]) > Math.abs(r1[0])) {
            SWAP_ROWS(r2, r1);
        }
        if (Math.abs(r1[0]) > Math.abs(r0[0])) {
            SWAP_ROWS(r1, r0);
        }
        if (0.0 == r0[0]) {
            return false;
        }

        // eliminate first variable




        m1 = r1[0] / r0[0];
        m2 = r2[0] / r0[0];
        m3 = r3[0] / r0[0];
        s = r0[1];
        r1[1] -= m1 * s;
        r2[1] -= m2 * s;
        r3[1] -= m3 * s;
        s = r0[2];
        r1[2] -= m1 * s;
        r2[2] -= m2 * s;
        r3[2] -= m3 * s;
        s = r0[3];
        r1[3] -= m1 * s;
        r2[3] -= m2 * s;
        r3[3] -= m3 * s;
        s = r0[4];

        if (s != 0.0) {
            r1[4] -= m1 * s;
            r2[4] -= m2 * s;
            r3[4] -= m3 * s;
        }
        s = r0[5];

        if (s != 0.0) {
            r1[5] -= m1 * s;
            r2[5] -= m2 * s;
            r3[5] -= m3 * s;
        }
        s = r0[6];

        if (s != 0.0) {
            r1[6] -= m1 * s;
            r2[6] -= m2 * s;
            r3[6] -= m3 * s;
        }
        s = r0[7];

        if (s != 0.0) {
            r1[7] -= m1 * s;
            r2[7] -= m2 * s;
            r3[7] -= m3 * s;
        }

        // choose pivot





        if (Math.abs(r3[1]) > Math.abs(r2[1])) {
            SWAP_ROWS(r3, r2);
        }
        if (Math.abs(r2[1]) > Math.abs(r1[1])) {
            SWAP_ROWS(r2, r1);
        }
        if (0.0 == r1[1]) {
            return false;
        }

        // eliminate second variable







        m2 = r2[1] / r1[1];
        m3 = r3[1] / r1[1];
        r2[2] -= m2 * r1[2];
        r3[2] -= m3 * r1[2];
        r2[3] -= m2 * r1[3];
        r3[3] -= m3 * r1[3];
        s = r1[4];
        if (0.0 != s) {
            r2[4] -= m2 * s;
            r3[4] -= m3 * s;
        }
        s = r1[5];
        if (0.0 != s) {
            r2[5] -= m2 * s;
            r3[5] -= m3 * s;
        }
        s = r1[6];
        if (0.0 != s) {
            r2[6] -= m2 * s;
            r3[6] -= m3 * s;
        }
        s = r1[7];
        if (0.0 != s) {
            r2[7] -= m2 * s;
            r3[7] -= m3 * s;
        }

        // choose pivot




        if (Math.abs(r3[2]) > Math.abs(r2[2])) {
            SWAP_ROWS(r3, r2);
        }
        if (0.0 == r2[2]) {
            return false;
        }

        // eliminate third variable
        m3 = r3[2] / r2[2];


        r3[3] -= m3 * r2[3];
        r3[4] -= m3 * r2[4];
        r3[5] -= m3 * r2[5];
        r3[6] -= m3 * r2[6];
        r3[7] -= m3 * r2[7];

        // last check
        if (0.0 == r3[3]) {

            return false;
        }

        s = 1.0 / r3[3];

        r3[4] *= s;
        r3[5] *= s;
        r3[6] *= s;
        r3[7] *= s;

        m2 = r2[3];
        s = 1.0 / r2[2];


        r2[4] = s * (r2[4] - r3[4] * m2);
        r2[5] = s * (r2[5] - r3[5] * m2);
        r2[6] = s * (r2[6] - r3[6] * m2);
        r2[7] = s * (r2[7] - r3[7] * m2);
        m1 = r1[3];


        r1[4] -= r3[4] * m1;
        r1[5] -= r3[5] * m1;
        r1[6] -= r3[6] * m1;
        r1[7] -= r3[7] * m1;
        m0 = r0[3];


        r0[4] -= r3[4] * m0;
        r0[5] -= r3[5] * m0;
        r0[6] -= r3[6] * m0;
        r0[7] -= r3[7] * m0;

        m1 = r1[2];
        s = 1.0 / r1[1];


        r1[4] = s * (r1[4] - r2[4] * m1);
        r1[5] = s * (r1[5] - r2[5] * m1);
        r1[6] = s * (r1[6] - r2[6] * m1);
        r1[7] = s * (r1[7] - r2[7] * m1);
        m0 = r0[2];


        r0[4] -= r2[4] * m0;
        r0[5] -= r2[5] * m0;
        r0[6] -= r2[6] * m0;
        r0[7] -= r2[7] * m0;

        m0 = r0[1];
        s = 1.0 / r0[0];


        r0[4] = s * (r0[4] - r1[4] * m0);
        r0[5] = s * (r0[5] - r1[5] * m0);
        r0[6] = s * (r0[6] - r1[6] * m0);
        r0[7] = s * (r0[7] - r1[7] * m0);









        m[0][0] = r0[4];
        m[0][1] = r0[5];
        m[0][2] = r0[6];
        m[0][3] = r0[7];

        m[1][0] = r1[4];
        m[1][1] = r1[5];
        m[1][2] = r1[6];
        m[1][3] = r1[7];

        m[2][0] = r2[4];
        m[2][1] = r2[5];
        m[2][2] = r2[6];
        m[2][3] = r2[7];

        m[3][0] = r3[4];
        m[3][1] = r3[5];
        m[3][2] = r3[6];
        m[3][3] = r3[7];

        return true;
    }

    /**
     * Build a perspective matrix, according to the gluPerspective function.
     * @param fovy
     * @param aspect
     * @param zNear
     * @param zFar
     */
    public void buildPerspectiveMatrix(double fovy, double aspect,
            double zNear, double zFar) {
        double xMin, xMax, yMin, yMax;

        yMax = zNear * Math.tan(fovy * JConstants.CHAI_PI / 360.0);
        yMin = -yMax;

        xMin = yMin * aspect;
        xMax = yMax * aspect;

        buildFrustumMatrix(xMin, xMax, yMin, yMax, zNear, zFar);
    }

    /**
     * Build a 4x4 matrix transform, according to the gluLookAt function.
     * @param eyex
     * @param eyey
     * @param eyez
     * @param centerx
     * @param centery
     * @param centerz
     * @param upx
     * @param upy
     * @param upz
     */
    public void buildLookAtMatrix(double eyex, double eyey, double eyez,
            double centerx, double centery, double centerz,
            double upx, double upy, double upz) {
        double[] x = new double[3];
        double[] y = new double[3];
        double[] z = new double[3];

        double mag;

        // create rotation matrix

        // Z vector
        z[0] = eyex - centerx;
        z[1] = eyey - centery;
        z[2] = eyez - centerz;
        mag = Math.sqrt(z[0] * z[0] + z[1] * z[1] + z[2] * z[2]);
        if (mag > 0) {  /* mpichler, 19950515 */
            z[0] /= mag;
            z[1] /= mag;
            z[2] /= mag;
        }

        // Y vector
        y[0] = upx;
        y[1] = upy;
        y[2] = upz;

        // X vector = Y cross Z
        x[0] = y[1] * z[2] - y[2] * z[1];
        x[1] = -y[0] * z[2] + y[2] * z[0];
        x[2] = y[0] * z[1] - y[1] * z[0];

        // Recompute Y = Z cross X
        y[0] = z[1] * x[2] - z[2] * x[1];
        y[1] = -z[0] * x[2] + z[2] * x[0];
        y[2] = z[0] * x[1] - z[1] * x[0];


        // Normalize
        mag = Math.sqrt(x[0] * x[0] + x[1] * x[1] + x[2] * x[2]);
        if (mag > 0) {
            x[0] /= mag;
            x[1] /= mag;
            x[2] /= mag;
        }

        mag = Math.sqrt(y[0] * y[0] + y[1] * y[1] + y[2] * y[2]);
        if (mag > 0) {
            y[0] /= mag;
            y[1] /= mag;
            y[2] /= mag;
        }

        m[0][0] = x[0];
        m[1][0] = x[1];
        m[2][0] = x[2];
        m[3][0] = -x[0] * eyex + -x[1] * eyey + -x[2] * eyez;
        m[0][1] = y[0];
        m[1][1] = y[1];
        m[2][1] = y[2];
        m[3][1] = -y[0] * eyex + -y[1] * eyey + -y[2] * eyez;
        m[0][2] = z[0];
        m[1][2] = z[1];
        m[2][2] = z[2];
        m[3][2] = -z[0] * eyex + -z[1] * eyey + -z[2] * eyez;
        m[0][3] = 0.0;
        m[1][3] = 0.0;
        m[2][3] = 0.0;
        m[3][3] = 1.0;
    }

    /**
     * Build a 4x4 matrix transform, according to the gluLookAt function.
     * @param aEye
     * @param aLookAt
     * @param aUp
     */
    public void buildLookAtMatrix(JVector3d aEye, JVector3d aLookAt, JVector3d aUp) {
        buildLookAtMatrix(aEye.getX(), aEye.getY(), aEye.getZ(),
                aLookAt.getX(), aLookAt.getY(), aLookAt.getZ(),
                aUp.getX(), aUp.getY(), aUp.getZ());
    }

    /**
     * Push the current OpenGL matrix stack.
     */
    public void glMatrixPush() {
        GLContext.getCurrent().getGL().getGL2().glPushMatrix();
    }

    /**
     * Load the current OpenGL matrix with this JMatrixGL matrix.
     */
    public void glMatrixLoad() {
        GLContext.getCurrent().getGL().getGL2().glLoadMatrixd(pMatrix(), 0);
    }

    /**
     * Multiply the current OpenGL matrix with this JMatrixGL matrix.
     */
    public void glMatrixMultiply() {
        GLContext.getCurrent().getGL().getGL2().glMultMatrixd(pMatrix(), 0);
    }

    /**
     * Push the current OpenGL matrix stack and multiply with this JMatrixGL
    matrix.
     */
    public void glMatrixPushMultiply() {
        GLContext.getCurrent().getGL().getGL2().glPushMatrix();
        GLContext.getCurrent().getGL().getGL2().glMultMatrixd(pMatrix(), 0);
    }

    /**
     * Pop current OpenGL matrix off the stack.
     */
    public void glMatrixPop() {
        GLContext.getCurrent().getGL().getGL2().glPopMatrix();
    }

    /**
     * Convert the current matrix into an string.
     * @param aString
     * @param aPrecision
     */
    public void str(String aString, int aPrecision) {
        aString.concat("[ ");
        for (int i = 0; i < 4; i++) {
            aString.concat("( ");
            for (int j = 0; j < 4; j++) {
                JString.jStr(aString, m[j][i], aPrecision);
                if (j < 3) {
                    aString.concat(", ");
                }
            }
            aString.concat(" ) ");
        }
        aString.concat("]");
    }

    public double[] toArray() {
        double[] k = new double[16];
        k[0] = m[0][0];
        k[1] = m[0][1];
        k[2] = m[0][2];
        k[3] = m[0][3];

        k[4] = m[1][0];
        k[5] = m[1][1];
        k[6] = m[1][2];
        k[7] = m[1][3];

        k[8] = m[2][0];
        k[9] = m[2][1];
        k[10] = m[2][2];
        k[11] = m[2][3];

        k[12] = m[3][0];
        k[13] = m[3][1];
        k[14] = m[3][2];
        k[15] = m[3][3];
        return k;
    }

    public static void jLookAt(GL2 gl, JVector3d eye, JVector3d at, JVector3d up) {
        // Define our look vector (z axis)
        JVector3d look = at.operatorSub(eye);
        look.normalize();

        // Define our new x axis
        JVector3d xaxis = JMaths.jCross(look, up);
        xaxis.normalize();

        // Define our new y axis as the cross of the x and z axes
        JVector3d upv = JMaths.jCross(xaxis, look);

        // Turn around the z axis
        look.mul(-1.0);

        // Put it all into a GL-friendly matrix
        double[] dm = new double[16];
        dm[0] = xaxis.getX();
        dm[1] = xaxis.getY();
        dm[2] = xaxis.getZ();
        dm[3] = 0.f;
        dm[4] = upv.getX();
        dm[5] = upv.getY();
        dm[6] = upv.getZ();
        dm[7] = 0.f;
        dm[8] = look.getX();
        dm[9] = look.getY();
        dm[10] = look.getZ();
        dm[11] = 0.f;
        dm[12] = eye.getX();
        dm[13] = eye.getY();
        dm[14] = eye.getZ();
        dm[15] = 1.f;

        // Push it onto the matrix stack
        gl.glMultMatrixd(dm, 0);
    }
}
