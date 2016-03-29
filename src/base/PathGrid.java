package base;

import base.path.DesiredPath;
import base.path.Path;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrabens on 3/28/2016.
 */
public class PathGrid {
    private int         numStripes  = 10;
    private Vector3d    origin      = new Vector3d(0,0,-8.0);
    private double      length      = 100.0;
    private double      stride      = 12.37333;
    private double      widenRadius = 0.79375;
    private int         sweepSteps  = 5;

    private List<Path>  paths;

    public void make() {
        paths = new ArrayList<>();

        for (int i = 0; i < numStripes; ++i) {
            Vector3d stripeOrigin = new Vector3d(origin);
            stripeOrigin.x += stride * i;
            makeStripe(stripeOrigin);
        }
    }

    private void makeStripe(Vector3d stripeOrigin) {
        // start with deepest center cut, as a DesiredPath with pass slices
        DesiredPath centerPath = new DesiredPath();
        centerPath.addNode(stripeOrigin);
        Vector3d endNode = new Vector3d(stripeOrigin);
        endNode.y += length;
        centerPath.addNode(endNode);
        centerPath.slicePath();
        paths.add(centerPath);

        // sweep up to the left and right to widen this stripe to desired radius
        sweepStripe(stripeOrigin, -1);
        sweepStripe(stripeOrigin, 1);
    }

    private void sweepStripe(Vector3d stripeOrigin, double angleDirection) {
        Vector3d sweepCenter = new Vector3d(stripeOrigin);
        sweepCenter.z += widenRadius;
        for (int step = 1; step <= sweepSteps; ++step) {
            double stepAngle = (Math.PI / 2) * ((double)step / sweepSteps) * angleDirection;
            Vector3d n0 = new Vector3d(sweepCenter);
            n0.z -= widenRadius * Math.cos(stepAngle);
            n0.x += widenRadius * Math.sin(stepAngle);
            Vector3d n1 = new Vector3d(n0);
            n1.y += length;

            Path path = new Path();
            path.addNode(n0);
            path.addNode(n1);
            if ((step & 1) == 0) {
                path.setReverseOrder(true);
            }
            paths.add(path);
        }
    }

    public String getGCode() {
        StringBuilder gCodeStr = new StringBuilder();

        for (Path path : paths) {
            gCodeStr.append(path.getGCode());
        }

        return gCodeStr.toString();
    }
}
