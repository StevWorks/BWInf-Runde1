import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Turnier {

	File selektierteDatei;
	Spieler[] spielerListe;


	private Turnier() {
		einlesen();
		int anzahlSpieler = spielerAnzahlErmitteln();
		spielerListe = new Spieler[anzahlSpieler];
		spielerStaerkenBefuellen();
	}

	public void einlesen() {
		JFileChooser dateiauswahldialog = new JFileChooser(System.getProperty("user.dir"));
		int ergebnis = dateiauswahldialog.showOpenDialog(null);

		if (ergebnis != JFileChooser.APPROVE_OPTION) {
			return; // abgebrochen
		}
		File f = dateiauswahldialog.getSelectedFile();
		selektierteDatei = f;
	}

	public int spielerAnzahlErmitteln() {
		try {
			FileReader fr = new FileReader(selektierteDatei);
			BufferedReader br = new BufferedReader(fr);

			String zeile = br.readLine();
			br.close();
			return Integer.parseInt(zeile);

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} catch (java.lang.NumberFormatException ex) {
			System.out.println("Formatfehler in der Datei");
			JOptionPane.showMessageDialog(null, "Datei hat das falsche Format", "Warnung", JOptionPane.ERROR_MESSAGE);

		}
		return 0;

	}

	public void spielerStaerkenBefuellen() {

		try {
			FileReader fr = new FileReader(selektierteDatei);
			BufferedReader br = new BufferedReader(fr);

			String zeile = br.readLine();

			zeile = br.readLine();
			int i = 0;
			while (zeile != null) {
				spielerListe[i] = new Spieler(i + 1, Integer.parseInt(zeile));
				i++;
				zeile = br.readLine();
			}
			br.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} catch (java.lang.NumberFormatException ex) {
			System.out.println("Formatfehler in der Datei");
			JOptionPane.showMessageDialog(null, "Datei hat das falsche Format", "Warnung", JOptionPane.ERROR_MESSAGE);

		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	/*************************************************************************************************/
	/*************************************************************************************************/

	public void bef�lleListeZufaellig() {
		for (int i = 0; i < spielerListe.length; i++) {
			Spieler temp = new Spieler(i + 1);
			temp.setScore((int) (Math.random() * 100));
			spielerListe[i] = temp;
		}
	}

	public void resetSiege() {
		for (int i = 0; i < spielerListe.length; i++) {
			spielerListe[i].setSiege(0);
		}
	}

	public Spieler meistenSiege() {
		Spieler s = spielerListe[0];
		for (int i = 1; i < spielerListe.length; i++) {
			if (spielerListe[i].getSiege() > s.getSiege()) {
				s = spielerListe[i];
			}
			if (spielerListe[i].getSiege() == s.getSiege()) {
				if (spielerListe[i].getSpielernummer() < s.getSpielernummer()) {
					s = spielerListe[i];
				}
			}
		}
		return s;
	}

	public Spieler besterSpieler() {
		Spieler besterSpieler = spielerListe[0];
		for (int i = 1; i < spielerListe.length; i++) {
			if (spielerListe[i].getScore() > besterSpieler.getScore())
				besterSpieler = spielerListe[i];
		}
		return besterSpieler;
	}

	/************/
	/************/
	/************/

	public Spieler spiele(Spieler spieler1, Spieler spieler2) {
		if (!(spieler1.getScore() == 0 && spieler2.getScore() == 0)) {
			double x = spieler1.getScore() + spieler2.getScore();
			double y = spieler1.getScore() / x;
			double r = Math.random();
			if (r < y) {
				spieler1.erhoeheSiege();
				return spieler1;
			} else {
				spieler2.erhoeheSiege();
				return spieler2;
			}
		} else
			return null;
	}

	public Spieler spiele5mal(Spieler spieler1, Spieler spieler2) {
		double x = spieler1.getScore() + spieler2.getScore();
		double y = spieler1.getScore() / x;
		int zwischenstandSpieler1 = 0;
		int zwischenstandSpieler2 = 0;
		for (int i = 0; i < 5; i++) {
			double r = Math.random();
			if (r < y) {
				zwischenstandSpieler1++;
			} else {
				zwischenstandSpieler2++;
			}
		}

		if (zwischenstandSpieler1 < zwischenstandSpieler2) {
			spieler2.erhoeheSiege();
			return spieler2;

		} else {
			spieler1.erhoeheSiege();
			return spieler1;
		}
	}

	/************/
	/************/
	/************/

	public Spieler ligaTurnier() {
		int b = 1;
		for (int i = 0; i < spielerListe.length - 1; i++) {
			for (int j = b; j < spielerListe.length; j++) {
				spiele(spielerListe[i], spielerListe[j]);
			}
			b++;
		}
		return meistenSiege();
	}

	public Spieler spieleKOTurnier(int anfang, int ende) {
		if (ende - anfang == 1) // Abbruchbedingung
			return spiele(spielerListe[anfang], spielerListe[ende]);
		return spiele(spieleKOTurnier(anfang, (int) (Math.floor((anfang + ende) / 2))),
				spieleKOTurnier((int) (Math.ceil((anfang + ende) / 2)), ende));
	}

	public Spieler spieleKO5Turnier(int anfang, int ende) {
		if (ende - anfang == 1)
			return spiele5mal(spielerListe[anfang], spielerListe[ende]);
		return spiele5mal(spieleKO5Turnier(anfang, (int) Math.floor((anfang + ende) / 2)),
				spieleKO5Turnier((int) Math.ceil((anfang + ende) / 2), ende));
	}

	/******************************************************************/
	/******************************************************************/
	/******************************************************************/
	/******************************************************************/
	/******************************************************************/

	public void variantenTesten(int w) {
		Spieler besterSpieler = besterSpieler();
		int wiederholungen = w;

		double anzahlLigaSiege = 0;
		for (int i = 0; i < wiederholungen; i++) {
			if (ligaTurnier() == besterSpieler)
				anzahlLigaSiege++;
		}

		double anzahlKOSiege = 0;
		for (int i = 0; i < wiederholungen; i++) {
			if (spieleKOTurnier(0, spielerListe.length - 1) == besterSpieler)
				anzahlKOSiege++;
		}

		double anzahlKO5Siege = 0;
		for (int i = 0; i < wiederholungen; i++) {
			if (spieleKO5Turnier(0, spielerListe.length - 1) == besterSpieler)
				anzahlKO5Siege++;
		}

		System.out.println("Siege im Liga-System : " + anzahlLigaSiege + " bei " + wiederholungen + " Wiederholungen");
		System.out.println("Siege im KO-System : " + anzahlKOSiege + " bei " + wiederholungen + " Wiederholungen");
		System.out.println("Siege im KO5-System : " + anzahlKO5Siege + " bei " + wiederholungen + " Wiederholungen");
		
		System.out.println();
		
		double durchschnittLigaSiege = (anzahlLigaSiege / wiederholungen);
		double durchschnittKOSiege = (anzahlKOSiege / wiederholungen);
		double durchschnittKO5Siege = (anzahlKO5Siege / wiederholungen);

		System.out.println("Durchschnitt Liga: " + durchschnittLigaSiege);
		System.out.println("Durchschnitt KO: " + durchschnittKOSiege);
		System.out.println("Durchschnitt KO5: " + durchschnittKO5Siege);
		
		double x = Math.max(durchschnittLigaSiege, durchschnittKOSiege);
		double max = Math.max(x, durchschnittKO5Siege);
		
		if (max == durchschnittLigaSiege)
			System.out.println("Das Liga-System wird empfohlen!");
		if (max == durchschnittKOSiege)
			System.out.println("Das KO-System wird empfohlen!");
		if (max == durchschnittKO5Siege)
			System.out.println("Das KO5-System wird empfohlen!");

	}

	public static void main(String[] args) {
		Turnier t = new Turnier();
		t.variantenTesten(5000);
		
		/*
		System.out.println();
		System.out.println("Spielerstaerken: ");
		for (int i = 0; i < t.spielerListe.length; i++) {
			System.out.println(t.spielerListe[i].toString() + " " + t.spielerListe[i].getScore());
		}
		*/
	}
}
