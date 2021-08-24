package moveration.bot.data;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import lombok.val;
import moveration.bot.io.PathResolver;
import moveration.bot.io.operations.ChecksumOperations;
import moveration.bot.io.operations.DBOperations;
import moveration.bot.io.operations.InfoOperations;
import moveration.bot.util.Observable;
import moveration.bot.util.Observer;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * The DataManager creates, loads and saves all available GuildDiskEntry
 *
 * @author Jonas Mohr
 */
@UtilityClass
@Getter
@Log
//TODO call observers
public class DataManager implements Observable<List<GuildDiskEntry>> {

	private final List<Observer<List<GuildDiskEntry>>> observers = new ArrayList<>();
	private final List<GuildDiskEntry> guilds = new ArrayList<>();

	public GuildDiskEntry getGuild(long guildId) {
		return guilds.stream()
				.filter(guild -> guild.guild().getGuildId() == guildId)
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}

	/**
	 * Create a new GuildDiskEntry by providing a guildId
	 *
	 * @param guildId the id of the new guild
	 * @return a new GuildDiskEntry
	 * @implNote this creates the necessary files, saves them to the disk and returns a GuildDiskEntry.
	 * @see GuildDiskEntry
	 */
	@SneakyThrows
	public GuildDiskEntry createNewGuild(long guildId) {
		val guildFolder = PathResolver.getGuildFolder(guildId);
		val infoFile = guildFolder.resolve("info.json");
		val dbFile = guildFolder.resolve("database.db");
		Files.createDirectory(guildFolder);
		InfoOperations.createNew(infoFile);
		val info = InfoOperations.load(infoFile);
		DBOperations.generateDatabase(dbFile);
		val guild = new Guild(guildId, DBOperations.loadRoles(dbFile));
		val checksum = ChecksumOperations.generateChecksum(dbFile);
		val entry = new GuildDiskEntry(guild, info, checksum, guildFolder);
		guilds.add(entry);
		notifyObservers(guilds);
		return entry;
	}

	/**
	 * Load all GuildDiskEntries from disk into memory
	 *
	 * @see GuildDiskEntry
	 */
	@SneakyThrows
	public void loadData() {
		preparePath();
		Files.list(PathResolver.getGuildFoldersPath()).forEach(path -> {
			if (!Files.isDirectory(path)) log.info(String.format("Skipping non-folder %s", path));
			else {
				val infoFile = path.resolve("info.json");
				val dbFile = path.resolve("database.db");
				val checksumFile = path.resolve("checksum.md5");
				val guild = new Guild(Long.parseLong(path.getFileName().toString()), DBOperations.loadRoles(dbFile));
				val info = InfoOperations.load(infoFile);
				val checksum = ChecksumOperations.loadChecksum(checksumFile);
				guilds.add(new GuildDiskEntry(guild, info, checksum, path));
			}
		});
	}

	/**
	 * Create the required directories
	 *
	 * @see PathResolver
	 */
	@SneakyThrows
	private void preparePath() {
		Files.createDirectories(PathResolver.getGuildFoldersPath());
	}

	@Override
	public void registerObserver(Observer<List<GuildDiskEntry>> observer) {
		observers.add(observer);
	}

	@Override
	public void deregisterObserver(Observer<List<GuildDiskEntry>> observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(List<GuildDiskEntry> list) {
		observers.forEach(o -> o.onNotification(list));
	}
}
