package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizeyang on 2019/5/2.
 * function:放在队列里面，表示刚刚发生了什么
 */
public class EventModel {
    private EventType type;
    private int actorId;   //触发者
    private int entityId;
    private int entityType;
    private int entityOwnerId;

    private Map<String ,String> exts = new HashMap<>();

    public Map<String, String> getExts() {
        return exts;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventModel setExt(String name,String value){
       exts.put(name,value);
        return this;
    }

    //构造函数
    public EventModel(EventType type){
        this.type = type;
    }

    //默认构造函数
    public EventModel(){

    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }


}
