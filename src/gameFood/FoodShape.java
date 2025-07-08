package gameFood;
import java.util.Random;
import java.awt.Point;

public class FoodShape {
    public Point []foods;
    public  int Length;
    public int pattern;
    public FoodShape(){
        this.init();
    }

    public void init(){
        Random R=new Random();
        Length=R.nextInt(4)+1;
        if(Length==1) {
            foods=new Point[1];
            foods[0]=new Point(R.nextInt(60),R.nextInt(40));
        }
        else if(Length==2){
            foods=new Point[2];
            pattern=R.nextInt(2);
            if(pattern==0){
                foods[0]=new Point(R.nextInt(60),R.nextInt(40));
                foods[1]=new Point(foods[0].x+1,foods[0].y);
            }
            else if(pattern==1){
                foods[0]=new Point(R.nextInt(60),R.nextInt(40));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
            }
        }
        else if(Length==3){
            foods=new Point[3];
            pattern=R.nextInt(4);
            if(pattern==0){
                foods[0]=new Point(R.nextInt(59),R.nextInt(39));
                foods[1]=new Point(foods[0].x+1,foods[0].y);
                foods[2]=new Point(foods[1].x+1,foods[1].y);
            }
            else if(pattern==1){
                foods[0]=new Point(R.nextInt(59),R.nextInt(39));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
                foods[2]=new Point(foods[1].x,foods[1].y+1);
            }
            else if(pattern==2){
                foods[0]=new Point(R.nextInt(59),R.nextInt(39));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
                foods[2]=new Point(foods[1].x+1,foods[1].y);
            }
            else if(pattern==3){
                foods[0]=new Point(R.nextInt(59),R.nextInt(39));
                foods[1]=new Point(foods[0].x+1,foods[0].y);
                foods[2]=new Point(foods[1].x,foods[1].y+1);
            }
        }
        else if(Length==4){
            foods=new Point[4];
            pattern=R.nextInt(5);
            if(pattern==0){
                foods[0]=new Point(R.nextInt(57),R.nextInt(37));
                foods[1]=new Point(foods[0].x+1,foods[0].y);
                foods[2]=new Point(foods[1].x+1,foods[1].y);
                foods[3]=new Point(foods[2].x+1,foods[2].y);
            }
            else if(pattern==1){
                foods[0]=new Point(R.nextInt(57),R.nextInt(37));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
                foods[2]=new Point(foods[1].x,foods[1].y+1);
                foods[3]=new Point(foods[2].x,foods[2].y+1);
            }
            else if(pattern==2){
                foods[0]=new Point(R.nextInt(57),R.nextInt(37));
                foods[1]=new Point(foods[0].x+1,foods[0].y);
                foods[2]=new Point(foods[1].x+1,foods[1].y);
                foods[3]=new Point(foods[2].x,foods[2].y+1);
            }
            else if(pattern==3){
                foods[0]=new Point(R.nextInt(57),R.nextInt(37));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
                foods[2]=new Point(foods[1].x,foods[1].y+1);
                foods[3]=new Point(foods[2].x+1,foods[2].y);
            }
            else if(pattern==4){
                foods[0]=new Point(R.nextInt(57),R.nextInt(37));
                foods[1]=new Point(foods[0].x,foods[0].y+1);
                foods[2]=new Point(foods[1].x+1,foods[1].y);
                foods[3]=new Point(foods[2].x,foods[2].y-1);
            }
        }

    }
}
