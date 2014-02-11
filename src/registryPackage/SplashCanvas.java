/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryPackage;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

/**
 * @author LANREWAJU
 */
public class SplashCanvas extends Canvas implements CommandListener, Runnable {

    /**
     * constructor
     */
    stubMidlet sm;
    Display display;
    List list;
    ReadWriteRMS rwr;
    //   Display display;

    public SplashCanvas(stubMidlet stubmidlet) {
        new Thread(this).start();
        setCommandListener(this);
        sm = stubmidlet;
        setFullScreenMode(true);

        try {
            // Set up this canvas to listen to command events
            setCommandListener(this);
            // Add the Exit command
            addCommand(new Command("Exit", Command.EXIT, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * paint
     */
    public void paint(Graphics g) {
        g.setColor(Colour.WHITE);
        // g.fillRect(0, 0,getWidth() , getHeight());
        //  g.setColor(Colour.DARK_GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());
        //  g.drawString("The Logo should come here", getWidth()/2, getHeight()/2, Graphics.HCENTER | Graphics.BASELINE);
        g.drawImage(createScaledImage(getLogo()), getWidth() / 2, getHeight() / 2 - 5, 3);
    }
    Image logo;

    public Image getLogo() {

        try {

            logo = Image.createImage("/splashscreen.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return logo;
    }

    /**
     * Called when a key is pressed.
     */
    protected void keyPressed(int keyCode) {
    }

    /**
     * Called when a key is released.
     */
    protected void keyReleased(int keyCode) {
    }

    /**
     * Called when a key is repeated (held down).
     */
    protected void keyRepeated(int keyCode) {
    }

    /**
     * Called when the pointer is dragged.
     */
    protected void pointerDragged(int x, int y) {
    }

    /**
     * Called when the pointer is pressed.
     */
    protected void pointerPressed(int x, int y) {
    }

    /**
     * Called when the pointer is released.
     */
    protected void pointerReleased(int x, int y) {
    }

    /**
     * Called when action should be handled
     */
//    public void commandAction(Command command, Displayable displayable) {
//        if(displayable==menuList()){
//            if(command == menuList().SELECT_COMMAND){
//                selectAction();
//            }
//        }
//    }
    public void run() {
        try {
            System.out.println("SMSTriggered : " + sm.SMSTriggerred);
          //  if (!sm.SMSTriggerred) {
                Thread.sleep(5000);
                dismiss();
          //  }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void dismiss() {
        //  for (int i = 0; i < RecordStore.listRecordStores().length; i++) {
//        if ("ReadWriteRMS".equals(RecordStore.listRecordStores()[0])) {
//            sm.menuList();
//        } else {
        rwr = new ReadWriteRMS(sm);
        rwr.openRecStore();
        if (RecordStore.listRecordStores() != null) {
            System.out.println("here");
            if (RecordStore.listRecordStores()[0].equals("ReadWriteRMS")) {
                System.out.println("here2");
                rwr.readRecords();
                if (rwr.jsonarr.length() > 0) {
//                    System.out.println("present is ");
                       display.setCurrent(sm.menuList());
//                    display.se
                } else {
                    sm.getIndicator();
                }
                //  
            }
        } else {
            sm.getIndicator();
        }
        //  }
        //display.setCurrent(sm.menuList());
        //display.setCurrent(sm.getMenuCanvas());
    }

    public ReadWriteRMS readWriteRMS() {
        rwr = new ReadWriteRMS(sm);
        rwr.splashscreen = this;
        return rwr;
    }

    protected Image createScaledImage(Image image) {
        final int sourceWidth = image.getWidth();
        final int sourceHeight = image.getHeight();
        final Image sImg = Image.createImage(getWidth(), getHeight());
        final Graphics g = sImg.getGraphics();
        final int[] lineRGB = new int[sourceWidth];
        final int[] sourcePos = new int[getWidth()];
        int y = 0;
        int eps = -(sourceWidth >> 1);
        for (int x = 0; x < sourceWidth; x++) {
            eps += getWidth();
            if ((eps << 1) >= sourceWidth) {
                if (++y == getWidth()) {
                    break;
                }
                sourcePos[y] = x;
                eps -= sourceWidth;
            }
        }
        for (int z = 0; z < getHeight(); z++) {
            image.getRGB(lineRGB, 0, sourceWidth, 0, z * sourceHeight / getHeight(), sourceWidth, 1);
            for (int x = 1; x < getWidth(); x++) // skip pixel 0 
            {
                lineRGB[x] = lineRGB[sourcePos[x]];
            }
            g.drawRGB(lineRGB, 0, getWidth(), 0, z, getWidth(), 1, false);
        }
        return sImg;
    }

    public void commandAction(Command c, Displayable d) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}