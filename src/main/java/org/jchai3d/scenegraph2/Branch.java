/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import java.util.ArrayList;


/**
 * A branch, as in a tree, is a collection of nodes.
 * @author Marcos da Silva Ramos
 */
public class Branch extends Node {

    /**
     * The childs of this branch
     */
    protected ArrayList <Node> childNodes;
    
    /**
     * Adds a new child node into this branch and return its internal index as
     * convenience.
     *
     * @param node the new child node to append in this branch.
     * @return the child node index as convenience.
     * @throws ScenegraphException if the specifies node is null or if it is 
     * already attached to another branch.
     */
    public int addChild(Node node) throws ScenegraphException {
        if (!childExists(node) && !node.attached) {
            childNodes.add(node);
            node.attached = true;
            node.setParent(this);
            return childNodes.size() - 1;
        } else {
            throw new ScenegraphException("Node is null or is already attached in other branch.");
        }
    }

    /**
     * Remove the referenced node from this branch.
     * @param node the node to be removed
     * @throws ScenegraphException if <code>node</node> is null or if it is not
     * attached to this branch.
     */
    public void removeChild(Node node) throws ScenegraphException{
        if(node == null || !childExists(node) || !node.attached)
            throw new ScenegraphException("Specified node is null or is not attached to this branch.");
        childNodes.remove(node);
        node.setAttached(false);
    }

    /**
     * Remove the node specified by the index from this branch.
     * @param index
     * @throws ScenegraphException if index is less then zero of if index is higher
     * than the last node's index.
     */
    public void removeChild(int index) throws ScenegraphException {
        if(index < 0 || index >=childNodes.size())
            throw new ScenegraphException("Node index ["+index+"] is invalid.");
        childNodes.get(index).setAttached(false);
        childNodes.remove(index);
    }

    
    /**
     * Tests if specified if node exists in this, and only in this branch.
     * @param node the node to be tested
     * @return <code>true</code> if, and only if, 
     */
    public boolean childExists(Node node) {
        return childExists(node, true);
    }
    /**
     * Tests if specified node exists, optionally checking in child branches.
     * @param node the node to be tested
     * @param recursive if true, then this method recursivelly call this method
     * on branch-type nodes.
     * @return <code>true</code> if, and only if, 
     */
    public boolean childExists(Node node,boolean recursive) {
        if (node == null) {
            return false;
        }

        for (Node child : childNodes) {
            
            if (child == node) {
                return true;
            }
            
            if(child instanceof Branch && recursive) {
                if(((Branch)child).childExists(node, true))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void init() {
        for(Node node: childNodes) {
            node.init();
        }
    }
}
