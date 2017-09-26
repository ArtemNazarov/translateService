package org.temqua.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogHandler {
    private static List<String> logList = new ArrayList<>();


    public static List<String> getLogList() {
        return logList;
    }

    public static void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (String s : logList) {
            response.getWriter().println(s);
        }
    }
}
