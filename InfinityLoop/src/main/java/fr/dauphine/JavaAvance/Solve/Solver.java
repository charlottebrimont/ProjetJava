package fr.dauphine.JavaAvance.Solve;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

public class Solver {
	private static String fileName;

	public static void main(String[] args) {
		
		// To be implemented
		try {
			
			//Grid save = new Grid("save.txt");
			//save.writeGridFile("save.txt");
			//System.out.println(Checker.isSolution(save));
			
			Grid g = new Grid(25, 25);
			
			//Generator.generateLevel("test1.txt", g);
			
			g = new Grid("test1.txt");
			
			System.out.println(g);
		
			
			solve("test2.txt", g);
			g = new Grid("test2.txt");
			//solve(g);

			//System.out.println(initWaiting(g));
			System.out.println(g);
			System.out.println(Checker.isSolution(g));
		
		} catch (FileNotFoundException e) {
			System.err.println("Erreur : " + e);
		}
		
	//}
	}
	
	public static void solve(String fileName, Grid toSolveGrid) {
		if (!Checker.isSolution(toSolveGrid)) {
			if (solveWaiting(toSolveGrid) == 1) {
				System.out.println("SOLVED : false (1)");		//pas sur de l'endroit ou afficher ca... (je veux dire sur le terminal)
				return;
			}
			
			while (!Checker.isSolution(toSolveGrid)) {
				if (solveAlea(toSolveGrid) == 1) {
					System.out.println("SOLVED : false (2)");		//pas sur de l'endroit ou afficher ca... (je veux dire sur le terminal)
					return;
				}
			}
		}
		
		System.out.println("SOLVED : true");		//pas sur de l'endroit ou afficher ca... (je veux le dire sur le terminal)
		toSolveGrid.writeGridFile(fileName);
	}
	
	/*
	public static void solve(String fileName, Grid toSolveGrid) {
		Solver.fileName = fileName;
		solveRec(toSolveGrid);
	}
	*/
	
	public static int solveWaiting(ArrayList<Piece> waiting, Grid toSolveGrid) {		
		while (waiting.size() != 0) {
			Piece cur = waiting.get(0);
			waiting.remove(cur);
			
			ArrayList<Orientation> possibleOrientations = new ArrayList<>(toSolveGrid.oriTotallyConnectedToFixedGenerator(cur));
			
			//cur.setPossibleOrientations(possibleOrientations); 
			
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
		}
		return 0;
	}

	public static ArrayList<Piece> initWaiting(Grid toSolveGrid) {
		ArrayList<Piece> waiting = new ArrayList<Piece>();
		
		for (int i = 0; i < toSolveGrid.getHeight(); i++) {
			for (int j = 0; j < toSolveGrid.getWidth(); j++) {
				Piece cur = toSolveGrid.getPiece(i, j);
				
				ArrayList<Orientation> possibleOrientations = new ArrayList<>(toSolveGrid.oriTotallyConnectedToFixedGenerator(cur));
				//cur.setPossibleOrientations(possibleOrientations);	
				
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
	/*
	public static void solveRec (Grid toSolveGrid) {
		if (!Checker.isSolution(toSolveGrid)) {
			if (solveWaiting(toSolveGrid) == 1) {
				System.out.println("SOLVED : false (1.1)");
				return;
			}
			if (Checker.isSolution(toSolveGrid)) {
				System.out.println("SOLVED : true");		//pas sur de l'endroit ou afficher ca... (je veux le dire sur le terminal)
				toSolveGrid.writeGridFile(fileName);
				return;
			}
			solveAlea(toSolveGrid);
			return;
			
		}
		
		System.out.println("SOLVED : true");		//pas sur de l'endroit ou afficher ca... (je veux le dire sur le terminal)
		toSolveGrid.writeGridFile(fileName);
	}
	

	public static void solveAlea(Grid toSolveGrid) {
		Piece alea = toSolveGrid.getPiece(0, 0);
		while (alea != null && alea.isFixed()) {
			alea = toSolveGrid.getNextPiece(alea);
		}
		if (alea == null) {
			System.out.println("SOLVED : false, piece null");
			return;
		}
		System.out.println(toSolveGrid.oriTotallyConnectedToFixedGenerator(alea));
		System.out.println(alea);
		for (Orientation ori : toSolveGrid.oriTotallyConnectedToFixedGenerator(alea)) {	
			System.out.println("test");
			Grid tempG = toSolveGrid.copyGridS();
			tempG.getPiece(alea.getPosY(), alea.getPosX()).setFixed(true);
			tempG.getPiece(alea.getPosY(), alea.getPosX()).setOrientation(ori.getValue());
			System.out.println(tempG.getPiece(alea.getPosY(), alea.getPosX()));
			solveRec(tempG);
			
		}
		
	}
	*/
	
/*public static Grid solveAlea(Grid toSolveGrid) {
		
		ArrayList<Grid> wait = new ArrayList<>();
		Grid tempG = toSolveGrid;
		wait.add(tempG);
		while (!Checker.isSolution(tempG)) {
			System.out.println(wait.size());
			if(wait.size() == 0) {
				System.out.println("problem 1: ");
				return null;
			}
			tempG = wait.get(wait.size()-1);
			wait.remove(wait.size()-1);
			
			while (alea != null && alea.isFixed()) {
				alea = tempG.getNextPiece(alea);
			}
			if (alea == null) {
				System.out.println("problem 2: ");
				return null;
				
			}
			ArrayList<Orientation> possibleOrientations = new ArrayList<>(tempG.oriTotallyConnectedToFixedGenerator(alea));
			//alea.setPossibleOrientations(possibleOrientations);
			for (Orientation ori : possibleOrientations) {	
				Grid tempG1 = tempG.copyGridS();
                //PieceType type = alea.getType();
                //Piece tempP = new Piece(alea.getPosY(),alea.getPosX(),type,ori);
                //tempP.setPossibleOrientations(new ArrayList<>(alea.getPossibleOrientations()));
				tempG1.getAllPieces()[alea.getPosY()][alea.getPosX()].setFixed(true);
				tempG1.getAllPieces()[alea.getPosY()][alea.getPosX()].setOrientation(ori.getValue());
				//tempG.setPiece(tempP.getPosY(), tempP.getPosX(), tempP);
				if (solveWaiting(tempG1) == 1) {
				System.out.println("ici");
				}else {
					wait.add(tempG1);
				}
			}
		}
		return tempG;
	}*/
	
	/*
	public static int solveAlea(Grid toSolveGrid) {
		
		ArrayList<Grid> wait = new ArrayList<>();
		Grid tempG = toSolveGrid;
		wait.add(tempG);
		while (!Checker.isSolution(tempG)) {
			tempG = wait.get(wait.size()-1);
			wait.remove(wait.size()-1);
			Piece alea = tempG.getPiece(0, 0);
			while (alea != null && alea.isFixed()) {
				alea = tempG.getNextPiece(alea);
			}
			if (alea == null) {
				return 1;
			}
			ArrayList<Orientation> possibleOrientations = toSolveGrid.oriTotallyConnectedToFixedSolver(alea);
			alea.setPossibleOrientations(possibleOrientations);
			for (Orientation ori : possibleOrientations) {				
				tempG = new Grid(tempG);
				Piece tempP = new Piece(alea);
				
				tempP.setOrientation(ori.getValue());
				tempP.setFixed(true);
				tempG.setPiece(tempP.getPosY(), tempP.getPosX(), tempP);
				if (solveWaiting(tempG) == 1) {
				;
				}else {
					wait.add(tempG);
				}
			}
		}
		toSolveGrid = new Grid(tempG);
		return 0;
	}
	*/
	
/*
	public static int solveAlea(Grid toSolveGrid) {
		Piece alea = toSolveGrid.getPiece(0, 0);
		while (alea != null && alea.isFixed()) {
			alea = toSolveGrid.getNextPiece(alea);
		}
		
		if (alea == null) {
			return 1;
		}
		
		Grid tempG = new Grid(toSolveGrid);
		
		while (!Checker.isSolution(tempG)) {
			ArrayList<Orientation> possibleOrientations = toSolveGrid.oriTotallyConnectedToFixedSolver(alea);
			alea.setPossibleOrientations(possibleOrientations);
			
			for (Orientation ori : possibleOrientations) {				
				tempG = new Grid(toSolveGrid);
				Piece tempP = new Piece(alea);
				
				tempP.setOrientation(ori.getValue());
				tempP.setFixed(true);
				tempG.setPiece(tempP.getPosY(), tempP.getPosX(), tempP);
				
				if (solveWaiting(tempG) == 1)
					return 1;
				
				else
					solveAlea(tempG);
			}
			
			do {
				alea = toSolveGrid.getNextPiece(alea);
				if (alea == null || alea.getPossibleOrientations().size() == 0) {
					System.out.println("ICI");
					return 1;
				}
			}
			while (alea.isFixed());
			

		}
		
		toSolveGrid.copyGrid(tempG);
		return 0;
	}
	*/
}