package com.nowcoder.async;


import java.util.List;

/**
 * Created by lizeyang on 2019/12/28.
 */

public interface EventHandle {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
