/**
 * 类名：com.nci.svg.ui
 * 创建人:yx.nci
 * 创建日期：2008-6-26
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.ui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;

/**
 * @author yx.nci
 *
 */
public class NCIComboBoxEditor implements ComboBoxEditor {

    JTextField jtf = new JTextField();    
    public NCIComboBoxEditor(){ 
        jtf.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e) { 
                boolean isExit = false; 
                String codeStr = jtf.getText().trim(); 
                if(codeStr.indexOf("%") > -1)
                {
                    codeStr = codeStr.substring(0,codeStr.indexOf("%"));
                }
                int nScale = 0;
                try
                {
                    nScale = new Integer(codeStr).intValue();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    nScale = 1;
                    System.out.println("输入框格式不正确");
                }
                System.out.println("输入框:" + nScale);
            }    
        }); 
        jtf.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent e) { 
                selectAll(); 

            } 
            public void mouseEntered(MouseEvent e) {    
            } 
            public void mouseExited(MouseEvent e) {        
            } 
            public void mousePressed(MouseEvent e) { 
                selectAll(); 
            } 
            public void mouseReleased(MouseEvent e) {    
                selectAll(); 
            }        
        }); 

    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
     */
    public void addActionListener(ActionListener l) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#getEditorComponent()
     */
    public JTextField getEditorComponent() {
        // TODO Auto-generated method stub
        return jtf;
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#getItem()
     */
    public Object getItem() {
        // TODO Auto-generated method stub
        return  jtf.getText(); 
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
     */
    public void removeActionListener(ActionListener l) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#selectAll()
     */
    public void selectAll() {
        // TODO Auto-generated method stub
        jtf.selectAll(); 
        jtf.requestFocus(); 
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxEditor#setItem(java.lang.Object)
     */
    public void setItem(Object anObject) {
        // TODO Auto-generated method stub
//        jtf.setText(anObject.toString()); 
    }

}
