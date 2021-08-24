package moveration.bot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class Assert {

	public void allNotNull(@Nullable Object... objects) {
		if (Arrays.stream(objects).anyMatch(Objects::isNull))
			throw new NullPointerException("Object was null");
	}

	public void allNotEmpty(@NotNull String... strings) {
		if (Arrays.stream(strings).anyMatch(String::isEmpty))
			throw new NullPointerException("String was empty");
	}
}
