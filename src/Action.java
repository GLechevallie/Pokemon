import com.rits.cloning.Cloner;


public class Action implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Attaque attaque;
	private boolean MegaEvolution;
	private boolean switcher;

	public Action (Attaque atq, boolean mevo){
		attaque = atq;
		MegaEvolution = mevo;
		switcher = false;
	}
	public Action (boolean switcher){
		attaque = new Attaque ("(No Move)");
		MegaEvolution = false;
		this.switcher = true;
	}

	public Attaque getAttaque() {
		return attaque;
	}

	public boolean isMegaEvolution() {
		return MegaEvolution;
	}

	public boolean isSwitcher() {
		return switcher;
	}
	public Action clone(){
		Cloner cloner = new Cloner();
		return (Action) cloner.deepClone(this);
	}

	@Override
	public String toString() {
		String res = "";
		if (this.switcher){
			res += "switch";
		}
		else{
			res = attaque.getNom();
			if (MegaEvolution){
				res = "Méga-évolution + " + res;
			}
		}
		return res;
	}
}
