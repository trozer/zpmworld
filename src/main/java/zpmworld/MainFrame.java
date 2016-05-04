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
    private static Thread updateLoop = null;

    private Graphic stageGraphic = null;
    private Game game;

    //16:9-re van optimalizalva
    public static int FWIDTH = 1366;
    public static int FHEIGHT = 768;

    private  MainFrame() {
        super("ZPMWorld");
        setPreferredSize(new DimensionUIResource(FWIDTH, FHEIGHT));
        setFocusable(true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = (int) (FHEIGHT*0.9);
        c.ipadx = (int) (FWIDTH*0.65);

        stageGraphic = new Graphic((int) (FWIDTH * 0.8), FHEIGHT);
        stageGraphic.setPreferredSize(new Dimension((int) (FWIDTH * 0.8), FHEIGHT));
        add(stageGraphic, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = (int) (FHEIGHT*0.1);

        menu = new Menu();
        add(menu, c);

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.ipadx = (int) (FWIDTH*0.35);
        c.ipady = (int) (FHEIGHT);

        status = new Status();
        status.setPreferredSize(new Dimension((int) (FWIDTH * 0.2), FHEIGHT));
        add(status, c);

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
       // frame.revalidate();
    }

    public void renderGame(){
        try {
            stageGraphic.requestFocus();
            stageGraphic.setGame(game);
            game.newGame(new File("testMap.xml"));
            game.update();
            game.setState(State.GAME);
            Timer updateTimer = new Timer(16, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    game.update();
                }
            });
            Timer paintTimer = new Timer(16, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stageGraphic.repaint();
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
