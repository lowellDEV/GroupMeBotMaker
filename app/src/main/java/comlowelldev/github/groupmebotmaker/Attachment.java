package comlowelldev.github.groupmebotmaker;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LowellDev
 */
class Attachment {
    private AttachmentType type;
    private JSONObject attachments;
    public Attachment(AttachmentType type,JSONObject info) throws JSONException {
        if(type ==AttachmentType.MENTION){
            attachments.put("type", "mentions");
            attachments.put("user_ids",info.getJSONArray("user_ids"));
            attachments.put("loci",info.getJSONArray("loci"));
        }
        else{
            System.out.println("Failed to Create");
            return;
        }
        
    }
}
