package moveration.bot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

	public String stripPrefix(String s) {
		return s.substring(Constants.getPrefix().length());
	}

}
