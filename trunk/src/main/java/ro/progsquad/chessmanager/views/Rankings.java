/**
 * 
 */
package ro.progsquad.chessmanager.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ro.progsquad.chessmanager.model.Game;
import ro.progsquad.chessmanager.model.Player;

/**
 * @author cgdobre
 *
 */
public class Rankings {

	public static final String SHEET_NAME = "Rankings";
	public static final String USERNAME_KEY = "username";
	public static final String ONLINE_RATING_KEY = "onlinerating";
	public static final String VICTORIES_KEY = "victories";
	public static final String GAME_COUNT_KEY = "gamecount";
	public static final String IN_PROGRESS_KEY = "inprogress";
	public static final String DRAWS_KEY = "draws";
	public static final String LOSSES_KEY = "losses";
	public static final String SCORE_KEY = "score";
	public static final String TIMEOUT_COUNT_KEY = "timeouts";
	public static final String LAST_UPDATE_KEY = "lastupdate";
	
	public static Map<String, String> asRankingMap(Player player) {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put(USERNAME_KEY, player.getUsername());
		attributesMap.put(ONLINE_RATING_KEY, player.getOnlineRating() + "");
		attributesMap.put(GAME_COUNT_KEY, player.getGames().size() + "");
		
		int gamesInProgress = 0;
		int victories = 0;
		int draws = 0;
		int losses = 0;
		int timeouts = 0;
		for (Game game : player.getGames()) {
			if (game.getEndDate() == null) {
				gamesInProgress++;
				continue;
			}
			if (game.getWinner() == null) {
				draws++;
			} else if (player.equals(game.getWinner())) {
				victories++;
			} else {
				losses++;
				if (game.getWonOnTime()) {
					timeouts++;
				}
			}
		}
		
		attributesMap.put(IN_PROGRESS_KEY, gamesInProgress + "");
		attributesMap.put(VICTORIES_KEY, victories + "");
		attributesMap.put(DRAWS_KEY, draws + "");
		attributesMap.put(LOSSES_KEY, losses + "");
		attributesMap.put(SCORE_KEY, (victories + draws / 2) + "" + (draws % 2 == 1 ? ",5" : ""));
		attributesMap.put(TIMEOUT_COUNT_KEY, timeouts + "");
		attributesMap.put(LAST_UPDATE_KEY, new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		return attributesMap;
	}

}
