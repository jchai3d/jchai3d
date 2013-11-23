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
 * The GeometryArray class stores vertex, normals, colors and texture coords in
 * traditional arrays.
 *
 * @author Marcos da Silva Ramos
 */
public class GeometryArray extends Geometry {

    protected TriangleVertex[] vertices;
    protected int vertexIndex;
    protected JVector3d[] points;
    protected int pointIndex;
    protected JVector3d[] normals;
    protected int normalIndex;
    protected JColorf[] colors;
    protected int colorIndex;
    protected TexCoord2d[] texCoords2D;
    protected int texCoord2DIndex;
    protected TexCoord3d[] texCoords3D;
    protected int texCoord3DIndex;
    protected TexCoord4d[] texCoords4D;
    protected int texCoord4DIndex;

    @Override
    public void setPointCount(int count) {
        points = new JVector3d[count];
        pointIndex = 0;
    }

    @Override
    public void setNormalsCount(int count) {
        normals = new JVector3d[count];
        normalIndex = 0;
    }

    @Override
    public void setColorsCount(int count) {
        colors = new JColorf[count];
        colorIndex = 0;
    }

    @Override
    public void setTexCoords2DCount(int count) {
        texCoords2D = new TexCoord2d[count];
        texCoord2DIndex = 0;
    }

    @Override
    public void setTexCoords3DCount(int count) {
        texCoords3D = new TexCoord3d[count];
        texCoord3DIndex = 0;
    }

    @Override
    public void setTexCoords4DCount(int count) {
        texCoords4D = new TexCoord4d[count];
        texCoord4DIndex = 0;
    }

    @Override
    public int getPointCount() {
        return points.length;
    }

    @Override
    public int getNormalCount() {
        return normals.length;
    }

    @Override
    public int getTexCoord2DCount() {
        return texCoords2D.length;
    }

    @Override
    public int getTexCoord3DCount() {
        return texCoords3D.length;
    }

    @Override
    public int getTexCoord4DCount() {
        return texCoords4D.length;
    }

    @Override
    public int addPoint(JVector3d vertex) {
        if (pointIndex < points.length) {
            points[pointIndex] = vertex;
            return pointIndex++;
        }
        return -1;
    }

    @Override
    public int addPoint(double x, double y, double z) {
        return addPoint(new JVector3d(x, y, z));
    }

    @Override
    public int addNormal(JVector3d normal) {
        if (normalIndex < normals.length) {
            normals[normalIndex] = normal;
            return normalIndex++;
        }
        return -1;
    }

    @Override
    public int addNormal(double x, double y, double z) {
        return addNormal(new JVector3d(x, y, z));
    }

    @Override
    public int addColor(JColorf color) {
        if (colorIndex < colors.length) {
            colors[colorIndex] = color;
            return colorIndex++;
        }
        return -1;
    }

    @Override
    public int addColor(float r, float g, float b, float alpha) {
        return addColor((new JColorf(r, g, b, alpha)));
    }

    @Override
    public int addTexCoord2D(TexCoord2d coord) {
        if (texCoord2DIndex < texCoords2D.length) {
            texCoords2D[texCoord2DIndex] = coord;
            return texCoord2DIndex++;
        }
        return -1;
    }

    @Override
    public int addTexCoord2D(float s, float t) {
        return addTexCoord2D(new TexCoord2d(s, t));
    }

    @Override
    public int addTexCoord3D(TexCoord3d coord) {
        if (texCoord3DIndex < texCoords3D.length) {
            texCoords3D[texCoord3DIndex] = coord;
            return texCoord3DIndex++;
        }
        return -1;
    }

    @Override
    public int addTexCoord3D(float s, float t, float r) {
        return addTexCoord3D(new TexCoord3d(s, t, r));
    }

    @Override
    public int addTexCoord4D(TexCoord4d coord) {
        if (texCoord4DIndex < texCoords4D.length) {
            texCoords4D[texCoord4DIndex] = coord;
            return texCoord4DIndex++;
        }
        return -1;
    }

    @Override
    public int addTexCoord4D(float s, float t, float r, float q) {
        return addTexCoord4D(new TexCoord4d(s, t, r, q));
    }

    @Override
    public void setPoint(int index, JVector3d vertex) {
        if (index < 0 || index >= points.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid vertex index.");
        }
        this.points[index] = vertex;
    }

    @Override
    public void setPoint(int index, double x, double y, double z) {
        setPoint(index, new JVector3d(x, y, z));
    }

    @Override
    public void setNormal(int index, JVector3d normal) {
        if (index < 0 || index >= normals.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid normal index.");
        }
        this.normals[index] = normal;
    }

    @Override
    public void setNormal(int index, double x, double y, double z) {
        setNormal(index, new JVector3d(x, y, z));
    }

    @Override
    public void setColor(int index, JColorf color) {
        if (index < 0 || index >= colors.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid color index.");
        }
        this.colors[index] = color;
    }

    @Override
    public void setColor(int index, float r, float g, float b, float alpha) {
        setColor(index, new JColorf(r, g, b, alpha));
    }

    @Override
    public void setTexCoord2D(int index, TexCoord2d coord) {
        if (index < 0 || index >= texCoords2D.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid 2D texture coord index.");
        }
        this.texCoords2D[index] = coord;
    }

    @Override
    public void setTexCoord2D(int index, double s, double t) {
        setTexCoord2D(index, new TexCoord2d(s, t));
    }

    @Override
    public void setTexCoord3D(int index, TexCoord3d coord) {
        if (index < 0 || index >= texCoords3D.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid 3D texture coord index.");
        }
        this.texCoords3D[index] = coord;
    }

    @Override
    public void setTexCoord3D(int index, double s, double t, double r) {
        setTexCoord3D(index, new TexCoord3d(s, t, r));
    }

    @Override
    public void setTexCoord4D(int index, TexCoord4d coord) {
        if (index < 0 || index >= texCoords4D.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid 4D texture coord index.");
        }
        this.texCoords4D[index] = coord;
    }

    @Override
    public void setTexCoord4D(int index, double s, double t, double r, double q) {
        setTexCoord4D(index, new TexCoord4d(s, t, r, q));
    }

    @Override
    public JVector3d getPoint(int index) {
        return points[index];
    }

    @Override
    public double[] getPointAsArray(int index) {
        return points[index].toArray();
    }

    @Override
    public JVector3d getNormal(int index) {
        return normals[index];
    }

    @Override
    public double[] getNormalAsArray(int index) {
        return normals[index].toArray();
    }

    @Override
    public JColorf getColor(int index) {
        return colors[index];
    }

    @Override
    public float[] getColorAsArray(int index) {
        return colors[index].color;
    }

    @Override
    public TexCoord2d getTexCoord2D(int index) {
        return texCoords2D[index];
    }

    @Override
    public double[] getTexCoord2DAsrray(int index) {
        return texCoords2D[index].toArray();
    }

    @Override
    public TexCoord3d getTexCoord3D(int index) {
        return texCoords3D[index];
    }

    @Override
    public double[] getTexCoord3DAsrray(int index) {
        return texCoords3D[index].toArray();
    }

    @Override
    public TexCoord4d getTexCoord4D(int index) {
        return texCoords4D[index];
    }

    @Override
    public double[] getTexCoord4DAsrray(int index) {
        return texCoords2D[index].toArray();
    }

    @Override
    public void setVertexCount(int count) {
        this.vertices = new TriangleVertex[count];
        vertexIndex = 0;
    }

    @Override
    public int getVertexCount() {
        return this.vertices.length;
    }

    @Override
    public int addVertex(TriangleVertex vertex) {
        this.vertices[vertexIndex] = vertex;
        return vertexIndex++;
    }

    @Override
    public int addVertex(int point, int normal, int color, int texture) {
        return addVertex(new TriangleVertex(point, normal, color, texture));
    }

    @Override
    public void setVertex(int index, TriangleVertex vertex) {
        this.vertices[index] = vertex;
    }

    @Override
    public void setVertex(int index, int point, int normal, int color, int texCoord) {
        setVertex(index, new TriangleVertex(point, normal, color, texCoord));
    }

    @Override
    public TriangleVertex getVertex(int index) {
        return this.vertices[index];
    }

    @Override
    public int[] getVertexAsArray(int index) {
        return this.vertices[index].toArray();
    }

    @Override
    public void setVertices(TriangleVertex[] vertices) {
        this.vertices = vertices;
        this.vertexIndex = vertices != null ? this.vertices.length - 1 : -1;
    }

    @Override
    public void setPoints(JVector3d[] points) {
        this.points = points;
        this.pointIndex = points != null ? this.points.length - 1 : - 1;
    }

    @Override
    public void setNormals(JVector3d[] normals) {
        this.normals = normals;
        this.normalIndex = this.normals != null ? this.normals.length - 1 : -1;
    }

    @Override
    public void setColors(JColorf[] colors) {
        this.colors = colors;
        this.colorIndex = this.colors != null ? this.colors.length -1 : -1;
    }

    @Override
    public void setTexCoords2D(TexCoord2d[] coords) {
        this.texCoords2D = coords;
        this.texCoord2DIndex = coords!= null ? coords.length - 1: -1;
    }

    @Override
    public void setTexCoords3D(TexCoord3d[] coords) {
        this.texCoords3D = coords;
        this.texCoord3DIndex = coords!= null ? coords.length - 1: -1;
    }

    @Override
    public void setTexCoords4D(TexCoord4d[] coords) {
        this.texCoords4D = coords;
        this.texCoord4DIndex = coords!= null ? coords.length - 1: -1;
    }
}
