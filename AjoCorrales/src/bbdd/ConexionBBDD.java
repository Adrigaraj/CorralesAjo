package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

public class ConexionBBDD {
	private static Connection conn;
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	static {
		try {
			// new FileInputStream("bbdd.properties");
			// prop.load(getClass().getResourceAsStream("resources/bbdd.properties"));
			// Class.forName(prop.getProperty("driver"));
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/upmsocialdb?useSSL=false", "restuser1",
					"restuser1");
		} catch (ClassNotFoundException | SQLException e) {
			log.error(e.getMessage() + e.getStackTrace());
		}
	}

	public static Connection getConn() {
		return conn;
	}

}
