package com.nci.svg.sdk.ui.graphunit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.StringPinyinComparator;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphunit.AbstractSymbolManager;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.ui.EditorPanel;
import com.nci.svg.sdk.ui.GradientPanel;
import com.nci.svg.sdk.ui.graphunit.search.GraphUnitSimpleSearchPanel;
import com.nci.svg.sdk.ui.outlook.OutlookPane;
import com.nci.svg.sdk.ui.outlook.VTextIcon;

/**
 * �༭��ͼԪѡ������panel����������ͼԪ������������
 * 
 * @author Qil.Wong
 * 
 */
public class GraphUnitOutlookPanel extends EditorPanel {

	private GraphUnitSimpleSearchPanel searchPanel = null;
	private JTabbedPane outlookTabbedPane = null;
	private final static String resultTabName = "�������";
	// resultPanel������TabbedPane�е���š��������е�tabtitle��Ϊnull������ֻ��ͨ����סtab��index����ȡ����Ӧ��tab
	private int resultPanelIndex = -1;

	public GraphUnitOutlookPanel(EditorAdapter editor,
			GraphUnitSimpleSearchPanel searchPanel,
			final JTabbedPane outlookTabbedPane) {
		super(editor);
		this.searchPanel = searchPanel;
		this.outlookTabbedPane = outlookTabbedPane;
		init();
		searchPanel.getTextField().addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					showResult(search());
				}
			}
		});
		searchPanel.getCloseButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				closeSearchPanel();
			}
		});
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		BorderLayout layout = new BorderLayout(5, 3);
		this.setLayout(layout);
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setBorder(new EtchedBorder());
		this.add(outlookTabbedPane, BorderLayout.CENTER);

		// GridBagLayout rootLayout = new GridBagLayout();
		// this.setLayout(rootLayout);
		// rootLayout.columnWeights = new double[]{1.0};
		// rootLayout.rowWeights = new double[]{1.0};
		// GridBagConstraints searchConstraints = new GridBagConstraints();
		// searchConstraints.gridx = 0;
		// searchConstraints.gridy = 0;
		// searchConstraints.insets = new Insets(5,5,5,5);
		// // searchConstraints.fill = GridBagConstraints.BOTH;
		// GridBagConstraints outlookConstraints = new GridBagConstraints();
		// outlookConstraints.gridx = 0;
		// outlookConstraints.gridy = 1;
		// // outlookConstraints.fill = GridBagConstraints.BOTH;
		// outlookConstraints.anchor = GridBagConstraints.CENTER;
		// rootLayout.setConstraints(searchPanel, searchConstraints);
		// rootLayout.setConstraints(outlookTabbedPane, outlookConstraints);
		// this.add(searchPanel);
		// this.add(outlookTabbedPane);
	}

	/**
	 * �ر�������壬ͬʱҲҪ����ѯ�����������
	 */
	private void closeSearchPanel() {
		if (resultPanelIndex >= 0) {
			if (outlookTabbedPane.getTabCount() >= 0) {
				outlookTabbedPane.setSelectedIndex(0);
			}
			outlookTabbedPane.removeTabAt(resultPanelIndex);
			resultPanelIndex = -1;
		}
	}

	/**
	 * ͼԪģ������
	 */
	private Vector search() {
		String searchText = searchPanel.getTextField().getText().trim();
		if (searchText != null && !searchText.equals("")) {
			editor.getLogger().log(null, LoggerAdapter.DEBUG,
					"ͼԪ��ѯ������" + searchText);
			// FIXME �޸Ļ�ȡͼԪ��ģ��ļ��� deprecated by wangql @2008.12.11-13:41
			Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap = editor
					.getSymbolManager().getAllSymbols();
			// Map<String, NCIEquipSymbolBean> symbolMap =
			// editor.getGraphUnitManager().getOriginalSymbolMap();
			Iterator<SymbolTypeBean> it = symbolMap.keySet().iterator();
			Vector<String> result = new Vector<String>();
			while (it.hasNext()) {
				Map<String, NCIEquipSymbolBean> temp = symbolMap.get(it.next());
				Iterator<String> symbolNameIt = temp.keySet().iterator();
				while (symbolNameIt.hasNext()) {
					String name = symbolNameIt.next();
					if (name.indexOf(searchText) >= 0) {
						result.add(name);
					}
				}
			}
			Collections.sort(result, new StringPinyinComparator());
			editor.getLogger().log(null, LoggerAdapter.DEBUG,
					"ͼԪ��ѯ���Ϊ��" + result);

			return result;
		}
		return null;
	}

	private void cleanResult(OutlookPane resultPane) {
		ResultPanel resultPanel = (ResultPanel) resultPane.getPaneComponents()
				.get(0);
		resultPanel.getScroolPanel().removeAll();
	}

	/**
	 * ��ʾ��ѯ�����û�н���������ʾ
	 * 
	 * @param result
	 */
	private void showResult(Vector<String> result) {
		if (result == null) {
			return;
		}
		closeSearchPanel();
		OutlookPane resultPane = getResultOutlookPane();
		if (result.size() > 0) { // ����н�����������������������

			// ��ʹ���н������δ�����Ѿ���ʾ����Ҫ��ŵ����ش��̲����أ�
			AbstractSymbolManager graphunitManager = editor.getSymbolManager();
			Map<SymbolTypeBean, Map<String, NCIThumbnailPanel>> thumbShownMap = graphunitManager
					.getThumbnailShownMap();
			// TODO ��ȡͼԪģ�弯�� deprecated by wangql @2008.12.11-13:41
			Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap = graphunitManager
					.getAllSymbols();
			// Map<String, NCIEquipSymbolBean> symbolMap = null;
			// Map<String, NCIEquipSymbolBean> symbolMap =
			// editor.getGraphUnitManager().getOriginalSymbolMap();
			for (String name : result) {
				NCIThumbnailPanel thumb = null;

				NCIEquipSymbolBean symbolBean = null;
				Map<String, NCIEquipSymbolBean> temp = null;
				SymbolTypeBean symbolTypeBean = null;
				Iterator<SymbolTypeBean> it_all = symbolMap.keySet().iterator();
				
				while (it_all.hasNext()) {
					symbolTypeBean = it_all.next();
					temp = symbolMap.get(symbolTypeBean);
					if(temp==null || temp.size()>0){
						thumbShownMap.put(symbolTypeBean,
								new HashMap());
					}
					Iterator<NCIEquipSymbolBean> it = temp.values().iterator();
					while (it.hasNext()) { // ����ͨ���������ҳ��������ڵĵ�δ��ʾ��ͼԪ
						NCIEquipSymbolBean b = it.next();
						if (b.getName().equalsIgnoreCase(name)) {
							symbolBean = b;
//							if (!graphunitManager.isSymbolShown(name)) { // ˵����û����ʾ��,δ�ӱ��ش��̼���
								if (symbolBean != null) {
									thumb = new NCIThumbnailPanel(
											NCIThumbnailPanel.THUMBNAIL_OUTLOOK,
											editor);
									thumb.setText(name);
//									File f = new File(editor.getSvgSession()
//											.getSymbolPath(symbolBean));
//									thumb.setDocument(Utilities
//											.getSVGDocumentByURL(f.toURI()
//													.toASCIIString()));
									thumb.setDocument(Utilities.getSVGDocumentByContent(b));
									thumb
											.getSvgCanvas()
											.setSize(
													NCIThumbnailPanel.outlookPrefferedSize);
									thumb.setSymbolBean(symbolBean);
									// FIXME
									// �����Ҫ���ԣ�������hashcode����δ�������ԣ�����ͨ������ɾ��fixme��
									Map<String, NCIThumbnailPanel> thumbTypeMap = thumbShownMap
											.get(symbolTypeBean);
									if (thumbTypeMap == null) {
										thumbTypeMap = new HashMap<String, NCIThumbnailPanel>();
									}
									thumbShownMap.put(symbolTypeBean,
											thumbTypeMap);
									thumbTypeMap.put(symbolBean.getName(),
											thumb);
									break;
								}
//							} else {
//								Map<String, NCIThumbnailPanel> thumbTypeMap = thumbShownMap
//										.get(symbolTypeBean);
//								thumb = thumbTypeMap.get(symbolBean.getName());
//								break;
//							}
						}
					}

				}

				if (thumb != null) {
					((ResultPanel) resultPane.getPaneComponents().get(0))
							.getScroolPanel().add(thumb);
				}
			}
			// ��󣬽�����������������ǰ��
			outlookTabbedPane.setSelectedIndex(resultPanelIndex);
			outlookTabbedPane.updateUI();
		} else { // ���û�н�����������ʾ
			JOptionPane.showConfirmDialog(editor.findParentFrame(), "û�в鵽"
					+ "\"" + searchPanel.getTextField().getText().trim() + "\""
					+ "��ص�ͼԪ!", "���", JOptionPane.CLOSED_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * ��ȡ��Ų���ʾ�����OutlookPane
	 * 
	 * @return OutlookPane
	 */
	public OutlookPane getResultOutlookPane() {
		OutlookPane resultOutlookPane = null;
		// �����δ������������Ҫ���´����������
		if (resultPanelIndex == -1) {
			VTextIcon textIcon = new VTextIcon(outlookTabbedPane,
					resultTabName, 0);
			resultOutlookPane = new OutlookPane(editor);
			this.outlookTabbedPane.addTab(null, textIcon, resultOutlookPane);
			ResultPanel resultPanel = new ResultPanel(editor);
			resultOutlookPane.addPane(resultTabName, null, resultPanel);
			resultPanelIndex = outlookTabbedPane.getTabCount() - 1;
		} else {
			resultOutlookPane = (OutlookPane) outlookTabbedPane
					.getComponentAt(resultPanelIndex);
		}
		return resultOutlookPane;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return ����������
	 */
	public GraphUnitSimpleSearchPanel getSearchPanel() {
		return searchPanel;
	}

	/**
	 * ��ȡͼԪ��ʾ��TabbedPane����
	 * 
	 * @return ͼԪ��ʾ��TabbedPane����
	 */
	public JTabbedPane getOutlookTabbedPane() {
		return outlookTabbedPane;
	}

	/**
	 * ͼԪ����������
	 */
	private class ResultPanel extends EditorPanel {

		private JPanel scrollPanel;

		public ResultPanel(EditorAdapter editor) {
			super(editor);
			init();
		}

		private void init() {
			this.setLayout(new BorderLayout());
			scrollPanel = new GradientPanel();
			JScrollPane scroll = new JScrollPane();
			this.add(scroll, BorderLayout.CENTER);
			scrollPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			scrollPanel.setPreferredSize(new Dimension(
					(NCIThumbnailPanel.outlookPrefferedSize.width + 5) * 3,
					4 * (NCIThumbnailPanel.outlookPrefferedSize.height + 5)));
			scroll.setViewportView(scrollPanel);
			scroll.setAutoscrolls(true);
		}

		public JPanel getScroolPanel() {
			return scrollPanel;
		}
	}
}
