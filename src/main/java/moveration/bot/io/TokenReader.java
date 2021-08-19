package moveration.bot.io;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.nio.file.Files;

@UtilityClass
public class TokenReader {

	@SneakyThrows
	public String getToken() {
		if (!Files.exists(PathResolver.getTokenFile()) || !Files.isReadable(PathResolver.getTokenFile()))
			throw new NullPointerException("Unable to read token-file");
		@Cleanup val reader = Files.newBufferedReader(PathResolver.getTokenFile());
		return reader.readLine();
	}

}