package moveration.bot.io;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import moveration.bot.data.Role;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Log
public class DBReader {

	private final Connection connection;

	@SneakyThrows
	public DBReader(Long guildId) {
		val file = PathResolver.getDBFile(guildId);
		connection = DriverManager.getConnection(String.format("jdbc:sqlite:C:%s", file.toString()));
		if (!Files.exists(file)) {
			log.info(String.format("Database for guild %d doesn't yet exist. Creating new one", guildId));
			createNewDB();
		}
	}

	@SneakyThrows
	private void createNewDB() {
		val s =
				"""
						CREATE TABLE IF NOT EXISTS roles (
						id INTEGER PRIMARY KEY,
						roleName TEXT NOT NULL,
						rolePower INTEGER 
						)			
						""";
		val statement = connection.createStatement();
		statement.executeQuery(s);
	}

	@SneakyThrows
	private ImmutableList<Role> parseRoles() {
		val s = """
				                SELECT * FROM roles
				""";
		val statement = connection.createStatement();
		val result = statement.executeQuery(s);
		val list = new ArrayList<Role>();
		while (result.next()) {
			list.add(new Role(result.getString("roleName"), result.getInt("rolePower")));
		}
		return ImmutableList.copyOf(list);
	}

	@SneakyThrows
	private void saveRoles(List<Role> roles) {
		connection.createStatement().executeQuery("""
					DELETE * FROM roles
				""");
		roles.forEach(new Consumer<>() {
			@SneakyThrows
			@Override
			public void accept(Role role) {
				val s = """
							INSERT INTO roles(roleName,rolePower) VALUES(?,?)
						""";
				val prepStatement = connection.prepareStatement(s);
				prepStatement.setString(1, role.getName());
				prepStatement.setInt(2, role.getPower());
				prepStatement.executeUpdate();
			}
		});
	}

	@SneakyThrows
	public void close() {
		connection.close();
	}
}
