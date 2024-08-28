package com.protect7.authanalyzer.gui.util;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import com.protect7.authanalyzer.entities.OriginalRequestResponse;
import com.protect7.authanalyzer.entities.SortParameter;
import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CopyAPIsTool {

    public static APIElement getAPIElement(OriginalRequestResponse originalRequestResponse) {
        if (originalRequestResponse == null) {
            return null;
        }

        IHttpRequestResponse requestResponse = originalRequestResponse.getRequestResponse();
        if (requestResponse == null) {
            return null;
        }

        IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(requestResponse);
        if (requestInfo == null) {
            return null;
        }

        List<String> headers = requestInfo.getHeaders();
        String httpVersion = extractHttpVersion(headers);

        APIElement apiElement = new APIElement();
        apiElement.setMethod(requestInfo.getMethod());
        apiElement.setPath(requestInfo.getUrl().getPath());
        apiElement.setQueryParams(requestInfo.getUrl().getQuery());
        apiElement.setHttpVersion(httpVersion + "@" + requestResponse.getHttpService().getProtocol());

        return apiElement;
    }

    public static String extractHttpVersion(List<String> headers)  {
        if (headers == null) {
            return null;
        }

        String firstLine = headers.get(0);
        if (firstLine == null) {
            return null;
        }
        String[] firstLineSplit = firstLine.split(" ");
        if (firstLineSplit.length != 3) {
            return null;
        }

        return firstLineSplit[2];
    }

    public static String getAPIStr(CopyAPIConfig config, OriginalRequestResponse originalRequestResponse) {
        List<String> apiElementStringList = new ArrayList<>();
        APIElement element = getAPIElement(originalRequestResponse);
        if (element == null) {
            return null;
        }

        config.apiElementList.forEach(apiElement -> {
            String elementStr = element.getAPIElementByAPIElementEnum(apiElement);
            if (elementStr != null) {
                apiElementStringList.add(elementStr);
            }
        });

        return String.join(config.separator, apiElementStringList);
    }

    public static String calculateAPIStrSha1(CopyAPIConfig config, OriginalRequestResponse originalRequestResponse) {
        List<String> apiElementStringList = new ArrayList<>();
        APIElement element = getAPIElement(originalRequestResponse);
        if (element == null) {
            return null;
        }
        config.getDuplicationAPIElementList().forEach(apiElement -> {
            String elementStr = element.getAPIElementByAPIElementEnum(apiElement);
            if (elementStr != null) {
                apiElementStringList.add(elementStr);
            }
        });
        String apiStr = String.join("", apiElementStringList);
        return DigestUtils.sha1Hex(apiStr);
    }

    public static List<OriginalRequestResponse> deduplicateAPIs(List<OriginalRequestResponse> originalRequestResponseList, CopyAPIConfig config) {
        if (originalRequestResponseList == null || originalRequestResponseList.isEmpty()) {
            return null;
        }

        List<OriginalRequestResponse> deuplicateList = new ArrayList<>();
        HashSet<String> apiStrSet = new HashSet<>();
        for (OriginalRequestResponse originalRequestResponse : originalRequestResponseList) {
            String apiStrSha1 = calculateAPIStrSha1(config, originalRequestResponse);
            if (apiStrSet.contains(apiStrSha1)) {
                continue;
            }
            apiStrSet.add(apiStrSha1);
            deuplicateList.add(originalRequestResponse);
        }
        return deuplicateList;
    }

    public static List<OriginalRequestResponse> sortOriginalRequestResponseList(List<OriginalRequestResponse> originalRequestResponseList, List<SortParameter> sortParameterList) {
        if (originalRequestResponseList == null || originalRequestResponseList.isEmpty() || 1 == originalRequestResponseList.size()) {
            return originalRequestResponseList;
        }
        List<APIElement> apiElementList = new ArrayList<>();
        List<OriginalRequestResponse> storedOriginalRequestResponseList = new ArrayList<>();
        HashMap<APIElement, OriginalRequestResponse> apiElementComparatorOriginalRequestResponseHashMap = new HashMap<>();

        for (OriginalRequestResponse originalRequestResponse : originalRequestResponseList) {
            APIElement apiElement = getAPIElement(originalRequestResponse);
            if (apiElement == null) {
                continue;
            }
//            storedOriginalRequestResponseList.add(originalRequestResponse);
            apiElementList.add(apiElement);
            apiElementComparatorOriginalRequestResponseHashMap.put(apiElement, originalRequestResponse);
        }

        APIElementComparator apiElementComparator = new APIElementComparator(sortParameterList);
        apiElementList.sort(apiElementComparator);

        for (APIElement apiElement : apiElementList) {
            storedOriginalRequestResponseList.add(apiElementComparatorOriginalRequestResponseHashMap.get(apiElement));
        }

        return storedOriginalRequestResponseList;
    }

    public static void copyToClipboard(String content) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(content);
            clipboard.setContents(selection, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String content, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
