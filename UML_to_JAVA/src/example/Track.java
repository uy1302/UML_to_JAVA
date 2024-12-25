class Track implements Playable {
	private String title ;
	private int length ;

	public  Track(String title, int length) {
        this.title = title;
        this.length = length;
	}
	public String getTitle() {
		return title;
	}
	public boolean equals(Track track) {
        if (track == null) {
            return false;
        }
        return Objects.equals(this.title, track.title) && this.length == track.length;
	}
	public void play() {
		System.out.println("Track Title: " + title + ", Length: " + length);
	}
}
