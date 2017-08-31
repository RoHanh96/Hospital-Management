/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chung;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author trieu_000
 */
public class SetCursor {
    public void setCusor(JFrame frame) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("syringe.jpg");
        Point point = new Point(0, 0);
        Cursor c = toolkit.createCustomCursor(image, point, "img");
        frame.setCursor (c);
    }
}
