package nci.yc.svg.ges;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.client.mode.EditorMode;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;

public class EditorGesMode extends EditorMode {

	public EditorGesMode(EditorAdapter editor) {
		super(editor);
		createPropertyPane = false;
		graphunit_has_model = true;
	}

	@Override
	public Document getModuleDocument() {
		return Utilities.getXMLDocumentByStream(getClass().getResourceAsStream(
				"modules_ges.xml"));
	}
	
	public Element modifyUseElement(Element useElement){
		return useElement;
	}

}
