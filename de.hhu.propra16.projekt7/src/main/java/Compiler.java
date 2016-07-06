import jdk.nashorn.internal.codegen.CompileUnit;
import vk.core.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jesus on 06.07.2016.
 */
public class Compiler {

    private List<CompilationUnit> unitsList;
    private JavaStringCompiler compiler;
    private CompilerResult compileResult;
    private TestResult testResult;

    /*
     False if compilation failed. True if compilation succeeded
     has to be used first
      */
    public boolean tryCompiling(Exercise currentEx){
        unitsList = new ArrayList<>();
        HashMap<String, String> classMap = currentEx.getClassesText();
        HashMap<String, String> testMap = currentEx.getTestsText();
        for (String key: classMap.keySet())  unitsList.add(new CompilationUnit(key, classMap.get(key), false));
        for (String key: testMap.keySet())   unitsList.add(new CompilationUnit(key, classMap.get(key), true));
        CompilationUnit[] compUnitAr= new CompilationUnit[unitsList.size()];
        for(int i=0;i<unitsList.size();i++) compUnitAr[i]=unitsList.get(i);
        compiler = CompilerFactory.getCompiler(compUnitAr);

        compiler.compileAndRunTests();
        compileResult = compiler.getCompilerResult();
        testResult = compiler.getTestResult();

        return !compileResult.hasCompileErrors();
    }

    // Returns a String of all CompileErrors
    public String getCompileError(){
        if(compileResult.hasCompileErrors()){
            String compilationResult ="";
            for(CompilationUnit compUnit : unitsList) {
                Collection<CompileError> allErrors = compileResult.getCompilerErrorsForCompilationUnit(compUnit);
                for(CompileError er : allErrors) compilationResult+= er.toString() + "\n";

            }
            return  compilationResult;
        }
        return null;
    }

    //False if tests failed. True if tests succeeded
    public boolean tryTests(){
        return testResult.getNumberOfFailedTests() == 0;
    }

    //returns a String of all Failed Tests
    public String getTestfailMessage(){
        if(!this.tryTests()){
            String testFailMessage ="";
           Collection<TestFailure> allfails = testResult.getTestFailures();
            for(TestFailure tFail: allfails) testFailMessage+= tFail.getMessage() + "\n";
            return  testFailMessage;
        }
        return null;
    }



}
