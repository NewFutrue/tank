public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank tank) {
        int bX = tank.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = tank.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        // tf.bullets.add(new Bullet(this.x, this.y, this.dir,this.tf));  // 将该坦克创建的子弹交给其所属的窗口
        new Bullet(bX, bY, tank.dir, tank.group, tank.tf);
    }
}
