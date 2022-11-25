package interpreter;

import exception.query.OperatorInvalidException;

import java.util.Arrays;
import java.util.Vector;

public class Operator {
    static Vector<String> operators = new Vector<>(
            Arrays.asList("!=", "==", "<=", "<", ">=", ">")
    );

    public static String getCompOperator(String comp) throws OperatorInvalidException {
        for(String c:getOperators())
            if(comp.contains(c))
                return c;
        throw new OperatorInvalidException(comp);
    }

    public static int getCompOperatorCode(String comp) throws OperatorInvalidException {
        try {
           return getOperators().indexOf(getCompOperator(comp));
        } catch (Exception e) {
            throw new OperatorInvalidException(comp);
        }
    }


    static void setOperators(Vector<String> operators) {
        Operator.operators = operators;
    }

    public static Vector<String> getOperators() {
        return operators;
    }
}
