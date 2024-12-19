package zmija;

public class Pozicija {
	
	public enum Smer{
		LEVO, GORE, DESNO, DOLE;
	}
	
	private int vrsta, kolona;
	
	public Pozicija(int v, int k) {
		vrsta = v;
		kolona = k;
	}

	public int getVrsta() {
		return vrsta;
	}

	public int getKolona() {
		return kolona;
	}
	
	public Pozicija pomeri(Smer s) {
		int vrsta = this.vrsta;
		int kolona = this.kolona;
		switch (s) {
		case LEVO: 
			kolona--;
		    break;
		case DESNO: 
			kolona++;
			break;
		case GORE:
			vrsta--;
			break;
		case DOLE:
			vrsta++;
			break;
		}
		return new Pozicija(vrsta, kolona);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Pozicija)) return false;
		Pozicija p = (Pozicija)obj;
		return (p.getKolona() == this.kolona && p.getVrsta() == this.vrsta);
	}

}
