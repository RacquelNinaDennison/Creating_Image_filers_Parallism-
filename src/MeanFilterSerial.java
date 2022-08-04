import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;;

public class MeanFilterSerial {

    public static void main (String[] args) throws IOException {
        try{
        File imageFile = new File("minions.jfif");
        BufferedImage image = ImageIO.read(imageFile);
        //getting the dimensions of the image 
        int maxHeight = image.getHeight();
        int maxWidth =image.getWidth();

        

    } 
    catch (IOException e){
        System.out.println("Error was faced: "+e);
        

    }

}