/**
 * 
 */
package ro.progsquad.chessmanager.factories;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.dao.EmptyResultDataAccessException;

import ro.progsquad.chessmanager.dao.HtmlDAO;
import ro.progsquad.chessmanager.model.Player;

/**
 * @author cgdobre
 *
 */
public class PlayerFactory {
	
	public static final String MEMBER_GAME_ARCHIVE_URL = "/home/game_archive?member=";
	public static final String MEMBER_URL = "/members/view/";
	public static final String TEAMS_URL = "/groups/mygroups?username=";
	
	public static Player build(String username) throws ParseException, IOException {
		Player player;
		try {
			player = Player.findPlayersByUsernameEquals(username).getSingleResult();
			// update player
			Element page = HtmlDAO.getBody(HtmlDAO.BASE_URL + MEMBER_URL + username);
			
			// get and validate player profile page
			if (page.select("h1:containsOwn(Member Account Closed)").first() != null) {
				player.setIsDisabled(true);
				player.merge();
				return player;
			}
			
			parseOnlineRating(player, page);
			parseGroupCount(player, page);
			parseOnlineChessStats(player, page);
			
			player.setLastUpdate(Calendar.getInstance().getTime());
			
			player.merge();
			
		} catch (EmptyResultDataAccessException e) {
			// connect
			Element page = null;
			try {
				page = HtmlDAO.getBody(HtmlDAO.BASE_URL + MEMBER_URL + username);
			} catch (Exception e2) {
				System.err.println("Could not get member page for " + username + ": " + e2.getStackTrace());
			}
			
			// build player
			player = new Player();
			player.setUsername(username);
			
			// get and validate player profile page
			if (page == null || page.select("h1:containsOwn(Member Account Closed)").first() != null) {
				player.setIsDisabled(true);
				Date fakeMemberSinceDate = Calendar.getInstance().getTime();
				fakeMemberSinceDate.setTime(fakeMemberSinceDate.getTime() - 600 * 1000); // ensure it's in the past
				player.setMemberSince(fakeMemberSinceDate);
				player.persist();
				return player;
			}
			
			parseOnlineRating(player, page);
			parseGroupCount(player, page);
			parseOnlineChessStats(player, page);
			
			// parse member since
			Element aboutMeElement = page.getElementById("profile-tabs").parent()
										 .getElementsByAttributeValue("class", "section-content section-content-2").first()
										 .getElementsByTag("ul").first()
										 .select("li > strong:containsOwn(Member Since:)").first()
										 .parent();
			String memberSinceString = aboutMeElement.text();
			memberSinceString = memberSinceString.substring(memberSinceString.indexOf(":") + 1).trim();
			Date memberSince = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US).parse(memberSinceString);
			player.setMemberSince(memberSince);
			
			player.persist();
		}
		
		return player;
	}

	/**
	 * @param player
	 * @param page
	 */
	private static void parseOnlineChessStats(Player player, Element page) {
		Element onlineChessStatsElement = page.getElementById("c19")
											  .getElementsByTag("tbody").first();
		Iterator<Element> statsIterator = onlineChessStatsElement.children().iterator();
		while (statsIterator.hasNext()) {
			Element stat = statsIterator.next();
			if ("Timeouts:".equals(stat.getElementsByTag("th").first().text())) {
				String timeoutString = stat.getElementsByTag("td").first().text();
				timeoutString = timeoutString.substring(0, timeoutString.indexOf("%"));
				player.setTimeout(Integer.parseInt(timeoutString));
			} else if ("Total Games:".equals(stat.getElementsByTag("th").first().text())) {
				Element totalGamesElement = stat.getElementsByTag("td").first();
				String totalGames = totalGamesElement.getElementsByTag("a").first().text();
				String winsLossesDraws = totalGamesElement.getElementsByTag("span").first().text();
				player.setTotalGames(totalGames + " " + winsLossesDraws);
			}
		}
		
		// parse # of current games
		Element gamesElement = page.getElementById("profile-tabs").parent()
				 				   .getElementsByAttributeValue("class", "section-content section-content-3").first()
				 				   .getElementsByTag("div").first();
		if (gamesElement == null || gamesElement.getElementsByAttributeValue("class", "section-title").first() == null) {
			player.setCurrentGamesNo("0");
		} else {
			String currentGamesString = gamesElement.getElementsByAttributeValue("class", "section-title").first().text();
			currentGamesString = currentGamesString.substring(currentGamesString.indexOf("(") + 1, currentGamesString.indexOf(")"));
			player.setCurrentGamesNo(currentGamesString);
		}
	}

	/**
	 * @param player
	 * @param page
	 */
	private static void parseGroupCount(Player player, Element page) {
		Element playerGroupsElement = page.getElementsByAttributeValueStarting("class", "user-left-sidebar").first()
										  .getElementsByAttributeValueStarting("class", "groups-and-teams").first();
		if (playerGroupsElement == null) {
			player.setGroupCount(0);
		} else {
			player.setGroupCount(Integer.parseInt(playerGroupsElement.getElementsByAttributeValue("class", "parenthesis-link").first().text()));
		}
	}

	/**
	 * @param player
	 * @param page
	 */
	private static void parseOnlineRating(Player player, Element page) {
		Element onlineChessRating = page.getElementById("profile-tabs").parent()
										.getElementsByAttributeValue("class", "section-content section-content-1").first()
										.getElementsByAttributeValueStarting("class", "ratings").first()
										.getElementById("c19")
										.getElementsByAttributeValue("class", "right").first();
		if (StringUtils.equalsIgnoreCase("Unrated", onlineChessRating.text())) {
			player.setOnlineRating(0);
		} else {
			player.setOnlineRating(Integer.parseInt(onlineChessRating.text()));
		}
	}
}
