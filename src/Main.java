public class Main {

    public static void main(String[] args) {
        DesiredPath path1 = new DesiredPath();

        path1.addNode(10, 10, 0);
        path1.addNode(10, 20, 1);
        path1.addNode(11, 30, 2);
        path1.addNode(13, 40, 3);
        path1.addNode(16, 50, 4);
        path1.addNode(20, 60, 5);
        path1.addNode(25, 70, 6);
        path1.addNode(31, 80, 7);
        path1.addNode(38, 90, 7);
        path1.addNode(46, 100, 7);
        path1.addNode(55, 110, 7);
        path1.addNode(65, 120, 7);
        path1.addNode(76, 130, 7);
        path1.addNode(88, 140, 7);
        path1.addNode(101, 150, 7);
        path1.addNode(115, 160, 7);
        path1.addNode(130, 170, 7);
        path1.addNode(146, 180, 7);
        path1.addNode(163, 190, 7);
        path1.addNode(181, 200, 7);
        path1.addNode(200, 200, 7);

        path1.createPassPaths();
    }
}
