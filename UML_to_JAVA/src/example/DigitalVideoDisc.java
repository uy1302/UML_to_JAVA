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
        this.length= length;
    }
    private int length;

    @Override
    public String toString() {
        return "DigitalVideoDisc{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\\'' +
                ", category='" + getCategory() + '\\'' +
                ", cost=" + getCost() +
                ", director='" + getDirector() + '\\'' +
                 ", length=" + length +
                '}';
    }

    @Override
    public void play() {
        System.out.println("Playing DVD: " + getTitle() + ", Length: " + length);

    }
}

