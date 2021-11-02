public class FourDirFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank tank) {
        int bX = tank.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = tank.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        // tf.bullets.add(new Bullet(this.x, this.y, this.dir,this.tf));  // 将该坦克创建的子弹交给其所属的窗口
        // 定义朝四个方向开火
        Dir[] dirs = Dir.values();
        for (Dir dir : dirs){
            new Bullet(bX, bY, dir, tank.group, tank.tf);
        }
    }
}
