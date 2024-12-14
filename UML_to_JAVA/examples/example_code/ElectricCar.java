public class ElectricCar {
	private double batteryCapacity;
	private double currentCharge;
	private String chargingPortType;

	void chargeBattery(double hours){
		currentCharge += hours * 10; // Assuming a charging rate of 10 units per hour.
		if(currentCharge > batteryCapacity) currentCharge = batteryCapacity;
	}
	void regeneratePower(double energy){
		currentCharge += energy;
		if(currentCharge > batteryCapacity) currentCharge = batteryCapacity;
	}
}

