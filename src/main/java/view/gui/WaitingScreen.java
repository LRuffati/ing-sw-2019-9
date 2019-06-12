package view.gui;

import gamemanager.ParserConfiguration;
import network.Player;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

//todo: check temporizzazione. Quando faccio la login mi arriva prima quella o no?
public class WaitingScreen extends JFrame {

    List<JPanel> playerList;
    JTextPane messageArea;

    private void appendToPane(JTextPane tp, String msg, Color c) {
        msg = msg + "\n";

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        try {
            StyledDocument doc = tp.getStyledDocument();
            doc.insertString(doc.getLength(), msg, aset);
        }
        catch (BadLocationException e) {
            System.out.println(e.offsetRequested());
        }
    }

    WaitingScreen(Framework framework) {
        super("ADRENALINE - Waiting for other players");

        setLayout(new GridLayout());

        JLabel label = new JLabel("Waiting some players");
        label.setFont(new Font(label.getName(), Font.BOLD, 20));
        add(label);

        messageArea = new JTextPane();
        messageArea.setEditable(false);
        messageArea.setSize(new Dimension(2,10));
        JScrollPane messageAreaPane = new JScrollPane(messageArea);
        messageAreaPane.setSize(2,10);

        JPanel players = new JPanel();
        players.setLayout(new FlowLayout());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void onConnection(Player player, boolean connected) {
        appendToPane(messageArea, String.format("%s %s", player.getUsername(), connected ? "connected" : "disconnected"), Color.black);
        if(connected)
            playerList.add(new GifPane());
        else
            playerList.remove(playerList.size());

        revalidate();
        repaint();
    }

    public void onTimer(int timeToCount) {
        appendToPane(messageArea, "Game will start in "+timeToCount/1000+" seconds!", Color.blue);
    }
}

class GifPane extends JPanel {

    private ImageIcon image;

    GifPane() {
        image = new ImageIcon(ParserConfiguration.parsePath("GuiDirectoryPath") + "parrot.gif");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int imageWidth = image.getIconWidth();
        int imageHeight = image.getIconHeight();

        if (imageWidth == 0 || imageHeight == 0) {
            return;
        }
        double widthScale = (double)getWidth() / (double)imageWidth;
        double heightScale = (double)getHeight() / (double)imageHeight;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(image.getImage(), AffineTransform.getScaleInstance(widthScale, heightScale), this);
        g2d.dispose();
    }

}
