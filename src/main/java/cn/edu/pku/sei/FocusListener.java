package cn.edu.pku.sei;


import cn.edu.pku.sei.mapStructures.Coordinate;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class FocusListener implements WindowFocusListener{

    @Override
    public void windowGainedFocus(WindowEvent e) {
        // TODO Auto-generated method stub
        String focusWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow().getName();
        Coordinate.changeGeometry(focusWindow);
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        // TODO Auto-generated method stub
    }



}
