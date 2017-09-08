package org.temqua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Controller() {

    }

    public String translateByYandex(HttpServletRequest request, HttpServletResponse response, HttpSession session, String text) throws ServletException, IOException {

        String toLang = request.getParameter("toLang");
        String fromLang = request.getParameter("fromLang");
        String lang = fromLang + '-' + toLang;
        String[] words = text.split("([\\s^.,*/$!%&()?]+)");
        String key = "trnsl.1.1.20170824T195439Z.d5d39835b76bd84e.1d792ffebf0c49922175557c35dca874392a48a1";
        StringBuilder translatedText = new StringBuilder();
        List<String> translatedWordsList = new ArrayList<String>();

        for (String word : words) {
            String codedText = URLEncoder.encode(word, "UTF-8");
            String address = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang=" + lang + "&key="
                    + key + "&text=" + codedText;
            String translatedWord = connectToYandex(address);
            translatedText.append(translatedWord);
        }
        return translatedText.toString();
    }

    public String connectToYandex(String address) throws IOException {
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        connection.connect();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String rc;
        StringBuilder sb = new StringBuilder();
        while ((rc = bfr.readLine()) != null)
            sb.append(rc);
        bfr.close();
        return sb.toString();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String textForTranslate = request.getParameter("translateText");
        StringBuilder stringBuilder = new StringBuilder();

        if (textForTranslate == null) {
            response.getWriter().println("You entered no text for translate!");
            return;
        }
        try {
            stringBuilder.append(translateByYandex(request, response, session, textForTranslate));
        } catch (IOException exception) {
            response.getWriter().println("Error connection to server. Check your internet settings.");
            exception.toString();
            response.getWriter().println(exception.toString());
            return;
        }
        stringBuilder.trimToSize();
        if (stringBuilder.toString().length() == 0)
            response.getWriter().println("Error while translating by yandex service.");
        else
            response.getWriter().println(stringBuilder.toString());

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
