package ro.progsquad.chessmanager.model;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findTeamMatchesByTeamMatchIdEquals" })
public class TeamMatch {

    @NotNull
    @Column(unique = true)
    private Long teamMatchId;

    @NotNull
    private String teamMatchName;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date endDate;

    private String gameType;

    private String timePerMove;

    private String ratingRange;

    private String startingPosition;

    private Boolean isRated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamMatch")
    private Set<Game> games = new HashSet<Game>();

    @NotNull
    @ManyToOne
    private Team challengerTeam;

    @NotNull
    @ManyToOne
    private Team responderTeam;

    @NotNull
    @Min(1L)
    @Max(2L)
    private Integer simultaneousGames;

	public final Set<Player> getPlayers() {
		Set<Player> players = new HashSet<Player>();
		for (Game game : getGames()) {
			if (!players.contains(game.getWhite())) {
				players.add(game.getWhite());
			}
			if (!players.contains(game.getBlack())) {
				players.add(game.getBlack());
			}
		}
		return Collections.unmodifiableSet(players);
	}
}
