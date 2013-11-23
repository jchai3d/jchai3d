/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LCamera extends LObject {

    protected LVector3 pos;
    protected LVector3 target;
    protected float bank;
    protected float fov;
    protected float near;
    protected float far;

    // the default constructor
    LCamera() {
        Clear();
    }

    // clears the data the class holds
    void Clear() {
        setPos(new LVector3());
        setTarget(new LVector3());
        setFov(80);
        setBank(0);;
        setNear(10);
        setFar(10000);
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
     * @return the bank
     */
    public float getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(float bank) {
        this.bank = bank;
    }

    /**
     * @return the fov
     */
    public float getFov() {
        return fov;
    }

    /**
     * @param fov the fov to set
     */
    public void setFov(float fov) {
        this.fov = fov;
    }

    /**
     * @return the near
     */
    public float getNear() {
        return near;
    }

    /**
     * @param near the near to set
     */
    public void setNear(float near) {
        this.near = near;
    }

    /**
     * @return the far
     */
    public float getFar() {
        return far;
    }

    /**
     * @param far the far to set
     */
    public void setFar(float far) {
        this.far = far;
    }

    
}
