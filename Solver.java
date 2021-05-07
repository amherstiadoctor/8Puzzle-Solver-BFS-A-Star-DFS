import java.util.*;

public class Solver {
    public static enum SOLVE_METHOD{BFS, DFS, A_STAR};

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

    /*---FUNCTION FOR A*---
    hashmap has a string key and a board config value
    */
    public static Map<String, int[]> aStar(int[] current){
        PriorityQueue<State> q = new PriorityQueue<>();
        Map<String, Integer> dist = new HashMap<>();
        Map<String, int[]> parent = new HashMap<>();
        
        times = 0;
        
        //intialize the distance of the current state to be 0
        dist.put(stringify(current), 0);
        
        //add the current state to the front of the states queue
        q.add(new State(current, 0));
        
        //A* Algorithm ...
        while(!q.isEmpty()){
            State crnt = q.poll();
            times++;
            if(Arrays.equals(crnt.getBoard(), PuzzleModel.GOAL)) break;            
            for(State child : crnt.getNextStates()){
                if(dist.getOrDefault(stringify(child.getBoard()), Integer.MAX_VALUE) > child.getCost()){                    
                    parent.put(stringify(child.getBoard()), crnt.getBoard());
                    dist.put(stringify(child.getBoard()), child.getCost());
                    q.add(child);
                }
            }
        }
        
        return parent;
    }
    
    /*---FUNCTION FOR DFS---
    hashmap has a string key and a board config value
    very very inefficient
    */
    public static Map<String, int[]> dfs(int[] current){
        Stack<State> stack = new Stack<>();
        Map<String, int[]> parent = new HashMap<>();
        Set<String> vis = new HashSet<>();
        
        times = 0;
        
        //add the current state to the front of the states queue
        stack.push(new State(current, 0));
        
        //the simlated recursion part
        while(!stack.isEmpty()){
            State crnt = stack.pop();
            vis.add(stringify(crnt.getBoard()));
            times++;
            if(Arrays.equals(crnt.getBoard(), PuzzleModel.GOAL)) break;            
            for(State child : crnt.getNextStates()){
                if(vis.contains(stringify(child.getBoard()))) continue;
                parent.put(stringify(child.getBoard()), crnt.getBoard());
                stack.push(child);
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