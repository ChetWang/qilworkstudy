/*
 * SortController.java
 *
 * Created on 2007年12月9日, 上午10:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.nci.domino.components.table.tablesorter;

/**
 *
 * @author Qil.Wong
 */
public interface SortController {
    
    public static final UpDownArrow upIcon = new UpDownArrow(0);
    public static final UpDownArrow downIcon = new UpDownArrow(1);
    
    public  int getSortColumnIndex();
    
    public  boolean getSortedColumnAscending();
    
}
