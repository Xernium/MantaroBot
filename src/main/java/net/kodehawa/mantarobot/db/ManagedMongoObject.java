package net.kodehawa.mantarobot.db;

import net.kodehawa.mantarobot.data.MantaroData;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public interface ManagedMongoObject {
    
    String getId();

    @BsonIgnore
    
    String getTableName();

    @BsonIgnore
    
    default String getDatabaseId() {
        return getId();
    }

    @BsonIgnore
    default void updateField(String key, Object value) {
        MantaroData.db().updateFieldValue(this, key, value);
    }

    @BsonIgnore
    default void updateAllChanged() {
        throw new UnsupportedOperationException();
    }

    // Need to implement class-by-class...
    @BsonIgnore
    void insertOrReplace();
    @BsonIgnore
    void delete();
}
