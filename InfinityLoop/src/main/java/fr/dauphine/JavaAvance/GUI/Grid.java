package fr.dauphine.JavaAvance.GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import fr.dauphine.JavaAvance.Components.Orientation;
import fr.dauphine.JavaAvance.Components.Piece;
import fr.dauphine.JavaAvance.Components.PieceType;

/**
 * Grid handler and peces'functions which depends of the grid
 * 
 *
 */
public class Grid {
	private int width; // j
	private int height; // i
	private int nbcc = -1;
	private Piece[][] pieces;
	
	
	/**
	 * Constructor to read the grid from a file
	 * @param str
	 * @throws FileNotFoundException
	 */
	public Grid(String str) {
		try {
			File file = new File(str);
			Scanner sc;
			sc = new Scanner(file);
			if (sc.hasNext()) {
				String w = sc.next();
				width = Integer.parseInt(w);
			}
			if (sc.hasNext()) {
				 String h = sc.next();
				 height = Integer.parseInt(h);
			}
			
		    pieces = new Piece[height][width];
		    int i = 0;
		    int j = 0;
	
		    while (sc.hasNextLine()) {
		    	String ligne = sc.next();
			   	String[] ligneL = ligne.split("x");
			   	int pieceType = Integer.parseInt(ligneL[0]);
			   	int pieceOri = Integer.parseInt(ligneL[1]);
			   	Piece p = new Piece(i, j, pieceType, pieceOri);
			   	this.setPiece(i, j, p);
			   	if (j<width) {
			   		j++;
			   	}
			   	if (j == width) {
			   		i++;
			   		j = 0;
			   	}
		    }
		    
		    sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erreur : " + e);
		}
	    

	}
	
	/**
	 * Constructor that duplicates the grid passed as parameter 
	 * @param g
	 */
	public Grid (Grid g) {
		this.height = g.getHeight();
		this.width = g.getWidth();
		this.nbcc = g.getNbcc();
		pieces = new Piece[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.setPiece(i, j, g.getPiece(i, j));
			}
		}
	}

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		initPieces();
	}
	
	
	/** 
	 * Constructor with specified number of connected component
	 * 
	 * @param width
	 * @param height
	 * @param nbcc
	 */
	public Grid(int width, int height, int nbcc) {
		this.width = width;
		this.height = height;
		this.nbcc = nbcc;
		initPieces();
	}
	
	public void initPieces() {
		this.pieces = new Piece[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.setPiece(i, j, new Piece(i, j));
			}
		}
	}
	
	/**
	 * Generate a grid and copy the grid passed as a parameter into this grid 
	 * 
	 * @param g
	 */
	public Grid copyGrid() {
        Grid copy = new Grid(this.getWidth(),this.getHeight());
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j=0; j < this.getWidth(); j++) {
                Piece current = this.getPiece(i, j);
                PieceType type = current.getType();
                Orientation ori = current.getOrientation();
                Piece pcopy = new Piece(i,j,type,ori);
                if (current.isFixed()) pcopy.setFixed(true);
                copy.setPiece(i,j,pcopy);
            }
        }

        return copy;
    }

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Integer getNbcc() {
		return nbcc;
	}

	public void setNbcc(int nbcc) {
		this.nbcc = nbcc;
	}

	public Piece getPiece(int line, int column) {
		return this.pieces[line][column];
	}

	public void setPiece(int line, int column, Piece piece) {
		this.pieces[line][column] = piece;
	}

	public Piece[][] getAllPieces() {
		return pieces;
	}

	/**
	 * Check if a case is a corner
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a corner
	 */
	public boolean isCorner(int line, int column) {
		if (line == 0) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else if (line == this.getHeight() - 1) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Check if a case is member of the first or the last line
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a corner
	 */
	public boolean isBorderLine(int line, int column) {
		if (line == 0 && column > 0 && column < this.getWidth() - 1) {
			return true;
		} else if (line == this.getHeight() - 1 && column > 0 && column < this.getWidth() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * Check if a case is member of the first or the last column
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a corner
	 */
	public boolean isBorderColumn(int line, int column) {
		if (column == 0 && line > 0 && line < this.getHeight() - 1) {
			return true;
		} else if (column == this.getWidth() - 1 && line > 0 && line < this.getHeight() - 1) {
			return true;
		}
		return false;

	}

	/**
	 * Check if a piece has a neighbour for its connectors for one orientation
	 * 
	 * @param p
	 *            piece
	 * @return true if there is a neighbour for all connectors
	 */
	public boolean hasNeighbour(Piece p) {
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				if (this.getPiece(oppPieceY, oppPieceX).getType() == PieceType.VOID) {
					return false;
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Check if a piece has a fixed neighbor for each one of its connecotrs
	 * 
	 * @param p
	 *            the piece
	 * @return true if there is a fixed piece for each connector
	 */
	public boolean hasFixedNeighbour(Piece p) {
		boolean bool = false;
		for (Orientation ori : p.getConnectors()) {
			bool = false;
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.getType() == PieceType.VOID || !neigh.isFixed()) {
					return false;
				}
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (ori == oriOppPiece.getOpposedOrientation()) {
							bool = true;
						}
					}
					if (!bool) {
						return false;
					}

				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return bool;
	}

	/**
	 * Check if a piece has a at least one fixed neighbor
	 * 
	 * @param p
	 *            the piece
	 * @return true if there is a fixed piece for each connector
	 */
	public boolean hasAtLeast1FixedNeighbour(Piece p) {
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (ori == oriOppPiece.getOpposedOrientation()) {
							return true;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * list of neighbors
	 * 
	 * @param p
	 *            the piece
	 * @return the list of neighbors
	 */
	public ArrayList<Piece> listOfNeighbours(Piece p) {
		ArrayList<Piece> lp = new ArrayList<Piece>();
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j

			if (oppPieceY >= 0 && oppPieceY < this.getHeight() && oppPieceX >= 0 && oppPieceX < this.width) {
				if (this.getPiece(oppPieceY, oppPieceX).getType() != PieceType.VOID) {
					lp.add(this.getPiece(oppPieceY, oppPieceX));
				}
			}

		}
		return lp;
	}

	/**
	 * this function returns the number of neighbors
	 * 
	 * @param p
	 * @return the number of neighbors
	 */
	public int numberOfNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;
		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID)
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID)
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID)
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID)
			count++;
		return count;
	}

	/**
	 * this function returns the number of pieces that are fixed around p
	 * 
	 * @param p
	 * @returns the number of pieces that are fixed around p
	 */
	public int nbFixedPiecesAround(Piece p) {
		int nb = 0;
		
		if (topPiece(p) == null || topPiece(p).isFixed()) {
			nb++;
		}
		if (rightPiece(p) == null || rightPiece(p).isFixed()) {
			nb++;
		}
		if (bottomPiece(p) == null || bottomPiece(p).isFixed()) {
			nb++;
		}
		if (leftPiece(p) == null || leftPiece(p).isFixed()) {
			nb++;
		}
		
		return nb;
	}
	
	/**
	 * this function returns the list of orientation where there is a connector of a fixed piece
	 * 
	 * @param p
	 * @returns the list of orientation with a fixed connector endnig in p
	 */
	public ArrayList<Orientation> listFixedConnsAround(Piece p) {
		ArrayList<Orientation> l = new ArrayList<Orientation>();

		if (topPiece(p) != null && topPiece(p).isFixed() && topPiece(p).hasBottomConnector()) {
			l.add(Orientation.NORTH);
		}
		if (rightPiece(p) != null && rightPiece(p).isFixed() && rightPiece(p).hasLeftConnector()) {
			l.add(Orientation.EAST);
		}
		if (bottomPiece(p) != null && bottomPiece(p).isFixed() && bottomPiece(p).hasTopConnector()) {
			l.add(Orientation.SOUTH);
		}
		if (leftPiece(p) != null && leftPiece(p).isFixed() && leftPiece(p).hasRightConnector()) {
			l.add(Orientation.WEST);
		}
		
		return l;
	}
	
	/**
	 * this function returns the number of fixed neighbors
	 * 
	 * @param p
	 * @return the number of neighbors
	 */
	public int numberOfFixedNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;

		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID && getPiece(Y + 1, X).isFixed())
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID && getPiece(Y, X + 1).isFixed())
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID && getPiece(Y - 1, X).isFixed())
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID && getPiece(Y, X - 1).isFixed())
			count++;
		return count;
	}

	/**
	 * Check if all pieces have neighbors even if we don't know the orientation
	 * 
	 * @param p
	 * @return false if a piece has no neighbor
	 */
	public boolean allPieceHaveNeighbour() {

		for (Piece[] ligne : this.getAllPieces()) {
			for (Piece p : ligne) {

				if (p.getType() != PieceType.VOID) {
					if (p.getType().getNbConnectors() > numberOfNeibours(p)) {
						return false;
					}
				}

			}
		}
		return true;

	}

	/**
	 * Return the next piece of the current piece
	 * 
	 * @param p
	 *            the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPiece(Piece p) {
		int i = p.getPosY();
		int j = p.getPosX();
		if (j < this.getWidth() - 1) {
			p = this.getPiece(i, j + 1);
		} else {
			if (i < this.getHeight() - 1) {
				p = this.getPiece(i + 1, 0);
			} else {
				return null;
			}

		}
		return p;
	}
	
	/**
	 * Return the next piece of the current piece right2left and bottom2top
	 * 
	 * @param p
	 *            the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPieceInv(Piece p) {

		int i = p.getPosY();
		int j = p.getPosX();
		if (j > 0) {
			p = this.getPiece(i, j - 1);
		} else {
			if (i > 0) {
				p = this.getPiece(i - 1, this.getWidth()-1);
			} else {
				return null;
			}

		}

		return p;

	}

	/**
	 * Check if a piece is connected
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public boolean isConnected(Piece p, Orientation ori) {
		int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
		int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
		if (p.getType() == PieceType.VOID)
			return true;
		try {
			for (Orientation oppConnector : this.getPiece(oppPieceY, oppPieceX).getConnectors()) {
				
				if (oppConnector == ori.getOpposedOrientation()) {
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	/**
	 * Check if a piece is totally connected
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public boolean isTotallyConnected(Piece p) {
		if (p.getType() != PieceType.VOID) {
			for (Orientation connector : p.getConnectors()) {
				if (!this.isConnected(p, connector)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if a piece is connected to the fixed pieces around
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected to a fixed pieces around
	 */
	public boolean isOriConnectedToFixed(Piece p, Orientation ori) {
		p.setOrientation(ori.getValue());
		
		Piece tp = this.topPiece(p);
		Piece rp = this.rightPiece(p);
		Piece bp = this.bottomPiece(p);
		Piece lp = this.leftPiece(p);
		
		if (tp == null) {
			if (p.getConnectors().contains(Orientation.NORTH))
				return false;
		}
		else {
			if (tp.isFixed() && p.getConnectors().contains(Orientation.NORTH) != tp.getConnectors().contains(Orientation.SOUTH))
				return false;
		}
		
		if (rp == null) {
			if (p.getConnectors().contains(Orientation.EAST))
				return false;
		}
		else {
			if (rp.isFixed() && p.getConnectors().contains(Orientation.EAST) != rp.getConnectors().contains(Orientation.WEST))
				return false;
		}
		
		if (bp == null) {
			if (p.getConnectors().contains(Orientation.SOUTH))
				return false;
		}
		else {
			if (bp.isFixed() && p.getConnectors().contains(Orientation.SOUTH) != bp.getConnectors().contains(Orientation.NORTH))
				return false;
		}
			
		if (lp == null) {
			if (p.getConnectors().contains(Orientation.WEST))
				return false;
		}
		else {
			if (lp.isFixed() && p.getConnectors().contains(Orientation.WEST) != lp.getConnectors().contains(Orientation.EAST))
				return false;
		}

		return true;
	}
	
	/**
	 * Check if a piece is totally connected to the fixed pieces around
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public ArrayList<Orientation> oriTotallyConnectedToFixed(Piece p) {
		Orientation initOri = p.getOrientation();
		ArrayList<Orientation> oris = new ArrayList<Orientation>();
		
		if (p.getType() == PieceType.VOID || p.getType() == PieceType.FOURCONN) {
			if (isOriConnectedToFixed(p, Orientation.NORTH)) {
				oris.add(Orientation.NORTH);
			}
			p.setOrientation(initOri.getValue());
			return oris;
		}
		
		if (p.getType() == PieceType.BAR) {
			if (isOriConnectedToFixed(p, Orientation.NORTH)) {
				oris.add(Orientation.NORTH);
			}
			if (isOriConnectedToFixed(p, Orientation.EAST)) {
				oris.add(Orientation.EAST);
			}
			p.setOrientation(initOri.getValue());
			return oris;
		}
		
		for (int i = 0; i < 4; i++) {
			Orientation ori = Orientation.getOrifromValue(i);
			if (isOriConnectedToFixed(p, ori)) {
				oris.add(ori);
			}
		}	
		
		p.setOrientation(initOri.getValue());
		return oris;
	}
	
	/**
	 * Check if a piece position is valid
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public boolean isValidOrientation(int line, int column) {

		Piece tn = this.topNeighbor(this.getPiece(line, column));
		Piece ln = this.leftNeighbor(this.getPiece(line, column));
		Piece rn = this.rightNeighbor(this.getPiece(line, column));
		Piece bn = this.bottomNeighbor(this.getPiece(line, column));

		if (this.getPiece(line, column).getType() != PieceType.VOID) {
			if (line == 0) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;
				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}
				if (this.getPiece(line, column).hasTopConnector())
					return false;
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector())
					return false;
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector())
					return false;

			} else if (line > 0 && line < this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;

				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}

				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector())
					return false;
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector())
					return false;

			} else if (line == this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector())
						return false;
				} else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector())
						return false;
				}
				if (this.getPiece(line, column).hasBottomConnector())
					return false;
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector())
					return false;
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector())
					return false;

			}
			if (this.getPiece(line, column).hasLeftConnector() && ln == null)
				return false;
			if (this.getPiece(line, column).hasTopConnector() && tn == null)
				return false;
			if (this.getPiece(line, column).hasRightConnector() && rn == null)
				return false;
			if (this.getPiece(line, column).hasBottomConnector() && bn == null)
				return false;
		}

		return true;
	}

	/**
	 * Find the left neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece leftNeighbor(Piece p) {

		if (p.getPosX() > 0) {
			if (this.getPiece(p.getPosY(), p.getPosX() - 1).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY(), p.getPosX() - 1);
			}
		}
		return null;
	}

	/**
	 * Find the top neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece topNeighbor(Piece p) {

		if (p.getPosY() > 0) {
			if (this.getPiece(p.getPosY() - 1, p.getPosX()).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY() - 1, p.getPosX());
			}
		}
		return null;
	}

	/**
	 * Find the right neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece rightNeighbor(Piece p) {

		if (p.getPosX() < this.getWidth() - 1) {
			if (this.getPiece(p.getPosY(), p.getPosX() + 1).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY(), p.getPosX() + 1);
			}
		}
		return null;
	}

	/**
	 * Find the bottom neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece bottomNeighbor(Piece p) {

		if (p.getPosY() < this.getHeight() - 1) {
			if (this.getPiece(p.getPosY() + 1, p.getPosX()).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY() + 1, p.getPosX());
			}
		}
		return null;
	}

	public Piece leftPiece(Piece p) {
		if (p.getPosX() > 0) {
			return this.getPiece(p.getPosY(), p.getPosX()-1);
		}
		
		return null;
	}
	
	public Piece topPiece(Piece p) {
		if (p.getPosY() > 0) {
			return this.getPiece(p.getPosY() - 1, p.getPosX());
		}
		return null;
	}
	
	public Piece rightPiece(Piece p) {
		if (p.getPosX() < this.getWidth() - 1) {
			return this.getPiece(p.getPosY(), p.getPosX() + 1);
		}
		return null;
	}
	
	public Piece bottomPiece(Piece p) {
		if (p.getPosY() < this.getHeight() - 1) {
			return this.getPiece(p.getPosY() + 1, p.getPosX());
		}
		return null;
	}
	
	@Override
	public String toString() {

		String s = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				s += DisplayUnicode.getUnicodeOfPiece(pieces[i][j].getType(), pieces[i][j].getOrientation());
			}
			s += "\n";
		}
		return s;
	}
	
	public void writeGridFile (String file) {
		try {
		      FileWriter myWriter = new FileWriter(file);
		      myWriter.write("" + width +"\n" + height);
		      for (Piece[] ligne : this.getAllPieces()) {
					for (Piece p : ligne) {
						myWriter.write("\n");
						myWriter.write(p.getType().getValue() + "x" + p.getOrientation().getValue());
					}
				}
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}