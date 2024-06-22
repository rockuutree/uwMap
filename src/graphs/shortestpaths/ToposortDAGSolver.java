package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();

        edgeTo.put(start, null);
        distTo.put(start, 0.0);

        Stack<V> result = new Stack<>();
        dfsPostOrder(graph, start, new HashSet<>(), result);
        apply_relaxing_edges(result, graph);
    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        Stack<V> stack = new Stack<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            V curr_node = stack.peek();

            boolean all_neighbors_are_visited = true;
            for (Edge<V> e : graph.neighbors(curr_node)) {
                if (!visited.contains(e.to)) {
                    all_neighbors_are_visited = false;
                    break;
                }
            }

            if (graph.neighbors(curr_node).isEmpty() || all_neighbors_are_visited) {
                visited.add(curr_node);
                result.add(curr_node);
                stack.pop();
            } else {
                for (Edge<V> e : graph.neighbors(curr_node)) {
                    if (!visited.contains(e.to)) {
                        stack.add(e.to);
                    }
                }
            }
        }
    }

    private void apply_relaxing_edges(Stack<V> result, Graph<V> graph) {
        // dfsPostOrder =>      [nodes with 0 out-coming pointers ... nodes with 0 in coming pointers]
        // consider node with 0 in-coming pointer first => right to left => pop() => no need to reverse
        while (!result.isEmpty()) {
            V from = result.pop();

            for (Edge<V> e : graph.neighbors(from)) {
                V to = e.to;
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + e.weight;

                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                }
            }
        }
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}