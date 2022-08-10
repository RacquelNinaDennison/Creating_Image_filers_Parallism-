import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.nio.file.*;

public class MeanFilterParallel extends RecursiveAction {

    static File imageFile; // args[0]
    static BufferedImage image = null; // buffered image to send to
    static String windowSize = null;
    static int sliderVariable = 0;
    static int radius = 0;
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

    public static void mean(BufferedImage image) throws IOException {

        height = image.getHeight();
        MeanFilterParallel fb = new MeanFilterParallel(image.getWidth(), 0);
        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
        String oFile = ("results/mean/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
        String timeTaken = ("Report for parallel median--------------------------------" + "\n"
                + "Time taken for mean parallel : " + (endTime - startTime) / 1000.00 + " seconds"
                + " at a slider value of "
                + sliderVariable + '\n' + "------------------------"
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
                "pictures/mean/meanParallel" + "kernelValue" + sliderVariable + "_" + args[1] + ".jpg");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }
}
