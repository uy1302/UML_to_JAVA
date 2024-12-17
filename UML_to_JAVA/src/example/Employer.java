public class Employer extends Person {
	public String company;
	public String position;

	public void hireEmployee(Employee employee) {
        System.out.println("Hiring employee: " + employee.name);
        employee.work();
    }
	public String getEmployerDetails() {
        return "Name: " + name + ", ID: " + id + ", Address: " + address + ", Company: " + company + ", Position: " + position;
    }
	public void Employer(String company, String position) {
        this.company = company;
        this.position = position;
    }
}
