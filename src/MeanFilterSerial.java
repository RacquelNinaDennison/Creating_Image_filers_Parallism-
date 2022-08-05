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

public class MeanFilterSerial {

    // methods

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

    public static int makePixel(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    // main class
    public static void main(String[] args) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        File imageFile; // args[0]
        BufferedImage image = null;
        int sliderVariable = 9; // args[2]
        int radius = sliderVariable / 2;
        BufferedImage image2 = null; // args[1]

        try {
            // set all the variabls of the file
            imageFile = new File("example.jpg"); // TODO jpg
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

        for (int x = radius; x < maxWidth - radius; ++x) {
            for (int y = radius; y < maxHeight - radius; ++y) {
                // call an average method
                image2.setRGB(x, y, average(x, y, image, radius));
            }

        }
        File outputfile = new File("task1output3x3.png");

        // TODO- maybe just writing to the thing will work
        ImageIO.write(image2, "jpg", outputfile);

    }

}
