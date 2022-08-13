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
import java.nio.file.*;

public class MeanFilterSerial {
    public static BufferedImage image2 = null;
    /*
     * The average will comput the average of the surround pixels
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

            for (int j = y - radius; j <= y + radius; ++j) {
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

        BufferedImage image = null;
        String windowSize = args[2];
        int sliderVariable = Integer.parseInt(windowSize);
        int radius = sliderVariable / 2;

        try {

            image = ImageIO.read(new File("pictures/samples/" + args[0] + ".jpg"));
            image2 = ImageIO.read(new File("pictures/samples/" + args[0] + ".jpg"));

        }

        catch (IOException e) {
            System.out.println("Error was faced: " + e);
        }

        for (int r = 0; r < 10; r++) {
            long startTime = System.currentTimeMillis();
            for (int x = radius; x < image.getWidth() - radius; ++x) {
                for (int y = radius; y < image.getHeight() - radius; ++y) {
                    // call an average method
                    image2.setRGB(x, y, average(x, y, image, radius));
                }

            }
            long endTime = System.currentTimeMillis();
            // writing to a text file
            String oFile = ("results/mean/" + (new File("pictures/samples/" + args[0] + ".jpg")).getName() + "_"
                    + sliderVariable + "sliderVariable.txt");
            File outputfile = new File(
                    "pictures/mean/meanSerial" + "kernelValue" + sliderVariable + "_" + args[1] + ".jpg");
            String timeTaken = ("Report for serial mean--------------------------------" + "\n"
                    + "Time taken for mean serial : " + (endTime - startTime) / 1000.00 + " seconds"
                    + " at a slider value of "
                    + sliderVariable + '\n' + "------------------------"
                    + " Width : " + image.getWidth() + " Height: " + image.getHeight() +
                    " -----------------------------------------------" + '\n');
            Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            ImageIO.write(image2, "jpg", outputfile);

        }

    }
}
