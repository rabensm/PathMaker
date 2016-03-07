package base.path;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;

public class Path {
    protected List<Vector3d> nodes;

    public Path () {
        nodes = new ArrayList<>();
    }

    public void addNode (double x, double y, double z) {
        nodes.add (new Vector3d (x, y, z));
    }

    public void addNode (Vector3d node) {
        nodes.add(node);
    }

    public int size() {
        return nodes.size();
    }

}
