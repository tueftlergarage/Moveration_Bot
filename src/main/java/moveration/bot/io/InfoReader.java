package moveration.bot.io;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import moveration.bot.data.Info;

import java.nio.file.Files;
import java.nio.file.Path;

@Log
public class InfoReader {

	private final JsonObject object;
	private final Path file;

	@SneakyThrows
	public InfoReader(Long guildId) {
		val file = PathResolver.getInfoFile(guildId);
		this.file = file;
		if (!Files.exists(file)) {
			log.info("Info-file for guild %d doesn't exists. Creating new one");
			createInfo(file);
		}
		@Cleanup val reader = Files.newBufferedReader(file);
		object = Json.parse(reader).asObject();
	}

	@SneakyThrows
	private void createInfo(Path file) {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", 1);
		jsonObject.add("InfoVersion", 1);
		jsonObject.add("Prefix", "M!");
		@Cleanup val writer = Files.newBufferedWriter(file);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}

	public Info parseInfo() {
		return new Info(object.get("DbVersion").asInt(), object.get("InfoVersion").asInt(), object.get("Prefix").asString());
	}

	@SneakyThrows
	public void writeInfo(Info info) {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", info.getDbVersion());
		jsonObject.add("InfoVersion", info.getInfoVersion());
		jsonObject.add("Prefix", info.getPrefix());
		@Cleanup val writer = Files.newBufferedWriter(file);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}

}
