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
		
		for (int i = 0; i < inputGrid.getHeight(); i++) {
			for (int j = 0; j < inputGrid.getWidth(); j++) {
				
			}
		}
		
		mixGrid(inputGrid);
		//mettre dans un fichier (nom fileName passer en argument)
	}
	
	public static void generatecc(Grid ccGrid){
		ArrayList<Piece> path = new ArrayList<Piece>();
		
		path.add(new Piece(0, 0));
	}
	
	public static Piece generatePiece(Grid g, Piece p){
		int nbFixedNeighbours = g.nbFixedPiecesAround(p);
		ArrayList<Orientation> fixedConns = g.listFixedConnsAround(p);
		
		//We set the list of types that have a number of connectors compatible
		ArrayList<PieceType> possibleTypes = new ArrayList<PieceType>();
		for (int i = 0; i < 6; i++) {
			int nbConns = PieceType.getTypefromValue(i).getNbConnectors();
			if (nbConns >= fixedConns.size() && nbConns <= 4 - (nbFixedNeighbours - fixedConns.size())) {
				possibleTypes.add(PieceType.getTypefromValue(i));
			}
		}
		
		//We supress the types that are not compatible with the configuration
		for (PieceType pt : possibleTypes) {
			p.setType(pt);
			boolean validType = false;
			for (Orientation ori : p.getPossibleOrientations()) {
				p.setOrientation(ori.getValue());
				if (g.isTotallyConnectedToFixed(p)) {
					validType = true;
				}
			}
			
			if (!validType) {
				possibleTypes.remove(pt);
			}
		}
		
		//We select randomly one of the possible types
		//We set the type to the random one and the orientation to north
		Random rd = new Random();
		int rdInt = rd.nextInt(possibleTypes.size());
		
		p.setType(possibleTypes.get(rdInt));
		p.setOrientation((Orientation.NORTH).getValue());
		
		//We create a list of all the possible orientation for the random type
		//We supress the orientations that are not compatible with the fixed pieces around
		ArrayList<Orientation> possibleOrientations = p.getPossibleOrientations();
		
		//For each possible orientation we look wether it matchs with the other pieces
		for (Orientation ori : possibleOrientations) {
			p.setOrientation(ori.getValue());
			
			if (!g.isTotallyConnectedToFixed(p)) {
				possibleOrientations.remove(ori);
			}
		}
		
		//We select randomly an orientation
		rd = new Random();
		rdInt = rd.nextInt(possibleOrientations.size());
		
		p.setOrientation(possibleOrientations.get(rdInt).getValue());
		p.setFixed(true);
		
		return p;
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