import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class Menu extends JFrame implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JButton newGameButton, highScoresButton, exitButton;
    private JLabel titleLabel;
    private JButton  cancelButton;
    private JButton defaultMapButton;
    private JButton customMapButton;

    private ImageIcon pcLOGO = new ImageIcon("src/PC.png");
    public Menu() {
        frame = new JFrame();
        panel = new JPanel(new GridLayout(4, 1));
        newGameButton = new JButton("New Game");
        highScoresButton = new JButton("High Scores");
        exitButton = new JButton("Quit");
        titleLabel = new JLabel();
        titleLabel.setIcon(pcLOGO);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        titleLabel.setBackground(Color.BLACK);



        newGameButton.addActionListener(this);
        highScoresButton.addActionListener(this);
        exitButton.addActionListener(this);


        newGameButton.setForeground(Color.YELLOW);
        newGameButton.setBackground(Color.BLACK);
        newGameButton.setFont(new Font("Arial",Font.BOLD,24));
        highScoresButton.setForeground(Color.YELLOW);
        highScoresButton.setBackground(Color.BLACK);
        highScoresButton.setFont(new Font("Arial",Font.BOLD,24));
        exitButton.setForeground(Color.YELLOW);
        exitButton.setBackground(Color.BLACK);
        exitButton.setFont(new Font("Arial",Font.BOLD,24));


        panel.add(titleLabel);
        panel.add(newGameButton);
        panel.add(highScoresButton);
        panel.add(exitButton);
        panel.setBackground(Color.BLACK);


        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
                        frame.dispose();
                        Menu menu = new Menu();
                    }
                }
                return false;
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGameButton) {
            cancelButton = new JButton("Cancel");
            defaultMapButton = new JButton("Basic Map");
            customMapButton = new JButton("Custom Map");

            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Menu menu = new Menu();
                    frame.dispose();
                }
            });

            defaultMapButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    Plansza plansza = new Plansza();
                }
            });

            customMapButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JOptionPane.showMessageDialog(null, "Available soon... ");


                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(3,1));
            buttonPanel.add(defaultMapButton);
            buttonPanel.add(customMapButton);
            buttonPanel.add(cancelButton);

            defaultMapButton.setForeground(Color.YELLOW);
            defaultMapButton.setBackground(Color.BLACK);
            defaultMapButton.setFont(new Font("Arial",Font.BOLD,24));
            customMapButton.setForeground(Color.YELLOW);
            customMapButton.setBackground(Color.BLACK);
            customMapButton.setFont(new Font("Arial",Font.BOLD,24));
            cancelButton.setForeground(Color.YELLOW);
            cancelButton.setBackground(Color.BLACK);
            cancelButton.setFont(new Font("Arial",Font.BOLD,24));

            frame.setLocationRelativeTo(null);
            frame.setSize(500,500);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(buttonPanel);



        } else if (e.getSource() == highScoresButton) {
            JFrame jFrame = new JFrame();
            jFrame.setTitle("High Scores");
            jFrame.setSize(400, 400);
            jFrame.setResizable(false);
            jFrame.setLocationRelativeTo(null);
            JList<String> jList = new JList<>();
            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            jList.setModel(defaultListModel);
            jList.setBackground(Color.BLACK);
            jList.setForeground(Color.YELLOW);
            jList.setFont(new Font("Arial",Font.BOLD,15));
            jFrame.add(new JScrollPane(jList));
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("ranking.txt"))) {
                ArrayList<PacMan> arrayList = (ArrayList<PacMan>) inputStream.readObject();
                arrayList.sort(new Comparator<PacMan>() {
                    @Override
                    public int compare(PacMan o1, PacMan o2) {
                        return Integer.compare(o2.getScore(), o1.getScore());
                    }
                });
                for (int i=0; i<arrayList.size(); i++){
                    defaultListModel.addElement(String.valueOf(arrayList.get(i)));
                }
            } catch (ClassNotFoundException | IOException ex) {
                if (!(ex instanceof EOFException)) ex.printStackTrace();
            }
            jFrame.setVisible(true);
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }



}


