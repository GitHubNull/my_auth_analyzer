package com.protect7.authanalyzer.filter;

import burp.IBurpExtenderCallbacks;
import burp.IRequestInfo;
import burp.IResponseInfo;

public class FileTypeFilter extends RequestFilter {
	

	public FileTypeFilter(int filterIndex, String description) {
		super(filterIndex, description);
		setFilterStringLiterals(new String[]{"js", "script", "css", "png", "jpg", "jpeg", "gif", "svg", "bmp", "woff", "ico"});
	}
	
	@Override
	public boolean filterRequest(IBurpExtenderCallbacks callbacks, int toolFlag, IRequestInfo requestInfo, IResponseInfo responseInfo) {
		if(onOffButton.isSelected() && responseInfo != null) {
			String url = requestInfo.getUrl().toString().toLowerCase();
			for(String fileType : stringLiterals) {
				if(url.endsWith(fileType.toLowerCase()) && !fileType.equals("") || 
						(fileType.toLowerCase().equals(responseInfo.getInferredMimeType().toLowerCase()) && !fileType.trim().equals(""))) {
					incrementFiltered();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasStringLiterals() {
		return true;
	}
}