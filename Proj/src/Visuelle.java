import java.awt.CardLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


//Cette classe gère l'interface graphique, à la fois l'affichage et l'interaction avec le joueur

public class Visuelle extends JFrame implements Afficheur, Interacteur{
	
	private JPanel mainPanel;
	private MenuCommencer menu1;
	private MenuNom menu2;
	private MenuLevel menu3;
	private MenuJeu menuJeu;
	//Ajouter pour plus tard menu plateau et menu fin
	
	private CardLayout cardLayout = new CardLayout();
	
	private Jeu j;
	
	//Attribut pour l'animation des blocs qui se déplacent
	private boolean running = true;
	private Thread t;
	
	//ces dimensions représentent l'écart de dimension entre la fenetre et le contenue de la fenetre
	//permettant de pouvoir redimmensionner plus précisément même si cela ne marche pas parfaitement
	private final int dHauteur;
	private final int dLargeur;
	
	private int maxLevel;
	
	public Visuelle(Jeu j) {
		this.j = j;
		
		this.setTitle("Pet Rescue");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700,500);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.menu1 = new MenuCommencer(this);
		this.menu2 = new MenuNom(this);
		this.menu3 = new MenuLevel(this);
		
		this.mainPanel = new JPanel(cardLayout);
		this.mainPanel.add("commencer", menu1);
		this.mainPanel.add("nom", menu2);
		this.mainPanel.add("level", menu3);
		
		this.getContentPane().add(mainPanel);
		
		this.setVisible(true);
		
		this.dHauteur = this.getHeight() - this.getContentPane().getHeight();
		this.dLargeur = this.getWidth() - this.getContentPane().getWidth();
	}
	
	@Override
	public void setMaxLevel(int levelMax) {
		this.maxLevel = levelMax;
	}
	
	public void start() {
		cardLayout.show(mainPanel, "commencer");
	}
	
	public void changeToName() {
		cardLayout.show(mainPanel, "nom");
	}

	public void changeToLevel() {
		this.menu3 = new MenuLevel(this);
		
		cardLayout.show(mainPanel, "level");
	}
	
	public void changeToPlateau(int i) {
		Plateau p = new Plateau(i);
		menuJeu = new MenuJeu(this, p);
		
		int coup = p.getCoup();
		if(coup > 0) menuJeu.setCoup(coup);
		
		this.setSize(menuJeu.getWidth() + this.dLargeur, menuJeu.getHeight() + this.dHauteur + 1);
		j.start(quelNom(), p);
		
		this.mainPanel.add("jeu", menuJeu);
		this.cardLayout.show(mainPanel, "jeu");
	}
	
	public String quelNom() {
		return menu2.getText();
	}
	
	public void joue(int i, int j) {
		this.j.turn(i,j);
		
	}
	public void rescue() {
		if(j.rescue()) j.move();
		else {
			if(j.finished()) j.finDePartie();
			running = false;
			menuJeu.unLock();
		}
	}
	 
	@Override
	public void prochainCoup() {
		
	}
	
	@Override
	public void veutRejouer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afficherP(Plateau p) {
		running = true;
		menuJeu.lock();
		t = new Thread(() -> {
			while(running) {
				menuJeu.afficheP();
				try {
					Thread.sleep(1000 / 60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			menuJeu.afficheP();
		});
		t.start();
		
		
	}

	@Override
	public void afficheScore(Joueur j) {
		menuJeu.setScore(j.getScore());
	}


	@Override
	public void afficheCoup(Plateau p) {
		int coup = p.getCoup();
		if(coup >= 0) menuJeu.setCoup(coup);
		
	}
	
	@Override
	public void afficheFinDePartie(Plateau p, Joueur j) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}