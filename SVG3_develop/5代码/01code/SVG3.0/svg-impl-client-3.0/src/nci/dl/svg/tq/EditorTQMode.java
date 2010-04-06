package nci.dl.svg.tq;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.client.mode.EditorMode;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;

public class EditorTQMode extends EditorMode{
	public EditorTQMode(EditorAdapter editor) {
		super(editor);
		createPropertyPane = true;
		graphunit_has_model = true;
	}

	@Override
	public Document getModuleDocument() {
		return Utilities.getXMLDocumentByStream(getClass().getResourceAsStream(
				"modules_tq.xml"));
	}
	
	public Element modifyUseElement(Element useElement){
		return useElement;
	}
}
