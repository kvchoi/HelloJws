package javaflow;

import java.net.URL;

import org.apache.commons.javaflow.Continuation;
import org.apache.commons.javaflow.ContinuationClassLoader;

public class GettingStarted {

    public static void main(String[] args) throws Exception {
        String url = "file:D:\\workspaces\\";
        ClassLoader cl = new ContinuationClassLoader(new URL[] { new URL(url) },
                GettingStarted.class.getClassLoader()); // parent class loader
        Class tar=  cl.loadClass("javaflow.MyRunnable");
        Continuation c = Continuation.startWith(new MyRunnable());
        System.out.println("returned a continuation");
        Continuation d = Continuation.continueWith(c);
        System.out.println("returned another continuation");
        while (d != null) {
            d = Continuation.continueWith(d);
        }
    }
}
