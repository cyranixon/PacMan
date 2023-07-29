import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PacMan implements Serializable {


    private  int score =0;

    private  String nick;

    private static ArrayList<PacMan> pacmans = new ArrayList<>();

    public  PacMan(){
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("ranking.txt"))) {
            while (true) {
                pacmans = (ArrayList<PacMan>) inputStream.readObject();
            }
        } catch (ClassNotFoundException | IOException ex) {
            if (!(ex instanceof EOFException)) ex.printStackTrace();
        }
    }

    public static void addPacmans(PacMan pacMan){
        pacmans.add(pacMan);
    }

    public static ArrayList<PacMan> getPacmans() {
        return pacmans;
    }

    public int getScore() {
        return score;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public void setScore() {
        this.score++;
    }

    @Override
    public String toString() {
        return nick + " " + score;
    }
}
