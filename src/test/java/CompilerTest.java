import org.junit.Test;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Jonas on 11.07.2016.
 */
//@RunWith(Parameterized.class)
public class CompilerTest {


    @Test
    public void testTryCompiling(){
        Compiler testCompiler=new Compiler();
        Exercise mockExercise = mock(Exercise.class);
        HashMap<String, String> classMap= new HashMap<String, String>();
                        classMap.put("Class", "public class Class {" +
                                "            public static int method() {" +
                        "                return 1;" +
                        "            }" +
                        "        }");
        HashMap<String, String> testMap=new HashMap<String, String>();
        testMap.put("MainTest", "import static org.junit.Assert.*;" +
                "        import org.junit.Test;" +
                "        public class MainTest {" +
                "            @Test" +
                "            public void testSomething2() {" +
                "                int x = Class.method();" +
                "                assertEquals(1, x);" +
                "            } " +
                "}");
        when(mockExercise.getClassMap()).thenReturn(classMap);
        when(mockExercise.getTestMap()).thenReturn(testMap);

        assertEquals(true, testCompiler.tryCompiling(mockExercise));
        verify(mockExercise).getClassMap();
        verify(mockExercise).getTestMap();

    }


  /*  @Parameterized.Parameters
    public static Collection<Object[]> data(){
        Object[][] data= new Object[1][2];
        HashMap<String, String> classmap=new HashMap<String, String>();
        classmap.put("Class", "public class Class {" +
                        "            public static int method() {" +
                        "                return 1;" +
                        "            }" +
                        "        }");
        data[0][0]=classmap;

        HashMap<String, String> testmap= new HashMap<String, String>();
        testmap.put("MainTest", "import static org.junit.Assert.*;" +
                        "        import org.junit.Test;" +
                        "        public class MainTest {" +
                        "            @Test" +
                        "            public void testSomething2() {" +
                        "                int x = Class.method();" +
                        "                assertEquals(1, x);" +
                        "            } " +
                        "}");
        data[0][1]= testmap;
        return Arrays.asList(data);
    }*/
}
