import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrabens on 2/28/2016.
 */
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
