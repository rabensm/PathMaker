package base.path;

import base.Config;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class Slice {
    public enum SegmentInSlice {
        IN_SLICE,
        NOT_IN_SLICE
    }

    double top;
    double bottom;
    private List<Path> paths;
    private Path openPath;

    public Slice (int depthIndex) {
        top = depthIndex * -Config.PassDepth;
        bottom = (depthIndex + 1) * -Config.PassDepth;
        paths = new ArrayList<>();
        openPath = null;
    }

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
        }

        return SegmentInSlice.IN_SLICE;
    }

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

    public Path getOpenPath() {
        if (openPath == null) {
            openPath = new Path();
        }

        return openPath;
    }

    public void closeOpenPath() {
        if (openPath != null && openPath.size() > 1) {
            paths.add(openPath);
        }
        openPath = null;
    }
}
