package zpmworld;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tóth on 2016. 05. 04..
 */
public class Status extends JPanel {

    JPanel Jaffa;
    JPanel Oneill;


    // TODO majd valaki ezt fejezze be :D
    Status(){
        super();
        setLayout(new GridLayout(2,0));
        Jaffa = new JPanel();
        Oneill = new JPanel();
        Oneill.setLayout(new FlowLayout());
        Jaffa.setLayout(new FlowLayout());
        JLabel Oneilllabel = new JLabel("O'Neill");
        Oneilllabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        Oneill.add(Oneilllabel);
        Jaffa.add(new JLabel("Jaffa",SwingConstants.RIGHT));
        Jaffa.add(new JLabel("Jani",SwingConstants.RIGHT));
        add(Oneill);
        add(Jaffa);
        setBorder(BorderFactory.createRaisedBevelBorder());
        Jaffa.setBorder(BorderFactory.createRaisedBevelBorder());
        Oneill.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
