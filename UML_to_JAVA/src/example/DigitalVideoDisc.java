class DigitalVideoDisc extends Disc implements Playable {

	public  DigitalVideoDisc(String title) {
		super(title);
	}
	public  DigitalVideoDisc(String title, String category, float cost) {
		super(title, category, cost);
	}
	public  DigitalVideoDisc(String title, String category, String director, float cost) {
		super(title, category, director, cost);
	}
    public  DigitalVideoDisc(String title, String category, String director, int length, float cost) {
        super(title, category, director, cost);
        super.length=length;

    }
	public String toString() {
		return "DigitalVideoDisc{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\\'' +
                ", category='" + getCategory() + '\\'' +
                ", director='" + getDirector() + '\\'' +
                ", length='" + getLength() + '\\'' +
                ", cost=" + getCost() +
                '}';
	}
	public void play() {
		System.out.println("Playing DVD: " + getTitle() + ", Length: " + getLength());
	}
}
