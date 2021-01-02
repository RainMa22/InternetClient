import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFactory {
    private ArrayList<File> Files=new ArrayList<>(0);
    private ArrayList<ZipEntry> ZipEntries=new ArrayList<>(0);
    private String output;
    private void CreateZipEntry(String parent,File f){
        File f2=new File(new File(parent),f.toString());
        if (f2.isDirectory()){
            for (String s:f2.list()) {
            CreateZipEntry(parent,new File(f,s));
        }
        }else if (f2.isFile()){
            System.out.println(f.toString());
            Files.add(f2);
            ZipEntries.add(new ZipEntry(f.toString()));

        }
    }
    void CreateZip() throws Exception{
        File f=new File(output);
        //f.getParentFile().mkdirs();
        f.createNewFile();
        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(f));
        zos.setLevel(1);
        byte[] tmp=new byte[2048];
        int i;
        for (int j = 0; j < ZipEntries.size(); j++){
            zos.putNextEntry(ZipEntries.get(j));
            FileInputStream fis=new FileInputStream(Files.get(j));
            while ((i=fis.read(tmp))>0){
                zos.write(tmp);
                zos.flush();
            }
            zos.closeEntry();
        }
        zos.close();
    }
    ZipFactory(File f,String output){
        CreateZipEntry(f.getParent(),new File(f.getName()));
        this.output=output;
    }

}
