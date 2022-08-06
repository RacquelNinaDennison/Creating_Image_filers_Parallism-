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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MeanFilterParallel extends RecursiveAction {
    int height;
    int width;
    static File imageFile; // args[0]
    static BufferedImage image = null; // buffered image to send to
    static int sliderVariable; // args[2]
    static int radius = sliderVariable / 2;
    static BufferedImage image2 = null; // args[1]
    protected static int sThreshold = 100000;
    int start;

    // constructor method
    public MeanFilterParallel(int width, int start) {
        this.width = width;
        this.start = start;

    }

    protected void computeDirectly() {
        for (int x = radius; x < start + radius; ++x) {
            for (int y = 0; y < radius; ++y) {
                // call an average method
                if (y < 0 || y >= image.getHeight()) {
                    continue;
                }
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
            invokeAll(new MeanFilterParallel(split, start),
                    new MeanFilterParallel(split, start + split));
            System.out.println("Invoked the pooled threads");
        }

    }

    // main class
    public static void main(String[] args) throws IOException {
        try {
            // set all the variabls of the file

            imageFile = new File("example.jpg"); // TODO change it to the args
            image = ImageIO.read(imageFile);
            // getting the dimensions of the image

        }

        catch (IOException e) {
            System.out.println("Error was faced: " + e);
        }
        // TODO Fix the looping variables
        // this will be done once all the threads are done
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(Integer.toString(processors) + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");

        MeanFilterParallel fb = new MeanFilterParallel(image.getWidth(), 0);
        fb.height = image.getHeight();
        image2 = new BufferedImage(fb.width, fb.height, BufferedImage.TYPE_INT_RGB);
        mean(image);
        // ForkJoinPool pool = new ForkJoinPool();
        // long startTime = System.currentTimeMillis();
        // pool.invoke(fb);
        // long endTime = System.currentTimeMillis();

        // System.out.println("Image blur took " + (endTime - startTime) +
        // " milliseconds.");

        System.out.println("Writing to file");
        File outputfile = new File("imageBlurParallel.png");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);
        System.out.println("Image done ");

    }

    public static void mean(BufferedImage image) {

    }

    public static int average(int x, int y, BufferedImage image, int radius) {
        int totals[] = { 0, 0, 0 };
        int count = 0;
        for (int i = x; i < x + radius; i++) {
            // add outter bounds conditions
            for (int j = y - radius; j <= y + radius; ++j) {
                int pixel = image.getRGB(i, j);
                totals[0] += (pixel >> 16) & 0xff;
                totals[1] += (pixel >> 8) & 0xff;
                totals[2] += pixel & 0xff;
                ++count;

            }

        }
        System.out.println("Called the average filter");
        return makePixel(totals[0] / count, totals[0] / count, totals[0] / count);

    }

    public static int makePixel(int r, int g, int b) {
        System.out.println("Called the make filter");
        return (r << 16) | (g << 8) | b;
    }

}
