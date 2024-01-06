package com.protect7.authanalyzer.entities;

import burp.IHttpRequestResponse;

public class CopyAPIOriginalRequestResponse extends OriginalRequestResponse {
    public CopyAPIOriginalRequestResponse(int id, IHttpRequestResponse requestResponse, String method, String url, String infoText, int statusCode, int responseContentLength) {
        super(id, requestResponse, method, url, infoText, statusCode, responseContentLength);
    }

    public CopyAPIOriginalRequestResponse(OriginalRequestResponse originalRequestResponse) {
        super(originalRequestResponse.getId(), originalRequestResponse.getRequestResponse(),
                originalRequestResponse.getMethod(), originalRequestResponse.getUrl(),
                originalRequestResponse.getInfoText(), originalRequestResponse.getStatusCode(),
                originalRequestResponse.getResponseContentLength());
    }
}
