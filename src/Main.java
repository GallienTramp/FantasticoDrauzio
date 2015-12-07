import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
        String x = "afd3.txt";
        Digraphton d = readingNews(x);//Cria o Digrafo
        boolean wall[] = DFS(d);//Faz uma busca em profundidade e recebe vetor com true para os que foram visitados e false cc.
        ArrayList<Integer> removed = new ArrayList();
        for(int i =0; i < wall.length; i++)//Os que nao foram visitados sao guardados num arrayList
            if(!wall[i]) removed.add(i);
        
        //1a exclusao
        Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
        for(Integer gone : removed)
            d.byeState(gone);//Removido
        //2a exclusao
        removed.clear();
        ArrayList<Digraphton> DrWells = new ArrayList();
        boolean [] act = d.whereHappyMomentsHappens();
        for(int i = 0; i < act.length; i++)
            if(act[i]) DrWells.add(d.reverseFlash(i));
        
        Iterator<Digraphton> centralCity = DrWells.iterator();
        Digraphton dgp = centralCity.next();
        wall = DFS(dgp);
        for(int i =0; i < wall.length; i++)//Os que nao foram visitados sao guardados num arrayList
            if(!wall[i]) removed.add(i);
        for(Digraphton rmv : DrWells)
        {
            ArrayList<Integer> hv2bremoved = new ArrayList();
            wall = DFS(rmv);
            for(int i =0; i < wall.length; i++)//Os que nao foram visitados sao guardados num arrayList
                if(!wall[i]) hv2bremoved.add(i);
            removed.retainAll(hv2bremoved);//Mantem apenas os que n conseguem acessar nenhum dos estados de aceitacao
        }
        Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
        for(Integer gone : removed)//Remove estes
            d.byeState(gone);//Removido
         
        Digraphton.printT(Digraphton.reverseConvertTransition(d.getTransitions(), d.alphabetCont()));
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
        return new Digraphton(Digraphton.convertTransition(transit),symbols, start, acp);
    }
    
    public static boolean[] DFS(Digraphton d)//BUSCA EM PROFUNDIDADE RETORNANDO ARRAY DE VISITADOS
    {
        boolean [] travelingAchieve = new boolean [d.getTransitions().length];
        for(int i =0; i < travelingAchieve.length; i++)
            travelingAchieve[i] = false;
        DFSrecursive(d, travelingAchieve, d.whereAllBegins());
        return travelingAchieve;
    }
    
    public static void DFSrecursive(Digraphton d, boolean[] vst, int friend)//BUSCA EM PROFUNDIDADE RECURSÃO
    {
        vst[friend] = true;
        int[][] adj = d.getTransitions();
        for(int i = 0; i < adj[0].length; i++)
            if(adj[friend][i] >= 0 && vst[i] == false)
                DFSrecursive(d, vst, i);
    }
    
}
