
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Main {
    static boolean NameReceived = false;

    static Runnable sendFileName(String s, OutputStreamWriter out) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                        out.write(s);
                        out.write(':');
                        out.flush();
                } catch (Exception e) {

                }
            }
        };
    }

    public static void main(String[] args) throws Exception {
        //new ZipFactory(new File("D:\\EnterTheGungeon"),"file.zip").CreateZip();
        String KeepFolder="";
        System.out.println(args.length);
        if(args.length==0){
            System.out.println("usage: java Main <FilePath/FolderPath>");
            Runtime.getRuntime().exit(1);
        }else if (args.length==1){
            KeepFolder="";
        }else if (args.length>=2){
            KeepFolder=args[1]+"/";
        }
        boolean running = true;
        File f = new File(args[0]);
        if (f.isDirectory()) {
            for (String s : f.list()) {
                File f2 = new File(f.getAbsolutePath(), s);
                System.out.println(f2.getAbsolutePath());
                main(new String[]{f2.getAbsolutePath(),KeepFolder+f.getName()});

            }
        }
        if (!f.isDirectory()) {
            while (running) {
                try {
                    Socket s = new Socket("localhost", 2222);
                    OutputStreamWriter out = new OutputStreamWriter(new DataOutputStream(s.getOutputStream()));
                    InputStreamReader in = new InputStreamReader(new FileInputStream(f));
                    InputStreamReader sin = new InputStreamReader(s.getInputStream());
                    int i;
                    new Thread(sendFileName(KeepFolder+f.getName(), out)).start();
                    System.out.println("Filename sent:" + KeepFolder+f.getName());
                    if (sin.read() == 1) {
                        NameReceived = true;
                        System.out.println("Response Received");
                        char[] tmp=new char[2048];
                        while ((i = in.read(tmp)) > 0) {
                            System.out.println(i);
                            out.write(tmp,0,i);
                            out.flush();
                        }
                        out.close();
                        s.close();
                    } else {
                        throw new ConnectException();
                    }
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