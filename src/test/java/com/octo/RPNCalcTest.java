package com.octo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BinaryOperator;

public class RPNCalcTest {


    @Test
    void quand_l_expression_est_un_1_alors_renvoit_le_1_lui_meme() {


        //Arrange //Given
        final var rpnCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());

        //Act
        int result = rpnCalculator.calculate("1");
        //Assert

        Assertions.assertEquals(1, result);
    }

    @Test
    void quand_l_expression_est_un_nombre_alors_renvoit_le_nombre_lui_meme() {
        //Arrange //Given
        final var rpnCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());

        //Act
        int result = rpnCalculator.calculate("22");
        //Assert

        Assertions.assertEquals(22, result);
    }

    @Test
    void quand_expression_est_1_2_plus_alors_resultat_est_3() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("1 2 +");
        //Assert
        Assertions.assertEquals(3, resultat);
    }

    @Test
    void quand_expression_est_2_1_moins_alors_resultat_est_1() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("2 1 -");
        //Assert
        Assertions.assertEquals(1, resultat);
    }

    @Test
    void quand_expression_est_2_3_multiplié_alors_resultat_est_6() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("2 3 *");
        //Assert
        Assertions.assertEquals(6, resultat);
    }
    @Test
    void quand_expression_est_3_2_divisé_alors_resultat_est_1() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("3 2 /");
        //Assert
        Assertions.assertEquals(1, resultat);
    }
    @Test
    void quand_expression_est_1_3_2_plus_moins_alors_resultat_est_2() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new ArrayDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("1 3 2 + -");
        //Assert
        Assertions.assertEquals(2, resultat);
    }

    @Test
    void quand_expression_est_complexe_plus_moins_alors_resultat_est_X() {
        //Arrange
        final var reversePolishNotationCalculator = new ReversePolishNotationCalculator(new LinkedBlockingDeque<>());
        //Act
        int resultat = reversePolishNotationCalculator.calculate("1 3 2 + - 4 / 5 * 6 -");
        //Assert
        Assertions.assertEquals(-6, resultat);
    }

    private class ReversePolishNotationCalculator {

        private Deque<Integer> pile;

        public ReversePolishNotationCalculator(Deque<Integer> pile) {
            this.pile = pile;
        }

        public int calculate(String arithmeticalExpression) {

            final String termesArithmetiques[] = parse(arithmeticalExpression);


            if (termesArithmetiques.length>1) {
                for (String termeArithmetique : termesArithmetiques) {
                    if (Operateur.isOperator(termeArithmetique)) {
                        ajouteEnDernier(appliqueOpération(termeArithmetique));
                    } else {
                        ajouteEnPremier(termeArithmetique);
                    }
                }
                return pile.pop();
            }
            return defaultCase(arithmeticalExpression);
        }

        private void ajouteEnDernier(Integer resultat) {
            pile.addLast(resultat);
        }

        private Integer appliqueOpération(String termeArithmetique) {
            int a = depileDernierElement();
            int b = depileDernierElement();
            final var resultat = Operateur.of(termeArithmetique).apply(a, b);
            return resultat;
        }

        private void ajouteEnPremier(String termeArithmetique) {
            pile.push(Integer.parseInt(termeArithmetique));
        }

        private Integer depileDernierElement() {
            return pile.pollLast();
        }

        private static String[] parse(String arithmeticalExpression) {
            return arithmeticalExpression.split(" ");
        }

        private static int defaultCase(String arithmeticalExpression) {
            return Integer.parseInt(arithmeticalExpression);
        }


    }
}


abstract class Operateur {
    static Map<String, BinaryOperator<Integer>> operators = Map.of(
            "+", (a, b) -> a + b,
            "-", (a, b)-> a - b,
            "*", (a, b)-> a * b,
            "/", (a, b)-> a / b);

    static BinaryOperator<Integer> of(String symbol) {
        return operators.get(symbol);
    }

    static boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }
}





