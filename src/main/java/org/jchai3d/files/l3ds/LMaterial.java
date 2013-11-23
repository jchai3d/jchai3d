/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LMaterial extends LObject {

    /**
     * the unique material ID
     */
    protected int id;
    /**
     * the first texture map
     */
    protected LMap textureMap1;
    /**
     * the second texture map
     */
    protected LMap textureMap2;
    /**
     * the opacity map
     */
    protected LMap opacityMap;
    /**
     * the reflection map
     */
    protected LMap reflectionMap;
    /**
     * the bump map
     */
    protected LMap bumpMap;
    /**
     * specular map
     */
    protected LMap specularMap;
    /**
     * material ambient color
     */
    protected LColor3 ambient;
    /**
     * material diffuse color
     */
    protected LColor3 diffuse;
    /**
     * material specular color
     */
    protected LColor3 specular;
    /**
     * shininess
     */
    protected float shininess;
    /**
     * transparency
     */
    protected float transparency;
    /**
     * the shading type for the material
     */
    protected LShading shading;

    /**
     * the default finalructor, does the initialization
     */
    LMaterial() {

        id = 0;
        textureMap1 = LGlobals.emptyMap;
        textureMap2 = LGlobals.emptyMap;
        opacityMap = LGlobals.emptyMap;
        bumpMap = LGlobals.emptyMap;
        reflectionMap = LGlobals.emptyMap;
        specularMap = LGlobals.emptyMap;
        ambient = LGlobals.black;
        diffuse = LGlobals.black;
        specular = LGlobals.black;
        shading = LShading.SHADING_GOURAD;
        shininess = 0;
        transparency = 0;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the textureMap1
     */
    public LMap getTextureMap1() {
        return textureMap1;
    }

    /**
     * @param textureMap1 the textureMap1 to set
     */
    public void setTextureMap1(LMap textureMap1) {
        this.textureMap1 = textureMap1;
    }

    /**
     * @return the textureMap2
     */
    public LMap getTextureMap2() {
        return textureMap2;
    }

    /**
     * @param textureMap2 the textureMap2 to set
     */
    public void setTextureMap2(LMap textureMap2) {
        this.textureMap2 = textureMap2;
    }

    /**
     * @return the opacityMap
     */
    public LMap getOpacityMap() {
        return opacityMap;
    }

    /**
     * @param opacityMap the opacityMap to set
     */
    public void setOpacityMap(LMap opacityMap) {
        this.opacityMap = opacityMap;
    }

    /**
     * @return the reflectionMap
     */
    public LMap getReflectionMap() {
        return reflectionMap;
    }

    /**
     * @param reflectionMap the reflectionMap to set
     */
    public void setReflectionMap(LMap reflectionMap) {
        this.reflectionMap = reflectionMap;
    }

    /**
     * @return the bumpMap
     */
    public LMap getBumpMap() {
        return bumpMap;
    }

    /**
     * @param bumpMap the bumpMap to set
     */
    public void setBumpMap(LMap bumpMap) {
        this.bumpMap = bumpMap;
    }

    /**
     * @return the specularMap
     */
    public LMap getSpecularMap() {
        return specularMap;
    }

    /**
     * @param specularMap the specularMap to set
     */
    public void setSpecularMap(LMap specularMap) {
        this.specularMap = specularMap;
    }

    /**
     * @return the ambient
     */
    public LColor3 getAmbient() {
        return ambient;
    }

    /**
     * @param ambient the ambient to set
     */
    public void setAmbient(LColor3 ambient) {
        this.ambient = ambient;
    }

    /**
     * @return the diffuse
     */
    public LColor3 getDiffuse() {
        return diffuse;
    }

    /**
     * @param diffuse the diffuse to set
     */
    public void setDiffuse(LColor3 diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * @return the specular
     */
    public LColor3 getSpecular() {
        return specular;
    }

    /**
     * @param specular the specular to set
     */
    public void setSpecular(LColor3 specular) {
        this.specular = specular;
    }

    /**
     * @return the shininess
     */
    public float getShininess() {
        return shininess;
    }

    /**
     * @param shininess the shininess to set
     */
    public void setShininess(float shininess) {

        this.shininess = shininess;
        if (this.shininess < 0) {
            this.shininess = 0;
        }
        if (this.shininess > 1) {
            this.shininess = 1;
        }
    }

    /**
     * @return the transparency
     */
    public float getTransparency() {
        return transparency;
    }

    /**
     * @param transparency the transparency to set
     */
    public void setTransparency(float transparency) {

        this.transparency = transparency;
        if (this.transparency < 0) {
            this.transparency = 0;
        }
        if (this.transparency > 1) {
            this.transparency = 1;
        }
    }

    /**
     * @return the shading
     */
    public LShading getShading() {
        return shading;
    }

    /**
     * @param shading the shading to set
     */
    public void setShading(LShading shading) {
        this.shading = shading;
    }
}
