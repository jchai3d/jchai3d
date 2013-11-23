package org.jchai3d.math;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class TriangleVertex {

    int pointIndex;
    int normalIndex;
    int colorIndex;
    int texCoordIndex;

    /**
     * Creates a new triangle vertex with all indices set as -1;
     */
    public TriangleVertex() {
        this(-1, -1, -1, -1);
    }

    /**
     * Creates a new triangle vertex with given point, normal, color and texture
     * coordinate indexes.
     *
     * @param pointIndex the index of a point in my geometry
     * @param normalIndex the index of a normal in my geometry
     * @param colorIndex the index of a color in my geometry
     * @param texCoordIndex the index of a tex coord in my geometry
     */
    public TriangleVertex(int pointIndex, int normalIndex, int colorIndex, int texCoordIndex) {
        this.pointIndex = pointIndex;
        this.normalIndex = normalIndex;
        this.colorIndex = colorIndex;
        this.texCoordIndex = texCoordIndex;
    }
    
    public int[] toArray() {
        return new int[]{pointIndex,normalIndex,colorIndex,texCoordIndex};
    }
}
