package edu.usc.cs572.hw3;
/**
 * Java 8 Spelling Corrector. Copyright 2016 Peter Kuhar.
 *
 * Open source code under MIT license: http://www.opensource.org/licenses/mit-license.php
 */

import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Spelling {
    private Map<String, Integer> dict = new HashMap<>();

    public void SpellingIt(Path dictionaryFile) throws Exception {
        String pat = "/Users/mukesh/Office/Tools/Solr/solr-7.5.0/big2.txt";
        dictionaryFile = Paths.get(pat);
        Stream.of(new String(Files.readAllBytes(dictionaryFile)).toLowerCase().replaceAll("[^a-z ]", "").split(" ")).forEach((word) -> {
            dict.compute(word, (k, v) -> v == null ? 1 : v + 1);
        });
        System.out.println(dict.entrySet());
        System.out.println(this.correct("tesla infia"));
    }

    public static void main(String ars[]) {
        try {
            new Spelling().SpellingIt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Stream<String> edits1(final String word) {
        Stream<String> deletes = IntStream.range(0, word.length()).mapToObj((i) -> word.substring(0, i) + word.substring(i + 1));
        Stream<String> replaces = IntStream.range(0, word.length()).mapToObj((i) -> i).flatMap((i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i + 1)));
        Stream<String> inserts = IntStream.range(0, word.length() + 1).mapToObj((i) -> i).flatMap((i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj((c) -> word.substring(0, i) + (char) c + word.substring(i)));
        Stream<String> transposes = IntStream.range(0, word.length() - 1).mapToObj((i) -> word.substring(0, i) + word.substring(i + 1, i + 2) + word.charAt(i) + word.substring(i + 2));
        return Stream.of(deletes, replaces, inserts, transposes).flatMap((x) -> x);
    }

    Stream<String> known(Stream<String> words) {
        return words.filter((word) -> dict.containsKey(word));
    }

    String correct(String word) {
        Optional<String> e1 = known(edits1(word)).max((a, b) -> dict.get(a) - dict.get(b));
        Optional<String> e2 = known(edits1(word).map((w2) -> edits1(w2)).flatMap((x) -> x)).max((a, b) -> dict.get(a) - dict.get(b));
        return dict.containsKey(word) ? word : (e1.isPresent() ? e1.get() : (e2.isPresent() ? e2.get() : word));
    }
}