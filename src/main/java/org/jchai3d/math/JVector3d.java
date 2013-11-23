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

/**
 * This vector class provides storage for a 3 dimensional double precision
 * floating point vector as well as simple floating point arithmetic operations
 *
 * @author Jairo
 * @author Marcos
 */
public class JVector3d implements Cloneable {

    //! Component X of vector.
    public double x;
    //! Component Y of vector.
    public double y;
    //! Component Z of vector.
    public double z;

    public double get(int axis) {
        switch (axis) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                return 0.000;
        }
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Default constructor. The new vector will point to (0,0,0)
     */
    public JVector3d() {
        zero();
    }

    /**
     * Constructor by passing a string to initialize vector.
     *
     * @param a_initstr
     */
    public JVector3d(final String a_initstr) {
        String[] tmp = a_initstr.trim().split(" ");
        x = (Double.parseDouble(tmp[0]));
        y = (Double.parseDouble(tmp[1]));
        z = (Double.parseDouble(tmp[2]));
    }

    /**
     * Constructor by passing a string to initialize vector.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     */
    public JVector3d(final double a_x, final double a_y,
            final double a_z) {
        x = a_x;
        y = a_y;
        z = a_z;
    }

    /**
     * Constructor by passing a vector to initialize vector.
     *
     * @param a_source
     */
    public JVector3d(final JVector3d a_source) {
        x = a_source.x;
        y = a_source.y;
        z = a_source.z;
    }

    /**
     * Constructor by passing an array of doubles to initialize vector.
     *
     * @param a_in
     */
    public JVector3d(final double[] a_in) {
        x = a_in[0];
        y = a_in[1];
        z = a_in[2];
    }

    /**
     * Constructor by passing an array of floats to initialize vector.
     *
     * @param a_in
     */
    public JVector3d(final float[] a_in) {
        x = a_in[0];
        y = a_in[1];
        z = a_in[2];
    }

    public double[] toArray() {
        return new double[]{x, y, z};
    }

    /**
     * Clear vector with zeros.
     */
    public void zero() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    /**
     * Initialize 3 dimensional vector with parameters x, y, and z.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     */
    public void set(final double a_x, final double a_y, final double a_z) {
        x = a_x;
        y = a_y;
        z = a_z;
    }

    /**
     * Initialize by string separate with " "
     * 
     * @param a_initstr 
     */
    public void set(final String a_initstr) {

        String[] tmp = a_initstr.trim().split(" ");
        x = (Double.parseDouble(tmp[0]));
        y = (Double.parseDouble(tmp[1]));
        z = (Double.parseDouble(tmp[2]));
    }

    /**
     * Copy current vector to external vector as parameter.
     *
     * @param a_destination
     */
    public final void copyTo(JVector3d a_destination) {
        a_destination.x = x;
        a_destination.y = y;
        a_destination.z = z;
    }

    /**
     * Copy external vector as parameter to current vector.
     *
     * @param a_source
     */
    public void copyFrom(final JVector3d a_source) {
        x = a_source.x;
        y = a_source.y;
        z = a_source.z;
    }

    /**
     * Addition between current vector and external vector passed as parameter.
     * Result is stored in current vector.
     *
     * @param a_vector
     */
    public void add(final JVector3d a_vector) {
        x = x + a_vector.x;
        y = y + a_vector.y;
        z = z + a_vector.z;
    }

    /**
     * Addition between current vector and external vector passed as parameter.
     * Result is stored in current vector.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     */
    public void add(final double a_x, final double a_y, final double a_z) {
        x = x + a_x;
        y = y + a_y;
        z = z + a_z;
    }

    /**
     * Addition between current vector and external vector passed as parameter.
     * Result is stored in external result vector.
     *
     * @param a_vector
     * @param a_result
     */
    public final void addr(final JVector3d a_vector, JVector3d a_result) {
        a_result.x = x + a_vector.x;
        a_result.y = y + a_vector.y;
        a_result.z = z + a_vector.z;
    }

    /**
     * Addition between current vector and vector passed by parameter. Result is
     * stored in result vector.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     * @param a_result
     */
    public final void addr(final double a_x, final double a_y, final double a_z, JVector3d a_result) {
        a_result.x = x + a_x;
        a_result.y = y + a_y;
        a_result.z = z + a_z;
    }

    /**
     * Subtraction between current vector and an external vector passed as
     * parameter. Result is stored in current vector.
     *
     * @param a_vector
     */
    public void sub(final JVector3d a_vector) {
        x = x - a_vector.x;
        y = y - a_vector.y;
        z = z - a_vector.z;
    }

    /**
     * Subtract an external vector passed as parameter from current vector.
     * Result is stored in current vector.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     */
    public void sub(final double a_x, final double a_y, final double a_z) {
        x = x - a_x;
        y = y - a_y;
        z = z - a_z;
    }

    /**
     * Subtraction between current vector and external vector passed as
     * parameter. Result is stored in external a_result vector.
     *
     * @param a_vector
     * @param a_result
     */
    public final void subr(final JVector3d a_vector, JVector3d a_result) {
        a_result.x = x - a_vector.x;
        a_result.y = y - a_vector.y;
        a_result.z = z - a_vector.z;
    }

    /**
     * Subtract current vector from vector passed by parameter. Result is stored
     * in result vector.
     *
     * @param a_x
     * @param a_y
     * @param a_z
     * @param a_result
     */
    public final void subr(final double a_x, final double a_y, final double a_z,
            JVector3d a_result) {
        a_result.x = x - a_x;
        a_result.y = y - a_y;
        a_result.z = z - a_z;
    }

    /**
     * Multiply current vector by a scalar. Result is stored in current vector.
     *
     * @param a_scalar
     */
    public void mul(final double a_scalar) {
        x = a_scalar * x;
        y = a_scalar * y;
        z = a_scalar * z;
    }

    /**
     * Multiply current vector by a scalar. Result is stored in \e result
     * vector.
     *
     * @param a_scalar
     * @param a_result
     */
    public final void mulr(final double a_scalar, JVector3d a_result) {
        a_result.x = a_scalar * x;
        a_result.y = a_scalar * y;
        a_result.z = a_scalar * z;
    }

    /**
     * Divide current vector by a scalar. No check for divide-by-zero is
     * performed.
     *
     * @param a_scalar
     */
    public void div(final double a_scalar) {
        x = x / a_scalar;
        y = y / a_scalar;
        z = z / a_scalar;
    }

    /**
     * Divide current vector by a scalar. Result is stored in \e result vector.
     *
     * @param a_scalar
     * @param a_result
     */
    public final void divr(final double a_scalar, JVector3d a_result) {
        a_result.x = x / a_scalar;
        a_result.y = y / a_scalar;
        a_result.z = z / a_scalar;
    }

    /**
     * Negate current vector. Result is stored in \e result vector.
     */
    public void negate() {
        x = -x;
        y = -y;
        z = -z;
    }

    /**
     * Negate current vector. Result is stored in \e result vector.
     *
     * @param a_result
     */
    public final void negater(JVector3d a_result) {
        a_result.x = -x;
        a_result.y = -y;
        a_result.z = -z;
    }

    /**
     * Compute the cross product between current vector and an external vector.
     * Result is stored in current vector.
     *
     * @param a_vector
     */
    public void cross(final JVector3d a_vector) {
        // compute cross product
        double a = (y * a_vector.z) - (z * a_vector.y);
        double b = -(x * a_vector.z) + (z * a_vector.x);
        double c = (x * a_vector.y) - (y * a_vector.x);

        // store result in current vector
        x = a;
        y = b;
        z = c;
    }

    /**
     * Compute the cross product between current vector and an external vector
     * passed as parameter. \n
     *
     * Result is returned. Performance-wise, cross() and crossr() are usually
     * preferred, since this version creates a new stack variable.
     *
     * @param a_vector
     * @return
     */
    public final JVector3d crossAndReturn(final JVector3d a_vector) {
        JVector3d r = new JVector3d();
        crossr(a_vector, r);
        return (r);
    }

    /**
     * Compute the cross product between current vector and an external vector
     * passed as parameter. Result is stored in a_result vector.
     *
     * @param a_vector
     * @param a_result
     */
    public final void crossr(final JVector3d a_vector, JVector3d a_result) {
        a_result.x = (y * a_vector.z) - (z * a_vector.y);
        a_result.y = -(x * a_vector.z) + (z * a_vector.x);
        a_result.z = (x * a_vector.y) - (y * a_vector.x);
    }

    /**
     * Compute the dot product between current vector and an external vector
     * passed as parameter.
     *
     * @param a_vector
     * @return
     */
    public final double dot(final JVector3d a_vector) {
        return ((x * a_vector.x) + (y * a_vector.y) + (z * a_vector.z));
    }

    /**
     * Compute the element-by-element product between current vector and an
     * external vector and store in the current vector.
     *
     * @param a_vector
     */
    public void elementMul(final JVector3d a_vector) {
        x *= a_vector.x;
        y *= a_vector.y;
        z *= a_vector.z;
    }

    /**
     * Compute the element-by-element product between current vector and an
     * external vector and store in the supplied output vector.
     *
     * @param a_vector
     * @param a_result
     */
    public final void elementMulr(final JVector3d a_vector, JVector3d a_result) {
        a_result.x = x * a_vector.x;
        a_result.y = y * a_vector.y;
        a_result.z = z * a_vector.z;
    }

    /**
     * Compute the length of current vector.
     *
     * @return
     */
    public final double length() {
        return (Math.sqrt((x * x) + (y * y) + (z * z)));
    }

    /**
     * Compute the square length of current vector.
     *
     * @return
     */
    public final double lengthsq() {
        return ((x * x) + (y * y) + (z * z));
    }

    /**
     * Normalize current vector to become a vector of length one. Warning:
     * Vector should not be equal to (0,0,0) or a division by zero error will
     * occur.
     */
    public void normalize() {
        // compute length of vector
        double length = Math.sqrt((x * x) + (y * y) + (z * z));

        // divide current vector by its length
        x = x / length;
        y = y / length;
        z = z / length;
    }

    /**
     * Normalize current vector to become a vector of length one. \n WARNING:
     * Vector should not be equal to (0,0,0) or a division by zero error will
     * occur. Result is stored in \e result vector.
     *
     * @param a_result
     */
    public final void normalizer(JVector3d a_result) {
        // compute length of vector
        double length = Math.sqrt((x * x) + (y * y) + (z * z));

        // divide current vector by its length
        a_result.x = x / length;
        a_result.y = y / length;
        a_result.z = z / length;
    }

    /**
     * Compute the distance between current point and an external point passed
     * as parameter.
     *
     * @param a_vector
     * @return
     */
    public final double distance(final JVector3d a_vector) {
        // compute distance between both points
        double dx = x - a_vector.x;
        double dy = y - a_vector.y;
        double dz = z - a_vector.z;

        // return result
        return (Math.sqrt(dx * dx + dy * dy + dz * dz));
    }

    /**
     * Compute the square distance between the current point and an external
     * point.
     *
     * @param a_vector
     * @return
     */
    public final double distancesq(final JVector3d a_vector) {
        // compute distance between both points
        double dx = x - a_vector.x;
        double dy = y - a_vector.y;
        double dz = z - a_vector.z;

        // return result
        return (dx * dx + dy * dy + dz * dz);
    }

    /**
     * Test whether the current vector and an external vector are equal.
     *
     * @param a_vector
     * @param epsilon
     * @return
     */
    public boolean equals(final JVector3d a_vector, final double epsilon) {
        // Accelerated path for exact equality
        if (epsilon == 0.0) {
            if ((x == a_vector.x) && (y == a_vector.y) && (z == a_vector.z)) {
                return (true);
            } else {
                return (false);
            }
        }

        if ((Math.abs(a_vector.x - x) < epsilon)
                && (Math.abs(a_vector.y - y) < epsilon)
                && (Math.abs(a_vector.z - z) < epsilon)) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Convert current vector into a string.
     *
     * @param a_string
     * @param a_precision
     */
    public final void str(String a_string, final int a_precision) {
        a_string.concat("( ");
        //cStr(a_string, x, a_precision);
        a_string.concat(", ");
        //cStr(a_string, y, a_precision);
        a_string.concat(", ");
        //cStr(a_string, z, a_precision);
        a_string.concat(" )");
    }

    /**
     * Convert current vector into a string, which is returned on the stack.
     *
     * @param a_precision
     * @return
     */
    public final String str(final int a_precision) {
        String a_string = new String();
        str(a_string, a_precision);
        return (a_string);
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

    /**
     * Decompose me into components parallel and perpendicular to the input
     * vector.
     *
     * @param a_input
     * @param a_parallel
     * @param a_perpendicular
     */
    public void decompose(final JVector3d a_input, JVector3d a_parallel, JVector3d a_perpendicular) {
        double scale = (dot(a_input) / (a_input.dot(a_input)));
        a_parallel = a_input;
        a_parallel.mul(scale);
        subr(a_parallel, a_perpendicular);
    }

    public void slerp(double a_level, final JVector3d a_vector1, JVector3d a_vector2) {
        // a_vector2 is passed in by value so that we may scale it
        double a_vec1lensq = a_vector1.lengthsq();
        double cosomega = a_vector1.dot(a_vector2) / (a_vec1lensq);
        if ((cosomega - 1.0) > -1e-4 && (cosomega - 1.0) < 1e-4) {
            // vectors are (almost) parallel
            // linearly interpolate
            //*this = a_vector1;
            this.mul(1.0 - a_level);
            a_vector2.mul(a_level);
            //this.operator +=(a_vector2);
            this.mul(Math.sqrt(a_vec1lensq / this.lengthsq()));
        } else {
            if (cosomega < 0.0) {
                cosomega = -cosomega;
                a_vector2.negate();
            }
            double ratio1, ratio2;
            if ((cosomega + 1.0) > -1e-4 && (cosomega + 1.0) < 1e-4) {
                // vectors are 180 degrees apart
                // there is no unique path between them
                if ((a_vector1.x < a_vector1.y) && (a_vector1.x < a_vector1.z)) {
                    // x component is the smallest
                    a_vector2.x = 0;
                    a_vector2.y = -a_vector1.z;
                    a_vector2.z = a_vector1.y;
                } else if (a_vector1.y < a_vector1.z) {
                    // y component is the smallest
                    a_vector2.x = -a_vector1.z;
                    a_vector2.y = 0;
                    a_vector2.z = a_vector1.x;
                } else {
                    // z component is the smallest
                    a_vector2.x = -a_vector1.y;
                    a_vector2.y = a_vector1.x;
                    a_vector2.z = 0;
                }
                // scale it so it is the same length as before
                a_vector2.mul(Math.sqrt(a_vec1lensq / a_vector2.lengthsq()));

                ratio1 = Math.sin(JConstants.CHAI_PI * (0.5 - a_level));
                ratio2 = Math.sin(JConstants.CHAI_PI * a_level);
            } else {
                double omega = Math.acos(cosomega);
                double sinomega = Math.sin(omega);
                ratio1 = Math.sin(omega * (1.0 - a_level)) / sinomega;
                ratio2 = Math.sin(omega * a_level) / sinomega;
            }
            //*this = a_vector1;
            this.mul(ratio1);
            a_vector2.mul(ratio2);
            this.add(a_vector2);
        }
    }

    /*
     * !
     * Cast to a double*
     *
     * This replaces the previous [] operators (without breaking existing code).
     */
    //-----------------------------------------------------------------------
    //! operator []
    //double& operator[] (unsigned int index) { return (&x)[index]; }
    //! operator []
    //double operator[] (unsigned int index) const { return ((double*)(&x))[index]; }
    /**
     * /= operator for vector/scalar division
     *
     * @param a_val
     */
    public void operatorDiv(final double a_val) {
        x /= a_val;
        y /= a_val;
        z /= a_val;
    }

    /**
     * operator for vector/scalar multiplication
     *
     * @param a_val
     */
    public void operatorMul(final double a_val) {
        x *= a_val;
        y *= a_val;
        z *= a_val;
    }

    /**
     * operator for vector/vector addition
     *
     * @param a_input
     */
    public void operatorAdd(final double a_val) {
        x += a_val;
        y += a_val;
        z += a_val;
    }

    /**
     * operator for vector/vector subtraction
     *
     * @param a_input
     */
    public void operatorSub(final double a_val) {
        x -= a_val;
        y -= a_val;
        z -= a_val;
    }

    /**
     * operator for vector/scalar multiplication.
     *
     * @param v
     * @param a_input
     * @return
     */
    public JVector3d operatorMul(final JVector3d v, final double a_input) {
        return (new JVector3d(v.x * a_input, v.y * a_input, v.z * a_input));
    }

    /**
     * operator for vector/scalar division.
     *
     * @param v
     * @param a_input
     * @return
     */
    public JVector3d operatorDiv(final JVector3d v, final double a_input) {
        return (new JVector3d(v.x / a_input, v.y / a_input, v.z / a_input));
    }

    /**
     * operator for vector division.
     *
     * @param v
     * @return
     */
    public JVector3d operatorDiv(final JVector3d v) {
        return (new JVector3d(x / v.x, y / v.y, z / v.z));
    }

    /**
     * operator for vector division.
     *
     * @param v
     * @return
     */
    public double div(final JVector3d v) {
        return x / v.x + y / v.y + z / v.z;
    }

    /**
     * operator for scalar/vector multiplication.
     *
     * @param a_input
     * @param v
     * @return
     */
    public JVector3d operatorMul(final double a_input, final JVector3d v) {
        return (new JVector3d(v.x * a_input, v.y * a_input, v.z * a_input));
    }

    /**
     * operator for vector multiplication.
     *
     * @param v
     * @return
     */
    public JVector3d operatorMul(final JVector3d v) {
        return (new JVector3d(x * v.x, y * v.y, z * v.z));
    }

    /**
     * operator for vector/vector addition.
     *
     * @param v1
     * @param v2
     * @return
     */
    public JVector3d operatorAdd(final JVector3d v1, final JVector3d v2) {
        return (new JVector3d(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z));
    }

    /**
     * operator for vector/vector addition.
     *
     * @param v
     * @return
     */
    public JVector3d operatorAdd(final JVector3d v) {
        return (new JVector3d(x + v.x, y + v.y, z + v.z));
    }

    /**
     * operator for vector/vector subtraction.
     *
     * @param v1
     * @param v2
     * @return
     */
    public JVector3d operatorSub(final JVector3d v1, final JVector3d v2) {
        return (new JVector3d(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z));
    }

    /**
     * operator for vector/vector subtraction.
     *
     * @param v
     * @return
     */
    public JVector3d operatorSub(final JVector3d v) {
        return (new JVector3d(x - v.x, y - v.y, z - v.z));
    }

    /**
     * operator for vector/vector dotting
     *
     * @param v1
     * @param v2
     * @return
     */
    public double operatorMul(final JVector3d v1, final JVector3d v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public void set(int index, double value) {
        switch (index) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            case 2:
                z = value;
                break;
        }
    }

    //---------------------------------------------------------------------------
    /*
     * !
     * ostream operator. \n Outputs the vector's components separated by commas.
     */
    //---------------------------------------------------------------------------
    /*
     * static inline std::ostream &operator << (std::ostream &a_os, cVector3d
     * const& a_vec) { a_os << a_vec.x << ", " << a_vec.y << ", " << a_vec.z;
     * return a_os;
    }
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new JVector3d(this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JVector3d)) {
            return false;
        }
        JVector3d v = (JVector3d) obj;
        return x == v.x && y == v.y && z == v.z;
    }
}

/**
 * JRay3d represents a 3D vector with an origin.
 *
 * @author T315443
 */
class JRay3d {
    //! Vector representing ray origin.

    JVector3d m_origin;
    //! Unit vector representing ray direction.
    JVector3d m_direction;

    //! Constructor of JRay3d.
    JRay3d() {
    }

    //! This constructor assumes that a_direction is normalized already.
    JRay3d(final JVector3d a_origin, final JVector3d a_direction) {
        this.m_origin = m_origin;
        this.m_direction = a_direction;
    }
}

/**
 * JSegment3d represents a line segment with a start and an end.
 *
 * @author T315443
 */
class JSegment3d {

    //! Start point of segment.
    JVector3d m_start;
    //! End point of segment
    JVector3d m_end;

    /**
     * Returns the squared distance from this segment to a_point.
     *
     * @param a_point
     * @param a_t
     * @param a_closestPoint
     * @return
     */
    double distanceSquaredToPoint(final JVector3d a_point,
            double a_t,
            JVector3d a_closestPoint) {

        double mag = JMaths.jDistance(m_start, m_end);

        // Project this point onto the line
        a_point.operatorSub(m_start).operatorMul(m_end.operatorSub(m_start)).div(mag * mag);

        //a_t = (a_point - m_start) * (m_end - m_start) / (mag * mag);

        // Clip to segment endpoints
        if (a_t < 0.0) {
            a_t = 0.0;
        } else if (a_t > 1.0) {
            a_t = 1.0;
        }

        // Find the intersection point
        //JVector3d intersection = m_start + a_t * (m_end - m_start);
        JVector3d intersection = m_start.operatorMul(m_end.operatorSub(m_start));
        if (a_closestPoint != null) {
            a_closestPoint = intersection;
        }

        // Compute distance
        return JMaths.jDistanceSq(a_point, intersection);
    }

    /**
     * Constructor of cSegment3d.
     *
     * @param a_start
     * @param a_end
     */
    JSegment3d(final JVector3d a_start, final JVector3d a_end) {
        m_start = a_start;
        m_end = a_end;
    }
}