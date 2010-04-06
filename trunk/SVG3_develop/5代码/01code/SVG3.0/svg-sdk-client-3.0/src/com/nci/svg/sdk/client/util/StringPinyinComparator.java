/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.sdk.client.util;

import java.util.Comparator;

/**

 * 按拼音字母对名字进行排序

 * @author Qil.Wong
 */
public class StringPinyinComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        if (o1 instanceof String && o2 instanceof String) {
            return ((java.text.RuleBasedCollator) java.text.Collator.getInstance(java.util.Locale.CHINA)).compare((String) o1, (String) o2);
        } else {
            return 0;
        }
    }
}
