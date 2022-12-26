package liborsmital.hrakreslenidojpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Libor
 */
public class Panel extends JPanel {

    Dimension rozmerPanelu;
    Prekazky prekazky;
    private final Hero hero;
    private Ladit ladit;

    public Panel() {

        rozmerPanelu = new Dimension(800, 600);
        prekazky = new Prekazky();
        ladit = new Ladit();

        this.setPreferredSize(rozmerPanelu);
        this.setBackground(Color.WHITE);

        PosluchacCasovace posluchacCasovace = new PosluchacCasovace();
        Timer casovac = new Timer(10, posluchacCasovace);
        casovac.start();

        PoslochacKlavesnice klavesnice = new PoslochacKlavesnice();
        this.addKeyListener(klavesnice);
        this.setFocusable(true);
        hero = new Hero(rozmerPanelu, ladit);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        prekazky.setOfset(-hero.getOfset(prekazky.getPocetPrekazekVRadku(),
                new Dimension(this.getWidth(), this.getHeight())));
        hero.vyhodnoceniKolize(prekazky);
        kresleniPrekazek(prekazky, g);

        hero.paintHero(g);
        ladit.paint(g);

    }

    public void go() {
        this.repaint();
    }

    private class PoslochacKlavesnice implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int i = e.getKeyCode();

            //System.out.println(i);
            switch (i) {
                case 37 -> //doleva
                    hero.setKlavesa(PrikazKlavesnice.SipkaL, '+');
                case 39 -> //doprava
                    hero.setKlavesa(PrikazKlavesnice.SipkaP, '+');
                case 38 -> //nahoru
                    hero.setKlavesa(PrikazKlavesnice.SipkaN, '+');
                case 40 -> //dolu
                    hero.setKlavesa(PrikazKlavesnice.SipkaD, '+');
                case 32 -> //mezernik
                    hero.setKlavesa(PrikazKlavesnice.Mezernik, '+');
                default -> {
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

            int i = e.getKeyCode();
            
            //System.out.println(i);
            switch (i) {
                case 37 -> //doleva
                    hero.setKlavesa(PrikazKlavesnice.SipkaL, '-');
                case 39 -> //doprava
                    hero.setKlavesa(PrikazKlavesnice.SipkaP, '-');
                case 38 -> //nahoru
                    hero.setKlavesa(PrikazKlavesnice.SipkaN, '-');
                case 40 -> //dolu
                    hero.setKlavesa(PrikazKlavesnice.SipkaD, '-');
                case 32 -> //mezernik
                    hero.setKlavesa(PrikazKlavesnice.Mezernik, '-');
                case 27 -> //Esc  reset hry
                    hero.reset();
                case 76 -> {// zapne lazeni
                    ladit.setLadit();
                    hero.setLadit();
                }
                default -> {
                }
            }
        }

    }

    private class PosluchacCasovace implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            go();
        }

    }

    private void kresleniPrekazek(Prekazky pre, Graphics g) {
        int pocetVykreslenzch = 0;
        for (int i = 0; i < pre.getPocetPrekazek(); i++) {
            Prekazka p = pre.getPrakazka(i);
            Rectangle rec = p.getObdelnik();
            g.setColor(p.getBarvaVyplne());
            if (rec.x > -40 && rec.x < this.rozmerPanelu.width + 40) {
                //if (true) {    
                pocetVykreslenzch++;
                g.fillRect(rec.x, rec.y, rec.width, rec.height);
                g.setColor(p.getBarvaRamu());
                g.drawRect(rec.x, rec.y, rec.width, rec.height);
                g.setColor(Color.red);
                if (hero.isLadit()) {
                    
                    if (p.getKolize().contains(Kolize.Dole)) { // kolize pod Hero

                        g.drawLine(rec.x,
                                rec.y,
                                rec.x + rec.width,
                                rec.y);

                    }
                    if (p.getKolize().contains(Kolize.Nahore)) { // kolize nad Hero

                        g.drawLine(rec.x,
                                rec.y + rec.height,
                                rec.x + rec.width,
                                rec.y + rec.height);
                    }
                    if (p.getKolize().contains(Kolize.Levo)) { // kolize vlevo od Hero

                        g.drawLine(rec.x + rec.width,
                                rec.y,
                                rec.x + rec.width,
                                rec.y + rec.height);
                    }
                    if (p.getKolize().contains(Kolize.Pravo)) {  /// kolize vpravo od Hero

                        g.drawLine(rec.x,
                                rec.y,
                                rec.x,
                                rec.y + rec.height);
                    }
                    if (!p.getKolize().isEmpty()) {

                        g.drawString(String.valueOf(p.getKolize().size()), (int) p.getObdelnik().getCenterX(), (int) p.getObdelnik().getCenterY());
                    }
                }
            }

        }
        if (hero.isLadit()) {
            g.setColor(Color.red);
            ladit.add("Počet vykreslených překážek", String.valueOf(pocetVykreslenzch));
            //g.drawString("Počet vykreslených překážek: " + String.valueOf(pocetVykreslenzch), 10, 10);
        }
    }

}
