package zmija;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import zmija.Pozicija.Smer;

public class Igra extends Frame {

	private Choice c;
	private Tabla tabla;
	private TextField t1, t2;
	private Label duzina;
	private int score = 0;
	Button dugme;
	
	public Igra() {
		setBounds(700, 300, 400, 400);
		addSouth();
		addCenter();
		addMenu();
		addListeners();
		setVisible(true);
	}
	
	public void addCenter() {
		tabla = new Tabla(20, 20);
		tabla.setIgra(this);
		add(tabla, BorderLayout.CENTER);
	}
	
	public void addSouth() {
		//south panel
		Panel jug = new Panel(new GridLayout(2, 2));
		
		//adding choice to south panel
		c = new Choice();
		c.add("Tezak");
		c.add("Srednji");
		c.add("Lak");
		jug.add(c);
		
		//adding button to south panel
		dugme = new Button("Pokreni igru");
		jug.add(dugme);
		
		//adding score label to south panel
		duzina = new Label("Score: " + score);
		duzina.setFont(new Font("Arial", Font.BOLD, 20));
		jug.add(duzina);
		
		//adding TextFields to south panel
		Panel pan = new Panel();
		Label l = new Label("vrsta, kolona: ");
		l.setFont(new Font("Arial", Font.BOLD, 10));
		pan.add(l);
		t1 = new TextField("20");
		t2 = new TextField("20");
		pan.add(t1);
		pan.add(t2);
		jug.add(pan);
		
		//adding south panel to frame
		add(jug, BorderLayout.SOUTH);
	}
	
	private void addMenu() {
		MenuBar bar = new MenuBar();
		setMenuBar(bar);
		Menu menu = new Menu("Menu");
		bar.add(menu);
		MenuItem novaIgra = new MenuItem("Nova igra", new MenuShortcut('N'));
		menu.add(novaIgra);
		novaIgra.addActionListener(e -> {
			tabla.prekini();
			tabla.setVrsta(20);
			tabla.setKolona(20);
			tabla.setZmija(null);
			tabla.setMuva(null);
			t1.setEnabled(true);
			t2.setEnabled(true);
			t1.setText("20");
			t2.setText("20");
			c.setEnabled(true);
			dugme.setEnabled(true);
			dugme.setLabel("Pokreni igru");
			tabla.setPobeda(false);
			tabla.setPoraz(false);
			score = 0;
			duzina.setText("Score: " + score);
			tabla.repaint();
		});
		menu.addSeparator();
		MenuItem zatvori = new MenuItem("Zatvori", new MenuShortcut('Z'));
		menu.add(zatvori);
		zatvori.addActionListener(e -> {
			tabla.prekini();
			dispose();});
		}
	
	public void addListeners() {
		//button listener
		dugme.addActionListener((ae) -> {
			switch(dugme.getLabel()) {
			//start game
			case "Pokreni igru":
				//set components 
				dugme.setLabel("Zaustavi");
				c.setEnabled(false);
				t1.setEnabled(false);
				t2.setEnabled(false);
				
				//add snake to the table
				tabla.dodajZmiju();
				
				//if size of the table is one then game is over and you won
				if(tabla.size == 1) {
					dugme.setEnabled(false);
					tabla.setPobeda(true);
					tabla.repaint();
					return;
				}
				
				//add fly to the table
				tabla.dodajMuvu();
				
				//set difficulty based on choice
				tabla.setTime(c.getSelectedItem());
				
				//start table thread
				tabla.pokreni();
				tabla.kreni();
				requestFocus();
				break;
				
			//pause game
			case "Zaustavi":
				tabla.zaustavi();
				dugme.setLabel("Nastavi");
				break;
				
			//continue game
			case "Nastavi":
				tabla.kreni();
				dugme.setLabel("Zaustavi");
				requestFocus();
				break;
			}
		});
		
		//keyboard listener
		addKeyListener(new KeyAdapter() {	
			@Override
			public void keyPressed(KeyEvent e) {
				//this flag is used to ensure that snake has moved at least one square to the direction we set last time
				//this way it avoids some unwanted behaviors 
				//like going in opposite direction(backwards) when fast clicking two directions, second being opposite direction
				if(!tabla.getChangeDirectionFlag()) return;
				
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if(tabla.getZmija().getSmer() == Smer.DOLE) return;
					tabla.getZmija().setSmer(Smer.GORE);
					break;
				case KeyEvent.VK_DOWN:
					if(tabla.getZmija().getSmer() == Smer.GORE) return;
					tabla.getZmija().setSmer(Smer.DOLE);
					break;
				case KeyEvent.VK_LEFT:
					if(tabla.getZmija().getSmer() == Smer.DESNO) return;
					tabla.getZmija().setSmer(Smer.LEVO);
					break;
				case KeyEvent.VK_RIGHT:
					if(tabla.getZmija().getSmer() == Smer.LEVO) return;
					tabla.getZmija().setSmer(Smer.DESNO);
					break;
				}
				tabla.setChangeDirectionFlag(false);
			}
		});
		
		//listeners for TextFields
		t1.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				//remove non digits characters
				String t = t1.getText();
				t = t.replaceAll("[^0-9]", "");
				
				//remove leading zeros
				int temp = 0;
				while(temp < t.length() && t.charAt(temp) == '0') temp++;
				String text = t.substring(temp);
				
				//set rows for table and draw it again
				t1.setText(text);
				int vrsta = text.equals("") ? 20 : Integer.parseInt(text);
				if(vrsta > 100) {
					vrsta = 100;
					t1.setText("100");
				}
				tabla.setVrsta(vrsta);
				tabla.repaint();
			}
		});
		t2.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				//remove non digits characters
				String t = t2.getText();
				t = t.replaceAll("[^0-9]", "");
				
				//remove leading zeros
				int temp = 0;
				while(temp < t.length() && t.charAt(temp) == '0') temp++;
				String text = t.substring(temp);
				
				//set columns for table and draw it again
				t2.setText(text);
				int kolona = text.equals("") ? 20 : Integer.parseInt(t2.getText());
				if(kolona > 100) {
					kolona = 100;
					t2.setText("100");
				}
				tabla.setKolona(kolona);
				tabla.repaint();
			}
		});
		
		//closing window listener
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();		
				tabla.prekini();
			}
		});
	}
	
	public void updateScore() {
		duzina.setText("Score: " + ++score);
	}
	
	public static void main(String[] args) {
		new Igra();
	}

}
