/*
 * This file is part of the JCHAI 3D visualization and haptics libraries.
 * Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License("GPL") version 2 as published by
 * the Free Software Foundation.
 *
 * For using the JCHAI 3D libraries with software that can not be combined with
 * the GNU GPL, and for taking advantage of the additional benefits of our
 * support services, please contact CHAI 3D about acquiring a Professional
 * Edition License.
 *
 * project <https://sourceforge.net/projects/jchai3d>
 */
package org.jchai3d.forces;

/**
 * This class contains a list of settings which are passed to the interaction
 * detector when checking for an interaction.
 *
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (java implementation)
 */
public class JInteractionSettings {

    /**
     * If <b>true</b>, then collision detector shall check for collisions on
     * haptic enabled objects only.
     */
    private boolean visibleObjectsOnly;
    /**
     * If <b>true</b>, then collision detector shall check for collisions on
     * haptic enabled objects only.
     */
    private boolean hapticObjectsOnly;

    /**
     * Default constructor
     */
    public JInteractionSettings() {
        visibleObjectsOnly = true;
        hapticObjectsOnly = true;
    }

    /**
     *
     * @param visibleObjectsOnly
     * @param hapticObjectsOnly
     */
    public JInteractionSettings(boolean visibleObjectsOnly, boolean hapticObjectsOnly) {
        this.visibleObjectsOnly = visibleObjectsOnly;
        this.hapticObjectsOnly = hapticObjectsOnly;
    }

    /**
     * @return the visibleObjectsOnly
     */
    public boolean isVisibleObjectsOnly() {
        return visibleObjectsOnly;
    }

    /**
     * @param visibleObjectsOnly the visibleObjectsOnly to set
     */
    public void setVisibleObjectsOnly(boolean visibleObjectsOnly) {
        this.visibleObjectsOnly = visibleObjectsOnly;
    }

    /**
     * @return the hapticObjectsOnly
     */
    public boolean isHapticObjectsOnly() {
        return hapticObjectsOnly;
    }

    /**
     * @param hapticObjectsOnly the hapticObjectsOnly to set
     */
    public void setHapticObjectsOnly(boolean hapticObjectsOnly) {
        this.hapticObjectsOnly = hapticObjectsOnly;
    }
}
