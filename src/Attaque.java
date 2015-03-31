import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import com.rits.cloning.Cloner;


public class Attaque  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nom;
	private String cible;
	private String categorie = "-1";
	private String classe = "autre";
	private String type = "-";
	private int puissance = 0;
	private int precision = -1;
	private int PP = 0;
	private int priorite = 0;

	private String NCoups = "1";
	private int critique = 0;
	private int trouille = 0;
	private int recul = 0;

	private String effet = "-";
	private int[] variations = {0};
	private int taux = 0;

	private boolean contact = false;
	private boolean sonore = false;

	private boolean meFirst = false;
	private boolean possessif = false;

	public Attaque(String N){
		this.nom = N;

		try{
			BufferedReader liseur = new BufferedReader (new FileReader ("Database/Attaques/Liste Attaques.txt"));
			boolean cherche = true;
			String ligne = "";
			while (cherche){
				ligne = liseur.readLine();
				try{
					if (ligne.split("	")[1].equals(nom)){
						cherche = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e){}
			}
			categorie = ligne.split("	")[0];
			liseur.close();

			// CATEGORIE 0 : degets sans effet secondaire
			if (categorie.equals("0")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 0.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				classe = ligne.split("	")[0];
				type = ligne.split("	")[1];
				puissance = Integer.valueOf(ligne.split("	")[2]).intValue();
				precision = Integer.valueOf(ligne.split("	")[3]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[4]).intValue();
				PP = Integer.valueOf(ligne.split("	")[5]).intValue();
				cible = ligne.split("	")[6];
				NCoups = ligne.split("	")[7];
				critique = Integer.valueOf(ligne.split("	")[8]).intValue();
				trouille = Integer.valueOf(ligne.split("	")[9]).intValue();
				recul = Integer.valueOf(ligne.split("	")[10]).intValue();
				contact = ligne.split("	")[11].equals("yes");
				sonore = ligne.split("	")[12].equals("yes");

			}

			// CATEGORIE 1 : attaques de statut
			if (categorie.equals("1")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 1.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				type = ligne.split("	")[0];
				precision = Integer.valueOf(ligne.split("	")[1]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[2]).intValue();
				PP = Integer.valueOf(ligne.split("	")[3]).intValue();
				effet = ligne.split("	")[4];
				cible = ligne.split("	")[5];
				sonore = ligne.split("	")[6].equals("yes");
			}

			// CATEGORIE 2 : variations de stats
			if (categorie.equals("2")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 2.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				cible = ligne.split("	")[0];
				type = ligne.split("	")[1];
				precision = Integer.valueOf(ligne.split("	")[2]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[3]).intValue();
				PP = Integer.valueOf(ligne.split("	")[4]).intValue();
				effet = ligne.split("	")[5];
				variations = new int[ligne.split("	")[5].split("/").length];
				for (int i=0; i<variations.length; ++i){
					variations[i] = Integer.valueOf(ligne.split("	")[6].split("/")[i]).intValue();
				}
				sonore = ligne.split("	")[6].equals("yes");

			}

			// CATEGORIE 3 : attaques de soin
			if (categorie.equals("3")){
				cible = "user";

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 3.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				type = ligne.split("	")[0];
				precision = Integer.valueOf(ligne.split("	")[1]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[2]).intValue();
				PP = Integer.valueOf(ligne.split("	")[3]).intValue();
				effet = ligne.split("	")[4];
			}

			// CATEGORIE 4 : degats et statut
			if (categorie.equals("4")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 4.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				classe = ligne.split("	")[0];
				type = ligne.split("	")[1];
				puissance = Integer.valueOf(ligne.split("	")[2]).intValue();
				precision = Integer.valueOf(ligne.split("	")[3]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[4]).intValue();
				PP = Integer.valueOf(ligne.split("	")[5]).intValue();
				cible = ligne.split("	")[6];
				NCoups = ligne.split("	")[7];
				critique = Integer.valueOf(ligne.split("	")[8]).intValue();
				trouille = Integer.valueOf(ligne.split("	")[9]).intValue();
				recul = Integer.valueOf(ligne.split("	")[10]).intValue();
				effet = ligne.split("	")[11];
				taux = Integer.valueOf(ligne.split("	")[12]).intValue();
				contact = ligne.split("	")[13].equals("yes");
				sonore = ligne.split("	")[14].equals("yes");

			}

			// CATEGORIE 6 : degats et variation de stat de la cible
			if (categorie.equals("6")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 6.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				classe = ligne.split("	")[0];
				type = ligne.split("	")[1];
				puissance = Integer.valueOf(ligne.split("	")[2]).intValue();
				precision = Integer.valueOf(ligne.split("	")[3]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[4]).intValue();
				PP = Integer.valueOf(ligne.split("	")[5]).intValue();
				cible = ligne.split("	")[6];
				NCoups = ligne.split("	")[7];
				critique = Integer.valueOf(ligne.split("	")[8]).intValue();
				trouille = Integer.valueOf(ligne.split("	")[9]).intValue();
				recul = Integer.valueOf(ligne.split("	")[10]).intValue();
				effet = ligne.split("	")[11];

				variations = new int[ligne.split("	")[12].split("/").length];
				for (int i=0; i<variations.length; ++i){
					variations[i] = Integer.valueOf(ligne.split("	")[12].split("/")[i]).intValue();
				}

				taux = Integer.valueOf(ligne.split("	")[13]).intValue();
				contact = ligne.split("	")[14].equals("yes");
				sonore = ligne.split("	")[15].equals("yes");

			}

			// CATEGORIE 7 : degats et variation de stat du lanceur
			if (categorie.equals("7")){

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 7.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				classe = ligne.split("	")[0];
				type = ligne.split("	")[1];
				puissance = Integer.valueOf(ligne.split("	")[2]).intValue();
				precision = Integer.valueOf(ligne.split("	")[3]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[4]).intValue();
				PP = Integer.valueOf(ligne.split("	")[5]).intValue();
				cible = ligne.split("	")[6];
				NCoups = ligne.split("	")[7];
				critique = Integer.valueOf(ligne.split("	")[8]).intValue();
				trouille = Integer.valueOf(ligne.split("	")[9]).intValue();
				recul = Integer.valueOf(ligne.split("	")[10]).intValue();
				effet = ligne.split("	")[11];

				variations = new int[ligne.split("	")[12].split("/").length];
				for (int i=0; i<variations.length; ++i){
					variations[i] = Integer.valueOf(ligne.split("	")[12].split("/")[i]).intValue();
				}

				taux = Integer.valueOf(ligne.split("	")[13]).intValue();
				contact = ligne.split("	")[14].equals("yes");

			}

			// CATEGORIE 8 : vol de vie
			if (categorie.equals("8")){
				cible = "adv";

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 8.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				classe = ligne.split("	")[0];
				type = ligne.split("	")[1];
				puissance = Integer.valueOf(ligne.split("	")[2]).intValue();
				precision = Integer.valueOf(ligne.split("	")[3]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[4]).intValue();
				PP = Integer.valueOf(ligne.split("	")[5]).intValue();
				recul = Integer.valueOf(ligne.split("	")[6]).intValue();
				contact = ligne.split("	")[7].equals("yes");
			}

			// CATEGORIE 10 : terrain
			if (categorie.equals("10")){
				cible = "Field";

				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 10.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				type = ligne.split("	")[0];
				precision = 101;
				priorite = Integer.valueOf(ligne.split("	")[1]).intValue();
				PP = Integer.valueOf(ligne.split("	")[2]).intValue();
			}

			// CATEGORIE 11 : attaques d'equipe
			if (categorie.equals("11")){
				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 11.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				cible = ligne.split("	")[0] + "Field";
				type = ligne.split("	")[1];
				precision = 101;
				priorite = Integer.valueOf(ligne.split("	")[2]).intValue();
				PP = Integer.valueOf(ligne.split("	")[3]).intValue();
			}

			// CATEGORIE 13 : autres
			if (categorie.equals("13")){
				liseur = new BufferedReader (new FileReader ("Database/Attaques/Categorie 13.txt"));
				cherche = true;
				ligne = "";
				while (cherche){
					if (ligne.equals(nom)){
						cherche = false;
					}
					ligne = liseur.readLine();
				}
				type = ligne.split("	")[0];
				precision = Integer.valueOf(ligne.split("	")[1]).intValue();
				priorite = Integer.valueOf(ligne.split("	")[2]).intValue();
				PP = Integer.valueOf(ligne.split("	")[3]).intValue();
				cible = ligne.split("	")[4];
				sonore = ligne.split("	")[5].equals("yes");
			}
		}
		catch(NullPointerException e){
			System.out.println("Attaque '" + nom + "' (categorie " + this.categorie + ") non-implementee.");
			System.out.println(1/0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if (!(cible.equals("user") || cible.equals("adv") || cible.equals("allAdv") || cible.equals("allAdj") || cible.equals("userTeam") || cible.equals("Allie") || cible.equals("all") || cible.equals("Field") || cible.equals("userField") || cible.equals("advField") || nom.equals("(No Move)"))){
			System.out.println(nom + " - Cible incorrecte : " + cible);
		}
	}

	public void retirePP(int N){
		this.PP = this.PP-N;
		if (this.PP < 0){
			this.PP = 0;
		}
	}

	public double probaCritique (int bonus){
		double rep = 6.25;
		if (critique + bonus == 1){
			rep = 12.5;
		}
		if (critique + bonus == 2){
			rep = 50;
		}
		if (critique + bonus >= 3){
			rep = 100;
		}
		if (critique < 0){
			rep = 0;
		}
		return rep;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCible() {
		return cible;
	}

	public void setCible(String cible) {
		this.cible = cible;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getClasse() {
		return classe;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPuissance() {
		return puissance;
	}

	public int getPrecision() {
		return precision;
	}

	public int getPP() {
		return PP;
	}

	public void setPP(int pP) {
		PP = pP;
	}

	public int getPriorite() {
		return priorite;
	}

	public String getNCoups() {
		return NCoups;
	}

	public int getCritique() {
		return critique;
	}

	public int getTrouille() {
		return trouille;
	}

	public int getRecul() {
		return recul;
	}

	public String getEffet() {
		return effet;
	}

	public int[] getVariations() {
		return variations;
	}

	public int getTaux() {
		return taux;
	}

	public boolean isContact() {
		return contact;
	}

	public boolean isSonore() {
		return sonore;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setPriorite(int priorite) {
		this.priorite = priorite;
	}

	public void setNCoups(String nCoups) {
		NCoups = nCoups;
	}

	public void setCritique(int critique) {
		this.critique = critique;
	}

	public void setTrouille(int trouille) {
		this.trouille = trouille;
	}

	public void setRecul(int recul) {
		this.recul = recul;
	}

	public void setEffet(String effet) {
		this.effet = effet;
	}

	public void setVariations(int[] variations) {
		this.variations = variations;
	}

	public void setTaux(int taux) {
		this.taux = taux;
	}

	public void setContact(boolean contact) {
		this.contact = contact;
	}

	public boolean isMeFirst() {
		return meFirst;
	}

	public void setMeFirst(boolean meFirst) {
		this.meFirst = meFirst;
	}

	public boolean isPossessif() {
		return possessif;
	}

	public void setPossessif(boolean possessif) {
		this.possessif = possessif;
	}

	public Attaque clone(){
		try{
			Cloner cloner = new Cloner();
			return (Attaque) cloner.deepClone(this);
		}
		catch(NoClassDefFoundError e){}
		return new Attaque("Nothing");
	}


	@Override
	public String toString() {
		String decalage = "";
		if (nom.length()<11){decalage += "\t";}
		if (nom.length()<7){decalage += "\t";}
		return "\n " + nom + decalage + "\t[cible=" + cible + ", categorie=" + categorie + ", classe="
				+ classe + ", type=" + type + ", puissance=" + puissance
				+ ", precision=" + precision + ", PP=" + PP + ", priorite="
				+ priorite + ", NCoups=" + NCoups + ", critique=" + critique
				+ ", trouille=+" + trouille + ", recul=" + recul + ", effet="
				+ effet + ", variation=" + Arrays.toString(variations) + ", taux=" + taux + ", contact=" + contact + ", sonore=" + sonore + "]";
	}
}
