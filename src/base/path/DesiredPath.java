package base.path;

import base.Config;

import javax.vecmath.Vector3d;
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

        // iterate through our nodes, getting node pairs (our path segments) into n0 and n1
        // nodes 0 and 1, then 1 and 2, then 2 and 3, etc.
        for (int iNode = 0; iNode < nodes.size(); ++iNode) {
            Vector3d n0 = nodes.get(iNode);
            // on the last iteration, n0 will be last node and n1 will be null
            Vector3d n1 = (iNode + 1 < nodes.size()) ? nodes.get(iNode + 1) : null;

            int sliceIndex = 0;

            // probe into lower and lower slices, passing our path segment,
            //  until we hit one where the segment isn't in the slice
            while (getSlice(sliceIndex).slicePathSegment(n0, n1) == Slice.SegmentInSlice.IN_SLICE) {
                ++sliceIndex;
            }
        }
    }

    private Slice getSlice(int index) {
        while (slices.size() <= index) {
            int newSliceIndex = slices.size();
            slices.add(new Slice(newSliceIndex));
        }
        return slices.get(index);
    }
}
