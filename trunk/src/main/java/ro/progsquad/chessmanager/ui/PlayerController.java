package ro.progsquad.chessmanager.ui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ro.progsquad.chessmanager.dao.GoogleSheetDAO;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;
import ro.progsquad.chessmanager.views.Rankings;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

@RequestMapping("/players")
@Controller
@RooWebScaffold(path = "players", formBackingObject = Player.class)
@RooWebFinder
public class PlayerController {
	
	@RequestMapping(params = { "find=computeRankingsByTeam", "form" }, method = RequestMethod.GET)
    public String computeRankingsByTeamForm(Model uiModel) {
        uiModel.addAttribute("teams", Team.findAllTeams());
        return "players/computeRankingsByTeam";
    }
    
    @RequestMapping(params = "find=computeRankingsByTeam", method = RequestMethod.GET)
    public String computeRankingsByTeam(@RequestParam("teams") Set<Team> teams, Model uiModel) {
    	
    	Team team = teams.iterator().next();
    	
    	System.out.println("Connecting to google and writing rankings");
		GoogleSheetDAO dao;
		List<String> rankedUserNames = new ArrayList<String>();
		try {
			dao = GoogleSheetDAO.getInstance("ChessManager", Rankings.SHEET_NAME);
		
			for (Player player : team.getMembers()) {
				List<ListEntry> rankingEntries = dao.query(Rankings.USERNAME_KEY + "=\"" + player.getUsername() + "\"");
				if (rankingEntries.isEmpty()) {
					dao.addNewEntry(Rankings.asRankingMap(player));
				} else {
					dao.update(rankingEntries.get(0), Rankings.asRankingMap(player));
				}
				rankedUserNames.add(player.getUsername());
			}
			
			// delete rankings of players that are no longer members of the team
			List<ListEntry> rankings = dao.getRows();
			for (ListEntry ranking : rankings) {
				String userName = ranking.getCustomElements().getValue(Rankings.USERNAME_KEY);
				if (!StringUtils.isEmpty(userName) && !rankedUserNames.contains(userName)) {
					System.out.println("Player " + userName + " no longer a member of the team. Deleting ranking.");
					ranking.delete();
				}
			}
		} catch (IOException | ServiceException e) {
			System.err.println("Error calculating rankings for team " + team.getTeamName() + ":\n" + e);e.printStackTrace();
			return "players/list";
		}
		
		System.out.println("Finished calculating rankings for team " + team.getTeamName());
        uiModel.addAttribute("players", Player.findPlayersByTeams(teams).getResultList());
        return "players/list";
    }
}
