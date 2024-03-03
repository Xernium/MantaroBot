package net.kodehawa.mantarobot.db.rel.meta;

import net.kodehawa.mantarobot.db.rel.help.DataMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataAccessMode {
    DataMode value();
}
