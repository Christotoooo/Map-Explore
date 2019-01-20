package cn.edu.pku.sei.drivers;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import cn.edu.pku.sei.TestLogger;
import cn.edu.pku.sei.shared.EE;
import cn.edu.pku.sei.shared.Graph;
import cn.edu.pku.sei.shared.Maze;
import cn.edu.pku.sei.shared.MazeBuilder;

/**
 * This driver (it doesn't evaluate results) is handy for launching tests
 * (especially when you want to work in debug mode) of the shortest path
 * algorithm. 
 * 
 * NOTE: see MazeTest for an automated test covering all these files. 
 * 
 * @author Robert Ward
 *
 */
public class MazeDriver {
    private static String[] maze = { 
            "mazes/maze.txt",    
            "mazes/maze.10.10.50.txt",
            "mazes/maze.20.10.90.txt",
            "mazes/maze.30.100.10.txt",
            "mazes/maze.30.50.75.txt"
    };

    // edit this line to run with a different one of the above input files. 
    //     //
    String fname = "./mazes/maze.txt";
    String expected = "[0, 4, 2, 5, 7, 9]";
    int selected = 0; 
    
    @Test
    public void test() throws FileNotFoundException {
        MazeBuilder builder = new MazeBuilder();
        Graph<EE> g = builder.buildGraph(maze[selected]);
        //System.out.println("A graph representing a maze");
        //System.out.println(g);

//        BareV source = builder.getEndVertex(g, true);
//        BareV last = builder.getEndVertex(g, false);
//        List<Integer> path = PathFinder.findPath(g, source, last);
//        System.out.println("A shortest path in the maze");
//        if ( path != null )
//          System.out.println( path );
//        else
//          System.out.println( "No path exists" );
        
        List<Integer> path=builder.run(maze[selected]);
        assertEquals(path.toString(),expected);
   }
    
    
   @Test
    public void testMain() {
        String[] args = { fname };
        Maze.setLogger(new TestLogger());
        Maze.main(args );
        String result = Maze.getLogger().toString();
        assertEquals(expected,result);
    }
}
