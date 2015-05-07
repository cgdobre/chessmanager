/**
 * 
 */
package ro.progsquad.chessmanager.views;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

import ro.progsquad.chessmanager.ChessManager;
import ro.progsquad.chessmanager.dao.GoogleSheetDAO;
import ro.progsquad.chessmanager.model.Game;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;

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
	public static final String MONTHLY_SCORE_KEY = "monthlyscore";
	public static final String TIMEOUT_COUNT_KEY = "timeouts";
	public static final String LAST_UPDATE_KEY = "lastupdate";
	public static final String LAST_SCORE = "lastscore";
	public static final String ONLINE_CHESS_STATS = "onlinechessstats";
	public static final String CURRENT_GAMES_NO = "CurrentGamesNo";
	
	public static Map<String, String> asRankingMap(Player player, Team team) throws NumberFormatException, IOException, ServiceException {
		System.out.println("Calculate rankings for user " + player.getUsername() + " in team " + team.getTeamName());
		
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put(USERNAME_KEY, player.getUsername());
		attributesMap.put(ONLINE_RATING_KEY, player.getOnlineRating() + "");
		
		Set<Game> filteredGames = new HashSet<Game>();
		for (Game game : player.getGames()) {
			if (game.getTeamMatch() == null || 
					(!game.getTeamMatch().getChallengerTeam().getTeamId().equals(team.getTeamId()) 
							&& !game.getTeamMatch().getResponderTeam().getTeamId().equals(team.getTeamId()))) {
				continue;
			}
			filteredGames.add(game);
		}
		attributesMap.put(GAME_COUNT_KEY, filteredGames.size() + "");
		
		int gamesInProgress = 0;
		int victories = 0;
		int draws = 0;
		int losses = 0;
		int timeouts = 0;
		for (Game game : filteredGames) {
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
		
		List<ListEntry> rankings = GoogleSheetDAO.getInstance(ChessManager.CHESS_MANAGER_WORKBOOK_NAME, Rankings.SHEET_NAME + "-" + team.getTeamName())
				.query(Rankings.USERNAME_KEY + "=\"" + player.getUsername() + "\"");
		double lastScore = rankings.isEmpty() || StringUtils.isEmpty(rankings.get(0).getCustomElements().getValue(LAST_SCORE)) ? 0 
				: Double.parseDouble(rankings.get(0).getCustomElements().getValue(LAST_SCORE).replace(',', '.'));
		double currentScore = (victories + draws / 2) + (draws % 2 == 1 ? 0.5 : 0);
		
		attributesMap.put(IN_PROGRESS_KEY, gamesInProgress + "");
		attributesMap.put(VICTORIES_KEY, victories + "");
		attributesMap.put(DRAWS_KEY, draws + "");
		attributesMap.put(LOSSES_KEY, losses + "");
		attributesMap.put(TIMEOUT_COUNT_KEY, timeouts + "");
		attributesMap.put(SCORE_KEY, (victories + draws / 2) + "" + (draws % 2 == 1 ? ",5" : ""));
		attributesMap.put(MONTHLY_SCORE_KEY, ((currentScore - lastScore) + "").replace('.', ','));
		attributesMap.put(LAST_UPDATE_KEY, new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime()));
		attributesMap.put(ONLINE_CHESS_STATS, player.getTotalGames());
		attributesMap.put(CURRENT_GAMES_NO, player.getCurrentGamesNo());
		
		return attributesMap;
	}

}
