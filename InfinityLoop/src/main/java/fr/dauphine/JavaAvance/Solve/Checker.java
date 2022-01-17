package fr.dauphine.JavaAvance.Solve;



import java.io.FileNotFoundException;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Checker {

	/**
	 * Cette méthode return true si la Grid passée en paramètre est résolue
	 * Elle renvoie false sinon
	 * @param g
	 * @return
	 */
	public static boolean isSolution(Grid g) {
		for (Piece[] ligne : g.getAllPieces()) {
			for (Piece p : ligne) {
				if (!g.isTotallyConnected(p)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Cette méthode return true si le String passé en paramètre correspond à une Grid résolue
	 * Elle renvoie false sinon
	 * @param g
	 * @return
	 */
	public static boolean isSolution(String gridfile) throws FileNotFoundException {
		Grid grid = new Grid(gridfile);
		return isSolution(grid);
	}
	
	/**
	 * Cette méthode return la Grid associée au String passé en paramètre
	 * Elle renvoie false sinon
	 * @param g
	 * @return
	 */
	public static Grid buildGrid(String inputFile) throws FileNotFoundException {
		Grid grid = new Grid(inputFile);
		return grid;
	}
	
	
	
}
