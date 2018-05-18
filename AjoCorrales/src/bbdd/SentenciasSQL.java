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
			ps = ConexionBBDD.getConn().prepareStatement(sql);
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}

	public static int insertUsuario(String nickname, String nombreCompleto, String pais, String fechaNacimiento,
			String correo, String fechaAlta) {
		PreparedStatement ps = null;
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
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet selectUsuario(String nickname) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(
					"SELECT nickname, nombreCompleto, pais, fechaNacimiento, correo, fechaAlta from Usuarios where nickname='"
							+ nickname + "'");
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}

	public static int borrarPublicacion(String pub, String nickname) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(
					"delete from Publicaciones where propietario='" + nickname + "' and idPublicacion='" + pub + "'");

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static int insertPublicacion(String idPublicacion, String fechaPublicacion, String propietario,
			String tweet) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn()
					.prepareStatement("insert into Publicaciones (idPublicacion, fechaPublicacion, propietario, tweet) "
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
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet selectPublicaciones(String nickname) {
		String sql = "SELECT idPublicacion,propietario,fechaPublicacion,tweet FROM Publicaciones where propietario='"
				+ nickname + "'";
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(sql);
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}

	public static int agregarAmigo(String nickname, String nickAmigo) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement("insert into Amigos (nickname, nickAmigo) " + "values(?,?)");
			ps.setString(1, nickname);

			ps.setString(2, nickAmigo);

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static int borrarAmigo(String nickamigo, String nickname) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(
					"delete from Amigos where nickname='" + nickname + "' and nickamigo='" + nickamigo + "'");

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet selectAmigos(String nickname) {
		String sql = "select * from Usuarios where nickname in (select nickAmigo from Amigos where nickname ='"
				+ nickname + "')";
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(sql);
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}

	public static ResultSet buscarAmigos(String patron) {
		String sql = "select * from Usuarios where nickname like '%" + patron + "%'";
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(sql);
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}

	public static int borrarPerfil(String nickname) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement("delete from Usuarios where nickname='" + nickname + "'");

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet buscarEstadosContenido(String patron) {
		String sql = "select * from Publicaciones where tweet like '%" + patron + "%'";
		// select * from Publicaciones where tweet like '%numero%' and propietario <> 'Alba1';
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(sql);
			return ps.executeQuery();
		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}
}
