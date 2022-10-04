import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

public class Convert {
    String property = "java.io.tmpdir";
    public String tmpDir(){
        return System.getProperty(property)+File.separator;
    }
    public String tempFullFilePath(DocsScan doc){
        return  tmpDir()+TEMP_FILENAME+ doc.extension;
    }
    private void writeToTempFile(DocsScan doc) throws IOException {
        var tempfile = tempFullFilePath(doc);
        System.out.println("WRITING FILE!!!>>"+tempfile);
        var fos = new FileOutputStream(tempfile);
        fos.write(doc.body);
        fos.close();
    };
    private byte[] readBytesfrmTmp(String fileName){
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            return null;
        }
    }
    private static final String TEMP_FILENAME = "TEMP";

    public DocsScan convert2(DocsScan input, String toExt) {
        try {
            writeToTempFile(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessBuilder builder = new ProcessBuilder();
        Process process=null;
            var command = "soffice --convert-to "+ toExt.replace(".", "")+" "+ tmpDir()+"TEMP"+input.extension;
            System.out.println("COMAND::"+command);
            builder.command(command);
            try {
                process = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ///builder.command("soffice --convert-to %s TEMP.x", toExt);

        process.destroy();
        var filename = input.fileName;
        var body = readBytesfrmTmp("./TEMP"+toExt);///tmpDir()+
        return  new DocsScan(body, filename, toExt);
        //  InputStream is;
//            byte[] bytes = IOUtils.toByteArray(inputStream);
//            var fos = new FileOutputStream("TEMP.odt");
//            fos.write(bytes);
//            fos.close;
    }

    public static void main(String[] args) throws IOException {
        Convert  convert = new Convert();
        System.out.println(convert.tmpDir());
        var datafile = Files.readAllBytes(new File("/tmp/TEMP.odt").toPath());
        DocsScan scan = new DocsScan(datafile, "TEMP", DocsScan.ODT);
        var converted = convert.convert2(scan, DocsScan.DOCX);
        if (isNull(converted))
            return;
        System.out.println(converted.body.length);
    };

}
