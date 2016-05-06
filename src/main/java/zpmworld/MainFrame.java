package zpmworld;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Tóth on 2016. 05. 03..
 */
public class MainFrame extends JFrame{
    private static MainFrame mainFrame = null;
    private static Menu menu = null;
    private static Status status = null;

    private Graphic stageGraphic = null;
    private Game game;

    //16:9-re van optimalizalva
    private static int FWIDTH = 1366;
    private static int FHEIGHT = 768;

    private static int GAMESPEED = 70; //update millisec
    private static int FPS = 16; //update millisec

    private  MainFrame() {
        super("ZPMWorld");
        setPreferredSize(new DimensionUIResource(FWIDTH, FHEIGHT));
        setFocusable(true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.75;
        c.ipady = (int) (FHEIGHT*0.01);

        menu = new Menu();
        add(menu, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.25;
        c.gridheight = 2;

        status = new Status(FHEIGHT);
        status.setPreferredSize(new Dimension((int) (FWIDTH * 0.15), FHEIGHT));
        add(status, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.ipady = (int) (FHEIGHT*0.9);

        //harmadik parameterrel lehet allitani a "nagyitast"
        stageGraphic = new Graphic((int) (FWIDTH * 0.8), FHEIGHT, 0.7);
        stageGraphic.setPreferredSize(new Dimension((int) (FWIDTH * 0.8), FHEIGHT));
        add(stageGraphic, c);


        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        try {
            game = new Game(stageGraphic);
            stageGraphic.addKeyListener(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        MainFrame frame = getMainFrame();
        frame.revalidate();
    }

    public void pause(){
        game.pause();
    }

    public void renderGame(){
        try {
            stageGraphic.requestFocus();
            stageGraphic.setGame(game);
            game.newGame(new File("testMap.xml"));
            game.update();
            game.setState(State.GAME);
            Timer updateTimer = new Timer(GAMESPEED, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(game.getState() != State.PAUSE)
                        game.update();
                }
            });
            Timer paintTimer = new Timer(FPS, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(game.getState() != State.PAUSE) {
                        stageGraphic.repaint();
                    }
                }
            });
            updateTimer.setInitialDelay(100);
            paintTimer.setInitialDelay(200);
            updateTimer.start();
            paintTimer.start();
            //game.console() //ha konzolon akar valaki tesztelni...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MainFrame getMainFrame(){
        if(mainFrame == null) {
            mainFrame = new MainFrame();
            menu.setMainFrame(mainFrame);
        }
        return mainFrame;
    }
}
