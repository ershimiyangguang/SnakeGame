package entity;

import java.awt.*;
import java.util.Random;

public class Enemy {
    private Point location=new Point();//位置
    public enum Enemystate{attack,wait}   //敌人的状态
    public Enemystate enemystate;
    int distance;  //逃跑的方向
    static int range;
    static int count;
    int escape_direction;
    int i;
    public static void add(int x) {
        count=count+x;
    }
    public static int getCount() {
        return count;
    }
    public static void addRange(int x) {
        range+=x;
    }
    public Enemy() {
        init();
    }
    public static void reset() {
        count=4;
    }
    //重置敌人位置与伤害
    public void init() {
        range=6;
        Random r =new Random();
        int k=r.nextInt(4);
        if(k==0) {
            location.setLocation(1,r.nextInt(41));
            escape_direction=3;
        }
        else if(k==1) {
            location.setLocation(60,r.nextInt(41));
            escape_direction=1;
        }
        else if(k==2) {
            location.setLocation(r.nextInt(61),1);
            escape_direction=4;
        }
        else {
            location.setLocation(r.nextInt(61),40);
            escape_direction=2;
        }
        //随机生成敌人的位置
        enemystate=Enemystate.wait;
    }
    //攻击蛇
    public int attack(Point[] snake,int length) {
        if(enemystate==Enemystate.attack) {    //当敌人状态为攻击时，判断敌人是否攻击到蛇//
            for (int i = 0; i < length; i++) {
                if (location.equals(snake[i])) {
                    ;//如果攻击到蛇，则蛇的长度-1，并且敌人的状态改为逃跑
                    if(i==0){
                         return 2;
                    }
                    else {
                        init();
                        return 1;
                    }
                }
            }

        }
        return 0;
    }
    //移动
    public void move(Point[] snake) {  //敌人移动
        Point location1[];
        location1 = snake;
        Random r = new Random();
        distance=Math.abs(location.y-location1[0].y)+Math.abs(location.x-location1[0].x);
        if(distance<=range && enemystate==Enemystate.wait){
            enemystate=Enemystate.attack;
        }
        else if (distance>=range*3/2 && enemystate==Enemystate.attack) {
            enemystate=Enemystate.wait;
        }

        if (enemystate ==Enemystate. attack) {//如果敌人状态为攻击，则敌人根据蛇的位置自动改变自己的位
            int x = r.nextInt(2);
            if (location1[0].getX() > location.getX() && location1[0].getY() == location.getY()) {
                location.setLocation(location.x + 1, location.y);
            } else if (location1[0].getX() < location.getX() && location1[0].getY() == location.getY()) {
                location.setLocation(location.x - 1, location.y);
            } else if (location1[0].getX() == location.getX() && location1[0].getY() > location.getY()) {
                location.setLocation(location.x, location.y + 1);
            } else if (location1[0].getX() == location.getX() && location1[0].getY() < location.getY()) {
                location.setLocation(location.x, location.y - 1);
            } else if (location1[0].getX() > location.getX() && location1[0].getY() > location.getY()) {
                if (x == 0) {
                    location.setLocation(location.x + 1, location.y);
                } else {
                    location.setLocation(location.x, location.y + 1);
                }
            } else if (location1[0].getX() > location.getX() && location1[0].getY() < location.getY()) {
                if (x == 0) {
                    location.setLocation(location.x + 1, location.y);
                } else {
                    location.setLocation(location.x, location.y - 1);
                }
            } else if (location1[0].getX() < location.getX() && location1[0].getY() > location.getY()) {
                if (x == 0) {
                    location.setLocation(location.x - 1, location.y);
                } else {
                    location.setLocation(location.x, location.y + 1);
                }
            } else if (location1[0].getX() < location.getX() && location1[0].getY() < location.getY()) {
                if (x == 0) {
                    location.setLocation(location.x - 1, location.y);
                } else {
                    location.setLocation(location.x, location.y - 1);
                }
            }
        }
        else if(enemystate==Enemystate.wait){
            if (location.x==-1){
                escape_direction=3;
            }else if (location.y==-1){
                escape_direction=4;
            }else if (location.x==61){
                escape_direction=1;
            }else if (location.y==41){
                escape_direction=2;
            }
            if(i==30){
                int x = r.nextInt(3);
                escape_direction=(escape_direction+x)%4+1;
            }
            i++;
            if (escape_direction == 1) {
                location.setLocation(location.getX() - 1,location.y);
            }
            else if (escape_direction == 3) {
                location.setLocation(location.getX() + 1,location.y);
            }
            else if (escape_direction == 2) {
                location.setLocation(location.x,location.getY() - 1);
            }
            else if(escape_direction==4){
                location.setLocation(location.x,location.getY() + 1);
            }
        }
    }
    public void paint(Graphics gImage,int l,int x, int y) {
        if (enemystate == Enemystate.attack) {
            gImage.setColor(Color.ORANGE);
            gImage.fillRect((location.x- x)* l, (location.y-y) * l, l, l);

        } else {
            gImage.setColor(Color.CYAN);
        }
        int sx = location.x;
        int sy = location.y;
        if (sx-x<26&&sy-y<21&&sx-x>-1&&sy-y>-1) {
            gImage.fillRect((location.x-x) * l, (location.y-y) * l, l, l);
            gImage.setColor(Color.white);
            gImage.fillRect((sx-x) * l + l / 4 + 1, (sy-y) * l + l / 4 + 1, l / 2, l / 2);
            gImage.setColor(Color.black);
            gImage.fillRect((sx-x) * l + l / 3 + 1, (sy - y) * l  + l / 3 + 1, l / 4, l / 4);
        }
    }
}

