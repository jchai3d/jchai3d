/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * The Node class is the parent of all scenegraph-related classes.
 * 
 * @author Marcos da Silva Ramos
 */
public abstract class Node extends GenericObject{
    
    /**
     * Parent node of this node.
     */
    protected Node parent;
    
    /**
     * Each node can only be added to one branch.
     */
    protected boolean attached;
    
    /**
     * Is this node enabled?
     */
    protected boolean enabled;
    
    /**
     * The local position of this node.
     */
    protected JVector3d localPosition;
    
    /**
     * The previous position of this node, in local coordinates.
     */
    protected JVector3d previousLocalPosition;
    
    /**
     * The position of this node, with its root branch as reference.
     */
    protected JVector3d globalPosition;
    
    /**
     * The previous global position
     */
    protected JVector3d previousGlobalPosition;
    
    /**
     * The local orientation of this node.
     */
    protected JMatrix3d localRotation;
    
    /**
     * The global orientation of this node
     */
    protected JMatrix3d globalRotation;
    
    /**
     * 
     */
    protected JMatrix3d previousLocalRotation;
    
    /**
     * 
     */
    protected JMatrix3d previousGlobalRotation;
    
    /**
     * Inform if this node should be updated.
     */
    protected boolean markedToUpdate;
    

    public Node() {
        markedToUpdate = false;
    }
    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    protected void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * @return the attached
     */
    public boolean isAttached() {
        return attached;
    }

    /**
     * @param attached the attached to set
     */
    public void setAttached(boolean attached) {
        this.attached = attached;
        if(!attached) {
            this.parent = null;
        }
    }
    
    /**
     * This method is called when the viewport calls its init() method. Here
     * the node should initialize any properties or set any OpenGL states
     * that it requires.
     */
    public abstract void init();
    
    
    /**
     * 
     */
    public void update() {
        
    }
    
    /**
     * 
     */
    /**
     * Tell to this node that it should update its content.
     */
    public void markForUpdate() {
        this.markedToUpdate = true;
    }
    

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the localPosition
     */
    public JVector3d getLocalPosition() {
        return localPosition;
    }

    /**
     * @param localPosition the localPosition to set
     */
    public void setLocalPosition(JVector3d localPosition) {
        this.localPosition.copyFrom(localPosition);
    }

    /**
     * @return the globalPosition
     */
    public JVector3d getGlobalPosition() {
        return globalPosition;
    }

    /**
     * @param globalPosition the globalPosition to set
     */
    public void setGlobalPosition(JVector3d globalPosition) {
        this.globalPosition.copyFrom(globalPosition);
    }

    /**
     * @return the localRotation
     */
    public JMatrix3d getLocalRotation() {
        return localRotation;
    }

    /**
     * @param localRotation the localRotation to set
     */
    public void setLocalRotation(JMatrix3d localRotation) {
        this.localRotation.copyFrom(localRotation);
    }

    /**
     * @return the globalRotation
     */
    public JMatrix3d getGlobalRotation() {
        return globalRotation;
    }

    /**
     * @param globalRotation the globalRotation to set
     */
    public void setGlobalRotation(JMatrix3d globalRotation) {
        this.globalRotation.copyFrom(globalRotation);
    }

    /**
     * @return the previousLocalRotation
     */
    public JMatrix3d getPreviousLocalRotation() {
        return previousLocalRotation;
    }

    /**
     * Retrieves the previous rotation of this node.
     * @return the previousGlobalRotation
     */
    public JMatrix3d getPreviousGlobalRotation() {
        return previousGlobalRotation;
    }

    /**
     * Tells if this node is currently marked for update
     * @return the markedToUpdate
     */
    public boolean isMarkedToUpdate() {
        return markedToUpdate;
    }
}
