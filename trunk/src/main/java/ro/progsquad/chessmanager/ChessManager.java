/**
 * 
 */
package ro.progsquad.chessmanager;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import ro.progsquad.chessmanager.dao.GoogleSheetDAO;
import ro.progsquad.chessmanager.factories.TeamFactory;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;
import ro.progsquad.chessmanager.views.Rankings;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

/**
 * @author cgdobre
 *
 */
@Transactional
public class ChessManager {
	
	public static final String CHESS_MANAGER_WORKBOOK_NAME = "ChessManager";
	private static final String TEAM_PARAMETER = "-team";

	public ChessManager() {}

	// For console mode. ORM binding not functioning properly though
	public static void main(String[] args) throws IOException, ServiceException, ParseException {
		// initialize Spring framework
		ChessManager chessManager = new ChessManager();
		@SuppressWarnings("resource")
		ConfigurableApplicationContext applicationContext = 
                new ClassPathXmlApplicationContext("classpath*:META-INF/spring/applicationContext*.xml");
        applicationContext.registerShutdownHook();
        applicationContext.getBeanFactory().autowireBeanProperties(
        		chessManager, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        
		// parse args
		if (!ArrayUtils.contains(args, TEAM_PARAMETER)) {
			System.err.println("No team name specified. Use -team parameter, e.g. -team chess-dojo");
			System.exit(1);
		}
		int teamParameterIndex = ArrayUtils.indexOf(args, TEAM_PARAMETER);
		if (args.length <= teamParameterIndex + 1) {
			System.err.println("No team name specified. Use -team parameter, e.g. -team chess-dojo");
			System.exit(1);
		}
		String teamName = args[teamParameterIndex + 1];
		
		// compute rankings
		Team team = TeamFactory.build(teamName, true); // include team matches
		chessManager.computeRankings(team);
		
		System.out.println("DONE");
	}

	@Transactional
	private void computeRankings(Team team) throws IOException, ServiceException {
		System.out.println("Connecting to google and writing rankings");
		GoogleSheetDAO dao = GoogleSheetDAO.getInstance(CHESS_MANAGER_WORKBOOK_NAME, Rankings.SHEET_NAME);
		for (Player player : team.getMembers()) {
			List<ListEntry> rankingEntries = dao.query(Rankings.USERNAME_KEY + "=\"" + player.getUsername() + "\"");
			if (rankingEntries.isEmpty()) {
				dao.addNewEntry(Rankings.asRankingMap(player));
			} else {
				dao.update(rankingEntries.get(0), Rankings.asRankingMap(player));
			}
		}
	}

}
