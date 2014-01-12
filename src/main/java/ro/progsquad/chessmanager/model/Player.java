package ro.progsquad.chessmanager.model;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPlayersByUsernameEquals", "findPlayersByTeams" })
public class Player {

    @NotNull
    @Column(unique = true)
    private String username;

    @Min(0L)
    private Integer onlineRating = 0;

    @Max(100L)
    @Min(0L)
    private Integer timeout = 0;

    @NotNull
    private String totalGames = "0";

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date memberSince;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd_hh:mm:ss")
    private Date lastUpdate = Calendar.getInstance().getTime();

    @NotNull
    private String country = "International";

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Team> teams = new HashSet<Team>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "white")
    private Set<Game> whiteGames = new HashSet<Game>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "black")
    private Set<Game> blackGames = new HashSet<Game>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "winner")
    private Set<Game> wonGames = new HashSet<Game>();

    public final Set<Game> getGames() {
        Set<Game> games = new HashSet<Game>();
        games.addAll(blackGames);
        games.addAll(whiteGames);
        return Collections.unmodifiableSet(games);
    }

    @Min(0L)
    private Integer groupCount = 0;

    @NotNull
    private Boolean isDisabled = false;
    
    public String toString() {
    	Player player = new Player();
    	player.setId(this.getId());
    	player.setVersion(this.getVersion());
    	player.setCountry(this.getCountry());
    	player.setGroupCount(this.getGroupCount());
    	player.setIsDisabled(getIsDisabled());
    	player.setLastUpdate(getLastUpdate());
    	player.setMemberSince(getMemberSince());
    	player.setOnlineRating(getOnlineRating());
    	player.setTimeout(getTimeout());
    	player.setTotalGames(getTotalGames());
    	player.setUsername(getUsername());
    	
    	Set<Game> games = new HashSet<Game>();
    	for (Game game : getBlackGames()) {
    		Game gameStub = new Game();
    		gameStub.setId(game.getId());
    		games.add(gameStub);
    	}
    	player.setBlackGames(games);
    	
    	games = new HashSet<Game>();
    	for (Game game : getWhiteGames()) {
    		Game gameStub = new Game();
    		gameStub.setId(game.getId());
    		games.add(gameStub);
    	}
    	player.setWhiteGames(games);
    	
    	games = new HashSet<Game>();
    	for (Game game : getWonGames()) {
    		Game gameStub = new Game();
    		gameStub.setId(game.getId());
    		games.add(gameStub);
    	}
    	player.setWonGames(games);
    	
    	Set<Team> teams = new HashSet<Team>();
    	for (Team team : getTeams()) {
    		Team teamStub = new Team();
    		teamStub.setId(team.getId());
    		teams.add(teamStub);
    	}
    	player.setTeams(teams);
    	
        return ReflectionToStringBuilder.toString(player, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
