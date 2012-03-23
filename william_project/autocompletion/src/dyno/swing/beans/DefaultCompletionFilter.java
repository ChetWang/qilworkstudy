/*
 * DefaultCompletionFilter.java
 *
 * Created on 2007-6-21, 23:18:58
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dyno.swing.beans;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author William Chen
 */
public class DefaultCompletionFilter implements CompletionFilter {

    private Vector vector;

    public DefaultCompletionFilter() {
        vector = new Vector();
    }

    public DefaultCompletionFilter(Vector v) {
        vector = v;
    }
    public ArrayList filter(String text) {
        ArrayList list=new ArrayList();
        String txt=text.trim();
        int length=txt.length();
        for(int i=0;i<vector.size();i++){
            Object o=vector.get(i);
            String str=o.toString();
            if(length==0||str.startsWith(txt))
                list.add(o);
        }
        return list;
    }
}
