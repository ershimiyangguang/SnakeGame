package gameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

import gameBlock.*;
import gameCamera.*;
import gameEnemy.*;
import gameFood.*;
import gameImage.IconTool;
import gameSnake.*;
import gameScore.*;
import gameSound.*;


public class SnakeGameFrame extends JFrame implements KeyListener {
    //定义区，定义蛇，食物等对象
    public enum GameScene {main, game, pause, end, option}

    private int l = 30, x = 5, y = 30;
    public Sound bgm;
    private Sound attack;
    public boolean isSoundOpen=true;
    public Sound click;
    private Sound crush;
    private Sound eat;
    private Image image = null;
    private Food food0 = new Food(0);
    private Food food1 = new Food(1);
    private Snake snake = new Snake();
    private Blocks blocks = new Blocks();
    private Camera camera = new Camera();
    private Score score = new Score();
    private GameScene game_scene;
    private SoundButton soundButton = new SoundButton(this);
    private MusicButton musicButton = new MusicButton(this);
    private ClearButton clearButton = new ClearButton(this);
    private Play play = new Play(this);
    private Replay replay = new Replay(this);
    private Exit exit = new Exit(this);
    private ToMain to_main = new ToMain(this);
    private Option option = new Option(this);
    private Pause pause = new Pause(this);
    private ToMain2 toMain2 = new ToMain2(this);
    private Exit2 exit2 = new Exit2(this);
    private ScoreBoard2 scoreboard2 = new ScoreBoard2(this);
    private MessageBoard messageBoard = new MessageBoard(this);
    private Background1 background1 = new Background1(this);
    private Background2 background2 = new Background2(this);
    private ScoreBoard scoreboard = new ScoreBoard(this);
    private Background3 background3 = new Background3(this);
    private BackgroundGame backgroundGame = new BackgroundGame(this);
    private BackgroundOption backgroundOption = new BackgroundOption(this);
    private Enemy[] enemy;
    private int t=0,et=0;
    //定时区，定义timer task对象设置定时任务，再定义timer对象定时执行该任务
    private TimerTask task = new TimerTask() {
        //执行任务内容
        @Override
        public void run() {
            t++;
            if (t==95*4) {
                t=0;
                bgm.stop();
                bgm = new Sound(new File("Resource/Sounds/bgm.wav"));
                bgm.play();
            }
            et++;
            if (et==60*4) {
                et=0;
                Enemy.add(1);
            }
            //游戏场景是否为“游戏中”
            if (getGameScene() == SnakeGameFrame.GameScene.game) {
                //蛇移动
                snake.move();
                //摄像头移动
                camera.move(snake.getSnake_body()[0].x,snake.getSnake_body()[0].y);
                for (int i = 0;i < Enemy.getCount();i++) {
                    //敌人攻击
                    int e=enemy[i].attack(snake.getSnake_body(),snake.getLength());
                    if (e==1) {
                        attack.play();
                        snake.setLength(snake.getLength()-1);
                        score.add(-1);
                        messageBoard.setText("<html>被敌人攻击身体，长度-1,分数-2</html>");
                    }
                    else if (e==2) {
                        attack.play();
                        score.add(-1);
                        toEnd();
                    }
                    //敌人移动
                    enemy[i].move(snake.getSnake_body());
                    //敌人攻击
                    e=enemy[i].attack(snake.getSnake_body(),snake.getLength());
                    if (e==1) {
                        attack.play();
                        snake.setLength(snake.getLength()-1);
                        score.add(-1);
                        messageBoard.setText("<html>被敌人攻击身体，长度-1,分数-1</html>");
                    }
                    else if (e==2) {
                        score.add(-1);
                        attack.play();
                        toEnd();
                    }
                }
                Boolean b = false;
                for (int i = 0; i < blocks.getBlock().length; i++) {
                    if (snake.getSnake_body()[0].equals(blocks.getBlock()[i])) {
                        b = true;
                        break;
                    }
                }
                //检测蛇是否撞到墙壁或者自己
                if (!snake.checkAlive() || b == true || snake.getLength()==0) {
                    crush.play();
                    SnakeGameFrame.this.toEnd();
                }
                //是否吃到食物
                if (food0.checkEat(snake.getSnake_body(), snake.getLength())) {
                    eat.play();
                    score.add(1);
                    //食物转移到下一个位置
                    food0.NextLocation(blocks.getBlock());
                    //蛇增长
                    snake.grow();
                    Random r = new Random();
                    int a=r.nextInt(12);
                    if (a==11) {
                        Enemy.add(-1);
                        messageBoard.setText("<html>分数+1,长度+1,敌人数量-1</html>");
                    }
                    else if (a==10||a==9) {
                        snake.grow();
                        messageBoard.setText("<html>分数+1,长度+2</html>");
                    }
                    else if (a==8||a==7) {
                        score.add(1);
                        messageBoard.setText("<html>分数+2,长度+1</html>");
                    }
                    else {
                        messageBoard.setText("<html>分数+1,长度+1</html>");
                    }
                }
                if (food1.checkEat(snake.getSnake_body(), snake.getLength())) {
                    eat.play();
                    //食物转移到下一个位置
                    food1.NextLocation(blocks.getBlock());
                    //蛇增长
                    snake.grow();
                    Random r = new Random();
                    int a=r.nextInt(6);
                    if (a==5) {
                        Enemy.add(1);
                        score.add(4);
                        messageBoard.setText("<html>分数+4,敌人数量+1</html>");
                    }
                    else if (a==3||a==4) {
                        score.add(3);
                        snake.setLength(snake.getLength()-1);
                        messageBoard.setText("<html>分数+3,长度-1</html>");
                    }
                    else if (a==2||a==1) {
                        score.add(3);
                        Enemy.addRange(1);
                        messageBoard.setText("<html>分数+3,敌人侦察范围+1</html>");
                    } else {
                        score.add(2);
                        messageBoard.setText("<html>分数+2</html>");
                    }
                }
                //定位蛇尾部的位置（用于蛇增长时确定新关节的位置，在判断完蛇是否吃到食物后重定向）
                snake.setTail();
                scoreboard.setText(""+score.getScore());

                //重新画图
                update(SnakeGameFrame.this.getGraphics());
            }
            //窗体大小是否变化
            if (isResizable()) {
                //各部分同步变化
                reSize();
            }
        }
    };
    private Timer timer = new Timer();
    //构造函数区，初始化各对象，属性
    public SnakeGameFrame() {
        ImageIcon icon = new ImageIcon("Resource/Pictures/icon.png");
        super.setIconImage(icon.getImage());
        super.setTitle("贪吃蛇");
        super.setSize(941, 664);
        super.setLayout(null);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setFocusable(true);
        super.setLocationRelativeTo(null);
        super.addKeyListener(this);
        //为timer分配任务task，在0毫秒后开始，每300毫秒执行一次
        timer.scheduleAtFixedRate(task,0,250);


        bgm = new Sound(new File("Resource/Sounds/bgm.wav"));
        bgm.play();
        attack = new Sound(new File("Resource/Sounds/attack.wav"));
        eat = new Sound(new File("Resource/Sounds/eat.wav"));
        crush = new Sound(new File("Resource/Sounds/crush.wav"));
        click = new Sound(new File("Resource/Sounds/click.wav"));

        enemy = new Enemy[100];
        for (int i = 0; i < 100; i++) {
            enemy[i] = new Enemy();
        }
        Enemy.reset();
        this.toMain();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    //键盘监听区
    public void keyPressed(KeyEvent e) {
        //x为按下按键的字符的ascii值
        int x = e.getKeyCode();
        if (getGameScene() == SnakeGameFrame.GameScene.game) {
            snake.setDirection(x);
            if (x==32) {
                game_scene = GameScene.pause;
                pause.change();
            }
        } else if (getGameScene() == SnakeGameFrame.GameScene.main) {
            if (x == 32) toGame();
            if (x==27) System.exit(0);
        } else if (getGameScene() == SnakeGameFrame.GameScene.end) {
            if (x == 32) toGame();
            if (x==77) toMain();
        } else if (getGameScene() == SnakeGameFrame.GameScene.pause) {
            if (x==32) game_scene = GameScene.game;
            pause.change();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //绘画区
    @Override
    public void paint(Graphics g) {
        if (game_scene == GameScene.game||game_scene==GameScene.pause) {
            
            //定义一张图片
            image = SnakeGameFrame.this.createImage(24*l, 23*l);
            //定义此图片的画笔g_image
            Graphics g_image = image.getGraphics();
            //使用画笔g清除画面
            g_image.drawImage(backgroundGame.getIcon().getImage(),(-15-camera.getP().x)*l,(-15-camera.getP().y)*l,null);
            blocks.paint(g_image,l,camera.getP().x,camera.getP().y);
            //使用画笔g画出蛇
            snake.paint(g_image, l,camera.getP().x,camera.getP().y);
            //使用画笔g画出食物
            food0.paint(g_image, l,camera.getP().x,camera.getP().y,snake.getSnake_body(), snake.getLength());
            food1.paint(g_image, l,camera.getP().x,camera.getP().y,snake.getSnake_body(), snake.getLength());
            for (int i = 0;i < Enemy.getCount();i++) {
                //使用画笔g画出敌人
                enemy[i].paint(g_image,l,camera.getP().x,camera.getP().y);
            }
            //把图片画到frame上
            g.drawImage(image, x, y, null);
        }
    }
    public GameScene getGameScene() {
        return game_scene;
    }
    public void reSize() {
        l = Math.min((getHeight() - 34) / 21, (getWidth() - 11) / 31);
        x = (getWidth() - 11 - l * 31) / 2 + 5;
        y = (getHeight() - 34 - l * 21) / 2 + 30;
        play.reSize(x, y, l);
        replay.reSize(x, y, l);
        exit.reSize(x,y,l);
        to_main.reSize(x,y,l);
        option.reSize(x,y,l);
        pause.reSize(x,y,l);
        toMain2.reSize(x,y,l);
        exit2.reSize(x,y,l);
        background1.reSize(x,y,l);
        background2.reSize(x,y,l);
        background3.reSize(x,y,l);
        scoreboard.reSize(x,y,l);
        messageBoard.reSize(x,y,l);
        scoreboard2.reSize(x,y,l);
        backgroundOption.reSize(x,y,l);
        soundButton.reSize(x,y,l);
        musicButton.reSize(x,y,l);
        clearButton.reSize(x,y,l);
    }
    public void toMain() {
        to_main.setX(7);
        clearButton.setVisible(false);
        musicButton.setVisible(false);
        background3.setVisible(false);
        scoreboard.setVisible(false);
        soundButton.setVisible(false);
        backgroundOption.setVisible(false);
        scoreboard2.setVisible(false);
        messageBoard.setVisible(false);
        background2.setVisible(false);
        background1.setVisible(true);
        play.setVisible(true);
        exit.setVisible(true);
        replay.setVisible(false);
        to_main.setVisible(false);
        option.setVisible(true);
        pause.setVisible(false);
        toMain2.setVisible(false);
        exit2.setVisible(false);
        game_scene = GameScene.main;
    }

    public void toGame() {
        Enemy.reset();
        score.init();
        pause.init();
        scoreboard2.setVisible(false);
        messageBoard.setVisible(true);
        background2.setVisible(false);
        background3.setVisible(true);
        scoreboard.setVisible(true);
        background1.setVisible(false);
        pause.setVisible(true);
        play.setVisible(false);
        exit.setVisible(false);
        replay.setVisible(false);
        option.setVisible(false);
        to_main.setVisible(false);
        toMain2.setVisible(true);
        exit2.setVisible(true);
        food0.init();
        food1.init();
        snake.init();
        game_scene = GameScene.game;
        setFocusable(true);
    }
    public void setGame_scene(int n) {
        if (n==0) game_scene=GameScene.pause;
        else {
            game_scene=GameScene.game;
            pause.setVisible(false);
            pause.setVisible(true);
            toMain2.setVisible(false);
            toMain2.setVisible(true);
            exit2.setVisible(false);
            exit2.setVisible(true);
            setFocusable(true);
        }
    }
    public void toEnd() {
        getBest();
        if (score.getScore()<best){
            scoreboard2.setText("<html>最终得分:"+score.getScore()+"<br>历史最佳:"+best+"</html>");
        }
        else {
            scoreboard2.setText("<html>最终得分:"+score.getScore()+"<br>新纪录!!!</html>");
        }
        scoreboard2.setVisible(true);
        messageBoard.setVisible(false);
        background3.setVisible(false);
        exit2.setVisible(false);
        toMain2.setVisible(false);
        pause.setVisible(false);
        scoreboard.setVisible(false);
        background2.setVisible(true);
        to_main.setVisible(true);
        replay.setVisible(true);
        exit.setVisible(true);
        to_main.setX(12);
        game_scene = GameScene.end;
    }
    public void toOption() {
        game_scene=GameScene.option;
        clearButton.setVisible(true);
        musicButton.setVisible(true);
        soundButton.setVisible(true);
        to_main.setVisible(true);
        backgroundOption.setVisible(true);
        background1.setVisible(false);
        play.setVisible(false);
        exit.setVisible(false);
        option.setVisible(false);
    }
    public void clear() {
        try{
            RankList list = new RankList(0);
            list.clear();
        }
        catch (IOException e) {

        }
    }
    int best=0;
    public void getBest() {
        try{
            RankList list = new RankList(score.getScore());
            best=list.getScore(1);
        }
        catch (IOException e) {

        }
    }
    public static void main(String[] args) {
        SnakeGameFrame frame = new SnakeGameFrame();
        frame.setVisible(true);
    }

    public abstract static class GameButton extends JButton implements ActionListener {
        private SnakeGameFrame frame;
        private int width;
        private int height;
        private int x;
        private int y;

        public GameButton(SnakeGameFrame frame) {
            super.setContentAreaFilled(false);
            super.setBorderPainted(false);
            super.setVisible(false);
            super.setFocusPainted(false);
            this.frame = frame;
            frame.getContentPane().add(this);
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public SnakeGameFrame getFrame() {
            return frame;
        }

        public void reSize(int x, int y, int l) {
            super.setBounds(x + this.x * l, y +  this.y * l, width * l, height * l);
            super.setFont(new Font("宋体",0,l*3/5));
        }
        @Override
        public abstract void actionPerformed(ActionEvent e);

    }

    public abstract static class GameLabel extends JLabel{
        private int width;
        private int height;
        private int x;
        private int y;
        public GameLabel(SnakeGameFrame frame) {
            super.setVisible(false);
            frame.getContentPane().add(this);
        }
        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
        public void reSize(int x, int y, int l) {
            super.setBounds(x+this.x * l-5, y+this.y * l-30, width * l, height * l);
        }
    }

    public static class Background1 extends GameLabel {
        private ImageIcon icon;
        public Background1(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(31);
            super.setHeight(21);
            setX(0);
            super.setY(0);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/background1.jpg",false);
            super.setIcon(icon);
        }

    }

    public static class Background2 extends GameLabel {
        private ImageIcon icon;
        public Background2(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(31);
            super.setHeight(21);
            setX(0);
            super.setY(0);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/background2.jpg",false);
            super.setIcon(icon);
        }
    }

    public static class Background3 extends GameLabel {
        private ImageIcon icon;
        public Background3(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(6);
            super.setHeight(21);
            setX(25);
            super.setY(0);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/background3.jpg",false);
            super.setIcon(icon);
        }
    }

    public static class BackgroundGame extends GameLabel {
        private ImageIcon icon;
        public BackgroundGame(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(61);
            super.setHeight(41);
            super.setX(0);
            super.setY(0);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/backgroundgame.jpg",false);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/backgroundgame.jpg",false);
            setIcon(icon);
        }
        public ImageIcon getIcon() {
            return icon;
        }
    }

    public static class BackgroundOption extends GameLabel {
        private ImageIcon icon;
        public BackgroundOption(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(31);
            super.setHeight(21);
            setX(0);
            super.setY(0);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/backgroundoption.jpg",false);
            super.setIcon(icon);
        }
    }

    public static class ClearButton extends GameButton {
        public ClearButton(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(6);
            super.setHeight(2);
            setX(7);
            super.setY(11);
            super.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().clear();
        }

        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/clear.png",false);
            super.setIcon(icon);
        }
    }

    public static class Exit extends GameButton {
        public Exit(SnakeGameFrame frame) {
            super(frame);
            super.setText("退出");
            super.setWidth(6);
            super.setHeight(2);
            setX(12);
            super.setY(16);
            this.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            System.exit(0);
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/exit.png",false);
            super.setIcon(icon);
        }
    }

    public static class Exit2 extends GameButton {
        public Exit2(SnakeGameFrame frame) {
            super(frame);
            super.setText("退出");
            super.setWidth(4);
            super.setHeight(2);
            super.setX(26);
            super.setY(11);
            super.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            System.exit(0);
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/exit2.png",false);
            super.setIcon(icon);
        }
    }
    public static class MessageBoard extends GameLabel {
        public MessageBoard (SnakeGameFrame frame) {
            super(frame);
            super.setText("");
            super.setWidth(4);
            super.setHeight(4);
            super.setX(26);
            super.setY(16);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            super.setFont(new Font("宋体",0,l*3/5));
        }

    }

    public static class MusicButton extends GameButton {
        public MusicButton(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(6);
            super.setHeight(2);
            setX(7);
            super.setY(8);
            n=0;
            this.addActionListener(this);
        }
        int n=0;
        private ImageIcon icon1;
        private ImageIcon icon2;

        public void reSize(int x, int y, int l) {
            super.reSize(x, y, l);
            icon1 = IconTool.createAutoAdjustIcon("Resource/Pictures/musicopen.png", false);
            icon2 = IconTool.createAutoAdjustIcon("Resource/Pictures/musicclose.png", false);
            if (n==0) setIcon(icon1);
            else setIcon(icon2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            if (n==0) n=1;
            else n=0;
            if (n==0) {
                setIcon(icon1);
                getFrame().bgm=new Sound(new File("Resource/Sounds/bgm.wav"));
                getFrame().bgm.play();
            }
            else {
                setIcon(icon2);
                getFrame().bgm.stop();
            }
        }
    }

    public static class Option extends GameButton {
        public Option(SnakeGameFrame frame) {
            super(frame);
            super.setText("设置");
            super.setWidth(6);
            super.setHeight(2);
            setX(12);
            super.setY(14);
            this.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().toOption();
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/option.png",false);
            super.setIcon(icon);
        }
    }

    public static class Pause extends GameButton {
        public Pause(SnakeGameFrame frame) {
            super(frame);
            super.setText("按空格暂停");
            super.setWidth(4);
            super.setHeight(2);
            super.setX(26);
            super.setY(5);
            n=0;
            super.addActionListener(this);
        }
        int n=0;
        private ImageIcon icon1;
        private ImageIcon icon2;

        public void reSize(int x, int y, int l) {
            super.reSize(x, y, l);
            icon2 = IconTool.createAutoAdjustIcon("Resource/Pictures/pause.png", false);
            icon1 = IconTool.createAutoAdjustIcon("Resource/Pictures/continue.png", false);
            if (n==0) setIcon(icon1);
            else setIcon(icon2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().setGame_scene(n);
            change();
        }

        public void change() {
            if (n==0) n=1;
            else n=0;
        }
        public void init() {
            n=0;
        }
    }

    public static class Play extends GameButton {
        public Play(SnakeGameFrame frame) {
            super(frame);
            super.setText("开始游戏");
            super.setWidth(6);
            super.setHeight(2);
            setX(12);
            super.setY(12);
            super.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().toGame();
        }

        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/start.png",false);
            super.setIcon(icon);
        }
    }

    public static class Replay extends GameButton {
        public Replay(SnakeGameFrame frame) {
            super(frame);
            super.setText("重新开始");
            super.setWidth(6);
            super.setHeight(2);
            setX(12);
            super.setY(12);
            super.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().toGame();
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/replay.png",false);
            super.setIcon(icon);
        }
    }

    public static class ScoreBoard extends GameLabel {
        public ScoreBoard (SnakeGameFrame frame) {
            super(frame);
            super.setText("");
            super.setWidth(4);
            super.setHeight(4);
            super.setX(26);
            super.setY(2);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            Font font = new Font("黑体",0,l*8/3);
            setFont(font);
        }

    }

    public static class ScoreBoard2 extends GameLabel {
        private ImageIcon icon;
        public ScoreBoard2 (SnakeGameFrame frame) {
            super(frame);
            super.setText("");
            super.setWidth(10);
            super.setHeight(5);
            super.setX(10);
            super.setY(7);
        }
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            Font font = new Font("黑体",0,l*3/2);
            setFont(font);
        }
    }

    public static class SoundButton extends GameButton {
        public SoundButton(SnakeGameFrame frame) {
            super(frame);
            super.setWidth(6);
            super.setHeight(2);
            setX(7);
            super.setY(5);
            n=0;
            this.addActionListener(this);
        }
        int n=0;
        private ImageIcon icon1;
        private ImageIcon icon2;

        public void reSize(int x, int y, int l) {
            super.reSize(x, y, l);
            icon1 = IconTool.createAutoAdjustIcon("Resource/Pictures/soundopen.png", false);
            icon2 = IconTool.createAutoAdjustIcon("Resource/Pictures/soundclose.png", false);
            if (n==0) setIcon(icon1);
            else setIcon(icon2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            if (n==0) n=1;
            else n=0;
            if (n==0) setIcon(icon1);
            else setIcon(icon2);
            if (n==0) getFrame().isSoundOpen=true;
            else getFrame().isSoundOpen=false;
        }
    }

    public static class ToMain extends GameButton {
        public ToMain(SnakeGameFrame frame) {
            super(frame);
            super.setText("返回主界面");
            super.setWidth(6);
            super.setHeight(2);
            setX(12);
            super.setY(14);
            this.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().toMain();
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/tomain.png",false);
            super.setIcon(icon);
        }

        @Override
        public void setX(int x) {
            super.setX(x);
        }
    }

    public static class ToMain2 extends GameButton {
        public ToMain2(SnakeGameFrame frame) {
            super(frame);
            super.setText("主界面");
            super.setWidth(4);
            super.setHeight(2);
            super.setX(26);
            super.setY(8);
            super.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getFrame().isSoundOpen==true) {
                getFrame().click.play();
            }
            getFrame().toMain();
        }
        private ImageIcon icon;
        public void reSize(int x, int y, int l) {
            super.reSize(x,y,l);
            icon = IconTool.createAutoAdjustIcon("Resource/Pictures/tomain2.png",false);
            super.setIcon(icon);
        }
    }
}

