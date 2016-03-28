package base;

import base.path.DesiredPath;

public class Main {

    public static void main(String[] args) {
        DesiredPath path1 = new DesiredPath();

        // hacked together test path
        path1.addNode(10, 10, 0);
        path1.addNode(10, 20, -2);
        path1.addNode(10, 30, -4);
        path1.addNode(10, 40, -6);
        path1.addNode(10, 50, -8);
        path1.addNode(10, 60, -10);
        path1.addNode(10, 70, -8);
        path1.addNode(10, 80, -6);

        path1.slicePath();
        String gCode = path1.getGCode();
    }
}
