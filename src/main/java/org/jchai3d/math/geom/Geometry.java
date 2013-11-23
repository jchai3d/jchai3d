/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.math.geom;

import org.jchai3d.graphics.JColorf;
import org.jchai3d.math.JVector3d;
import org.jchai3d.math.TexCoord2d;
import org.jchai3d.math.TexCoord3d;
import org.jchai3d.math.TexCoord4d;
import org.jchai3d.math.TriangleVertex;

/**
 *
 * @author Marcos da Silva Ramos
 */
public abstract class Geometry {

    /**
     * Normals data present in this geometry?
     */
    protected boolean normalsPresent;
    /**
     * Vertex colors present in this geometry?
     */
    protected boolean colorsPresent;
    /**
     * If true, 2D texture coordinates present in this geometry.
     */
    protected boolean texCoords2DPresent;
    /**
     * If true, 3D texture coordinates present in this geometry.
     */
    protected boolean texCoords3DPresent;
    /**
     * If true, 4D texture coordinates present in this geometry.
     */
    protected boolean texCoords4DPresent;

    /**
     * Creates a new empty geometry, with no normals, colors, or texture coords.
     */
    public Geometry() {
        this.normalsPresent = false;
        this.colorsPresent = false;
        this.texCoords2DPresent = false;
        this.texCoords3DPresent = false;
        this.texCoords4DPresent = false;
    }

    /**
     * @return the normalsPresent
     */
    public boolean isNormalsPresent() {
        return normalsPresent;
    }

    /**
     * @param normalsPresent the normalsPresent to set
     */
    public void setNormalsPresent(boolean normalsPresent) {
        this.normalsPresent = normalsPresent;
    }

    /**
     * @return the colorsPresent
     */
    public boolean isColorsPresent() {
        return colorsPresent;
    }

    /**
     * @param colorsPresent the colorsPresent to set
     */
    public void setColorsPresent(boolean colorsPresent) {
        this.colorsPresent = colorsPresent;
    }

    /**
     * @return the texCoords2DPresent
     */
    public boolean isTexCoords2DPresent() {
        return texCoords2DPresent;
    }

    /**
     * @param texCoords2DPresent the texCoords2DPresent to set
     */
    public void setTexCoords2DPresent(boolean texCoords2DPresent) {
        this.texCoords2DPresent = texCoords2DPresent;
    }

    /**
     * @return the texCoords3DPresent
     */
    public boolean isTexCoords3DPresent() {
        return texCoords3DPresent;
    }

    /**
     * @param texCoords3DPresent the texCoords3DPresent to set
     */
    public void setTexCoords3DPresent(boolean texCoords3DPresent) {
        this.texCoords3DPresent = texCoords3DPresent;
    }

    /**
     * @return the texCoords4DPresent
     */
    public boolean isTexCoords4DPresent() {
        return texCoords4DPresent;
    }

    /**
     * @param texCoords4DPresent the texCoords4DPresent to set
     */
    public void setTexCoords4DPresent(boolean texCoords4DPresent) {
        this.texCoords4DPresent = texCoords4DPresent;
    }

    public abstract void setVertexCount(int count);
    
    public abstract void setPointCount(int count);

    public abstract void setNormalsCount(int count);

    public abstract void setColorsCount(int count);

    public abstract void setTexCoords2DCount(int count);

    public abstract void setTexCoords3DCount(int count);

    public abstract void setTexCoords4DCount(int count);

    public abstract int getVertexCount();
    
    public abstract int getPointCount();

    public abstract int getNormalCount();

    public abstract int getTexCoord2DCount();

    public abstract int getTexCoord3DCount();

    public abstract int getTexCoord4DCount();
    
    public abstract void setVertices(TriangleVertex[] vertices);
    
    public abstract void setPoints(JVector3d[] points);
    
    public abstract void setNormals(JVector3d[] normals);
    
    public abstract void setColors(JColorf[] colors);
    
    public abstract void setTexCoords2D(TexCoord2d[] coords);
    
    public abstract void setTexCoords3D(TexCoord3d[] coords);
    
    public abstract void setTexCoords4D(TexCoord4d[] coords);

    public abstract int addVertex(TriangleVertex vertex);
    
    public abstract int addVertex(int point, int normal, int color, int texture);
    
    public abstract int addPoint(JVector3d point);

    public abstract int addPoint(double x, double y, double z);

    public abstract int addNormal(JVector3d normal);

    public abstract int addNormal(double x, double y, double z);

    public abstract int addColor(JColorf color);

    public abstract int addColor(float r, float g, float b, float alpha);

    public abstract int addTexCoord2D(TexCoord2d coord);

    public abstract int addTexCoord2D(float s, float t);

    public abstract int addTexCoord3D(TexCoord3d coord);

    public abstract int addTexCoord3D(float s, float t, float r);

    public abstract int addTexCoord4D(TexCoord4d coord);

    public abstract int addTexCoord4D(float s, float t, float r, float q);

    public abstract void setVertex(int index, TriangleVertex vertex);

    public abstract void setVertex(int index, int point, int normal, int color, int texCoord);
    
    public abstract void setPoint(int index, JVector3d point);

    public abstract void setPoint(int index, double x, double y, double z);

    public abstract void setNormal(int index, JVector3d normal);

    public abstract void setNormal(int index, double x, double y, double z);

    public abstract void setColor(int index, JColorf color);

    public abstract void setColor(int index, float r, float g, float b, float alpha);

    public abstract void setTexCoord2D(int index, TexCoord2d coord);
    
    public abstract void setTexCoord2D(int index, double s, double t);

    public abstract void setTexCoord3D(int index, TexCoord3d coord);
    
    public abstract void setTexCoord3D(int index, double s, double t, double r);

    public abstract void setTexCoord4D(int index, TexCoord4d coord);
    
    public abstract void setTexCoord4D(int index, double s, double t, double r, double q);

    public abstract TriangleVertex getVertex(int index);

    public abstract int[] getVertexAsArray(int index);
    
    public abstract JVector3d getPoint(int index);

    public abstract double[] getPointAsArray(int index);

    public abstract JVector3d getNormal(int index);

    public abstract double[] getNormalAsArray(int index);

    public abstract JColorf getColor(int index);

    public abstract float[] getColorAsArray(int index);

    public abstract TexCoord2d getTexCoord2D(int index);

    public abstract double[] getTexCoord2DAsrray(int index);

    public abstract TexCoord3d getTexCoord3D(int index);

    public abstract double[] getTexCoord3DAsrray(int index);

    public abstract TexCoord4d getTexCoord4D(int index);

    public abstract double[] getTexCoord4DAsrray(int index);
}
