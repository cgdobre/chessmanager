// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ro.progsquad.chessmanager.model.Game;

privileged aspect Game_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Game.entityManager;
    
    public static final List<String> Game.fieldNames4OrderClauseFilter = java.util.Arrays.asList("gameId", "white", "black", "winner", "wonOnTime", "timePerMove", "numberOfMoves", "startDate", "endDate", "teamMatch");
    
    public static final EntityManager Game.entityManager() {
        EntityManager em = new Game().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Game.countGames() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Game o", Long.class).getSingleResult();
    }
    
    public static List<Game> Game.findAllGames() {
        return entityManager().createQuery("SELECT o FROM Game o", Game.class).getResultList();
    }
    
    public static List<Game> Game.findAllGames(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Game o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Game.class).getResultList();
    }
    
    public static Game Game.findGame(Long id) {
        if (id == null) return null;
        return entityManager().find(Game.class, id);
    }
    
    public static List<Game> Game.findGameEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Game o", Game.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Game> Game.findGameEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Game o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Game.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Game.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Game.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Game attached = Game.findGame(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Game.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Game.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Game Game.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Game merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}