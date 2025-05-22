// JTwainException.java

package com.studiodomino.scanner.twain;

/**
 *  This class defines the exception that is thrown from various places in the
 *  jtwain.dll code when some kind of failure occurs other than an unsupported
 *  capability.
 *
 *  @author Jeff Friesen
 */

public class JTwainException extends Exception
{
   /**
    *  Construct a JTwainException object with the specified message.
    *
    * @param message a message that describes jtwain.dll code failure
    */

   public JTwainException (String message)
   {
      super (message);
   }
}
