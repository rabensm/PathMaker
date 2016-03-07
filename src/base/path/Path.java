package base.path;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;

/**
 * Represents a 3D path as a list of 3D nodes
 */
public class Path {
    protected List<Vector3d> nodes;

    public Path () {
        nodes = new ArrayList<>();
    }

    /**
     * Add one node to end of path
     *
     * @param x
     * @param y
     * @param z
     */
    public void addNode (double x, double y, double z) {
        nodes.add (new Vector3d (x, y, z));
    }

    /**
     * Add one node to end of path
     *
     * @param node
     */
    public void addNode (Vector3d node) {
        nodes.add(node);
    }

    /**
     * Number of nodes in path
     *
     * @return
     */
    public int size() {
        return nodes.size();
    }

}
