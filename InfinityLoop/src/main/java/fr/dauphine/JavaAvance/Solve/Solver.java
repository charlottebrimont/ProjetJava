package fr.dauphine.JavaAvance.Solve;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.DisplayUnicode;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Solver {

	public static void main(String[] args) {
		
		
		// To be implemented
		try {
		Grid g = new Grid(5, 7);
		Generator.generateLevel("test1.txt", g);
		g = new Grid("test1.txt");	
		
		Piece p = new Piece(0, 0);
		p.setType(PieceType.ONECONN);
		
		System.out.println(DisplayUnicode.getUnicodeOfPiece(p.getType(), p.getOrientation()));
		
		System.out.println(g.getPiece(0, 0));
		System.out.println(g);
		
		solve("test2.txt", g);
		
		g = new Grid("test2.txt");
		
		
		
		
		System.out.println("Check solve true : " + Checker.isSolution(g));
		} catch (FileNotFoundException e) {
			System.err.println("Erreur : " + e);
		}
		
		
	}
	
	public static void solve(String fileName, Grid toSolveGrid) {
		if (!Checker.isSolution(toSolveGrid)) {
			if (solveWaiting(toSolveGrid) == 1) {
				System.out.println("SOLVED : false");		//pas sur de l'endroit ou afficher ca... (je veux dire sur le terminal)
				return;
			}
			
			while (!Checker.isSolution(toSolveGrid)) {
				if (solveAlea(toSolveGrid) == 1) {
					System.out.println("SOLVED : false");		//pas sur de l'endroit ou afficher ca... (je veux dire sur le terminal)
					return;
				}			
			}
		}
		
		System.out.println("SOLVED : true");		//pas sur de l'endroit ou afficher ca... (je veux le dire sur le terminal)
		toSolveGrid.writeGridFile(fileName);
	}
	
	public static int solveWaiting(Grid toSolveGrid) {
		ArrayList<Piece> waiting = initWaiting(toSolveGrid);
		
		while (waiting.size() != 0) {
			Piece cur = waiting.get(0);
			
			ArrayList<Orientation> possibleOrientations = toSolveGrid.oriTotallyConnectedToFixed(cur);
			
			cur.setPossibleOrientations(toSolveGrid.oriTotallyConnectedToFixed(cur));
			
			if (cur.getPossibleOrientations().size() == 0) {
				return 1;
			}
			
			
			if (cur.getPossibleOrientations().size() == 1) {
				cur.setOrientation(possibleOrientations.get(0).getValue());
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
		return 0;
	}

	public static ArrayList<Piece> initWaiting(Grid toSolveGrid) {
		ArrayList<Piece> waiting = new ArrayList<Piece>();
		
		for (int i = 0; i < toSolveGrid.getHeight(); i++) {
			for (int j = 0; j < toSolveGrid.getWidth(); j++) {
				Piece cur = toSolveGrid.getPiece(i, j);
				cur.setPossibleOrientations(toSolveGrid.oriTotallyConnectedToFixed(cur));
				if (cur.isFixed() || cur.getPossibleOrientations().size() == 1) {
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

	public static int solveAlea(Grid toSolveGrid) {
		Piece alea = toSolveGrid.getPiece(0, 0);
		
		while (alea.isFixed()) {
			alea = toSolveGrid.getNextPiece(alea);
		}
		
		for (Orientation ori : (toSolveGrid.oriTotallyConnectedToFixed(alea))) {   //ici c'est pas mal de caler le multithread 
			alea.setOrientation(ori.getValue());
			
			if (solveWaiting(toSolveGrid) == 0) {
				return 0;
			}
		}
		
		return 1;
	}
}
