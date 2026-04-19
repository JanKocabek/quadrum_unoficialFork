package dmillerw.quadrum.common.lib;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dmillerw.quadrum.Quadrum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;

public class LanguageHelper {
   private static final FilenameFilter FILTER = new ExtensionFilter("lang");

   public LanguageHelper() {
   }

   public static void loadDirectory(File dir) {
      if (dir.isDirectory()) {
         for (File file : dir.listFiles(FILTER)) {
            loadFile(file);
         }
      }
   }

   public static void loadFile(File file) {
      try {
         String lang = file.getName().substring(0, file.getName().lastIndexOf("."));
         Properties properties = new Properties();
         properties.load(new FileInputStream(file));
         HashMap<String, String> map = Maps.newHashMap();

         for (Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
         }

         LanguageRegistry.instance().injectLanguage(lang, map);
      } catch (IOException var6) {
         Quadrum.log(Level.WARN, "Failed to load localizations from %s. Reason: %s", file.getName(), var6.toString());
      }
   }
}
