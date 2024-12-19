package zmija;

import java.awt.Color;
import java.awt.Graphics;

import zmija.Pozicija.Smer;

public class Muva extends Figura {
	
	private Tabla tabla;

	public Muva(Pozicija p) {
		super(p, Color.BLACK);
	}

	@Override
	public void crtaj(Graphics g) {
		g.setColor(boja);
		int x = tabla.getKolone(pozicija.getKolona());
		int y = tabla.getVrste(pozicija.getVrsta());
		g.drawLine(x + tabla.squareWidth / 2, y, x + tabla.squareWidth / 2, y + tabla.squareHeight);
		g.drawLine(x, y + tabla.squareHeight / 2, x + tabla.squareWidth, y + tabla.squareHeight / 2);
		g.drawLine(x, y, x + tabla.squareWidth, y + tabla.squareHeight);
		g.drawLine(x, y + tabla.squareHeight, x + tabla.squareWidth, y);
	}
	
	public void setTabla(Tabla t) {
		tabla = t;
	}

	@Override
	public void pomeri() throws Greska {}

}
