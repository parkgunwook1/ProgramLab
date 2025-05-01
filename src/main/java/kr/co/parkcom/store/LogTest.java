package kr.co.parkcom.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        log.info("server start");
        log.warn("exception 주의");

        try {

        }catch(Exception e) {
        log.error("error : {}" , e.getMessage() );
        }
    }
}
