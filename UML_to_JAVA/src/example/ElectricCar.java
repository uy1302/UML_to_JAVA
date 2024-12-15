public class ElectricCar {
	private double batteryCapacity;
	private double currentCharge;
	private String chargingPortType;

	void chargeBattery(double hours){
		currentCharge += hours * 50; // Assuming 50 units charge per hour.  Adjust as needed.
		System.out.println("Battery charged for " + hours + " hours. Current charge: " + currentCharge);

	}
	void regeneratePower(double battery){
		currentCharge += battery;
		System.out.println("Regenerated " + battery + " units of power. Current charge: " + currentCharge);
	}
}
