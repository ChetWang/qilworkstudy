/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.search;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.itris.glips.svgeditor.resources.ResourcesManager;
import java.awt.Dimension;

/**
 *
 * @author Qil.Wong
 */
public class NCISvgSearchManager {

    private Document searchItemsDoc;

    public NCISvgSearchManager() {
        searchItemsDoc = ResourcesManager.getXMLDocument("nciSvgSearch.xml");

    }

    public Document getSearchItemsDocument() {
        return searchItemsDoc;
    }
}
