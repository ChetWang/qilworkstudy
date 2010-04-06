/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ums.demo.util;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Qil.Wong
 */
public class UMSDemoUtil {

    private static Base64Encoder encoder;
    public static void replace(String[] parameter, String[] value, String filename) throws IOException {
        File output = new File(filename);
        BufferedWriter bw = new BufferedWriter(new FileWriter(output.getPath(), false));
        for (int i = 0; i < parameter.length; i++) {
            bw.write(parameter[i] + "=" + value[i] + "\r\n");
        }
        bw.close();
    }
    
    public static Base64Encoder getBase64Encoder(){
        if(encoder == null)
            encoder = new Base64Encoder();
        return encoder;
    }
}
