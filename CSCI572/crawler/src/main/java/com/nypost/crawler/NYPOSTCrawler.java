package com.nypost.crawler;

import com.nypost.Contants.WebConstant;
import com.nypost.Utils.CSVUtils;
import com.nypost.pojo.FetechNYPost;
import com.nypost.pojo.NYPostCrawlInfo;
import com.nypost.pojo.URLsNYPost;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class NYPOSTCrawler extends WebCrawler {
    final static Logger logger = Logger.getLogger(Controller.class);

    static List<NYPostCrawlInfo> visitNyPostData = new ArrayList<>();
    static List<FetechNYPost> fetchNyPostData = new ArrayList<>();
    static Set<URLsNYPost> urlsNyPostData = new HashSet<>();
    static long totalURLCount = 0;

    private void addURLsNYPost(WebURL curURL) {
        try {
            String href = curURL.getURL().toLowerCase();
            String status = (href.startsWith(Controller.HTTPS_NY_POST_NEWS) ||
                    href.startsWith(Controller.HTTP_NY_POST_NEWS)) ? WebConstant.STATUS_OK : WebConstant.STATUS_N_OK;
            URLsNYPost urlsNyPost = URLsNYPost.builder().url(href).okayStatus(status).build();

            urlsNyPostData.add(urlsNyPost);
            totalURLCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean shouldReturnIndicator(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !WebConstant.FILTERS.matcher(href).matches()
                && (href.startsWith(Controller.HTTPS_NY_POST_NEWS) || href.startsWith(Controller.HTTP_NY_POST_NEWS));
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        addURLsNYPost(url);
        return shouldReturnIndicator(referringPage, url);
    }


    private void updateVisitStats(Page page) {
        String url = page.getWebURL().getURL();
        String contentType = page.getContentType();
        if (contentType.contains(WebConstant.TEXT_HTML)) {
            contentType = WebConstant.TEXT_HTML;
        }

        Set<WebURL> outGoingLinks = new HashSet<>();
        try {
            if (page.getParseData().getClass().isInstance(edu.uci.ics.crawler4j.parser.BinaryParseData.class)) {
                BinaryParseData htmlParseData = (BinaryParseData) page.getParseData();
                outGoingLinks = htmlParseData.getOutgoingUrls();
            } else {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                outGoingLinks = htmlParseData.getOutgoingUrls();
            }
        } catch (Exception e) {

        }


        NYPostCrawlInfo nyPostCrawlInfo = NYPostCrawlInfo.builder().url(url.replaceAll(",", "_")).
                statusCode(String.valueOf(page.getStatusCode())).
                contentType(contentType).outLinkNumbers(outGoingLinks.size()).
                contentSize(page.getContentData().length / 1024)
                .build();
        visitNyPostData.add(nyPostCrawlInfo);
    }


    @Override
    public void visit(Page page) {
        updateVisitStats(page);
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        FetechNYPost fetchData = FetechNYPost.builder().url(webUrl.getURL().replaceAll(",", "_")).
                statusCode(String.valueOf(statusCode)).build();
        fetchNyPostData.add(fetchData);

    }


    public static void onBeforeExitCrawler() {
        try {
            FileWriter writer = new FileWriter(WebConstant.FETCH_NYPOST);
            CSVUtils.writeLine(writer, Arrays.asList(WebConstant.URL, WebConstant.STATUS_CODE));
            buildFetchCSV(writer);


            writer = new FileWriter(WebConstant.VISIT_NYPOST);
            CSVUtils.writeLine(writer, Arrays.asList(WebConstant.URL, WebConstant.CONTENT_SIZE, WebConstant.OUTGOING_URL_NUMBER, WebConstant.CONTENT_TYPE));
            buildVisitCSV(writer);


            writer = new FileWriter(WebConstant.URL_NYPOST);
            CSVUtils.writeLine(writer, Arrays.asList(WebConstant.URL, WebConstant.STATUS_OK_NOK));
            buildURLCSV(writer);

            writer = new FileWriter(WebConstant.OK_NOVISIT_NYPOST);
            CSVUtils.writeLine(writer, Arrays.asList(WebConstant.URL));

            StringBuffer sbuff = new StringBuffer();
            sbuff.append(WebConstant.AUTH_NAME).append(WebConstant.AUTH_ID).append(WebConstant.CRAWL_WEB).append(WebConstant.SEG_LINE).
                    append(WebConstant.FETCH_STAT).append(WebConstant.SEG_LINE);


            long featchSucceded = 0;
            long twoHund = 0, threeHundOne = 0, fourNotOne = 0, fourNotThree = 0, fourNotFour = 0, other = 0;


            for (int idx = 0; idx < fetchNyPostData.size(); idx++) {
                FetechNYPost data = fetchNyPostData.get(idx);
                int statusCode = Integer.valueOf(data.getStatusCode());

                if (statusCode >= 200 && statusCode <= 299) featchSucceded++;
                else if (statusCode == 301) threeHundOne++;
                else if (statusCode == 401) fourNotOne++;
                else if (statusCode == 403) fourNotThree++;
                else if (statusCode == 404) fourNotFour++;
                else other++;


            }

            int htmlCount = 0, imageGifCount = 0, imageJPEGCount = 0, imagePNGCount = 0, pdfCount = 0;
            int lessOneKB = 0, oneToTenKB = 0, tenToHunKB = 0, hundToOneMB = 0, moreThanOneMB = 0, otherFileTypes = 0, json = 0, xml = 0;

            for (int idx = 0; idx < visitNyPostData.size(); idx++) {

                NYPostCrawlInfo data = visitNyPostData.get(idx);
                if (data.getContentType().contains(WebConstant.TEXT_HTML)) htmlCount++;
                else if (data.getContentType().contains(WebConstant.IMAGE_GIF)) imageGifCount++;
                else if (data.getContentType().contains(WebConstant.IMAGE_JPEG)) imageJPEGCount++;
                else if (data.getContentType().contains(WebConstant.IMAGE_PNG)) imagePNGCount++;
                else if (data.getContentType().contains(WebConstant.APP_PDF)) pdfCount++;
                else if (data.getContentType().contains(WebConstant.APP_JSON)) json++;
                else if (data.getContentType().contains(WebConstant.APP_RSS_XML)) xml++;
                else otherFileTypes++;

                if (data.getContentSize() < 1) lessOneKB++;
                else if (data.getContentSize() >= 1 && data.getContentSize() < 10) oneToTenKB++;
                else if (data.getContentSize() >= 10 && data.getContentSize() < 100) tenToHunKB++;
                else if (data.getContentSize() >= 100 && data.getContentSize() < 1024)
                    hundToOneMB++;
                else moreThanOneMB++;


            }

            //long totalOutGoingNumbers = visitNyPostData.parallelStream().mapToInt(data -> data.getOutLinkNumbers()).sum();

            long OKCount = 0, NOKCount = 0;
            Iterator<URLsNYPost> itr = urlsNyPostData.iterator();
            while (itr.hasNext()) {
                URLsNYPost data = itr.next();
                if (data.getOkayStatus().equals(WebConstant.STATUS_OK)) {
                    OKCount++;
                } else {
                    NOKCount++;
                }

            }


            sbuff.append(WebConstant.FETCH_ATEMP + fetchNyPostData.size()).append(WebConstant.FETEH_SCUS + featchSucceded);
            sbuff.append(WebConstant.FETCH_ABORT_FAIL + (fetchNyPostData.size() - featchSucceded));

            sbuff.append(WebConstant.OUTGOING_URL);
            sbuff.append(WebConstant.SEG_LINE).
                    append(WebConstant.TOTAL_URL_EXT + totalURLCount);
            sbuff.append(WebConstant.TOTAL_UNQ_URL_EXT + (OKCount + NOKCount));

            sbuff.append(WebConstant.TOTAL_UNQ_URL_INSIDE + OKCount);
            sbuff.append(WebConstant.TOTAL_UNQ_URL_OUTSIDE + NOKCount);

            sbuff.append(WebConstant.SEG_LINE);
            sbuff.append(WebConstant.STATUS_CODES);
            sbuff.append(WebConstant.SEG_LINE);
            sbuff.append(WebConstant.TWO_100 + featchSucceded);
            sbuff.append(WebConstant.THREE_101 + threeHundOne);
            sbuff.append(WebConstant.FOUR_101 + fourNotOne);
            sbuff.append(WebConstant.FOUR_103 + fourNotThree);
            sbuff.append(WebConstant.FOUR_104 + fourNotFour);
            sbuff.append(WebConstant.OTHER_CODE + other);

            sbuff.append(WebConstant.SEG_LINE);
            sbuff.append(WebConstant.FILE_SIZES).append(WebConstant.SEG_LINE);

            sbuff.append(WebConstant.LESS_1).append(lessOneKB);
            sbuff.append(WebConstant.ONE_TEN_KB).append(oneToTenKB);
            sbuff.append(WebConstant.TEN_HUND_KB).append(tenToHunKB);
            sbuff.append(WebConstant.HUND_HUND_ONE_MB).append(hundToOneMB);
            sbuff.append(WebConstant.MORE_THANN_ONE_MB).append(moreThanOneMB);

            sbuff.append(WebConstant.SEG_LINE);
            sbuff.append(WebConstant.CONTENT_TYPE_STR);
            sbuff.append(WebConstant.SEG_LINE);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.TEXT_HTML + WebConstant.SPACE_COL + htmlCount);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.APP_JSON + WebConstant.SPACE_COL + json);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.APP_RSS_XML + WebConstant.SPACE_COL + xml);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.IMAGE_GIF + WebConstant.SPACE_COL + imageGifCount);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.IMAGE_JPEG + WebConstant.SPACE_COL + imageJPEGCount);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.IMAGE_PNG + WebConstant.SPACE_COL + imagePNGCount);
            sbuff.append(WebConstant.NEW_LINE + WebConstant.APP_PDF + WebConstant.SPACE_COL + pdfCount);



            buildCrawlReportNYPosy(sbuff.toString());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private static void buildCrawlReportNYPosy(String content) {
        try {
            PrintWriter writer = new PrintWriter(new File(WebConstant.CRAWL_REPORT_NYPOST), "UTF-8");
            writer.print(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private static void buildURLCSV(FileWriter writer) {
        try {
            urlsNyPostData.parallelStream().forEach(data -> {
                try {
                    CSVUtils.writeLine(writer, Arrays.asList(data.getUrl(), String.valueOf(data.getOkayStatus())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("urlsNyPostData " + urlsNyPostData.size());
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }

    private static void buildVisitCSV(FileWriter writer) {
        try {
            visitNyPostData.parallelStream().forEach(data -> {
                try {
                    CSVUtils.writeLine(writer, Arrays.asList(data.getUrl(),
                            String.valueOf(data.getContentSize()), String.valueOf(data.getOutLinkNumbers()),
                            data.getContentType()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("NyPostData " + visitNyPostData.size());
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }


    public static void buildFetchCSV(FileWriter writer) {
        try {
            fetchNyPostData.parallelStream().forEach(data -> {
                try {
                    CSVUtils.writeLine(writer, Arrays.asList(data.getUrl(), data.getStatusCode()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("fetchNyPostData " + fetchNyPostData.size());
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
    }
}
