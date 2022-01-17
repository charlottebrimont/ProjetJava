package fr.dauphine.JavaAvance.Solve;


import java.util.ArrayList;
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
	
	public static Grid getGrid() {
		return filledGrid;
	}
	
	public static void setGrid(Grid inputGrid) {
		filledGrid = new Grid(inputGrid);
		
	}
	
	
	/**
	 * @param output
	 *            file name
	 * @throws IOException
	 *             - if an I/O error occurs.
	 * @return a File that contains a grid filled with pieces (a level)
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void generateLevel(String fileName, Grid inputGrid) {
		//We initialize filledGrid with the size of inputGrid
		filledGrid = new Grid(inputGrid.getWidth(), inputGrid.getHeight(), inputGrid.getNbcc());
		
		if (filledGrid.getNbcc() == -1) {
			generateRandomLevel();
		}
		else {
			for (int cpt = 0; cpt < filledGrid.getNbcc(); cpt++) {
				generateCC();
			}
		}
		
		mixGrid(filledGrid);
		filledGrid.writeGridFile(fileName);
	}
	
	public static void generateRandomLevel() {
		Piece cur = filledGrid.getPiece(0, 0);
		
		while (cur != null) {
			generatePiece(cur);
			cur = filledGrid.getNextPiece(cur);
		}
	}
	
	public static void generateCC(){
		ArrayList<Piece> cc = new ArrayList<Piece>();
		ArrayList<Piece> waiting = new ArrayList<Piece>();
		Piece start = filledGrid.getPiece(0, 0);
		while (start.isFixed() && filledGrid.getNextPiece(start) != null) {
			start = filledGrid.getNextPiece(start);
		}
		if (filledGrid.getNextPiece(start) == null) {
			return;
		}
		waiting.add(start);
		
		while (waiting.size() != 0) {
			Piece cur = waiting.get(0);
			
			generatePiece(cur);
			cc.add(cur);						
			
			//We check that the first piece isn't void to avoid counting a void piece as a connexe component
			//If it's a void we take the next Piece
			if (cur.getType() == PieceType.VOID) {
				cc.remove(cur);
				
				while (start.isFixed() && filledGrid.getNextPiece(start) != null) {
					start = filledGrid.getNextPiece(start);
				}
				if (filledGrid.getNextPiece(start) == null) {
					return;
				}
				
				waiting.add(start);
			}
			
			//We remove the current piece from the waiting list
			waiting.remove(cur);
			
			//We update the waiting list by adding the neighbors that aren't fixed and that aren't already in the waiting list
			for (Orientation ori : cur.getConnectors()) {
				Piece oriP;
				switch (ori) {
				case NORTH:
					oriP = filledGrid.topPiece(cur);
					break;
				case EAST:
					oriP = filledGrid.rightPiece(cur);
					break;
				case SOUTH:
					oriP = filledGrid.bottomPiece(cur);
					break;
				case WEST:
					oriP = filledGrid.leftPiece(cur);
					break;
				default :
					oriP = null;
				}
					
				if (!oriP.isFixed() && !waiting.contains(oriP) && !cc.contains(oriP)) {
					waiting.add(oriP);
				}
			}
		}
	}
	
	public static void generatePiece(Piece p){
		int nbFixedNeighbours = filledGrid.nbFixedPiecesAround(p);
		ArrayList<Orientation> fixedConns = filledGrid.listFixedConnsAround(p);
		
		//We set the list of types that have a number of connectors compatible
		ArrayList<PieceType> possibleTypes = new ArrayList<PieceType>();
		for (int i = 0; i < 6; i++) {
			int nbConns = PieceType.getTypefromValue(i).getNbConnectors();
			if (nbConns >= fixedConns.size() && nbConns <= 4 - (nbFixedNeighbours - fixedConns.size())) {
				possibleTypes.add(PieceType.getTypefromValue(i));
			}
		}
		
		//We suppress the types that are not compatible with the configuration
		ArrayList<PieceType> toSuppress = new ArrayList<PieceType>();
		
		for (PieceType pt : possibleTypes) {
			p.setType(pt);
			boolean validType = false;
			for (Orientation ori : p.getPossibleOrientations()) {
				p.setOrientation(ori.getValue());
				if (filledGrid.oriTotallyConnectedToFixedGenerator(p).size() != 0) {
					validType = true;
				}
			}
			
			if (!validType) {
				toSuppress.add(pt);
			}
		} 
		
		for (PieceType pt : toSuppress) {
			possibleTypes.remove(pt);
		}
		
		//We select randomly one of the possible types
		//We set the type to the random one and the orientation to north
		Random rd = new Random();
		int rdInt = rd.nextInt(possibleTypes.size());
		
		p.setType(possibleTypes.get(rdInt));
		p.setOrientation((Orientation.NORTH).getValue());
		
		//We create a list of all the possible orientation for the random type
		//We use the function to give us only the orientations that matches with all the fixed neighbours
		ArrayList<Orientation> possibleOrientations = filledGrid.oriTotallyConnectedToFixedGenerator(p);
				
		//We select randomly an orientation
		rd = new Random();
		rdInt = rd.nextInt(possibleOrientations.size());
		
		p.setOrientation(possibleOrientations.get(rdInt).getValue());
		
		p.setFixed(true);
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
			
			p = g.getNextPiece(p);
		}
	}
}