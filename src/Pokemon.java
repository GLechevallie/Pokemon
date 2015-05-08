import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

import com.rits.cloning.Cloner;


public class Pokemon implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Ce qui est propre a une espece
	private String nom;
	private String espece;
	private String metagame;
	private float utilisationEspece;
	private String[] type = new String[2];
	private String[] typeOriginal;
	private int[] baseStats = new int[6];
	private float poids;

	private String movepool;

	private int tailleMovepool = 0;
	private int tailleItems = 0;
	private int tailleSpreads = 0;

	private int NTotalSets;

	// Ce qui personnalise un individu
	private String maison;
	private int set;
	private String nomSet;
	private float utilisationSet;
	private boolean probable = true;
	private boolean competitif = true;
	private String commentaire = "";
	private String talentOriginal;
	private String talent;
	private String nature = "Hardy";
	private float[] tabNature = {1,1,1,1,1};
	private String objet = "(No Item)";
	private int[] IVs = {31,31,31,31,31,31};
	private int[] EVs = {0,0,0,0,0,0};
	private int[] statsInitiales = new int[9];
	private Attaque[] moves = new Attaque[4];
	private String PC = "Dark";
	private String sexe = null;

	// Ce qui peut varier pendant le match
	private int[] statsBoosts = {0,0,0,0,0,0,0,0,0};
	private float[] statsModifieurs = {1,1,1,1,1,1,1,1,1};
	private float[] statsModifiees = new float[9];			// PV, Atq, Def, SAtq, SDef, Vit, Prec, Esq, Crit
	private String statut = "OK";
	private int tourStatut = 0;
	private boolean trouille = false;
	private boolean amoureux = false;
	private int confus = 0;
	private boolean puissance = false;
	private float poidsModifieur = 0;
	private float poidsModifie;
	private Action actionPrevue = (Action) null;
	private Attaque derniereAttaque = null;
	private ArrayList<String> attaquesUtilisees = new ArrayList<String>();
	private boolean dejaAttaque = false;
	private boolean dejaSwitche = false;
	private boolean tenteSwitch = false;
	private boolean switchPrecedent = true;
	private String meteo = "claire";
	private String terrain = "normal";
	private boolean gravite = false;
	private boolean zoneMagique = false;
	private String objetConsomme = "(No Item)";
	private boolean briseMoule = false;
	private int telekinesis = 0;
	private int volMagnetic = 0;
	private String chargement = "-";
	private boolean abrite = false;
	private boolean prevention = false;
	private boolean gardeLarge = false;
	private boolean bouclierRoyal = false;
	private boolean picoDefense = false;
	private boolean tatami = false;
	private boolean tenace = false;
	private boolean saisie = false;
	private int compteurAbrite = 1;
	private boolean tailladeReussie;
	private int puisTaillade = 40;
	private int metronome = 0;
	private boolean choiced = false;
	private int encore = 0;
	private boolean tourmente = false;;
	private int taunted = 0;
	private boolean assaut = false;
	private boolean atterri = false;
	private boolean antiAir = false;
	private boolean embargo = false;
	private int enColere = 0;
	private int mania = 0;
	private int ventArriere = 0;
	private boolean torche = false;
	private int bind = 0;
	private int infeste = 0;
	private int magmaStorm = 0;
	private int fireSpin = 0;
	private int sandTomb = 0;
	private int siphon = 0;
	private int brouhaha = 0;
	private boolean matchBrouhaha = false;
	private boolean poursuiveur = false;
	private boolean focus = false;
	private int degatsRecus = -1;
	private int degatsPhysRecus = 0;
	private int degatsSpecRecus = 0;
	private int protection = 0;
	private int murLumiere = 0;
	private int runeProtect = 0;
	private boolean brume = false;
	private int antiSoin = 0;
	private boolean tendu = false;
	private boolean vampigraine = false;
	private boolean cauchemar = false;
	private boolean racines = false;
	private boolean anneauHydro = false;
	private boolean decharge = false;
	private String entrave = "no";
	private int tourEntrave = 0;
	private boolean prlvtDestin = false;
	private boolean verouillage = false;
	private int requiem = -1;
	private boolean maudit = false;
	private int voeu = 0;
	private int prescience = -1;
	private int charge = 0;
	private int stockage = 0;
	private int baille = 0;
	private boolean refletMagik = false;
	private boolean clairvoyance = false;
	private boolean oeilMiracle = false;
	private boolean nueePoudre = false;
	private boolean truant = false;

	private boolean substitute = false;
	private Pokemon clone = null;
	private boolean morphing = false;

	private boolean PDR = false;
	private int picots = 0;
	private int picsToxics = 0;
	private boolean toileGluante = false;

	private boolean canMevo = false;

	// CONSTRUCTEUR
	// Le nom du Pokemon et le numero de son set
	public Pokemon (String N, int S, String metagame, String constructeur){
		this.maison = constructeur;

		this.nom = N;
		this.espece = this.nom;
		this.set = S;
		this.nomSet = N + " [" + this.maison + " " + S + "]";
		if (!nom.equals(espece)){
			nomSet += " (" + espece + ")";
		}
		this.metagame = metagame;

		if (!nom.equals("Missingno")){
			try{
				BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/" + metagame));
				for (int i=0; i<5; ++i){
					liseur.readLine();
				}
				boolean cherche = true;
				while (cherche){
					String ligne = liseur.readLine();
					if (ligne.split(Pattern.quote("|"))[2].split(" ")[1].equals(nom)){
						utilisationEspece = Float.valueOf(ligne.split(Pattern.quote("|"))[3].split("%")[0]).floatValue();
						cherche = false;
					}
				}
				liseur.close();
			}
			catch(Exception e){
				System.out.println("Probleme avec " + nomSet);
				e.printStackTrace();
			}
		}
		else{
			Attaque noMove = new Attaque("(No Move)");
			moves = new Attaque[] {noMove, noMove, noMove, noMove};
			talent = "(No Ability)";
		}

		if (this.maison.equals("Smogon") && !nom.equals("Missingno")){
			ArrayList<String> Abilities = new ArrayList<String>();
			ArrayList<String> Items = new ArrayList<String>();
			ArrayList<String> Spreads = new ArrayList<String>();
			ArrayList<String> Moves = new ArrayList<String>();

			try{
				// Generalites sur le Pokemon
				BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/Fiches/" + nom + ".txt"));
				type = liseur.readLine().split("/");
				typeOriginal = type;
				String[] txtBaseStats = liseur.readLine().split("/");
				for (int i=0; i<6; ++i){
					baseStats[i] = Integer.valueOf(txtBaseStats[i]).intValue();
				}
				poids = Float.valueOf(liseur.readLine()).floatValue();

				String tauxSexeTxt = liseur.readLine();
				if (tauxSexeTxt.equals("asexue")){
					sexe = "asexue";
				}
				else{
					Random alea = new Random();
					if (12.5*(alea.nextInt(8)+1) <= Float.valueOf(tauxSexeTxt.split("/")[0]).floatValue()){
						sexe = "male";
					}
					else{
						sexe = "femelle";
					}
				}

				liseur.close();

				// Donnees sur les sets possibles
				liseur = new BufferedReader (new FileReader ("Database/Pokemons/moveset " + metagame));
				String ligne;

				boolean cherche = true; // cherche le pokemon
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains(this.nom + "  ") && !ligne.contains("+") && !ligne.contains("(")){
						cherche = false;
					}
				}

				cherche = true; // cherche la liste des talents
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains("Abilities")){
						cherche = false;
					}
				}
				cherche = true; // liste des talents
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains("%") && !ligne.contains(" 0.")){
						Abilities.add(ligne);
					}
					else{
						cherche = false;
					}
				}

				cherche = true; // cherche la liste des objets
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains("Items")){
						cherche = false;
					}
				}
				cherche = true; // liste des objets
				while (cherche){
					ligne = liseur.readLine();
					if (!ligne.contains("Other") && !ligne.contains(" 0.") && !ligne.contains("+------")){
						Items.add(ligne);
					}
					else{
						cherche = false;
					}
				}

				tailleItems = Items.size();

				cherche = true; // cherche la liste des spreads
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains("Spreads")){
						cherche = false;
					}
				}
				cherche = true; // liste des spreads
				while (cherche){
					ligne = liseur.readLine();
					if (!ligne.contains("Other") && !ligne.contains(" 0.") && !ligne.contains("+--")){
						Spreads.add(ligne);
					}
					else{
						cherche = false;
					}
				}
				tailleSpreads = Spreads.size();

				cherche = true; // cherche la liste des attaques
				while (cherche){
					ligne = liseur.readLine();
					if (ligne.contains("Moves")){
						cherche = false;
					}
				}
				cherche = true; // liste des attaques
				while (cherche){
					ligne = liseur.readLine();
					if (!ligne.contains("Other") && !ligne.contains("+") && !ligne.contains(" 0.")){
						if (!ligne.contains("Nothing")){
							Moves.add(ligne);
							movepool += ligne;
						}
					}
					else{
						cherche = false;
					}
				}
				tailleMovepool = Moves.size();

				liseur.close();
			}
			catch (Exception e){
				System.out.println(this.nom);
				e.printStackTrace();
			}

			NTotalSets = Abilities.size() * Items.size() * Spreads.size() * Binomial.main(Moves.size(), 4);
			if (Moves.size() < 4){
				NTotalSets = Abilities.size() * Items.size() * Spreads.size();
			}

			//			System.out.println(Abilities.size() + " , " + Abilities.toString());
			//			System.out.println(Items.size() + " , " + Items.toString());
			//			System.out.println(Spreads.size() + " , " + Spreads.toString());
			//			System.out.println(Moves.size() + " , " + Moves.toString());
			//			System.out.println("Nombre total de sets : " + NTotalSets);

			// Calcul des donnees du set
			if (set > NTotalSets){
				System.out.println("Numero du set superieur au nombre de sets (" + set + "/" + NTotalSets + ")");
				System.out.println(1/0);
			}
			utilisationSet = 100;
			int setBis = set;
			int totalBis = NTotalSets;

			talent = "(None)";
			int i=0;
			do{
				if (setBis < (i+1)*totalBis/Abilities.size()){
					talent = Abilities.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(0, Abilities.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7);
					while (talent.endsWith(" ")){
						talent = talent.substring(0, talent.length()-1);
					}
					talentOriginal = talent;
					utilisationSet *= Float.valueOf(Abilities.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(Abilities.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7, Abilities.get(i).split(Pattern.quote("| "))[1].split("%")[0].length())).floatValue() /100;
					setBis = setBis - i*totalBis/Abilities.size();
					totalBis = totalBis/Abilities.size();
				}
				++i;
			}
			while(talent.equals("(None)"));

			objet = "(None)";
			i=0;
			do{
				if (setBis < (i+1)*totalBis/Items.size()){
					objet = Items.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(0, Items.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7);
					while (objet.endsWith(" ")){
						objet = objet.substring(0, objet.length()-1);
					}
					utilisationSet *= Float.valueOf(Items.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(Items.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7, Items.get(i).split(Pattern.quote("| "))[1].split("%")[0].length())).floatValue() /100;
					setBis = setBis - i*totalBis/Items.size();
					totalBis = totalBis/Items.size();
				}
				++i;
			}
			while(objet.equals("(None)"));
			if (objet.equals("Nothing")){
				objet = "(No Item)";
			}

			String spread = "(None)";
			i=0;
			do{
				if (setBis < (i+1)*totalBis/Spreads.size()){
					spread = Spreads.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(0, Spreads.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7);
					while (spread.endsWith(" ")){
						spread = spread.substring(0, spread.length()-1);
					}
					utilisationSet *= Float.valueOf(Spreads.get(i).split(Pattern.quote("| "))[1].split("%")[0].substring(Spreads.get(i).split(Pattern.quote("| "))[1].split("%")[0].length()-7, Spreads.get(i).split(Pattern.quote("| "))[1].split("%")[0].length())).floatValue() /100;
					setBis = setBis - i*totalBis/Spreads.size();
					totalBis = totalBis/Spreads.size();
				}
				++i;
			}
			while(spread.equals("(None)"));
			nature = spread.split(":")[0];
			EVs = new int[6];
			for (int k=0; k<6; ++k){
				EVs[k] = Integer.valueOf(spread.split(":")[1].split("/")[k]).intValue();
			}

			if (Moves.size() > 4){
				for (int k=4; k>0; --k){
					moves[k-1] = new Attaque ("(No Move)");

					int Ck = k-1;
					while (Binomial.main(Ck, k) <= setBis){
						++Ck;
					}
					if (Ck > 0){
						setBis -= Binomial.main(Ck-1, k);
					}

					String nomAttaque = Moves.get(Ck-1).split(Pattern.quote("| "))[1].split("%")[0].substring(0, Moves.get(Ck-1).split(Pattern.quote("| "))[1].split("%")[0].length()-7);
					while (nomAttaque.endsWith(" ")){
						nomAttaque = nomAttaque.substring(0, nomAttaque.length()-1);
					}
					utilisationSet *= Float.valueOf(Moves.get(Ck-1).split(Pattern.quote("| "))[1].split("%")[0].substring(Moves.get(Ck-1).split(Pattern.quote("| "))[1].split("%")[0].length()-7, Moves.get(Ck-1).split(Pattern.quote("| "))[1].split("%")[0].length())).floatValue() /100;
					if (nomAttaque.startsWith("Hidden Power")){
						PC = nomAttaque.split(" ")[2];
						nomAttaque = "Hidden Power";
					}
					if (nomAttaque.startsWith("Nothing")){
						nomAttaque = "(No Move)";
					}
					else{
						moves[k-1] = new Attaque (nomAttaque);
					}
				}
			}
			else{
				int decalage = 0;
				for (int k=0; k<4; ++k){
					moves[k] = new Attaque ("(No Move)");
					try{
						String nomAttaque = Moves.get(k).split(Pattern.quote("| "))[1].split("%")[0].substring(0, Moves.get(k).split(Pattern.quote("| "))[1].split("%")[0].length()-7);
						if (nomAttaque.endsWith(" ")){
							nomAttaque = nomAttaque.substring(0, nomAttaque.length()-1);
						}
						if (nomAttaque.startsWith("Hidden Power")){
							PC = nomAttaque.split(" ")[2];
							nomAttaque = "Hidden Power";
						}
						if (nomAttaque.startsWith("Nothing")){
							++decalage;
						}
						else{
							moves[k-decalage] = new Attaque (nomAttaque);
						}
					}
					catch(Exception e){
						moves[k] = new Attaque ("(No Move)");
					}
				}
			}

			//			System.out.println(this.getNom());
			//			System.out.println(talent);
			//			System.out.println(objet);
			//			System.out.println(spread);
			//			System.out.println("Code moveset = " + setBis + " (total: " + totalBis + ")");
			//			System.out.println("Taux utilisation = " + utilisationSet);
		}

		// Calcul des stats initiales
		// Valeurs du tableau de natures
		try{
			BufferedReader liseur = new BufferedReader (new FileReader ("Database/Natures.txt"));
			boolean cherche = true;
			String ligne = "";
			while (cherche){
				ligne = liseur.readLine();
				if (ligne.startsWith(nature)){
					cherche = false;
				}
			}
			for (int i=0; i<5; ++i){
				tabNature[i] = Float.valueOf(ligne.split("	")[i+1]).floatValue();
			}
		}
		catch(NullPointerException e){
			System.out.println("Nature " + nature + " non-implementee.");
			System.out.println(1/0);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		if (!PC.equals("Dark")){
			boolean found = false;
			if (PC.equals("Bug")){
				IVs = new int[] {31,30,30,31,30,31};
				found = true;
			}
			if (PC.equals("Dragon")){
				IVs = new int[] {31,30,31,31,31,31};
				found = true;
			}
			if (PC.equals("Electric")){
				IVs = new int[] {31,31,31,30,31,31};
				found = true;
			}
			if (PC.equals("Fighting")){
				IVs = new int[] {31,31,30,30,30,30};
				found = true;
			}
			if (PC.equals("Fire")){
				IVs = new int[] {31,30,31,30,31,30};
				found = true;
			}
			if (PC.equals("Flying")){
				IVs = new int[] {30,30,30,30,30,31};
				found = true;
			}
			if (PC.equals("Ghost")){
				IVs = new int[] {31,30,31,31,30,31};
				found = true;
			}
			if (PC.equals("Grass")){
				IVs = new int[] {31,30,31,30,31,31};
				found = true;
			}
			if (PC.equals("Ground")){
				IVs = new int[] {31,31,31,30,30,31};
				found = true;
			}
			if (PC.equals("Ice")){
				IVs = new int[] {31,30,30,31,31,31};
				found = true;
			}
			if (PC.equals("Poison")){
				IVs = new int[] {31,31,30,30,30,31};
				found = true;
			}
			if (PC.equals("Psychic")){
				IVs = new int[] {31,30,31,31,31,30};
				found = true;
			}
			if (PC.equals("Rock")){
				IVs = new int[] {31,31,30,31,30,30};
				found = true;
			}
			if (PC.equals("Steel")){
				IVs = new int[] {31,31,31,31,30,31};
				found = true;
			}
			if (PC.equals("Water")){
				IVs = new int[] {31,30,30,30,31,31};
				found = true;
			}
			if (!found){
				System.out.println("PC " + PC + " non-implementee");
				System.out.println(1/0);
			}
		}
		if (moves[0].getNom().equals("Gyro Ball") || moves[1].getNom().equals("Gyro Ball") || moves[2].getNom().equals("Gyro Ball") || moves[3].getNom().equals("Gyro Ball")){
			IVs[5] = 0;
		}
		if (moves[0].getNom().equals("Trick Room") || moves[1].getNom().equals("Trick Room") || moves[2].getNom().equals("Trick Room") || moves[3].getNom().equals("Trick Room")){
			IVs[5] = 0;
		}

		statsInitiales[0] = 2*baseStats[0] + 110 + IVs[0] + EVs[0]/4;
		if (baseStats[0] == 1){
			statsInitiales[0] = 1;
		}
		statsModifiees[0] = statsInitiales[0];
		for (int i=1; i<6; ++i){
			statsInitiales[i] = (int) ((5 + 2*baseStats[i] + IVs[i] + EVs[i]/4)*tabNature[i-1]);	
		}
		statsInitiales[6] = 1;
		statsInitiales[7] = 1;
		statsInitiales[8] = 0;

		if (objet.equals("Assault Vest") && (moves[0].getPuissance() <= 0 || moves[1].getPuissance() <= 0 || moves[2].getPuissance() <= 0 || moves[3].getPuissance() <= 0)){
			competitif = false;
			commentaire += "\tVeste Assaut + attaque de statut";
		}
		
		if (this.objet.equals("Insect Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Bug") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("BlackGlasses") || this.objet.equals("Dread Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Dark") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Dragon Fang") || this.objet.equals("Draco Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Dragon") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Magnet") || this.objet.equals("Zap Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Electric") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Pixie Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Fairy") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Black Belt") || this.objet.equals("Fist Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Fighting") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Charcoal") || this.objet.equals("Flame Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Fire") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Sharp Beak") || this.objet.equals("Sky Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Flying") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Spell Tag") || this.objet.equals("Spooky Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Ghost") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Miracle Seed")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Grass") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Soft Sand") || this.objet.equals("Earth Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Ground") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Never-Melt Ice") || this.objet.equals("Icicle Plate")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Ice") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Silk Scarf") || this.objet.equals("Normal Gem")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Normal") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Poison Barb")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Poison") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Twisted Spoon") || this.objet.equals("Mind Plate") || this.objet.equals("Odd Incense")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Psychic") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}
		if (this.objet.equals("Mystic Water") || this.objet.equals("Splash Plate") || this.objet.equals("Sea Incense") || this.objet.equals("Wave Incense")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if ((moves[i].getType().equals("Water") && moves[i].getPuissance() > 0) || this.getTalent().equals("Multitype") || this.connait("Judgment")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tObjet non-pertinent";
			}
		}

		for (int i=0; i<3; ++i){
			for (int j=i+1; j<4; ++j){
				if ((moves[i].getNom().equals("Return") && moves[j].getNom().equals("Frustration")) || (moves[j].getNom().equals("Return") && moves[i].getNom().equals("Frustration"))){
					competitif = false;
					commentaire += "\tRetour + Frustration";
				}
				if (moves[i].getNom().equals(moves[j].getNom()) && !moves[i].getNom().equals("(No Move)")){
					competitif = false;
					commentaire += "\tAttaque en double";
					i=3; j=4;
				}
			}
		}

		if (tabNature[0] > 1 || EVs[1] > 0 || objet.equals("Choice Band") || objet.equals("Liechi Berry")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if (moves[i].getClasse().equals("phys")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tAttaque/objet/nature non-pertinente";
			}
		}
		if (tabNature[2] > 1 || EVs[3] > 0 || objet.equals("Choice Specs") || objet.equals("Petaya Berry")){
			boolean pertinent = false;
			for (int i=0; i<4; ++i){
				if (moves[i].getClasse().equals("spec")){
					pertinent = true;
				}
			}
			if (!pertinent){
				competitif = false;
				commentaire += "\tRepartition/objet non-pertinente";
			}
		}

		if (IVs[5]==0 && (EVs[5]>0 || tabNature[4] >= 1)){
			competitif = false;
			commentaire += "\tRepartition non-pertinente";
		}

		if (this.objet.equals("Light Clay") && !(this.moves[0].getNom().equals("Reflect") || this.moves[1].getNom().equals("Reflect") || this.moves[2].getNom().equals("Reflect") || this.moves[3].getNom().equals("Reflect") || this.moves[0].getNom().equals("Light Screen") || this.moves[1].getNom().equals("Light Screen") || this.moves[2].getNom().equals("Light Screen") || this.moves[3].getNom().equals("Light Screen"))){
			competitif = false;
			commentaire += "\tLumargile sans mur";
		}

		if (this.canMevo() && (moves[0].getNom().equals("Trick") || moves[1].getNom().equals("Trick") || moves[2].getNom().equals("Trick") || moves[3].getNom().equals("Trick"))){
			competitif = false;
			commentaire += "\tTrick + Mega-gemme";
		}

		if (this.getObjet().equals("Heat Rock") && !(this.connait("Sunny Day") || this.getTalent().equals("Drought"))){
			competitif = false;
			commentaire += "\tRoche climat inutile";
		}

		if (this.getObjet().equals("Damp Rock") && !(this.connait("Rain Dance") || this.getTalent().equals("Drizzle"))){
			competitif = false;
			commentaire += "\tRoche climat inutile";
		}
		
		if (this.getObjet().equals("Smooth Rock") && !(this.connait("Sandstorm") || this.getTalent().equals("Sand Stream"))){
			competitif = false;
			commentaire += "\tRoche climat inutile";
		}
		
		if (this.getObjet().equals("Icy Rock") && !(this.connait("Hail") || this.getTalent().equals("Snow Warning"))){
			competitif = false;
			commentaire += "\tRoche climat inutile";
		}

		if (this.getTalent().equals("Sheer Force")) {
			boolean utile = false;
			for (int i=0; i<4; ++i){
				utile = utile || moves[i].getTaux() > 0 || moves[i].getTrouille() > 0;
			}
			if (!utile){
				competitif = false;
				commentaire += "\tTalent inutile";
			}
		}
		
		if (this.getTalent().equals("Gluttony")){
			if (!(objet.equals("Liechi Berry") || objet.equals("Ganlon Berry") || objet.equals("Petaya Berry") || objet.equals("Apicot Berry") || objet.equals("Salac Berry") || objet.equals("Lansat Berry") || objet.equals("Micle Berry") || objet.equals("Custap Berry") || objet.equals("Starf Berry"))){
				competitif = false;
				commentaire += "\tTalent inutile";
			}
		}

		if (this.getTalent().equals("Iron Fist")){
			boolean utile = false;
			for (int i=0; i<4; ++i){
				utile = utile || ((moves[i].getNom().contains("Punch") || moves[i].getNom().contains("punch") || moves[i].getNom().equals("Hammer Arm") || moves[i].getNom().equals("Meteor Mash") || moves[i].getNom().equals("Sky Uppercut")) && !moves[i].getNom().equals("Sucker Punch"));
			}
			if (!utile){
				competitif = false;
				commentaire += "\tTalent inutile";
			}
		}

		if (this.objet.equals("Toxic Orb") && !(this.talent.equals("Poison Heal") || this.talent.equals("Guts") || this.talent.equals("Magic Guard") || this.talent.equals("Quick Feet") || this.talent.equals("Toxic Boost") || this.connait("Trick") || this.connait("Switcheroo") || this.connait("Fling"))){
			competitif = false;
			commentaire += "\tOrbe Toxique";
		}
		
		if (this.objet.equals("Flame Orb") && !(this.talent.equals("Flare Boost") || this.talent.equals("Guts") || this.talent.equals("Magic Guard") || this.talent.equals("Quick Feet") || this.talent.equals("Water Veil") || this.connait("Trick") || this.connait("Switcheroo") || this.connait("Fling"))){
			competitif = false;
			commentaire += "\tOrbe Flamme";
		}
		
		for (int i=0; i<4; ++i){
			if (this.moves[i].getCategorie().equals("2")){
				if (this.moves[i].getEffet().equals("Atq") && this.moves[i].getCible().equals("user")){
					boolean atqPhys = false;
					for (int j=0; j<4; ++j){
						if (this.moves[j].getClasse().equals("phys")){
							atqPhys = true;
						}
					}
					if (!atqPhys){
						competitif = false;
						commentaire += "\tBoost inutile";
					}
				}
				if (this.moves[i].getEffet().equals("SAtq")){
					boolean atqSpec = false;
					for (int j=0; j<4; ++j){
						if (this.moves[j].getClasse().equals("spec")){
							atqSpec = true;
						}
					}
					if (!atqSpec){
						competitif = false;
						commentaire += "\tBoost inutile";
					}
				}
			}
		}
		
		if (this.objet.startsWith("Choice ") && !(this.connait("Trick") || this.connait("Switcheroo"))){
			boolean attaquePassive = false;
			for (int i=0; i<4; ++i){
				if (this.moves[i].getPuissance() <= 0){
					attaquePassive = true;
				}
			}
			if (attaquePassive){
				competitif = false;
				commentaire += "\tChoice + attaque de statut";
			}
		}
		
		if (this.objet.equals("Weakness Policy")){
			boolean utile = false;
			for (int i=0; i<4; ++i){
				if (this.moves[i].getPuissance() > 0){
					utile = true;
				}
			}
			if (!utile){
				competitif = false;
				commentaire += "\tObjet inutile";
			}
		}

		if (set > 0){
			if (utilisationSet < (new Pokemon(nom, 0, metagame, constructeur)).getUtilisationSet() /100) {
				probable = false;
				commentaire += "\tSet peu probable";
			}
		}
		if (utilisationSet == 0){
			competitif = false;
		}

		this.actualiseStats(false, matchBrouhaha, false);

		this.checkImplemente();
	}

	public boolean MegaEvolution(boolean affichage){
		boolean autorise = false;

		if (espece.equals("Abomasnow") && objet.equals("Abomasite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Abomasnow !");
			}
			espece = "Mega-Abomasnow";
		}
		if (espece.equals("Absol") && objet.equals("Absolite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Absol !");
			}
			espece = "Mega-Absol";
		}
		if (espece.equals("Aerodactyl") && objet.equals("Aerodactylite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Aerodactyl !");
			}
			espece = "Mega-Aerodactyl";
		}
		if (espece.equals("Aggron") && objet.equals("Aggronite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Aggron !");
			}
			espece = "Mega-Aggron";
		}
		if (espece.equals("Alakazam") && objet.equals("Alakazite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Alakazam !");
			}
			espece = "Mega-Alakazam";
		}
		if (espece.equals("Altaria") && objet.equals("Altarianite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Altaria !");
			}
			espece = "Mega-Altaria";
		}
		if (espece.equals("Ampharos") && objet.equals("Ampharosite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Ampharos !");
			}
			espece = "Mega-Ampharos";
		}
		if (espece.equals("Audino") && objet.equals("Audinite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Audino !");
			}
			espece = "Mega-Audino";
		}
		if (espece.equals("Banette") && objet.equals("Banettite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Banette !");
			}
			espece = "Mega-Banette";
		}
		if (espece.equals("Beedrill") && objet.equals("Beedrillite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Beedrill !");
			}
			espece = "Mega-Beedrill";
		}
		if (espece.equals("Blastoise") && objet.equals("Blastoisinite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Blastoise !");
			}
			espece = "Mega-Blastoise";
		}
		if (espece.equals("Blaziken") && objet.equals("Blazikenite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Blaziken !");
			}
			espece = "Mega-Blaziken";
		}
		if (espece.equals("Camerupt") && objet.equals("Cameruptite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Camerupt !");
			}
			espece = "Mega-Camerupt";
		}
		if (espece.equals("Diancie") && objet.equals("Diancite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Diancie !");
			}
			espece = "Mega-Diancie";
		}
		if (espece.equals("Gallade") && objet.equals("Galladite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Gallade !");
			}
			espece = "Mega-Gallade";
		}
		if (espece.equals("Garchomp") && objet.equals("Garchompite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Garchomp !");
			}
			espece = "Mega-Garchomp";
		}
		if (espece.equals("Gengar") && objet.equals("Gengarite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Gengar !");
			}
			espece = "Mega-Gengar";
		}
		if (espece.equals("Glalie") && objet.equals("Glalitite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Glalie !");
			}
			espece = "Mega-Glalie";
		}
		if (espece.equals("Groudon") && objet.equals("Red Orb")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " se gonfle des forces de la nature et redevient Primo-Groudon !");
			}
			espece = "Primo-Groudon";
		}
		if (espece.equals("Kangaskhan") && objet.equals("Kangaskhanite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Kangaskhan !");
			}
			espece = "Mega-Kangaskhan";
		}
		if (espece.equals("Kyogre") && objet.equals("Blue Orb")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " se gonfle des forces de la nature et redevient Primo-Kyogre !");
			}
			espece = "Primo-Kyogre";
		}
		if (espece.equals("Latias") && objet.equals("Latiasite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Latias !");
			}
			espece = "Mega-Latias";
		}
		if (espece.equals("Latios") && objet.equals("Latiosite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Latios !");
			}
			espece = "Mega-Latios";
		}
		if (espece.equals("Lucario") && objet.equals("Lucarionite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Lucario !");
			}
			espece = "Mega-Lucario";
		}
		if (espece.equals("Metagross") && objet.equals("Metagrossite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Metagross !");
			}
			espece = "Mega-Metagross";
		}
		if (espece.equals("Mewtwo") && objet.equals("Mewtwonite X")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Mewtwo X !");
			}
			espece = "Mega-Mewtwo X";
		}
		if (espece.equals("Mewtwo") && objet.equals("Mewtwonite Y")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Mewtwo Y !");
			}
			espece = "Mega-Mewtwo Y";
		}
		if (espece.equals("Sableye") && objet.equals("Sablenite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Sableye !");
			}
			espece = "Mega-Sableye";
		}
		if (espece.equals("Salamence") && objet.equals("Salamencite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Salamence !");
			}
			espece = "Mega-Salamence";
		}
		if (espece.equals("Sceptile") && objet.equals("Sceptilite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Sceptile !");
			}
			espece = "Mega-Sceptile";
		}
		if (espece.equals("Scizor") && objet.equals("Scizorite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Scizor !");
			}
			espece = "Mega-Scizor";
		}
		if (espece.equals("Sharpedo") && objet.equals("Sharpedonite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Sharpedo !");
			}
			espece = "Mega-Sharpedo";
		}
		if (espece.equals("Steelix") && objet.equals("Steelixite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Steelix !");
			}
			espece = "Mega-Steelix";
		}
		if (espece.equals("Charizard") && objet.equals("Charizardite X")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Charizard X !");
			}
			espece = "Mega-Charizard X";
		}
		if (espece.equals("Charizard") && objet.equals("Charizardite Y")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Charizard Y !");
			}
			espece = "Mega-Charizard Y";
		}
		if (espece.equals("Lopunny") && objet.equals("Lopunnite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Lopunny !");
			}
			espece = "Mega-Lopunny";
		}
		if (espece.equals("Rayquaza") && this.connait("Dragon Ascent")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Rayquaza !");
			}
			espece = "Mega-Rayquaza";
		}
		if (espece.equals("Slowbro") && objet.equals("Slowbronite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Slowbro !");
			}
			espece = "Mega-Slowbro";
		}
		if (espece.equals("Swampert") && objet.equals("Swampertite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Swampert !");
			}
			espece = "Mega-Swampert";
		}
		if (espece.equals("Venusaur") && objet.equals("Venusaurite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Venusaur !");
			}
			espece = "Mega-Venusaur";
		}
		if (espece.equals("Gardevoir") && objet.equals("Gardevoirite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Gardevoir !");
			}
			espece = "Mega-Gardevoir";
		}
		if (espece.equals("Gyarados") && objet.equals("Gyaradosite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Gyarados !");
			}
			espece = "Mega-Gyarados";
		}
		if (espece.equals("Heracross") && objet.equals("Heracronite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Heracross !");
			}
			espece = "Mega-Heracross";
		}
		if (espece.equals("Houndoom") && objet.equals("Houndoominite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Houndoom !");
			}
			espece = "Mega-Houndoom";
		}
		if (espece.equals("Manectric") && objet.equals("Manectite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Manectric !");
			}
			espece = "Mega-Manectric";
		}
		if (espece.equals("Mawile") && objet.equals("Mawilite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Mawile !");
			}
			espece = "Mega-Mawile";
		}
		if (espece.equals("Medicham") && objet.equals("Medichamite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Medicham !");
			}
			espece = "Mega-Medicham";
		}
		if (espece.equals("Pidgeot") && objet.equals("Pidgeotite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Pidgeot !");
			}
			espece = "Mega-Pidgeot";
		}
		if (espece.equals("Pinsir") && objet.equals("Pinsirite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Pinsir !");
			}
			espece = "Mega-Pinsir";
		}
		if (espece.equals("Tyranitar") && objet.equals("Tyranitarite")){
			autorise = true;
			if (affichage){
				System.out.println("\n" + nomSet + " mega-evolue en Mega-Tyranitar !");
			}
			espece = "Mega-Tyranitar";
		}

		if (autorise){
			this.nomSet = nom + " [" + this.maison + " " + set + "]";
			if (!nom.equals(espece)){
				this.nomSet += " (" + espece + ")";
			}
			try{
				BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/Transformations/" + espece + ".txt"));
				type = liseur.readLine().split("/");
				typeOriginal = type;
				String[] txtBaseStats = liseur.readLine().split("/");
				for (int i=0; i<6; ++i){
					baseStats[i] = Integer.valueOf(txtBaseStats[i]).intValue();
				}
				talent = liseur.readLine();
				talentOriginal = talent;

				poids = Float.valueOf(liseur.readLine()).floatValue();
				liseur.close();

				this.checkImplemente();

				// Calcul des stats initiales
				for (int i=1; i<6; ++i){
					statsInitiales[i] = (int) ((2*baseStats[i] + 5 + IVs[i] + EVs[i]/4)*tabNature[i-1]);
				}
				statsInitiales[6] = 1;
				statsInitiales[7] = 1;
				statsInitiales[8] = 0;

				this.actualiseStats(false, this.matchBrouhaha, affichage);
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println(1/0);
			}
		}

		return autorise;
	}

	private void checkImplemente(){
		try{
			BufferedReader liseurObjet = new BufferedReader (new FileReader ("Database/Objets.txt"));
			boolean trouve = false;
			int compteur = 0;
			while (!trouve && compteur < 200){
				++compteur;
				if (objet.equals(liseurObjet.readLine())){
					trouve = true;
				}
			}
			if (compteur >= 200){
				System.out.println("\nObjet " + objet + " non-implemente.");
				System.out.println(1/0);
			}
			liseurObjet.close();

			if (talent.equals("Compoundeyes")){
				talent = "Compound Eyes";
				talentOriginal = talent;
			}

			BufferedReader liseurTalent = new BufferedReader (new FileReader ("Database/Talents.txt"));
			trouve = false;
			compteur = 0;
			while (!trouve && compteur < 200){
				++compteur;
				if (talent.equals(liseurTalent.readLine())){
					trouve = true;
				}
			}
			if (compteur >= 200){
				System.out.println("\nTalent " + talent + " non-implemente.");
				System.out.println(1/0);
			}
			liseurTalent.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(1/0);
		}
	}

	public boolean peutApprendre (String attaque){
		return this.movepool.contains(attaque);
	}
	
	public boolean connait (String attaque){
		return (this.moves[0].getNom().equals(attaque) || this.moves[1].getNom().equals(attaque) || this.moves[2].getNom().equals(attaque) || this.moves[3].getNom().equals(attaque));
	}

	public boolean transformation(String forme, boolean affichage){
		boolean autorise = false;

		if ((espece.equals("Aegislash") || espece.equals("Aegislash-Shield")) && talent.equals("Stance Change") && forme.equals("Epee")){
			autorise = true;
			if (affichage){
				System.out.print("\n" + nomSet + " passe en forme Epee.");
			}
			espece = "Aegislash-Blade";
		}
		if (espece.equals("Aegislash-Blade") && talent.equals("Stance Change") && forme.equals("Bouclier")){
			autorise = true;
			if (affichage){
				System.out.print("\n" + nomSet + " passe en forme Bouclier.");
			}
			espece = "Aegislash";
		}

		if (autorise){
			this.nomSet = nom + " [" + this.maison + " " + set + "]";
			if (!nom.equals(espece)){
				this.nomSet += " (" + espece + ")";
			}
			try{
				BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/Transformations/" + espece + ".txt"));
				type = liseur.readLine().split("/");
				String[] txtBaseStats = liseur.readLine().split("/");
				for (int i=0; i<6; ++i){
					baseStats[i] = Integer.valueOf(txtBaseStats[i]).intValue();
				}
				talent = liseur.readLine();
				talentOriginal = talent;
				poids = Float.valueOf(liseur.readLine()).floatValue();
				liseur.close();

				this.checkImplemente();

				// Calcul des stats initiales
				for (int i=1; i<6; ++i){
					statsInitiales[i] = (int) ((2*baseStats[i] + 5 + IVs[i] + EVs[i]/4)*tabNature[i-1]);
				}
				statsInitiales[6] = 1;
				statsInitiales[7] = 1;
				statsInitiales[8] = 0;

				this.actualiseStats(false, this.matchBrouhaha, affichage);
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println(1/0);
			}
		}
		return autorise;
	}

	public void deltaPV (int delta, boolean direct, boolean passeClone, int classe, boolean affichage){

		if (delta > 0 && antiSoin > 0){
			delta = 0;
		}

		if (talent.equals("Magic Guard") && !direct && delta < 0){
			delta = 0;
		}

		if (talent.equals("Sturdy") && statsModifiees[0] >= statsInitiales[0] && -delta >= statsInitiales[0] && (!substitute || passeClone)){
			delta = -statsInitiales[0]+1;
			if (affichage){
				System.out.println(nomSet + " tient bon grace a Fermete !");
			}
		}
		if (objet.equals("Focus Sash") && statsModifiees[0] >= statsInitiales[0] && -delta >= statsInitiales[0] && (!substitute || passeClone) && direct){
			delta = -statsInitiales[0]+1;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " tient bon grace a sa ceinture force !");
			}
		}
		if (this.tenace && -delta >= this.statsModifiees[0] && (!substitute || passeClone) && direct){
			delta = (int) (-statsModifiees[0]+1);
			if (affichage){
				System.out.println(nomSet + " est tenace face au coup.");
			}
		}
		if (objet.equals("Focus Band") && -delta >= statsInitiales[0] && (!substitute || passeClone) && direct){
			Random alea = new Random();
			if (alea.nextInt(10) == 0){
				delta = (int) (-statsModifiees[0]+1);
				if (affichage){
					System.out.println(nomSet + " tient bon grace a son Bandeau !");
				}
			}
		}

		if (statsModifiees[0] + delta > statsInitiales[0]){
			delta = (int) (statsInitiales[0] - statsModifiees[0]);
		}
		if (statsModifiees[0] + delta < 0  && (!substitute || passeClone)){
			delta = (int) (-statsModifiees[0]);
		}

		if (affichage){
			if (delta > 0 && statsModifiees[0] < statsInitiales[0]){
				//System.out.println(nomSet + " regagne " + delta + "PV (" + (int) ((float)delta/(float)statsInitiales[0]*100) + "%).");
				System.out.println(nomSet + " regagne " + ((float) (int) ((float)delta/(float)statsInitiales[0]*1000))/10 + "% de sa vie.");
			}
			if (delta < 0 && (!substitute || passeClone)){
				//System.out.println(nomSet + " perd " + -delta + "PV (" + (int) ((float)delta/(float)statsInitiales[0]*100) + "%).");
				System.out.println(nomSet + " perd " + -((float) (int) ((float)delta/(float)statsInitiales[0]*1000))/10 + "% de sa vie.");
			}
			if (substitute && delta < 0 && !passeClone){
				System.out.println("Le clone de " + nomSet + " prend les degats a sa place.");
			}
		}

		if (substitute && delta < 0 && !passeClone){
			clone.deltaPV(delta, false, false, classe, false);
			if (clone.getStatsModifiees()[0] <= 0){
				clone = null;
				substitute = false;
				if (affichage){
					System.out.println("Le clone de " + nomSet + " disparait.");
				}
			}
		}
		else{

			if (direct && delta <= 0){
				if (degatsRecus == -1){
					degatsRecus = 0;
				}
				degatsRecus -= delta;

				if (classe == 2){
					degatsPhysRecus -= delta;
					if (this.objet.equals("Kee Berry") && !this.isTendu()){
						if (affichage){
							System.out.println(this.nomSet + " mange sa baie Eka.");
						}
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(2, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
				}
				if (classe == 4){
					degatsSpecRecus -= delta;
					if (this.objet.equals("Maranga Berry") && !this.isTendu()){
						if (affichage){
							System.out.println(this.nomSet + " mange sa Baie Maranga.");
						}
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(4, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
				}
			}

			statsModifiees[0] += delta;
			if (statsModifiees[0] > statsInitiales[0]){
				statsModifiees[0] = statsInitiales[0];
			}

			if (statsModifiees[0] <= 0){
				this.setStatut("KO", true, true, false, this.matchBrouhaha, affichage);
			}
			else{
				if (objet.equals("Berry Juice") && statsModifiees[0] <= statsInitiales[0]/2){
					if(affichage){
						System.out.println(nomSet + " boit son Jus de Baie !");
					}
					this.setObjet("(No Item)", true, true, affichage);
					this.deltaPV(20, false, true, 0, affichage);
				}
				if (!tendu){
					if (objet.equals("Sitrus Berry") && statsModifiees[0] <= statsInitiales[0]/2){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Citrus !");
						}
						this.setObjet("(No Item)", true, true, affichage);
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Mago Berry") && statsModifiees[0] <= statsInitiales[0]/2){
						if(affichage){
							System.out.println(nomSet + " mange sa Baie Mago !");
						}
						this.setObjet("(No Item)", true, true, affichage);
						this.deltaPV((int)statsInitiales[0]/8, false, true, 0, affichage);
						if (this.tabNature[4] < 1){
							this.setStatut("Conf", true, true, true, matchBrouhaha, affichage);
						}
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int) statsInitiales[0] / 4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Liechi Berry") && (statsModifiees[0] <= statsInitiales[0]/4 || (talent.equals("Gluttony") && statsModifiees[0] <= statsInitiales[0]/2))){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Lichi !");
						}	
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(1, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Petaya Berry") && (statsModifiees[0] <= statsInitiales[0]/4 || (talent.equals("Gluttony") && statsModifiees[0] <= statsInitiales[0]/2))){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Pitaye !");
						}	
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(3, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Apicot Berry") && (statsModifiees[0] <= statsInitiales[0]/4 || (talent.equals("Gluttony") && statsModifiees[0] <= statsInitiales[0]/2))){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Abriko !");
						}	
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(4, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Salac Berry") && (statsModifiees[0] <= statsInitiales[0]/4 || (talent.equals("Gluttony") && statsModifiees[0] <= statsInitiales[0]/2))){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Sailak!");
						}	
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(5, 1, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
					if (objet.equals("Starf Berry") && statsModifiees[0] <= statsInitiales[0]/4){
						if(affichage){
							System.out.println(nomSet + " mange sa baie Frista!");
						}	
						Random alea = new Random();
						this.setObjet("(No Item)", true, true, affichage);
						this.boost(1+alea.nextInt(7), 2, true, true, affichage);
						if (this.talent.equals("Cheek Pouch")){
							if (affichage){
								System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
							}
							this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
						}
					}
				}
				if (objet.equals("Lansat Berry") && (statsModifiees[0] <= statsInitiales[0]/4 || (talent.equals("Gluttony") && statsModifiees[0] <= statsInitiales[0]/2))){
					if(affichage){
						System.out.println(nomSet + " mange sa Baie Lansat !");
					}
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(8, 2, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (direct && delta < 0){
					focus = false;
				}
			}
		}
	}

	public void fixePV (int PV, boolean passeClone, boolean affichage){

		if (substitute && !passeClone){
			clone.fixePV(PV, false, affichage);
		}
		else{
			this.statsModifiees[0] = PV;
			if (statsModifiees[0] > statsInitiales[0]){
				statsModifiees[0] = statsInitiales[0];
			}
		}

		if (statsModifiees[0] <= 0){
			this.setStatut("KO", true, true, false, this.matchBrouhaha, affichage);
		}
		else{
			if (objet.equals("Berry Juice") && statsModifiees[0] <= statsInitiales[0]/2){
				if(affichage){
					System.out.println(nomSet + " boit son Jus de Baie !");
				}
				this.setObjet("(No Item)", true, true, affichage);
				this.deltaPV(20, false, true, 0, affichage);
			}
			if (!tendu){
				if (objet.equals("Sitrus Berry") && statsModifiees[0] <= statsInitiales[0]/2){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Citrus!");
					}
					this.setObjet("(No Item)", true, true, affichage);
					this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Mago Berry") && statsModifiees[0] <= statsInitiales[0]/2){
					if(affichage){
						System.out.println(nomSet + " mange sa Baie Mago !");
					}
					this.setObjet("(No Item)", true, true, affichage);
					this.deltaPV((int)statsInitiales[0]/8, false, true, 0, affichage);
					if (this.tabNature[5] < 1){
						this.setStatut("Conf", true, true, true, matchBrouhaha, affichage);
					}
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Liechi Berry") && statsModifiees[0] <= statsInitiales[0]/4){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Lichi!");
					}	
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(1, 1, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Petaya Berry") && statsModifiees[0] <= statsInitiales[0]/4){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Pitaye!");
					}	
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(3, 1, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Apicot Berry") && statsModifiees[0] <= statsInitiales[0]/4){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Abriko !");
					}	
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(4, 1, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Salac Berry") && statsModifiees[0] <= statsInitiales[0]/4){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Sailak!");
					}	
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(5, 1, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
				if (objet.equals("Starf Berry") && statsModifiees[0] <= statsInitiales[0]/4){
					if(affichage){
						System.out.println(nomSet + " mange sa baie Frista!");
					}	
					Random alea = new Random();
					this.setObjet("(No Item)", true, true, affichage);
					this.boost(1+alea.nextInt(7), 2, true, true, affichage);
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
			}
		}
	}

	private void boost(int stat, int delta, boolean passeClone, boolean selfInduced, boolean affichage) {

		if (substitute && !passeClone){
			if (affichage){
				System.out.println("Le clone bloque l'alteration de stats.");
			}
			delta = 0;
		}
		if ((talent.equals("Clear Body") || talent.equals("White Smoke")) && delta < 0 && !selfInduced){
			if (affichage){
				System.out.println("Corps Sain bloque la baisse de stat.");
			}
			delta = 0;
		}
		if (brume && delta < 0 && !selfInduced){
			if (affichage){
				System.out.println("Brume bloque la baisse de stat.");
			}
			delta = 0;
		}
		if (talent.equals("Hyper Cutter") && stat == 1 && delta < 0 && !selfInduced){
			if (affichage){
				System.out.println("Hyper Cutter bloque la baisse d'attaque.");
			}
			delta = 0;
		}
		if (talent.equals("Big Pecks") && stat == 2 && delta < 0 && !selfInduced){
			if (affichage){
				System.out.println("Coeur de Coq bloque la baisse de defense.");
			}
			delta = 0;
		}
		if (talent.equals("Keen Eye") && stat == 6 && delta < 0 && !selfInduced){
			if (affichage){
				System.out.println("Regard Vif bloque la baisse de precision.");
			}
			delta = 0;
		}
		if (this.getTalent().equals("Contrary")){
			delta = -delta;
		}
		if (this.getTalent().equals("Simple")){
			delta *= 2;
		}
		int varReelle = -statsBoosts[stat];
		statsBoosts[stat] = statsBoosts[stat] + delta;
		if (stat == 8){
			if (statsBoosts[stat] > 3){statsBoosts[stat] = 3;}
			if (statsBoosts[stat] < 0){statsBoosts[stat] = 0;}
			puissance = true;
		}
		else{
			if (statsBoosts[stat] > 6){statsBoosts[stat] = 6;}
			if (statsBoosts[stat] < -6){statsBoosts[stat] = -6;}
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
		varReelle += statsBoosts[stat];

		if (affichage && varReelle != 0){
			if (stat == 1){System.out.print("L'attaque de " + nomSet);}
			if (stat == 2){System.out.print("La defense de " + nomSet);}
			if (stat == 3){System.out.print("L'attaque speciale de " + nomSet);}
			if (stat == 4){System.out.print("La defense speciale de " + nomSet);}
			if (stat == 5){System.out.print("La vitesse de " + nomSet);}
			if (stat == 6){System.out.print("La precision de " + nomSet);}
			if (stat == 7){System.out.print("L'esquive de " + nomSet);}
			if (stat == 8){System.out.print("Le taux de critique de " + nomSet);}

			if (delta == 12){System.out.println(" augmente au maximum !! (" + statsBoosts[stat] + ")");}
			if (varReelle == 4){System.out.println(" augmente vraiment enormement ! (" + statsBoosts[stat] + ")");}
			if (varReelle == 3){System.out.println(" augmente enormement ! (" + statsBoosts[stat] + ")");}
			if (varReelle == 2){System.out.println(" augmente beaucoup ! (" + statsBoosts[stat] + ")");}
			if (varReelle == 1){System.out.println(" augmente. (" + statsBoosts[stat] + ")");}
			if (varReelle == -1){System.out.println(" diminue. (" + statsBoosts[stat] + ")");}
			if (varReelle == -2){System.out.println(" diminue beaucoup. (" + statsBoosts[stat] + ")");}

			if (varReelle == 0){
				if (delta > 0){
					System.out.println(" ne peut pas augmenter.");
				}
				if (delta < 0){
					System.out.println(" ne peut pas diminuer.");
				}
			}
		}
	}

	public void multiBoost (String[] effets, int[] variations, boolean passeClone, boolean selfInduced, boolean affichage){
		for (int i=0; i<effets.length; ++i){
			if (effets[i].equals("Atq")){
				this.boost(1, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("Def")){
				this.boost(2, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("SAtq")){
				this.boost(3, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("SDef")){
				this.boost(4, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("Vit")){
				this.boost(5, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("Prec")){
				this.boost(6, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("Esq")){
				this.boost(7, variations[i], passeClone, selfInduced, affichage);
			}
			if (effets[i].equals("Crit")){
				this.boost(8, variations[i], passeClone, selfInduced, affichage);
			}
		}

		if (talent.equals("Competitive") && !selfInduced){
			boolean augmenter = false;
			for (int i=0; i<variations.length; ++i){
				if (variations[i] < 0){
					augmenter = true;
				}
			}
			if (augmenter){
				if (affichage){
					System.out.println(nomSet + " veut etre competitif !");
				}
				this.boost(3, 2, true, true, affichage);
			}
		}
		if (talent.equals("Defiant") && !selfInduced){
			boolean augmenter = false;
			for (int i=0; i<variations.length; ++i){
				if (variations[i] < 0){
					augmenter = true;
				}
			}
			if (augmenter){
				if (affichage){
					System.out.println(nomSet + " s'acharne !");
				}
				this.boost(1, 2, true, true, affichage);
			}
		}

		if (objet.equals("White Herb")){
			for (int i=1; i<=7; ++i){
				if (this.getStatsBoosts()[i] < 0){
					if (objet.equals("White Herb") && affichage){
						System.out.println("L'herbe blanche de " + nomSet + " restaure les stats baissees.");
					}
					this.boost(i, -this.getStatsBoosts()[i], true, true, false);
					this.setObjet("(No Item)", true, true, affichage);
				}
			}
		}

		this.actualiseStats(false, this.matchBrouhaha, affichage);

	}

	public void actualiseStats(boolean infiltration, boolean mBrou, boolean affichage){
		assaut = false;
		this.matchBrouhaha = mBrou;
		
		if (briseMoule && (this.talent.equals("Battle Armor") || this.talent.equals("Big Pecks") || this.talent.equals("Clear Body") || this.talent.equals("Contrary") || this.talent.equals("Damp") || this.talent.equals("Dry Skin") || this.talent.equals("Filter") || this.talent.equals("Flash Fire") || this.talent.equals("Flower Gift") || this.talent.equals("Heat Proof") || this.talent.equals("Heavy Metal") || this.talent.equals("Hyper Cutter") || this.talent.equals("Immunity") || this.talent.equals("Inner Focus") || this.talent.equals("Insomnia") || this.talent.equals("Keen Eye") || this.talent.equals("Leaf Guard") || this.talent.equals("Levitate") || this.talent.equals("Light Metal") || this.talent.equals("Lightningrod") || this.talent.equals("Limber") || this.talent.equals("Magic Bounce") || this.talent.equals("Magma Armor") || this.talent.equals("Marvel Scale") || this.talent.equals("Motor Drive") || this.talent.equals("Multiscale") || this.talent.equals("Oblivious") || this.talent.equals("Own Tempo") || this.talent.equals("Sand Veil") || this.talent.equals("Shell Armor") || this.talent.equals("Shield Dust") || this.talent.equals("Simple") || this.talent.equals("Snow Cloak") || this.talent.equals("Solid Rock") || this.talent.equals("Soundproof") || this.talent.equals("Sticky Hold") || this.talent.equals("Storm Drain") || this.talent.equals("Sturdy") || this.talent.equals("Suction Cups") || this.talent.equals("Tangled Feet") || this.talent.equals("Telepathy") || this.talent.equals("Thick Fat") || this.talent.equals("Unaware") || this.talent.equals("Vital Spirit") || this.talent.equals("Volt Absorb") || this.talent.equals("Water Absorb") || this.talent.equals("Water Veil") || this.talent.equals("White Smoke") || this.talent.equals("Wonder Guard") || this.talent.equals("Wonder Skin"))){
			this.talent += " (brise)";
		}
		
		if (!briseMoule){
			this.briseMoule = false;
		}

		for (int i=0; i<8; ++i){
			statsModifieurs[i] = 1;
		}
		statsModifieurs[8] = 0;

		if (statut.equals("Brul") && !talent.equals("Guts")){
			statsModifieurs[1] = statsModifieurs[1] /2;
		}
		if (statut.equals("Para") && !talent.equals("Quick Feet")){
			statsModifieurs[5] = statsModifieurs[5] /4;
		}

		if (objet.equals("DeepSeaTooth") && this.espece.equals("Clamperl")){
			statsModifieurs[3] = (float) (statsModifieurs[3] *2);
		}
		if (objet.equals("DeepSeaScale") && this.espece.equals("Clamperl")){
			statsModifieurs[4] = (float) (statsModifieurs[4] *2);
		}
		if (objet.equals("Light Ball") && this.espece.equals("Pikachu")){
			statsModifieurs[1] = (float) (statsModifieurs[1] *2);
			statsModifieurs[3] = (float) (statsModifieurs[3] *2);
		}
		if (objet.equals("Thick Club") && (this.espece.equals("Marowak") || this.espece.equals("Cubone"))){
			statsModifieurs[1] = (float) (statsModifieurs[1] *2);
		}
		if (objet.equals("Choice Band")){
			statsModifieurs[1] = (float) (statsModifieurs[1] *1.5);
		}
		if (objet.equals("Eviolite")){
			if (this.espece.equals("Chansey") || this.espece.equals("Combusken") || this.espece.equals("Doublade") || this.espece.equals("Dusclops") || this.espece.equals("Eelektrik") || this.espece.equals("Ferroseed") || this.espece.equals("Fletchinder") || this.espece.equals("Floette") || this.espece.equals("Frogadier") || this.espece.equals("Gabite") || this.espece.equals("Gligar") || this.espece.equals("Golbat") || this.espece.equals("Klang") || this.espece.equals("Krokorok") || this.espece.equals("Gothorita") || this.espece.equals("Lickitung") || this.espece.equals("Magneton") || this.espece.equals("Metang") || this.espece.equals("Misdreavus") || this.espece.equals("Monferno") || this.espece.equals("Murkrow") || this.espece.equals("Pawniard") || this.espece.equals("Piloswine") || this.espece.equals("Porygon") || this.espece.equals("Porygon2") || this.espece.equals("Rhydon") || this.espece.equals("Scyther") || this.espece.equals("Skorupi") || this.espece.equals("Tangela") || this.espece.equals("Vullaby") || this.espece.equals("Wartortle")){
				statsModifieurs[2] = (float) (statsModifieurs[2] *1.5);
				statsModifieurs[4] = (float) (statsModifieurs[4] *1.5);
			}
			else{
				if (! (this.espece.equals("Abomasnow") || this.espece.equals("Azelf") || this.espece.equals("Basculin") || this.espece.equals("Chandelure") || this.espece.equals("Chatot") || this.espece.equals("Gothitelle") || this.espece.equals("Jirachi") || this.espece.equals("Klefki") || this.espece.equals("Latios") || this.espece.equals("Malamar") || this.espece.equals("Nidoqueen") || this.espece.equals("Poliwrath") || this.espece.equals("Quagsire") || this.espece.equals("Raticate") || this.espece.startsWith("Rotom") || this.espece.equals("Sableye") || this.espece.equals("Togekiss") || this.espece.equals("Slurpuff") || this.espece.equals("Smeargle") || this.espece.equals("Swanna") || this.espece.equals("Venomoth") || this.espece.equals("Xatu"))){
					System.out.println("Eviolite sur " + this.espece + " ???");
				}
			}
		}

		if (objet.equals("Metal Powder") && this.espece.equals("Ditto")){
			statsModifieurs[2] = (float) (statsModifieurs[2] *2);
			statsModifieurs[4] = (float) (statsModifieurs[4] *2);
		}
		if (objet.equals("Choice Specs")){
			statsModifieurs[3] = (float) (statsModifieurs[3] *1.5);
		}
		if (objet.equals("Assault Vest")){
			statsModifieurs[4] = (float) (statsModifieurs[4] *1.5);
			assaut = true;
		}
		if (objet.equals("Choice Scarf")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *1.5);
		}
		if (objet.equals("Iron Ball") || objet.equals("Macho Brace")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *0.5);
			this.setAntiAir(false);
		}
		if (objet.equals("Quick Powder") && this.espece.equals("Ditto")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}
		if (objet.equals("Wide Lens")){
			statsModifieurs[6] = (float) (statsModifieurs[6] *1.1);
		}
		if (objet.equals("Bright Powder") || objet.equals("BrightPowder")){
			statsModifieurs[7] = (float) (statsModifieurs[7] *1.1);
		}

		if (talent.equals("Defeatist") && statsModifiees[0] <= (int) (statsInitiales[0]/2)){
			statsModifieurs[1] = (float) (statsModifieurs[1] /2);
			statsModifieurs[3] = (float) (statsModifieurs[3] /2);
		}
		if (statut.equals("Brul") && talent.equals("Flare Boost")){
			statsModifieurs[3] = (float) (statsModifieurs[3] *1.5);
		}
		if ((statut.equals("GPsn") || statut.equals("Psn")) && talent.equals("Toxic Boost")){
			statsModifieurs[1] = (float) (statsModifieurs[1] *1.5);
		}
		if (!statut.equals("OK") && talent.equals("Guts")){
			statsModifieurs[1] = (float) (statsModifieurs[1] *1.5);
		}
		if (!statut.equals("OK") && talent.equals("Marvel Scale")){
			statsModifieurs[2] = (float) (statsModifieurs[2] *1.5);
		}
		if (talent.equals("Huge Power") || talent.equals("Pure Power")){
			statsModifieurs[1] = (float) (statsModifieurs[1] *2);
		}
		if (talent.equals("Fur Coat")){
			statsModifieurs[2] = (float) (statsModifieurs[2] *2);
		}
		if (!statut.equals("OK") && talent.equals("Quick Feet")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *1.5);
		}
		if (talent.equals("Unburden") && !objetConsomme.equals("(No Item)")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}
		if (talent.equals("Compoundeyes") || talent.equals("Compound Eyes")){
			statsModifieurs[6] = (float) (statsModifieurs[6] *1.3);
		}

		if (meteo.equals("grele") && talent.equals("Snow Cloak")){
			statsModifieurs[7] = (float) (statsModifieurs[7] *1.25);
		}
		if (meteo.equals("pluie") && talent.equals("Swift Swim")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}
		if (meteo.endsWith("soleil") && talent.equals("Solar Power")){
			statsModifieurs[3] = (float) (statsModifieurs[3] *1.5);
		}
		if (meteo.endsWith("soleil") && talent.equals("Chlorophyll")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}
		if (meteo.equals("tempete de sable") && talent.equals("Sand Rush")){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}
		if (meteo.equals("tempete de sable") && (type[0].equals("Rock") || type[1].equals("Rock")) && !talent.equals("Cloud Nine") && !talent.equals("Air Lock")){
			statsModifieurs[4] = (float) (statsModifieurs[4] *1.5);
		}
		if (meteo.equals("tempete de sable") && talent.equals("Sand Veil")){
			statsModifieurs[7] = (float) (statsModifieurs[7] *1.25);
		}
		if (meteo.equals("grele") && talent.equals("Snow Cloak")){
			statsModifieurs[7] = (float) (statsModifieurs[7] *1.25);
		}

		if (protection > 0 && !infiltration){
			statsModifieurs[2] = (float) (statsModifieurs[2] *2);
		}
		if (murLumiere > 0 && !infiltration){
			statsModifieurs[4] = (float) (statsModifieurs[4] *2);
		}
		if (ventArriere > 0){
			statsModifieurs[5] = (float) (statsModifieurs[5] *2);
		}

		if (gravite){
			statsModifieurs[7] = (float) (statsModifieurs[7] *0.6);
		}
		if (telekinesis > 0){
			statsModifieurs[7] = 0;
		}

		if ((clairvoyance || oeilMiracle) && statsBoosts[7] > 0){
			statsBoosts[7] = 0;
		}

		for (int i=1; i<8; ++i){
			int piedsConfus = 0;
			if (i == 7 && this.talent.equals("Tangled Feet") && confus > 0 && statsBoosts[i] <= 5){
				piedsConfus = 1;
				if (affichage){
					System.out.println("Niveau d'esquive de " + this.getNom() + " : " + this.getStatsBoosts()[7]);
				}
			}
			if (statsBoosts[i] > 0){
				statsModifiees[i] = statsInitiales[i] * statsModifieurs[i] * (2+statsBoosts[i]+piedsConfus)/2;
			}
			else{
				statsModifiees[i] = statsInitiales[i] * statsModifieurs[i] * 2/(2-statsBoosts[i]+piedsConfus);
			}
		}

		statsBoosts[8] = 0;
		if (puissance){
			++statsBoosts[8];
		}
		if (talent.equals("Super Luck")){
			++statsBoosts[8];
		}
		if (objet.equals("Scope Lens") || objet.equals("Razor Claw")){
			++statsBoosts[8];
		}
		if (objet.equals("Lucky Punch") && this.espece.equals("Chansey")){
			statsBoosts[8] += 2;
		}
		statsModifiees[8] = statsBoosts[8];

		poidsModifie = (float) (poids * Math.pow(2, poidsModifieur));
		if (talent.equals("Light Metal")){
			poidsModifie *= 0.5;
		}
		if (talent.equals("Heavy Metal")){
			poidsModifie *= 2;
		}
		if (objet.equals("Float Stone")){
			poidsModifie *= 0.5;
		}
		
		if (nom.equals("Arceus") && talent.equals("Multitype") && objet.contains(" Plate")){
			boolean found = false;
			if (objet.startsWith("Spooky Plate")){
				this.transformation("Arceus-Ghost", false);
				found = true;
			}
			if (!found){
				System.out.println("Plaque non-trouvée : " + this.objet);
				System.out.println(1/0);
			}
		}

		if (statut.equals("Gel") && meteo.endsWith("soleil") && !talent.equals("Cloud Nine") && !talent.equals("Air Lock")){
			statut = "OK";
			if (affichage){
				System.out.println(nomSet + " est degele grace a la chaleur du Soleil!");
			}
		}
		if (statut.equals("Somm") && terrain.equals("Electric") && !this.getType()[0].equals("Flying") && !this.getType()[1].equals("Flying") && !this.talent.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0){
			statut = "OK";
			if (affichage){
				System.out.println(nomSet + " se reveille !");
			}
		}
		if (statut.equals("Somm") && (talent.equals("Insomnia") || talent.equals("Vital Spirit"))){
			statut = "OK";
			if (affichage){
				System.out.println(nomSet + " se reveille !");
			}
		}
		if (statut.equals("Somm") && matchBrouhaha){
			statut = "OK";
			if (affichage){
				System.out.println(nomSet + " se reveille !");
			}
		}
		
		if (!statut.equals("Somm")){
			cauchemar = false;
		}

		if (this.objet.equals("Leppa Berry")){
			for (int i=0; i<4; ++i){
				if (this.moves[i].getPP() <= 0){
					this.setObjet("(No Item)", true, true, affichage);
					this.moves[i].setPP(10);
					if (new Attaque(this.moves[i].getNom()).getPP() < 10){
						this.moves[i].setPP(new Attaque(this.moves[i].getNom()).getPP());
					}
					if (this.isMorphing()){
						this.moves[i].setPP(5);
					}
					if (affichage){
						System.out.println(this.nomSet + " mange sa baie Mepo et restaure les PP de " + this.moves[i].getNom());
					}
					i = 4;
					if (this.talent.equals("Cheek Pouch")){
						if (affichage){
							System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
						}
						this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
					}
				}
			}
		}
	}

	public void setStatut(String alter, boolean selfInflicted, boolean passeClone, boolean infiltration, boolean mBrou, boolean affichage) {

		this.matchBrouhaha = mBrou;

		Random alea = new Random();

		if (alter.equals("TriAtk")){
			int alt = alea.nextInt(3);
			if (alt == 0){alter = "Para";}
			if (alt == 1){alter = "Brul";}
			if (alt == 2){alter = "Gel";}
		}

		if (alter.equals("UFO")){
			int alt = alea.nextInt(3);
			if (alt == 0){this.setTrouille(affichage);}
			if (alt == 1){alter = "Conf";}
			if (alt == 2){alter = "Amour";}
		}

		if (statut.equals("OK") && alter.equals("OK") && affichage){
			System.out.println(nomSet + " va deja bien.");
		}
		if (!statut.equals("OK") && alter.equals("OK") && affichage){
			System.out.println(nomSet + " guerit de son probleme de statut.");
		}
		if (alter.equals("OK")){
			statut = "OK";
			tourStatut = 0;
		}

		if (alter.equals("KO")){
			statut = "KO";
			if (affichage){
				System.out.println(nomSet + " est KO.");
			}
		}

		if ((runeProtect > 0 || terrain.equals("Misty")) && !selfInflicted && !infiltration && !alter.equals("Bind") && !alter.startsWith("FirSpin") && !alter.startsWith("Infest") && !alter.startsWith("MgmStrm") && !alter.startsWith("SandTomb") && !alter.startsWith("Siphon")){
			alter = "OK";
			if (affichage){
				System.out.println(nomSet + " est protege contre les statuts.");
			}
		}

		if (alter.equals("Amour") && (!substitute || passeClone)){
			if (this.getTalent().equals("Oblivious") || this.getTalent().equals("Aroma Veil")){
				if (affichage){
					System.out.println(this.getNom() + " est protégé contre l'amour.");
				}
			}
			else{
				if (this.amoureux){
					if (affichage){
						System.out.println(this.getNom() + " est deja amoureux.");
					}
				}
				else{
					this.amoureux = true;
					if (affichage){
						System.out.println(this.getNom() + " tombe amoureux.");
					}
				}
			}
		}

		if (alter.equals("Conf") && (!substitute || passeClone)){
			if (this.getTalent().equals("Own Tempo")){
				if (affichage){
					System.out.println("Tempo Perso protege " + this.getNom() + " de la confusion.");
				}
			}
			else{
				if (this.confus <= 0){
					this.setConfus(2+alea.nextInt(4));
					if (affichage){
						System.out.println(this.getNom() + " devient confus.");
					}
				}
				else{
					if (affichage){
						System.out.println(nomSet + " est deje confus.");
					}
				}
			}
		}

		if (alter.equals("Repos")){
			if (statsModifiees[0] < statsInitiales[0] || !(statut.equals("Somm") || statut.equals("OK"))){
				if ((talent.equals("Insomnia") || talent.equals("Vital Spirit")) || (terrain.equals("Electric") && !this.getType()[0].equals("Flying") && !this.getType()[1].equals("Flying") && !this.talent.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0) || matchBrouhaha){
					if (affichage){
						System.out.println(this.getNom() + " ne peut pas s'endormir.");
					}
				}
				else{
					statut = "Somm";
					tourStatut = 2;
					if (this.talent.equals("Early Bird")){
						tourStatut /= 2;
					}
					statsModifiees[0] = statsInitiales[0];
					if (affichage){
						System.out.println(nomSet + " s'endort et regagne toute sa vie.");
					}
				}
			}
			else{
				if (affichage){
					System.out.println("Mais cela echoue!");
				}
			}
		}

		if (alter.startsWith("Bind") && bind <= 0 && (!substitute || passeClone)){
			bind = 4 + alea.nextInt(2);
			if (alter.endsWith("5")){
				bind = 5;
			}
			if (affichage){
				System.out.println(this.getNom() + " est piege dans l'étreinte.");
			}
		}
		if (alter.startsWith("FirSpin") && fireSpin <= 0 && (!substitute || passeClone)){
			fireSpin = 4 + alea.nextInt(2);
			if (alter.endsWith("5")){
				fireSpin = 5;
			}
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon de flammes.");
			}
		}
		if (alter.equals("Infest") && infeste <= 0 && (!substitute || passeClone)){
			infeste = 4 + alea.nextInt(2);
			if (affichage){
				System.out.println(this.getNom() + " est infeste.");
			}
		}
		if (alter.equals("Infest5") && infeste <= 0 && (!substitute || passeClone)){
			infeste = 5;
			if (affichage){
				System.out.println(this.getNom() + " est infeste.");
			}
		}
		if (alter.equals("MgmStrm") && magmaStorm <= 0 && (!substitute || passeClone)){
			magmaStorm = 4 + alea.nextInt(2);
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon de lave.");
			}
		}
		if (alter.equals("MgmStrm5") && magmaStorm <= 0 && (!substitute || passeClone)){
			magmaStorm = 5;
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon de lave.");
			}
		}
		if (alter.equals("SandTomb") && sandTomb <= 0 && (!substitute || passeClone)){
			sandTomb = 4 + alea.nextInt(2);
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon de sable.");
			}
		}
		if (alter.equals("SandTomb5") && sandTomb <= 0 && (!substitute || passeClone)){
			sandTomb = 5;
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon de sable.");
			}
		}
		if (alter.equals("Siphon") && siphon <= 0 && (!substitute || passeClone)){
			siphon = 4 + alea.nextInt(2);
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon.");
			}
		}
		if (alter.equals("Siphon5") && siphon <= 0 && (!substitute || passeClone)){
			siphon = 5;
			if (affichage){
				System.out.println(this.getNom() + " est piege dans un tourbillon.");
			}
		}

		if (statut.equals("OK") && (!substitute || passeClone)){

			if (alter.equals("Brul") && !type[0].equals("Fire") && !type[1].equals("Fire") && !this.talent.equals("Water Veil")){
				this.statut = "Brul";
				if (affichage){
					System.out.println(nomSet + " est brule.");
				}
			}
			if (alter.equals("Brul") && (type[0].equals("Fire") || type[1].equals("Fire"))){
				if (affichage){
					System.out.println(nomSet + " ne peut pas etre brule.");
				}
			}
			if (alter.equals("Brul") && this.talent.equals("Water Veil")){
				if (affichage){
					System.out.println("Ignifuvoile de " + nomSet + " le protege de la brulure.");
				}
			}

			if (alter.equals("Gel") && !type[0].equals("Ice") && !type[1].equals("Ice") && (!meteo.endsWith("soleil") || talent.equals("Cloud Nine") || talent.equals("Air Lock"))){
				this.statut = "Gel";
				if (affichage){
					System.out.println(nomSet + " est gele.");
				}
			}
			if (alter.equals("Gel") && (type[0].equals("Ice") || type[1].equals("Ice") || (meteo.endsWith("soleil") && !talent.equals("Cloud Nine") && !talent.equals("Air Lock")) || talent.equals("Magma Armor"))){
				if (affichage){
					System.out.println(nomSet + " ne peut pas etre gele.");
				}
			}

			if (alter.equals("Para") && !type[0].equals("Electric") && !type[1].equals("Electric") && !this.talent.equals("Limber")){
				this.statut = "Para";
				if (affichage){
					System.out.println(nomSet + " est paralyse.");
				}
			}
			if (alter.equals("Para") && (type[0].equals("Electric") || type[1].equals("Electric"))){
				if (affichage){
					System.out.println(nomSet + " ne peut pas etre paralyse.");
				}
			}
			if (alter.equals("Para") && this.talent.equals("Limber")){
				if (affichage){
					System.out.println("Echauffement de " + nomSet + " le protege de la paralysie.");
				}
			}

			if (alter.equals("Psn") && !type[0].equals("Poison") && !type[1].equals("Poison") && !type[0].equals("Steel") && !type[1].equals("Steel") && !talent.equals("Immunity")){
				this.statut = "Psn";
				if (affichage){
					System.out.println(nomSet + " est empoisonne.");
				}
			}
			if (alter.equals("Psn") && (type[0].equals("Poison") || type[1].equals("Poison") || type[0].equals("Steel") || type[1].equals("Steel") || talent.equals("Immunity"))){
				if (affichage){
					System.out.println(nomSet + " ne peut pas etre empoisonne.");
				}
			}

			if (alter.equals("GPsn") && !type[0].equals("Poison") && !type[1].equals("Poison") && !type[0].equals("Steel") && !type[1].equals("Steel") && !talent.equals("Immunity")){
				this.statut = "GPsn";
				if (affichage){
					System.out.println(nomSet + " est gravement empoisonne.");
				}
			}
			if (alter.equals("GPsn") && (type[0].equals("Poison") || type[1].equals("Poison") || type[0].equals("Steel") || type[1].equals("Steel") || talent.equals("Immunity"))){
				if (affichage){
					System.out.println(nomSet + " ne peut pas être empoisonné.");
				}
			}

			if (alter.equals("Somm")){
				if ((terrain.equals("Electric") && !this.getType()[0].equals("Flying") && !this.getType()[1].equals("Flying") && !this.talent.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0) || (talent.equals("Insomnia") || talent.equals("Vital Spirit")) || matchBrouhaha){
					if (affichage){
						System.out.println(nomSet + " ne peut etre endormi.");
					}
				}
				else{
					statut = "Somm";
					tourStatut = 1+alea.nextInt(5);
					if (this.talent.equals("Early Bird")){
						tourStatut /= 2;
					}
					if (affichage){
						System.out.println(nomSet + " s'endort.");
					}
				}
			}
		}
		else{
			if (!alter.equals("KO") && !statut.equals("KO") && !alter.equals("Repos") && !alter.equals("Conf") && !alter.equals("Bind") && !alter.startsWith("FirSpin") && !alter.startsWith("Infest") && !alter.startsWith("MgmStrm") && !alter.startsWith("SandTomb") && !alter.startsWith("Siphon")){
				if (affichage){
					System.out.println("Ca n'affecte pas " + nomSet + ".");
				}
			}
		}

		if (statut.equals("Somm") && objet.equals("Chesto Berry") && !tendu){
			statut = "OK";
			tourStatut = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " mange sa baie Maron et se reveille !");
			}
			if (this.talent.equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
			}
		}
		if (statut.equals("Para") && objet.equals("Cheri Berry") && !tendu){
			statut = "OK";
			tourStatut = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " mange sa baie Ceriz et guerit de sa paralysie !");
			}
			if (this.talent.equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
			}
		}
		if (!statut.equals("OK") && !statut.equals("KO") && objet.equals("Lum Berry") && !tendu){
			statut = "OK";
			tourStatut = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " mange sa baie Prine et guerit de son probleme de statut!");
			}
			if (this.talent.equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
			}
		}
		if (confus > 0 && objet.equals("Lum Berry") && !tendu){
			confus = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " mange sa baie Prine et n'est plus confus!");	
			}
			if (this.talent.equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
			}
		}
		if (confus > 0 && objet.equals("Persim Berry") && !tendu){
			confus = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println(nomSet + " mange sa baie Kika et n'est plus confus!");	
			}
			if (this.talent.equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int)statsInitiales[0]/4, false, true, 0, affichage);
			}
		}
		if (amoureux && objet.equals("Mental Herb")){
			amoureux = false;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println("L'Herbe Mental de " + nomSet + " le libere de l'amour.");
			}
		}

		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public boolean switche (boolean selfInduced, boolean liberation, boolean relais, boolean affichage){

		tenteSwitch = false;
		boolean reussi = true;

		if ((!liberation && (bind > 0 || fireSpin > 0 || infeste > 0 || magmaStorm > 0 || sandTomb > 0 || siphon > 0 || racines) && !objet.equals("Shed Shell")) || (this.getTalent().equals("Suction Cups") && !selfInduced)){
			reussi = false;
			if (affichage){
				System.out.println(nomSet + " ne peut pas switcher.");
			}
		}
		else{
			if (affichage){
				System.out.println(nomSet + " switche.");
			}

			if (talent.equals("Natural Cure")){
				this.setStatut("OK", true, true, false, this.matchBrouhaha, false);
			}
			if (talent.equals("Regenerator")){
				this.deltaPV(this.statsInitiales[0]/3, false, true, 0, false);
			}

			this.transformation("Bouclier", false);

			if (espece.startsWith("Mega-")){
				try{
					BufferedReader liseur = new BufferedReader (new FileReader ("Database/Pokemons/Transformations/" + espece + ".txt"));
					type = liseur.readLine().split("/");
					liseur.readLine();
					talent = liseur.readLine();
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println(1/0);
				}
			}
			else{
				this.setType(new Pokemon (nom, set, metagame, maison).getType(), false);
				this.setTalent(talentOriginal, false);
			}

			if (statut.equals("GPsn")){
				this.tourStatut = 0;
			}

			poidsModifieur = 0;
			derniereAttaque = null;
			attaquesUtilisees = new ArrayList<String>();
			chargement = "-";
			trouille = false;

			objetConsomme = "(No Item)";
			abrite = false;
			prevention = false;
			gardeLarge = false;
			bouclierRoyal = false;
			picoDefense = false;
			tatami = false;
			tenace = false;
			saisie = false;
			compteurAbrite = 1;
			tailladeReussie = false;
			puisTaillade = 40;
			metronome = 0;
			choiced = false;
			encore = 0;
			assaut = false;
			atterri = false;
			antiAir = false;
			enColere = 0;
			mania = 0;
			torche = false;
			poursuiveur = false;
			focus = false;
			amoureux = false;
			embargo = false;
			puissance = false;
			degatsRecus = -1;
			tourEntrave = 0;
			entrave = "no";
			prlvtDestin = false;
			decharge = false;
			clairvoyance = false;
			oeilMiracle = false;
			nueePoudre = false;
			truant = false;

			for (int i=0; i<4; ++i){
				this.getMoves()[i].setPossessif(false);
			}

			dejaSwitche = true;
			switchPrecedent = true;

			if (!relais){
				this.statsBoosts = new int[] {0,0,0,0,0,0,0,0,0};
				this.statsModifieurs = new float[] {1,1,1,1,1,1,1,1,1};

				confus = 0;
				taunted = 0;
				tourmente = false;
				vampigraine = false;
				cauchemar = false;
				racines = false;
				anneauHydro = false;
				brume = false;
				bind = 0;
				infeste = 0;
				fireSpin = 0;
				magmaStorm = 0;
				sandTomb = 0;
				siphon = 0;
				brouhaha = 0;
				verouillage = false;
				requiem = -1;
				stockage = 0;
				maudit = false;
				baille = 0;
				telekinesis = 0;
				volMagnetic = 0;

				substitute = false;
				clone = null;
			}

			if (picots > 0 && !type[0].equals("Flying") && !type[1].equals("Flying") && !talent.equals("Levitate") && !objet.equals("Air Balloon")){
				if (affichage){
					System.out.println(nomSet + " est blesse par les picots.");
				}
				if (picots == 1){
					this.deltaPV(- (int) (this.statsInitiales[0] /8), false, true, 0, affichage);
				}
				if (picots == 2){
					this.deltaPV(- (int) (this.statsInitiales[0] /6), false, true, 0, affichage);
				}
				if (picots >= 3){
					this.deltaPV(- (int) (this.statsInitiales[0] /4), false, true, 0, affichage);
				}
				if (this.statsInitiales[0] < 8){
					this.deltaPV(- 1, false, true, 0, affichage);
				}
			}

			if (picsToxics > 0 && !type[0].equals("Flying") && !type[1].equals("Flying") && !talent.equals("Levitate") && !objet.equals("Air Balloon")){
				if (type[0].equals("Poison") && type[1].equals("Poison")){
					picsToxics = 0;
					if (affichage){
						System.out.println(nomSet + " absorbe le poison des pics toxics.");
					}
				}
				else{
					if (affichage){
						System.out.println(nomSet + " est touche par les pics toxics.");
					}
					if (picsToxics == 1){
						this.setStatut("Psn", false, true, false, this.matchBrouhaha, affichage);
					}
					if (picsToxics >= 2){
						this.setStatut("GPsn", false, true, false, this.matchBrouhaha, affichage);
					}
				}
			}

			if (PDR){
				double efficacite = (double) 1;

				try{
					BufferedReader fis = new BufferedReader (new FileReader ("DataBase/typestable.txt"));
					int NtypeDef1 = -1;
					int NtypeDef2 = -1;
					String[] txt;
					String NumTypeAtq = "";
					for (int i=0; i<=18;++i){
						txt = fis.readLine().split(" ");
						if (txt[1].equals("Rock")){NumTypeAtq += i;}
						if (txt[1].equals(this.getType()[0])){NtypeDef1 = i;}
						if (txt[1].equals(this.getType()[1])){NtypeDef2 = i;}
					}

					do{txt = fis.readLine().split(" ");}
					while (!txt[0].equals(NumTypeAtq));

					efficacite = Double.valueOf(txt[Integer.valueOf(NtypeDef1).intValue() + 1]).doubleValue() * Double.valueOf(txt[Integer.valueOf(NtypeDef2).intValue() + 1]).doubleValue() /4;
				}
				catch(Exception e){
					e.printStackTrace();
					System.out.println("Probleme de type" + 1/0);
				}

				if (affichage && efficacite > 0){
					System.out.println(this.nomSet + " est blesse par le piege de roc.");
				}

				this.deltaPV(- (int) (efficacite * this.statsInitiales[0] /8), false, true, 0, affichage);

				if (this.statsInitiales[0] < 8){
					this.deltaPV(- 1, false, true, 0, affichage);
				}
			}

			if (toileGluante){
				if (gravite || (!this.getType()[0].equals("Flying") && !this.getType()[1].equals("Flying") && !this.talent.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0)){
					if (affichage){
						System.out.println(this.getNom() + " est pris dans la Toile Gluante.");
					}
					this.boost(5, -1, true, false, affichage);
				}
			}

			this.actualiseStats(false, this.matchBrouhaha, affichage);
		}

		return reussi;
	}

	public void adversaireParti(){
		amoureux = false;
		bind = 0;
		fireSpin = 0;
		magmaStorm = -1;
		siphon = -1;
		infeste = -1;
		sandTomb = -1;
	}

	public void clonage(boolean affichage){
		if (!substitute && this.statsModifiees[0] > this.statsInitiales[0]/4){
			this.deltaPV(- this.statsInitiales[0]/4, false, false, 0, affichage);
			if (affichage){
				System.out.println(nomSet + " sacrifie des PV pour creer un clone.");
			}
			clone = this.clone();
			this.substitute = true;

			clone.statsInitiales[0] = this.statsInitiales[0]/4;
			clone.fixePV(this.statsInitiales[0]/4, false, affichage);
			clone.setNom("Clone de " + nom);
			clone.setNomSet(("Clone de " + nomSet));
			clone.setObjet("(No Item)", true, false, false);
		}
		else{
			if (affichage){
				System.out.println(this.nomSet + " ne peut pas creer de clone!");
			}
		}
	}

	public void mangeBaie(String baie, boolean affichage){

		boolean trouve = false;

		if (baie.endsWith(" (inutilisable)")){
			baie = baie.split(" (inutilisable)")[0];
		}

		if (baie.equals("Apicot Berry") && !tendu){
			trouve = true;
			if (affichage){
				System.out.println(nomSet + " mange la baie Abriko!");
			}
			this.boost(4, 1, true, true, affichage);
		}
		
		if (baie.equals("Chesto Berry") && !tendu){
			trouve = true;
			if (statut.equals("Somm")){ 
				statut = "OK";
				tourStatut = 0;
				if (affichage){
					System.out.println(nomSet + " mange la baie Maron et se reveille!");
				}
			}
			else{
				if (affichage){
					System.out.println(nomSet + " mange la baie Maron.");
				}
			}
		}

		if (baie.equals("Liechi Berry") && !tendu){
			trouve = true;
			if(affichage){
				System.out.println(nomSet + " mange la baie Lichi!");
			}
			this.boost(1, 1, true, true, affichage);
		}

		if (baie.equals("Lum Berry") && !tendu){
			trouve = true;
			if (!statut.equals("OK") && !statut.equals("KO")){
				statut = "OK";
				tourStatut = 0;
				if (affichage){
					System.out.println(nomSet + " mange la baie Prine et guerit de son probleme de statut!");
				}
			}
			if (confus > 0){
				confus = 0;
				if (affichage){
					System.out.println(nomSet + " mange la baie Prine et n'est plus confus!");	
				}
			}
			if (!trouve && affichage){
				System.out.println(nomSet + " mange la baie Prine.");
			}
		}

		if (baie.equals("Petaya Berry") && !tendu){
			trouve = true;
			if (affichage){
				System.out.println(nomSet + " mange la baie Pitaye !");
			}
			this.boost(3, 1, true, true, affichage);
		}

		if (baie.equals("Sitrus Berry") && !tendu){
			trouve = true;
			if (affichage){
				System.out.println(nomSet + " mange la baie Citrus !");
			}
			this.deltaPV((int)(statsInitiales[0]/4), false, true, 0, affichage);
		}

		if (baie.equals("Mago Berry")){
			trouve = true;
			if(affichage){
				System.out.println(nomSet + " mange la Baie Mago !");
			}
			this.deltaPV((int)statsInitiales[0]/8, false, true, 0, affichage);
			if (this.tabNature[5] < 1){
				this.setStatut("Conf", true, true, true, matchBrouhaha, affichage);
			}
		}

		if (baie.equals("Salac Berry") && !tendu){
			trouve = true;
			if (affichage){
				System.out.println(nomSet + " mange la baie Sailak!");	
			}
			this.boost(5, 1, true, true, affichage);
		}

		if (baie.equals("Chople Berry") || baie.equals("Coba Berry") || baie.equals("Custap Berry") || baie.equals("Colbur Berry") || baie.equals("Haban Berry") || baie.equals("Occa Berry") || baie.equals("Passho Berry") || baie.equals("Shuca Berry") || baie.equals("Watmel Berry") || baie.equals("Yache Berry")){
			trouve = true;
			if (affichage){
				System.out.println(nomSet + " mange la baie " + baie.split(" Ber")[0] + ".");
			}
		}

		if (trouve){
			if (this.getTalent().equals("Cheek Pouch")){
				if (affichage){
					System.out.println("Bajoue restaure de la vie a " + this.getNom() + ".");
				}
				this.deltaPV((int) this.getStatsInitiales()[0]/4, false, true, 0, affichage);
			}
		}
		else{
			System.out.println("Baie introuvable : " + baie);
			System.out.println(1/0);
		}
	}

	public void antiBrume (boolean affichage){
		if (PDR && affichage){
			System.out.println(nomSet + " se debarrasse des Pieges de Roc.");
		}
		if (picots > 0 && affichage){
			System.out.println(nomSet + " se debarrasse des Picots.");
		}
		if (picsToxics > 0 && affichage){
			System.out.println(nomSet + " se debarrasse des Pics Toxics.");
		}
		if (toileGluante && affichage){
			System.out.println(nomSet + " se debarrasse de la Toile Gluante.");
		}
		if (protection > 0 && affichage){
			System.out.println(nomSet + " n'est plus protege par Protection.");
		}
		if (murLumiere > 0 && affichage){
			System.out.println(nomSet + " n'est plus protege par Mur Lumiere.");
		}
		if (runeProtect > 0 && affichage){
			System.out.println(nomSet + " n'est plus protege par Rune Protect.");
		}
		PDR = false;
		picots = 0;
		picsToxics = 0;
		toileGluante = false;
		protection = 0;
		murLumiere = 0;
		runeProtect = 0;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void tourRapide (boolean affichage){
		if (vampigraine && affichage){
			System.out.println(nomSet + " se libere de la Vampigraine.");
		}
		if (bind > 0 && affichage){
			System.out.println(nomSet + " se libere de l'etreinte.");
		}
		if (infeste > 0 && affichage){
			System.out.println(nomSet + " se libere de l'infestation.");
		}
		if (fireSpin > 0 && affichage){
			System.out.println(nomSet + " se libere du tourbillon de flammes.");
		}
		if (magmaStorm > 0 && affichage){
			System.out.println(nomSet + " se libere du tourbillon de lave.");
		}
		if (siphon > 0 && affichage){
			System.out.println(nomSet + " se libere du siphon.");
		}
		if (sandTomb > 0 && affichage){
			System.out.println(nomSet + " se libere du tourbillon de sable.");
		}
		if (PDR && affichage){
			System.out.println(nomSet + " se debarrasse des Pieges de Roc.");
		}
		if (picots > 0 && affichage){
			System.out.println(nomSet + " se debarrasse des Picots.");
		}
		if (picsToxics > 0 && affichage){
			System.out.println(nomSet + " se debarrasse des Pics Toxics.");
		}
		if (toileGluante && affichage){
			System.out.println(nomSet + " se debarrasse de la Toile Gluante.");
		}
		vampigraine = false;
		bind = 0;
		infeste = 0;
		fireSpin = 0;
		magmaStorm = 0;
		siphon = 0;
		sandTomb = 0;
		PDR = false;
		picots = 0;
		picsToxics = 0;
		toileGluante = false;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void entrave (boolean affichage){
		try{
			if (tourEntrave == 0 && !this.talent.equals("Aroma Veil")){
				entrave = derniereAttaque.getNom();
				tourEntrave = 4;
				if (affichage){
					System.out.println(entrave + " de " + nomSet + " est sous entrave.");
				}
			}
			else{
				if (affichage){
					System.out.println("L'entrave echoue.");
				}
			}
		}
		catch(Exception e){
			if (affichage){
				System.out.println("L'entrave echoue.");
			}
		}

		if (objet.equals("Mental Herb")){
			entrave = "no";
			tourEntrave = 0;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println("L'Herbe Mental de " + nomSet + " le libere de l'entrave.");
			}
		}
	}

	public void renversement (boolean affichage){
		for (int i=0; i<9; ++i){
			this.statsBoosts[i] = - this.statsBoosts[i];
		}
		if (affichage){
			System.out.println("Les changements de stats de " + this.getNom() + " sont renverses !");
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void morphing (Pokemon adversaire, boolean affichage){
		if (adversaire.isMorphing()){
			if (affichage){
				System.out.println(this.getNom() + " ne peut pas se copier " + adversaire.getNom() + "...");
			}
		}
		else{
			if (affichage){
				System.out.println(this.nomSet + " se transforme en " + adversaire.getEspece() + ".");
			}
			morphing = true;
			this.espece = adversaire.getEspece();
			this.nomSet = nom + " [" + this.maison + " " + set + "]";
			if (!nom.equals(espece)){
				nomSet += " (" + espece + ")";
			}
			this.type = (String[]) DeepCopy.copy(adversaire.getType());
			this.poids = adversaire.getPoids();
			this.talent = adversaire.getTalent();
			this.nature = adversaire.getNature();
			this.tabNature = (float[]) DeepCopy.copy(adversaire.getTabNature());
			this.PC = adversaire.getPC();
			this.statsBoosts = (int[]) DeepCopy.copy(adversaire.getStatsBoosts());
			for (int i=1; i<6; ++i){
				this.baseStats[i] = adversaire.getBaseStats()[i];
				this.EVs[i] = adversaire.getEVs()[i];
				this.statsInitiales[i] = adversaire.getStatsInitiales()[i];
			}
			this.actualiseStats(false, this.matchBrouhaha, false);
			this.moves = (Attaque[]) DeepCopy.copy(adversaire.getMoves());
			for (int i=0; i<4; ++i){
				this.moves[i].setPP(5);
				if (moves[i].getNom().equals("-") || moves[i].getNom().equals("(No Move)")){
					this.moves[i].setPP(0);
				}
			}
		}
	}

	public void finDeTour (boolean affichage){

		trouille = false;
		atterri = false;
		poursuiveur = false;
		dejaAttaque = false;
		tenteSwitch = false;
		switchPrecedent = false;
		if (dejaSwitche){
			switchPrecedent = true;
		}
		dejaSwitche = false;
		focus = false;
		degatsRecus = -1;
		degatsPhysRecus = 0;
		degatsSpecRecus = 0;
		refletMagik = false;
		saisie = false;
		nueePoudre = false;
		truant = talent.equals("Truant") && !truant && !switchPrecedent;
		try {
			if (this.getDerniereAttaque().getNom().equals("Lock-On")) {
				verouillage = true;
			} else {
				verouillage = false;
			}
		}
		catch(Exception e){
			verouillage = false;
		}

		if (!statut.equals("KO") && requiem > 0){
			--requiem;
			if (affichage){
				System.out.println("\nLe compteur Requiem de " + nomSet + " descend a " + requiem + ".");
			}
			if (requiem == 0){
				this.fixePV(0, true, affichage);
			}
		}

		if (murLumiere > 0){
			--murLumiere;
			if (murLumiere == 0 && affichage){
				System.out.println("\nLe Mur Lumiere de " + nom + " disparait.");
			}
		}
		if (protection > 0){
			--protection;
			if (protection == 0 && affichage){
				System.out.println("\nLa Protection de " + nom + " disparait.");
			}
		}
		if (runeProtect > 0){
			--runeProtect;
			if (runeProtect == 0 && affichage){
				System.out.println("\nLa Rune Protect de " + nom + " disparait.");
			}
		}
		if (ventArriere > 0){
			--ventArriere;
			if (ventArriere == 0 && affichage){
				System.out.println("\nLe vent s'arrete de souffler sur " + nom + ".");
			}
		}

		if (tourEntrave > 0){
			--tourEntrave;
			if (tourEntrave == 0){
				if (affichage){
					System.out.println("\n" + nomSet + " peut de nouveau utiliser " + entrave + ".");
				}
				entrave = "no";
			}
		}

		if (encore > 0){
			--encore;
			if (encore == 0){
				if (affichage){
					System.out.println("\n" + nomSet + " n'a plus a repeter la meme capacite.");
				}
			}
		}

		if (antiSoin > 0){
			--antiSoin;
			if (antiSoin == 0 && affichage){
				System.out.println("\n" + nom + " peut de nouveau guerir.");
			}
		}

		if (telekinesis > 0){
			--telekinesis;
			if (telekinesis == 0){
				if (affichage){
					System.out.println("\n" + nomSet + " retourne au sol.");
				}
			}
		}
		if (volMagnetic > 0){
			--volMagnetic;
			if (volMagnetic == 0){
				if (affichage){
					System.out.println("\nLe champ magnetique de " + nomSet + " se dissipe.");
				}
			}
		}

		if (this.talent.equals("Hydration") && this.meteo.contains("pluie") && !this.statut.equals("KO") && !this.statut.equals("OK")){
			if (affichage){
				System.out.println("La pluie soigne " + this.getNom() + " de son probleme de statut.");
			}
			this.setStatut("OK", true, true, false, this.matchBrouhaha, false);
		}
		if (!statut.equals("KO") && talent.equals("Rain Dish") && meteo.contains("pluie")){
			if (affichage){
				System.out.println("\nCuvette de " + nomSet + " le soigne un peu.");
			}
			this.deltaPV((int)(statsInitiales[0]/16), false, true, 0, affichage);
		}
		if (this.talent.equals("Leaf Guard") && this.meteo.endsWith("soleil") && !this.statut.equals("KO") && !this.statut.equals("OK")){
			if (affichage){
				System.out.println("Le Soleil soigne " + this.getNom() + " de son probleme de statut.");
			}
			this.setStatut("OK", true, true, false, this.matchBrouhaha, false);
		}

		if (!statut.equals("KO") && meteo.equals("tempete de sable") && !type[0].equals("Ground") && !type[1].equals("Ground") && !type[0].equals("Rock") && !type[1].equals("Rock") && !type[0].equals("Steel") && !type[1].equals("Steel") && !talent.equals("Sand Veil") && !talent.equals("Overcoat") && !objet.equals("Safety Goggles") && !talent.equals("Cloud Nine") && !talent.equals("Air Lock")){
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par la tempete de sable.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/16), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (!statut.equals("KO") && meteo.equals("grele") && !type[0].equals("Ice") && !type[1].equals("Ice") && !talent.equals("Ice Body") && !talent.equals("Snow Cloak") && !talent.equals("Overcoat") && !objet.equals("Safety Goggles") && !talent.equals("Cloud Nine") && !talent.equals("Air Lock")){
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par la tempete de grele.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/16), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		if (!statut.equals("KO") && talent.equals("Ice Body") && meteo.equals("grele")){
			if (affichage){
				System.out.println("\nCorps Gel de " + nomSet + " le soigne un peu.");
			}
			this.deltaPV((int)(statsInitiales[0]/16), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && talent.equals("Solar Power") && meteo.endsWith("soleil")){
			if (affichage){
				System.out.println("\n" + nomSet + " souffre a cause du Soleil.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && talent.equals("Dry Skin") && meteo.endsWith("soleil")){
			if (affichage){
				System.out.println("\n" + nomSet + " souffre a cause du Soleil.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
		}
		if (!statut.equals("KO") && talent.equals("Dry Skin") && meteo.contains("pluie")){
			if (affichage){
				System.out.println("\n" + nomSet + " se regenere sous la pluie.");
			}
			this.deltaPV((int)(statsInitiales[0]/8), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && terrain.equals("Grassy") && !(this.type[0].equals("Flying") || this.type[1].equals("Flying") || this.talent.equals("Levitate") || this.objet.equals("Air Balloon") || this.volMagnetic > 0)){
			if (affichage && statsModifiees[0] < statsInitiales[0]){
				System.out.println(this.getNom() + " broute l'herbe.");
			}
			this.deltaPV((int)(statsInitiales[0] /16), false, true, 0, affichage);
		}

		if (prescience > 0){
			--prescience;
		}

		if (voeu > 0){
			--voeu;
			if (voeu == 0){
				if (affichage){
					System.out.println("\nLe voeu de " + nomSet + " se realise.");
				}
				this.deltaPV(this.getStatsInitiales()[0]/2, false, true, 0, affichage);
			}
		}

		if (charge > 0){
			--charge;
		}

		if (!statut.equals("KO") && objet.equals("Leftovers")){
			if (affichage && statsModifiees[0] < statsInitiales[0]){
				System.out.println();
			}
			this.deltaPV((int)(statsInitiales[0] /16), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && objet.equals("Black Sludge")){
			if (affichage && statsModifiees[0] < statsInitiales[0]){
				System.out.println("");
			}
			if (type[0].equals("Poison") || type[1].equals("Poison")){
				this.deltaPV((int)(statsInitiales[0] /16), false, true, 0, affichage);
			}
			else{
				this.deltaPV(- (int)(statsInitiales[0] /8), false, true, 0, affichage);
				if (this.statsInitiales[0] < 8){
					this.deltaPV(- 1, false, true, 0, affichage);
				}
			}
		}

		if (!statut.equals("KO") && this.anneauHydro){
			if (affichage && statsModifiees[0] < statsInitiales[0]){
				System.out.println("\n" + this.getNom() + " se regenere avec Anneau Hydro.");
			}
			int delta = (int)(statsInitiales[0] /16);
			if (objet.equals("Big Root")){
				delta *= 1.3;
			}
			this.deltaPV(delta, false, true, 0, affichage);
		}
		if (!statut.equals("KO") && this.racines){
			if (affichage && statsModifiees[0] < statsInitiales[0]){
				System.out.println("\n" + this.getNom() + " absorbe les nutriments du sol.");
			}
			int delta = (int)(statsInitiales[0] /16);
			if (objet.equals("Big Root")){
				delta *= 1.3;
			}
			this.deltaPV(delta, false, true, 0, affichage);
		}

		if (statut.equals("Somm") && this.cauchemar){
			if (affichage){
				System.out.println("\n" + this.getNom() + " fait un cauchemar.");
			}
			this.deltaPV(- (int)(statsInitiales[0] /4), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && bind > 0){
			--bind;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par l'etreinte.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		if (!statut.equals("KO") && fireSpin > 0){
			--fireSpin;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par le tourbillon de flammes.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		if (!statut.equals("KO") && infeste > 0){
			--infeste;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par l'infestation.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		if (!statut.equals("KO") && magmaStorm > 0){
			--magmaStorm;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par la tempete de lave.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}if (!statut.equals("KO") && sandTomb > 0){
			--sandTomb;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par le tourbillon de sable.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		if (!statut.equals("KO") && siphon > 0){
			--siphon;
			if (affichage){
				System.out.println("\n" + nomSet + " est blesse par le siphon.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (!statut.equals("KO") && this.brouhaha > 0){
			--brouhaha;
			if (brouhaha > 0){
				if (affichage){
					System.out.println("\n" + nomSet + " continue son Brouhaha.");
				}
			}
			else{
				if (affichage){
					System.out.println("\nLe Brouhaha de " + nomSet + " s'arrete.");
				}
			}
		}

		if (!statut.equals("KO") && maudit){
			if (affichage){
				System.out.println("\n" + nomSet + " souffre de sa malediction.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/4), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}
		
		if (!statut.equals("KO") && !statut.equals("OK") && talent.equals("Shed Skin")){
			Random alea = new Random();
			if (alea.nextInt(3) == 0){
				this.setStatut("OK", true, true, true, brouhaha > 0, false);
				if (affichage){
					System.out.println("Mue de " + this.getNom() + " le guerit de son probleme de statut !");
				}
			}
		}

		if (!statut.equals("KO") && statut.equals("Brul") && !talent.equals("Magic Guard")){
			if (affichage){
				System.out.println("\n" + nomSet + " souffre de sa brulure.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (!statut.equals("KO") && statut.equals("GPsn") && !talent.equals("Poison Heal") && !talent.equals("Magic Guard")){
			++tourStatut;
			if (tourStatut > 15){tourStatut = 15;}
			if (affichage){
				System.out.println("\n" + nomSet + " souffre de la toxine.");
			}
			this.deltaPV(- tourStatut * (int)(statsInitiales[0]/16), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (!statut.equals("KO") && statut.equals("Psn") && !talent.equals("Poison Heal")){
			if (affichage){
				System.out.println("\n" + nomSet + " souffre du poison.");
			}
			this.deltaPV(- (int)(statsInitiales[0]/8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (!statut.equals("KO") && (statut.equals("Psn") || statut.equals("GPsn")) && talent.equals("Poison Heal")){
			if (affichage){
				System.out.println("");
			}
			this.deltaPV((int)(statsInitiales[0]/8), false, true, 0, affichage);
		}

		if (!statut.equals("KO") && talent.equals("Speed Boost") && this.statsBoosts[5] < 6){
			if (affichage){
				System.out.println("");
			}
			this.boost(5, 1, true, true, affichage);
		}
		
		if (!statut.equals("KO") && talent.equals("Moody")){
			if (affichage){
				System.out.println("");
			}
			
			Random alea = new Random();
			int stat1 = -1;
			int stat2 = -1;
			
			boolean cherche = true;
			int compteur = 0;
			while (cherche && compteur < 100){
				stat1 = 1+alea.nextInt(7);
				if (this.statsBoosts[stat1] < 6){
					cherche = false;
				}
				else{
					++compteur;
				}
			}
			if (compteur < 100){
				this.boost(stat1, 2, true, true, affichage);
			}
			else{
				stat1 = -1;
			}

			cherche = true;
			compteur = 0;
			while (cherche && compteur < 100){
				stat2 = 1+alea.nextInt(7);
				if (this.statsBoosts[stat2] > -6 && (stat1 != stat2)){
					cherche = false;
				}
				else{
					++compteur;
				}
			}
			if (compteur < 100){
				this.boost(stat2, -1, true, true, affichage);
			}
			else{
				stat2 = -1;
			}
		}

		if (!statut.equals("KO") && taunted > 0){
			--taunted;
			if (taunted == 0 && affichage){
				System.out.println("\nLa provoc de " + nom + " est terminee.");
			}
		}

		if (!statut.equals("KO") && (abrite || prevention || gardeLarge || bouclierRoyal || picoDefense || tatami || tenace)){
			abrite = false;
			prevention = false;
			gardeLarge = false;
			bouclierRoyal = false;
			picoDefense = false;
			tenace = false;
			compteurAbrite *= 2;
		}
		else{
			compteurAbrite = 1;
		}

		try{
			if (!statut.equals("KO") && this.getDerniereAttaque().getNom().equals("Fury Cutter") && tailladeReussie){
				puisTaillade *= 2;
				if (affichage){
					System.out.println("Taillade +");
				}
				if (puisTaillade > 160){
					puisTaillade = 160;
				}
			}
			else{
				puisTaillade = 40;
			}
		}
		catch(NullPointerException e){
			puisTaillade = 40;
		}

		tailladeReussie = false;

		if (statut.equals("OK") && objet.equals("Flame Orb")){
			if (affichage){
				System.out.println("");
			}
			this.setStatut("Brul", true, true, false, this.matchBrouhaha, affichage);
		}
		if (statut.equals("OK") && objet.equals("Toxic Orb")){
			if (affichage){
				System.out.println("");
			}
			this.setStatut("GPsn", true, true, false, this.matchBrouhaha, affichage);
		}
		if (statut.equals("OK") && baille > 0){
			--baille;
			if (baille == 0){
				if (affichage){
					System.out.println("");
				}
				this.setStatut("Somm", true, true, false, this.matchBrouhaha, affichage);
			}
		}

		if (talent.equals("Harvest") && this.getObjet().equals("(No Item)") && this.objetConsomme.endsWith(" Berry")){
			Random alea = new Random();
			if (this.meteo.endsWith("soleil") || alea.nextInt(2) == 0){
				this.setObjet(objetConsomme, true, false, affichage);
				if (affichage){
					System.out.println(this.getNom() + " recolte une " + this.getObjet());
				}
			}
		}

		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public String getNom() {
		return nomSet;
	}

	public String getNomSeul(){
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEspece() {
		return espece;
	}

	public int getNTotalSets() {
		return NTotalSets;
	}

	public float getUtilisationEspece() {
		return utilisationEspece;
	}

	public float getUtilisationSet() {
		return utilisationSet;
	}

	public boolean isProbable() {
		return probable;
	}

	public float getPoints() {
		return utilisationEspece * utilisationSet;
	}

	public String getMetagame() {
		return metagame;
	}

	public String[] getType() {
		String[] typeBis = (String[]) DeepCopy.copy(type);

		if (atterri || antiAir || gravite){
			if (typeBis[0].equals("Flying")){
				typeBis[0] = "-";
			}
			if (typeBis[1].equals("Flying")){
				typeBis[1] = "-";
			}
			if (typeBis[0].equals("-") && typeBis[1].equals("-")){
				typeBis[0] = "Normal";
			}
		}
		return typeBis;
	}

	public void setType(String[] type, boolean affichage) {
		this.type = type;
		if (affichage){
			System.out.print(this.getNom() + " prend le type " + type[0]);
			if (!type[1].equals("-")){
				System.out.print("/" + type[1]);
			}
			System.out.println(".");
		}
	}

	public int[] getBaseStats() {
		return baseStats;
	}

	public void setBaseStats(int[] baseStats) {
		this.baseStats = baseStats;
	}

	public float getPoids() {
		return poidsModifie;
	}

	public void setPoids(float poids) {
		this.poids = poids;
	}

	public int getTailleMovepool() {
		return tailleMovepool;
	}

	public int getTailleItems() {
		return tailleItems;
	}

	public int getTailleSpreads() {
		return tailleSpreads;
	}

	public int diversite (){
		return Math.max(tailleItems, tailleSpreads);
	}

	public String getSexe() {
		return sexe;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public void setNomSet(String nomSet) {
		this.nomSet = nomSet;
	}

	public boolean isCompetitif() {
		return competitif;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public String getTalent() {
		return talent;
	}

	public void setTalent(String talent, boolean affichage) {
		if (this.talent.equals("Stance Change")){
			if (affichage){
				System.out.println("Le talent de " + this.nomSet + " ne peut pas etre modifie.");
			}
		}
		else{
			this.talent = talent;
			if (!talent.contains("(") && affichage){
				System.out.println(this.nomSet + " acquiert le talent " + this.talent + " !");
			}
			this.actualiseStats(false, this.matchBrouhaha, affichage);
		}
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public float[] getTabNature() {
		return tabNature;
	}

	public int[] getEVs() {
		return EVs;
	}

	public String getObjet() {
		if ((zoneMagique || embargo || this.getTalent().equals("Klutz")) && !objet.equals("(No Item)")){
			return objet + " (inutilisable)";
		}
		else{
			return objet;
		}
	}

	public void setObjet(String objet, boolean selfInduced, boolean recyclable, boolean affichage) {
		if (objet.endsWith(" (inutilisable)")){
			objet = objet.split(" (inutilisable)")[0];
		}
		if (selfInduced || !talent.equals("Sticky Hold")) {
			if (recyclable) {
				objetConsomme = this.objet;
			}
			this.objet = objet;
			choiced = false;
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void recyclage(boolean affichage){
		if (!objetConsomme.equals("(No Item") && objet.equals("(No Item)")){
			this.objet = this.objetConsomme;
			objetConsomme = "(No Item)";
			if (affichage){
				System.out.println(this.getNom() + " recupere son objet " + this.objet + ".");
			}
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue.");
			}
		}
	}

	public int[] getStatsInitiales() {
		return statsInitiales;
	}

	public void setStatsInitiales(int[] statsInitiales) {
		this.statsInitiales = statsInitiales;
	}

	public int[] getStatsBoosts() {
		return statsBoosts;
	}

	public void setStatsBoosts(int[] statsBoosts) {
		this.statsBoosts = statsBoosts;
	}

	public float[] getStatsModifieurs() {
		return statsModifieurs;
	}

	public void setStatsModifieurs(float[] statsModifieurs) {
		this.statsModifieurs = statsModifieurs;
	}

	public float[] getStatsModifiees() {
		return statsModifiees;
	}

	public void setStatsModifiees(float[] statsModifiees) {
		this.statsModifiees = statsModifiees;
	}

	public String getStatut() {
		return statut;
	}

	public int getTourStatut() {
		return tourStatut;
	}

	public void setTourStatut(int tourStatut) {
		this.tourStatut = tourStatut;
	}

	public boolean isTrouille() {
		return trouille;
	}

	public void setTrouille(boolean affichage) {
		if (talent.equals("Inner Focus")){
			if (affichage){
				System.out.println("Attention protege contre la peur!");
			}
		}
		else{
			this.trouille = true;
		}
	}

	public boolean isAmoureux() {
		return amoureux;
	}

	public int getConfus() {
		return confus;
	}

	public void setConfus(int confus) {
		this.confus = confus;
	}

	public void divisePoids(boolean affichage) {
		-- poidsModifieur;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
		if (affichage){
			System.out.println(nomSet + " s'allege (" + poidsModifie + " kg).");
		}
	}

	public Action getActionPrevue() {
		return actionPrevue;
	}

	public void setActionPrevue(Action actionPrevue) {
		this.actionPrevue = actionPrevue;
	}

	public Attaque getDerniereAttaque() {
		return derniereAttaque;
	}

	public void setDerniereAttaque(Attaque attaque) {
		this.derniereAttaque = attaque;
		if (! attaquesUtilisees.contains(attaque.getNom())){
			attaquesUtilisees.add(attaque.getNom());
		}
	}

	public ArrayList<String> getAttaquesUtilisees() {
		return attaquesUtilisees;
	}

	public boolean isTenteSwitch() {
		return tenteSwitch;
	}

	public void setTenteSwitch() {
		this.tenteSwitch = true;
	}

	public String getMeteo() {
		return meteo;
	}

	public boolean isGravite() {
		return gravite;
	}

	public void setGravite(boolean gravite, boolean affichage) {
		this.gravite = gravite;
		if (gravite && (type[0].equals("Flying") || type[1].equals("Flying") || talent.equals("Levitate")) && affichage){
			System.out.println(this.getNom() + " ne peut pas rester en l'air a cause de la Gravite.");
		}
		if (this.getChargement().equals("Fly") || this.getChargement().equals("Bounce") || this.getChargement().equals("Sky Drop")){
			this.chargement = "-";
		}
	}

	public void setZoneMagique(boolean zoneMagique) {
		this.zoneMagique = zoneMagique;
		this.actualiseStats(false, this.matchBrouhaha, false);
	}

	public boolean isTelekinesis() {
		return (telekinesis > 0);
	}

	public void setTelekinesis(boolean affichage) {
		if (!racines && !antiAir && !objet.equals("Iron Ball") && !gravite && telekinesis == 0){
			telekinesis = 3;
			if (affichage){
				System.out.println(this.getNom() + " levite.");
			}
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue.");
			}
		}
	}
	
	public void setBriseMoule(boolean affichage){
		this.briseMoule = true;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public boolean isVolMagnetic() {
		return (volMagnetic > 0);
	}

	public void setVolMagnetic(boolean affichage) {
		if (!racines && !antiAir && !objet.equals("Iron Ball") && !gravite && volMagnetic == 0){
			volMagnetic = 3;
			if (affichage){
				System.out.println(this.getNom() + " levite sur un champ magnetique.");
			}
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue.");
			}
		}
	}

	public boolean isDejaAttaque() {
		return dejaAttaque;
	}

	public void setDejaAttaque(boolean dejaAttaque) {
		this.dejaAttaque = dejaAttaque;
	}

	public boolean isDejaSwitche() {
		return dejaSwitche;
	}

	public void setDejaSwitche(boolean dejaSwitche) {
		this.dejaSwitche = dejaSwitche;
	}

	public boolean isSwitchPrecedent() {
		return switchPrecedent;
	}

	public void setMeteo(String meteo, boolean affichage) {
		this.meteo = meteo;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void setTerrain (String terrain, boolean affichage) {
		if (!(this.type[0].equals("Flying") || this.type[1].equals("Flying") || this.talent.equals("Levitate") || this.objet.equals("Air Balloon") || this.volMagnetic > 0)){
			this.terrain = terrain;
			this.actualiseStats(false, this.matchBrouhaha, affichage);
		}
	}

	public String getTerrain() {
		return terrain;
	}

	public String getChargement() {
		return chargement;
	}

	public void setChargement(String chargement) {
		this.chargement = chargement;
	}

	public boolean isChoiced() {
		return choiced;
	}

	public void setChoiced(boolean choiced) {
		this.choiced = choiced;
	}

	public int getEncore() {
		return encore;
	}

	public void setEncore(boolean affichage) {
		try{
			if (!this.getDerniereAttaque().equals((Attaque) null) && !this.getTalent().equals("Aroma Veil")){
				if (encore <= 0){
					encore = 3;
					if (affichage){
						System.out.println(nomSet + ", encore une fois !");
					}
				}
				else{
					if (affichage){
						System.out.println(nomSet + " doit deja repeter une capacite !");
					}
				}
				if (encore > 0 && objet.equals("Mental Herb")){
					encore = 0;
					this.setObjet("(No Item)", true, true, affichage);
					if (affichage){
						System.out.println("L'Herbe Mental de " + nomSet + " le libere de Encore.");
					}
				}
			}
			else{
				if (affichage){
					System.out.println("Mais cela echoue.");
				}
			}
		}
		catch(NullPointerException npe){
			if (affichage){
				System.out.println("Mais cela echoue.");
			}
		}
	}

	public boolean isTourmente() {
		return tourmente;
	}

	public void setTourmente(boolean affichage) {
		if (!this.tourmente && !this.talent.equals("Aroma Veil")){
			this.tourmente = true;
			if (affichage){
				System.out.println(nomSet + " est tourmente.");
			}
		}
		else{
			if (affichage){
				System.out.println("Ca n'affecte pas " + nomSet + ".");
			}
		}
		if (objet.equals("Mental Herb")){
			tourmente = false;
			this.setObjet("(No Item)", true, true, affichage);
			if (affichage){
				System.out.println("L'Herbe Mental de " + nomSet + " le guerit de sa Tourmente.");
			}
		}
	}
	
	public void detruitAbri (){
		abrite = false;
		prevention = false;
		gardeLarge = false;
		bouclierRoyal = false;
		picoDefense = false;
		tatami = false;
	}

	public boolean isAbrite() {
		return abrite;
	}

	public void setAbrite(boolean affichage) {
		this.abrite = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}
	
	public boolean isPrevention() {
		return prevention;
	}

	public void setPrevention(boolean affichage) {
		this.prevention = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}

	public boolean isGardeLarge() {
		return gardeLarge;
	}

	public void setGardeLarge(boolean affichage) {
		this.gardeLarge = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}

	public boolean isBouclierRoyal() {
		return bouclierRoyal;
	}

	public void setBouclierRoyal(boolean affichage) {
		this.bouclierRoyal = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}

	public boolean isPicoDefense() {
		return picoDefense;
	}

	public void setPicoDefense(boolean affichage) {
		this.picoDefense = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}

	public boolean isTatami() {
		return tatami;
	}

	public void setTatami(boolean affichage) {
		this.tatami = true;
		if (affichage){
			System.out.println(nomSet + " est pret a se proteger.");
		}
	}

	public void setTenace(boolean affichage) {
		this.tenace = true;
		if (affichage){
			System.out.println(nomSet + " est pret a encaisser les coups.");
		}
	}

	public void setSaisie(boolean affichage) {
		this.saisie = true;
		if (affichage){
			System.out.println(nomSet + " attend que son adversaire attaque.");
		}
	}

	public boolean isSaisie() {
		return saisie;
	}

	public int getCompteurAbrite() {
		return compteurAbrite;
	}

	public void setCompteurAbrite(int compteurAbrite) {
		this.compteurAbrite = compteurAbrite;
	}

	public void setTailladeReussie() {
		this.tailladeReussie = true;
	}

	public int getPuisTaillade() {
		return puisTaillade;
	}

	public int getMetronome() {
		return metronome;
	}

	public void resetMetronome() {
		this.metronome = 0;
	}

	public void plusMetronome() {
		++this.metronome;
		if (this.metronome > 5){
			this.metronome = 5;
		}
	}

	public int getTaunted() {
		return taunted;
	}

	public void setTaunted(boolean affichage) {
		if (this.talent.equals("Oblivious") || this.talent.equals("Aroma Veil")){
			if (affichage){
				System.out.println("Ca n'affecte pas " + this.getNom() + ".");
			}
		}
		else{
			if (taunted <= 0){
				this.taunted = 3;
				if (affichage){
					System.out.println(this.getNom() + " repond a la provocation.");
				}
			}
			else{
				if (affichage){
					System.out.println(nomSet + " est deja sous influence de la Provoc.");
				}
			}
			if (objet.equals("Mental Herb")){
				taunted = 0;
				this.setObjet("(No Item)", true, true, affichage);
				if (affichage){
					System.out.println("L'Herbe Mental de " + nomSet + " le libere de la provocation.");
				}
			}
		}
	}

	public boolean isAssaut() {
		return assaut;
	}

	public void setAssaut(boolean assaut) {
		this.assaut = assaut;
	}

	public boolean isAtterri() {
		return atterri;
	}

	public void setAtterri() {
		this.atterri = true;
	}

	public boolean isAntiAir() {
		return antiAir;
	}

	public void setAntiAir(boolean affichage) {
		if (!antiAir && affichage && (this.getType()[0].equals("Flying") || this.getType()[1].equals("Flying") || talent.equals("Levitate") || telekinesis > 0 || volMagnetic > 0)){
			System.out.println(nomSet + " s'ecrase au sol.");
		}
		this.antiAir = true;
		this.telekinesis = 0;
		this.volMagnetic = 0;
		if (chargement.equals("Bounce") || chargement.equals("Fly") || chargement.equals("Sky Drop")){
			chargement = "-";
		}
	}

	public void setEmbargo(boolean affichage) {
		this.embargo = true;
		if (affichage){
			System.out.println(this.getNom() + " ne peut plus utiliser d'objet.");
		}
	}

	public int getEnColere() {
		return enColere;
	}

	public void setEnColere(int enColere) {
		this.enColere = enColere;
	}

	public int getMania() {
		return mania;
	}

	public void setMania(int mania) {
		this.mania = mania;
	}

	public boolean isBrouhaha() {
		return brouhaha > 0;
	}

	public void setBrouhaha() {
		Random alea = new Random();
		if (this.brouhaha <= 0){
			this.brouhaha = 2 + alea.nextInt(4);
		}
	}

	public boolean isTorche() {
		return torche;
	}

	public void setTorche(boolean torche) {
		this.torche = torche;
	}

	public boolean isPoursuiveur() {
		return poursuiveur;
	}

	public void setPoursuiveur() {
		this.poursuiveur = true;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean affichage) {
		if (affichage){
			System.out.println(nomSet + " se concentre davantage.");
		}
		this.focus = true;
	}

	public int getDegatsRecus() {
		return degatsRecus;
	}

	public int getDegatsPhysRecus() {
		return degatsPhysRecus;
	}

	public int getDegatsSpecRecus() {
		return degatsSpecRecus;
	}

	public void setProtection(boolean affichage) {
		if (protection > 0){
			if (affichage){
				System.out.println(nomSet + " est deja protege par Protection.");
			}
		}
		else{
			protection = 5;
			if (objet.equals("Light Clay")){
				protection = 8;
			}

			if (affichage){
				System.out.println(nomSet + " s'entoure de Protection.");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void setMurLumiere(boolean affichage) {
		if (murLumiere > 0){
			if (affichage){
				System.out.println(nomSet + " est deja protege par Mur Lumiere.");
			}
		}
		else{
			murLumiere = 5;
			if (objet.equals("Light Clay")){
				murLumiere = 8;
			}
			if (affichage){
				System.out.println(nomSet + " s'entoure de Mur Lumiere.");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void setRuneProtect(boolean affichage) {
		if (runeProtect > 0){
			if (affichage){
				System.out.println(nomSet + " est deja protege par Rune Protect.");
			}
		}
		else{
			runeProtect = 5;

			if (affichage){
				System.out.println(nomSet + " s'entoure de Rune Protect.");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}
	
	public void setBrume (boolean affichage){
		if (!brume){
			brume = true;
			if (affichage){
				System.out.println(nomSet + " s'entoure de Brume.");
			}
		}
		else{
			if (affichage){
				System.out.println("Mais cela échoue.");
			}
		}
	}

	public void setVentArriere(boolean affichage) {
		if (ventArriere > 0){
			if (affichage){
				System.out.println(nomSet + " est deja porte par le vent.");
			}
		}
		else{
			ventArriere = 4;
			if (affichage){
				System.out.println("Le vent porte " + nomSet + ".");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void casseBrique (boolean affichage){
		if (protection > 0 && affichage){
			System.out.println("La Protection de " + nomSet + " disparait.");
		}
		protection = 0;
		if (murLumiere > 0 && affichage){
			System.out.println("Le Mur Lumiere de " + nomSet + " disparait.");
		}
		murLumiere = 0;
		this.actualiseStats(false, this.matchBrouhaha, affichage);
	}

	public void setAntiSoin(boolean affichage) {
		if (antiSoin > 0 || this.getTalent().equals("Aroma Veil")){
			if (affichage){
				System.out.println("Ca n'affecte pas " + nomSet + ".");
			}
		}
		else{
			antiSoin = 5;
			if (affichage){
				System.out.println(nomSet + " ne peut plus guerir.");
			}
		}
	}

	public boolean isTendu() {
		return tendu;
	}

	public void setTendu(boolean tendu) {
		this.tendu = tendu;
	}

	public boolean isVampigraine() {
		return vampigraine;
	}

	public void setVampigraine(boolean vampigraine, boolean passeClone, boolean affichage) {
		if (!type[0].equals("Grass") && !type[1].equals("Grass") && (!this.isSubstitute() || passeClone) && !this.vampigraine){
			this.vampigraine = true;
			if (affichage){
				System.out.println(nomSet + " est infecte.");
			}
		}
		else{
			if (affichage){
				System.out.println("Ca n'affecte pas " + nomSet + "...");
			}
		}
	}

	public void setCauchemar(boolean affichage) {
		if (statut.equals("Somm")){
		this.cauchemar = true;
		if (affichage){
			System.out.println(this.getNom() + " commence un cauchemar.");
		}
		}
		else{
			if (affichage){
				System.out.println("Ca n'affecte pas " + this.getNom() + ".");
			}
		}
	}

	public void anneauHydro(boolean affichage){
		if (affichage && !this.anneauHydro){
			System.out.println(this.getNom() + " s'entoure d'un voile d'eau.");
		}
		if (affichage && this.anneauHydro){
			System.out.println(this.getNom() + " a deja un Anneau Hydro.");
		}
		anneauHydro = true;
	}

	public void racines(boolean affichage){
		if (affichage && !this.racines){
			System.out.println(this.getNom() + " plante ses racines.");
		}
		if (affichage && this.racines){
			System.out.println(this.getNom() + " a deja plante ses racines.");
		}
		racines = true;
		telekinesis = 0;
		this.volMagnetic = 0;
	}

	public boolean isDecharge() {
		return decharge;
	}

	public void setDecharge(boolean decharge) {
		this.decharge = decharge;
	}

	public String getEntrave() {
		return entrave;
	}

	public boolean isPrlvtDestin() {
		return prlvtDestin;
	}

	public void setPrlvtDestin(boolean prlvtDestin) {
		this.prlvtDestin = prlvtDestin;
	}

	public boolean isVerouillage() {
		return verouillage;
	}

	public void setVerouillage(boolean affichage) {
		this.verouillage = true;
		if (affichage){
			System.out.println(this.getNom() + " vise.");
		}
	}

	public void setRequiem(boolean affichage) {
		if (affichage && requiem >= 0){
			System.out.println(nomSet + " est deja sous l'influence du Requiem.");
		}
		if (requiem == -1){
			requiem = 4;
		}
	}

	public void setMaudit(boolean affichage) {
		this.maudit = true;
	}

	public void voeu(boolean affichage) {
		if (voeu == 0){
			voeu = 2;
			if (affichage){
				System.out.println(nomSet + " fait un voeu.");
			}
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}
	}

	public void prescience (boolean affichage) {
		if (prescience <= 0){
			prescience = 3;
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}
	}

	public int getPrescience() {
		return prescience;
	}

	public void chargeur(boolean affichage) {
		charge = 2;
		if (affichage){
			System.out.println(nomSet + " emmagasine de l'energie.");
		}
		this.boost(4, 1, true, true, affichage);
	}

	public int getCharge() {
		return charge;
	}

	public void stockage(boolean affichage){
		if (this.stockage < 3){
			++stockage;
			if (affichage){
				System.out.println(this.getNom() + " utilise Stockage " + stockage + " fois.");
			}
			this.multiBoost(new String[] {"Def", "SDef"}, new int[] {1,1}, true, true, affichage);
		}
		else{
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}
	}

	public void baille (boolean affichage){
		if (this.statut.equals("OK") && baille == 0 && !substitute){
			baille = 2;
			if (affichage){
				System.out.println(nomSet + " est somnolent.");
			}
		}
		else{
			if (affichage){
				System.out.println("Ca n'affecte pas " + nomSet +"...");
			}
		}
	}

	public boolean isRefletMagik() {
		return refletMagik;
	}

	public void setRefletMagik(boolean affichage) {
		this.refletMagik = true;
		if (affichage){
			System.out.println(nomSet + " s'entoure de Reflet Magik.");
		}
	}

	public boolean isOeilMiracle() {
		return oeilMiracle;
	}

	public void setOeilMiracle(boolean affichage) {
		if (!oeilMiracle){
			this.oeilMiracle = true;
			if (affichage){
				System.out.println(nomSet + " est identifie.");
			}
		}
		else{
			if (affichage){
				System.out.println(nomSet + " est deja identifie.");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, false);
	}

	public boolean isClairvoyance() {
		return this.clairvoyance;
	}

	public void setClairvoyance(boolean affichage) {
		if (!clairvoyance){
			this.clairvoyance = true;
			if (affichage){
				System.out.println(nomSet + " est identifie.");
			}
		}
		else{
			if (affichage){
				System.out.println(nomSet + " est deja identifie.");
			}
		}
		this.actualiseStats(false, this.matchBrouhaha, false);
	}

	public void setNueePoudre(boolean passeClone, boolean affichage){
		if (!this.isSubstitute() || passeClone){
			if (affichage){
				System.out.println("De la poudre hautement inflammable se repand sur " + this.getNom() + ".");
			}
			this.nueePoudre = true;
		}
		else{
			if (affichage){
				System.out.println("Le clone protege " + this.getNom() + " de la poudre.");
			}
		}
	}

	public boolean isNueePoudre() {
		return nueePoudre;
	}

	public boolean isTruant() {
		return truant;
	}

	public boolean isSubstitute() {
		return substitute;
	}

	public void setSubstitute(boolean substitute) {
		this.substitute = substitute;
	}

	public Pokemon getClone() {
		return clone;
	}

	public boolean isMorphing() {
		return morphing;
	}

	public void setPDR(boolean appliquer, boolean affichage) {
		if (!PDR && affichage){
			System.out.println(nomSet + " est pris dans un piege de roc!");
		}

		if (appliquer){
			double efficacite = (double) 1;

			try{
				BufferedReader fis = new BufferedReader (new FileReader ("DataBase/typestable.txt"));
				int NtypeDef1 = -1;
				int NtypeDef2 = -1;
				String[] txt;
				String NumTypeAtq = "";
				for (int i=0; i<=18;++i){
					txt = fis.readLine().split(" ");
					if (txt[1].equals("Rock")){NumTypeAtq += i;}
					if (txt[1].equals(typeOriginal[0])){NtypeDef1 = i;}
					if (txt[1].equals(typeOriginal[1])){NtypeDef2 = i;}
				}

				do{txt = fis.readLine().split(" ");}
				while (!txt[0].equals(NumTypeAtq));

				efficacite = Double.valueOf(txt[Integer.valueOf(NtypeDef1).intValue() + 1]).doubleValue() * Double.valueOf(txt[Integer.valueOf(NtypeDef2).intValue() + 1]).doubleValue() /4;
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Probleme de type" + 1/0);
			}

			if (affichage && efficacite > 0){
				System.out.println(this.nomSet + " est blesse par le piege de roc.");
			}

			this.deltaPV(- (int) (efficacite * this.statsInitiales[0] /8), false, true, 0, affichage);
			if (this.statsInitiales[0] < 8){
				this.deltaPV(- 1, false, true, 0, affichage);
			}

		}

		PDR = true;
	}

	public void plusPicots(boolean appliquer, boolean affichage) {
		++picots;
		if (picots <= 3 && affichage){
			System.out.println(nomSet + " est piege au milieu de " + picots + " rangee(s) de picots.");
		}

		if (appliquer){

			if (!typeOriginal[0].equals("Flying") && !typeOriginal[1].equals("Flying") && !this.talentOriginal.equals("Levitate") && !this.talentOriginal.equals("Magic Guard") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0){
				if (affichage){
					System.out.println(nomSet + " est blesse par les picots.");
				}
				if (picots == 1){
					this.deltaPV(- (int) (this.statsInitiales[0] /8), false, true, 0, affichage);
				}
				if (picots == 2){
					//this.deltaPV((int) (this.statsInitiales[0] *(1/8 - 1/6)), false, true, affichage);
					this.deltaPV(- (int) (this.statsInitiales[0] /6), false, true, 0, affichage);
				}
				if (picots >= 3){
					//this.deltaPV((int) (this.statsInitiales[0] *(1/6 - 1/4)), false, true, affichage);
					this.deltaPV(- (int) (this.statsInitiales[0] /4), false, true, 0, affichage);
				}
				if (this.statsInitiales[0] < 8){
					this.deltaPV(- 1, false, true, 0, affichage);
				}
			}
		}

		if (picots > 3){
			picots = 3;
			if (affichage){
				System.out.println("Il ne peut pas y avoir plus de picots autour de " + nomSet + ".");
			}
		}
	}

	public void setPicots() {
		picots = 3;
	}

	public void plusPicsToxics(boolean appliquer, boolean affichage) {
		++picsToxics;

		if (appliquer){
			if (picsToxics <= 2){
				if (affichage){
					System.out.println(nomSet + " est piege au milieu de " + picsToxics + " rangee(s) de pics toxics.");
				}

				if (!typeOriginal[0].equals("Flying") && !typeOriginal[1].equals("Flying") && !this.talentOriginal.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0){
					if (picsToxics == 1){
						this.setStatut("Psn", false, true, false, this.matchBrouhaha, affichage);
					}
					if (picsToxics == 2){
						this.setStatut("GPsn", false, true, false, this.matchBrouhaha, affichage);
					}
				}
			}
			if (picsToxics > 2){
				picsToxics = 2;
				if (affichage){
					System.out.println("Il ne peut pas y avoir plus de pics toxics autour de " + nomSet);
				}
			}
		}
	}

	public void setPicsToxics(){
		picsToxics = 2;
	}

	public void setToileGluante(boolean appliquer, boolean affichage) {
		if (!toileGluante && affichage){
			System.out.println(nomSet + " est pris dans une toile gluante !");
		}
		toileGluante = true;

		if (appliquer && (gravite || (!typeOriginal[0].equals("Flying") && !typeOriginal[1].equals("Flying") && !this.talentOriginal.equals("Levitate") && !this.getObjet().equals("Air Balloon") && telekinesis == 0 && volMagnetic == 0))){
			this.boost(5, -1, true, false, affichage);
		}
	}

	public Attaque[] getMoves() {
		return moves;
	}

	public void setMoves(Attaque[] moves) {
		this.moves = moves;
	}

	public String getPC() {
		return PC;
	}

	public void setPC(String pC) {
		PC = pC;
	}


	// Outils pour l'IA

	public boolean canMevo() {
		Pokemon clone = this.clone();
		canMevo = clone.MegaEvolution(false);
		return canMevo;
	}

	public Pokemon cloneLent(){
		Cloner cloner = new Cloner();
		return (Pokemon) cloner.deepClone(this);
	}

	public Pokemon clone(){
		return (Pokemon) DeepCopy.copy(this);
	}

	public float distance(Pokemon pkmn2){
		float distance = 0;
		if (!pkmn2.getTalent().equals(this.getTalent())){
			++distance;
		}
		if (!pkmn2.getObjet().equals(this.getObjet())){
			++distance;
		}
		for (int i=0; i<6; ++i){
			distance += Math.abs(((float)pkmn2.getStatsInitiales()[i]-(float)this.getStatsInitiales()[i])/(float)this.getStatsInitiales()[i]);
		}

		for (int i=0; i<4; ++i){
			boolean memeAttaque = false;
			for (int j=0; j<4; ++j){
				if (pkmn2.getMoves()[j].getNom().equals(this.getMoves()[i].getNom())){
					memeAttaque = true;
				}
			}
			if (!memeAttaque){
				++distance;
			}
		}

		return distance;
	}

	@Override
	public String toString() {
		return "\n" + nom + "\n type=" + Arrays.toString(type)
				+ "\n baseStats=" + Arrays.toString(baseStats) + "\n poids="
				+ poidsModifie + "\n sexe=" + sexe + "\nset=" + set + "\n utilisation de l'espece=" + utilisationEspece + "\n utilisation du set=" + utilisationSet + "\n competitif=" + competitif + "\n commentaire=" + commentaire + "\n talent=" + talent + "\n nature="
				+ nature + "\n objet=" + objet + "\n EVs=" + Arrays.toString(EVs)
				+ "\n statsInitiales=" + Arrays.toString(statsInitiales) + "\n PC=" + PC
				+ "\nattaques=" + Arrays.toString(moves);
	}

	public void print() {
		System.out.println(this.toString());
	}
}