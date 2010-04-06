/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-2-3
 * @功能：数字文字显示,右侧带点击按钮,点击后出现滑块窗口可以通过滑块进行操作
 *
 */
package com.nci.svg.sdk.graphmanager.property.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Element;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphmanager.property.NciGraphProperty;

public class EditorAndSliderPropertyEditor extends AbstractPropertyEditor {
	private PropertySheetPanel sheet = null;
	private JTextField textField = null;
	private JButton button = null;
	private NciGraphProperty property = null;
	private Set<Element> elements = new HashSet<Element>();
	private EditorAdapter nciEditor = null;
	private JDialog sliderDialog = null;
	private JSlider slider = null;
    public EditorAndSliderPropertyEditor(EditorAdapter nciEditor,PropertySheetPanel sheet,final NciGraphProperty property,final Set<Element> elements)
    {
    	super();
    	this.nciEditor = nciEditor;
    	this.property = property;
    	this.elements.addAll(elements);
    	property.setPropertyEditor(this);
    	this.sheet = sheet;
    	editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL,0))
    	{
    		public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
			}
    	};
    	
    	textField = new JTextField();
    	textField.addKeyListener(new KeyListener()
    	{

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doAction();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
    		
    	});
    	((JPanel)editor).add("*",textField);
    	button = ComponentFactory.Helper.getFactory().createMiniButton();
    	button.addActionListener(new ActionListener()
    	{
			public void actionPerformed(ActionEvent e) {
				showSlider();
			}
    	
    	});
    	((JPanel)editor).add(button);
    	
    	sliderDialog = new JDialog();
    	slider = new JSlider(0,100);
    	slider.addFocusListener(new FocusListener()
    	{

			/* (non-Javadoc)
			 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
			 */
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			/* (non-Javadoc)
			 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
			 */
			@Override
			public void focusLost(FocusEvent e) {
				sliderDialog.setVisible(false);
				
			}
    		
    	});
    	
    	slider.addChangeListener(new ChangeListener()
    	{

			/* (non-Javadoc)
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = slider.getValue();
				textField.setText(String.valueOf(value));
				doAction();
			}
    		
    	});
    	sliderDialog.add(slider);

    	sliderDialog.setUndecorated(true);
    	sliderDialog.setLocationRelativeTo(sheet);
    	sliderDialog.setVisible(false);
    	sliderDialog.setSize(sheet.getWidth(), 80);
//    	sliderDialog.setResizable(false);
    }
    
    private void showSlider(){
    	int x = (int)sheet.getLocationOnScreen().getX();
    	int y = (int)button.getLocationOnScreen().getY() + button.getHeight();
    	if(sliderDialog!= null && !sliderDialog.isVisible())
    	{
	
    		String value = textField.getText();
    		int nValue = Integer.parseInt(value);
    		slider.setValue(nValue);
    		sliderDialog.setBounds(x, y, sheet.getWidth(), 10);
    		sliderDialog.setVisible(true);
    	}
    }
    
    /* (non-Javadoc)
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#getValue()
	 */
	@Override
	public Object getValue() {
		String value = textField.getText();
		if(value == null || value.length() == 0)
			value = "0";
		Double nValue = Double.parseDouble(value);
		return String.valueOf(nValue/100);
	}

	/* (non-Javadoc)
	 * @see com.l2fprod.common.beans.editor.AbstractPropertyEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		double d = Double.parseDouble((String)value);
		int nValue = (int)(d*100);
		textField.setText(String.valueOf(nValue));
	}

	private void doAction()
    {
		if(property != null)
		{
			property.writeToObject(elements);
		}
    }
}
