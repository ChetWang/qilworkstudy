package com.nci.ums.v3.message.basic;

import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * Title: MsgAttachment.java
 * </p>
 * <p>
 * Description: The attachment object of a message. Generally, it contains a
 * title and content, the content is a base64 encoded String Object.for example:
 * 
 * String filebyteBase64; byte[] bytes=new byte[1024000];//less than 1M
 * InputStream in=Classloader.getResourceAsStream(filename); in.read(bytes);
 * filebyteBase64=new sun.misc.BASE64Encoder().encode(bytes); in.close();
 * </p>
 * <p>
 * Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering£¬ Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.19
 * @version 1.0
 */
public class MsgAttachment implements Serializable {

    private String fileName;
    /**
     * the base64 encoded string of bytes generated from file. for example:
     * 
     * String filebyteBase64; byte[] bytes=new byte[1024000];//less than 1M
     * InputStream in=new FileInputStream(new File(filename)); in.read(bytes);
     * filebyteBase64=new Base64Encoder().encode(bytes); in.close();
     */
    private String fileByteBase64;
    private static BASE64Encoder encoder;
    private static BASE64Decoder decoder;

    public MsgAttachment() {
    }

    public MsgAttachment(String fileName, String fileByteBase64) {
        this.fileName = fileName;
        this.fileByteBase64 = fileByteBase64;
    }

    /**
     * get the attachment file name
     * 
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * set the attachment file name
     * 
     * @param fileName
     *            fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * get the String of a file that is encoded the file bytes by Base64
     * ecoding.
     * 
     * @return the fileByte_Base64
     */
    public String getFileByteBase64() {
        return fileByteBase64;
    }

    /**
     * set the Base64 encoding value of a file
     * 
     * @param fileByteBase64
     *            the fileByte_Base64 to set
     */
    public void setFileByteBase64(String fileByteBase64) {
        this.fileByteBase64 = fileByteBase64;
    }

    public String toString() {
        return "The attachment is named:" + this.fileName;
    }

    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        hash = 41 * hash + (this.fileByteBase64 != null ? this.fileByteBase64.hashCode() : 0);
        return hash;
    }
    
    public boolean equals(Object o){
        if(o instanceof MsgAttachment){
            return ((MsgAttachment)o).getFileName().equals(this.getFileName()) && ((MsgAttachment)o).getFileByteBase64().equals(this.getFileByteBase64());
        }return false;
    }

    public static BASE64Encoder getBASE64Encoder() {
        if (encoder == null) {
            encoder = new BASE64Encoder();
        }
        return encoder;
    }

    public static BASE64Decoder getBASE64Decoder() {
        if (decoder == null) {
            decoder = new BASE64Decoder();
        }
        return decoder;
    }
}
