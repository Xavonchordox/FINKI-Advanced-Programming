package Lab2.P1;

public class InsufficientElementsException extends Exception{
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}
