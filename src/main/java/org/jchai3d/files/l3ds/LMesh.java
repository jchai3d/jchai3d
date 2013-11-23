/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

import java.util.ArrayList;

/**
 *
 * @author Marcos
 */
public class LMesh extends LObject {

    // the vertices, normals, etc.
    ArrayList<LVector4> vertices;
    ArrayList<LVector3> normals;
    ArrayList<LVector3> binormals;
    ArrayList<LVector3> tangents;
    ArrayList<LVector2> uv;
    ArrayList<LColor3> colors;
    // triangles
    ArrayList<LTriangle> triangles;
    //used internally
    ArrayList<LTri> tris;
    // the transformation matrix.
    LMatrix4 matrix;
    // the material ID array
    ArrayList<Integer> materials;

    // the default constructor
    LMesh() {
        super();

        this.vertices = new ArrayList<LVector4>();
        this.normals = new ArrayList<LVector3>();
        this.uv = new ArrayList<LVector2>();
        this.colors = new ArrayList<LColor3>();
        this.tangents = new ArrayList<LVector3>();
        this.binormals = new ArrayList<LVector3>();
        this.triangles = new ArrayList<LTriangle>();
        this.tris = new ArrayList<LTri>();
        this.materials = new ArrayList<Integer>();
        matrix = new LMatrix4();
        clear();
    }

    // clears the mesh, deleting all data
    void clear() {

        this.vertices.clear();
        this.normals.clear();
        this.uv.clear();
        this.colors.clear();
        this.tangents.clear();
        this.binormals.clear();
        this.triangles.clear();
        this.tris.clear();
        this.materials.clear();
        LMatrix4.loadIdentityMatrix(this.matrix);
    }

    // returns the number of vertices in the mesh
    int getVertexCount() {
        return vertices.size();
    }

    // sets the the size of the vertex array - for internal use
    void setVertexArraySize(int value) {

        this.vertices.ensureCapacity(value);
        this.normals.ensureCapacity(value);
        this.uv.ensureCapacity(value);
        this.colors.ensureCapacity(value);
        this.tangents.ensureCapacity(value);
        this.binormals.ensureCapacity(value);
    }

    // returns the number of triangles in the mesh
    int getTriangleCount() {
        return triangles.size();
    }

    // sets the size of the triangle array - for internal use
    void setTriangleArraySize(int value) {
        triangles.ensureCapacity(value);
        tris.ensureCapacity(value);
        tris.ensureCapacity(value);
        triangles.ensureCapacity(value);
    }

    // returns given vertex
    final LVector4 getVertex(int index) {
        return vertices.get(index);
    }

    // returns the given normal
    final LVector3 getNormal(int index) {
        return normals.get(index);
    }

    // returns the given texture coordinates vector
    final LVector2 getUV(int index) {
        return uv.get(index);
    }

    // return the color given the index
    LColor3 getColor(int index) {
        return colors.get(index);
    }

    // returns the pointer to the array of tangents
    final LVector3 getTangent(int index) {
        return tangents.get(index);
    }

    // returns the pointer to the array of binormals
    final LVector3 getBinormal(int index) {
        return binormals.get(index);
    }

    // sets the vertex at a given index to "vec" - for internal use
    void setVertex(final LVector4 vec, int index) {
        if (index >= vertices.size()) {
            return;
        }
        vertices.set(index, vec);
    }

    // sets the normal at a given index to "vec" - for internal use
    void setNormal(final LVector3 vec, int index) {
        if (index >= normals.size()) {
            return;
        }
        normals.set(index, vec);
    }

    // sets the texture coordinates vector at a given index to "vec" - for internal use
    void setUV(final LVector2 vec, int index) {
        if (index >= uv.size()) {
            return;
        }
        uv.set(index, vec);
    }

    // sets the color
    void setColor(final LColor3 vec, int index) {
        if (index >= colors.size()) {
            return;
        }
        colors.set(index, vec);
    }

    // sets the tangent at a given index to "vec" - for internal use
    void setTangent(final LVector3 vec, int index) {
        if (index >= tangents.size()) {
            return;
        }
        tangents.set(index, vec);
    }

    // sets the binormal at a given index to "vec" - for internal use
    void setBinormal(final LVector3 vec, int index) {
        if (index >= binormals.size()) {
            return;
        }
        binormals.set(index, vec);
    }

    // returns the triangle with a given index
    final LTriangle getTriangle(int index) {
        return triangles.get(index);
    }

    // returns the triangle with a given index, see LTriangle2 structure description
    LTriangle2 getTriangle2(int index) {
        LTriangle2 f = new LTriangle2();
        LTriangle t = getTriangle(index);
        f.vertices[0] = getVertex(t.a);
        f.vertices[1] = getVertex(t.b);
        f.vertices[2] = getVertex(t.c);

        f.vertexNormals[0] = getNormal(t.a);
        f.vertexNormals[1] = getNormal(t.b);
        f.vertexNormals[2] = getNormal(t.c);

        f.textureCoords[0] = getUV(t.a);
        f.textureCoords[1] = getUV(t.b);
        f.textureCoords[2] = getUV(t.c);

        f.color[0] = getColor(t.a);
        f.color[1] = getColor(t.b);
        f.color[2] = getColor(t.c);

        LVector3 a, b;

        a = LVector3.subtractVectors(LVector3._4to3(f.vertices[1]), LVector3._4to3(f.vertices[0]));
        b = LVector3.subtractVectors(LVector3._4to3(f.vertices[1]), LVector3._4to3(f.vertices[2]));

        f.faceNormal = LVector3.crossProduct(b, a);

        f.faceNormal = LVector3.normalizeVector(f.faceNormal);

        f.materialId = tris.get(index).materialId;

        return f;
    }

    // returns the mesh matrix, should be identity matrix after loading
    LMatrix4 getMatrix() {
        return this.matrix;
    }

    // sets the mesh matrix to a given matrix - for internal use
    void setMatrix(LMatrix4 m) {
        this.matrix = m;
    }

    void calcNormals(boolean useSmoothingGroups) {

        int i;
        // first calculate the face normals
        for (i = 0; i < this.triangles.size(); i++) {
            LVector3 a, b;
            a = LVector3.subtractVectors(
                    LVector3._4to3(this.vertices.get(this.tris.get(i).b)),
                    LVector3._4to3(this.vertices.get(this.tris.get(i).a)));

            b = LVector3.subtractVectors(
                    LVector3._4to3(this.vertices.get(this.tris.get(i).b)),
                    LVector3._4to3(this.vertices.get(this.tris.get(i).c)));

            this.tris.get(i).normal = LVector3.normalizeVector(LVector3.crossProduct(b, a));
        }

        ArrayList< ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>(this.vertices.size());

        for (i = 0; i < this.triangles.size(); i++) {
            int k = this.tris.get(i).a;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);

            k = this.tris.get(i).b;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);

            k = this.tris.get(i).c;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);
        }

        LVector3 temp;

        if (!useSmoothingGroups) {
            // now calculate the normals without using smoothing groups
            for (i = 0; i < this.vertices.size(); i++) {
                temp = LGlobals.zero3;
                int t = array.get(i).size();

                for (int k = 0; k < t; k++) {
                    temp.x += this.tris.get(array.get(i).get(k)).normal.x;
                    temp.y += this.tris.get(array.get(i).get(k)).normal.y;
                    temp.z += this.tris.get(array.get(i).get(k)).normal.z;
                }
                this.normals.set(i, LVector3.normalizeVector(temp));
            }
        } else {
            // now calculate the normals _USING_ smoothing groups
            // I'm assuming a triangle can only belong to one smoothing group at a time!
            ArrayList<Long> smGroups = new ArrayList<Long>();
            ArrayList< ArrayList<Integer>> smList = new ArrayList<ArrayList<Integer>>();

            int loop_size = this.vertices.size();

            for (i = 0; i < loop_size; i++) {
                int t = array.get(i).size();

                if (t == 0) {
                    continue;
                }

                smGroups.clear();
                smList.clear();
                smGroups.add(this.tris.get(array.get(i).get(0)).smoothingGroups);
                smList.ensureCapacity(smGroups.size());
                smList.get(smGroups.size() - 1).add(array.get(i).get(0));

                // first build a list of smoothing groups for the vertex
                for (int k = 0; k < t; k++) {
                    boolean found = false;
                    for (int j = 0; j < smGroups.size(); j++) {
                        if (this.tris.get(array.get(i).get(k)).smoothingGroups == smGroups.get(j)) {
                            smList.get(j).add(array.get(i).get(k));
                            found = true;
                        }
                    }
                    if (!found) {
                        smGroups.add(this.tris.get(array.get(i).get(k)).smoothingGroups);
                        smList.ensureCapacity(smGroups.size());
                        smList.get(smGroups.size() - 1).add(array.get(i).get(k));
                    }
                }
                // now we have the list of faces for the vertex sorted by smoothing groups


                // now duplicate the vertices so that there's only one smoothing group "per vertex"
                if (smGroups.size() > 1) {
                    for (int j = 1; j < smGroups.size(); j++) {
                        this.vertices.add(this.vertices.get(i));
                        this.normals.add(this.normals.get(i));
                        this.uv.add(this.uv.get(i));
                        this.colors.add(this.colors.get(i));
                        this.tangents.add(this.tangents.get(i));
                        this.binormals.add(this.binormals.get(i));

                        t = this.vertices.size() - 1;
                        for (int h = 0; h < smList.get(j).size(); h++) {
                            if (this.tris.get(smList.get(j).get(h)).a == i) {
                                this.tris.get(smList.get(j).get(h)).a = (short) t;
                            }

                            if (this.tris.get(smList.get(j).get(h)).b == i) {
                                this.tris.get(smList.get(j).get(h)).b = (short) t;
                            }

                            if (this.tris.get(smList.get(j).get(h)).c == i) {
                                this.tris.get(smList.get(j).get(h)).c = (short) t;
                            }
                        }
                    }
                }
            }

            // now rebuild a face list for each vertex, since the old one is invalidated
            for (i = 0; i < array.size(); i++) {
                array.get(i).clear();
            }
            array.clear();
            array.ensureCapacity(this.vertices.size());
            for (i = 0; i < this.triangles.size(); i++) {

                int k = this.tris.get(i).a;
                if (array.get(k) == null) {
                    array.set(k, new ArrayList<Integer>());
                }
                array.get(k).add(i);

                k = this.tris.get(i).b;
                if (array.get(k) == null) {
                    array.set(k, new ArrayList<Integer>());
                }
                array.get(k).add(i);

                k = this.tris.get(i).c;
                if (array.get(k) == null) {
                    array.set(k, new ArrayList<Integer>());
                }
                array.get(k).add(i);
            }

            // now compute the normals
            for (i = 0; i < this.vertices.size(); i++) {
                temp = LGlobals.zero3;
                int t = array.get(i).size();

                for (int k = 0; k < t; k++) {
                    temp.x += this.tris.get(array.get(i).get(k)).normal.x;
                    temp.y += this.tris.get(array.get(i).get(k)).normal.y;
                    temp.z += this.tris.get(array.get(i).get(k)).normal.z;
                }
                this.normals.set(i, LVector3.normalizeVector(temp));
            }

        }

        // copy this.tris to this.triangles
        for (i = 0; i < this.triangles.size(); i++) {
            this.triangles.get(i).a = this.tris.get(i).a;
            this.triangles.get(i).b = this.tris.get(i).b;
            this.triangles.get(i).c = this.tris.get(i).c;
        }
    }

    private void calcTextureSpace() {

        // a understandable description of how to do that can be found here:
        // http://members.rogers.com/deseric/tangentspace.htm
        // first calculate the tangent for each triangle
        LVector3 x_vec,
                y_vec,
                z_vec;
        LVector3 v1 = new LVector3(), v2 = new LVector3();
        int i;

        for (i = 0; i < triangles.size(); i++) {
            v1.x = vertices.get(tris.get(i).b).x - vertices.get(tris.get(i).a).x;
            v1.y = uv.get(tris.get(i).b).x - uv.get(tris.get(i).a).x;
            v1.z = uv.get(tris.get(i).b).y - uv.get(tris.get(i).a).y;

            v2.x = vertices.get(tris.get(i).c).x - vertices.get(tris.get(i).a).x;
            v2.y = uv.get(tris.get(i).c).x - uv.get(tris.get(i).a).x;
            v2.z = uv.get(tris.get(i).c).y - uv.get(tris.get(i).a).y;

            x_vec = LVector3.crossProduct(v1, v2);

            v1.x = vertices.get(tris.get(i).b).y - vertices.get(tris.get(i).a).y;
            v1.y = uv.get(tris.get(i).b).x - uv.get(tris.get(i).a).x;
            v1.z = uv.get(tris.get(i).b).y - uv.get(tris.get(i).a).y;

            v2.x = vertices.get(tris.get(i).c).y - vertices.get(tris.get(i).a).y;
            v2.y = uv.get(tris.get(i).c).x - uv.get(tris.get(i).a).x;
            v2.z = uv.get(tris.get(i).c).y - uv.get(tris.get(i).a).y;

            y_vec = LVector3.crossProduct(v1, v2);

            v1.x = vertices.get(tris.get(i).b).z - vertices.get(tris.get(i).a).z;
            v1.y = uv.get(tris.get(i).b).x - uv.get(tris.get(i).a).x;
            v1.z = uv.get(tris.get(i).b).y - uv.get(tris.get(i).a).y;

            v2.x = vertices.get(tris.get(i).c).z - vertices.get(tris.get(i).a).z;
            v2.y = uv.get(tris.get(i).c).x - uv.get(tris.get(i).a).x;
            v2.z = uv.get(tris.get(i).c).y - uv.get(tris.get(i).a).y;

            z_vec = LVector3.crossProduct(v1, v2);

            tris.get(i).tangent.x = -(x_vec.y / x_vec.x);
            tris.get(i).tangent.y = -(y_vec.y / y_vec.x);
            tris.get(i).tangent.z = -(z_vec.y / z_vec.x);

            tris.get(i).binormal.x = -(x_vec.z / x_vec.x);
            tris.get(i).binormal.y = -(y_vec.z / y_vec.x);
            tris.get(i).binormal.z = -(z_vec.z / z_vec.x);

        }

        // now for each vertex build a list of face that share this vertex
        ArrayList< ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>(vertices.size());

        for (i = 0; i < triangles.size(); i++) {
            int k = this.tris.get(i).a;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);

            k = this.tris.get(i).b;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);

            k = this.tris.get(i).c;
            if (array.get(k) == null) {
                array.set(k, new ArrayList<Integer>());
            }
            array.get(k).add(i);
        }

        // now average the tangents and compute the binormals as (tangent X normal)
        for (i = 0; i < vertices.size(); i++) {
            v1 = LGlobals.zero3;
            v2 = LGlobals.zero3;
            int t = array.get(i).size();

            for (int k = 0; k < t; k++) {
                v1.x += tris.get(array.get(i).get(k)).tangent.x;
                v1.y += tris.get(array.get(i).get(k)).tangent.y;
                v1.z += tris.get(array.get(i).get(k)).tangent.z;

                v2.x += tris.get(array.get(i).get(k)).binormal.x;
                v2.y += tris.get(array.get(i).get(k)).binormal.y;
                v2.z += tris.get(array.get(i).get(k)).binormal.z;
            }
            tangents.set(i, LVector3.normalizeVector(v1));
            //binormals.get(i) = NormalizeVector(v2);

            binormals.set(i, LVector3.normalizeVector(LVector3.crossProduct(tangents.get(i), normals.get(i))));
        }

    }

    // optimizises the mesh using a given optimization level
    void optimize(LOptimizationLevel value) {
        switch (value) {
            case OPTIMIZATION_NONE:
                //TransformVertices();
                break;
            case OPTIMZATION_SIMPLE:
                //TransformVertices();
                calcNormals(false);
                break;
            case OPTIMIZATION_FULL:
                //TransformVertices();
                calcNormals(true);
                calcTextureSpace();
                break;
        }
    }

    // sets an internal triangle structure with index "index" - for internal use only
    void setTri(final LTri tri, int index) {
        if (index >= tris.size()) {
            return;
        }

        tris.add(index, tri);
    }

    // returns the pointer to the internal triangle structure - for internal use only
    LTri getTri(int index) {
        return tris.get(index);
    }

    // returns the material id with a given index for the mesh
    int getMaterial(int index) {
        return materials.get(index);
    }

    // adds a material to the mesh and returns its index - for internal use
    int AddMaterial(int id) {
        materials.add(id);
        return materials.size() - 1;
    }

    // returns the number of materials used in the mesh
    int getMaterialCount() {
        return materials.size();
    }
}
