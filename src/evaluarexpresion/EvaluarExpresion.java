package evaluarexpresion;

import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Nodo {

    int operando;
    int esoperando;
    char opr;
    Nodo izquierda;
    Nodo derecha;

    Nodo(int operando, char opr, int esoperando) {
        this.operando = operando;
        this.esoperando = esoperando;
        this.izquierda = null;
        this.derecha = null;
        this.opr = opr;
    }

}//cierra clase Nodo

public class EvaluarExpresion {

    public static Nodo crearNodo(int operando, char opr, int esoperando) {
        return new Nodo(operando, opr, esoperando);
    }//cierra crearNodo

    
    public static boolean esoperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }//cierra esoperador

    public static int precedencia(char c) {
        if (c == '+' || c == '-') {
            return 1;
        } else if (c == '*' || c == '/') {
            return 2;
        } else if (c == '^') {
            return 3;
        }
        return 0;
    }//cierra precedencia

    
    public static String infijoAposfijo(String infix) {
        Scanner read = new Scanner(System.in);
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
                
        // Encontrar todas las variables en la expresión
        Set<String> variables = buscarVariables(infix);
        
                
        //se solicita valores para las variables y se guardan en un map hash
        Map<String, Integer> valorVar = new HashMap<>();
        
        for (String variable : variables) {
            System.out.print("Ingrese el valor de " + variable + ": ");
            Integer value = read.nextInt();
            valorVar.put(variable, value);
        }
        
        //ciclo para reemplazar las variables por los valores
        for (HashMap.Entry<String, Integer> entry : valorVar.entrySet()) {
                System.out.println(entry.getKey() +" = "+ entry.getValue());        
                        
                infix = infix.replaceAll(entry.getKey(), entry.getValue().toString());

                System.out.println(infix);  
        }
        
        String[] tokens = infix.split("\\s+");

        for (String token : tokens) {
            char c = token.charAt(0);
            if (Character.isDigit(c)) {
                postfix.append(token).append(" ");
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }//cierra ciclo white
                stack.pop();

            } else if (esoperador(c)) {
                while (!stack.isEmpty() && precedencia(c) <= precedencia(stack.peek())) {
                    postfix.append(stack.pop()).append(" ");
                }//cierra while
                stack.push(c);
                
                } 
        }//cierra llave for

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }//cierra while

        return postfix.toString();

    }//cierra metodo infijoAposfijo

    
    public static Nodo construirArbolExpression(String posfij) {
        Stack<Nodo> stack = new Stack<>();
        String[] tokens = posfij.split("\\s+");

        for (String token : tokens) {

            char c = token.charAt(0);

            if (Character.isDigit(c)) {
                //int aux = Integer.parseInt(token);
                stack.push(crearNodo(Integer.parseInt(token), '#', 1));

            } else if (esoperador(c)) {
                Nodo nuevoNodo = crearNodo(-1, c, 0);
                nuevoNodo.derecha = stack.pop();
                nuevoNodo.izquierda = stack.pop();
                stack.push(nuevoNodo);
            }//cierra llave else

        }//cierra for tokens
        return stack.pop();
    } //Cierrra llave metodo construirArbolExpression

    
    public static int evaluarArbolExpresion(Nodo node) {

        if (node.esoperando == 1) {
            return node.operando;
        }//cierra if

        int izqVal = evaluarArbolExpresion(node.izquierda);
        int derVal = evaluarArbolExpresion(node.derecha);

        switch (node.opr) {
            case '+':
                return izqVal + derVal;
            case '-':
                return izqVal - derVal;
            case '*':
                return izqVal * derVal;
            case '/':
                if (derVal == 0) {
                    throw new ArithmeticException("Nose puede dividir entre cero.");
                }//cierra if
                return izqVal / derVal;
                
            //en caso de que exista un exponente
            case '^':
                return (int) Math.pow(izqVal, derVal);
                
            default:
                return 0;
        }//cierra switch
    }//cierra llave metodo evaluarArbolExpresion

    
    public static void main(String[] args) {
        String infixExpression = "10 + 3 + Z - X ^ Y";
        String postfixExpression = infijoAposfijo(infixExpression);

        System.out.println(postfixExpression);
        Nodo expressionTree = construirArbolExpression(postfixExpression);
        int result = evaluarArbolExpresion(expressionTree);
        System.out.println("Value of the expression: " + result);
    }//cierra clase main
    
    private static Set<String> buscarVariables(String expression) {

        Set<String> variables = new HashSet<>();

        //rellenar el alfabeto
        String alfa = "";
        for (char c = 'A'; c <= 'Z'; c++) {
            alfa += c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            alfa += c;
        }

        String[] tokens = expression.split("\\s+");
      
        
        for (int i = 0; i < alfa.length(); i++) {
            char letra = alfa.charAt(i);
            //si en expresion hay alguna letra del abecedario al set <Variable> se le agrega la letra encontrada
            if (expression.contains(String.valueOf(letra))) {
                variables.add(letra + "");
            }
        }

        return variables;
    }


}//cierra llave de la clase principal
