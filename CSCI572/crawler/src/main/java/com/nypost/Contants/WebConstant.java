package com.nypost.Contants;

import java.util.regex.Pattern;

public class WebConstant {

    public final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|mp4|zip|gz))$");
    public static final String CRAWL_REPORT_NYPOST = "/Users/mukesh/Office/crawler/data/crawl/CrawlReport_nypost.txt";


    public static final String URL = "URL";
    public static final String CONTENT_SIZE = "Content Size";
    public static final String OUTGOING_URL_NUMBER = "No of Outgoing Link";
    public static final String CONTENT_TYPE = "Content Type";
    public static final String STATUS_CODE = "STATUS_CODE";
    public static final String STATUS_OK_NOK = "OK/N_OK";
    public static final String STATUS_OK = "OK";
    public static final String STATUS_N_OK = "N_OK";


    public static final String FETCH_NYPOST = "data/crawl/fetch_nypost.csv";
    public static final String VISIT_NYPOST = "data/crawl/visit_nypost.csv";
    public static final String URL_NYPOST = "data/crawl/url_nypost.csv";

    public static final String OK_NOVISIT_NYPOST = "data/crawl/ok_novisit_nypost.csv";

    public static final String AUTH_NAME = "\n Name :\t Mukesh Dangi \n";
    public static final String AUTH_ID = "\n USC ID :\t 4297380684";
    public static final String CRAWL_WEB = "\n News site crawled: nypost.com";

    public static final String SEG_LINE = "\n========================================================";
    public static final String FETCH_STAT = "\n\t\tFetch Statistics";
    public static final String FETCH_ATEMP = "\n# fetches attempted:\t";
    public static final String FETEH_SCUS = "\n# fetches succeeded:\t";
    public static final String FETCH_ABORT_FAIL = "\n# fetches Failed/Aborted:\t";


    public static final String OUTGOING_URL = "\n\nOutgoing URLs:";
    public static final String TOTAL_URL_EXT = "\n # Total URLs extracted::\t";
    public static final String TOTAL_UNQ_URL_EXT = "\n # unique URLs extracted::\t";
    public static final String TOTAL_UNQ_URL_INSIDE = "\n # Unique URLs within News Site:\t";
    public static final String TOTAL_UNQ_URL_OUTSIDE = "\n # Unique URLs outside News Site:\t";

    public static final String NEW_LINE = "\n";
    public static final String SPACE_COL = " : ";
    public static final String TEXT_HTML = "text/html";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_GIF = "image/gif";
    public static final String APP_PDF = "applciation/pdf";
    public static final String APP_JSON = "application/json";
    public static final String APP_RSS_XML = "application/rss+xml";


    public static final String STATUS_CODES = "\n\nStatus Codes:";
    public static final String TWO_100 = "\n 200 OK:\t";
    public static final String THREE_101 = "\n 301 Moved Permanently:\t";
    public static final String FOUR_101 = "\n 401 Unauthorized:\t";
    public static final String FOUR_103 = "\n 403 Forbidden:\t";
    public static final String FOUR_104 = "\n 404 Not Found:\t";
    public static final String OTHER_CODE = "\n Other Codes Count:\t";

    public static final String FILE_SIZES = "\n\n  File Sizes:";
    public static final String LESS_1 = "\n <1KB : ";
    public static final String ONE_TEN_KB = "\n 1KB ~ <10 KB : ";
    public static final String TEN_HUND_KB = "\n 10KB ~ <100 KB : ";
    public static final String HUND_HUND_ONE_MB = "\n 100KB ~ <1 MB : ";
    public static final String MORE_THANN_ONE_MB = "\n >=1 MB : ";

    public static final String CONTENT_TYPE_STR = "\n\n Content Types: ";


}
