package com.news.toutiao.async;

import java.util.List;

/**
 * Created by huali on 2018/2/11.
 */
public interface EventHandler {
    void doHandler(EventModel model);  //处理model的handler

    List<EventType>getSupportEventType();  //关注那些EventType
}
