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

package net.kodehawa.mantarobot.commands.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.kodehawa.mantarobot.MantaroInfo;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.utils.Utils;
import net.kodehawa.mantarobot.utils.data.JsonDataManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class ActionAPIRequester {

    private static final Logger log = LoggerFactory.getLogger(ActionAPIRequester.class);

    @MonotonicNonNull
    private static ActionAPIRequester activeAPI;

    public static ActionAPIRequester getActiveInstance() {
        if (activeAPI == null) {
            log.info("Loading Action-query api...");
            if (MantaroData.config().get().weebapiKey == null || MantaroData.config().get().weebapiKey.isEmpty()) {
                activeAPI = new OpenRamAPIRequester();
                log.warn("Could not load Weeb.sh API (Missing key). Falling back to restricted open Ram API.");
            } else {
                activeAPI = new WeebAPIRequester();
                log.info("Loaded weeb.sh API");
            }
        }
        return activeAPI;
    }

    public abstract ActionAPIObject getRandomImageByType(String type, boolean nsfw, String filetype) throws JsonProcessingException;

    public record ActionAPIObject(String id, String url, String fileType, boolean nsfw, String type, List<ActionAPITag> tags) { }
    @SuppressWarnings("unused")
    public record ActionAPITag(String user, boolean hidden, String name) { }
}
