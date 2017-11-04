package sp3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.JPanel;

import sp3.Maze.Position;


@SuppressWarnings("serial")
public class MazePanel extends JPanel {
	private Maze m;
	private List<Position> path;
	private static final int SQUARESIZE = 20;
	
	public MazePanel(Maze m, List<Position> path) {
		super();
		this.m = m;
		this.path = path;
		setBackground(Color.GRAY);
	}
	
	/*
	 * Paints the maze, m.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		int dim = m.getDimension();
		
		// Create an image to draw the maze on
		// (Helps avoid flickering)
		Image im = createImage(SQUARESIZE*dim-2,SQUARESIZE*dim-2);
		Graphics g = im.getGraphics();
		
		// Draw a background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, SQUARESIZE*dim-2, SQUARESIZE*dim-2);
		
		// Draw the maze borders
		g.setColor(Color.GRAY);
		for (int i = 0; i < dim - 1; i++) {
			for (int j = 0; j < dim - 1; j++) {
				Position p = new Position(i,j);
				Position pRight = new Position(i+1,j);
				Position pDown = new Position(i,j+1);
				if (!m.getNeighboringSpaces(p).contains(pRight)) {
					g.fillRect(SQUARESIZE*(i+1)-1, SQUARESIZE*j-1, 2, SQUARESIZE+2);
				}

				if (!m.getNeighboringSpaces(p).contains(pDown)) {
					g.fillRect(SQUARESIZE*i-1, SQUARESIZE*(j+1)-1, SQUARESIZE+2, 2);
				}
			}
			Position borderP1 = new Position(i,dim-1);
			Position borderP1Right = new Position(i+1,dim-1);
			if (!m.getNeighboringSpaces(borderP1).contains(borderP1Right)) {
				g.fillRect(SQUARESIZE*(i+1)-1, SQUARESIZE*(dim-1)+1, 2, SQUARESIZE+2);
			}
			Position borderP2 = new Position(dim-1,i);
			Position borderP2Down = new Position(dim-1,i+1);
			if (!m.getNeighboringSpaces(borderP2).contains(borderP2Down)) {
				g.fillRect(SQUARESIZE*(dim-1)+1, SQUARESIZE*(i+1)-1, SQUARESIZE+2, 2);
			}
		}
		
		// Draw the path if it has been set
		g.setColor(new Color(240, 156, 9));
		if(path != null) {
			int[] xPoints = new int[path.size()];
			int[] yPoints = new int[path.size()];
			
			for(int i = 0; i < path.size(); i++) {
				Position p = path.get(i);
				xPoints[i] = SQUARESIZE*p.x + SQUARESIZE/2;
				yPoints[i] = SQUARESIZE*p.y + SQUARESIZE/2;
			}
			
			g.drawPolyline(xPoints, yPoints, path.size());
		}
		
		// Draw the maze start and exit squares
		g.setColor(Color.RED);
		g.fillRect(SQUARESIZE/4, SQUARESIZE/4, SQUARESIZE/2, SQUARESIZE/2);
		g.setColor(Color.BLUE);
		g.fillRect(SQUARESIZE*dim-SQUARESIZE*3/4, SQUARESIZE*dim - SQUARESIZE*3/4, SQUARESIZE/2, SQUARESIZE/2);
		
		// Copy the maze to the swing window
		graphics.drawImage(im, 5, 5, getWidth()-5, getHeight()-5, 0, 0, SQUARESIZE*dim - 2, SQUARESIZE*dim -2, null);
	}
}
