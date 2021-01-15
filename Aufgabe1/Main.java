import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static String input;
    public static String key;
    public static String output = "";

    public static ArrayList<Word> wordlist = new ArrayList<Word>();
    public static ArrayList<String> keylist = new ArrayList<String>();

    public static void main(String[] args) {
        JFileChooser dateiauswahldialog = new JFileChooser(System.getProperty("user.dir"));
        int ergebnis = dateiauswahldialog.showOpenDialog(null);

        if (ergebnis != JFileChooser.APPROVE_OPTION) {
            return; // abgebrochen
        }
        File selektierteDatei = dateiauswahldialog.getSelectedFile();
        try {
            FileReader fr = new FileReader(selektierteDatei);
            BufferedReader br = new BufferedReader(fr);
            input = br.readLine();
            key = br.readLine();
            br.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (java.lang.NumberFormatException ex) {
            System.out.println("Formatfehler in der Datei");
            JOptionPane.showMessageDialog(null, "Datei hat das falsche Format", "Warnung", JOptionPane.ERROR_MESSAGE);

        }


        String[] temp = input.split("\\s");
        for (String s : temp) {
            wordlist.add(new Word(s));
        }
        Collections.addAll(keylist, key.split("\\s"));

        while (keylist.size() > 0) {
            for (Word w : wordlist) {
                if (!w.getSolved()) {
                    w.solveWort();
                    if (w.getSolved()) {
                        keylist.remove(w.getKey());
                    }
                    System.out.println(w.getInput() + " | " + w.getOutput());
                }
            }
        }

        for (Word w : wordlist) {
            output += w.getOutput() + " ";
        }
        System.out.println(output);

    }
}
