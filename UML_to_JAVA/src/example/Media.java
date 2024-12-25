abstract class Media {
	private int id ;
	private String title ;
	private String category ;
	private float cost ;
	private static int nbMedia=0 ;

	public  Media(String title) {
        this.title = title;
        this.id = ++nbMedia;
	}
	public  Media(String title, String category, float cost) {
        this(title);
        this.category = category;
        this.cost = cost;
	}
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getCategory() {
		return category;
	}
	public float getCost() {
		return cost;
	}
	public boolean equals(Media other) {
        if (other == null) {
            return false;
        }
        return this.id == other.id;
	}
}
