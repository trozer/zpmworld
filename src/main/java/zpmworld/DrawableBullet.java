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
        super(2);
        this.bullet = bullet;
        setImgs();
    }

    public void draw(Graphics graphic){
        if(bullet.getCurrentField() != null) {
            Point pos = bullet.getCurrentField().getPosition();
            graphic.drawImage(img.get(0), (int) pos.getX() * (int) sizeReference.getX(), (int) pos.getY() * (int) sizeReference.getY(),
                    (int) (Scale * img.get(0).getWidth()), (int) (Scale * img.get(0).getHeight()), null);
        }
    }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("doboz.PNG")));
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
