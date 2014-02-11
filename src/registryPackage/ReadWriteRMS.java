/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryPackage;

/**
 *
 * @author LANREWAJU
 */
import javax.microedition.rms.RecordStore;
import org.json.me.*;

//import javax.microedition.rms.RecordStoreException;
public class ReadWriteRMS {

    private RecordStore rs = null;
    static final String REC_STORE = "ReadWriteRMS";
    stubMidlet sm;
    SplashCanvas splashscreen;
    JSONArray jsonarr;

    public ReadWriteRMS(stubMidlet stubmidlet) {
        sm = stubmidlet;
    }

    public void openRecStore() {
        try {
            rs = RecordStore.openRecordStore(REC_STORE, true);
        } catch (Exception e) {
        }
    }

    public void closeRecStore() {
        try {
            rs.closeRecordStore();
        } catch (Exception e) {
        }
    }

    public void deleteRecStore() {
//        for (int i = 0; i < RecordStore.listRecordStores().length; i++) {
//            System.out.println("the record is this " + RecordStore.listRecordStores()[i]);
//        }
        if (RecordStore.listRecordStores() != null) {
            try {
                RecordStore.deleteRecordStore(REC_STORE);
            } catch (Exception e) {
            }
        }
    }

    public void writeRecord(JSONObject obj) {
        byte[] rec = obj.toString().getBytes();
        try {
            rs.addRecord(rec, 0, rec.length);
        } catch (Exception e) {
        }
    }

    public JSONArray readRecords() {
        System.out.println("===================Hello");
        try {
            byte[] recData = new byte[5];
            jsonarr = new JSONArray();
            int len;
            System.out.println("record size = " + rs.getNumRecords());
            for (int i = 1; i <= rs.getNumRecords(); i++) {
                if (rs.getRecordSize(i) > recData.length) {
                    recData = new byte[rs.getRecordSize(i)];
                    //=========================
                }

                len = rs.getRecord(i, recData, 0);

                //    vector.addElement(new String(recData, 0, len));
                JSONObject jsonobj = new JSONObject(new String(recData, 0, len));
                // System.out.println("Jsonobject " + jsono);
                jsonarr.put(jsonobj);
                // System.out.println("------------------------------");
                // System.out.println("Record " + i + " : " + new String(recData, 0, len));
                //   System.out.println("------------------------------");
            }
            System.out.println("json object  at 1" + jsonarr.getJSONObject(1));
        } catch (Exception e) {
            System.out.println("Nothing found " + e.toString());
        }
        return jsonarr;
    }
}
