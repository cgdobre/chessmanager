package ro.progsquad.chessmanager.ui;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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
	
	@RequestMapping(params = { "find=RankingsByTeam", "form" }, method = RequestMethod.GET)
    public String computeRankingsByTeamForm(Model uiModel) {
        uiModel.addAttribute("teams", Team.findAllTeams());
        return "players/findPlayersByTeams";
    }
    
    @RequestMapping(params = "find=RankingsByTeam", method = RequestMethod.GET)
    public String computeRankingsByTeam(@RequestParam("teams") Set<Team> teams, Model uiModel) {
    	
    	Team team = teams.iterator().next();
    	
    	System.out.println("Connecting to google and writing rankings");
		GoogleSheetDAO dao;
		try {
			dao = GoogleSheetDAO.getInstance("ChessManager", Rankings.SHEET_NAME);
		
			for (Player player : team.getMembers()) {
				List<ListEntry> rankingEntries = dao.query(Rankings.USERNAME_KEY + "=\"" + player.getUsername() + "\"");
				if (rankingEntries.isEmpty()) {
					dao.addNewEntry(Rankings.asRankingMap(player));
				} else {
					dao.update(rankingEntries.get(0), Rankings.asRankingMap(player));
				}
			}
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
			return "players/list";
		}
		
        uiModel.addAttribute("players", Player.findPlayersByTeams(teams).getResultList());
        return "players/list";
    }
}
