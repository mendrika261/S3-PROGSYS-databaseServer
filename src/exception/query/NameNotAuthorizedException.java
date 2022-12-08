package exception.query;

public class NameNotAuthorizedException extends Exception {
    public NameNotAuthorizedException(String name) {
        super("Ce nom d'objet \""+name+"\" n'est pas autorisé ici!");
    }
}
