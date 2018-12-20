package edu.usc.cs572.hw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;

public class ParserDem {
    private static ArrayList<Double> ttr = new ArrayList<Double>();
    static String output = "/Users/mukesh/Office/Tools/Solr/solr-7.5.0/big.txt";

    public static void main(String[] args) throws FileNotFoundException {
        final File fileOrFolder = new File("/Users/mukesh/Office/Tools/Solr/solr-7.5.0/nypost/nypost");

        File out_file = new File(output);
        if (out_file.exists())
            out_file.delete();
        int count = 0;
        PrintWriter writer = new PrintWriter(output);
        ;
        try {
            for (File file : fileOrFolder.listFiles()) {

                count++;

                BodyContentHandler handler = new BodyContentHandler(-1);


                Metadata metadata = new Metadata();


                ParseContext pcontext = new ParseContext();


                HtmlParser htmlparser = new HtmlParser();


                FileInputStream inputstream = new FileInputStream(file);


                htmlparser.parse(inputstream, handler, metadata, pcontext);


                String content = handler.toString();


                String words[] = content.split(" ");


                for (String t : words)


                {


                    if (t.matches("[a-zA-Z]+\\.?"))


                    {

                        writer.print(t.toLowerCase() + "\n");


                    }


                }


            }


        } catch (Exception e) {


            System.err.println("Caught IOException: " + e.getMessage());


            e.printStackTrace();


        }
        writer.close();


    }


}