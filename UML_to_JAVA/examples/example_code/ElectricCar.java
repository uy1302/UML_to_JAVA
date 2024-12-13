public class ElectricCar {
	private double batteryCapacity;
	private double currentCharge;
	private String chargingPortType;

	void chargeBattery(double hours){
		currentCharge += hours * 10; //Assuming a charge rate of 10 units/hour
	}
	void regeneratePower(double energy){
		currentCharge += energy;
	}
}

