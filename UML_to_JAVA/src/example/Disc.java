public class Disc extends Media {
	private int length ;
	private String director ;

	public  Disc(String title){
        super(title);
    }
	public  Disc(String title, String category, float cost){
        super(title, category, cost);
    }
	public  Disc(String title, String category, String director, float cost){
        super(title, category, cost);
        this.director = director;
    }
	public int getLength(){
        return this.length;
    }
	public String getDirector(){
        return this.director;
    }
}
