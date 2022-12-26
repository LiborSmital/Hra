package liborsmital.hrakreslenidojpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Libor
 */
public class Ladit {

    private ArrayList<String> seznam = new ArrayList<>();
    private int whightText = 12;
    public boolean ladit = true;

    void paint(Graphics g) {
        if (ladit) {
            Font pismo = new Font("Arial", Font.TYPE1_FONT , 12);
            
            g.setFont(pismo);
            int width = this.whightText;
            g.setColor(Color.BLACK);
            for (String string : seznam) {
                g.drawString(string, 10, width);

                width += this.whightText;
            }
        }
    }

    public boolean add(String popis, String obsah) {
        for (int i = 0; i < seznam.size(); i++) {
            String string = seznam.get(i);
            if (string.startsWith(popis)) {
                string = popis + ": " + obsah;
                seznam.set(i, string);
                return true;
            }
        }
        String str = popis + ": " + obsah;
        seznam.add(str);
        return true;
    }

    public void remove(String popis) {
        for (int i = 0; i < seznam.size(); i++) {
            String string = seznam.get(i);
            if (string.equals(popis)) {
                seznam.remove(i);
            }
        }
    }

    public void removeAll() {
        seznam.clear();
    }

    public void setLadit() {
        if (this.ladit) {
            this.ladit = false;
        } else {
            this.ladit = true;
        }
    }

}
