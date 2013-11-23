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


package org.jchai3d.math;

/**
 *
 * @author jairo simão
 */
public class JString {

    /**
     * compute the length of a string up to 255 characters.
     *
     * @param a_string
     * @return
     */
    public static int jStringLength(final char [] a_string){
            int counter = 0;
            while (counter < 256)
            {
                    if (a_string[counter] == 0) { return (counter); }
                    counter++;
            }
            return (-1);
    }

    /**
     * Convert a \e boolean into a \e string.
     *
     * @param a_string
     * @param a_value
     */
    public static void staticjStr(String a_string, final boolean a_value){
        if (a_value) a_string.concat("true");
        else a_string.concat("false");
    }

    /**
     * Convert an \e integer into a \e string.
     *
     * @param a_string
     * @param a_value
     */
    public static void jStr(String a_string, final int a_value) {
        String buffer = new String();
        System.out.printf(buffer, "%d", a_value);
        a_string.concat(buffer);
    }

    /**
     * Convert a \e float into a \e string.
     *
     * @param a_string
     * @param a_value
     * @param a_precision
     */
    public static void jStr(String a_string, final float a_value, final int a_precision){
        // make sure number of digits ranges between 0 and 20
        int numDigits = (int)a_precision;
        if (numDigits > 20)
        {
            numDigits = 20;
        }

        // if number of digits is zero, remove '.'
        if (numDigits == 0)
        {
                numDigits = -1;
        }

        String buffer = new String();
        System.out.printf(buffer, "%.20f", a_value);
        buffer.toCharArray()[ (jStringLength(buffer.toCharArray()) - 20 + numDigits) ] = '\0';
        double chopped_value = Float.parseFloat(buffer);
        double round_diff = a_value - chopped_value;
        double round_threshold = 0.5f*Math.pow(0.1f, (int)a_precision);
        if (Math.abs(round_diff) >= round_threshold)
        {
        double rounded_value;
                if (a_value >= 0.0) rounded_value = a_value + round_threshold;
                else rounded_value = a_value - round_threshold;

        System.out.printf(buffer, "%.20f", rounded_value);
            buffer.toCharArray()[ (jStringLength(buffer.toCharArray()) - 20 + numDigits) ] = '\0';
        }
        a_string.concat(buffer);
    }

    /**
     * Convert a \e double into a \e string.
     *
     * @param a_string
     * @param a_value
     * @param a_precision
     */
    public static void jStr(String a_string, final double a_value, final int a_precision){
        // make sure number of digits ranges between 0 and 20
        int numDigits = a_precision;
        if (numDigits > 20)
        {
          numDigits = 20;
        }

            // if number of digits is zero, remove '.'
            if (numDigits == 0)
            {
                    numDigits = -1;
            }

            String buffer = new String();
            System.out.printf(buffer, "%.20f", a_value);
            buffer.toCharArray()[ (jStringLength(buffer.toCharArray()) - 20 + numDigits) ] = '\0';
            double chopped_value = Float.parseFloat(buffer);
            double round_diff = a_value - chopped_value;
            double round_threshold = 0.5*Math.pow(0.1, (int)a_precision);
            if (Math.abs(round_diff) >= round_threshold)
            {
            double rounded_value;
                    if (a_value >= 0.0) rounded_value = a_value + round_threshold;
                    else rounded_value = a_value - round_threshold;

            System.out.printf(buffer, "%.20f", rounded_value);
                buffer.toCharArray()[ (jStringLength(buffer.toCharArray()) - 20 + numDigits) ] = '\0';
            }
            a_string.concat(buffer);
    }
}
