// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.model;

import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.Team;

privileged aspect Team_Roo_Finder {
    
    public static Long Team.countFindTeamsByMembers(Set<Player> members) {
        if (members == null) throw new IllegalArgumentException("The members argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Team AS o WHERE");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :members_item").append(i).append(" MEMBER OF o.members");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int membersIndex = 0;
        for (Player _player: members) {
            q.setParameter("members_item" + membersIndex++, _player);
        }
        return ((Long) q.getSingleResult());
    }
    
    public static Long Team.countFindTeamsByMembersAndTeamNameEquals(Set<Player> members, String teamName) {
        if (members == null) throw new IllegalArgumentException("The members argument is required");
        if (teamName == null || teamName.length() == 0) throw new IllegalArgumentException("The teamName argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM Team AS o WHERE o.teamName = :teamName");
        queryBuilder.append(" AND");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :members_item").append(i).append(" MEMBER OF o.members");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        int membersIndex = 0;
        for (Player _player: members) {
            q.setParameter("members_item" + membersIndex++, _player);
        }
        q.setParameter("teamName", teamName);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Team.countFindTeamsByTeamIdEquals(Long teamId) {
        if (teamId == null) throw new IllegalArgumentException("The teamId argument is required");
        EntityManager em = Team.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Team AS o WHERE o.teamId = :teamId", Long.class);
        q.setParameter("teamId", teamId);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Team.countFindTeamsByTeamNameEquals(String teamName) {
        if (teamName == null || teamName.length() == 0) throw new IllegalArgumentException("The teamName argument is required");
        EntityManager em = Team.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Team AS o WHERE o.teamName = :teamName", Long.class);
        q.setParameter("teamName", teamName);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Team> Team.findTeamsByMembers(Set<Player> members) {
        if (members == null) throw new IllegalArgumentException("The members argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Team AS o WHERE");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :members_item").append(i).append(" MEMBER OF o.members");
        }
        TypedQuery<Team> q = em.createQuery(queryBuilder.toString(), Team.class);
        int membersIndex = 0;
        for (Player _player: members) {
            q.setParameter("members_item" + membersIndex++, _player);
        }
        return q;
    }
    
    public static TypedQuery<Team> Team.findTeamsByMembersAndTeamNameEquals(Set<Player> members, String teamName) {
        if (members == null) throw new IllegalArgumentException("The members argument is required");
        if (teamName == null || teamName.length() == 0) throw new IllegalArgumentException("The teamName argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Team AS o WHERE o.teamName = :teamName");
        queryBuilder.append(" AND");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :members_item").append(i).append(" MEMBER OF o.members");
        }
        TypedQuery<Team> q = em.createQuery(queryBuilder.toString(), Team.class);
        int membersIndex = 0;
        for (Player _player: members) {
            q.setParameter("members_item" + membersIndex++, _player);
        }
        q.setParameter("teamName", teamName);
        return q;
    }
    
    public static TypedQuery<Team> Team.findTeamsByTeamIdEquals(Long teamId) {
        if (teamId == null) throw new IllegalArgumentException("The teamId argument is required");
        EntityManager em = Team.entityManager();
        TypedQuery<Team> q = em.createQuery("SELECT o FROM Team AS o WHERE o.teamId = :teamId", Team.class);
        q.setParameter("teamId", teamId);
        return q;
    }
    
    public static TypedQuery<Team> Team.findTeamsByTeamIdEquals(Long teamId, String sortFieldName, String sortOrder) {
        if (teamId == null) throw new IllegalArgumentException("The teamId argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Team AS o WHERE o.teamId = :teamId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Team> q = em.createQuery(queryBuilder.toString(), Team.class);
        q.setParameter("teamId", teamId);
        return q;
    }
    
    public static TypedQuery<Team> Team.findTeamsByTeamNameEquals(String teamName) {
        if (teamName == null || teamName.length() == 0) throw new IllegalArgumentException("The teamName argument is required");
        EntityManager em = Team.entityManager();
        TypedQuery<Team> q = em.createQuery("SELECT o FROM Team AS o WHERE o.teamName = :teamName", Team.class);
        q.setParameter("teamName", teamName);
        return q;
    }
    
    public static TypedQuery<Team> Team.findTeamsByTeamNameEquals(String teamName, String sortFieldName, String sortOrder) {
        if (teamName == null || teamName.length() == 0) throw new IllegalArgumentException("The teamName argument is required");
        EntityManager em = Team.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Team AS o WHERE o.teamName = :teamName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Team> q = em.createQuery(queryBuilder.toString(), Team.class);
        q.setParameter("teamName", teamName);
        return q;
    }
    
}
