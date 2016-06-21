package base;

import java.util.EnumSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

public class TestJsonPath {

    public static void init() {
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new GsonJsonProvider();
            private final MappingProvider mappingProvider = new GsonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }
        });
    }

    public static void main(String[] args) {
        init();
        Configuration conf = Configuration.defaultConfiguration();
        String json = "[{\"name\" : \"john\",\"gender\" : \"male\"},{\"name\" : \"ben\"}]";
        // // Works fine
        // Object gender0 = JsonPath.using(conf).parse(json).read("$[0]['gender']");
        // // PathNotFoundException thrown
        // Object gender1 = JsonPath.using(conf).parse(json).read("$[1]['gender']");

        Configuration conf2 = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        // Works fine
        String gender0 = JsonPath.using(conf2).parse(json).read("$[0]['gender']", String.class);
        // Works fine (null is returned)
        String gender1 = JsonPath.using(conf2).parse(json).read("$[1]['gender']", String.class);
        System.out.println(gender0);
        System.out.println(gender1);

        long c = System.currentTimeMillis();
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        for (int i = 0; i < 10000; i++) {
            Object author0 = JsonPath.read(document, "$[0]['gender']");
        }
        long d = System.currentTimeMillis();
        System.out.println(d-c);
        
        long a = System.currentTimeMillis();
        ReadContext ctx = JsonPath.using(conf2).parse(json);
        for (int i = 0; i < 10000; i++) {
            Object result = ctx.read("$[0]['gender']", String.class);
        }
        long b = System.currentTimeMillis();
        System.out.println(b - a);

        
        long e = System.currentTimeMillis();
        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(json);
        for (int i = 0; i < 10000; i++) {
            if(ele!=null && ele.isJsonArray()){
                JsonArray ja = ele.getAsJsonArray();
               String result = ja.get(0).getAsJsonObject().get("gender").toString();
             }
        }
        long f = System.currentTimeMillis();
        System.out.println(f-e);
    }

}
