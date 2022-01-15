package fr.dauphine.JavaAvance.Solve;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Solver {

	public static void main(String[] args) {
		
		
		// To be implemented
		try {

		Grid g = new Grid(5, 7);
		Generator.generateLevel("test1.txt", g);
		g = new Grid("test1.txt");	
		
		//Piece p = new Piece(0, 0);
		//p.setType(PieceType.ONECONN);
		
		//System.out.println(DisplayUnicode.getUnicodeOfPiece(p.getType(), p.getOrientation()));
		
		//System.out.println(g.getPiece(0, 0));
		System.out.println("1: " + g);
		Grid tempG = new Grid(g);
		Piece tempP = new Piece(g.getPiece(2, 0));
		tempG.setPiece(0, 0, tempP);
		System.out.println("2: " + tempG);
		System.out.println("/n" + g.getPiece(0, 0)+"/n" + tempG.getPiece(0, 0));
		
		/*
		solve("test2.txt", g);
		
		g = new Grid("test2.txt");
		
		System.out.println("Check solve true : " + Checker.isSolution(g));
		*/

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
				
				Piece tp = toSolveGrid.topNeighbor(cur);
				Piece rp = toSolveGrid.rightNeighbor(cur);
				Piece bp = toSolveGrid.bottomNeighbor(cur);
				Piece lp = toSolveGrid.leftNeighbor(cur);
				
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
					cur.setOrientation(cur.getPossibleOrientations().get(0).getValue());
					cur.setFixed(true);
					
					Piece tp = toSolveGrid.topNeighbor(cur);
					Piece rp = toSolveGrid.rightNeighbor(cur);
					Piece bp = toSolveGrid.bottomNeighbor(cur);
					Piece lp = toSolveGrid.leftNeighbor(cur);
					
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
		while (alea != null && alea.isFixed()) {
			alea = toSolveGrid.getNextPiece(alea);
		}
		if (alea == null) {
			return 1;
		}
		for (Orientation ori : (toSolveGrid.oriTotallyConnectedToFixed(alea))) {   //ici c'est pas mal de caler le multithread 
			Grid tempG = new Grid(toSolveGrid);
			Piece tempP = new Piece(alea);
			tempG.setPiece(alea.getPosY(), alea.getPosY(), tempP);
			tempP.setOrientation(ori.getValue());
			
			if (solveWaiting(tempG) == 0) {
				if(Checker.isSolution(tempG)) {
					toSolveGrid.copieGrid(tempG);
					return 0;
				}
				else {
					solveAlea(tempG);
				}
			}
			
		}
		
		return 1;
	}
}