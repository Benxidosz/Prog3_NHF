package ges.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;

import java.io.*;
import java.util.LinkedHashSet;

public class Graph implements Serializable, Cloneable {
	public int nodeRadius;
	private StringBuilder tmpId;
	public String title;

	private LinkedHashSet<Node> nodes;
	private LinkedHashSet<Edge> edges;

	public Graph(int nodeRadius) {
		title = "Untitled";
		this.nodeRadius = nodeRadius;
		tmpId = new StringBuilder("A");

		nodes = new LinkedHashSet<>();
		edges = new LinkedHashSet<>();
	}

	public Graph(Graph graph) {
		nodeRadius = graph.nodeRadius;
		nodes = new LinkedHashSet<Node>();
		edges = new LinkedHashSet<Edge>();
		LinkedHashSet<Node> added = new LinkedHashSet<>();
		for (Edge edge : graph.edges) {
			Node n1 = null;
			Node n2 = null;
			if (!added.contains(edge.nodes[0])) {
				n1 = new Node(this, edge.nodes[0]);
				nodes.add(n1);
				added.add(edge.nodes[0]);
			} else {
				n1 = getNode(edge.nodes[0].getId());
			}


			if (!added.contains(edge.nodes[1])) {
				n2 = new Node(this, edge.nodes[1]);
				nodes.add(n2);
				added.add(edge.nodes[1]);
			} else {
				n2 = getNode(edge.nodes[1].getId());
			}

			if (n1 != null && n2 != null)
				addEdge(n1, n2);
		}
		for (Node node : graph.nodes) {
			if (!added.contains(node)) {
				Node cpyNode = new Node(this, node);
				nodes.add(cpyNode);
				added.add(cpyNode);
			}
		}
		tmpId = new StringBuilder(graph.tmpId);
		title = graph.title;
	}

	public void setChooser(String choose) {
		for (Node node : nodes) {
			node.setChooser(choose);
		}
	}

	public boolean addNode(Position pos, Canvas canvas, String chooser) {
		boolean ret = nodes.add(new Node(this, tmpId.toString(), pos, chooser));

		for (int i = tmpId.length() - 1; i >= 0; --i) {
			tmpId.setCharAt(i, (char) (tmpId.charAt(i) + 1));

			if (tmpId.charAt(i) <= 'Z')
				break;

			tmpId.setCharAt(i, 'A');
			if (i == 0) {
				tmpId.append('A');
			}
		}

		return ret;
	}

	public boolean rmNode(Node removable, Canvas canvas) {
		boolean ret = nodes.remove(removable);

		for (Node node : nodes) {
			node.pop(removable);
		}

		edges.removeIf(edge -> edge.nodeInEdge(removable));

		return ret;
	}

	public Node getNode(Position pos) {
		for (Node node : nodes) {
			Position nodePos = node.getPosition();
			if (Math.sqrt(Math.pow(pos.x - nodePos.x, 2) + Math.pow(pos.y - nodePos.y, 2)) < nodeRadius)
				return node;
		}

		return null;
	}

	public Node getNode(String id) {
		for (Node node : nodes) {
			if (node.getId().equals(id))
				return node;
		}
		return null;
	}

	private Edge getEdge(Node n1, Node n2) {
		Edge searched = null;
		for (Edge edge : edges) {
			if (edge.nodeInEdge(n1) && edge.nodeInEdge(n2)) {
				searched = edge;
				break;
			}
		}
		return searched;
	}

	public boolean addEdge(Node n1, Node n2) {
		Edge newEdge = getEdge(n1, n2);
		boolean ret;
		if (newEdge != null) {
			return true;
		} else {
			ret = edges.add(new Edge(n1, n2));
		}

		ret = ret && n1.push(n2);
		ret = ret && n2.push(n1);

		return ret;
	}

	public boolean rmEdge(Node n1, Node n2) {
		Edge removable = getEdge(n1, n2);

		boolean ret = true;

		if (removable != null) {
			ret = edges.remove(removable);

			ret = ret && n1.pop(n2);
			ret = ret && n2.pop(n1);
		}
		return ret;
	}

	public boolean addEdge(Edge edge) {
		return edges.add(edge);
	}

	public boolean rmEdge(Edge edge) {
		return edges.remove(edge);
	}

	public LinkedHashSet<Node> getNodes() {
		return nodes;
	}

	public void export(File file) throws IOException {
		double minx = Double.POSITIVE_INFINITY;
		double miny = Double.POSITIVE_INFINITY;
		double maxx = Double.NEGATIVE_INFINITY;
		double maxy = Double.NEGATIVE_INFINITY;
		for (Node node : nodes) {
			double x = node.getPosition().x;
			double y = node.getPosition().y;
			if (x > maxx)
				maxx = x;
			if (x < minx)
				minx = x;
			if (y > maxy)
				maxy = y;
			if (y < miny)
				miny = y;
		}
		double width = maxx - minx + nodeRadius * 2 + 10;
		double height = maxy - miny + nodeRadius * 2 + 10;

		FileWriter myWriter = new FileWriter(file);
		PrintWriter myPrint = new PrintWriter(myWriter);
		myPrint.println("<svg height=\"" + height + "\" width=\"" + width + "\">");
		for (Edge edge : edges) {
			myPrint.println(edge.export(minx, miny));
		}
		for (Node node : nodes) {
			myPrint.println(node.export(minx, miny));
		}
		myPrint.println("</svg>");
		myPrint.close();
	}

	public void refresh(Canvas canvas) {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (Edge edge : edges)
			edge.draw(canvas);
		for (Node node : nodes)
			node.draw(canvas);
	}
}
