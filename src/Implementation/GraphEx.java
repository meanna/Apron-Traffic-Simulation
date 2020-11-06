package Implementation;

import Interfaces.IEdge;
import Interfaces.IModel;
import Interfaces.INode;

import java.util.Comparator;

import java.util.List;

/**
 * Experimental Graph Class
 *
 * Converts Nodes & Edges into an Adjacency Matrix (Graph)
 * The entry in the Matrix is an Edge, between two nodes
 */

public class GraphEx
{
    private IEdge[][] graph;

    public GraphEx(IModel model)
    {
        List<INode> nodeList = model.getNodes();
        List<IEdge> edgeList = model.getEdges();

        //Looks for the highest node number in the nodeList
        int maxNumber = nodeList.stream().map(x -> x.GetNumber()).max(Comparator.comparing(Integer::valueOf)).get();

        graph = new IEdge[maxNumber +1][maxNumber +1];

        //Edges between two nods represented as an Edge
        for (IEdge e : edgeList)
        {
            INode inNode = model.GetNodeByNumber(Integer.parseInt(e.GetInNodeNumber()));
            INode outNode = model.GetNodeByNumber(Integer.parseInt(e.GetOutNodeNumber()));

            graph[inNode.GetNumber()][outNode.GetNumber()] = e;
        }

    }

    /**
     * Returns the Edge between two Nodes
     * Care: returns null if there is no edge
     * @param node1
     * @param node2
     * @return the Edge between two Nodes
     */
    public IEdge EdgeBetweenNodes(INode node1, INode node2)
    {
        return graph[node1.GetNumber()][node2.GetNumber()];
    }

    /**
     * For Testing the Graph in the console
     * @return Graph visual
     */
    public String toString()
    {
        StringBuilder b = new StringBuilder();

        for (int y = 0; y < graph.length; y++)
        {
            b.append("[");
            for (int x = 0; x < graph[y].length; x++)
            {
                b.append(" [");
                b.append(graph[x][y]);
                b.append("] ");
            }
            b.append("]\n");
        }

        return b.toString();
    }
}
