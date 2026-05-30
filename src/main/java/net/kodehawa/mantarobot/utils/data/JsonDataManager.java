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

package net.kodehawa.mantarobot.utils.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.json.JsonFactoryBuilder;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class JsonDataManager<T> implements DataManager<T> {
    private static final ObjectMapper mapper = new ObjectMapper(new JsonFactoryBuilder()
            .enable(JsonReadFeature.ALLOW_UNQUOTED_PROPERTY_NAMES) // Custom commands.
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS) // Allow newlines.
            .build()).rebuild()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // Anime / Character lookup.
            .build();
    private static final Logger log = LoggerFactory.getLogger(JsonDataManager.class);
    private final Path configPath;
    private final T data;

    public JsonDataManager(Class<T> clazz, String file, Supplier<T> constructor) {
        this.configPath = Paths.get(file);

        if (!configPath.toFile().exists()) {
            log.info("Could not find config file at {}", configPath.toFile().getAbsolutePath() + ", creating a new one...");
            try {
                var absPath = this.configPath.toAbsolutePath();
                var pathExists = absPath.toFile().getParentFile().exists();
                if (!pathExists) {
                    pathExists = absPath.toFile().getParentFile().mkdirs();
                }
                if (pathExists && configPath.toFile().createNewFile()) {
                    log.info("Generated new config file at {}", absPath + ".");
                    FileIOUtils.write(configPath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(constructor.get()));
                    log.info("Please, fill the file with valid properties.");
                } else {
                    log.warn("Could not create config file at {}", file);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.exit(0);
        }

        try {
            this.data = fromJson(FileIOUtils.read(configPath), clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public T get() {
        return data;
    }

    @Override
    public void save() {
        try {
            FileIOUtils.write(configPath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> String toJson(T object) throws JacksonException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JacksonException {
        return mapper.readValue(json, clazz);
    }

    public static <T> T fromJson(InputStream json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    @SuppressWarnings("unused")
    public static <T> T fromJson(String json, TypeReference<T> type) throws JacksonException {
        return mapper.readValue(json, type);
    }

    public static <T> T fromJson(String json, JavaType type) throws JacksonException {
        return mapper.readValue(json, type);
    }
}
