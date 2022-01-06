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
	},
	EAST(1) {
		@Override
		public Orientation turn90() {
			return SOUTH;
		}
	},
	SOUTH(2) {
		@Override
		public Orientation turn90() {
			return WEST;
		}
	},
	WEST(3) {
		@Override
		public Orientation turn90() {
			return NORTH;
		}
	};
	
	private int value;
	
	private Orientation(int val) {
		this.value = val;
	}
	
	abstract public Orientation turn90();
	
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
}
