package comlowelldev.github.groupmebotmaker;

/**
 * Created by Rider X2 on 6/19/2017.
 */

public class Group {
    private String  groupName;
    private String groupID;
    public Group(String gpName,String gpID){
        groupName =gpName;
        groupID =gpID;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    @Override
    public String toString(){
        return "Group Name:\n"+groupName;
    }
}
