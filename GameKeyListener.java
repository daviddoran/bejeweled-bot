package bejeweled;

import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameKeyListener extends Frame implements KeyListener {
    Label l1;

    public GameKeyListener(String s) {
        super(s);
        Panel p = new Panel();
        l1 = new Label("Key Listener!");
        p.add(l1);
        add(p);
        addKeyListener(this);
        setSize(200, 100);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void keyTyped(KeyEvent e) {
        l1.setText("Key Typed");
        if (e.getKeyChar() == 'x') {
            System.out.println("Exiting for user!");
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent e) {
        l1.setText("Key Pressed");
    }

    public void keyReleased(KeyEvent e) {
        l1.setText("Key Released");
    }
} 