import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class test {

    public static void main(String args[]) throws IOException {

        File imageOne = new File("pictures/mean/meanSerialkernelValue9_example1.jpg");
        File imageTwo = new File("pictures/mean/meanParallelkernelValue9_example1.jpg");

        BufferedImage image1 = ImageIO.read(imageOne);
        BufferedImage image2 = ImageIO.read(imageTwo);
        boolean same = false;
        for (int i = 0; i < image1.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                if (image1.getRGB(i, j) == image2.getRGB(i, j)) {
                    same = true;
                }
            }
        }

        if (same) {
            System.out.println("The images are the same");

        }

        else {
            System.out.println("The images are not the same");

        }

    }

}
