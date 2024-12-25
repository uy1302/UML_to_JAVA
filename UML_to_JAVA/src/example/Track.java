public class Track implements Playable {
	private String title ;
	private int length ;

	public  Track(String title, int length){
        this.title = title;
        this.length = length;
    }
	public String getTitle(){
        return this.title;
    }
	public boolean equals(Track track){
        return this.title.equals(track.title) && this.length == track.length;
    }
	public void play(){
        System.out.println("Track: " + this.title + ", length: " + this.length);
    }
}
