import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**********************************************************************************/
/*                         | UNIVERSIDADE DE SAO PAULO |                          */
/*                 | ESCOLA DE ARTES, CIENCIAS E HUMANIDADES |                    */
/*--------------------------------------------------------------------------------*/
/* Caio Tavares Cruz - 8921840 - T. 04                                            */
/* Giovani de Sousa Leite - 8921902 - T. 04                                       */
/*--------------------------------------------------------------------------------*/
/* Exercicio Programa de ITC: Reducao de Automatos Finitos                        */
/**********************************************************************************/

public class Afn {
	
	/*BEGIN: PARTE 1*/
	/*1 - MODELADORES DO AUTOMATO COMO UM GRAFO*/
	
    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    private int start;//ESTADO INICIAL
    private boolean[] accept;
    //final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 

    //Construtor - Cria o digrafo de acordo com os parametros passados
    public Afn(int[][] transition, int symbol, int st, boolean[] acp) {
        adjTransition = transition;
        start = st;
        accept = new boolean[adjTransition.length];
        for (int i = 0; i < accept.length; i++) {
            accept[i] = acp[i];
        }
    }

    //Metodo que retorna o estado inicial
    public int whereAllBegins() {
        return start;
    }

    //retorna vetor com true pros de aceitacao
    public boolean[] whereHappyMomentsHappens() {
        return accept.clone();
    }

    //Metodo que retorna a matriz de adjacencia do digrafo
    public int[][] getTransitions() {
        return adjTransition.clone();
    }

    //Metodo byeState - Remocao de estagio
    public void byeState(int dead) {//Anarchist metod //REMOVE ESTADO

        for (int i = dead + 1; i < adjTransition.length; i++)//Sobrepoe a linha do estado removido
        {
            for (int j = 0; j < adjTransition[0].length; j++) {
                adjTransition[i - 1][j] = adjTransition[i][j];
            }
        }

        for (int i = 0; i < adjTransition.length; i++)//Sobrepoe a linha do estado removido
        {
            for (int j = 0; j < adjTransition[0].length; j++) {
                if (adjTransition[i][j] == dead) {
                    adjTransition[i][j] = -1;
                } else if (adjTransition[i][j] > dead) {
                    adjTransition[i][j]--;
                }
            }
        }

        //Criar nova matriz de transicao com (n-1)x(n-1) entradas - por conta da remocao do estado
        int[][] yoshi = new int[adjTransition.length - 1][adjTransition[0].length];

        for (int i = 0; i < yoshi.length; i++)//passa para um vetor auxiliar
        {
            for (int j = 0; j < yoshi[0].length; j++) {
                yoshi[i][j] = adjTransition[i][j];
            }
        }

        adjTransition = yoshi.clone();//recebe o auxiliar

        //Reorganizar o vetor de estados de aceitacao
        for (int i = dead + 1; i < accept.length; i++)//sobrepoe o estado removido
        {
            accept[i - 1] = accept[i];
        }

        boolean[] acp = new boolean[accept.length - 1];//auxiliar
        for (int i = 0; i < acp.length; i++)//transfere pro auxiliar
        {
            acp[i] = accept[i];
        }

        accept = acp.clone();//guarda o auxiliar
        if (start > dead) {
            start--;//atualiza o valor do inicial
        }
    }

    //Devolve um vetor de simbolos de transicao a partir do estado state, para os q ele esta definido o valor eh 1, cc 0
    public int[] symbolsDefined(int state) {
        int[] definition = new int[this.adjTransition[0].length];
        for (int i = 0; i < definition.length; i++) {
            definition[i] = -1;
        }

        for (int i = 0; i < adjTransition[state].length; i++) {
            if (adjTransition[state][i] != -1) {
                definition[i] = 1;
            }
        }

        return definition;
    }

    //Metodo de impressao de acordo com o modelo dado
    public String toString() {
        String pinoquio = this.adjTransition.length + " " + this.adjTransition[0].length + " " + this.start + "\n"
                + (accept[0] ? "1" : "0");
        for (int i = 1; i < this.accept.length; i++) {
            pinoquio = pinoquio + (accept[i] ? " 1" : " 0");
        }
        pinoquio = pinoquio + "\n";
        int t[][] = adjTransition;

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                if (j == 0) {
                    pinoquio = pinoquio + t[i][j];
                } else {
                    pinoquio = pinoquio + " " + t[i][j];
                }
            }
            if (i != t.length - 1) {
                pinoquio = pinoquio + "\n";
            }
        }
        return pinoquio;
    }
    
    /*END: PARTE 1*/
    
    /*BEGIN: PARTE 2*/
    /*2: METODOS PARA REDUCAO DO AUTOMATO*/
    //Constantes para estados equivalentes e nao equivalentes
    private final static int EQUIVALENTE = 1;
    private final static int NAOEQUIVALENTE = 0;

    //Metodo main recebe dois argumentos: Arquivo de entrada e arquivo de saida (nomes)
    public static void main(String args[]) {
        //args[0] = entrada, args[1] = saida.
        try {

            String x = args[0];
            Afn d = readingNews(x);//Cria o Digrafo a partir dos dados de entrada
            
            //1o Objetivo - Eliminacao de estados inalcancaveis
            d = EstadosInacessiveis(d);
            
            //2o Objetivo - Eliminacao de estados inuteis
            d = EstadosInuteis(d);
            
            //Digrafo reduzido - sem estados inuteis nem estados inalcancaveis
            //3o Objetivo Busca estados equivalentes
            d = EstadosEquivalentes(d);
            
            writeBack(args[1], d);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("Arquivo nao encontrado");
        }
        catch (Exception e) 
        {
            System.out.println("Erro: Utilize a chamada correta - java Afn ArqEntrada.txt ArqSaida.txt");
        }
    }

    //Metodo que le o arquivo e constroi o digrafo
    public static Afn readingNews(String arq) throws FileNotFoundException //LEITURA DO ARQUIVO
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
        boolean[] acp = new boolean[states];
        for (int i = 0; i < states; i++) {
            if (sc.nextInt() == 1) {
                acp[i] = true;
            }
        }

        //Populando a matriz de transicao
        for (int i = 0; i < transit.length; i++) {
            for (int j = 0; j < transit[0].length; j++) {
                transit[i][j] = sc.nextInt();
            }
        }

        //Criando o objeto de grafo dirigido e retornando-o.
        return new Afn(transit, symbols, start, acp);
    }
    
    //Metodo que escreve o arquivo de saida
    public static void writeBack(String output, Afn automato) throws IOException
    {
    	FileWriter _out = new FileWriter(output);
    	PrintWriter escrever = new PrintWriter(_out);
    	
    	escrever.print(automato);
    	
    	_out.close();
    }

    //Metodo que faz uma busca em profundidade no digrafo d e retorna um array booleando
    //contendo TRUE caso o vertice tenha sido visitado e FALSE caso contrario
    public static boolean[] DFS(Afn d, int st)//BUSCA EM PROFUNDIDADE RETORNANDO ARRAY DE VISITADOS
    {
        //Criando o Array de Retorno
        boolean[] travelingAchieve = new boolean[d.getTransitions().length];
        for (int i = 0; i < travelingAchieve.length; i++) {
            travelingAchieve[i] = false;
        }

        //Metodo que implementa a busca em largura recursiva
        DFSrecursive(d, travelingAchieve, st);

        //Retorna array de visitados
        return travelingAchieve;
    }

    //Metodo que implementa uma busca em profundidade recursiva,
    //recebe como entrada o digrafo, a referencia de um array de visitados, e o ponto de partida (estado inicial)
    public static void DFSrecursive(Afn d, boolean[] vst, int friend) {
        vst[friend] = true;
        //Buscar os vizinhos do estado inicial e fazer busca em profundidade neles
        //Caso eles ainda nao tenham sido visitados
        int[][] adj = d.getTransitions();
        for (int i = 0; i < adj[0].length; i++) {
            if (adj[friend][i] >= 0 && vst[adj[friend][i]] == false) {
                DFSrecursive(d, vst, adj[friend][i]);
            }
        }
    }

    //Metodo que identifica estados inacessiveis atraves de uma busca em profundidade
    public static Afn EstadosInacessiveis(Afn d)
    {
        
            //Estados inalcancaveis - Nao podem ser atingidos a partir do estado inicial
            boolean wall[] = DFS(d, d.whereAllBegins());//Faz uma busca em profundidade e recebe vetor com true para os que foram visitados e false cc.
            ArrayList<Integer> removed = new ArrayList<Integer>();
            //Os que nao foram visitados sao guardados num arrayList
            for (int i = 0; i < wall.length; i++) {
                if (!wall[i]) {
                    removed.add(i);
                }
            }
            //Remover os estados
            Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
            removed.stream().forEach((gone) -> {
                d.byeState(gone);//Removido
            });
            
            return d;
        
    }
    
    public static Afn EstadosInuteis(Afn d)
    {
        
            //Estados inuteis - Estados que nao podem conduzir a um estado de aceitacao
            ArrayList <Integer>removed = new ArrayList<Integer>();
            //Faz uma busca em profundidade a partir de cada estado que nao eh de aceitacao
            boolean[] act = d.whereHappyMomentsHappens();
            for (int i = 0; i < act.length; i++) {
                if (!act[i]) {
                    boolean[] visitors = DFS(d, i);
                    boolean rm = false;
                    for (int j = 0; j < act.length; j++) {
                        if (act[j] && !visitors[j]) {
                            rm = rm || false;
                        } else if (act[j]) {
                            rm = true;
                        }
                    }
                    //Guarda aqueles que nao alcancam nenhum dos de aceitacao
                    if (!rm) {
                        removed.add(i);
                    }
                }
            }

            Collections.sort(removed, Collections.reverseOrder());//eh ordenado em ordem decrescente
            //Remover os estados inuteis encontrados
            removed.stream().forEach((gone) -> {
                //Remove estes
                d.byeState(gone);//Removido
            });
            return d;
    }
    
    //Metodo que identifica estados equivalentes
    public static Afn EstadosEquivalentes(Afn dig) {
        int[][] transicoes;
        transicoes = dig.getTransitions();

        boolean[] estadosFinais;
        estadosFinais = dig.whereHappyMomentsHappens();

        int i, j;

        //1 - Particionar o conjunto em estados finais e nao finais
        int[][] matrizDeEquivalencia;

        matrizDeEquivalencia = new int[transicoes.length][transicoes.length];

        //Preencher a diagonal principal com -1
        for (i = 0; i < matrizDeEquivalencia.length; i++) {
            matrizDeEquivalencia[i][i] = -1;
        }

        //1a Classe de Equivalencia: Estados finais e nao finais
        for (i = 0; i < estadosFinais.length; i++) {
            for (j = 0; j < estadosFinais.length; j++) {
                if (estadosFinais[i] == estadosFinais[j] && i != j) {
                    matrizDeEquivalencia[i][j] = matrizDeEquivalencia[j][i] = EQUIVALENTE;
                }
            }
        }

        //Ao fim desse passo a matriz de equivalencia estara dividida em duas classes de equivalencia:
        //Estados finais e estados nao finais
        //2a Classe de Equivalencia: Transicoes definidas sob o mesmo simbolo.
        for (i = 0; i < matrizDeEquivalencia.length; i++) {
            for (j = 0; j < matrizDeEquivalencia.length; j++) {
                if (matrizDeEquivalencia[i][j] == EQUIVALENTE) {
                    int[] s1 = dig.symbolsDefined(i);
                    int[] s2 = dig.symbolsDefined(j);
                    for (int k = 0; k < s1.length; k++) {
                        if (s1[k] != s2[k]) {
                            matrizDeEquivalencia[i][j] = matrizDeEquivalencia[j][i] = NAOEQUIVALENTE;
                        }
                    }
                }
            }
        }
        //Ao fim desse passo a matriz de equivalencia estara dividida
        //em classes de equivalencia de estados com transicao definida sob os mesmos simbolos

        //3a Classe de Equivalencia: Transicoes iguais ou para estados equivalentes sob o mesmo simbolo
        for (i = 0; i < matrizDeEquivalencia.length; i++) {
            for (j = 0; j < matrizDeEquivalencia.length; j++) {
                if (matrizDeEquivalencia[i][j] == EQUIVALENTE) {
                    for (int k = 0; k < transicoes[0].length; k++) {
                        if (!((transicoes[i][k] == transicoes[j][k]) || (matrizDeEquivalencia[transicoes[i][k]][transicoes[j][k]] == EQUIVALENTE && j != i))) {
                            matrizDeEquivalencia[i][j] = matrizDeEquivalencia[j][i] = NAOEQUIVALENTE;
                        }
                    }
                }
            }
        }

        //Agrupamento - Unificar estados nas suas classes de equivalencia
        int c = 0;
        int[] classeEq = new int[transicoes.length];
        for (i = 0; i < classeEq.length; i++) {
            classeEq[i] = -1;
        }
        for (i = 0; i < classeEq.length; i++) {
            if (classeEq[i] == -1) {
                c++;
                classeEq[i] = c - 1;
                for (j = 0; j < matrizDeEquivalencia[0].length; j++) {
                    if (matrizDeEquivalencia[i][j] == 1) {
                        classeEq[j] = classeEq[i];
                    }
                }
            }
        }
        //Ao termino desse passo temos a qual classe de equivalencia cada estado pertence

        //Construcao do AFD minimo com c estados
        int[][] novaTransicao = new int[c][transicoes[0].length];
        for (i = 0; i < novaTransicao.length; i++) {
            for (j = 0; j < novaTransicao[0].length; j++) {
                novaTransicao[i][j] = -1;
            }
        }

        //Popular nova matriz de transicao com menos estados
        for (i = 0; i < transicoes.length; i++) {
            for (j = 0; j < transicoes[0].length; j++) {
                if (novaTransicao[classeEq[i]][j] == -1) {
                    novaTransicao[classeEq[i]][j] = transicoes[i][j] != -1 ? classeEq[transicoes[i][j]] : -1;
                }
            }
        }
        
        //Encontrar estados de aceitacao
        boolean[] aceitacao = new boolean[c];
        boolean[] ac = dig.whereHappyMomentsHappens();
        for (i = 0; i < ac.length; i++) {
            aceitacao[classeEq[i]] = ac[i];
        }

        //Retornar o automato minimizado
        Afn minimized = new Afn(novaTransicao, novaTransicao[0].length, classeEq[dig.whereAllBegins()], aceitacao);
        return minimized;
    }
    /*END: PARTE 2*/

}
