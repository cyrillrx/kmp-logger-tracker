package com.cyrillrx.external.volley;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cyrillrx.android.toolbox.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * @author Cyril Leroux
 *         Created 21/12/2014.
 */
public class GsonError<T> {

    private static final String TAG = GsonError.class.getSimpleName();

    private final Gson mGson = new Gson();
    private final Class<T> mClass;
    /** The server json response. */
    private String mJson;

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
            mJson = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Logger.verbose(TAG, "Json response  : " + mJson);
        } catch (UnsupportedEncodingException e) {
            Logger.error(TAG, "UnsupportedEncodingException", e);
            return Response.error(new ParseError(response));
        }

        try {
            return Response.success(mGson.fromJson(mJson, mClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            Logger.error(TAG, "Parsing error : " + mJson, e);
            return Response.error(new ParseError(response));
        }
    }

    /**
     * This function must be called after {@link #parse(com.android.volley.VolleyError)}
     *
     * @return The server json response
     */
    public String getJson() { return mJson; }
}
