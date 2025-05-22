// JTwain.java

package com.studiodomino.scanner.twain;

import java.awt.Image;

/**
 *  This class provides the "glue" to connect the Java side of JTWAIN to the
 *  C++ side. Methods exist to initialize JTwain and interact with TWAIN.
 *
 *  @author Jeff Friesen
 */

public class JTwain
{
   // Return codes.

   public final static int RC_SUCCESS = 0;
   public final static int RC_FAILURE = 1;
   public final static int RC_CHECKSTATUS = 2;
   public final static int RC_CANCEL = 3;
   public final static int RC_DSEVENT = 4;
   public final static int RC_NOTDSEVENT = 5;
   public final static int RC_XFERDONE = 6;
   public final static int RC_ENDOFLIST = 7;
   public final static int RC_INFONOTSUPPORTED = 8;
   public final static int RC_DATANOTAVAILABLE = 9;

   // Condition codes. Check condition code if return code is RC_FAILURE.

   /**
    *  No failure.
    */

   public final static int CC_SUCCESS = 0;

   /**
    *  Failure due to unknown causes.
    */

   public final static int CC_BUMMER = 1;

   /**
    *  Low memory conditions.
    */

   public final static int CC_LOWMEMORY = 2;

   /**
    *  No data source.
    */

   public final static int CC_NODS = 3;

   /**
    *  Data source connected to maximum possible applications.
    */

   public final static int CC_MAXCONNECTIONS = 4;

   /**
    *  DS/DSM reported error. Application should not report error.
    */

   public final static int CC_OPERATIONERROR = 5;

   /**
    *  Unknown capability.
    */

   public final static int CC_BADCAP = 6;

   /**
    *  Unrecognized MSG DG DAT combination.
    */

   public final static int CC_BADPROTOCOL = 9;

   /**
    *  Data parameter out of range.
    */

   public final static int CC_BADVALUE = 10;

   /**
    *  DG DAT MSG out of expected sequence.
    */

   public final static int CC_SEQERROR = 11;

   /**
    *  Unknown destination Application/Source in DSM_Entry().
    */

   public final static int CC_BADDEST = 12;

   /**
    *  Capability not supported by data source.
    */

   public final static int CC_CAPUNSUPPORTED = 13;

   /**
    *  Operation not supported by capability.
    */

   public final static int CC_CAPBADOPERATION = 14;

   /**
    *  Capability has dependency on other capability.
    */

   public final static int CC_CAPSEQERROR = 15;

   /**
    *  File system operation is denied (file is protected).
    */

   public final static int CC_DENIED = 16;

   /**
    *  Operation failed because file already exists.
    */

   public final static int CC_FILEEXISTS = 17;

   /**
    *  File not found.
    */

   public final static int CC_FILENOTFOUND = 18;

   /**
    *  Operation failed because directory is not empty.
    */

   public final static int CC_NOTEMPTY = 19;

   /**
    *  The feeder is jammed.
    */

   public final static int CC_PAPERJAM = 20;

   /**
    *  The feeder detected multiple pages.
    */

   public final static int CC_PAPERDOUBLEFEED = 21;

   /**
    *  Error writing the file (disk full or similar conditions).
    */

   public final static int CC_FILEWRITEERROR = 22;

   /**
    *  The device went offline prior to or during this operation.
    */

   public final static int CC_CHECKDEVICEONLINE = 23;

   // ICAP_PIXELTYPE capability constants

   public final static int PT_BW = 0;
   public final static int PT_GRAY = 1;
   public final static int PT_RGB = 2;
   public final static int PT_PALETTE = 3;
   public final static int PT_CMY = 4;
   public final static int PT_CMYK = 5;
   public final static int PT_YUV = 6;
   public final static int PT_YUVK = 7;
   public final static int PT_CIEXYZ = 8;

   /**
    *  Initialize JTwain. Initialization succeeds if System.loadLibrary() is
    *  able to find the jtwain library, if the jtwain library is able to find
    *  TWAIN_32.DLL, and if TWAIN_32.DLL contains the DSM_Entry() function. A
    *  messagebox is displayed if either TWAIN_32.DLL or DSM_Entry() can't be
    *  found.
    *
    *  IMPORTANT: This method must be called before any other method. If this
    *  method returns false, do NOT call any other method.
    *
    *  @return true if JTwain successfully initialized, otherwise false
    */

   public static boolean init ()
   {
      try
      {
          JarLib.load(JTwain.class,"jtwain");
          return true;
      }
      catch (UnsatisfiedLinkError e)
      { 
          return false;
      }
   }

   /**
    *  Acquire an array of images from the currently open data source. The
    *  exact number of acquired images can be determined from the array's
    *  length.
    *
    *  @return Image array describing acquired image(s)
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native Image [] acquire ()
      throws JTwainException;

   /**
    *  Close the currently open data source.
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native void closeDS () throws JTwainException;

   /**
    *  Close the data source manager.
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native void closeDSM () throws JTwainException;

   /**
    *  Return the condition code from the most recent operation.
    *
    *  @param dest zero if data source manager, nonzero if data source
    *
    *  @return most recent operation's condition code
    */

   public static native int getCC (int dest) throws JTwainException;

   /**
    *  Return the name of the default data source.
    *
    *  @return default source name
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native String getDefaultDS () throws JTwainException;

   /**
    *  Return the name of the first data source.
    *
    *  @return first data source's name
    *
    *  @throws JTwainException if something goes wrong (e.g., no data sources)
    */

   public static native String getFirstDS () throws JTwainException;

   /**
    *  Return the name of the next data source.
    *
    *  @return next data source's name, or "" if no more data source names
    *
    *  @throws JTwainException if something goes wrong (e.g., low memory)
    */

   public static native String getNextDS () throws JTwainException;

   /**
    *  Return the currently open data source's current pixel type.
    *
    *  @return current pixel type (see the PT_ constants above)
    *
    *  @throws JTwainException if something goes wrong
    *  @throws UnsupportedCapabilityException if capability not supported
    */

   public static native int getPixelType ()
      throws JTwainException, UnsupportedCapabilityException;

   /**
    *  Return the currently open data source's supported pixel types.
    *
    *  @return array of supported pixel types (see PT_ constants above)
    *
    *  @throws JTwainException if something goes wrong
    *  @throws UnsupportedCapabilityException if capability not supported
    */

   public static native int [] getPixelTypes ()
      throws JTwainException, UnsupportedCapabilityException;

   /**
    *  Return the return code from the most recent operation.
    *
    *  @return most recent operation's return code
    */

   public static native int getRC ();

   /**
    *  Open a specific data source.
    *
    *  @param data source's name
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native void openDS (String srcName) throws JTwainException;

   /**
    *  Open the data source manager.
    *
    *  @throws JTwainException if something goes wrong
    */

   public static native void openDSM () throws JTwainException;

   /**
    *  Set the currently open data source's current pixel type.
    *
    *  @param type one of the PT_ (pixel type) constants declared earlier
    *
    *  @throws JTwainException if something goes wrong
    *  @throws UnsupportedCapabilityException if capability not supported
    */

   public static native void setPixelType (int type)
      throws JTwainException, UnsupportedCapabilityException;
}
