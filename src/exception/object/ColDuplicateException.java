package exception.object;

public class ColDuplicateException extends Exception {
    public ColDuplicateException(String col, String table) {
        super("La colonne "+col+" est dupliquée dans la table "+table);
    }
}
