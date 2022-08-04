import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;;

public class MeanFilterSerial {

    public static void main(String[] args) throws IOException {
        int maxHeight = 0;
        int maxWidth = 0;
        File imageFile;
        BufferedImage image = null;
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
        int picturefile[][] = new int[maxHeight][maxWidth];
        for (int i = 0; i < maxHeight; i++) {
            for (int j = 0; j < maxWidth; j++) {
                picturefile[i][j] = image.getRGB(j, i);
            }
        }

    }
}
