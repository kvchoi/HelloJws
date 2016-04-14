import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class TestIfConfig {

    public static void main(String[] args) {
        StringBuilder IFCONFIG = new StringBuilder();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                     System.out.println(inetAddress.getHostAddress().toString());
                     System.out.println("isAnyLocalAddress : "+inetAddress.isAnyLocalAddress());
                     System.out.println("isLinkLocalAddress : "+inetAddress.isLinkLocalAddress());
                     System.out.println("isLoopbackAddress : "+inetAddress.isLoopbackAddress());
                     System.out.println("isMCGlobal : "+inetAddress.isMCGlobal());
                     System.out.println("isMCLinkLocal : "+inetAddress.isMCLinkLocal());
                     System.out.println("isMCNodeLocal : "+inetAddress.isMCNodeLocal());
                     System.out.println("isMCOrgLocal : "+inetAddress.isMCOrgLocal());
                     System.out.println("isMCSiteLocal : "+inetAddress.isMCSiteLocal());
                     System.out.println("isMulticastAddress : "+inetAddress.isMulticastAddress());
                     System.out.println("isSiteLocalAddress : "+inetAddress.isSiteLocalAddress());
                     System.out.println("---------------------");
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                            && !inetAddress.isSiteLocalAddress()
                            && inetAddress instanceof Inet4Address) {
                        IFCONFIG.append(inetAddress.getHostAddress().toString() + "\n");
                    }
                }
            }
        } catch (SocketException ex) {
        }
        System.out.println("result:");
        System.out.println(IFCONFIG);
    }
}
