import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {

    private static final String DIR=Logger.class.getProtectionDomain().getCodeSource().getLocation().toString().split(":")[1] +"/log.txt";

    public static void authenticated(String operation,String name) throws IOException {

        Path path = Paths.get(DIR);
        if(!Files.exists(path)){
            path.toFile().createNewFile();
        }

        String log = operation+"-"+name+"\n";
        Files.write(Paths.get(DIR),log.getBytes(), StandardOpenOption.APPEND);
    }
}
