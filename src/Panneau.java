import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Panneau extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String adresse = "Database/Images/(void).png";
	
	private Panneau[][] tabPanes;
	
	public void miseEnForme (int[] nPanesParLigne){

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Taille de tabPanes
		int largeur = 0;
		for (int i=0; i<nPanesParLigne.length; ++i){
			if (nPanesParLigne[i] > largeur){
				largeur = nPanesParLigne[i];
			}
		}
		tabPanes = new Panneau[nPanesParLigne.length][largeur+1];

		// Création des lignes
		for (int x = 0; x < nPanesParLigne.length; ++x){
			for (int y = 0; y<nPanesParLigne[x]+1; ++y){
				tabPanes[x][y] = new Panneau();
			}
			tabPanes[x][0].setLayout(new BoxLayout(tabPanes[x][0], BoxLayout.LINE_AXIS));
			for (int y = 1; y<nPanesParLigne[x]+1; ++y){
				tabPanes[x][0].add(tabPanes[x][y]);
			}
			this.add(tabPanes[x][0]);
		}
	}

	public void dessine(String adresse, int x, int y, Graphics g){
		this.adresse = adresse;
		try {
			Image img = ImageIO.read(new File(adresse));
			g.drawImage(img, x, y, this);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(adresse);
		}                
	}

	public void dessine(String adresse, int x, int y, int largeur, int hauteur, Graphics g){
		this.adresse = adresse;
		try {
			Image img = ImageIO.read(new File(adresse));
			g.drawImage(img, x, y, largeur, hauteur, this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}                
	}
	
	public void paintComponent (Graphics g){
		try {
			Image img = ImageIO.read(new File(adresse));
			g.drawImage(img, 0, 0, this);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Panneau[][] getTabPanes() {
		return tabPanes;
	}

	public void setTabPanes(Panneau[][] tabPanes) {
		this.tabPanes = tabPanes;
	}
}