package zpmworld;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tóth on 2016. 05. 04..
 */
public class Status extends JPanel {

    private Game game;

    private JPanel Jaffa;
    private JPanel Oneill;
    private JLabel Oneillscore;
    private JLabel Oneillbox;
    private JLabel Oneillend;
    private JLabel Jaffascore;
    private JLabel Jaffabox;
    private JLabel Jaffaend;
    private JLabel Oneillskull;
    private JLabel Jaffaskull;


    private BufferedImage hasbox = null;
    private BufferedImage nobox = null;
    private BufferedImage onskull = null;
    private BufferedImage offskull = null;


    // TODO majd valaki ezt fejezze be :D
    Status(int HEIGHT){
        super();
        setLayout(new GridBagLayout());
        Jaffa = new JPanel();
        Oneill = new JPanel();
        Oneill.setLayout(new GridLayout(5, 1));
        Jaffa.setLayout(new GridLayout(5, 1));

        try {
            hasbox = ImageIO.read(new File("statusbox.PNG"));
            nobox = ImageIO.read(new File("statusnobox.PNG"));
            onskull = ImageIO.read(new File("onskull.PNG"));
            offskull = ImageIO.read(new File("offskull.PNG"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel Oneilllabel = new JLabel("O'Neill");
        Oneilllabel.setFont(new Font(Oneilllabel.getFont().getName(), Font.BOLD, 40));
        Oneill.add(Oneilllabel);

        Oneillscore = new JLabel("");
        Oneillscore.setFont(new Font(Oneillscore.getFont().getName(), Font.BOLD, 16));
        Oneill.add(Oneillscore);

        Oneillbox = new JLabel(new ImageIcon(nobox));
        Oneill.add(Oneillbox);

        Oneillskull = new JLabel(new ImageIcon(offskull));
        Oneill.add(Oneillskull);

        Oneillend = new JLabel("");
        Oneillend.setFont(new Font(Oneillend.getFont().getName(), Font.BOLD, 40));
        Oneillend.setVisible(false);
        Oneill.add(Oneillend);


        JLabel Jaffalabel = new JLabel("Jaffa");
        Jaffalabel.setFont(new Font(Jaffalabel.getFont().getName(), Font.BOLD, 40));
        Jaffa.add(Jaffalabel);

        Jaffascore = new JLabel("");
        Jaffascore.setFont(new Font(Jaffascore.getFont().getName(), Font.BOLD, 16));
        Jaffa.add(Jaffascore);

        Jaffabox = new JLabel(new ImageIcon(nobox));
        Jaffa.add(Jaffabox);

        Jaffaskull = new JLabel(new ImageIcon(offskull));
        Jaffa.add(Jaffaskull);

        Jaffaend = new JLabel("");
        Jaffaend.setFont(new Font(Jaffaend.getFont().getName(), Font.BOLD, 40));
        Jaffaend.setVisible(false);
        Jaffa.add(Jaffaend);


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;

        add(Oneill, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;

        add(Jaffa, c);

        setBorder(BorderFactory.createRaisedBevelBorder());
        Jaffa.setBorder(BorderFactory.createRaisedBevelBorder());
        Oneill.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void endgame(boolean oneillwins) {
        if (oneillwins) {
            Oneillend.setText("WIN");
            Oneillend.setForeground(Color.GREEN);
            Oneillend.setVisible(true);
            Jaffaend.setText("LOOSE");
            Jaffaend.setForeground(Color.RED);
            Jaffaend.setVisible(true);
        }
        else {
            Oneillend.setText("LOOSE");
            Oneillend.setForeground(Color.RED);
            Oneillend.setVisible(true);
            Jaffaend.setText("WIN");
            Jaffaend.setForeground(Color.GREEN);
            Jaffaend.setVisible(true);
        }
    }

    public void update(int sumzpm, int oneillzpm, int jaffazpm, Box oneillbox, Box jaffabox, boolean oneillkill, boolean jaffakill) {
        Oneillscore.setText("have: " + oneillzpm + ",  free: " + sumzpm + " ZPM");
        Jaffascore.setText("have: " + jaffazpm + ",  free: " + sumzpm + " ZPM");
        if (oneillbox != null){
            Oneillbox.setIcon(new ImageIcon(hasbox));
        }
        else {
            Oneillbox.setIcon(new ImageIcon(nobox));
        }
        if (jaffabox != null) {
            Jaffabox.setIcon(new ImageIcon(hasbox));
        }
        else {
            Jaffabox.setIcon(new ImageIcon(nobox));
        }
        if(oneillkill){
            Oneillskull.setIcon(new ImageIcon(onskull));
        } else{
            Oneillskull.setIcon(new ImageIcon(offskull));
        }
        if(jaffakill){
            Jaffaskull.setIcon(new ImageIcon(onskull));
        } else{
            Jaffaskull.setIcon(new ImageIcon(offskull));
        }
    }

    public void clear(){
        Oneillend.setVisible(false);
        Jaffaend.setVisible(false);
    }

    public void setGame(Game game){
        this.game = game;
    }
}
