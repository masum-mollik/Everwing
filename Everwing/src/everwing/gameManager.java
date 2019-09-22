package everwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Masum Mollik & Dipto Mondal
 */
public class gameManager implements KeyListener{
    private Player player;
    private boss bs;
    private miniBossOne miniBossOne;
    private miniBossTwo miniBossTwo;
    
    public static ArrayList<Bullet> bullet;
    private ArrayList<Enemy> enemies;
    private long current;
    private long delay;
    private int health;
    private int score;
    private boolean start;
    private int bossHealth;
    public static boolean bossOneDead = false;
    private int miniBossHealthOne;
    private int miniBossHealthTwo;
    public static boolean miniBossOneDead = false;
    public static boolean miniBossTwoDead = false;
    
    
    public gameManager(){
        
    }
    public void init(){
        Display.frame.addKeyListener(this);
        player = new Player((gameSetup.gameWidth/2)+50,(gameSetup.gameheight-30)+50);
        player.init();
        bullet = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        bs = new boss((gameSetup.gameWidth/2)+50, 50);
       current = System.nanoTime();
        delay = 2000;
        health = player.getHealth();
        score  = 0;
        bossHealth = bs.getHealth();
        
    }
    public void render(Graphics g){
        if(start){
//        g.fillRect(0, 0, 50, 80);
        player.render(g);
        for(int i = 0; i < bullet.size(); i++) {
            bullet.get(i).render(g);
        }
        for (int i = 0; i < bullet.size(); i++) {
            if(bullet.get(i).getY()<=50){
                bullet.remove(i);
                i--;
            }
        }
        
        for (int i = 0; i < enemies.size(); i++) {
            if(!(enemies.get(i).getX()<= 60|| enemies.get(i).getX()>=392 || enemies.get(i).getY()>= 450-40)){          
               if(enemies.get(i).getY()>=50){
                    enemies.get(i).render(g);
               }
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            int ex = enemies.get(i).getX();
            int ey = enemies.get(i).getY();
            
            int px = player.getX();
                int py = player.getY();
                if(px < ex + 50 && px + 60 > ex && 
                   py < ey + 50 && py + 60 > ey && ex <= 394 && ex >= 62){
                    enemies.remove(i);
                    i--;
                    health--;
                    System.out.println(health);
                    System.out.println(px);
                    System.out.println(ex);
                    if(health <= 0){
                        enemies.removeAll(enemies);
                        player.setHealth(0);
                    }
                }
                
            //all collisions with bullets start
            for (int j = 0; j < bullet.size(); j++) {
                //collision of enemy and playe
                /*if(r1.x < r2.x+width &&
                 r1.x+width > r2.x &&
                 r1.y < r2.y + width &&
                r1.y + width > r2.y
                */
                // r1 = enemies / player
                //r2 = bullet   / enemies
                
                
                
                //collsion between enemies and bullets starts
                int bx = bullet.get(j).getX();
                int by = bullet.get(j).getY();                
                if(ex < bx + 8 && ex + 50 > bx && ey < by + 8 && ey + 50 > by){
                    enemies.remove(i);
                    i--;
                    bullet.remove(j);
                    j--;
                    score += 5;
                }
                //collsion between enemies and bullets ends
                
                //collision between boss and bullets starts
                if(bossHealth>0){
                    int bossX = bs.getX();
                    int bossY = bs.getY();
                    if(bossX < bx + 8 && bossX + 80 > bx && bossY < by + 8 && bossY + 80 > by){
                    bullet.remove(j);
                    j--;
                    bossHealth--;
                    bs.setHealth(bossHealth);
                    System.out.println("BOSS HEALTH REMAINING:    "+bossHealth);
                    
                }
                }
              
                //collsion between boss and bullets ends
                
                
            }
            
            if(bossHealth <= 0) bossOneDead = true;
//            System.out.println("boss one dead :"+bossOneDead);
            //all collsions with bullets end
            g.setColor(Color.CYAN);
            g.setFont(new Font("arial", Font.ITALIC, 40));
            g.drawString(""+score,70,50);
            
        }
        //boss generate
        if((score >= 10) && bossHealth > 0){
                bs.render(g);
            }
        //boss && player collision starts
        int bossX = bs.getX();
        int bossY = bs.getY();
        int px = player.getX();
        int py = player.getY();
        if(px < bossX + 70 && px + 60 > bossX && py < bossY + 70 && py + 60 > bossY){
                        health--;
                        bs.setY(85);
                        System.out.println("boss collide,,,          health :"+health);
                        if(health <= 0){
                        enemies.removeAll(enemies);
                        player.setHealth(0);
                    }
        }
        //boss && player collision ends
        }
        else{
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("arial",Font.PLAIN,33));
            g.drawString("Press enter start", 70, gameSetup.gameheight/2 +50);
            
        }
    }
    public void tick(){
        if(start){
            if(score>= 10 && bossHealth > 0){
                bs.tick();
            }
            player.tick();
        
        
        for(int i = 0; i < bullet.size(); i++) {
            bullet.get(i).tick();
            
        }
        
        //enemy
        if(bossOneDead == true){
            delay = 1000;
        }
        long breaks = (System.nanoTime() - current)/1000000;
        if(breaks > delay){
            for(int i = 0 ; i < 2 ; i++){
            Random rand = new Random();
            int randx  = rand.nextInt(450);
            int randy = rand.nextInt(450);
            if(health > 0){
            enemies.add(new Enemy(randx, -randy));
            }
        }
            current = System.nanoTime();
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).tick();
        }
    }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int source = ke.getKeyCode();
        if(source == KeyEvent.VK_ENTER){
            start = true;
            init();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int source = ke.getKeyCode();
        if(source == KeyEvent.VK_ENTER){
            start = true;
            init();
        }
    }
    public boolean getStart(){
        return start;
    }
    public void setStart(boolean start){
        this.start = start;
    }
    public int getScore(){
        return  score;
    }
//    public boolean getBossOneDead(){
//        System.out.println(bossOneDead);
//        return bossOneDead;
//    }
//   public void setBossOneDead(boolean bossOneDead){
//       this.bossOneDead = bossOneDead;
//   }
   
}
