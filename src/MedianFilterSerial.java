
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class MedianFilterSerial {

    /*
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

    public static int makePixel(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    // main class
    public static void main(String[] args) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        File imageFile = null; // args[0]
        BufferedImage image = null;
        int sliderVariable = 9; // args[2]
        int radius = sliderVariable / 2;
        BufferedImage image2 = null; // args[1]

        try {
            // set all the variabls of the file
            imageFile = new File("pictures/samples/example.jpg"); // TODO jpg
            image = ImageIO.read(imageFile);
            // getting the dimensions of the image
            maxHeight = image.getHeight();
            maxWidth = image.getWidth();
            image2 = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            // new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        }

        catch (IOException e) {
            System.out.println("Error was faced: " + e);
        }
        // TODO Fix the looping variables
        // compute method idea
        // timed section

        long startTime = System.currentTimeMillis();
        for (int x = radius; x < maxWidth - radius; x++) {
            for (int y = radius; y < maxHeight - radius; y++) {
                // call an average method
                image2.setRGB(x, y, average(x, y, image, radius));
            }

        }
        // creating a path to store the timemake
        long endTime = System.currentTimeMillis();

        String oFile = ("results/median/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
        File outputfile = new File("pictures/median/medianSerial" + "kernelValue" + sliderVariable + ".jpg");
        String timeTaken = ("Time taken for median serial: " + (endTime - startTime) / 1000.00 + " seconds"
                + " at a slider value of "
                + sliderVariable + '\n' + "----------------------------------------------------------" + '\n');
        Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        ImageIO.write(image2, "jpg", outputfile);

    }

}
