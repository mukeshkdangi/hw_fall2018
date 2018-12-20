package com.nypost.crawler;

import org.apache.log4j.Logger;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Controller {
    final static Logger logger = Logger.getLogger(Controller.class);
    public static String USER_AGENT = "MUKESH_DANGI";
    public static String HTTPS_NY_POST_NEWS = "https://nypost.com/";
    public static String HTTP_NY_POST_NEWS = "http://nypost.com/";

    static public void main(String... args) throws Exception {
        String crawlStorageFolder = "data/crawl";

        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(7);
        config.setMaxPagesToFetch(100);
        // config.setPolitenessDelay();
        config.setUserAgentString(USER_AGENT);
        config.setConnectionTimeout(20000);
        config.setIncludeBinaryContentInCrawling(true);




        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(true);

        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(HTTPS_NY_POST_NEWS);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(NYPOSTCrawler.class, numberOfCrawlers);
       // NYPOSTCrawler.onBeforeExitCrawler();
    }
}
