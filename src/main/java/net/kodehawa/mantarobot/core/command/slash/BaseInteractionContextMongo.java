package net.kodehawa.mantarobot.core.command.slash;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.kodehawa.mantarobot.core.command.helpers.IContextMongo;
import net.kodehawa.mantarobot.core.command.i18n.I18nContext;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.dbold.ManagedDatabase;
import net.kodehawa.mantarobot.dbold.entities.MantaroObject;
import net.kodehawa.mantarobot.utils.commands.UtilsContext;

import java.util.Collection;

@SuppressWarnings("unused")
public abstract class BaseInteractionContextMongo<T extends GenericCommandInteractionEvent> extends BaseInteractionContext<T> implements IContextMongo {
    protected final ManagedDatabase managedDatabase = MantaroData.db();

    public BaseInteractionContextMongo(T event, I18nContext i18n) {
        super(event, i18n);
    }

    @Override
    public ManagedDatabase db() {
        return managedDatabase;
    }


    @Override
    public MantaroObject getMantaroData() {
        return managedDatabase.getMantaroData();
    }

    public boolean isUserBlacklisted(String id) {
        return getMantaroData().getBlackListedUsers().contains(id);
    }
}
