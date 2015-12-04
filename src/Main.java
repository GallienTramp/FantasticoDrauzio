import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Giovani
 */
public class Main {
    public static void main(String args[]) throws FileNotFoundException
    {   String x = "afd1.txt";
        readingNews(x);
    }
    
    public static Digraphton readingNews(String arq) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(arq));
        int states = sc.nextInt();
        int symbols = sc.nextInt();
        int start = sc.nextInt();
        int transit[][] = new int[states][symbols];
        ArrayList<Integer> acp = new ArrayList();
        for(int i = 0; i < states; i++)
            if(sc.nextInt()==1) acp.add(i);
        
        for(int i = 0; i < transit.length; i++)
            for(int j = 0; j< transit[0].length; j++)
                transit[i][j] = sc.nextInt();
        return new Digraphton(transit, start, acp);
    }
    
    
    
}
