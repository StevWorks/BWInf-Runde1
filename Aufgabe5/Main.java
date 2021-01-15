import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static int[][] wichteln;  // w1, w2, w3, Verteilung/Wahl, Priortät dieser Wahl -> Schüler in dem Array mit entsprechenden Wünschen
    public static ArrayList<Integer> vergeben = new ArrayList<Integer>();

    public static void main(String[] args) {
        initFileAndArray();

        for (int i = 0; i < wichteln.length; i++) {
            System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2]);
        }
        System.out.println("----------------------");

        earlyCheck();

        /*
        Solange 1. Check true zurückgibt (Solange Schülern ihr 1. Wunsch erfüllt werden kann), führe weiterhin den 1. Check aus

        Ist dies nicht mehr möglich...:
        Wenn der 2. Check true zurückgibt (Wenn einem Schüler sein 2. Wunsch erfüllt werden kann), beginne wieder von Vorne

        Ist dies nicht möglich...:
        Wenn der 3. Check true zurückgibt (Wenn einem Schüler sein 3. Wunsch erfüllt werden kann), beginne wieder von Vorne

        Ist dies nicht möglich...:
        Überprüfe einen letzten "Notfall"-Check, welcher dazu da ist, noch etwas zu verteilen, wenn irgendwie möglich. Wenn dies möglich ist, beginne wieder von Vorne

        Ist dies nicht möglich...:
        Beende das Programm (setze active auf false)
        */
        boolean active = true;
        while (active) {
            while (checkOnlyFirstPriority()) {
            }
            if (checkOnlySecondPriority()) {
                continue;
            } else {
                if (checkOnlyThirdPriority()) {
                    continue;
                } else {
                    if (finalNotfallCheck()) {
                        continue;
                    } else {
                        active = false;
                    }
                }
            }
        }

        System.out.println("----------------------");
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][4] != 0) {
                System.out.println("Der Schüler " + (i + 1) + " erhält ein Geschenk von Schüler " + wichteln[i][3] + " - Das war sein " + wichteln[i][4] + "er Wunsch");
            } else {
                System.out.println("Der Schüler " + (i + 1) + " konnte leider nicht zugeteilt werden, da alle seine Wünsche besetzt sind.");
            }
        }
    }


    public static void earlyCheck() { // 0. Check: Ermittle direkte Möglichkeiten für 1. Proirität (Wunsch) - Vorangestellter einzelner Check
        for (int i = 0; i < wichteln.length; i++) {
            int anzahlVorhanden = 0;
            int w1 = wichteln[i][0]; // Wunsch des aktuellen Schülers
            for (int j = 0; j < wichteln.length; j++) { // Mit allen anderen Schülern abgleichen
                if (wichteln[j][0] == w1) { // Gleicher Wunsch mehrmals vorhanden?
                    anzahlVorhanden++;
                }
            }
            if (anzahlVorhanden == 1) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // 0. Check - 1. Wunsch nur einmal vorhanden -> Zuweisen");
                wichteln[i][3] = w1; // Person verteilt - 1. Proirität konnte erfüllt werden - Wunsch festgelegt
                wichteln[i][4] = 1; // Person verteilt - 1. Proirität konnte erfüllt werden - Priorität dieses Wunsches festgelegt
                updateVergeben(w1); // Entsprechender Wunsch ist für alle anderen nun vergeben
            }
        }
    }

    public static boolean checkOnlyFirstPriority() { // 1. Check: Ermittle Schüler, welchen nur noch der erste Wunsch möglich ist und verteile diese
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][3] != 0) { // Prüfung, ob der Schüler, bereits einen Wunsch zugewiesen bekommen hat
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][1] == 0 && wichteln[i][2] == 0) { // Prüfung, ob der Schüler gar keine Möglichkeit mehr hat, sprich, ob alle Wünsche bereits belegt sind
                wichteln[i][3] = -1; // -1 = Keiner seiner Wünsche konnte verteilt werden... Der Schüler bleibt übrig
                continue;
            }

            if (wichteln[i][1] == 0 && wichteln[i][2] == 0) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // 1. Check - Erste Priorität als einzige Möglichkeit? -> Zuweisen");
                wichteln[i][3] = wichteln[i][0]; // Person verteilt - 1. Proirität konnte erfüllt werden - Wunsch festgelegt
                wichteln[i][4] = 1; // Person verteilt - 1. Proirität konnte erfüllt werden - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][0]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
        }
        return false;
    }

    public static boolean checkOnlySecondPriority() { // 2. Check: Ermittle Schüler, welchen nur noch der zweite Wunsch möglich ist und verteile diese
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][3] != 0) { // Prüfung, ob der Schüler, bereits einen Wunsch zugewiesen bekommen hat
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][1] == 0 && wichteln[i][2] == 0) { // Prüfung, ob der Schüler gar keine Möglichkeit mehr hat, sprich, ob alle Wünsche bereits belegt sind
                wichteln[i][3] = -1; // -1 = Keiner seiner Wünsche konnte verteilt werden... Der Schüler bleibt übrig
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][2] == 0) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // 2. Check - Zweite Priorität als einzige Möglichkeit? -> Zuweisen");
                wichteln[i][3] = wichteln[i][1]; // Person verteilt - 2. Proirität konnte erfüllt werden - Wunsch festgelegt
                wichteln[i][4] = 2; // Person verteilt - 2. Proirität konnte erfüllt werden - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][1]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
        }
        return false;
    }

    public static boolean checkOnlyThirdPriority() { // 3. Check: Ermittle Schüler, welchen nur noch der dritte Wunsch möglich ist und verteile diese
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][3] != 0) { // Prüfung, ob der Schüler, bereits einen Wunsch zugewiesen bekommen hat
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][1] == 0 && wichteln[i][2] == 0) { // Prüfung, ob der Schüler gar keine Möglichkeit mehr hat, sprich, ob alle Wünsche bereits belegt sind
                wichteln[i][3] = -1; // -1 = Keiner seiner Wünsche konnte verteilt werden... Der Schüler bleibt übrig
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][1] == 0) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // 3. Check - Dritte Priorität als einzige Möglichkeit? -> Zuweisen");
                wichteln[i][3] = wichteln[i][2]; // Person verteilt - 3. Proirität konnte erfüllt werden - Wunsch festgelegt
                wichteln[i][4] = 3; // Person verteilt - 3. Proirität konnte erfüllt werden - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][2]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
        }
        return false;
    }

    public static boolean finalNotfallCheck() { // "Notfall"-Check: Ermittle, ob noch irgendwelche Verteilungen möglich sind und teile entsprechende Wünsche zu
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][3] != 0) { // Prüfung, ob der Schüler, bereits einen Wunsch zugewiesen bekommen hat
                continue;
            }
            if (wichteln[i][0] == 0 && wichteln[i][1] == 0 && wichteln[i][2] == 0) { // Prüfung, ob der Schüler gar keine Möglichkeit mehr hat, sprich, ob alle Wünsche bereits belegt sind
                wichteln[i][3] = -1; // -1 = Keiner seiner Wünsche konnte verteilt werden... Der Schüler bleibt übrig
                continue;
            }
            if (!vergeben.contains(wichteln[i][0])) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // Notfall-Check - Erste Priorität wird 'zufällig' verteilt -> Zuweisen");
                wichteln[i][3] = wichteln[i][0]; // Person verteilt - 1. Proirität wurde zugeteilt - Wunsch festgelegt
                wichteln[i][4] = 1; // Person verteilt - 1. Proirität wurde zugeteilt - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][0]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
            if (!vergeben.contains(wichteln[i][1])) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // Notfall-Check - Zweite Priorität wird 'zufällig' verteilt -> Zuweisen");
                wichteln[i][3] = wichteln[i][1]; // Person verteilt - 2. Proirität wurde zugeteilt - Wunsch festgelegt
                wichteln[i][4] = 2; // Person verteilt - 2. Proirität wurde zugeteilt - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][1]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
            if (!vergeben.contains(wichteln[i][2])) {
                System.out.println((i + 1) + ": " + wichteln[i][0] + " | " + wichteln[i][1] + " | " + wichteln[i][2] + " // Notfall-Check - Dritte Priorität wird 'zufällig' verteilt -> Zuweisen");
                wichteln[i][3] = wichteln[i][2]; // Person verteilt - 3. Proirität wurde zugeteilt - Wunsch festgelegt
                wichteln[i][4] = 3; // Person verteilt - 3. Proirität wurde zugeteilt - Priorität dieses Wunsches festgelegt
                updateVergeben(wichteln[i][2]); // Entsprechender Wunsch ist für alle anderen nun vergeben
                return true;
            }
        }
        return false;
    }


    public static void updateVergeben(int toAdd) { // Aktualisiere die Liste aller Schüler entsprechend der bereits vergebenen Wünsche
        vergeben.add(toAdd);
        for (int i = 0; i < wichteln.length; i++) {
            if (wichteln[i][3] != 0) { // Prüfung, ob der Schüler, bereits einen Wunsch zugewiesen bekommen hat
                continue;
            }
            for (int v = 0; v < vergeben.size(); v++) { // Gehe alle vergebenen Wünsche durch
                if (wichteln[i][0] == vergeben.get(v)) { // Ist der Wunsch bereits vergeben?
                    wichteln[i][0] = 0; // 0 = Wunsch nicht mehr verfügbar
                } else if (wichteln[i][1] == vergeben.get(v)) { // Ist der Wunsch bereits vergeben?
                    wichteln[i][1] = 0; // 0 = Wunsch nicht mehr verfügbar
                } else if (wichteln[i][2] == vergeben.get(v)) { // Ist der Wunsch bereits vergeben?
                    wichteln[i][2] = 0; // 0 = Wunsch nicht mehr verfügbar
                }
            }
        }
    }



    public static void initFileAndArray() {
        JFileChooser dateiauswahldialog = new JFileChooser(System.getProperty("user.dir"));
        int ergebnis = dateiauswahldialog.showOpenDialog(null);

        if (ergebnis != JFileChooser.APPROVE_OPTION) {
            return; // abgebrochen
        }
        File selektierteDatei = dateiauswahldialog.getSelectedFile();
        try {
            FileReader fr = new FileReader(selektierteDatei);
            BufferedReader br = new BufferedReader(fr);

            int size = Integer.valueOf(br.readLine());
            wichteln = new int[size][5]; // w1, w2, w3, Verteilung/Wahl, Priortät dieser Wahl

            for (int i = 0; i < size; i++) { // i = Schüler
                String[] wInput = br.readLine().replaceAll("^\\s*\\s|\\s*\\s$", "").split("(\\s*\\s)"); // Entfernen der Leerzeichen am Anfang/Ende & split in Array
                wichteln[i][0] = Integer.valueOf(wInput[0]); // 1. Wunsch -> w1
                wichteln[i][1] = Integer.valueOf(wInput[1]); // 2. Wunsch -> w2
                wichteln[i][2] = Integer.valueOf(wInput[2]); // 3. Wunsch -> w3
            }
            br.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (java.lang.NumberFormatException ex) {
            System.out.println("Formatfehler in der Datei");
            JOptionPane.showMessageDialog(null, "Datei hat das falsche Format", "Warnung", JOptionPane.ERROR_MESSAGE);

        }
    }
}
