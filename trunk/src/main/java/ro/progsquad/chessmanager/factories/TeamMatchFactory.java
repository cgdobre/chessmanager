/**
 * 
 */
package ro.progsquad.chessmanager.factories;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.EmptyResultDataAccessException;

import ro.progsquad.chessmanager.dao.HtmlDAO;
import ro.progsquad.chessmanager.model.Game;
import ro.progsquad.chessmanager.model.TeamMatch;

/**
 * @author cgdobre
 *
 */
public class TeamMatchFactory {
	
	public static final String TEAM_MATCH_URL = "/groups/team_match?id=";
	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

	public static TeamMatch build(Long teamMatchId) throws IOException, ParseException {
		TeamMatch teamMatch;
		try {
			teamMatch = TeamMatch.findTeamMatchesByTeamMatchIdEquals(teamMatchId).getSingleResult();
			if (teamMatch.getEndDate() != null) {
				return teamMatch;
			}
			// update of fields required
			parseEndDate(teamMatch, HtmlDAO.getBody(HtmlDAO.BASE_URL + TEAM_MATCH_URL + teamMatchId)
								  		   .getElementsByAttributeValue("class", "content left").first());
			teamMatch.merge();
			
			try {
				loadGames(teamMatch);
			} catch (Exception e2) {
				teamMatch.setEndDate(null); // allow for update in case of parse exception
				teamMatch.merge();
			}
		} catch (EmptyResultDataAccessException e) {
			// connect
			Element page = HtmlDAO.getBody(HtmlDAO.BASE_URL + TEAM_MATCH_URL + teamMatchId)
								  .getElementsByAttributeValue("class", "content left").first();
			teamMatch = new TeamMatch();
			teamMatch.setTeamMatchId(teamMatchId);
			
			// parse team match attributes
			String teamMatchName = page.getElementsByAttributeValue("class", "page-title").first().text();
			teamMatch.setTeamMatchName(teamMatchName);
			
			String registrationOpen = page.select("td:containsOwn(Registration Open:) + td").first().text();
			teamMatch.setStartDate(dateFormat.parse(registrationOpen));
			
			String gameType = page.select("td:containsOwn(Game Type:) + td").first().text();
			teamMatch.setGameType(gameType);
			
			String daysPerMove = page.select("td:containsOwn(Days per Move:) + td").first().text();
			teamMatch.setTimePerMove(daysPerMove);
			
			String ratingRange = page.select("td:containsOwn(Rating Range:) + td").first().text();
			teamMatch.setRatingRange(ratingRange);
			
			String startingPosition = page.select("td:containsOwn(Starting Position:) + td").first().text();
			teamMatch.setStartingPosition(startingPosition);
			
			String rated = page.select("td:containsOwn(Rated:) + td").first().text();
			teamMatch.setIsRated("Yes".equals(rated));
			
			String simultaneousGames = page.select("td > strong:matchesOwn([12] simultaneous game[s]?)").first().text();
			simultaneousGames = simultaneousGames.substring(0, simultaneousGames.indexOf("simultaneous")).trim();
			teamMatch.setSimultaneousGames(Integer.parseInt(simultaneousGames));
			if (teamMatch.getSimultaneousGames() != 1 && teamMatch.getSimultaneousGames() != 2) {
				throw new ParseException("Could not parse simultaneous games for " + teamMatch.getTeamMatchId()
						+ ". Expected 1 or 2 but got " + teamMatch.getSimultaneousGames(), 0);
			}
			
			// parse opponents
			Elements opponents = page.select("th > a[href^=" + TeamFactory.GROUP_URL + "]");
			if (opponents.size() != 2) {
				throw new ParseException("Could not parse opponents for team match " + teamMatchId
						+ ". Expected 2 opponents but got " + opponents.html(), 0);
			}
			String challenger = opponents.get(0).attr("href");
			challenger = challenger.substring(challenger.lastIndexOf("/") + 1);
			teamMatch.setChallengerTeam(TeamFactory.build(challenger, false));
			String responder = opponents.get(1).attr("href");
			responder = responder.substring(responder.lastIndexOf("/") + 1);
			teamMatch.setResponderTeam(TeamFactory.build(responder, false));
			
			// finally parse end date. needs to be last to allow for update in case of parse exception
			parseEndDate(teamMatch, page);
			
			teamMatch.persist();
			
			// parse games
			try {
				loadGames(teamMatch);
			} catch (Exception e2) {
				teamMatch.setEndDate(null); // allow for update in case of parse exception
				teamMatch.merge();
			}
		}
		
		return teamMatch;
	}

	/**
	 * @param teamMatch
	 * @param page
	 * @throws ParseException
	 */
	private static void parseEndDate(TeamMatch teamMatch, Element page) throws ParseException {
		Element finishedOnElement = page.select("td:containsOwn(Finished On:) + td").first();
		if (finishedOnElement == null) {
			teamMatch.setEndDate(null);
		} else {
			teamMatch.setEndDate(dateFormat.parse(finishedOnElement.text()));
		}
	}

	private static void loadGames(TeamMatch teamMatch) throws IOException, NumberFormatException, ParseException {
		Elements gameElements = HtmlDAO.getBody(HtmlDAO.BASE_URL + TEAM_MATCH_URL + teamMatch.getTeamMatchId())
				   					   .getElementsByAttributeValue("class", "content left").first()
				   					   .getElementsByAttributeValueMatching("id", "TeamMatchGame[0-9]+");

		Iterator<Element> gameIterator = gameElements.iterator();
		while (gameIterator.hasNext()) {
			String elementId = gameIterator.next().attr("id");
			elementId = elementId.substring(elementId.lastIndexOf("TeamMatchGame") + "TeamMatchGame".length());
			
			Game game = GameFactory.build(Long.parseLong(elementId));
			game.setTeamMatch(teamMatch);
			if (teamMatch.getEndDate() != null && game.getEndDate() != null
					&& teamMatch.getEndDate().compareTo(game.getEndDate()) < 0) {
				game.setEndDate(teamMatch.getEndDate());
			}
			game.merge();
		}
	}
}
