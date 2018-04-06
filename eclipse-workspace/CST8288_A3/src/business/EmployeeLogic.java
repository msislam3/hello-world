package business;

import java.util.List;

import dataaccess.EmployeeDAO;
import dataaccess.EmployeeDAOImpl;
import transferobjects.Employee;

public class EmployeeLogic {

	private EmployeeDAO employeeDAO = null; 

	public EmployeeLogic() {
		employeeDAO = new EmployeeDAOImpl();
	}

	public List<Employee> getAllEmployees() {
		return employeeDAO.getAllEmployees();
	}

	public void addEmployee(Employee employee) {
		cleanEmployee(employee);
		validateEmployee(employee);
		employeeDAO.addEmployee(employee);
	}

	private void validateEmployee(Employee employee) {
		validateString(employee.getFirstName(), "FirstName", 50, false);
		validateString(employee.getLastName(), "LastName", 50, false);
	}

	private void cleanEmployee(Employee employee) {
		if (employee.getFirstName() != null) {
			employee.setFirstName(employee.getFirstName().trim());
		}

		if (employee.getLastName() != null) {
			employee.setLastName(employee.getLastName().trim());
		}
	}

	private void validateString(String value, String fieldName, int maxLength, boolean isNullAllowed) {
		if (value == null && isNullAllowed) {
			// null permitted, nothing to validate
		} else if (value == null && !isNullAllowed) {
			throw new IllegalStateException(String.format("%s cannot be null", fieldName));
		} else if (value.isEmpty()) {
			throw new IllegalStateException(String.format("%s cannot be empty or only whitespace", fieldName));
		} else if (value.length() > maxLength) {
			throw new IllegalStateException(String.format("%s cannot exceed %d characters", fieldName, maxLength));
		}
	}
}
