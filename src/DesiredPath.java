import java.util.ArrayList;
import java.util.List;

public class DesiredPath extends Path {

    private List<Path> passPaths;

    public DesiredPath () {
        passPaths = new ArrayList<>();
    }

    public void createPassPaths() {
        passPaths.add(this);
       // addPathsBelow(passPaths, Config.PassDepth);
    }

}
