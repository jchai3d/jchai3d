/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LTriangle2 {

    LVector4 vertices[];
    LVector3 vertexNormals[];
    LVector2 textureCoords[];
    LVector3 faceNormal;
    LColor3 color[];
    int materialId;

    public LTriangle2() {
        vertices = new LVector4[3];
        vertexNormals = new LVector3[3];
        textureCoords = new LVector2[3];
        color = new LColor3[3];
        materialId = -1;
        faceNormal = new LVector3();
    }
}
