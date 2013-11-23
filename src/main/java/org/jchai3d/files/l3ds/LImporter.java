/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Marcos
 */
public abstract class LImporter {

    // the cameras found in the scene
    ArrayList<LCamera> cameras;
    // the lights found in the scene
    ArrayList<LLight> lights;
    // triangular meshes
    ArrayList<LMesh> meshes;
    // the materials in the scene
    ArrayList<LMaterial> materials;
    // level of optimization to perform on the meshes
    LOptimizationLevel optimizationLevel;

    // the default finalructor
    LImporter() {
        cameras = new ArrayList<LCamera>();
        lights = new ArrayList<LLight>();
        meshes = new ArrayList<LMesh>();
        materials = new ArrayList<LMaterial>();
        clear();
    }

    public void clear() {

        meshes.clear();
        lights.clear();
        cameras.clear();
        materials.clear();
        optimizationLevel = LOptimizationLevel.OPTIMZATION_SIMPLE;
    }

    // reads the model from a file, must be overriden by the child classes
    abstract boolean loadFile(final String filename) throws IOException, FileNotFoundException;

    // returns the number of meshes in the scene
    int getMeshCount() {
        return meshes.size();
    }

    // returns the number of lights in the scene
    int getLightCount() {
        return lights.size();
    }

    // returns the number of materials in the scene
    int getMaterialCount() {
        return materials.size();
    }

    // returns the number of cameras in the scene
    int getCameraCount() {
        return materials.size();
    }

    // returns a pointer to a mesh
    LMesh getMesh(int index) {
        return meshes.get(index);
    }

    // returns a pointer to a camera at a given index
    LCamera getCamera(int index) {
        return cameras.get(index);
    }

    // returns a pointer to a light at a given index
    LLight getLight(int index) {
        return lights.get(index);
    }

    // returns the pointer to the material
    LMaterial getMaterial(int index) {
        return materials.get(index);
    }

    // returns the pointer to the material with a given name, or NULL if the material was not found
    LMaterial findMaterial(final String name) {
        for (LMaterial material : materials) {
            if (material.isObject(name)) {
                return material;
            }
        }
        return null;
    }

    // returns the pointer to the mesh with a given name, or NULL if the mesh with such name
    // is not present in the scene
    LMesh findMesh(final String name) {
        for (LMesh mesh : meshes) {
            if (mesh.isObject(name)) {
                return mesh;
            }
        }
        return null;
    }

    // returns the pointer to the light with a given name, or NULL if not found
    LLight findLight(final String name) {
        for (LLight light : lights) {
            if (light.isObject(name)) {
                return light;
            }
        }
        return null;
    }

    // sets the optimization level to a given value
    void setOptimizationLevel(LOptimizationLevel value) {
        this.optimizationLevel = value;
    }

    // returns the current optimization level
    LOptimizationLevel getOptimizationLevel() {
        return optimizationLevel;
    }
}
