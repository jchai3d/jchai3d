/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LGlobals {

    public static final int SEEK_START = 1900;
    public static final int SEEK_CURSOR = 1901;
    public static final int COLOR_F = 0x0010;
    public static final int COLOR_24 = 0x0011;
    public static final int LIN_COLOR_24 = 0x0012;
    public static final int LIN_COLOR_F = 0x0013;
    public static final int INT_PERCENTAGE = 0x0030;
    public static final int FLOAT_PERCENTAGE = 0x0031;
    public static final int AMBIENT_LIGHT = 0x2100;
    public static final int MAIN3DS = 0x4D4D;
    public static final int EDIT3DS = 0x3D3D;
    public static final int KFDATA = 0xB000;
    public static final int KFHDR = 0xB00A;
    public static final int OBJECT_NODE_TAG = 0xB002;
    public static final int NODE_HDR = 0xB010;
    public static final int PIVOT = 0xB013;
    public static final int POS_TRACK_TAG = 0xB020;
    public static final int ROT_TRACK_TAG = 0xB021;
    public static final int SCL_TRACK_TAG = 0xB022;
    public static final int MAT_ENTRY = 0xAFFF;
    public static final int MAT_NAME = 0xA000;
    public static final int MAT_AMBIENT = 0xA010;
    public static final int MAT_DIFFUSE = 0xA020;
    public static final int MAT_SPECULAR = 0xA030;
    public static final int MAT_SHININESS = 0xA040;
    public static final int MAT_SHIN2PCT = 0xA041;
    public static final int MAT_TRANSPARENCY = 0xA050;
    public static final int MAT_SHADING = 0xA100;
    public static final int MAT_TWO_SIDE = 0xA081;
    public static final int MAT_ADDITIVE = 0xA083;
    public static final int MAT_WIRE = 0xA085;
    public static final int MAT_FACEMAP = 0xA088;
    public static final int MAT_WIRESIZE = 0xA087;
    public static final int MAT_DECAL = 0xA082;
    public static final int MAT_TEXMAP = 0xA200;
    public static final int MAT_MAPNAME = 0xA300;
    public static final int MAT_MAP_TILING = 0xA351;
    public static final int MAT_MAP_USCALE = 0xA354;
    public static final int MAT_MAP_VSCALE = 0xA356;
    public static final int MAT_MAP_UOFFSET = 0xA358;
    public static final int MAT_MAP_VOFFSET = 0xA35A;
    public static final int MAT_MAP_ANG = 0xA35C;
    public static final int MAT_TEX2MAP = 0xA33A;
    public static final int MAT_OPACMAP = 0xA210;
    public static final int MAT_BUMPMAP = 0xA230;
    public static final int MAT_SPECMAP = 0xA204;
    public static final int MAT_SHINMAP = 0xA33C;
    public static final int MAT_REFLMAP = 0xA220;
    public static final int MAT_ACUBIC = 0xA310;
    public static final int EDIT_OBJECT = 0x4000;
    public static final int OBJ_TRIMESH = 0x4100;
    public static final int OBJ_LIGHT = 0x4600;
    public static final int OBJ_CAMERA = 0x4700;
    public static final int CAM_RANGES = 0x4720;
    public static final int LIT_OFF = 0x4620;
    public static final int LIT_SPOT = 0x4610;
    public static final int LIT_INRANGE = 0x4659;
    public static final int LIT_OUTRANGE = 0x465A;
    public static final int TRI_VERTEXLIST = 0x4110;
    public static final int TRI_VERTEXOPTIONS = 0x4111;
    public static final int TRI_FACELIST = 0x4120;
    public static final int TRI_MAT_GROUP = 0x4130;
    public static final int TRI_SMOOTH_GROUP = 0x4150;
    public static final int TRI_FACEMAPPING = 0x4140;
    public static final int TRI_MATRIX = 0x4160;
    public static final int SPOTLIGHT = 0x4610;
    public static final int MAX_SHARED_TRIS = 100;
    public static final LColor3 black = new LColor3(0, 0, 0);
    public static final LVector3 zero3 = new LVector3(0, 0, 0);
    public static final LVector4 zero4 = new LVector4(0, 0, 0, 0);
    public static final LMap emptyMap = new LMap(0, "", 1, 1, 0, 0, 0);
}
