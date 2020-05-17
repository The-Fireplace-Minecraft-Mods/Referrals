package the_fireplace.referrals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Config {
    public static final File configFile = new File("config", Referrals.MODID+".yml");
    private static Config instance = null;
    public static Config getInstance() {
        if(instance == null)
            instance = load();
        return instance;
    }
    //Config options
    public String locale = "en_us";
    public Map<Integer, List<String>> rankedRewards = Maps.newHashMap();
    public List<String> standardRewards = Lists.newArrayList("wallet add [referrer] 500", "wallet add [referred] 500", "say [referrer] has referred [referred] to the server! They have been given 500.00 gp each.");

    public Config() {
        rankedRewards.put(1, Lists.newArrayList("wallet add [referrer] 1000", "wallet add [referred] 500", "say [referrer] has referred [referred] to the server! They have been given 500.00 gp each.", "say [referrer] has referred a player for the first time! They have been given an extra 500.00 gp."));
        rankedRewards.put(5, Lists.newArrayList("wallet add [referrer] 1000", "wallet add [referred] 500", "say [referrer] has referred [referred] to the server! They have been given 500.00 gp each.", "say [referrer] has referred a player for the fifth time! They have been given an extra 500.00 gp."));
    }

    private static Config load() {
        if(!configFile.exists())
            createDefaultConfigFile();
        Yaml yaml = new Yaml(new Constructor(Config.class));
        try {
            FileReader reader = new FileReader(configFile);
            Config cfg = (Config) yaml.load(reader);
            reader.close();
            return cfg;
        } catch(IOException e) {
            e.printStackTrace();
            return new Config();
        }
    }

    /*public static void save() {
        Yaml yaml = new Yaml();
        try {
            FileWriter writer = new FileWriter(configFile);
            yaml.dump(getInstance(), writer);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    private static void createDefaultConfigFile() {
        try {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(configFile);
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(new Config(), writer);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
