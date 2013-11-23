/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LMatrix4 {

    float _11, _12, _13, _14;
    float _21, _22, _23, _24;
    float _31, _32, _33, _34;
    float _41, _42, _43, _44;

    public LMatrix4() {
        loadIdentityMatrix(this);
    }

    public static void loadIdentityMatrix(LMatrix4 m) {
        m._11 = 1.0f;
        m._12 = 0.0f;
        m._13 = 0.0f;
        m._14 = 0.0f;

        m._21 = 0.0f;
        m._22 = 1.0f;
        m._23 = 0.0f;
        m._24 = 0.0f;

        m._31 = 0.0f;
        m._32 = 0.0f;
        m._33 = 1.0f;
        m._34 = 0.0f;

        m._41 = 0.0f;
        m._42 = 0.0f;
        m._43 = 1.0f;
        m._44 = 0.0f;
    }
}
