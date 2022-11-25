package exception.object;

public class TableIncompatibleException extends Exception {
    public TableIncompatibleException(String table1, String table2) {
        super(table1 + " et " + table2 + " sont incompatibles");
    }
}
