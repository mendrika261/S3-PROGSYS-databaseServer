package exception.object;

public class ColMissingException extends Exception {
    public ColMissingException() {
        super("\nIl faut renseigner au moins une colonne");
    }
}
