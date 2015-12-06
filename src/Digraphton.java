import java.util.ArrayList;
/**
 *
 * @author Giovani
 */
public class Digraphton {
    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    final private int start;//ESTADO INICIAL
    final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 
    
    public Digraphton(int[][] transition, int st, ArrayList<Integer> acp)
    {
        adjTransition = convertTransition(transition);
        start = st;
        accept = new ArrayList();
        accept.addAll(acp);
    }
    
    public int whereAllBegins()//RETORNA ESTADO INICIAL
    {
        return start;
    }
    
    public int[][] sexTypeThing()//RIP Scott //RETORNA MATRIX DE ADJACENCIA
    {
        return adjTransition;
    }
        
    public void byeState(int dead){//Anarchist metod //REMOVE ESTADO
//        if(dead == start) return;
//        int[][] yoshi = new int[adjTransition.length-1][adjTransition[0].length-1];
//        int k=0, l=0;
//        for(int i = 0; i < yoshi.length; i++){
//            if (i == dead) k++;
//            for(int j =0; j < yoshi[0].length; j++)
//            {
//                if(j == dead) l++;
//                yoshi[i][j] = adjTransition[k][l];
//                l++;
//            }
//            k++;
//        }
//        adjTransition = yoshi.clone();
          for(int i = dead+1; i < adjTransition.length; i++)
              for(int j = 0; j < adjTransition[0].length; j++)
                  adjTransition[i-1][j] = adjTransition[i][j];
          for(int i = 0; i < adjTransition.length; i++)
              for(int j = dead+1; j < adjTransition[0].length; j++)
                  adjTransition[i][j-1] = adjTransition[i][j];
          int [][] yoshi = new int[adjTransition.length-1][adjTransition[0].length-1];
          for(int i = 0; i < yoshi.length; i++)
              for(int j = 0; j < yoshi[0].length; j++)
                  yoshi[i][j] = adjTransition[i][j];
          adjTransition = yoshi.clone();
    }
    
    public static int[][] convertTransition(int [][] t)//CONVERTE TRANSICAO FORMATO LAURETTO PRA FORMATO GRAFO
    {
        int[][] tr = new int[t.length][t.length];
        for(int i = 0; i < tr.length; i++)
            for(int j = 0; j < tr[0].length; j++)
                tr[i][j] = -1;
        for(int i =0; i < t.length; i++)
            for(int j = 0; j < t[i].length; j++)
            {
                int k = t[i][j];
                if(k != -1) tr[i][k] = j;
            }
        return tr;
    }
    
    public static void printT(int[][] t){
        for(int i =0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++)
                System.out.print(t[i][j] + " ");
            System.out.println();
        }
    }
    
}
