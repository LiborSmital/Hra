package liborsmital.hrakreslenidojpanel;

/**
 *
 * @author Libor
 */
public class Obrazek {
    boolean zobrazeno = false;
    int pocitadloZobrazeni;
    
    static int DOBA_ZOBRAZENI = 20; // kolikrat se obr8zek zobrazi než se zobrazi dalsi 

    public boolean next() {
        pocitadloZobrazeni++;
        System.out.println("pocitadloZobrazeni: "+pocitadloZobrazeni);
        if(pocitadloZobrazeni < DOBA_ZOBRAZENI && zobrazeno){
            
            return false;
        }else if(pocitadloZobrazeni >= DOBA_ZOBRAZENI && zobrazeno){
            this.reset();
            return true;
        }
        return false;
        
    }
    int sx1,
        sy1,
        sx2,
        sy2;

    public Obrazek(int sx1, int sy1, int sx2, int sy2) {
        this.sx1 = sx1;
        this.sy1 = sy1;
        this.sx2 = sx2;
        this.sy2 = sy2;
        this.pocitadloZobrazeni = 0;
    }
    public void reset(){
        this.zobrazeno = false;
        this.pocitadloZobrazeni = 0;
    }
    
    
    

}
