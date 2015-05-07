/**
 * 
 */
package ro.progsquad.chessmanager.factories;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.dao.EmptyResultDataAccessException;

import ro.progsquad.chessmanager.dao.HtmlDAO;
import ro.progsquad.chessmanager.model.Game;
import ro.progsquad.chessmanager.model.Player;

/**
 * @author cgdobre
 *
 */
public class GameFactory {
	
	public static final String GAME_URL = "/echess/game?id=";

	public static Game build(Long gameId) throws IOException, ParseException {
		Game game;
		try {
			game = Game.findGamesByGameIdEquals(gameId).getSingleResult();
			if (game.getEndDate() != null) {
				return game;
			}
			// update required
			Element resultElement = HtmlDAO.getBody(HtmlDAO.BASE_URL + GAME_URL + gameId)
										   .getElementsByAttributeValueStarting("class", "notice info").first();
			if (resultElement == null) {
				return game;
			} 
			parseGameResult(game, resultElement);
			
			game.merge();
		} catch (EmptyResultDataAccessException e) {
			// connect
			Element page = HtmlDAO.getBody(HtmlDAO.BASE_URL + GAME_URL + gameId);
			game = new Game();
			game.setGameId(gameId);
			
			// parse players
			String whitePlayer = page.getElementById("details")
									 .select("strong:containsOwn(White:) + a[href^=" + HtmlDAO.BASE_URL + PlayerFactory.MEMBER_URL + "]")
									 .first()
									 .text();
			String blackPlayer = page.select("strong:containsOwn(Black:) + a[href^=" + HtmlDAO.BASE_URL + PlayerFactory.MEMBER_URL + "]")
					 				 .first()
					 				 .text();
			game.setWhite(PlayerFactory.build(whitePlayer));
			game.setBlack(PlayerFactory.build(blackPlayer));
			
			// parse time per move
			Node timeElement = page.getElementById("details")
								   .select("strong:containsOwn(Time:)").first()
								   .nextSibling();
			if (!(timeElement instanceof TextNode)) {
				throw new ParseException("Can not parse time per move from <" + timeElement + "> for game " + game.getGameId(), 0);
			}
			game.setTimePerMove(((TextNode)timeElement).text());
			
			// parse # of moves
			String fenString = page.select("strong:containsOwn(FEN:) + input#textfield")
					                   .first()
					                   .attr("value");
			game.setNumberOfMoves(Integer.parseInt(fenString.substring(fenString.lastIndexOf(" ") + 1)));
			
			// parse start date
			Node dateElement = page.getElementById("details")
					  			   .select("strong:containsOwn(Start:)").first()
					  			   .nextSibling();
			if (!(dateElement instanceof TextNode)) {
				throw new ParseException("Can not parse date from <" + dateElement + "> for game " + game.getGameId(), 0);
			}
			String dateString = ((TextNode)dateElement).text();
			dateString = dateString.substring(0, dateString.indexOf("|") - 1).trim();
			game.setStartDate(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US).parse(dateString));
			
			// finally parse result. needs to be last to allow for update in case of parse exception
			Element resultElement = page.getElementsByAttributeValueStarting("class", "notice info").first();
			parseGameResult(game, resultElement);
		}
		
		return game;
	}

	/**
	 * @param game
	 * @param resultElement
	 * @throws ParseException
	 */
	private static void parseGameResult(Game game, Element resultElement) throws ParseException {
		if (resultElement == null) {
			if (game.getEndDate() != null) {
				throw new ParseException("resultElement is null while end date is notnull for game " + game.getGameId(), 0);
			}
			game.setEndDate(null); // game in progress
			game.setWinner(null);
		} else if (resultElement.text().contains("won")) {
			String winner = resultElement.text().substring(0, resultElement.text().indexOf(" won")).trim();
			Player winningPlayer = Player.findPlayersByUsernameEquals(winner).getSingleResult();
			game.setWinner(winningPlayer);
			if (resultElement.text().contains("on time")) {
				game.setWonOnTime(true);
			} else {
				game.setWonOnTime(false);
			}
			if (game.getEndDate() == null) {
				game.setEndDate(Calendar.getInstance().getTime());
			}
		} else if (resultElement.text().contains("drawn")) {
			game.setWinner(null);
			if (game.getEndDate() == null) {
				game.setEndDate(Calendar.getInstance().getTime());
			}
		} else {
			throw new ParseException("Unrecognized result <" + resultElement.text() + "> for game " + game.getGameId(), 0);
		}
	}

}
