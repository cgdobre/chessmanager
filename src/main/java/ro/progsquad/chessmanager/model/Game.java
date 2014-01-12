package ro.progsquad.chessmanager.model;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findGamesByGameIdEquals" })
public class Game {

    @NotNull
    @Column(unique = true)
    private Long gameId;

    @NotNull
    @ManyToOne
    private Player white;

    @NotNull
    @ManyToOne
    private Player black;

    @ManyToOne
    private Player winner = null;

    private Boolean wonOnTime = false;

    private String timePerMove = "";

    @Min(0L)
    private Integer numberOfMoves = 0;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate = null;

    @ManyToOne
    private TeamMatch teamMatch = null;
}
