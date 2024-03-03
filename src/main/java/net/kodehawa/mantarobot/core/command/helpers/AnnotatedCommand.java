package net.kodehawa.mantarobot.core.command.helpers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.kodehawa.mantarobot.core.command.helpers.CommandCategory;
import net.kodehawa.mantarobot.core.command.helpers.CommandPermission;
import net.kodehawa.mantarobot.core.command.helpers.HelpContent;
import net.kodehawa.mantarobot.core.command.meta.Category;
import net.kodehawa.mantarobot.core.command.meta.Help;
import net.kodehawa.mantarobot.core.command.meta.Name;
import net.kodehawa.mantarobot.core.command.meta.Permission;
import net.kodehawa.mantarobot.core.command.helpers.IContext;
import net.kodehawa.mantarobot.db.rel.help.DataAccess;
import net.kodehawa.mantarobot.db.rel.help.DataMode;
import net.kodehawa.mantarobot.db.rel.help.DataResult;
import net.kodehawa.mantarobot.db.rel.help.DatabaseRollbackException;
import net.kodehawa.mantarobot.db.rel.meta.DataAccessMode;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AnnotatedCommand<T extends IContext> {
    protected CommandCategory category;

    protected final String name;
    protected final CommandPermission permission;
    protected HelpContent help;
    protected final DataMode dataMode;

    public AnnotatedCommand() {
        var clazz = getClass();
        if (clazz.getAnnotation(Name.class) != null) {
            this.name = clazz.getAnnotation(Name.class).value();
        } else {
            this.name = clazz.getSimpleName().toLowerCase();
        }
        var c = clazz.getAnnotation(Category.class);
        if (c == null) {
            this.category = null;
        } else {
            this.category = c.value();
        }

        var p = clazz.getAnnotation(Permission.class);
        if (p == null) {
            this.permission = getDefaultPermission();
        } else {
            this.permission = p.value();
        }

        var l = clazz.getAnnotation(DataAccessMode.class);
        if (l == null) {
            this.dataMode = DataMode.getDefault();
        } else {
            this.dataMode = l.value();
        }

        var h = clazz.getAnnotation(Help.class);
        if (h == null) {
            this.help = new HelpContent.Builder().build();
        } else {
            var builder = new HelpContent.Builder()
                    .setDescription(h.description().isBlank() ? null : h.description())
                    .setUsage(h.usage().isBlank() ? null : h.usage())
                    .setRelated(Arrays.asList(h.related()))
                    .setParameters(Arrays.asList(h.parameters()))
                    .setSeasonal(h.seasonal());
            this.help = builder.build();
        }
    }

    protected CommandPermission getDefaultPermission() {
        return CommandPermission.USER;
    }

    public String getName() {
        return name;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public CommandPermission getPermission() {
        return permission;
    }

    public HelpContent getHelp() {
        return help;
    }

    @SuppressWarnings("unused")
    public abstract Throwable execute(T ctx, Jdbi dbCon);
    protected final Throwable preProcess(T ctx, Jdbi dbCon) {
        if (dataMode.isDataAccess()) {
            AtomicReference<Throwable> t = new AtomicReference<>();
            dbCon.inTransaction(dataMode.getLevel(), handle -> {
                try {
                    DataResult r = process(ctx, new DataAccess(handle, dataMode));
                    if (r == DataResult.ROLLBACK) {
                        DatabaseRollbackException.throwThis();
                    }
                    return r == DataResult.COMMIT_SUCCESS;
                } catch (Throwable e) {
                    t.set(e);
                    DatabaseRollbackException.throwThis();
                }
                return false;
            });
            return t.get();
        } else {
            try {
                process(ctx, null);
                return null;
            } catch (Throwable t) {
                return t;
            }
        }
    }
    protected abstract DataResult process(T ctx, DataAccess dao);

    public void setCategory(CommandCategory category) {
        this.category = category;
    }

    public void setHelp(HelpContent help) {
        this.help = help;
    }

    protected EmbedBuilder baseEmbed(T ctx, String name) {
        return baseEmbed(ctx, name, ctx.getMember().getEffectiveAvatarUrl());
    }

    protected EmbedBuilder baseEmbed(T ctx, String name, String image) {
        return new EmbedBuilder()
                .setAuthor(name, null, image)
                .setColor(ctx.getMember().getColor())
                .setFooter("Requested by: %s".formatted(ctx.getMember().getEffectiveName()),
                        ctx.getGuild().getIconUrl()
                );
    }
}
