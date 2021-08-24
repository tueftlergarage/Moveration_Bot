package moveration.bot.io.operations;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import moveration.bot.data.Role;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * List of operations that are useful for accessing an on-disk sqlite database
 *
 * @author Jonas Mohr
 */
@UtilityClass
public class DBOperations {

	/**
	 * Generate a new sqlite database by creating the file first and then creating the required tables
	 *
	 * @param file the file the database is to be stored in
	 */
	@SneakyThrows
	public void generateDatabase(Path file) {
		val connection = connect(file);
		connection.createStatement().executeQuery("""
				                CREATE TABLE IF NOT EXISTS Roles (
				                       roleName TEXT NOT NULL,
				                       rolePower INTEGER NOT NULL
				                       )			
				""");
		connection.createStatement().executeQuery("""
								CREATE TABLE IF NOT EXISTS Spending (
				                        user TEXT NOT NULL,
				                        roleName TEXT NOT NULL,
				                        spent INTEGER NOT NULL 
				                        )	
				""");
		connection.close();
	}

	/**
	 * Establish a connection to an on-disk sqlite database
	 *
	 * @param file the file the database in stored in
	 * @return a connection to the sqlite database
	 */
	@SneakyThrows
	private Connection connect(Path file) {
		return DriverManager.getConnection(String.format("jdbc:sqlite:C:%s", file.toAbsolutePath()));
	}

	/**
	 * Load a list of roles from an on-disk sqlite database
	 * The table the roles are stored in is called "Roles"
	 * The table has the following signature
	 * <table>
	 * <tr><th>roleName TEXT NOT NULL <th>rolePower INTEGER NOT NULL
	 * <tr><td>roleName<td>rolePower
	 * </table>
	 *
	 * @param file the file the database in stored in
	 */
	@SneakyThrows
	public List<Role> loadRoles(Path file) {
		val connection = connect(file);
		val set = connection.createStatement().executeQuery("""
								SELECT * FROM Roles  
				""");
		val roles = new ArrayList<Role>();
		while (set.next())
			roles.add(new Role(set.getString("roleName"), set.getInt("rolePower")));
		return roles;
	}

	/**
	 * Save a list of roles to an on-disk sqlite database
	 *
	 * @param file  the file the database in stored in
	 * @param roles the roles to save
	 * @implNote This currently only supports updating the power from and already existent role
	 * @see DBOperations#loadRoles(Path) for specifics about how the sqlite database is implemented
	 */
	public void saveRoles(Path file, List<Role> roles) {
		val connection = connect(file);
		val sql = """
				                            UPDATE Roles SET rolePower = ? WHERE roleName = ?
				""";
		roles.forEach(new Consumer<Role>() {
			@SneakyThrows
			@Override
			public void accept(Role role) {
				val statement = connection.prepareStatement(sql);
				statement.setInt(1, role.getPower());
				statement.setString(2, role.getName());
			}
		});
	}
}
