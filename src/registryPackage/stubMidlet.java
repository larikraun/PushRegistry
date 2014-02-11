/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;
import org.json.me.*;

/**
 *
 * @author LANREWAJU
 */
public class stubMidlet extends MIDlet implements MessageListener, CommandListener {

    private final String MIDLET_CLASS_NAME;
    private MessageConnection connection, conn;
    private TextMessage message;
//     private SplashScreen splashScreen;
    // Endpoint to register. This MIDlet will be launched when an SMS is
    // received in the port 6555.	
    private static final String SMS_CONNECTION_URL = "sms://:6555";
    // This MIDlet can be activated by any sender
    private static final String ALLOWED_SENDER_FILTER = "*";
    private static final Command EXIT_COMMAND = new Command("Exit", Command.EXIT, 0);
    // Command that registers the MIDlet to launch automatically when an SMS is
    // received
    private static final Command REGISTER_CONNECTION_COMMAND = new Command("YES", Command.ITEM, 0);
    // Command that unregisters the MIDlet from auto launching by incoming SMS
    // message
    private static final Command UNREGISTER_CONNECTION_COMMAND = new Command("Unregister", Command.OK, 0);
    // Command that registers the MIDlet to launch automatically after a certain
    // time period
    private static final Command REGISTER_TIMER_ALARM_COMMAND = new Command("Register timer alarm", Command.OK, 0);
    //command that buys the song
    Command buySong = new Command("Check songs", Command.OK, 0);
    Command buy = new Command("Buy", Command.OK, 0);
    Command back = new Command("Back", Command.BACK, 0);
    //Command OK = new Command("OK", Command.OK, 0);
    Command YES = new Command("YES", Command.OK, 0);
    Command NO = new Command("NO. THANKS!", Command.CANCEL, 1);
    Command NAH = new Command("NO. THANKS!", Command.CANCEL, 1);
    Command CANCEL = new Command("Main Menu", Command.CANCEL, 0);
    Command CONTINUE = new Command("CONTINUE", Command.OK, 0);
    // Available push connections for this MIDlet
    private Vector availableConnections = new Vector();
    boolean connected = false;
    private Form mainForm;
    Graphics g;
    public Alert alert;
    boolean SMSTriggerred = false;
    // public JSONObject j = null;
    public JSONObject incomingMessage = null;
    public JSONArray jb = null;
    public JSONObject jc = null;
    public JSONArray jd = new JSONArray();
    private List list;
    public List mList;
    public List oList;
    String[] connections;
    String mtnCode = "mtn_code";
    String gloCode = "glo_code";
    String etiCode = "etisalat_code";
    String airtelCode = "airtel_code";
    Form form, oForm;
    String messageContent;
    // String [] array = {"Dbanj","Iyanya","tuface","djaflex"};
    Hashtable hash, hash1, hash2, hash3;
    // Vector vector = new Vector();
    Vector vectors;
    Vector vec;
    private Display display;
    SplashCanvas sc;
    MenuCanvas mc;
    ReadWriteRMS rwr;
    private Alert al;
    private Alert rAlert;
    private Alert sAlert;
    RecordStore rs;

    public stubMidlet() {
        // The Class name is needed for the registration	 
        MIDLET_CLASS_NAME = this.getClass().getName();
    }

    /**
     * Registers the connection for launching this MIDlet automatically by an
     * incoming SMS.
     */
    public SplashCanvas getSplashCanvas() {
        sc = new SplashCanvas(this);
        sc.display = Display.getDisplay(this);
        sc.sm = this;
        return sc;
    }

    public ReadWriteRMS readWriteRMS() {
        rwr = new ReadWriteRMS(this);
        rwr.sm = this;
        return rwr;
    }

    public MenuCanvas getMenuCanvas() {
        mc = new MenuCanvas(this);
        mc.display = Display.getDisplay(this);
        mc.sm = this;
        return mc;
    }
//    public displayManager getDisplayManager() {
//        dm = new displayManager(this);
//        dm.display = Display.getDisplay(this);
//        dm.stubmidlet = this;
//        return dm;
    //}
    Image logo;

    public void dismissShowAlert(Displayable d1, Displayable d2) {
        if (d1 instanceof Alert && d2 instanceof Alert) {
            System.out.println("true");
            display.setCurrent(getMenuCanvas());
        } else {
            System.out.println("false");
            display.setCurrent(d2);
        }
        // display.setCurrent(displayable);

    }

    public Image getLogo(String splashscreen) {

        try {

            logo = Image.createImage(splashscreen);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return logo;
    }

    protected Image createScaledImage(Image image) {
        final int sourceWidth = image.getWidth();
        final int sourceHeight = image.getHeight();

        final Image sImg = Image.createImage(sourceWidth, sourceHeight);
        final Graphics g = sImg.getGraphics();
        final int[] lineRGB = new int[sourceWidth];
        final int[] sourcePos = new int[sourceWidth];
        int y = 0;
        int eps = -(sourceWidth >> 1);
        for (int x = 0; x < sourceWidth; x++) {
            eps += sourceWidth;
            if ((eps << 1) >= sourceWidth) {
                if (++y == sourceWidth) {
                    break;
                }
                sourcePos[y] = x;
                eps -= sourceWidth;
            }
        }
        for (int z = 0; z < sourceHeight; z++) {
            image.getRGB(lineRGB, 0, sourceWidth, 0, z * sourceHeight / sourceHeight, sourceWidth, 1);
            for (int x = 1; x < sourceWidth; x++) // skip pixel 0 
            {
                lineRGB[x] = lineRGB[sourcePos[x]];
            }
            g.drawRGB(lineRGB, 0, sourceWidth, 0, z, sourceWidth, 1, false);
        }
        return sImg;
    }

// public SplashScreen getSplashScreen() {
//        if (splashScreen == null) {//GEN-END:|31-getter|0|31-preInit
//            // write pre-init user code here
//            splashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|31-getter|1|31-postInit
//           // splashScreen.setTitle("welcome");
//            splashScreen.setCommandListener(this);
//            splashScreen.setFullScreenMode(true);
//            splashScreen.setImage(createScaledImage(getLogo()));
//           // splashScreen.setText("powered by Adeyimika");
//            splashScreen.setTimeout(3000);//GEN-END:|31-getter|1|31-postInit
//            // write post-init user code here
//        }//GEN-BEGIN:|31-getter|2|
//        return splashScreen;
//    }
    private void registerSMSConnection() {
        // printString("Registering the connection...");

        try {
            // Register this MIDlet to be launched automatically when an SMS is
            // sent to the specified port
            System.out.println("displayable4");
            PushRegistry.registerConnection(SMS_CONNECTION_URL, MIDLET_CLASS_NAME, ALLOWED_SENDER_FILTER);
//           if( display.getCurrent() instanceof Alert){
//               System.out.println("displayable check");
//               display.setCurrent(mainForm);
//               
//           }
            //  display.setCurrent(successfulReg());
            display.setCurrent(getSplashCanvas());
            System.out.println("Port already registered");
            // Update the inbound connections
            //  clearConnections();
            //  getPushRegistryConnections();
        } catch (Exception ex) {
            // printString("Connection failed.\n");
            //mainForm.append("Port already registered");
            // printString(ex.getMessage());

            System.out.println("exception being caught " + ex.getMessage());
        }
    }

    /**
     * Unregisters the connection so that this MIDlet will not be launched when
     * an SMS is received.
     */
    private void unregisterSMSConnection() {
        // printString("Unregistering the connection...");
        PushRegistry.unregisterConnection(SMS_CONNECTION_URL);
        // printString("Unregistration is complete!");
        display.setCurrent(successfulReg());
    }

    /**
     * Registers this MIDlet to be launched automatically after a certain time
     * period.
     *
     * @param timePeriodToAutoStart - period in milliseconds after which this
     * MIDlet will launch.
     */
    private void registerTimerAlarm(long timePeriodToAutoStart) {
        // Set the launch time to current time + the specified period
        long timeToWakeUp = System.currentTimeMillis() + timePeriodToAutoStart;
//        printString("Registering the timer alarm...");

        try {
            PushRegistry.registerAlarm(MIDLET_CLASS_NAME, timeToWakeUp);
            //    printString("Alarm is registered!");
        } catch (ClassNotFoundException ex) {
            //        printString(ex.getMessage());
            //       printString("Alarm registration failed.");
        } catch (ConnectionNotFoundException ex) {
//            printString(ex.getMessage());
//            printString("Alarm registration failed.");
        }
    }

    /**
     * Unregisters this MIDlet from all inbound connections and removes them
     * from the internal collection.
     */
    private void clearConnections() {
        if (availableConnections != null) {
            while (!availableConnections.isEmpty()) {
                MessageConnection messageConnection =
                        (MessageConnection) availableConnections.firstElement();
                if (messageConnection != null) {
                    try {
                        messageConnection.setMessageListener(null);
                        messageConnection.close();
                    } catch (IOException ex) {
//                        printString(ex.toString());
                    }
                }
                availableConnections.removeElementAt(0);
            }
        }
    }

    /**
     * Establishes all available push connections.
     */
    private void getPushRegistryConnections() {

        // Get a list of registereG42d connections for this MIDlet
        connections = PushRegistry.listConnections(true);

        if (connections.length != 0) {
            System.out.println("List of available connections: ");
            connected = true;
            // display.setCurrent(mainForm);
//                             mainForm.append(incomingMessage.get("message").toString());
            mainForm.append(createScaledImage(getLogo("/splashscreen.png")));
            display.setCurrent(mainForm);
            for (int i = 0; i < connections.length; i++) {
                try {
                    MessageConnection mc =
                            (MessageConnection) Connector.open(connections[i]);
                    // printString("(" + i + ") - " + connections[i]);
                    // Register this MIDlet to be notified when a message has
                    // been received on the connection
                    mc.setMessageListener(this);
                    availableConnections.addElement(mc);
                } catch (SecurityException ex) {
//                    printString("Connection failed.");
                    // mainForm.append(ex.getMessage() + "security exception caught ");
                    // printString(ex.getMessage());
                } catch (IOException ex) {
                    // printString("Connection failed.");
                    // printString(ex.getMessage());
                } catch (ClassCastException ex) {
                    //  printString("Class cast exception being caught " + ex.getMessage());
                }
            }
        } else if (connections.length == 0 || connections == null) {
            // System.out.println("A connection is present " + connections[0]);
            display.setCurrent(getSplashCanvas());

        }
    }

    public Alert messageSendingStatus() {
        Alert loadingDialog = new Alert("", "Sending Message", null, AlertType.INFO);
        // //#style infoAlert
//        loadingDialog = new Form("Please wait");

        Gauge gau = new Gauge(null, false,
                Gauge.INDEFINITE,
                Gauge.CONTINUOUS_RUNNING);
        // loadingDialog.append(gau);
        loadingDialog.setIndicator(gau);
        loadingDialog.setTimeout(Alert.FOREVER);
        display.setCurrent(loadingDialog);

        Thread t = new Thread(new Runnable() {
            int time = 20;

            public void run() {
                display.setCurrent(menuList());
            }
        ;


        });
        return loadingDialog;
    }

    public Alert getIndicator() {
        Alert loadingDialog = new Alert("", "Fetching Contents", null, AlertType.INFO);
        // //#style infoAlert
//        loadingDialog = new Form("Please wait");

        Gauge gau = new Gauge(null, false,
                Gauge.INDEFINITE,
                Gauge.CONTINUOUS_RUNNING);
        // loadingDialog.append(gau);
        loadingDialog.setIndicator(gau);
        loadingDialog.setTimeout(Alert.FOREVER);
        display.setCurrent(loadingDialog);

        Thread t = new Thread(new Runnable() {
            int time = 20;

            public void run() {
                System.out.println("time " + time);
                time--;
                setConnection();
                if (vec == null || vec.isEmpty()) {
                    System.out.println("Check your internet connection");
                    //display.setCurrent(errorAlert());
                    dismissShowAlert(display.getCurrent(), getMenuCanvas());
                    // sm.alert.setString("Check your internet connection");
                    // display.setCurrent(sm.alertAlert());
                } else {

                    System.out.println("vec exists");
                    readWriteRMS();
                    //rwr.closeRecStore();
                    // rwr.deleteRecStore();
                    rwr.openRecStore();
                    for (int i = 0; i < vec.size(); i++) {
                        rwr.writeRecord((JSONObject) vec.elementAt(i));
                    }
                    System.out.println("========================Record store created");

                    System.out.println("========================");
                    // == == == = display.setCurrent(buyList());
                    display.setCurrent(menuList());
                    //sm.switchDisplay(null, sm.buyList());
                }
                // display.setCurrent(buyList());
            }
        });
        t.start();
        return loadingDialog;
    }

    public Form offlineForm() {
        int j = oList.getSelectedIndex();
        // for (int i = 0; i < vector.size(); i++) {
        final JSONObject ht = (JSONObject) vectors.elementAt(j);
        //Form oForm;
        oForm = new Form("");
        StringItem mtnButton = new StringItem("Buy Song", "Buy", Item.BUTTON);
        Command buttonCommand = new Command("", Command.ITEM, 1);
        try {
            mtnButton.setDefaultCommand(buttonCommand);
            mtnButton.setItemCommandListener(new ItemCommandListener() {
                public void commandAction(Command c, Item item) {
                    try {
                        System.out.println("Button mtn was clicked");
                        handleSendCommand("6580", ht.get(mtnCode).toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            StringItem gloButton = new StringItem("", "Buy", Item.BUTTON);
            //Command buttonCommand1 = new Command("", Command.ITEM, 1);

            gloButton.setDefaultCommand(buttonCommand);
            gloButton.setItemCommandListener(new ItemCommandListener() {
                public void commandAction(Command c, Item item) {
                    System.out.println("Button glo was clicked");
                    try {
                        handleSendCommand("6580", ht.get(gloCode).toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            StringItem airtelButton = new StringItem("", "Buy", Item.BUTTON);
            //  Command buttonCommand2 = new Command("", Command.ITEM, 1);

            airtelButton.setDefaultCommand(buttonCommand);
            airtelButton.setItemCommandListener(new ItemCommandListener() {
                public void commandAction(Command c, Item item) {
                    System.out.println("Button airtel was clicked");
                    try {
                        handleSendCommand("6580", ht.get(airtelCode).toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            StringItem etisalatButton = new StringItem("", "Buy", Item.BUTTON);
            // Command buttonCommand = new Command("", Command.ITEM, 1);

            etisalatButton.setDefaultCommand(buttonCommand);
            etisalatButton.setItemCommandListener(new ItemCommandListener() {
                public void commandAction(Command c, Item item) {
                    try {
                        System.out.println("Button etisalat was clicked");
                        handleSendCommand("6580", ht.get(etiCode).toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            //String code = ht.get

            oForm.append(oList.getString(oList.getSelectedIndex()) + " by " + ht.get("artist").toString());
            // oForm.append((Image)vector.elementAt(j));
            System.out.println("the selected index " + oList.getSelectedIndex() + " by " + ht.get("artist").toString());
            oForm.append("\n");

            //  System.out.println("code is " + ht.get(mtnCode).toString());
            oForm.append(mtnButton);
            oForm.append("MTN - " + ht.get(mtnCode).toString());
            oForm.append(gloButton);
            oForm.append("GLO - " + ht.get(gloCode).toString());
            oForm.append(airtelButton);
            oForm.append("AIRTEL - " + ht.get(airtelCode).toString());
            oForm.append(etisalatButton);
            oForm.append("ETISALAT - " + ht.get(etiCode).toString());
            //}
            oForm.addCommand(back);
            //  oForm.addCommand(buy);
            oForm.setCommandListener(this);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return oForm;
    }

    /**
     * From CommandListener. Called by the system to indicate that a command has
     * been invoked on a particular displayable.
     *
     * @param command the command that was invoked
     * @param displayable the displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == mList) {
            if (command == List.SELECT_COMMAND) {
                selectAction();
            } else if (command == EXIT_COMMAND) {
                System.out.println("exit command pressed");
                exitMidlet();
            }
        } else if (displayable == list) {
            if (command == back) {
                display.setCurrent(mList);
            }else if(command==List.SELECT_COMMAND){
                display.setCurrent(buyForm());
            }
        } else if (command == EXIT_COMMAND) {
            System.out.println("exit command pressed");
            exitMidlet();
        } else if (command == REGISTER_CONNECTION_COMMAND) {
            System.out.println("displayable3");
            registerSMSConnection();
        } else if (command == YES) {
            //    try {
            try {
                // handleSendCommand(messageContent.substring(messageContent.indexOf('~') + 1, messageContent.length()), messageContent.substring(messageContent.indexOf('*') + 1, messageContent.indexOf('~')));
                handleSendCommand(incomingMessage.getString("receiver"), incomingMessage.getString("code"));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

//            } catch (StringIndexOutOfBoundsException ex) {
//                mainForm.append(ex.getMessage() + " " + "message being sent to " + messageContent);
//            }
            display.setCurrent(menuList());
        } else if (command == NO) {
            System.out.println("switching displayable");
            //   display.setCurrent(getIndicator(offlineList()));
            //  rwr = new ReadWriteRMS(this);
            // rwr.openRecStore();
            // rwr.writeRecord(incomingMessage);
            display.setCurrent(menuList());
            // System.out.println("The vector is here " + vec);
            // switchDisplay(null, buyList());
            System.out.println("done");
        } else if (command == NAH) {
            System.out.println("displayable1");
            display.setCurrent(menuList());
        } else if (command == CONTINUE) {
            display.setCurrent(menuList());

        } else if (command == UNREGISTER_CONNECTION_COMMAND) {
            unregisterSMSConnection();
        } else if (command == REGISTER_TIMER_ALARM_COMMAND) {
            //registering timer alarm. Alarm will start MIDlet throught
            //30 seconds
            registerTimerAlarm(5000);
        } else if (command == CANCEL) {
            display.setCurrent(menuList());
        } else if (command == buySong) {
            System.out.println("switching displayable");

//            switchDisplay(null, buyList());
            setConnection();
//            switchDisplay(null, buyList());
            System.out.println("done");
        } // if (displayable == list) {
        else if (command == List.SELECT_COMMAND) {
            System.out.println("the selected index is " + list.getSelectedIndex());
            display.setCurrent(buyForm());
            // }
        } else if (displayable == form) {
            if (command == back) {
                display.setCurrent(buyList());
            }

        } else if (displayable == mList) {
            if (command == EXIT_COMMAND) {
                exitMidlet();
            }
        }
    }

    public void exitMidlet() {
        try {
            //switchDisplayable(null, null);
            destroyApp(true);
        } catch (MIDletStateChangeException ex) {
            ex.printStackTrace();
        }
        notifyDestroyed();
    }

    public Form buyForm() {
        try {
            int j = list.getSelectedIndex();
            // for (int i = 0; i < vector.size(); i++) {
            final JSONObject ht = (JSONObject) rwr.jsonarr.getJSONObject(j);
            // Form form;
            form = new Form("");
            StringItem mtnButton = new StringItem("Buy Song", "Buy", Item.BUTTON);
            Command buttonCommand = new Command("", Command.ITEM, 1);
            try {
                mtnButton.setDefaultCommand(buttonCommand);
                mtnButton.setItemCommandListener(new ItemCommandListener() {
                    public void commandAction(Command c, Item item) {
                        try {
                            System.out.println("Button mtn was clicked");
                            handleSendCommand("6580", ht.get(mtnCode).toString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                StringItem gloButton = new StringItem("", "Buy", Item.BUTTON);
                gloButton.setDefaultCommand(buttonCommand);
                gloButton.setItemCommandListener(new ItemCommandListener() {
                    public void commandAction(Command c, Item item) {
                        System.out.println("Button glo was clicked");
                        try {
                            handleSendCommand("6580", ht.get(gloCode).toString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                StringItem airtelButton = new StringItem("", "Buy", Item.BUTTON);
                airtelButton.setDefaultCommand(buttonCommand);
                airtelButton.setItemCommandListener(new ItemCommandListener() {
                    public void commandAction(Command c, Item item) {
                        System.out.println("Button airtel was clicked");
                        try {
                            handleSendCommand("6580", ht.get(airtelCode).toString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                StringItem etisalatButton = new StringItem("", "Buy", Item.BUTTON);
                // Command buttonCommand = new Command("", Command.ITEM, 1);

                etisalatButton.setDefaultCommand(buttonCommand);
                etisalatButton.setItemCommandListener(new ItemCommandListener() {
                    public void commandAction(Command c, Item item) {
                        try {
                            System.out.println("Button etisalat was clicked");
                            handleSendCommand("6580", ht.get(etiCode).toString());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //String code = ht.get

                form.append(list.getString(list.getSelectedIndex()) + " by " + ht.get("artist").toString());
                // form.append((Image)vector.elementAt(j));
                System.out.println("the selected index " + list.getSelectedIndex() + " by " + ht.get("artist").toString());
                form.append("\n");

                //  System.out.println("code is " + ht.get(mtnCode).toString());
                if (!ht.get(mtnCode).equals("")) {
                    form.append(mtnButton);
                    form.append("MTN - " + ht.get(mtnCode).toString());
                }
                if (!ht.get(gloCode).equals("")) {
                    form.append(gloButton);
                    form.append("GLO - " + ht.get(gloCode).toString());
                }
                if (!ht.get(airtelCode).equals("")) {
                    form.append(airtelButton);
                    form.append("AIRTEL - " + ht.get(airtelCode).toString());
                }
                if (!ht.get(etiCode).equals("")) {
                    form.append(etisalatButton);
                    form.append("ETISALAT - " + ht.get(etiCode).toString());
                }
                form.addCommand(back);
                //  form.addCommand(buy);
                form.setCommandListener(this);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return form;
    }

    public Alert errorAlert() {
        //#style infoAlert
        al = new Alert("Oops");
        // al.setImage(getLogo("/icon1.png"));
        al.setString("Check your internet connection");
        //  al.setTimeout(5000);
        al.setType(AlertType.ERROR);
        al.addCommand(EXIT_COMMAND);
        al.setCommandListener(this);
        return al;
    }

    private Alert successfulReg() {
        //#style infoAlert
        sAlert = new Alert("");
        sAlert.setString("You have successfully unregistered");
        sAlert.addCommand(CONTINUE);
        sAlert.setCommandListener(this);
        return sAlert;
    }

    public Alert alertAlert(String message, boolean addCommand) {
        //#style infoAlert
        alert = new Alert("New Message");

        // alert.setImage(getLogo("/icon1.png"));
        // alert.setType(AlertType.INFO);
        // alert.setTimeout(300);
        alert.setString(message);
        if (addCommand) {
            alert.addCommand(YES);
            alert.addCommand(NO);
        } else {
            alert.addCommand(CANCEL);
        }
        // alert.addCommand(CANCEL);
        alert.setCommandListener(this);
        return alert;
    }

    public Alert registerAlert() {
        //#style infoAlert
        rAlert = new Alert("");
        rAlert.setString("Register for updates for this app?");
        rAlert.addCommand(REGISTER_CONNECTION_COMMAND);
        rAlert.addCommand(NAH);
        rAlert.setCommandListener(this);
        return rAlert;
    }

    public void selectAction() {
        switch (mList.getSelectedIndex()) {
            case 0:
                jd = new JSONArray();
                //  System.out.println("setting connnection");
                // setConnection();
                //if (list != null) {
                readWriteRMS();
                rwr = new ReadWriteRMS(this);
                System.out.println("herex");
                rwr.openRecStore();
                System.out.println("herexxx");
                System.out.println("and that is how I got here " + rwr.jsonarr);

                System.out.println("switching to buylist..........................");
                jd = rwr.readRecords();
                display.setCurrent(buyList());

                //System.out.println("the vector contains " + rwr.vector.toString());
                // } else {
                //    display.setCurrent(getIndicator());
                // }
                //  display.setCurrent(buyList());
                break;
        }
    }

//    public List offlineList() {
//        if ((oList == null) || (oList.size() <= 0)) {
//            oList = new List("Songs", List.IMPLICIT);
//            try {
//                //incomingMessage=new JSONArray(messageContent);
//                jb = new JSONArray(incomingMessage.getString(1));
//                // System.out.println("the array is " + jb);
//                vectors = new Vector();
//                jc = new JSONObject();
//                for (int i = 0; i < jb.length(); i++) {
//                    vectors.addElement(jb.getJSONObject(i));
//
//                }
//                for (int i = 0; i < vectors.size(); i++) {
//                    jc = (JSONObject) vectors.elementAt(i);
//                    System.out.println("jc contains " + jc);
//                    System.out.println(jc.get("tune_name").toString() + " appended");
//                    oList.append(jc.get("tune_name").toString(), null);
//                }
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//            oList.addCommand(EXIT_COMMAND);
//            oList.setCommandListener(this);
//        }
//        return oList;
//    }
    public List menuList() {
        try {
            if (mList == null) {
                //#style menulist
                mList = new List("Services", List.IMPLICIT);
                mList.append("CRBTs", null);
                mList.append("SUBSCRIPTION", null);
                //mList.addCommand(UNREGISTER_CONNECTION_COMMAND);
                mList.addCommand(EXIT_COMMAND);
                mList.setCommandListener(this);
            } else {
                System.out.println("list is not null");
            }

        } catch (Exception e) {
            System.out.println("command exception" + e.getMessage());
        }
        return mList;

    }
//

//    public List extraList() {
//        mList = new List("Services", List.IMPLICIT);
//        mList.append("Hello", null);
//       // mList.addCommand(UNREGISTER_CONNECTION_COMMAND);
//        mList.addCommand(EXIT_COMMAND);
//        mList.setCommandListener(this);
//        return mList;
//    }
    public List buyList() {
        //  rwr = new ReadWriteRMS(this);
        System.out.println("vector is ");
        if ((list == null) || (list.size() <= 0)) {
            list = new List("Songs", List.IMPLICIT);
            for (int i = 0; i < jd.length(); i++) {
                try {
                    JSONObject ht = (JSONObject) jd.getJSONObject(i);
                    // System.out.println("vector is " + ht);
                    String tune_name = ht.get("tune_name").toString();
                    list.append(tune_name, null);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("list not null");
        }
        list.addCommand(back);
        // list.addCommand(buy);
        //list.addCommand(UNREGISTER_CONNECTION_COMMAND);
        list.setCommandListener(this);
        return list;
    }

    public void setConnection() {
        HttpConnection connection = null;
        InputStream is;
        // OutputStream os;

        try {
            connection = (HttpConnection) Connector.open("http://www.mobile-hook.com/api/callertune_list.php?start=0");
            connection.setRequestMethod(HttpConnection.GET);
            connection.setRequestProperty("Content-type", "application/json ");


            //     getIndicator();
            int resp = connection.getResponseCode();
            System.out.println(resp);
            if (String.valueOf(resp) != null && resp == HttpConnection.HTTP_OK) {
                System.out.println("successful");
                StringBuffer sb = new StringBuffer();
                // os = connection.openOutputStream();
                is = connection.openDataInputStream();
                int chr;
                while ((chr = is.read()) != -1) {
                    sb.append((char) chr);
                }
                try {
                    vec = new Vector();
                    JSONArray json = new JSONArray(sb.toString());
                    // InputStreamReader in = new InputStreamReader(connection.openDataInputStream());
                    // System.out.println("Input Stream : " + json);
                    for (int i = 0; i < json.length(); i++) {
                        vec.addElement(json.getJSONObject(i));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else if (String.valueOf(resp) == null || resp != HttpConnection.HTTP_OK) {
                System.out.println("check your internet connection");
                dismissShowAlert(display.getCurrent(), getMenuCanvas());
//                display.setCurrent(getMenuCanvas());
//                display.setCurrent(errorAlert());
            }
        } catch (Exception ex) {
            // display.setCurrent(getMenuCanvas());
            // display.setCurrent(errorAlert());
            //   dm = new displayManager(this);
            System.out.println("*******error*******");
//            dm.setCurrent(errorAlert());
            dismissShowAlert(display.getCurrent(), getMenuCanvas());
            System.out.println("Alert displayed");
            System.out.println("catching IOexception " + ex.toString());
            System.out.println("check your internet connection1");

//             display.setCurrent(getMenuCanvas());
//             display.setCurrent(errorAlert());
            // System.out.println("Alert displayed");
            //  mainForm.append("Check your network connection");
        }

    }

    /**
     * From MessageListener.
     */
    public void notifyIncomingMessage(final MessageConnection conn) {

        // Because this method is called by the platform, it is good practice to
        // minimize the processing done here, on the system thread. Therefore,
        // let's create a new thread for reading the message. +

        //MessageConnection newCon;
        System.out.println("SMSTriggered? : " + SMSTriggerred);
        //  SMSTriggerred=false;
        //   if(SMSTriggerred){
        Thread smsThread = new Thread() {
            public void run() {
                //  SMSTriggerred = true;
                TextMessage textMessage = null;

                //  mainForm.append("Message received");
                Message messages;
                try {
                    /// mainForm.deleteAll();
                    messages = (Message) conn.receive();
                    try {
                        textMessage = (TextMessage) messages;
                    } catch (NullPointerException ex) {
                        //=       mainForm.append("the exception message reads thus " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    mainForm.append("Fifth " + ex.getMessage());
                } catch (Exception e) {
                    mainForm.append("First " + e.getMessage());
                }

                try {
                    messageContent = textMessage.getPayloadText();
                    //   mainForm.append(messageContent);
                    try {
                        incomingMessage = new JSONObject(messageContent);
                        /// j = new JSONObject(messageContent);
                        //   j = new JSONObject(incomingMessage.getString(0));
                    } catch (JSONException ex) {
                        mainForm.append(ex.toString());
                    }
                    //      mainForm.deleteAll();
                    if (messageContent != null) {
                        //    mainForm.deleteAll();
                        try {
//                             

                            display.setCurrent(alertAlert(incomingMessage.get("message").toString(), true));
                        } catch (JSONException ex) {
                            // alert.setString("Nothing found");
                            display.setCurrent(alertAlert("Oops! Something went wrong", false));
                        }

                    } else {
                        //      mainForm.deleteAll();
                        display.setCurrent(alertAlert("Oops! Something went wrong", false));
                        // mainForm.deleteAll();
                        //mainForm.append("Subscription failed");
                    }
                    //mainForm.append("Message: " + messageContent.substring(0, messageContent.indexOf('*')) + "\n");
                } catch (NullPointerException e) {
                    // mainForm.append("Third " + e.getMessage())
                    mainForm.append(e.toString());
                }
                //    mainForm.addCommand(OK);

            }
        };
        smsThread.start();

    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
        // TODO Auto-generated method stub
    }

    protected void startApp() throws MIDletStateChangeException {
        //  Runtime runtime = Runtime.getRuntime();
        long before, after;
        // System.gc();
        //before = runtime.freeMemory();
        before = System.currentTimeMillis();
        display = Display.getDisplay(this);
        mainForm = new Form("Mobile Hook");
        getPushRegistryConnections();
        // notifyIncomingMessage(connection);
//        if (connected ) {
//            
//            notifyIncomingMessage(conn);
////            System.out.println("A connection is present " + connections[0]);
////            display.setCurrent(getSplashCanvas());
//            
//        } else {
//
//            // registerAlert();
//            System.out.println("A connection is absent");
//            display.setCurrent(registerAlert());
//            mainForm.addCommand(EXIT_COMMAND);
//            mainForm.addCommand(REGISTER_CONNECTION_COMMAND);
//            mainForm.addCommand(buySong);
//            mainForm.addCommand(OK);
//            mainForm.setCommandListener(this);
//        }
        after = System.currentTimeMillis();
        System.out.println("before = " + before + " after = " + after + " and the time taken is " + (after - before));
    }

//    private void printString(String message) {
//        mainForm.append(message + "\n");
//    }
    private void handleSendCommand(final String smsNumber, final String smsText) {
        // Send the message on its own thread of execution
        Thread smsThread = new Thread() {
            public void run() {
                try {
                    String number;

                    //Obtain the destination address
                    number = "sms://" + smsNumber + ":5000";

                    // Obtain the specified text
                    String text = smsText;
                    connection = (MessageConnection) Connector.open(number);
                    message = (TextMessage) connection.newMessage(MessageConnection.TEXT_MESSAGE);
                    message.setPayloadText(text);
                    connection.send(message);
                    // messageSendingStatus();
                    //  mainForm.append("Message sent!\n");
                } catch (InterruptedIOException ex) {
                    //mainForm.append("Interrupted Exception!\n");
                } catch (IOException ex) {
                    //mainForm.append("IOException!\n");
                } catch (IllegalArgumentException ex) {
                    //mainForm.append("Illegal Argument!\n");
                } catch (SecurityException ex) {
                    //mainForm.append("Security Exception!\n");
                }
            }
        };
        smsThread.start();
    }

    public Display getDisplay() {
        return Display.getDisplay(this);
    }
}