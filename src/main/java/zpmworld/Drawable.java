package zpmworld;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by Tóth on 2016. 05. 02..
 */
public abstract class Drawable {
    private int Zindex;
    private java.util.List<BufferedImage> img;

    Drawable(int index){
        Zindex = index;
        img = new ArrayList<BufferedImage>();
    }

    public abstract void setImgs();


}
