package view.gui;

import controller.controllerclient.ClientControllerClientInterface;
import network.Player;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class Login extends JFrame {

    private static ClientControllerClientInterface cliController;
    private Framework controller;

    private JLabel titleLabel;
    private JLabel subTitleLabel;

    private JComboBox connectionMenu;

    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JTextField passwordField;
    private JComboBox colorMenu;
    private JButton confirm;

    private JLabel messageLabel;
    private JScrollPane messageAreaPane;
    private JTextPane messageArea;

    private boolean connect = true;
    private String color;


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

        /*int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);*/

        try {
            StyledDocument doc = tp.getStyledDocument();
            doc.insertString(doc.getLength(), msg, aset);
        }
        catch (BadLocationException e) {
            System.out.println(e.offsetRequested());
        }
    }


    Login(Framework controller) {
        super("Adrenaline - Login");

        setLayout(new GridBagLayout());
        //super(new GridBagLayout());

        this.controller = controller;
        cliController = controller.getClientController();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(500,500);

        titleLabel = new JLabel("ADRENALINE");
        titleLabel.setFont(new Font(titleLabel.getName(), Font.BOLD, 20));
        subTitleLabel = new JLabel("login");
        subTitleLabel.setFont(new Font(subTitleLabel.getName(), Font.PLAIN, 15));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(10);
        passwordLabel = new JLabel("Password:");
        passwordField = new JTextField(10);

        colorMenu = new JComboBox(List.of("Blue", "Pink", "Green", "Yellow", "Gray").toArray());
        color = "Blue";
        colorMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = (String)colorMenu.getSelectedItem();
                System.out.println(color);
            }
        });



        messageArea = new JTextPane();
        messageArea.setEditable(false);
        messageArea.setSize(new Dimension(2,10));
        messageAreaPane = new JScrollPane(messageArea);
        messageAreaPane.setSize(2,10);


        connectionMenu = new JComboBox(List.of("Connect", "Reconnect").toArray());
        connectionMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connectionMenu.getSelectedItem() == null)
                    return;
                if (connectionMenu.getSelectedItem().equals("Connect"))
                    connect = true;
                if (connectionMenu.getSelectedItem().equals("Reconnect"))
                    connect = false;
            }
        });


        confirm = new JButton("CONFERMA");
        confirm.setMnemonic(KeyEvent.VK_ENTER);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                appendToPane(messageArea, "Sending login request", Color.blue);
                if (connect)
                    cliController.login(username, password, color);
                else
                    cliController.login(username, password);
            }
        });


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

        add(colorMenu, put(0,8,GridBagConstraints.HORIZONTAL));
    }


     void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor) {
        if (result) {
            appendToPane(messageArea, "Registration completed", Color.green);
        } else {
            appendToPane(messageArea, "Registration failed", Color.red);
            if (invalidUsername) appendToPane(messageArea, "Invalid username", Color.red);
            if (invalidColor) appendToPane(messageArea, "Invalid colorMenu", Color.red);
        }
    }

    void onConnection(Player player, boolean connected) {
        appendToPane(messageArea, String.format("%s %s", player.getUsername(), connected ? "connected" : "disconnected"), Color.black);
    }
}
