package fr.dauphine.JavaAvance.Solve;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.dauphine.JavaAvance.GUI.Grid;

class GeneratorTest {

	@Test
	public void generateRandomIsResolvable() {
		Grid g = new Grid(3, 3);
		Generator.setGrid(g);
		Generator.generateRandomLevel();
		
		assertEquals(Checker.isSolution(Generator.getGrid()), true);
	}
	
	@Test
	void generateCCIsResolvable() {
		Grid g = new Grid(3, 3);
		Generator.setGrid(g);
		Generator.generateCC();
		
		assertEquals(Checker.isSolution(Generator.getGrid()), true);
	}

}
