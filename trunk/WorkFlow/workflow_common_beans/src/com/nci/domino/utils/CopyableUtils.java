package com.nci.domino.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CopyableUtils {

    
    public static String toString(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(obj);
        encoder.close();
        out.close();
        return new String(baos.toByteArray(), "UTF-8");
    }

    public static Object toObject(String xml) throws Exception {
        Object obj = null;
        ByteArrayInputStream bais = new ByteArrayInputStream((xml).getBytes("UTF-8"));
        XMLDecoder decoder = new XMLDecoder(bais);
        obj = decoder.readObject();
        decoder.close();
        bais.close();
        return obj;
    }
}
