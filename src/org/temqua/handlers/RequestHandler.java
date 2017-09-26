package org.temqua.handlers;

import org.temqua.databaseService.DatabaseException;
import org.temqua.databaseService.DatabaseService;
import org.temqua.databaseService.dataSets.LogDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RequestHandler {

    public void handle(HttpServletRequest request, HttpServletResponse response, DatabaseService database) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Timestamp requestTime = Timestamp.valueOf(LocalDateTime.now());
        String textForTranslate = request.getParameter("translateText");
        String toLang = request.getParameter("toLang");
        String fromLang = request.getParameter("fromLang");
        StringBuilder yandexResponse = new StringBuilder();
        String statusMessage;
        if ((textForTranslate == null) || (textForTranslate.equals(""))) {
            response.getWriter().println("You entered no text for translate!");
            statusMessage = "Error. User entered no text.";
        } else {
            try {
                yandexResponse.append(translateByYandex(request, response, session, textForTranslate));
            } catch (IOException exception) {
                String errorMessage = "Error connection to server. Check your internet settings. \n" + exception.toString();
                response.getWriter().println(errorMessage);
                statusMessage = "Error with connection to server. " + exception.toString();
                log(database, requestTime, statusMessage, toLang, fromLang, textForTranslate, yandexResponse.toString(), request.getRemoteAddr());
                return;
            }
            yandexResponse.trimToSize();
            String resp = yandexResponse.toString().length() == 0 ? "Error while translating by yandex service." : yandexResponse.toString();
            response.getWriter().println(resp);
            statusMessage = yandexResponse.toString().length() == 0 ? "Error with translating by yandex service." : "Successfully translated";
        }
        log(database, requestTime, statusMessage, toLang, fromLang, textForTranslate, yandexResponse.toString(), request.getRemoteAddr());

    }


    public String translateByYandex(HttpServletRequest request, HttpServletResponse response, HttpSession session, String text) throws ServletException, IOException {

        String toLang = request.getParameter("toLang");
        String fromLang = request.getParameter("fromLang");
        String lang = fromLang + '-' + toLang;
        String key = "trnsl.1.1.20170824T195439Z.d5d39835b76bd84e.1d792ffebf0c49922175557c35dca874392a48a1";
        StringBuilder translatedText = new StringBuilder();
        String codedText = URLEncoder.encode(text, "UTF-8");
        String address = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang=" + lang + "&key="
                + key + "&text=" + codedText;
        translatedText.append(connectToYandex(address));
        return translatedText.toString();
    }

    public String connectToYandex(String address) throws IOException {
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        connection.connect();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String rc;
        StringBuilder sb = new StringBuilder();
        while ((rc = bfr.readLine()) != null) {
            sb.append(rc);
        }
        bfr.close();
        return sb.toString();
    }

    public void log(DatabaseService database, Timestamp requestTime, String statusMess, String toLang, String fromLang, String word, String yandexResponse, String ipAddr) {
        Timestamp responseTime = Timestamp.valueOf(LocalDateTime.now());
        try {
            long id = database.addLog(requestTime, responseTime, statusMess, toLang, fromLang, word, yandexResponse, ipAddr);
            LogDataSet dataSet = database.getLog(id);
            LogHandler.getLogList().add(dataSet.toString());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

    }
}
