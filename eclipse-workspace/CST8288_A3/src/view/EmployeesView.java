package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.EmployeeLogic;
import transferobjects.Employee;

/**
 * Servlet implementation class EmployeesView
 */
@WebServlet("/EmployeesView")
public class EmployeesView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesView() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		 processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//doGet(request, response);
		 processRequest(request, response);
	}
	
	/**
	    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	    *
	    * @param request servlet request
	    * @param response servlet response
	    * @throws ServletException if a servlet-specific error occurs
	    * @throws IOException if an I/O error occurs
	    */
	   protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	       response.setContentType("text/html;charset=UTF-8");
	       try (PrintWriter out = response.getWriter()) {
	           out.println("<!DOCTYPE html>");
	           out.println("<html>");
	           out.println("<head>");
	           out.println("<title>Employees</title>");            
	           out.println("</head>");
	           out.println("<body>");
	           out.println("<h1>Employees View at " + request.getContextPath() + "</h1>");
	           EmployeeLogic logic = new EmployeeLogic();
	           List<Employee> employees = logic.getAllEmployees();
	           out.println("<table border=\"1\">");
	           out.println("<tr>");
	           out.println("<td>First Name</td>");
	           out.println("<td>Last Name</td>");
	           out.println("</tr>");
	           int i = 0;
	           for(Employee employee : employees){
	               out.printf("<tr><td>%s</td><td>%s</td></tr>", employee.getFirstName(), employee.getLastName());
	               i++;
	               if(i>100) break;
	           }
	           out.println("</table>");
	           out.println("</body>");
	           out.println("</html>");
	       }
	   }

}
