package me.playajames.thesimpleeconomy.utilities;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import me.playajames.thesimpleeconomy.TheSimpleEconomy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseUtil {

    private static boolean initiated = false;
    public static void init(String database) {
        if (initiated) return;
        File databaseFile = createDatabaseIfNotExists(database);
        DatabaseOptions options = DatabaseOptions.builder().sqlite(databaseFile.toString()).build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        createTablesIfNotExists();
        initiated = true;
    }

    public static void close() {
        DB.close();
    }

    public static Database getDatabase() {
        return DB.getGlobalDatabase();
    }

    private static File createDatabaseIfNotExists(String database) {
        File file = new File(TheSimpleEconomy.getPlugin(TheSimpleEconomy.class).getDataFolder(), database);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void createTablesIfNotExists() {
        try {
            //Create accounts table
            getDatabase().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS accounts " +
                    "(owner_uuid VARCHAR (40) PRIMARY KEY " +
                    "UNIQUE " +
                    "NOT NULL, " +
                    "balance DOUBLE NOT NULL);");
            //Create transactions table
            getDatabase().executeUpdate("CREATE TABLE IF NOT EXISTS transactions " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT " +
                    "UNIQUE NOT NULL, " +
                    "sender_uuid   VARCHAR (40) NOT NULL, " +
                    "receiver_uuid VARCHAR (40) NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "status VARCHAR (255) NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
