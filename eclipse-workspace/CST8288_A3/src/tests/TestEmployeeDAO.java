package tests;

import static org.junit.Assert.fail;

import org.junit.Test;

import dataaccess.EmployeeDAO;
import dataaccess.EmployeeDAOImpl;

public class TestEmployeeDAO {

	@Test
	public void test() {
		EmployeeDAO dao = new EmployeeDAOImpl();
		dao.getAllEmployees();
	}

}
