package com.news.toutiao.async.handler;

import com.news.toutiao.async.EventHandler;
import com.news.toutiao.async.EventModel;
import com.news.toutiao.async.EventType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huali on 2018/2/11.
 */
@Component
public class LikeHandler  implements EventHandler{
    @Override
    public void doHandler(EventModel model) {
        System.out.println("Liked");
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}