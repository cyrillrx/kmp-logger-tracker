package com.cyrilleroux.android.external.volley;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * @author Cyril Leroux
 *         Created 21/12/2014.
 */
public class GsonError<T> {

    private final Gson mGson = new Gson();
    private final Class<T> mClass;

    /**
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonError(Class<T> clazz) { mClass = clazz; }

    public T parse(VolleyError error) {
        if (error.networkResponse == null) {
            return null;
        }
        return parseNetworkResponse(error.networkResponse).result;
    }

    protected Response<T> parseNetworkResponse(@NonNull NetworkResponse response) {

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}
