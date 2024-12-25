public class CompactDisc extends Disc implements Playable {
	private String artist ;
	private List<Track> track ;

	public String getArtist(){
        return this.artist;
    }
	public  CompactDisc(String title, String Category, String director, int length, float cost, String artist, List<Track>tracks){
        super(title, Category, director, cost);
        super.length = length;
        this.artist = artist;
        this.track = tracks;
    }
	public  CompactDisc(String title, String Category, String director, int length, float cost){
        super(title, Category, director, cost);
        super.length = length;
         this.track = new ArrayList<>();
    }
	public  CompactDisc(String title){
        super(title);
        this.track = new ArrayList<>();
    }
	public List<Track> getTracks(){
        return this.track;
    }
	public void play(){
        System.out.println("Playing CD: " + getTitle() + ", length: " + getLength());
        for (Track t : track) {
            t.play();
        }
    }
	public String toString(){
        return "CompactDisc{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\\'' +
                ", category='" + getCategory() + '\\'' +
                ", director='" + getDirector() + '\\'' +
                ", length=" + getLength() +
                ", cost=" + getCost() +
                 ", artist='" + artist + '\\'' +
                ", tracks=" + track +
                '}';
    }
	public void removeTrack(Track track){
        this.track.removeIf(t -> t.equals(track));
    }
	public void addTrack(Track track){
         for(Track t : this.track){
            if(t.equals(track)){
                return;
            }
        }
        this.track.add(track);
    }
}
