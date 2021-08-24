package moveration.bot.io.operations;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * List of operations that are useful for accessing checksums
 */
@UtilityClass
public class ChecksumOperations {

	/**
	 * Generate the md5-hash of the given file
	 *
	 * @param file The file to hash
	 * @return The md5-hash as string
	 */
	@SneakyThrows
	public String generateChecksum(Path file) {
		@Cleanup val stream = Files.newInputStream(file);
		return DigestUtils.md5Hex(stream);
	}

	/**
	 * Load the md5-hash of the given file
	 *
	 * @param file The file to hash
	 * @return The md5-hash as string
	 */
	@SneakyThrows
	public String loadChecksum(Path file) {
		return Files.readString(file);
	}
}
