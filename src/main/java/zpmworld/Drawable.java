package zpmworld;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Tóth on 2016. 05. 02..
 */
public abstract class Drawable implements Comparable{
    protected int Zindex;
    protected List<BufferedImage> img;
    protected Point sizeReference;
    protected double Scale;
    protected int deadTime;
    protected int sinceDead;

    Drawable(int index){
        Zindex = index;
        img = new ArrayList<BufferedImage>();
        deadTime = 0;
        sinceDead = 0;
    }

    public void setSizeReference(Point ref){
        sizeReference = ref;
    }

    public void setScale(double scale){
        Scale = scale;
    }

    public Unit getUnit(){ return null; }
    public Field getField(){ return  null; }

    public int compareTo(Object o){
        Drawable drawable = (Drawable) o;
        if(Zindex < drawable.getZindex()) return -1;
        else if(Zindex == drawable.getZindex()) return 0;
        else return 1;
    }

    public boolean finallyDead(){
        return true;
    }

    public int getZindex(){
        return Zindex;
    }

    public boolean compare(Field field){ return false; }
    public boolean compare(Unit unit){ return false; }

    public abstract void draw(Graphics graphic);
    public abstract void setImgs() throws IOException;
}
