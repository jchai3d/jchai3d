/*
 *   This file is part of the JCHAI 3D visualization and haptics libraries.
 *   Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License("GPL") version 2
 *   as published by the Free Software Foundation.
 *
 *   For using the JCHAI 3D libraries with software that can not be combined
 *   with the GNU GPL, and for taking advantage of the additional benefits
 *   of our support services, please contact CHAI 3D about acquiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */
package org.jchai3d.deformation.fisics;

import org.jchai3d.deformation.IDeformation;
import org.jchai3d.forces.JProxyPointForceAlgo;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JMesh;

/**
 *
 * @author jairo
 */
public class JSpringMass implements IDeformation {

    private double K = 0.01;
    private double M = 1.0;
    private JVector3d oldVelocidade;
    private JVector3d newVelocidade;
    private JVector3d forca;
    private JVector3d posicao;
    private JTriangle triangle;
    
    public JSpringMass() {
        oldVelocidade = new JVector3d();
        newVelocidade = new JVector3d();
        forca = new JVector3d();
        posicao = new JVector3d();

    }

    private JVector3d getForca() {
        return forca;
    }

    public void setForca(JVector3d forca) {
        this.forca = forca;
    }

    private JVector3d getPosicao() {
        return posicao;
    }

    public void setPosicao(JVector3d posicao) {
        this.posicao = posicao;
    }

    private double getConstant() {
        return -(1.0 - K);
    }

    private double getMass() {
        return (M);
    }

    @Override
    public JMesh processDeformation(float time_pass, JMesh jMesh) {

        //F = k.X

        //F = Força
        //K = Constante de deformaç?o
        //X = Posiç?o de Deformaç?o

        // 1? (Calccular a nova deformaç?o) X = F/-K

        JVector3d newPosition = JMaths.jDiv(getConstant(), getForca());

        // 2? Gera vetor de aceleraç?o
        // a = F/m

        JVector3d aceleration = JMaths.jDiv(getMass(), getForca());

        // 3? (Atualizar a nova posiç?o na mesh) S = S0 + Vot + at^2/2
        // 

        JVector3d part4 = JMaths.jMul(Math.pow(time_pass, 2), aceleration);

        JVector3d part3 = JMaths.jDiv(2, part4);

        JVector3d part2 = JMaths.jMul(time_pass, oldVelocidade);

        JVector3d posicaoAtual = JMaths.jAdd(getPosicao(), part2, JMaths.jAdd(part3, part4));

        triangle.getVertex0().setPosition(posicaoAtual);

        for (JTriangle tri : triangle.getNeighbors()) {
            
            System.out.println("Tri V=" + tri.getIndex());
            tri.getVertex0().setPosition(posicaoAtual);
            
        }


        return null;
    }

    @Override
    public void setVelocidade(JVector3d velocidade) {
        this.oldVelocidade = velocidade;
    }

    @Override
    public void setTriangulosVizinhos(JTriangle triangle) {
        this.triangle = triangle;
    }
}
