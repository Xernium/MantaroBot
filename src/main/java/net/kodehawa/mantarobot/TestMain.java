package net.kodehawa.mantarobot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.kodehawa.mantarobot.db.MantaroPremiumKey;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.JdbcSettings;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public class TestMain {

    public static void main(String... args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting(JdbcSettings.JAKARTA_JDBC_DRIVER, "org.h2.Driver")
                .applySetting(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:mantaro;DB_CLOSE_DELAY=-1")
                .applySetting(JdbcSettings.JAKARTA_JDBC_USER, "sa")
                .applySetting(JdbcSettings.JAKARTA_JDBC_PASSWORD, "")
                .applySetting(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect")
                .applySetting(AvailableSettings.HBM2DDL_AUTO, "create-drop")
                .applySetting(AvailableSettings.SHOW_SQL, "true")
                .applySetting(AvailableSettings.FORMAT_SQL, "true")
                .build();

        try (SessionFactory sessionFactory = new MetadataSources(registry)
                //.addAnnotatedClass(SampleUser.class)
                .addAnnotatedClass(MantaroPremiumKey.class)
                .buildMetadata()
                .buildSessionFactory()) {

            //Long userId;

            long testId;
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();

                //SampleUser user = new SampleUser("blanc");
                //session.persist(user);
                MantaroPremiumKey testKey = new MantaroPremiumKey();
                testKey.setEnabled(true);
                testKey.setDuration(Duration.ofDays(20));
                testKey.setStart(Instant.now());
                testKey.setType(MantaroPremiumKey.Type.USER);
                session.persist(testKey);

                tx.commit();
                testId = testKey.getId();
                //userId = user.getId();
            }

            LoggerFactory.getLogger(TestMain.class).info("Created test key with ID: {}", testId);
            try (Session session = sessionFactory.openSession()) {
                MantaroPremiumKey loaded = session.find(MantaroPremiumKey.class, testId);
                LoggerFactory.getLogger(TestMain.class).info("Loaded as: {}", loaded);
            }
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Entity
    @Table(name = "sample_user")
    public static class SampleUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;

        protected SampleUser() {
            // Required by JPA.
        }

        public SampleUser(String username) {
            this.username = username;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }
}
