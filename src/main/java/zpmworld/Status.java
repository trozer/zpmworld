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
    Status(int HEIGHT){
        super();
        setLayout(new GridBagLayout());
        Jaffa = new JPanel();
        Oneill = new JPanel();
        Oneill.setLayout(new FlowLayout());
        Jaffa.setLayout(new FlowLayout());


        JLabel Oneilllabel = new JLabel("O'Neill");
        Oneilllabel.setFont(new Font(Oneilllabel.getFont().getName(), Font.BOLD, 40));
        Oneilllabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        Oneill.add(Oneilllabel);

        JLabel Jaffalabel = new JLabel("Jaffa");
        Jaffalabel.setFont(new Font(Jaffalabel.getFont().getName(), Font.BOLD, 40));
        Jaffalabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        Jaffa.add(Jaffalabel);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.ipady = (int) (HEIGHT*0.392);

       add(Oneill, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
      c.ipady = (int) (HEIGHT*0.392);

        add(Jaffa, c);

        setBorder(BorderFactory.createRaisedBevelBorder());
        Jaffa.setBorder(BorderFactory.createRaisedBevelBorder());
        Oneill.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
