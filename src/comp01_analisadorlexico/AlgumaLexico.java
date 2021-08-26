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
        if (c == '<') {
            c = (char) leitor.lerProximoCaractere();
            if (c == '>') {
                return new Token(TipoToken.OpRelDif, leitor.getLexema());
            } else if (c == '=') {
                return new Token(TipoToken.OpRelMenorIgual, leitor.getLexema());
            } else {
                leitor.retroceder();
                return new Token(TipoToken.OpRelMenor, leitor.getLexema());
            }
        } else if (c == '=') {
            return new Token(TipoToken.OpRelIgual, leitor.getLexema());
        } else if (c == '>') {
            c = (char) leitor.lerProximoCaractere();
            if (c == '=') {
                return new Token(TipoToken.OpRelMaiorIgual, leitor.getLexema());
            } else {
                leitor.retroceder();
                return new Token(TipoToken.OpRelMaior, leitor.getLexema());
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
                if (lexema.equals("DECLARACOES")) {
                    return new Token(TipoToken.PCDeclaracoes, lexema);
                } else if (lexema.equals("ALGORITMO")) {
                    return new Token(TipoToken.PCAlgoritmo, lexema);
                } else if (lexema.equals("INT")) {
                    return new Token(TipoToken.PCInteiro, lexema);
                } else if (lexema.equals("REAL")) {
                    return new Token(TipoToken.PCReal, lexema);
                } else if (lexema.equals("ATRIBUIR")) {
                    return new Token(TipoToken.PCAtribuir, lexema);
                } else if (lexema.equals("A")) {
                    return new Token(TipoToken.PCA, lexema);
                } else if (lexema.equals("LER")) {
                    return new Token(TipoToken.PCLer, lexema);
                } else if (lexema.equals("IMPRIMIR")) {
                    return new Token(TipoToken.PCImprimir, lexema);
                } else if (lexema.equals("SE")) {
                    return new Token(TipoToken.PCSe, lexema);
                } else if (lexema.equals("ENTAO")) {
                    return new Token(TipoToken.PCEntao, lexema);
                } else if (lexema.equals("ENQUANTO")) {
                    return new Token(TipoToken.PCEnquanto, lexema);
                } else if (lexema.equals("INICIO")) {
                    return new Token(TipoToken.PCInicio, lexema);
                } else if (lexema.equals("FIM")) {
                    return new Token(TipoToken.PCFim, lexema);
                } else if (lexema.equals("E")) {
                    return new Token(TipoToken.OpBoolE, lexema);
                } else if (lexema.equals("OU")) {
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
