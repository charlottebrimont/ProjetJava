package fr.dauphine.JavaAvance.Components;

import java.util.HashMap;

/**
 * 
 * Orientation of the piece enum
 * 
 */
public enum Orientation {
	/* Implement all the possible orientations and 
	 *  required methods to rotate
	 */

	NORTH(0) {
		@Override
		public Orientation turn90() {
			return EAST;
		}
		
		@Override
		public int[] getOpposedPieceCoordinates(Piece p) {
			int[] opp = new int[2];
			opp[0] = p.getPosY() - 1; 
			opp[1] = p.getPosX();
			return opp;
		}
	},
	
	EAST(1) {
		@Override
		public Orientation turn90() {
			return SOUTH;
		}
		
		@Override
		public int[] getOpposedPieceCoordinates(Piece p) {
			int[] opp = new int[2];
			opp[0] = p.getPosY();
			opp[1] = p.getPosX() + 1;
			return opp;
		}
	},
	
	SOUTH(2) {
		@Override
		public Orientation turn90() {
			return WEST;
		}
		
		@Override
		public int[] getOpposedPieceCoordinates(Piece p) {
			int[] opp = new int[2];
			opp[0] = p.getPosY() + 1;
			opp[1] = p.getPosX();
			return opp;
		}
	},
	
	WEST(3) {
		@Override
		public Orientation turn90() {
			return NORTH;
		}
		
		@Override
		public int[] getOpposedPieceCoordinates(Piece p) {
			int[] opp = new int[2];
			opp[0] = p.getPosY();
			opp[1] = p.getPosX() - 1;
			return opp;
		}
	};
	
	private int value;
	
	private Orientation(int val) {
		this.value = val;
	}
	
	abstract public Orientation turn90();
	abstract public int[] getOpposedPieceCoordinates(Piece p);
	
	public static Orientation getOrifromValue(int val) {
		switch(val) {
		case 0:
			return NORTH;
		case 1:
			return EAST;
		case 2:
			return SOUTH;
		case 3:
			return WEST;
		default:
			throw(new IllegalArgumentException("The value " + val + " isn't associated to any orientation !"));
		}
	}
	
	public Orientation getOpposedOrientation() {
		return this.turn90().turn90();
	}
	
	public int getValue() {
		return value;
	}
}
