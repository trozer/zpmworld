package zpmworld;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.io.File;

/**
 * Created by Tóth on 2016. 05. 03..
 */
public class MainFrame extends JFrame implements Runnable{
    private static MainFrame mainFrame = null;
    private static Menu menu = null;
    private static JPanel status = null;

    private Graphic stageGraphic = null;
    private Game game;

    public static int FWIDTH = 853;
    public static int FHEIGHT = 480;

    private  MainFrame(){
        super("ZPMWorld");
        setPreferredSize(new DimensionUIResource(FWIDTH,FHEIGHT));
        setFocusable(true);

        stageGraphic = new Graphic((int)(FWIDTH*0.8),FHEIGHT);
        stageGraphic.setPreferredSize(new Dimension((int)(FWIDTH*0.8),FHEIGHT));
        add(stageGraphic, BorderLayout.CENTER);
        menu = new Menu();
        add(menu, BorderLayout.NORTH);
        status = new JPanel();
        status.add(new JButton("Placeholder"));
        status.setPreferredSize(new Dimension((int)(FWIDTH*0.2),FHEIGHT));
        add(status, BorderLayout.EAST);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

    }

    public static void main(String[] args){
        MainFrame frame = getMainFrame();
        frame.revalidate();
    }

    public void renderGame(){
        try {
            game = new Game(stageGraphic);
            stageGraphic.setGame(game);
            game.newGame(new File("testMap.xml"));
            game.update();
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
    public void run(){}
}
