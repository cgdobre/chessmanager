/**
 * 
 */
package ro.progsquad.chessmanager.factories;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import ro.progsquad.chessmanager.dao.HtmlDAO;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;
import ro.progsquad.chessmanager.model.TeamMatch;

/**
 * @author cgdobre
 *
 */
public class TeamFactory {
	
	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
	private static final String MIN_START_DATE = "Nov 1, 2013";
	
	public static final String GROUP_MEMBERS_BASE_URL = "/groups/membersearch?";
	public static final String GROUP_MEMBERS_URL = GROUP_MEMBERS_BASE_URL + "allnew=1&id=";
	public static final String GROUP_MATCHES_URL = "/groups/matches/";
	public static final String GROUP_MATCH_ARCHIVE_URL = "/groups/team_match_archive?id=";
	public static final String GROUP_URL = "/groups/home/";
	
	@Transactional
	public static Team build(String teamName, boolean includeTeamMatchesAndPlayers) throws IOException, ParseException {
		Team team;
		try {
			team = Team.findTeamsByTeamNameEquals(teamName).getSingleResult();
		} catch (EmptyResultDataAccessException e) {
			// connect
			String teamMatchesUrl = HtmlDAO.BASE_URL + GROUP_MATCHES_URL + teamName + "?show_all_current=1";
			Element page = HtmlDAO.getBody(teamMatchesUrl);
			
			// build team
			team = new Team();
			team.setTeamName(teamName);
			Long teamId = HtmlDAO.parseIdFromAnchorElement(page.select(" a[href^=" + GROUP_MEMBERS_BASE_URL + "]").first());
			team.setTeamId(teamId);
			team.persist();
		}
		
		if (includeTeamMatchesAndPlayers) {
			System.out.println("Loading players and matches for team " + team.getTeamName() + " with id " + team.getTeamId());
			loadPlayers(team);
			loadTeamMatches(team);
		}
		
		return team;
	}

	@Transactional
	private static void loadPlayers(Team team) throws IOException, ParseException {
		String pageUrl;
		String nextPageUrl = HtmlDAO.BASE_URL + GROUP_MEMBERS_URL + team.getTeamId();
		Set<String> parsedUserNames = new HashSet<String>();
		do {
			pageUrl = nextPageUrl;
			Element page = HtmlDAO.getBody(pageUrl)
								  .getElementsByAttributeValue("class", "content left").first();
			Elements playerElements = page.select("a[href^=" + HtmlDAO.BASE_URL + PlayerFactory.MEMBER_URL + "]");
			
			Iterator<Element> playerIterator = playerElements.iterator();
			while (playerIterator.hasNext()) {
				String url = playerIterator.next().attr("href");
				String username = url.substring(url.lastIndexOf("/") + 1);
				
				if (parsedUserNames.contains(username)) {
					continue;
				}
				parsedUserNames.add(username);
				
				Player player = PlayerFactory.build(username);
				
				if (!player.getIsDisabled()) {
					if (!player.getTeams().contains(team)) {
						player.getTeams().add(team);
						player.merge();
					}
					
					System.out.println("Team Player: " + player.getUsername() 
										+ ", OR: " + player.getOnlineRating() 
										+ " TO: " + player.getTimeout()
										+ " #G: " + player.getTotalGames());
				}
			}
			
			nextPageUrl = HtmlDAO.parseNextPageURL(page);

		} while (nextPageUrl != null && !nextPageUrl.equals(pageUrl));
		
		// remove players that are no longer part of the group
		Set<Team> teams = new HashSet<Team>();
		teams.add(team);
		for (Player player : Player.findPlayersByTeams(teams).getResultList()) {
			if (!parsedUserNames.contains(player.getUsername())) {
				Set<Player> players = new HashSet<Player>();
				players.add(player);
				Set<Team> playerTeams = new HashSet<Team>();
				playerTeams.addAll(Team.findTeamsByMembers(players).getResultList());
				playerTeams.remove(team);
				player.setTeams(playerTeams);
				player.merge();
				System.out.println("Player removed from team: " + player.getUsername());
			}
		}
	}

	@Transactional
	public static void loadTeamMatches(Team team) throws IOException, ParseException {
		Date minStartDate = dateFormat.parse(MIN_START_DATE);
		Element page;
		Elements teamMatchElements;
		Iterator<Element> teamMatchElementsIterator;
		
		// parse matches in progress
		page = HtmlDAO.getBody(HtmlDAO.BASE_URL + GROUP_MATCHES_URL + team.getTeamName() + "?show_all_current=1");
		teamMatchElements = page.select("table#c3 a[href^=" + TeamMatchFactory.TEAM_MATCH_URL + "]");
		teamMatchElementsIterator = teamMatchElements.iterator();
		
		while (teamMatchElementsIterator.hasNext()) {
			Element teamMatchElement = teamMatchElementsIterator.next();
			Long teamMatchId = HtmlDAO.parseIdFromAnchorElement(teamMatchElement);
			TeamMatch teamMatch = TeamMatchFactory.build(teamMatchId);
			System.out.println("Team Match in progress: " + teamMatch.getTeamMatchName() 
								+ " Opponent: " + (team.getTeamName().equals(teamMatch.getChallengerTeam().getTeamName()) ? teamMatch.getResponderTeam().getTeamName() : teamMatch.getChallengerTeam().getTeamName())
								+ " Started: " + dateFormat.format(teamMatch.getStartDate()));
		}
		
		// parse match archive
		Set<TeamMatch> existingTeamMatches = team.getTeamMatches();
		String pageUrl;
		String nextPageUrl = HtmlDAO.BASE_URL + GROUP_MATCH_ARCHIVE_URL + team.getTeamId();
		boolean finished = false;
		do {
			pageUrl = nextPageUrl;
			
			page = HtmlDAO.getBody(pageUrl);
			teamMatchElements = page.select("table a[href^=" + TeamMatchFactory.TEAM_MATCH_URL + "]");
			teamMatchElementsIterator = teamMatchElements.iterator();
			
			while (teamMatchElementsIterator.hasNext()) {
				Element teamMatchElement = teamMatchElementsIterator.next();
				Long teamMatchId = HtmlDAO.parseIdFromAnchorElement(teamMatchElement);
				TeamMatch teamMatch = TeamMatchFactory.build(teamMatchId);
				
				if (teamMatch.getEndDate() != null && // allow update of team matches where parse error encountered
						(existingTeamMatches.contains(teamMatch) || minStartDate.after(teamMatch.getStartDate()))) {
					System.out.println("Stopping at team match " + teamMatch.getTeamMatchName() 
										+ " id " + teamMatch.getTeamMatchId()
										+ " Started: " + dateFormat.format(teamMatch.getStartDate()));
					finished = true;
					break;
				} else {
					System.out.println("Team Match finished: " + teamMatch.getTeamMatchName() 
							+ " Opponent: " + (team.getTeamName().equals(teamMatch.getChallengerTeam().getTeamName()) ? teamMatch.getResponderTeam().getTeamName() : teamMatch.getChallengerTeam().getTeamName())
							+ " Started: " + dateFormat.format(teamMatch.getStartDate())
							+ " Ended: " + dateFormat.format(teamMatch.getEndDate()));
				}
			}
			
			if (!finished) {
				nextPageUrl = HtmlDAO.parseNextPageURL(page);
			}
			
		} while (!finished && nextPageUrl != null && !nextPageUrl.equals(pageUrl));
	}
	
}
