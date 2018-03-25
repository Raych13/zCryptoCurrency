package com.example.jingze.zcryptocurrency.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jingze.zcryptocurrency.model.Coin;
import com.example.jingze.zcryptocurrency.model.CoinMenu;
import com.example.jingze.zcryptocurrency.net.Bitfinex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class ModelUtils {

    private static Gson gson = new Gson();
    public static final Gson GSON_EXPOSS = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static final Gson GSON_BITFINEX = new GsonBuilder().registerTypeAdapter(Bitfinex.class, new TypeAdapter<Bitfinex>() {

        @Override
        public void write(JsonWriter out, Bitfinex value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name("event").value(value.event);
            out.name("channel").value(value.channel);
            out.name("chanId").value(value.chanId);
            out.name("symbol").value(value.symbol);
            out.name("pair").value(value.pair);
            out.name("version").value(value.version);
            if (value.platform != null) {
                out.name("platform").value(ModelUtils.toString(value.platform, new TypeToken<Bitfinex.PlatformInfo>(){}));
            }
            out.name("msg").value(value.msg);
            out.name("errorCode").value(value.errorCode);
            out.name("flags").value(value.flags);
            if (value.coin != null) {
                out.name("coin").value(ModelUtils.toString(value.coin, new TypeToken<Coin>(){}));
            }
            out.endObject();
        }

        @Override
        public Bitfinex read(JsonReader in) throws IOException {
            return null;
        }

    }).create();
    private static String PREF_NAME = "models";

    public static void save(Context context, String key, Object object) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

        String jsonString = gson.toJson(object);
        sp.edit().putString(key, jsonString).apply();
    }

    public static <T> T read(Context context, String key, TypeToken<T> typeToken) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);
        try {
            return gson.fromJson(sp.getString(key, ""), typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T toObject(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public static <T> String toString(T object, TypeToken<T> typeToken) {
        return gson.toJson(object, typeToken.getType());
    }

    public static <T> String toStringSpecified(Gson specifiedGson, T object, TypeToken<T> typeToken) {
        return specifiedGson.toJson(object, typeToken.getType());
    }

}
