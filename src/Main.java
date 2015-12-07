/**********************************************************************************/
/*                         | UNIVERSIDADE DE SAO PAULO |                          */
/*                 | ESCOLA DE ARTES, CIENCIAS E HUMANIDADES |                    */
/*--------------------------------------------------------------------------------*/
/* Caio Tavares Cruz - 8921840 - T. 04                                            */
/* Giovani de Sousa Leite - 8921902 - T. 04                                       */
/*--------------------------------------------------------------------------------*/
/* Exercicio Programa de ITC: Reducao de Automatos Finitos                        */
/**********************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

//Classe MAIN - Execucao de rotinas
public class Main {

	//Metodo main recebe dois argumentos: Arquivo de entrada e arquivo de saida (nomes)
	public static void main(String args[]) throws FileNotFoundException
    {   
        //args[0] = entrada, args[1] = saida.
        try
        {
        	
        	String x = "afd3.txt";
        	Digraphton d = readingNews(x);//Cria o Digrafo a partir dos dados de entrada
        	
        	//1o Objetivo - Eliminacao de estados inalcancaveis
        	//Estados inalcancaveis - Nao podem ser atingidos a partir do estado inicial
        	
        	boolean wall[] = DFS(d);//Faz uma busca em profundidade e recebe vetor com true para os que foram visitados e false cc.
        	
        	ArrayList<Integer> removed = new ArrayList<Integer>();
        
        	//Os que nao foram visitados sao guardados num arrayList
        	for(int i =0; i < wall.length; i++)
        		if(!wall[i]) removed.add(i);
        
	        //Remover os estados
	        Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
	        for(Integer gone : removed)
	        	d.byeState(gone);//Removido
        
	        //2o Objetivo - Eliminacao de estados inuteis
        	//Estados inuteis - Estados que nao podem conduzir a um estado de aceitacao
	        removed.clear();
	        ArrayList<Digraphton> DrWells = new ArrayList<Digraphton>();
	        
	        //A partir de cada estado de aceitacao, geraremos um novo automato reverso
	        boolean [] act = d.whereHappyMomentsHappens();
	        for(int i = 0; i < act.length; i++)
	        	if(act[i]) DrWells.add(d.reverseFlash(i));
        
	        //Iteramos pelos automatos reversos identificando estagios inuteis em cada um deles
	        Iterator<Digraphton> centralCity = DrWells.iterator();
	        Digraphton dgp = centralCity.next();
	        wall = DFS(dgp);
        
	        //Os que nao foram visitados sao guardados num arrayList - 1a ITERACAO
	        for(int i =0; i < wall.length; i++)
	        	if(!wall[i]) removed.add(i);
        
	        //Iterar pelo conjunto de digrafos - ITERACOES SEGUINTES
	        for(Digraphton rmv : DrWells)
	        {
	        	ArrayList<Integer> hv2bremoved = new ArrayList();
	        	wall = DFS(rmv);
            
	        	//Os que nao foram visitados sao guardados num arrayList
	        	for(int i =0; i < wall.length; i++)
	        		if(!wall[i]) hv2bremoved.add(i);
	        	
	        	//Metodo retainAll: Mantem apenas os que n conseguem acessar nenhum dos estados de aceitacao
	        	removed.retainAll(hv2bremoved);
	        }
        
	        Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
        
	        //Remover os estados inuteis encontrados
	        for(Integer gone : removed)//Remove estes
	        	d.byeState(gone);//Removido
         
	        //Digrafo reduzido - sem estados inuteis nem estados inalcancaveis
	        Digraphton.printT(Digraphton.reverseConvertTransition(d.getTransitions(), d.alphabetCont()));
        }
        catch(Exception e )
        {
        	//Tratamento de Erros
        }
    }
    
    //Método que lê o arquivo e constroi o digrafo
    public static Digraphton readingNews(String arq) throws FileNotFoundException //LEITURA DO ARQUIVO
    {
        Scanner sc = new Scanner(new File(arq));
        
        //Identificando a quantidade de estados
        int states = sc.nextInt();
        //Identificando a quantidade de simbolos do alfabeto
        int symbols = sc.nextInt();
        //Identificando o estado inicial
        int start = sc.nextInt();
        
        //Criando a matriz de transicao
        int transit[][] = new int[states][symbols];
        
        //ArrayList de estados de aceitacao
        ArrayList<Integer> acp = new ArrayList();
        for(int i = 0; i < states; i++)
            if(sc.nextInt()==1) acp.add(i);
        
        //Populand a matriz de transicao
        for(int i = 0; i < transit.length; i++)
            for(int j = 0; j< transit[0].length; j++)
                transit[i][j] = sc.nextInt();
        
        //Criando o objeto de grafo dirigido e retornando-o.
        return new Digraphton(Digraphton.convertTransition(transit),symbols, start, acp);
    }
    
    //Metodo que faz uma busca em profundidade no digrafo d e retorna um array booleando
    //contendo TRUE caso o vertice tenha sido visitado e FALSE caso contrario
    public static boolean[] DFS(Digraphton d)//BUSCA EM PROFUNDIDADE RETORNANDO ARRAY DE VISITADOS
    {
    	//Criando o Array de Retorno
        boolean [] travelingAchieve = new boolean [d.getTransitions().length];
        for(int i =0; i < travelingAchieve.length; i++)
            travelingAchieve[i] = false;
        
        //Metodo que implementa a busca em largura recursiva
        DFSrecursive(d, travelingAchieve, d.whereAllBegins());
        
        //Retorna array de visitados
        return travelingAchieve;
    }
    
    //Metodo que implementa uma busca em profundidade recursiva,
    //recebe como entrada o digrafo, a referência de um array de visitados, e o ponto de partida (estado inicial)
    public static void DFSrecursive(Digraphton d, boolean[] vst, int friend)
    {
        vst[friend] = true;
        //Buscar os vizinhos do estado inicial e fazer busca em profundidade neles
        //Caso eles ainda nao tenham sido visitados
        int[][] adj = d.getTransitions();
        for(int i = 0; i < adj[0].length; i++)
            if(adj[friend][i] >= 0 && vst[i] == false)
                DFSrecursive(d, vst, i);
    }
    
}
