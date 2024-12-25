public class Book extends Media {
	private List<String> authors ;

	public  Book(String title){
        super(title);
        this.authors = new ArrayList<>();
    }
	public  Book(String title, String category, float cost, List<String>authors){
        super(title, category, cost);
        this.authors = authors;
    }
	public List<String> getAuthors(){
        return this.authors;
    }
	public  Book(String title, String category, float cost){
        super(title, category, cost);
        this.authors = new ArrayList<>();
    }
	public void addAuthor(String authorName){
        this.authors.add(authorName);
    }
	public void removeAuthor(String authorName){
        this.authors.remove(authorName);
    }
	public String toString(){
        return "Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\\'' +
                ", category='" + getCategory() + '\\'' +
                ", cost=" + getCost() +
                ", authors=" + authors +
                '}';
    }
}
