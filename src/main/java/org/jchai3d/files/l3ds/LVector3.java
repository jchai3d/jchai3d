/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LVector3 {

    float x;
    float y;
    float z;

    public LVector3() {
        x = y = z = 0;
    }

    public LVector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static LVector3 _4to3(LVector4 vec) {

        LVector3 t = new LVector3();
        t.x = vec.x;
        t.y = vec.y;
        t.z = vec.z;
        return t;
    }

//---------------------------------------------------------------------------
    public static LVector3 addVectors(final LVector3 a, final LVector3 b) {
        LVector3 t = new LVector3();
        t.x = a.x + b.x;
        t.y = a.y + b.y;
        t.z = a.z + b.z;
        return t;
    }

//---------------------------------------------------------------------------
    public static LVector3 subtractVectors(final LVector3 a, final LVector3 b) {
        LVector3 t = new LVector3();
        t.x = a.x - b.x;
        t.y = a.y - b.y;
        t.z = a.z - b.z;
        return t;
    }

//---------------------------------------------------------------------------
    public static float vectorLength(final LVector3 vec) {
        return (float) Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }

    public static LVector3 normalizeVector(final LVector3 vec) {
        float a = vectorLength(vec);
        if (a == 0) {
            return vec;
        }
        float b = 1 / a;
        LVector3 v = new LVector3();
        v.x = vec.x * b;
        v.y = vec.y * b;
        v.z = vec.z * b;
        return v;
    }

//---------------------------------------------------------------------------
    public static LVector3 crossProduct(final LVector3 a, final LVector3 b) {
        LVector3 v = new LVector3();
        v.x = a.y * b.z - a.z * b.y;
        v.y = a.z * b.x - a.x * b.z;
        v.z = a.x * b.y - a.y * b.x;
        return v;
    }
}
