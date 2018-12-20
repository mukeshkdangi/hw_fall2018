package edu.usc.cs572.hw3;

public abstract class AbstractWordIndexer {

    abstract public void preProcess(String[] args) throws Exception;

    abstract public void process();

    abstract public void postProcess();
}
