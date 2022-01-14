package fr.dauphine.JavaAvance.Components;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * Type of the piece enum
 * 
 */
public enum PieceType {
	// Each Type has a number of connectors and a specific value

	VOID(0, 0) {
		@Override
		public Orientation getOrientation(Orientation o) {
			return Orientation.NORTH;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			return new LinkedList<Orientation>();
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			return ori;
		}
	},
	
	ONECONN(1, 1) {
		@Override
		public Orientation getOrientation(Orientation o) {
			return o;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			LinkedList<Orientation> conns = new LinkedList<Orientation>();
			conns.add(o);
			return conns;
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			ori.add(Orientation.EAST);
			ori.add(Orientation.SOUTH);
			ori.add(Orientation.WEST);
			return ori;
		}
	},
	
	BAR(2, 2) {
		@Override
		public Orientation getOrientation(Orientation o) {
			if (o == Orientation.NORTH || o == Orientation.SOUTH) {
				return Orientation.NORTH;
			}
			return Orientation.EAST;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			LinkedList<Orientation> conns = new LinkedList<Orientation>();
			conns.add(o);
			conns.add(o.turn90().turn90());
			return conns;
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			ori.add(Orientation.EAST);
			return ori;
		}
	},
	
	TTYPE(3, 3) {
		@Override
		public Orientation getOrientation(Orientation o) {
			return o;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			LinkedList<Orientation> conns = new LinkedList<Orientation>();
			conns.add(o);
			conns.add(o.turn90());
			conns.add(o.turn90().turn90().turn90());
			return conns;
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			ori.add(Orientation.EAST);
			ori.add(Orientation.SOUTH);
			ori.add(Orientation.WEST);
			return ori;
		}
	},
	
	FOURCONN(4, 4) {
		@Override
		public Orientation getOrientation(Orientation o) {
			return Orientation.NORTH;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			LinkedList<Orientation> conns = new LinkedList<Orientation>();
			conns.add(Orientation.NORTH);
			conns.add(Orientation.SOUTH);
			conns.add(Orientation.EAST);
			conns.add(Orientation.WEST);
			return conns;
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			return ori;
		}
	},
	
	LTYPE(2, 5) {
		@Override
		public Orientation getOrientation(Orientation o) {
			return o;
		}
		
		@Override
		public LinkedList<Orientation> setConnectorsList(Orientation o) {
			LinkedList<Orientation> conns = new LinkedList<Orientation>();
			conns.add(o);
			conns.add(o.turn90());
			return conns;
		}
		
		@Override
		public ArrayList<Orientation> getListOfPossibleOri() {
			ArrayList<Orientation> ori = new ArrayList<Orientation>();
			ori.add(Orientation.NORTH);
			ori.add(Orientation.EAST);
			ori.add(Orientation.SOUTH);
			ori.add(Orientation.WEST);
			return ori;
		}
	};
		
	private final int nbConnectors;
	private final int value;
	
	private PieceType(int conn, int val) {
		this.nbConnectors = conn;
		this.value = val;
	}
	
	abstract public Orientation getOrientation(Orientation o);
	abstract public LinkedList<Orientation> setConnectorsList(Orientation o);
	abstract public ArrayList<Orientation> getListOfPossibleOri();
	
	public static PieceType getTypefromValue(int val) {
		switch(val) {
		case 0:
			return VOID;
		case 1:
			return ONECONN;
		case 2:
			return BAR;
		case 3:
			return TTYPE;
		case 4:
			return FOURCONN;
		case 5:
			return LTYPE;
		default:
			throw(new IllegalArgumentException("The value " + val + " isn't associated to any piece type !"));
		}
	}
	
	public int getNbConnectors() {
		return nbConnectors;
	}

	public int getValue() {
		return value;
	}
	
	
}
