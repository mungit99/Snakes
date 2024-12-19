package zmija;

import java.awt.Color;
import java.awt.Graphics;

import zmija.Pozicija.Smer;

public abstract class Figura {
	
	protected Pozicija pozicija;
	protected Color boja;

	public Figura(Pozicija p, Color c) {
		pozicija = p;
		boja = c;
	}

	public Pozicija getPozicija() {
		return pozicija;
	}

	public void setBoja(Color boja) {
		this.boja = boja;
	}
	
	public abstract void crtaj(Graphics g);
	
	public abstract void pomeri() throws Greska;

}
