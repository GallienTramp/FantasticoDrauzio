import java.util.ArrayList;
/**
 *
 * @author Giovani
 */
public class Digraphton {
    private int[][] adjTransition;
    final private int start;
    final private ArrayList<Integer> accept; 
    
    public Digraphton(int[][] transition, int st, ArrayList<Integer> acp)
    {
        adjTransition = convertTransition(transition);
        start = st;
        accept = new ArrayList();
        accept.addAll(acp);
    }
    
    public int tellMeTheWay(int state, int symbol)
    {
        return adjTransition[state][symbol];
    }
    
    public void byeState(int dead){//Anarchist metod
        if(dead == start) return;
        boolean zombie = false;
        int[][] yoshi = new int[adjTransition.length-1][adjTransition[0].length];
        for(int i = 0; i < yoshi.length; i++){
            if(i==dead) zombie = true;
            if(zombie)
                System.arraycopy(adjTransition[i+1], 0, yoshi[i], 0, yoshi[0].length);
            else
                System.arraycopy(adjTransition[i], 0, yoshi[i], 0, yoshi[0].length);       
        }
        adjTransition = yoshi.clone();
    }
    
    public static int[][] convertTransition(int [][] t)
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
        printT(tr);
        return tr;
    }
    
    public static void printT(int[][] t){
        for(int i =0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++)
                System.out.print(t[i][j]);
            System.out.println();
        }
    }
    
}
