
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.midi.Sequencer.SyncMode;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MedianFilterParallel extends RecursiveAction {

    static File imageFile; // args[0]
    static BufferedImage image = null; // buffered image to send to
    static int sliderVariable = 9; // args[2]
    static int radius = sliderVariable / 2;
    static BufferedImage image2 = null; // args[1]
    protected static int sThreshold = 100;
    int start;
    static int height;
    int width;

    // constructor method
    public MedianFilterParallel(int width, int start) {
        this.width = width;
        this.start = start;

    }

    protected void computeDirectly() {
        for (int x = start; x < start + width; ++x) {
            for (int y = 0; y < height; ++y) {
                // call an average method
                // if (y < 0 || y >= image.getHeight()) {
                // continue;
                // }
                // System.out.println("Called the computeDirectly");
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

            // split value + your start value
            invokeAll(new MedianFilterParallel(split, start),
                    new MedianFilterParallel(split, start + split));
            System.out.println("Invoked the pooled threads");
        }

    }

    public static int average(int x, int y, BufferedImage image, int radius) {
        // making list arrays; maybe less dynamic
        ArrayList<Integer> redPixels = new ArrayList<>();
        ArrayList<Integer> bluePixels = new ArrayList<>();
        ArrayList<Integer> greenPixels = new ArrayList<>();

        int count = 0;
        for (int i = x; i < x + radius; i++) {
            // add outter bounds conditions
            if (i < 0 || i >= image.getWidth()) {
                continue;
            }

            for (int j = y - radius; j <= y + radius; ++j) {
                if (j < 0 || j >= image.getHeight()) {
                    continue;
                }
                int pixel = image.getRGB(i, j);
                redPixels.add((pixel >> 16) & 0xff);
                greenPixels.add((pixel >> 8) & 0xff);
                bluePixels.add(pixel & 0xff);
                ++count;

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

    public static void mean(BufferedImage image) {

        height = image.getHeight();
        MedianFilterParallel fb = new MedianFilterParallel(image.getWidth(), 0);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {

        imageFile = new File("pictures/samples/example.jpg");
        image = ImageIO.read(imageFile);
        image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        mean(image);
        File outputfile = new File("pictures/median/medianParallel" + "kernelValue" + sliderVariable + ".jpg");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }
}
