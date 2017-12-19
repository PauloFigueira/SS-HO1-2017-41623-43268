import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {

    //private static final String DIR="/home/x3me/Desktop/5-1SEM/SegSoft/";
    private String dir;
    public Logger(String dir){
        this.dir=dir;
    }
    public  void authenticated(String operation,String name) throws IOException {

        Path path = Paths.get(dir+"log.txt");
        if(!Files.exists(path)){
            path.toFile().createNewFile();
        }

        String log = operation+"-"+name+"\n";
        Files.write(Paths.get(dir+"log.txt"),log.getBytes(), StandardOpenOption.APPEND);
    }
}
