/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LLight extends LObject{

    protected LVector3 pos;
    protected LColor3 color;
    protected boolean spotlight;
    protected LVector3 target;
    protected float hotspot;
    protected float falloff;
    protected float attenuationstart;
    protected float attenuationend;

    // the default constructor
    LLight() {
        clear();
    }

    // clears the data the class holds
    void clear() {
        setPos(new LVector3());
        setColor(new LColor3());
        setSpotlight(false);
        setAttenuationend(100);
        setAttenuationstart(1000);
    }

    /**
     * @return the pos
     */
    public LVector3 getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(LVector3 pos) {
        this.pos = pos;
    }

    /**
     * @return the color
     */
    public LColor3 getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(LColor3 color) {
        this.color = color;
    }

    /**
     * @return the spotlight
     */
    public boolean isSpotlight() {
        return spotlight;
    }

    /**
     * @param spotlight the spotlight to set
     */
    public void setSpotlight(boolean spotlight) {
        this.spotlight = spotlight;
    }

    /**
     * @return the target
     */
    public LVector3 getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(LVector3 target) {
        this.target = target;
    }

    /**
     * @return the hotspot
     */
    public float getHotspot() {
        return hotspot;
    }

    /**
     * @param hotspot the hotspot to set
     */
    public void setHotspot(float hotspot) {
        this.hotspot = hotspot;
    }

    /**
     * @return the falloff
     */
    public float getFalloff() {
        return falloff;
    }

    /**
     * @param falloff the falloff to set
     */
    public void setFalloff(float falloff) {
        this.falloff = falloff;
    }

    /**
     * @return the attenuationstart
     */
    public float getAttenuationstart() {
        return attenuationstart;
    }

    /**
     * @param attenuationstart the attenuationstart to set
     */
    public void setAttenuationstart(float attenuationstart) {
        this.attenuationstart = attenuationstart;
    }

    /**
     * @return the attenuationend
     */
    public float getAttenuationend() {
        return attenuationend;
    }

    /**
     * @param attenuationend the attenuationend to set
     */
    public void setAttenuationend(float attenuationend) {
        this.attenuationend = attenuationend;
    }

}
