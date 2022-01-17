package fr.dauphine.JavaAvance.Solve;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;
import fr.dauphine.JavaAvance.GUI.Grid;

class GeneratorTest {

	@Test
	void generateRandomIsResolvable() {
		Grid g = new Grid(3, 3);
		Generator.setGrid(g);
		Generator.generateRandomLevel();
		
		assertEquals(true, Checker.isSolution(Generator.getGrid()));
	}
	
	@Test
	void generateCCIsResolvable() {
		Grid g = new Grid(3, 3);
		Generator.setGrid(g);
		Generator.generateCC();
		
		assertEquals(true, Checker.isSolution(Generator.getGrid()));
	}

	@Test
	void generatePieceTTYPEWEST() {
		Grid g = new Grid(3, 3);
		Piece p1 = new Piece(0, 1, PieceType.ONECONN, Orientation.SOUTH);
		Piece p2 = new Piece(1, 0, PieceType.ONECONN, Orientation.EAST);
		Piece p3 = new Piece(1, 2, PieceType.VOID, Orientation.NORTH);
		Piece p4 = new Piece(2, 1, PieceType.ONECONN, Orientation.NORTH);
		
		p1.setFixed(true);
		p2.setFixed(true);
		p3.setFixed(true);
		p4.setFixed(true);
		
		g.setPiece(0, 1, p1);
		g.setPiece(1, 0, p2);
		g.setPiece(1, 2, p3);
		g.setPiece(2, 1, p4);
		
		Generator.setGrid(g);
		Generator.generatePiece(Generator.getGrid().getPiece(1, 1));
		
		assertEquals(PieceType.TTYPE, Generator.getGrid().getPiece(1, 1).getType());
		assertEquals(Orientation.WEST, Generator.getGrid().getPiece(1, 1).getOrientation());
	}
	
	@Test
	void possiblesOrientationsAccordingToFixedLTYPE() {
		Grid g = new Grid(3, 3);
		Piece p1 = new Piece(0, 1, PieceType.ONECONN, Orientation.SOUTH);
		p1.setFixed(true);
		g.setPiece(0, 1, p1);
		
		Piece p = new Piece(1, 1);
		p.setType(PieceType.LTYPE);
		
		ArrayList<Orientation> res = new ArrayList<Orientation>();
		res.add(Orientation.NORTH);
		res.add(Orientation.WEST);
		
		assertEquals(res, g.oriTotallyConnectedToFixed(p));
	}
	
	@Test
	void possiblesOrientationsAccordingToFixedBAR() {
		Grid g = new Grid(3, 3);
		Piece p1 = new Piece(0, 1, PieceType.ONECONN, Orientation.SOUTH);
		p1.setFixed(true);
		g.setPiece(0, 1, p1);
		
		Piece p = new Piece(1, 1);
		p.setType(PieceType.BAR);
		
		ArrayList<Orientation> res = new ArrayList<Orientation>();
		res.add(Orientation.NORTH);
		
		assertEquals(res, g.oriTotallyConnectedToFixed(p));
	}
	
	@Test
	void possiblesOrientationsAccordingToFixedFOURCONN() {
		Grid g = new Grid(3, 3);
		Piece p1 = new Piece(0, 1, PieceType.ONECONN, Orientation.SOUTH);
		p1.setFixed(true);
		g.setPiece(0, 1, p1);
		
		Piece p = new Piece(1, 1);
		p.setType(PieceType.FOURCONN);
		
		ArrayList<Orientation> res = new ArrayList<Orientation>();
		res.add(Orientation.NORTH);
		
		assertEquals(res, g.oriTotallyConnectedToFixed(p));
	}
	
	@Test
	void possiblesOrientationsAccordingToFixedONECONN() {
		Grid g = new Grid(3, 3);
		Piece p1 = new Piece(0, 1, PieceType.TTYPE, Orientation.SOUTH);
		p1.setFixed(true);
		g.setPiece(0, 1, p1);
		
		Piece p = new Piece(1, 1);
		p.setType(PieceType.ONECONN);
		
		ArrayList<Orientation> res = new ArrayList<Orientation>();
		res.add(Orientation.NORTH);
		
		assertEquals(res, g.oriTotallyConnectedToFixed(p));
	}
}
