package sample;

class Position {
	public double x;
	public double y;
}

public abstract class Scheme {
	protected Position pos;

	Scheme(Position pos){
		this.pos = pos;
	}

	public void setPosition(Position pos) { this.pos = pos; }
	public Position getPosition() { return pos; }

	public abstract void draw();
}
