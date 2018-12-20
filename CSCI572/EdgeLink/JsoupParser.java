package edu.usc.cs572.hw3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsoupParser {
    static Map<String, String> fileUrlMap = new HashMap<>();
    static Map<String, String> urlFileUrlMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        PrintWriter pwt = new PrintWriter(new FileWriter("/Users/mukesh/Office/Tools/Solr/solr-7.5.0/EdgeList.txt"));
        buildNameToURLMap();
        File dire = new File("/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/nypost");
        Set<String> edgeMap = new HashSet<>();

        for (File file : dire.listFiles()) {
            Document doc = Jsoup.parse(file, "UTF-8", fileUrlMap.get(file.getName()));
            Elements links = doc.select("a[href]");
            Elements pages = doc.select("src");

            int count = 0;
            int count2 = 0;
            for (Element link : links) {

                String url = link.attr("abs:href").trim();
                if (fileUrlMap.containsValue(url)) {
                    pwt.println(file.getName() + " " + urlFileUrlMap.get(url));
                    count++;
                }

                url = link.attr("href").trim();
                if (fileUrlMap.containsValue(url)) {
                    count2++;
                }

            }
            System.out.println(count);
            System.out.println(count2);
        }

        pwt.close();


    }

    private static void buildNameToURLMap() {
        String csvFile = "/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/URLtoHTML_nypost.csv";
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String[] urlMapDetails = line.split(cvsSplitBy);
                urlFileUrlMap.put(urlMapDetails[1], urlMapDetails[0]);
                fileUrlMap.put(urlMapDetails[0], urlMapDetails[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
