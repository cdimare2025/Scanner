/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.studiodomino.scanner.twain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Donato
 */
public class SelectSource extends JDialog
{
   /**
    *  Construct SelectSource dialog box with names of available data sources
    *  in a list.
    *
    *  @param f the parent JFrame-based window's reference
    *  @param defSrcName default data source name
    */

   public SelectSource (JFrame f, String defSrcName)
   {
      // Assign title to dialog box's title bar and ensure dialog box is
      // modal.

      super (f, "Seleziona lo scanner", true);

      // Obtain list of data source names. Store each data source name in
      // srcNames Vector.

      Vector<String> srcNames = new Vector<String> ();

      try
      {
          JTwain.openDSM ();

          try
          {
              srcNames.add (JTwain.getFirstDS ());

              String srcName;
              while (!(srcName = JTwain.getNextDS ()).equals (""))
                 srcNames.add (srcName);
          }
          catch (JTwainException e)
          {
          }

          JTwain.closeDSM ();
      }
      catch (JTwainException e)
      {
      }

      // Create layout panel for top section of dialog box's GUI.

      JPanel p = new JPanel ();

      // Create a list of data source names and ensure that only one name can
      // be selected at a time.

      final JList list = new JList (srcNames);

      list.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);

      // If any entries in Vector, select default item in list.

      if (srcNames.size () != 0)
          list.setSelectedValue (defSrcName, true);

      // Add list (inside scroll pane) to top section layout panel.

      p.add (new JScrollPane (list));

      // Add top section layout panel to North area of main window's content
      // pane.

      getContentPane ().add (p, BorderLayout.NORTH);

      // Create layout panel for bottom section of dialog box's GUI. Change
      // default layout manager to a flow that right-justifies its components.

      p = new JPanel ();
      p.setLayout (new FlowLayout (FlowLayout.RIGHT));

      // Create and add Ok button to bottom section layout panel.

      JButton b = new JButton ("Ok");
      p.add (b);

      // Make button the default, so that pressing Enter causes the Ok button
      // to fire an action event.

      getRootPane ().setDefaultButton (b);

      // Add action listener to respond to Ok button action events. In 
      // response to those events, listener clears the canceled status (to
      // indicate that the Cancel button is not clicked), caches the selected
      // data source name, and disposes of the dialog box -- returning GUI
      // control to its caller.

      b.addActionListener (new ActionListener ()
                           {
                               public void actionPerformed (ActionEvent e)
                               {
                                  canceled = false;

                                  srcName = (String) list.getSelectedValue ();

                                  dispose ();
                               }
                           });

      // Create and add Cancel button to bottom section layout panel.

      b = new JButton ("Cancel");
      p.add (b);

      // Modify dialog box so that pressing Esc appears to invoke the Cancel
      // button's actionPerformed() method.

      addCancelByEscapeKey ();

      // Add action listener to respond to Cancel button action events. In
      // response to those events, listener sets the canceled status (to
      // indicate that the Cancel button is clicked), and disposes of the
      // dialog box -- returning GUI control to its caller.

      b.addActionListener (new ActionListener ()
                           {
                               public void actionPerformed (ActionEvent e)
                               {
                                  canceled = true;

                                  dispose ();
                               }
                           });

      // Add bottom section layout panel to South area of main window's 
      // content pane.

      getContentPane ().add (p, BorderLayout.SOUTH);

      // Resize dialog box to union of collective preferred sizes of all
      // contained components.

      pack ();

      // Center dialog box (when it appears) relative to main window.

      setLocationRelativeTo (f);
   }

   /**
    *  Return the selected data source name. If the Cancel button is clicked,
    *  null returns.
    *
    *  @return selected data source name
    */

   public String getSrcName ()
   {
      return srcName;
   }

   /**
    *  Return the canceled status. Boolean true returns if the user clicked
    *  the Cancel button.
    *
    *  @return canceled status
    */

   public boolean isCanceled ()
   {
      return canceled;
   }

   // ============
   // PRIVATE AREA
   // ============

   /**
    *  Modify the dialog box, so that pressing the Esc key does the same thing
    *  as clicking the Cancel button. This method's actionPerformed() method
    *  must contain the same code as the Cancel button's actionPerformed()
    *  method.
    */

   private void addCancelByEscapeKey ()
   {
      // Map the Esc key to the cancel action description in the dialog box's
      // input map.

      String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";

      int noModifiers = 0;

      KeyStroke escapeKey =
        KeyStroke.getKeyStroke (KeyEvent.VK_ESCAPE, noModifiers, false);

      InputMap inputMap = getRootPane ().getInputMap
        (JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

      inputMap.put (escapeKey, CANCEL_ACTION_KEY);

      // Map the cancel action description to the cancel action in the dialog
      // box's action map.

      AbstractAction cancelAction = new AbstractAction ()
      {
         public void actionPerformed (ActionEvent e)
         {
            canceled = true;

            dispose ();
        }
      };

      getRootPane ().getActionMap ().put (CANCEL_ACTION_KEY, cancelAction);
   }

   /**
    *  srcName references a String containing the selected data source name
    *  (or is null).
    */

   private String srcName;

   /**
    *  canceled contains this dialog box's canceled status.
    */

   private boolean canceled;
}
