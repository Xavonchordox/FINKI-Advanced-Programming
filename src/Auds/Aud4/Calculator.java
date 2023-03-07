package Auds.Aud4;

import java.util.UnknownFormatConversionException;

public class Calculator {
    private double result;
    private Strategy strategy;

    public Calculator() {
        this.result = 0.0;
    }

    public String execution(char operation, double value) throws UnknownOperatorException {
        if (operation == '+') {
            strategy = new Addition();
        } else if (operation == '-') {
            strategy = new Subtraction();
        } else if (operation == '*') {
            strategy = new Multiplication();
        } else if (operation == '/') {
            strategy = new Devision();
        } else throw new UnknownOperatorException(operation);

        result = strategy.calculate(result, value);

        return String.format("result %c %.2f = %.2f", operation, value, result);
    }

    public String init() {
        return String.format("result = %.2f", result);
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.format("updated result = %.2f", result);
    }
}
