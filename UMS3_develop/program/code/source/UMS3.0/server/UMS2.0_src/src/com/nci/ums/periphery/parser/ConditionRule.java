package com.nci.ums.periphery.parser;

import java.util.*;

import com.nci.ums.periphery.exception.*;

/**
 *  当条件rule校验成功的时候，才校验目标rule
 */
public class ConditionRule implements Rule {
    private Rule cond[];
    private Rule check[];

    public ConditionRule(Rule[] cond, Rule[] check) {
        this.cond = cond;
        this.check = check;
    }

    public boolean check(Map map, int count) throws ParserException {
        try {
            for(int i = 0; i < cond.length; i++)
                cond[i].check(map, count);
        }
        catch (ParserException ex) {
            return true;
        }
        for(int i = 0; i < cond.length; i++)
            check[i].check(map, count);
        return true;
    }

}