import java.awt.BorderLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Main {

	public static void main(String[] args) {

		Random alea = new Random();

		Fenetre menu = new Fenetre ("Pokemon-GL 2.0 Simulator");
		int imgParLigne = 8;
		menu.miseEnForme(new int[] {1,8,8,8,8,8,8,8});

		String liste = ".txt";
		double seuil = 3.406367108;
		seuil = 0.01;

		menu.addLabel("Quel tier voulez-vous charger ?", 0, 0, false);
		menu.addButton("OU", 1, 0, 0, false);
		menu.addButton("NU", 2, 0, 0, false);
		menu.addButton("Battle Spot", 3, 0, 0, false);
		menu.addButton("Anything Goes", 4, 0, 0, false);
		menu.addButton("Double", 5, 0, 0, false);
		menu.addButton("Mediocremon", 6, 0, 0, false);
		menu.addButton("Touhoumon", 7, 0, 0, false);
		menu.setVisible(true);

		int clic = -1;
		while (clic < 0){
			clic = menu.quelClic();
		}
		if (clic == 1){
			liste = "ou-1825 2015-02.txt";
		}
		if (clic == 2){
			liste = "nu-1760 2014-11.txt";
		}
		if (clic == 3){
			liste = "battlespot-1760 2014-12.txt";
		}
		if (clic == 4){
			liste = "anythinggoes-1760 2015-01.txt";
		}
		if (clic == 5){
			liste = "smogondoubles-1825 2014-12.txt";
		}
		if (clic == 6){
			liste = "mediocremon-1760 2014-07.txt";
		}
		if (clic == 7){
			liste = "Touhoumon.txt";
		}
		menu.resetClics();

		// Importation de la liste des OU
		System.out.println("\nChargement du tier en cours...");
		ArrayList<String> tiers = new ArrayList<String>();
		try{
			BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/" + liste));
			for (int i=0; i<5; ++i){
				liseur.readLine();
			}
			boolean cherche = true;
			while (cherche){
				String ligne = liseur.readLine();
				if (Float.valueOf(ligne.split(Pattern.quote("|"))[3].split("%")[0]).floatValue() > seuil){
					tiers.add(ligne.split(Pattern.quote("|"))[2].split(" ")[1]);
				}
				else{
					cherche = false;
				}
			}
			liseur.close();
			System.out.println("Fichier ferme sans faire de bug");
		}
		catch(FileNotFoundException e){
			menu.removeAll();
			System.out.println("FileNotFoundExcetion");
		}
		catch(Exception e){
			System.out.println("Chargement du tier termine.");
		}
		int tailleTier = tiers.size();

		System.out.println("Verification des donnees du tier.");

		menu.getTabPanes()[0][1].removeAll();
		menu.addLabel("Chargement des donnees", 0, 0, true);

		String reussi = "OK";

		boolean PDRpresent = false;
		boolean picotsPresents = false;
		boolean picsToxicsPresents = false;
		boolean toileGluantePresente = false;

		int diversite = 0;
		String pokemonVersatile = "Missingno";
		
		int tailleOU = 0;

		for (int i=0; i<tiers.size(); ++i){
			Pokemon test = new Pokemon(tiers.get(i), 0, liste, "Smogon");
			String nom = test.getNomSeul();
			int setMax = test.getNTotalSets();
			System.out.println((i+1) + "/" + tiers.size() + " - " + nom);
			test = new Pokemon(tiers.get(i), setMax-1, liste, "Smogon");
			test = new Pokemon(tiers.get(i), (int)(setMax/2), liste, "Smogon");
			test = new Pokemon(tiers.get(i), alea.nextInt(setMax), liste, "Smogon");

			int iBis = i;
			//if (iBis>8){++iBis;}
			int x=(int) iBis/imgParLigne +1;
			int y=(int) iBis%imgParLigne;
			if (test.getUtilisationEspece() > 3.406367108){
				menu.addImage(tiers.get(i), x, y);
				tailleOU = i;
			}

			try{
				test = new Pokemon(tiers.get(i), 0, liste, "Smogon");

				if (test.diversite() > diversite){
					diversite = test.diversite();
					pokemonVersatile = tiers.get(i);
				}

				if (test.peutApprendre("Stealth Rock")){
					PDRpresent = true;
				}
				if (test.peutApprendre("Spikes")){
					picotsPresents = true;
				}
				if (test.peutApprendre("Toxic Spikes")){
					picsToxicsPresents = true;
				}
				if (test.peutApprendre("Sticky Web")){
					toileGluantePresente = true;
				}
			}
			catch(Exception e){
				reussi = tiers.get(i);
				i = tiers.size();
			}
		}

		if (reussi.equals("OK")){
			System.out.println("\nLa database est complete.\n");
		}
		else{
			System.out.println("Il manque le fichier de " + reussi + ".");
			System.out.println(1/0);
		}

		System.out.println("Pokemon le plus versatile : " + pokemonVersatile + " (" + diversite + ")");

		if (diversite > 6){
			diversite = 6;
			System.out.println("Diversite ramenee a " + 6);
		}

		boolean match = true;
		
		menu.getTabPanes()[0][1].removeAll();
		menu.addLabel("Que voulez-vous faire ?", 0, 0, false);
		menu.addButton("Match", 1, 0, 0, false);
		menu.addButton("Team Building", 2, 0, 0, true);

		clic = -1;
		while (clic < 0){
			clic = menu.quelClic();
		}
		if (clic == 2){
			match = false;
		}
		menu.resetClics();

		Scanner sc = new Scanner(System.in);

		Fenetre chosePkmn = new Fenetre("Choisissez votre Pokemon");
		chosePkmn.getContentPane().setLayout(new BorderLayout());
		BorderLayout layout = (BorderLayout) chosePkmn.getContentPane().getLayout();
		JLabel txt = new JLabel ("Choisissez votre Pokemon");
		txt.setHorizontalAlignment(JLabel.CENTER);
		chosePkmn.getContentPane().add(txt, BorderLayout.NORTH);

		while (match){

			menu.setVisible(false);
			menu.getTabPanes()[0][1].removeAll();

			Joueur J1 = new Joueur("J1", false);
			Joueur J2 = new Joueur("J2", true);

			chosePkmn.setVisible(true);

			Image img = null;
			try {
				img = ImageIO.read(new File("Database/Images/Honor Ball.png"));
			}
			catch(Exception e){
				System.out.println("cnerof");
			}

			try{
				chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.WEST));
			}
			catch(Exception e){}
			Box WestBox = Box.createVerticalBox();
			Bouton aleaWest = new Bouton("Aleatoire");
			WestBox.add(aleaWest);
			JLabel labelGauche = new JLabel(new ImageIcon(img));
			labelGauche.setVerticalAlignment(JLabel.TOP);
			WestBox.add(labelGauche);
			chosePkmn.getContentPane().add(WestBox, BorderLayout.WEST);
			chosePkmn.associateBoutonListener(aleaWest, 997);

			try{
				chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.EAST));
			}
			catch(Exception e){}
			Box EastBox = Box.createVerticalBox();
			Bouton aleaEast = new Bouton("Aleatoire");
			EastBox.add(aleaEast);
			JLabel labelDroite = new JLabel(new ImageIcon(img));
			labelDroite.setVerticalAlignment(JLabel.TOP);
			EastBox.add(labelDroite);
			chosePkmn.getContentPane().add(EastBox, BorderLayout.EAST);
			chosePkmn.associateBoutonListener(aleaEast, 998);

			Panneau panCentral = new Panneau();
			panCentral.miseEnForme(new int[] {8,8,8,8,8,8,8});
			for (int i=0; i<tailleOU; ++i){
				int iBis = i;
				//if (iBis>8){++iBis;}
				int x=(int) iBis/imgParLigne;
				int y=(int) iBis%imgParLigne;
				Bouton boutonPkmn = new Bouton("");
				boutonPkmn.setIcon(new ImageIcon ("Database/Images/" + tiers.get(i) + "_icon.png"));
				chosePkmn.associateBoutonListener(boutonPkmn, i);
				panCentral.getTabPanes()[x][y+1].add(boutonPkmn);
			}
			chosePkmn.add(panCentral, BorderLayout.CENTER);

			Bouton boutonSud = new Bouton("Demarrer");
			chosePkmn.associateBoutonListener(boutonSud, 999);
			chosePkmn.getContentPane().add(boutonSud, BorderLayout.SOUTH);

			chosePkmn.setVisible(false);
			chosePkmn.setVisible(true);

			String pkmn1 = "alea";
			int set1 = alea.nextInt(50)+1;
			String pkmn2 = "alea";
			int set2 = alea.nextInt(50)+1;

			boolean demarrer = false;
			int joueur = 1;
			while (!demarrer){

				clic = chosePkmn.quelClic();

				if (clic >= 0 && clic < tiers.size()){
					if (joueur == 1){
						pkmn2 = tiers.get(clic);

						chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.WEST));

						WestBox = Box.createVerticalBox();
						aleaWest = new Bouton("Aleatoire");
						WestBox.add(aleaWest);
						labelGauche = new JLabel(new ImageIcon("Database/Images/" + pkmn2 + ".png"));
						labelGauche.setVerticalAlignment(JLabel.TOP);
						WestBox.add(labelGauche);
						chosePkmn.getContentPane().add(WestBox, BorderLayout.WEST);
						chosePkmn.associateBoutonListener(aleaWest, 97);

						joueur = 2;

						chosePkmn.resetClics();
						chosePkmn.setVisible(false);
						chosePkmn.setVisible(true);
					}
					else{
						pkmn1 = tiers.get(clic);

						chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.EAST));

						EastBox = Box.createVerticalBox();
						aleaEast = new Bouton("Aleatoire");
						EastBox.add(aleaEast);
						labelDroite = new JLabel(new ImageIcon("Database/Images/" + pkmn1 + ".png"));
						labelDroite.setVerticalAlignment(JLabel.TOP);
						EastBox.add(labelDroite);
						chosePkmn.getContentPane().add(EastBox, BorderLayout.EAST);
						chosePkmn.associateBoutonListener(aleaEast, 98);

						joueur = 1;

						chosePkmn.resetClics();
						chosePkmn.setVisible(false);
						chosePkmn.setVisible(true);
					}
				}

				if (clic == 997){
					pkmn2 = "alea";

					chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.WEST));

					WestBox = Box.createVerticalBox();
					aleaWest = new Bouton("Aleatoire");
					WestBox.add(aleaWest);
					labelGauche = new JLabel(new ImageIcon("Database/Images/Honor Ball.png"));
					labelGauche.setVerticalAlignment(JLabel.TOP);
					WestBox.add(labelGauche);
					chosePkmn.getContentPane().add(WestBox, BorderLayout.WEST);
					chosePkmn.associateBoutonListener(aleaWest, 97);

					chosePkmn.resetClics();
					chosePkmn.setVisible(false);
					chosePkmn.setVisible(true);
				}
				if (clic == 998){
					pkmn1 = "alea";

					chosePkmn.getContentPane().remove(layout.getLayoutComponent(BorderLayout.EAST));

					EastBox = Box.createVerticalBox();
					aleaEast = new Bouton("Aleatoire");
					EastBox.add(aleaEast);
					labelDroite = new JLabel(new ImageIcon("Database/Images/Honor Ball.png"));
					labelDroite.setVerticalAlignment(JLabel.TOP);
					EastBox.add(labelDroite);
					chosePkmn.getContentPane().add(EastBox, BorderLayout.EAST);
					chosePkmn.associateBoutonListener(aleaEast, 98);

					chosePkmn.resetClics();
					chosePkmn.setVisible(false);
					chosePkmn.setVisible(true);
				}

				if (clic == 999){
					demarrer = true;
					chosePkmn.resetClics();
				}
			}

			chosePkmn.setVisible(false);

			if (pkmn1.equals("alea")){
				pkmn1 = tiers.get(alea.nextInt(tiers.size()));
			}
			if (pkmn2.equals("alea")){
				pkmn2 = tiers.get(alea.nextInt(tiers.size()));
			}

			set1 = alea.nextInt((new Pokemon (pkmn1, 0, liste, "Smogon")).getNTotalSets());
			set2 = alea.nextInt((new Pokemon (pkmn2, 0, liste, "Smogon")).getNTotalSets());

			//set2 = 2963;

			Pokemon P1 = new Pokemon (pkmn1, set1, liste, "Smogon");
			Pokemon P2 = new Pokemon (pkmn2, set2, liste, "Smogon");

			P1.print();
			P2.print();

			System.out.println("\n");

			if (!P1.isCompetitif()){
				System.out.println(P1.getNom() + " n'est pas competitif.");
			}
			if (!P2.isCompetitif()){
				System.out.println(P2.getNom() + " n'est pas competitif.");
			}
			if (!P1.isCompetitif() || !P2.isCompetitif()){
				System.out.println("");
			}

			System.out.println("Distance entre ces pokemons : " + P1.distance(P2) + "\n\n");

			Combat test = new Combat(J1, J2, P1, P2, "claire", 0, "normal", 0, 0, 0, 0, 0, true);

			System.out.println(1000 - test.match(false, false, false, false, false, false, false, false, false) + " points pour " + P2.getNom());

			menu.getTabPanes()[0][1].removeAll();
			menu.addLabel("Voulez-vous faire un autre match ?", 0, 0, false);
			menu.addButton("Oui", 1, 0, 0, false);
			menu.addButton("Non", 2, 0, 0, true);
			menu.setVisible(true);

			clic = -1;
			while (clic < 0){
				clic = menu.quelClic();
			}
			if (clic == 2){
				match = false;
			}
			menu.resetClics();
		}

		System.out.println("\nL'etude d'equipe commence.");

		ArrayList<String> equipe = new ArrayList<String>();

		Joueur J1 = new Joueur("J1", false);
		Joueur J2 = new Joueur("J2", false);

		ArrayList<String> adversairesGlobaux = new ArrayList<String>();
		ArrayList<Float> poidsAdversaires = new ArrayList<Float>();
		for (int i = 0; i<tiers.size(); ++i){
			String machin = tiers.get(i) + " " + (new Pokemon(tiers.get(i), 0, liste, "Smogon")).getUtilisationEspece();
			adversairesGlobaux.add(machin);
		}

		//	String desc = "....:...10%...:...20%...:...30%...:...40%...:...50%...:...60%...:...70%...:...80%...:...90%...:.100%";
		//	int crans = desc.length();

		// IMPORTATION DE LA SAUVEGARDE

		String meilleurPokemon = "aucun";
		int meilleurScore = 0;

		ArrayList<String> ennemisRestants = new ArrayList<String>();

		ArrayList<Integer> pointsMax = new ArrayList<Integer>();
		ArrayList<Integer> pointsMaxCumules = new ArrayList<Integer>();
		ArrayList<Integer> pallier = new ArrayList<Integer>();

		boolean horizontal = true;
		int setZero = 0;
		int pkmnZero = 0;

		try{
			BufferedReader liseur = new BufferedReader (new FileReader("Database/Sauvegardes/" + liste));
			String lgn = liseur.readLine();
			horizontal = lgn.split(" ")[0].equals("Horizontal");
			setZero = Integer.valueOf(lgn.split(" ")[1]).intValue();
			if (setZero <0){
				setZero = 0;
			}

			lgn = liseur.readLine();
			pkmnZero = Integer.valueOf(lgn.split(" - ")[0]).intValue() + 1;

			liseur.readLine();
			liseur.readLine();
			liseur.readLine();
			meilleurPokemon = liseur.readLine();
			meilleurScore = Integer.valueOf(liseur.readLine()).intValue();

			System.out.println("\nActuel meilleur Pokemon : " + meilleurPokemon + " (" + meilleurScore + "pts)");
			System.out.println("Ennemis de " + meilleurPokemon + " :");

			liseur.readLine();
			String[] ennemis = liseur.readLine().split(" / ");
			System.out.println(Arrays.toString(ennemis));
			ennemisRestants = new ArrayList<String>();
			for (int i=0; i<ennemis.length; ++i){
				ennemisRestants.add(ennemis[i]);
			}
			liseur.readLine();
			liseur.readLine();
			String[] team = liseur.readLine().split(" / ");
			for (int i=0; i<team.length; ++i){
				if (!team[i].equals("")){
					equipe.add(team[i]);
				}
			}
			System.out.println("\nEquipe actuelle : " + equipe.size() + " membre(s)");
			System.out.println(equipe.toString());

			System.out.println("\nAdversaires :");
			liseur.readLine();
			String Nmi = liseur.readLine();
			ennemis = Nmi.split(" / ");
			System.out.println(Arrays.toString(ennemis));
			adversairesGlobaux = new ArrayList<String>();
			for (int i=0; i<ennemis.length; ++i){
				adversairesGlobaux.add(ennemis[i]);
			}

			liseur.close();

			if (pkmnZero >= tiers.size()){
				pkmnZero = 0;
				++setZero;
				System.out.println("Wrap pokemon navigation");
			}

			System.out.println("\nImportation de la sauvegarde reussie!");
		}
		catch(Exception e){
			System.out.println("Echec d'importation de la sauvegarde");
			e.printStackTrace();
			sc = new Scanner(System.in);
			sc.nextLine();
		}

		// SIMULATIONS

		if (adversairesGlobaux.size() == 0 || equipe.size() <= 0){
			System.out.println("Reset advGlob");
			adversairesGlobaux = new ArrayList<String>();
			for (int i = 0; i<tiers.size(); ++i){
				String machin = tiers.get(i) + " " + (new Pokemon(tiers.get(i), 0, liste, "Smogon")).getUtilisationEspece();
				adversairesGlobaux.add(machin);
			}
		}


		while (adversairesGlobaux.size() > 0 && equipe.size() < 6){
			System.out.println("\nDescription du tier :");
			tailleTier = adversairesGlobaux.size();
			poidsAdversaires = new ArrayList<Float>();
			int maxPoints = 0;
			for (int i=0; i<tailleTier; ++i){
				poidsAdversaires.add(Float.valueOf(adversairesGlobaux.get(i).split(" ")[1]).floatValue());
				pointsMax.add((int) (1000 * poidsAdversaires.get(i)));
				maxPoints += pointsMax.get(i);
				System.out.println(poidsAdversaires.get(i) + "\t" + adversairesGlobaux.get(i).split(" ")[0]);
			}
			System.out.println("Nombre d'adversaires : " + tailleTier + "\n");
			System.out.println("Score max : " + maxPoints);

			System.out.println("");

			if (equipe.size() == 0){
				PDRpresent = false;
				picotsPresents = false;
				picsToxicsPresents = false;
				toileGluantePresente = false;
			}

			if (PDRpresent){
				System.out.println("Piege de Roc present");
			}
			if (picotsPresents){
				System.out.println("Picots presents");
			}
			if (picsToxicsPresents){
				System.out.println("Pics Toxics presents");
			}
			if (toileGluantePresente){
				System.out.println("Toile Gluante presente");
			}
			System.out.println("");

			pointsMaxCumules.add(pointsMax.get(0));
			for (int i=1; i<pointsMax.size(); ++i){
				pointsMaxCumules.add(pointsMaxCumules.get(i-1) + pointsMax.get(i));
			}

			//System.out.println("\nPalliers :");
			for (int i=0; i<pointsMaxCumules.size(); ++i){
				pallier.add(pointsMaxCumules.get(pointsMaxCumules.size()-1) - pointsMaxCumules.get(i));
				//System.out.println(pallier.get(i));
			}

			boolean PDR1 = PDRpresent;
			boolean picots1 = picotsPresents;
			boolean picsToxics1 = picsToxicsPresents;
			boolean toileGluante1 = toileGluantePresente;

			boolean PDR2 = false;
			boolean picots2 = false;
			boolean picsToxics2 = false;
			boolean toileGluante2 = false;

			boolean freeMegaSlot = true;

			for (int i=0; i<equipe.size(); ++i){
				System.out.println(equipe.get(i));
				String nom = equipe.get(i).split(Pattern.quote(" ["))[0];
				if (nom.startsWith("Mega-")){
					nom = nom.split("Mega-")[1];
				}
				int set = Integer.valueOf(equipe.get(i).split(Pattern.quote(" [Smogon "))[1].split(Pattern.quote("]"))[0]).intValue();

				Pokemon test = new Pokemon (nom, set, liste, "Smogon");

				if ((test.getType()[0].equals("Poison") || test.getType()[1].equals("Poison")) && !test.getType()[0].equals("Flying") && !test.getType()[1].equals("Flying") && !test.getTalent().equals("Levitate") && !test.getObjet().equals("Air Balloon")){
					picsToxics1 = false;
					System.out.println("Pics Toxics absorbes!");
				}
				for (int j=0; j<4; ++j){
					if (test.getMoves()[j].getNom().equals("Rapid Spin") || test.getMoves()[j].getNom().equals("Defog")){
						PDR1 = false;
						picots1 = false;
						picsToxics1 = false;
						toileGluante1 = false;
						System.out.println("Entry Hazards elimines");
					}
					if (test.getMoves()[j].getNom().equals("Stealth Rock")){
						PDR2 = true;
						System.out.println("Pieges de Roc en place !");
					}
					if (test.getMoves()[j].getNom().equals("Spikes")){
						picots2 = true;
						System.out.println("Picots en place !");
					}
					if (test.getMoves()[j].getNom().equals("Toxic Spikes")){
						picsToxics2 = true;
						System.out.println("Pics Toxics en place !");
					}
					if (test.getMoves()[j].getNom().equals("Sticky Web")){
						toileGluante2 = true;
						System.out.println("Toile Gluante en place !");
					}
				}
				if ((test.getObjet().endsWith("ite") || test.getObjet().endsWith("ite X") || test.getObjet().endsWith("ite Y")) && !test.getObjet().equals("Eviolite")){
					System.out.println("Mega-slot occupe");
					freeMegaSlot = false;
				}
			}

			System.out.print("\nMeilleur Pokemon : ");

			try{
				(new Pokemon(meilleurPokemon.split(" ")[0], Integer.valueOf(meilleurPokemon.split("Smogon ")[1].split("]")[0]).intValue(), liste, "Smogon")).print();
			}
			catch(Exception e){

			}

			if (horizontal){
				for (int iAtq=pkmnZero; iAtq<tiers.size(); ++iAtq){
						if (pkmnZero > 0){
							pkmnZero = 0;
						}

						boolean speciesClause = true;

						for (int n=0; n<equipe.size(); ++n){
							if (equipe.get(n).startsWith(tiers.get(iAtq))){
								speciesClause = false;
								//System.out.println("\n" + tiers.get(iAtq) + " abandonne pour species clause.");
							}
						}

						if (speciesClause) {

							boolean valide = false;

							int setAtq = alea.nextInt((new Pokemon(tiers.get(iAtq), 0, liste, "Smogon")).getNTotalSets());

							int compteur = 0;
							int limiteCompteur = 100;

							while (!valide && setAtq > 0 && compteur < limiteCompteur) {
								++compteur;

								boolean competitif = true;
								boolean probable = false;
								boolean megaClause = true;

								setAtq = alea.nextInt((new Pokemon(tiers.get(iAtq), 0, liste, "Smogon")).getNTotalSets());

								competitif = (new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).isCompetitif();
								probable = ((new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).getUtilisationSet() >= 1);

								if (competitif && !freeMegaSlot && ((new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).getObjet().endsWith("ite") || (new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).getObjet().endsWith("ite X") || (new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).getObjet().endsWith("ite Y")) && !(new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon")).getObjet().endsWith("Eviolite")) {
									megaClause = false;
								}

								valide = competitif && megaClause && probable;
							}

							if (compteur >= limiteCompteur) {
								System.out.println("\nAucun set valide trouve pour " + tiers.get(iAtq) + ".\n");
							} else {

								System.out.println("\nMeilleur Pokemon : " + meilleurPokemon + " (" + meilleurScore + " points)");

								int NpokeAdv = 0;

								int score = 0;
								Liste ennemis = new Liste();
								Pokemon P1 = new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon");

								System.out.println("\n");
								P1.print();
								System.out.println();

								boolean PDR1bis = PDR1;
								boolean picots1bis = picots1;
								boolean picsToxics1bis = picsToxics1;
								boolean toileGluante1bis = toileGluante1;

								boolean PDR2bis = PDR2;
								boolean picots2bis = picots2;
								boolean picsToxics2bis = picsToxics2;
								boolean toileGluante2bis = toileGluante2;

								if ((P1.getType()[0].equals("Poison") || P1.getType()[1].equals("Poison")) && !P1.getType()[0].equals("Flying") && !P1.getType()[1].equals("Flying") && !P1.getTalent().equals("Levitate") && !P1.getObjet().equals("Air Balloon")) {
									picsToxics1bis = false;
									System.out.println("Pics Toxics absorbes!");
								}
								for (int j = 0; j < 4; ++j) {
									if (P1.getMoves()[j].getNom().equals("Rapid Spin") || P1.getMoves()[j].getNom().equals("Defog")) {
										PDR1bis = false;
										picots1bis = false;
										picsToxics1bis = false;
										System.out.println("Entry Hazards elimines");
									}
									if (P1.getMoves()[j].getNom().equals("Stealth Rock")) {
										PDR2bis = true;
										System.out.println("Pieges de Roc en place !");
									}
									if (P1.getMoves()[j].getNom().equals("Spikes")) {
										picots2bis = true;
										System.out.println("Picots en place !");
									}
									if (P1.getMoves()[j].getNom().equals("Toxic Spikes")) {
										picsToxics2bis = true;
										System.out.println("Pics Toxics en place !");
									}
									if (P1.getMoves()[j].getNom().equals("Sticky Web")) {
										toileGluante2bis = true;
										System.out.println("Toile Gluante en place !");
									}
								}

								int numRatioInit = (int) (maxPoints - meilleurScore);
								if (numRatioInit <= 0) {
									numRatioInit = 0;
								}
								int denRatioInit = meilleurScore;
								if (denRatioInit <= 0) {
									denRatioInit = 1;
								}
								float ratioInit = (float) numRatioInit / (float) denRatioInit;

								System.out.print("r_init = " + ratioInit);

								int iPal = 0;
								while (pallier.get(iPal) > meilleurScore) {
									++iPal;
								}
								System.out.println(" (--> " + adversairesGlobaux.get(iPal).split(" ")[0] + ")");

								for (int iDef = 0; iDef < adversairesGlobaux.size(); ++iDef) {

									System.out.print("VS " + adversairesGlobaux.get(iDef) + "	");
									if (adversairesGlobaux.get(iDef).length() < 5) {
										System.out.print("\t");
									}
									if (adversairesGlobaux.get(iDef).length() < 9) {
										System.out.print("\t");
									}
									if (adversairesGlobaux.get(iDef).length() < 13) {
										System.out.print("\t");
									}
									if (adversairesGlobaux.get(iDef).length() < 17) {
										System.out.print("\t");
									}
									if (adversairesGlobaux.get(iDef).length() < 21) {
										System.out.print("\t");
									}
									if (adversairesGlobaux.get(iDef).length() < 25) {
										System.out.print("\t");
									}

									float pointsVSespece = 0;
									float ponderation = 0;

									for (int sDef = 0; sDef < diversite; ++sDef) {

										boolean competitif = false;
										boolean probable = false;

										int setDef = 0;

										while (!competitif && !probable) {

											setDef = alea.nextInt((new Pokemon(adversairesGlobaux.get(iDef).split(" ")[0], 0, liste, "Smogon")).getNTotalSets());

											competitif = (new Pokemon(adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon")).isCompetitif();
											probable = ((new Pokemon(adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon")).getUtilisationSet() > 1);
										}

										P1 = new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon");
										Pokemon P2 = new Pokemon(adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon");

										Combat test = new Combat(J1, J2, P1, P2, "claire", 0, "normal", 0, 0, 0, 0, 0, false);

										boolean passeTour = (equipe.size() > 0 && equipe.size() < 5);
										//boolean passeTour = false;

										int pointsMatch = test.match(PDR1bis, picots1bis, picsToxics1bis, toileGluante1bis, PDR2bis, picots2bis, picsToxics2bis, toileGluante2bis, passeTour);

										pointsVSespece += pointsMatch * P2.getUtilisationSet();
										ponderation += P2.getUtilisationSet();
										//System.out.println("Match : " + pointsMatch + " / VS espece : " + pointsVSespece + " / ponderation : " + ponderation);

										if (pointsMatch <= 500) {
											if (pointsMatch < 500 && pointsMatch > 0) {
												System.out.print("x");
											}
											if (pointsMatch == 500) {
												System.out.print("=");
											}
											if (pointsMatch == 0) {
												System.out.print(".");
											}
										} else {
											if (pointsMatch < 1000) {
												System.out.print("o");
											}
											if (pointsMatch == 1000) {
												System.out.print("O");
											}
										}
									}
									++NpokeAdv;

									double preScore = (int) (pointsVSespece / ponderation);

									score += preScore * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue();

									Pokemon ajout = new Pokemon(adversairesGlobaux.get(iDef).split(" ")[0], 0, liste, "Smogon");
									Float coef = (float) (Math.pow((1000 - preScore), 2) / 1000000 + (1000 - preScore) / 2000 + 0.5);
									ennemis.add(ajout.getNomSeul() + " " + coef * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue());

									int numRatio = (int) (score + pallier.get(NpokeAdv - 1) - meilleurScore);
									if (numRatio <= 0) {
										numRatio = 0;
									}
									int denRatio = meilleurScore - score;
									if (denRatio <= 0) {
										denRatio = 1;
									}
									float ratio = (float) numRatio / (float) denRatio;
									System.out.println("	" + score + " pts" + " (" + ((int) (pointsVSespece / ponderation / 10)) + "% --> " + (coef * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue()) + " - " + "AR: " + (tailleTier - NpokeAdv) + " --> " + (score + pallier.get(NpokeAdv - 1) - meilleurScore) + ", r=" + ratio + ")");

									if (score + pallier.get(NpokeAdv - 1) <= meilleurScore) {
										iDef = adversairesGlobaux.size();
									}

									if (iDef >= iPal && ratio < ratioInit) {
										iDef = adversairesGlobaux.size();
									}
								}
								ennemis.triFusion();
								System.out.println(ennemis.toString());

								if (score > meilleurScore) {
									meilleurScore = score;
									meilleurPokemon = P1.getNom();
									ennemisRestants = ennemis.getListe0();
								}

								// ECRITURE DE LA SAUVEGARDE
								try {
									P1 = new Pokemon(tiers.get(iAtq), setAtq, liste, "Smogon");
									PrintWriter save = new PrintWriter(new BufferedWriter(new FileWriter("Database/Sauvegardes/" + liste)));
									//								if (iAtq >= tiers.size()){
									//									++sAtq;
									//								}
									save.println("Horizontal 0");
									save.println(iAtq + " - " + P1.getNom());
									save.println(score);
									save.println("");
									save.println("Meilleur score :");
									save.println(meilleurPokemon);
									save.println(meilleurScore);
									save.println("Adversaires de " + meilleurPokemon + " :");
									for (int i = 0; i < ennemisRestants.size() - 1; ++i) {
										save.print(ennemisRestants.get(i) + " / ");
									}
									if (ennemisRestants.size() > 0) {
										save.println(ennemisRestants.get(ennemisRestants.size() - 1));
									} else {
										save.println("");
									}
									save.println("");
									save.println("EQUIPE :");
									if (equipe.size() == 0) {
										save.println("");
									} else {
										for (int i = 0; i < equipe.size() - 1; ++i) {
											save.print(equipe.get(i) + " / ");
										}
										save.println(equipe.get(equipe.size() - 1));
									}


									save.println("ADVERSAIRES RESTANTS :");
									for (int i = 0; i < adversairesGlobaux.size() - 1; ++i) {
										save.print(adversairesGlobaux.get(i) + " / ");
									}
									save.println(adversairesGlobaux.get(adversairesGlobaux.size() - 1));

									if (equipe.size() > 0) {
										for (int i = 0; i < equipe.size(); ++i) {
											String nomI = equipe.get(i).split(Pattern.quote(" [Smogon "))[0];
											int setI = Integer.valueOf(equipe.get(i).split(Pattern.quote(" [Smogon "))[1].split("]")[0]).intValue();
											save.print("\n\n" + (new Pokemon(nomI, setI, liste, "Smogon")).toString());
										}
									}

									save.close();
								} catch (Exception e) {
									System.out.println("La sauvegarde a echoue");
									e.printStackTrace();
								}
							}
						}
				}

				horizontal = false;
				setZero = -1;
			}

			if (!horizontal){

				String meilleureEspece = meilleurPokemon.split(" ")[0];
				int meilleurSet = Integer.valueOf(meilleurPokemon.split("Smogon ")[1].split("]")[0]).intValue();
				int Nset = (new Pokemon(meilleureEspece, 0, liste, "Smogon")).getNTotalSets();

				for (int sAtq=setZero+1; sAtq < Nset; ++sAtq){
					if (setZero > 0){
						setZero = 0;
					}

					meilleureEspece = meilleurPokemon.split(" ")[0];
					meilleurSet = Integer.valueOf(meilleurPokemon.split("Smogon ")[1].split("]")[0]).intValue();

					Pokemon veryBest = new Pokemon (meilleureEspece, meilleurSet, liste, "Smogon");
					Pokemon cousin = new Pokemon (meilleureEspece, sAtq, liste, "Smogon");

					boolean megaClause = true;

					if (!freeMegaSlot && (cousin.getObjet().endsWith("ite") || cousin.getObjet().endsWith("ite X") || cousin.getObjet().endsWith("ite Y")) && !cousin.getObjet().endsWith("Eviolite")){
						megaClause = false;
					}

					System.out.println(sAtq + " / " + Nset);

					if (cousin.isCompetitif() && megaClause && cousin.distance(veryBest) <= 2 && cousin.distance(veryBest) > 0){

						System.out.println("\nMeilleur Pokemon : " + meilleurPokemon + " (" + meilleurScore + " points)");

						int NpokeAdv = 0;

						int score = 0;
						Liste ennemis = new Liste();
						Pokemon P1 = new Pokemon (meilleureEspece, sAtq, liste, "Smogon");

						System.out.println("\n");
						P1.print();
						System.out.println();

						boolean PDR1bis = PDR1;
						boolean picots1bis = picots1;
						boolean picsToxics1bis = picsToxics1;
						boolean toileGluante1bis = toileGluante1;

						boolean PDR2bis = PDR2;
						boolean picots2bis = picots2;
						boolean picsToxics2bis = picsToxics2;
						boolean toileGluante2bis = toileGluante2;

						if ((P1.getType()[0].equals("Poison") || P1.getType()[1].equals("Poison")) && !P1.getType()[0].equals("Flying") && !P1.getType()[1].equals("Flying") && !P1.getTalent().equals("Levitate") && !P1.getObjet().equals("Air Balloon")){
							picsToxics1bis = false;
							System.out.println("Pics Toxics absorbes!");
						}
						for (int j=0; j<4; ++j){
							if (P1.getMoves()[j].getNom().equals("Rapid Spin") || P1.getMoves()[j].getNom().equals("Defog")){
								PDR1bis = false;
								picots1bis = false;
								picsToxics1bis = false;
								System.out.println("Entry Hazards elimines");
							}
							if (P1.getMoves()[j].getNom().equals("Stealth Rock")){
								PDR2bis = true;
								System.out.println("Pieges de Roc en place !");
							}
							if (P1.getMoves()[j].getNom().equals("Spikes")){
								picots2bis = true;
								System.out.println("Picots en place !");
							}
							if (P1.getMoves()[j].getNom().equals("Toxic Spikes")){
								picsToxics2bis = true;
								System.out.println("Pics Toxics en place !");
							}
							if (P1.getMoves()[j].getNom().equals("Sticky Web")){
								toileGluante2bis = true;
								System.out.println("Toile Gluante en place !");
							}
						}

						int numRatioInit = (int) (maxPoints - meilleurScore);
						if (numRatioInit<=0){numRatioInit = 0;}
						int denRatioInit = meilleurScore;
						if (denRatioInit<=0){denRatioInit = 1;}
						float ratioInit = (float) numRatioInit / (float) denRatioInit;

						System.out.print("r_init = " + ratioInit);

						int iPal=0;
						while (pallier.get(iPal) > meilleurScore){
							++iPal;
						}
						System.out.println(" (--> " + adversairesGlobaux.get(iPal).split(" ")[0] + ")");

						for (int iDef=0; iDef<adversairesGlobaux.size(); ++iDef){

							System.out.print("VS " + adversairesGlobaux.get(iDef) + "	");
							if (adversairesGlobaux.get(iDef).length() < 5){
								System.out.print("\t");
							}
							if (adversairesGlobaux.get(iDef).length() < 9){
								System.out.print("\t");
							}
							if (adversairesGlobaux.get(iDef).length() < 13){
								System.out.print("\t");
							}
							if (adversairesGlobaux.get(iDef).length() < 17){
								System.out.print("\t");
							}
							if (adversairesGlobaux.get(iDef).length() < 21){
								System.out.print("\t");
							}
							if (adversairesGlobaux.get(iDef).length() < 25){
								System.out.print("\t");
							}

							float pointsVSespece = 0;
							float ponderation = 0;

							for (int sDef = 0; sDef < diversite; ++sDef){

								boolean competitif = false;
								boolean probable = false;

								int setDef = 0;

								while (!competitif && !probable){

									setDef = alea.nextInt((new Pokemon (adversairesGlobaux.get(iDef).split(" ")[0], 0, liste, "Smogon")).getNTotalSets());

									competitif = (new Pokemon (adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon")).isCompetitif();
									probable = ((new Pokemon (adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon")).getUtilisationSet() > 1);
								}

								P1 = new Pokemon (meilleureEspece, sAtq, liste, "Smogon");
								Pokemon P2 = new Pokemon (adversairesGlobaux.get(iDef).split(" ")[0], setDef, liste, "Smogon");

								Combat test = new Combat(J1, J2, P1, P2, "claire", 0, "normal", 0, 0, 0, 0, 0, false);

								boolean passeTour = (equipe.size() > 0);
								//boolean passeTour = false;

								int pointsMatch = test.match(PDR1bis, picots1bis, picsToxics1bis, toileGluante1bis, PDR2bis, picots2bis, picsToxics2bis, toileGluante2bis, passeTour);

								pointsVSespece += pointsMatch * P2.getUtilisationSet();
								ponderation += P2.getUtilisationSet();
								//System.out.println("Match : " + pointsMatch + " / VS espece : " + pointsVSespece + " / ponderation : " + ponderation);

								if (pointsMatch <= 500){
									if (pointsMatch < 500 && pointsMatch > 0){
										System.out.print("x");
									}
									if (pointsMatch == 500){
										System.out.print("=");
									}
									if (pointsMatch == 0){
										System.out.print(".");
									}
								}
								else{
									if (pointsMatch < 1000){
										System.out.print("o");
									}
									if (pointsMatch == 1000){
										System.out.print("O");
									}
								}
								//System.out.println(pointsVSespece + " - " + ponderation);
							}
							++NpokeAdv;

							double preScore = (int) (pointsVSespece / ponderation);

							score += preScore * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue();

							Pokemon ajout = new Pokemon (adversairesGlobaux.get(iDef).split(" ")[0], 0, liste, "Smogon");
							Float coef = (float) (Math.pow((1000-preScore), 2)/1000000 + (1000-preScore)/2000 + 0.5);
							ennemis.add(ajout.getNomSeul() + " " + coef * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue());

							int numRatio = (int) (score + pallier.get(NpokeAdv-1) - meilleurScore);
							if (numRatio<=0){numRatio = 0;}
							int denRatio = meilleurScore - score;
							if (denRatio<=0){denRatio = 1;}
							float ratio = (float) numRatio / (float) denRatio;
							System.out.println("	" + score + " pts" + " (" + ((int) (pointsVSespece/ponderation/10)) + "% --> " + (coef * Float.valueOf(adversairesGlobaux.get(iDef).split(" ")[1]).floatValue()) + " - " + "AR: " + (tailleTier - NpokeAdv) + " --> " + (score + pallier.get(NpokeAdv-1) - meilleurScore) + ", r=" + ratio + ")");

							if (score + pallier.get(NpokeAdv-1) <= meilleurScore){
								iDef = adversairesGlobaux.size();
							}

							if (iDef >= iPal && ratio < ratioInit){
								iDef = adversairesGlobaux.size();
							}
						}

						ennemis.triFusion();
						System.out.println(ennemis.toString());

						if (score > meilleurScore){
							meilleurScore = score;
							meilleurPokemon = P1.getNom();
							ennemisRestants = ennemis.getListe0();
							sAtq = -1;
						}

						// ECRITURE DE LA SAUVEGARDE
						try{
							P1 = new Pokemon (meilleureEspece, P1.getSet(), liste, "Smogon");
							PrintWriter save =  new PrintWriter(new BufferedWriter (new FileWriter("Database/Sauvegardes/" + liste)));
							save.println("Vertical " + sAtq);
							save.println("0 - " + P1.getNom());
							save.println(score);
							save.println("");
							save.println("Meilleur score :");
							save.println(meilleurPokemon);
							save.println(meilleurScore);
							save.println("Adversaires de " + meilleurPokemon + " :");
							for (int i=0; i<ennemisRestants.size()-1; ++i){
								save.print(ennemisRestants.get(i) + " / ");
							}
							if (ennemisRestants.size()>0){
								save.println(ennemisRestants.get(ennemisRestants.size()-1));
							}
							else{
								save.println("");
							}
							save.println("");
							save.println("EQUIPE :");
							if (equipe.size() == 0){
								save.println("");
							}
							else{
								for (int i=0; i< equipe.size()-1; ++i){
									save.print(equipe.get(i) + " / ");
								}
								save.println(equipe.get(equipe.size()-1));
							}


							save.println("ADVERSAIRES RESTANTS :");
							for (int i=0; i< adversairesGlobaux.size()-1; ++i){
								save.print(adversairesGlobaux.get(i) + " / ");
							}
							save.println(adversairesGlobaux.get(adversairesGlobaux.size()-1));

							save.println("\n\nDETAILS DE L'EQUIPE :");
							if (equipe.size() == 0){
								save.println("");
							}
							else{
								for (int i=0; i< equipe.size(); ++i){
									String nom = equipe.get(i).split(Pattern.quote(" [Smogon "))[0];
									int set = Integer.valueOf(equipe.get(i).split(Pattern.quote(" [Smogon "))[1].split("]")[0]).intValue();
									Pokemon aImprimer = new Pokemon(nom, set, liste, "Smogon");
									save.println("\n" + aImprimer.toString() + "\n");
								}
							}

							save.close();
						}
						catch (Exception e){
							System.out.println("La sauvegarde a echoue");
							e.printStackTrace();
						}

					}
				}

				System.out.println("\n\nMeilleur Pokemon : " + meilleurPokemon);
				equipe.add(meilleurPokemon);

				System.out.println("Ennemis restants :");
				for (int i=0; i<ennemisRestants.size(); ++i){
					System.out.println(ennemisRestants.get(i));
				}

				adversairesGlobaux = ennemisRestants;
				meilleurScore = 0;
				meilleurPokemon = "aucun";

				try{
					PrintWriter save =  new PrintWriter(new BufferedWriter (new FileWriter("Database/Sauvegardes/" + liste)));
					save.println("Horizontal " + -1);
					save.println(-1 + " - " + "No Pokemon [1]");
					save.println("0");
					save.println(" ");
					save.println("Meilleur score :");
					save.println(meilleurPokemon);
					save.println(meilleurScore);
					save.println("Adversaires de " + meilleurPokemon + " :");
					for (int i=0; i<ennemisRestants.size()-1; ++i){
						save.print(ennemisRestants.get(i) + " / ");
					}
					if (ennemisRestants.size()>0){
						save.println(ennemisRestants.get(ennemisRestants.size()-1));
					}
					save.println("");
					save.println("EQUIPE :");
					if (equipe.size() == 0){
						save.println("");
					}
					else{
						for (int i=0; i< equipe.size()-1; ++i){
							save.print(equipe.get(i) + " / ");
						}
						save.println(equipe.get(equipe.size()-1));
					}


					save.println("ADVERSAIRES RESTANTS :");
					for (int i=0; i< adversairesGlobaux.size()-1; ++i){
						save.print(adversairesGlobaux.get(i) + " / ");
					}
					save.println(adversairesGlobaux.get(adversairesGlobaux.size()-1));

					save.println("\n\nDETAILS DE L'EQUIPE :");
					if (equipe.size() == 0){
						save.println("");
					}
					else{
						for (int i=0; i< equipe.size(); ++i){
							String nom = equipe.get(i).split(Pattern.quote(" [Smogon "))[0];
							int set = Integer.valueOf(equipe.get(i).split(Pattern.quote(" [Smogon "))[1].split("]")[0]).intValue();
							Pokemon aImprimer = new Pokemon(nom, set, liste, "Smogon");
							save.println("\n" + aImprimer.toString() + "\n");
						}
					}
					
					save.close();
				}
				catch (Exception e){
					System.out.println("La sauvegarde a echoue");
				}

				horizontal = true;
			}
		}

		try{
			PrintWriter save =  new PrintWriter(new BufferedWriter (new FileWriter("Database/Sauvegardes/" + liste)));
			save.println("Horizontal " + -1);
			save.println(-1 + " - " + "No Pokemon [1]");
			save.println("0");
			save.println(" ");
			save.println("Meilleur score :");
			save.println(meilleurPokemon);
			save.println(meilleurScore);
			save.println("Adversaires de " + meilleurPokemon + " :");
			for (int i=0; i<ennemisRestants.size()-1; ++i){
				save.print(ennemisRestants.get(i) + " / ");
			}
			if (ennemisRestants.size()>0){
				save.println(ennemisRestants.get(ennemisRestants.size()-1));
			}
			save.println("");
			save.println("EQUIPE :");
			if (equipe.size() == 0){
				save.println("");
			}
			else{
				for (int i=0; i< equipe.size()-1; ++i){
					save.print(equipe.get(i) + " / ");
				}
				save.println(equipe.get(equipe.size()-1));
			}


			save.println("ADVERSAIRES RESTANTS :");
			for (int i=0; i< adversairesGlobaux.size()-1; ++i){
				save.print(adversairesGlobaux.get(i) + " / ");
			}
			save.println(adversairesGlobaux.get(adversairesGlobaux.size()-1));

			save.println("\n\nDETAILS DE L'EQUIPE :");
			if (equipe.size() == 0){
				save.println("");
			}
			else{
				for (int i=0; i< equipe.size(); ++i){
					String nom = equipe.get(i).split(Pattern.quote(" [Smogon "))[0];
					int set = Integer.valueOf(equipe.get(i).split(Pattern.quote(" [Smogon "))[1].split("]")[0]).intValue();
					Pokemon aImprimer = new Pokemon(nom, set, liste, "Smogon");
					save.println("\n" + aImprimer.toString() + "\n");
				}
			}

			save.close();
		}
		catch (Exception e){
			System.out.println("La sauvegarde a echoue");
		}
		
		System.out.println("\nProgramme termine avec succes !");
	}

}
