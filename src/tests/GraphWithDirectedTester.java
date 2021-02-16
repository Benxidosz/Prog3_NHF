import ges.graph.Graph;
import ges.graph.Position;
import ges.graph.edges.skins.BaseEdgeSkin;
import ges.graph.edges.skins.DirectedEdgeSkin;
import ges.graph.node.Node;
import org.junit.*;

import java.util.HashMap;

public class GraphWithDirectedTester {
	Graph testGraph;
	HashMap<String, Node> testNodes;

	@Before
	public void init() {
		testGraph = new Graph(50);
		for (int i = 0; i < 6; ++i) {
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
		Node nodeF = testGraph.getNode("F");
		testNodes.put("F", nodeE);

		testGraph.addEdge(nodeA, nodeC);
		testGraph.addDirectedEdge(nodeB, nodeA);
		testGraph.addEdge(nodeB, nodeD);
		testGraph.addDirectedEdge(nodeC, nodeD);
		testGraph.addDirectedEdge(nodeB, nodeF);
	}

	@Test
	public void TestInit() {
		Node nodeA = testGraph.getNode("A");
		Node nodeB = testGraph.getNode("B");
		Node nodeC = testGraph.getNode("C");
		Node nodeD = testGraph.getNode("D");
		Node nodeE = testGraph.getNode("E");
		Node nodeF = testGraph.getNode("F");

		Assert.assertTrue(testGraph.getNodes().contains(nodeA));
		Assert.assertTrue(testGraph.getNodes().contains(nodeB));
		Assert.assertTrue(testGraph.getNodes().contains(nodeC));
		Assert.assertTrue(testGraph.getNodes().contains(nodeD));
		Assert.assertTrue(testGraph.getNodes().contains(nodeE));
		Assert.assertTrue(testGraph.getNodes().contains(nodeF));

		Assert.assertEquals(1, nodeA.getDim());
		Assert.assertEquals(3, nodeB.getDim());
		Assert.assertEquals(2, nodeC.getDim());
		Assert.assertEquals(1, nodeD.getDim());
		Assert.assertEquals(0, nodeE.getDim());
		Assert.assertEquals(0, nodeF.getDim());

		Assert.assertTrue(nodeA.getNeighbours().contains(nodeC)
				&& !nodeA.getNeighbours().contains(nodeB)
		);
		Assert.assertTrue(nodeB.getNeighbours().contains(nodeA)
				&& nodeB.getNeighbours().contains(nodeD)
				&& nodeB.getNeighbours().contains(nodeF)
		);
		Assert.assertTrue(nodeC.getNeighbours().contains(nodeA)
				&& nodeC.getNeighbours().contains(nodeD)
		);
		Assert.assertTrue(nodeD.getNeighbours().contains(nodeB)
				&& !nodeD.getNeighbours().contains(nodeC)
		);
		Assert.assertFalse(nodeF.getNeighbours().contains(nodeB));
	}

	@Test
	public void addDirectedEdge() {
		Node nodeA = testGraph.getNode("A");
		Node nodeE = testGraph.getNode("E");

		testGraph.addDirectedEdge(nodeA, nodeE);
		Assert.assertTrue(
				nodeA.getNeighbours().contains(nodeE)
						&& !nodeE.getNeighbours().contains(nodeA)
		);
		Assert.assertEquals(DirectedEdgeSkin.class, testGraph.getEdge(nodeA, nodeE).getMySkin().getClass());
		Assert.assertEquals(DirectedEdgeSkin.class, testGraph.getEdge(nodeE, nodeA).getMySkin().getClass());
		Assert.assertEquals(testGraph.getEdge(nodeA, nodeE), testGraph.getEdge(nodeE, nodeA));
		Assert.assertEquals(2, nodeA.getDim());
		Assert.assertEquals(0, nodeE.getDim());

		testGraph.addDirectedEdge(nodeE, nodeA);
		Assert.assertTrue(
				nodeA.getNeighbours().contains(nodeE)
						&& nodeE.getNeighbours().contains(nodeA)
		);
		Assert.assertEquals(BaseEdgeSkin.class, testGraph.getEdge(nodeA, nodeE).getMySkin().getClass());
		Assert.assertEquals(BaseEdgeSkin.class, testGraph.getEdge(nodeE, nodeA).getMySkin().getClass());
		Assert.assertEquals(testGraph.getEdge(nodeA, nodeE), testGraph.getEdge(nodeE, nodeA));
		Assert.assertEquals(2, nodeA.getDim());
		Assert.assertEquals(1, nodeE.getDim());
	}

	@Test
	public void rmDirectedEdge() {
		Node nodeA = testGraph.getNode("A");
		Node nodeC = testGraph.getNode("C");

		testGraph.rmDirectedEdge(nodeC, nodeA);
		Assert.assertTrue(
				nodeA.getNeighbours().contains(nodeC)
						&& !nodeC.getNeighbours().contains(nodeA)
		);
		Assert.assertEquals(DirectedEdgeSkin.class, testGraph.getEdge(nodeA, nodeC).getMySkin().getClass());
		Assert.assertEquals(DirectedEdgeSkin.class, testGraph.getEdge(nodeC, nodeA).getMySkin().getClass());
		Assert.assertEquals(testGraph.getEdge(nodeA, nodeC), testGraph.getEdge(nodeC, nodeA));
		Assert.assertEquals(1, nodeA.getDim());
		Assert.assertEquals(1, nodeC.getDim());

		testGraph.rmDirectedEdge(nodeA, nodeC);
		Assert.assertTrue(
				!nodeA.getNeighbours().contains(nodeC)
						&& !nodeC.getNeighbours().contains(nodeA)
		);
		Assert.assertNull(testGraph.getEdge(nodeA, nodeC));
		Assert.assertNull(testGraph.getEdge(nodeC, nodeA));
		Assert.assertEquals(0, nodeA.getDim());
		Assert.assertEquals(1, nodeC.getDim());
	}
}
