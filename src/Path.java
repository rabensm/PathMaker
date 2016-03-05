import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;

/**
 * Created by mrabens on 2/28/2016.
 */
public class Path {
    protected List<Vector3d> nodes;

    public Path () {
        nodes = new ArrayList<>();
    }

    public void addNode (float x, float y, float z) {
        nodes.add (new Vector3d (x, y, z));
    }

    public void addNode (Vector3d node) {
        nodes.add(node);
    }

}
