/*
 * Copyright (C) 2024 FivePB (Xernium)
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

package net.kodehawa.mantarobot.db.rel;

import net.kodehawa.mantarobot.core.command.helpers.CommandCategory;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class NewGuild {

    private long id;
    private boolean blacklistedFromBot;
    private Set<CommandCategory> disabledCommandCategories;
    private Set<String> specificDisabledCommands;
    private Set<String> blackListedImageTags;
    private Set<String> modLogExcludedWords;
    private List<String> joinMessages;
    private List<String> leaveMessages;
    private GuildChannelData birthdayChannel;
    private GuildRoleData birthdayRole;
    private String birthdayMessage;
    private GuildRoleData autoAssignRole;
    private boolean treatOtherBotsAsUsersForAutoAssignedRole;
    private boolean treatOtherBotsAsUsersForWelcomeLeaveMessages;
    private GuildRoleData moderationMutedRole;
    private long moderationMuteLengthMillis;
    private Instant lastGameTimeoutExpectedAt;
    private String customPrefix;
    private boolean botPingsUsersInActionCommands;
    private boolean allowNonAdminToCreateCustomCommands;
    // Locale stuff
    private boolean useMilitaryTime;
    private Locale language;
    private TimeZone timeZone;
    private boolean disableExplicitContent;
    private boolean showWarningOnAttemptedDisabledCommand;
    private boolean didShowServerGreetingIntroduction;
    // Log stuff
    private GuildChannelData logUserJoinGuildChannel;
    private GuildChannelData logUserLeaveGuildChannel;
    // Moderation log
    private GuildChannelData moderationUserEditMessageLog;
    private GuildChannelData moderationUserDeleteMessageLog;
    private GuildChannelData moderationUserBannedLog;
    private GuildChannelData moderationUserPardonedLog;
    private GuildChannelData moderationUserKickedLog;
    // Stats
    private int ranPolls;
    private int moderationActionsPerformed;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBlacklistedFromBot() {
        return blacklistedFromBot;
    }

    public void setBlacklistedFromBot(boolean blacklistedFromBot) {
        this.blacklistedFromBot = blacklistedFromBot;
    }

    public Set<CommandCategory> getDisabledCommandCategories() {
        return disabledCommandCategories;
    }

    public void setDisabledCommandCategories(Set<CommandCategory> disabledCommandCategories) {
        this.disabledCommandCategories = disabledCommandCategories;
    }

    public Set<String> getSpecificDisabledCommands() {
        return specificDisabledCommands;
    }

    public void setSpecificDisabledCommands(Set<String> specificDisabledCommands) {
        this.specificDisabledCommands = specificDisabledCommands;
    }

    public Set<String> getBlackListedImageTags() {
        return blackListedImageTags;
    }

    public void setBlackListedImageTags(Set<String> blackListedImageTags) {
        this.blackListedImageTags = blackListedImageTags;
    }

    public Set<String> getModLogExcludedWords() {
        return modLogExcludedWords;
    }

    public void setModLogExcludedWords(Set<String> modLogExcludedWords) {
        this.modLogExcludedWords = modLogExcludedWords;
    }

    public List<String> getJoinMessages() {
        return joinMessages;
    }

    public void setJoinMessages(List<String> joinMessages) {
        this.joinMessages = joinMessages;
    }

    public List<String> getLeaveMessages() {
        return leaveMessages;
    }

    public void setLeaveMessages(List<String> leaveMessages) {
        this.leaveMessages = leaveMessages;
    }

    public GuildChannelData getBirthdayChannel() {
        return birthdayChannel;
    }

    public void setBirthdayChannel(GuildChannelData birthdayChannel) {
        this.birthdayChannel = birthdayChannel;
    }

    public GuildRoleData getBirthdayRole() {
        return birthdayRole;
    }

    public void setBirthdayRole(GuildRoleData birthdayRole) {
        this.birthdayRole = birthdayRole;
    }

    public String getBirthdayMessage() {
        return birthdayMessage;
    }

    public void setBirthdayMessage(String birthdayMessage) {
        this.birthdayMessage = birthdayMessage;
    }

    public GuildRoleData getAutoAssignRole() {
        return autoAssignRole;
    }

    public void setAutoAssignRole(GuildRoleData autoAssignRole) {
        this.autoAssignRole = autoAssignRole;
    }

    public boolean isTreatOtherBotsAsUsersForAutoAssignedRole() {
        return treatOtherBotsAsUsersForAutoAssignedRole;
    }

    public void setTreatOtherBotsAsUsersForAutoAssignedRole(boolean treatOtherBotsAsUsersForAutoAssignedRole) {
        this.treatOtherBotsAsUsersForAutoAssignedRole = treatOtherBotsAsUsersForAutoAssignedRole;
    }

    public boolean isTreatOtherBotsAsUsersForWelcomeLeaveMessages() {
        return treatOtherBotsAsUsersForWelcomeLeaveMessages;
    }

    public void setTreatOtherBotsAsUsersForWelcomeLeaveMessages(boolean treatOtherBotsAsUsersForWelcomeLeaveMessages) {
        this.treatOtherBotsAsUsersForWelcomeLeaveMessages = treatOtherBotsAsUsersForWelcomeLeaveMessages;
    }

    public GuildRoleData getModerationMutedRole() {
        return moderationMutedRole;
    }

    public void setModerationMutedRole(GuildRoleData moderationMutedRole) {
        this.moderationMutedRole = moderationMutedRole;
    }

    public long getModerationMuteLengthMillis() {
        return moderationMuteLengthMillis;
    }

    public void setModerationMuteLengthMillis(long moderationMuteLengthMillis) {
        this.moderationMuteLengthMillis = moderationMuteLengthMillis;
    }

    public Instant getLastGameTimeoutExpectedAt() {
        return lastGameTimeoutExpectedAt;
    }

    public void setLastGameTimeoutExpectedAt(Instant lastGameTimeoutExpectedAt) {
        this.lastGameTimeoutExpectedAt = lastGameTimeoutExpectedAt;
    }

    public String getCustomPrefix() {
        return customPrefix;
    }

    public void setCustomPrefix(String customPrefix) {
        this.customPrefix = customPrefix;
    }

    public boolean isBotPingsUsersInActionCommands() {
        return botPingsUsersInActionCommands;
    }

    public void setBotPingsUsersInActionCommands(boolean botPingsUsersInActionCommands) {
        this.botPingsUsersInActionCommands = botPingsUsersInActionCommands;
    }

    public boolean isAllowNonAdminToCreateCustomCommands() {
        return allowNonAdminToCreateCustomCommands;
    }

    public void setAllowNonAdminToCreateCustomCommands(boolean allowNonAdminToCreateCustomCommands) {
        this.allowNonAdminToCreateCustomCommands = allowNonAdminToCreateCustomCommands;
    }

    public boolean isUseMilitaryTime() {
        return useMilitaryTime;
    }

    public void setUseMilitaryTime(boolean useMilitaryTime) {
        this.useMilitaryTime = useMilitaryTime;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isDisableExplicitContent() {
        return disableExplicitContent;
    }

    public void setDisableExplicitContent(boolean disableExplicitContent) {
        this.disableExplicitContent = disableExplicitContent;
    }

    public boolean isShowWarningOnAttemptedDisabledCommand() {
        return showWarningOnAttemptedDisabledCommand;
    }

    public void setShowWarningOnAttemptedDisabledCommand(boolean showWarningOnAttemptedDisabledCommand) {
        this.showWarningOnAttemptedDisabledCommand = showWarningOnAttemptedDisabledCommand;
    }

    public boolean isDidShowServerGreetingIntroduction() {
        return didShowServerGreetingIntroduction;
    }

    public void setDidShowServerGreetingIntroduction(boolean didShowServerGreetingIntroduction) {
        this.didShowServerGreetingIntroduction = didShowServerGreetingIntroduction;
    }

    public GuildChannelData getLogUserJoinGuildChannel() {
        return logUserJoinGuildChannel;
    }

    public void setLogUserJoinGuildChannel(GuildChannelData logUserJoinGuildChannel) {
        this.logUserJoinGuildChannel = logUserJoinGuildChannel;
    }

    public GuildChannelData getLogUserLeaveGuildChannel() {
        return logUserLeaveGuildChannel;
    }

    public void setLogUserLeaveGuildChannel(GuildChannelData logUserLeaveGuildChannel) {
        this.logUserLeaveGuildChannel = logUserLeaveGuildChannel;
    }

    public GuildChannelData getModerationUserEditMessageLog() {
        return moderationUserEditMessageLog;
    }

    public void setModerationUserEditMessageLog(GuildChannelData moderationUserEditMessageLog) {
        this.moderationUserEditMessageLog = moderationUserEditMessageLog;
    }

    public GuildChannelData getModerationUserDeleteMessageLog() {
        return moderationUserDeleteMessageLog;
    }

    public void setModerationUserDeleteMessageLog(GuildChannelData moderationUserDeleteMessageLog) {
        this.moderationUserDeleteMessageLog = moderationUserDeleteMessageLog;
    }

    public GuildChannelData getModerationUserBannedLog() {
        return moderationUserBannedLog;
    }

    public void setModerationUserBannedLog(GuildChannelData moderationUserBannedLog) {
        this.moderationUserBannedLog = moderationUserBannedLog;
    }

    public GuildChannelData getModerationUserPardonedLog() {
        return moderationUserPardonedLog;
    }

    public void setModerationUserPardonedLog(GuildChannelData moderationUserPardonedLog) {
        this.moderationUserPardonedLog = moderationUserPardonedLog;
    }

    public GuildChannelData getModerationUserKickedLog() {
        return moderationUserKickedLog;
    }

    public void setModerationUserKickedLog(GuildChannelData moderationUserKickedLog) {
        this.moderationUserKickedLog = moderationUserKickedLog;
    }

    public int getRanPolls() {
        return ranPolls;
    }

    public void setRanPolls(int ranPolls) {
        this.ranPolls = ranPolls;
    }

    public int getModerationActionsPerformed() {
        return moderationActionsPerformed;
    }

    public void setModerationActionsPerformed(int moderationActionsPerformed) {
        this.moderationActionsPerformed = moderationActionsPerformed;
    }
}
