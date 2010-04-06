/*
 * To change this template, choose Tools | Templates | Licenses | Default License
 * and open the template in the editor.
 */
package com.nci.ums.util;

import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Qil.Wong
 */
public class ServletTemp {

    public InputStream getInputStreamFromFile(String relativePath) {
        return getClass().getResourceAsStream(relativePath);
    }
    
    public URL getURL(String relativePath){
    	 return getClass().getResource(relativePath);
    }
}
