import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Fenetre extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Panneau container = new Panneau();

	private Panneau[][] tabPanes;

	private boolean[] clics = new boolean[1000];

	public Fenetre(String titre){
		this.setTitle(titre);
		this.setSize(this.getToolkit().getScreenSize());
		this.setLocationRelativeTo(null);
		this.setContentPane(container);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(false);
	}

	public void visible(boolean visible){
		this.setContentPane(container);
		this.setVisible(visible);
	}

	public void miseEnForme(int[] nPanesParLigne){

		container = new Panneau();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		// Taille de tabPanes
		int largeur = 0;
		for (int i=0; i<nPanesParLigne.length; ++i){
			if (nPanesParLigne[i] > largeur){
				largeur = nPanesParLigne[i];
			}
		}
		tabPanes = new Panneau[nPanesParLigne.length][largeur+1];

		// Cr�ation des lignes
		for (int x = 0; x < nPanesParLigne.length; ++x){
			for (int y = 0; y<nPanesParLigne[x]+1; ++y){
				tabPanes[x][y] = new Panneau();
			}
			tabPanes[x][0].setLayout(new BoxLayout(tabPanes[x][0], BoxLayout.LINE_AXIS));
			for (int y = 1; y<nPanesParLigne[x]+1; ++y){
				tabPanes[x][0].add(tabPanes[x][y]);
			}
			container.add(tabPanes[x][0]);
		}
		this.setContentPane(container);
	}

	public void addLabel(String txt, int x, int y, boolean actualiser){
		JLabel label = new JLabel(txt);
		tabPanes[x][y+1].add(label);
		if (actualiser){
			this.setContentPane(container);
			this.setVisible(false);
			this.setVisible(true);
		}
	}

	public Bouton addButton (String infoBouton, int i, int x, int y, boolean actualiser){
		Bouton bouton = new Bouton(infoBouton);
		tabPanes[x][y+1].add(bouton);
		bouton.addActionListener(new BoutonListener(i));
		if (actualiser){
			this.setContentPane(container);
			this.setVisible(false);
			this.setVisible(true);
		}
		return bouton;
	}

	public void addImage (String image, int x, int y){
		String adresse = "Database/Images/" + image + ".png";
		try{
			tabPanes[x][y+1].dessine(adresse, 0, 0, tabPanes[x][y+1].getGraphics());
		}
		catch(Exception e){
			System.out.println("L'image de " + image + " n'a pas �t� correctement charg�e (x=" + x + ",y=" + y + ").");
			e.printStackTrace();
		}
	}

	public void addImage (String image, int x, int y, int largeur, int hauteur){
		String adresse = "Database/Images/" + image + ".png";
		try{
			tabPanes[x][y+1].dessine(adresse, 0, 0, largeur, hauteur, tabPanes[x][y+1].getGraphics());
		}
		catch(Exception e){
			System.out.println("L'image de " + image + " n'a pas �t� correctement charg�e.");
			e.printStackTrace();
		}
	}

	public void videFenetre (){
		container.removeAll();
		this.repaint();
	}
	
	public boolean isClic (){
		boolean result = false;
		for (int i=0; i<clics.length; ++i){
			result = (result || clics[i]);
		}
		return result;
	}

	public int quelClic () {
		for (int i=0; i<clics.length; ++i){
			if (clics[i]){
				return i;
			}
		}
		return -1;
	}

	public void resetClics() {
		for (int i=0; i<clics.length; ++i){
			clics[i] = false;
		}
	}

	public Panneau[][] getTabPanes() {
		return tabPanes;
	}

	class BoutonListener implements ActionListener{
		
		int index;
		
		public BoutonListener (int i){
			index = i;
		}
		
		public void actionPerformed(ActionEvent e) {
			clics[index] = true;
		}
	}

	public void actionPerformed(ActionEvent arg0){
		System.out.println("Click!");
	}
	
	public void associateBoutonListener (Bouton butt, int i){
		butt.addActionListener(new BoutonListener(i));
	}
}