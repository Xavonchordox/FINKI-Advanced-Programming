package Lab2.P3;

public class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(int x, int y, int r) {
        super(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", x, y, r));
    }
}

