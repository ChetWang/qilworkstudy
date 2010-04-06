/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.sdk.search;

/**
 * 
 * @author Qil.Wong
 */
public class SymbolIDSearchItem implements SearchItem {

	public StringBuffer getExpr(String searchContent, boolean caseSensitive,
			boolean wholeWord) {
		StringBuffer xpathExpr = new StringBuffer();
		if (!caseSensitive && !wholeWord) {
			xpathExpr
					.append(
							"//*[contains(translate(@id,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'")
					.append(searchContent.toUpperCase()).append("')]");
		} else if (caseSensitive && !wholeWord) {
			xpathExpr.append("//*[contains(@id,'").append(searchContent)
					.append("')]");
		} else if (!caseSensitive && wholeWord) {
			xpathExpr
					.append(
							"//*[translate(@id,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='")
					.append(searchContent.toUpperCase()).append("']");
		} else if (caseSensitive && wholeWord) {
			xpathExpr.append("//*[@id='").append(searchContent).append("']");
		}
		return xpathExpr;
	}

}
