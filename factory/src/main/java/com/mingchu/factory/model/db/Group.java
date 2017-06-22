package com.mingchu.factory.model.db;

import com.mingchu.factory.data.helper.GroupHelper;
import com.mingchu.factory.model.db.view.MemberUserModel;
import com.mingchu.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Table(database = AppDatabase.class)
public class Group extends BaseDbModel<Group> implements Serializable {
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String desc;
    @Column
    private String picture;
    @Column
    private int notifyLevel;
    @Column
    private Date joinAt;
    @Column
    private Date modifyAt;

    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User owner;

    List<GroupMember> groupMembers;

    public Object holder;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public Date getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(Date joinAt) {
        this.joinAt = joinAt;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "groupMembers")
    public List<GroupMember> getGroupMembers() {
        if (groupMembers == null || groupMembers.isEmpty()) {
            groupMembers = GroupHelper.getMembers(id, -1);
        }
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    private List<MemberUserModel> groupLatelyMembers;

    public List<MemberUserModel> getLatelyGroupMembers() {
        if (groupLatelyMembers == null || groupLatelyMembers.isEmpty()) {
            groupLatelyMembers = GroupHelper.getMemberUsers(id, 4);
        }
        return groupLatelyMembers;
    }

    private long groupMemberCount = -1;

    public long getGroupMemberCount() {
        if (groupMemberCount == -1) {
            groupMemberCount = GroupHelper.getMemberCount(id);
        }
        return groupMemberCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id != null ? id.equals(group.id) : group.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (groupMembers != null ? groupMembers.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isSame(Group oldT) {
        return Objects.equals(this, oldT);
    }

    @Override
    public boolean isUiContentSame(Group oldT) {
        return Objects.equals(this.name, oldT.name)
                && Objects.equals(this.desc, oldT.desc)
                && Objects.equals(this.picture, oldT.picture)
                && Objects.equals(this.holder, oldT.holder);
    }
}
