package bbdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

public class UtilsBBDD {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public static void cerrarRs(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage());
		}

	}

	public static void cerrarPs(PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage());
		}

	}
}
