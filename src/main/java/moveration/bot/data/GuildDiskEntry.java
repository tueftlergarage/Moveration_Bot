package moveration.bot.data;

import java.nio.file.Path;

public record GuildDiskEntry(Guild guild, Info info, String dbChecksum, Path guildFolder) {
}
