package fr.dauphine.JavaAvance.Solve;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

public class SolverTest {
	@Test
	public void testSolv() throws FileNotFoundException {
		Grid g = new Grid(50, 50);
		Generator.generateLevel("test1.txt", g);
		g = new Grid("test1.txt");
		Solver.solve("test2.txt", g);
		
		g = new Grid("test2.txt");
		assertEquals(true, Checker.isSolution(g));
	}
	
	@Test
	public void testPasSolv() throws FileNotFoundException {
		Grid g = new Grid(5, 5);
		Generator.setGrid(g);
		Generator.generateCC();
		g.setPiece(0,0, new Piece(0,0 , PieceType.BAR, PieceType.BAR.getListOfPossibleOri().get(0)));
		
		Solver.solve("test3.txt", g);
	
		assertEquals(false, Checker.isSolution(g));
	}

}
