package exception.consoleApp;


import display.Color;

public class ArgumentMissingException extends Exception {
    public ArgumentMissingException() {
        super(Color.YELLOW + "\n\tLe port du serveur doit etre mentionné" + Color.RESET);
        setStackTrace(new StackTraceElement[]{});
    }
}
