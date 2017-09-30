package org.temqua.handlers;

import org.temqua.databaseService.DatabaseException;
import org.temqua.databaseService.DatabaseService;
import org.temqua.databaseService.dataSets.LogDataSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogHandler {

	public static void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseService database = new DatabaseService();
		try {
			List<LogDataSet> logDataSets = database.getLogsContent();
			for (LogDataSet logDataSet : logDataSets) {
				response.getWriter().println(logDataSet.toString(false));
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
}
