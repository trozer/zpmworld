package zpmworld;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Graphic extends JPanel{
    private final int WIDTH;
    private final int HEIGHT;
    private List<Drawable> drawableFields;
    private List<Drawable> drawableUnits;
    private Game game;

    public Graphic(int WIDTH, int HEIGHT){
        super();
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        drawableFields = new ArrayList<Drawable>();
        drawableUnits = new ArrayList<Drawable>();
    }

    public void update(){
        for(Drawable drawable : drawableFields)
            drawable.draw(this.getGraphics());
        for(Drawable drawable : drawableUnits){
            drawable.draw(this.getGraphics());
        }
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void registerDrawableField(Drawable fieldDrawable){
        fieldDrawable.setSizeReference(new Point((int)(WIDTH*0.071),(int)(HEIGHT*0.1)));
        fieldDrawable.setScale(HEIGHT/480);
        drawableFields.add(fieldDrawable);
    }

    public void registerDrawableUnit(Drawable unitDrawable){
        drawableUnits.add(unitDrawable);
    }

    public void deleteDrawableField(Field field){
        for(int i = 0; i < drawableFields.size(); i++){
            if(drawableFields.get(i).getField() == field)
                drawableFields.remove(drawableFields.get(i));
        }
    }

    public void deleteDrawableUnit(Unit unit){
        for(int i = 0; i < drawableUnits.size(); i++){
            if(drawableUnits.get(i).getUnit() == unit)
                drawableUnits.remove(drawableUnits.get(i));
        }
    }
}
