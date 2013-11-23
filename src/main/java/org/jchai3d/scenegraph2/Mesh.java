package org.jchai3d.scenegraph2;

import org.jchai3d.math.JVector3d;
import org.jchai3d.math.geom.Geometry;

/**
 *
 * The Mesh class represents a triangle mesh.
 * 
 * @author Marcos da Silva Ramos
 */
public class Mesh extends Node{

    /**
     * The geometry of this
     */
    protected Geometry geometry;
    
    /**
     * 
     */
    protected Appearance appearance;
    
    /**
     * 
     */
    protected FaceGroup[] faceGroup;
    
    
    /**
     * This method perform a uniform scale along x, y and z axis of this
     * mesh geometry.
     * 
     * @param scaleFactor the multiplier factor
     */
    public void scale(double scaleFactor) {
        scale(scaleFactor, scaleFactor, scaleFactor);
    }
    
    /**
     * This method performs a non-uniform scale in the x, y and z axis for
     * all points of all vertices of this mesh geometry. Also, update the bounds
     * of this mesh.
     * @param xScale the multiplier for x axis
     * @param yScale the multiplier for y axis
     * @param zScale the multiplier for z axis
     */
    public void scale(double xScale, double yScale, double zScale) {
        JVector3d scale = new JVector3d(xScale, yScale, zScale);
        int n = geometry.getPointCount();
        for(int i=0; i<n; i++) {
            geometry.getPoint(i).elementMul(scale);
        }
        
        //TODO: update bounds
    }

    @Override
    public void init() {
        
    }
}