package com.mingchu.factory.model.db;

import com.mingchu.common.factory.model.Author;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.mingchu.factory.model.db.GroupMember_Table;


import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 *
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author {
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex;
    @Column
    private boolean isFollow;
    //我对某人的备注信息
    @Column
    private String alias;
    //用户关注人的数量
    @Column
    private int follows;
    //用户粉丝数量
    @Column
    private int following;
    //时间字段
    @Column
    private Date modifyAt;

    List<GroupMember> groupMembers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "groupMembers")
    public List<GroupMember> getGroupMembers() {
        if (groupMembers == null || groupMembers.isEmpty()) {
            groupMembers = SQLite.select()
                    .from(GroupMember.class)
                    .where(GroupMember_Table.user_id.eq(id))
                    .queryList();
        }
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return sex == user.sex
                && isFollow == user.isFollow
                && follows == user.follows
                && following == user.following
                && (id != null ? id.equals(user.id) : user.id == null
                && (name != null ? name.equals(user.name) : user.name == null
                && (phone != null ? phone.equals(user.phone) : user.phone == null
                && (portrait != null ? portrait.equals(user.portrait) : user.portrait == null
                && (desc != null ? desc.equals(user.desc) : user.desc == null
                && (alias != null ? alias.equals(user.alias) : user.alias == null
                && (modifyAt != null ? modifyAt.equals(user.modifyAt) : user.modifyAt == null)))))));

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(User old) {
        return this == old || Objects.equals(id, old.getId());
    }

    @Override
    public boolean isUiContentSame(User data) {
        //内容是否相等  名字头像 并别  是否已经关注
        return this == data || (Objects.equals(name, data.getName()) &&
                Objects.equals(portrait, data.getPortrait())
                && Objects.equals(sex, data.getSex())
                && Objects.equals(isFollow, data.isFollow())
        )
                ;
    }
}
