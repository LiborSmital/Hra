
package liborsmital.hrakreslenidojpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Enemy {
    private int posunDoLeva, posunDoPrava ; 
    private Rectangle obdelnik;
    private final int HOR_RYCHLOST = 4;
    private Dimension velikostOkna;

    public Enemy(Dimension velikostOkna) {
        this.velikostOkna = velikostOkna;
        obdelnik = new Rectangle(15, 15);    
    }
    
    
    public void paintEnemy(Graphics g){
        if (obdelnik.x > -40 || obdelnik.x < velikostOkna.width + 40) {
            
        g.setColor(Color.red);
        g.fillRect(obdelnik.x, obdelnik.y, obdelnik.width, obdelnik.height);
        g.setColor(Color.ORANGE);
        g.drawRect(obdelnik.x, obdelnik.y, obdelnik.width, obdelnik.height);
        }
        
    }
    
    
}
