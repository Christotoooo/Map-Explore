package cn.edu.pku.sei;

import java.awt.*;
import java.awt.event.*;

public class GraphWindowListener implements WindowListener {
@Override
public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

        }

@Override
public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub
        e.getWindow().setVisible(false);
        ((Window) e.getComponent()).dispose();
        MapSelection.openedGraph -= 1;
        }

@Override
public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

        }

@Override
public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

        }

@Override
public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

        }

@Override
public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

        }

@Override
public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

        }
}
