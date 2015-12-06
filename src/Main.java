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
    {   
        //args[0] = entrada, args[1] = saida.
        try{
        String x = "afd1.txt";
        Digraphton d = readingNews(x);
        boolean wall[] = DFS(d);
        for(int i = 0; i < wall.length; i++)//INUTEIS - INALCANCAVEIS A PARTIR DO INICIO
            if(!wall[i])
                d.byeState(i);
       
        }catch(Exception e )
        {}
    }
    
    public static Digraphton readingNews(String arq) throws FileNotFoundException //LEITURA DO ARQUIVO
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
    
    public static boolean[] DFS(Digraphton d)//BUSCA EM PROFUNDIDADE RETORNANDO ARRAY DE VISITADOS
    {
        boolean [] travelingAchieve = new boolean [d.sexTypeThing().length];
        for(int i =0; i < travelingAchieve.length; i++)
            travelingAchieve[i] = false;
        getTbeSmokers(d, travelingAchieve, d.whereAllBegins());
        return travelingAchieve;
    }
    
    public static void getTbeSmokers(Digraphton d, boolean[] vst, int friend)//BUSCA EM PROFUNDIDADE RECURSÃO
    {
        vst[friend] = true;
        int[][] adj = d.sexTypeThing();
        for(int i = 0; i < adj[0].length; i++)
            if(adj[friend][i] >= 0 && vst[i] == false)
                getTbeSmokers(d, vst, i);
        
        //if(friend==d.whereAllBegins())
            //return vst;
    }
    
}
