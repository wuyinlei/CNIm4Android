package com.mingchu.factory.data.helper;

import com.mingchu.factory.model.db.AppDatabase;
import com.mingchu.factory.model.db.GroupMember;
import com.mingchu.factory.model.db.Message;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;


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

    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    private <T extends BaseModel> Set<ChangedListener> getListeners(Class<T> tClass) {
        if (changedListeners.containsKey(tClass))
            return changedListeners.get(tClass);
        return null;
    }

    public static <T extends BaseModel> void addChangedListener(Class<T> tClass, ChangedListener<T> changedListener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            changedListeners = new HashSet<>();
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(changedListener);
    }

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
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }


        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }

    private void updateSession(Message[] models) {

    }

    private void updateGroup(GroupMember[] models) {

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
    public interface ChangedListener<Data> {

        void onDataDelete(Data... list);

        void onDataSave(Data... list);
    }
}
