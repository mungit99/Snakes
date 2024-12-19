package zmija;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Deque;
import java.util.LinkedList;
import zmija.Pozicija.Smer;

public class Zmija extends Figura {
	
	private Deque<Pozicija> pozicije;
	private Smer smer;
	private Tabla tabla;

	public Zmija(Pozicija p) {
		super(p, Color.GREEN);
		pozicije = new LinkedList<>();
		pozicije.offer(p);
		smer = Smer.DESNO;
		
	}

	@Override
	public void crtaj(Graphics g) {
		//paint the body of snake (red color if the flag for defeat is up)
		g.setColor(tabla.getPoraz() ? Color.RED : boja);
		for(Pozicija p: pozicije) {
			g.fillOval(tabla.getKolone(p.getKolona()), tabla.getVrste(p.getVrsta()), tabla.squareWidth, tabla.squareHeight);
		}
		
		//mark the head of snake with white inner circle 
		Pozicija head = pozicije.getLast();
		g.setColor(Color.WHITE);
		g.fillOval(tabla.getKolone(head.getKolona()) + tabla.squareWidth / 4, tabla.getVrste(head.getVrsta()) + tabla.squareHeight / 4, tabla.squareWidth / 2, tabla.squareHeight / 2);
	}
	
	public Deque<Pozicija> getPozicije() {
		return pozicije;
	}

	public void setSmer(Smer s) {
		smer = s;
	}
	
	public void setTabla(Tabla t) {
		tabla = t;
	}
	
	public Smer getSmer() {
		return smer;
	}
	
	public boolean pomeriZmiju() throws Greska {
		//get position of new head of the snake queue
		Pozicija head = pozicije.getLast();
		Pozicija newHead = head.pomeri(smer);
		
		//check if the position is within the boundaries of the table or if the snake has bitten itself
		if(newHead.getVrsta() < 0 || newHead.getVrsta() >= tabla.getVrsta() || newHead.getKolona() < 0 || newHead.getKolona() >= tabla.getKolona() || tabla.zauzeto[newHead.getVrsta()][newHead.getKolona()] == true)
			throw new Greska();
		
		//move snake, update occupied positions and return if snake ate a fly
		pozicije.offer(newHead);
		tabla.zauzeto[newHead.getVrsta()][newHead.getKolona()] = true;
		tabla.slobodnePozicije.remove(newHead);
		if(pozicije.getLast().equals(tabla.getMuva().getPozicija())) return true;
		return false;
	}

	@Override
	public void pomeri() throws Greska {
		//move snake one square 
		boolean pojedenaMuva = pomeriZmiju();
		
		//check if snake has eaten a fly 
		if(!pojedenaMuva) {
			//if not, remove tail of snake and update occupied positions
			Pozicija tail = pozicije.poll();
			tabla.zauzeto[tail.getVrsta()][tail.getKolona()] = false;
			tabla.slobodnePozicije.add(tail);
			return;
		}
		
		//fly is eaten
		//keep the tail, this way snake increases its length by one
		
		//update score on screen
		tabla.getIgra().updateScore();
		
		//check if the game is completed
		if(pozicije.size() == tabla.size) {
			tabla.setPobeda(true);
			tabla.prekini();
			tabla.repaint();
			tabla.setMuva(null);
			return;
		}
		
		//set new fly for the table
		tabla.dodajMuvu();
	}
}
