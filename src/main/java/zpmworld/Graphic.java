package zpmworld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Graphic extends JPanel{
    private final int WIDTH;
    private final int HEIGHT;
    private List<Drawable> drawableFields;
    private List<Drawable> drawableUnits;
    private Game game;
    private boolean updateSort;

    public Graphic(int WIDTH, int HEIGHT){
        super();
        updateSort = true;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        drawableFields = new ArrayList<Drawable>();
        drawableUnits = new ArrayList<Drawable>();
        setBorder(BorderFactory.createRaisedBevelBorder());
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
    }

    public void update(){
        if(updateSort) {
            drawableUnits.sort(new Comparator<Drawable>() {
                public int compare(Drawable o1, Drawable o2) {
                    return o1.compareTo(o2);
                }
            });
            updateSort = false;
        }
        //paintComponent(this.getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Drawable drawable : drawableFields) {
            drawable.draw(g);
        }
        for(Drawable drawable : drawableUnits){
            drawable.draw(g);
        }
    }


    public void setGame(Game game){
        this.game = game;
    }

    public void registerDrawableField(Drawable fieldDrawable){
        fieldDrawable.setSizeReference(new Point((int)(WIDTH*0.065),(int)(HEIGHT*0.093)));
        fieldDrawable.setScale(HEIGHT/(double)540);
        drawableFields.add(fieldDrawable);
        updateSort = true;
    }

    public void registerDrawableUnit(Drawable unitDrawable){
        unitDrawable.setSizeReference(new Point((int)(WIDTH*0.065),(int)(HEIGHT*0.093)));
        unitDrawable.setScale((HEIGHT/(double)540));
        drawableUnits.add(unitDrawable);
        updateSort = true;
    }

    public void deleteDrawableField(Field field){
        for(int i = 0; i < drawableFields.size(); i++){
            if(drawableFields.get(i).getField() == field)
                drawableFields.remove(drawableFields.get(i));
        }
        updateSort = true;
    }

    public void deleteDrawableUnit(Unit unit){
        for(int i = 0; i < drawableUnits.size(); i++){
            if(drawableUnits.get(i).getUnit() == unit)
                drawableUnits.remove(drawableUnits.get(i));
        }
        updateSort = true;
    }
}
