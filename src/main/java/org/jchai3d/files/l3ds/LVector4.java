/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LVector4 {

    float x;
    float y;
    float z;
    float w;

    public LVector4() {
    }

    public LVector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static LVector4 vectorByMatrix(final LMatrix4 m, final LVector4 vec) {
        LVector4 res = new LVector4();
        res.x = m._11 * vec.x + m._12 * vec.y + m._13 * vec.z + m._14 * vec.w;
        res.y = m._21 * vec.x + m._22 * vec.y + m._23 * vec.z + m._24 * vec.w;
        res.z = m._31 * vec.x + m._32 * vec.y + m._33 * vec.z + m._34 * vec.w;
        res.w = m._41 * vec.x + m._42 * vec.y + m._43 * vec.z + m._44 * vec.w;
        if (res.w != 0) {
            float b = 1 / res.w;
            res.x *= b;
            res.y *= b;
            res.z *= b;
            res.w = 1;
        } else {
            res.w = 1;
        }

        return res;
    }
}
