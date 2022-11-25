package exception.object;

import object.Row;
import object.Table;

public class ColsNumberNotMatchingException extends Exception {
    public ColsNumberNotMatchingException(Row row, Table table) {
        super("\nNombre de colonne invalide:\nLa ligne " + row +
                " ne correspond pas au nombre de colonnes nécessaires pour la table " +
                "\""+ table +"\"");
        setStackTrace(new StackTraceElement[]{});
    }
}
