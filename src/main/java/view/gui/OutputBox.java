package view.gui;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;

public class OutputBox extends JPanel {
    private JTextPane screenPane;

    public OutputBox(){
        screenPane = new JTextPane(new DefaultStyledDocument());
        screenPane.setEditable(false);
        screenPane.setSize(new Dimension(300,400));
        screenPane.setPreferredSize(new Dimension(300,400));
        screenPane.setText(">>Giocatore A spara a B\nC ha lasciato la partita!\nGiocatore E si è mosso nella tile" +
                "(4,5)!");
        //setLayout(new BorderLayout());
        add(screenPane, BorderLayout.CENTER);
        setSize(new Dimension(300,400));
        setPreferredSize(new Dimension(300,400));
        setLayout(new BorderLayout());
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.add(new OutputBox());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void writeOnOutput(String str){
        screenPane.setText(str);
    }
}
