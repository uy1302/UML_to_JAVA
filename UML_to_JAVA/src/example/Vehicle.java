public class Vehicle {
	private String make;
	private String model;
	private int year;
	private double speed;

	void start(){
		System.out.println("Vehicle started.");
		speed = 0;
	}
	void stop(){
		System.out.println("Vehicle stopped.");
		speed = 0;
	}
	void accelerate(double increment){
		speed += increment;
		System.out.println("Accelerated by " + increment + ". Current speed: " + speed);
	}
	void brake(double decrement){
		speed -= decrement;
		if (speed < 0) speed = 0; // Speed cannot be negative
		System.out.println("Braked by " + decrement + ". Current speed: " + speed);
	}
}

