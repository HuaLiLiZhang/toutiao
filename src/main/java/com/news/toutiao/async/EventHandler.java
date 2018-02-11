package com.news.toutiao.async;

import java.util.List;

/**
 * Created by huali on 2018/2/11.
 */
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType>getSupportEventType();
}
