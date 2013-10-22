package com.lavida.swing.groovy.model.collector;

public class RuleFactoryImpl implements RuleFactory{

    public Rule createRule(RuleType type) {
        Rule rule = new Rule();
        rule.setType(type);
        switch (type) {
            case FULL_MATCH:
                rule.getParams().put("startFrom", "");
                rule.getParams().put("endWith", "");
                rule.getParams().put("skipTimes", "");
                rule.getParams().put("direction", "");
                break;
        }
        return rule;
    }
}
