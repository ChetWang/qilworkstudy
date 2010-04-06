/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.client.ui.search;

import javax.swing.JCheckBox;

/**
 *
 * @author Qil.Wong
 */
public class SearchRangeCheckBox extends JCheckBox {

    private String nameText;
    
    private String code;
    
    public SearchRangeCheckBox(String nameText,String code) {
        super(nameText);
        this.nameText = nameText;
        this.code = code;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}