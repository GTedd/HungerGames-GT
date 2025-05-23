package com.shanebeestudios.hg.api.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Data class for holding a {@link Game Game's} commands
 */
@SuppressWarnings("unused")
public class GameCommandData extends Data {

    private List<String> commands = null;

    protected GameCommandData(Game game) {
        super(game);
    }

    /**
     * Set the list of a commands to run for this game
     * <p><b>format = </b> "type:command"</p>
     * <p><b>types = </b> start, stop, death, join</p>
     *
     * @param commands List of commands
     */
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    /**
     * Add a command to the list of commands for this game
     *
     * @param command The command to add
     * @param type    The type of the command
     */
    public void addCommand(String command, CommandType type) {
        this.commands.add(type.getName() + ":" + command);
    }

    /**
     * Get all the commands for this data
     *
     * @return All commands
     */
    public List<String> getCommands() {
        return this.commands;
    }

    /**
     * Run commands for this game that are defined in the arenas.yml
     *
     * @param commandType Type of command to run
     * @param player      The player involved (can be null)
     */
    @SuppressWarnings("ConstantConditions")
    public void runCommands(CommandType commandType, @Nullable Player player) {
        if (commands == null) return;
        for (String command : commands) {
            String type = command.split(":")[0];
            if (!type.equals(commandType.getName())) continue;
            if (command.equalsIgnoreCase("none")) continue;
            command = command.split(":")[1]
                .replace("<world>", this.game.getGameArenaData().getGameRegion().getWorld().getName())
                .replace("<arena>", this.game.getGameArenaData().getName());
            if (player != null) {
                command = command.replace("<player>", player.getName());
            }
            if (commandType == CommandType.START && command.contains("<player>")) {
                for (Player player1 : this.game.getGamePlayerData().getPlayers()) {
                    String newCommand = command.replace("<player>", player1.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), newCommand);
                }
            } else
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    /**
     * Command types
     */
    public enum CommandType {
        /**
         * A command to run when a player dies in game
         */
        DEATH("death"),
        /**
         * A command to run at the start of a game
         */
        START("start"),
        /**
         * A command to run at the end of a game
         */
        STOP("stop"),
        /**
         * A command to run when a player joins a game
         */
        JOIN("join");

        final String type;

        CommandType(String type) {
            this.type = type;
        }

        /**
         * Get the name of this command type
         *
         * @return Name of type
         */
        public String getName() {
            return type;
        }
    }

}
