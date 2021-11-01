import sun.font.TrueTypeFont;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class TankFrame extends Frame {

    Tank myTank = new Tank(200, 400, Dir.DOWN, Group.GOOD, this);  // 面向对象，将Tank抽象出来
    List<Bullet> bullets = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();  // 敌方坦克
    List<Explode> explodes = new ArrayList<>();

    static final int GAME_WIDTH = 800, GAME_HEIGHT = 600;

    public TankFrame() {
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setTitle("Tank War");
        setVisible(true);

        // 添加键盘监听
        this.addKeyListener(new MyKeyListener());

        // 添加窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 双缓冲，解决闪烁问题
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹的数量" + bullets.size(), 10, 60);
        g.drawString("敌军的数量" + tanks.size(), 100, 60);
        g.drawString("爆炸的数量" + explodes.size(), 200, 60);


        g.setColor(c);

        /*
        for (Bullet b: bullets){  // 这种遍历方式在for循环外面删除时，会报错。
            b.paint(g);
        }*/

        // 画出我方坦克
        myTank.paint(g);
        for (int i = 0; i < bullets.size(); i++){
            bullets.get(i).paint(g);
        }

        // 画出敌方坦克
        for (int i = 0; i < tanks.size(); i++){
            tanks.get(i).paint(g);
        }

        // 将爆炸画出来
        for (int i = 0; i < explodes.size(); i++){
            explodes.get(i).paint(g);
        }

        // 子弹与敌方坦克的碰撞检测
        for(int i=0; i<bullets.size(); i++){
            for (int j=0; j<tanks.size(); j++){
                bullets.get(i).collideWith(tanks.get(j));
            }
        }



    }

    // 添加成员内部类（继承KeyAdapter类），处理键盘监听
    class MyKeyListener extends KeyAdapter {

        boolean bL = false;  // 标记“向左键”是否被按下
        boolean bR = false;
        boolean bU = false;
        boolean bD = false;

        // 键被按下时
        @Override
        public void keyPressed(KeyEvent e) {
            // super.keyPressed(e);
            System.out.println("key pressed");
            int key = e.getKeyCode();  // 获取按键信息
            // 根据按键指令，向指定方向移动坦克
            switch(key){
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                default:
                    break;
            }

            setMainTankDir();  // 按键被按下时，改变坦克的方向
        }

        // 按键被抬起时
        @Override
        public void keyReleased(KeyEvent e) {
            // super.keyReleased(e);
            System.out.println("key released");
            int key = e.getKeyCode();  // 获取按键信息
            // 键抬起时，恢复按键为false的状态
            switch(key){
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_CONTROL:
                    myTank.fire();  // 开火
                    break;
                default:
                    break;
            }
            setMainTankDir();  // 松开按键也要改变方向
        }

        public void setMainTankDir(){
            if(!bL && !bR && !bU && !bD){  //四个方向键都没有被按，则tank静止，不移动
                myTank.setMoving(false);
            }else {
                myTank.setMoving(true);
                if(bL) myTank.setDir(Dir.LEFT);
                if(bR) myTank.setDir(Dir.RIGHT);
                if(bU) myTank.setDir(Dir.UP);
                if(bD) myTank.setDir(Dir.DOWN);
            }
        }

    }

}
