package bbdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

public class SentenciasSQL {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public static ResultSet selectUsuarios() {
		String sql = "SELECT nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta FROM Usuarios order by fechaAlta";
		PreparedStatement ps = null;
		try {
			try {
				ps = ConexionBBDD.getConn().prepareStatement(sql);
				return ps.executeQuery();
			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			}
		} catch (SQLException se) {
			log.error("Code: " + se.getErrorCode());
			log.error("SqlState: " + se.getSQLState());
			log.error("Error Message: " + se.getMessage());
			se = se.getNextException();
			return null;
		}
	}

	public static int insertUsuario(String nickname, String nombreCompleto, String pais, String fechaNacimiento,
			String correo, String fechaAlta) {
		PreparedStatement ps = null;
		try {
			try {
				ps = ConexionBBDD.getConn().prepareStatement(
						"insert into Usuarios (nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta) "
								+ "values(?,?,?,?,?,?)");
				ps.setString(1, nickname);
				if (nombreCompleto != null)
					ps.setString(2, nombreCompleto);
				else
					ps.setString(2, "");
				if (pais != null)
					ps.setString(3, pais);
				else
					ps.setString(3, "");
				if (fechaNacimiento != null)
					ps.setString(4, fechaNacimiento);
				else
					ps.setString(4, "");
				if (correo != null)
					ps.setString(5, correo);
				else
					ps.setString(5, "");
				if (fechaAlta != null)
					ps.setString(6, fechaAlta);
				else
					ps.setString(6, "");

				return ps.executeUpdate();

			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			}
		} catch (SQLException se) {
			log.error("Code: " + se.getErrorCode());
			log.error("SqlState: " + se.getSQLState());
			log.error("Error Message: " + se.getMessage());
			se = se.getNextException();
			return 0;
		}
	}

	public static ResultSet selectUsuario(String nickname) {
		PreparedStatement ps = null;
		try {
			try {
				ps = ConexionBBDD.getConn().prepareStatement(
						"SELECT nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta from Usuarios where nickname='"
								+ nickname + "'");
				return ps.executeQuery();
			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			}
		} catch (SQLException se) {
			log.error("Code: " + se.getErrorCode());
			log.error("SqlState: " + se.getSQLState());
			log.error("Error Message: " + se.getMessage());
			se = se.getNextException();
			return null;
		}
	}

	public static int borrarPublicacion(String pub, String nickname) {
		PreparedStatement ps = null;
		try {
			try {
				ps = ConexionBBDD.getConn().prepareStatement("delete from Publicaciones where propietario='" + nickname
						+ "' and idPublicacion='" + pub + "'");

				return ps.executeUpdate();

			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			}
		} catch (SQLException se) {
			log.error("Code: " + se.getErrorCode());
			log.error("SqlState: " + se.getSQLState());
			log.error("Error Message: " + se.getMessage());
			se = se.getNextException();
			return -1;
		}
	}

	public static int insertPublicacion(String idPublicacion, String fechaPublicacion, String propietario,
			String tweet) {
		PreparedStatement ps = null;
		try {
			try {
				ps = ConexionBBDD.getConn().prepareStatement(
						"insert into Publicaciones (idPublicacion, fechaPublicacion, propietario, tweet) "
								+ "values (?,?,?,?)");
				ps.setString(1, idPublicacion);

				if (fechaPublicacion != null)
					ps.setString(2, fechaPublicacion);
				else
					ps.setString(2, "");

				ps.setString(3, propietario);

				if (tweet != null)
					ps.setString(4, tweet);
				else
					ps.setString(4, "");

				return ps.executeUpdate();

			} catch (SQLException seRs) {
				String exMsg = "Message from MySQL Database";
				String exSqlState = "Exception";
				SQLException mySqlEx = new SQLException(exMsg, exSqlState);
				seRs.setNextException(mySqlEx);
				throw seRs;
			}
		} catch (SQLException se) {
			log.error("Code: " + se.getErrorCode());
			log.error("SqlState: " + se.getSQLState());
			log.error("Error Message: " + se.getMessage());
			se = se.getNextException();
			return 0;
		}
	}
}
