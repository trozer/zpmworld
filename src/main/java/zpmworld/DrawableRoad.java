package zpmworld;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 03..
 */
public class DrawableRoad extends Drawable{

    Road field;

    DrawableRoad(Road road) throws IOException {
        super(0);
        setImgs();
        field = road;
    }

    public void draw(Graphics graphic){
        Point pos = field.getPosition();
        graphic.drawImage(img.get(0),(int)pos.getX()*(int)sizeReference.getX(),(int)pos.getY()*(int)sizeReference.getY(),
                        Scale*img.get(0).getWidth(),Scale*img.get(0).getHeight(),null);
    }

    public Field getField(){ return  field; }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("ut.PNG")));
    }
}
