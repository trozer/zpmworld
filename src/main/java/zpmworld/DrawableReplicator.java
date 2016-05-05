package zpmworld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 05..
 */
public class DrawableReplicator extends Drawable{
    private Replicator replicator;

    DrawableReplicator(Replicator replicator) throws IOException {
        super(4);
        this.replicator = replicator;
        setImgs();
    }

    public void draw(Graphics graphic){
        if(replicator.getCurrentField() != null) {
            Point pos = replicator.getCurrentField().getPosition();
            graphic.drawImage(img.get(0), (int) pos.getX() * (int) sizeReference.getX(), (int) pos.getY() * (int) sizeReference.getY(),
                    (int) (Scale * img.get(0).getWidth()), (int) (Scale * img.get(0).getHeight()), null);
        }
    }

    public void setImgs() throws IOException {
        img.add(ImageIO.read(new File("replicator.PNG")));
    }


    @Override
    public Unit getUnit(){
        return replicator;
    }

    @Override
    public boolean compare(Unit unit){
        if(replicator == unit)
            return true;
        return false;
    }
}
