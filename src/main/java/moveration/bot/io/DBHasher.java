package moveration.bot.io;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class DBHasher {

	@SneakyThrows
	public String hashDB(Long guildId) {
		final Path db = PathResolver.getDBFile(guildId);
		if (!Files.exists(db)) throw new NullPointerException("Can't hash a non-existent db");
		@Cleanup val stream = Files.newInputStream(db);
		return DigestUtils.md5Hex(stream);
	}
}
