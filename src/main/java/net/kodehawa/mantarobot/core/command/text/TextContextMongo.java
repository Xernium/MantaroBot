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

package net.kodehawa.mantarobot.core.command.text;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kodehawa.mantarobot.core.command.argument.split.StringSplitter;
import net.kodehawa.mantarobot.core.command.helpers.IContextMongo;
import net.kodehawa.mantarobot.core.command.i18n.I18nContext;
import net.kodehawa.mantarobot.data.Config;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.dbold.ManagedDatabase;
import net.kodehawa.mantarobot.dbold.entities.MantaroObject;
import net.kodehawa.mantarobot.utils.commands.CustomFinderUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class TextContextMongo extends TextContext implements IContextMongo {
    private final ManagedDatabase managedDatabase = MantaroData.db();

    public TextContextMongo(@Nonnull MessageReceivedEvent event, @Nonnull I18nContext i18n, @Nonnull String contentAfterPrefix, boolean isMentionPrefix) {
        super(event, i18n, contentAfterPrefix, isMentionPrefix);
    }

    @Override
    public ManagedDatabase db() {
        return managedDatabase;
    }

    @Override
    public MantaroObject getMantaroData() {
        return managedDatabase.getMantaroData();
    }

    public void findMember(String query, Consumer<List<Member>> success) {
        CustomFinderUtil.lookupMember(getGuild(), this, query).onSuccess(s -> {
            try {
                success.accept(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
