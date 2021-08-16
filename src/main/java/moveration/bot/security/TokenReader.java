package moveration.bot.security;

import lombok.experimental.UtilityClass;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class TokenReader {

    @SneakyThrows
    public String getToken() {
        val tokenPath = Path.of(System.getProperty("user.home"), "moveration_files", "bot_token.txt");
        if(!Files.exists(tokenPath) || !Files.isReadable(tokenPath)) throw new NullPointerException("Unable to read token-file");
        @Cleanup val reader = Files.newBufferedReader(tokenPath);
        return reader.readLine();
    }

}