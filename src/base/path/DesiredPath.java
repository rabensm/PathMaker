package base.path;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

/**
 * The desired path we want cut
 */
public class DesiredPath extends Path {

    // depth slices (one for each pass, to cut path in multiple passes)
    private List<Slice> slices;

    public DesiredPath () {
        slices = new ArrayList<>();
    }

    /**
     * Creates a list of depth slices from this path, where each slice is a lower depth than the previous.
     */
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

        // reverse every other slice for efficiency when cutting
        for (int iSlice = 0; iSlice < slices.size(); ++iSlice) {
            if ((iSlice & 1) == 1) {
                slices.get(iSlice).setReverseOrder(true);
            }
        }
    }

    /**
     * Gets a slice by index. Automatically creates slices down to the required index.
     *
     * @param index index of slice to get
     * @return      the requested slice
     */
    private Slice getSlice(int index) {
        while (slices.size() <= index) {
            int newSliceIndex = slices.size();
            slices.add(new Slice(newSliceIndex));
        }
        return slices.get(index);
    }
}
