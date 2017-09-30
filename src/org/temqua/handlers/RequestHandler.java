package org.temqua.handlers;

import org.temqua.bundles.ResBundle;
import org.temqua.databaseService.DatabaseException;
import org.temqua.databaseService.DatabaseService;
import org.temqua.databaseService.dataSets.LogDataSet;
import org.temqua.sessionService.SessionService;

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
        ResBundle bundle = new ResBundle();
        database.createTable();
        HttpSession session = request.getSession();
        SessionService.getSessionsQueue().add(session);
        //Номер текущей сессии определяется текущим количеством элементов в очереди
        int sessionNumber = SessionService.getSessionsQueue().size();
        String sessionID = session.getId();
        Timestamp requestTime = Timestamp.valueOf(LocalDateTime.now());
        //Принимаем параметры запроса
        String textForTranslate = request.getParameter(bundle.getValue("parameter.textForTranslation"));
        String toLang = request.getParameter(bundle.getValue("parameter.toLanguage"));
        String fromLang = request.getParameter(bundle.getValue("parameter.fromLanguage"));
        StringBuilder yandexResponse = new StringBuilder();
        String statusMessage;
        if ((textForTranslate == null) || (textForTranslate.equals(""))) {
            response.getWriter().println(bundle.getValue("translate.error.noText"));
            statusMessage = bundle.getValue("status.error.noText");
        } else {
            try {
                yandexResponse.append(translateByYandex(request, response, session, textForTranslate));
            } catch (IOException exception) {
                String errorMessage =  bundle.getValue("translate.error.connection") + exception.toString();
                response.getWriter().println(errorMessage);
                statusMessage = bundle.getValue("status.error.connection") + exception.toString();
              //добавляем лог в таблицу
                log(database, requestTime, statusMessage, toLang, fromLang, textForTranslate, yandexResponse.toString(), request.getRemoteAddr(), sessionNumber, sessionID);
                SessionService.getSessionsQueue().remove(session);
                return;
            }
            yandexResponse.trimToSize();
            //Если ответ от яндекса пуст, значит ошибка
            String resp = yandexResponse.toString().length() == 0 ? bundle.getValue("translate.error.main") : yandexResponse.toString();
            response.getWriter().println(resp);
            statusMessage = yandexResponse.toString().length() == 0 ? bundle.getValue("translate.error.main"): bundle.getValue("translate.success");
        }
        //добавляем лог в таблицу
        log(database, requestTime, statusMessage, toLang, fromLang, textForTranslate, yandexResponse.toString(), request.getRemoteAddr(), sessionNumber, sessionID);
        //После обработки, удаляем сессию из очереди
        SessionService.getSessionsQueue().remove(session);
    }


    public String translateByYandex(HttpServletRequest request, HttpServletResponse response, HttpSession session, String text) throws ServletException, IOException {
        ResBundle bundle = new ResBundle();
        String toLang = request.getParameter(bundle.getValue("parameter.toLanguage"));
        String fromLang = request.getParameter(bundle.getValue("parameter.fromLanguage"));
        String lang = fromLang + '-' + toLang;
        //API-ключ для работы с переводчиком
        String key = bundle.getValue("yandex.apiKey");
        StringBuilder translatedText = new StringBuilder();
        String codedText = URLEncoder.encode(text, "UTF-8");
        String address = bundle.getValue("yandex.address") + "?lang=" + lang + "&key="
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

    public void log(DatabaseService database, Timestamp requestTime, String statusMess, String toLang, String fromLang, String word, String yandexResponse, String ipAddr, Integer sessionNumber, String sessionID) {
        Timestamp responseTime = Timestamp.valueOf(LocalDateTime.now());
        try {
            database.addLog(requestTime, responseTime, statusMess, toLang, fromLang, word, yandexResponse, ipAddr, sessionNumber, sessionID);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

    }
}
