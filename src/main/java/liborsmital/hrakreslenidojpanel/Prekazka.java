package liborsmital.hrakreslenidojpanel;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.EnumSet;

/**
 *
 * @author Libor
 */
public class Prekazka {

    public int index;
    private Rectangle obdelnik;
    private Color barvaRamu, barvaVyplne;
    private EnumSet kolize = EnumSet.noneOf(Kolize.class);

    public Rectangle getObdelnik() {
        return obdelnik;
    }

    public void setObdelnik(Rectangle obdelnik) {
        this.obdelnik = obdelnik;
    }

    public Prekazka(int x, int y, Color barvaRamu, Color barvaVyplne) {
        obdelnik = new Rectangle(x, y, 30, 30);
        this.barvaRamu = barvaRamu;
        this.barvaVyplne = barvaVyplne;
    }

    public Prekazka() {
        this.barvaRamu = Color.DARK_GRAY;
        this.barvaVyplne = Color.GRAY;
    }

    public Color getBarvaRamu() {
        return barvaRamu;
    }

    public Color getBarvaVyplne() {
        return barvaVyplne;
    }

    public Rectangle getRectangel() {
        return obdelnik;
    }

    public void setOfset(int ofset) {
        obdelnik.x = ofset;
    }

    public int getX() {
        return obdelnik.x;
    }

    public EnumSet<Kolize> getKolize() {

        return kolize;

    }

    public void setAddKolize(Kolize kolize) {

        this.kolize.add(kolize);
        this.kolize.remove(Kolize.Nic);

    }

    public void setSmazatAllKolize() {
        this.kolize.clear();
        //this.kolize.add(Kolize.Nic);
    }

    

}
