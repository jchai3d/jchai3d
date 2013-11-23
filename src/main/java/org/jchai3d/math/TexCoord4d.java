/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.math;

/**
 *
 * @author Usuário
 */
public class TexCoord4d extends TexCoord3d {

    public double q;

    public TexCoord4d() {
        super();
        q = 0;
    }

    
    public TexCoord4d(double s, double t, double r, double q) {
        super(r, s, t);
        this.q = q;
    }

    /**
     * @return the q
     */
    public double getQ() {
        return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ(double q) {
        this.q = q;
    }

    @Override
    public double[] toArray() {
        return new double[]{s,t,r,q};
    }
}
