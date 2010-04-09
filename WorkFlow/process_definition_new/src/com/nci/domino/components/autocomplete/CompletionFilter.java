package com.nci.domino.components.autocomplete;

import java.util.List;

/**
 *
 * @author Qil.Wong
 */
public interface CompletionFilter {
    List filter(String text);
}
