// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ro.progsquad.chessmanager.model;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.progsquad.chessmanager.model.TeamMatch;
import ro.progsquad.chessmanager.model.TeamMatchDataOnDemand;
import ro.progsquad.chessmanager.model.TeamMatchIntegrationTest;

privileged aspect TeamMatchIntegrationTest_Roo_IntegrationTest {
    
    declare @type: TeamMatchIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: TeamMatchIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: TeamMatchIntegrationTest: @Transactional;
    
    @Autowired
    TeamMatchDataOnDemand TeamMatchIntegrationTest.dod;
    
    @Test
    public void TeamMatchIntegrationTest.testCountTeamMatches() {
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", dod.getRandomTeamMatch());
        long count = TeamMatch.countTeamMatches();
        Assert.assertTrue("Counter for 'TeamMatch' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void TeamMatchIntegrationTest.testFindTeamMatch() {
        TeamMatch obj = dod.getRandomTeamMatch();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to provide an identifier", id);
        obj = TeamMatch.findTeamMatch(id);
        Assert.assertNotNull("Find method for 'TeamMatch' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TeamMatch' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void TeamMatchIntegrationTest.testFindAllTeamMatches() {
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", dod.getRandomTeamMatch());
        long count = TeamMatch.countTeamMatches();
        Assert.assertTrue("Too expensive to perform a find all test for 'TeamMatch', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<TeamMatch> result = TeamMatch.findAllTeamMatches();
        Assert.assertNotNull("Find all method for 'TeamMatch' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TeamMatch' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void TeamMatchIntegrationTest.testFindTeamMatchEntries() {
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", dod.getRandomTeamMatch());
        long count = TeamMatch.countTeamMatches();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TeamMatch> result = TeamMatch.findTeamMatchEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TeamMatch' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TeamMatch' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void TeamMatchIntegrationTest.testFlush() {
        TeamMatch obj = dod.getRandomTeamMatch();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to provide an identifier", id);
        obj = TeamMatch.findTeamMatch(id);
        Assert.assertNotNull("Find method for 'TeamMatch' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTeamMatch(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TeamMatch' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void TeamMatchIntegrationTest.testMergeUpdate() {
        TeamMatch obj = dod.getRandomTeamMatch();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to provide an identifier", id);
        obj = TeamMatch.findTeamMatch(id);
        boolean modified =  dod.modifyTeamMatch(obj);
        Integer currentVersion = obj.getVersion();
        TeamMatch merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'TeamMatch' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void TeamMatchIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", dod.getRandomTeamMatch());
        TeamMatch obj = dod.getNewTransientTeamMatch(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TeamMatch' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'TeamMatch' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void TeamMatchIntegrationTest.testRemove() {
        TeamMatch obj = dod.getRandomTeamMatch();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TeamMatch' failed to provide an identifier", id);
        obj = TeamMatch.findTeamMatch(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TeamMatch' with identifier '" + id + "'", TeamMatch.findTeamMatch(id));
    }
    
}
