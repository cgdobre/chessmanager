// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import ro.progsquad.chessmanager.model.Player;
import ro.progsquad.chessmanager.model.PlayerDataOnDemand;

privileged aspect PlayerDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PlayerDataOnDemand: @Component;
    
    private Random PlayerDataOnDemand.rnd = new SecureRandom();
    
    private List<Player> PlayerDataOnDemand.data;
    
    public Player PlayerDataOnDemand.getNewTransientPlayer(int index) {
        Player obj = new Player();
        setCountry(obj, index);
        setCurrentGamesNo(obj, index);
        setGroupCount(obj, index);
        setIsDisabled(obj, index);
        setLastUpdate(obj, index);
        setMemberSince(obj, index);
        setOnlineRating(obj, index);
        setTimeout(obj, index);
        setTotalGames(obj, index);
        setUsername(obj, index);
        return obj;
    }
    
    public void PlayerDataOnDemand.setCountry(Player obj, int index) {
        String country = "International_" + index;
        obj.setCountry(country);
    }
    
    public void PlayerDataOnDemand.setCurrentGamesNo(Player obj, int index) {
        String currentGamesNo = "currentGamesNo_" + index;
        obj.setCurrentGamesNo(currentGamesNo);
    }
    
    public void PlayerDataOnDemand.setGroupCount(Player obj, int index) {
        Integer groupCount = 0;
        if (groupCount < 0) {
            groupCount = 0;
        }
        obj.setGroupCount(groupCount);
    }
    
    public void PlayerDataOnDemand.setIsDisabled(Player obj, int index) {
        Boolean isDisabled = false;
        obj.setIsDisabled(isDisabled);
    }
    
    public void PlayerDataOnDemand.setLastUpdate(Player obj, int index) {
        Date lastUpdate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setLastUpdate(lastUpdate);
    }
    
    public void PlayerDataOnDemand.setMemberSince(Player obj, int index) {
        Date memberSince = new Date(new Date().getTime() - 10000000L);
        obj.setMemberSince(memberSince);
    }
    
    public void PlayerDataOnDemand.setOnlineRating(Player obj, int index) {
        Integer onlineRating = 0;
        if (onlineRating < 0) {
            onlineRating = 0;
        }
        obj.setOnlineRating(onlineRating);
    }
    
    public void PlayerDataOnDemand.setTimeout(Player obj, int index) {
        Integer timeout = 0;
        if (timeout < 0 || timeout > 100) {
            timeout = 100;
        }
        obj.setTimeout(timeout);
    }
    
    public void PlayerDataOnDemand.setTotalGames(Player obj, int index) {
        String totalGames = "0_" + index;
        obj.setTotalGames(totalGames);
    }
    
    public void PlayerDataOnDemand.setUsername(Player obj, int index) {
        String username = "username_" + index;
        obj.setUsername(username);
    }
    
    public Player PlayerDataOnDemand.getSpecificPlayer(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Player obj = data.get(index);
        Long id = obj.getId();
        return Player.findPlayer(id);
    }
    
    public Player PlayerDataOnDemand.getRandomPlayer() {
        init();
        Player obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Player.findPlayer(id);
    }
    
    public boolean PlayerDataOnDemand.modifyPlayer(Player obj) {
        return false;
    }
    
    public void PlayerDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Player.findPlayerEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Player' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Player>();
        for (int i = 0; i < 10; i++) {
            Player obj = getNewTransientPlayer(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}