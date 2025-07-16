package uk.phsh.footyhub.rest.models;

/**
 * Model class for Http Rest Response
 * @author Peter Blackburn
 */
public class RestResponse {

    private String _responseBody;
    private int _responseCode;

    /**
     * @param responseBody The Http response body
     * @param responseCode The http response code
     */
    public RestResponse(String responseBody, int responseCode) {
        this._responseBody = responseBody;
        this._responseCode = responseCode;
    }

    /**
     * Returns the http response body
     * @return String The http response body as String
     */
    public String getResponseBody() { return _responseBody; }

    /**
     * Returns the http response code
     * @return in The http response code as int
     */
    public int getResponseCode() { return _responseCode; }

    /**
     * Sets the Http response body
     * @param responseBody Http response body as String
     */
    public void setResponseBody(String responseBody) {
        this._responseBody = responseBody;
    }

    /**
     * Sets the Http response code
     * @param responseCode Http response code as int
     */
    public void setResponseCode(int responseCode) {
        this._responseCode = responseCode;
    }

}
