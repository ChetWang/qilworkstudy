package fr.itris.glips.svgeditor.shape;

import java.util.*;
import org.w3c.dom.*;

import com.nci.svg.sdk.client.EditorAdapter;

import fr.itris.glips.svgeditor.*;

/**
 * the class used to handle all the selections and actions that
 * can be done on a multiple selection
 * @author ITRIS, Jordi SUC
 */
public class ManyShape extends MultiAbstractShape {

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public ManyShape(EditorAdapter editor) {
		
		super(editor);

		shapeModuleId="ManyShape";
	}
	
	@Override
	public Set<Element> getElements(Set<Element> elements) {

		return new HashSet<Element>(elements);
	}
}
