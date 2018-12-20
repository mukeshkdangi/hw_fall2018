package com.nypost.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NYPostCrawlInfo {

    private String url;
    private String statusCode;
    private boolean isiInsideUrl;
    private double sideOfContent;
    private int outLinkNumbers;
    private String contentType;
    private double contentSize;


}
