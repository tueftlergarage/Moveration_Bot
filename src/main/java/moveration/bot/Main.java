package moveration.bot;

import lombok.SneakyThrows;
import lombok.val;
import moveration.bot.commands.Command;
import moveration.bot.data.DataManager;
import moveration.bot.io.TokenReader;
import net.dv8tion.jda.api.JDABuilder;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public class Main {

	@SneakyThrows
	public static void main(String[] args) {
		DataManager.loadData();
		val jda = JDABuilder.createDefault(TokenReader.getToken()).build();
		val eventManager = new EventManager();
		new Reflections().getSubTypesOf(Command.class).forEach(clazz -> {
			try {
				eventManager.registerCommand(clazz.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		});
		jda.addEventListener(eventManager);
	}
}