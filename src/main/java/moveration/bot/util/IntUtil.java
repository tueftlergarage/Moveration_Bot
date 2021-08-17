package moveration.bot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntUtil {

	public boolean isInteger(String s) {
		return s.matches("[0-9]+");
	}
}
