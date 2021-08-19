package moveration.bot.data;

import lombok.Getter;
import moveration.bot.io.DBReader;
import moveration.bot.io.InfoReader;

//TODO add db-datatype
@Getter
public class Guild {

	private final long guildId;
	private final InfoReader infoReader;
	private final DBReader dbReader;
	private final Info info;

	public Guild(Long guildId) {
		this.guildId = guildId;
		this.infoReader = new InfoReader(guildId);
		this.dbReader = new DBReader(guildId);
		this.info = infoReader.parseInfo();
	}

}
