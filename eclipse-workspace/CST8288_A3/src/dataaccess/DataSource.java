package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Shahriar Emami
 * @author Stanley Pieda
 * @see https://tomcat.apache.org/tomcat-8.0-doc/jndi-resources-howto.html
 */
public class DataSource {

	private static Connection connection;
	private static final DataSource singleton = new DataSource();
	
	private DataSource() {
		createConnection();
	}
	
    private void createConnection() {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            javax.sql.DataSource ds = (javax.sql.DataSource) envCtx.lookup("jdbc/Employee");
            connection = ds.getConnection();
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getConnection() {
    	return connection;
    }
}
