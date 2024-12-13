public class Vehicle {
	private String make;
	private String model;
	private int year;
	private double speed;

	void start(){
		System.out.println("Engine started.");
	}
	void stop(){
		speed = 0;
		System.out.println("Engine stopped.");
	}
	void accelerate(double increment){
		speed += increment;
	}
	void brake(double decrement){
		speed = Math.max(0, speed - decrement);
	}
}
