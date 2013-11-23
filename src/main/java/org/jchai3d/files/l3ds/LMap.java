/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LMap {

    /**
     * the strength of the texture map
     */
    float strength;
    /**
     * the file name of the map. only 8.3 format in 3ds files :(
     */
    String mapName;
    float uScale;
    float vScale;
    float uOffset;
    float vOffset;
    float angle;

    public LMap(float strength, String mapName, float uScale, float vScale, float uOffset, float vOffset, float angle) {
        this.strength = strength;
        this.mapName = mapName;
        this.uScale = uScale;
        this.vScale = vScale;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.angle = angle;
    }

    public LMap() {
        this(-1,"",-1,-1,-1,-1,-1);
    }
}
