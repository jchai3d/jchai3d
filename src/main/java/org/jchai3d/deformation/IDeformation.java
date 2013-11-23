/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.deformation;

import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JMesh;

/**
 *
 * @author Jairo Melo <jairossmunb@gmail.com>
 */
public interface IDeformation {
        
    public void setVelocidade(JVector3d velocidade);
    
    public void setPosicao(JVector3d posicao);
    
    public void setForca(JVector3d forca);
    
    public void setTriangulosVizinhos(JTriangle triangle);
    
    public JMesh processDeformation(float time_pass, JMesh jMesh);
}
