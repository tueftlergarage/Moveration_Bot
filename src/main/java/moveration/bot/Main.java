package moveration.bot;

import lombok.SneakyThrows;
import lombok.val;
import moveration.bot.commands.ChangePrefix;
import moveration.bot.commands.HelloWorld;
import moveration.bot.commands.Help;
import moveration.bot.commands.MoveTestCommand;
import moveration.bot.data.DataManager;
import moveration.bot.io.TokenReader;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

	@SneakyThrows
	public static void main(String[] args) {
		DataManager.loadData();
		val jda = JDABuilder.createDefault(TokenReader.getToken()).build();
		val eventManager = new EventManager();
		eventManager.registerCommand(new HelloWorld());
		eventManager.registerCommand(new MoveTestCommand());
		eventManager.registerCommand(new Help());
		eventManager.registerCommand(new ChangePrefix());
		jda.addEventListener(eventManager);
	}
}