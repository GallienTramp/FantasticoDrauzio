/**********************************************************************************/
/*                         | UNIVERSIDADE DE SAO PAULO |                          */
/*                 | ESCOLA DE ARTES, CIENCIAS E HUMANIDADES |                    */
/*--------------------------------------------------------------------------------*/
/* Caio Tavares Cruz - 8921840 - T. 04                                            */
/* Giovani de Sousa Leite - 8921902 - T. 04                                       */
/*--------------------------------------------------------------------------------*/
/* Exercicio Programa de ITC: Reducao de Automatos Finitos                        */
/**********************************************************************************/

import java.util.ArrayList;

//Classe Digraphton - Modelagem de um grafo dirigido (digrafo)
public class Digraphton {
    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    private int start;//ESTADO INICIAL
    private int symbols;
    private boolean[] accept;
    //final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 
    
    //Construtor - Cria o digrafo de acordo com os parametros passados
    public Digraphton(int[][] transition,int symbol, int st, ArrayList<Integer> acp)
    {
        adjTransition = transition;
        start = st;
        symbols = symbol;
        accept = new boolean[adjTransition.length];
        for(int i =0; i < accept.length; i++)
            accept[i] = acp.contains(i);
    }
    
    //Metodo que retorna o estado inicial
    public int whereAllBegins()
    {
        return start;
    }
    
    //retorna vetor com true pros de aceitacao
    public boolean[] whereHappyMomentsHappens()
    {
        return accept.clone();
    }
    
    //Retorna o alfabeto
    public int alphabetCont()
    {
        return symbols;
    }
    
    //Metodo que retorna a matriz de adjacencia do digrafo
    public int[][] getTransitions()
    {
        return adjTransition.clone();
    }
        
    //Metodo byeState - Remocao de estagio
    public void byeState(int dead){//Anarchist metod //REMOVE ESTADO
    	
          for(int i = dead+1; i < adjTransition.length; i++)//Sobrepoe a linha do estado removido
              for(int j = 0; j < adjTransition[0].length; j++)
                  adjTransition[i-1][j] = adjTransition[i][j];
          
          for(int i = 0; i < adjTransition.length; i++)//Sobrepoe a coluna do estado removido
              for(int j = dead+1; j < adjTransition[0].length; j++)
                  adjTransition[i][j-1] = adjTransition[i][j];
          
          //Criar nova matriz de transicao com (n-1)x(n-1) entradas - por conta da remocao do estado
          int [][] yoshi = new int[adjTransition.length-1][adjTransition[0].length-1];
          
          for(int i = 0; i < yoshi.length; i++)//passa para um vetor auxiliar
              for(int j = 0; j < yoshi[0].length; j++)
                  yoshi[i][j] = adjTransition[i][j];
          
          adjTransition = yoshi.clone();//recebe o auxiliar
          
          //Reorganizar o vetor de estados de aceitacao
          for(int i =dead+1; i < accept.length; i++)//sobrepoe o estado removido
              accept[i-1] = accept[i];
          
          boolean [] acp = new boolean [accept.length-1];//auxiliar
          for(int i =0; i < acp.length; i++)//transfere pro auxiliar
              acp[i] = accept[i];
          
          accept = acp.clone();//guarda o auxiliar
          if(start > dead) start--;//atualiza o valor do inicial
          
    }
    
    //Metodo que converte a funcao de transicao dada em uma matriz de adjacencia de grafo
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
    
    //Metodo que volta a matriz de adjacencia do grafo a uma funcao de transicao de um automato
    public static int[][] reverseConvertTransition(int[][] t, int s)//Volta ao formato Lauretto
    {
        int tr[][] = new int[t.length][s];
        
        for(int i = 0; i < tr.length; i++)
            for(int j = 0; j < tr.length; j++)
                tr[i][j] = -1;
        for(int i =0; i < t.length; i++)
            for(int j = 0; j < t[i].length; j++)
            {
                int k = t[i][j];
                if(k != -1) tr[i][k] = j;
            }
        return tr;
    }
    
    //Metodo que imprime uma matriz t
    public static void printT(int[][] t){
        for(int i =0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++)
                System.out.print(t[i][j] + " ");
            System.out.println();
        }
    }
    
    //Metodo que transpoe a matriz de adjacencia revertendo o grafo
    public static int[][] transpose(int[][] m)//transpoe a matriz de transicao/Inversao das arestas
    {
        int[][] aux = new int[m[0].length][m.length];
        for(int i =0; i < m.length; i++)
            for(int j = 0; j < m.length; j++)
                aux[j][i] = m[i][j];
        return aux;
    }
    
    //Metodo reverse flash - Reverte o grafo e fornece um novo estado inicial
    public Digraphton reverseFlash(int beg)
    {
    	//Transpor a matriz de adjacencia - Reverter o grafo
        int [][] auxT = transpose(this.adjTransition.clone());
        
        //Os novos estados de aceitacao serao aqueles que nao eram estados de aceitacao antes
        ArrayList<Integer> ac = new ArrayList();
        for(int i = 0; i < this.accept.length; i++)
            if(!this.accept[i]) ac.add(i);
        
        //Retornar o novo digrafo
        return new Digraphton(auxT,this.symbols, beg, ac);
    }
    
}
