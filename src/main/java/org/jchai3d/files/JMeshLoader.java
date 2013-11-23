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
 *   author    Marcos da Silva Ramos
 *   version   1.0.0
 */

package org.jchai3d.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import org.jchai3d.scenegraph.JMesh;

public class JMeshLoader {

    public static boolean loadMeshFromFile(JMesh aMesh, File file) throws FileNotFoundException, IOException {
        return loadMeshFromFile(aMesh, file, null);
    }

    public static boolean loadMeshFromFile(JMesh aMesh, URL url, File file) throws FileNotFoundException, IOException {
        return loadMeshFromFile(aMesh, url, file, null);
    }
    
    public static boolean loadMeshFromFile(JMesh aMesh, File file, Observer observer) throws FileNotFoundException, IOException{

        // verify mesh object
        

        // retrieve filename
        String fileName = file.getName();
        int i = fileName.lastIndexOf(".");
        String extension = fileName.substring(i,fileName.length());

        // We need a file extension to figure out file type
        if (extension.length() == 0) {
            throw new IOException("Invalid file extension.");
        }
        // return value
        boolean result = false;

        // Load an .obj file
        if (extension.equals(".obj")) {
            result = JFileLoaderOBJ.jLoadFileOBJ(aMesh, file);
            
        } // Load a .3ds file
        else if (extension.equals(".3ds")) {
            //result = JFileLoader3DS.loadFile3DS(aMesh, aFileName);
        }

        // if file has loaded, set the super parent to all child nodes.
        // the root (current object a_mesh) becomes the super parent of itself.
        if (result) {
            aMesh.setSuperParent(aMesh, true);
        }

        // return result
        return result;
    }
    
        public static boolean loadMeshFromFile(JMesh aMesh, URL url, File file, Observer observer) throws FileNotFoundException, IOException{

        // verify mesh object
        

        // retrieve filename
        String fileName = file.getName();
        int i = fileName.lastIndexOf(".");
        String extension = fileName.substring(i,fileName.length());

        // We need a file extension to figure out file type
        if (extension.length() == 0) {
            throw new IOException("Invalid file extension.");
        }
        // return value
        boolean result = false;

        // Load an .obj file
        if (extension.equals(".obj")) {
            result = JFileLoaderOBJ.jLoadFileOBJ(aMesh, url, file);
            
        } // Load a .3ds file
        else if (extension.equals(".3ds")) {
            //result = JFileLoader3DS.loadFile3DS(aMesh, aFileName);
        }

        // if file has loaded, set the super parent to all child nodes.
        // the root (current object a_mesh) becomes the super parent of itself.
        if (result) {
            aMesh.setSuperParent(aMesh, true);
        }

        // return result
        return result;
    }
}
