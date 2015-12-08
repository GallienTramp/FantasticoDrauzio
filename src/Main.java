
/**
 * *******************************************************************************
 */
/*                         | UNIVERSIDADE DE SAO PAULO |                          */
 /*                 | ESCOLA DE ARTES, CIENCIAS E HUMANIDADES |                    */
 /*--------------------------------------------------------------------------------*/
 /* Caio Tavares Cruz - 8921840 - T. 04                                            */
 /* Giovani de Sousa Leite - 8921902 - T. 04                                       */
 /*--------------------------------------------------------------------------------*/
 /* Exercicio Programa de ITC: Reducao de Automatos Finitos                        */
/**
 * *******************************************************************************
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//Classe MAIN - Execucao de rotinas
public class Main {

    //Constantes para estados equivalentes e nao equivalentes
    private final static int EQUIVALENTE = 1;
    private final static int NAOEQUIVALENTE = 0;

    //Metodo main recebe dois argumentos: Arquivo de entrada e arquivo de saida (nomes)
    public static void main(String args[]) {
        //args[0] = entrada, args[1] = saida.
        try {

            String x = "afd6.txt";
            Digraphton d = readingNews(x);//Cria o Digrafo a partir dos dados de entrada
            //1o Objetivo - Eliminacao de estados inalcancaveis
            //Estados inalcancaveis - Nao podem ser atingidos a partir do estado inicial

            boolean wall[] = DFS(d, d.whereAllBegins());//Faz uma busca em profundidade e recebe vetor com true para os que foram visitados e false cc.

            ArrayList<Integer> removed = new ArrayList();

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

            //2o Objetivo - Eliminacao de estados inuteis
            //Estados inuteis - Estados que nao podem conduzir a um estado de aceitacao
            removed.clear();

            //A partir de cada estado de aceitacao, geraremos um novo automato reverso
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
            removed.clear();
            //Digrafo reduzido - sem estados inuteis nem estados inalcancaveis
            //Digraphton.printT(Digraphton.reverseConvertTransition(d.getTransitions(), d.alphabetCont()));
            EstadosEquivalentes(d);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    //M�todo que l� o arquivo e constroi o digrafo
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
        boolean[] acp = new boolean[states];
        for (int i = 0; i < states; i++) {
            if (sc.nextInt() == 1) {
                acp[i] = true;
            }
        }

        //Populand a matriz de transicao
        for (int i = 0; i < transit.length; i++) {
            for (int j = 0; j < transit[0].length; j++) {
                transit[i][j] = sc.nextInt();
            }
        }

        //Criando o objeto de grafo dirigido e retornando-o.
        return new Digraphton(transit, symbols, start, acp);
    }

    //Metodo que faz uma busca em profundidade no digrafo d e retorna um array booleando
    //contendo TRUE caso o vertice tenha sido visitado e FALSE caso contrario
    public static boolean[] DFS(Digraphton d, int st)//BUSCA EM PROFUNDIDADE RETORNANDO ARRAY DE VISITADOS
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
    //recebe como entrada o digrafo, a refer�ncia de um array de visitados, e o ponto de partida (estado inicial)
    public static void DFSrecursive(Digraphton d, boolean[] vst, int friend) {
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

    //M�todo que identifica estados equivalentes
    public static void EstadosEquivalentes(Digraphton dig) {
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
        //2a Classe de Equivalencia: Transições definidas sob o mesmo simbolo.
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

        //3a Classe de Equivalencia: Transições iguais ou para estados equivalentes sob o mesmo simbolo
        // int[][] transicoes = Digraphton.reverseConvertTransition(matrizDeAdjacencia, dig.alphabetCont());//Volta ao formato inicial
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

        //Agrupamento
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

        for (i = 0; i < transicoes.length; i++) {
            for (j = 0; j < transicoes[0].length; j++) {
                if (novaTransicao[classeEq[i]][j] == -1) {
                    novaTransicao[classeEq[i]][j] = transicoes[i][j] != -1 ? classeEq[transicoes[i][j]] : -1;
                }
            }
        }
        boolean[] aceitacao = new boolean[c];
        boolean[] ac = dig.whereHappyMomentsHappens();
        for (i = 0; i < ac.length; i++) {
            aceitacao[classeEq[i]] = ac[i];
        }

        Digraphton minimized = new Digraphton(novaTransicao, novaTransicao[0].length, classeEq[dig.whereAllBegins()], aceitacao);
        System.out.println(minimized);
    }
}
