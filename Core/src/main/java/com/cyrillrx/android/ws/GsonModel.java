package com.cyrillrx.android.ws;

import com.google.gson.Gson;

/**
 * Base class for ws data model.
 *
 * @author Cyril Leroux
 *         Created on 12/06/15
 */
public class GsonModel {

    public String serialize() { return new Gson().toJson(this); }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

}