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

package net.kodehawa.mantarobot.commands.action;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.kodehawa.mantarobot.MantaroInfo;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.utils.Utils;
import okhttp3.Request;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OpenRamAPIRequester {
    private static final Logger log = LoggerFactory.getLogger(OpenRamAPIRequester.class);

    public static final List<String> SUPPORTED_TAGS = ImmutableList.of("cry", "cuddle", "hug", "kiss", "lewd", "lick",
            "nom", "nyan", "owo", "pat", "pout", "slap", "smug", "stare", "tickle");

    private static final String API_REQUEST_URL = "https://rra.ram.moe/";
    private static final String API_CDN_URL = "https://cdn.ram.moe/";
    private static final String TYPE_ENDPOINT = API_REQUEST_URL + "i/r?type=";

    public Pair<String, String> getRandomImageByType(String type, boolean nsfw) {
        Preconditions.checkArgument(SUPPORTED_TAGS.contains(type.toLowerCase(Locale.ROOT)));
        var req = request(TYPE_ENDPOINT + type);
        if (req == null) {
            return Pair.of(null, null);
        }

        var object = new JSONObject(req);
        return Pair.of(API_CDN_URL + object.getString("path").substring(3), object.getString("id"));
    }

    private String request(String endpoint) {
        try {
            var r = new Request.Builder()
                    .url(endpoint)
                    .build();

            try(var response = Utils.httpClient.newCall(r).execute()) {
                var body = response.body();
                if (body == null) {
                    throw new IllegalStateException("body == null");
                }

                return body.string();
            }
        } catch (Exception ex) {
            log.error("Error getting image from weeb.sh", ex);
            return null;
        }
    }
}
