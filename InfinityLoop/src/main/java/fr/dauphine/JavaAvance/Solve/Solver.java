package fr.dauphine.JavaAvance.Solve;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Pair;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Solver {

	public static void main(String[] args) {

		// To be implemented

	}
	
	public static void solve(String FileName, Grid toSolveGrid) {
		boolean solvable = true;
		ArrayList<Piece> waiting = fixPieces(toSolveGrid);
		//waiting.add(g.getPiece(0, 0));		//si recurssive il faut recupéré la premiere piece non fixed
		
		while (solvable) {
			while (waiting.size() != 0) {
				Piece cur = waiting.get(0);
				
				ArrayList<Orientation> possibleOrientations = toSolveGrid.oriTotallyConnectedToFixed(cur);
				
				cur.setPossibleOrientations(toSolveGrid.oriTotallyConnectedToFixed(cur));
				if (cur.getPossibleOrientations().size() == 1) {
					cur.setFixed(true);
					
					Piece tp = toSolveGrid.topPiece(cur);
					Piece rp = toSolveGrid.rightPiece(cur);
					Piece bp = toSolveGrid.bottomPiece(cur);
					Piece lp = toSolveGrid.leftPiece(cur);
					
					if (tp != null && !tp.isFixed() && !waiting.contains(tp))
						waiting.add(tp);
					
					if (rp != null && !rp.isFixed() && !waiting.contains(rp))
						waiting.add(rp);
					
					if (bp != null && !bp.isFixed() && !waiting.contains(bp))
						waiting.add(bp);
						
					if (lp != null && !lp.isFixed() && !waiting.contains(lp))
						waiting.add(lp);
				}
				
				
				waiting.remove(cur);
			}
			
			//partie aleatoire sur les trucs possibles quand waiting est fini  //ici c'est pas mal de caler le multithread genre chaque thread test pour une valeur differente
			//fonction rescurssive pour fixer un truc et regarder si solve reussit le reste
			//stop quand une des pieces a 0 possible orientation
		}
		
		if (Checker.isSolution(toSolveGrid)) {
			System.out.println("SOLVED : true");	//pas sur de l'endroit ou afficher ca...
		}
		else {
			System.out.println("SOLVED : false");
		}
	}

	public static ArrayList<Piece> fixPieces(Grid toSolveGrid) {
		ArrayList<Piece> waiting = new ArrayList<Piece>();
		
		for (int i = 0; i < toSolveGrid.getHeight(); i++) {
			for (int j = 0; j < toSolveGrid.getWidth(); j++) {
				Piece cur = toSolveGrid.getPiece(i, j);
				cur.setPossibleOrientations(toSolveGrid.oriTotallyConnectedToFixed(cur));
				if (cur.getPossibleOrientations().size() == 1) {
					cur.setFixed(true);
					
					Piece tp = toSolveGrid.topPiece(cur);
					Piece rp = toSolveGrid.rightPiece(cur);
					Piece bp = toSolveGrid.bottomPiece(cur);
					Piece lp = toSolveGrid.leftPiece(cur);
					
					if (tp != null && !tp.isFixed() && !waiting.contains(tp))
						waiting.add(tp);
					
					if (rp != null && !rp.isFixed() && !waiting.contains(rp))
						waiting.add(rp);
					
					if (bp != null && !bp.isFixed() && !waiting.contains(bp))
						waiting.add(bp);
						
					if (lp != null && !lp.isFixed() && !waiting.contains(lp))
						waiting.add(lp);
				}
			}
		}
		
		return waiting;
	}
}
