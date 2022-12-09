package hr.fer.oprpp1.hw05.shell.commands;

import hr.fer.oprpp1.hw05.shell.ShellIOException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains utility methods for {@code ShellCommands}
 * @author MatijaPav
 */
public class ShellUtil {

    /**
     * Parses paths that contain spaces.
     * @param args Arguments of the command.
     * @return List of modified arguments.
     */

    public static List<String> parsePath(String[] args){

        List<String> newArgs = new ArrayList<>();

        for(int i = 0; i < args.length; i++){
            String curr = args[i];
            if(curr.charAt(0) == '\"' && curr.charAt(args[i].length() - 1) == '\"')
                newArgs.add(args[i].replace("\"", ""));

            else if(curr.charAt(0) == '\"'){
                StringBuilder sb = new StringBuilder();
                sb.append(curr);
                int appended = 0;

                for(int j = i + 1; j < args.length; j++){
                    if(args[j].endsWith("\"")){
                        sb.append(" ").append(args[j]);
                        appended++;
                        i += appended;
                        break;
                    }
                    sb.append(" ").append(args[j]);
                    appended++;
                }
                newArgs.add(sb.toString().replace("\"", ""));
            }
            else
                newArgs.add(args[i]);
        }
        return newArgs;
    }
}
