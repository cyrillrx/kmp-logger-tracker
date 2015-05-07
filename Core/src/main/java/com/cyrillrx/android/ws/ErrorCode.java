package com.cyrillrx.android.ws;

/**
 * @author Cyril Leroux
 *         Created on 04/02/2015.
 */
public class ErrorCode {

    public final static int OK = 200;

    /**
     * The server cannot or will not process the request due to something that is perceived to be a client error.
     */
    public final static int BAD_REQUEST = 400;

    /**
     * Similar to 403 Forbidden, but specifically for use when authentication is required and has failed or has not yet been provided.
     */
    public final static int UNAUTHORIZED = 401;

    /**
     * The request was a valid request, but the server is refusing to respond to it.
     * Unlike a 401 Unauthorized response, authenticating will make no difference.
     */
    public final static int FORBIDDEN = 403;

    /**
     * The requested resource could not be found but may be available again in the future.
     * Subsequent requests by the client are permissible.
     */
    public final static int NOT_FOUND = 404;

    /**
     * The server timed out waiting for the request.
     * According to HTTP specifications:
     * "The client did not produce a request within the time that the server was prepared to wait.
     * The client MAY repeat the request without modifications at any later time."
     */
    public final static int REQUEST_TIMEOUT = 408;

    /**
     * A generic error message, given when an unexpected condition was encountered and no more specific message is suitable.
     */
    public final static int INTERNAL_SERVER_ERROR = 500;
}
