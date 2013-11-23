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
 */
package org.jchai3d.scenegraph;

/**
 * Constants that define specific rendering passes
 *
 * @author Francois Conti (original author)
 * @author Dan Morris (original author)
 * @author Jairo Melo (java implementation)
 */
public enum JChaiRenderMode {

    CHAI_RENDER_MODE_RENDER_ALL,
    CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY,
    CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY,
    CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY;
}
