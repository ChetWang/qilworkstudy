/*
 * WebServiceSettingPanel.java
 *
 * Created on __DATE__, __TIME__
 */

package com.nci.ums.desktop.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.nci.ums.desktop.ConsoleUtil;
import com.nci.ums.desktop.bean.NumOnlyDocument;
import com.nci.ums.desktop.bean.tablesorter.DefaultSimpleTableSortRenderer;

/**
 * 
 * @author __USER__
 */
public class WebServiceSettingPanel extends javax.swing.JPanel implements
		Setting {

	private Document originalDoc;
	private static String xmlFile = System.getProperty("user.dir")
			+ "/conf/ums_axis2.xml";
	private static String clientws = "/resources/clientws.props";
	private JPopupMenu mainMenu;
	private JMenuItem addItem;
	private JMenuItem removeItem;

	/** Creates new form WebServiceSettingPanel */
	public WebServiceSettingPanel() {
		initComponents();
		jLabel1.setText(ConsoleUtil
				.getLocaleString("Information_of_UMS_WebService_deployment:")); // NOI18N

		jLabel2.setText(ConsoleUtil.getLocaleString("WS_Port:")); // NOI18N
		wsPortText.setDocument(new NumOnlyDocument(5));
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) clientWSTable
				.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(20);
		tcm.getColumn(1).setPreferredWidth(280);
		DefaultSimpleTableSortRenderer renderer = new DefaultSimpleTableSortRenderer(
				clientWSTable);
		renderer.initSortHeader();
		clientWSTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					showPopupMenu(e);
			}
		});
		iniMenu();
	}

	private void showPopupMenu(MouseEvent e) {
		int selectRow = clientWSTable.rowAtPoint(e.getPoint());
		clientWSTable.setRowSelectionInterval(selectRow, selectRow);
		mainMenu.show(clientWSTable, e.getX(), e.getY());
	}

	private void iniMenu() {
		mainMenu = new JPopupMenu();
		addItem = new JMenuItem(ConsoleUtil.getLocaleString("Add_WSClient"));
		removeItem = new JMenuItem(ConsoleUtil
				.getLocaleString("Remove_WSClient"));
		mainMenu.add(addItem);
		mainMenu.add(removeItem);
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DefaultTableModel) clientWSTable.getModel())
						.addRow(new Object[0]);
			}
		});
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((DefaultTableModel) clientWSTable.getModel())
						.removeRow(clientWSTable.getSelectedRow());
				if (clientWSTable.getRowCount() == 0)
					((DefaultTableModel) clientWSTable.getModel())
							.addRow(new Object[0]);
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		clientWSTable = new javax.swing.JTable();
		jLabel3 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		wsPortText = new javax.swing.JTextField();

		clientWSTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {

				}, new String[] { "Service ID", "WebService Class " }));
		jScrollPane1.setViewportView(clientWSTable);

		jLabel3.setText(ConsoleUtil.getLocaleString("intro_WSClient"));

		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																jPanel1Layout
																		.createSequentialGroup()
																		.add(
																				jLabel2)
																		.addPreferredGap(
																				org.jdesktop.layout.LayoutStyle.RELATED)
																		.add(
																				wsPortText,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				322,
																				Short.MAX_VALUE))
														.add(
																jLabel1,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																266,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								org.jdesktop.layout.GroupLayout.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.add(
												jLabel1,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												10, Short.MAX_VALUE)
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(
												jPanel1Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.BASELINE)
														.add(jLabel2)
														.add(
																wsPortText,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				this);
		this.setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(
								layout
										.createSequentialGroup()
										.add(
												layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(
																layout
																		.createSequentialGroup()
																		.add(
																				20,
																				20,
																				20)
																		.add(
																				jLabel3,
																				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																				340,
																				Short.MAX_VALUE))
														.add(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.add(
																				layout
																						.createParallelGroup(
																								org.jdesktop.layout.GroupLayout.LEADING)
																						.add(
																								jPanel1,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.add(
																								jScrollPane1,
																								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
																								350,
																								Short.MAX_VALUE))))
										.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(jPanel1,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
						org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED).add(
								jLabel3).addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED).add(
								jScrollPane1,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								52, Short.MAX_VALUE).addContainerGap()));
	}// </editor-fold>

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JTable clientWSTable;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField wsPortText;

	// End of variables declaration
	public void implSetting() throws IOException {
		// 更改axis2.xml
		Element config = originalDoc.getRootElement(); // 得到根元素
		Element transportReceiver = (Element) config
				.getChild("transportReceiver");
		Element port = (Element) transportReceiver.getContent().get(1);
		port.setText(wsPortText.getText());
		Format format = Format.getCompactFormat();
		format.setEncoding("GBK");
		// 设置缩进字符串
		format.setIndent("  ");
		XMLOutputter XMLOut = new XMLOutputter(format);
		// 输出到文件
		XMLOut.output(originalDoc, new FileOutputStream(xmlFile));
		// 更改clientws
		ConsoleUtil.stopEditingCells(this.clientWSTable);
		DefaultTableModel model = (DefaultTableModel) clientWSTable.getModel();
		Vector v = model.getDataVector();
		String[] parameters = new String[v.size()];
		String[] values = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			Vector data = (Vector) v.get(i);
			if (data != null) {
				parameters[i] = data.get(0) == null ? "" : data.get(0)
						.toString();
				values[i] = data.get(1) == null ? "" : data.get(1).toString();
			}
		}
		// com.nci.ums.v3.service.common.UMSClientWS3003
		ConsoleUtil.replacePropFile(parameters, values, getClass().getResource(
				clientws).getPath());
	}

	public void initSetting() throws IOException {
		Vector port = ConsoleUtil.getAxis2PortVector(xmlFile);
		originalDoc = (Document) port.get(0);
		wsPortText.setText(port.get(1).toString());
		Properties props = new Properties();
		InputStream is = getClass().getResourceAsStream(clientws);
		props.load(is);
		is.close();
		Iterator it = props.keySet().iterator();
		while (it.hasNext()) {
			String serviceID = it.next().toString();
			((DefaultTableModel) clientWSTable.getModel()).addRow(new Object[] {
					serviceID, props.getProperty(serviceID) });
		}
		if (clientWSTable.getRowCount() == 0) {
			((DefaultTableModel) clientWSTable.getModel())
					.addRow(new Object[0]);
		}
	}

	public static void main(String[] args) {
		WebServiceSettingPanel p = new WebServiceSettingPanel();
		try {
			p.initSetting();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}