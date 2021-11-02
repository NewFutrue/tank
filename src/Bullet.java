import java.awt.*;

public class Bullet {

    private int x, y;
    private Dir dir = Dir.DOWN;
    private static final int SPEED = 10;

    public static int WIDTH = ResourceMgr.bulletL.getWidth();
    public static int HEIGHT = ResourceMgr.bulletL.getHeight();

    Rectangle rectangle = new Rectangle();

    private boolean live = true;  // 判断子弹是否存活(飞出画面、撞到敌方坦克)
    private TankFrame tf = null;
    private Group group = Group.BAD;


    public Bullet(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;

        rectangle.x = this.x;
        rectangle.y = this.y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        tf.bullets.add(this);  // 每次创建一个子弹，就将该子弹添加到tf中
    }

    public void paint(Graphics g){
        if(!live){
            this.tf.bullets.remove(this);  // 将这颗子弹移除
        }

        Color c = g.getColor();
/*        g.setColor(Color.RED);
        g.fillOval(x, y, WIGTH, HEIGHT);
        g.setColor(c);  // 再恢复画笔的颜色*/

        switch (dir){
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }

        move();
    }

    public void move(){

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

        // 使rectangle与子弹一起移动
        rectangle.x = x;
        rectangle.y = y;

        if(x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT){
            live = false;  // 子弹已经移动到窗口外
        }
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

    public void collideWith(Tank tank) {
        if(this.group == tank.getGroup()) return;  // 不打友军

        // 小bug TODO: 用一个rect来记录子弹的位置，而不是每次都new（这种方法：每次需要重画很多Rectangle）
        //Rectangle rect1 = new Rectangle(this.x, this.y, WIDTH, HEIGHT);  // 矩形
        //Rectangle rect2 = new Rectangle(tank.getX(), tank.getY(), Tank.WIDTH, Tank.HEIGHT);  // 矩形

        if(this.rectangle.intersects(tank.rectangle)){  // intersects()是判断图形相交的方法
            tank.die();
            this.die();

            // 计算爆炸的位置, 使爆炸位于坦克的中心
            int eX = tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
            int eY = tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
            tf.explodes.add(new Explode(eX,eY,tf));

        }
    }

    private void die() {
        this.live = false;
    }
}
