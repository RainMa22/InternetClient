
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Main {
    static boolean NameReceived = false;

    public static void main(String[] args) throws Exception {
        System.out.println(args.length);
        if(args.length==0){
            System.out.println("usage: java Main <FilePath/FolderPath>");
            Runtime.getRuntime().exit(1);
        }else if (args.length>=1){
            new ZipFactory(new File(args[0]),"file.zip").CreateZip();
        boolean running = true;
        File f = new File("file.zip");
            while (running) {
                try {
                    Socket s = new Socket("localhost", 2222);
                    BufferedOutputStream out = new BufferedOutputStream(new DataOutputStream(s.getOutputStream()));
                    FileInputStream in = new FileInputStream(f);
                    BufferedInputStream sin = new BufferedInputStream(new DataInputStream(s.getInputStream()));
                    int i;
                        NameReceived = true;
                        System.out.println("Response Received");
                        byte[] tmp=new byte[2048];
                        while ((i = in.read(tmp)) > 0) {
                            out.write(tmp,0,i);
                            out.flush();
                        }
                        out.close();
                        s.close();
                    System.out.println("Success");
                    s.close();
                    NameReceived = false;
                    break;
                } catch (ConnectException ce) {
                    System.out.println("Connection Refused! Retrying in 10 seconds!");
                    Thread.sleep(10000);
                } catch (FileNotFoundException fne) {
                    Runtime.getRuntime().exit(0);
                }
            }
    }
}
}