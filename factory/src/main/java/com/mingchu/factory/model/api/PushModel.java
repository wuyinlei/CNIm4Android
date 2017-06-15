package com.mingchu.factory.model.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mingchu.factory.Factory;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class PushModel {
    public static final int ENTITY_TYPE_LOGOUT = -1;
    public static final int ENTITY_TYPE_MESSAGE = 200;
    public static final int ENTITY_TYPE_ADD_FRIEND = 1001;
    public static final int ENTITY_TYPE_ADD_GROUP = 1002;
    public static final int ENTITY_TYPE_ADD_GROUP_MEMBERS = 1003;
    public static final int ENTITY_TYPE_MODIFY_GROUP_MEMBERS = 2004;

    private List<Entity> entities = new ArrayList<>();

    private PushModel(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public static PushModel decode(String json) {
        Gson gson = Factory.getGson();
        Type type = new TypeToken<List<Entity>>() {
        }.getType();

        try {
            List<Entity> entities = gson.fromJson(json, type);
            if (entities.size() > 0)
                return new PushModel(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Entity {
        public Entity() {

        }

        // 消息类型
        public int type;
        // 消息实体
        public String content;
        // 消息生成时间
        public Date createAt;
    }

    @Override
    public String toString() {
        return "PushModel{" +
                "entities=" + entities +
                '}';
    }
}
