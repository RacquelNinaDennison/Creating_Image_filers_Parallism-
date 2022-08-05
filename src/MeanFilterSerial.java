import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class MeanFilterSerial {

    public int average(ArrayList<Integer> array) {
        int sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);

        }
        return sum / array.size();
    }

    public static void main(String[] args) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        File imageFile;
        BufferedImage image = null;
        int sliderVariable = 3;
        int kernelWidth = sliderVariable;
        int kernelHeight = sliderVariable;
        int stoppingVariable = sliderVariable / 2;
        int counterVariable = stoppingVariable;
        // declaring the arrays to store the pixels
        ArrayList<Integer> redPixels = new ArrayList<>();
        ArrayList<Integer> bluePixels = new ArrayList<>();
        ArrayList<Integer> greenPixels = new ArrayList<>();
        boolean keepGoing = true;
        MeanFilterSerial classObject = new MeanFilterSerial();

        int output[][];
        try {
            imageFile = new File("minions.jfif");
            image = ImageIO.read(imageFile);
            // getting the dimensions of the image
            maxHeight = image.getHeight();
            maxWidth = image.getWidth();
        }

        catch (IOException e) {
            System.out.println("Error was faced: " + e);
        }

        // inserting the coloration of each image pixel into the array
        // int picturefile[][] = new int[maxHeight][maxWidth];
        // for (int i = 0; i < maxHeight; i++) {
        // for (int j = 0; j < maxWidth; j++) {
        // picturefile[i][j] = image.getRGB(j, i);
        // // System.out.println(image.getRGB(j, i));

        // }
        // }

        for (int v = stoppingVariable; v < maxHeight - stoppingVariable; v++) {
            for (int u = stoppingVariable; u < maxWidth - stoppingVariable; u++) {
                for (int p = v - stoppingVariable; p < kernelHeight; p++) {
                    for (int q = u - stoppingVariable; q < kernelWidth; q++) {
                        // Color color = new Color(image.getRGB(p, q)); // getting the color at each
                        // // pixel
                        int pixelValue = image.getRGB(p, q);
                        int redValue = (pixelValue >> 16) & 0xff;
                        int blueValue = pixelValue & 0xff;
                        int greenValue = (p >> 8) & 0xff;
                        redPixels.add(redValue);
                        bluePixels.add(blueValue);
                        greenPixels.add(greenValue);
                    }

                }
                int redAverage = classObject.average(redPixels);
                int blueAverage = classObject.average(bluePixels);
                int greenAverage = classObject.average(greenPixels);
                int p = (redAverage + blueAverage + greenAverage) / 3;
                image.setRGB(v, u, p);

            }
        }

        // re-inserting the image

        // BufferedImage theImage = new BufferedImage(
        // maxHeight,
        // maxWidth,
        // BufferedImage.TYPE_INT_RGB);
        // int value;
        // for (int y = 1; y < maxHeight; y++) {
        // for (int x = 1; x < maxWidth; x++) {
        // value = picturefile[y][x];
        // theImage.setRGB(y, x, value);
        // }
        // }

        // File outputfile = new File("task1output3x3.png");
        // ImageIO.write(theImage, "png", outputfile);
    }
}
