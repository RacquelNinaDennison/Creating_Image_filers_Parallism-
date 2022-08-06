import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MeanFilterParallel extends RecursiveAction {

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
    public MeanFilterParallel(int width, int start) {
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
            invokeAll(new MeanFilterParallel(split, start),
                    new MeanFilterParallel(split, start + split));
            System.out.println("Invoked the pooled threads");
        }

    }

    public static int average(int x, int y, BufferedImage image, int radius) {
        int totals[] = { 0, 0, 0 };
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
                totals[0] += (pixel >> 16) & 0xff;
                totals[1] += (pixel >> 8) & 0xff;
                totals[2] += pixel & 0xff;
                ++count;

            }

        }

        return makePixel(totals[0] / count, totals[1] / count, totals[2] / count);

    }

    public static int makePixel(int r, int g, int b) {

        return (r << 16) | (g << 8) | b;
    }

    public static void mean(BufferedImage image) {

        height = image.getHeight();
        MeanFilterParallel fb = new MeanFilterParallel(image.getWidth(), 0);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {

        imageFile = new File("example.jpg");
        image = ImageIO.read(imageFile);
        image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        mean(image);
        File outputfile = new File("imageBlurParallel.jpg");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }
}
