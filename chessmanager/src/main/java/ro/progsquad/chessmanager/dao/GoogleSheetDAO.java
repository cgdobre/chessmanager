package ro.progsquad.chessmanager.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleSheetDAO {

	/** Our view of Google Spreadsheets as an authenticated Google user. */
	private SpreadsheetService service = new SpreadsheetService("ProgSquad-ChessManager-2.0.0-SNAPSHOT");

	/** A factory that generates the appropriate feed URLs. */
	private FeedURLFactory factory = FeedURLFactory.getDefault();
	
	private WorksheetEntry worksheet = null;

	private GoogleSheetDAO() {}
	private static Map<String, GoogleSheetDAO> worksheetDAOs = new HashMap<String, GoogleSheetDAO>();
	
	public static GoogleSheetDAO getInstance(String workbookName, String sheetName) throws IOException, ServiceException {
		String instanceKey = workbookName + "/" + sheetName;
		if (worksheetDAOs.containsKey(instanceKey)) {
			return worksheetDAOs.get(instanceKey);
		}
		
		GoogleSheetDAO newDao = new GoogleSheetDAO();
		newDao.login(System.getProperty("googleUser"), System.getProperty("googleAppPassword"));
		newDao.loadSheet(workbookName, sheetName);
		
		worksheetDAOs.put(instanceKey, newDao);
		return newDao;
	}

	/**
	 * Log in to Google, under a Google Spreadsheets account.
	 * 
	 * @param username
	 *            name of user to authenticate (e.g. yourname@gmail.com)
	 * @param password
	 *            password to use for authentication
	 * @throws AuthenticationException
	 *            if the service is unable to validate the username and password.
	 */
	private void login(String username, String password) throws AuthenticationException {
		// Authenticate
		service.setUserCredentials(username, password);
	}

	/**
	 * Uses the user's credentials to get a list of spreadsheets. Then loads a sheet.
	 * 
	 * @throws ServiceException
	 *            when the request causes an error in the Google Spreadsheets service.
	 * @throws IOException
	 *            when an error occurs in communication with the Google Spreadsheets service.
	 */
	private void loadSheet(String sheetId, String worksheetName) throws IOException, ServiceException {
		// list spreadsheets
		SpreadsheetFeed feed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		
		// find spreadsheet by id
		for (SpreadsheetEntry sheet : spreadsheets) {
			if (StringUtils.equals(sheet.getTitle().getPlainText(), sheetId)) {
				Iterator<WorksheetEntry> worksheetIterator = sheet.getWorksheets().iterator();
				while (worksheetIterator.hasNext()) {
					WorksheetEntry worksheetEntry = worksheetIterator.next();
					if (worksheetEntry.getTitle().getPlainText().equals(worksheetName)) {
						worksheet = worksheetEntry;
						break;
					}
				}
				break;
			}
		}
		
		if (worksheet == null) {
			throw new ServiceException("No sheet found with id " + sheetId + " and worksheet " + worksheetName);
		}
	}
	
	/**
	 * Adds a new list entry.
	 * 
	 * @throws ServiceException
	 *             when the request causes an error in the Google Spreadsheets service.
	 * @throws IOException
	 *             when an error occurs in communication with the Google Spreadsheets service.
	 */
	public void addNewEntry(Map<String, String> nameValuePairs) throws IOException, ServiceException {
		ListEntry newEntry = new ListEntry();

		for (String tag : nameValuePairs.keySet()) {
			newEntry.getCustomElements().setValueLocal(tag, nameValuePairs.get(tag));
		}

		service.insert(worksheet.getListFeedUrl(), newEntry);
	}

	/**
	 * Performs a full database-like query on the rows.
	 * 
	 * @param structuredQuery
	 *            a query like: name = "Bob" and phone != "555-1212"
	 * @throws ServiceException
	 *             when the request causes an error in the Google Spreadsheets service.
	 * @throws IOException
	 *             when an error occurs in communication with the Google Spreadsheets service.
	 */
	public List<ListEntry> query(String structuredQuery) throws IOException, ServiceException {
		ListQuery query = new ListQuery(worksheet.getListFeedUrl());
		query.setSpreadsheetQuery(structuredQuery);
		
		List<ListEntry> result = null;
		
		try {
			result = service.query(query, ListFeed.class).getEntries();
		} catch (Exception e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			result = service.query(query, ListFeed.class).getEntries();
		}
		
		return result;
	}

	/**
	 * Updates an existing entry.
	 * 
	 * @param entry
	 *            the entry to update
	 * @param nameValuePairs
	 *            the name value pairs, such as "name=Rosa" to change the row's name field to Rosa
	 * @throws ServiceException
	 *             when the request causes an error in the Google Spreadsheets service.
	 * @throws IOException
	 *             when an error occurs in communication with the Google Spreadsheets service.
	 */
	public void update(ListEntry entry, Map<String, String> nameValuePairs) throws IOException, ServiceException {
		if (entry == null) {
			return;
		}
		
		for (String tag : nameValuePairs.keySet()) {
			entry.getCustomElements().setValueLocal(tag, nameValuePairs.get(tag));
		}
		
		entry.update(); // This updates the existing entry.
	}
	
	public List<ListEntry> getRows() throws IOException, ServiceException {
		ListFeed feed = service.getFeed(worksheet.getListFeedUrl(), ListFeed.class);
		return feed.getEntries();
	}

}
