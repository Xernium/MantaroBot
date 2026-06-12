/*
 * Copyright (C) 2016 Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 *
 */

package net.kodehawa.mantarobot.core.command.compat;

import net.kodehawa.mantarobot.core.command.helpers.*;

import java.util.List;

public class AliasCommand<T extends IContextBase> implements Command<T> {
    private final Command<T> command;
    private final String commandName;
    private final String originalName;
    private final List<String> aliases;

    public AliasCommand(String commandName, String originalName, Command<T> command) {
        this.commandName = commandName;
        this.command = command;
        this.originalName = originalName;
        this.aliases = command.getAliases();
    }

    @SuppressWarnings("unused")
    public CommandCategory parentCategory() {
        return command.category();
    }

    public String parentName() {
        return originalName;
    }

    @Override
    public CommandCategory category() {
        return null; //Alias Commands are hidden
    }

    @Override
    public CommandPermission permission() {
        return command.permission();
    }

    @Override
    public void run(T context, String ignored, String content) {
        command.run(context, commandName, content);
    }

    @Override
    public HelpContent help() {
        return command.help();
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    public Command<T> getCommand() {
        return this.command;
    }

    @SuppressWarnings("unused")
    public String getCommandName() {
        return this.commandName;
    }

    public String getOriginalName() {
        return this.originalName;
    }
}
