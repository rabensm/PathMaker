package base.path;

import base.Config;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a depth slice. Each slice contains paths that are cut at this slice's depth.
 */
public class Slice {
    // whether or not a segment is in this slice
    // a segment is still "in" this slice if it's completely below this slice, because this slice must still cut the
    //  segment in its pass before the same segment is cut in lower passes
    public enum SegmentInSlice {
        IN_SLICE,
        NOT_IN_SLICE
    }

    // z depth of top of slice
    double top;

    // z depth of bottom of slice
    double bottom;

    // the cutting paths for this slice
    private List<Path> paths;

    // the current path being added to when slicing
    private Path openPath;

    // are paths in this slice in reverse order?
    private boolean reverseOrder;

    public Slice (int depthIndex) {
        top = depthIndex * -Config.PassDepth;
        bottom = (depthIndex + 1) * -Config.PassDepth;
        paths = new ArrayList<>();
        openPath = null;
        reverseOrder = false;
    }

    /**
     * Takes a path segment expressed as two nodes, and creates or continues a path for it in this slice
     *
     * @param n0    segment start node
     * @param n1    segment end node
     * @return      whether or not the segment was in this slice
     */
    public SegmentInSlice slicePathSegment (Vector3d n0, Vector3d n1) {
        if (n0.z > top) {
            // n0 is above this slice, so we don't need to add node for it
            if (n1 == null || n1.z > top) {
                // entire segment is above this slice - we're done
                return SegmentInSlice.NOT_IN_SLICE;
            }
        }
        else {
            // add node at n0
            Vector3d newPathNode = new Vector3d(n0);
            if (n0.z <= bottom) {
                // clamp node to bottom
                newPathNode.z = bottom;
            }
            getOpenPath().addNode(newPathNode);
        }

        if (n1 == null) {
            // no segment (n0 was last node), so close the open path
            closeOpenPath();
        }
        else {
            // add nodes if and where segment crosses top and bottom
            // bottom then top if segment slopes up, top then bottom if slopes down
            double intersects[] = (n0.z < n1.z) ? new double[]{bottom, top} : new double[]{top, bottom};
            addNodeAtZIntersect(n0, n1, intersects[0]);
            addNodeAtZIntersect(n0, n1, intersects[1]);

            if (n1.z > top) {
                // n0 is in this slice, but next node (n1) is not, so close path
                closeOpenPath();
            }
        }

        return SegmentInSlice.IN_SLICE;
    }

    /**
     * Takes a path segment, and tests whether it intersects a z-plane. If it does, it adds a node at the intersection
     *   to the open path.
     *
     * @param n0            segment start node
     * @param n1            segment end node
     * @param zIntersect    z intersect plane
     */
    private void addNodeAtZIntersect (Vector3d n0, Vector3d n1, double zIntersect) {
        double segmentZSpan = n1.z - n0.z;
        if (segmentZSpan == 0) {
            // we won't intersect in z if segment doesn't slope in z
            return;
        }

        // where our interpolation lies along segment
        double interpAlpha = (zIntersect - n0.z) / segmentZSpan;

        if (interpAlpha > 0 && interpAlpha < 1) {
            Vector3d newNode = new Vector3d();
            newNode.interpolate(n0, n1, interpAlpha);
            getOpenPath().addNode(newNode);
        }
    }

    /**
     * Gets the current open path. Creates a new one if there isn't one.
     *
     * @return  the current open path
     */
    public Path getOpenPath() {
        if (openPath == null) {
            openPath = new Path();
        }

        return openPath;
    }

    /**
     * Closes the current open path, and adds it to this slice's paths.
     */
    public void closeOpenPath() {
        if (openPath != null && openPath.size() > 1) {
            paths.add(openPath);
        }
        openPath = null;
    }

    /**
     * Put all paths in this slice and their content in forward or reverse order
     *
     * @param isReverse true if reverse, false if forward
     */
    public void setReverseOrder (boolean isReverse) {
        if (reverseOrder != isReverse) {
            for (Path path : paths) {
                path.setReverseOrder(isReverse);
            }
            Collections.reverse(paths);
            reverseOrder = isReverse;
        }
    }

    /**
     * Generates the g-code to cut all the paths in this slice, in order
     *
     * @return  String containing g-code for this slice
     */
    public String getGCode() {
        StringBuilder gCodeStr = new StringBuilder();

        for (Path path : paths) {
            gCodeStr.append(path.getGCode());
        }

        return gCodeStr.toString();
    }
}
