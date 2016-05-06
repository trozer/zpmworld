package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawableBullet extends Drawable{
    private Bullet bullet;

    DrawableBullet(Bullet bullet) throws IOException {
        super(6);
        this.bullet = bullet;
        setImgs();
    }

    public void draw(Graphics graphic){
        if(bullet.getCurrentField() != null) {
            Point pos = bullet.getCurrentField().getPosition();

            int imageByDir = 0;
            Color color = bullet.getColor();
            if(color.equals(Color.BLUE))
                imageByDir = 0;
            if(color.equals(Color.YELLOW))
                imageByDir = 1;
            if(color.equals(Color.RED))
                imageByDir = 2;
            if(color.equals(Color.GREEN))
                imageByDir = 3;

            graphic.drawImage(img.get(imageByDir), (int) pos.getX() * (int) sizeReference.getX(), (int) pos.getY() * (int) sizeReference.getY(),
                    (int) (Scale * img.get(imageByDir).getWidth()), (int) (Scale * img.get(imageByDir).getHeight()), null);
        }
    }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("bulletb.PNG")));
        img.add(ImageIO.read(new File("bullety.PNG")));
        img.add(ImageIO.read(new File("bulletr.PNG")));
        img.add(ImageIO.read(new File("bulletg.PNG")));
    }


    @Override
    public Unit getUnit(){
        return bullet;
    }

    @Override
    public boolean compare(Unit unit){
        if(bullet == unit)
            return true;
        return false;
    }
}
