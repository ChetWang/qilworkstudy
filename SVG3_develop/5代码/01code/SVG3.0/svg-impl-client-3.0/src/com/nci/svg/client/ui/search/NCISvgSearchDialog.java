/*
 * NCISvgSearchDialog.java
 *
 * Created on 2008年7月1日, 上午11:38
 */
package com.nci.svg.client.ui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.client.module.NCISvgSearchModule;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.search.NCISvgSearchManager;
import com.nci.svg.sdk.search.SearchItem;

import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 
 * @author Qil.Wong
 */
public class NCISvgSearchDialog extends javax.swing.JDialog {

    NCISvgSearchManager searchManager;
    ArrayList<SearchRangeCheckBox> rangeBoxes = new ArrayList<SearchRangeCheckBox>();
    NCISvgSearchModule searchModule;
    /**
     * 当前查询显示所在所有结果集中的序号
     */
    private int currentIndex = -1;
    /**
     * 重置“前一步”操作的监听器
     */
    private ActionListener resetPreviousListener;
    /**
     * 判断是否有指定的查询对象范围，如果没有，则不提供查询
     */
    private ActionListener rangeCheckListener;
    /**
     * 搜索结果集
     */
    private NodeList searchResult;

    /**
     * 构造函数
     */
    public NCISvgSearchDialog(java.awt.Frame parent, boolean modal,
            NCISvgSearchModule module) {
        super(parent, modal);
        initComponents();
        searchModule = module;
        this.setLocationRelativeTo(parent);
        resetPreviousListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetPrevious();
            }
        };
        rangeCheckListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                nextBtn.setEnabled(false);
                for (SearchRangeCheckBox checkbox : rangeBoxes) {
                    if (checkbox.isSelected()) {
                        nextBtn.setEnabled(true);
                        break;
                    }
                }
            }
        };
        wholeWordCheckBox.addActionListener(resetPreviousListener);
        caseSensitiveCheckBox.addActionListener(resetPreviousListener);
        searchContentTextField.getDocument().addDocumentListener(
                new DocumentListener() {

                    public void insertUpdate(DocumentEvent e) {
                        resetPrevious();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        resetPrevious();
                    }

                    public void changedUpdate(DocumentEvent e) {
                        resetPrevious();
                    }
                });
        searchContentTextField.addKeyListener(new KeyAdapter(){
        	@Override
        	public void keyPressed(KeyEvent e){
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        			nextBtn.doClick();
        	}
        });
        searchManager = searchModule.getEditor().getSearchManager();
        
        initSearchItems();
        this.setTitle(ResourcesManager.bundle.getString("nci_svg_search"));
        previousBtn.setMnemonic(KeyEvent.VK_P);
        nextBtn.setMnemonic(KeyEvent.VK_N);
        searchItemsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourcesManager.bundle.getString("nci_svg_search_range")));
        searchModule.getEditor().getHandlesManager().addHandlesListener(new HandlesListener(){
        	@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
        		resetPrevious();
        	}
        });
    }

    /**
     * 初始化查询对象
     */
    public void initSearchItems() {
        searchItemsPanel.removeAll();
        Document doc = searchManager.getSearchItemsDocument();
        NodeList items = doc.getElementsByTagName("searchItem");
        Element item = null;
        String itemName = null;
        String code = null;
        SearchRangeCheckBox checkBox = null;
        for (int i = 0; i < items.getLength(); i++) {
            item = (Element) items.item(i);
            itemName = item.getAttribute("name");
            code = item.getAttribute("code");
            checkBox = new SearchRangeCheckBox(itemName, code);
            // 默认只搜索文本
            if (code.equalsIgnoreCase("com.nci.svg.sdk.search.TextSearchItem")) {
                checkBox.setSelected(true);
            }
            checkBox.addActionListener(resetPreviousListener);
            checkBox.addActionListener(rangeCheckListener);
            searchItemsPanel.add(checkBox);
            rangeBoxes.add(checkBox);
        }
        pack();
    }

    /**
     * 重置“前一步”的数据，这个方法在查询界面中查询条件和查询内容更改时用到
     */
    private void resetPrevious() {
        previousBtn.setEnabled(false);
        searchResult = null;
        currentIndex = -1;
    }

    private void searchAction() {
    	String searchContent = this.searchContentTextField.getText().trim();
        SVGHandle handle = searchModule.getEditor().getHandlesManager().getCurrentHandle();
        handle.getSelection().clearSelection();
        Element eRoot = handle.getCanvas().getDocument().getDocumentElement();
        // xpath搜索表达式
        StringBuffer xpathExpr = new StringBuffer();
        SearchItem searchItem = null;
        for (SearchRangeCheckBox checkbox : rangeBoxes) {
            if (checkbox.isSelected()) {// 这个范围是要查询的
                try {
                    searchItem = (SearchItem) Class.forName(checkbox.getCode()).getConstructors()[0].newInstance();
                    xpathExpr.append(
                            searchItem.getExpr(searchContent,caseSensitiveCheckBox.isSelected(), wholeWordCheckBox.isSelected())).append("|");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // String xpathExpr = getTextSearch(str);
        // xpathExpr += "|";
        // xpathExpr += getIDSearch(str);
        try {
            searchResult = Utilities.findNodes(xpathExpr.toString().substring(
                    0, xpathExpr.toString().length() - 1), eRoot);
            if (searchResult == null || searchResult.getLength() == 0) {
                System.out.println("没有找到数据");
                JOptionPane.showConfirmDialog(this, ResourcesManager.bundle.getString("nci_svg_search_finished"),
                        ResourcesManager.bundle.getString("nci_optionpane_infomation_title"),
                        JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else {
                currentIndex = 0;
                searchModule.showElement(handle, (Element) searchResult.item(currentIndex));
                if(searchResult.getLength()>1)
                	previousBtn.setEnabled(true);
            }
        } catch (XPathExpressionException e) {
            
            e.printStackTrace();
        }
        
    }

    /**
     * “下一个”查找的操作
     */
    private void nextAction() {
        currentIndex++;
        if (currentIndex == searchResult.getLength()) {
            JOptionPane.showConfirmDialog(this, ResourcesManager.bundle.getString("nci_svg_search_finished"),
                    ResourcesManager.bundle.getString("nci_optionpane_infomation_title"),
                    JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
            resetPrevious();
        }else{
            SVGHandle handle = searchModule.getEditor().getHandlesManager().getCurrentHandle();
            searchModule.showElement(handle, (Element) searchResult.item(currentIndex));
        }
    }
    
    /**
     * “上一个”查找的操作
     */
    private void previousAction(){
    	currentIndex--;
    	if(currentIndex==-1){
    		JOptionPane.showConfirmDialog(this, ResourcesManager.bundle.getString("nci_svg_search_finished"),
                    ResourcesManager.bundle.getString("nci_optionpane_infomation_title"),
                    JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
            resetPrevious();
    	}else{
            SVGHandle handle = searchModule.getEditor().getHandlesManager().getCurrentHandle();
            searchModule.showElement(handle, (Element) searchResult.item(currentIndex));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		searchItemsPanel = new javax.swing.JPanel();
		previousBtn = new javax.swing.JButton();
		nextBtn = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		caseSensitiveCheckBox = new javax.swing.JCheckBox();
		wholeWordCheckBox = new javax.swing.JCheckBox();
		jLabel1 = new javax.swing.JLabel();
		searchContentTextField = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		searchItemsPanel.setLayout(new javax.swing.BoxLayout(searchItemsPanel,
				javax.swing.BoxLayout.X_AXIS));

		previousBtn.setText(ResourcesManager.bundle
				.getString("nci_svg_search_previous")); // NOI18N
		previousBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				previousBtnActionPerformed(evt);
			}
		});

		nextBtn.setText(ResourcesManager.bundle
				.getString("nci_svg_search_next")); // NOI18N
		nextBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextBtnActionPerformed(evt);
			}
		});

		jPanel1.setBorder(javax.swing.BorderFactory
				.createTitledBorder(ResourcesManager.bundle
						.getString("nci_svg_search_option")));

		caseSensitiveCheckBox.setText(ResourcesManager.bundle
				.getString("nci_svg_search_case_sensitive")); // NOI18N

		wholeWordCheckBox.setText(ResourcesManager.bundle
				.getString("nci_svg_search_whole_world")); // NOI18N

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout.createSequentialGroup().addContainerGap().add(
						caseSensitiveCheckBox,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(
						wholeWordCheckBox).add(35, 35, 35)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout.createParallelGroup(
						org.jdesktop.layout.GroupLayout.BASELINE).add(
						caseSensitiveCheckBox).add(wholeWordCheckBox)));

		jLabel1.setText(ResourcesManager.bundle
				.getString("nci_svg_search_content")); // NOI18N

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.TRAILING)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																searchItemsPanel,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																298,
																Short.MAX_VALUE)
														.add(
																org.jdesktop.layout.GroupLayout.LEADING,
																layout
																		.createSequentialGroup()
																		.add(
																				jLabel1)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				searchContentTextField,
																				org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																				0,
																				Short.MAX_VALUE))
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				jPanel1,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								org.jdesktop.layout.GroupLayout.TRAILING,
																								previousBtn)
																						.add(
																								org.jdesktop.layout.GroupLayout.TRAILING,
																								nextBtn))))
										.addContainerGap()));

		layout.linkSize(new java.awt.Component[] { nextBtn, previousBtn },
				org.jdesktop.layout.GroupLayout.HORIZONTAL);

		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel1)
														.add(
																searchContentTextField,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												searchItemsPanel,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												59, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				previousBtn)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				nextBtn))
														.add(
																jPanel1,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (searchContentTextField.getText().trim().equals("")) {
            JOptionPane.showConfirmDialog(this, ResourcesManager.bundle.getString("nci_svg_search_inputcontent_pls"),
                    ResourcesManager.bundle.getString("nci_optionpane_infomation_title"),
                    JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
        } else if (currentIndex == -1) {
            searchAction();
        } else {
            nextAction();
        }

    }

    private void previousBtnActionPerformed(java.awt.event.ActionEvent evt) {
    	previousAction();
    }// GEN-LAST:event_previousBtnActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox caseSensitiveCheckBox;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton nextBtn;
	private javax.swing.JButton previousBtn;
	private javax.swing.JTextField searchContentTextField;
	private javax.swing.JPanel searchItemsPanel;
	private javax.swing.JCheckBox wholeWordCheckBox;
	// End of variables declaration//GEN-END:variables
}
