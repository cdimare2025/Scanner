package com.studiodomino.scanner.twain;

// JTwainDemo.java

import java.awt.*;
import java.awt.event.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 *  This class defines the GUI for the JTwainDemo application.
 *
 *  @author Jeff Friesen
 */

public class JTwainDemo extends JFrame
{
   /**
    *  Each acquired image is displayed in an ImageArea panel.
    */

   ImageArea ia = new ImageArea ();

   /**
    *  To support the display of images that can't be fully displayed without
    *  scrolling, the ImageArea panel is placed in a JScrollPane.
    */

   JScrollPane jsp;

   /**
    *  Construct JTwainDemo's GUI and start event-handling thread.
    *
    *  @param title text to display in JTwainDemo's title bar
    */

   /**
    *  Name of selected data source.
    */

   String srcName;

   public JTwainDemo (String title)
   {
      // Place title in the title bar of JTwainDemo's main window.

      super (title);

      // Exit the application if user selects Close from system menu.

      setDefaultCloseOperation (EXIT_ON_CLOSE);

      // Initialize srcName to the name of the default data source.

      try
      {
          JTwain.openDSM ();

          try
          {
              srcName = JTwain.getDefaultDS ();
          }
          catch (JTwainException e)
          {
          }

          JTwain.closeDSM ();
      }
      catch (JTwainException e)
      {
      }

      // Create the application's File menu.

      JMenu menu = new JMenu ("File");

      ActionListener al;

      // Build the Acquire... menu item.

      JMenuItem mi = new JMenuItem ("Acquire...");
      al = new ActionListener ()
           {
               public void actionPerformed (ActionEvent e)
               {
                  try
                  {
                      // Create and show config source dialog to the user.
        
                      ConfigSource cs = new ConfigSource (JTwainDemo.this,
                                                          srcName);
                      cs.setVisible (true);

                      // Return if dialog box's Cancel button was clicked.

                      if (cs.isCanceled ())
                          return;

                      // Attempt to open data source manager.

                      JTwain.openDSM ();

                      try
                      {
                          // Attempt to open recently-selected data source.

                          JTwain.openDS (srcName);

                          try
                          {
                              // Set any selected pixel type.

                              JTwain.setPixelType (cs.getPixType ());

                              // Acquire one or more images. Only one image
                              // should be returned because no capabilities
                              // involving an automatic document feeder have
                              // been set.

                              Image [] im = JTwain.acquire ();

                              // Update ImageArea panel with the first new
                              // image, and adjust the scrollbars.
                              if (im!= null) {
                              ia.setImage (im [0]);

                              jsp.getHorizontalScrollBar ().setValue (0);
                              jsp.getVerticalScrollBar ().setValue (0);
                              try {
                              OutputStream out=new FileOutputStream("c:\\tmp\\scan.pdf");
                              out=ImageToPdf.ImmagineAPdf(im, out);
                              out.flush();
                              out.close();
                              } catch (Exception ex) {}
                          }
                          }
                          catch (JTwainException e2)
                          {
                              JOptionPane.showMessageDialog (JTwainDemo.this,
                                                             e2.getMessage ());
                          }
                          catch (UnsupportedCapabilityException e2)
                          {
                              JOptionPane.showMessageDialog (JTwainDemo.this,
                                                             e2.getMessage ()
                                                             + " unsupported");
                          }

                          JTwain.closeDS ();
                          
                      }
                      catch (JTwainException e2)
                      {
                          JOptionPane.showMessageDialog (JTwainDemo.this,
                                                         e2.getMessage ());
                      }

                      JTwain.closeDSM ();
                  }
                  catch (JTwainException e2)
                  {
                      JOptionPane.showMessageDialog (JTwainDemo.this,
                                                     e2.getMessage ());
                  }
               }
           };

      mi.addActionListener (al);
      menu.add (mi);

      // Build the Select Source... menu item.

      mi = new JMenuItem ("Select Source...");
      al = new ActionListener ()
           {
               public void actionPerformed (ActionEvent e)
               {
                  // Create and show select source dialog.

                  SelectSource ss = new SelectSource (JTwainDemo.this,
                                                      srcName);
                  ss.setVisible (true);

                  // Return if dialog box's Cancel button was clicked.

                  if (ss.isCanceled ())
                      return;

                  // Retrieve selected source name from dialog box.

                  srcName = ss.getSrcName ();
               }
           };

      mi.addActionListener (al);
      menu.add (mi);

      menu.addSeparator ();

      // Build the Exit menu item.

      mi = new JMenuItem ("Exit");
      mi.addActionListener (new ActionListener ()
                            {
                                public void actionPerformed (ActionEvent e)
                                {
                                   dispose ();
                                }
                            });
      menu.add (mi);

      // Create the main window's menu bar and add the file menu to that menu
      // bar.

      JMenuBar mb = new JMenuBar ();
      mb.add (menu);
      setJMenuBar (mb);

      // Place the ImageArea panel component inside a JScrollPane and add the
      // JScrollPane to the main window's content pane.

      getContentPane ().add (jsp = new JScrollPane (ia));

      // Pack all components to their preferred sizes.

      pack ();

      // Display the GUI and start the event-handling thread.

      setVisible (true);
   }

   /**
    *  Application entry point.
    *
    *  @param args array of command-line arguments.
    */

   public static void main (String [] args)
   {
      // Initialize JTwain.
     
      if (!JTwain.init ())
      {
          System.out.println ("JTwainDemo: TWAIN not supported");
          return;
      }

      // Construct the GUI and begin the event-handling thread.

      new JTwainDemo ("JTwain Demo");
   }
}
/**
 *  This class creates the ConfigSource dialog box.
 */

class ConfigSource extends JDialog
{
   /**
    *  Construct ConfigSource dialog box.
    *
    *  @param f the parent JFrame-based window's reference
    *  @param srcName the name of the data source to be configured
    */

   ConfigSource (JFrame f, String srcName)
   {
      // Assign title to dialog box's title bar and ensure dialog box is
      // modal.

      super (f, "Config Source", true);

      // Obtain list of pixel types and current pixel type.

      try
      {
          JTwain.openDSM ();

          try
          {
              JTwain.openDS (srcName);

              try
              {
                  pixTypes = JTwain.getPixelTypes ();
                  pixType = JTwain.getPixelType ();
              }
              catch (JTwainException e)
              {
              }
              catch (UnsupportedCapabilityException e)
              {
              }

              JTwain.closeDS ();
          }
          catch (JTwainException e)
          {
          }

          JTwain.closeDSM ();
      }
      catch (JTwainException e)
      {
      }

      // Build top section of GUI if there are pixel types to display.

      if (pixTypes != null)
      {
          // Create layout panel for top section of dialog box's GUI.

          JPanel p = new JPanel ();

          p.add (new JLabel ("Pixel Types"));

          // Convert pixTypes integers into an array of Strings.

          String [] sPixTypes = new String [pixTypes.length];

          for (int i = 0; i < pixTypes.length; i++)
               switch (pixTypes [i])
               {
                  case JTwain.PT_BW:
                       sPixTypes [i] = "Black & White";
                       break;

                  case JTwain.PT_GRAY:
                       sPixTypes [i] = "Grayscale";
                       break;

                  case JTwain.PT_RGB:
                       sPixTypes [i] = "RGB";
                       break;


                  case JTwain.PT_PALETTE:
                       sPixTypes [i] = "Palette";
                       break;

                  case JTwain.PT_CMY:
                       sPixTypes [i] = "CMY";
                       break;

                  case JTwain.PT_CMYK:
                       sPixTypes [i] = "CMYK";
                       break;

                  case JTwain.PT_YUV:
                       sPixTypes [i] = "YUV";
                       break;

                  case JTwain.PT_YUVK:
                       sPixTypes [i] = "YUVK";
                       break;

                  case JTwain.PT_CIEXYZ:
                       sPixTypes [i] = "CIEXYZ";
               }

          // Create a list of pixel type names and ensure that only one name
          // can be selected at a time.

          JList list = new JList (sPixTypes);

          list.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);

          // Add a listener to respond to list selection events by saving the
          // selected pixel type.

          ListSelectionListener lsl;
          lsl = new ListSelectionListener ()
                {
                    public void valueChanged (ListSelectionEvent e)
                    {
                       JList list = (JList) e.getSource ();
                       pixType = pixTypes [list.getSelectedIndex ()];
                    }
                };
          list.addListSelectionListener (lsl);

          // Select the default entry.

          switch (pixType)
          {
             case JTwain.PT_BW:
                  list.setSelectedValue ("Black & White", true);
                  break;

             case JTwain.PT_GRAY:
                  list.setSelectedValue ("Grayscale", true);
                  break;

             case JTwain.PT_RGB:
                  list.setSelectedValue ("RGB", true);
                  break;

             case JTwain.PT_PALETTE:
                  list.setSelectedValue ("Palette", true);
                  break;

             case JTwain.PT_CMY:
                  list.setSelectedValue ("CMY", true);
                  break;

             case JTwain.PT_CMYK:
                  list.setSelectedValue ("CMYK", true);
                  break;

             case JTwain.PT_YUV:
                  list.setSelectedValue ("YUV", true);
                  break;

             case JTwain.PT_YUVK:
                  list.setSelectedValue ("YUVK", true);
                  break;
                                                 
             case JTwain.PT_CIEXYZ:
                  list.setSelectedValue ("CIEXYZ", true);
          }

          // Add list (inside scroll pane) to top section layout panel.

          p.add (new JScrollPane (list));

          // Add top section layout panel to North area of main window's
          // content pane.

          getContentPane ().add (p, BorderLayout.NORTH);
      }

      // Create layout panel for bottom section of dialog box's GUI. Change
      // default layout manager to a flow that right-justifies its components.

      JPanel p = new JPanel ();
      p.setLayout (new FlowLayout (FlowLayout.RIGHT));

      // Create and add Ok button to bottom section layout panel.

      JButton b = new JButton ("Ok");
      p.add (b);

      // Make button the default, so that pressing Enter causes the Ok button
      // to fire an action event.

      getRootPane ().setDefaultButton (b);

      // Add action listener to respond to Ok button action events. In 
      // response to those events, listener clears the canceled status (to
      // indicate that the Cancel button is not clicked), and disposes of the
      // dialog box -- returning GUI control to its caller.

      b.addActionListener (new ActionListener ()
                           {
                               public void actionPerformed (ActionEvent e)
                               {
                                  canceled = false;

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
    *  Return the selected pixel type.
    *
    *  @return selected pixel type
    */

   int getPixType ()
   {
      return pixType;
   }

   /**
    *  Return the canceled status. Boolean true returns if the user clicked
    *  the Cancel button.
    *
    *  @return canceled status
    */

   boolean isCanceled ()
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
    *  pixType contains the PT_ value chosen by the user.
    */

   private int pixType;

   /**
    *  pixTypes contains an array of supported PT_ values.
    */

   private int [] pixTypes;

   /**
    *  canceled contains this dialog box's canceled status.
    */

   private boolean canceled;
}

/**
 *  This class creates the SelectSource dialog box.
 */

