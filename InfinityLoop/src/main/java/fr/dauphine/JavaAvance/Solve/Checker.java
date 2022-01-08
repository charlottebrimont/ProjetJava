package fr.dauphine.JavaAvance.Solve;



import java.io.File;
import java.util.Scanner;

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
	
	
	
}
