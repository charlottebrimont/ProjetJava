package fr.dauphine.JavaAvance.Solve;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

class CheckerTest {
	private static Grid g;
	
	@BeforeEach
	public void initGrid() {
		g = new Grid(3, 3);
		
		Piece p1 = new Piece(0, 1, PieceType.ONECONN, Orientation.SOUTH);
		Piece p2 = new Piece(1, 0, PieceType.ONECONN, Orientation.EAST);
		Piece p3 = new Piece(1, 2, PieceType.ONECONN, Orientation.WEST);
		Piece p4 = new Piece(2, 1, PieceType.ONECONN, Orientation.NORTH);
		
		p1.setFixed(true);
		p2.setFixed(true);
		p3.setFixed(true);
		p4.setFixed(true);
		
		g.setPiece(0, 1, p1);
		g.setPiece(1, 0, p2);
		g.setPiece(1, 2, p3);
		g.setPiece(2, 1, p4);
	}
	
	@Test
	void checkerTrue() {
		Piece p = new Piece(1, 1, PieceType.FOURCONN, Orientation.NORTH);
		p.setFixed(true);
		g.setPiece(1, 1, p);
		
		assertEquals(true, Checker.isSolution(g));
	}
	
	@Test
	void checkerFalse() {
		Piece p = new Piece(1, 1, PieceType.VOID, Orientation.NORTH);
		p.setFixed(true);
		g.setPiece(1, 1, p);
		
		assertEquals(false, Checker.isSolution(g));
	}

}
