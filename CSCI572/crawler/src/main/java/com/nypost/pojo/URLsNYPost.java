package com.nypost.pojo;

import javax.swing.*;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class URLsNYPost {

    private String url;
    private String okayStatus;
    private boolean isExcluded;
}
