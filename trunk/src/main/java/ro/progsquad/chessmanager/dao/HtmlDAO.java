package ro.progsquad.chessmanager.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author cgdobre
 * 
 */
public class HtmlDAO {
	public static final int TO = 50000;

	public static final String BASE_URL = "http://www.chess.com";

	private static final int MAX_CACHE_SIZE = 350;

	private static Map<String, Element> cache = new HashMap<String, Element>();
	private static BiMap<Long, String> age = HashBiMap.create();

	public static Element getBody(String url) throws IOException {
		if (cache.containsKey(url)) {
			storeAge(url);
			return cache.get(url);
		}

		Element body = null;
		try {
			body = Jsoup.connect(url).timeout(HtmlDAO.TO).get().body()
					.getElementById("body");
		} catch (IOException e) {
			body = Jsoup.connect(url).timeout(HtmlDAO.TO).get().body()
					.getElementById("body");
		}

		cache.put(url, body);
		storeAge(url);

		cleanup();

		return body;
	}

	private static void storeAge(String entry) {
		Long time = System.currentTimeMillis();
		while (age.containsKey(time)) {
			time++;
		}
		
		age.inverse().put(entry, time);
	}

	private static void cleanup() {
		if (cache.size() < MAX_CACHE_SIZE) {
			return;
		}
		final List<Long> sortedList = asSortedList(age.keySet());
		for (int i = 0; i < 50; i++) {
			cache.remove(age.get(sortedList.get(i)));
			age.remove(sortedList.get(i));
		}
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	/**
	 * @param anchorElement
	 * @return the id within the href attribute of the element
	 */
	public static Long parseIdFromAnchorElement(Element anchorElement) {
		if (anchorElement == null) {
			throw new RuntimeException("HtmlDAO: Can not parse id from null anchor element.");
		}
		final String hrefAttribute = anchorElement.attr("href");
		final String id = hrefAttribute.substring(
				hrefAttribute.lastIndexOf("id=") + "id=".length()).trim();
		return Long.parseLong(id);
	}

	/**
	 * @param page
	 * @return the url of the next page or {@code null} if there is no next page
	 */
	public static String parseNextPageURL(Element page) {
		Element paginationElement = page
				.getElementsByAttributeValue("class", "pagination").first()
				.getElementsByAttributeValue("class", "next-on").first();
		if (paginationElement != null) {
			return HtmlDAO.BASE_URL
					+ paginationElement.getElementsByTag("a").first()
							.attr("href");
		}
		return null;
	}
}
