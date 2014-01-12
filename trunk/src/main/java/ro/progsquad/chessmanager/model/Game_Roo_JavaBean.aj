// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.model;

import java.util.Date;
import ro.progsquad.chessmanager.model.Game;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.TeamMatch;

privileged aspect Game_Roo_JavaBean {
    
    public Long Game.getGameId() {
        return this.gameId;
    }
    
    public void Game.setGameId(Long gameId) {
        this.gameId = gameId;
    }
    
    public Player Game.getWhite() {
        return this.white;
    }
    
    public void Game.setWhite(Player white) {
        this.white = white;
    }
    
    public Player Game.getBlack() {
        return this.black;
    }
    
    public void Game.setBlack(Player black) {
        this.black = black;
    }
    
    public Player Game.getWinner() {
        return this.winner;
    }
    
    public void Game.setWinner(Player winner) {
        this.winner = winner;
    }
    
    public Boolean Game.getWonOnTime() {
        return this.wonOnTime;
    }
    
    public void Game.setWonOnTime(Boolean wonOnTime) {
        this.wonOnTime = wonOnTime;
    }
    
    public String Game.getTimePerMove() {
        return this.timePerMove;
    }
    
    public void Game.setTimePerMove(String timePerMove) {
        this.timePerMove = timePerMove;
    }
    
    public Integer Game.getNumberOfMoves() {
        return this.numberOfMoves;
    }
    
    public void Game.setNumberOfMoves(Integer numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }
    
    public Date Game.getStartDate() {
        return this.startDate;
    }
    
    public void Game.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date Game.getEndDate() {
        return this.endDate;
    }
    
    public void Game.setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public TeamMatch Game.getTeamMatch() {
        return this.teamMatch;
    }
    
    public void Game.setTeamMatch(TeamMatch teamMatch) {
        this.teamMatch = teamMatch;
    }
    
}
