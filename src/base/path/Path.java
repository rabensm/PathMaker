package base.path;

import base.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.vecmath.Vector3d;

/**
 * Represents a 3D path as a list of 3D nodes
 */
public class Path {
    // the 3D nodes that make this path
    protected List<Vector3d> nodes;

    // is this path in reverse order?
    private boolean reverseOrder;

    public Path () {
        nodes = new ArrayList<>();
        reverseOrder = false;
    }

    /**
     * Add one node to end of path
     *
     * @param x x coordinate of new node
     * @param y y coordinate of new node
     * @param z z coordinate of new node
     */
    public void addNode (double x, double y, double z) {
        nodes.add (new Vector3d (x, y, z));
    }

    /**
     * Add one node to end of path
     *
     * @param node  new node to add
     */
    public void addNode (Vector3d node) {
        nodes.add(node);
    }

    /**
     * Number of nodes in path
     *
     * @return  number of nodes in path
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Put this path in forward or reverse order
     *
     * @param isReverse true if reverse, false if forward
     */
    public void setReverseOrder (boolean isReverse) {
        if (reverseOrder != isReverse) {
            Collections.reverse(nodes);
            reverseOrder = isReverse;
        }
    }

    /**
     * Generates the g-code to cut this path
     *
     * @return  String containing g-code for this path
     */
    public String getGCode () {
        if (nodes.size() < 2) {
            return "";
        }

        StringBuilder gCodeStr = new StringBuilder();

        Vector3d firstNode = nodes.get(0);

        // enter rapid move mode and move up to safe height
        gCodeStr.append("G00 Z").append(Config.SafeHeight).append("\n")

                // move over to above first node
                .append("X ").append(firstNode.x).append(" Y").append(firstNode.y).append(" Z").append(Config.SafeHeight).append("\n")

                // move down to starting height
                .append("Z ").append(Config.StartHeight).append("\n")

                // enter linear move mode, and set feed rate
                .append("G01 F").append(Config.FeedRate).append("\n");

        // append the x, y, z of each of the nodes
        for (Vector3d node : nodes) {
            gCodeStr.append("X").append(node.x).append(" Y").append(node.y).append(" Z").append(node.z).append("\n");
        }

        // enter rapid move mode and go back to safe height
        gCodeStr.append("G00 Z").append(Config.SafeHeight).append("\n");

        return gCodeStr.toString();
    }
}
