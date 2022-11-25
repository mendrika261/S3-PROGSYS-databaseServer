package exception.object;

import object.Row;
import object.Table;

public class RowDuplicatedException extends Exception {
    public RowDuplicatedException(Row row, Table table) {
        super("\nAjout impossible:\nLa ligne "+ row +" sera dupliquée dans la table \""+table+"\"");
        setStackTrace(new StackTraceElement[]{});
    }
}
