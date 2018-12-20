package com.nypost.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FetechNYPost {

    private String url;
    private String statusCode;
}
