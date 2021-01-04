import ges.graph.Graph;
import ges.graph.Position;
import org.junit.Assert;
import org.junit.Before;
import ges.graph.nodes.Node;
import org.junit.Test;

import java.util.HashMap;

public class GraphTester {
	Graph testGraph;
	HashMap<String, Node> testNodes;

	@Before
	public void init() {
		testGraph = new Graph(50);
		for (int i = 0; i < 5; ++i) {
			testGraph.addNode(new Position(100, (100 + i * 50)), null);
		}

		testNodes = new HashMap<>();

		Node nodeA = testGraph.getNode("A");
		testNodes.put("A", nodeA);
		Node nodeB = testGraph.getNode("B");
		testNodes.put("B", nodeB);
		Node nodeC = testGraph.getNode("C");
		testNodes.put("C", nodeC);
		Node nodeD = testGraph.getNode("D");
		testNodes.put("D", nodeD);
		Node nodeE = testGraph.getNode("E");
		testNodes.put("E", nodeE);

		testGraph.addEdge(nodeB, nodeC);
		testGraph.addEdge(nodeD, nodeC);
		testGraph.addEdge(nodeE, nodeC);
		testGraph.addEdge(nodeE, nodeD);

	}

	@Test
	public void testCpyCons() {
		Graph clone = new Graph(testGraph);

		Assert.assertNotSame(clone, testGraph);
		Assert.assertEquals(clone.nodeRadius, testGraph.nodeRadius);
		Assert.assertEquals(clone.title, testGraph.title);

		Assert.assertNotSame(clone.getNodes().toArray(), testGraph.getNodes().toArray());
		clone.getNodes().forEach(node -> {
			Assert.assertEquals(node.getId(), testGraph.getNode(node.getPosition()).getId());
			Assert.assertEquals(node.getDim(), testGraph.getNode(node.getId()).getDim());
		});
	}

	@Test
	public void testNodeInGraphPos() {
		testNodes.forEach((key, value) -> {
			Node tmp = testGraph.getNode(value.getPosition());
			Assert.assertEquals(testNodes.get(key), tmp);
		});
	}

	@Test
	public void testNodeInGraphId() {
		testNodes.forEach((key, value) -> {
			Node tmp = testGraph.getNode(value.getId());
			Assert.assertEquals(testNodes.get(key), tmp);
		});
	}

	@Test
	public void testNodeNeighbours() {
		Node[] expectA = new Node[0];

		Node[] expectB = new Node[1];
		expectB[0] = testNodes.get("C");

		Node[] expectC = new Node[3];
		expectC[0] = testNodes.get("B");
		expectC[1] = testNodes.get("D");
		expectC[2] = testNodes.get("E");

		Node[] expectD = new Node[2];
		expectD[0] = testNodes.get("C");
		expectD[1] = testNodes.get("E");

		Node[] expectE = new Node[2];
		expectE[0] = testNodes.get("C");
		expectE[1] = testNodes.get("D");

		Assert.assertArrayEquals(expectA, testNodes.get("A").getNeighbours().toArray());
		Assert.assertArrayEquals(expectB, testNodes.get("B").getNeighbours().toArray());
		Assert.assertArrayEquals(expectC, testNodes.get("C").getNeighbours().toArray());
		Assert.assertArrayEquals(expectD, testNodes.get("D").getNeighbours().toArray());
		Assert.assertArrayEquals(expectE, testNodes.get("E").getNeighbours().toArray());
	}

	@Test
	public void testRemoveNode() {
		testGraph.rmNode(testNodes.get("C"));
		Node nodeCId = testGraph.getNode(testNodes.get("C").getId());
		Node nodeCPos = testGraph.getNode(testNodes.get("C").getPosition());
		Assert.assertNull(nodeCId);
		Assert.assertNull(nodeCPos);

		Node[] expectB = new Node[0];
		Node[] expectD = new Node[1];
		expectD[0] = testNodes.get("E");
		Node[] expectE = new Node[1];
		expectE[0] = testNodes.get("D");

		Assert.assertArrayEquals(expectB, testNodes.get("B").getNeighbours().toArray());
		Assert.assertArrayEquals(expectD, testNodes.get("D").getNeighbours().toArray());
		Assert.assertArrayEquals(expectE, testNodes.get("E").getNeighbours().toArray());

	}

	@Test
	public void testRemoveEdge() {
		Node nodeC = testNodes.get("C");
		Node nodeE = testNodes.get("E");
		testGraph.rmEdge(nodeE, nodeC);

		Node[] expectC = new Node[2];
		expectC[0] = testNodes.get("B");
		expectC[1] = testNodes.get("D");

		Node[] expectE = new Node[1];
		expectE[0] = testNodes.get("D");

		Assert.assertArrayEquals(expectC, testNodes.get("C").getNeighbours().toArray());
		Assert.assertArrayEquals(expectE, testNodes.get("E").getNeighbours().toArray());
	}

	@Test
	public void testIdGenerator() {
		Graph testIdGraph = new Graph(50);
		for (char i = 'A'; i <= ('Z' + 1); ++i) {
			testIdGraph.addNode(new Position(0, 0), null);
		}
		Assert.assertNotNull(testIdGraph.getNode("AA"));
	}

	@Test
	public void testLoopEdge() {
		Node nodeA = testNodes.get("A");
		int divA = testNodes.get("A").getDim();
		testGraph.addEdge(nodeA, nodeA);
		Assert.assertEquals(divA, testNodes.get("A").getDim());
		nodeA.push(nodeA);
		Assert.assertEquals(divA, testNodes.get("A").getDim());
	}
}
