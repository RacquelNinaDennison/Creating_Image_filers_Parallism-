
/*
 * The aim of this program is to filter an image and change the surrounding pixels, given a window size
 * to the average of the pixels around the pixel. 
 * This code implements the idea of serialism that will be measured against a paralized program 
 * @author RacquelDennison
 * @since 2022-08-13
 * 
 */

// importing all the packages 

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class MedianFilterSerial {

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

    /*
     * making the value that will be used for the pixels
     */

    public static int makePixel(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    // main class
    /*
     * The main method will call the average and make pixel function in order to
     * filter the image
     * 
     * @param arsg are used
     * arg[0] - name of the image
     * arg[1] - name of the image it is being sent to
     * arg[2] - the window size that will be filtered
     */
    public static void main(String[] args) throws IOException {
        File imageFile = null; // args[0]
        BufferedImage image = null;
        String windowSize = args[2]; // args[2]
        int sliderVariable = Integer.parseInt(windowSize);
        int radius = sliderVariable / 2;
        BufferedImage image2 = null; // args[1]

        try {
            // set all the variabls of the file
            imageFile = new File("pictures/samples/" + args[0] + ".jpg"); // TODO jpg
            image = ImageIO.read(imageFile);
            // getting the dimensions of the image
            image2 = ImageIO.read(imageFile);

        }

        catch (IOException e) {
            System.out.println("Error was faced: " + e);
        }

        // compute method idea
        // timed section
        for (int r = 0; r < 10; r++) {
            long startTime = System.currentTimeMillis();
            for (int x = radius; x < image.getWidth() - radius; x++) {
                for (int y = radius; y < image.getHeight() - radius; y++) {
                    // call an average method
                    image2.setRGB(x, y, average(x, y, image, radius));
                }

            }
            // creating a path to store the timemake
            long endTime = System.currentTimeMillis();

            String oFile = ("results/median/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
            File outputfile = new File(
                    "pictures/median/medianParallel" + "kernelValue" + sliderVariable + "_" + args[1] + ".jpg");
            String timeTaken = ("Report for serial median--------------------------------" + "\n"
                    + "Time taken for mean serial : " + (endTime - startTime) + " seconds"
                    + " at a slider value of "
                    + sliderVariable + '\n' + "------------------------"
                    + " Width : " + image.getWidth() + " Height: " + image.getHeight() +
                    " -----------------------------------------------" + '\n');
            Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            ImageIO.write(image2, "jpg", outputfile);

        }

    }
}
