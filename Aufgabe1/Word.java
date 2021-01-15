import java.util.ArrayList;

public class Word {

    private String input;
    private String output;
    private String key;
    private String satzzeichen;
    private int letterIndex;

    private boolean solved;

    public Word(String pInput) {
        this.input = pInput;
        this.output = "";
        this.satzzeichen = "";
        this.letterIndex = -1;

        this.solved = false;

        checkSatzzeichen();
        checkLetterIndex();
    }

    public void checkSatzzeichen() {
        if (Character.toString(input.charAt(input.length() - 1)).matches("[,|;|!|.|?]")) {
            this.satzzeichen = String.valueOf(input.charAt(input.length() - 1));
            input = input.substring(0, input.length() - 1);
        }
    }

    public void checkLetterIndex() {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != '_') {
                this.letterIndex = i;
            }
        }
    }

    private ArrayList<String> filterKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        keys.addAll(Main.keylist);

        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            // CHECK: Passt die Länge?
            if (k.length() != input.length()) {
                keys.remove(i);
                i--;
            } else {
                // CHECK: Falls der Input einen Buchstaben enthält, stimmt dieser von der Position her überein?
                if (letterIndex != -1) {
                    if (k.charAt(letterIndex) != input.charAt(letterIndex)) {
                        keys.remove(i);
                        i--;
                    }
                }
            }
        }
        // CHECK: Kommt das Wort mehrfach vor?
        if (keys.size() > 1) {
            for (int j = 0; j < keys.size(); j++) {
                String checkWord = keys.get(j);
                int wordCount = 1;
                for (int j2 = j + 1; j2 < keys.size(); j2++) {
                    if (keys.get(j2).equals(checkWord)) {
                        keys.remove(j2);
                        j2--;
                    }
                }
            }
        }
        return keys;
    }

    public void solveWort() {
        ArrayList<String> keys = filterKeys();
        if (keys.size() > 1) {
            System.out.println("Warnung: Noch nicht eindeutig");
            return;
        }
        if (keys.size() < 1) {
            System.out.println("FEHLER: Kein passendes Wort gefunden");
            return;
        }
        this.key = keys.get(0);
        this.output = keys.get(0) + satzzeichen;
        this.solved = true;
    }


    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getKey() {
        return key;
    }

    public boolean getSolved() {
        return solved;
    }

}
