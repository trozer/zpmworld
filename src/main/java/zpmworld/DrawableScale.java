package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawableScale extends Drawable{
    Scale field;

    DrawableScale(Scale scale) throws IOException {
        super(0);
        setImgs();
        field = scale;
    }

    public void draw(Graphics graphic){
        Point pos = field.getPosition();
        int imageByDir = 0;
        switch (field.getBoxNum()){
            case 0:
                imageByDir = 0;
                break;
            case 1:
                imageByDir = 0;
                break;
            case 2:
                imageByDir = 2;
                break;
            default:
                imageByDir = 3;
        }

            graphic.drawImage(img.get(imageByDir),(int)pos.getX()*(int)sizeReference.getX(),(int)pos.getY()*(int)sizeReference.getY(),
                    (int)(Scale*img.get(imageByDir).getWidth()),(int)(Scale*img.get(imageByDir).getHeight()),null);
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
        img.add(ImageIO.read(new File("merleg1.PNG")));
        img.add(ImageIO.read(new File("merleg1le.PNG")));
        img.add(ImageIO.read(new File("merleg2d.PNG")));
        img.add(ImageIO.read(new File("merleg3d.PNG")));
        img.add(ImageIO.read(new File("merleg2.PNG")));
    }
}
