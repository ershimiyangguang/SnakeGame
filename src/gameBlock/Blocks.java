package gameBlock;

import java.awt.*;
import java.util.Random;

public class Blocks {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    private Point[] p;

    public Blocks() {
        Random random = new Random();
        int mapChoice = random.nextInt(3);
        switch (mapChoice) {
            case 0:
                createMap1();
                break;
            case 1:
                createMap2();
                break;
            case 2:
                createMap3();
                break;
        }
    }

    private void createMap1() {
        p = new Point[10];
        Point[] horizontalBlock = createHorizontalBlock(5, 10, 4);
        Point[] verticalBlock = createVerticalBlock(15, 20, 3);
        Point[] horizontalBlock1 = createHorizontalBlock(30, 30, 3);
        System.arraycopy(horizontalBlock, 0, p, 0, 4);
        System.arraycopy(verticalBlock, 0, p, 4, 3);
        System.arraycopy(horizontalBlock1, 0, p, 7, 3);
    }

    private void createMap2() {
        p = new Point[16];
        // 创建四个长度至少为 4 的障碍物，并将它们存储在一个数组中
        Point[] point = createHorizontalBlock(0, 0, 4);
        Point[] point1 = createVerticalBlock(5, 20, 4);
        Point[] point2 = createHorizontalBlock(10, 5, 4) ;
        Point[] point3 = createVerticalBlock(45, 5, 4);
        // 将障碍物的坐标添加到数组中
        System.arraycopy(point, 0, p, 0, 4);
        System.arraycopy(point1, 0, p, 4, 4);
        System.arraycopy(point2, 0, p, 8, 4);
        System.arraycopy(point3, 0, p, 12, 4);
    }

    private void createMap3() {
        p = new Point[14];
        Point[] horizontalBlock = createHorizontalBlock(15, 18, 4);
        Point[] verticalBlock = createVerticalBlock(25, 22, 5);
        Point[] horizontalBlock1 = createHorizontalBlock(48, 30, 7);
        System.arraycopy(horizontalBlock, 0, p, 0, 4);
        System.arraycopy(verticalBlock, 0, p, 4, 5);
        System.arraycopy(horizontalBlock1, 0, p, 7, 7);
    }


    // 创建横向障碍物
    private Point[] createHorizontalBlock(int x, int y, int length) {
        Point[] block = new Point[length];
        for (int i = 0; i < length; i++) {
            block[i] = new Point(x + i, y);
        }
        return block;
    }

    // 创建纵向障碍物
    private Point[] createVerticalBlock(int x, int y, int length) {
        Point[] block = new Point[length];
        for (int i = 0; i < length; i++) {
            block[i] = new Point(x, y + i);
        }
        return block;
    }

    public Point[] getBlock() {
        return p;
    }

    public void paint(Graphics g,int l,int x,int y) {
        for (int i=0;i< p.length;i++) {
            g.fillRect((p[i].x - x) * l, (p[i].y - y) * l, l, l);
        }
        g.fillRect((-1 - x) * l, (-1 - y) * l, 62*l, l);
        g.fillRect((-1 - x) * l, (-1 - y) * l, l, 42*l);
        g.fillRect((-1 - x) * l, (41 - y) * l, 63*l, l);
        g.fillRect((61 - x) * l, (-1 - y) * l, l, 42*l);
    }
}