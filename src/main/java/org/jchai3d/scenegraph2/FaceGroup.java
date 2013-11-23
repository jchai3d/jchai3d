package org.jchai3d.scenegraph2;

/**
 * The FaceGroup object can be used to define different appearances for different
 * parts of the same geometry.
 * 
 * @author Marcos da Silva Ramos
 */
public class FaceGroup {
   
    /**
     * The indices of the faces of this group.
     */
    int[] faceIndices;
    
    /**
     * The appearance of this group.
     */
    Appearance groupAppearance;
    
    /**
     * A reference to the owner of this group.
     */
    Mesh mesh;
    
    
    
}
