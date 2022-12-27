import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.List;

public class Playground {

    public static int A(){
        return  2;
    }
    public static void main(String[] args) throws IOException {
       int A;
       A= A();
        System.out.println(A);

    }
}
