package ro.progsquad.chessmanager.ui;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.progsquad.chessmanager.model.Game;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/games")
@Controller
@RooWebScaffold(path = "games", formBackingObject = Game.class)
@RooWebFinder
public class GameController {
}
