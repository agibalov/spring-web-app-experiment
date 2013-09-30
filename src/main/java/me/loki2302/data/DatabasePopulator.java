package me.loki2302.data;

import java.util.Random;

import javax.annotation.PostConstruct;

import me.loki2302.service.CurrentTimeProvider;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator {
    private final static Logger logger = LoggerFactory.getLogger(DatabasePopulator.class);
    
    @Autowired
    private CurrentTimeProvider currentTimeProvider;

    @Autowired
    private WorldFacade worldFacade;
    
    @PostConstruct
    public void populateDatabase() {
        RandomOptionGenerator<HistoryEvent> historyEventGenerator = 
                new RandomOptionGenerator<HistoryEvent>()
                    .withOption(HistoryEvent.NewCategory, 1)
                    .withOption(HistoryEvent.NewUser, 400)
                    .withOption(HistoryEvent.NewArticle, 1200)
                    .withOption(HistoryEvent.NewComment, 2400);
        
        Random random = new Random();
        
        DateTime historyBeginningTime = new DateTime().minusYears(1);
        DateTime historyEndTime = new DateTime().minusSeconds(10);
                
        DateTime currentTime = historyBeginningTime;
        int eventCount = 0;
        while(currentTime.isBefore(historyEndTime)) {
            currentTimeProvider.overrideCurrentTime(currentTime.toDate());
            
            HistoryEvent historyEvent = historyEventGenerator.generate();
            if(historyEvent.equals(HistoryEvent.NewCategory)) {
                worldFacade.makeRandomCategory();
            } else if(historyEvent.equals(HistoryEvent.NewUser)) {
                worldFacade.makeRandomUser();
            } else if(historyEvent.equals(HistoryEvent.NewArticle)) {
                worldFacade.makeRandomArticle();
            } else if(historyEvent.equals(HistoryEvent.NewComment)) {
                worldFacade.makeRandomComment();
            } else {
                throw new RuntimeException("Unknown history event type");
            }
            
            ++eventCount;
            if(eventCount % 500 == 0) {
                logger.info("{} events...", eventCount);
            }
            
            currentTime = currentTime
                    //.plusDays(random.nextInt(15))
                    //.plusHours(random.nextInt(23))
                    .plusMinutes(random.nextInt(60))
                    .plusSeconds(random.nextInt(60))
                    .plusMillis(random.nextInt(1000));
        }
        
        logger.info("generated:");
        logger.info("  {} users", worldFacade.userIds.size());
        logger.info("  {} categories", worldFacade.categoryIds.size());
        logger.info("  {} articles", worldFacade.articleIds.size());
        logger.info("  {} comments", worldFacade.commentIds.size());
        
        currentTimeProvider.overrideCurrentTime(null);
    }
    
    private static enum HistoryEvent {
        NewCategory,
        NewUser,
        NewArticle,
        NewComment
    }
}