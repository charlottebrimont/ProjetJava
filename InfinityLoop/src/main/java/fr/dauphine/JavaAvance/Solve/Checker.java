package fr.dauphine.JavaAvance.Solve;



import java.io.FileNotFoundException;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Checker {

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
	
	public static boolean isSolution(String gridfile) throws FileNotFoundException {
		Grid grid = new Grid(gridfile);
		return isSolution(grid);
	}
	
	public static Grid buildGrid(String inputFile) throws FileNotFoundException {
		Grid grid = new Grid(inputFile);
		return grid;
	}
	
	
	
}
