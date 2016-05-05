package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawableGate extends Drawable{
    Gate field;

    DrawableGate(Gate gate) throws IOException {
        super(0);
        setImgs();
        field = gate;
    }

    public void draw(Graphics graphic){
        Point pos = field.getPosition();
        if(field.isOpened())
            graphic.drawImage(img.get(0),(int)pos.getX()*(int)sizeReference.getX(),(int)pos.getY()*(int)sizeReference.getY(),
                (int)(Scale*img.get(0).getWidth()),(int)(Scale*img.get(0).getHeight()),null);
        else
            graphic.drawImage(img.get(1),(int)pos.getX()*(int)sizeReference.getX(),(int)pos.getY()*(int)sizeReference.getY(),
                    (int)(Scale*img.get(1).getWidth()),(int)(Scale*img.get(1).getHeight()),null);
    }

    @Override
    public Field getField(){ return  field; }

    @Override
    public boolean compare(Field field){
        if(this.field == field)
            return true;
        return false;
    }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("kapu.PNG")));
        img.add(ImageIO.read(new File("kapu2.PNG")));
    }
}
