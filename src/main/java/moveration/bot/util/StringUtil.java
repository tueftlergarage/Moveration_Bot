package moveration.bot.util;

import lombok.experimental.UtilityClass;
import moveration.bot.data.DataManager;
import moveration.bot.data.Guild;

@UtilityClass
public class StringUtil {

	public String stripPrefix(String s, Guild guild) {
		return s.substring(DataManager.getGuildEntry(guild.getGuildId()).info().prefix().length());
	}
}
