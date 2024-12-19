package zmija;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Tabla extends Canvas implements Runnable {
	
	private int vrsta, kolona;
	private Zmija zmija;
	private Muva muva;
	private Igra igra;
	private Thread nit;
	private boolean radi;
	private int[] vrste;
	private int[] kolone;
	private int time;
	private boolean changeDirectionFlag = false;
	boolean[][] zauzeto;
	private boolean poraz = false;
	private boolean pobeda = false;
	int size;
	int squareHeight;
	int squareWidth;
	List<Pozicija> slobodnePozicije = new ArrayList<>();
	
	
	public Tabla(int v, int k) {
		vrsta = v;
		kolona = k;
		size = v * k;
		vrste = new int[v];
		kolone = new int[k];
		zauzeto = new boolean[v][k];
		makeSlobodnePozicije();
		radi = false;
		setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public void run() {
		try {
			while(!nit.isInterrupted()) {
				synchronized(this) {
					if(!radi) wait();
				}
				repaint();
				Thread.sleep(time);
				zmija.pomeri();
				changeDirectionFlag = true;
			}
		} catch (InterruptedException e) {
		} catch (Greska e) {
			//snake has bit itself or went out of borders (defeat)
			nit.interrupt();
			poraz = true;
			igra.dugme.setEnabled(false);
			repaint();
		}
	}
	
	public void pokreni() {
		nit = new Thread(this);
		nit.start();
	}
	
	public void makeSlobodnePozicije() {
		slobodnePozicije = new ArrayList<>();
		for(int i = 0; i < vrsta; i++) 
			for(int j = 0; j < kolona; j++)
				slobodnePozicije.add(new Pozicija(i, j));
	}
	
	public void setTime(String s) {
		switch(s) {
		case "Tezak":
			time = 100;
			break;
		case "Srednji":
			time = 300;
			break;
		case "Lak":
			time = 500;
			break;
		}
	}
	
	public void prekini() {
		if(nit != null) nit.interrupt();
	}
	
	public synchronized void kreni() {
		radi = true;
		notify();
	}
	
	public synchronized void zaustavi() {
		radi = false;
	}
	
	public void setZmija(Zmija z) {
		zmija = z;
	}
	
	public void setIgra(Igra i) {
		igra = i;
	}
	
	public Igra getIgra() {
		return igra;
	}
	
	public void setChangeDirectionFlag(boolean b) {
		changeDirectionFlag = b;
	}
	
	public boolean getChangeDirectionFlag() {
		return changeDirectionFlag;
	}

	public int getVrsta() {
		return vrsta;
	}
	
	public void setPobeda(boolean p) {
		pobeda = p;
	}

	public void setVrsta(int vrsta) {
		this.vrsta = vrsta;
		vrste = new int[vrsta];
		zauzeto = new boolean[vrsta][kolona];
		size = vrsta * kolona;
		makeSlobodnePozicije();
	}

	public int getKolona() {
		return kolona;
	}
	
	public Zmija getZmija() {
		return zmija;
	}
	
	public boolean getPoraz() {
		return poraz;
	}
	
	public void setPoraz(boolean b) {
		poraz = b;
	}

	public void setKolona(int kolona) {
		this.kolona = kolona;
		kolone = new int[kolona];
		zauzeto = new boolean[vrsta][kolona];
		size = vrsta * kolona;
		makeSlobodnePozicije();
	}
		
	public int getVrste(int index) {
		return vrste[index];
	}
	
	public int getKolone(int index) {
		return kolone[index];
	}
	
	public void setMuva(Muva m) {
		muva = m;
	}
	
	public void dodajMuvu() {
		int ran = (int)(Math.random() * slobodnePozicije.size());
		Muva m = new Muva(slobodnePozicije.get(ran));
		m.setTabla(this);
		muva = m;
	}
	
	public void dodajZmiju() {
		Pozicija p = new Pozicija(vrsta / 2, kolona / 2);
		Zmija zmija = new Zmija(p);
		zauzeto[vrsta / 2][kolona / 2] = true;
		slobodnePozicije.remove(p);
		zmija.setTabla(this);
		this.zmija = zmija;
	}
	
	public Muva getMuva() {
		return muva;
	}
	
	public void drawLines(Graphics g) {
	   /* 
	    * modh, modw are used for resizing canvas to add one
	    * pixel to the first mod squares so that full width and
	    * height would be populated evenly to the last pixel
	    */

		//calculate x-axis coordinates of vertical lines
		squareWidth = getWidth() / kolona;
		int w1 = squareWidth + 1;
		int modw = getWidth() % kolona;
		int temp = 1;
		while(temp <= modw) kolone[temp] = temp++ * w1;
		while(temp < kolona) kolone[temp] = kolone[temp++ - 1] + squareWidth;
		
		//calculate y-axis coordinates of horizontal lines 
		squareHeight = getHeight() / vrsta;
		int h1 = squareHeight + 1;
		int modh = getHeight() % vrsta;
		temp = 1;
		while(temp <= modh) vrste[temp] = temp++ * h1;
		while(temp < vrsta) vrste[temp] = vrste[temp++ - 1] + squareHeight;
		
		//draw vertical lines
		for(int i = 0; i < kolone.length; i++) g.drawLine(kolone[i], 0, kolone[i], getHeight());
		g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
		
		//draw horizontal lines
		for(int i = 0; i < vrste.length; i++) g.drawLine(0, vrste[i], getWidth(), vrste[i]);
		g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
	}
	
	@Override
	public void paint(Graphics g) {
		drawLines(g);
		if(zmija != null) zmija.crtaj(g);
		if(muva != null) muva.crtaj(g);
		if(pobeda) {
			g.setColor(Color.magenta);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString("Cestitamo pobedili ste!", getWidth() / 10, getHeight() / 2);
		}
	}

}
