package comlowelldev.github.groupmebotmaker;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LowellDEv
 */

public class Message {

    private String id;
    private String source_guid;
    private String created_at;
    private User user;
    private String groupID;
    private String text;
    private Boolean system;
    private String[] favorited;
    private ArrayList<Attachment> attachments;
    
    public Message(String text) {
        this.attachments = new ArrayList<Attachment>();
        this.text =text;
        int end = new Random().nextInt(100000-1000)+1000;
        this.source_guid ="JAVALIBRARYBASED"+end;
    }
    public Message(String text, Attachment[] attachments) {
        this.attachments = new ArrayList<Attachment>();
        for (Attachment attach : attachments) {
            this.attachments.add(attach);
        }
        this.text =text;
        int end = new Random().nextInt(100000-1000)+1000;
        this.source_guid =GroupMe.baseURL+end;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the source_guid
     */
    public String getSource_guid() {
        return source_guid;
    }

    /**
     * @param source_guid the source_guid to set
     */
    public void setSource_guid(String source_guid) {
        this.source_guid = source_guid;
    }

    /**
     * @return the created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at the created_at to set
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the groupID
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * @param groupID the groupID to set
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the system
     */
    public Boolean getSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(Boolean system) {
        this.system = system;
    }

    /**
     * @return the favorited
     */
    public String[] getFavorited() {
        return favorited;
    }

    /**
     * @param favorited the favorited to set
     */
    public void setFavorited(String[] favorited) {
        this.favorited = favorited;
    }

    /**
     * @return the attachments
     */
    public Attachment getAttachment(int index) {
        return attachments.get(index);
    }
    public Attachment[] getAttachments() {
        return  attachments.toArray(new Attachment[attachments.size()]);
    }

    /**
     * @param attachments the attachments to set
     */
    public void addAttachment(Attachment attachments) {
        this.attachments.add(attachments);
    }
}
