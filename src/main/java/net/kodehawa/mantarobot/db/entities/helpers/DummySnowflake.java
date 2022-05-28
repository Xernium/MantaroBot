package net.kodehawa.mantarobot.db.entities.helpers;

import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;

public class DummySnowflake implements ISnowflake {

    private final long id;

    public DummySnowflake(long id) {
        this.id = id;
    }

    public DummySnowflake(String id) {
        this.id = Long.parseUnsignedLong(id);
    }

    @NotNull
    @Override
    public long getIdLong() {
        return id;
    }
}
