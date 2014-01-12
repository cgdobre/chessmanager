package ro.progsquad.chessmanager.model;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findTeamsByTeamNameEquals", "findTeamsByMembers", "findTeamsByMembersAndTeamNameEquals" })
public class Team {

    @NotNull
    @Column(unique = true)
    private Long teamId;

    @NotNull
    @Column(unique = true)
    private String teamName;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "teams")
    private Set<Player> members = new HashSet<Player>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "challengerTeam")
    private Set<TeamMatch> challengerTeamMatches = new HashSet<TeamMatch>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responderTeam")
    private Set<TeamMatch> responderTeamMatches = new HashSet<TeamMatch>();

    public final Set<TeamMatch> getTeamMatches() {
        Set<TeamMatch> teamMatches = new HashSet<TeamMatch>();
        teamMatches.addAll(getChallengerTeamMatches());
        teamMatches.addAll(getResponderTeamMatches());
        return Collections.unmodifiableSet(teamMatches);
    }

    public String toString() {
        Team team = new Team();
        team.setId(getId());
        team.setVersion(getVersion());
        team.setTeamId(getTeamId());
        team.setTeamName(getTeamName());
        Set<Player> members = new HashSet<Player>();
        for (Player player : getMembers()) {
            Player playerStub = new Player();
            playerStub.setId(player.getId());
            members.add(playerStub);
        }
        team.setMembers(members);
        Set<TeamMatch> matches = new HashSet<TeamMatch>();
        for (TeamMatch match : getChallengerTeamMatches()) {
            TeamMatch matchStub = new TeamMatch();
            matchStub.setId(match.getId());
            matches.add(matchStub);
        }
        team.setChallengerTeamMatches(matches);
        matches = new HashSet<TeamMatch>();
        for (TeamMatch match : getResponderTeamMatches()) {
            TeamMatch matchStub = new TeamMatch();
            matchStub.setId(match.getId());
            matches.add(matchStub);
        }
        team.setResponderTeamMatches(matches);
        return ReflectionToStringBuilder.toString(team, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
