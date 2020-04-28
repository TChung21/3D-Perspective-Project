import javax.xml.soap.Text;
import java.util.ArrayList;
import java.awt.Color;

public class Screen {

    public int[][] map; //same map created in the game
    //the screen uses this to figure out where walls are and how far away from the player they are
    public int mapWidth, mapHeight, width, height; //define size of screen (same as frame created in main)
    public ArrayList <Texture> textures; //same list of textures

    public Screen(int[][] m, int mapW, int mapH, ArrayList tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;

    }

    public int[] update(Camera camera, int[] pixels) { //recalculates how the screen should look to the user
        for (int n = 0; n < pixels.length / 2; n++) {
            Color color = new Color(255-(n/1200),255-(n/1200),255-(n/1200));
            if(pixels[n] != color.getRGB()) pixels[n] = color.getRGB();
        }

        for (int i = pixels.length / 2; i < pixels.length; i++) {
            Color color2 = new Color((i/1200),(i/1200),(i/1200));
            if(pixels[i] != color2.getRGB()) pixels[i] = color2.getRGB();
        }

        //goes through every vertical bar on screen and casts a ray to figure out where wall should be
        for (int x = 0; x < width; x = x + 1) {
            double cameraX = 2 * x / (double) (width) - 1; //current x coordinate of the vertical stripe on camera plane
            double rayDirX = camera.xDir + camera.xPlane * cameraX; //vector for ray
            double rayDirY = camera.yDir + camera.yPlane * cameraX;
            //Map position
            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist; //distance from player to the first wall the ray collides with
            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side = 0;//was the wall vertical or horizontal


            //Figure out the step direction and initial distance to a side
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }

            //Loop to find where the ray hits a wall
            while (!hit) {
                //Jump to next square
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                if (map[mapX][mapY] > 0) hit = true; //we now know where the ray hits
            }

            //Calculate distance to the point of impact
            if (side == 0)
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);

            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if (perpWallDist > 0) lineHeight = Math.abs((int) (height / perpWallDist));
            else lineHeight = height;

            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height)
                drawEnd = height - 1;

            //add a texture
            int texNum = map[mapX][mapY] - 1;
            double wallX;//Exact position of where wall was hit
            if (side == 1) {//If its a y-axis wall
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX);
                //where wall was hit minus integer value which is multiplied by size of the texture
            } else {//X-axis wall
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }
            wallX -= Math.floor(wallX);
            int texX = (int)(wallX * ((textures.get(texNum)).SIZE));
            if(side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZE - texX - 1;
            if(side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZE - texX - 1;
            //calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(side==0) color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                else color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]>>1) & 8355711;//Make y sides darker
                pixels[x + y*(width)] = color;
            }

        }
        //writes data from the pixel on the texture into an array of pixels on the screen
        //horizontal walls darker than vertical walls

        return pixels;

    }


}


