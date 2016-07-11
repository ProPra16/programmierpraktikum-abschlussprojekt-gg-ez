import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Jonas on 11.07.2016.
 */
@RunWith(Parameterized.class)
public class CompilerTest {

    private  HashMap<String, String> classMap;
    private  HashMap<String, String> testMap;
    private boolean testsCompile;

    public CompilerTest(HashMap<String, String> inputClassMap, HashMap<String, String> inputTestMap, boolean inputCopmile){
        classMap=inputClassMap;
        testMap=inputTestMap;
        testsCompile=inputCopmile;
    }


    @Test
    public void testTryCompiling(){
        Compiler testCompiler=new Compiler();
        Exercise mockExercise = mock(Exercise.class);
        when(mockExercise.getClassMap()).thenReturn(classMap);
        when(mockExercise.getTestMap()).thenReturn(testMap);

        assertEquals(testsCompile, testCompiler.tryCompiling(mockExercise));
        verify(mockExercise).getClassMap();
        verify(mockExercise).getTestMap();

    }


    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        //--------- First Parameters--------------
        HashMap<String, String> classmap1=new HashMap<String, String>();
        classmap1.put("Class", "public class Class {" +
                        "            public static int method() {" +
                        "                return 1;" +
                        "            }" +
                        "        }");

        HashMap<String, String> testmap1= new HashMap<String, String>();
        testmap1.put("MainTest", "import static org.junit.Assert.*;" +
                        "        import org.junit.Test;" +
                        "        public class MainTest {" +
                        "            @Test" +
                        "            public void testSomething2() {" +
                        "                int x = Class.method();" +
                        "                assertEquals(1, x);" +
                        "            } " +
                        "}");

        //--------- Second Parameters--------------


        return Arrays.asList(new Object[][] {
                {classmap1, testmap1, true}
        });
    }
}
