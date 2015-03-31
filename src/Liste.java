import java.util.ArrayList;


public class Liste {

	private ArrayList<String> liste0;
	
	public Liste (ArrayList<String> liste){
		this.liste0 = liste;
	}
	public Liste (){
		this.liste0 = new ArrayList<String>();
	}
	
	public int size(){
		return liste0.size();
	}
	
	public void add(String elt){
		liste0.add(elt);
	}
	
	public String get(int i){
		return liste0.get(i);
	}
	
	public ArrayList<String> getListe0() {
		return liste0;
	}
	public void setListe0(ArrayList<String> liste0) {
		this.liste0 = liste0;
	}
	
	public Liste sousListe(int min, int max){
		Liste sub = new Liste();
		for (int k=min; k<max; ++k){
			sub.add(this.get(k));
		}
		return sub;
	}
	
	@Override
	public String toString() {
		return liste0.toString();
	}
	
	private ArrayList<Liste> scinde(){
		ArrayList<Liste> res = new ArrayList<Liste>();
		if (liste0.size() <= 1){
			 res.add(new Liste(liste0));
			 res.add(new Liste());
		}
		else{
			res.add(this.sousListe(0, (int)(liste0.size()/2)));
			res.add(this.sousListe((int)(liste0.size()/2), liste0.size()));
		}
		return res;
	}
	
	private Liste fusion(ArrayList<Liste> tabs){
		if (tabs.get(0).size() == 0){
			return tabs.get(1);
		}
		else{
			if (tabs.get(1).size() == 0){
				return tabs.get(0);
			}
			else{
				int i0 = 0;
				int i1 = 0;
				Liste res = new Liste();
				while (i0 < tabs.get(0).size() && i1 < tabs.get(1).size()){
					if (Float.valueOf(tabs.get(0).get(i0).split(" ")[1]).floatValue() >= Float.valueOf(tabs.get(1).get(i1).split(" ")[1]).floatValue()){
						res.add(tabs.get(0).get(i0));
						++i0;
					}
					else{
						res.add(tabs.get(1).get(i1));
						++i1;
					}
				}
				while (i0 < tabs.get(0).size()){
					res.add(tabs.get(0).get(i0));
					++i0;
				}
				while (i1 < tabs.get(1).size()){
					res.add(tabs.get(1).get(i1));
					++i1;
				}
				return res;
			}
		}
	}
	
	public void triFusion(){
		if (liste0.size()<=1){
			// do nothing
		}
		else{
			ArrayList<Liste> scindat = this.scinde();
			scindat.get(0).triFusion();
			scindat.get(1).triFusion();
			this.setListe0(this.fusion(scindat).getListe0());
		}
	}
	
}
