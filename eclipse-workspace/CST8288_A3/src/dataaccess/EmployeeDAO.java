package dataaccess;

import java.util.List;

import transferobjects.Employee;


public interface EmployeeDAO {
	List<Employee> getAllEmployees();
	void addEmployee(Employee employee);
	Employee getEmployeeById(int employeeId);
	List<Employee> getEmployeeByFirstName(String firstName);
	void updateEmpoyee(Employee employee);
	void deleteEmployee(Employee employee);
}
