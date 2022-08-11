import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.nio.file.*;

public class MedianFilterParallel extends RecursiveAction {

    static File imageFile; // args[0]
    static BufferedImage image = null; // buffered image to send to; // args[2]
    static String windowSize = null;
    static int sliderVariable;
    static int radius;
    static BufferedImage image2 = null; // args[1]
    static int height;
    protected static int sThreshold = 100;
    int start;
    int width;

    // constructor method
    public MedianFilterParallel(int width, int start) {
        this.width = width;
        this.start = start;

    }

    protected void computeDirectly() {
        for (int x = start; x < start + width; ++x) {
            for (int y = 0; y < height - radius; ++y) {
                image2.setRGB(x, y, average(x, y, image, radius));
            }

        }

        // System.out.println("Called the computeDirectly");
    }

    protected void compute() {
        if (width < sThreshold) {
            computeDirectly();

            return;
        } else {
            int split = width / 2;

            // // split value + your start value
            // System.out.println("Made fork");
            invokeAll(new MedianFilterParallel(split, start),
                    new MedianFilterParallel(split, start + split));

        }

    }

    public static int average(int x, int y, BufferedImage image, int radius) {
        // making list arrays; maybe less dynamic
        int count = 0;
        ArrayList<Integer> redPixels = new ArrayList<>();
        ArrayList<Integer> bluePixels = new ArrayList<>();
        ArrayList<Integer> greenPixels = new ArrayList<>();

        for (int i = x; i < x + radius; ++i) {
            // add outter bounds conditions
            if (i < 0 || i >= image.getWidth()) {
                continue;
            }

            for (int j = y - radius; j <= y + radius; j++) {
                if (j < 0 || j >= image.getHeight()) {
                    continue;
                }

                int pixel = image.getRGB(i, j);
                redPixels.add((pixel >> 16) & 0xff);
                greenPixels.add((pixel >> 8) & 0xff);
                bluePixels.add(pixel & 0xff);
                count++;

            }

        }
        Collections.sort(redPixels);
        Collections.sort(greenPixels);
        Collections.sort(bluePixels);

        return makePixel(redPixels.get(count / 2), greenPixels.get(count / 2), bluePixels.get(count / 2));

    }

    public static int makePixel(int r, int g, int b) {

        return (r << 16) | (g << 8) | b;
    }

    public static void mean(BufferedImage image) throws IOException {

        height = image.getHeight();
        sThreshold = height;
        MedianFilterParallel fb = new MedianFilterParallel(image.getWidth(), 0);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
        String oFile = ("results/median/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
        String timeTaken = ("Report for parallel median--------------------------------" + "\n"
                + "Time  : " + (endTime - startTime) / 1000.00 + " seconds"
                + " at a slider value of "
                + sliderVariable + '\n' + "-----------" + " Threshold Value " + sThreshold
                + " Width : " + image.getWidth() + " Height: " + image.getHeight() +
                " -----------------------------------------------" + '\n');
        Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    }

    public static void main(String[] args) throws IOException {
        windowSize = args[2];
        sliderVariable = Integer.parseInt(windowSize);
        radius = sliderVariable / 2;

        imageFile = new File("pictures/samples/" + args[0] + ".jpg");
        image = ImageIO.read(imageFile);
        image2 = ImageIO.read(imageFile);
        for (int r = 0; r < 10; r++) {
            mean(image);
        }
        File outputfile = new File(
                "pictures/median/medianParallel" + "windowSize" + sliderVariable + "_" +
                        args[1] + ".jpg");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }
}
