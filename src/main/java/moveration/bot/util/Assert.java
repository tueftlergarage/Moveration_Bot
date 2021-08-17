package moveration.bot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class Assert {

	public static void allNotNull(@Nullable Object... objects) {
		if (Arrays.stream(objects).anyMatch(Objects::isNull))
			throw new NullPointerException("Object was null");
	}

}
