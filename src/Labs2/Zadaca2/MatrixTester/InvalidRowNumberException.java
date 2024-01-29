package Labs2.Zadaca2.MatrixTester;

public class InvalidRowNumberException extends Throwable {
    private final String message;

    public InvalidRowNumberException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
