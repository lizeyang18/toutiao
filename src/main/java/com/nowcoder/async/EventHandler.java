package com.nowcoder.async;

import java.util.List;

/**
 * Created by lizeyang on 2019/5/2.
 */
public interface EventHandler {
    void doHandler(EventModel model);
    List<EventType> getSupportEventType();

}
