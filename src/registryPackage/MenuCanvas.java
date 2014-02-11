/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryPackage;

import javax.microedition.lcdui.*;

/**
 * @author LANREWAJU
 */
public class MenuCanvas extends Canvas implements CommandListener {
    
    stubMidlet sm;
    Display display;
    int lastX, lastY;
    /**
     * constructor
     */
    int index = 0;
    private final static int RIGHT_SOFT_KEY = -7;
    private final static int LEFT_SOFT_KEY = -6;
    
    public MenuCanvas(stubMidlet stubmidlet) {
//        try {
//            // Set up this canvas to listen to command events
//            setCommandListener(this);
//            // Add the Exit command
//            addCommand(new Command("Exit", Command.EXIT, 1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        
        sm = stubmidlet;
        lastX = 0;
        lastY = 0;
        setFullScreenMode(true);
    }

    /**
     * paint
     */
    public void paint(Graphics g) {
        /* g.setColor(Colour.WHITE);
         g.fillRect(0, 0, getWidth(), getHeight());
         g.setColor(Colour.DARK_GREEN);
         g.fillRect(0, 20, getWidth(), getHeight() - 40);
         g.setColor(0xFFFFFF);
         g.fillRect(0, 75 + (50 * index), getWidth(), 40);
         g.setColor(0x660066);
         // g.drawString("CRBTs", getWidth() / 2, 100, Graphics.HCENTER | Graphics.BASELINE);
         //  g.drawString("SUBSCRIPTION", getWidth() / 2, 150, Graphics.HCENTER | Graphics.BASELINE);
         //  g.drawString("", getWidth()/2, 200,Graphics.HCENTER|Graphics.BASELINE);
         g.setColor(Colour.DARK_GREEN);
         // g.drawString("Select", 35, getHeight() - 5, Graphics.HCENTER | Graphics.BASELINE);
         g.drawString("Exit", getWidth() - 35, getHeight() - 5, Graphics.HCENTER | Graphics.BASELINE);*/
        g.setColor(Colour.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        display.setCurrent(sm.errorAlert());
    }
    
    public void select() {
        switch (index) {
            case 0:
                //sm.switchDisplay(null, sm.buyList());
                System.out.println("CRBTs clicked");
                display.setCurrent(sm.getIndicator());
                sm.setConnection();
                System.out.println("The vector is here " + sm.vec);
                if (sm.vec.isEmpty()) {
                    System.out.println("Check your internet connection");
                    display.setCurrent(alertAlert());

                    // sm.alert.setString("Check your internet connection");
                    // display.setCurrent(sm.alertAlert());
                } else {
                    // display.setCurrent(sm.buyList());
                    //sm.switchDisplay(null, sm.buyList());
                }
                break;
//            case 1:
//                castlemidlet.switchDisplayable(null, castlemidlet.getCastleForm());
//                break;
//            case 3 :
//                castlemidlet.switchDisplayable(null, castlemidlet.getAboutForm());
//                break;
        }
    }

    /**
     * Called when a key is pressed.
     */
    protected void keyPressed(int keyCode) {
        int k = getGameAction(keyCode);
        if (k == UP) {
            if (index == 0) {
                index = 1;
            } else {
                index = index - 1;
            }
            
        } else if (k == DOWN) {
            if (index == 1) {
                index = 0;
            } else {
                index = index + 1;
            }
            
        } else if (keyCode == RIGHT_SOFT_KEY) {
            // castlemidlet.exitMidlet();
            //s//m.destroyApp(true);
        } else if (keyCode == LEFT_SOFT_KEY || k == FIRE) {
            select();
        }
        repaint();
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
        lastX = x;
        lastY = y;
        System.out.println("The last point is " + lastX + " : " + x + " and " + lastY + " : " + y);
        if (y > 75 && y < 125) {
            index = 0;
            repaint();
            System.out.println("CRBTs clicked");
            
            sm.setConnection();
            //    display.setCurrent(sm.getIndicator());
            System.out.println("The vector is here " + sm.vec);
            if (sm.vec.isEmpty()) {
                System.out.println("Check your internet connection");
                display.setCurrent(alertAlert());

                // sm.alert.setString("Check your internet connection");
                // display.setCurrent(sm.alertAlert());
            } else {
                // display.setCurrent(sm.getIndicator());
                //   display.setCurrent(sm.getListRenderer());
//                sm.switchDisplay(null, sm.buyList());
            }
        } else if (y > 125 && y < 175) {
            index = 1;
            System.out.println("Subscription clicked");
            repaint();
        }
    }
    
    public Alert alertAlert() {
        //#style infoAlert
        Alert alert = new Alert("Check your internet connection");
        alert.setType(AlertType.WARNING);
        alert.setTimeout(5000);
        alert.addCommand(Alert.DISMISS_COMMAND);
        return alert;
    }

    /**
     * Called when the pointer is released.
     */
    protected void pointerReleased(int x, int y) {
    }

    /**
     * Called when action should be handled
     */
    public void commandAction(Command command, Displayable displayable) {
    }
}