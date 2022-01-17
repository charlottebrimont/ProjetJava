package fr.dauphine.JavaAvance.Solve;

import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Solver {
	private static String fileName;

	public static void main(String[] args) {
		
		// To be implemented
		Grid g = new Grid(50, 50);
		Generator.generateLevel("test1.txt", g);
		g = new Grid("test1.txt");
		
		solve("test2.txt", g);
		
		g = new Grid("test2.txt");
		System.out.println(g);
	}
	
	public static void solve(String fileName, Grid toSolveGrid) {
		Solver.fileName = fileName;
		solveRec(initWaiting(toSolveGrid), toSolveGrid);
		
		toSolveGrid = new Grid(fileName);
		if (Checker.isSolution(toSolveGrid))
			return;
	}

	
	public static int solveRec (ArrayList<Piece> waiting, Grid toSolveGrid) {
		if (!Checker.isSolution(toSolveGrid)) {
			if (solveWaiting(waiting, toSolveGrid) == 1) 
				return 1;
			
			if (Checker.isSolution(toSolveGrid)) {
				System.out.println("SOLVED : true");
				toSolveGrid.writeGridFile(fileName);
				return 0;
			}
			
			if (solveAlea(toSolveGrid) == 0) {
				return 0;
			}
			return 1;
			
		}
		System.out.println("SOLVED : true");
		toSolveGrid.writeGridFile(fileName);
		return 0;
	}
	
	public static int solveWaiting(ArrayList<Piece> waiting, Grid toSolveGrid) {		
		while (waiting.size() != 0) {
			Piece cur = waiting.get(0);
			
			
			ArrayList<Orientation> possibleOrientations = new ArrayList<>(toSolveGrid.oriTotallyConnectedToFixed(cur));
		
			
			if (possibleOrientations.size() == 0)
				return 1;
			
			if (possibleOrientations.size() == 1) {
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
				
				ArrayList<Orientation> possibleOrientations = new ArrayList<>(toSolveGrid.oriTotallyConnectedToFixed(cur));	
				
				if (cur.isFixed() || possibleOrientations.size() == 1) {
					if (!cur.isFixed()) {
						cur.setOrientation(possibleOrientations.get(0).getValue());
						cur.setFixed(true);
					}
					
					waiting.remove(cur);
					
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
		
		for (Orientation ori : toSolveGrid.oriTotallyConnectedToFixed(alea)) {
			Grid tempG = toSolveGrid.copyGridS();
			Piece tempP = new Piece(tempG.getPiece(alea.getPosY(), alea.getPosX()));
			tempP.setFixed(true);
			tempP.setOrientation(ori.getValue());
			tempG.setPiece(alea.getPosY(), alea.getPosX(), tempP);
			
			ArrayList<Piece> waiting = new ArrayList<Piece>();
			
			Piece tp = tempG.topNeighbor(tempP);
			Piece rp = tempG.rightNeighbor(tempP);
			Piece bp = tempG.bottomNeighbor(tempP);
			Piece lp = tempG.leftNeighbor(tempP);
			
			if (tp != null && !tp.isFixed() && !waiting.contains(tp))
				waiting.add(tp);
			
			if (rp != null && !rp.isFixed() && !waiting.contains(rp))
				waiting.add(rp);
			
			if (bp != null && !bp.isFixed() && !waiting.contains(bp))
				waiting.add(bp);
				
			if (lp != null && !lp.isFixed() && !waiting.contains(lp))
				waiting.add(lp);
			
			Grid tempG1 = tempG.copyGridS();
			if (solveRec(waiting, tempG1) == 0) {
				return 0;
			}
			tempP.setFixed(false);
		}
		return 1;
	}
}