package liborsmital.hrakreslenidojpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumSet;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Libor
 */
public class Hero {

    //private int sirkaVyskaPrekazek = Prekazky.sirkaVyska;
    private Rectangle obdelnikH;
    private int ofset;
    private int delkaSceny;
    private boolean konecHry;
    private boolean ladit;

    private final float GRAVITACE = 0.2f;
    private final float MAX_HOR_RYCHOST = 1.8f;
    private final float MAX_SKOK_RYCHOST = 5.5f;
    
    private float horRychlost;
    private float vertRychlost;
    private Dimension velikostOkna;
    private EnumSet coDelaH = EnumSet.of(CoDelaHeto.naPravo, CoDelaHeto.Pada);
    private EnumSet prikazKlavesnice = EnumSet.noneOf(PrikazKlavesnice.class);
    private EnumSet kolizeOdkud = EnumSet.noneOf(Kolize.class);
    private ArrayList<Prekazka> kolizniPrekazkyList;
    BufferedImage obrazek;
    private ArrayList<Obrazek> obrazkyH_Chuze;
    private Ladit laditO;
    //private ArrayList<Obrazek> obrazkyH_Skok;

    public Hero(Dimension d, Ladit ladit) {
        this.obrazkyH_Chuze = new ArrayList<Obrazek>();
        //this.obrazkyH_Skok = new ArrayList<Obrazek>();
        this.obdelnikH = new Rectangle(10, 0, 20, 25);
        this.ofset = Prekazky.sirkaVyska / 2 - obdelnikH.width / 2;
        this.velikostOkna = d;
        this.horRychlost = 0.0f;
        this.vertRychlost = 0.0f;
        this.kolizniPrekazkyList = new ArrayList<>();
        this.delkaSceny = d.width;
        this.konecHry = false;
        this.ladit = true;
        this.laditO = ladit;
        this.setObrazyChuzeH();
        try {
            //TODO: dodělat cestu k obrázku. 
            obrazek = ImageIO.read(new File("C:\\Users\\Libor\\Documents\\NetBeansProjects\\HraKresleniDoJPanel\\src\\main\\resources\\char.png"));
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void reset() {
        obdelnikH = new Rectangle(0, 0, 25, 25);
        this.ofset = Prekazky.sirkaVyska / 2 - obdelnikH.width / 2;
        this.horRychlost = 0.0f;
        this.vertRychlost = 0.0f;
        this.kolizniPrekazkyList = new ArrayList<>();
        this.konecHry = false;
    }

    private Obrazek getObrazekHero() {

        if (!this.obrazkyH_Chuze.isEmpty()) {
            if (coDelaH.contains(CoDelaHeto.Stoji) || coDelaH.contains(CoDelaHeto.Skok) || coDelaH.contains(CoDelaHeto.Pada)) {
                return this.obrazkyH_Chuze.get(0);
            }
            if (coDelaH.contains(CoDelaHeto.Chuze)) {
                for (int i = 0; i < this.obrazkyH_Chuze.size(); i++) {
                    if (this.obrazkyH_Chuze.get(i).zobrazeno) {
                        if (this.obrazkyH_Chuze.get(i).next()) {

                            i++;
                            if (i >= this.obrazkyH_Chuze.size()) {
                                i = 0;
                            }
                            this.obrazkyH_Chuze.get(i).zobrazeno = true;
                        }
                        System.out.println("Obrazek: " + i);
                        return this.obrazkyH_Chuze.get(i);
                    }
                }
            }
        }
        return null;

    }

    private void setObrazToPole(ArrayList<Obrazek> arrL, int index, int sx1, int sy1, int sx2, int sy2) {

        Obrazek p = new Obrazek(sx1, sy1, sx2, sy2);
        arrL.add(p);
        Obrazek obr = arrL.get(0);
        obr.zobrazeno = true;
        arrL.set(0, obr);
    }

    private void setObrazyChuzeH() {
        setObrazToPole(obrazkyH_Chuze, 0, 35, 19, 35 + 65, 124);
        setObrazToPole(obrazkyH_Chuze, 1, 163, 19, 163 + 65, 124);

    }

    public void paintHero(Graphics g) {

        if (getObrazekHero() != null) {
            Obrazek obrazek = getObrazekHero();

            if (coDelaH.contains(CoDelaHeto.naLevo)) {
                g.drawImage(this.obrazek, ////  vykreslování doleva
                        obdelnikH.x + obdelnikH.width,
                        obdelnikH.y,
                        obdelnikH.x,
                        obdelnikH.y + obdelnikH.height,
                        obrazek.sx1,
                        obrazek.sy1,
                        obrazek.sx2,
                        obrazek.sy2,
                        null);
            } else {
                g.drawImage(this.obrazek, ////  vykreslování doprava
                        obdelnikH.x,
                        obdelnikH.y,
                        obdelnikH.x + obdelnikH.width,
                        obdelnikH.y + obdelnikH.height,
                        obrazek.sx1,
                        obrazek.sy1,
                        obrazek.sx2,
                        obrazek.sy2,
                        null);
            }
        } else {

            g.setColor(Color.CYAN);
            g.fillRect(obdelnikH.x,
                    obdelnikH.y,
                    obdelnikH.width,
                    obdelnikH.height);

            g.drawRect(obdelnikH.x,
                    obdelnikH.y,
                    obdelnikH.width,
                    obdelnikH.height);

        }
        if (konecHry) {
            Font pismo = new Font("Arial", Font.BOLD, 40);
            String text = "KONEC HRY";
            g.setFont(pismo);
            g.setColor(Color.red);
            FontMetrics fm = g.getFontMetrics(pismo);

            int sirkaTextu = fm.stringWidth(text);
            g.drawString(text, (velikostOkna.width - sirkaTextu) / 2, velikostOkna.height / 2);
        }

        if (this.ladit) {
            g.setColor(Color.ORANGE);
            if (kolizeOdkud.contains(Kolize.Nahore)) { // kolizniPrekazkyList pod Hero
                g.setColor(Color.red);
                g.drawLine(obdelnikH.x,
                        obdelnikH.y,
                        obdelnikH.x + obdelnikH.width,
                        obdelnikH.y);
            }
            if (kolizeOdkud.contains(Kolize.Dole)) { // kolizniPrekazkyList nad Hero

                g.drawLine(obdelnikH.x,
                        obdelnikH.y + obdelnikH.height,
                        obdelnikH.x + obdelnikH.width,
                        obdelnikH.y + obdelnikH.height);
            }
            if (kolizeOdkud.contains(Kolize.Pravo)) { // kolizniPrekazkyList vlevo od Hero

                g.drawLine(obdelnikH.x + obdelnikH.width,
                        obdelnikH.y,
                        obdelnikH.x + obdelnikH.width,
                        obdelnikH.y + obdelnikH.height);
            }
            if (kolizeOdkud.contains(Kolize.Levo)) {  /// kolizniPrekazkyList vpravo od Hero

                g.drawLine(obdelnikH.x,
                        obdelnikH.y,
                        obdelnikH.x,
                        obdelnikH.y + obdelnikH.height);
            }
        }
    }

    /*public*/ void setKlavesa(PrikazKlavesnice p, char ch) { // prijima stisknute klavesnice a vyhodnocuje pohyb
        if (ch == '+') {
            this.prikazKlavesnice.add(p);
        }
        if (ch == '-') {
            this.prikazKlavesnice.remove(p);
            if (p == PrikazKlavesnice.SipkaL || p == PrikazKlavesnice.SipkaP) {
                if (kolizeOdkud.contains(Kolize.Dole)) {
                    horRychlost = 0.0f;
                }
            }
        }

        if (this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaN) && kolizeOdkud.contains(Kolize.Dole)) {
            setCoDelaH_Skok();
        }
        if (this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaL) && kolizeOdkud.contains(Kolize.Dole)) {
            setCoDelaH_naLevo();
            if (this.horRychlost < -MAX_HOR_RYCHOST) {
                this.horRychlost = -MAX_HOR_RYCHOST;

            } else if (this.horRychlost > -0.9f) {
                this.horRychlost = -1.0f;
            }

        }
        if (this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaP) && kolizeOdkud.contains(Kolize.Dole)) {
            setCoDelaH_naPravo();
            if (this.horRychlost > MAX_HOR_RYCHOST) {
                this.horRychlost = MAX_HOR_RYCHOST;
            } else if (this.horRychlost < 0.9f) {
                this.horRychlost = 1.0f;
            }

        }
        if ((!this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaP) || !this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaL))
                && kolizeOdkud.contains(Kolize.Dole) && !coDelaH.contains(CoDelaHeto.Skok)) {
            setCoDelaH_Stoji();
        }

        //System.out.println(prikazKlavesnice);
    }

    public void vyhodnoceniKolize(Prekazky p) {
        this.kolizeOdkud.clear();
        kolizniPrekazkyList.clear();

        for (int i = 0; i < p.getSize(); i++) {            // vyhledání kolizních oběktů.
            p.getPrakazka(i).setSmazatAllKolize();
            Prekazka pre = p.getPrakazka(i);
            p.setIndex(i);

            if (jeKolize(obdelnikH, pre.getObdelnik())) {
                kolizniPrekazkyList.add(pre);
            }
        }
        if (!kolizniPrekazkyList.isEmpty()) {                // Test zda jsou nejake kolizniPrekazkyList k vyhodnoceni.

            for (int j = 0; j < kolizniPrekazkyList.size(); j++) { // Procházení všech oběktů Prekazka kolidující s Hero v seznamu kolizniPrekazkyList
                Prekazka prekazka = kolizniPrekazkyList.get(j);

                if (jeVeStejnemRadku(obdelnikH, prekazka.getRectangel(), 0)) {// test na kolizniPrekazkyList dole od Hero  toto funguje
                    if (obdelnikH.getCenterY() < prekazka.getObdelnik().getCenterY()) {
                        prekazka.setAddKolize(Kolize.Dole);

                    } else if (obdelnikH.getCenterY() > prekazka.getObdelnik().getCenterY()) {
                        prekazka.setAddKolize(Kolize.Nahore);

                    }

                }
                if (jeVeStejnemSloupci(obdelnikH, prekazka.getRectangel(), 3)
                        && jeKolizeNaPravo(obdelnikH, prekazka.getRectangel())
                        && !kolizeOdkud.contains(Kolize.Nahore)) // rozlišení kolizniPrekazkyList v řádku 
                {
                    prekazka.setAddKolize(Kolize.Levo);

                } else if (jeVeStejnemSloupci(obdelnikH, prekazka.getRectangel(), 3)
                        && jeKolizeNaLevo(obdelnikH, prekazka.getRectangel())
                        && !kolizeOdkud.contains(Kolize.Nahore)) {
                    prekazka.setAddKolize(Kolize.Pravo);
                }

                kolizeOdkud.addAll(prekazka.getKolize());
                //---------------------------------------------------oprava chyby se kterou si nevm rady
                if (kolizeOdkud.contains(Kolize.Levo) && kolizeOdkud.contains(Kolize.Pravo)) {
                    kolizeOdkud.remove(Kolize.Levo);
                    kolizeOdkud.remove(Kolize.Pravo);
                }
                //----------------------------------------------------------------------------------------        
                p.setPrekazka(prekazka);
            }
        }
        vyhodnoceniCoDelaHero();
        aktualizacePohybu();
    }

    private boolean jeKolize(Rectangle h, Rectangle p) {//  dobaz jestli je kolizniPrekazkyList mezi dvouma obdelnikama.
        return h.intersects(p);
    }

    private boolean jeVeStejnemRadku(Rectangle h, Rectangle p, int korekce) {
        /// bez kolizniPrekazkyList

        return h.getMaxY() - korekce > p.y && h.y + korekce < p.getMaxY();
    }

    private boolean jeVeStejnemSloupci(Rectangle h, Rectangle p, int korekce) {
        /// bez kolizniPrekazkyList
        return h.getMaxX() - korekce > p.x && h.x + korekce < p.getMaxX();
    }

    private boolean jeKolizeNaLevo(Rectangle h, Rectangle p) {
        return (h.getCenterX() - p.getCenterX() < 0 && jeVeStejnemRadku(h, p, 2));      ///   chyba v porovnanani, ale funguje 
    }

    private boolean jeKolizeNaPravo(Rectangle h, Rectangle p) {
        return (h.getCenterX() - p.getCenterX()) > 0 && jeVeStejnemRadku(h, p, 2);      ///   chyba v porovnanani, ale funguje 
    }

    private void vyhodnoceniCoDelaHero() {  // nastaveí pouze CoDelaHero
        if (!konecHry) {  // test na konec hry
            if (kolizeOdkud.contains(Kolize.Dole) && !coDelaH.contains(CoDelaHeto.Skok)) {
                if (horRychlost != 0) {
                    setCoDelaH_Chuze();
                } else {
                    setCoDelaH_Stoji();
                }
            } else if (!coDelaH.contains(CoDelaHeto.Skok) || kolizeOdkud.contains(Kolize.Nahore)) {
                setCoDelaH_Pada();
            }

            if (((kolizeOdkud.contains(Kolize.Levo) || kolizeOdkud.contains(Kolize.Pravo)))) {

                if (kolizeOdkud.contains(Kolize.Nahore)) {
                    setCoDelaH_Pada();
                } else {
                    setCoDelaH_Stoji();
                }

            }
            if (coDelaH.contains(CoDelaHeto.Skok) && vertRychlost > 0.0f) {
                setCoDelaH_Pada();
            }

            if (this.obdelnikH.x < -5 || this.obdelnikH.x > this.velikostOkna.width || this.obdelnikH.y < -5 || this.obdelnikH.y > this.velikostOkna.height) {
                konecHry = true;

            }
        }
        if (konecHry) {

        }
        laditO.add("coDelaH", coDelaH.toString());
        System.out.println("    coDelaH: " + coDelaH);
        laditO.add("kolizeOdkudH", kolizeOdkud.toString());
        System.out.println("kolizeOdkud: " + kolizeOdkud);
        laditO.add("Počet kolizních překážek", String.valueOf(kolizniPrekazkyList.size()));
        System.out.println(kolizniPrekazkyList.size());
        laditO.add("prikazKlavesnice", prikazKlavesnice.toString());
        //System.out.println(prikazKlavesnice);
        System.out.println("Hrychlost: " + this.horRychlost + " Vrychlost: " + this.vertRychlost + " Hero-X: " + this.obdelnikH.x + " Hero-Y: " + this.obdelnikH.y);

    }

    private void aktualizacePohybu() { // nastavení pouze rychlosti.!!!!!!!!!!!!

        if (!konecHry) {  // test na konec hry
            if (coDelaH.contains(CoDelaHeto.Pada)) {   /// výpočet pro pád
                if (vertRychlost == 0.0f) {
                    vertRychlost = 0.1f;

                } else {
                    vertRychlost = vertRychlost + GRAVITACE;
                }

            }
            if (coDelaH.contains(CoDelaHeto.Skok) && kolizeOdkud.contains(Kolize.Dole)) { // výpočet pro skok

                vertRychlost = -MAX_SKOK_RYCHOST;

            } else if (coDelaH.contains(CoDelaHeto.Skok)) { // výpočet pro pokračování skoku
                vertRychlost = vertRychlost + GRAVITACE;

            }

            if (coDelaH.contains(CoDelaHeto.Stoji)) {  // uprava vertikální rychlosti pro chůy a skok 
                vertRychlost = 0.0f;
                if (!this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaL)
                        && !this.prikazKlavesnice.contains(PrikazKlavesnice.SipkaP)) {
                    horRychlost = 0.0f;
                }
            }

            if (coDelaH.contains(CoDelaHeto.Skok) && coDelaH.contains(CoDelaHeto.Chuze)) {   // oprava chyby kdy se Hero zastaví někde v prostoru
                setCoDelaH_Pada();
            }
            if (coDelaH.contains(CoDelaHeto.Chuze)) {
                if (horRychlost != 0) {
                    if (horRychlost >= 1.0f && horRychlost < MAX_HOR_RYCHOST) {
                        horRychlost += 0.2f;
                    } else if (horRychlost <= -1.0f && horRychlost > -MAX_HOR_RYCHOST) {
                        horRychlost -= 0.2f;
                    }
                }

            }

            posunHeroMimoPrekazku();
            obdelnikH.y += vertRychlost;
            obdelnikH.x += horRychlost + 0.25f;
        } else { // konec hry 

            horRychlost = 0.0f;
            vertRychlost = 0.0f;

        }

    }

    public void setCoDelaH_Skok() {
        coDelaH.remove(CoDelaHeto.Pada);
        coDelaH.remove(CoDelaHeto.Stoji);
        coDelaH.remove(CoDelaHeto.Chuze);
        vertRychlost = -0.1f;
        coDelaH.add(CoDelaHeto.Skok);
    }

    public void setCoDelaH_Chuze() {
        coDelaH.remove(CoDelaHeto.Pada);
        coDelaH.remove(CoDelaHeto.Skok);
        coDelaH.remove(CoDelaHeto.Stoji);

        coDelaH.add(CoDelaHeto.Chuze);
    }

    public void setCoDelaH_Stoji() {
        coDelaH.remove(CoDelaHeto.Chuze);
        coDelaH.remove(CoDelaHeto.Skok);
        coDelaH.remove(CoDelaHeto.Pada);

        coDelaH.add(CoDelaHeto.Stoji);

    }

    public void setCoDelaH_Pada() {
        coDelaH.remove(CoDelaHeto.Chuze);
        coDelaH.remove(CoDelaHeto.Skok);
        coDelaH.remove(CoDelaHeto.Stoji);

        coDelaH.add(CoDelaHeto.Pada);
    }

    public void setCoDelaH_naLevo() {
        coDelaH.remove(CoDelaHeto.naPravo);
        coDelaH.remove(CoDelaHeto.Stoji);

        coDelaH.add(CoDelaHeto.naLevo);

    }

    public void setCoDelaH_naPravo() {
        coDelaH.remove(CoDelaHeto.naLevo);
        coDelaH.remove(CoDelaHeto.Stoji);

        coDelaH.add(CoDelaHeto.naPravo);

    }

    public void posunHeroMimoPrekazku() {
        vymazaniNepodstatnychKolizi();
        for (int i = 0; i < kolizniPrekazkyList.size(); i++) {

            Prekazka p = kolizniPrekazkyList.get(i);

            if (p.getKolize().contains(Kolize.Dole)) {
                obdelnikH.y = p.getObdelnik().y - obdelnikH.height + 1;
                //continue;

            } else if (p.getKolize().contains(Kolize.Nahore)) { // && coDelaH.contains(CoDelaHeto.Skok)) {
                obdelnikH.y = (int) p.getObdelnik().getMaxY() + 1;
                vertRychlost = vertRychlost * -1;
                vertRychlost = vertRychlost / 2;

            }
            //TODO: Dodelat posun vlevo a vpravo    
            if (p.getKolize().contains(Kolize.Levo)) {    //!!!!!!!!!!!!!!!!!!!!!!! nefunguje spravne
                obdelnikH.x = (int) p.getObdelnik().getMaxX() + 0;
            } else if (p.getKolize().contains(Kolize.Pravo)) {   //!!!!!!!!!!!!!!!!!!!!!!   nefunguje spravne
                obdelnikH.x = (int) p.getObdelnik().getX() - obdelnikH.width - 0;
            }
        }
    }

    private void vymazaniNepodstatnychKolizi() {
        for (int i = 0; i < kolizniPrekazkyList.size(); i++) {
            Prekazka p = kolizniPrekazkyList.get(i);
            Rectangle re = p.getObdelnik().union(obdelnikH);

            if (re.width < re.height) {
                p.getKolize().remove(Kolize.Levo);
                p.getKolize().remove(Kolize.Pravo);

            } else if (re.width > re.height) {
                p.getKolize().remove(Kolize.Nahore);
                p.getKolize().remove(Kolize.Dole);
            }
            kolizniPrekazkyList.set(i, p);

        }

    }

    public int getOfset(int pocetPrekazekVRsdku, Dimension d) {
        velikostOkna = d;
        this.delkaSceny = pocetPrekazekVRsdku * Prekazky.sirkaVyska;

        if (obdelnikH.x < velikostOkna.width / 2) { //pohyb Hero do prvni poloviny okna.
            ofset = ofset + (obdelnikH.x - (this.velikostOkna.width / 2));
            if (ofset > 0) {
                obdelnikH.x = this.velikostOkna.width / 2;
            }

        } else if (obdelnikH.x > velikostOkna.width / 2 && ofset < delkaSceny - velikostOkna.width) { // pohyb od prvni poloviny okna do posledni 
            ofset = ofset + (obdelnikH.x - (this.velikostOkna.width / 2));
            obdelnikH.x = this.velikostOkna.width / 2;

        }
        if (obdelnikH.x + obdelnikH.width >= velikostOkna.width) {
            obdelnikH.x = velikostOkna.width - obdelnikH.width;

        } else if (obdelnikH.x < 0) {
            obdelnikH.x = 0;
        }

        if (ofset > delkaSceny - velikostOkna.width / 2) {
            ofset = delkaSceny - velikostOkna.width / 2;
        } else if (ofset < 0) {
            ofset = 0;
        }
        //System.out.println("Ofset: " + ofset + "Delka Sceny: " + delkaSceny + " velikostOkna.width: " + velikostOkna.width / 2 + " pocetPrekazekVRsdku: " + pocetPrekazekVRsdku);
        return ofset;
    }

    public boolean isLadit() {
        return ladit;
    }

    public void setLadit() {
        this.ladit = !ladit;
    }

}

enum CoDelaHeto {
    Skok,
    Chuze,
    Stoji,
    Pada,
    naLevo,
    naPravo
}

enum PrikazKlavesnice {
    SipkaL,
    SipkaP,
    SipkaD,
    SipkaN,
    Mezernik
}

enum Kolize {
    Levo,
    Pravo,
    Nahore,
    Dole,
    Nic
}
