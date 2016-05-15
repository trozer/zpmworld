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
    double Scale;

    public Graphic(int WIDTH, int HEIGHT, double Scale){
        super();
        updateSort = true;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        drawableFields = new ArrayList<Drawable>();
        drawableUnits = new ArrayList<Drawable>();
        setBorder(BorderFactory.createRaisedBevelBorder());
        setFocusable(true);
        this.Scale = Scale;
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
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(Drawable drawable : drawableFields) {
            drawable.draw(g);
        }

        Iterator<Drawable> drIt = drawableUnits.iterator();
        while(drIt.hasNext()){
            Drawable drawableUnit = drIt.next();
            if(drawableUnit.getUnit().isDead() && drawableUnit.finallyDead()) {
                drIt.remove();
            }
            else {
                drawableUnit.draw(g);
            }
        }
    }

    public void clear(){
        drawableFields = new ArrayList<Drawable>();
        drawableUnits = new ArrayList<Drawable>();
        getGraphics().clearRect(0,0,WIDTH,HEIGHT);
    }

    public void setGame(Game game){
        this.game = game;
    }

    public void registerDrawableField(Drawable fieldDrawable){
        fieldDrawable.setSizeReference(new Point((int)((WIDTH*0.0639)*Scale),(int)((HEIGHT*0.093)*Scale)));
        fieldDrawable.setScale((HEIGHT/(double)540)*Scale);
        drawableFields.add(fieldDrawable);
        updateSort = true;
    }

    public void registerDrawableUnit(Drawable unitDrawable){
        unitDrawable.setSizeReference(new Point((int)((WIDTH*0.0639)*Scale),(int)((HEIGHT*0.093)*Scale)));
        unitDrawable.setScale((HEIGHT/(double)540)*Scale);
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
}
