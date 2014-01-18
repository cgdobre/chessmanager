package ro.progsquad.chessmanager.ui;
import java.io.IOException;
import java.text.ParseException;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ro.progsquad.chessmanager.factories.TeamFactory;
import ro.progsquad.chessmanager.model.Team;

@RequestMapping("/teams")
@Controller
@RooWebScaffold(path = "teams", formBackingObject = Team.class)
@RooWebFinder
public class TeamController {
	
	@RequestMapping(params = { "find=createByTeamName", "form" }, method = RequestMethod.GET)
    public String createByTeamNameForm(Model uiModel) {
        return "teams/createByTeamName";
    }
    
    @RequestMapping(params = "find=createByTeamName", method = RequestMethod.GET)
    public String createByTeamName(@RequestParam("teamName") String teamName, Model uiModel) {
    	Team team;
		try {
			team = TeamFactory.build(teamName, true);
		} catch (IOException | ParseException e) {
			System.err.println("Error creating team by name " + teamName + ":\n" + e);e.printStackTrace();
			return "teams/list";
		}
        System.out.println("Finished creating team by name " + teamName);
    	return "redirect:/teams/" + team.getId();
    }
}
