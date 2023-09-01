package uk.co.gamma.address.model;

/**
 * A Zone represents a geographical area. Contains a postcode field.
 * The postcode is used to identify a zone. Used by the blacklistService.
 */
public class Zone {

    private String postCode;

    public Zone(String postCode) {
        this.postCode = postCode;
    }

    public Zone() {

    }

    /**
     * getPostCode.

     * @return the postcode String.
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * setPostCode.

     * @param postCode the string postcode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


}
