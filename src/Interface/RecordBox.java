import javax.swing.*;
import java.awt.*;

public class RecordBox extends JFrame {
    private Category category;
    private Point pos;

    private JPanel panel;
    private JTextArea textArea;

    public RecordBox() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new Panel();
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        //panel.add(textArea);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(250,100));
        this.add(textArea, BorderLayout.CENTER);
        this.setResizable(false);
        this.pack();
        //this.setVisible(true);
    }

    public void setCategory(Category category) {
        this.category = category;
        changeText();
    }

    private void changeText() {
        Run[] WR_Runs = new Run[4];
        String[] WR_Texts = new String[4];
        String finalText = "";

        for(int i = 0; i < 4; i++) {
            WR_Runs[i] = category.getWR(i);
            //System.out.println("WR: " + category.getWR(i));

            if(WR_Runs[i] != null) {
                int time[] = WR_Runs[i].getTime();
                String rta = time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms";
                String igt = time[0] + "y / " + time[1] + "m / " + time[2] + "d";

                switch(i) {
                    case 0:
                        WR_Texts[i] = "RTA NS5: " + rta;
                        //System.out.println("RTA NS5: " + rta);
                        break;
                    case 1:
                        WR_Texts[i] = "RTA WS5: " + rta;
                        //System.out.println("RTA WS5: " + rta);
                        break;
                    case 2:
                        WR_Texts[i] = "IGT NSS: " + igt;
                        //System.out.println("IGT NSS: " + igt);
                        break;
                    case 3:
                        WR_Texts[i] = "IGT WSS: " + igt;
                        //System.out.println("IGT WSS: " + igt);
                }
                WR_Texts[i] += "\n" + WR_Runs[i].getRunner().getName();
                finalText += WR_Texts[i] + "\n";
            }
        }
        //System.out.println("Final Text: " + finalText);
        textArea.setText(finalText);
        this.pack();
    }

}
