public class Car {
	private String entertainmentSystem;
	private int seatingCapacity;

	void playMusic(String songname){
		System.out.println("Playing " + songname + " on the " + entertainmentSystem);
	}
	void enableCruiseControl(double speed){
		System.out.println("Cruise control enabled at " + speed + " km/h");
	}
}
