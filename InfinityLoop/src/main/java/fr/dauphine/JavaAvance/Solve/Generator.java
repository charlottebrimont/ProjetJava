package fr.dauphine.JavaAvance.Solve;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

/**
 * Generate a solution, number of connexe composant is not finished
 *
 */

public class Generator {

	private static Grid filledGrid;

	/**
	 * @param output
	 *            file name
	 * @throws IOException
	 *             - if an I/O error occurs.
	 * @return a File that contains a grid filled with pieces (a level)
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void generateLevel(String fileName, Grid inputGrid) {	//inputGrid a deja des pieces ? faut mettre le generated dans filled ? si oui a quoi sert input sauf pour les dimensions ?
		ArrayList<PieceType> possibleTypes;
		ArrayList<Orientation> possibleOrientations;
		
		//index 0 represent the upper piece
		//index 1 represent the left piece
		//we don't need the other because we initialize the grid from left to right and from up to down
		boolean[] oppConns = new boolean[2];
		
		for (int i = 0; i < inputGrid.getHeight(); i++) {
			for (int j = 0; j < inputGrid.getWidth(); j++) {
				possibleTypes = new ArrayList<PieceType>();
				possibleOrientations = new ArrayList<Orientation>();
				Piece p = new Piece(i, j);
				
				//we look the upper piece and the left piece to check wether there is connectors
				if (i == 0) {
					oppConns[0] = false;
				}
				else {
					oppConns[0] = inputGrid.topNeighbor(p).hasBottomConnector();
				}
				
				if (j == 0) {
					oppConns[j] = false;
				}
				else {
					oppConns[1] = inputGrid.leftNeighbor(p).hasBottomConnector();
				}
				
				//we add the types which match with the other pieces
				if (oppConns[0]) {
					if (oppConns[1]) {
						possibleTypes.add(PieceType.TTYPE);
						possibleTypes.add(PieceType.FOURCONN);
						possibleTypes.add(PieceType.LTYPE);
					}
					
					else {
						possibleTypes.add(PieceType.ONECONN);
						possibleTypes.add(PieceType.BAR);
						possibleTypes.add(PieceType.TTYPE);
						possibleTypes.add(PieceType.LTYPE);
					}
				}
				
				else {
					if (oppConns[1]) {
						possibleTypes.add(PieceType.ONECONN);
						possibleTypes.add(PieceType.BAR);
						possibleTypes.add(PieceType.TTYPE);
						possibleTypes.add(PieceType.LTYPE);
					}
					
					else {
						possibleTypes.add(PieceType.VOID);
						possibleTypes.add(PieceType.ONECONN);
						possibleTypes.add(PieceType.LTYPE);
					}
				}
				
				//We select randomly one of the possible types
				//We create the piece and set orientation to north
				Random rd = new Random();
				int rdInt = rd.nextInt(possibleTypes.size());
				
				p.setType(possibleTypes.get(rdInt));
				p.setOrientation((Orientation.NORTH).getValue());
				
				//For each possible orientation we look wether it matchs with the other pieces
				for (Orientation ori : p.getPossibleOrientations()) {
					p.setOrientation(ori.getValue());

					LinkedList<Orientation> conns = p.getConnectors();
					boolean matchNorth = (conns.contains(Orientation.NORTH) && oppConns[0]) || (!conns.contains(Orientation.NORTH) && !oppConns[0]);
					boolean matchWest = (conns.contains(Orientation.WEST) && oppConns[1]) || (!conns.contains(Orientation.WEST) && !oppConns[1]);
					
					if (matchNorth && matchWest) {
						possibleOrientations.add(ori);
					}
				}
				
				//we select randomly one of the possible orientation 
				rd = new Random();
				rdInt = rd.nextInt(possibleOrientations.size());
				
				p.setOrientation(possibleOrientations.get(rdInt).getValue());
				p.setFixed(true);
			}
		}
		
		mixGrid(inputGrid);
		//mettre dans un fichier
	}
	
	public static int[] copyGrid(Grid filledGrid, Grid inputGrid, int i, int j) {
		Piece p;
		int hmax = inputGrid.getHeight();
		int wmax = inputGrid.getWidth();

		if (inputGrid.getHeight() != filledGrid.getHeight())
			hmax = filledGrid.getHeight() + i; // we must adjust hmax to have the height of the original grid
		if (inputGrid.getWidth() != filledGrid.getWidth())
			wmax = filledGrid.getWidth() + j;

		int tmpi = 0;// temporary variable to stock the last index
		int tmpj = 0;

		// DEBUG System.out.println("copyGrid : i =" + i + " & j = " + j);
		// DEBUG System.out.println("hmax = " + hmax + " - wmax = " + wmax);
		for (int x = i; x < hmax; x++) {
			for (int y = j; y < wmax; y++) {
				// DEBUG System.out.println("x = " + x + " - y = " + y);
				p = filledGrid.getPiece(x - i, y - j);
				// DEBUG System.out.println("x = " + x + " - y = " +
				// y);System.out.println(p);
				inputGrid.setPiece(x, y, new Piece(x, y, p.getType(), p.getOrientation()));
				// DEBUG System.0out.println("x = " + x + " - y = " +
				// y);System.out.println(inputGrid.getPiece(x, y));
				tmpj = y;
			}
			tmpi = x;
		}
		//DEBUGSystem.out.println("tmpi =" + tmpi + " & tmpj = " + tmpj);
		return new int[] { tmpi, tmpj };
	}

	public static void mixGrid(Grid g) {
		Piece p = g.getPiece(0, 0);
		
		while (p != null) {
			Random rd = new Random();
			int rdInt = rd.nextInt(4);
			
			for (int i = 0; i < rdInt; i++) {
				p.turn();
			}
		}
	}
}