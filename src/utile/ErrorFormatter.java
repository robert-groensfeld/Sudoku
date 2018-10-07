package utile;

public class ErrorFormatter {

    public String format(Exception error) {
        StringBuilder message = new StringBuilder();
        message.append(error.getMessage()).append(System.lineSeparator());

        Throwable cause = error.getCause();
        while (cause != null) {
            message.append(cause.getMessage()).append(System.lineSeparator());
            cause = cause.getCause();
        }

        return message.toString();
    }
}