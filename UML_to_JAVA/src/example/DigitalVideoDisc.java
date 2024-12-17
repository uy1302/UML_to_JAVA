public class DigitalVideoDisc extends Disc implements Playable {

    public DigitalVideoDisc(String title) {
        super(title);
    }

    public DigitalVideoDisc(String title, String category, float cost) {
        super(title, category, cost);
    }

    public DigitalVideoDisc(String title, String category, String director, float cost) {
        super(title, category, director, cost);
    }

    public DigitalVideoDisc(String title, String category, String director, int length, float cost) {
        super(title, category, director, cost);
        
    }

   public String toString() {
        return "DVD - " + super.toString() + (getDirector() != null ? (" - Director: " + getDirector()) : "");
    }
  
    public void play() {
        System.out.println("Playing DVD: " + getTitle() + ", Length: " + getLength());
    }
}

