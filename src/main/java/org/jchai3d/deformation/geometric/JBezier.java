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

package org.jchai3d.deformation.geometric;

import org.jchai3d.deformation.IDeformation;
import org.jchai3d.forces.JProxyPointForceAlgo;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JMesh;

/**
 * 
 * @author jairo
 */
public class JBezier implements IDeformation {

	public JMesh processDeformation(float time_pass, JMesh jMesh) {
		return null;
	}

	public void setVelocidade(JVector3d velocidade) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setPosicao(JVector3d posicao) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setForca(JVector3d forca) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setTriangulosVizinhos(JTriangle triangle) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
