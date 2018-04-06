package transferobjects.factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import transferobjects.Employee;
import transferobjects.Gender;

public class EmployeeBuilder {

	private final String COL_NUMBER = "emp_no";
	private final String COL_FIRST_NAME = "first_name";
	private final String COL_LAST_NAME = "last_name";
	private final String COL_GENDER = "gender";
	private final String COL_BIRTH_DATE = "birth_date";
	private final String COL_HIRE_DATE = "hire_date";
	
	private Employee employee = new Employee();
	
	public void setEmployeeNumber(ResultSet rs) throws SQLException {
		employee.setNumber(rs.getInt(COL_NUMBER));
	}
	
	public void setFirstName(ResultSet rs) throws SQLException {
		 employee.setFirstName(rs.getString(COL_FIRST_NAME));
	}
	
	public void setLastName(ResultSet rs) throws SQLException {
		 employee.setLastName(rs.getString(COL_LAST_NAME));
	}
	
	public void setGender(ResultSet rs) throws SQLException {
		  //https://stackoverflow.com/questions/3155967/are-enums-supported-by-jdbc
	    employee.setGender(Gender.valueOf(rs.getString(COL_GENDER)));
	}
	
	public void setBirthDate(ResultSet rs) throws SQLException {
		  employee.setBirthDate(rs.getObject(COL_BIRTH_DATE, LocalDate.class));
	}
	
	public void setHireDate(ResultSet rs) throws SQLException {
		employee.setHireDate(rs.getObject(COL_HIRE_DATE, LocalDate.class));    
	}
	
	public Employee get() {return employee;}
}
