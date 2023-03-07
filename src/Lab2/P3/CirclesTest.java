package Lab2.P3;

import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    TYPE getType();

    String exceptionMessage();

    int getR();

    Movable moveInDirection(String direction) throws ObjectCanNotBeMovedException;
}

class MovingPoint implements Movable {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }


    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.yMax) {
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        }
        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0) {
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        }
        y -= ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0) {
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        }
        x -= xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.xMax) {
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        }
        x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String exceptionMessage() {
        return "Movable point with coordinates (" + x + "," + y + ") can not be fitted into the collection";
    }

    @Override
    public int getR() {
        return 0;
    }

    @Override
    public Movable moveInDirection(String direction) throws ObjectCanNotBeMovedException {
        if (direction.equals("UP")) {
            moveUp();
        } else if (direction.equals("DOWN")) {
            moveDown();
        } else if (direction.equals("LEFT")) {
            moveLeft();
        } else if (direction.equals("RIGHT")) {
            moveRight();
        }
        return this;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }
}

class MovingCircle implements Movable {
    private int r;
    MovingPoint center;

    public MovingCircle(int r, MovingPoint center) {
        this.r = r;
        this.center = center;
    }

    public int getR() {
        return r;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() + r < 0 || center.getCurrentYPosition() + r > MovablesCollection.yMax)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (center.getCurrentYPosition() + r < 0 || center.getCurrentYPosition() + r > MovablesCollection.yMax)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() + r < 0 || center.getCurrentXPosition() + r > MovablesCollection.xMax)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (center.getCurrentXPosition() + r < 0 || center.getCurrentXPosition() + r > MovablesCollection.xMax)
            throw new ObjectCanNotBeMovedException(center.getCurrentXPosition(), center.getCurrentYPosition());
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String exceptionMessage() {
        return "Movable circle with center (" + center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ") and radius " + r + "can not be fitted into the collection";
    }

    @Override
    public Movable moveInDirection(String direction) throws ObjectCanNotBeMovedException {
        if (direction.equals("UP")) {
            moveUp();
        } else if (direction.equals("DOWN")) {
            moveDown();
        } else if (direction.equals("LEFT")) {
            moveLeft();
        } else if (direction.equals("RIGHT")) {
            moveRight();
        }
        return this;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + getCurrentXPosition() + "," + getCurrentYPosition() + ") and radius " + r;
    }
}

class MovablesCollection {
    //    private Movable[] movables;
    private List<Movable> movables;
    static int xMax = 0;
    static int yMax = 0;

    public MovablesCollection(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.movables = new ArrayList<>();
    }

    public static void setxMax(int xMax) {
        MovablesCollection.xMax = xMax;
    }

    public static void setyMax(int yMax) {
        MovablesCollection.yMax = yMax;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m instanceof MovingPoint) {
            if (m.getCurrentXPosition() >= 0 && m.getCurrentXPosition() <= xMax
                    && m.getCurrentYPosition() >= 0 && m.getCurrentYPosition() <= yMax) {
                movables.add(m);
            } else throw new MovableObjectNotFittableException(m.getCurrentXPosition(), m.getCurrentYPosition(), 0);
        } else if (m instanceof MovingCircle) {
            if (m.getCurrentXPosition() - m.getR() >= 0 && m.getCurrentXPosition() + m.getR() <= xMax
                    && m.getCurrentYPosition() - m.getR() >= 0 && m.getCurrentYPosition() + m.getR() <= yMax) {
                movables.add(m);
            } else
                throw new MovableObjectNotFittableException(m.getCurrentXPosition(), m.getCurrentYPosition(), m.getR());
        }
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        if (type.equals(TYPE.CIRCLE)) {
            for (int i = 0; i < movables.size(); i++) {
                if (movables.get(i) instanceof MovingPoint) {
                    try {
                        movables.set(i, movables.get(i).moveInDirection(direction.name()));
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } else if (type.equals(TYPE.POINT)) {
            for (int i = 0; i < movables.size(); i++) {
                if (movables.get(i) instanceof MovingCircle) {
                    try {
                        movables.set(i, movables.get(i).moveInDirection(direction.name()));
                    } catch (ObjectCanNotBeMovedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size " + movables.size() + ":");
        for (Movable movable : movables) {
            if (movable instanceof MovingPoint) {
                sb.append(String.format("Movable point with coordinates (%d,%d)\n", movable.getCurrentXPosition(), movable.getCurrentYPosition()));
            } else {
                sb.append(String.format("Movable circle with center coordinates (%d,%d) and radius %d\n", movable.getCurrentXPosition(), movable.getCurrentYPosition(), movable.getR()));
            }
        }

        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");

        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);

        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());

    }


}