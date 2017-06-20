package comlowelldev.github.groupmebotmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LowellDev
 */
public class GroupMeBot {

    private String name;
    private String botID;
    private String groupID;
    private String avatarURL;
    private String callBackURL;
    private String groupName;
    private Boolean DMNotification;
    private ArrayList<String> searchTerms;

    /**
     * Create a bot object
     *
     * @param name the name of the bot
     * @param botID the id given to the bot by GroupMe
     * @param group the group the bot resides in
     */
    public GroupMeBot(String name, String botID, String group) {
        this.name = name;
        this.botID = botID;
        this.groupID = group;
        searchTerms = new ArrayList<>();
    }

    /**
     * Create a bot object
     *
     * @param name the name of the bot
     * @param botID the id given to the bot by GroupMe
     * @param group the group the bot resides in@param name
     * @param avatar the url of the bot picture
     * @param callback the url to send messages to
     * @param DMNotification Boolean to represent if notifications are accepted
     */
    public GroupMeBot(String name, String botID, String group, String avatar, String callback, Boolean DMNotification) {
        this(name, botID, group);
        this.avatarURL = avatar;
        this.callBackURL = callback;
        this.DMNotification = DMNotification;
        searchTerms = new ArrayList<>();
    }

    /**
     * Not implemented
     *
     * @param token the creator's token
     * @param botName the name of the bot
     * @param GroupID the id of the group to add the bot to
     * @return the new bot [null if failed]
     */
    public static GroupMeBot createBot(String token, String botName, String GroupID) {
        try {
            JSONObject cmd = new JSONObject()
                    .put("bot", new JSONObject()
                            .put("name", botName)
                            .put("group_id", GroupID));
            String url = "https://api.groupme.com/v3/bots";
            InputStream response = HTTP.SendPOST(url, new JSONObject().put("token", token), cmd.toString());
            if (response == null) {
                return null;
            }
            StringBuilder responseBld = new StringBuilder();
            BufferedReader read = new BufferedReader(new InputStreamReader(response));
            String jsonStr;
            while ((jsonStr = read.readLine()) != null) {
                responseBld.append(jsonStr);
            }
            JSONObject json = new JSONObject(responseBld.toString()).getJSONObject("response").getJSONObject("bot");
            System.out.println("Response: " + json.toString());
            return new GroupMeBot(json.getString("name"), json.getString("bot_id"), json.getString("group_id")); //should return a newly created bot
        } catch (IOException ex) {
            Logger.getLogger(GroupMeBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean destroy(String token,String botID) throws JSONException {
    return HTTP.SendPOST(GroupMe.baseURL+"/bots/destroy",
            new JSONObject().put("token",token),
            (new JSONObject().put("bot_id",botID)).toString())!=null;
    }

    /**
     * Send a message from the bot
     *
     * @param text the message to send
     * @return success/fail
     */
    public boolean sendMessage(String text) throws JSONException{
        return GroupMe.sendMessage(new Message(text), this.botID, true);
    }

    /**
     * Have the bot send a message
     *
     * @param text the message to send
     * @param users the users to mention
     * @return success/fail
     */
    public boolean sendMessage(String text, User[] users)throws JSONException {
        Message message = new Message(text);
        JSONObject info = new JSONObject();
        int count = 0;
        String loci[] = new String[users.length];
        String userids[] = new String[users.length];
        for (int i = 0; i < users.length; i++) {
            userids[i] = users[i].getUserID();
            loci[i] = "[" + count + "," + (users[i].getName().length() + 1) + "]";
            text = "@" + users[i] + " " + text;
            count += ((users[i].getName().length() + 1) + 2);
        }
        info.put("user_ids", new JSONArray(userids));
        info.put("loci", new JSONArray(loci));
        Attachment attachment = new Attachment(AttachmentType.MENTION, info);
        message.setText(text);
        message.addAttachment(attachment);
        return GroupMe.sendMessage(message, this.botID, true);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the avatarURL
     */
    public String getAvatarURL() {
        return avatarURL;
    }

    /**
     * @param avatarURL the avatarURL to set
     */
    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    /**
     * @return the callBackURL
     */
    public String getCallBackURL() {
        return callBackURL;
    }

    /**
     * @param callBackURL the callBackURL to set
     */
    public void setCallBackURL(String callBackURL) {
        this.callBackURL = callBackURL;
    }

    /**
     * @return the DMNotification
     */
    public Boolean getDMNotification() {
        return DMNotification;
    }

    /**
     * @param DMNotification the DMNotification to set
     */
    public void setDMNotification(Boolean DMNotification) {
        this.DMNotification = DMNotification;
    }

    String getBotID() {
        return botID; //To change body of generated methods, choose Tools | Templates.
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupName() {
        return groupName;
    }
    public void addTerm(String term){
        searchTerms.add(term);

    }
    public void addTerms(String[] terms){
        for(String term: terms){
            this.addTerm(term);
        }
    }
    public void addTerms(List<String> terms){
        for(String term: terms){
            this.addTerm(term);
        }
    }
    @Override
    public String toString(){
        return(name+"\n"+groupName);
    }


}
