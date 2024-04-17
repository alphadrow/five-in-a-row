package ru.alphadrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.abs;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int SIZE = 15;
    private static final int CROSS = 1;
    private static final int ZERO = -1;
    private static final int EMPTY = 0;


    private static int[][] board = new int[SIZE][SIZE];
    private static final ArrayList<int[]> moves = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Привет, давай сыграем в 5 в ряд? 1 - Да, 2 - Нет.");
        do {
            play();
            board = new int[SIZE][SIZE];
        } while (isPlayAgain());
    }

    private static void play() {
        board[7][7] = ZERO;
        while (true) {
            playersTurn();
            if (isGameIsOver()) {
                System.out.println("Поздравляю, Вы победили!");
                return;
            }
            computersTurn();
            if (isGameIsOver()) {
                printBoard(board);
                System.out.println("Увы, Вы проиграли!");
                return;
            }
        }
    }

    public static void printBoard(int[][] board) {
        System.out.print("    ");
        for (char i = 'A'; i < board[0].length + 'A'; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println();
        int i = 0;
        for (int[] lines : board) {
            System.out.printf("%3d ", ++i);
            for (int line : lines) {
                switch (line) {
                    case EMPTY:
                        System.out.print(" . ");
                        break;
                    case CROSS:
                        System.out.print(" x ");
                        break;
                    case ZERO:
                        System.out.print(" o ");
                        break;
                    default:
                        System.out.printf(" %s ", line);
                }
            }
            System.out.println();

        }
    }

    private static void playersTurn() {
        printBoard(board);
        Scanner sc = new Scanner(System.in);
        int a, b;
        do {
            System.out.println("Введите ваш ход через пробел, например: A 1");
            String[] turn = sc.nextLine().toUpperCase().split(" ");
            a = Integer.parseInt(turn[1]) - 1;
            b = turn[0].charAt(0) - 'A';
        } while (!isTurnIsCorrect(a, b));
        board[a][b] = CROSS;
        int[] move = {a, b};
        moves.add(move);
/*        for (int[] ints : board) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }*/

    }

    private static boolean isTurnIsCorrect(int a, int b) {
        if ((board[a][b]) == 0) {
            return true;
        }
        System.out.println("Это место занято! Сюда нельзя ходить!");
        return false;
    }

    private static void computersTurn() {
        int[][] weights = calculateWeights(board);
//        printBoard(weights);
        int maxX = 0;
        int maxY = 0;
        int maxValue = 0;

        for (int i = 0; i < weights.length; i++) {
            for (int i1 = 0; i1 < weights[i].length; i1++) {
                if (maxValue <= weights[i][i1]) {
                    maxValue = weights[i][i1];
                    maxX = i;
                    maxY = i1;
                }
            }
        }
        System.out.printf("Хожу %s %s \n", (char) (maxY + 'A'), maxX + 1);
 /*       for (int i = 0; i < weights.length; i++) {
            for (int i1 = 0; i1 < weights[i].length; i1++) {
                if (weights[i][i1] != 0) {
                                    System.out.println(i + "-" +  i1 + ": " + weights[i][i1] );
                }
            }
        }*/
        board[maxX][maxY] = ZERO;
        int[] move = {maxX, maxY};
        moves.add(move);

    }

    private static int[][] calculateWeights(int[][] board) {
        int[][] weights = new int[SIZE][SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int i1 = 0; i1 < board[i].length; i1++) {
                if (board[i][i1] == 0) {
                    weights[i][i1] = calculateWeight(i, i1);
                    //                 System.out.printf("weights[%s][%s]: %s\n", i, i1, weights[i][i1]);
                }
            }
        }
        return weights;
    }

    private static int calculateWeight(int x, int y) {
        int[][] tmp = new int[8][2];
        int result = 0;
        tmp[0] = countLineWeight(x, y, 1, 0, 2);
        tmp[1] = countLineWeight(x, y, 1, 1, 2);
        tmp[2] = countLineWeight(x, y, 0, 1,2);
        tmp[3] = countLineWeight(x, y, -1, 1,2);
        tmp[4] = countLineWeight(x, y, -1, 0,2);
        tmp[5] = countLineWeight(x, y, -1, -1, 2);
        tmp[6] = countLineWeight(x, y, 0, -1, 2);
        tmp[7] = countLineWeight(x, y, 1, -1, 2);
        for (int i = 0; i < 8; i++) {
            if (tmp[i][1] > 1) {
                tmp[i][1] += 1;
            }
            if ((tmp[i][1] > 1 && tmp[i][0] < 0) || (tmp[i][1] > 0 && tmp[i][0] < -1)) {
                result = 0;
            } else result += abs(tmp[i][0]) + tmp[i][1];
//            System.out.println("result:" + result);
        }
        return result;
    }


    private static boolean isGameIsOver() {
        for (int[] move : moves) {
            if (doesLinesHasFive(move)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesLinesHasFive(int[] move) {

        return (Arrays.stream(countLineWeight(move[0], move[1], 1, 0, 1)).anyMatch(x -> abs(x) == 5)
                || (Arrays.stream(countLineWeight(move[0], move[1], 1, 1, 1)).anyMatch(x -> abs(x) == 5))
                || (Arrays.stream(countLineWeight(move[0], move[1], 0, 1, 1)).anyMatch(x -> abs(x) == 5))
                || (Arrays.stream(countLineWeight(move[0], move[1], -1, 1, 1)).anyMatch(x -> abs(x) == 5))
                || (Arrays.stream(countLineWeight(move[0], move[1], -1, 0, 1)).anyMatch(x -> abs(x) == 5))
                || (Arrays.stream(countLineWeight(move[0], move[1], -1, -1, 1)).anyMatch(x -> abs(x) == 5))
                || (Arrays.stream(countLineWeight(move[0], move[1], 0, -1, 1)).anyMatch(x -> abs(x) == 5)
                || (Arrays.stream(countLineWeight(move[0], move[1], 1, -1, 1)).anyMatch(x -> abs(x) == 5))));
    }

    private static int[] countLineWeight(int x, int y, int a, int b, int z) {
        int[] result = new int[2];
        int playerSum = 0;
        int computerSum = 0;
        int prevVal = board[x][y];
        for (int i = 0; i < 5; i++) {
//            System.out.printf("Проверяю элемент массива [%s][%s] сумма = %s \n", x, y, playerSum);
            if (x < 0 || y < 0 || x >= board[0].length || y >= board[1].length) {
                break;
            }

            switch (board[x][y]) {
                case -1:
                    if ((prevVal == board[x][y]) || (prevVal == 0)) {
                        computerSum = computerSum * z + board[x][y];
                    } else {
                        computerSum = 0;
                    }
                    break;
                case 1:
                    if ((prevVal == board[x][y]) || (prevVal == 0)) {
                        playerSum = playerSum * z + board[x][y];
                    } else {
                        playerSum = 0;
                    }
                    break;

            }
            prevVal = board[x][y];
            x += a;
            y += b;
            result[0] = computerSum;
            result[1] = playerSum;
        }
/*        System.out.print(result[0] + ":" + result[1] + " ");
        System.out.println();*/
        return result;

}
        private static boolean isPlayAgain () {
            Scanner scanner = new Scanner(System.in);
            int answer;
            do {
                System.out.print("Играем ещё? (0 - нет / 1 - да)");
                answer = scanner.nextInt();
            } while (answer != 1 && answer != 0);
            return answer == 1;
        }
    }
