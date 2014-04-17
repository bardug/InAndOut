package com.berdugo.timeclock.frontend;

/**
 * User: Ami
 * Date: 03/06/13
 * Time: 16:56
 */
public class ErrorDialogTest {
    public static void main(String args[]) {
        InAndOutErrorHandler.popErrorDialog("short error message", null);
        InAndOutErrorHandler.popErrorDialog("long error message:" +
                "Consider the calling hierarchy of a large system. Functions at the top call functions\n" +
                "below them, which call more functions below them, ad infinitum. Now let’s say one of the\n" +
                "lowest level functions is modified in such a way that it must throw an exception. If that\n" +
                "exception is checked, then the function signature must add a throws clause. But this\n" +
                "means that every function that calls our modified function must also be modified either to\n" +
                "catch the new exception or to append the appropriate throws clause to its signature. Ad\n" +
                "infinitum. The net result is a cascade of changes that work their way from the lowest levels\n" +
                "of the software to the highest! Encapsulation is broken because all functions in the path\n" +
                "of a throw must know about details of that low-level exception. Given that the purpose of\n" +
                "exceptions is to allow you to handle errors at a distance, it is a shame that checked exceptions\n" +
                "break encapsulation in this way.", null);
    }
}
