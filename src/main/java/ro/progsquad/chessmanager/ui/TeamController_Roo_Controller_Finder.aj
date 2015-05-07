// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.ui;

import java.util.Set;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;
import ro.progsquad.chessmanager.ui.TeamController;

privileged aspect TeamController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByMembers", "form" }, method = RequestMethod.GET)
    public String TeamController.findTeamsByMembersForm(Model uiModel) {
        uiModel.addAttribute("players", Player.findAllPlayers());
        return "teams/findTeamsByMembers";
    }
    
    @RequestMapping(params = "find=ByMembers", method = RequestMethod.GET)
    public String TeamController.findTeamsByMembers(@RequestParam("members") Set<Player> members, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("teams", Team.findTeamsByMembers(members, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Team.countFindTeamsByMembers(members) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("teams", Team.findTeamsByMembers(members, sortFieldName, sortOrder).getResultList());
        }
        return "teams/list";
    }
    
    @RequestMapping(params = { "find=ByMembersAndTeamNameEquals", "form" }, method = RequestMethod.GET)
    public String TeamController.findTeamsByMembersAndTeamNameEqualsForm(Model uiModel) {
        uiModel.addAttribute("players", Player.findAllPlayers());
        return "teams/findTeamsByMembersAndTeamNameEquals";
    }
    
    @RequestMapping(params = "find=ByMembersAndTeamNameEquals", method = RequestMethod.GET)
    public String TeamController.findTeamsByMembersAndTeamNameEquals(@RequestParam("members") Set<Player> members, @RequestParam("teamName") String teamName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("teams", Team.findTeamsByMembersAndTeamNameEquals(members, teamName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Team.countFindTeamsByMembersAndTeamNameEquals(members, teamName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("teams", Team.findTeamsByMembersAndTeamNameEquals(members, teamName, sortFieldName, sortOrder).getResultList());
        }
        return "teams/list";
    }
    
    @RequestMapping(params = { "find=ByTeamIdEquals", "form" }, method = RequestMethod.GET)
    public String TeamController.findTeamsByTeamIdEqualsForm(Model uiModel) {
        return "teams/findTeamsByTeamIdEquals";
    }
    
    @RequestMapping(params = "find=ByTeamIdEquals", method = RequestMethod.GET)
    public String TeamController.findTeamsByTeamIdEquals(@RequestParam("teamId") Long teamId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("teams", Team.findTeamsByTeamIdEquals(teamId, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Team.countFindTeamsByTeamIdEquals(teamId) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("teams", Team.findTeamsByTeamIdEquals(teamId, sortFieldName, sortOrder).getResultList());
        }
        return "teams/list";
    }
    
    @RequestMapping(params = { "find=ByTeamNameEquals", "form" }, method = RequestMethod.GET)
    public String TeamController.findTeamsByTeamNameEqualsForm(Model uiModel) {
        return "teams/findTeamsByTeamNameEquals";
    }
    
    @RequestMapping(params = "find=ByTeamNameEquals", method = RequestMethod.GET)
    public String TeamController.findTeamsByTeamNameEquals(@RequestParam("teamName") String teamName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("teams", Team.findTeamsByTeamNameEquals(teamName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Team.countFindTeamsByTeamNameEquals(teamName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("teams", Team.findTeamsByTeamNameEquals(teamName, sortFieldName, sortOrder).getResultList());
        }
        return "teams/list";
    }
    
}