/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;
import org.jchai3d.files.l3ds.LGlobals.*;

/**
 *
 * @author Marcos
 */
public class L3DS extends LImporter {

    String objectName;
    byte[] buffer;
    int bufferSize;
    int position;
    boolean eof;

    public L3DS() {
    }

    public L3DS(String fileName) throws FileNotFoundException, IOException {
        this();
        loadFile(fileName);
    }

    @Override
    boolean loadFile(String filename) throws FileNotFoundException, IOException {

        RandomAccessFile file = new RandomAccessFile(filename, "r");
        bufferSize = (int) file.length();
        buffer = new byte[bufferSize];
        position = 0;
        file.read(buffer, 0, bufferSize);
        file.close();

        clear();
        boolean res = read3DS();

        /**
         * *
         *
         * Added by Dan Morris.
         *
         * Populates the vertex color list from the material array, using
         * diffuse colors, to approximate the correct colors.
         *
         **
         */
        if (this.getMaterialCount() > 0) {
            int last_mat = 0;

            // Now build colors for each triangle...
            for (int i = 0; i < this.getMeshCount(); i++) {

                LMesh mesh = this.getMesh(i);

                // For each triangle
                for (int j = 0; j < mesh.tris.size(); j++) {
                    int mat_id = mesh.tris.get(j).materialId;

                    if (mat_id >= this.getMaterialCount()) {
                        mat_id = last_mat;
                    }

                    last_mat = mat_id;

                    LMaterial mat = this.getMaterial(mat_id);

                    LColor3 c = mat.getDiffuse();

                    // Set each vertex's color          
                    mesh.setColor(c, mesh.tris.get(j).a);
                    mesh.setColor(c, mesh.tris.get(j).b);
                    mesh.setColor(c, mesh.tris.get(j).c);
                }
            }

        }

        return res;
    }

    // reads a short value from the buffer
    int readShort() throws IOException {
        if ((buffer != null) && (bufferSize != 0) && ((position + 2) < bufferSize)) {
            int s = (buffer[position + 1] << 8) | (buffer[position]);
            position += 2;
            return s;
        }
        eof = true;
        return 0;
    }

    // reads an int value from the buffer
    int readInt() throws IOException {
        if ((buffer != null) && (bufferSize != 0) && ((position + 4) < bufferSize)) {
            int i = (buffer[position + 3] << 24) | (buffer[position + 2] << 16) + (buffer[position + 1] << 8) + (buffer[position]);
            position += 4;
            return i;
        }
        eof = true;
        return 0;
    }

    // reads a char from the buffer
    int readChar() throws IOException {
        if ((buffer != null) && (bufferSize != 0) && ((position + 1) < bufferSize)) {
            int c = buffer[position];
            position += 1;
            return c;
        }
        eof = true;
        return 0;
    }

    // reads a float value from the buffer
    float readFloat() throws IOException {
        if ((buffer != null) && (bufferSize != 0) && ((position + 1) < bufferSize)) {
            int bits = readInt();
            float f = Float.intBitsToFloat(bits);
            return f;
        }
        eof = true;
        return 0;
    }

    // reads an unsigned byte from the buffer
    int readByte() throws IOException {
        if ((buffer != null) && (bufferSize != 0) && ((position + 1) < bufferSize)) {
            int b = buffer[position];
            position += 1;
            return b;
        }
        eof = true;
        return 0;
    }

    // reads an asciiz string
    String readASCIIZ(int max_count) throws IOException {

        StringBuilder buff = new StringBuilder(max_count);
        char c = (char) readChar();
        int count = 0;

        while (c != 0 && count < (max_count - 1)) {
            buff.append(c);
            count++;
            c = (char) readChar();
        }

        if (buff.length() == 0) {
            return null;
        }

        return buff.toString();
    }

    // seek within the buffer
    void seek(int offset, int origin) throws IOException {
        if (origin == LGlobals.SEEK_START) {
            position = Math.max(0, offset);
        }
        if (origin == LGlobals.SEEK_CURSOR) {
            if (offset < 0 && (Math.abs(offset)) > position) {
                position = 0;
            } else {
                position += offset;
            }
        }
        if (position >= bufferSize) {
            position = bufferSize - 1;
        }
        eof = false;
    }

    // returns the position of the cursor
    int pos() throws IOException {
        return position;
    }

    // read the chunk and return it.
    LChunk readChunk() throws IOException {
        LChunk chunk = new LChunk();
        chunk.id = readShort();
        int a = readInt();
        chunk.start = position;
        chunk.end = chunk.start + a - 6;
        return chunk;
    }

    // read until given chunk is found
    boolean findChunk(LChunk target, final LChunk parent) throws IOException {
        
        if (position >= parent.end) {
            return false;
        }
        LChunk chunk;
        chunk = readChunk();
        while ((chunk.id != target.id) && (chunk.end <= parent.end)) {
            skipChunk(chunk);
            if (chunk.end >= parent.end) {
                break;
            }
            chunk = readChunk();
        }
        if (chunk.id == target.id) {
            target.start = chunk.start;
            target.end = chunk.end;
            return true;
        }
        return false;
    }

    // skip to the end of chunk "chunk"
    void skipChunk(final LChunk chunk) throws IOException {
        seek(chunk.end, LGlobals.SEEK_START);
    }

    // goes to the beginning of the data in the given chunk
    void gotoChunk(final LChunk chunk) throws IOException {
        seek(chunk.start, LGlobals.SEEK_START);
    }

    // the function read the color chunk (any of the color chunks)
    LColor3 readColor(final LChunk chunk) throws IOException {
        LColor3 col = LGlobals.black;
        gotoChunk(chunk);
        switch (chunk.id) {
            case LGlobals.COLOR_F:
                col.r = readFloat();
                col.g = readFloat();
                col.b = readFloat();
                break;
            case LGlobals.COLOR_24:
                col.r = readByte() / 255.0f;
                col.g = readByte() / 255.0f;
                col.b = readByte() / 255.0f;
                break;
            case LGlobals.LIN_COLOR_F:
                col.r = readFloat();
                col.g = readFloat();
                col.b = readFloat();
                break;
            case LGlobals.LIN_COLOR_24:
                col.r = readByte() / 255.0f;
                col.g = readByte() / 255.0f;
                col.b = readByte() / 255.0f;
                break;
            default:
                throw new IOException("L3DS::ReadColor - error this is not a color chunk");
        }
        return col;
    }

    // the function that read the percentage chunk and returns a float from 0 to 1
    float readPercentage(final LChunk chunk) throws IOException {
        gotoChunk(chunk);
        switch (chunk.id) {
            case LGlobals.INT_PERCENTAGE:
                return (readShort() / 100.0f);
            case LGlobals.FLOAT_PERCENTAGE:
                return readFloat();
            default:
                throw new IOException("L3DS::ReadPercentage - error, the chunk is not a percentage chunk");
        }

    }

    // this is where 3ds file is being read
    boolean read3DS() throws IOException {

        LChunk mainchunk;
        LChunk edit = new LChunk();
        edit.id = LGlobals.EDIT3DS;
        mainchunk = readChunk();
        if (mainchunk.id != LGlobals.MAIN3DS) {
            throw new IOException("L3DS::Read3DS - wrong file format");
        }
        if (!findChunk(edit, mainchunk)) {
            return false;
        }
        LChunk obj = new LChunk();
        LChunk ml;

        gotoChunk(edit);
        obj.id = LGlobals.MAT_ENTRY;
        while (findChunk(obj, edit)) {
            readMaterial(obj);
            skipChunk(obj);
        }
        gotoChunk(edit);

        obj.id = LGlobals.EDIT_OBJECT;
        {
            while (findChunk(obj, edit)) {
                objectName = readASCIIZ(99);
                ml = readChunk();
                if (ml.id == LGlobals.OBJ_TRIMESH) {
                    readMesh(ml);
                } else if (ml.id == LGlobals.OBJ_LIGHT) {
                    readLight(ml);
                } else if (ml.id == LGlobals.OBJ_CAMERA) {
                    readCamera(ml);
                }
                skipChunk(obj);
            }
        }

        // read the keyframer data here to find out correct object orientation

        LChunk keyframer = new LChunk();
        keyframer.id = LGlobals.KFDATA;

        LChunk objtrack = new LChunk();
        objtrack.id = LGlobals.OBJECT_NODE_TAG;

        gotoChunk(mainchunk);
        if (findChunk(keyframer, mainchunk)) {   // keyframer chunk is present
            gotoChunk(keyframer);
            while (findChunk(objtrack, keyframer)) {
                readKeyframeData(objtrack);
                skipChunk(objtrack);
            }
        }

        for (LMesh mesh : meshes) {
            mesh.optimize(optimizationLevel);
        }
        position = 0;
        objectName = "";
        return true;
    }

    // read a light chunk
    void readLight(final LChunk parent) throws IOException {

        float t;
        LVector3 v = new LVector3();
        LLight light = new LLight();
        light.setName(objectName);
        v.x = readFloat();
        v.y = readFloat();
        v.z = readFloat();
        light.setPos(v);
        LChunk chunk = readChunk();
        while (chunk.end <= parent.end) {
            switch (chunk.id) {
                case LGlobals.COLOR_24:
                case LGlobals.COLOR_F:
                case LGlobals.LIN_COLOR_F:
                case LGlobals.LIN_COLOR_24:
                    light.setColor(readColor(chunk));
                    break;
                case LGlobals.SPOTLIGHT:
                    v.x = readFloat();
                    v.y = readFloat();
                    v.z = readFloat();
                    light.setTarget(v);
                    t = readFloat();
                    light.setHotspot(t);
                    t = readFloat();
                    light.setFalloff(t);
                    break;
                case LGlobals.LIT_INRANGE:
                    light.setAttenuationstart(readFloat());
                    break;
                case LGlobals.LIT_OUTRANGE:
                    light.setAttenuationend(readFloat());
                    break;
                default:
                    break;
            }
            skipChunk(chunk);
            if (chunk.end >= parent.end) {
                break;
            }
            chunk = readChunk();

        }
        lights.add(light);
    }

    // read a camera chunk
    void readCamera(final LChunk parent) throws IOException {
        LVector3 v = new LVector3(), t = new LVector3();
        LCamera camera = new LCamera();
        camera.setName(objectName);
        v.x = readFloat();
        v.y = readFloat();
        v.z = readFloat();
        t.x = readFloat();
        t.y = readFloat();
        t.z = readFloat();
        camera.setPos(v);
        camera.setTarget(t);
        camera.setBank(readFloat());
        camera.setFov(2400.0f / readFloat());
        LChunk chunk = readChunk();
        while (chunk.end <= parent.end) {
            switch (chunk.id) {
                case LGlobals.CAM_RANGES:
                    camera.setNear(readFloat());
                    camera.setFar(readFloat());
                    break;
                default:
                    break;
            }
            skipChunk(chunk);
            if (chunk.end >= parent.end) {
                break;
            }
            chunk = readChunk();

        }
        cameras.add(camera);
    }

    // read a trimesh chunk
    void readMesh(final LChunk parent) throws IOException {

        int count, i;
        LVector4 p = new LVector4();
        LMatrix4 m = new LMatrix4();
        LVector2 t = new LVector2();
        p.w = 1.0f;
        LMesh mesh = new LMesh();
        mesh.setName(objectName);
        gotoChunk(parent);
        LChunk chunk = readChunk();
        while (chunk.end <= parent.end) {
            switch (chunk.id) {
                case LGlobals.TRI_VERTEXLIST:
                    count = readShort();
                    mesh.setVertexArraySize(count);
                    for (i = 0; i < count; i++) {
                        p.x = readFloat();
                        p.y = readFloat();
                        p.z = readFloat();
                        mesh.setVertex(p, i);
                    }
                    break;
                case LGlobals.TRI_FACEMAPPING:
                    count = readShort();
                    if (mesh.getVertexCount() == 0) {
                        mesh.setVertexArraySize(count);
                    }
                    for (i = 0; i < count; i++) {
                        t.x = readFloat();
                        t.y = readFloat();
                        mesh.setUV(t, i);
                    }
                    break;
                case LGlobals.TRI_FACELIST:

                    readFaceList(chunk, mesh);
                    break;
                case LGlobals.TRI_MATRIX:
                    m._11 = readFloat();
                    m._12 = readFloat();
                    m._13 = readFloat();

                    m._21 = readFloat();
                    m._22 = readFloat();
                    m._23 = readFloat();

                    m._31 = readFloat();
                    m._32 = readFloat();
                    m._33 = readFloat();

                    /*
                     * m._41 = ReadFloat(); m._42 = ReadFloat(); m._43 =
                     * ReadFloat();
                     */

                    m._41 = 0.0f;
                    m._42 = 0.0f;
                    m._43 = 0.0f;

                    m._14 = 0.0f;
                    m._24 = 0.0f;
                    m._34 = 0.0f;
                    m._44 = 1.0f;

                    mesh.setMatrix(m);

                    break;
                default:
                    break;
            }
            skipChunk(chunk);
            if (chunk.end >= parent.end) {
                break;
            }
            chunk = readChunk();
        }
        meshes.add(mesh);
    }

    // reads the face list, face materials, smoothing groups... and fill the information into the mesh
    void readFaceList(final LChunk chunk, LMesh mesh) throws IOException {

        // variables 
        int count, t;
        int i;
        LTri tri = new LTri();
        LChunk ch;
        String str;
        //uint mat;

        // consistency checks
        if (chunk.id != LGlobals.TRI_FACELIST) {
            throw new IOException("L3DS::ReadFaceList - internal error: wrong chunk passed as parameter");
        }
        gotoChunk(chunk);
        tri.smoothingGroups = 1;
        // read the number of faces
        count = readShort();
        mesh.setTriangleArraySize(count);

        for (i = 0; i < count; i++) {
            tri.a = readShort();
            tri.b = readShort();
            tri.c = readShort();
            readShort();
            mesh.setTri(tri, i);
        }

        // now read the optional chunks
        ch = readChunk();
        int mat_id = 0;
        while (ch.end <= chunk.end) {
            LMaterial mat;

            switch (ch.id) {
                case LGlobals.TRI_MAT_GROUP:
                    str = readASCIIZ(20);
                    mat = findMaterial(str);

                    if (mat != null) {
                        mat_id = mat.getId();
                        mesh.AddMaterial(mat_id);
                    }

                    count = readShort();

                    for (i = 0; i < count; i++) {

                        t = readShort();
                        if (mat != null) {
                            mesh.getTri(t).materialId = mat_id;
                        }
                    }

                    break;
                case LGlobals.TRI_SMOOTH_GROUP:
                    for (i = 0; i < mesh.getTriangleCount(); i++) {
                        mesh.getTri(i).smoothingGroups = readInt();
                    }
                    break;
            }
            skipChunk(ch);
            ch = readChunk();
        }
    }

    // reads the material
    void readMaterial(final LChunk parent) throws IOException {
        // variables
        LChunk chunk;
        LChunk child;
        String str;
        LMaterial mat = new LMaterial();
        int sh;

        gotoChunk(parent);

        chunk = readChunk();
        while (chunk.end <= parent.end) {
            switch (chunk.id) {
                case LGlobals.MAT_NAME:
                    str = readASCIIZ(30);
                    mat.setName(str);
                    break;
                case LGlobals.MAT_AMBIENT:
                    child = readChunk();
                    mat.setAmbient(readColor(child));
                    break;
                case LGlobals.MAT_DIFFUSE:
                    child = readChunk();
                    mat.setDiffuse(readColor(child));
                    break;
                case LGlobals.MAT_SPECULAR:
                    child = readChunk();
                    mat.setSpecular(readColor(child));
                    break;
                case LGlobals.MAT_SHININESS:
                    child = readChunk();
                    mat.setShininess(readPercentage(child));
                    break;
                case LGlobals.MAT_TRANSPARENCY:
                    child = readChunk();
                    mat.setTransparency(readPercentage(child));
                    break;
                case LGlobals.MAT_SHADING:
                    sh = readShort();
                    switch (sh) {
                        case 0:
                            mat.setShading(LShading.SHADING_WIREFRAME);
                            break;
                        case 1:
                            mat.setShading(LShading.SHADING_FLAT);
                            break;
                        case 2:
                            mat.setShading(LShading.SHADING_GOURAD);
                            break;
                        case 3:
                            mat.setShading(LShading.SHADING_PHONG);
                            break;
                        case 4:
                            mat.setShading(LShading.SHADING_METAL);
                            break;
                    }
                    break;
                case LGlobals.MAT_WIRE:
                    mat.setShading(LShading.SHADING_WIREFRAME);
                    break;
                case LGlobals.MAT_TEXMAP:

                    readMap(chunk, mat.getTextureMap1());
                    break;
                case LGlobals.MAT_TEX2MAP:
                    readMap(chunk, mat.getTextureMap2());
                    break;
                case LGlobals.MAT_OPACMAP:
                    readMap(chunk, mat.getOpacityMap());
                    break;
                case LGlobals.MAT_BUMPMAP:
                    readMap(chunk, mat.getBumpMap());
                    break;
                case LGlobals.MAT_SPECMAP:
                    readMap(chunk, mat.getSpecularMap());
                    break;
                case LGlobals.MAT_REFLMAP:
                    child = readChunk();
                    mat.getReflectionMap().strength = readPercentage(child);
                    skipChunk(child);
                    child = readChunk();
                    if (child.id != LGlobals.MAT_MAPNAME) {
                        throw new IOException("L3DS::readMaterial - error, expected chunk not found");
                    }
                    str = readASCIIZ(30);
                    if (str == null || str.isEmpty()) {
                        mat.getReflectionMap().mapName = "auto";
                    }
                    break;
            }

            skipChunk(chunk);
            chunk = readChunk();
        }
        materials.add(mat);
        materials.get(materials.size() - 1).setId(materials.size() - 1);
    }

    // reads the map info and fills the given map with this information
    void readMap(final LChunk chunk, LMap map) throws IOException {
        LChunk child;
        String str;
        gotoChunk(chunk);
        child = readChunk();
        while (child.end <= chunk.end) {
            switch (child.id) {
                case LGlobals.INT_PERCENTAGE:
                    map.strength = readPercentage(child);
                    break;
                case LGlobals.MAT_MAPNAME:
                    str = readASCIIZ(20);
                    map.mapName = str;
                    break;
                case LGlobals.MAT_MAP_USCALE:
                    map.uScale = readFloat();
                    break;
                case LGlobals.MAT_MAP_VSCALE:
                    map.vScale = readFloat();
                    break;
                case LGlobals.MAT_MAP_UOFFSET:
                    map.uOffset = readFloat();
                    break;
                case LGlobals.MAT_MAP_VOFFSET:
                    map.vOffset = readFloat();
                    break;
                case LGlobals.MAT_MAP_ANG:
                    map.angle = readFloat();
                    break;
            }
            skipChunk(child);
            child = readChunk();
        }
    }

    // reads keyframer data of the OBJECT_NODE_TAG chunk
    void readKeyframeData(final LChunk parent) throws IOException {

        int frames = 0;

        LChunk node_hdr = new LChunk();
        node_hdr.id = LGlobals.NODE_HDR;

        String str;
        LMesh mesh = new LMesh();

        gotoChunk(parent);
        if (!findChunk(node_hdr, parent)) {
            return;
        }
        gotoChunk(node_hdr);

        str = readASCIIZ(19);
        mesh = findMesh(str);
        if (mesh == null) {
            return;
        }
        gotoChunk(parent);

        // read the pivot
        LVector3 pivot = LGlobals.zero3;

        LChunk pivotchunk = new LChunk();
        pivotchunk.id = LGlobals.PIVOT;
        if (findChunk(pivotchunk, parent)) {
            gotoChunk(pivotchunk);
            pivot.x = readFloat();
            pivot.y = readFloat();
            pivot.z = readFloat();
        }
        gotoChunk(parent);

        // read frame 0 from the position track
        LVector3 pos = LGlobals.zero3;

        frames = 0;

        LChunk poschunk = new LChunk();
        poschunk.id = LGlobals.POS_TRACK_TAG;
        if (findChunk(poschunk, parent)) {
            gotoChunk(poschunk);
            // read the trackheader structure
            readShort();
            readInt();
            readInt();
            frames = readInt();
            if (frames > 0) {
                readKeyheader();
                pos.x = readFloat();
                pos.y = readFloat();
                pos.z = readFloat();
            }
        }
        gotoChunk(parent);

        // now read the rotation track
        LVector4 rot = LGlobals.zero4;

        LChunk rotchunk = new LChunk();
        rotchunk.id = LGlobals.ROT_TRACK_TAG;

        frames = 0;
        if (findChunk(rotchunk, parent)) {
            gotoChunk(rotchunk);
            // read the trackheader structure
            readShort();
            readInt();
            readInt();
            frames = readInt();
            if (frames > 0) {
                readKeyheader();
                rot.x = readFloat();
                rot.y = readFloat();
                rot.z = readFloat();
                rot.w = readFloat();
            }
        }
        gotoChunk(parent);

        // now read the scaling chunk
        LVector3 scale = new LVector3();
        scale.x = 1;
        scale.y = 1;
        scale.z = 1;

        LChunk scalechunk = new LChunk();
        scalechunk.id = LGlobals.SCL_TRACK_TAG;

        frames = 0;

        if (findChunk(scalechunk, parent)) {
            gotoChunk(scalechunk);
            // read the trackheader structure
            readShort();
            readInt();
            readInt();
            frames = readInt();
            if (frames > 0) {
                readKeyheader();
                scale.x = readFloat();
                scale.y = readFloat();
                scale.z = readFloat();
            }
        }
        gotoChunk(parent);
    }

    // reads the keyheader structure from the current offset and returns the frame number
    long readKeyheader() throws IOException {
        long frame;
        frame = readInt();
        int opts = readShort();
        if (opts == 32768) // 32768 is 1000000000000000 binary
        {  // tension is present
            readFloat();
        }
        if (opts == 16384) // 16384 is 0100000000000000 binary
        {  // continuity is present
            readFloat();
        }
        if (opts == 8192) {  // bias info present
            readFloat();
        }
        if (opts == 4096) {  // "ease to" present
            readFloat();
        }
        if (opts == 2048) {  // "ease from" present
            readFloat();
        }
        return frame;
    }
}
