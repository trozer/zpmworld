package zpmworld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tóth on 2016. 05. 03..
 */
public class Menu extends JPanel{
    private JButton newGame;
    private JButton loadGame;
    private JButton saveGame;
    private JButton quitGame;
    private JButton pause;
    private boolean isPaused;

    private MainFrame mainFrame;

    Menu(){
        super(new FlowLayout(FlowLayout.LEFT));
        isPaused = false;
        newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               newGame();
            }
        });
        add(newGame);
        loadGame = new JButton("Load Game");
        add(loadGame);
        saveGame = new JButton("Save Game");
        add(saveGame);
        quitGame = new JButton("Quit Game");
        add(quitGame);
        pause = new JButton("Pause");
        add(pause);
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isPaused){
                    pause.setText("Pause");
                    isPaused = false;
                } else {
                    pause.setText("Resume");
                    isPaused = true;
                }
                pause();
            }
        });

        saveGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               save();
            }
        });

        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });

        quitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        setBorder(BorderFactory.createRaisedBevelBorder());

    }

    private void newGame(){
        mainFrame.renderGame();
    }

    private void pause(){ mainFrame.pause(); }
    private void save(){ mainFrame.save(); }
    private void load(){ mainFrame.load(); }

    public void setMainFrame(MainFrame frame){
        mainFrame = frame;
    }

    public void exit(){
        mainFrame.exit();
    }

}
