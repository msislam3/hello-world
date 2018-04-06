package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import transferobjects.Employee;
import transferobjects.factory.DTOFactoryCreator;
import transferobjects.factory.Factory;

public class EmployeeDAOImpl implements EmployeeDAO {

	private static final String GET_ALL_EMPLOYEES = "SELECT * FROM employees ORDER BY emp_no";
    private static final String INSERT_EMPLOYEES = "INSERT INTO employees VALUES(?, ?, ?, ?, ?, ?)";
    private static final String DELETE_EMPLOYEES = "DELETE FROM employees WHERE emp_no = ?";
    //private static final String UPDATE_EMPLOYEES = "UPDATE employyes SET name = ? WHERE course_num = ?";
    private static final String GET_BY_NO_EMPLOYEES = "SELECT * FROM employees WHERE emp_no = ?";
    
    //private Factory<Employee> factory = DTOFactoryCreator.getFactory("EMPLOYEE");
    private Factory<Employee> factory = DTOFactoryCreator.createBuilder(Employee.class);
    
	public List<Employee> getAllEmployees(){
		List<Employee> employees = Collections.emptyList();
        Employee employee;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = DataSource.getConnection();
            pstmt = con.prepareStatement( GET_ALL_EMPLOYEES);
            rs = pstmt.executeQuery();
            employees = new ArrayList<>(100);
            while( rs.next()){
                /*employee = new Employee();
                employee.setNumber(rs.getInt("emp_no"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                //https://stackoverflow.com/questions/3155967/are-enums-supported-by-jdbc
                employee.setGender(Gender.valueOf(rs.getString("gender")));
                employee.setBirthDate(rs.getObject("birth_date", LocalDate.class));
                employee.setHireDate(rs.getObject("hire_date", LocalDate.class));*/
            	employee = factory.createFromResultSet(rs);
                employees.add(employee);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return employees;
	}
	
	public void addEmployee(Employee employee) {
		try( Connection con = DataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement( INSERT_EMPLOYEES);){
            pstmt.setInt(1, employee.getNumber());
            pstmt.setObject(2, employee.getBirthDate());
			pstmt.setString(3, employee.getFirstName());
            pstmt.setString(4, employee.getLastName());
            pstmt.setString(5, employee.getGender().toString());
            pstmt.setObject(6,  employee.getHireDate());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
	}
	
	public Employee getEmployeeById(int employeeId) {
		return null;
	}
	
	public List<Employee> getEmployeeByFirstName(String firstName){
		return null;
	}
	
	public void updateEmpoyee(Employee employee) {
		
	}
	
	public void deleteEmployee(Employee employee) {
		
	}
}
