import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tank {

    int x, y;
    Dir dir = Dir.DOWN;
    private static final int SPEED = 5;

    public static int WIDTH = ResourceMgr.goodTankL.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankL.getHeight();

    Rectangle rectangle = new Rectangle();


    public boolean moving = true;  // 初始时，tank静止
    TankFrame tf = null;
    private boolean live = true;
    Group group = Group.BAD;

    // 定义开火的策略
    FireStrategy fs;  // 默认开火策略（已修改为在构造方法中根据不同类型的坦克指定不同类型的策略）

    private Random random = new Random();

    public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;  // 构造该坦克时，指定是哪个窗口创建的该坦克

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        if (group == Group.GOOD){  // 如果是好坦克，则定义朝四面开火的策略（通过配置文件指定）
            String goodFSName = (String) PropertyMgr.get("goodFS");
            try {
                fs = (FireStrategy) Class.forName(goodFSName).newInstance();  // 通过Class实例化对象。

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else {  // 否则如果是坏坦克，则采用默认开火策略
            String badFSName = (String) PropertyMgr.get("badFS");
            try {
                fs = (FireStrategy) Class.forName(badFSName).newInstance();  // 通过Class实例化对象。
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public void paint(Graphics g){
        if (!live) tf.tanks.remove(this);
        //Color c = g.getColor();
        // g.setColor(Color.YELLOW);
        // g.fillRect(x, y, 50, 50);  // 画出tank
        // g.setColor(c);  // 再恢复画笔的颜色*/
        switch (dir){
            case LEFT:
                // g.drawImage(ResourceMgr.tankL, x, y, null);
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
                break;
            case RIGHT:
                // g.drawImage(ResourceMgr.tankR, x, y, null);
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
                break;
            case UP:
                // g.drawImage(ResourceMgr.tankU, x, y, null);
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU: ResourceMgr.badTankU, x, y, null);

                break;
            case DOWN:
                // g.drawImage(ResourceMgr.tankD, x, y, null);
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);

                break;
        }
        move();

    }

    public void move(){
        if(!moving) return;

        switch(dir){  // 根据方向，让坦克移动
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }


        // move完换方向
        // randomDir();

        // 让敌军坦克能够打出子弹。随机开火
        if(this.group == Group.BAD && random.nextInt(100) > 92)
            this.fire();

        // 敌方坦克随机换移动方向
        if(this.group == Group.BAD && random.nextInt(100) > 95)
            randomDir();

        // 边界检测
        boundsCheck();

        // 使rectangle与坦克一起移动(在边界检测以后)
        rectangle.x = x;
        rectangle.y = y;


    }

    private void boundsCheck(){
        if (this.x < 0){
            this.x = 0;
            randomDir();
        }
        if(this.x > TankFrame.GAME_WIDTH - Tank.WIDTH){
            this.x = TankFrame.GAME_WIDTH - Tank.WIDTH;
            randomDir();
        }
        if(this.y < 30){
            this.y = 30;
            randomDir();
        }
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT){
            this.y = TankFrame.GAME_HEIGHT - Tank.HEIGHT;
            randomDir();

        }
    }

    private void randomDir(){
        this.dir = Dir.values()[random.nextInt(4)];  // 从方向枚举类的Dir的四个值中随机选择一个
    }

    public void fire(){
        fs.fire(this);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public static int getSPEED() {
        return SPEED;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void die() {
        this.live = false;
    }
}
