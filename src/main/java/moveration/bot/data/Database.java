package moveration.bot.data;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import moveration.bot.io.PathResolver;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Database implements DiskResource {

	private final Connection connection;
	private final Path databaseFile;
	private final Path checksumFile;
	private final List<Role> roles = new ArrayList<>();

	@SneakyThrows
	public Database(long guildId) {
		this.databaseFile = PathResolver.getDBFile(guildId);
		this.checksumFile = PathResolver.getChecksumFile(guildId);
		this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:C:%s", databaseFile.toString()));
	}

	@SneakyThrows
	@Override
	public void createNew() {
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

	@Override
	public boolean exists() {
		return Files.exists(databaseFile) &&
		       Files.isReadable(databaseFile) &&
		       Files.isWritable(databaseFile);
	}

	@SneakyThrows
	@Override
	public void delete() {
		Files.delete(databaseFile);
	}

	@SneakyThrows
	@Override
	public boolean isValid() {
		@Cleanup val stream = Files.newInputStream(databaseFile);
		@Cleanup val reader = Files.newBufferedReader(checksumFile);
		val savedChecksum = reader.readLine();
		val actualChecksum = DigestUtils.md5Hex(stream);
		return savedChecksum.equals(actualChecksum);
	}

	@SneakyThrows
	@Override
	public void load() {
		val s = """
				                SELECT * FROM roles
				""";
		val statement = connection.createStatement();
		val result = statement.executeQuery(s);
		while (result.next()) {
			roles.add(new Role(result.getString("roleName"), result.getInt("rolePower")));
		}
	}

	@SneakyThrows
	@Override
	public void write() {
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
		@Cleanup val stream = Files.newInputStream(databaseFile);
		@Cleanup val writer = Files.newBufferedWriter(checksumFile);
		writer.write(DigestUtils.md5Hex(stream));
	}

	@Override
	public Path getPath() {
		return databaseFile;
	}
}