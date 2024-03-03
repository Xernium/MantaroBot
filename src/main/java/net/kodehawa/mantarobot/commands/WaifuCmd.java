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

package net.kodehawa.mantarobot.commands;

import com.google.common.eventbus.Subscribe;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kodehawa.mantarobot.commands.currency.WaifuStats;
import net.kodehawa.mantarobot.commands.currency.item.ItemReference;
import net.kodehawa.mantarobot.commands.currency.item.ItemStack;
import net.kodehawa.mantarobot.commands.currency.profile.Badge;
import net.kodehawa.mantarobot.core.CommandRegistry;
import net.kodehawa.mantarobot.core.command.meta.Category;
import net.kodehawa.mantarobot.core.command.meta.Defer;
import net.kodehawa.mantarobot.core.command.meta.Description;
import net.kodehawa.mantarobot.core.command.meta.Help;
import net.kodehawa.mantarobot.core.command.meta.Name;
import net.kodehawa.mantarobot.core.command.meta.Options;
import net.kodehawa.mantarobot.core.command.slash.SlashCommand;
import net.kodehawa.mantarobot.core.command.slash.SlashContext;
import net.kodehawa.mantarobot.core.listeners.operations.ButtonOperations;
import net.kodehawa.mantarobot.core.listeners.operations.core.Operation;
import net.kodehawa.mantarobot.core.command.meta.Module;
import net.kodehawa.mantarobot.core.command.helpers.CommandCategory;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.db.rel.InventoryItem;
import net.kodehawa.mantarobot.db.rel.NewUser;
import net.kodehawa.mantarobot.db.rel.UserBadge;
import net.kodehawa.mantarobot.db.rel.help.DataAccess;
import net.kodehawa.mantarobot.db.rel.help.DataMode;
import net.kodehawa.mantarobot.db.rel.help.DataResult;
import net.kodehawa.mantarobot.db.rel.help.WaifuState;
import net.kodehawa.mantarobot.db.rel.meta.DataAccessMode;
import net.kodehawa.mantarobot.db.rel.Waifu;
import net.kodehawa.mantarobot.utils.commands.DiscordUtils;
import net.kodehawa.mantarobot.utils.commands.EmoteReference;
import net.kodehawa.mantarobot.utils.commands.ratelimit.IncreasingRateLimiter;
import net.kodehawa.mantarobot.utils.commands.ratelimit.RatelimitUtils;

import java.awt.Color;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Module
public class WaifuCmd {
    private static final IncreasingRateLimiter waifuRatelimiter = new IncreasingRateLimiter.Builder()
            .limit(1)
            .spamTolerance(2)
            .cooldown(5, TimeUnit.SECONDS)
            .maxCooldown(5, TimeUnit.SECONDS)
            .randomIncrement(true)
            .pool(MantaroData.getDefaultJedisPool())
            .prefix("waifu")
            .build();

    private static final IncreasingRateLimiter claimlockRatelimiter = new IncreasingRateLimiter.Builder()
            .limit(1)
            .spamTolerance(2)
            .cooldown(2, TimeUnit.MINUTES)
            .maxCooldown(3, TimeUnit.MINUTES)
            .randomIncrement(false)
            .pool(MantaroData.getDefaultJedisPool())
            .prefix("claimlock")
            .build();


    @Subscribe
    public void register(CommandRegistry cr) {
        cr.registerSlash(WaifuCommand.class);
    }

    @Name("waifu")
    @Category(CommandCategory.CURRENCY)
    @Description("Several waifu-related commands.")
    @DataAccessMode(DataMode.NO_ACCESS)
    public static class WaifuCommand extends SlashCommand {
        @Override
        protected DataResult process(SlashContext ctx, DataAccess dao) {
            // IMPLEMENTATION NOTES FOR THE WAIFU SYSTEM
            // You get 3 free slots to put "waifus" in.
            // Each extra slot (up to 9) costs exponentially more than the last one (2x more than the costs of the last one)
            // Every waifu has a "claim" price which increases in the following situations:
            // For every 100000 money owned, it increases by 3% base value (base: 1500)
            // For every 10 badges, it increases by 20% base value.
            // For every 1000 experience, the value increases by 20% of the base value.
            // After all those calculations are complete,
            // the value then is calculated using final * (reputation scale / 10)
            // where reputation scale goes up by 1 every 10 reputation points.
            // Maximum waifu value is Integer.MAX_VALUE.
            // Having a common waifu with your married partner will increase some marriage stats.
            // If you claim a waifu, and then your waifu claims you, that will unlock the "Mutual" achievement.
            // If the waifu status is mutual,
            // the MP game boost will go up by 20% and giving your daily to that waifu will increase the amount of money that your
            // waifu will receive.

            // This is an empty command, as slash commands can't have a parent command if there's subcommands.
            return null;
        }

        @Override
        public Predicate<SlashContext> getPredicate() {
            return ctx -> RatelimitUtils.ratelimit(waifuRatelimiter, ctx, false);
        }

        @Name("list")
        @Description("Show a list of all your waifu(s) and their value.")
        @Options({
                @Options.Option(type = OptionType.BOOLEAN, name = "id", description = "Show IDs")
        })
        @Help(description = "Show a list of all your waifu(s) and their value.", usage = "`/waifu list [id]`", parameters = {
                @Help.Parameter(name = "id", description = "Whether to show the user ID or not.", optional = true)
        })
        @DataAccessMode(DataMode.READ_WRITE)
        public static class ListCommand extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                // Default call will bring out the waifu list.
                NewUser cuser = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());
                final var lang = ctx.getLanguageContext();

                if (cuser.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }
                Set<Waifu> waifus = dao.getWaifuAccess().getWaifusByOwner(cuser);

                final var description = waifus.isEmpty() ?
                        lang.get("commands.waifu.waifu_header") + "\n" + lang.get("commands.waifu.no_waifu") :
                        lang.get("commands.waifu.waifu_header");


                final var waifusEmbed = new EmbedBuilder()
                        .setAuthor(lang.get("commands.waifu.header"), null, ctx.getAuthor().getEffectiveAvatarUrl())
                        .setThumbnail("https://apiv2.mantaro.site/image/common/throbbing-heart.png")
                        .setColor(Color.CYAN)
                        .setFooter(lang.get("commands.waifu.footer").formatted(
                                        waifus.size(), cuser.getWaifuSlots() - waifus.size()),
                                null
                        );

                if (waifus.isEmpty()) {
                    waifusEmbed.setDescription(description);
                    ctx.send(waifusEmbed.build());
                    return DataResult.COMMIT_SUCCESS;
                }

                final var id = ctx.getOptionAsBoolean("id");
                List<MessageEmbed.Field> fields = new LinkedList<>();

                for (Waifu waifu : waifus) {
                    User user = ctx.retrieveUserById(waifu.getClaimed().getId());
                    if (user == null) {
                        fields.add(new MessageEmbed.Field(
                                "%sUnknown User (ID: %s)".formatted(EmoteReference.BLUE_SMALL_MARKER, waifu),
                                lang.get("commands.waifu.value_format") + " unknown\n" +
                                        lang.get("commands.waifu.value_b_format") + " " + waifu.getValuePaid() +
                                        lang.get("commands.waifu.credits_format"), false)
                        );
                    } else {
                        NewUser waifuClaimed = dao.getUserAccess().getUserById(user.getIdLong());
                        // Really not required by virtue of the relational db anymore, but I'll keep it anyway
                        if (waifuClaimed.getWaifuState() == WaifuState.OPT_OUT) {
                            dao.getWaifuAccess().delete(waifu);
                            continue;
                        }

                        fields.add(new MessageEmbed.Field(
                                EmoteReference.BLUE_SMALL_MARKER + user.getName(),
                                (id ? lang.get("commands.waifu.id") + " " + user.getId() + "\n" : "") +
                                        lang.get("commands.waifu.value_format") + " " +
                                        new WaifuStats(waifuClaimed, dao.getWaifuAccess().getWaifuCountByClaimed(waifuClaimed),
                                                dao.getBadgeAccess().getUsersBadgesCountByOwner(waifuClaimed)).getFinalValue() + " " +
                                        lang.get("commands.waifu.credits_format") + "\n" +
                                        lang.get("commands.waifu.value_b_format") + " " + waifu.getValuePaid() +
                                        lang.get("commands.waifu.credits_format"), false)
                        );
                    }
                }

                final var toSend = lang.get("commands.waifu.description_header").formatted(cuser.getWaifuSlots()) + description;
                DiscordUtils.sendPaginatedEmbed(ctx.getUtilsContext(), waifusEmbed, DiscordUtils.divideFields(4, fields), toSend);
                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Description("Locks you from being claimed. Use remove to remove it.")
        @Options({@Options.Option(type = OptionType.BOOLEAN, name = "remove", description = "Remove claimlock.")})
        @DataAccessMode(DataMode.READ_WRITE)
        public static class ClaimLock extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                NewUser who = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());

                if (Objects.requireNonNull(who.getWaifuState()) == WaifuState.OPT_OUT) {
                    ctx.replyEphemeral("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (ctx.getOptionAsBoolean("remove")) {
                    who.setWaifuState(WaifuState.NORMAL);
                    dao.getUserAccess().updateFull(who);

                    ctx.replyEphemeral("commands.profile.claimlock.removed", EmoteReference.CORRECT);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (who.getWaifuState() == WaifuState.LOCKED) {
                    ctx.replyEphemeral("commands.profile.claimlock.already_locked", EmoteReference.CORRECT);
                    return DataResult.COMMIT_SUCCESS;
                }

                InventoryItem claimKey = dao.getInventoryAccess().getUserItem(who, ItemReference.CLAIM_KEY);

                if (claimKey == null || claimKey.getItemStack().getAmount() <= 0) {
                    ctx.replyEphemeral("commands.profile.claimlock.no_key", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (!RatelimitUtils.ratelimit(claimlockRatelimiter, ctx, false)) {
                    return DataResult.COMMIT_FAIL;
                }

                who.setWaifuState(WaifuState.LOCKED);
                dao.getUserAccess().updateFull(who);

                claimKey.setItemStack(new ItemStack(ItemReference.CLAIM_KEY, claimKey.getItemStack().getAmount() - 1));
                dao.getInventoryAccess().updateFull(claimKey);

                ctx.replyEphemeral("commands.profile.claimlock.success", EmoteReference.CORRECT);
                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Defer
        @Description("Opt-out of the waifu stuff. This will disable the waifu system permanently.")
        @DataAccessMode(DataMode.READ_ONLY)
        public static class OptOut extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                NewUser who = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());
                if (who.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                var message = ctx.sendResult(ctx.getLanguageContext().get("commands.waifu.optout.warning").formatted(EmoteReference.WARNING));
                ButtonOperations.create(dao.getMode(), dao.getOwner(), message, 60, (e, d) -> {
                    if (e.getUser().getIdLong() != ctx.getAuthor().getIdLong()) {
                        return Operation.IGNORED;
                    }

                    final var button = e.getButton().getId();
                    if (button == null) {
                        return Operation.IGNORED;
                    }

                    if (button.equals("yes")) {
                        final NewUser optOutUser = d.getUserAccess().getUserById(ctx.getAuthor().getIdLong());
                        optOutUser.setWaifuState(WaifuState.OPT_OUT);
                        d.getUserAccess().updateFull(optOutUser);
                        d.getWaifuAccess().deleteByClaimed(optOutUser);

                        ctx.edit("commands.waifu.optout.success", EmoteReference.CORRECT);
                        return Operation.COMPLETED;
                    } else if (button.equals("no")) {
                        ctx.edit("commands.waifu.optout.cancelled", EmoteReference.CORRECT);
                        return Operation.COMPLETED;
                    }

                    return Operation.IGNORED;
                    // Well, old one was cursed if you didn't speak english...
                }, Button.danger("yes", ctx.getLanguageContext().get("commands.waifu.optout.yes_button")),
                        Button.primary("no", ctx.getLanguageContext().get("commands.waifu.optout.no_button")));

                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Defer
        @Description("Claim a waifu. Yeah, this is all fiction.")
        @Options({
                @Options.Option(type = OptionType.USER, name = "user", description = "The user to claim.", required = true)
        })
        @Help(description = "Claim a waifu. Yeah, this is all fiction.", usage = "`/waifu claim user:<user>`", parameters = {
                @Help.Parameter(name = "user", description = "The user to claim.")
        })
        @DataAccessMode(DataMode.READ_WRITE)
        public static class Claim extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                NewUser claimer = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());
                if (claimer.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final var toLookup = ctx.getOptionAsUser("user");
                if (toLookup == null) {
                    ctx.reply("general.slash_member_lookup_failure", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (toLookup.isBot()) {
                    ctx.reply("commands.waifu.bot", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }
                NewUser claimed = dao.getUserAccess().getUserById(toLookup.getIdLong());

                if (claimed.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.claim_notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                //Waifu object declaration.
                final WaifuStats waifuToClaim = new WaifuStats(claimed,
                        dao.getWaifuAccess().getWaifuCountByClaimed(claimed),
                        dao.getBadgeAccess().getUsersBadgesCountByOwner(claimed));

                final long waifuFinalValue = waifuToClaim.getFinalValue();

                //Checks.
                if (toLookup.getIdLong() == ctx.getAuthor().getIdLong()) {
                    ctx.reply("commands.waifu.claim.yourself", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                // Existing waifu
                Waifu existing = dao.getWaifuAccess().getWaifuByKey(claimer, claimed);

                if (existing != null) {
                    ctx.reply("commands.waifu.claim.already_claimed", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                //If the to-be claimed has the claim key in their inventory, it cannot be claimed.
                if (!claimed.getWaifuState().canBeClaimed()) {
                    ctx.reply("commands.waifu.claim.key_locked", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (claimed.isBlacklistedFromBot()) {
                    ctx.reply("commands.waifu.claim.locked", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                //Deduct from balance and checks for money.
                if (!claimer.deductBalance(waifuFinalValue)) {
                    ctx.reply("commands.waifu.claim.not_enough_money", EmoteReference.ERROR, waifuFinalValue);
                    return DataResult.COMMIT_SUCCESS;
                }

                int waifuCount = dao.getWaifuAccess().getWaifuCountByOwner(claimer);

                if (waifuCount >= claimer.getWaifuSlots()) {
                    ctx.reply("commands.waifu.claim.not_enough_slots",
                            EmoteReference.ERROR, claimer.getWaifuSlots(), waifuCount
                    );

                    return DataResult.COMMIT_SUCCESS;
                }

                if (waifuFinalValue > 100_000) {
                    dao.getBadgeAccess().insert(new UserBadge(claimer, Badge.GOLD_VALUE, Instant.now()));
                }

                //Add waifu to claimer list.
                Waifu created = new Waifu(claimer, waifuFinalValue, claimed);

                //Add badges
                if (dao.getWaifuAccess().getWaifuByKey(claimed, claimer) != null) {
                    Instant when = Instant.now();
                    dao.getBadgeAccess().insert(new UserBadge(claimer, Badge.MUTUAL, Instant.now()));
                    dao.getBadgeAccess().insert(new UserBadge(claimed, Badge.MUTUAL, Instant.now()));
                }

                dao.getBadgeAccess().insert(new UserBadge(claimer, Badge.WAIFU_CLAIMER, Instant.now()));

                dao.getUserAccess().updateFull(claimer);
                dao.getWaifuAccess().insert(created);

                //Send confirmation message
                ctx.reply("commands.waifu.claim.success",
                        EmoteReference.CORRECT, toLookup.getName(), waifuFinalValue, waifuCount + 1
                );
                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Defer
        @Description("Unclaims a waifu.")
        @Options({
                @Options.Option(type = OptionType.USER, name = "user", description = "The user to unclaim. If unknown, use the id.", required = true)
        })
        @Help(description = "Unclaims a waifu.", usage = "`/waifu unclaim user:<user or id>`", parameters = {
                @Help.Parameter(name = "user", description = "The user to unclaim.")
        })
        @DataAccessMode(DataMode.READ_AND_HOLD)
        public static class Unclaim extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                NewUser claimer = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());
                if (claimer.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final var user = ctx.getOptionAsGlobalUser("user");
                if (user == null) {
                    ctx.reply("commands.waifu.unclaim.no_user", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (user.isBot()) {
                    ctx.reply("commands.waifu.bot", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final Waifu existing = dao.getWaifuAccess().getWaifuByKey(claimer,  new NewUser(claimer.getId()));

                if (existing == null) {
                    ctx.reply("commands.waifu.not_claimed", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                NewUser claimed = dao.getUserAccess().getUserById(user.getIdLong());

                final var currentValue = new WaifuStats(claimed,
                        dao.getWaifuAccess().getWaifuCountByClaimed(claimed),
                        dao.getBadgeAccess().getUsersBadgesCountByOwner(claimed)).getFinalValue();
                final var valuePayment = (long) (currentValue * 0.15);
                //Send confirmation message.
                var message = ctx.sendResult(ctx.getLanguageContext().get("commands.waifu.unclaim.confirmation").formatted(EmoteReference.MEGA, name, valuePayment, EmoteReference.STOPWATCH));
                ButtonOperations.create(dao.getMode(), dao.getOwner(), message, 60, (ie, d) -> {
                    if (ie.getUser().getIdLong() != ctx.getAuthor().getIdLong()) {
                        return Operation.IGNORED;
                    }

                    var button = ie.getButton();
                    if (button.getId() == null) {
                        return Operation.IGNORED;
                    }

                    if (button.getId().equals("yes")) {
                        final NewUser p = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());

                        if (!p.deductBalance(valuePayment)) {
                            ctx.edit("commands.waifu.unclaim.not_enough_money", EmoteReference.ERROR);
                            return Operation.COMPLETED;
                        }

                        d.getUserAccess().updateFull(p);
                        d.getWaifuAccess().delete(existing);

                        ctx.edit("commands.waifu.unclaim.success", EmoteReference.CORRECT, name, valuePayment);
                        return Operation.COMPLETED;
                    } else if (button.getId().equals("no")) {
                        ctx.edit("commands.waifu.unclaim.scrapped", EmoteReference.CORRECT);
                        return Operation.COMPLETED;
                    }

                    return Operation.IGNORED;
                }, Button.primary("yes", ctx.getLanguageContext().get("buttons.yes")), Button.primary("no", ctx.getLanguageContext().get("buttons.no")));
                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Defer
        @Description("Buys a new waifu slot. Maximum slots are 30, costs get increasingly higher.")
        @DataAccessMode(DataMode.READ_WRITE)
        public static class BuySlot extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                final var baseValue = 3000;
                final NewUser who = dao.getUserAccess().getUserById(ctx.getAuthor().getIdLong());

                if (who.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final var currentSlots = who.getWaifuSlots();
                final var baseMultiplier = (currentSlots / 3) + 1;
                final var finalValue = baseValue * baseMultiplier;

                if (!who.deductBalance(finalValue)) {
                    ctx.reply("commands.waifu.buyslot.not_enough_money", EmoteReference.ERROR, finalValue);
                    return DataResult.COMMIT_SUCCESS;
                }

                if (who.getWaifuSlots() >= 30) {
                    ctx.reply("commands.waifu.buyslot.too_many", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                who.setWaifuSlots(who.getWaifuSlots() + 1);
                dao.getUserAccess().updateFull(who);

                ctx.reply("commands.waifu.buyslot.success",
                        EmoteReference.CORRECT, finalValue, who.getWaifuSlots(), (who.getWaifuSlots() - dao.getWaifuAccess().getWaifuCountByOwner(who))
                );
                return DataResult.COMMIT_SUCCESS;
            }
        }

        @Description("Shows your waifu stats or the stats or someone else.")
        @Options({
                @Options.Option(type = OptionType.USER, name = "user", description = "The user to check stats for. Yourself, if nothing specified.")
        })
        @Help(description = "Shows your waifu stats or the stats or someone else.", usage = "`/waifu stats user:[user]`", parameters = {
                @Help.Parameter(name = "user", description = "The user to check. Yourself, if nothing specified.", optional = true),
        })
        @DataAccessMode(DataMode.READ_ONLY)
        public static class Stats extends SlashCommand {
            @Override
            protected DataResult process(SlashContext ctx, DataAccess dao) {
                final var lang = ctx.getLanguageContext();

                final var toLookup = ctx.getOptionAsUser("user", ctx.getAuthor());
                if (toLookup.isBot()) {
                    ctx.reply("commands.waifu.bot", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final NewUser whom = dao.getUserAccess().getUserById(toLookup.getIdLong());

                if (whom.getWaifuState() == WaifuState.OPT_OUT) {
                    ctx.reply("commands.waifu.optout.lookup_notice", EmoteReference.ERROR);
                    return DataResult.COMMIT_SUCCESS;
                }

                final var waifuStats = new WaifuStats(whom,
                        dao.getWaifuAccess().getWaifuCountByClaimed(whom),
                        dao.getBadgeAccess().getUsersBadgesCountByOwner(whom));
                final var finalValue = waifuStats.getFinalValue();

                EmbedBuilder statsBuilder = new EmbedBuilder()
                        .setThumbnail(toLookup.getEffectiveAvatarUrl())
                        .setAuthor(toLookup == ctx.getAuthor() ?
                                        lang.get("commands.waifu.stats.header") :
                                        lang.get("commands.waifu.stats.header_other").formatted(toLookup.getName()),
                                null, toLookup.getEffectiveAvatarUrl()
                        ).setColor(Color.PINK)
                        .setDescription(lang.get("commands.waifu.stats.format").formatted(
                                EmoteReference.BLUE_SMALL_MARKER,
                                waifuStats.getMoneyValue(),
                                waifuStats.getBadgeValue(),
                                0L,
                                waifuStats.getClaimValue(),
                                waifuStats.getReputationMultiplier())
                        ).addField(EmoteReference.ZAP.toHeaderString() + lang.get("commands.waifu.stats.performance"),
                                waifuStats.getPerformance() + "wp", true
                        ).addField(EmoteReference.MONEY.toHeaderString() + lang.get("commands.waifu.stats.value"),
                                lang.get("commands.waifu.stats.credits").formatted(finalValue),
                                false
                        ).setFooter(lang.get("commands.waifu.notice"), null);

                ctx.reply(statsBuilder.build());
                return DataResult.COMMIT_SUCCESS;
            }
        }
    }
}
