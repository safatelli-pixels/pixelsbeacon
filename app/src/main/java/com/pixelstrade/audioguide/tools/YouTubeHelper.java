package com.pixelstrade.audioguide.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeHelper {

   static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    static final String[] videoIdRegex = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};

    public static String extractVideoIdFromUrl(String url) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);

        for (String regex : videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    private static String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }
}