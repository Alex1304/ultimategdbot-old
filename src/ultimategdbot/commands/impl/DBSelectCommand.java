package ultimategdbot.commands.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Command that allows granted users to execute SELECT SQL queries.
 */
public class DBSelectCommand extends CoreCommand {

	public DBSelectCommand(EnumSet<BotRoles> rolesRequired) {
		super("dbselect", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			ResultSet result = execute("SELECT " + AppTools.concatCommandArgs(args));
			AppTools.sendMessage(event.getChannel(), formatResults(result));
		} catch (SQLException e) {
			throw new CommandFailedException("Query failed\n```\n" + e.getLocalizedMessage() + "\n```");
		}
	}
	
	private ResultSet execute(String query) throws SQLException {
		Statement s = DatabaseConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		return s.executeQuery(query);
	}
	
	private String formatResults(ResultSet result) throws SQLException {
		int rowcount = 0;
		if (result.last()) {
			rowcount = result.getRow();
			result.beforeFirst();
		}
		
		String output = "Total entries found: " + rowcount + "\n\n```\n";
		
		for (int i = 1 ; i <= result.getMetaData().getColumnCount() ; i++)
			output += " " + fixedLength(result.getMetaData().getColumnName(i)) + " |";
		
		output += "\n";
		
		int row = 0;
		while (row < 20 && result.next()) {
			for (int i = 1 ; i <= result.getMetaData().getColumnCount() ; i++)
				output += " " + fixedLength(result.getString(i)) + " |";
			output += "\n";
			row++;
		}
		
		output += "```";
		return output;
	}
	
	private String fixedLength(String s) {
		final int num = 18;
		
		if (s == null)
			s = "";
		
		if (s.length() == num)
			return s;
		if (s.length() > num)
			return s.substring(0, num);
		
		while (s.length() < num)
			s += " ";
		
		return s;
	}

	@Override
	public String getHelp() {
		return "Executes a database SELECT query and displays the results.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "<select_query>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "* FROM awarded_level", "COUNT(*) FROM guild_settings" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
