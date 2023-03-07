package Auds.Aud4;

public class UnknownOperatorException extends Exception{
    public UnknownOperatorException(char operation) {
        super(String.format("This operator %c is not valid.", operation));
    }
}
