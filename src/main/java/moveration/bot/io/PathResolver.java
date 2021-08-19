package moveration.bot.io;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class PathResolver {

	private final Path basePath = Path.of(System.getProperty("user.home"), "moveration_files");

	public Path getGuildFolder(Long guildId) {
		return basePath.resolve("guilds").resolve(String.valueOf(guildId));
	}

	public Path getTokenFile() {
		return basePath.resolve("bot_token.txt");
	}

	public Path getDBFile(Long guildId) {
		return getGuildFolder(guildId).resolve("database.db");
	}

	public Path getChecksumFile(Long guildId) {
		return getGuildFolder(guildId).resolve("checksum.md5");
	}

	public Path getInfoFile(Long guildId) {
		return getGuildFolder(guildId).resolve("info.json");
	}

}
