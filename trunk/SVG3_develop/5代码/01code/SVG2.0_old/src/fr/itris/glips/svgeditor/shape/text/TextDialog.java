package fr.itris.glips.svgeditor.shape.text;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import fr.itris.glips.library.widgets.TitledDialog;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class of the dialog used to choose the parameters 
 * used to create a new svg file
 * @author ITRIS, Jordi SUC
 */
public class TextDialog extends TitledDialog{

	/**
	 * the TextShape module object
	 */
	private TextShape textShape;
	
	/**
	 * the current handle
	 */
	private SVGHandle currentHandle;
	
//	/**
//	 * the text field
//	 */
//	private JTextField textField;
	
	private JTextArea textField;
	
	/**
	 * the textfield listener
	 */
	private CaretListener textFieldListener;

	/**
	 * the constructor of the class
	 * @param textShape the TextShape module object
	 * @param parent the parent of the dialog
	 */
	public TextDialog(TextShape textShape, Frame parent, Editor editor){
		
		super(parent, true, true,editor);
		this.textShape=textShape;
	}
	
	/**
	 * the constructor of the class
	 * @param textShape the TextShape module object
	 * @param parent the parent of the dialog
	 */
	public TextDialog(TextShape textShape, JDialog parent,Editor editor){
		
		super(parent, true,editor);
		this.textShape=textShape;
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
		String titleDialogLabel=bundle.getString("TextShapeDialogTitle");
		final String titleDialogMessageLabel=bundle.getString("TextShapeDialogMessage");
		String textShapeTextPromptLabel=bundle.getString("TextShapeTextPrompt");
		final String textShapeErrorEmptyStringLabel=bundle.getString("TextShapeErrorEmptyString");

		//setting the title
		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabel for the textfield
		JLabel textPromptLbl=new JLabel(textShapeTextPromptLabel+" : ");
		
		//creates the text field
//		textField=new JTextField(15);
		
		//modified by wangql from JTextField to JTextArea
		textField = new JTextArea();
		textField.setLineWrap(false);
		JScrollPane textScroll = new JScrollPane();
		textScroll.setPreferredSize(new Dimension(300,150));
		textScroll.setViewportView(textField);
		
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(textPromptLbl, c);
		widgetsPanel.add(textPromptLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		
		
//		layout.setConstraints(textField, c);
//		widgetsPanel.add(textField);
		
		layout.setConstraints(textScroll, c);
		widgetsPanel.add(textScroll);
		okButton.setEnabled(false);
		
		
		//creating the listener to the text field button
		textFieldListener=new CaretListener(){
			
			public void caretUpdate(CaretEvent e) {
				
				if(textField.getText().equals("")){
					
					setMessage(textShapeErrorEmptyStringLabel, TitledDialog.ERROR_TYPE);
					okButton.setEnabled(false);
					
				}else{
					
					setMessage(titleDialogMessageLabel, TitledDialog.INFORMATION_TYPE);
					okButton.setEnabled(true);
				}
			}
		};
		
		textField.addCaretListener(textFieldListener);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					
					textShape.createElement(currentHandle, textField.getText());
				}
				
				setVisible(false);
				editor.getHandlesManager().getCurrentHandle().getSelection().setBTextDialog(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);
		setMessage(titleDialogMessageLabel, TitledDialog.INFORMATION_TYPE);

		return widgetsPanel;
	}
	
	/**
	 * shows the dialog
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 * @param handle a svg handle
	 */
	public void showDialog(JComponent relativeComponent, SVGHandle handle) {
		
		this.currentHandle=handle;
		textField.grabFocus();
		textField.setText("");
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		textField.removeCaretListener(textFieldListener);
		
		super.disposeDialog();
	}
}
