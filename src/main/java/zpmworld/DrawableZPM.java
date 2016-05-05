package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawableZPM extends Drawable{
    private ZPM zpm;

    DrawableZPM(ZPM zpm) throws IOException {
        super(3);
        this.zpm = zpm;
        setImgs();
    }

    public void draw(Graphics graphic){
        if(zpm.getCurrentField() != null) {
            Point pos = zpm.getCurrentField().getPosition();
            graphic.drawImage(img.get(0), (int) pos.getX() * (int) sizeReference.getX(), (int) pos.getY() * (int) sizeReference.getY(),
                    (int) (Scale * img.get(0).getWidth()), (int) (Scale * img.get(0).getHeight()), null);
        }
    }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("ZPM.PNG")));
    }


    @Override
    public Unit getUnit(){
        return zpm;
    }

    @Override
    public boolean compare(Unit unit){
        if(zpm == unit)
            return true;
        return false;
    }
}
