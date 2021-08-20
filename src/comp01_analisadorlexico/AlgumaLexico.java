package comp01_analisadorlexico;

public class AlgumaLexico {

    LeitorArquivo leitor;

    public AlgumaLexico(String arquivo) {
        leitor = new LeitorArquivo(arquivo);
    }

    public Token proximoToken() {
        Token proximo = null;

        proximo = operadorAritmetico();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }

        proximo = delimitador();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        
        // parentesis
        // operadorRelacional
        // numeros
        // variavel
        // cadeia
        // espacos e comentarios
        // palavras chave
        // fim
        
        System.err.println("Erro léxico!");
        System.err.println(leitor.toString());
        return null;
    }

    private Token operadorAritmetico() {
        int charLido = leitor.lerProximoCaractere();
        char c = (char) charLido;
        if (c == '*') {
            return new Token(TipoToken.OpAritMult, "*");
        } else if (c == '/') {
            return new Token(TipoToken.OpAritDiv, "/");
        } else if (c == '-') {
            return new Token(TipoToken.OpAritSub, "-");
        } else if (c == '+') {
            return new Token(TipoToken.OpAritSoma, "+");
        } else {
            return null;
        }
    }

    private Token delimitador() {
        int charLido = leitor.lerProximoCaractere();
        char c = (char) charLido;
        if (c == ':') {
            return new Token(TipoToken.Delim, ":");
        } else {
            return null;
        }
    }

}
