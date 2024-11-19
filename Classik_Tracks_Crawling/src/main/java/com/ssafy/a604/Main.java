package com.ssafy.a604;

import com.ssafy.a604.crawling.CrawlingClassic;
import com.ssafy.a604.crawling.CrawlingComposer;

public class Main {

    public static void main(String[] args) {
        new CrawlingComposer().startCrawling();
        new CrawlingClassic().startCrawling();
    }

}