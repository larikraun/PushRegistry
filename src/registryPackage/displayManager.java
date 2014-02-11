/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryPackage;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 *
 * @author LANREWAJU
 */
public final class displayManager {

    public Display display;
    private Displayable displayable;
    public stubMidlet stubmidlet;
//   private static class BlackCanvas extends Canvas {
//      protected void paint(Graphics g) {
//         // Paint canvas to black to reduce flicker.
//         // Note: You may decide to change this to suite your needs.
//         g.setColor(0x000000);
//         g.fillRect(0, 0, getWidth(), getHeight());
//
//         // Now show the alert that we really wanted!
//         DisplayManager.setCurrent(DisplayManager.getCurrent());
//      }

    public displayManager(stubMidlet sm) {
        sm = stubmidlet;
    }

    public void setCurrent(Displayable d) {
        displayable = d;
        if (displayable instanceof javax.microedition.lcdui.Alert && display.getCurrent() instanceof javax.microedition.lcdui.Alert) {
            System.out.println("True");
            display.setCurrent(stubmidlet.getMenuCanvas());
        } else {
            display.setCurrent(displayable);
            System.out.println("false");
        }

    }
}
