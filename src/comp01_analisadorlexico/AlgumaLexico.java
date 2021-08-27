package comp01_analisadorlexico;

public class AlgumaLexico {

    LeitorArquivo leitor;

    public AlgumaLexico(String arquivo) {
        leitor = new LeitorArquivo(arquivo);
    }

    public Token proximoToken() {
        Token proximo = null;
        espacosEComentarios();
        leitor.confirmar();
        proximo = fim();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = palavrasChave();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = variavel();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = numeros();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = operadorAritmetico();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = operadorRelacional();
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
        proximo = parenteses();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        proximo = cadeia();
        if (proximo == null) {
            leitor.zerar();
        } else {
            leitor.confirmar();
            return proximo;
        }
        System.err.println("Erro léxico!");
        System.err.println(leitor.toString());
        return null;
    }

    private Token operadorAritmetico() {
        int caractereLido = leitor.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '*') {
            return new Token(TipoToken.OpAritMult, leitor.getLexema());
        } else if (c == '/') {
            return new Token(TipoToken.OpAritDiv, leitor.getLexema());
        } else if (c == '+') {
            return new Token(TipoToken.OpAritSoma, leitor.getLexema());
        } else if (c == '-') {
            return new Token(TipoToken.OpAritSub, leitor.getLexema());
        } else {
            return null;
        }
    }

    private Token delimitador() {
        int caractereLido = leitor.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == ':') {
            return new Token(TipoToken.Delim, leitor.getLexema());
        } else {
            return null;
        }
    }

    private Token parenteses() {
        int caractereLido = leitor.lerProximoCaractere();
        char c = (char) caractereLido;
        if (c == '(') {
            return new Token(TipoToken.AbrePar, leitor.getLexema());
        } else if (c == ')') {
            return new Token(TipoToken.FechaPar, leitor.getLexema());
        } else {
            return null;
        }
    }

    private Token operadorRelacional() {
        int caractereLido = leitor.lerProximoCaractere();
        char c = (char) caractereLido;
        
        if (c == '$') {
            c = (char) leitor.lerProximoCaractere();
            if (c == 'l') {
                c = (char) leitor.lerProximoCaractere();
                if (c == 't') {
                    c = (char) leitor.lerProximoCaractere();
                    if (c == 'e') {
                        return new Token(TipoToken.OpRelMenorIgual, "$lte");
                    }
                    leitor.retroceder();
                    return new Token(TipoToken.OpRelMenor, "$lt");
                }
            } else if (c == 'g') {
                c = (char) leitor.lerProximoCaractere();
                if (c == 't') {
                    c = (char) leitor.lerProximoCaractere();
                    if (c == 'e') {
                        return new Token(TipoToken.OpRelMaiorIgual, "$gte");
                    }
                    leitor.retroceder();
                    return new Token(TipoToken.OpRelMaior, "$gte");
                }
            } else if (c == 'e') {
                c = (char) leitor.lerProximoCaractere();
                if (c == 'q') {
                    return new Token(TipoToken.OpRelIgual, "$eq");
                }
            } else if (c == 'd') {
                c = (char) leitor.lerProximoCaractere();
                if (c == 'i') {
                    c = (char) leitor.lerProximoCaractere();
                    if (c == 'f') {
                        return new Token(TipoToken.OpRelIgual, "$dif");
                    }
                }
            }
        }
        
        return null;
    }

    private Token numeros() {
        int estado = 1;
        while (true) {
            char c = (char) leitor.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isDigit(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '.') {
                    c = (char) leitor.lerProximoCaractere();
                    if (Character.isDigit(c)) {
                        estado = 3;
                    } else {
                        return null;
                    }
                } else if (!Character.isDigit(c)) {
                    leitor.retroceder();
                    return new Token(TipoToken.NumInt, leitor.getLexema());
                }
            } else if (estado == 3) {
                if (!Character.isDigit(c)) {
                    leitor.retroceder();
                    return new Token(TipoToken.NumReal, leitor.getLexema());
                }
            }
        }
    }

    private Token variavel() {
        int estado = 1;
        while (true) {
            char c = (char) leitor.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isLetter(c)) {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (!Character.isLetterOrDigit(c)) {
                    leitor.retroceder();
                    return new Token(TipoToken.Var, leitor.getLexema());
                }
            }
        }
    }

    private Token cadeia() {
        int estado = 1;
        while (true) {
            char c = (char) leitor.lerProximoCaractere();
            if (estado == 1) {
                if (c == '\'') {
                    estado = 2;
                } else {
                    return null;
                }
            } else if (estado == 2) {
                if (c == '\n') {
                    return null;
                }
                if (c == '\'') {
                    return new Token(TipoToken.Cadeia, leitor.getLexema());
                } else if (c == '\\') {
                    estado = 3;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return null;
                } else {
                    estado = 2;
                }
            }
        }
    }

    private void espacosEComentarios() {
        int estado = 1;
        while (true) {
            char c = (char) leitor.lerProximoCaractere();
            if (estado == 1) {
                if (Character.isWhitespace(c) || c == ' ') {
                    estado = 2;
                } else if (c == '%') {
                    estado = 3;
                } else {
                    leitor.retroceder();
                    return;
                }
            } else if (estado == 2) {
                if (c == '%') {
                    estado = 3;
                } else if (!(Character.isWhitespace(c) || c == ' ')) {
                    leitor.retroceder();
                    return;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return;
                }
            }
        }
    }

    private Token palavrasChave() {
        while (true) {
            char c = (char) leitor.lerProximoCaractere();
            if (!Character.isLetter(c)) {
                leitor.retroceder();
                String lexema = leitor.getLexema();
                if (lexema.equals("DECLARATIONS")) {
                    return new Token(TipoToken.PCDeclaracoes, lexema);
                } else if (lexema.equals("ALGORITHM")) {
                    return new Token(TipoToken.PCAlgoritmo, lexema);
                } else if (lexema.equals("INTEGER")) {
                    return new Token(TipoToken.PCInteiro, lexema);
                } else if (lexema.equals("FLOAT")) {
                    return new Token(TipoToken.PCReal, lexema);
                } else if (lexema.equals("SET")) {
                    return new Token(TipoToken.PCAtribuir, lexema);
                } else if (lexema.equals("TO")) {
                    return new Token(TipoToken.PCA, lexema);
                } else if (lexema.equals("READ")) {
                    return new Token(TipoToken.PCLer, lexema);
                } else if (lexema.equals("PRINT")) {
                    return new Token(TipoToken.PCImprimir, lexema);
                } else if (lexema.equals("IF")) {
                    return new Token(TipoToken.PCSe, lexema);
                } else if (lexema.equals("THEN")) {
                    return new Token(TipoToken.PCEntao, lexema);
                } else if (lexema.equals("WHILE")) {
                    return new Token(TipoToken.PCEnquanto, lexema);
                } else if (lexema.equals("BEGIN")) {
                    return new Token(TipoToken.PCInicio, lexema);
                } else if (lexema.equals("END")) {
                    return new Token(TipoToken.PCFim, lexema);
                } else if (lexema.equals("AND")) {
                    return new Token(TipoToken.OpBoolE, lexema);
                } else if (lexema.equals("OR")) {
                    return new Token(TipoToken.OpBoolOu, lexema);
                } else {
                    return null;
                }
            }
        }
    }

    private Token fim() {
        int caractereLido = leitor.lerProximoCaractere();
        if (caractereLido == -1) {
            return new Token(TipoToken.Fim, "Fim");
        }
        return null;
    }

}
