/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.math;

/**
 *
 * @author Usuário
 */
public class TexCoord3d extends TexCoord2d{

    public double r;
    
    public TexCoord3d() {
        super();
        this.r = 0;
    }
    
    public TexCoord3d(double s, double t, double r) {
        super(s, t);
        this.r = r;
    }
    

    /**
     * @return the r
     */
    public double getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(double r) {
        this.r = r;
    }

    @Override
    public double[] toArray() {
        return new double[]{s,t,r};
    }
    
    
}
