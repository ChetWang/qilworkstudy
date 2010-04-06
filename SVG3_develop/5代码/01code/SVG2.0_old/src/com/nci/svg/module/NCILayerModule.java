/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.nci.svg.ui.layer.LayerSelecionDialog;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 *
 * @author Qil.Wong
 */
public class NCILayerModule extends ModuleAdapter {

    public static String LAYER_MODULE_ID = "layerModule";
    private JButton btn;
//    private ImageIcon icon = new ImageIcon("");
    private LayerSelecionDialog selectionDialog;

    public NCILayerModule(Editor editor) {
        super(editor);
        btn = new JButton(ResourcesManager.bundle.getString("nci_layers"));
        selectionDialog = new LayerSelecionDialog(editor, true);
        selectionDialog.setTitle(ResourcesManager.bundle.getString("nci_layers"));
    }

    @Override
    public HashMap<String, AbstractButton> getToolItems() {

        btn.setToolTipText(ResourcesManager.bundle.getString("nci_layers"));

//        btn.setIcon(icon);
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (editor.getHandlesManager().getCurrentHandle() == null) {
                    JOptionPane.showConfirmDialog(editor.findParentFrame(), ResourcesManager.bundle.getString("nci_layer_no_handle"), ResourcesManager.bundle.getString("nci_optionpane_infomation_title"), JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    //Ϊ��ֹ��;svgͼ���ܵ��Ķ���ÿ����ʾ����˶Ի���ʱӦ��������³�ʼ��,������Ч�ʣ�����֤����ȷ��
                    selectionDialog.initTabbedPane();
                    selectionDialog.setVisible(true);
                	
                }
            }
        });
        HashMap<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
        toolItems.put(LAYER_MODULE_ID, btn);
        return toolItems;
    }

    public String getName() {
        return LAYER_MODULE_ID;
    }
}
