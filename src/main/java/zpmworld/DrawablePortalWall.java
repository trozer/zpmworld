package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawablePortalWall extends Drawable{
    PortalWall field;

    DrawablePortalWall(PortalWall portalWall) throws IOException {
        super(0);
        setImgs();
        field = portalWall;
    }

    public void draw(Graphics graphic){
        Point pos = field.getPosition();
        int imageByDir = 0;

        Portal portalInfo = field.getPortal();

        if(!portalInfo.amIPortal(field))
            imageByDir = 0;
        else{
           Color color = portalInfo.getColor(field);
            if(color.equals(Color.BLUE))
                imageByDir = 1;
            if(color.equals(Color.YELLOW))
                imageByDir = 2;
            if(color.equals(Color.RED))
                imageByDir = 3;
            if(color.equals(Color.GREEN))
                imageByDir = 4;
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
        img.add(ImageIO.read(new File("pfal.PNG")));
        img.add(ImageIO.read(new File("p1.PNG")));
        img.add(ImageIO.read(new File("p2.PNG")));
        img.add(ImageIO.read(new File("p3.PNG")));
        img.add(ImageIO.read(new File("p4.PNG")));
    }
}
