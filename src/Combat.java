import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;


public class Combat {

	private Joueur Joueur1;
	private Joueur Joueur2;
	private Joueur Bot = new Joueur ("Cyber-Guillaume", false);
	private Pokemon Pokemon1;
	private Pokemon Pokemon2;
	private boolean affichage;
	private String meteo = "claire";
	private int compteurMeteo;
	private String terrain = "normal";
	private int compteurTerrain = 0;
	private int gravite = 0;
	private int zoneMagique = 0;
	private int distorsion = 0;
	private int tourniquet = 0;
	private boolean echoUtilise = false;
	private int compteurEcho = 0;
	private boolean brouhaha = false;
	private Attaque derniereAttaque = new Attaque("(No Move)");
	private boolean briseMoule = false;

	public Combat (Joueur J1, Joueur J2, Pokemon pkmn1, Pokemon pkmn2, String met, int comptMet, String terr, int comptTerr, int comptGrav, int zoneMag, int disto, int tourniq, boolean aff){
		Joueur1 = J1;
		Joueur2 = J2;

		affichage = aff;
		if (J1.isHumain() || J2.isHumain()){
			affichage = true;
		}

		Pokemon1 = pkmn1;
		Pokemon2 = pkmn2;

		meteo = met;
		compteurMeteo = comptMet;
		terrain = terr;
		gravite = comptGrav;
		zoneMagique = zoneMag;
		distorsion = disto;
		tourniquet = tourniq;
	}

	public void attaque(Pokemon attaquant, Attaque attaque, Pokemon defenseur, boolean passeFiltres) {
		Random alea = new Random();
		boolean reussi = true;
		attaquant.setPrlvtDestin(false);

		float puissance = attaque.getPuissance();
		float boostPuissance1 = 1;
		String type = attaque.getType();
		double efficacite = (double) 1;

		if (attaque.getNom().equals("Echoed Voice")){
			if (compteurEcho <= 0){puissance = 40;}
			if (compteurEcho == 1){puissance = 80;}
			if (compteurEcho == 2){puissance = 160;}
			if (compteurEcho >= 3){puissance = 200;}
			++compteurEcho;
			echoUtilise = true;
		}

		if (attaque.getNom().equals("Hidden Power")){
			type = attaquant.getPC();
		}

		if (attaque.getNom().equals("Natural Gift")){
			if (attaquant.getObjet().endsWith(" Berry")){
				boolean trouve = false;
				if (attaquant.getObjet().equals("Chesto Berry")){
					trouve = true;
					puissance = 80;
					attaque.setType("Water");
				}
				if (attaquant.getObjet().equals("Liechi Berry")){
					trouve = true;
					puissance = 100;
					attaque.setType("Grass");
				}
				if (attaquant.getObjet().equals("Lum Berry")){
					trouve = true;
					puissance = 80;
					attaque.setType("Flying");
				}
				if (attaquant.getObjet().equals("Salac Berry")){
					trouve = true;
					puissance = 100;
					attaque.setType("Fighting");
				}
				if (attaquant.getObjet().equals("Sitrus Berry")){
					trouve = true;
					puissance = 80;
					attaque.setType("Psychic");
				}
				if (attaquant.getObjet().equals("Watmel Berry")){
					trouve = true;
					puissance = 80;
					attaque.setType("Fire");
				}
				if (!trouve){
					System.out.println("Baie introuvable : " + attaquant.getObjet());
					System.out.println(1/0);
				}
				attaquant.setObjet("(No Item)", true, true, false);
			}
		}

		if (attaque.getNom().equals("Secret Power")){
			if (this.terrain.equals("normal") || this.terrain.equals("Electric")){
				attaque.setCategorie("4");
				attaque.setEffet("Para");
			}
			if (this.terrain.equals("Misty")){
				attaque.setCategorie("6");
				attaque.setEffet("SAtq");
				attaque.setVariations(new int[] {-1});
			}
		}

		if (attaque.getNom().equals("Trump Card")){
			if (attaque.getPP() <= 5){puissance = 40;}
			if (attaque.getPP() <= 4){puissance = 50;}
			if (attaque.getPP() <= 3){puissance = 60;}
			if (attaque.getPP() <= 2){puissance = 75;}
			if (attaque.getPP() <= 1){puissance = 190;}
		}

		if (attaque.getNom().equals("Weather Ball")){
			if (this.meteo.equals("claire")){
				puissance = 50;
				type = "Normal";
			}
			if (this.meteo.endsWith("soleil")){
				puissance = 100;
				type = "Fire";
			}
			if (this.meteo.contains("pluie")){
				puissance = 100;
				type = "Water";
			}
			if (this.meteo.equals("grele")){
				puissance = 100;
				type = "Ice";
			}
			if (this.meteo.equals("tempete de sable")){
				puissance = 100;
				type = "Rock";
			}
		}

		if (attaque.getNom().equals("Judgment") && attaquant.getObjet().contains(" Plate")){
			boolean found = false;
			if (attaquant.getObjet().startsWith("Pixie Plate")){
				type = "Fairy";
				found = true;
			}
			if (attaquant.getObjet().startsWith("Spooky Plate")){
				type = "Ghost";
				found = true;
			}
			if (!found){
				System.out.println("Plaque non-trouvée : " + attaquant.getObjet());
				System.out.println(1/0);
			}
		}

		if (attaquant.getTalent().equals("Aerilate") && type.equals("Normal")){
			boostPuissance1 *= 1.3;
			type = "Flying";
		}
		if (attaquant.getTalent().equals("Pixilate") && type.equals("Normal")){
			boostPuissance1 *= 1.3;
			type = "Fairy";
		}
		if (attaquant.getTalent().equals("Refrigerate") && type.equals("Normal")){
			boostPuissance1 *= 1.3;
			type = "Ice";
		}

		if (attaquant.getCharge() > 0 && attaque.getType().equals("Electric")){
			boostPuissance1 *= 2;
			if (affichage){
				System.out.println("Chargeur booste la puissance !");
			}
		}

		if (attaquant.getObjet().equals("Metronome")){
			try{
				if (attaque.equals(attaquant.getDerniereAttaque())){
					if (affichage){
						System.out.println("Metronome : " + attaquant.getMetronome());
					}
					boostPuissance1 *= (1 + 0.2*attaquant.getMetronome());
					attaquant.plusMetronome();
				}
				else{
					attaquant.resetMetronome();
				}
			}
			catch(Exception e){
				attaquant.resetMetronome();
			}
		}

		if (!passeFiltres){
			attaquant.setDejaAttaque(true);
		}

		if (attaquant.isDecharge()){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " doit se recharger.");
			}
			attaquant.setDecharge(false);
		}

		if (reussi && attaquant.getEncore() > 0){
			try{
				if (attaquant.getDerniereAttaque().getPP() > 0){
					attaque = attaquant.getDerniereAttaque();
				}
				else{
					attaque = new Attaque("Struggle");
				}
			}
			catch(Exception e){}
		}

		try{
			if (reussi && attaquant.getEntrave().equals(attaque.getNom())){
				reussi = false;
				if (affichage){
					System.out.println("\nIl y a une entrave sur " + attaque.getNom() + ".");
				}
			}
		}
		catch (Exception e){
			System.out.println(attaquant.getEntrave());
			System.out.println(attaque.getNom());
			System.out.println(1/0);
		}

		if ((attaquant.isAntiAir() || gravite > 0) && (attaque.getNom().equals("Bounce") || attaque.getNom().equals("Fly") || attaque.getNom().endsWith("Jump Kick") || attaque.getNom().equals("Sky Drop"))){
			reussi = false;
			if (affichage){
				System.out.println(attaquant.getNom() + " est cloue au sol.");
			}
		}

		if (defenseur.getChargement().equals("Sky Drop")){
			reussi = false;
			if (affichage){
				System.out.println("/n" + attaquant.getNom() + " ne peut pas bouger dans les airs.");
			}
		}

		if (reussi && attaquant.getTalent().equals("Stance Change")){
			if (attaque.getNom().equals("King's Shield")){
				attaquant.transformation("Bouclier", affichage);
			}
			if (attaque.getPuissance() > 0){
				attaquant.transformation("Epee", affichage);
			}
		}

		if (reussi && !passeFiltres && attaquant.getStatut().equals("Gel")){
			if (alea.nextInt(100)+1 > 20){
				reussi = false;
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " est completement gele !");
				}
			}
			else{
				attaquant.setStatut("OK", true, true, false, brouhaha, false);
				if (affichage){
					System.out.print("\n" + attaquant.getNom() + " est degele !");
				}
			}
		}

		if (reussi && !passeFiltres && attaquant.getStatut().equals("Para") && (alea.nextInt(100)+1) <= 25){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " est completement paralyse !");
			}
		}

		if (reussi && !passeFiltres && attaquant.getStatut().equals("Somm")){
			if (attaquant.getTourStatut() == 0){
				attaquant.setStatut("OK", true, true, false, brouhaha, false);
				if (affichage){
					System.out.print("\n" + attaquant.getNom() + " se reveille !");
				}
			}
			else{
				reussi = false;
				attaquant.setTourStatut(attaquant.getTourStatut()-1);
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " dort profondement.");
				}
				if (attaque.getNom().equals("Sleep Talk")){
					reussi = true;
				}
			}
		}
		
		if (reussi && attaquant.isTruant()){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " flemmarde.");
			}
		}

		if (reussi && !passeFiltres && attaquant.isTrouille()){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " a la trouille!");
			}
			if (attaquant.getTalent().equals("Steadfast")){
				attaquant.multiBoost(new String[] {"Vit"}, new int[] {1}, true, true, affichage);
			}
		}

		if (attaque.getNom().equals("Focus Punch") && !attaquant.isFocus()){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " est deconcentre et ne peut pas attaquer.");
			}
		}

		if (reussi && !passeFiltres && attaquant.isAmoureux()){
			if (affichage){
				System.out.print("\n" + attaquant.getNom() + " est amoureux de " + defenseur.getNom() + ".");
			}
			if ((alea.nextInt(100)+1) <= 50){
				reussi = false;
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " est paralyse par l'amour !");
				}
			}
		}

		if (reussi && !passeFiltres && attaquant.getConfus() > 0){
			attaquant.setConfus(attaquant.getConfus()-1);
			if (attaquant.getConfus() == 0 && attaquant.getStatsModifiees()[0] > 0){
				if (affichage){
					System.out.print("\n" + attaquant.getNom() + " sort de sa confusion.");
				}
			}
			if (affichage && attaquant.getConfus() > 0){
				System.out.print("\n" + attaquant.getNom() + " est confus.");
			}
			if (alea.nextInt(2) == 0 && attaquant.getConfus() > 0){
				reussi = false;
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " se blesse dans sa confusion.");
				}
				int degats = (int) (((42 * attaquant.getStatsModifiees()[1] * 40 / 50 / attaquant.getStatsModifiees()[2]) + 2) * (85 + alea.nextInt(16))/100);
				attaquant.deltaPV(-degats, false, true, 0, affichage);
			}
		}

		if (reussi && !passeFiltres && (attaquant.getTaunted() > 0 || attaquant.isAssaut()) && !(attaque.getCategorie().equals("0") || attaque.getCategorie().equals("4") || attaque.getCategorie().equals("6") || attaque.getCategorie().equals("7") || attaque.getCategorie().equals("8"))){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " ne peut pas utiliser " + attaque.getNom() + " apres Provoc.");
			}
		}

		if (reussi && !passeFiltres && attaque.isPossessif()){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " ne peut pas utiliser " + attaque.getNom() + " apres Possessif.");
			}
		}

		if (reussi && (attaque.getNom().equals("Explosion") || attaque.getNom().equals("Self-Destruct")) && (defenseur.getTalent().equals("Damp") || attaquant.getTalent().equals("Damp"))){
			reussi = false;
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " ne peut pas utiliser " + attaque.getNom() + " a cause de la Moiteur.");
			}
		}

		if (!reussi){
			attaquant.setChargement("-");
		}

		if (reussi && attaquant.getTalent().equals("Protean")){
			if (!(attaquant.getType()[0].equals(type) && attaquant.getType()[1].equals("-"))){
				if (affichage){
					System.out.println("");
				}
				attaquant.setType(new String[] {type, "-"}, affichage);
			}
		}

		boolean attaqueLancee = false;

		if (reussi && attaquant.getChargement().equals("-") && !attaque.getNom().equals("Prescience")){
			if (affichage){
				System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + " !");
			}
			this.derniereAttaque = new Attaque (attaque.getNom());
			if (!passeFiltres && attaquant.getEnColere() == 0){
				attaque.retirePP(1);
				if (defenseur.getTalent().equals("Pressure") && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
					attaque.retirePP(1);
				}
			}
			if (!passeFiltres && !attaque.getNom().equals("Struggle")){
				attaquant.setDerniereAttaque(attaque);
			}
			if (attaquant.getObjet().startsWith("Choice ") && !attaquant.getObjet().endsWith(" (inutilisable)")){
				attaquant.setChoiced(true);
			}
			attaqueLancee = true;
		}
		
		if (defenseur.getStatsModifiees()[0] <= 0 && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
			reussi = false;
			if (affichage){
				System.out.println("Mais il n'y a pas de cible...");
			}
		}

		boolean clone = defenseur.isSubstitute() && !attaquant.getTalent().equals("Infiltrator");
		boolean passeClone = attaque.isSonore() || attaquant.getTalent().equals("Infiltrator");
		if (attaque.getCible().startsWith("user")){
			passeClone = true;
		}

		if (attaquant.isNueePoudre() && attaque.getType().equals("Fire")){
			reussi = false;
			if (affichage){
				System.out.println("La poudre explose !");
			}
			attaquant.deltaPV(-(int) (attaquant.getStatsInitiales()[0] /4), false, true, 0, affichage);
			if (attaquant.getStatsInitiales()[0] < 8){
				attaquant.deltaPV(- 1, false, true, 0, affichage);
			}
		}

		if (reussi && !passeFiltres && attaque.getNom().equals("Me First")){
			reussi = false;
			if (!defenseur.isDejaAttaque() && (defenseur.getActionPrevue().getAttaque().getCategorie().equals("0") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("4") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("6") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("7") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("8")) && !defenseur.getActionPrevue().getAttaque().getNom().equals("Chatter") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Counter") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Covet") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Focus Punch") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Me First") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Metal Burst") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Mirror Coat") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Mirror Move") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Struggle") && !defenseur.getActionPrevue().getAttaque().getNom().equals("Thief")){
				try{
					System.out.println(defenseur.getActionPrevue().getAttaque().toString());
					Attaque newAttaque = defenseur.getActionPrevue().getAttaque().clone();
					newAttaque.setMeFirst(true);
					this.attaque(attaquant, newAttaque, defenseur, true);
				}
				catch (Exception e){
					e.printStackTrace();
					System.out.println(defenseur.getActionPrevue().getAttaque().toString());
				}
			}
			else{
				if (affichage){
					System.out.println("Mais cela echoue...");
				}
			}
		}

		if (reussi && !passeFiltres && attaque.getNom().equals("Mirror Move")){
			try{
				Attaque newAttaque = defenseur.getDerniereAttaque().clone();
				newAttaque.setMeFirst(true);
				this.attaque(attaquant, newAttaque, defenseur, true);
			}
			catch (Exception e){}
		}

		if (reussi && !passeFiltres && attaque.getNom().equals("Copycat")){
			if (!derniereAttaque.getNom().equals("(No Move")){
				this.attaque(attaquant, derniereAttaque, defenseur, true);
			}
			else{
				if (affichage){
					System.out.println("Mais cela echoue.");
				}
			}
		}

		if (reussi && !passeFiltres && (defenseur.getTalent().equals("Magic Bounce") || defenseur.isRefletMagik()) && !(attaquant.getActionPrevue().getAttaque().getCategorie().equals("0") || attaquant.getActionPrevue().getAttaque().getCategorie().equals("4") || attaquant.getActionPrevue().getAttaque().getCategorie().equals("6") || attaquant.getActionPrevue().getAttaque().getCategorie().equals("7") || attaquant.getActionPrevue().getAttaque().getCategorie().equals("8")) && attaque.getCible().startsWith("adv") && !attaque.getNom().equals("Pain Split")){
			if (affichage && defenseur.getTalent().equals("Magic Bounce")){
				System.out.print("Miroir Magik renvoie l'attaque !");
				reussi = false;
			}
			if (affichage && defenseur.isRefletMagik() && !defenseur.getTalent().equals("Magic Bounce")){
				System.out.print("Reflet Magik renvoie l'attaque !");
			}
			reussi = false;
			this.attaque(defenseur, attaque, attaquant, true);
		}

		if (reussi && !passeFiltres && defenseur.isSaisie() && attaque.getCible().startsWith("user")){
			if (affichage){
				System.out.print(defenseur.getNom() + " vole l'effet de l'attaque !");
			}
			reussi = false;
			this.attaque(defenseur, attaque, attaquant, true);
		}

		if (reussi && (attaque.getNom().equals("Explosion") || attaque.getNom().equals("Self-Destruct"))){
			attaquant.fixePV(0, true, affichage);
		}

		if (reussi && (attaque.getNom().equals("Fake Out") || attaque.getNom().equals("Mat Block")) && !attaquant.isSwitchPrecedent()){
			reussi = false;
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}

		if (reussi && attaque.getNom().equals("Dream Eater") && !defenseur.getStatut().equals("Somm")){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " ne dort pas...");
			}
		}

		if (reussi && attaque.getNom().equals("Venom Drench") && !(defenseur.getStatut().equals("Psn") || defenseur.getStatut().equals("GPsn"))){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " n'est pas empoisonne.");
			}
		}

		if (reussi && attaque.getNom().equals("Last Resort") && attaquant.getAttaquesUtilisees().size() < 4){
			reussi = false;
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}

		if (reussi && attaque.getNom().equals("Synchronoise")){
			if(!(attaquant.getType()[0].equals(defenseur.getType()[0]) || attaquant.getType()[0].equals(defenseur.getType()[1]) || attaquant.getType()[1].equals(defenseur.getType()[0]) || attaquant.getType()[1].equals(defenseur.getType()[1]))){

				reussi = false;
				if (affichage){
					System.out.println("Ca n'affecte pas " + defenseur.getNom() + "...");
				}
			}
		}

		if (reussi && attaque.getNom().equals("Freeze Shock")){
			if(!attaquant.getChargement().equals("Freeze Shock")){
				attaquant.setChargement("Freeze Shock");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " prepare son attaque.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && attaque.getNom().equals("Geomancy")){
			if(!attaquant.getChargement().equals("Geomancy")){
				attaquant.setChargement("Geomancy");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " rayonne.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && attaque.getNom().equals("Bounce")){
			if(!attaquant.getChargement().equals("Bounce")){
				attaquant.setChargement("Bounce");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " rebondit haut.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}
		if (reussi && attaque.getNom().equals("Fly")){
			if(!attaquant.getChargement().equals("Fly")){
				attaquant.setChargement("Fly");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " s'envole.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}
		if (reussi && attaque.getNom().equals("Dig")){
			if(!attaquant.getChargement().equals("Dig")){
				attaquant.setChargement("Dig");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " creuse un trou.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}
		if (reussi && attaque.getNom().equals("Dive")){
			if(!attaquant.getChargement().equals("Dive")){
				attaquant.setChargement("Dive");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " plonge sous l'eau.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}
		if (reussi && attaque.getNom().equals("Phantom Force")){
			if(!attaquant.getChargement().equals("Phantom Force")){
				attaquant.setChargement("Phantom Force");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " disparait.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}
		if (reussi && attaque.getNom().equals("Shadow Force")){
			if(!attaquant.getChargement().equals("Shadow Force")){
				attaquant.setChargement("Shadow Force");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " disparait.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && attaque.getNom().equals("Sky Drop")){
			if (!attaquant.getChargement().equals("Sky Drop")){
				reussi = false;
				if (defenseur.isSubstitute() || defenseur.getPoids() >= 200){
					if (affichage){
						System.out.println("Mais cela echoue.");
					}
				}
				else{
					attaquant.setChargement("Sky Drop");
					if (affichage){
						System.out.println(attaquant.getNom() + " emporte "+ defenseur.getNom() + " dans le ciel.");
					}
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && (attaque.getNom().equals("Solar Beam") || attaque.getNom().equals("SolarBeam")) && !meteo.endsWith("soleil")){
			if(!attaquant.getChargement().equals("SolarBeam")){
				attaquant.setChargement("SolarBeam");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " absorbe les rayons du Soleil.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && attaque.getNom().equals("Skull Bash")){
			if(!attaquant.getChargement().equals("Skull Bash")){
				reussi = false;
				attaquant.setChargement("Skull Bash");
				if (affichage){
					System.out.println(attaquant.getNom() + " replie la tete.");
				}
				attaquant.multiBoost(new String[] {"Def"}, new int[] {1}, true, true, affichage);
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (reussi && attaque.getNom().equals("Sky Attack")){
			if(!attaquant.getChargement().equals("Sky Attack")){
				attaquant.setChargement("Sky Attack");
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " s'entoure d'une lumiere intense.");
				}
			}
			else{
				if (affichage){
					System.out.println("\n" + attaquant.getNom() + " utilise " + attaque.getNom() + ".");
				}
				attaquant.setChargement("-");
			}
		}

		if (!attaquant.getChargement().equals("-") && !attaquant.getChargement().equals("Sky Drop") && attaquant.getObjet().equals("Power Herb")){
			attaquant.setObjet("(No Item)", true, true, false);
			reussi = true;
			if (affichage){
				System.out.println(attaquant.getNom() + " est plein d'energie grace a l'Herbe Pouvoir !");
				System.out.println(attaquant.getNom() + " utilise " + attaque.getNom() + ".");
			}
			attaquant.setChargement("-");
		}

		if (reussi && (defenseur.getChargement().equals("Bounce") || defenseur.getChargement().equals("Fly") || defenseur.getChargement().equals("Sky Drop")) && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && !attaque.getCible().endsWith("ield")){
			if (attaque.getNom().equals("Gust") || attaque.getNom().equals("Hurricane") || attaque.getNom().equals("Sky Uppercut") || attaque.getNom().equals("Smack Down") || attaque.getNom().equals("Thunder") || attaque.getNom().equals("Twister") || attaque.getNom().equals("Whirlwind") || attaque.getCategorie().equals("11")){
				puissance *= 2;
			}
			else{
				if (!(attaquant.getTalent().equals("No Guard") || defenseur.getTalent().equals("No Guard") || attaque.getCategorie().equals("11"))){
					reussi = false;
					if (affichage){
						System.out.println(defenseur.getNom() + " evite l'attaque !");
					}
				}
			}
		}
		if (reussi && (defenseur.getChargement().equals("Phantom Force") || defenseur.getChargement().equals("Shadow Force")) && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
			if (!(attaquant.getTalent().equals("No Guard") || defenseur.getTalent().equals("No Guard") || attaque.getCategorie().equals("11"))){
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " evite l'attaque !");
				}
			}
		}
		if (reussi && defenseur.getChargement().equals("Dive") && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
			if (attaque.getNom().equals("Surf") || attaque.getCategorie().equals("11")){
				puissance *= 2;
			}
			else{
				if (!(attaquant.getTalent().equals("No Guard") || defenseur.getTalent().equals("No Guard"))){
					reussi = false;
					if (affichage){
						System.out.println(defenseur.getNom() + " evite l'attaque !");
					}
				}
			}
		}
		if (reussi && defenseur.getChargement().equals("Dig") && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
			if (attaque.getNom().equals("Earthquake") || attaque.getNom().equals("Magnitude") || attaque.getCategorie().equals("11")){
				puissance *= 2;
			}
			else{
				if (!(attaquant.getTalent().equals("No Guard") || defenseur.getTalent().equals("No Guard"))){
					reussi = false;
					if (affichage){
						System.out.println(defenseur.getNom() + " evite l'attaque !");
					}
				}
			}
		}

		if (reussi && (attaque.getNom().endsWith("Powder") || attaque.getNom().endsWith("Spore")) && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && (defenseur.getType()[0].equals("Grass") || defenseur.getType()[1].equals("Grass") || defenseur.getTalent().equals("Overcoat") || defenseur.getObjet().equals("Safety Goggles"))){
			reussi = false;
			if (affichage){
				if (defenseur.getTalent().equals("Overcoat")){
					System.out.println(defenseur.getNom() + " est protege par Envelocape.");
				}
				else{
					if (defenseur.getObjet().equals("Safety Goggles")){
						System.out.println(defenseur.getNom() + " est protege par ses lunettes de protection.");
					}
					else{
						System.out.println("Ca n'affecte pas " + defenseur.getNom() + "...");
					}
				}
			}
		}

		if (reussi && attaquant.getActionPrevue().getAttaque().getNom().equals("Sucker Punch") && !(!defenseur.isDejaAttaque() && (defenseur.getActionPrevue().getAttaque().getCategorie().equals("0") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("4") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("6") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("7") || defenseur.getActionPrevue().getAttaque().getCategorie().equals("8")))){
			reussi = false;
			if (affichage){
				System.out.println("Mais cela echoue...");
			}
		}

		int precision = attaque.getPrecision();

		if (meteo.contains("pluie") && (attaque.getNom().equals("Hurricane") || attaque.getNom().equals("Thunder")) && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
			precision = 100;
		}
		if (meteo.equals("grele") && attaque.getNom().equals("Blizzard") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
			precision = 100;
		}
		
		if (attaquant.isVerouillage()){
			precision = 101;
		}

		if (defenseur.getTalent().equals("Wonder Skin") && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && !attaque.getCategorie().equals("11") && attaque.getPuissance() < 1 && !(attaque.getNom().equals("Future Sight") || attaque.getNom().equals("Pain Split"))){
			precision = 50;
		}

		if (attaquant.getTalent().equals("Hustle") && attaque.getClass().equals("phys")){
			precision *= 0.8;
		}
		if (attaquant.getObjet().equals("Zoom Lens") && (defenseur.isDejaAttaque() || defenseur.isDejaSwitche())){
			precision *= 1.2;
		}
		if (reussi && (attaque.getNom().equals("Detect") || attaque.getNom().equals("Endure") || attaque.getNom().equals("Protect") || attaque.getNom().equals("King's Shield") || attaque.getNom().equals("Spiky Shield"))){
			precision = 100/(attaquant.getCompteurAbrite());
		}

		if (reussi && defenseur.isAbrite() && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && !attaque.getCategorie().equals("11") && !attaque.getNom().equals("Defog") && !attaque.getNom().equals("Future Sight") && !attaque.getNom().equals("Roar") && !attaque.getNom().equals("Whirlwind")){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit l'Abri.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
			}
		}

		if (reussi && attaque.getNom().equals("Sky Drop") && (defenseur.getType()[0].equals("Flying") || defenseur.getType()[1].equals("Flying")) && !defenseur.isAntiAir()){
			reussi = false;
			if (affichage){
				System.out.println("Ca n'affecte pas " + defenseur.getNom() + ".");
			}
		}

		if (reussi && defenseur.getTalent().equals("Soundproof") && attaque.isSonore()){
			reussi = false;
			if (affichage){
				System.out.println("Anti-Bruit immunise contre l'attaque.");
			}
		}

		if (reussi && defenseur.getTalent().equals("Flash Fire") && type.equals("Fire")){
			reussi = false;
			defenseur.setTorche(true);
			if (affichage){
				System.out.println("Torche absorbe le feu.");
			}
		}

		if (reussi && defenseur.getTalent().equals("Motor Drive") && type.equals("Electric")){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " absorbe l'attaque.");
			}
			defenseur.multiBoost(new String[] {"Vit"}, new int[] {1}, passeClone, false, affichage);
		}
		
		if (reussi && defenseur.getTalent().equals("Sap Sipper") && type.equals("Grass")){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " mange l'attaque.");
			}
			defenseur.multiBoost(new String[] {"Atq"}, new int[] {1}, passeClone, false, affichage);
		}

		if (reussi && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && ((defenseur.getTalent().equals("Volt Absorb") && type.equals("Electric")) || ((defenseur.getTalent().equals("Dry Skin") || defenseur.getTalent().equals("Water Absorb")) && type.equals("Water")))){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " absorbe l'attaque.");
			}
			defenseur.deltaPV((int)(defenseur.getStatsInitiales()[0] / 4), false, false, 0, affichage);
		}

		if (reussi && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && (((defenseur.getTalent().equals("Lightningrod") || defenseur.getTalent().equals("Lightning Rod")) && type.equals("Electric")) || (defenseur.getTalent().equals("Storm Drain") && type.equals("Water")))){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " absorbe l'attaque.");
			}
			defenseur.multiBoost(new String[] {"SAtq"}, new int[] {1}, false, true, affichage);
		}

		if (reussi && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && defenseur.getTalent().equals("Bulletproof")
				&& (attaque.getNom().equals("Acid Spray") || attaque.getNom().equals("Aura Sphere") || attaque.getNom().equals("Barrage") || attaque.getNom().equals("Bullet Seed") || attaque.getNom().equals("Egg Bomb") || attaque.getNom().equals("Electro Ball") || attaque.getNom().equals("Energy Ball") || attaque.getNom().equals("Focus Blast") || attaque.getNom().equals("Gyro Ball") || attaque.getNom().equals("Ice Ball") || attaque.getNom().equals("Magnet Bomb") || attaque.getNom().equals("Mist Ball") || attaque.getNom().equals("Mud Bomb") || attaque.getNom().equals("Octazooka") || attaque.getNom().equals("Rock Wrecker") || attaque.getNom().equals("Searing Shot") || attaque.getNom().equals("Seed Bomb") || attaque.getNom().equals("Shadow Ball") || attaque.getNom().equals("Sludge Bomb") || attaque.getNom().equals("Weather Ball") || attaque.getNom().equals("Zap Cannon"))){
			reussi = false;
			if (affichage){
				System.out.println(defenseur.getNom() + " est protégé par Pare-Balles.");
			}
		}
		
		if (reussi && defenseur.isPrevention() && (attaque.getPriorite() > 0 || (attaque.getPriorite() >= 0 && ((attaquant.getTalent().equals("Gale Wings") && attaque.getType().equals("Flying")) || (attaquant.getTalent().equals("Prankster") && attaque.getPuissance() > 0))))){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				defenseur.detruitAbri();
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit la Prevention.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
			}
		}
		
		if (reussi && defenseur.isGardeLarge() && attaque.getCible().startsWith("all")){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				defenseur.detruitAbri();
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit la Garde Large.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
			}
		}

		if (reussi && defenseur.isBouclierRoyal() && attaque.getPuissance() > 0){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				defenseur.detruitAbri();
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit le Bouclier Royal.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
				if (attaque.isContact()){
					attaquant.multiBoost(new String[] {"Atq"}, new int[] {-2}, true, false, affichage);
				}
			}
		}

		if (reussi && defenseur.isPicoDefense() && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && !attaque.getCategorie().equals("11") && !attaque.getNom().equals("Defog") && !attaque.getNom().equals("Future Sight") && !attaque.getNom().equals("Roar") && !attaque.getNom().equals("Whirlwind")){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				defenseur.detruitAbri();
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit le Bouclier Royal.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
				if (attaque.isContact()){
					attaquant.deltaPV(-(int) (attaquant.getStatsInitiales()[0] /8), false, true, 0, affichage);
					if (attaquant.getStatsInitiales()[0] < 8){
						attaquant.deltaPV(- 1, false, true, 0, affichage);
					}
				}
			}
		}

		if (reussi && defenseur.isTatami() && attaque.getPuissance() > 0){
			if (attaque.getNom().equals("Feint") || attaque.getNom().equals("Phantom Force") || attaque.getNom().equals("Shadow Force")){
				defenseur.detruitAbri();
				if (affichage){
					System.out.println(attaquant.getNom() + " detruit le tatami.");
				}
			}
			else{
				reussi = false;
				if (affichage){
					System.out.println(defenseur.getNom() + " se protege.");
				}
			}
		}

		if (attaque.getNom().equals("Fling")){
			boolean trouve = false;

			if (attaquant.getObjet().equals("Choice Band") || attaquant.getObjet().equals("Choice Scarf") || attaquant.getObjet().equals("Choice Specs") || attaquant.getObjet().equals("Expert Belt") || attaquant.getObjet().equals("Focus Sash") || attaquant.getObjet().equals("Leftovers") || attaquant.getObjet().endsWith(" Berry")){
				trouve = true;
				attaque.setPuissance(10);
			}
			if (attaquant.getObjet().equals("Eject Button") || attaquant.getObjet().equals("Life Orb")){
				trouve = true;
				attaque.setPuissance(30);
			}
			if (attaquant.getObjet().equals("Rocky Helmet")){
				trouve = true;
				attaque.setPuissance(60);
			}
			if (attaquant.getObjet().equals("Flame Orb")){
				trouve = true;
				attaque.setPuissance(30);
				attaque.setCategorie("4");
				attaque.setEffet("Brul");
				attaque.setTaux(100);
			}
			if (attaquant.getObjet().equals("Toxic Orb")){
				trouve = true;
				attaque.setPuissance(30);
				attaque.setCategorie("4");
				attaque.setEffet("GPsn");
				attaque.setTaux(100);
			}
			if (attaquant.getObjet().equals("Light Ball")){
				trouve = true;
				attaque.setPuissance(30);
				attaque.setCategorie("4");
				attaque.setEffet("Para");
				attaque.setTaux(100);
			}
			if (attaquant.getObjet().endsWith("(inutilisable)")){
				trouve = true;
				reussi = false;
				if (affichage){
					System.out.println("La Zone Magique empeche d'utiliser l'objet.");
				}
			}
			if (attaquant.getObjet().equals("(No Item)")){
				trouve = true;
				reussi = false;
				if (affichage){
					System.out.println(attaquant.getNom() + " ne tient pas d'objet...");
				}
			}

			if (reussi && affichage){
				System.out.println(attaquant.getNom() + " lance " + attaquant.getObjet() + ".");
			}

			if (!trouve){
				System.out.println("Degommage, objet introuvable : " + attaquant.getObjet());
				System.out.println(1/0);
			}
		}

		if (reussi && attaque.getNom().equals("Natural Gift") && !attaquant.getObjet().endsWith(" Berry")){
			reussi = false;
			if (affichage){
				System.out.println("Il faut une baie pour reussir cette attaque!");
			}
		}

		if (attaque.getNom().equals("Curse") && (attaquant.getType()[0].equals("Ghost") || attaquant.getType()[1].equals("Ghost"))){
			attaque.setCategorie("13");
		}

		if (reussi && precision <= 100 && !attaquant.getTalent().equals("No Guard") && !defenseur.getTalent().equals("No Guard")){
			if (attaquant.getTalent().equals("Victory Star")){
				precision *= 1.1;
			}
			float evasion = defenseur.getStatsModifiees()[7];
			if (attaquant.getTalent().equals("Keen Eye") && evasion > 1){
				evasion = 1;
			}
			if (attaquant.getTalent().equals("Unaware") || attaque.getNom().equals("Sacred Sword")){
				evasion = defenseur.getStatsInitiales()[7] * defenseur.getStatsModifieurs()[7];
			}
			if ((alea.nextInt(100)+1) * evasion > precision * attaquant.getStatsModifiees()[6]){
				reussi = false;
				if (affichage && (attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
					System.out.println(defenseur.getNom() + " evite l'attaque !");
				}
				if (affichage && !(attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all"))){
					System.out.println("Mais cela echoue.");
				}
			}
		}

		if (!reussi){
			attaquant.setEnColere(0);
			attaquant.setMania(0);
		}

		if (attaque.getNom().equals("High Jump Kick") && !reussi && attaqueLancee && defenseur.getStatsModifiees()[0] > 0){
			if (affichage){
				System.out.println(attaquant.getNom() + " s'ecrase au sol.");
			}
			//System.out.println("Pied voltige echoue");
			Pokemon cloneDef = defenseur.clone();
			cloneDef.detruitAbri();
			cloneDef.setClairvoyance(false);
			cloneDef.setChargement("-");
			attaquant.deltaPV((int)(-this.degatsPrevusJumpKick(cloneDef, attaquant, new Action (attaque, false))/2), false, true, 2, affichage);
		}
		if (attaque.getNom().equals("Jump Kick") && !reussi && attaqueLancee && defenseur.getStatsModifiees()[0] > 0){
			if (affichage){
				System.out.println(attaquant.getNom() + " s'ecrase au sol.");
			}
			//System.out.println("Pied saute echoue");
			Pokemon cloneDef = defenseur.clone();
			cloneDef.detruitAbri();
			cloneDef.setClairvoyance(false);
			cloneDef.setChargement("-");
			attaquant.deltaPV((int)(-this.degatsPrevusJumpKick(cloneDef, attaquant, new Action (attaque, false))/8), false, true, 2, affichage);
		}

		if (reussi){

			int NParentalBond = 1;
			if (attaquant.getTalent().equals("Parental Bond")){
				NParentalBond = 2;
			}

			int degats = 0;
			boolean cartonRouge = false;

			for (int coupPB = 0; coupPB < NParentalBond; ++coupPB){

				int Ncoups = 1;

				if (attaque.getNCoups().equals("2")){
					Ncoups = 2;
				}
				if (attaque.getNCoups().equals("6")){
					Ncoups = 6;
				}

				if (attaque.getNCoups().equals("2-5")){
					int de = alea.nextInt(6);
					if (de <= 1){
						Ncoups = 2;
					}
					if (de == 2){
						Ncoups = 3;
					}
					if (de >= 3){
						Ncoups = de;
					}
					if (attaquant.getTalent().equals("Skill Link")){
						Ncoups = 5;
					}
				}

				for (int coup=0; coup<Ncoups; ++coup){

					reussi = true;

					clone = defenseur.isSubstitute() && !attaquant.getTalent().equals("Infiltrator");
					passeClone = attaque.isSonore() || attaquant.getTalent().equals("Infiltrator");
					if (attaque.getCible().startsWith("user") || attaque.getCible().endsWith("Field")){
						passeClone = true;
					}

					// CATEGORIES 0, 4, 6, 7 & 8 : degats
					if ((attaquant.getStatsModifiees()[0] > 0 || attaque.getNom().equals("Explosion") || attaque.getNom().equals("Self-Destruct")) && reussi && (attaque.getCategorie().equals("0") || attaque.getCategorie().equals("4") || attaque.getCategorie().equals("6") || attaque.getCategorie().equals("7") || attaque.getCategorie().equals("8"))){

						try{
							BufferedReader fis = new BufferedReader (new FileReader ("DataBase/typestable.txt"));
							int NtypeDef1 = -1;
							int NtypeDef2 = -1;
							String[] txt;
							String NumTypeAtq = "";
							for (int i=0; i<=19;++i){
								txt = fis.readLine().split(" ");
								if (txt[1].equals(type)){NumTypeAtq += i;}
								if (txt[1].equals(defenseur.getType()[0])){NtypeDef1 = i;}
								if (txt[1].equals(defenseur.getType()[1])){NtypeDef2 = i;}
							}
							if (attaque.getNom().equals("Freeze-Dry")){
								NumTypeAtq = "19";
							}
							if ((type.equals("Fighting") || type.equals("Normal")) && defenseur.getType()[0].equals("Ghost") && (defenseur.isClairvoyance() || attaquant.getTalent().equals("Scrappy"))){
								NtypeDef1 = 18;
							}
							if ((type.equals("Fighting") || type.equals("Normal")) && defenseur.getType()[1].equals("Ghost") && (defenseur.isClairvoyance() || attaquant.getTalent().equals("Scrappy"))){
								NtypeDef2 = 18;
							}
							if (type.equals("Psychic") && defenseur.getType()[0].equals("Dark") && defenseur.isOeilMiracle()){
								NtypeDef1 = 18;
							}
							if (type.equals("Psychic") && defenseur.getType()[1].equals("Dark") && defenseur.isOeilMiracle()){
								NtypeDef2 = 18;
							}

							do{txt = fis.readLine().split(" ");}
							while (!txt[0].equals(NumTypeAtq));

							efficacite = Double.valueOf(txt[Integer.valueOf(NtypeDef1).intValue() + 1]).doubleValue() * Double.valueOf(txt[Integer.valueOf(NtypeDef2).intValue() + 1]).doubleValue() /4;
						}
						catch(Exception e){
							e.printStackTrace();
							System.out.println("Probleme de type" + 1/0);
						}

						if (attaque.getNom().equals("Struggle")){
							efficacite = 1;
						}

						if (efficacite == 0 && defenseur.getObjet().equals("Ring Target")){
							efficacite = 1;
							System.out.println("La Cible rend " + defenseur.getNom() + " vulnerable.");
						}

						if ((defenseur.getTalent().equals("Levitate") || defenseur.getObjet().equals("Air Balloon") || defenseur.isTelekinesis()) && type.equals("Ground") && gravite == 0){
							efficacite = 0;
						}

						if (defenseur.getTalent().equals("Wonder Guard") && efficacite < 2){
							efficacite = 0;
						}

						if (attaque.getNom().equals("Brick Break") && efficacite > 0){
							defenseur.casseBrique(affichage);
						}

						double critique = 1;
						//if (affichage){
						//System.out.println("Taux de critique : " + attaque.probaCritique((int)attaquant.getStatsModifiees()[8]));
						//}
						if (attaque.probaCritique((int)attaquant.getStatsModifiees()[8]) >= (alea.nextInt(100)+1) && !attaque.getNom().equals("Counter") && !attaque.getNom().equals("Final Gambit") && !attaque.getNom().equals("Metal Burst") && !attaque.getNom().equals("Mirror Coat") && !attaque.getNom().equals("Night Shade") && !attaque.getNom().equals("Psywave") && !attaque.getNom().equals("Seismic Toss") && !attaque.getNom().equals("Super Fang") && !attaque.getEffet().equals("OHKO")){
							critique = 1.5;
							if (attaquant.getTalent().equals("Sniper")){
								critique = 2.25;
							}
						}
						if (defenseur.getTalent().equals("Battle Armor") || defenseur.getTalent().equals("Shell Armor")){
							critique = 1;
						}

						int classeAtq = -10;
						int classeDef = -10;
						if (attaque.getClasse().equals("phys")){
							classeAtq = 1;
							classeDef = 2;
						}
						if (attaque.getClasse().equals("spec")){
							classeAtq = 3;
							classeDef = 4;
						}

						if (attaque.getNom().equals("Psyshock") || attaque.getNom().equals("Psystrike") || attaque.getNom().equals("Secret Sword") || attaque.getNom().equals("Yakumo Gap")){
							classeAtq = 3;
							classeDef = 2;
						}

						defenseur.actualiseStats(attaquant.getTalent().equals("Infiltrator"), brouhaha, affichage);

						float atq = attaquant.getStatsModifiees()[classeAtq];
						float def = defenseur.getStatsModifiees()[classeDef];
						if (critique > 1){
							if (attaquant.getStatsBoosts()[classeAtq] < 0){
								atq = attaquant.getStatsInitiales()[classeAtq] * attaquant.getStatsModifieurs()[classeAtq];
							}
							Pokemon cloneDef = defenseur.clone();
							cloneDef.casseBrique(false);
							if (cloneDef.getStatsBoosts()[classeDef] >= 0){
								def = cloneDef.getStatsInitiales()[classeDef] * cloneDef.getStatsModifieurs()[classeDef];
							}
							else{
								def = cloneDef.getStatsModifiees()[classeDef];
							}
						}

						if (defenseur.getTalent().equals("Unaware")){
							atq = attaquant.getStatsInitiales()[classeAtq] * attaquant.getStatsModifieurs()[classeAtq];
						}
						if (attaquant.getTalent().equals("Unaware")){
							def = defenseur.getStatsInitiales()[classeDef] * defenseur.getStatsModifieurs()[classeDef];
						}

						if (attaque.getNom().equals("Sacred Sword")){
							def = defenseur.getStatsInitiales()[2] * defenseur.getStatsModifieurs()[2];
						}

						if (attaque.getNom().equals("Foul Play")){
							Pokemon attaquantBis = attaquant.clone();
							attaquantBis.setStatsInitiales(defenseur.getStatsInitiales());
							attaquantBis.setStatsBoosts(defenseur.getStatsBoosts());
							attaquantBis.actualiseStats(false, brouhaha, false);
							atq = attaquantBis.getStatsModifiees()[1];
						}

						float boostPuissance2 = 1;

						if (attaque.getNom().equals("Acrobatics") && attaquant.getObjet().equals("(No Item)")){
							boostPuissance2 *= 2;
						}

						if ((attaque.getNom().equals("Avalanche") || attaque.getNom().equals("Revenge")) && attaquant.getDegatsRecus() >= 0){
							boostPuissance2 *= 2;
						}
						
						if (attaque.getNom().equals("Beat Up")){
							puissance = (int) (attaquant.getBaseStats()[1]/10 + 5);
						}

						if (attaque.getNom().equals("Brine") && defenseur.getStatsModifiees()[0] <= (defenseur.getStatsInitiales()[0] /2)){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Electro Ball")){
							int ratio = (int) (100 * defenseur.getStatsModifiees()[5] / attaquant.getStatsModifiees()[5]);
							puissance = 150;
							if (ratio > 25){puissance = 120;}
							if (ratio > 33){puissance = 80;}
							if (ratio > 50){puissance = 60;}
						}

						if (attaque.getNom().equals("Eruption") || attaque.getNom().equals("Water Spout")){
							puissance = 150 * attaquant.getStatsModifiees()[0] / attaquant.getStatsInitiales()[0];
						}

						if (attaque.getNom().equals("Facade") && !attaquant.getStatut().equals("OK")){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Flail") || attaque.getNom().equals("Reversal")){
							int ratio = (int) (48 * attaquant.getStatsModifiees()[0] / attaquant.getStatsInitiales()[0]);
							puissance = 200;
							if (ratio > 1){puissance = 150;}
							if (ratio > 4){puissance = 100;}
							if (ratio > 9){puissance = 80;}
							if (ratio > 16){puissance = 40;}
							if (ratio > 32){puissance = 20;}
						}

						if (attaque.getNom().equals("Fury Cutter")){
							puissance = attaquant.getPuisTaillade();
						}

						if (attaque.getNom().equals("Grass Knot") || attaque.getNom().equals("Low Kick")){
							puissance = 120;
							if (defenseur.getPoids() < 200){puissance = 100;}
							if (defenseur.getPoids() < 100){puissance = 80;}
							if (defenseur.getPoids() < 50){puissance = 60;}
							if (defenseur.getPoids() < 25){puissance = 40;}
							if (defenseur.getPoids() < 10){puissance = 20;}
						}

						if (attaque.getNom().equals("Gyro Ball")){
							puissance = 25 * defenseur.getStatsModifiees()[5] / attaquant.getStatsModifiees()[5];
							if (puissance < 1){
								puissance = 1;
							}
							if (puissance > 150){
								puissance = 150;
							}
						}

						if (attaque.getNom().equals("Heavy Slam")){
							int ratio = (int) (100 * defenseur.getPoids() / attaquant.getPoids());
							puissance = 40;
							if (ratio <= 50){puissance = 60;}
							if (ratio <= 33){puissance = 80;}
							if (ratio <= 25){puissance = 100;}
							if (ratio <= 20){puissance = 120;}
						}

						if (attaque.getNom().equals("Hex") && !defenseur.getStatut().equals("OK")){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Knock Off") && !defenseur.getObjet().equals("(No Item)") && !(defenseur.getObjet().endsWith("ite") && !defenseur.getObjet().equals("Eviolite")) && !defenseur.getObjet().endsWith("ite X") && !defenseur.getObjet().endsWith("ite Y") && !(defenseur.getTalent().equals("Multitype") && defenseur.getObjet().contains(" Plate")) && !clone){
							boostPuissance2 *= 1.5;
						}

						if (attaque.getNom().equals("Payback") && (defenseur.isDejaAttaque() || defenseur.isDejaSwitche())){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Pursuit") && defenseur.isTenteSwitch()){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Stored Power")){
							puissance = 20;
							for (int i=1; i<=7; ++i){
								if (attaquant.getStatsBoosts()[i] > 0){
									puissance += 20*attaquant.getStatsBoosts()[i];
								}
							}
						}

						if (attaque.getNom().equals("Venoshock") && (defenseur.getStatut().equals("Psn") || attaquant.getStatut().equals("GPsn"))){
							boostPuissance2 *= 2;
						}

						if (attaque.getNom().equals("Wake-Up Slap") && defenseur.getStatut().equals("Somm")){
							boostPuissance2 *= 2;
						}

						if ((attaquant.getTalent().equals("Dark Aura") || defenseur.getTalent().equals("Dark Aura")) && type.equals("Dark")){
							if (attaquant.getTalent().equals("Aura Break") || defenseur.getTalent().equals("Aura Break")){
								boostPuissance2 /= 1.333;
							}
							else {
								boostPuissance2 *= 1.333;
								if (affichage) {
									System.out.println("Aura Ténébreuse booste la puissance !");
								}
							}
						}
						if ((attaquant.getTalent().equals("Fairy Aura") || defenseur.getTalent().equals("Fairy Aura")) && type.equals("Fairy")){
							if (attaquant.getTalent().equals("Aura Break") || defenseur.getTalent().equals("Aura Break")){
								boostPuissance2 /= 1.333;
							}
							else {
								boostPuissance2 *= 1.333;
								if (affichage) {
									System.out.println("Aura Féérique booste la puissance !");
								}
							}
						}
						
						if (attaquant.getTalent().equals("Iron Fist") && (attaque.getNom().contains("Punch") || attaque.getNom().contains("punch") || attaque.getNom().equals("Hammer Arm") || attaque.getNom().equals("Meteor Mash") || attaque.getNom().equals("Sky Uppercut")) && !attaque.getNom().equals("Sucker Punch")){
							boostPuissance2 *= 1.2;
						}
						if (attaquant.getTalent().equals("Mega Launcher") && (attaque.getNom().contains("Pulse") || attaque.getNom().contains("Aura"))){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.getTalent().equals("Strong Jaw") && (attaque.getNom().contains("Fang") || attaque.getNom().equals("Crunch") || attaque.getNom().equals("Bite"))){
							boostPuissance2 *= 1.5;
						}

						if (attaquant.getTalent().equals("Sheer Force") && (attaque.getTrouille() > 0 || attaque.getTaux() > 0)){
							boostPuissance2 *= 1.3;
						}

						if (attaquant.getTalent().equals("Technician") && puissance <= 60){
							boostPuissance2 *= 1.5;
						}

						if (meteo.equals("pluie") && type.equals("Fire") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 0.5;
						}
						if (meteo.contains("pluie") && type.equals("Water") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 1.5;
						}
						if (meteo.endsWith("soleil") && type.equals("Fire") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 1.5;
						}
						if (meteo.equals("soleil") && type.equals("Water") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 0.5;
						}
						if (meteo.equals("forte pluie") && type.equals("Fire") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 0;
							if (affichage){
								System.out.println("L'intensite de la pluie dissipe l'attaque.");
							}
						}
						if (meteo.equals("fort soleil") && type.equals("Water") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 0;
							if (affichage){
								System.out.println("L'intensite du soleil dissipe l'attaque.");
							}
						}
						if (meteo.equals("vent fort") && (type.equals("Electric") || type.equals("Ice") || type.equals("Rock")) && (defenseur.getType()[0].equals("Flying") || defenseur.getType()[1].equals("Flying")) && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 0.5;
							if (affichage){
								System.out.println("Le courant d'air affaiblit l'attaque.");
							}
						}
						
						if (attaquant.getTalent().equals("Sand Force") && meteo.equals("tempete de sable") && (type.equals("Ground") || type.equals("Rock") || type.equals("Steel")) && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							boostPuissance2 *= 1.3;
						}

						if ((attaquant.getObjet().equals("Insect Plate")) && type.equals("Bug")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Dread Plate") || attaquant.getObjet().equals("BlackGlasses") || attaquant.getObjet().equals("Black Glasses")) && type.equals("Dark")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Draco Plate") || attaquant.getObjet().equals("Dragon Fang")) && type.equals("Dragon")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Magnet") || attaquant.getObjet().equals("Zap Plate")) && type.equals("Electric")){
							boostPuissance2 *= 1.2;
						}
						if (attaquant.getObjet().equals("Pixie Plate") && type.equals("Fairy")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Black Belt") || attaquant.getObjet().equals("Fist Plate")) && type.equals("Fighting")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Flame Plate") || attaquant.getObjet().equals("Charcoal")) && type.equals("Fire")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Sharp Beak") || attaquant.getObjet().equals("Sky Plate")) && type.equals("Flying")){
							boostPuissance2 *= 1.2;
						}
						if (attaquant.getObjet().equals("Spell Tag") || (attaquant.getObjet().equals("Spooky Plate")) && type.equals("Ghost")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Meadow Plate") || attaquant.getObjet().equals("Miracle Seed")) && type.equals("Grass")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Earth Plate") || attaquant.getObjet().equals("Soft sand")) && type.equals("Ground")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Icicle Plate") || attaquant.getObjet().equals("Never-Melt Ice")) && (type.equals("Ice") || type.equals("Freeze-Dry"))){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Silk Scarf")) && type.equals("Normal")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Poison Barb")) && type.equals("Poison")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Twisted Spoon") || attaquant.getObjet().equals("Mind Plate")) && type.equals("Psychic")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Stone Plate")) && type.equals("Rock")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Metal Coat")) && type.equals("Steel")){
							boostPuissance2 *= 1.2;
						}
						if ((attaquant.getObjet().equals("Mystic Water") || attaquant.getObjet().equals("Splash Plate")) && type.equals("Water")){
							boostPuissance2 *= 1.2;
						}

						if (attaquant.getObjet().equals("Adamant Orb") && attaquant.getEspece().equals("Dialga") && (type.equals("Dragon") || type.equals("Steel"))){
							boostPuissance2 *= 1.2;
						}

						if (attaquant.getObjet().equals("Muscle Band") && classeAtq == 1){
							boostPuissance2 *= 1.1;
						}
						if (attaquant.getObjet().equals("Wise Glasses") && classeAtq == 3){
							boostPuissance2 *= 1.1;
						}

						if (attaquant.getObjet().equals("Normal Gem") && type.equals("Normal")){
							boostPuissance2 *= 1.5;
							attaquant.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println("Le Joyau Normal de " + attaquant.getNom() + " augmente la puissance de l'attaque !");
							}
						}

						if (attaquant.getTalent().equals("Analytic") && (defenseur.isDejaAttaque() || defenseur.isDejaSwitche())){
							boostPuissance2 *= 1.3;
						}
						if (attaquant.getTalent().equals("Blaze") && type.equals("Fire") && attaquant.getStatsModifiees()[0] <= attaquant.getStatsInitiales()[0]/3){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.getTalent().equals("Overgrow") && type.equals("Grass") && attaquant.getStatsModifiees()[0] <= attaquant.getStatsInitiales()[0]/3){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.getTalent().equals("Swarm") && type.equals("Bug") && attaquant.getStatsModifiees()[0] <= attaquant.getStatsInitiales()[0]/3){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.getTalent().equals("Torrent") && type.equals("Water") && attaquant.getStatsModifiees()[0] <= attaquant.getStatsInitiales()[0]/3){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.isTorche() && type.equals("Fire")){
							boostPuissance2 *= 1.5;
						}
						if (attaquant.getTalent().equals("Tough Claws") && attaque.isContact()){
							boostPuissance2 *= 1.3;
						}
						if (attaquant.getTalent().equals("Rivalry") && !attaquant.getSexe().equals("asexue") && !defenseur.getSexe().equals("asexue")){
							if (attaquant.getSexe().equals(defenseur.getSexe())){
								boostPuissance2 *= 1.25;
							}
							else{
								boostPuissance2 *= 0.75;
							}
						}
						if (attaquant.getTalent().equals("Reckless") && attaque.getRecul() < 0){
							boostPuissance2 *= 1.2;
							if (affichage){
								System.out.println("Temeraire boost la puissance !");
							}
						}

						if (attaque.isMeFirst()){
							boostPuissance2 *= 1.5;
						}

						float STAB = 1;
						if (type.equals(attaquant.getType()[0]) || type.equals(attaquant.getType()[1])){
							STAB = (float) 1.5;
							if (attaquant.getTalent().equals("Adaptability")){
								STAB = 2;
							}
						}

						if (efficacite > 0 && critique > 1 && affichage){
							System.out.println("Coup critique !");
						}

						if (affichage && !attaque.getNom().equals("Counter") && !attaque.getNom().equals("Endeavor") && !attaque.getNom().equals("Final Gambit") && !attaque.getNom().equals("Metal Burst") && !attaque.getNom().equals("Mirror Coat") && !attaque.getNom().equals("Night Shade") && !attaque.getNom().equals("Psywave") && !attaque.getNom().equals("Seismic Toss") && !attaque.getNom().equals("Super Fang") && !attaque.getEffet().equals("OHKO") && coup == 0 && coupPB == 0){
							if (efficacite > 1){System.out.println("C'est super efficace !");}
							if (efficacite < 1 && efficacite > 0){System.out.println("Ce n'est pas tres efficace...");}
						}
						if (efficacite == 0 && affichage){
							System.out.println("Ca n'affecte pas " + defenseur.getNom() + ".");
						}

						if (efficacite > 1 && attaquant.getObjet().equals("Expert Belt")){
							boostPuissance2 *= 1.2;
						}

						//					if (affichage){
						//						System.out.println("Atq=" + atq + " Puis=" + puissance + "Boost1=" + boostPuissance1 + " Boost2=" + boostPuissance2 + " Def=" + def + " STAB=" + STAB + " Efficacite=" + efficacite + " Critique=" + critique);
						//					}

						degats = (int) (((42 * atq * puissance * boostPuissance1 * boostPuissance2 / 50 / def) + 2) * STAB * efficacite * critique * (85 + alea.nextInt(16))/100);

						if (degats>0 && attaquant.getTalent().equals("Hustle") && classeAtq == 1){
							degats = (int) (1.5 * degats);
						}
						if (degats>0 && attaquant.getObjet().equals("Life Orb")){
							degats = (int) (1.3 * degats);
						}

						if (degats>0 && defenseur.getTalent().equals("Dry Skin") && type.equals("Fire")){
							degats = (int) (1.25 * degats);
						}

						if (efficacite < 1 && attaquant.getTalent().equals("Tinted Lens")){
							degats *= 2;
						}
						
						if (efficacite > 1 && (defenseur.getTalent().equals("Filter") || defenseur.getTalent().equals("Solid Rock"))){
							degats *= 0.75;
						}

						if (coupPB > 0){
							degats /= 2;
						}

						if (defenseur.getTalent().equals("Multiscale") && defenseur.getStatsModifiees()[0] == defenseur.getStatsInitiales()[0]){
							degats = (int) (degats/2);
						}

						if (defenseur.getTalent().equals("Thick Fat") && (type.equals("Fire") || type.equals("Ice") || type.equals("Freeze-Dry"))){
							degats = (int) (degats/2);
						}
						if (defenseur.getTalent().equals("Heatproof") && type.equals("Fire")){
							degats = (int) (degats/2);
						}

						if (attaquant.getTerrain().equals("Electric") && type.equals("Dragon")){
							degats = (int) (degats*1.5);
							if (affichage){
								System.out.println("Le terrain electrique renforce l'attaque.");
							}
						}
						if (defenseur.getTerrain().equals("Misty") && type.equals("Dragon")){
							degats = (int) (degats/2);
							if (affichage){
								System.out.println("Le champ brumeux affaiblit l'attaque.");
							}
						}
						if (tourniquet > 0 && type.equals("Fire")){
							degats = (int) (degats/2);
						}

						if (defenseur.getObjet().equals("Babiri Berry") && degats>0 && efficacite>1 && type.equals("Steel") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Babiri pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Charti Berry") && degats>0 && efficacite>1 && type.equals("Rock") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Jouca pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Chople Berry") && degats>0 && efficacite>1 && type.equals("Fighting") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Pomroz pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Coba Berry") && degats>0 && efficacite>1 && type.equals("Flying") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Cobaba pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Colbur Berry") && degats>0 && efficacite>1 && type.equals("Dark") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Lampou pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Haban Berry") && degats>0 && efficacite>1 && type.equals("Dragon") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Kryptonite pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Kasib Berry") && degats>0 && efficacite>1 && type.equals("Ghost") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Sedra pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Occa Berry") && degats>0 && efficacite>1 && type.equals("Fire") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Chocco pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Passho Berry") && degats>0 && efficacite>1 && type.equals("Water") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa Baie Pocpoc pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Rindo Berry") && degats>0 && efficacite>1 && type.equals("Grass") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Panga pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Roseli Berry") && degats>0 && efficacite>1 && type.equals("Fairy") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Selro pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Shuca Berry") && degats>0 && efficacite>1 && type.equals("Ground") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Jouca pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Wacan Berry") && degats>0 && efficacite>1 && type.equals("Electric") && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Parma pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Yache Berry") && degats>0 && efficacite>1 && (type.equals("Ice") || type.equals("Freeze-Dry")) && !defenseur.isTendu()){
							degats = (int) (degats/2);
							defenseur.setObjet("(No Item)", true, true, false);
							if (affichage){
								System.out.println(defenseur.getNom() + " mange sa baie Nanone pour reduire les degats.");
							}
							if (defenseur.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + defenseur.getNom() + ".");
								}
								defenseur.deltaPV((int)defenseur.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}

						if (attaque.getNom().equals("Counter") && efficacite > 0){
							degats = 2* attaquant.getDegatsPhysRecus();
							if (degats <= 0 && affichage){
								System.out.println("L'attaque a echoue.");
							}
						}
						if (attaque.getNom().equals("Metal Burst") && efficacite > 0){
							degats = (int) (1.5* attaquant.getDegatsRecus());
							if (degats < 0){
								degats = 0;
							}
							if (degats <= 0 && affichage){
								System.out.println("L'attaque a echoue.");
							}
						}
						if (attaque.getNom().equals("Mirror Coat") && efficacite > 0){
							degats = 2* attaquant.getDegatsSpecRecus();
							if (degats <= 0 && affichage){
								System.out.println("L'attaque a echoue.");
							}
						}

						if (attaque.getNom().equals("Endeavor") && efficacite > 0){
							degats = (int) (defenseur.getStatsModifiees()[0] - attaquant.getStatsModifiees()[0]);
							if (degats <= 0){
								degats = 0;
								if (affichage){
									System.out.println("Cela n'a aucun effet.");
								}
							}
							if (efficacite == 0){
								degats = 0;
							}
						}

						if (attaque.getNom().equals("Final Gambit") && efficacite > 0){
							degats = (int) attaquant.getStatsModifiees()[0];
							attaquant.fixePV(0, true, affichage);
						}

						if ((attaque.getNom().equals("Night Shade") || attaque.getNom().equals("Seismic Toss")) && efficacite > 0){
							degats = 100;
						}

						if (attaque.getNom().equals("Psywave") && efficacite > 0){
							degats = 100 + alea.nextInt(51);
						}

						clone = defenseur.isSubstitute() && !attaquant.getTalent().equals("Infiltrator");

						if (attaque.isSonore()){
							clone = false;
						}

						if (!clone && degats > defenseur.getStatsModifiees()[0]){
							degats = (int) defenseur.getStatsModifiees()[0];
						}
						if (clone && degats > defenseur.getClone().getStatsModifiees()[0]){
							degats = (int) defenseur.getClone().getStatsModifiees()[0];
						}

						if (attaque.getNom().equals("Super Fang") && efficacite > 0){
							if (!clone){
								degats = (int) (defenseur.getStatsModifiees()[0] /2);
							}
							else{
								degats = (int) (defenseur.getClone().getStatsModifiees()[0] /2);
							}
						}

						if (efficacite > 0 && attaque.getEffet().equals("OHKO")){
							if (defenseur.getTalent().equals("Sturdy")){
								if (affichage){
									System.out.println("Fermete de " + defenseur.getNom() + " le protege contre le KO.");
								}
							}
							else{
								degats = defenseur.getStatsInitiales()[0];
								if (affichage){
									System.out.println("KO en un coup !");
								}
							}
						}

						defenseur.deltaPV(-degats, true, passeClone, classeDef, affichage);

						if (defenseur.getStatsModifiees()[0] <= 0 && defenseur.isPrlvtDestin()){
							if (affichage){
								System.out.println(defenseur.getNom() + " emmene son adversaire au tapis.");
							}
							attaquant.fixePV(0, true, affichage);
						}
						
						if (critique > 1 && defenseur.getTalent().equals("Anger Point")){
							if (affichage){
								System.out.println(defenseur.getNom() + " se met en colere !");
							}
							defenseur.multiBoost(new String[] {"Atq"}, new int[] {12},	true, true, affichage);
						}

						if (attaque.getNom().equals("High Jump Kick") && degats <= 0 && defenseur.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println(attaquant.getNom() + " s'ecrase au sol.");
							}
							//System.out.println("Pied voltige degats=0");
							Pokemon cloneDef = defenseur.clone();
							cloneDef.detruitAbri();
							cloneDef.setClairvoyance(false);
							cloneDef.setChargement("-");
							attaquant.deltaPV((int)(-this.degatsPrevusJumpKick(cloneDef, attaquant, new Action (attaque, false))/2), false, true, 2, affichage);
						}
						if (attaque.getNom().equals("Jump Kick") && degats <= 0 && defenseur.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println(attaquant.getNom() + " s'ecrase au sol.");
							}
							//System.out.println("Pied saute degats=0");
							Pokemon cloneDef = defenseur.clone();
							cloneDef.detruitAbri();
							cloneDef.setClairvoyance(false);
							cloneDef.setChargement("-");
							attaquant.deltaPV((int)(-this.degatsPrevusJumpKick(cloneDef, attaquant, new Action (attaque, false))/8), false, true, 2, affichage);
						}

						reussi = false;

						int tauxTrouille = attaque.getTrouille();
						int tauxEffet = attaque.getTaux();

						if (tauxTrouille <= 0 && (attaquant.getObjet().equals("King's Rock") || attaquant.getObjet().equals("Razor Fang"))){
							tauxTrouille = 10;
						}

						if (attaquant.getTalent().equals("Serene Grace")){
							tauxTrouille *= 2;
							tauxEffet *= 2;
						}

						if (degats > 0 && defenseur.getStatsModifiees()[0] > 0 && (attaque.getCategorie().equals("4") || attaque.getCategorie().equals("6")) && !clone && !((attaquant.getTalent().equals("Sheer Force") || defenseur.getTalent().equals("Shield Dust")) && tauxEffet < 100)){
							if (tauxEffet >= (alea.nextInt(100)+1)){
								reussi = true;
							}
							if (clone){
								reussi = false;
							}
						}
						if (degats > 0 && attaque.getCategorie().equals("7") && attaquant.getStatsModifiees()[0] > 0 && !(attaquant.getTalent().equals("Sheer Force") && tauxEffet < 100)){
							if (tauxEffet >= (alea.nextInt(100)+1)){
								reussi = true;
							}
						}

						if (attaque.getRecul() != 0 && attaquant.getStatsModifiees()[0] > 0 && (!attaquant.getTalent().equals("Rock Head") || attaque.getRecul() > 0)){
							int recul = attaque.getRecul();
							if (attaquant.getObjet().equals("Big Root")){
								recul *= 1.3;
							}
							if (defenseur.getTalent().equals("Liquid Ooze") && recul > 0){
								if (affichage){
									System.out.println("Machin aspire le Suitement !");
								}
								recul = -recul;
							}
							attaquant.deltaPV((int)(recul * degats / 100), true, true, 0, affichage);
						}
						if (attaquant.getObjet().equals("Shell Bell") && attaquant.getStatsModifiees()[0] > 0){
							attaquant.deltaPV((int)(degats / 8), true, true, 0, affichage);
						}

						if (attaque.getNom().equals("Struggle") && attaquant.getStatsModifiees()[0] > 0){
							attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0]/4), true, true, 0, affichage);
							if (attaquant.getStatsInitiales()[0] < 8){
								attaquant.deltaPV(- 1, false, true, 0, affichage);
							}
						}

						if (defenseur.getTalent().equals("Iron Barbs") && attaque.isContact() && degats>0 && attaquant.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println(attaquant.getNom() + " est blesse par les epines de fer.");
							}
							attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0]/8), false, true, 0, affichage);
							if (attaquant.getStatsInitiales()[0] < 8){
								attaquant.deltaPV(- 1, false, true, 0, affichage);
							}
						}
						if (defenseur.getTalent().equals("Rough Skin") && attaque.isContact() && degats>0 && attaquant.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println(attaquant.getNom() + " est blesse par la peau dure.");
							}
							attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0]/8), false, true, 0, affichage);
							if (attaquant.getStatsInitiales()[0] < 8){
								attaquant.deltaPV(- 1, false, true, 0, affichage);
							}
						}
						if (defenseur.getObjet().equals("Rocky Helmet") && attaque.isContact() && degats>0 && attaquant.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println(attaquant.getNom() + " est blesse par le casque brut.");
							}
							attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0]/6), false, true, 0, affichage);
							if (attaquant.getStatsInitiales()[0] < 8){
								attaquant.deltaPV(- 1, false, true, 0, affichage);
							}
						}

						if (defenseur.getTalent().equals("Weak Armor") && attaque.isContact() && degats>0 && defenseur.getStatsModifiees()[0] > 0 && !clone){
							defenseur.multiBoost(new String[] {"Def", "Vit"}, new int[] {-1,1}, passeClone, true, affichage);
						}

						if (defenseur.getTalent().equals("Justified") && degats > 0 && attaque.getType().equals("Dark") && defenseur.getStatsModifiees()[0] > 0 && !clone){
							if (affichage){
								System.out.println("Coeur Noble se declenche!");
							}
							defenseur.multiBoost(new String[] {"Atq"}, new int[] {1}, passeClone, true, affichage);
						}
						if (defenseur.getTalent().equals("Rattled") && degats > 0 && (attaque.getType().equals("Bug") || attaque.getType().equals("Ghost") || attaque.getType().equals("Dark")) && defenseur.getStatsModifiees()[0] > 0 && !clone){
							if (affichage){
								System.out.println("Phobique se declenche!");
							}
							defenseur.multiBoost(new String[] {"Vit"}, new int[] {1}, passeClone, true, affichage);
						}

						if (defenseur.getObjet().equals("Cell Battery") && attaque.getType().equals("Electric") && defenseur.getStatsModifiees()[0] > 0  && attaquant.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println("La pile de " + defenseur.getNom() + " augmente son attaque speciale !");
							}
							defenseur.multiBoost(new String[] {"SAtq"}, new int[] {1}, true, true, affichage);
							defenseur.setObjet("(No Item)", true, true, false);
						}
						
						if (defenseur.getObjet().equals("Absorb Bulb") && attaque.getType().equals("Water") && defenseur.getStatsModifiees()[0] > 0  && attaquant.getStatsModifiees()[0] > 0){
							if (affichage){
								System.out.println("Le Bulbe de " + defenseur.getNom() + " augmente son attaque speciale !");
							}
							defenseur.multiBoost(new String[] {"SAtq"}, new int[] {1}, true, true, affichage);
							defenseur.setObjet("(No Item)", true, true, false);
						}

						if (defenseur.getObjet().equals("Weakness Policy") && degats > 0 && efficacite > 1 && defenseur.getStatsModifiees()[0] > 0 && !clone){
							if (affichage){
								System.out.println("Vulne-Assurance se declenche!");
							}
							defenseur.setObjet("(No Item)", true, true, false);
							defenseur.multiBoost(new String[] {"Atq", "SAtq"}, new int[] {2,2}, passeClone, true, affichage);
						}

						if (defenseur.getObjet().equals("Air Balloon") && degats > 0 && !clone){
							if(affichage){
								System.out.println("Le ballon de " + defenseur.getNom() + " eclate !");
							}
							defenseur.setObjet("(No Item)", true, true, false);
						}

						if ((attaque.getNom().equals("Bug Bite") || attaque.getNom().equals("Pluck")) && defenseur.getObjet().contains(" Berry") && degats > 0 && !clone && attaquant.getStatsModifiees()[0] > 0 && !attaquant.isTendu() && !defenseur.getTalent().equals("Sticky Hold")){
							attaquant.mangeBaie(defenseur.getObjet(), affichage);
							defenseur.setObjet("(No Item)", false, false, false);
							if (attaquant.getTalent().equals("Cheek Pouch")){
								if (affichage){
									System.out.println("Bajoue restaure de la vie a " + attaquant.getNom() + ".");
								}
								attaquant.deltaPV((int)attaquant.getStatsInitiales()[0]/4, false, true, 0, affichage);
							}
						}

						if (attaque.getNom().equals("Fling")){
							if (attaquant.getObjet().endsWith(" Berry")){
								defenseur.mangeBaie(attaquant.getObjet(), affichage);
							}
							attaquant.setObjet("(No Item)", true, true, false);
						}

						if (attaque.getNom().equals("Clear Smog") && defenseur.getStatsModifiees()[0] > 0 && degats > 0 && !clone){
							defenseur.setStatsBoosts(new int[] {0,0,0,0,0,0,0,0,0});
							if (affichage){
								System.out.println("Les stats de " + defenseur.getNom() + " sont reinitialisees.");
							}
						}

						if (attaque.getNom().equals("Rapid Spin") && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.tourRapide(affichage);
						}

						if (degats > 0 && tauxTrouille >= (alea.nextInt(100)+1) && !clone && !((attaquant.getTalent().equals("Sheer Force") || !defenseur.getTalent().equals("Shield Dust")) && tauxTrouille < 100)){
							defenseur.setTrouille(affichage);
						}

						if (attaque.getNom().equals("Smack Down") && degats > 0 && defenseur.getStatsModifiees()[0] > 0 && !clone){
							defenseur.setAntiAir(affichage);
						}

						if ((attaque.getNom().equals("Outrage") || attaque.getNom().equals("Petal Dance") || attaque.getNom().equals("Thrash")) && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							if (attaquant.getEnColere() == 0){
								attaquant.setEnColere(2+alea.nextInt(2));
							}
							if (attaquant.getEnColere() >= 0){
								attaquant.setEnColere(attaquant.getEnColere()-1);

								if (attaquant.getEnColere() == 0 && attaquant.getConfus() <= 0){
									if (affichage){
										System.out.println("La fatigue rend " + attaquant.getNom() + " confus.");
									}
									attaquant.setStatut("Conf", true, true, false, brouhaha, affichage);
								}
							}
						}

						if (attaque.getNom().equals("Uproar") && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.setBrouhaha();
						}

						if (defenseur.getTalent().equals("Effect Spore") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0 && !(attaquant.getType()[0].equals("Grass") || attaquant.getType()[1].equals("Grass") || attaquant.getTalent().equals("Overcoat") || attaquant.getObjet().equals("Safety Goggles"))){
							int effet = alea.nextInt(100);
							if (affichage && effet < 30){
								System.out.println(defenseur.getNom() + " libere des spores !");
							}
							if (effet <= 10){
								attaquant.setStatut("Somm", false, true, false, brouhaha, affichage);
							}
							else{
								if (effet <= 20){
									attaquant.setStatut("Psn", false, true, false, brouhaha, affichage);
								}
								else{
									if (effet < 30){
										attaquant.setStatut("Para", false, true, false, brouhaha, affichage);
									}
								}
							}
						}

						if (defenseur.getTalent().equals("Cute Charm") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0 && defenseur.getStatsModifiees()[0] > 0){
							if ((attaquant.getSexe().equals("male") && defenseur.getSexe().equals("femelle")) || (attaquant.getSexe().equals("femelle") && defenseur.getSexe().equals("male"))){
								attaquant.setStatut("Amour", false, passeClone, false, brouhaha, affichage);
								if(attaquant.getObjet().equals("Destiny Knot")){
									if (affichage){
										System.out.println("Le Noeud Destin rend l'amour réciproque !");
									}
									defenseur.setStatut("Amour", false, passeClone, false, brouhaha, affichage);
								}
							}
						}

						if (defenseur.getTalent().equals("Cursed Body") && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.entrave(affichage);
						}

						if (defenseur.getTalent().equals("Flame Body") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							if (alea.nextInt(100)+1 <= 30){
								attaquant.setStatut("Brul", false, true, false, brouhaha, affichage);
							}
						}

						if (defenseur.getTalent().equals("Gooey") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0) {
							attaquant.multiBoost(new String[]{"Vit"}, new int[]{-1}, true, false, affichage);
						}

						if (defenseur.getTalent().equals("Mummy") && !attaquant.getTalent().equals("Mummy") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.setTalent("Mummy", affichage);
						}

						if (defenseur.getTalent().equals("Point Poison") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							if (alea.nextInt(100)+1 <= 30){
								attaquant.setStatut("Psn", false, true, false, brouhaha, affichage);
							}
						}
						if (attaquant.getTalent().equals("Poison Touch") && attaque.isContact() && degats > 0 && defenseur.getStatsModifiees()[0] > 0){
							if (alea.nextInt(100)+1 <= 20){
								defenseur.setStatut("Psn", false, false, false, brouhaha, affichage);
							}
						}

						if (defenseur.getTalent().equals("Static") && attaque.isContact() && degats > 0 && attaquant.getStatsModifiees()[0] > 0){
							if (alea.nextInt(100)+1 <= 30){
								attaquant.setStatut("Para", false, true, false, brouhaha, affichage);
							}
						}

						if (defenseur.getTalent().equals("Color Change") && defenseur.getStatsModifiees()[0] > 0){
							defenseur.setType(new String[] {type, "-"}, affichage);
						}

						if (attaque.getNom().equals("Fury Cutter") && degats > 0){
							attaquant.setTailladeReussie();
						}

						if (attaquant.getTalent().equals("Moxie") && defenseur.getStatsModifiees()[0] <= 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.multiBoost(new String[] {"Atq"}, new int[] {1}, true, true, affichage);
						}

						if (attaque.getNom().equals("Fell Stinger") && defenseur.getStatsModifiees()[0] <= 0 && attaquant.getStatsModifiees()[0] > 0){
							attaquant.multiBoost(new String[] {"Atq"}, new int[] {2}, true, true, affichage);
						}

						if (defenseur.getStatsModifiees()[0] <= 0 && attaquant.getStatsModifiees()[0] > 0 && defenseur.getTalent().equals("Aftermath") && attaque.isContact()){
							attaquant.multiBoost(new String[] {"Atq"}, new int[] {2}, true, true, affichage);
						}

						if ((attaque.getNom().equals("Blast Burn") || attaque.getNom().equals("Frenzy Plant") || attaque.getNom().equals("Giga Impact") || attaque.getNom().equals("Hydro Cannon") || attaque.getNom().equals("Hyper Beam")) && attaquant.getStatsModifiees()[0] > 0){
							attaquant.setDecharge(true);
						}

						if (defenseur.getObjet().equals("Eject Button") && defenseur.getStatsModifiees()[0] > 0  && !clone){
							if (affichage){
								System.out.println(defenseur.getNom() + " s'echappe grace au Bouton Fuite !");
							}
							this.switche(defenseur, attaquant, true, true, false, false);
							defenseur.setObjet("(No Item)", true, true, false);
						}

						if (defenseur.getObjet().equals("Red Card") && defenseur.getStatsModifiees()[0] > 0  && attaquant.getStatsModifiees()[0] > 0){
							cartonRouge = true;
						}

						if (attaque.getNom().equals("Knock Off") && !defenseur.getObjet().equals("(No Item)") && !defenseur.getTalent().equals("Sticky Hold") && !(defenseur.getObjet().endsWith("ite") && !defenseur.getObjet().equals("Eviolite")) && !defenseur.getObjet().endsWith("ite X") && !defenseur.getObjet().endsWith("ite Y") && !(defenseur.getTalent().equals("Multitype") && defenseur.getObjet().contains(" Plate")) && degats > 0 && !clone){
							if (affichage){
								System.out.println(attaquant.getNom() + " rend l'objet " + defenseur.getObjet() + " de " + defenseur.getNom() + " inutilisable.");
							}
							defenseur.setObjet("(No Item)", false, false, false);
						}

						if (attaque.getNom().equals("Incinerate") && (defenseur.getObjet().endsWith(" Berry") || defenseur.getObjet().equals(" Gem")) && !defenseur.getTalent().equals("Sticky Hold")){
							if (affichage){
								System.out.println(attaquant.getNom() + " calcine la " + defenseur.getObjet() + " de " + defenseur.getNom());
							}
							defenseur.setObjet("(No Item)", false, false, false);
						}

						if ((attaque.getNom().equals("Thief") || attaquant.getTalent().equals("Magician")) && attaquant.getObjet().equals("(No Item)") && !defenseur.getObjet().equals("(No Item)") && !defenseur.getTalent().equals("Sticky Hold") && !(defenseur.getObjet().endsWith("ite") && !defenseur.getObjet().equals("Eviolite")) && !defenseur.getObjet().endsWith("ite X") && !defenseur.getObjet().endsWith("ite Y") && !(defenseur.getTalent().equals("Multitype") && defenseur.getObjet().contains(" Plate")) && degats > 0 && !clone){
							if (affichage){
								System.out.println(attaquant.getNom() + " vole l'objet " + defenseur.getObjet() + " de " + defenseur.getNom() + ".");
							}
							attaquant.setObjet(defenseur.getObjet(), true, false, affichage);
							defenseur.setObjet("(No Item)", false, false, false);
						}

						if (defenseur.getTalent().equals("Pickpocket") && defenseur.getObjet().equals("(No Item)") && !attaquant.getObjet().equals("(No Item)") && !attaquant.getTalent().equals("Sticky Hold") && !(attaquant.getObjet().endsWith("ite") && !attaquant.getObjet().equals("Eviolite")) && !attaquant.getObjet().endsWith("ite X") && !attaquant.getObjet().endsWith("ite Y") && !(defenseur.getTalent().equals("Multitype") && defenseur.getObjet().contains(" Plate")) && degats > 0 && attaque.isContact()){
							if (affichage){
								System.out.println(defenseur.getNom() + " vole l'objet " + attaquant.getObjet() + " de " + attaquant.getNom() + ".");
							}
							defenseur.setObjet(attaquant.getObjet(), true, false, affichage);
							attaquant.setObjet("(No Item)", false, false, false);
						}

						if (attaque.getNom().equals("Wake-Up Slap") && defenseur.getStatut().equals("Somm")){
							defenseur.setStatut("OK", false, false, false, brouhaha, affichage);
							if (affichage){
								System.out.println(defenseur.getNom() + " se reveille.");
							}
						}

						if ((attaque.getNom().equals("U-turn") || attaque.getNom().equals("Volt Switch")) && degats > 0 && attaquant.getStatsModifiees()[0] > 0 && !attaquant.isDejaSwitche()){
							this.switche(attaquant, defenseur, true, true, false, false);
						}

						if ((attaque.getNom().equals("Circle Throw") || attaque.getNom().equals("Dragon Tail") || attaque.getNom().equals("Yakumo Gap")) && degats > 0 && !clone && attaquant.getStatsModifiees()[0] > 0 && defenseur.getStatsModifiees()[0] > 0){
							this.switche(defenseur, attaquant, false, true, false, false);
						}
					}

					// CATEGORIES 1 & 4 : attaques de statut
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && (attaque.getCategorie().equals("1") || attaque.getCategorie().equals("4"))){
						if ((attaque.getNom().equals("Thunder Wave") && (defenseur.getType()[0].equals("Ground") || defenseur.getType()[1].equals("Ground")))){
							if (affichage){
								System.out.println("Ca n'affecte pas " + defenseur.getNom() + "...");
							}
						}
						else{
							String effet = attaque.getEffet();
							if ((effet.equals("FirSpin") || effet.equals("Infest") || effet.equals("MgmStrm") || effet.equals("SandTmb") || effet.equals("Siphon")) && attaquant.getObjet().equals("Binding Band")){
								effet += "5";
								if (affichage){
									System.out.println("Le bandeau etreinte prolonge la duree de l'attaque.");
								}
							}
							defenseur.setStatut(effet, false, passeClone, false, brouhaha, affichage);
							if (defenseur.getTalent().equals("Synchronize") && defenseur.getStatut().equals(attaque.getEffet()) && (attaque.getEffet().equals("Brul") || attaque.getEffet().equals("Para") || attaque.getEffet().equals("Psn") || attaque.getEffet().equals("GPsn"))){
								if (affichage){
									System.out.println("Synchro de " + defenseur.getNom() + " se declenche.");
								}
								attaquant.setStatut(attaque.getEffet(), false, passeClone, false, brouhaha, affichage);
							}
						}
					}

					// CATEGORIE 2, 6 & 7 : variations de stats
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && (attaque.getCategorie().equals("2") || attaque.getCategorie().equals("6") || attaque.getCategorie().equals("7"))){

						boolean selfInduced = false;
						
						Pokemon cible = new Pokemon("Missingno", 1, "none", "PO");
						if ((attaque.getCible().startsWith("user") || attaque.getCategorie().equals("7")) && !attaquant.isDejaSwitche()){
							cible = attaquant;
							passeClone = true;
							selfInduced = true;
						}
						if ((attaque.getCible().startsWith("adv") || attaque.getCible().startsWith("all")) && !attaque.getCategorie().equals("7")){
							cible = defenseur;
						}

						String[] effets = attaque.getEffet().split("/");
						int[] variations = attaque.getVariations();

						if (attaque.getNom().equals("Acupressure")){
							alea = new Random();
							int stat = -1;

							boolean cherche = true;
							while (cherche){
								stat = 1+alea.nextInt(7);
								if (attaquant.getStatsBoosts()[stat] < 6){
									cherche = false;
								}
							}
							
							String[] stats = new String[] {"PV","Atq","Def","SAtq","SDef","Vit","Prec","Esq"};
							effets = new String[] {stats[stat]};
						}
						
						if (attaque.getNom().equals("Growth") && meteo.endsWith("soleil") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
							variations = new int[] {2,2};
						}

						cible.multiBoost(effets, variations, passeClone, selfInduced, affichage);

						if (attaque.getNom().equals("Autotomize")){
							attaquant.divisePoids(affichage);
						}

						if (attaque.getNom().equals("Parting Shot") && !attaquant.isDejaSwitche()){
							this.switche(attaquant, defenseur, true, true, false, false);
						}
					}

					// CATEGORIE 3 : attaques de soin
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && attaque.getCategorie().equals("3")){

						if (attaquant.getStatsModifiees()[0] < attaquant.getStatsInitiales()[0]){
							float pourcentSoin = Integer.valueOf(attaque.getEffet()).intValue();

							if (attaque.getNom().equals("Moonlight") || attaque.getNom().equals("Morning Sun") || attaque.getNom().equals("Synthesis")){
								if (meteo.endsWith("soleil") && !(attaquant.getTalent().equals("Cloud Nine") || defenseur.getTalent().equals("Cloud Nine") || attaquant.getTalent().equals("Air Lock") || defenseur.getTalent().equals("Air Lock"))){
									pourcentSoin = (float) 66.66;
								}
								if (!meteo.endsWith("soleil") && !meteo.equals("claire")){
									pourcentSoin = (float) 25;;
								}
							}

							attaquant.deltaPV((int)(attaquant.getStatsInitiales()[0] * pourcentSoin/100), false, true, 0, affichage);

							if (attaque.getNom().equals("Roost")){
								attaquant.setAtterri();
							}
						}
						else{
							if (affichage){
								System.out.println(attaquant.getNom() + " a deja toute sa vie.");
							}
						}
					}

					// CATEGORIE 10 : attaques de terrain
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && attaque.getCategorie().equals("10")){

						if (attaque.getNom().equals("Electric Terrain")){
							this.setTerrain("Electric");
						}

						if (attaque.getNom().equals("Gravity") && gravite > 0){
							if (affichage){
								System.out.println("Mais cela echoue.");
							}
						}
						if (attaque.getNom().equals("Gravity") && gravite == 0){
							gravite = 5;
							attaquant.setGravite(true, affichage);
							defenseur.setGravite(true, affichage);
							if (affichage){
								System.out.println("La gravite s'intensifie.");
							}
						}

						if (attaque.getNom().equals("Hail") && meteo.equals("grele")){
							if (affichage){
								System.out.println("Mais cela echoue.");
							}
						}
						if (attaque.getNom().equals("Hail") && !meteo.equals("grele")){
							this.setMeteo("grele");
							compteurMeteo = 5;
							if (attaquant.getObjet().equals("Icy Rock")){
								compteurMeteo = 8;
							}
						}

						if (attaque.getNom().equals("Magic Room") && zoneMagique > 0){
							zoneMagique = 0;
							attaquant.setZoneMagique(false);
							defenseur.setZoneMagique(false);
							if (affichage){
								System.out.println(attaquant.getNom() + " fait disparaitre la Zone Magique.");
							}
						}
						if (attaque.getNom().equals("Magic Room") && zoneMagique == 0){
							zoneMagique = 5;
							attaquant.setZoneMagique(true);
							defenseur.setZoneMagique(true);
							if (affichage){
								System.out.println("Les objets sont inutilisables pendant 5 tours.");
							}
						}

						if (attaque.getNom().equals("Misty Terrain")){
							this.setTerrain("Misty");
						}

						if (attaque.getNom().equals("Rain Dance") && meteo.contains("pluie")){
							if (affichage){
								System.out.println("Mais cela echoue.");
							}
						}
						if (attaque.getNom().equals("Rain Dance") && !meteo.contains("pluie")){
							this.setMeteo("pluie");
							compteurMeteo = 5;
							if (attaquant.getObjet().equals("Damp Rock")){
								compteurMeteo = 8;
							}
						}

						if (attaque.getNom().equals("Sandstorm") && meteo.equals("tempete de sable")){
							if (affichage){
								System.out.println("Mais cela echoue.");
							}
						}
						if (attaque.getNom().equals("Sandstorm") && !meteo.equals("tempete de sable")){
							this.setMeteo("tempete de sable");
							compteurMeteo = 5;
							if (attaquant.getObjet().equals("Smooth Rock")){
								compteurMeteo = 8;
							}
						}

						if (attaque.getNom().equals("Sunny Day") && meteo.endsWith("soleil")){
							if (affichage){
								System.out.println("Mais cela echoue.");
							}
						}
						if (attaque.getNom().equals("Sunny Day") && !meteo.endsWith("soleil")){
							this.setMeteo("soleil");
							compteurMeteo = 5;
							if (attaquant.getObjet().equals("Heat Rock")){
								compteurMeteo = 8;
							}
						}

						if (attaque.getNom().equals("Trick Room")){
							if (distorsion <= 0){
								distorsion = 5;
								if (affichage){
									System.out.println("Les dimensions sont faussees.");
								}
							}
							else{
								distorsion = 0;
								if (affichage){
									System.out.println(attaquant.getNom() + " retablit les dimensions.");
								}
							}
						}

						if (attaque.getNom().equals("Water Sport")){
							if (this.tourniquet <= 0){
								this.tourniquet = 5;
								if (affichage){
									System.out.println("La puissance des attaques Feu est diminuee.");
								}
							}
							else{
								if (affichage){
									System.out.println("Mais cela echoue.");
								}
							}
						}
					}

					// CATEGORIE 11 : attaques d'equipe
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && attaque.getCategorie().equals("11")){

						if (attaque.getNom().equals("Light Screen")){
							attaquant.setMurLumiere(affichage);
						}

						if (attaque.getNom().equals("Reflect")){
							attaquant.setProtection(affichage);
						}

						if (attaque.getNom().equals("Safeguard")){
							attaquant.setRuneProtect(affichage);
						}

						if (attaque.getNom().equals("Spikes")){
							defenseur.plusPicots(true, affichage);
						}

						if (attaque.getNom().equals("Stealth Rock")){
							defenseur.setPDR(true, affichage);
						}

						if (attaque.getNom().equals("Sticky Web")){
							defenseur.setToileGluante(true, affichage);
						}

						if (attaque.getNom().equals("Tailwind")){
							attaquant.setVentArriere(affichage);
						}

						if (attaque.getNom().equals("Toxic Spikes")){
							defenseur.plusPicsToxics(true, affichage);
						}
					}

					// CATEGORIE 13 : autres...
					if (attaquant.getStatsModifiees()[0] > 0 && reussi && attaque.getCategorie().equals("13")){

						if (attaque.getNom().equals("Aromatherapy") || attaque.getNom().equals("Heal Bell") || attaque.getNom().equals("Refresh")){
							attaquant.setStatut("OK", true, true, false, brouhaha, affichage);
						}

						if (attaque.getNom().equals("Aqua Ring")){
							attaquant.anneauHydro(affichage);
						}

						if (attaque.getNom().equals("Attract")){
							if ((attaquant.getSexe().equals("male") && defenseur.getSexe().equals("femelle")) || (attaquant.getSexe().equals("femelle") && defenseur.getSexe().equals("male"))){
								defenseur.setStatut("Amour", false, passeClone, false, brouhaha, affichage);
								if(defenseur.getObjet().equals("Destiny Knot")) {
									if (affichage) {
										System.out.println("Le Noeud Destin rend l'amour réciproque !");
									}
									attaquant.setStatut("Amour", false, passeClone, false, brouhaha, affichage);
								}
							}
							else{
								if (affichage){
									System.out.println("Ca n'affecte pas " + defenseur.getNom() + ".");
								}
							}
						}

						if (attaque.getNom().equals("Baton Pass")){
							this.switche(attaquant, defenseur, true, true, true, false);
						}

						if (attaque.getNom().equals("Belly Drum")){
							if (attaquant.getStatsModifiees()[0] > attaquant.getStatsInitiales()[0] /2){
								attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0] /2), false, true, 0, affichage);
								attaquant.multiBoost(new String[] {"Atq"}, new int[] {12}, true, true, affichage);
							}
							else{
								if (affichage){
									System.out.println(attaquant.getNom() + " n'a pas assez de PV...");
								}
							}
						}

						if (attaque.getNom().equals("Charge")){
							attaquant.chargeur(affichage);
						}

						if (attaque.getNom().equals("Curse")){
							attaquant.deltaPV(- (int)(attaquant.getStatsModifiees()[0] /2), false, true, 0, affichage);
							defenseur.setMaudit(affichage);
						}

						if (attaque.getNom().equals("Defog")){
							attaquant.antiBrume(affichage);
							defenseur.antiBrume(affichage);
							if (!defenseur.isAbrite()){
								defenseur.multiBoost(new String[] {"Esq"}, new int[] {-1}, passeClone, false, affichage);
							}
						}

						if (attaque.getNom().equals("Destiny Bond")){
							attaquant.setPrlvtDestin(true);
							if (affichage){
								System.out.println(attaquant.getNom() + " est pret a emmener son adversaire au tapis.");
							}
						}

						if (attaque.getNom().equals("Detect") || attaque.getNom().equals("Protect")){
							attaquant.setAbrite(affichage);
						}

						if (attaque.getNom().equals("Disable")){
							defenseur.entrave(affichage);
						}

						if (attaque.getNom().equals("Embargo")){
							defenseur.setEmbargo(affichage);
						}

						if (attaque.getNom().equals("Encore")){
							defenseur.setEncore(affichage);
						}

						if (attaque.getNom().equals("Endure")){
							attaquant.setTenace(affichage);
						}

						if (attaque.getNom().equals("Entrainment")){
							defenseur.setTalent(attaquant.getTalent(), affichage);
						}

						if (attaque.getNom().equals("Flatter")){
							if (!defenseur.isSubstitute() || passeClone){
								defenseur.multiBoost(new String[] {"SAtq"}, new int[] {2}, passeClone, false, affichage);
								if (defenseur.getConfus() <= 0){
									defenseur.setConfus(2+alea.nextInt(4));
									if (affichage){
										System.out.println(defenseur.getNom() + " devient confus.");
									}
								}
								else{
									if (affichage){
										System.out.println(defenseur.getNom() + " est deja confus.");
									}
								}
							}
							else{
								if (affichage){
									System.out.println(defenseur.getNom() + " est protege par son clone.");
								}
							}
						}

						if (attaque.getNom().equals("Foresight")){
							defenseur.setClairvoyance(affichage);
						}

						if (attaque.getNom().equals("Future Sight")){
							defenseur.prescience(affichage);
							if (affichage){
								System.out.println(attaquant.getNom() + " prepare son attaque.");
							}
						}

						if (attaque.getNom().equals("Gastro Acid")){
							defenseur.setTalent("(Suc Digestif)", affichage);
							if (affichage){
								System.out.println(attaquant.getNom() + " supprime le talent de " + defenseur.getNom() + ".");
							}
						}

						if (attaque.getNom().equals("Haze")){
							attaquant.setStatsBoosts(new int[] {0,0,0,0,0,0,0,0,0});
							defenseur.setStatsBoosts(new int[] {0,0,0,0,0,0,0,0,0});
							if (affichage){
								System.out.println("Les changements de stats sont elimines.");
							}
						}

						if (attaque.getNom().equals("Heal Block")){
							defenseur.setAntiSoin(affichage);
						}

						if (attaque.getNom().equals("Healing Wish") || attaque.getNom().equals("Lunar Dance")){
							attaquant.fixePV(0, true, affichage);
						}

						if (attaque.getNom().equals("Heart Swap")){
							Pokemon cloneAtq = attaquant.clone();
							attaquant.setStatsBoosts(defenseur.getStatsBoosts());
							defenseur.setStatsBoosts(cloneAtq.getStatsBoosts());
						}

						if (attaque.getNom().equals("Imprison")){
							for (int i=0; i<4; ++i){
								for (int j=0; j<4; ++j){
									if (attaquant.getMoves()[i].getNom().equals(defenseur.getMoves()[j].getNom())){
										defenseur.getMoves()[j].setPossessif(true);
										if (affichage){
											System.out.println(defenseur.getMoves()[j].getNom() + " de " + defenseur.getNom() + " est bloque.");
										}
									}
								}
							}
						}

						if (attaque.getNom().equals("Ingrain")){
							attaquant.racines(affichage);
						}

						if (attaque.getNom().equals("King's Shield")){
							attaquant.setBouclierRoyal(affichage);
						}

						if (attaque.getNom().equals("Leech Seed")){
							defenseur.setVampigraine(true, false, affichage);
						}
						
						if (attaque.getNom().equals("Lock-On")){
							attaquant.setVerouillage(affichage);
						}

						if (attaque.getNom().equals("Magic Coat")){
							attaquant.setRefletMagik(affichage);
						}

						if (attaque.getNom().equals("Magnet Rise")){
							defenseur.setVolMagnetic(affichage);
						}

						if (attaque.getNom().equals("Mat Block")){
							attaquant.setTatami(affichage);
						}

						if (attaque.getNom().equals("Memento")){
							attaquant.fixePV(0, true, affichage);
							defenseur.multiBoost(new String[] {"Atq", "SAtq"}, new int[] {-2, -2}, passeClone, false, affichage);
						}

						if (attaque.getNom().equals("Miracle Eye")){
							defenseur.setOeilMiracle(affichage);
						}

						if (attaque.getNom().equals("Mist")){
							attaquant.setBrume(affichage);
						}

						if (attaque.getNom().equals("Nightmare")){
							attaquant.setCauchemar(affichage);
						}

						if (attaque.getNom().equals("Pain Split")){

							if (defenseur.isSubstitute()){
								if (affichage){
									System.out.println("Mais cela echoue.");
								}
							}
							else{
								if (affichage){
									System.out.println(attaquant.getNom() + " et " + defenseur.getNom() + " se partagent les PV.");
								}
								int PVattaquant = (int) attaquant.getStatsModifiees()[0];
								int PVmaxAttaquant = (int) attaquant.getStatsInitiales()[0];
								int PVdefenseur = (int) defenseur.getStatsModifiees()[0];
								int PVmaxdefenseur = (int) defenseur.getStatsInitiales()[0];

								int PVfinaux = (int) (PVattaquant + PVdefenseur)/2;
								if (PVfinaux > PVmaxAttaquant){
									PVfinaux = PVmaxAttaquant;
								}
								if (PVfinaux > PVmaxdefenseur){
									PVfinaux = PVmaxdefenseur;
								}
								attaquant.fixePV(PVfinaux, true, affichage);
								defenseur.fixePV(PVfinaux, passeClone, affichage);
							}
						}

						if (attaque.getNom().equals("Perish Song")){
							attaquant.setRequiem(affichage);
							defenseur.setRequiem(affichage);
						}

						if (attaque.getNom().equals("Powder")){
							defenseur.setNueePoudre(passeClone, affichage);
						}
						
						if (attaque.getNom().equals("Power Split")){
							int attaqueMoy = (attaquant.getStatsInitiales()[1] + defenseur.getStatsInitiales()[1])/2;
							int attaqueSpeMoy = (attaquant.getStatsInitiales()[3] + defenseur.getStatsInitiales()[3])/2;
							
							int[] nouvStatAttaquant = attaquant.getStatsInitiales();
							nouvStatAttaquant[1] = attaqueMoy;
							nouvStatAttaquant[3] = attaqueSpeMoy;
							attaquant.setStatsInitiales(nouvStatAttaquant);

							int[] nouvStatDefenseur = defenseur.getStatsInitiales();
							nouvStatDefenseur[1] = attaqueMoy;
							nouvStatDefenseur[3] = attaqueSpeMoy;
							attaquant.setStatsInitiales(nouvStatDefenseur);
						}

						if (attaque.getNom().equals("Psych Up")){
							attaquant.setStatsBoosts(attaquant.getStatsBoosts());
							if (affichage){
								System.out.println(attaquant.getNom() + " copie les changements de stats de " + defenseur.getNom());
							}
						}

						if (attaque.getNom().equals("Psycho Shift")){
							if (!attaquant.getStatut().equals("-") && defenseur.getStatut().equals("-")){
								defenseur.setStatut(attaquant.getStatut(), false, false, false, brouhaha, affichage);
								attaquant.setStatut("-", true, true, true, brouhaha, affichage);
							}
							else{
								if (affichage){
									System.out.println("Mais cela echoue.");
								}
							}
						}
						
						if (attaque.getNom().equals("Quick Guard")){
							attaquant.setPrevention(affichage);
						}

						if (attaque.getNom().equals("Recycle")){
							attaquant.recyclage(affichage);
						}

						if (attaque.getNom().equals("Reflect Type")){
							attaquant.setType(defenseur.getType(), affichage);
						}

						if (attaque.getNom().equals("Rest")){
							if (!attaquant.getStatut().equals("Somm")){
								attaquant.setStatut("Repos", true, true, false, brouhaha, affichage);
							}
							else{
								if (affichage){
									System.out.println("Mais cela echoue...");
								}
							}
						}

						if (attaque.getNom().equals("Roar") || attaque.getNom().equals("Whirlwind")){
							this.switche(defenseur, attaquant, false, false, false, false);
						}

						if (attaque.getNom().equals("Role Play")){
							attaquant.setTalent(defenseur.getTalent(), affichage);
						}

						if (attaque.getNom().equals("Simple Beam")){
							defenseur.setTalent("Simple", affichage);
						}

						if (attaque.getNom().equals("Skill Swap")){
							String talentAtq = attaquant.getTalent();
							attaquant.setTalent(defenseur.getTalent(), affichage);
							defenseur.setTalent(talentAtq, affichage);
						}

						if (attaque.getNom().equals("Sleep Talk")){
							if (attaquant.getStatut().equals("Somm")){
								Attaque autreAtq = null;
								boolean cherche = true;
								while (cherche){
									autreAtq = attaquant.getMoves()[alea.nextInt(4)];
									if (!autreAtq.getNom().equals("Sleep Talk")){
										cherche = false;
									}
								}
								this.attaque(attaquant, autreAtq, defenseur, true);
							}
							else{
								if (affichage){
									System.out.println("Il faut dormir pour pouvoir utiliser cette attaque!");
								}
							}
						}

						if (attaque.getNom().equals("Snatch")){
							attaquant.setSaisie(affichage);
						}

						if (attaque.getNom().equals("Soak")){
							defenseur.setType(new String[] {"Water", "-"}, affichage);
						}

						if (attaque.getNom().equals("Spiky Shield")){
							attaquant.setPicoDefense(affichage);
						}

						if (attaque.getNom().equals("Stockpile")){
							attaquant.stockage(affichage);
						}

						if (attaque.getNom().equals("Substitute")){
							attaquant.clonage(affichage);
						}

						if (attaque.getNom().equals("Swagger")){
							if (!defenseur.isSubstitute() || passeClone){
								defenseur.multiBoost(new String[] {"Atq"}, new int[] {2}, passeClone, false, affichage);
								if (defenseur.getConfus() <= 0){
									defenseur.setConfus(2+alea.nextInt(4));
									if (affichage){
										System.out.println(defenseur.getNom() + " devient confus.");
									}
								}
								else{
									if (affichage){
										System.out.println(defenseur.getNom() + " est deja confus.");
									}
								}
							}
							else{
								if (affichage){
									System.out.println(defenseur.getNom() + " est protege par son clone.");
								}
							}
						}

						if (attaque.getNom().equals("Taunt")){
							defenseur.setTaunted(affichage);
						}

						if (attaque.getNom().equals("Telekinesis")){
							defenseur.setTelekinesis(affichage);
						}

						if (attaque.getNom().equals("Topsy-Turvy")){
							defenseur.renversement(affichage);
						}

						if (attaque.getNom().equals("Torment")){
							defenseur.setTourmente(affichage);
						}

						if (attaque.getNom().equals("Transform")){
							attaquant.morphing(defenseur, affichage);
						}

						if (attaque.getNom().equals("Switcheroo") || attaque.getNom().equals("Trick")){
							String obj1 = attaquant.getObjet();
							String obj2 = defenseur.getObjet();

							if (defenseur.isSubstitute() || defenseur.getTalent().equals("Sticky Hold")){
								reussi = false;
								if (affichage){
									System.out.println("Mais cela echoue...");
								}
							}

							if (reussi && (obj1.endsWith("ite") && !obj1.equals("Eviolite")) || obj1.endsWith("ite X") || obj1.endsWith("ite Y") || (obj2.endsWith("ite") && !obj2.equals("Eviolite")) || obj2.endsWith("ite X") || obj2.endsWith("ite Y") || (attaquant.getTalent().equals("Multitype") && attaquant.getObjet().contains(" Plate")) || (defenseur.getTalent().equals("Multitype") && defenseur.getObjet().contains(" Plate"))){
								reussi = false;
								if (affichage){
									System.out.println("On ne peut pas echanger de mega-gemme.");
								}
							}

							if (reussi){
								attaquant.setObjet(obj2, false, false, false);
								defenseur.setObjet(obj1, false, false, false);
								if (affichage){
									System.out.println(attaquant.getNom() + " echange son objet avec " + defenseur.getNom() + ".");
									if (!obj1.equals("(No Item)")){
										System.out.println(defenseur.getNom() + " obtient " + obj1 + ".");
									}
									if (!obj2.equals("(No Item)")){
										System.out.println(attaquant.getNom() + " obtient " + obj2 + ".");
									}
								}
							}
						}
						
						if (attaque.getNom().equals("Wide Guard")){
							attaquant.setGardeLarge(affichage);
						}

						if (attaque.getNom().equals("Wish")){
							attaquant.voeu(affichage);
						}

						if (attaque.getNom().equals("Worry Seed")){
							defenseur.setTalent("Insomnia", affichage);
						}

						if (attaque.getNom().equals("Yawn")){
							defenseur.baille(affichage);
						}
					}

					if (attaquant.getStatut().equals("KO") || defenseur.getStatut().equals("KO")){
						Ncoups = coup+1;
						NParentalBond = coupPB+1;
					}
					if (cartonRouge){
						Ncoups = 1;
						NParentalBond = 1;
					}
				}
				if (affichage && Ncoups > 1){
					System.out.println("Touche " + Ncoups + " fois.");
				}
			}
			if (affichage && NParentalBond > 1){
				System.out.println("Touche 2 fois.");
			}

			if (attaquant.getObjet().equals("Life Orb") && degats > 0 && attaquant.getStatsModifiees()[0] > 0 && (!attaquant.getTalent().equals("Sheer Force") || (attaque.getTrouille() == 0 && attaque.getTaux() == 0)) && attaquant.getStatsModifiees()[0] > 0){
				attaquant.deltaPV(- (int)(attaquant.getStatsInitiales()[0]/10), false, true, 0, affichage);
				if (attaquant.getStatsInitiales()[0] < 8){
					attaquant.deltaPV(- 1, false, true, 0, affichage);
				}
			}

			if (cartonRouge){
				if (affichage){
					System.out.println(defenseur.getNom() + " met un Carton Rouge a " + attaquant.getNom() + " !");
				}
				this.switche(attaquant, defenseur, false, true, false, false);
				defenseur.setObjet("(No Item)", true, true, false);
			}
		}


		brouhaha = attaquant.isBrouhaha() || defenseur.isBrouhaha();
	}

	public void Tour (boolean passeTour){

		Random alea = new Random();

		// Prise de decision des joueurs
		Action action1 = new Action (true);
		if (!passeTour){
			action1 = Joueur1.Decision(Pokemon1, Pokemon2, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);
		}
		Action action2 = Joueur2.Decision(Pokemon2, Pokemon1, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, affichage);

		Attaque attaque1 = action1.getAttaque();
		boolean mevo1 = action1.isMegaEvolution();
		boolean switch1 = action1.isSwitcher();
		Pokemon1.setActionPrevue(action1);

		Attaque attaque2 = action2.getAttaque();
		boolean mevo2 = action2.isMegaEvolution();
		boolean switch2 = action2.isSwitcher();
		Pokemon2.setActionPrevue(action2);

		// Calcul des priorites
		int premier = 0;
		Pokemon PkmnRapide = Pokemon1;
		Attaque AtqRapide = attaque1;
		Pokemon PkmnLent = Pokemon2;
		Attaque AtqLente = attaque2;
		if (Pokemon1.getStatsModifiees()[5] > Pokemon2.getStatsModifiees()[5]){
			premier = 1;
		}
		if (Pokemon1.getStatsModifiees()[5] < Pokemon2.getStatsModifiees()[5]){
			premier = 2;
		}
		if (distorsion > 0){
			premier = 3-premier;
		}

		if ((Pokemon1.getObjet().equals("Lagging Tail") || Pokemon1.getTalent().equals("Stall")) && !(Pokemon2.getObjet().equals("Lagging Tail") || Pokemon2.getTalent().equals("Stall"))){
			premier = 2;
		}
		if ((Pokemon2.getObjet().equals("Lagging Tail") || Pokemon2.getTalent().equals("Stall")) && !(Pokemon1.getObjet().equals("Lagging Tail") || Pokemon1.getTalent().equals("Stall"))){
			premier = 1;
		}
		
		// Priorites
		int bonusPrio1 = 0;
		int bonusPrio2 = 0;
		if (Pokemon1.getTalent().equals("Gale Wings") && attaque1.getType().equals("Flying")){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Gale Wings") && attaque2.getType().equals("Flying")){
			++bonusPrio2;
		}
		if (Pokemon1.getTalent().equals("Prankster") && !(attaque1.getCategorie().equals("0") || attaque1.getCategorie().equals("4") || attaque1.getCategorie().equals("6") || attaque1.getCategorie().equals("7") || attaque1.getCategorie().equals("8"))){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Prankster") && !(attaque2.getCategorie().equals("0") || attaque2.getCategorie().equals("4") || attaque2.getCategorie().equals("6") || attaque2.getCategorie().equals("7") || attaque2.getCategorie().equals("8"))){
			++bonusPrio2;
		}

		if (Pokemon1.getObjet().equals("Lax Incense")){
			--bonusPrio1;
		}
		if (Pokemon2.getObjet().equals("Lax Incense")){
			--bonusPrio2;
		}
		if (Pokemon1.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio1;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon1.getNom() + " se declenche !");
				}
			}
		}
		if (Pokemon2.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio2;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon2.getNom() + " se declenche !");
				}
			}
		}
		if (Pokemon1.getObjet().equals("Custap Berry") && !Pokemon1.isTendu() && (Pokemon1.getStatsModifiees()[0] <= Pokemon1.getStatsInitiales()[0]/4)){
			Pokemon1.setObjet("(No Item)", true, true, false);
			++bonusPrio1;
			if (affichage){
				System.out.println(Pokemon1.getNom() + " mange sa Baie Prio !");
			}
		}
		if (Pokemon2.getObjet().equals("Custap Berry") && !Pokemon2.isTendu() && (Pokemon2.getStatsModifiees()[0] <= Pokemon1.getStatsInitiales()[0]/4)){
			Pokemon2.setObjet("(No Item)", true, true, false);
			++bonusPrio2;
			if (affichage){
				System.out.println(Pokemon2.getNom() + " mange sa Baie Prio !");
			}
		}

		if (attaque1.getPriorite() + bonusPrio1 > attaque2.getPriorite() + bonusPrio2){
			premier = 1;
		}
		if (attaque1.getPriorite() + bonusPrio1 < attaque2.getPriorite() + bonusPrio2){
			premier = 2;
		}

		// Switch
		if (premier == 1 && switch1){
			switch1 = false;
			this.switche(Pokemon1, Pokemon2, true, false, false, false);
		}
		if (premier == 2 && switch2){
			switch2 = false;
			this.switche(Pokemon2, Pokemon1, true, false, false, false);
		}
		if (switch1){
			switch1 = false;
			this.switche(Pokemon1, Pokemon2, true, false, false, false);
		}
		if (switch2){
			switch2 = false;
			this.switche(Pokemon2, Pokemon1, true, false, false, false);
		}

		// Mega-Evolution
		if (premier == 1 && mevo1){
			if (Pokemon1.MegaEvolution(affichage)){
				mevo1 = false;
				this.appliqueTalent(Pokemon1, Pokemon2);
			}
		}
		if (premier == 2 && mevo2){
			if (Pokemon2.MegaEvolution(affichage)){
				mevo2 = false;
				this.appliqueTalent(Pokemon2, Pokemon1);
			}
		}
		if (mevo1){
			if (Pokemon1.MegaEvolution(affichage)){
				mevo1 = false;
				this.appliqueTalent(Pokemon1, Pokemon2);
			}
		}
		if (mevo2){
			if (Pokemon2.MegaEvolution(affichage)){
				mevo2 = false;
				this.appliqueTalent(Pokemon2, Pokemon1);
			}
		}

		// Mitra-Poing
		if (attaque1.getNom().equals("Focus Punch")){
			Pokemon1.setFocus(affichage);
		}
		if (attaque2.getNom().equals("Focus Punch")){
			Pokemon2.setFocus(affichage);
		}

		// Priorites
		bonusPrio1 = 0;
		bonusPrio2 = 0;
		if (Pokemon1.getTalent().equals("Gale Wings") && attaque1.getType().equals("Flying")){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Gale Wings") && attaque2.getType().equals("Flying")){
			++bonusPrio2;
		}
		if (Pokemon1.getTalent().equals("Prankster") && !(attaque1.getCategorie().equals("0") || attaque1.getCategorie().equals("4") || attaque1.getCategorie().equals("6") || attaque1.getCategorie().equals("7") || attaque1.getCategorie().equals("8"))){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Prankster") && !(attaque2.getCategorie().equals("0") || attaque2.getCategorie().equals("4") || attaque2.getCategorie().equals("6") || attaque2.getCategorie().equals("7") || attaque2.getCategorie().equals("8"))){
			++bonusPrio2;
		}
		if (Pokemon1.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio1;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon1.getNom() + " se declenche !");
				}
			}
		}
		if (Pokemon2.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio2;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon2.getNom() + " se declenche !");
				}
			}
		}

		if (attaque1.getPriorite() + bonusPrio1 > attaque2.getPriorite() + bonusPrio2){
			premier = 1;
		}
		if (attaque1.getPriorite() + bonusPrio1 < attaque2.getPriorite() + bonusPrio2){
			premier = 2;
		}
		if (premier == 0){
			//if (affichage){
			//System.out.println("Speed Tie!");
			//}
			if (alea.nextInt(2) == 0){
				premier = 1;
			}
			else{
				premier = 2;
			}
		}
		if (premier == 1){
			PkmnRapide = Pokemon1;
			AtqRapide = attaque1;
			PkmnLent = Pokemon2;
			AtqLente = attaque2;
		}
		if (premier == 2){
			PkmnRapide = Pokemon2;
			AtqRapide = attaque2;
			PkmnLent = Pokemon1;
			AtqLente = attaque1;
		}

		// Attaque du premier Pokemon
		if (!AtqRapide.getNom().equals("(No Move)")){
			if (AtqLente.getNom().equals("Pursuit")){
				PkmnLent.setPoursuiveur();
			}
			this.attaque(PkmnRapide, AtqRapide, PkmnLent, false);
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		// Attaque du second Pokemon
		if (PkmnLent.getStatsModifiees()[0] > 0 && !PkmnLent.isDejaAttaque() && !PkmnLent.isDejaSwitche() && !AtqLente.getNom().equals("(No Move)")){
			this.attaque(PkmnLent, AtqLente, PkmnRapide, false);
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		// Fin du tour
		if (compteurMeteo >= 1){
			--compteurMeteo;
			if (compteurMeteo == 0){
				this.setMeteo("claire");
			}
			else{
				if (affichage){
					if (meteo.equals("grele")){
						System.out.println("\nLa grele continue de tomber.");
					}
					if (meteo.equals("pluie")){
						System.out.println("\nLa pluie continue de tomber.");
					}
					if (meteo.equals("forte pluie")){
						System.out.println("\nLa pluie est diluvienne.");
					}
					if (meteo.equals("soleil")){
						System.out.println("\nLes rayons du Soleil sont forts.");
					}
					if (meteo.equals("fort soleil")){
						System.out.println("\nLes rayons du Soleil sont ecrasants.");
					}
					if (meteo.equals("tempete de sable")){
						System.out.println("\nLa tempete de sable fait rage.");
					}
					if (meteo.equals("vent fort")){
						System.out.println("\nLe vent est dechaine.");
					}
				}
			}
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		if (compteurTerrain >= 1){
			--compteurTerrain;
			if (compteurTerrain == 0){
				this.setTerrain("normal");
			}
		}

		if (gravite >= 1){
			--gravite;
			if (gravite == 0){
				PkmnRapide.setGravite(false, affichage);
				PkmnLent.setGravite(false, affichage);
				if (affichage){
					System.out.println("La gravite redevient normale.");
				}
			}
		}

		if (zoneMagique >= 1){
			--zoneMagique;
			if (zoneMagique == 0){
				PkmnRapide.setZoneMagique(false);
				PkmnLent.setZoneMagique(false);
				if (affichage){
					System.out.println("La Zone Magique s'estompe.");
				}
			}
		}

		if (distorsion >= 1){
			--distorsion;
			if (distorsion == 0){
				if (affichage){
					System.out.println("\nLes dimensions faussees redeviennent normales.");
				}
			}
		}

		if (tourniquet >= 1){
			--tourniquet;
			if (tourniquet == 0){
				if (affichage){
					System.out.println("Le Tourniquet prend fin.");
				}
			}
		}

		if (!echoUtilise){
			compteurEcho = 0;
		}
		echoUtilise = false;

		if (PkmnRapide.getStatsModifiees()[0] > 0){
			if (PkmnRapide.isVampigraine() && PkmnLent.getStatsModifiees()[0] > 0 && !PkmnRapide.getTalent().equals("Magic Guard")){
				if (affichage){
					System.out.println("\nL'energie de " + PkmnRapide.getNom() + " est drainee.");
				}
				int delta = (int)(PkmnRapide.getStatsInitiales()[0]/8);
				if (PkmnRapide.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnRapide.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnRapide.deltaPV(- delta, false, true, 0, affichage);
				if (PkmnRapide.getTalent().equals("Liquid Ooze")){
					delta = -delta;
				}
				if (PkmnLent.getObjet().equals("Big Root")){
					delta *= 1.3;
				}
				PkmnLent.deltaPV(delta, false, true, 0, affichage);
			}

			PkmnRapide.finDeTour(affichage);
			
			if (PkmnLent.getTalent().equals("Bad Dreams") && PkmnRapide.getStatut().equals("Somm")){
				if (affichage){
					System.out.println("\nLe sommeil de " + PkmnRapide.getNom() + " est perturbé.");
				}
				int delta = (int)(PkmnRapide.getStatsInitiales()[0]/8);
				if (PkmnRapide.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnRapide.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnRapide.deltaPV(- delta, false, true, 0, affichage);
			}

			if (PkmnRapide.getPrescience() == 0){
				if (affichage){
					System.out.println("\n" + PkmnRapide.getNom() + " subit l'attaque Prescience.");
				}
				this.attaque(PkmnLent, new Attaque ("Prescience"), PkmnRapide, true);
			}
		}
		else{
			PkmnLent.adversaireParti();
		}

		if (PkmnLent.getStatsModifiees()[0] > 0){
			if (PkmnLent.isVampigraine() && PkmnRapide.getStatsModifiees()[0] > 0 && !PkmnLent.getTalent().equals("Magic Guard")){
				if (affichage){
					System.out.println("\nL'energie de " + PkmnLent.getNom() + " est drainee.");
				}
				int delta = (int)(PkmnLent.getStatsInitiales()[0]/8);
				if (PkmnLent.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnLent.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnLent.deltaPV(- delta, false, true, 0, affichage);
				if (PkmnLent.getTalent().equals("Liquid Ooze")){
					delta = -delta;
				}
				if (PkmnRapide.getObjet().equals("Big Root")){
					delta *= 1.3;
				}
				PkmnRapide.deltaPV(delta, false, true, 0, affichage);
			}

			PkmnLent.finDeTour(affichage);

			if (PkmnRapide.getTalent().equals("Bad Dreams") && PkmnLent.getStatut().equals("Somm")){
				if (affichage){
					System.out.println("\nLe sommeil de " + PkmnLent.getNom() + " est perturbé.");
				}
				int delta = (int)(PkmnLent.getStatsInitiales()[0]/8);
				if (PkmnLent.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnLent.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnLent.deltaPV(- delta, false, true, 0, affichage);
			}

			if (PkmnLent.getPrescience() == 0){
				if (affichage){
					System.out.println("\n" + PkmnLent.getNom() + " subit l'attaque Prescience.");
				}
				this.attaque(PkmnRapide, new Attaque ("Prescience"), PkmnLent, true);
			}

			brouhaha = PkmnLent.isBrouhaha() || PkmnRapide.isBrouhaha();
		}
		else{
			PkmnRapide.adversaireParti();
		}
	}

	public int match (boolean PDR1, boolean picots1, boolean picsToxics1, boolean toileGluante1, boolean PDR2, boolean picots2, boolean picsToxics2, boolean toileGluante2, boolean passeTour){

		Random alea = new Random();

		if (PDR1){
			Pokemon1.setPDR(false, false);
		}
		if (picots1){
			Pokemon1.setPicots();
		}
		if (picsToxics1){
			Pokemon1.setPicsToxics();
		}
		if (toileGluante1){
			Pokemon1.setToileGluante(false, false);
		}

		if (PDR2){
			Pokemon2.setPDR(false, false);
		}
		if (picots2){
			Pokemon2.setPicots();
		}
		if (picsToxics2){
			Pokemon2.setPicsToxics();
		}
		if (toileGluante2){
			Pokemon2.setToileGluante(false, false);
		}
		if (!passeTour){
			this.switche(Pokemon1, Pokemon2, true, true, false, true);
			Pokemon1.setDejaSwitche(false);
		}
		this.switche(Pokemon2, Pokemon1, true, true, false, true);
		Pokemon2.setDejaSwitche(false);

		int premier = 0;
		if (Pokemon1.getStatsModifiees()[5] > Pokemon2.getStatsModifiees()[5]){
			premier = 1;
		}
		if (Pokemon1.getStatsModifiees()[5] < Pokemon2.getStatsModifiees()[5]){
			premier = 2;
		}
		if (premier == 0){
			//if (affichage){
			//System.out.println("Speed Tie!");
			//}
			premier = 1+alea.nextInt(2);
		}

		if (premier == 1){
			if (!passeTour && Pokemon1.canMevo() && (Pokemon1.getObjet().equals("Blue Orb") || Pokemon1.getObjet().equals("Red Orb"))){
				Pokemon1.MegaEvolution(affichage);
			}
			if (Pokemon2.canMevo() && (Pokemon2.getObjet().equals("Blue Orb") || Pokemon2.getObjet().equals("Red Orb"))){
				Pokemon2.MegaEvolution(affichage);
			}
			
			if (!passeTour) {
				this.appliqueTalent(Pokemon1, Pokemon2);
				if (affichage && Pokemon1.getObjet().equals("Air Balloon") && gravite == 0) {
					System.out.println("\n" + Pokemon1.getNom() + " flotte grace a son ballon.");
				}
			}

			this.appliqueTalent(Pokemon2, Pokemon1);
			if (affichage && Pokemon2.getObjet().equals("Air Balloon") && gravite == 0){
				System.out.println("\n" + Pokemon2.getNom() + " flotte grace a son ballon.");
			}
		}
		if (premier == 2){
			if (Pokemon2.canMevo() && (Pokemon2.getObjet().equals("Blue Orb") || Pokemon2.getObjet().equals("Red Orb"))){
				Pokemon2.MegaEvolution(affichage);
			}
			if (!passeTour && Pokemon1.canMevo() && (Pokemon1.getObjet().equals("Blue Orb") || Pokemon1.getObjet().equals("Red Orb"))){
				Pokemon1.MegaEvolution(affichage);
			}

			this.appliqueTalent(Pokemon2, Pokemon1);
			if (affichage && Pokemon2.getObjet().equals("Air Balloon") && gravite == 0){
				System.out.println("\n" + Pokemon2.getNom() + " flotte grace a son ballon.");
			}
			
			if(!passeTour){
				this.appliqueTalent(Pokemon1, Pokemon2);
				if (affichage && Pokemon1.getObjet().equals("Air Balloon") && gravite == 0) {
					System.out.println("\n" + Pokemon1.getNom() + " flotte grace a son ballon.");
				}
			}
		}

		int Ntour = 0;

		do{
			++Ntour;
			if (affichage){
				System.out.println("\nTOUR " + Ntour);
			}
			try{
				this.Tour((Ntour == 1) && passeTour);
			}
			catch(StackOverflowError stofe){
				//stofe.printStackTrace();
				Pokemon1.print();
				Pokemon2.print();
				System.out.println(1/0);
			}

			if (Ntour > 141){
				System.out.println("Nombre de tours trop grand! ENDLESS BATTLE");
				affichage = true;
			}
		}
		while (Pokemon1.getStatsModifiees()[0] > 0 && Pokemon2.getStatsModifiees()[0] > 0);

		if (Pokemon1.getStatsModifiees()[0] <= 0 && Pokemon2.getStatsModifiees()[0] <= 0){
			if (affichage){
				System.out.println("\nEgalite");
			}
			return 500;
		}
		else{
			if (Pokemon2.getStatsModifiees()[0] <= 0){
				if (affichage){
					System.out.println("\n" + Pokemon1.getNom() + " gagne le match !");
				}
				return (int) (500 * (1 + Pokemon1.getStatsModifiees()[0]/Pokemon1.getStatsInitiales()[0]));
			}
			else{
				if (affichage){
					System.out.println("\n" + Pokemon2.getNom() + " gagne le match !");
				}
				return (int) (500 * (1 - Pokemon2.getStatsModifiees()[0]/Pokemon2.getStatsInitiales()[0]));
			}
		}
	}

	public void setMeteo(String met){
		
		int prioActu = 0;
		int nouvPrio = 0;
		
		if (this.meteo.contains("fort")){
			prioActu = 1;
		}
		if (met.contains("fort")){
			nouvPrio = 1;
		}
		
		if (nouvPrio < prioActu){
			if (affichage){
				System.out.println("La météo ne peut pas être dissipée aussi facilement !");
			}
		}
		else {

			if (met.equals("claire") && !meteo.equals("claire")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("\nLe temps redevient agreable.");
				}
			}
			if (met.equals("grele") && !meteo.equals("grele")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Une tempete de grele de prepare.");
				}
			}
			if (met.equals("pluie") && !meteo.contains("pluie")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Il commence a pleuvoir.");
				}
			}
			if (met.equals("forte pluie") && !meteo.contains("pluie")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Une pluie diluvienne s'abat.");
				}
			}
			if (met.endsWith("soleil") && !meteo.endsWith("soleil")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Les rayons du Soleil s'intensifient.");
				}
			}
			if (met.endsWith("fort soleil") && !meteo.endsWith("soleil")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Le Soleil devient ecrasant.");
				}
			}
			if (met.equals("tempete de sable") && !meteo.equals("tempete de sable")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Une tempete de sable se leve.");
				}
			}
			if (met.endsWith("vent fort") && !meteo.endsWith("vent fort")) {
				this.meteo = met;
				Pokemon1.setMeteo(met, affichage);
				Pokemon2.setMeteo(met, affichage);
				if (affichage) {
					System.out.println("Le vent se dechaine.");
				}
			}
		}
	}

	public void setTerrain(String terr){

		if (terr.equals("normal") && !terrain.equals("normal")){
			this.terrain = terr;
			Pokemon1.setTerrain(terr, affichage);
			Pokemon2.setTerrain(terr, affichage);
			if (affichage){
				System.out.println("Le terrain redevient normal.");
			}
		}
		if (terr.equals("Electric") && !terrain.equals("Electric")){
			this.terrain = terr;
			Pokemon1.setTerrain(terr, affichage);
			Pokemon2.setTerrain(terr, affichage);
			if (affichage){
				System.out.println("Le terrain s'electrifie.");
			}
		}
		if (terr.equals("Misty") && !terrain.equals("Misty")){
			this.terrain = terr;
			Pokemon1.setTerrain(terr, affichage);
			Pokemon2.setTerrain(terr, affichage);
			if (affichage){
				System.out.println("Le terrain devient brumeux.");
			}
		}
	}

	public void appliqueTalent(Pokemon pkmn1, Pokemon pkmn2){

		if (pkmn2.getTalent().endsWith(" (brise)") && !(pkmn1.getTalent().equals("Mold Breaker") || pkmn1.getTalent().equals("Teravolt"))){
			briseMoule = true;
			pkmn2.setBriseMoule(affichage);
		}

		if (pkmn1.getTalent().equals("Trace")){
			pkmn1.setTalent(pkmn2.getTalent(), false);
			if (affichage){
				System.out.println("\n" + pkmn1.getNom() + " calque " + pkmn1.getTalent() + " de " + pkmn2.getNom() + ".");
			}
		}

		if (pkmn1.getTalent().equals("Imposter")){
			if (affichage){
				System.out.println("");
			}
			pkmn1.morphing(pkmn2, affichage);
		}

		if (pkmn1.getTalent().equals("Anticipation") && affichage){
			boolean warning = false;
			for (int atq=0; atq<4; ++atq){
				String type = pkmn2.getMoves()[atq].getType();
				if (!type.equals("?") && pkmn2.getMoves()[atq].getPuissance() > 0){
					try{
						BufferedReader fis = new BufferedReader (new FileReader ("DataBase/typestable.txt"));
						int NtypeDef1 = -1;
						int NtypeDef2 = -1;
						String[] txt;
						String NumTypeAtq = "";
						for (int i=0; i<=19;++i){
							txt = fis.readLine().split(" ");
							if (txt[1].equals(type)){NumTypeAtq += i;}
							if (txt[1].equals(pkmn1.getType()[0])){NtypeDef1 = i;}
							if (txt[1].equals(pkmn1.getType()[1])){NtypeDef2 = i;}
						}

						do{txt = fis.readLine().split(" ");}
						while (!txt[0].equals(NumTypeAtq));

						double efficacite = Double.valueOf(txt[Integer.valueOf(NtypeDef1).intValue() + 1]).doubleValue() * Double.valueOf(txt[Integer.valueOf(NtypeDef2).intValue() + 1]).doubleValue() /4;

						if (efficacite > 2){
							warning = true;
						}
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("Probleme de type" + 1/0);
					}
				}
			}
			if (warning){
				System.out.println("\n" + pkmn1.getNom() + " est tout tremblant.");
			}
		}
		if (pkmn1.getTalent().equals("Delta Stream")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("vent fort");
			compteurMeteo = 999;
		}
		if (pkmn1.getTalent().equals("Desolate Land")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("fort soleil");
			compteurMeteo = 999;
		}
		if (pkmn1.getTalent().equals("Primordial Sea")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("forte pluie");
			compteurMeteo = 999;
		}

		if (pkmn1.getTalent().equals("Download")){
			if (affichage){
				System.out.println("");
			}
			if (pkmn2.getStatsModifiees()[2] < pkmn2.getStatsModifiees()[4]){
				pkmn1.multiBoost(new String[] {"Atq"}, new int[] {1}, true, true, affichage);
			}
			else{
				pkmn1.multiBoost(new String[] {"SAtq"}, new int[] {1}, true, true, affichage);
			}
		}

		if (pkmn1.getTalent().equals("Drizzle")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("pluie");
			compteurMeteo = 5;
			if (pkmn1.getObjet().equals("Damp Rock")){
				compteurMeteo = 8;
			}
		}
		if (pkmn1.getTalent().equals("Drought")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("soleil");
			compteurMeteo = 5;
			if (pkmn1.getObjet().equals("Heat Rock")){
				compteurMeteo = 8;
			}
		}

		if (pkmn1.getTalent().equals("Frisk")){
			if (affichage && !pkmn2.getObjet().equals("(No Item)")){
				System.out.println("\n" + pkmn1.getNom() + " fouille son adversaire et trouve " + pkmn2.getObjet() + ".");
			}
		}

		if (pkmn1.getTalent().equals("Intimidate")){
			if (affichage){
				System.out.println("\n" + pkmn1.getNom()+ " intimide " + pkmn2.getNom() + ".");
			}
			pkmn2.multiBoost(new String[] {"Atq"}, new int[] {-1}, false, false, affichage);
		}

		if (pkmn1.getTalent().equals("Mold Breaker")){
			pkmn2.setBriseMoule(affichage);
			if (affichage){
				System.out.println("\n" + pkmn1.getNom() + " a Brise Moule.");
			}
		}

		if (pkmn1.getTalent().equals("Teravolt")){
			pkmn2.setBriseMoule(affichage);
			if (affichage){
				System.out.println("\n" + pkmn1.getNom() + " a Teravoltage.");
			}
		}
		if (pkmn2.getTalent().equals("Mold Breaker") || pkmn2.getTalent().equals("Teravoltage")){
			pkmn1.setBriseMoule(affichage);
		}

		if (pkmn1.getTalent().equals("Pressure")){
			if (affichage){
				System.out.println("\n" + pkmn1.getNom() + " exerce Pression.");
			}
		}

		if (pkmn1.getTalent().equals("Sand Stream")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("tempete de sable");
			compteurMeteo = 5;
			if (pkmn1.getObjet().equals("Smooth Rock")){
				compteurMeteo = 8;
			}
		}
		if (pkmn1.getTalent().equals("Snow Warning")){
			if (affichage){
				System.out.println("");
			}
			this.setMeteo("grele");
			compteurMeteo = 5;
		}

		if (pkmn1.getTalent().equals("Unnerve")){
			pkmn2.setTendu(true);
			if (affichage){
				System.out.println("\n" + pkmn2.getNom() + " est tendu.");
			}
		}
	}

	public boolean switche(Pokemon switcheur, Pokemon adversaire, boolean selfInduced, boolean liberation, boolean relais, boolean premierTour){

		if (!premierTour){
			switcheur.setTenteSwitch();
		}

		if (!premierTour && adversaire.isPoursuiveur() && !adversaire.isDejaAttaque()){
			if (affichage){
				System.out.println("L'adversaire poursuit!");
			}
			boolean found = false;
			int i=0;
			while (!found){
				if (adversaire.getMoves()[i].getNom().equals("Pursuit")){
					found = true;
				}
				else{
					++i;
				}
			}
			this.attaque(adversaire, adversaire.getMoves()[i], switcheur, false);
		}

		boolean reussi = false;

		if (switcheur.getStatsModifiees()[0] > 0){
			reussi = switcheur.switche(selfInduced, liberation, relais, affichage);
		}

		adversaire.adversaireParti();

		if (!premierTour && reussi && switcheur.getStatsModifiees()[0] > 0){
			this.appliqueTalent(switcheur, adversaire);
		}

		return reussi;
	}

	public int degatsPrevus(Pokemon defenseur, Pokemon attaquant, Action actionDef){

		//		if (affichage){
		//			System.out.println("\nAnticipations de " + defenseur.getNom());
		//		}

		int PVinitiaux = (int) defenseur.getStatsModifiees()[0];
		int degatsMax = 0;

		Pokemon doubleDef = new Pokemon("Missingno", 1, "none", "Smogon");
		Pokemon doubleAtq = new Pokemon("Missingno", 1, "none", "Smogon");

		for (int i=0; i<4; ++i){

			if (attaquant.getMoves()[i].getPP() > 0 && !attaquant.getMoves()[i].isPossessif() && !((attaquant.getTaunted() > 0 || attaquant.isAssaut()) && !attaquant.getMoves()[i].getCategorie().equals("0") && !attaquant.getMoves()[i].getCategorie().equals("4") && !attaquant.getMoves()[i].getCategorie().equals("6") && !attaquant.getMoves()[i].getCategorie().equals("7")) && !(attaquant.getMoves()[i].getNom().equals("Detect") || attaquant.getMoves()[i].getNom().equals("Endure") || attaquant.getMoves()[i].getNom().equals("Protect") || attaquant.getMoves()[i].getNom().equals("King's Shield") || attaquant.getMoves()[i].getNom().equals("Spiky Shield")) && !attaquant.getMoves()[i].getNom().equals(attaquant.getEntrave())){
				for (int m=0; m<2; ++m){

					doubleDef = defenseur.clone();
					doubleAtq = attaquant.clone();

					boolean mevo = true;
					if (m == 1){
						mevo = false;
					}
					if (!doubleAtq.canMevo()){
						m = 1;
						mevo = false;
					}

					try{
						if (!(doubleAtq.isTourmente() && doubleAtq.getMoves()[i].getNom().equals(doubleAtq.getDerniereAttaque().getNom()))){
							++i;
							++m;
						}
					}
					catch(Exception e){
						//System.out.println("Exception Tourmente");
					}

					if (i<4){
						Action coupPrevu = new Action (doubleAtq.getMoves()[i], mevo);

						if (doubleAtq.isChoiced() || !doubleAtq.getChargement().equals("-") || doubleAtq.getEnColere() > 0 || doubleAtq.getEncore() > 0){
							try{
								doubleAtq.getDerniereAttaque().getNom();
								coupPrevu = new Action (doubleAtq.getDerniereAttaque(), mevo);
								i=10;
								m=10;
							}
							catch(Exception e){
								//System.out.println("exception geree");
							}
						}

						Combat prevision = new Combat (Bot, Bot, doubleDef, doubleAtq, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, false);
						prevision.simuleTour(actionDef, coupPrevu);

						// Degats directs
						int degats = (int) (PVinitiaux - doubleDef.getStatsModifiees()[0]);
						if (degats > degatsMax){
							degatsMax = degats;
						}

						//						if (false){
						//							System.out.println("Degats prevus : " + degats);
						//							System.out.println("Degats max : " + degatsMax);
						//							System.out.println("\n-----------------------");
						//						}
					}
				}
			}
		}
		//		System.out.println("Degats max subis par " + doubleDef.getNom() + " s'il fait " + actionDef.toString() + " : " + degatsMax);
		//		System.out.println("-----------------------");

		return degatsMax;
	}

	public int degatsPrevusJumpKick(Pokemon defenseur, Pokemon attaquant, Action actionAtq){

		//		if (affichage){
		//			System.out.println("\nAnticipations de " + defenseur.getNom());
		//		}

		int PVinitiaux = (int) defenseur.getStatsModifiees()[0];

		Pokemon doubleDef = new Pokemon("Missingno", 1, "none", "Smogon");
		Pokemon doubleAtq = new Pokemon("Missingno", 1, "none", "Smogon");

		doubleDef = defenseur.clone();
		doubleAtq = attaquant.clone();

		Combat prevision = new Combat (Bot, Bot, doubleDef, doubleAtq, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, false);
		prevision.attaque(doubleAtq, actionAtq.getAttaque(), doubleDef, true);

		// Degats directs
		int degats = (int) (PVinitiaux - doubleDef.getStatsModifiees()[0]);

		//						if (false){
		//							System.out.println("Degats prevus : " + degats);
		//							System.out.println("Degats max : " + degatsMax);
		//							System.out.println("\n-----------------------");
		//						}

		//System.out.println("Failed Jump Kick : " + degats);

		return degats;
	}

	public int[] survies(boolean affiche){

		Pokemon double1 = Pokemon1.clone();
		Pokemon double2 = Pokemon2.clone();

		for (int i=0; i<4; ++i){
			double1.getMoves()[i].setPrecision(101);
			if (double1.getMoves()[i].getTrouille() < 100){
				double1.getMoves()[i].setTrouille(-100);
			}
			if (double1.getMoves()[i].getTaux() < 100){
				double1.getMoves()[i].setTaux(-100);
			}
			double1.getMoves()[i].setCritique(-1);

			double2.getMoves()[i].setPrecision(101);
			if (double2.getMoves()[i].getTrouille() < 100){
				double2.getMoves()[i].setTrouille(-100);
			}
			if (double2.getMoves()[i].getTaux() < 100){
				double2.getMoves()[i].setTaux(-100);
			}
			double2.getMoves()[i].setCritique(-1);
		}

		double1.setSubstitute(false);
		double2.setSubstitute(false);

		float[] degats = {1000, 1000};

		for (int atq = 0; atq<4; ++atq){
			if (double1.getMoves()[atq].getPP() > 0 && !double1.getMoves()[atq].isPossessif() && !((double1.getTaunted() > 0 || double1.isAssaut()) && !double1.getMoves()[atq].getCategorie().equals("0") && !double1.getMoves()[atq].getCategorie().equals("4") && !double1.getMoves()[atq].getCategorie().equals("6") && !double1.getMoves()[atq].getCategorie().equals("7")) && !(double1.getMoves()[atq].getNom().equals("Detect") || double1.getMoves()[atq].getNom().equals("Endure") || double1.getMoves()[atq].getNom().equals("Protect") || double1.getMoves()[atq].getNom().equals("King's Shield") || double1.getMoves()[atq].getNom().equals("Spiky Shield")) && !double1.getMoves()[atq].getNom().equals(double1.getEntrave())){
				for (int m = 0; m < 2; ++m){
					boolean mevo = true;
					if (m == 1){
						mevo = false;
					}
					if (!double1.canMevo()){
						m = 1;
						mevo = false;
					}

					try{
						if (!(double1.isTourmente() && double1.getMoves()[atq].getNom().equals(double1.getDerniereAttaque().getNom()))){
							++atq;
							++m;
						}
					}
					catch(Exception e){
						//System.out.println("Exception Tourmente");
					}

					if (atq < 4){
						Action coup1 = new Action (double1.getMoves()[atq], mevo);
						if (double1.isChoiced() || !double1.getChargement().equals("-") || double1.getEnColere() > 0 || double1.getEncore() > 0){
							try{
								double1.getDerniereAttaque().getNom();
								coup1 = new Action (double1.getDerniereAttaque(), mevo);
								atq=10;
								m=10;
							}
							catch(Exception e){
								//System.out.println("exception geree");
							}
						}
						Combat testSurvie = new Combat (Bot, Bot, double1, double2, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, false);
						int deg = testSurvie.degatsPrevus(double1, double2, coup1);
						if (degats[0] > deg){
							degats[0] = deg;
						}
					}
				}
			}
		}

		for (int atq = 0; atq<4; ++atq){
			if (double2.getMoves()[atq].getPP() > 0 && !((double2.getTaunted() > 0 || double2.isAssaut()) && !double2.getMoves()[atq].getCategorie().equals("0") && !double2.getMoves()[atq].getCategorie().equals("4") && !double2.getMoves()[atq].getCategorie().equals("6") && !double2.getMoves()[atq].getCategorie().equals("7")) && !(double2.getMoves()[atq].getNom().equals("Detect") || double2.getMoves()[atq].getNom().equals("Endure") || double2.getMoves()[atq].getNom().equals("Protect") || double2.getMoves()[atq].getNom().equals("King's Shield") || double2.getMoves()[atq].getNom().equals("Spiky Shield"))){
				for (int m = 0; m < 2; ++m){
					boolean mevo = true;
					if (m == 1){
						mevo = false;
					}
					if (!double2.canMevo()){
						m = 1;
						mevo = false;
					}
					Action coup2 = new Action (double2.getMoves()[atq], mevo);
					Combat testSurvie = new Combat (Bot, Bot, double2, double1, meteo, compteurMeteo, terrain, compteurTerrain, gravite, zoneMagique, distorsion, tourniquet, false);
					int deg = testSurvie.degatsPrevus(double2, double1, coup2);
					if (degats[1] > deg){
						degats[1] = deg;
					}
				}
			}
		}

		int[] survie = new int[2];
		survie[0] = (int) (Pokemon1.getStatsModifiees()[0] / degats[0]);
		survie[1] = (int) (Pokemon2.getStatsModifiees()[0] / degats[1]);
		if (Pokemon1.getStatsModifiees()[0] <= 0){
			survie[0] = -999;
			if (Pokemon2.getStatsModifiees()[0] > 0){
				survie[1] = 999;
			}
		}
		if (Pokemon2.getStatsModifiees()[0] <= 0){
			survie[1] = -999;
			if (Pokemon1.getStatsModifiees()[0] > 0){
				survie[0] = 999;
			}
		}

		if (Pokemon1.isSubstitute()){
			++survie[0];
		}
		if (Pokemon2.isSubstitute()){
			++survie[1];
		}

		if (affiche){
			System.out.println("\n\nDegats previsionnels = " + Arrays.toString(degats));
			System.out.println("Survies previsionnelles = " + Arrays.toString(survie));
		}

		return survie;
	}

	public int Avantage(){
		int[] survie = this.survies(false);
		int avantage = ((int) survie[0]) - ((int) survie[1]);
		if (Pokemon1.getStatsModifiees()[5] > Pokemon2.getStatsModifiees()[5]){
			++avantage;
		}
		if (distorsion > 0 && Pokemon1.getStatsModifiees()[5] < Pokemon2.getStatsModifiees()[5]){
			++avantage;
		}
		if (avantage < -999){
			avantage = -999;
		}
		if (avantage > 999){
			avantage = 999;
		}
		return avantage;
	}

	public void simuleTour (Action action1, Action action2){

		// Prise de decision des joueurs
		Attaque attaque1 = action1.getAttaque();
		boolean mevo1 = action1.isMegaEvolution();
		boolean switch1 = action1.isSwitcher();
		Pokemon1.setActionPrevue(action1);

		Attaque attaque2 = action2.getAttaque();
		boolean mevo2 = action2.isMegaEvolution();
		boolean switch2 = action2.isSwitcher();
		Pokemon2.setActionPrevue(action2);

		Random alea = new Random();

		// Calcul des priorites
		int premier = 0;
		Pokemon PkmnRapide = Pokemon1;
		Attaque AtqRapide = attaque1;
		Pokemon PkmnLent = Pokemon2;
		Attaque AtqLente = attaque2;
		if (Pokemon1.getStatsModifiees()[5] > Pokemon2.getStatsModifiees()[5]){
			premier = 1;
		}
		if (Pokemon1.getStatsModifiees()[5] < Pokemon2.getStatsModifiees()[5]){
			premier = 2;
		}
		if (distorsion > 0){
			premier = 3-premier;
		}

		if ((Pokemon1.getObjet().equals("Lagging Tail") || Pokemon1.getTalent().equals("Stall")) && !(Pokemon2.getObjet().equals("Lagging Tail") || Pokemon2.getTalent().equals("Stall"))){
			premier = 2;
		}
		if ((Pokemon2.getObjet().equals("Lagging Tail") || Pokemon2.getTalent().equals("Stall")) && !(Pokemon1.getObjet().equals("Lagging Tail") || Pokemon1.getTalent().equals("Stall"))){
			premier = 1;
		}

		// Priorites
		int bonusPrio1 = 0;
		int bonusPrio2 = 0;
		if (Pokemon1.getTalent().equals("Gale Wings") && attaque1.getType().equals("Flying")){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Gale Wings") && attaque2.getType().equals("Flying")){
			++bonusPrio2;
		}
		if (Pokemon1.getTalent().equals("Prankster") && !(attaque1.getCategorie().equals("0") || attaque1.getCategorie().equals("4") || attaque1.getCategorie().equals("6") || attaque1.getCategorie().equals("7") || attaque1.getCategorie().equals("8"))){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Prankster") && !(attaque2.getCategorie().equals("0") || attaque2.getCategorie().equals("4") || attaque2.getCategorie().equals("6") || attaque2.getCategorie().equals("7") || attaque2.getCategorie().equals("8"))){
			++bonusPrio2;
		}
		if (Pokemon1.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio1;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon1.getNom() + " se declenche !");
				}
			}
		}
		if (Pokemon2.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio2;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon2.getNom() + " se declenche !");
				}
			}
		}

		if (attaque1.getPriorite() + bonusPrio1 > attaque2.getPriorite() + bonusPrio2){
			premier = 1;
		}
		if (attaque1.getPriorite() + bonusPrio1 < attaque2.getPriorite() + bonusPrio2){
			premier = 2;
		}

		// Switch
		if (premier == 1 && switch1){
			switch1 = false;
			this.switche(Pokemon1, Pokemon2, true, false, false, false);
		}
		if (premier == 2 && switch2){
			switch2 = false;
			this.switche(Pokemon2, Pokemon1, true, false, false, false);
		}
		if (switch1){
			switch1 = false;
			this.switche(Pokemon1, Pokemon2, true, false, false, false);
		}
		if (switch2){
			switch2 = false;
			this.switche(Pokemon2, Pokemon1, true, false, false, false);
		}

		// Mega-Evolution
		if (premier == 1 && mevo1){
			if (Pokemon1.MegaEvolution(affichage)){
				mevo1 = false;
				this.appliqueTalent(Pokemon1, Pokemon2);
			}
		}
		if (premier == 2 && mevo2){
			if (Pokemon2.MegaEvolution(affichage)){
				mevo2 = false;
				this.appliqueTalent(Pokemon2, Pokemon1);
			}
		}
		if (mevo1){
			if (Pokemon1.MegaEvolution(affichage)){
				mevo1 = false;
				this.appliqueTalent(Pokemon1, Pokemon2);
			}
		}
		if (mevo2){
			if (Pokemon2.MegaEvolution(affichage)){
				mevo2 = false;
				this.appliqueTalent(Pokemon2, Pokemon1);
			}
		}

		// Mitra-Poing
		if (attaque1.getNom().equals("Focus Punch")){
			Pokemon1.setFocus(affichage);
		}
		if (attaque2.getNom().equals("Focus Punch")){
			Pokemon2.setFocus(affichage);
		}

		// Priorites
		bonusPrio1 = 0;
		bonusPrio2 = 0;
		if (Pokemon1.getTalent().equals("Gale Wings") && attaque1.getType().equals("Flying")){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Gale Wings") && attaque2.getType().equals("Flying")){
			++bonusPrio2;
		}
		if (Pokemon1.getTalent().equals("Prankster") && !(attaque1.getCategorie().equals("0") || attaque1.getCategorie().equals("4") || attaque1.getCategorie().equals("6") || attaque1.getCategorie().equals("7") || attaque1.getCategorie().equals("8"))){
			++bonusPrio1;
		}
		if (Pokemon2.getTalent().equals("Prankster") && !(attaque2.getCategorie().equals("0") || attaque2.getCategorie().equals("4") || attaque2.getCategorie().equals("6") || attaque2.getCategorie().equals("7") || attaque2.getCategorie().equals("8"))){
			++bonusPrio2;
		}
		if (Pokemon1.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio1;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon1.getNom() + " se declenche !");
				}
			}
		}
		if (Pokemon2.getObjet().equals("Quick Claw")){
			if (alea.nextInt(16) < 3){
				++bonusPrio2;
				if (affichage){
					System.out.println("Vive Griffe de " + Pokemon2.getNom() + " se declenche !");
				}
			}
		}

		if (attaque1.getPriorite() + bonusPrio1 > attaque2.getPriorite() + bonusPrio2){
			premier = 1;
		}
		if (attaque1.getPriorite() + bonusPrio1 < attaque2.getPriorite() + bonusPrio2){
			premier = 2;
		}
		if (premier == 0){
			//if (affichage){
			//System.out.println("Speed Tie!");
			//}
			if (alea.nextInt(2) == 0){
				premier = 1;
			}
			else{
				premier = 2;
			}
		}
		if (premier == 1){
			PkmnRapide = Pokemon1;
			AtqRapide = attaque1;
			PkmnLent = Pokemon2;
			AtqLente = attaque2;
		}
		if (premier == 2){
			PkmnRapide = Pokemon2;
			AtqRapide = attaque2;
			PkmnLent = Pokemon1;
			AtqLente = attaque1;
		}

		// Attaque du premier Pokemon
		if (!AtqRapide.getNom().equals("(No Move)")){
			if (AtqLente.getNom().equals("Pursuit")){
				PkmnLent.setPoursuiveur();
			}
			this.attaque(PkmnRapide, AtqRapide, PkmnLent, false);
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		// Attaque du second Pokemon
		if (PkmnLent.getStatsModifiees()[0] > 0 && !PkmnLent.isDejaAttaque() && !PkmnLent.isDejaSwitche() && !AtqLente.getNom().equals("(No Move)")){
			this.attaque(PkmnLent, AtqLente, PkmnRapide, false);
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		// Fin du tour
		if (compteurMeteo >= 1){
			--compteurMeteo;
			if (compteurMeteo == 0){
				this.setMeteo("claire");
			}
			else{
				if (affichage){
					if (meteo.equals("grele")){
						System.out.println("\nLa grele continue de tomber.");
					}
					if (meteo.equals("pluie")){
						System.out.println("\nLa pluie continue de tomber.");
					}
					if (meteo.equals("soleil")){
						System.out.println("\nLes rayons du Soleil sont forts.");
					}
					if (meteo.equals("tempete de sable")){
						System.out.println("\nLa tempete de sable fait rage.");
					}
					if (meteo.equals("forte pluie")){
						System.out.println("\nLa pluie est diluvienne.");
					}
					if (meteo.equals("fort soleil")){
						System.out.println("\nLes rayons du Soleil sont ecrasants.");
					}
					if (meteo.equals("vent fort")){
						System.out.println("\nLe vent est déchaine.");
					}
				}
			}
		}

		if (PkmnRapide.getStatsModifiees()[0] <= 0){
			PkmnLent.adversaireParti();
		}
		if (PkmnLent.getStatsModifiees()[0] <= 0){
			PkmnRapide.adversaireParti();
		}

		if (compteurTerrain >= 1){
			--compteurTerrain;
			if (compteurTerrain == 0){
				this.setTerrain("normal");
			}
		}

		if (gravite >= 1){
			--gravite;
			if (gravite == 0){
				PkmnRapide.setGravite(false, affichage);
				PkmnLent.setGravite(false, affichage);
				if (affichage){
					System.out.println("La gravite redevient normale.");
				}
			}
		}

		if (zoneMagique >= 1){
			--zoneMagique;
			if (zoneMagique == 0){
				PkmnRapide.setZoneMagique(false);
				PkmnLent.setZoneMagique(false);
				if (affichage){
					System.out.println("La Zone Magique s'estompe.");
				}
			}
		}

		if (distorsion >= 1){
			--distorsion;
			if (distorsion == 0){
				if (affichage){
					System.out.println("\nLes dimensions faussees redeviennent normales.");
				}
			}
		}

		if (tourniquet >= 1){
			--tourniquet;
			if (tourniquet == 0){
				if (affichage){
					System.out.println("Le Tourniquet prend fin.");
				}
			}
		}

		if (!echoUtilise){
			compteurEcho = 0;
		}
		echoUtilise = false;

		if (PkmnRapide.getStatsModifiees()[0] > 0){
			if (PkmnRapide.isVampigraine() && PkmnLent.getStatsModifiees()[0] > 0 && !PkmnRapide.getTalent().equals("Magic Guard")){
				if (affichage){
					System.out.println("\nL'energie de " + PkmnRapide.getNom() + " est drainee.");
				}
				int delta = (int)(PkmnRapide.getStatsInitiales()[0]/8);
				if (PkmnRapide.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnRapide.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnRapide.deltaPV(- delta, false, true, 0, affichage);
				if (PkmnRapide.getTalent().equals("Liquid Ooze")){
					delta = -delta;
				}
				if (PkmnLent.getObjet().equals("Big Root")){
					delta *= 1.3;
				}
				PkmnLent.deltaPV(delta, false, true, 0, affichage);
			}

			PkmnRapide.finDeTour(affichage);

			if (PkmnRapide.getPrescience() == 0){
				if (affichage){
					System.out.println("\n" + PkmnRapide.getNom() + " subit l'attaque Prescience.");
				}
				this.attaque(PkmnLent, new Attaque ("Prescience"), PkmnRapide, true);
			}
		}
		else{
			PkmnLent.adversaireParti();
		}

		if (PkmnLent.getStatsModifiees()[0] > 0){
			if (PkmnLent.isVampigraine() && PkmnRapide.getStatsModifiees()[0] > 0 && !PkmnLent.getTalent().equals("Magic Guard")){
				if (affichage){
					System.out.println("\nL'energie de " + PkmnLent.getNom() + " est drainee.");
				}
				int delta = (int)(PkmnLent.getStatsInitiales()[0]/8);
				if (PkmnLent.getStatsModifiees()[0] < delta){
					delta = (int)(PkmnLent.getStatsModifiees()[0]);
				}
				if (delta < 1){
					delta = 1;
				}
				PkmnLent.deltaPV(- delta, false, true, 0, affichage);
				if (PkmnLent.getTalent().equals("Liquid Ooze")){
					delta = -delta;
				}
				if (PkmnRapide.getObjet().equals("Big Root")){
					delta *= 1.3;
				}
				PkmnRapide.deltaPV(delta, false, true, 0, affichage);
			}

			PkmnLent.finDeTour(affichage);

			if (PkmnLent.getPrescience() == 0){
				if (affichage){
					System.out.println("\n" + PkmnLent.getNom() + " subit l'attaque Prescience.");
				}
				this.attaque(PkmnRapide, new Attaque ("Prescience"), PkmnLent, true);
			}

			brouhaha = PkmnLent.isBrouhaha() || PkmnRapide.isBrouhaha();
		}
		else{
			PkmnRapide.adversaireParti();
		}
	}

	public boolean isAffichage() {
		return affichage;
	}


}
