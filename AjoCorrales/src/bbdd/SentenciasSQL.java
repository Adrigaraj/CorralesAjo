package bbdd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import javassist.bytecode.stackmap.TypeData.ClassName;

public class SentenciasSQL {
	private static final Logger log = Logger.getLogger(ClassName.class.getName());

	public static ResultSet selectUsuarios() {
		String sql = "SELECT nickname, nombreCompleto FROM Usuarios order by fechaAlta";
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

	public static ResultSet selectAmigosPatron(String nickname, String patron) {
		String sql = "select nickname,nombreCompleto from Usuarios where nickname in (select nickAmigo from Amigos where nickname ='"
				+ nickname + "') and nickname like '%" + patron + "%' or nombreCompleto like '%" + patron + "%'";
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

	public static ResultSet selectAmigos(String nickname) {
		String sql = "select nickname,nombreCompleto from Usuarios where nickname in (select nickAmigo from Amigos where nickname ='"
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

	public static ResultSet buscarAmigos(String nickname, String patron) {
		String sql = "select nickname,nombreCompleto from Usuarios where nickname like '%" + patron
				+ "%' and nickname not in (select nickAmigo from Amigos where nickname = '" + nickname
				+ "') and nickname not in (select nickname from Amigos where nickAmigo = '" + nickname + "')";
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
			ConexionBBDD.getConn().prepareStatement("delete from Amigos where nickname='" + nickname + "'")
					.executeUpdate();
			ConexionBBDD.getConn().prepareStatement("delete from Amigos where nickamigo='" + nickname + "'")
					.executeUpdate();
			ConexionBBDD.getConn().prepareStatement("delete from Publicaciones where propietario='" + nickname + "'")
					.executeUpdate();
			ps = ConexionBBDD.getConn().prepareStatement("delete from Usuarios where nickname='" + nickname + "'");

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet buscarEstadosContenido(String patron, String nickname) {
		String sql = "select * from Publicaciones where tweet like '%" + patron + "%' and propietario != '" + nickname
				+ "' or idPublicacion like '%" + patron + "%'";
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

	public static int actualizarPerfil(String nickname, String nombreCompleto, String pais, String fechaNacimiento,
			String correo) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(
					"UPDATE Usuarios SET nombreCompleto=?, pais=?, fechaNacimiento=?, correo=? where nickname = ?");
			ps.setString(1, nombreCompleto);
			ps.setString(2, pais);
			ps.setString(3, fechaNacimiento);
			ps.setString(4, correo);
			ps.setString(5, nickname);

			return ps.executeUpdate();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return seRs.getErrorCode();
		}
	}

	public static ResultSet getAppMovil(String nickname) {
		PreparedStatement ps = null;
		try {
			ps = ConexionBBDD.getConn().prepareStatement(
					"select distinct(us.nickname), us.nombreCompleto, us.pais, us.fechaNacimiento, us.correo, us.fechaAlta, "
							+ "pu.idPublicacion, pu.fechaPublicacion, pu.propietario, pu.tweet ultimoEstado, (select count(nickAmigo) from Amigos where nickname = '"
							+ nickname + "') numAmigos,"
							+ "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 0)"
							+ ") as tweet1," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 1)"
							+ ") as tweet2," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 2)"
							+ ") as tweet3," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 3)"
							+ ") as tweet4," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 4)"
							+ ") as tweet5," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 5)"
							+ ") as tweet6," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 6)"
							+ ") as tweet7," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 7)"
							+ ") as tweet8," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 8)"
							+ ") as tweet9," + "(select distinct(tweet) from Publicaciones where idPublicacion = "
							+ "	(select idPublicacion from Publicaciones where propietario in "
							+ "		(select a.nickAmigo from Amigos a where a.nickname = '" + nickname
							+ "') order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1 offset 9)"
							+ ") as tweet10" + "from (Usuarios as us, Publicaciones as pu, Amigos as am)"
							+ "where us.nickname = '" + nickname + "' "
							+ "and pu.idPublicacion = (select idPublicacion from Publicaciones where propietario = '"
							+ nickname + "' "
							+ "order by substring(fechaPublicacion, 7, 4) DESC, substring(fechaPublicacion, 4, 2) DESC, substring(fechaPublicacion, 1, 2) DESC limit 1);");
			ps.setString(1, nickname);

			return ps.executeQuery();

		} catch (SQLException seRs) {
			log.error("Code: " + seRs.getErrorCode());
			log.error("SqlState: " + seRs.getSQLState());
			log.error("Error Message: " + seRs.getMessage());
			return null;
		}
	}
}
