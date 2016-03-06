package base.path;

import java.util.ArrayList;
import java.util.List;

public class Slice {
    private List<Path> paths;
    private Path openPath;

    public Slice () {
        paths = new ArrayList<>();
        openPath = null;
    }

    public Path getOpenPath() {
        if (openPath == null) {
            openPath = new Path();
            paths.add(openPath);
        }

        return openPath;
    }

    public void closeOpenPath() {
        openPath = null;
    }
}
