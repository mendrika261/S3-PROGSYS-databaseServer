package exception.object;

import object.Table;

public class ColNotExistsException extends Exception {
    public ColNotExistsException(String colName, Table table) {
        super("\nLa colonne \"" + colName + "\" n'existe pas dans " + table);
        setStackTrace(new StackTraceElement[]{});
    }
}
