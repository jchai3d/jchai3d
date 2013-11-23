/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.math;

/**
 *
 * @author Usuário
 */
public class TexCoord2d {
    
    public double s;
    
    public double t;

    public TexCoord2d() {
        s = 0;
        t = 0;
    }

    public TexCoord2d(double s, double t) {
        this.s = s;
        this.t = t;
    }
    /**
     * @return the s
     */
    public double getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(double s) {
        this.s = s;
    }

    /**
     * @return the t
     */
    public double getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(double t) {
        this.t = t;
    }
    
    public double[] toArray() {
        return new double[]{s,t};
    }
}
