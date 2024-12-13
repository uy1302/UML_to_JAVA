public class ElectricCar {
	private double batteryCapacity;
	private double currentCharge;
	private String chargingPortType;

	void chargeBattery(double hours){
		currentCharge += hours * 50; //Example charging rate
		System.out.println("Battery charged for " + hours + " hours. Current charge: " + currentCharge);

	}
	void regeneratePower(double energy){
		currentCharge += energy;
		System.out.println("Regenerated " + energy + " units of power. Current charge: " + currentCharge);
	}
}
