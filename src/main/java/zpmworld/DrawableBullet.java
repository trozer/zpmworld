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
        super(5);
        this.bullet = bullet;
        setImgs();
    }

    public void draw(Graphics graphic){
        if(bullet.getCurrentField() != null) {
            Point pos = bullet.getCurrentField().getPosition();

            int imageByDir = 0;
            Color color = bullet.getColor();
            if(color.equals(Color.BLUE))
                switch (bullet.getCurrentDirection()){
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
            if(color.equals(Color.YELLOW))
                switch (bullet.getCurrentDirection()){
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
            if(color.equals(Color.RED))
                switch (bullet.getCurrentDirection()){
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
            if(color.equals(Color.GREEN))
                switch (bullet.getCurrentDirection()){
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

            graphic.drawImage(img.get(imageByDir), (int) pos.getX() * (int) sizeReference.getX(), (int) pos.getY() * (int) sizeReference.getY(),
                    (int) (Scale * img.get(imageByDir).getWidth()), (int) (Scale * img.get(imageByDir).getHeight()), null);
        }
    }

    public void setImgs() throws IOException {
        if (bullet.getColor().equals(Color.BLUE)){
            img.add(ImageIO.read(new File("bulletbbal.PNG")));
            img.add(ImageIO.read(new File("bulletbfel.PNG")));
            img.add(ImageIO.read(new File("bulletbjobb.PNG")));
            img.add(ImageIO.read(new File("bulletble.PNG")));
        }else if(bullet.getColor().equals(Color.YELLOW)){
            img.add(ImageIO.read(new File("bulletybal.PNG")));
            img.add(ImageIO.read(new File("bulletyfel.PNG")));
            img.add(ImageIO.read(new File("bulletyjobb.PNG")));
            img.add(ImageIO.read(new File("bulletyfle.PNG")));
        }else if(bullet.getColor().equals(Color.GREEN)){
            img.add(ImageIO.read(new File("bulletgbal.PNG")));
            img.add(ImageIO.read(new File("bulletgfel.PNG")));
            img.add(ImageIO.read(new File("bulletgjobb.PNG")));
            img.add(ImageIO.read(new File("bulletgle.PNG")));
        }else if(bullet.getColor().equals(Color.RED)){
            img.add(ImageIO.read(new File("bulletrbal.PNG")));
            img.add(ImageIO.read(new File("bulletrfel.PNG")));
            img.add(ImageIO.read(new File("bulletrjobb.PNG")));
            img.add(ImageIO.read(new File("bulletrle.PNG")));
        }

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
