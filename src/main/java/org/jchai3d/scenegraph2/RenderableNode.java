package org.jchai3d.scenegraph2;

/**
 * <p>The RenderableNode class is the superclass of every OpenGL renderable node
 * in JCHAI3D.</p>
 * <p>Users can choose among two rendering policies: rendering this node every
 * frame or render this node only when updated.</p>
 * 
 * @author Marcos da Silva Ramos
 */
public abstract class RenderableNode extends Node{
    
    /**
     * The rendering policy of this node.
     */
    private RenderPolicy renderingPolicy;
    
    /**
     * When this node rendering policy is set as {@link RenderPolicy#AS_NEEDED},
     * this field must be set as <code>true</code> to this object to be
     * rendered.
     */
    private boolean markedForRenderingUpdate;

    /**
     * Default constructor of RenderableNode. Initialized this RenderableNode
     * with {@link RenderPolicy#AS_NEEDED} policy and already marked
     * for rendering.
     */
    public RenderableNode() {
        super();
        this.renderingPolicy = RenderPolicy.AS_NEEDED;
        this.markedForRenderingUpdate = true;
    }

    public RenderableNode(RenderPolicy renderingPolicy) {
        this.renderingPolicy = renderingPolicy;
    }
    
    
    /**
     * Render this node in OpenGL. Subclasses implementation must render
     * its content in this method.
     */
    public abstract void renderNode();

    /**
     * @return the renderingPolicy
     */
    public RenderPolicy getRenderingPolicy() {
        return renderingPolicy;
    }

    /**
     * @param renderingPolicy the renderingPolicy to set
     */
    public void setRenderingPolicy(RenderPolicy renderingPolicy) {
        this.renderingPolicy = renderingPolicy;
    }

    /**
     * @return the markedForRenderingUpdate
     */
    public boolean isMarkedForRenderingUpdate() {
        return markedForRenderingUpdate;
    }
    
    /**
     * <p>When this method is called and the rendering policy is setted as
     * {@link RenderPolicy#AS_NEEDED} this node will call the
     * {@link RenderableNode#renderNode()} implementation of subclasses in the
     * next frame rendering.</p>
     * <p>If the rendering policy is set as {@link RenderPolicy#RENDER_ON_ALWAYS}
     * then a call for this method have no effects.</p>
     */
    public void markForRenderingUpdate() {
        this.markedForRenderingUpdate = true;
    }
}
