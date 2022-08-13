/*
 * The aim of this program is to filter an image and change the surrounding pixels, given a window size
 * to the average of the pixels around the pixel. 
 * This code implements the idea of parallelism and extends the RecursiveAction class
 * @author RacquelDennison
 * @since 2022-08-13
 * 
 */

// importing all the packages 
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
    protected static int sThreshold = 10; // the threshold cut off value
    int start;
    static int height;
    int width;

    // constructor method
    /*
     * The constuctor method of the class
     * 
     * @param width This is the width of the image size
     * 
     * @param start The starting value for the pixel
     */
    public MeanFilterParallel(int width, int start) {
        this.width = width;
        this.start = start;

    }
    /*
     * This is the method that will be called directly if the
     * width is below the seqiential cut off
     */

    protected void computeDirectly() {
        for (int x = start; x < start + width; ++x) {
            for (int y = 0; y < height; ++y) {
                image2.setRGB(x, y, average(x, y, image, radius));
            }

        }

    }

    /*
     * The compute method that is called on the forks
     */
    protected void compute() {
        if (width < sThreshold) {
            computeDirectly();

            return;
        } else {
            int split = width / 2;

            // creating threads using the fork divide and conquer framework
            invokeAll(new MeanFilterParallel(split, start),
                    new MeanFilterParallel(split, start + split));

        }

    }

    /*
     * The avergae will comput the average of the surround pixels
     * 
     * @param x this is the starting x coordinate
     * 
     * @param y this is the y starting coordinate
     * 
     * @param image The image that is being writen from
     * 
     * @param radius the window size
     * 
     * @return int the average method
     * 
     */
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

    /*
     * this method will return the pixel value
     */
    public static int makePixel(int r, int g, int b) {

        return (r << 16) | (g << 8) | b;
    }

    /*
     * this method will write to the file and test the running time of the frkjoin
     * framework
     * this is where the execution of the method takes place
     */
    public static void mean(BufferedImage image) throws IOException {

        height = image.getHeight();
        MeanFilterParallel fb = new MeanFilterParallel(image.getWidth(), 0); // creating a constrictor of the class
        ForkJoinPool pool = new ForkJoinPool(); // forkjoin method
        long startTime = System.currentTimeMillis();
        pool.invoke(fb);
        long endTime = System.currentTimeMillis();
        // writing to a file
        String oFile = ("results/mean/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
        String timeTaken = ("Report for parallel median--------------------------------" + "\n"
                + "Time taken for mean parallel : " + (endTime - startTime) / 1000.00 + " seconds"
                + " at a slider value of "
                + sliderVariable + '\n' + "------------------------"
                + " Width : " + image.getWidth() + " Height: " + image.getHeight() +
                " ----------------------------Threshold 10-------------------" + '\n');
        Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static void main(String[] args) throws IOException {
        windowSize = args[2];
        sliderVariable = Integer.parseInt(windowSize);
        radius = sliderVariable / 2;
        imageFile = new File("pictures/samples/" + args[0] + ".jpg"); // reading from a file that is stored in the
                                                                      // picture/sample diretory
        image = ImageIO.read(imageFile);
        image2 = ImageIO.read(imageFile);
        for (int r = 0; r < 10; r++) { // testing the time 10 times
            mean(image);
        }
        File outputfile = new File(
                "pictures/mean/meanParallel" + "kernelValue" + sliderVariable + "_" + args[1] + ".jpg");

        // writing to the new image
        ImageIO.write(image2, "jpg", outputfile);

    }
}
