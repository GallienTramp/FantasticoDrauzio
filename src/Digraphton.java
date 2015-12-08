/**********************************************************************************/
/*                         | UNIVERSIDADE DE SAO PAULO |                          */
/*                 | ESCOLA DE ARTES, CIENCIAS E HUMANIDADES |                    */
/*--------------------------------------------------------------------------------*/
/* Caio Tavares Cruz - 8921840 - T. 04                                            */
/* Giovani de Sousa Leite - 8921902 - T. 04                                       */
/*--------------------------------------------------------------------------------*/
/* Exercicio Programa de ITC: Reducao de Automatos Finitos                        */
/**********************************************************************************/
public class Digraphton {
    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    private int start;//ESTADO INICIAL
    private boolean[] accept;
    //final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 
    
    //Construtor - Cria o digrafo de acordo com os parametros passados
    public Digraphton(int[][] transition,int symbol, int st, boolean[] acp)
    {
        adjTransition = transition;
        start = st;
        accept = new boolean[adjTransition.length];
        for(int i =0; i < accept.length; i++)
            accept[i] = acp[i];
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
          
          for(int i = 0; i < adjTransition.length; i++)//Sobrepoe a linha do estado removido
              for(int j = 0; j < adjTransition[0].length; j++)
                  if(adjTransition[i][j]==dead) adjTransition[i][j] = -1;
                  else if(adjTransition[i][j]>dead) adjTransition[i][j]--;
          
          
          //Criar nova matriz de transicao com (n-1)x(n-1) entradas - por conta da remocao do estado
          int [][] yoshi = new int[adjTransition.length-1][adjTransition[0].length];
          
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
//    public static int[][] convertTransition(int [][] t)//CONVERTE TRANSICAO FORMATO LAURETTO PRA FORMATO GRAFO
//    {
//        int[][] tr = new int[t.length][t.length];
//        for(int i = 0; i < tr.length; i++)
//            for(int j = 0; j < tr[0].length; j++)
//                tr[i][j] = -1;
//        for(int i =0; i < t.length; i++)
//            for(int j = 0; j < t[i].length; j++)
//            {
//                int k = t[i][j];
//                if(k != -1) tr[i][k] = j;
//                
//            }
//        return tr;
//    }
    
    //Metodo que volta a matriz de adjacencia do grafo a uma funcao de transicao de um automato
//    public static int[][] reverseConvertTransition(int[][] t, int s)//Volta ao formato Lauretto
//    {
//        int tr[][] = new int[t.length][s];
//        
//        for(int i = 0; i < tr.length; i++)
//            for(int j = 0; j < tr[0].length; j++)
//                tr[i][j] = -1;
//        for(int i =0; i < t.length; i++)
//            for(int j = 0; j < t[i].length; j++)
//            {
//                int k = t[i][j];
//                
//                if(k != -1) tr[i][k] = j;
//            }
//        return tr;
//    }
    
    //Metodo que imprime uma matriz t
    public static void printT(int[][] t){
        for(int i =0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++)
                System.out.print(t[i][j] + " ");
            System.out.println();
        }
    }
    
    //Metodo que transpoe a matriz de adjacencia revertendo o grafo
//    public static int[][] transpose(int[][] m)//transpoe a matriz de transicao/Inversao das arestas
//    {
//        int[][] aux = new int[m.length][m[0].length];
//        for(int i =0; i < m.length; i++)
//            for(int j = 0; j < m[0].length; j++)
//                aux[i][j] = -1;
//        for(int i =0; i < m.length; i++)
//            for(int j = 0; j < m[0].length; j++)
//                if(m[i][j] != -1 )aux[m[i][j]][j] = i;
//        return aux;
//    }
    
    //Metodo reverse flash - Reverte o grafo e fornece um novo estado inicial
//    public Digraphton reverseFlash(int beg)
//    {
//    	//Transpor a matriz de adjacencia - Reverter o grafo
//        int [][] auxT = transpose(this.adjTransition.clone());
//        
//        //Os novos estados de aceitacao serao aqueles que nao eram estados de aceitacao antes
//        boolean[] ac = new boolean[accept.length];
//        for(int i = 0; i < this.accept.length; i++)
//            ac[i] = !accept[i];
//        
//        //Retornar o novo digrafo
//        return new Digraphton(auxT,this.symbols, beg, ac);
//    }
    
    public int[] symbolsDefined(int state)
    {
        int[] definition = new int[this.adjTransition[0].length];
        for(int i =0; i < definition.length; i++)
            definition[i] = -1;
        
        for(int i =0; i < adjTransition[state].length; i++)
            if(adjTransition[state][i] != -1) definition[i] = 1;
        
        return definition;
    }
    
    @Override
    public String toString()
    {
        String paper = this.adjTransition.length + " " + this.adjTransition[0].length + " " + this.start + "\n" +
                (accept[0] ? "1" : "0");
        for(int i = 1; i < this.accept.length; i++)
            paper = paper + (accept[i] ? " 1" : " 0");
        paper = paper+"\n";
        int t[][] = adjTransition;
        
        for(int i =0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++)
                if(j==0)  paper = paper + t[i][j];
                else paper = paper + " " + t[i][j];
            if(i != t.length-1) paper = paper + "\n";
        }
        
        return paper;
    }
    
}
