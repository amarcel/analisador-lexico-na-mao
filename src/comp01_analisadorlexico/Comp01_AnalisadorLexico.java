
package comp01_analisadorlexico;

public class Comp01_AnalisadorLexico {

    public static void main(String[] args) {
        AlgumaLexico lexico = new AlgumaLexico("/home/marcel/NetBeansProjects/Comp01_AnalisadorLexico/src/comp01_analisadorlexico/fatorial.alguma");
        Token t = null;
        while((t = lexico.proximoToken()).nome != TipoToken.Fim) {
            System.out.println(t);
        }
    }
    
}
