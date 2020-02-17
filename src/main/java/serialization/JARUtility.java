package serialization;

import static java.lang.System.out;
import java.util.Scanner;

/**
 *
 * @author Matt Crow
 */
public class JARUtility {
    public static void run(){
        Scanner in = new Scanner(System.in);
        int ip = 0;
        do {
            out.println("Choose an option:");
            out.println("1: Create a new set of customizables");
            out.println("-1: Quit");
            ip = in.nextInt();
        } while(ip != -1);
        in.close();
    }
    
    public static void main(String[] args){
        run();
    }
}
