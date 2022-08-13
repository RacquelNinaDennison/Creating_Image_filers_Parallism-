/* 
 * class used to test that the parallel and seial program produce the same images 
 *  @author RacquelDennison
 *  @since 2022-08-13
 */

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class test {

    /*
     * the main method will filter through the methods
     * 
     * @params args[0]-name of serial image
     * 
     * @param args[1]- name of the parallel image
     */

    public static void main(String args[]) throws IOException {

        File imageOne = new File("pictures/mean/" + args[0]);
        File imageTwo = new File("pictures/mean/" + args[1]);

        BufferedImage image1 = ImageIO.read(imageOne);
        BufferedImage image2 = ImageIO.read(imageTwo);
        boolean same = false;
        // iterating through each image
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
