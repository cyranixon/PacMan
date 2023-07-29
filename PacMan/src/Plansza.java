import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class  Plansza extends JFrame implements Serializable {


    private JTable table;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel livesLabel;
    private int row;
    private int col;
    private int time;
    private int lives;
    private MyTableModel model;

    private  ImageIcon pacManIcon = new ImageIcon("src/up.jpg");

    private PacMan pacMan = new PacMan();

    private int maxPoints =100;

    private ImageIcon ghostIcon = new ImageIcon("src/ghost.jpg");

    private Ghost[]ghosts;

    public Plansza() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Labirynt");

        time = 0;
        lives = 5;

        scoreLabel = new JLabel();
        timeLabel = new JLabel("Time: 0");
        livesLabel = new JLabel("Lives: 5");


        livesLabel.setFont(new Font("Arial",Font.BOLD,24));
        livesLabel.setForeground(Color.YELLOW);
        timeLabel.setFont(new Font("Arial",Font.BOLD,24));
        timeLabel.setForeground(Color.YELLOW);
        scoreLabel.setFont(new Font("Arial",Font.BOLD,24));
        scoreLabel.setForeground(Color.YELLOW);


        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(scoreLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(livesLabel);


        setLayout(new BorderLayout());
        add(infoPanel,BorderLayout.SOUTH);

        // utworzenie planszy
        Object[][] elements = {
                {"X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", "1", "1", "1", "X", "1","1", "1", "1", "X", "1", "1", "1", "1" , "X"},
                {"X", "X", "X", "1", "X", "1", "X", "X", "1", "X", "1", "X", "X", "1", "X"},
                {"X", "1", "1", "1", "1", "1", "1", "1", "1", "X", "1","1", "1", "1" , "X"},
                {"X", "X", "1", "X", "X", "X", "X", "1", "X", "X", "X", "X", "X", "1", "X"},
                {"X", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "X"},
                {"X", "1", "X", "X", "1", "X", "X", "X", "1", "X", "X", "X", "1", "1", "X"},
                {"X", "1", "1", "1", "1", "1", "1", "1", "1", "X", "1", "1", "1", "1", "X"},
                {"X", "X", "1", "X", "X", "X", "X", "X", "X", "X", "1", "X", "X", "1", "X"},
                {"X", "X", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "X", "1", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"}
        };

        Random random = new Random();


        // utworzenie modelu tabeli
        model = new MyTableModel(elements, new String[elements[0].length]);
        row = random.nextInt(elements.length);;
        col = random.nextInt(elements[0].length);
        while (model.getValueAt(row,col).equals("X")){

            row = random.nextInt(elements.length);;
            col = random.nextInt(elements[0].length);
        }
        model.setValueAt(pacMan,row,col);

        table = new JTable(model);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value.equals("X")){
                    component.setBackground(Color.BLUE);
                    component.setForeground(Color.BLUE);
                    super.setIcon(null);

                }else if(value.equals("1")){
                    component.setBackground(Color.BLACK);
                    JLabel label = (JLabel)component;
                    label.setText("O");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setVerticalAlignment(JLabel.CENTER);
                    label.setForeground(Color.YELLOW);
                    super.setIcon(null);
                } else if(value.equals(" ")){
                    component.setBackground(Color.BLACK);
                    super.setIcon(null);
                }else if (value.equals(pacMan)){
                    component.setBackground(Color.BLACK);
                    Image image = pacManIcon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
                    super.setIcon(new ImageIcon(image));
                    component.setForeground(Color.BLACK);
                }else if (value instanceof Ghost){
                    component.setBackground(Color.BLACK);
                    Image image = ghostIcon.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
                    super.setIcon(new ImageIcon(image));
                    component.setForeground(Color.BLACK);
                }
                return component;
            }
        };

        table.setDefaultRenderer(Object.class, renderer);

            // konfiguracja tabeli
        table.setRowHeight(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.setDefaultEditor(Object.class, null);// wyłączenie edycji pól

        // dodanie tabeli do okna
        pack();
        getContentPane().add(table);
        setVisible(true);
        setSize(800,600);
        setLocationRelativeTo(null);

        ghosts = new Ghost[3];
        createGhosts();
        startTimer();
        countScore();
        endGame();


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (!model.getValueAt(row-1,col).equals("X")){
                            pacManIcon = new ImageIcon("src/up.jpg");
                            if (model.getValueAt(row-1,col).equals("1")){
                                pacMan.setScore();
                            }
                            model.setValueAt(pacMan,row-1,col);
                            model.setValueAt(" ",row,col);
                            row = row-1;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!model.getValueAt(row+1,col).equals("X")){
                            pacManIcon = new ImageIcon("src/down.jpg");
                            if (model.getValueAt(row+1,col).equals("1")){
                               pacMan.setScore();
                            }
                            model.setValueAt(pacMan,row+1,col);
                            model.setValueAt(" ",row,col);
                            row = row+1;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (!model.getValueAt(row,col-1).equals("X")){
                            pacManIcon = new ImageIcon("src/left.jpg");
                            if (model.getValueAt(row,col-1).equals("1")){
                                pacMan.setScore();

                            }
                            model.setValueAt(pacMan,row,col-1);
                            model.setValueAt(" ",row,col);
                            col = col-1;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!model.getValueAt(row,col+1).equals("X")){
                            pacManIcon = new ImageIcon("src/right.png");
                            if (model.getValueAt(row,col+1).equals("1")){
                                pacMan.setScore();
                            }
                            model.setValueAt(pacMan,row,col+1);
                            model.setValueAt(" ",row,col);
                            col = col+1;
                        }
                        break;
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();

    }
    private void startTimer() {
        Thread timer = new Thread(() -> {
            while (!Thread.interrupted()) {
                if (lives<0||pacMan.getScore()==maxPoints)Thread.currentThread().interrupt();
                try {
                    Thread.sleep(1000); // Oczekaj 1 sekundę
                    time++;
                    timeLabel.setText("Time: " + time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timer.start();
    }
    private void countScore(){
        Thread  thread = new Thread(()-> {
            while (!Thread.interrupted()){
                if (lives<0||pacMan.getScore()==maxPoints)Thread.currentThread().interrupt();

                scoreLabel.setText("Score: "+ pacMan.getScore());

            }
        });
        thread.start();
    }
    private void endGame(){
        Thread thread = new Thread(()->{
            while (!Thread.interrupted()){
                if (lives<=0||pacMan.getScore()==maxPoints){
                    JOptionPane.showMessageDialog(null, "Game Over");
                    dispose();
                    JFrame okno = new JFrame();
                    JButton ok = new JButton("ok");
                    JPanel panel = new JPanel();
                    JLabel podajnick = new JLabel("Podaj nick: ");
                    podajnick.setForeground(Color.YELLOW);
                    podajnick.setFont(new Font("Arial",Font.BOLD,20));
                    JTextField text = new JTextField(20);
                    panel.setLayout(new FlowLayout());
                    panel.add(podajnick);
                    panel.add(text);
                    panel.add(ok);
                    panel.setBackground(Color.BLACK);
                    panel.setFont(new Font("Arial",Font.BOLD,24));
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            pacMan.setNick(text.getText());
                            try (FileOutputStream fileOutputStream = new FileOutputStream(new File("ranking.txt"))) {
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                ArrayList<PacMan> listaPacmanow = PacMan.getPacmans();
                                listaPacmanow.add(pacMan);
                                objectOutputStream.writeObject(listaPacmanow);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            okno.dispose();
                            Menu menu = new Menu();
                        }
                    });

                    okno.add(panel);
                    okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    okno.setSize(400, 100);
                    okno.setVisible(true);
                    okno.setLocationRelativeTo(null);
                    Thread.currentThread().interrupt();
                }
                moveGhosts();
                checkCollisions();
                try {
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    private void moveGhosts() {
        Random random = new Random();
        for (Ghost ghost : ghosts) {
            int ghostRow = ghost.getRow();
            int ghostCol = ghost.getCol();



            if (!model.getValueAt(ghostRow, ghostCol).equals(pacMan)) {
                model.setValueAt("1", ghostRow, ghostCol);
            }

            // randowmowy kierunek
            int direction = random.nextInt(4);

            switch (direction) {
                case 0: // do gory
                    if (!model.getValueAt(ghostRow - 1, ghostCol).equals("X") && !model.getValueAt(ghostRow - 1, ghostCol).equals(pacMan) ) {
                        ghostIcon = new ImageIcon("src/ghost.png");
                        ghostRow--;
                    }
                    break;
                case 1: // w dol
                    if (!model.getValueAt(ghostRow + 1, ghostCol).equals("X") && !model.getValueAt(ghostRow + 1, ghostCol).equals(pacMan) ) {
                        ghostIcon = new ImageIcon("src/ghost.png");
                        ghostRow++;
                    }
                    break;
                case 2: // w lewo
                    if (!model.getValueAt(ghostRow, ghostCol - 1).equals("X") && !model.getValueAt(ghostRow, ghostCol - 1).equals(pacMan) ) {
                        ghostIcon = new ImageIcon("src/ghost.png");
                        ghostCol--;
                    }
                    break;
                case 3: // w prawo
                    if (!model.getValueAt(ghostRow, ghostCol + 1).equals("X") && !model.getValueAt(ghostRow, ghostCol + 1).equals(pacMan)) {
                        ghostIcon = new ImageIcon("src/ghost.png");
                        ghostCol++;
                    }
                    break;
            }

            model.setValueAt(ghost, ghostRow, ghostCol);
            ghost.setRow(ghostRow);
            ghost.setCol(ghostCol);
        }
    }
    private void createGhosts() {
        Random random = new Random();
        for (int i = 0; i < ghosts.length; i++) {
            int ghostRow = random.nextInt(model.getRowCount());
            int ghostCol = random.nextInt(model.getColumnCount());
            while (model.getValueAt(ghostRow, ghostCol).equals("X") || model.getValueAt(ghostRow, ghostCol).equals(pacMan)) {
                ghostRow = random.nextInt(model.getRowCount());
                ghostCol = random.nextInt(model.getColumnCount());
            }
            Ghost ghost = new Ghost(ghostRow, ghostCol);
            ghosts[i] = ghost;
            model.setValueAt(ghost, ghostRow, ghostCol);
        }
    }
    private void checkCollisions() {
        int pacManRow = row;
        int pacManCol = col;

        for (Ghost ghost : ghosts) {
            if (ghost.getRow() == pacManRow && ghost.getCol() == pacManCol) {
                lives--;
                livesLabel.setText("Lives: " + lives);
                movePacManToRandomCell();
                break;
            }
        }
    }
    private void movePacManToRandomCell() {
        Random random = new Random();
        int newRow = random.nextInt(model.getRowCount());
        int newCol = random.nextInt(model.getColumnCount());
        while (model.getValueAt(newRow, newCol).equals("X")) {
            newRow = random.nextInt(model.getRowCount());
            newCol = random.nextInt(model.getColumnCount());
        }
        model.setValueAt(pacMan, newRow, newCol);
        row = newRow;
        col = newCol;
    }

}





