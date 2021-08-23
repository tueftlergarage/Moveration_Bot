package moveration.bot.data;

import lombok.Getter;

@Getter
public class Guild {						//validates if there is any database for the given guildid, if not creates one, and loads it

	private final long guildId;
	private final Database database;
	private final Info info;

	public Guild(long guildId) {
		this.guildId = guildId;
		this.database = new Database(guildId);
		this.info = new Info(guildId);
		if (!database.exists())
			database.createNew();
		if (!info.exists())
			info.createNew();
		if (!database.isValid())
			throw new IllegalArgumentException(String.format("Invalid database-file for guild %d", guildId));
		database.load();
		if (!info.isValid())
			throw new IllegalArgumentException(String.format("Invalid info-file for guild %d", guildId));
		info.load();
	}

}
