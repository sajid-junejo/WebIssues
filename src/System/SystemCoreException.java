/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SystemCoreException extends Exception {
    private Exception wrappedException = null;

    public SystemCoreException(String message, Exception wrappedException) {
        super((message == null && wrappedException != null) ? wrappedException.getMessage() : message);
        this.wrappedException = wrappedException;
    }

    @Override
    public String toString() {
        Exception exception = (wrappedException != null) ? wrappedException : this;
        return createLogString(exception);
    }

    protected String createLogString(Exception exception) {
        return String.format("Unhandled %s: %s in %s on line %d\nStack trace:\n%s", 
            exception.getClass().getName(), exception.getMessage(), exception.getStackTrace()[0].getFileName(), 
            exception.getStackTrace()[0].getLineNumber(), getStackTraceAsString(exception));
    }

    public static SystemCoreException wrapException(Exception exception) {
        if (exception instanceof SystemCoreException) {
            return (SystemCoreException) exception;
        }
        return new SystemCoreException(null, exception);
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
