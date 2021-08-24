package moveration.bot.io.operations;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.WriterConfig;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import moveration.bot.data.Info;

import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class InfoOperations {

	@SneakyThrows
	public void createNew(Path file) {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", 1);
		jsonObject.add("InfoVersion", 1);
		jsonObject.add("Prefix", "M!");
		@Cleanup val writer = Files.newBufferedWriter(file);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}

	@SneakyThrows
	public boolean isValid(Path file) {
		@Cleanup val reader = Files.newBufferedReader(file);
		try {
			Json.parse(reader);
			return true;
		} catch (ParseException ignored) {
			return false;
		}
	}

	@SneakyThrows
	public Info load(Path file) {
		@Cleanup val reader = Files.newBufferedReader(file);
		val object = Json.parse(reader).asObject();
		return new Info(object.get("DbVersion").asInt(), object.get("InfoVersion").asInt(), object.get("Prefix").asString());
	}

	@SneakyThrows
	public void save(Info info, Path file) {
		val jsonObject = new JsonObject();
		jsonObject.add("DbVersion", info.dbVersion());
		jsonObject.add("InfoVersion", info.infoVersion());
		jsonObject.add("Prefix", info.prefix());
		@Cleanup val writer = Files.newBufferedWriter(file);
		jsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);
	}
}
