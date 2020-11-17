package sample;

public class Edge {
	Node[] nodes;

	Edge(Node n1, Node n2) {
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = n2;
	}
}
