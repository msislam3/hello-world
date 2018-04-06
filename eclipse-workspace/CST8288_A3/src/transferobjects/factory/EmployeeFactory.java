package transferobjects.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import transferobjects.Employee;

public class EmployeeFactory extends AbstractFactory<Employee> {

	@Override
	public Employee createFromResultSet(ResultSet rs) throws SQLException {
		EmployeeBuilder builder = new EmployeeBuilder();
		builder.setEmployeeNumber(rs);
		builder.setFirstName(rs);
		builder.setLastName(rs);
		builder.setGender(rs);
		builder.setBirthDate(rs);
		builder.setHireDate(rs);
		return builder.get();
	} 
	
	@Override
	public Employee createFromMap(Map<String, String[]> map) {
		return null;
	}
}
