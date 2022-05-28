/*
 * Copyright (C) 2016-2021 David Rubio Escares / Kodehawa
 *
 *  Mantaro is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  Mantaro is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 */

package net.kodehawa.mantarobot.core.modules.commands.i18n;

import net.dv8tion.jda.api.entities.User;
import net.kodehawa.mantarobot.data.I18n;
import net.kodehawa.mantarobot.db.entities.DBGuild;
import net.kodehawa.mantarobot.db.entities.DBUser;
import net.kodehawa.mantarobot.db.entities.helpers.UserData;

public class I18nContext {
    private DBGuild guild;
    private DBUser user;
    private I18n i18n = null;

    public I18nContext(DBGuild guildData, DBUser user) {
        this.guild = guild;
        this.user = user;
    }

    public I18nContext(I18n i18n) {
        this.i18n = i18n;
    }

    public I18nContext() { }


    public String get(String s) {
        I18n context = I18n.getForLanguage(getContextLanguage());
        return context.get(s);
    }

    public String withRoot(String root, String s) {
        I18n context = I18n.getForLanguage(getContextLanguage());
        return context.withRoot(root, s);
    }

    public String getContextLanguage() {
        if (i18n != null) {
            return i18n.getLanguage();
        }

        if (guild == null && user == null) {
            return "en_US";
        }

        String lang;
        if (user == null)
            lang = guild.getLang();
        else
            lang = user.getLang() == null || user.getLang().isEmpty() ? guild.getLang() : user.getLang();

        I18n context = I18n.getForLanguage(lang);
        return context == null ? "en_US" : lang;
    }
}
