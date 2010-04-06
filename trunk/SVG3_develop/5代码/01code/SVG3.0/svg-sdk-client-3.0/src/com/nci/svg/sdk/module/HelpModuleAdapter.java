package com.nci.svg.sdk.module;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * ∞Ô÷˙ƒ£øÈ  ≈‰∆˜
 * @author Qil.Wong
 *
 */
public abstract class HelpModuleAdapter extends ModuleAdapter {
	
	public final static String MODULE_ID = "6cfa746e-d484-42eb-8c78-51db73c8780f";

	public HelpModuleAdapter(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}
	
	public abstract void closeHelp();

}
