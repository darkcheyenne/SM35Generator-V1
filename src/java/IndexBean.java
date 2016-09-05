import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@Stateless
public class IndexBean implements Serializable {

    private String input, tmpOutput;
    private String tmpFilePath;
    
    public IndexBean() {
    tmpFilePath = "X:\\Dropbox\\bdc_recording_java2.txt.txt";
    
    if (getOperatingSystem().contains("linux")){
        tmpFilePath = "/GlassFish/tmp/bdc_recording_java2.txt";
    }
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
    
    public String getOperatingSystem() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String[] getOutput() throws UnsupportedEncodingException, FileNotFoundException, IOException {
        if (input == null || input.length() == 0) {
            return null;
        }

        String textStr[] = input.split("\\r?\\n");

        tmpOutput = "";

        for (String string : textStr) {
            String[] substringArray = string.split("\t");

            tmpOutput = tmpOutput + "                                        \t0000\tT\tPFCG                                                                                                                                \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "SAPLPRGN_TREE                           \t0121\tX\t                                                                                                                                    \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_OKCODE                                                                                                                          \t=COPY";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tAGR_NAME_NEU                                                                                                                        \t" + substringArray[0];
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "SAPLPRGN_COPY_COLL                      \t0500\tX\t                                                                                                                                    \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_OKCODE                                                                                                                          \t=ALL";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tP_SOURCE                                                                                                                            \t" + substringArray[0];
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tP_DEST                                                                                                                              \t" + substringArray[1];
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\tT\tPFCG                                                                                                                                \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "SAPLPRGN_TREE                           \t0121\tX\t                                                                                                                                    \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_OKCODE                                                                                                                          \t=DELE";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tAGR_NAME_NEU                                                                                                                        \t" + substringArray[0];
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "SAPLSPO1                                \t0500\tX\t                                                                                                                                    \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_OKCODE                                                                                                                          \t=OPT1";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_SUBSCR                                                                                                                          \tSAPLSPO1                                0501SUBSCREEN";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "SAPMSSY0                                \t0120\tX\t                                                                                                                                    \t";
            tmpOutput = tmpOutput + System.getProperty("line.separator") + "                                        \t0000\t \tBDC_OKCODE                                                                                                                          \t=DBAC";
            tmpOutput = tmpOutput + System.getProperty("line.separator");
        }

        byte ptext[] = tmpOutput.getBytes();
        String value = new String(ptext, "UTF-8");

        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tmpFilePath), "UTF-8"));
        try {
            out.write(value);
        } finally {
            out.close();
        }

        return value.split(System.getProperty("line.separator"));
    }

    public void download() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        File file = new File(tmpFilePath);
        String fileName = file.getName();
        String contentType = ec.getMimeType(fileName); // JSF 1.x: ((ServletContext) ec.getContext()).getMimeType(fileName);
        int contentLength = (int) file.length();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseContentLength(contentLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();

        Files.copy(file.toPath(), output);

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
}
