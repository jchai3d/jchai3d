/*
 *   This file is part of the JCHAI 3D visualization and haptics libraries.
 *   Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License("GPL") version 2
 *   as published by the Free Software Foundation.
 *
 *   For using the JCHAI 3D libraries with software that can not be combined
 *   with the GNU GPL, and for taking advantage of the additional benefits
 *   of our support services, please contact CHAI 3D about acquiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */
package org.jchai3d.files;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jchai3d.graphics.*;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JMesh;
import org.jchai3d.scenegraph.JWorld;

/**
 *
 * @author Marcos
 */
public class JFileLoaderOBJ {

    /**
     * Clients can use this to tell the obj loader how to behave in terms of
     * vertex merging. \n If \b true (default), loaded obj files will have three
     * _distinct_ vertices per triangle, with no vertex re-use.
     */
    public static boolean OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES = true;

    /*
     * List of vertices.
     */
    ArrayList<JVector3d> vertices;
    /**
     * List of faces.
     */
    ArrayList<JFace> faces;
    /**
     * List of normals.
     */
    ArrayList<JVector3d> normals;
    /**
     * List of texture coordinates.
     */
    ArrayList<JVector3d> texCoords;
    /**
     * List of material and texture properties.
     */
    ArrayList<JMaterialInfo> materials;
    /**
     * The pointer to the file.
     */
    String currentToken;
    /**
     *
     */
    String currentParameter;
    /**
     * List of names obtained from 'g' commands, with the most recent at the
     * back...
     */
    ArrayList<String> groupNames;
    int objLine;
    int mtlLine;

    //! Load model file.
    public boolean loadModel(File file) throws IOException {

        //----------------------------------------------------------------------
        // Load a OBJ file and render its data into a display list
        //----------------------------------------------------------------------

        String basePath;            // Path were all paths in the OBJ start
        int curMaterial = -1;       // Current material

        // Get base path
        basePath = makePath(file);

        //----------------------------------------------------------------------
        // Open the OBJ file
        //----------------------------------------------------------------------
        //RandomAccessFile hFile = new RandomAccessFile(fileName, "r");
        BufferedReader hFile = new BufferedReader(new FileReader(file));

        //----------------------------------------------------------------------
        // Allocate space for structures that hold the model data
        //----------------------------------------------------------------------
        int len = (int) ((file.length() / 1024) / 15);

        groupNames = new ArrayList<String>();
        vertices = new ArrayList<JVector3d>(len);
        normals = new ArrayList<JVector3d>(len);
        texCoords = new ArrayList<JVector3d>(len);
        faces = new ArrayList<JFace>(len * 3);
        materials = new ArrayList<JMaterialInfo>();

        //----------------------------------------------------------------------
        // Read the file contents
        //----------------------------------------------------------------------

        // Start reading the file from the start
        hFile.close();
        hFile = new BufferedReader(new FileReader(file));

        objLine = 0;

        try {
            // Quit reading when end of file has been reached
            while (readNextString(hFile)) {


                // Next three elements are floats of a vertex
                if (currentToken.equals(CHAI_OBJ_VERTEX_ID)) {
                    // Read three floats out of the file
                    float[] f = stringToFloatArray(currentParameter, " ");


                    JVector3d v = new JVector3d();
                    v.x = f[0];
                    v.y = f[1];
                    v.z = f[2];
                    vertices.add(v);
                }

                // Next two elements are floats of a texture coordinate
                if (currentToken.equals(CHAI_OBJ_TEXCOORD_ID)) {
                    // Read two floats out of the file
                    float f[] = stringToFloatArray(currentParameter, " ");

                    JVector3d t = new JVector3d();
                    t.x = f[0];
                    t.y = f[1];
                    t.z = 0.0f;
                    texCoords.add(t);
                }

                // Next three elements are floats of a vertex normal
                if (currentToken.equals(CHAI_OBJ_NORMAL_ID)) {
                    // Read three floats out of the file
                    float[] f = stringToFloatArray(currentParameter, " ");

                    JVector3d n = new JVector3d();
                    n.x = f[0];
                    n.y = f[1];
                    n.z = f[2];

                    // Next normal
                    normals.add(n);
                }

                // Rest of the line contains face information
                if (currentToken.equals(CHAI_OBJ_FACE_ID)) {
                    // Convert string into a face structure
                    JFace f = parseFaceString(currentParameter, curMaterial);
                    faces.add(f);
                }

                // Rest of the line contains face information
                if (currentToken.equals(CHAI_OBJ_NAME_ID)) {
                    groupNames.add(currentParameter);
                }

                // Process material information only if needed
                if (materials != null) {
                    // Rest of the line contains the name of a material
                    if (currentToken.equals(CHAI_OBJ_USE_MTL_ID)) {
                        // Are any materials loaded ?
                        if (!materials.isEmpty()) {
                            // Find material array index for the material name
                            for (int i = 0; i < materials.size(); i++) {
                                if (materials.get(i).name.equals(currentParameter)) {
                                    curMaterial = i;
                                    break;
                                }
                            }
                        }
                    }

                    // Rest of the line contains the filename of a material library
                    if (currentToken.equals(CHAI_OBJ_MTL_LIB_ID)) {
                        // Append material library filename to the model's base path
                        String libraryFile = basePath + currentParameter;

                        // Append .mtl
                        //strcat(szLibraryFile, ".mtl");

                        // Load the material library
                        loadMaterialLib(new File(libraryFile), basePath);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while loading file[obj: " + objLine + "; mtl:" + mtlLine + "].", e);
        }

        // Close OBJ file
        hFile.close();

        //----------------------------------------------------------------------
        // Success
        //----------------------------------------------------------------------

        return true;
    }

    private long lenFileUrl(URL url) throws IOException {
        InputStreamReader stream = new InputStreamReader(url.openStream());

        BufferedReader hFile = new BufferedReader(stream);

        long lenFile = 0;
        /*
         * calcula o tamanho do arquivo
         */
        String anItem = null;
        while (null != (anItem = hFile.readLine())) {
            lenFile += anItem.getBytes().length;
        }

        return lenFile;
    }

    public boolean loadModel(URL url, File file) throws IOException {

        //----------------------------------------------------------------------
        // Load a OBJ file and render its data into a display list
        //----------------------------------------------------------------------

        String basePath;            // Path were all paths in the OBJ start
        int curMaterial = -1;       // Current material

        InputStreamReader stream = new InputStreamReader(url.openStream());

        // Get base path
        basePath = makePath(file);

        //----------------------------------------------------------------------
        // Open the OBJ file
        //----------------------------------------------------------------------
        //RandomAccessFile hFile = new RandomAccessFile(fileName, "r");
        BufferedReader hFile = new BufferedReader(stream);

        //----------------------------------------------------------------------
        // Allocate space for structures that hold the model data
        //----------------------------------------------------------------------
        int len = (int) ((lenFileUrl(url) / 1024) / 15);

        groupNames = new ArrayList<String>();
        vertices = new ArrayList<JVector3d>(len);
        normals = new ArrayList<JVector3d>(len);
        texCoords = new ArrayList<JVector3d>(len);
        faces = new ArrayList<JFace>(len * 3);
        materials = new ArrayList<JMaterialInfo>();

        //----------------------------------------------------------------------
        // Read the file contents
        //----------------------------------------------------------------------

        // Start reading the file from the start
        //hFile.close();
        hFile = new BufferedReader(stream);

        objLine = 0;

        try {
            // Quit reading when end of file has been reached
            while (readNextString(hFile)) {


                // Next three elements are floats of a vertex
                if (currentToken.equals(CHAI_OBJ_VERTEX_ID)) {
                    // Read three floats out of the file
                    float[] f = stringToFloatArray(currentParameter, " ");


                    JVector3d v = new JVector3d();
                    v.x = f[0];
                    v.y = f[1];
                    v.z = f[2];
                    vertices.add(v);
                }

                // Next two elements are floats of a texture coordinate
                if (currentToken.equals(CHAI_OBJ_TEXCOORD_ID)) {
                    // Read two floats out of the file
                    float f[] = stringToFloatArray(currentParameter, " ");

                    JVector3d t = new JVector3d();
                    t.x = f[0];
                    t.y = f[1];
                    t.z = 0.0f;
                    texCoords.add(t);
                }

                // Next three elements are floats of a vertex normal
                if (currentToken.equals(CHAI_OBJ_NORMAL_ID)) {
                    // Read three floats out of the file
                    float[] f = stringToFloatArray(currentParameter, " ");

                    JVector3d n = new JVector3d();
                    n.x = f[0];
                    n.y = f[1];
                    n.z = f[2];

                    // Next normal
                    normals.add(n);
                }

                // Rest of the line contains face information
                if (currentToken.equals(CHAI_OBJ_FACE_ID)) {
                    // Convert string into a face structure
                    JFace f = parseFaceString(currentParameter, curMaterial);
                    faces.add(f);
                }

                // Rest of the line contains face information
                if (currentToken.equals(CHAI_OBJ_NAME_ID)) {
                    groupNames.add(currentParameter);
                }

                // Process material information only if needed
                if (materials != null) {
                    // Rest of the line contains the name of a material
                    if (currentToken.equals(CHAI_OBJ_USE_MTL_ID)) {
                        // Are any materials loaded ?
                        if (!materials.isEmpty()) {
                            // Find material array index for the material name
                            for (int i = 0; i < materials.size(); i++) {
                                if (materials.get(i).name.equals(currentParameter)) {
                                    curMaterial = i;
                                    break;
                                }
                            }
                        }
                    }

                    // Rest of the line contains the filename of a material library
                    if (currentToken.equals(CHAI_OBJ_MTL_LIB_ID)) {
                        // Append material library filename to the model's base path
                        String libraryFile = basePath + currentParameter;

                        // Append .mtl
                        //strcat(szLibraryFile, ".mtl");

                        /* constroi URL */
                        URL urlMat = new URL(libraryFile);
                       
                        // Load the material library
                        loadMaterialLib(url, new File(libraryFile), basePath);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while loading file[obj: " + objLine + "; mtl:" + mtlLine + "].", e);
        }

        // Close OBJ file
        hFile.close();

        //----------------------------------------------------------------------
        // Success
        //----------------------------------------------------------------------

        return true;
    }

    /**
     * Load material file [mtl].
     */
    private boolean loadMaterialLib(File libraryFile, String basePath) throws FileNotFoundException, IOException {

        //----------------------------------------------------------------------
        // Loads a material library file (.mtl)
        //----------------------------------------------------------------------

        String str;    // Buffer used while reading the file
        boolean firstMaterial = true;         // Only increase index after first
        // material has been set

        //----------------------------------------------------------------------
        // Open library file
        //----------------------------------------------------------------------
        BufferedReader reader = new BufferedReader(new FileReader(libraryFile));

        //----------------------------------------------------------------------
        // Read all material definitions
        //----------------------------------------------------------------------
        JMaterialInfo currentMaterial = null;

        mtlLine = 0;
        // Quit reading when end of file has been reached
        while (readNextString(reader, false)) {

            // Is it a "new material" identifier ?
            if (currentToken.equals(CHAI_OBJ_NEW_MTL_ID)) {


                // Store material name in the structure
                currentMaterial = new JMaterialInfo();
                materials.add(currentMaterial);
                currentMaterial.name = currentParameter;
            }

            // Transparency
            if ((currentToken.equals(CHAI_OBJ_MTL_ALPHA_ID)
                    || currentToken.equals(CHAI_OBJ_MTL_ALPHA_ID_ALT))) {
                // Read into current material
                currentMaterial.alpha = Float.parseFloat(currentParameter);
            }

            // Ambient material properties
            if (currentToken.equals(CHAI_OBJ_MTL_AMBIENT_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.ambient[0] = f[0];
                currentMaterial.ambient[0] = f[1];
                currentMaterial.ambient[0] = f[2];
            }

            // Diffuse material properties
            if (currentToken.equals(CHAI_OBJ_MTL_DIFFUSE_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.diffuse[0] = f[0];
                currentMaterial.diffuse[0] = f[1];
                currentMaterial.diffuse[0] = f[2];
            }

            // Specular material properties
            if (currentToken.equals(CHAI_OBJ_MTL_SPECULAR_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.diffuse[0] = f[0];
                currentMaterial.diffuse[0] = f[1];
                currentMaterial.diffuse[0] = f[2];
            }

            // Texture map name
            if (currentToken.equals(CHAI_OBJ_MTL_TEXTURE_ID)) {
                if (currentParameter.isEmpty()) {
                    continue;
                } else {
                    // Append material library filename to the model's base path
                    String textureFile = basePath + currentParameter;

                    // Store texture filename in the structure
                    currentMaterial.texture = textureFile;

                    // Load texture and store its ID in the structure
                    currentMaterial.textureID = 1;//LoadTexture(szTextureFile);
                }
            }

            // Shininess
            if (currentToken.equals(CHAI_OBJ_MTL_SHININESS_ID)) {
                // Read into current material
                currentMaterial.shininess = Float.parseFloat(currentParameter);

                // OBJ files use a shininess from 0 to 1000; Scale for OpenGL
                currentMaterial.shininess /= 1000.0f;
                currentMaterial.shininess *= 128.0f;
            }
        }

        reader.close();

        return (true);
    }

    /**
     * Get material from URL
     *
     * @param url
     * @param libraryFile
     * @param basePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private boolean loadMaterialLib(URL url, File libraryFile, String basePath) throws FileNotFoundException, IOException {

        //----------------------------------------------------------------------
        // Loads a material library file (.mtl)
        //----------------------------------------------------------------------

        String str;    // Buffer used while reading the file
        boolean firstMaterial = true;         // Only increase index after first
        // material has been set

        InputStreamReader stream = new InputStreamReader(url.openStream());
        
        //----------------------------------------------------------------------
        // Open library file
        //----------------------------------------------------------------------
        BufferedReader reader = new BufferedReader(stream);

        //----------------------------------------------------------------------
        // Read all material definitions
        //----------------------------------------------------------------------
        JMaterialInfo currentMaterial = null;

        mtlLine = 0;
        // Quit reading when end of file has been reached
        while (readNextString(reader, false)) {

            // Is it a "new material" identifier ?
            if (currentToken.equals(CHAI_OBJ_NEW_MTL_ID)) {


                // Store material name in the structure
                currentMaterial = new JMaterialInfo();
                materials.add(currentMaterial);
                currentMaterial.name = currentParameter;
            }

            // Transparency
            if ((currentToken.equals(CHAI_OBJ_MTL_ALPHA_ID)
                    || currentToken.equals(CHAI_OBJ_MTL_ALPHA_ID_ALT))) {
                // Read into current material
                currentMaterial.alpha = Float.parseFloat(currentParameter);
            }

            // Ambient material properties
            if (currentToken.equals(CHAI_OBJ_MTL_AMBIENT_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.ambient[0] = f[0];
                currentMaterial.ambient[0] = f[1];
                currentMaterial.ambient[0] = f[2];
            }

            // Diffuse material properties
            if (currentToken.equals(CHAI_OBJ_MTL_DIFFUSE_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.diffuse[0] = f[0];
                currentMaterial.diffuse[0] = f[1];
                currentMaterial.diffuse[0] = f[2];
            }

            // Specular material properties
            if (currentToken.equals(CHAI_OBJ_MTL_SPECULAR_ID)) {
                // Read into current material
                float[] f = stringToFloatArray(currentParameter, " ");
                currentMaterial.diffuse[0] = f[0];
                currentMaterial.diffuse[0] = f[1];
                currentMaterial.diffuse[0] = f[2];
            }

            // Texture map name
            if (currentToken.equals(CHAI_OBJ_MTL_TEXTURE_ID)) {
                if (currentParameter.isEmpty()) {
                    continue;
                } else {
                    // Append material library filename to the model's base path
                    String textureFile = basePath + currentParameter;

                    // Store texture filename in the structure
                    currentMaterial.texture = textureFile;

                    // Load texture and store its ID in the structure
                    currentMaterial.textureID = 1;//LoadTexture(szTextureFile);
                }
            }

            // Shininess
            if (currentToken.equals(CHAI_OBJ_MTL_SHININESS_ID)) {
                // Read into current material
                currentMaterial.shininess = Float.parseFloat(currentParameter);

                // OBJ files use a shininess from 0 to 1000; Scale for OpenGL
                currentMaterial.shininess /= 1000.0f;
                currentMaterial.shininess *= 128.0f;
            }
        }

        reader.close();

        return (true);
    }

    /**
     * Parse information about face.
     */
    private JFace parseFaceString(String faceString, int materialIndex) {
        //----------------------------------------------------------------------
        // Convert face string from the OBJ file into a face structure
        //----------------------------------------------------------------------

        int i;
        int iVertex = 0, iTextureCoord = 0, iNormal = 0;

        String[] triplets = faceString.split(" ");

        // Init the face structure
        JFace faceOut = new JFace();
        faceOut.numVertices = triplets.length;


        if (groupNames != null && groupNames.size() > 0) {
            faceOut.groupIndex = groupNames.size() - 1;
        } else {
            faceOut.groupIndex = -1;
        }

        //----------------------------------------------------------------------
        // Allocate space for structures that hold the face data
        //----------------------------------------------------------------------

        // Vertices
        faceOut.vertices = new JVector3d[faceOut.numVertices];
        faceOut.vertexIndices = new int[faceOut.numVertices];

        // Allocate space for normals and texture coordinates only if present
        if (!normals.isEmpty()) {
            faceOut.normals = new JVector3d[faceOut.numVertices];
            faceOut.normalIndices = new int[faceOut.numVertices];
        } else {
            faceOut.normals = null;
            faceOut.normalIndices = null;
        }

        if (!texCoords.isEmpty()) {
            faceOut.texCoords = new JVector3d[faceOut.numVertices];
            faceOut.textureIndices = new int[faceOut.numVertices];
        } else {
            faceOut.texCoords = null;
            faceOut.textureIndices = null;
        }

        //----------------------------------------------------------------------
        // Copy vertex, normal, material and texture data into the structure
        //----------------------------------------------------------------------

        // Set material
        faceOut.materialIndex = materialIndex;


        boolean lineWithDoubleSlash = faceString.contains("//");

        // Process per-vertex data
        for (i = 0; i < faceOut.numVertices; i++) {
            // Read one triplet from the face string
            String curTriplet = triplets[i];


            iTextureCoord = -1;
            iNormal = -1;

            // double slash lines flags that this face have no tex coords
            if (lineWithDoubleSlash) {
                int[] a = stringToIntArray(curTriplet, "//");
                iVertex = a[0];
                iNormal = a[1];
                faceOut.texCoords = null;
                faceOut.textureIndices = null;
            } else {
                // Are vertices, normals and texture coordinates present ?
                if (!normals.isEmpty() && !texCoords.isEmpty()) {
                    // Yes
                    int[] a = stringToIntArray(curTriplet, "/");
                    iVertex = a[0];
                    iTextureCoord = a[1];
                    iNormal = a[2];

                } // Vertices and texture coordinates but no normals
                else if (!texCoords.isEmpty() && normals.isEmpty()) {
                    int[] a = stringToIntArray(curTriplet, "/");
                    iVertex = a[0];
                    iTextureCoord = a[1];
                    faceOut.normals = null;
                } // Only vertices
                else {
                    iVertex = Integer.parseInt(curTriplet);
                    faceOut.texCoords = null;
                    faceOut.textureIndices = null;
                    faceOut.normals = null;
                }
            }


            // Copy vertex into the face. Also check for normals and texture
            // coordinates and copy them if present.
            faceOut.vertices[i] = vertices.get(iVertex - 1);
            faceOut.vertexIndices[i] = iVertex - 1;

            if (iTextureCoord != -1) {
                faceOut.texCoords[i] = texCoords.get(iTextureCoord - 1);
                faceOut.textureIndices[i] = iTextureCoord - 1;
            }
            if (iNormal != -1) {
                faceOut.normals[i] = normals.get(iNormal - 1);
                faceOut.normals[i].normalize();
                faceOut.normalIndices[i] = iNormal - 1;
            }

        }
        return faceOut;
    }

    /*
     * Internal: get a (possibly new) vertex index for a vertex.
     */
    private static int getVertexIndex(JMesh a_mesh,
            JFileLoaderOBJ a_model,
            VertexIndexMap a_vertexMap,
            VertexIndexSet vis) {
        int index;


        // Have we seen this vertex before?
        // If we have, just grab the new index for this vertex
        if (a_vertexMap.find(vis)) {
            index = a_vertexMap.second();
            return index;
        } // Otherwise create a new vertex and put the mapping in our map
        else {
            index = a_mesh.newVertex(a_model.vertices.get(vis.vIndex));
            a_vertexMap.put(vis, index);
            return index;
        }
    }

    /**
     * Loads a OBJ image by providing a filename and mesh in which object is
     * loaded.
     */
    public static boolean jLoadFileOBJ(JMesh mesh, File file) throws IOException {
        JFileLoaderOBJ fileObj = new JFileLoaderOBJ();

        // load file into memory. If an error occurs, exit.

        if (!fileObj.loadModel(file)) {
            return (false);
        }

        // get information about mesh
        JWorld world = mesh.getParentWorld();

        // clear all vertices and triangle of current mesh
        mesh.clear();

        // get information about file
        int numMaterials = fileObj.materials.size();
        int numNormals = fileObj.normals.size();
        int numTexCoord = fileObj.texCoords.size();

        // extract materials
        ArrayList<JMaterial> materials = new ArrayList<JMaterial>();

        // object has no material properties
        if (numMaterials == 0) {
            // create a new child
            JMesh newMesh = mesh.createMesh();
            mesh.addChild(newMesh);

            // Give him a default color
            mesh.setVertexColor(new JColorf(1.0f, 1.0f, 1.0f, 1.0f), true);
            mesh.setVertexColorsEnabled(true, true);
            mesh.setMaterialEnabled(false, true);
            mesh.setTransparencyEnabled(false, true);
        } // object has material properties. Create a child for each material
        // property.
        else {

            int i = 0;
            boolean foundTransparentMaterial = false;

            while (i < numMaterials) {
                // create a new child
                JMesh newMesh = mesh.createMesh();
                mesh.addChild(newMesh);

                // get next material
                JMaterial newMaterial = new JMaterial();
                JMaterialInfo material = fileObj.materials.get(i);

                int textureId = material.textureID;
                if (textureId >= 1) {
                    JTexture2D newTexture;

                    if (world != null) {
                        newTexture = world.newTexture();
                    } else {
                        newTexture = new JTexture2D();
                    }

                    boolean result = newTexture.load(new File(material.texture));

                    // If this didn't work out, try again in the obj file's path
                    if (!result) {
                        String modelDir = file.getPath().replace(file.getName(), "");
                        String newTexturePath = modelDir + material.texture;

                        result = newTexture.load(new File(newTexturePath));
                    }

                    if (result) {
                        newMesh.setTexture(newTexture);
                        newMesh.setTextureMappingEnabled(true);
                    }
                    /*
                     * // We really failed to load a texture... else { #if
                     * defined(_WIN32) // CHAI_DEBUG_PRINT("Could not load
                     * texture map %s\n",material.texture); #endif }
                     */
                }

                float alpha = material.alpha;
                if (alpha < 1.0) {
                    newMesh.setTransparencyEnabled(true, false);
                    foundTransparentMaterial = true;
                }

                newMesh.setMaterial(material.toMaterial());
                newMaterial.setShininess((int) material.shininess);

                i++;
            }

            // Enable material property rendering
            mesh.setVertexColorsEnabled(false, true);
            mesh.setMaterialEnabled(true, true);

            // Mark the presence of transparency in the root mesh; don't
            // modify the value stored in children...
            mesh.setTransparencyEnabled(foundTransparentMaterial, false);

        }

        // Keep track of vertex mapping in each mesh; maps "old" vertices
        // to new vertices
        int nMeshes = mesh.getNumChildren();
        VertexIndexMap[] vertexMaps = new VertexIndexMap[nMeshes];
        for (int i = 0; i < nMeshes; i++) {
            vertexMaps[i] = new VertexIndexMap();
        }


        // build object


        // get triangles
        int numTriangles = fileObj.faces.size();
        int j = 0;
        while (j < numTriangles) {
            // get next face
            JFace face = fileObj.faces.get(j);

            // get material index attributed to the face
            int objIndex = face.materialIndex;

            if (objIndex == -1) {
                objIndex = 0;
            }

            JMesh curMesh;
            // the mesh that we're reading this triangle into
            if (fileObj.materials == null) {
                curMesh = mesh;
            } else {
                curMesh = (JMesh) mesh.getChild(objIndex);
            }

            // create a name for this mesh if necessary (over-writing a previous
            // name if one has been written)
            if ((face.groupIndex >= 0) && (fileObj.groupNames.size() > 0)) {
                curMesh.setObjectName(fileObj.groupNames.get(face.groupIndex));
            }

            // get the vertex map for this mesh
            VertexIndexMap curVertexMaps = vertexMaps[objIndex];

            // number of vertices on face
            int vertCount = face.numVertices;

            if (vertCount >= 3) {
                int indexV1 = face.vertexIndices[0];

                if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {
                    VertexIndexSet vis = new VertexIndexSet(indexV1);
                    if (numNormals > 0) {
                        vis.nIndex = face.normalIndices[0];
                    }
                    if (numTexCoord > 0) {
                        vis.tIndex = face.textureIndices[0];
                    }
                    indexV1 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);
                }

                for (int triangleVert = 2; triangleVert < vertCount; triangleVert++) {

                    int indexV2 = face.vertexIndices[triangleVert - 1];

                    int indexV3 = face.vertexIndices[triangleVert];

                    if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {

                        VertexIndexSet vis = new VertexIndexSet(indexV2);

                        if (numNormals > 0) {
                            vis.nIndex = face.normalIndices[triangleVert - 1];
                        }

                        if (numTexCoord > 0) {
                            vis.tIndex = face.textureIndices[triangleVert - 1];
                        }

                        indexV2 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);

                        vis.vIndex = indexV3;

                        if (numNormals > 0) {
                            vis.nIndex = face.normalIndices[triangleVert];
                        }

                        if (numTexCoord > 0) {
                            vis.tIndex = face.textureIndices[triangleVert];
                        }

                        indexV3 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);
                    }

                    // For debugging, I want to look for degenerate triangles, but
                    // I don't want to assert here.
                    //if (indexV1 == indexV2 || indexV2 == indexV3 || indexV1 == indexV3) {
                    //}

                    int indexTriangle;

                    // create triangle:
                    if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {
                        indexTriangle =
                                curMesh.newTriangle(indexV1, indexV2, indexV3);
                    } else {
                        indexTriangle =
                                curMesh.newTriangle(
                                fileObj.vertices.get(indexV1),
                                fileObj.vertices.get(indexV2),
                                fileObj.vertices.get(indexV3));
                    }


                    // assign normals
                    if (numNormals > 0 && face.normals != null) {
                        // set normals
                        curMesh.getTriangle(indexTriangle, false).getVertex0().setNormal(face.normals[0]);
                        curMesh.getTriangle(indexTriangle, false).getVertex1().setNormal(face.normals[triangleVert - 1]);
                        curMesh.getTriangle(indexTriangle, false).getVertex2().setNormal(face.normals[triangleVert]);
                    }

                    // assign texture coordinates
                    if (numTexCoord > 0 && (face.texCoords != null)) {
                        // set texture coordinates
                        curMesh.getTriangle(indexTriangle, false).getVertex0().setTexCoord(face.texCoords[0]);
                        curMesh.getTriangle(indexTriangle, false).getVertex1().setTexCoord(face.texCoords[triangleVert - 1]);
                        curMesh.getTriangle(indexTriangle, false).getVertex2().setTexCoord(face.texCoords[triangleVert]);
                    }
                }
            } else {
                // This faces doesn't have 3 vertices... this line is just
                // here for debugging, since this should never happen, but
                // I don't want to assert here.         
            }
            j++;
        }

        vertexMaps = null;

        // if no normals were specified in the file, compute them
        // based on triangle faces
        if (numNormals == 0) {
            mesh.computeAllNormals(true);
        }

        // compute boundary boxes
        mesh.computeBoundaryBox(true);

        // update global position in world
        if (world != null) {
            world.computeGlobalPositions(true);
        }

        // return success
        return (true);
    }

    public static boolean jLoadFileOBJ(JMesh mesh, URL url, File file) throws IOException {
        JFileLoaderOBJ fileObj = new JFileLoaderOBJ();

        // load file into memory. If an error occurs, exit.

        if (!fileObj.loadModel(url, file)) {
            return (false);
        }

        // get information about mesh
        JWorld world = mesh.getParentWorld();

        // clear all vertices and triangle of current mesh
        mesh.clear();

        // get information about file
        int numMaterials = fileObj.materials.size();
        int numNormals = fileObj.normals.size();
        int numTexCoord = fileObj.texCoords.size();

        // extract materials
        ArrayList<JMaterial> materials = new ArrayList<JMaterial>();

        // object has no material properties
        if (numMaterials == 0) {
            // create a new child
            JMesh newMesh = mesh.createMesh();
            mesh.addChild(newMesh);

            // Give him a default color
            mesh.setVertexColor(new JColorf(1.0f, 1.0f, 1.0f, 1.0f), true);
            mesh.setVertexColorsEnabled(true, true);
            mesh.setMaterialEnabled(false, true);
            mesh.setTransparencyEnabled(false, true);
        } // object has material properties. Create a child for each material
        // property.
        else {

            int i = 0;
            boolean foundTransparentMaterial = false;

            while (i < numMaterials) {
                // create a new child
                JMesh newMesh = mesh.createMesh();
                mesh.addChild(newMesh);

                // get next material
                JMaterial newMaterial = new JMaterial();
                JMaterialInfo material = fileObj.materials.get(i);

                int textureId = material.textureID;
                if (textureId >= 1) {
                    JTexture2D newTexture;

                    if (world != null) {
                        newTexture = world.newTexture();
                    } else {
                        newTexture = new JTexture2D();
                    }

                    boolean result = newTexture.load(new File(material.texture));

                    // If this didn't work out, try again in the obj file's path
                    if (!result) {
                        String modelDir = file.getPath().replace(file.getName(), "");
                        String newTexturePath = modelDir + material.texture;

                        result = newTexture.load(new File(newTexturePath));
                    }

                    if (result) {
                        newMesh.setTexture(newTexture);
                        newMesh.setTextureMappingEnabled(true);
                    }
                    /*
                     * // We really failed to load a texture... else { #if
                     * defined(_WIN32) // CHAI_DEBUG_PRINT("Could not load
                     * texture map %s\n",material.texture); #endif }
                     */
                }

                float alpha = material.alpha;
                if (alpha < 1.0) {
                    newMesh.setTransparencyEnabled(true, false);
                    foundTransparentMaterial = true;
                }

                newMesh.setMaterial(material.toMaterial());
                newMaterial.setShininess((int) material.shininess);

                i++;
            }

            // Enable material property rendering
            mesh.setVertexColorsEnabled(false, true);
            mesh.setMaterialEnabled(true, true);

            // Mark the presence of transparency in the root mesh; don't
            // modify the value stored in children...
            mesh.setTransparencyEnabled(foundTransparentMaterial, false);

        }

        // Keep track of vertex mapping in each mesh; maps "old" vertices
        // to new vertices
        int nMeshes = mesh.getNumChildren();
        VertexIndexMap[] vertexMaps = new VertexIndexMap[nMeshes];
        for (int i = 0; i < nMeshes; i++) {
            vertexMaps[i] = new VertexIndexMap();
        }


        // build object


        // get triangles
        int numTriangles = fileObj.faces.size();
        int j = 0;
        while (j < numTriangles) {
            // get next face
            JFace face = fileObj.faces.get(j);

            // get material index attributed to the face
            int objIndex = face.materialIndex;

            if (objIndex == -1) {
                objIndex = 0;
            }

            JMesh curMesh;
            // the mesh that we're reading this triangle into
            if (fileObj.materials == null) {
                curMesh = mesh;
            } else {
                curMesh = (JMesh) mesh.getChild(objIndex);
            }

            // create a name for this mesh if necessary (over-writing a previous
            // name if one has been written)
            if ((face.groupIndex >= 0) && (fileObj.groupNames.size() > 0)) {
                curMesh.setObjectName(fileObj.groupNames.get(face.groupIndex));
            }

            // get the vertex map for this mesh
            VertexIndexMap curVertexMaps = vertexMaps[objIndex];

            // number of vertices on face
            int vertCount = face.numVertices;

            if (vertCount >= 3) {
                int indexV1 = face.vertexIndices[0];

                if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {
                    VertexIndexSet vis = new VertexIndexSet(indexV1);
                    if (numNormals > 0) {
                        vis.nIndex = face.normalIndices[0];
                    }
                    if (numTexCoord > 0) {
                        vis.tIndex = face.textureIndices[0];
                    }
                    indexV1 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);
                }

                for (int triangleVert = 2; triangleVert < vertCount; triangleVert++) {

                    int indexV2 = face.vertexIndices[triangleVert - 1];

                    int indexV3 = face.vertexIndices[triangleVert];

                    if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {

                        VertexIndexSet vis = new VertexIndexSet(indexV2);

                        if (numNormals > 0) {
                            vis.nIndex = face.normalIndices[triangleVert - 1];
                        }

                        if (numTexCoord > 0) {
                            vis.tIndex = face.textureIndices[triangleVert - 1];
                        }

                        indexV2 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);

                        vis.vIndex = indexV3;

                        if (numNormals > 0) {
                            vis.nIndex = face.normalIndices[triangleVert];
                        }

                        if (numTexCoord > 0) {
                            vis.tIndex = face.textureIndices[triangleVert];
                        }

                        indexV3 = getVertexIndex(curMesh, fileObj, curVertexMaps, vis);
                    }

                    // For debugging, I want to look for degenerate triangles, but
                    // I don't want to assert here.
                    //if (indexV1 == indexV2 || indexV2 == indexV3 || indexV1 == indexV3) {
                    //}

                    int indexTriangle;

                    // create triangle:
                    if (!OBJ_LOADER_SHOULD_GENERATE_EXTRA_VERTICES) {
                        indexTriangle =
                                curMesh.newTriangle(indexV1, indexV2, indexV3);
                    } else {
                        indexTriangle =
                                curMesh.newTriangle(
                                fileObj.vertices.get(indexV1),
                                fileObj.vertices.get(indexV2),
                                fileObj.vertices.get(indexV3));
                    }


                    // assign normals
                    if (numNormals > 0 && face.normals != null) {
                        // set normals
                        curMesh.getTriangle(indexTriangle, false).getVertex0().setNormal(face.normals[0]);
                        curMesh.getTriangle(indexTriangle, false).getVertex1().setNormal(face.normals[triangleVert - 1]);
                        curMesh.getTriangle(indexTriangle, false).getVertex2().setNormal(face.normals[triangleVert]);
                    }

                    // assign texture coordinates
                    if (numTexCoord > 0 && (face.texCoords != null)) {
                        // set texture coordinates
                        curMesh.getTriangle(indexTriangle, false).getVertex0().setTexCoord(face.texCoords[0]);
                        curMesh.getTriangle(indexTriangle, false).getVertex1().setTexCoord(face.texCoords[triangleVert - 1]);
                        curMesh.getTriangle(indexTriangle, false).getVertex2().setTexCoord(face.texCoords[triangleVert]);
                    }
                }
            } else {
                // This faces doesn't have 3 vertices... this line is just
                // here for debugging, since this should never happen, but
                // I don't want to assert here.         
            }
            j++;
        }

        vertexMaps = null;

        // if no normals were specified in the file, compute them
        // based on triangle faces
        if (numNormals == 0) {
            mesh.computeAllNormals(true);
        }

        // compute boundary boxes
        mesh.computeBoundaryBox(true);

        // update global position in world
        if (world != null) {
            world.computeGlobalPositions(true);
        }

        // return success
        return (true);
    }

    private float[] stringToFloatArray(String str, String separator) {
        String[] splitted = str.split(separator);
        float[] k = new float[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            k[i] = Float.parseFloat(splitted[i]);
        }
        return k;
    }

    private int[] stringToIntArray(String str, String separator) {
        String[] splitted = str.split(separator);
        int[] k = new int[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            k[i] = Integer.parseInt(splitted[i]);
        }
        return k;
    }

    /**
     * Read next string of file.
     */
    private boolean readNextString(BufferedReader reader) throws IOException {
        return readNextString(reader, true);
    }

    /**
     * Read next string of file.
     */
    private boolean readNextString(BufferedReader reader, boolean isOBJ) throws IOException {
        boolean skipLine = false;
        String line;

        do {

            // read a line from the reader
            line = reader.readLine();
            if (isOBJ) {
                objLine++;
            } else {
                mtlLine++;
            }


            // end of reader
            if (line == null) {
                return false;
            }
            while (line.contains("  ")) {
                line = line.replace("  ", " ");
            }

            // this line have a comment
            if (line.contains(CHAI_OBJ_COMMENT_ID)) {

                // all line is a comment
                if (line.startsWith(CHAI_OBJ_COMMENT_ID)) {
                    skipLine = true;
                } else {

                    String[] value = line.split(CHAI_OBJ_COMMENT_ID);

                    // commented line
                    if (value[0].isEmpty()) {
                        skipLine = true;
                    } else {
                        updateTokenAndParameter(value[0]);
                        return true;
                    }
                }
            } else {
                if (line.isEmpty()) {
                    skipLine = true;
                } else {
                    updateTokenAndParameter(line);
                    return true;
                }

            }

        } while (skipLine);

        return false;
    }

    private void updateTokenAndParameter(String line) {
        int index = line.indexOf(" ");
        if (index == -1) {
            currentToken = line;
            currentParameter = null;
        } else {
            currentToken = line.substring(0, index);
            currentParameter = line.substring(index + 1, line.length());
        }
    }

    /**
     * File path.
     */
    private String makePath(File file) {
        return file.getPath().replace(file.getName(), "");
    }
    /*
     * OBJ File string identifiers
     */
    public static final String CHAI_OBJ_VERTEX_ID = "v";
    public static final String CHAI_OBJ_TEXCOORD_ID = "vt";
    public static final String CHAI_OBJ_NORMAL_ID = "vn";
    public static final String CHAI_OBJ_FACE_ID = "f";
    public static final String CHAI_OBJ_COMMENT_ID = "#";
    public static final String CHAI_OBJ_MTL_LIB_ID = "mtllib";
    public static final String CHAI_OBJ_USE_MTL_ID = "usemtl";
    public static final String CHAI_OBJ_NAME_ID = "g";
    /*
     * MTL File string identifiers
     */
    public static final String CHAI_OBJ_NEW_MTL_ID = "newmtl";
    public static final String CHAI_OBJ_MTL_TEXTURE_ID = "map_Kd";
    public static final String CHAI_OBJ_MTL_AMBIENT_ID = "Ka";
    public static final String CHAI_OBJ_MTL_DIFFUSE_ID = "Kd";
    public static final String CHAI_OBJ_MTL_SPECULAR_ID = "Ks";
    public static final String CHAI_OBJ_MTL_SHININESS_ID = "Ns";
    public static final String CHAI_OBJ_MTL_ALPHA_ID = "Tr";
    public static final String CHAI_OBJ_MTL_ALPHA_ID_ALT = "d";
    /*
     * Maximum number of vertices a that a single face can have
     */
    public static final int CHAI_OBJ_MAX_VERTICES = 256;
}

/**
 * A face vertex, as defined in an .obj file (a vertex/normal/texture set)
 */
class VertexIndexSet implements Comparable<VertexIndexSet> {

    protected int vIndex;
    protected int nIndex;
    protected int tIndex;

    public VertexIndexSet() {
        vIndex = nIndex = tIndex = 0;
    }

    public VertexIndexSet(int vIndex, int nIndex, int tIndex) {
        this.vIndex = vIndex;
        this.nIndex = nIndex;
        this.tIndex = tIndex;
    }

    public VertexIndexSet(int vIndex) {
        this.vIndex = vIndex;
        nIndex = tIndex = 0;
    }

    @Override
    public int compareTo(VertexIndexSet v2) {
        if (vIndex < v2.vIndex) {
            return 1;
        }
        if (v2.vIndex < vIndex) {
            return -1;
        }
        if (nIndex < v2.nIndex) {
            return 1;
        }
        if (v2.nIndex < nIndex) {
            return -1;
        }
        if (tIndex < v2.tIndex) {
            return 1;
        }

        return 0;
    }
}

class VertexIndexMap {

    TreeMap<VertexIndexSet, Integer> vertexIndexMap;

    public VertexIndexMap() {
        vertexIndexMap = new TreeMap<VertexIndexSet, Integer>();

    }

    public void put(VertexIndexSet v, Integer i) {
        vertexIndexMap.put(v, i);
    }

    public boolean find(VertexIndexSet v) {
        return vertexIndexMap.get(v) != null;
    }

    public int second() {
        Iterator<Integer> iter = vertexIndexMap.values().iterator();
        iter.next();
        return iter.next().intValue();
    }
}

/**
 * Information about a surface face.
 */
class JFace {

    protected int numVertices;
    protected int materialIndex;
    // Which 'g ...' group does this face belong to?  -1 indicates no group.
    protected int groupIndex;
    protected int[] vertexIndices;
    protected JVector3d[] vertices;
    protected int[] normalIndices;
    protected JVector3d[] normals;
    protected int[] textureIndices;
    protected JVector3d[] texCoords;
}

/**
 * Information about a material property
 */
class JMaterialInfo {

    protected String name;
    protected String texture;
    protected int textureID;
    protected float[] diffuse;
    protected float[] ambient;
    protected float[] specular;
    protected float[] emmissive;
    protected float alpha;
    protected float shininess;

    public JMaterialInfo() {
        name = null;

        texture = null;

        textureID = -1;

        diffuse = new float[]{0.8f, 0.8f, 0.8f};

        ambient = new float[]{0.8f, 0.8f, 0.8f};

        specular = new float[]{0.3f, 0.3f, 0.3f};

        emmissive = new float[]{0f, 0f, 0f};

        shininess = 0;

        alpha = 1.0f;
    }

    public JColorf getAmbientColor() {
        return new JColorf(ambient[0], ambient[1], ambient[2], alpha);
    }

    public JColorf getDiffuseColor() {
        return new JColorf(diffuse[0], diffuse[1], diffuse[2], alpha);
    }

    public JColorf getSpecularColor() {
        return new JColorf(specular[0], specular[1], specular[2], alpha);
    }

    public JColorf getEmmissiveColor() {
        return new JColorf(emmissive[0], emmissive[1], emmissive[2], alpha);
    }

    public JMaterial toMaterial() {
        JMaterial mat = new JMaterial();
        mat.setAmbient(getAmbientColor());
        mat.setDiffuse(getDiffuseColor());
        mat.setSpecular(getSpecularColor());
        mat.setEmission(getEmmissiveColor());
        mat.setShininess((int) shininess);
        return mat;
    }
}

enum FaceDesc {

    NORMALS_AND_TEXCOORD, NORMALS_ONLY, TEXCOORD_ONLY, VERTEX_ONLY;
}