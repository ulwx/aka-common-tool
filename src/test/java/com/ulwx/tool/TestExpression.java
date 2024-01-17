package com.ulwx.tool;


import org.codehaus.janino.ScriptEvaluator;

public class TestExpression {

    public static void main(String[] args) throws Exception{
        ScriptEvaluator se = new ScriptEvaluator();
        se.setParameters(new String[] { "a", "b" }, new Class[] { int.class, int.class });
        se.setReturnType(Object.class);
//        se.cook(
//                "if(a==1) {b=1+3; return b;} ; return a;"
//        );
        se.cook(
                "return true;"
        );
        Object res =se.evaluate(new Object[]{2,2});
        System.out.println(res);
    }

}
