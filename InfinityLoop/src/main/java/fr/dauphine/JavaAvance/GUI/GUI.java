package fr.dauphine.JavaAvance.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Icon;
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
import fr.dauphine.JavaAvance.Solve.Generator;

/**
 * This class handles the GUI
 * 
 *
 */
public class GUI {

	private static String fileName;
	private JFrame frame = new JFrame("InfinityLoop");
	private JPanel panel = new JPanel();
	private static ArrayList<ImageIcon> icons;
	private JButton[][] buttons;
	
	public static void changeIcons() {
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
	public static void startGUI(final String inputFile) {
		changeIcons();
		// We have to check that the grid is generated before to launch the GUI
		// construction
		Runnable task = new Runnable() {
			public void run() {
				
				final Grid grid = Checker.buildGrid(inputFile);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GUI window;
						window = new GUI(grid);
						window.frame.setSize(grid.getHeight()*50, grid.getWidth()*50);
						window.panel.setLayout(new GridLayout(grid.getHeight(), grid.getWidth()));
						window.frame.add(window.panel);
						window.frame.setVisible(true);
					}
				});
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
		this.buttons = new JButton[grid.getHeight()][grid.getWidth()];
		initialize(grid);
	}
	
	
	
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize(final Grid grid) {
		
		final JButton[][] buttons = this.buttons ; 
		for(int i = 0 ; i < grid.getHeight() ; i++) {
			for(int j = 0 ; j < grid.getWidth() ; j++) {
				final JButton button = new JButton(); 
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(button.getIcon() == null) {
							return;
						}
						else if (button.getIcon() == icons.get(3)) { //ONECONN
							button.setIcon(icons.get(0));
						}
						else if (button.getIcon() == icons.get(5)) { //BAR
							button.setIcon(icons.get(4));
						}
						else if (button.getIcon() == icons.get(9)) { //TTYPE
							button.setIcon(icons.get(6));
						}
						else if (button.getIcon() == icons.get(10)) { //FOURCONN
							return;
						}
						else if (button.getIcon() == icons.get(14)) { //LTYPE
							button.setIcon(icons.get(11));
						}
						else {
							button.setIcon( icons.get(icons.indexOf(button.getIcon()) + 1));
						}
						
						//grid.getPiece(fi, fj).turn();
						getGridFromButtons(grid);
						if (Checker.isSolution(grid))
							System.out.println("Gagne");
					}
				});
				button.setIcon(this.getImageIcon(grid.getPiece(i, j)));
				this.buttons[i][j] = button;
				this.panel.add(buttons[i][j]);
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
		int pOri = p.getOrientation().getValue();
		switch(p.getType()) {
		case VOID :
			return null;					//VOID(0) : 
		case ONECONN :
			return icons.get(0 + pOri); 	//ONECONN(1) : 1, 2, 3 et 4
		case BAR :
			return icons.get(4 + pOri); 	//BAR(2) : 5 et 6
		case TTYPE :
			return icons.get(6 + pOri); 	//TTYPE(3) : 7, 8, 9 et 10
		case FOURCONN :
			return icons.get(10 + pOri); 	//FOURCONN(4) : 11
		case LTYPE :
			return icons.get(11 + pOri); 	//LTYPE(5) : 12, 13, 14 et 15
		default :
			throw new IllegalArgumentException();
		}
	}
	
	 private void getGridFromButtons(Grid g) {
	        for (int i = 0; i < g.getHeight(); i++) {
	            for (int j = 0; j < g.getWidth(); j++) {
	                g.setPiece(i, j, this.getPieceFromButton(i,j));
	            }
	        }
	 }
	    
	private Piece getPieceFromButton(int i, int j) {
	    JButton button = this.buttons[i][j];
	    PieceType type = PieceType.VOID;
	    Orientation ori = Orientation.NORTH;
        Icon ic = button.getIcon();
        int indice = icons.indexOf(ic) + 1;
        int modulo = 0;
        if (indice >= 1 && indice < 5) {
            modulo = 1;
            type = PieceType.ONECONN;
        }
        else if (indice >= 5 && indice < 7) {
            modulo = 5;
            type = PieceType.BAR;
        }
        else if (indice >= 7 && indice < 11) {
            modulo = 7;
            type = PieceType.TTYPE;
        }
        else if (indice >= 11 && indice < 12) {
            modulo = 11;
            type = PieceType.FOURCONN;
        }
        else if (indice >= 12 && indice < 16) {
            modulo = 12;
            type = PieceType.LTYPE;
        }
        else {
        	modulo = 0;
        }	
        ori = Orientation.getOrifromValue(indice - modulo);
	    
	    return new Piece(i, j, type, ori);
	}


}