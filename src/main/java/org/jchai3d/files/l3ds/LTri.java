/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LTri {
    int a;
    int b;
    int c;
    long smoothingGroups;
    LVector3 normal;
    LVector3 tangent;
    LVector3 binormal;
    int materialId;

    public LTri() {
        a = b = c = 0;
        smoothingGroups = 0;
        materialId = 0;
        normal = new LVector3();
        tangent = new LVector3();
        binormal = new LVector3();
    }
    
    
}
