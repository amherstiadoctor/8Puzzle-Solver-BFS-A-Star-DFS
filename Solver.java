import java.util.*;

public class Solver {
    public static enum SOLVE_METHOD{BFS};

    private static final Map<String, int[]> bfs_parent = new HashMap<>();
    private static final Set<String> bfs_vis = new HashSet<>();

    public static long times;
    /*---FUNCTION FOR BFS---
    hashmap has a string key and a board config value
    */
    public static Map<String, int[]> bfs(int[] current){
        PriorityQueue<State> stack = new PriorityQueue<>();
        Map<String, int[]> parent = new HashMap<>();
        Set<String> vis = new HashSet<>();
        
        times = 0;
        
        //add the current state to the front of the states queue
        stack.add(new State(current, 0));
        
        //the simlated recursion part
        while(!stack.isEmpty()){
            State crnt = stack.remove();
            vis.add(stringify(crnt.getBoard()));
            times++;
            if(Arrays.equals(crnt.getBoard(), PuzzleModel.GOAL)) break;            
            for(State child : crnt.getNextStates()){
                if(vis.contains(stringify(child.getBoard()))) continue;
                parent.put(stringify(child.getBoard()), crnt.getBoard());
                stack.add(child);
            }
        }
        
        return parent;
    }
    
    //makes key for hashmap
    public static String stringify(int[] arr) {
        String str = "";
        for(int i = 0; i < arr.length; ++i){
            str += String.valueOf(arr[i]);
        }

        return str;
    }
}