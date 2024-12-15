public class Employee extends Person {
	public int employeeId;
	public float salary;
	public String department;

	public void work() {
		System.out.println(name + " is working in " + department + " department.");
	}

	public String getEmployeeDetails() {
		return "Name: " + name + "\
ID: " + id + "\
Address: " + address + "\
Employee ID: " + employeeId + "\
Salary: " + salary + "\
Department: " + department;
	}
}

