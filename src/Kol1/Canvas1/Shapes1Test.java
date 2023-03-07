package Kol1.Canvas1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

class Canvas {
    private String canvasID;
    private int[] shapesSize;

    public Canvas(String canvasID, String[] shapesSize) {
        this.canvasID = canvasID;
        this.shapesSize = new int[shapesSize.length];
        for (int i=0; i<shapesSize.length; i++){
            this.shapesSize[i] = Integer.parseInt(shapesSize[i]);
        }
    }

    public String getCanvasID() {
        return canvasID;
    }

    public int getNumShapes() {
        return shapesSize.length;
    }

    public int canvasPerimeter() {
        return IntStream.of(shapesSize).sum() * 4;
    }

    @Override
    public String toString() {
        return canvasID + " " + getNumShapes() + " " + canvasPerimeter();
    }
}

class ShapesApplication {
    private List<Canvas> list;

    public ShapesApplication() {
        list = new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int numOfCanvas = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            String canvasID = parts[0];
            String[] shapesSize = Arrays.copyOfRange(parts, 1, parts.length);
            list.add(new Canvas(canvasID, shapesSize));
            numOfCanvas += shapesSize.length;
        }

        return numOfCanvas;
    }

    public void printLargestCanvasTo(OutputStream outputStream) throws IOException {
        int count = 0;
        int per = 0;
        int i = 0;
        while (count != list.size()) {
            if (list.get(count).canvasPerimeter() > per) {
                i = count;
                per = list.get(count).canvasPerimeter();
            }

            count++;
        }

        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(list.get(i).toString());
        printWriter.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}