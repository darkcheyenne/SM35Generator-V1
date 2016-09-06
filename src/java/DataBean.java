
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateful;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andreas
 */
@Stateful
public class DataBean implements Serializable {
    private String tmpFilePath;
    private String input;

    public DataBean() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        tmpFilePath = "X:\\Dropbox\\" + dateFormat.format(date) + ".txt.txt";

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            tmpFilePath = "/GlassFish/tmp/" + dateFormat.format(date) + ".txt";
        }
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            tmpFilePath = "/Users/Andreas/" + dateFormat.format(date) + ".txt";
        }
    }

    public String getTmpFilePath() {
        return tmpFilePath;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
    
    
}
