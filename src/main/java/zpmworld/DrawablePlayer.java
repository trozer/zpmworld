package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 03..
 */
public class DrawablePlayer extends Drawable{
    private Player player;

    DrawablePlayer(Player player) throws IOException {
        super(5);
        this.player = player;
        setImgs();
    }

    public void draw(Graphics graphic){
        Point pos = player.getCurrentField().getPosition();

        int imageByDir = 0;
        switch (player.getCurrentDirection()){
            case WEST:
                imageByDir = 0;
                break;
            case NORTH:
                imageByDir = 1;
                break;
            case EAST:
                imageByDir = 2;
                break;
            case SOUTH:
                imageByDir = 3;
                break;
            case NONE:
                break;
        }

        graphic.drawImage(img.get(imageByDir),(int)pos.getX()*(int)sizeReference.getX(),(int)pos.getY()*(int)sizeReference.getY(),
                (int)(Scale*img.get(imageByDir).getWidth()),(int)(Scale*img.get(0).getHeight()),null);
    }

    public void setImgs() throws IOException {
        if(player.getName().equals("Jaffa")){
            img.add(ImageIO.read(new File("jbal.PNG")));
            img.add(ImageIO.read(new File("jfel.PNG")));
            img.add(ImageIO.read(new File("jjobb.PNG")));
            img.add(ImageIO.read(new File("jle.PNG")));
        }else{
            img.add(ImageIO.read(new File("obal.PNG")));
            img.add(ImageIO.read(new File("ofel.PNG")));
            img.add(ImageIO.read(new File("ojobb.PNG")));
            img.add(ImageIO.read(new File("ole.PNG")));
        }
    }


    @Override
    public Unit getUnit(){
        return player;
    }

    @Override
    public boolean compare(Unit unit){
        if(player == unit)
            return true;
        return false;
    }
}
