
public class DocsScan {

    public byte[] body;

    public String fileName;

    public String extension;
    public static final String ODT  = ".odt";
    public static final String RTF  = ".rtf";
    public static final String DOCX = ".docx";
    public static final String DOC  = ".doc";
    public static final String PDF  = ".pdf";
    public DocsScan(byte[] dataFile, String filename, String toExt) {
        this.body = dataFile;
        this.fileName = filename;
        this.extension = toExt;
    }
    public static String getExt(String input){
        return input.substring(input.indexOf(".")+1);
    }
}
