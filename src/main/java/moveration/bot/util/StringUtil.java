package moveration.bot.util;

import lombok.experimental.UtilityClass;
import moveration.bot.Constants;

@UtilityClass
public class StringUtil {

	public String stripPrefix(String s) {
		return s.substring(Constants.getPrefix().length());
	}

}
