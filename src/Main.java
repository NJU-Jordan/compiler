import java.io.*;
import java.util.Scanner;

public class Main
{    
    public static void main(String[] args) throws IOException {
        String cmd="";
        Scanner scanner=new Scanner(System.in);
        cmd=scanner.nextLine();
        String[]opts=cmd.split(" ");
        if(opts[0].equals("cat")){
            File file=new File("src/1");
            FileReader fileReader=new FileReader(file);
            BufferedReader br=new BufferedReader(fileReader);
            String line="";
            String contents="";


            while((line=br.readLine())!=null){

                contents+=line+"\n";


            }

            System.out.print(contents);
        }

    }
}
