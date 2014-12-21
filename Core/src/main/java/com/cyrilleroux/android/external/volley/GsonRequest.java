package com.cyrilleroux.android.external.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Cyril Leroux
 *         Created 16/12/2014.
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson mGson = new Gson();
    private final Class<T> mClass;
    private final Map<String, String> mHeaders;
    private final Response.Listener<T> mListener;

    /** POST or PUT params */
    protected final Map<String, String> mParams;
    /** GET params */
    protected final Map<String, String> mGetParams;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mClass = clazz;
        mHeaders = headers;
        mListener = listener;
        mGetParams = new HashMap<>();
        mParams = null;
    }

    /**
     * Make a request with the given method type and return a parsed object from JSON.
     *
     * @param method  The request method type (one of the values from {@link Method})
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mHeaders = headers;
        mListener = listener;
        mGetParams = new HashMap<>();
        mParams = new HashMap<>();
    }

    /**
     * Applies GET params if any.
     *
     * @return The url containing the GET parameters.
     */
    @Override
    public String getUrl() {
        String url = super.getUrl();
        if (mGetParams.isEmpty()) {
            return url;
        }
        url += "?";
        Set<Map.Entry<String, String>> entries = mGetParams.entrySet();
        for (Map.Entry entry : entries) {
            url += "&" + entry.getKey() + "=" + entry.getValue();
        }
        return url;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) { mListener.onResponse(response); }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

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