package view.gui;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;

public class OutputBox extends JPanel {
    private JTextPane screenPane;

    public OutputBox(){
        this.screenPane = new JTextPane(new DefaultStyledDocument());
        this.screenPane.setEditable(false);
        this.screenPane.setSize(new Dimension(300,400));
        this.screenPane.setPreferredSize(new Dimension(300,400));
        add(this.screenPane, BorderLayout.CENTER);
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
        if(this.screenPane.getText()!=null) {
            this.screenPane.setText(this.screenPane.getText() + "\n" + str);
        } else {
            this.screenPane.setText(str);
        }
    }

}
