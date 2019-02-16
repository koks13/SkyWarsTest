package koks.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import koks.SkyWars;
import koks.managers.KitManager;
import koks.objects.SWKit;
import koks.objects.SWPlayer;
import koks.objects.SWStats;
import koks.utility.ChatUtil;
import org.bson.Document;
import java.util.ArrayList;

public class MongoDb {

    SkyWars plugin = SkyWars.getInstance();
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    public void connection() {
        try {
            //MongoClientURI url = new MongoClientURI("mongodb+srv://Admin:test13@mongodb-ibntj.mongodb.net/test?retryWrites=true");
            mongoClient = new MongoClient(new MongoClientURI(plugin.getConfig().getString("Mongodb.URL")));
            database = mongoClient.getDatabase(plugin.getConfig().getString("Mongodb.databaseName"));
            collection = database.getCollection(plugin.getConfig().getString("Mongodb.collection"));
            SkyWars.getInstance().consoleSendMessage("&amongodb connection established!");
        } catch (Exception e) {
            SkyWars.getInstance().getLogger().warning("MongoDb connection error: " + e.toString());
            SkyWars.getInstance().consoleSendMessage( "&cDATABASE NOT CONNECTED!!!");
        }
    }

    public void loadPlayerStats(SWPlayer player) {
        try {
            Document doc = new Document("name", player.getName());
            Document found = collection.find(doc).first();
            if (found != null) {
                int kills = found.getInteger("kills");
                int deaths = found.getInteger("deaths");
                int games = found.getInteger("games");
                int wins = found.getInteger("wins");
                int coins = found.getInteger("coins");
                int exp = found.getInteger("exp");
                int level = found.getInteger("level");
                int souls = found.getInteger("souls");
                player.getStats().setStats(kills, deaths, games, wins, coins, exp, level, souls);
                Document kits = (Document) found.get("kits");
                ArrayList purchasedKits = (ArrayList) kits.get("purchasedKits");
                purchasedKits.forEach(kitName -> {
                    SWKit kit = KitManager.getKit((String) kitName);
                    if (kit != null) player.getStats().addKits(kit);
                });
                SWKit kit = player.getStats().getKit(kits.getString("selectedKit"));
                if (kit != null) player.selectKit(kit);
            } else
                collection.insertOne(new Document("name", player.getName()));
        } catch (Exception e) {
            SkyWars.getInstance().getLogger().warning("Error loading statistics player " + player.getName() + ". Exception: " + e.toString());
        }
    }

    public void updatePlayerStats(SWPlayer player) {
        try {
            SWStats stats = player.getStats();
            Document docStats = collection.find(new Document("name", player.getName())).first();
            if (docStats != null) {
                Document doc = new Document();
                doc.append("kills", stats.getKills());
                doc.append("deaths", stats.getDeaths());
                doc.append("games", stats.getGames());
                doc.append("wins", stats.getWins());
                doc.append("coins", stats.getCoins());
                doc.append("exp", stats.getExp());
                doc.append("level", stats.getLevel());
                doc.append("souls", stats.getSouls());
                ArrayList<String> kits = new ArrayList<>();
                stats.getKits().forEach(kit -> kits.add(ChatUtil.clearColor(kit.getKitName())));
                doc.append("kits", new Document("purchasedKits", kits).append("selectedKit", ChatUtil.clearColor(player.getSelectedKit().getKitName())));
                Document update = new Document("$set", doc);
                collection.updateOne(docStats, update);
            }
        } catch (Exception e) {
            SkyWars.getInstance().getLogger().warning("Error update statistics player " + player.getName() + ". Exception: " + e.toString());
        }
    }
}
