import java.util.ArrayList;
/**
 *
 * @author Giovani
 */
public class Digraphton {
    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    private int start;//ESTADO INICIAL
    private boolean[] accept;
    //final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 
    
    public Digraphton(int[][] transition, int st, ArrayList<Integer> acp)
    {
        adjTransition = convertTransition(transition);
        start = st;
        accept = new boolean[adjTransition.length];
        for(int i =0; i < accept.length; i++)
            accept[i] = acp.contains(i);
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
          for(int i = dead+1; i < adjTransition.length; i++)//Sobrepoe a linha do estado removido
              for(int j = 0; j < adjTransition[0].length; j++)
                  adjTransition[i-1][j] = adjTransition[i][j];
          for(int i = 0; i < adjTransition.length; i++)//Sobrepoe a coluna do estado removido
              for(int j = dead+1; j < adjTransition[0].length; j++)
                  adjTransition[i][j-1] = adjTransition[i][j];
          int [][] yoshi = new int[adjTransition.length-1][adjTransition[0].length-1];
          for(int i = 0; i < yoshi.length; i++)//passa para um vetor auxiliar
              for(int j = 0; j < yoshi[0].length; j++)
                  yoshi[i][j] = adjTransition[i][j];
          adjTransition = yoshi.clone();//recebe o auxiliar
          for(int i =dead+1; i < accept.length; i++)//sobrepoe o estado removido
              accept[i-1] = accept[i];
          boolean [] acp = new boolean [accept.length-1];//auxiliar
          for(int i =0; i < acp.length; i++)//transfere pro auxiliar
              acp[i] = accept[i];
          accept = acp.clone();//guarda o auxiliar
          if(start > dead) start--;//atualiza o valor do inicial
          
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
