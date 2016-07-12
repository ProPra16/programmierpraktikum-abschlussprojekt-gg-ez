import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        doCompilertest();
    }

    private static void doCompilertest() {
        Result compileTestResult = JUnitCore.runClasses(CompilerTest.class);
        for (Failure failure : compileTestResult.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(compileTestResult.wasSuccessful());
    }
}  	