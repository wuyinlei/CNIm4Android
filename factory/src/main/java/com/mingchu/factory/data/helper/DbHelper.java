package com.mingchu.factory.data.helper;

import com.mingchu.factory.model.db.AppDatabase;
import com.mingchu.factory.model.db.Group;
import com.mingchu.factory.model.db.GroupMember;
import com.mingchu.factory.model.db.Message;
import com.mingchu.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.mingchu.factory.model.db.Group_Table;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助工具类
 *
 * @function 增删改
 */

public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    /**
     * 观察者集合
     * Class<?>  观察的表
     * Set<ChangedListener>  每一个表对应的观察者
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听器中获取某一个表的所有监听者
     *
     * @param tClass 表
     * @param <T>    表的类型
     * @return 监听者
     */
    private <T extends BaseModel> Set<ChangedListener> getListeners(Class<T> tClass) {
        if (changedListeners.containsKey(tClass))
            return changedListeners.get(tClass);
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass          对某个表关注
     * @param changedListener 监听者
     * @param <T>             表的类型
     */
    public static <T extends BaseModel> void addChangedListener(Class<T> tClass, ChangedListener<T> changedListener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            changedListeners = new HashSet<>();
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(changedListener);
    }

    /**
     * 删除一个监听
     *
     * @param tClass          对某个表删除
     * @param changedListener 监听者
     * @param <T>             表的类型
     */
    public static <T extends BaseModel> void removeChangedListener(Class<T> tClass, ChangedListener<T> changedListener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            return;
        }
        changedListeners.remove(changedListener);
        instance.changedListeners.put(tClass, changedListeners);
    }

    /**
     * 新增或者修改的统一方法
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的类型 限定上线BaseModel
     */
    @SafeVarargs
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        //当前数据库的管理者
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(tClass)
                        .saveAll(Arrays.asList(models), databaseWrapper);
                instance.notifySave(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 删除操作
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的类型 限定上线BaseModel
     */
    @SafeVarargs
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(tClass)
                        .deleteAll(Arrays.asList(models), databaseWrapper);
                instance.notifyDelete(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 通知更改保存
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的类型 限定上线BaseModel
     */
    @SafeVarargs
    private final <Model extends BaseModel> void notifySave(Class<Model> tClass, final Model... models) {
        //找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }

        //群成员变更   例外情况
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            //消息发送变更  应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 更新会话列表
     *
     * @param messages 消息
     */
    private void updateSession(Message... messages) {
        //会话的唯一标识
        final Set<Session.Identify> identities = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identities.add(identify);
        }
        if (identities.size() == 0)
            return;

        // do in async
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync( new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identities.size()];
                int index = 0;
                for (Session.Identify identity : identities) {
                    Session session = SessionHelper.findFromLocal(identity.id);

                    if (session == null) {
                        //本地找不到  第一次聊天 创建一个和对方的会话
                        session = new Session(identity);
                    }
                    session.refreshToNow();  //刷新到当前的最新信息
                    adapter.save(session);  //保存会话
                    sessions[index++] = session;  //增加session  添加到集合
                }
                //通知存储
                instance.notifySave(Session.class, sessions);
            }
        }).build().execute();
    }

    /**
     * 群成员变更
     *
     * @param models 群成员
     */
    private void updateGroup(GroupMember... models) {
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember model : models) {
            //添加群id
            groupIds.add(model.getGroup().getId());
        }

        // do in async
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> group = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();

                instance.notifySave(Group.class, group.toArray(new Group[0]));
            }
        }).build().execute();

    }

    /**
     * 通知删除
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个实例的类型 限定上线BaseModel
     */
    @SafeVarargs
    private final <Model extends BaseModel> void notifyDelete(Class<Model> tClass, final Model... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }

        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }


    @SuppressWarnings("unchecked")
    public interface ChangedListener<Data extends BaseModel> {

        void onDataDelete(Data... list);

        void onDataSave(Data... list);
    }
}
