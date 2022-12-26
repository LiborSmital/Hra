package liborsmital.hrakreslenidojpanel;

import javax.swing.JFrame;

/**
 *
 * @author Libor
 */
public class HraKresleniDoJPanel extends JFrame {
        

    public HraKresleniDoJPanel() {
        
        
        this.setTitle("Bludiště");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLocationRelativeTo(this);
        
        Panel panel = new Panel();
        this.add(panel);
        this.pack();
        
        
        
        
        
    }

    public static void main(String[] args) {
        HraKresleniDoJPanel okno = new HraKresleniDoJPanel();
        okno.setVisible(true);
    }
}
