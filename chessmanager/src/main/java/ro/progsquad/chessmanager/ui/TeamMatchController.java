package ro.progsquad.chessmanager.ui;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.progsquad.chessmanager.model.TeamMatch;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/teammatches")
@Controller
@RooWebScaffold(path = "teammatches", formBackingObject = TeamMatch.class)
@RooWebFinder
public class TeamMatchController {
}
