import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
    public static Texture wood = new Texture("wood.jpg", 64);
    public static Texture brick = new Texture("brick.png", 64);
    public static Texture bluestone = new Texture("bluestone.jpg", 64);
    public static Texture stone = new Texture("stone.jpg", 64);
    public int[] pixels; //pixels for image of the texture
    private String loc; //where the image file can be found
    public final int SIZE; //final = can't be changed

    public Texture(String location, int size) {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load(); //gets data from images and stores them in the pixel array
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            pixels = new int [w * h];
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
