package com.wisely.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;



/**
 * The base class of all exceptions which can contain other exceptions.
 *
 * It is intended to ease the debugging by carrying on the information
 * about the exception which was caught and provoked throwing the
 * current exception. Catching and rethrowing may occur multiple
 * times, and provided that all exceptions except the first one
 * are descendands of <code>NestedException</code>, when the
 * exception is finally printed out using any of the <code>
 * printStackTrace()</code> methods, the stacktrace will contain
 * the information about all exceptions thrown and caught on
 * the way.
 * <p> Running the following program
 * <p><blockquote><pre>
 *  1 import org.apache.commons.NestedException;
 *  2
 *  3 public class Test {
 *  4     public static void main( String[] args ) {
 *  5         try {
 *  6             a();
 *  7         } catch(Exception e) {
 *  8             e.printStackTrace();
 *  9         }
 * 10      }
 * 11
 * 12      public static void a() throws Exception {
 * 13          try {
 * 14              b();
 * 15          } catch(Exception e) {
 * 16              throw new NestedException("foo", e);
 * 17          }
 * 18      }
 * 19
 * 20      public static void b() throws Exception {
 * 21          try {
 * 22              c();
 * 23          } catch(Exception e) {
 * 24              throw new NestedException("bar", e);
 * 25          }
 * 26      }
 * 27
 * 28      public static void c() throws Exception {
 * 29          throw new Exception("baz");
 * 30      }
 * 31 }
 * </pre></blockquote>
 * <p>Yields the following stacktrace:
 * <p><blockquote><pre>
 * java.lang.Exception: baz: bar: foo
 *    at Test.c(Test.java:29)
 *    at Test.b(Test.java:22)
 * rethrown as NestedException: bar
 *    at Test.b(Test.java:24)
 *    at Test.a(Test.java:14)
 * rethrown as NestedException: foo
 *    at Test.a(Test.java:16)
 *    at Test.main(Test.java:6)
 * </pre></blockquote><br>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:knielsen@apache.org">Kasper Nielsen</a>
 * @author <a href="mailto:steven@caswell.name">Steven Caswell</a>
 * @version $Id: NestableException.java,v 1.6.88.1 2008/02/15 15:31:40 jthomas Exp $
 */
public class NestableException extends Exception implements Nestable, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
     * The helper instance which contains much of the code which we
     * delegate to.
     */
    protected NestableDelegate delegate = new NestableDelegate(this);

    /**
     * Holds the reference to the exception or error that caused
     * this exception to be thrown.
     */
    private Throwable cause = null;

    /**
     * Constructs a new <code>NestableException</code> without specified
     * detail message.
     */
    public NestableException()
    {
        super();
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * detail message.
     *
     * @param msg The error message.
     */
    public NestableException(String msg)
    {
        super(msg);
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * nested <code>Throwable</code>.
     *
     * @param nested The exception or error that caused this exception
     *               to be thrown.
     */
    public NestableException(Throwable cause)
    {
        super();
        this.cause = cause;
    }

    /**
     * Constructs a new <code>NestableException</code> with specified
     * detail message and nested <code>Throwable</code>.
     *
     * @param msg    The error message.
     * @param nested The exception or error that caused this exception
     *               to be thrown.
     */
    public NestableException(String msg, Throwable cause)
    {
        super(msg);
        this.cause = cause;
    }

    /**
     * @see org.apache.commons.lang.exception.Nestable#getCause()
     */
    public Throwable getCause()
    {
        return cause;
    }

    /**
     * Returns the number of nested <code>Throwable</code>s represented by
     * this <code>Nestable</code>, including this <code>Nestable</code>.
     */
    public int getLength()
    {
        return delegate.getLength();
    }

    /**
     * @see org.apache.commons.lang.exception.Nestable#getMessage()
     */
    public String getMessage()
    {
        StringBuffer msg = new StringBuffer();
        String ourMsg = super.getMessage();
        if (ourMsg != null)
        {
            msg.append(ourMsg);
        }
        if (cause != null)
        {
            String causeMsg = cause.getMessage();
            // if we have a cause and it doesn't contain the original message then
            // we will append it
            if (causeMsg != null && (ourMsg != null && !(causeMsg.indexOf(ourMsg)>0)))
            {
                if (ourMsg != null)
                {
                    // if we have a cause and it isn't the same as the original message then
                    // we will append it
                    if (!causeMsg.equalsIgnoreCase(ourMsg))
                    {
                        if (ourMsg != null) {
                            msg.append(": ");
                        }
                        msg.append(causeMsg);
                    }
                }
// dead code
//                else
//                {
//                    msg.append(causeMsg);
//                }
            }
            else if (causeMsg != null && (ourMsg == null))
            {
                msg.append(causeMsg);
            }
        }
        return (msg.length() > 0 ? msg.toString() : null);
    }

    /**
     * Returns the error message of this and any nested <code>Throwable</code>s
     * in an array of Strings, one element for each message. Any
     * <code>Throwable</code> specified without a message is represented in
     * the array by a null.
     */
    public String[] getMessages()
    {
        return delegate.getMessages();
    }

    public String getAllMessages() {
        String[] msgs = getMessages();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i<msgs.length; i++) {
            if (msgs[i] != null) {
                if (buf.length() == 0)
                    buf.append(msgs[i]);
                else
                    buf.append(" (").append(msgs[i]).append(")");
            }
        }
        return buf.toString();
    }


    public Throwable getThrowable(int index)
    {
        return delegate.getThrowable(index);
    }

    public Throwable[] getThrowables()
    {
        return delegate.getThrowables();
    }

    public String getMessage(int index)
    {
        if(index == 0)
        {
            return super.getMessage();
        }
        else
        {
            return delegate.getMessage(index);
        }
    }

    /**
     * Returns the index, numbered from 0, of the first occurrence of the
     * specified type in the chain of <code>Throwable</code>s, or -1 if the
     * specified type is not found in the chain. If <code>pos</code> is
     * negative, the effect is the same as if it were 0. If <code>pos</code>
     * is greater than or equal to the length of the chain, the effect is the
     * same as if it were the index of the last element in the chain.
     *
     * @param type <code>Class</code> to be found
     * @return index of the first occurrence of the type in the chain, or -1 if
     * the type is not found
     */
    public int indexOfThrowable(Class type)
    {
        return delegate.indexOfThrowable(0, type);
    }

    /**
     * Returns the index, numbered from 0, of the first <code>Throwable</code>
     * that matches the specified type in the chain of <code>Throwable</code>s
     * with an index greater than or equal to the specified position, or -1 if
     * the type is not found. If <code>pos</code> is negative, the effect is the
     * same as if it were 0. If <code>pos</code> is greater than or equal to the
     * length of the chain, the effect is the same as if it were the index of
     * the last element in the chain.
     *
     * @param type <code>Class</code> to be found
     * @param pos index, numbered from 0, of the starting position in the chain
     * to be searched
     *
     * @return index of the first occurrence of the type in the chain, or -1 if
     * the type is not found
     */
    public int indexOfThrowable(int pos, Class type)
    {
        return delegate.indexOfThrowable(pos, type);
    }

    /**
     * Prints the stack trace of this exception the the standar error
     * stream.
     */
    public void printStackTrace()
    {
        delegate.printStackTrace();
    }

    /**
     * Prints the stack trace of this exception to the specified print stream.
     *
     * @param out <code>PrintStream</code> to use for output.
     */
    public void printStackTrace(PrintStream out)
    {
        delegate.printStackTrace(out);
    }

    /**
     * @see org.apache.commons.lang.exception.Nestable#printStackTrace(PrintWriter out)
     */
    public void printStackTrace(PrintWriter out)
    {
        delegate.printStackTrace(out);
    }

    /**
     * @see org.apache.commons.lang.exception.Nestable#printPartialStackTrace(PrintWriter out)
     */
    public final void printPartialStackTrace(PrintWriter out)
    {
        super.printStackTrace(out);
    }

}
