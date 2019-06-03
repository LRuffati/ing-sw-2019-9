package view.gui;

import controllerclient.ClientControllerClientInterface;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Login extends JPanel implements ActionListener{

    private static ClientControllerClientInterface cliController;
    private static GuiController guiManager;

    private JFrame frame;
    private JLabel titleLabel;
    private JLabel subTitleLabel;

    private JComboBox connectionMenu;

    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JTextField passwordField;
    private JComboBox color;
    private JButton confirm;

    private JLabel messageLabel;
    private JScrollPane messageAreaPane;
    private JTextPane messageArea;

    boolean connect = true;


    private GridBagConstraints put(int gridx, int gridy, int fill){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.fill = fill;
        return c;
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        msg = msg + "\n";

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    private Login() {
        super(new GridBagLayout());

        guiManager.attachView(this);

        titleLabel = new JLabel("ADRENALINE");
        titleLabel.setFont(new Font(titleLabel.getName(), Font.BOLD, 20));
        subTitleLabel = new JLabel("login");
        subTitleLabel.setFont(new Font(subTitleLabel.getName(), Font.PLAIN, 15));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField= new JTextField(10);

        messageArea = new JTextPane();
        messageArea.setEditable(true);
        messageArea.setSize(new Dimension(2,10));
        messageAreaPane = new JScrollPane(messageArea);


        connectionMenu = new JComboBox(List.of("Connect", "Reconnect").toArray());
        connectionMenu.addActionListener(this);


        confirm = new JButton("CONFERMA");
        confirm.addActionListener(this);


        add(titleLabel, put(0,0,GridBagConstraints.HORIZONTAL));

        add(subTitleLabel, put(0,1,GridBagConstraints.HORIZONTAL));

        add(usernameLabel, put(0,2,GridBagConstraints.HORIZONTAL));

        add(usernameField, put(1,2,GridBagConstraints.HORIZONTAL));

        add(passwordLabel, put(0,3,GridBagConstraints.HORIZONTAL));

        add(passwordField, put(1,3,GridBagConstraints.HORIZONTAL));

        GridBagConstraints c = put(0,4, GridBagConstraints.BOTH);
        c.insets = new Insets(10,0,10,0);
        add(confirm, c);

        c = put(0,5,GridBagConstraints.HORIZONTAL);
        c.gridwidth = 2;
        c.gridheight = 2;
        add(messageAreaPane, c);
        add(connectionMenu, put(0,7,GridBagConstraints.BOTH));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "comboBoxChanged":
                if (connectionMenu.getSelectedItem() == null)
                    break;
                if (connectionMenu.getSelectedItem().equals("Connect"))
                    connect = true;
                if (connectionMenu.getSelectedItem().equals("Reconnect"))
                    connect = false;
                break;

            case "CONFERMA":
                String username = usernameField.getText();
                String password = passwordField.getText();

                appendToPane(messageArea, "Sending login request", Color.blue);
                if (connect)
                    cliController.login(username, password, "yellow");
                else
                    cliController.login(username, password);
                break;

            default:
                System.out.println(e.getActionCommand());
                break;
        }
    }


     void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor) {
        if (result) {
            appendToPane(messageArea, "Registration completed", Color.green);
        } else {
            appendToPane(messageArea, "Registration failed", Color.red);
            if (invalidUsername) appendToPane(messageArea, "Invalid username", Color.red);
            if (invalidColor) appendToPane(messageArea, "Invalid color", Color.red);
        }
    }


    private static void createAndShowGUI() {createAndShowGUI(null,null);}
    private static void createAndShowGUI(GuiController guiManager1, ClientControllerClientInterface controller) {

        cliController = controller;
        guiManager = guiManager1;

        JFrame frame = new JFrame("Adrenaline - Login");
        frame.addWindowListener(new WindowEventHandler());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,500));


        Login login = new Login();
        frame.add(login);

        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void run(GuiController guiManager, ClientControllerClientInterface controller) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(guiManager, controller);
            }
        });
    }

    static class WindowEventHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent evt) {
            System.out.println("exit");
            cliController.quit();
        }
    }

}
