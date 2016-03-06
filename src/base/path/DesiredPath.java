package base.path;

import base.Config;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class DesiredPath extends Path {

    private List<Slice> slices;

    public DesiredPath () {
        slices = new ArrayList<>();
    }

    public void slicePath() {
        // we need 2 nodes to have any path segments
        if (nodes.size() < 2) {
            return;
        }

        Vector3f n0;
        Vector3f n1 = nodes.get(0);
        for (int iNode = 1; iNode <= nodes.size(); ++iNode) {

            // get our path segment into n0 and n1
            n0 = n1;
            n1 = iNode < nodes.size() ? nodes.get(iNode) : null;

            // the slice that we're currently looking at
            int sliceIndex = 0;
            while (slicePathSegment(n0, n1, sliceIndex)) {
                ++sliceIndex;
            }
        }
    }

    private boolean slicePathSegment (Vector3f n0, Vector3f n1, int sliceIndex) {
        // the top and bottom depth of the slice we're looking at
        float sliceTop = sliceIndex * -Config.PassDepth;
        float sliceBottom = (sliceIndex + 1) * -Config.PassDepth;

        boolean n0AboveTop = (n0.z > sliceTop);
        boolean n0BelowBottom = (n0.z <= sliceBottom);
        boolean n1AboveTop = (n1 != null) && (n1.z > sliceTop);
        boolean n1BelowBottom = (n1 != null) && (n1.z <= sliceBottom);

        if (n0AboveTop && (n1 == null || n1AboveTop)) {
            // this entire segment is above this slice
            return false;
        }

        Slice slice = getSlice(sliceIndex);

        Vector3f newPathNode;

        if (n0AboveTop) {
            // path segment crosses top. add new node where it crosses
            newPathNode = new Vector3f();
            newPathNode.interpolate(n0, n1, (sliceTop - n0.z) / (n1.z - n0.z));
            slice.getOpenPath().addNode(newPathNode);
        }
        else {
            // add this node
            newPathNode = new Vector3f(n0);
            if (n0BelowBottom) {
                // clamp node to bottom of slice
                newPathNode.z = sliceBottom;
            }
            slice.getOpenPath().addNode(newPathNode);
        }

        if (n1 == null) {
            // n0 is last node, so close path if it's open
            slice.closeOpenPath();
        }
        else {
            // handle segment crossing top or bottom

            if (n0BelowBottom != n1BelowBottom) {
                // path segment crosses slice bottom. add new node where it crosses
                newPathNode = new Vector3f();
                newPathNode.interpolate(n0, n1, (sliceBottom - n0.z) / (n1.z - n0.z));
                slice.getOpenPath().addNode(newPathNode);
            }

            if (n1AboveTop) {
                // path segment crosses slice top
                // add new node where it crosses
                newPathNode = new Vector3f();
                newPathNode.interpolate(n0, n1, (sliceTop - n0.z) / (n1.z - n0.z));
                slice.getOpenPath().addNode(newPathNode);
                // we've crossed out of this slice, so close path
                slice.closeOpenPath();
            }
        }

        return true;
    }

    private Slice getSlice(int index) {
        while (slices.size() <= index) {
            slices.add(new Slice());
        }
        return slices.get(index);
    }
}
