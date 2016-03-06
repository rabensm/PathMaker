package base.path;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3f;

public class Path {
    protected List<Vector3f> nodes;

    public Path () {
        nodes = new ArrayList<>();
    }

    public void addNode (float x, float y, float z) {
        nodes.add (new Vector3f (x, y, z));
    }

    public void addNode (Vector3f node) {
        nodes.add(node);
    }

}
