package com.example.test_session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class MF {
    static final Logger log = LoggerFactory.getLogger(MF.class);

    public MF() {
    }

    public static String format(final String messagePattern, Object arg1) {
        return MessageFormatter.format(messagePattern, arg1).getMessage();
    }

    public static String format(final String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.format(messagePattern, arg1, arg2).getMessage();
    }

    public static String format(String messagePattern, Object... arguments) {
        return MessageFormatter.arrayFormat(messagePattern, arguments).getMessage();
    }
}
