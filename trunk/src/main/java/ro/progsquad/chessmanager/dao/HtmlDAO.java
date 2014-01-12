/**
 * 
 */
package ro.progsquad.chessmanager.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * @author cgdobre
 *
 */
public class HtmlDAO {
	public static final int TO = 50000;
	
	public static final String BASE_URL = "http://www.chess.com";
	
	private static Map<String, Element> cache = new HashMap<String, Element>();

	public static Element getBody(String url) throws IOException {
		if (cache.containsKey(url)) {
			return cache.get(url);
		}
		
		Element body = null;
		try {
			body = Jsoup.connect(url)
					    .timeout(HtmlDAO.TO)
					    .get().body()
					    .getElementById("body");
		} catch (IOException e) {
			body = Jsoup.connect(url)
					  	.timeout(HtmlDAO.TO)
					  	.get().body()
					  	.getElementById("body");
		}
		
		cache.put(url, body);
		
		return body;
	}
	
	/**
	 * @param anchorElement
	 * @return the id within the href attribute of the element
	 */
	public static Long parseIdFromAnchorElement(Element anchorElement) {
		String hrefAttribute = anchorElement.attr("href");
		String id = hrefAttribute.substring(hrefAttribute.lastIndexOf("id=") + "id=".length()).trim();
		return Long.parseLong(id);
	}

	/**
	 * @param page
	 * @return the url of the next page or {@code null} if there is no next page
	 */
	public static String parseNextPageURL(Element page) {
		Element paginationElement = page.getElementsByAttributeValue("class", "pagination").first()
										.getElementsByAttributeValue("class", "next-on").first();
		if (paginationElement != null) {
			return HtmlDAO.BASE_URL + paginationElement.getElementsByTag("a").first().attr("href");
		}
		return null;
	}
}
