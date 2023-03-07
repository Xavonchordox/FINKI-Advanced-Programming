package Auds.Aud4;

public class Devision implements Strategy{
    @Override
    public double calculate(double num1, double num2) {
        if (num2 != 0)
            return num1 / num2;
        return num1;
    }
}
