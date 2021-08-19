package moveration.bot.data;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.WriterConfig;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import moveration.bot.io.PathResolver;

import java.nio.file.Files;
import java.nio.file.Path;

public class Info implements DiskResource {

	private final Path infoFilePath;
	private int dbVersion;
	private int infoVersion;
	private String prefix;

	public Info(long guildId) {
		infoFilePath = PathResolver.getInfoFile(guildId);
	}

	@SneakyThrows
	@Override
	public void createNew() {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", 1);
		jsonObject.add("InfoVersion", 1);
		jsonObject.add("Prefix", "M!");
		@Cleanup val writer = Files.newBufferedWriter(infoFilePath);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}

	@Override
	public boolean exists() {
		return Files.exists(infoFilePath) &&
		       Files.isReadable(infoFilePath) &&
		       Files.isWritable(infoFilePath);
	}

	@SneakyThrows
	@Override
	public void delete() {
		Files.delete(infoFilePath);
	}

	@SneakyThrows
	@Override
	public boolean isValid() {
		@Cleanup val reader = Files.newBufferedReader(infoFilePath);
		try {
			Json.parse(reader);
			return true;
		} catch (ParseException ignored) {
			return false;
		}
	}

	@SneakyThrows
	@Override
	public void load() {
		@Cleanup val reader = Files.newBufferedReader(infoFilePath);
		val object = Json.parse(reader).asObject();
		this.dbVersion = object.get("DbVersion").asInt();
		this.infoVersion = object.get("InfoVersion").asInt();
		this.prefix = object.get("Prefix").asString();
	}

	@SneakyThrows
	@Override
	public void write() {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", dbVersion);
		jsonObject.add("InfoVersion", infoVersion);
		jsonObject.add("Prefix", prefix);
		@Cleanup val writer = Files.newBufferedWriter(infoFilePath);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}

	@Override
	public Path getPath() {
		return infoFilePath;
	}
}
