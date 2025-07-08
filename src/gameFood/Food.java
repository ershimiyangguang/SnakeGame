package gameFood;

import java.awt.*;
import java.awt.Point;

public class Food {
    private int type;
    private FoodShape food;
    //private Location location;
    private Point foodsPoint;

    public Food(int x) {
        type=x;
        this.init();
    }

    //在frame上画出food
    public void paint(Graphics g, int l, int x, int y,Point[] snake,int length) {
        int X,Y;
        if (type==0) g.setColor(Color.BLUE);
        else g.setColor(Color.magenta);
        for(int i=0;i<food.foods.length;i++) {
            if (food.foods[i].x-x<26&&food.foods[i].y-y<21&&food.foods[i].x-x>-1&&food.foods[i].y-y>-1)
            g.fillRect(food.foods[i].x * l - x*l, food.foods[i].y * l - y*l, l, l);
        }

        Point[] body=snake;
        for(int i=0;i<food.foods.length;i++){
            for(int j=0;j<length;j++){
                foodsPoint=new Point();
                foodsPoint.setLocation(food.foods[i].x,food.foods[i].y);
                if(body[j].equals(foodsPoint)){
                    g.setColor(Color.yellow);
                    X = body[j].x;
                    Y = body[j].y;
                    g.fillRect(X * l - x*l, Y * l - y*l, l, l);
                    if(j==0)
                    {
                        g.setColor(Color.white);
                        g.fillRect(X * l - x*l + l / 4 + 1, Y * l - y*l + l / 4 + 1, l / 2, l / 2);
                        g.setColor(Color.black);
                        g.fillRect(X * l - x*l + l / 3 + 1, Y * l - y*l + l / 3 + 1, l / 4, l / 4);
                    }
                }
            }
        }
    }
    //食物被吃后转换位置
    public void NextLocation(Point[] blocks) {
        while (true) {
            food = new FoodShape();
            Point[] body = blocks;
            boolean b = true;
            for(int i=0;i<food.foods.length;i++){
                for (int j = 0; j < blocks.length; j++) {
                    foodsPoint=new Point();
                    foodsPoint.setLocation(food.foods[i].x,food.foods[i].y);
                    if (body[j].equals(foodsPoint)) {
                        b = false;
                        break;
                    }
                }
            }
            if (b) break;
        }
    }
    //重置食物位置
    public void init() {
        food=new FoodShape();
    }
    //检测蛇是否吃到食物
    public boolean checkEat(Point[] snake,int length) {
        Point[] body = snake;
        if(length<food.foods.length){
            boolean[] Eat=new boolean[length];
            for(int i=0;i<Eat.length;i++){
                Eat[i]=false;
            }
            for(int i=0;i<length;i++){
                for(int j=0;j<food.foods.length;j++){
                    foodsPoint=new Point();
                    foodsPoint.setLocation(food.foods[j].x,food.foods[j].y);
                    if(body[i].equals(foodsPoint)){
                        Eat[i]=true;
                        break;
                    }
                }
            }
            for(int i=0;i<Eat.length;i++)
            {
                if(Eat[i]==false)
                    return false;
            }
            return true;

        }
        else{
            boolean[] Eat=new boolean[food.foods.length];
            for(int i=0;i<Eat.length;i++)
            {
                Eat[i]=false;
            }
            for(int i=0;i<food.foods.length;i++)
            {
                foodsPoint=new Point();
                foodsPoint.setLocation(food.foods[i].x,food.foods[i].y);
                for(int j=0;j<length;j++)
                {
                    if(body[j].equals(foodsPoint))
                    {
                        Eat[i]=true;
                        break;
                    }
                }
            }

            for(int i=0;i<Eat.length;i++)
            {
                if(Eat[i]==false)
                    return false;
            }
            return true;
        }
    }
}