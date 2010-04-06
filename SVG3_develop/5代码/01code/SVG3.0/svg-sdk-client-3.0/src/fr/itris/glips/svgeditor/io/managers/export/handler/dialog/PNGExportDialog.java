package fr.itris.glips.svgeditor.io.managers.export.handler.dialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.nci.svg.sdk.client.EditorAdapter;


/**
 * the class of the dialog used to choose the parameters of the png export action
 * 
 * @author ITRIS, Jordi SUC
 */
public class PNGExportDialog extends ExportDialog{

    /**
     * the labels 
     */
    private String compressionLevelLabel="", 
    	encodeAlphaLabel="", imageSettingsLabel="";
    
    /**
     * the compression level
     */
    private int compressionLevel=9;
    
    /**
     * whether to encode the alpha channel or not
     */
    private boolean encodeAlpha=true;
    
	/**
	 * the constructor of the class
	 * @param parent the parent frame
	 */
	public PNGExportDialog(Frame parent,EditorAdapter editor) {

		super(parent,editor);
		initialize();
	}

	/**
	 * the constructor of the class
	 * @param parent the parent dialog
	 */
	public PNGExportDialog(JDialog parent,EditorAdapter editor) {

		super(parent,editor);
		initialize();
	}
	
	@Override
	protected void initialize() {

		super.initialize();
		
        if(bundle!=null){
            
            try{
                exportDialogTitle=bundle.getString("labelpngexport");
                imageSettingsLabel=bundle.getString("labelimagesettings");
                compressionLevelLabel=bundle.getString("labelpngCompressionLevel");
                encodeAlphaLabel=bundle.getString("labelpngEncodeAlpha");
            }catch (Exception ex){}
        }
        
        //creating the size chooser panel
        JPanel sizechooser=getSizeChooserPanel();
        
        //setting the title of the dialog
        setTitle(exportDialogTitle);
        
        //the panel that will containing the radio buttons
        JPanel compressionPanel=new JPanel();
        
        //the slider used to chooser the compression level
        final JSlider pngSlider=new JSlider(0, 9, compressionLevel);
        pngSlider.setMajorTickSpacing(1);
        pngSlider.setMinorTickSpacing(1);
        pngSlider.setPaintLabels(true);
        pngSlider.setPaintTicks(true);
        pngSlider.setPaintTrack(true);
        pngSlider.setSnapToTicks(true);

        //the listener to the slider
        ChangeListener pngSliderChangeListener=new ChangeListener(){
            
            public void stateChanged(ChangeEvent evt) {
 
                compressionLevel=pngSlider.getValue();
            }
        };
        
        //adds a listener to the slider
        pngSlider.addChangeListener(pngSliderChangeListener);

        //the label for the spinner
        JLabel pngLabel=new JLabel(compressionLevelLabel.concat(" :"));

        //the checkbox used for the alpha encoding
        final JCheckBox checkBox=new JCheckBox(encodeAlphaLabel);
        
        checkBox.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		
        		encodeAlpha=checkBox.isSelected();
        	}
        });
        
        checkBox.setSelected(encodeAlpha);

        //creating the layout and filling the compression panel
        GridBagLayout gridBag=new GridBagLayout();
        compressionPanel.setLayout(gridBag);
        GridBagConstraints c=new GridBagConstraints();
        
        c.anchor=GridBagConstraints.WEST;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.insets=new Insets(2, 5, 2, 5);

        c.gridwidth=1;
        gridBag.setConstraints(pngLabel, c);
        compressionPanel.add(pngLabel);
        
        c.gridwidth=GridBagConstraints.REMAINDER;
        gridBag.setConstraints(pngSlider, c);
        compressionPanel.add(pngSlider);

        c.gridwidth=GridBagConstraints.REMAINDER;
        gridBag.setConstraints(checkBox, c);
        compressionPanel.add(checkBox);
        
        compressionPanel.setBorder(new CompoundBorder(
            new TitledBorder(imageSettingsLabel), new EmptyBorder(4, 4, 4, 4)));
        
        //handling the parameters panel
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
        parametersPanel.add(sizechooser);
        parametersPanel.add(compressionPanel);
	}

	/**
	 * @return the compression level
	 */
	public int getCompressionLevel() {
		return compressionLevel;
	}

	/**
	 * @return whether to encode the alpha channel or not
	 */
	public boolean encodeAlpha() {
		return encodeAlpha;
	}
}
