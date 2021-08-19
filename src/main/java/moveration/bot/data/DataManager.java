package moveration.bot.data;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import moveration.bot.io.PathResolver;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Getter
@Log
public class DataManager {

	private final List<Guild> guilds = new ArrayList<>();

	public Guild getGuild(long guildId) {
		return guilds.stream().filter(guild -> guild.getGuildId() == guildId).findFirst().orElseThrow(NullPointerException::new);
	}

	@SneakyThrows
	public void loadData() {
		preparePath();
		Files.list(PathResolver.getGuildFoldersPath()).forEach(path -> {
			if (!Files.isDirectory(path)) log.info(String.format("Skipping non-folder %s", path));
			else guilds.add(new Guild(Long.parseLong(path.getFileName().toString())));
		});
	}

	@SneakyThrows
	private void preparePath() {
		Files.createDirectories(PathResolver.getGuildFoldersPath());
	}
}
