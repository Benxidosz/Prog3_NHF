package ges.graph;

import ges.graph.edges.Edge;
import ges.graph.edges.skins.BaseEdgeSkin;
import ges.graph.edges.skins.DirectedEdgeSkin;
import ges.graph.node.Node;
import javafx.scene.canvas.Canvas;

import java.io.*;
import java.util.LinkedHashSet;

/**
 * It is represent a Graph and it could be serialize.
 */
public class Graph implements Serializable {

	/**
	 * The radius of the nodes.
	 */
	public int nodeRadius;

	/**
	 * A StringBuilder, which the next id of Node.
	 */
	private final StringBuilder tmpId;

	/**
	 * The title of the graph.
	 */
	public String title;

	/**
	 * A hash set, which store the nodes.
	 */
	private final LinkedHashSet<Node> nodes;

	/**
	 * A hash set, which store the edges.
	 */
	private final LinkedHashSet<Edge> edges;

	/**
	 * Constructor of the Graph.
	 *
	 * @param nodeRadius The nodeRadius value.
	 */
	public Graph(int nodeRadius) {
		title = "Untitled";
		this.nodeRadius = nodeRadius;
		tmpId = new StringBuilder(String.valueOf((char) ('A' - 1)));

		nodes = new LinkedHashSet<>();
		edges = new LinkedHashSet<>();
	}

	/**
	 * A copy constructor of the Graph
	 *
	 * @param graph The graph, which from the deepCopy make.
	 */
	public Graph(Graph graph) {
		nodeRadius = graph.nodeRadius;
		nodes = new LinkedHashSet<>();
		edges = new LinkedHashSet<>();
		LinkedHashSet<Node> added = new LinkedHashSet<>();

		for (Edge edge : graph.edges) {
			Node n1;
			Node n2;

			if (!added.contains(edge.getNode(0))) {
				n1 = new Node(this, edge.getNode(0));
				nodes.add(n1);
				added.add(edge.getNode(0));
			} else {
				n1 = getNode(edge.getNode(0).getId());
			}

			if (!added.contains(edge.getNode(1))) {
				n2 = new Node(this, edge.getNode(1));
				nodes.add(n2);
				added.add(edge.getNode(1));
			} else {
				n2 = getNode(edge.getNode(1).getId());
			}

			if (n1 != null && n2 != null)
				if (edge.getNode(1).getNeighbours().contains(edge.getNode(0)))
					addEdge(n1, n2);
				else
					addDirectedEdge(n1, n2);

			getEdge(n1, n2).getMySkin().toComplete();
		}

		for (Node node : graph.nodes) {
			if (!added.contains(node)) {
				Node cpyNode = new Node(this, node);
				nodes.add(cpyNode);
				added.add(node);
			}
		}
		tmpId = new StringBuilder(graph.tmpId);
		title = graph.title;
	}

	/**
	 * Set the data shown of the nodes center.
	 *
	 * @param choose Id or Div
	 */
	public void setChooser(String choose) {
		for (Node node : nodes) {
			node.setChooser(choose);
		}
	}

	/**
	 * Add a node to the graph to a given position.
	 *
	 * @param pos     The position.
	 * @param chooser The data shown of the nodes center.
	 * @return If it is a success.
	 */
	public Node addNode(Position pos, String chooser) {
		increaseId();

		Node ret = new Node(this, tmpId.toString(), pos, chooser);
		nodes.add(ret);

		return ret;
	}

	public void increaseId() {
		if (tmpId.length() == 0) {
			tmpId.append('A');
			return;
		}

		for (int i = tmpId.length() - 1; i >= 0; --i) {
			if (tmpId.charAt(i) == ' ')
				tmpId.setCharAt(i, 'A');
			else
				tmpId.setCharAt(i, (char) (tmpId.charAt(i) + 1));

			if (tmpId.charAt(i) <= 'Z')
				break;

			tmpId.setCharAt(i, 'A');
			if (i == 0) {
				tmpId.append('A');
			}
		}
	}

	public void addNode(Node addable) {
		addable.getNeighbours().forEach(nei -> {
			if (nei.wereNei(addable)) {
				nei.push(addable);
				addEdge(addable, nei);
			} else
				addDirectedEdge(addable, nei);
		});

		nodes.forEach(node -> {
			if (node.wereNei(addable)) {
				node.push(addable);
				addDirectedEdge(node, addable);
			}
		});

		nodes.add(addable);
	}

	/**
	 * Remove a node from the graph.
	 *
	 * @param removable The node, which need to be removed.
	 * @return If it is a success.
	 */
	public boolean rmNode(Node removable) {
		boolean ret = nodes.remove(removable);

		nodes.forEach(node -> node.popWithTrack(removable));

		edges.removeIf(edge -> edge.nodeInEdge(removable));

		return ret;
	}

	/**
	 * Get a Node by a position.
	 *
	 * @param pos The position.
	 * @return The gotten node, null if there is is no node in position.
	 */
	public Node getNode(Position pos) {
		for (Node node : nodes) {
			Position nodePos = node.getPosition();
			if (Math.sqrt(Math.pow(pos.x - nodePos.x, 2) + Math.pow(pos.y - nodePos.y, 2)) < nodeRadius)
				return node;
		}

		return null;
	}

	/**
	 * Get a node by id.
	 *
	 * @param id The id.
	 * @return The founded Node, null if not find any.
	 */
	public Node getNode(String id) {
		for (Node node : nodes) {
			if (node.getId().equals(id))
				return node;
		}
		return null;
	}

	/**
	 * Get an edge by the 2 endNode of the edge.
	 *
	 * @param n1 first end.
	 * @param n2 second end
	 * @return The founded Edge, null if not find any.
	 */
	public Edge getEdge(Node n1, Node n2) {
		Edge searched = null;
		for (Edge edge : edges) {
			if (edge.nodeInEdge(n1) && edge.nodeInEdge(n2)) {
				searched = edge;
				break;
			}
		}
		return searched;
	}

	/**
	 * Add an edge, with two given nodes.
	 *
	 * @param n1 First.
	 * @param n2 Second
	 */
	public void addEdge(Node n1, Node n2) {
		Edge newEdge = getEdge(n1, n2);
		if (newEdge != null) {
			if (!n1.getNeighbours().contains(n2) || !n2.getNeighbours().contains(n1)) {
				newEdge.setMySkin(new BaseEdgeSkin(newEdge));
				n1.push(n2);
				n2.push(n1);
			}
		} else {
			if (!n1.equals(n2)) {
				edges.add(new Edge(n1, n2));
				n1.push(n2);
				n2.push(n1);
			}
		}

	}

	/**
	 * Add a given edge.
	 *
	 * @param edge The given edge.
	 * @return If it was a success.
	 */
	public boolean addEdge(Edge edge) {
		return edges.add(edge);
	}

	public void addDirectedEdge(Node n1, Node n2) {
		Edge newEdge = getEdge(n1, n2);

		if (newEdge != null) {
			if (!newEdge.exactCompare(n1, n2)) {
				newEdge.setMySkin(new BaseEdgeSkin(newEdge));
				n1.push(n2);
				n2.push(n1);
			}
		} else {
			if (!n1.equals(n2)) {
				newEdge = new Edge(n1, n2);

				Skin skin = new DirectedEdgeSkin(newEdge);
				skin.toComplete();
				newEdge.setMySkin(skin);

				addEdge(newEdge);
				n1.push(n2);
			}
		}
	}

	/**
	 * Remove an edge, with two given nodes.
	 *
	 * @param n1 First.
	 * @param n2 Second
	 * @return If it is a success.
	 */
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

	public void rmDirectedEdge(Node n1, Node n2) {
		Edge removable = getEdge(n1, n2);

		if (removable != null) {
			if (n2.getNeighbours().contains(n1)) {
				removable.getNodes()[0] = n2;
				removable.getNodes()[1] = n1;

				n1.pop(n2);

				Skin skin = new DirectedEdgeSkin(removable);
				skin.toComplete();
				removable.setMySkin(skin);
			} else {
				rmEdge(n1, n2);
			}
		}
	}

	/**
	 * Remove a given edge.
	 *
	 * @param edge The given edge.
	 * @return If it was a success.
	 */
	public boolean rmEdge(Edge edge) {
		return edges.remove(edge);
	}

	/**
	 * Get the nodes list.
	 *
	 * @return The list.
	 */
	public LinkedHashSet<Node> getNodes() {
		return nodes;
	}

	/**
	 * Export the graph to svg. It calculate the future file width and height:
	 * It find the min/max x/y value of the nodes.
	 * The width will be the difference between the maxx and minx (maxx - minx) + double of the nodeRadius
	 * + 10 (margin 5px left and right)
	 * The height will be the difference between the maxy and miny (maxy - miny) + double of the nodeRadius
	 * + 10 (margin 5px top and bottom)
	 * Than it will generate the sv files.
	 *
	 * @param file The destination file.
	 * @throws IOException The file management can throw it.
	 */
	public void export(File file) throws IOException {
		//width and height calculation.
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

		//The file generating and writing
		FileWriter myWriter = new FileWriter(file);
		PrintWriter myPrint = new PrintWriter(myWriter);

		//open tag.
		myPrint.println("<svg height=\"" + height + "\" width=\"" + width + "\" xmlns='http://www.w3.org/2000/svg' version='1.1' xmlns:xlink='http://www.w3.org/1999/xlink'>");

		//Code of all edges.
		for (Edge edge : edges) {
			myPrint.println(edge.export(minx, miny));
		}

		//Code of all nodes.
		for (Node node : nodes) {
			myPrint.println(node.export(minx, miny));
		}

		//close tag
		myPrint.println("</svg>");
		myPrint.close();
	}

	/**
	 * It draw all the elements of the graph.
	 *
	 * @param canvas The program canvas where the graph drawn.
	 */
	public void refresh(Canvas canvas) {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (Edge edge : edges)
			edge.draw(canvas);
		for (Node node : nodes)
			node.draw(canvas);
	}
}
