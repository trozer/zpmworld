package zpmworld;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

    private Clip music = null;

    //16:9-re van optimalizalva
    private static int FWIDTH = 1280;
    private static int FHEIGHT = 720;
    private  MainFrame() {
        super("ZPMWorld");
        setPreferredSize(new DimensionUIResource(FWIDTH, FHEIGHT));
        setFocusable(true);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8;

        double yRatio = 0.08/((double)FHEIGHT/(double)480);
        c.ipady = (int) (FHEIGHT*(yRatio));

        menu = new Menu();
        add(menu, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.2;
        c.gridheight = 2;

        status = new Status(FHEIGHT);
        status.setPreferredSize(new Dimension((int) (FWIDTH * 0.15), FHEIGHT));
        add(status, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        c.ipady = (int) (FHEIGHT*(1.0-yRatio));

        //harmadik parameterrel lehet allitani a "nagyitast" (az elso szamot allitsuk, a tobbi alkalmazkodik az ablakhoz)
        double scale = 0.560*((1.0-(yRatio-0.08)));
        stageGraphic = new Graphic((int) (FWIDTH * 0.8), FHEIGHT, scale);
        stageGraphic.setPreferredSize(new Dimension((int) (FWIDTH * 0.8), FHEIGHT));
        add(stageGraphic, c);


        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        try {
            game = new Game(stageGraphic, status);
            stageGraphic.addKeyListener(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        MainFrame frame = getMainFrame();
        //frame.revalidate();
    }

    public void pause(){
        if(music != null)
            if(!game.isPause())
                music.stop();
            else
                music.loop(Clip.LOOP_CONTINUOUSLY);
        game.pause();
    }

    public void renderGame(){
        try {
            if(music != null)
                music.stop();
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(
                            new File("bg.wav"));
            Clip clip = AudioSystem.getClip();
            this.music = clip;
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            stageGraphic.requestFocus();
            stageGraphic.setGame(game);
            status.setGame(game);
            game.newGame(new File("finalMap.xml"));
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
}
