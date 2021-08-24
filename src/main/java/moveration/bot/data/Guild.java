package moveration.bot.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * The main storage for a guild, i.e. a server
 */
@Getter
@Setter
@AllArgsConstructor
public class Guild {

	private final long guildId;
	private final List<Role> roles;
	private String prefix;

}
