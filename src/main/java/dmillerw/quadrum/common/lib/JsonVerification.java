package dmillerw.quadrum.common.lib;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dmillerw.quadrum.Quadrum;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;

public class JsonVerification {
   private static final String ERROR = "Failed to load %s. One or more required fields were missing %s";

   public JsonVerification() {
   }

   public static boolean verifyRequirements(File file, JsonObject jsonObject, Class<?> clazz) {
      List<String> keys = Lists.newArrayList();

      for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
         keys.add(entry.getKey());
      }

      List<String> missingFields = Lists.newArrayList();

      for (Field field : clazz.getDeclaredFields()) {
         String name = field.getName();
         if (field.getAnnotation(SerializedName.class) != null) {
            name = ((SerializedName)field.getAnnotation(SerializedName.class)).value();
         }

         if (field.getAnnotation(Required.class) != null && !keys.contains(name)) {
            missingFields.add(name);
         }
      }

      if (!missingFields.isEmpty()) {
         Quadrum.log(Level.WARN, "Failed to load %s. One or more required fields were missing %s", file.getName(), missingFields);
         return false;
      } else {
         return true;
      }
   }
}
