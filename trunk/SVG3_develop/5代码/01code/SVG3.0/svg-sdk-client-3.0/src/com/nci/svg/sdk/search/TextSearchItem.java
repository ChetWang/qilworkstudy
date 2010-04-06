package com.nci.svg.sdk.search;

public class TextSearchItem implements SearchItem {

    public TextSearchItem() {
    }

    public StringBuffer getExpr(String searchContent, boolean caseSensitive,
            boolean wholeWord) {
        StringBuffer xpathExpr = new StringBuffer();
        if (!caseSensitive && !wholeWord) {
            xpathExpr.append(
                    "//*[name()='text'][contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'").append(searchContent.toUpperCase()).append(
                    "')]|//*[name()='text'][contains(translate(*,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'").append(searchContent.toUpperCase()).append("')]");
        } else if (caseSensitive && !wholeWord) {
            xpathExpr.append("//*[name()='text'][contains(.,'").append(
                    searchContent).append("')]|//*[name()='text'][contains(*,'").append(
                    searchContent).append("')]");
        } else if (!caseSensitive && wholeWord) {
            xpathExpr.append(
                    "//*[name()='text'][translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='").append(searchContent.toUpperCase()).append(
                    "']|//*[name()='text'][translate(*,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='").append(searchContent.toUpperCase()).append("']");
        } else if (caseSensitive && wholeWord) {
            xpathExpr.append("//*[name()='text'][.='").append(searchContent).append("']|//*[name()='text'][*='").append(searchContent).append("']");
        }
        return xpathExpr;
    }
}
