
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
public class Digraphton {

    private int[][] adjTransition;//MATRIZ DE ADJACENCIA
    private int start;//ESTADO INICIAL
    private boolean[] accept;
    //final private ArrayList<Integer> accept;//ESTADOS DE ACEITACAO 

    //Construtor - Cria o digrafo de acordo com os parametros passados
    public Digraphton(int[][] transition, int symbol, int st, boolean[] acp) {
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

    @Override
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
}
