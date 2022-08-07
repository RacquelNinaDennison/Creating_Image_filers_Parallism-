
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;

public class MeanFilterSerial {

    // methods

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
            imageFile = new File("pictures/samples/example.jpg"); // change the arguments
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
        long startTime = System.currentTimeMillis();
        for (int x = radius; x < maxWidth - radius; ++x) {
            for (int y = radius; y < maxHeight - radius; ++y) {
                // call an average method
                image2.setRGB(x, y, average(x, y, image, radius));
            }

        }
        long endTime = System.currentTimeMillis();
        String oFile = ("results/mean/" + imageFile.getName() + "_" + sliderVariable + "sliderVariable.txt");
        File outputfile = new File("pictures/mean/meanSerial" + "kernelValue" + sliderVariable + ".jpg");
        String timeTaken = ("Time taken for mean serial: " + (endTime - startTime) / 1000.00 + " seconds"
                + " at a slider value of "
                + sliderVariable + '\n' + "----------------------------------------------------------" + '\n');
        Files.write(Paths.get(oFile), timeTaken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        ImageIO.write(image2, "jpg", outputfile);

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }

}
