package fr.dauphine.JavaAvance.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.Solve.Checker;

/**
 * This class handles the GUI
 * 
 *
 */
public class GUI {

	private JFrame frame = new JFrame("InfinityLoop");
	private JPanel panel = new JPanel();
	private static ArrayList<ImageIcon> icons;
	private JButton[][] buttons;
	
	static {
		icons = new ArrayList<ImageIcon>();
		String pathToIcons = "src/main/resources/fr/dauphine/JavaAvance/icons/io/";
		for (int i = 1 ; i < 16 ; i++) {
			icons.add(new ImageIcon(pathToIcons + i +".png"));
		}
	}
	

	/**
	 * 
	 * @param inputFile
	 *            String from IO
	 * @throws IOException
	 *             if there is a problem with the gui
	 */
	public static void startGUI(String inputFile) throws NullPointerException {
		// We have to check that the grid is generated before to launch the GUI
		// construction
		Runnable task = new Runnable() {
			public void run() {

				try {
					Grid grid = Checker.buildGrid(inputFile); //Pourquoi créer cette fonction dans Checker et ne pas simplement utilsier le constructeur Grid(String)??
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							GUI window;
							window = new GUI(grid);
							window.frame.setSize(grid.getHeight()*50, grid.getWidth()*50);
							window.frame.setVisible(true);
							
						}
					});
				} catch (IOException e) {
					throw new NullPointerException("Error with input file");
				}

			}
		};
		new Thread(task).start();

	}

	
	/**
	 * Create the application.
	 * @param icons 
	 * 
	 * @throws IOException
	 */
	
	public GUI(Grid grid) {
		//comment faire pour que ce soit le icons static ?
		this.buttons = new JButton[grid.getHeight()][grid.getWidth()];
		initialize(grid);
		//Ou faut-il initialiser le Frame et le Panel ?
	}
	
	
	
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	
	private void initialize(Grid grid) {
		
		// To implement:
		// creating frame, labels
		// Implementing method mouse clicked of interface MouseListener.
		
		final JButton[][] buttons = this.buttons ; //mettre final ?
		for(int i = 0 ; i < grid.getHeight() ; i++) {
			for(int j = 0 ; j < grid.getWidth() ; j++) {
				final JButton button = new JButton(); //mettre final ?
				
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(button.getIcon() == null) {
							return; 	//remplacer le return ???
						}
						else if (button.getIcon() == icons.get(4)) { //ONECONN
							button.setIcon(icons.get(1));
						}
						else if (button.getIcon() == icons.get(6)) { //BAR
							button.setIcon(icons.get(5));
						}
						else if (button.getIcon() == icons.get(10)) { //TTYPE
							button.setIcon(icons.get(7));
						}
						else if (button.getIcon() == icons.get(11)) { //FOURCONN
							return; 	//remplacer le return ???
						}
						else if (button.getIcon() == icons.get(15)) { //LTYPE
							button.setIcon(icons.get(12));
						}
						else {
							button.setIcon( icons.get(icons.indexOf(button.getIcon()) + 1));
						}
						
						buildFile("checkFile.txt", buttons);
						//if (Checker.isSolved("checkFile.txt")) //VOUS AVEZ GAGNE								//que faire ?????
							//System.out.println("Gagné.");
					}
				});
				button.setIcon(this.getImageIcon(grid.getPiece(i, j)));
				this.buttons[i][j] = button;
				this.panel.add(buttons[i][j]);
				//Changer ces lignes ?????
			}
		}
		
	}
	
	

	/**
	 * Display the correct image from the piece's type and orientation
	 * 
	 * @param p
	 *            the piece
	 * @return an image icon
	 */
	private ImageIcon getImageIcon(Piece p) {
		/*
		 * NORTH : 0
		 * EAST : 1
		 * SOUTH : 2
		 * WEST : 3
		 */
		int pOri = p.getOrientation().getValue(); //pareil que getValuefromOri ?
		switch(p.getType()) {
		case VOID :
			return null;					//VOID(0) : 
		case ONECONN :
			return icons.get(1 + pOri); 	//ONECONN(1) : 1, 2, 3 et 4
		case BAR :
			return icons.get(5 + pOri); 	//BAR(2) : 5 et 6
		case TTYPE :
			return icons.get(7 + pOri); 	//TTYPE(3) : 7, 8, 9 et 10
		case FOURCONN :
			return icons.get(11 + pOri); 	//FOURCONN(4) : 11
		case LTYPE :
			return icons.get(12 + pOri); 	//LTYPE(5) : 12, 13, 14 et 15
		default :
			throw new IllegalArgumentException();
		}
		
	}
	
	
	private static void buildFile(String string, JButton[][] buttons2) { //Change name / And everything else
		Charset charset = Charset.forName("US-ASCII");
		Path p = FileSystems.getDefault().getPath(string);
		try (BufferedWriter output = Files.newBufferedWriter(p, charset)){
			String text = "" + buttons2[0].length + "\n" + buttons2.length + "\n" ;
			
			for (JButton[] listbuttons : buttons2) {
				for (JButton button : listbuttons) {
					String tmp = null;
					if (button.getIcon() == null) {tmp = "0 0";}
					else if (button.getIcon() == icons.get(1)) {tmp = "1 0";} //ONECONN
					else if (button.getIcon() == icons.get(2)) {tmp = "1 1";} //ONECONN
					else if (button.getIcon() == icons.get(3)) {tmp = "1 2";} //ONECONN
					else if (button.getIcon() == icons.get(4)) {tmp = "1 3";} //ONECONN
					else if (button.getIcon() == icons.get(5)) {tmp = "2 0";} //BAR
					else if (button.getIcon() == icons.get(6)) {tmp = "2 1";} //BAR
					else if (button.getIcon() == icons.get(7)) {tmp = "3 0";} //TTYPE
					else if (button.getIcon() == icons.get(8)) {tmp = "3 1";} //TTYPE
					else if (button.getIcon() == icons.get(9)) {tmp = "3 2";} //TTYPE
					else if (button.getIcon() == icons.get(10)) {tmp = "3 3";} //TTYPE
					else if (button.getIcon() == icons.get(11)) {tmp = "4 0";} //FOURCONN
					else if (button.getIcon() == icons.get(12)) {tmp = "5 0";} //LTYPE
					else if (button.getIcon() == icons.get(13)) {tmp = "5 1";} //LTYPE
					else if (button.getIcon() == icons.get(14)) {tmp = "5 2";} //LTYPE
					else if (button.getIcon() == icons.get(15)) {tmp = "5 3";} //LTYPE
					
				}
			}
		}
		catch (IOException e) {
			System.out.println("Erreur fichier");
		}
	}

}
