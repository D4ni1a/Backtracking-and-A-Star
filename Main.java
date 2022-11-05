package com.company;

import java.io.*;
import java.util.*;

import static java.lang.Math.*;

/**
 * @author Danila Shulepin
 * @version 2.0
 */
public class Main {

    /* Variables represent positions of Jack Sparrow, Davy Jones, Kraken, Rock, Dead Man's Chest and Tortuga
    and type of perception for Jack Sparrow */
    static Cell jack;
    static Cell jones;
    static Cell kraken;
    static Cell rock;
    static Cell heart;
    static Cell rum;
    static int perception;

    public static void main(String[] args) {
        try {

            /* Call of method, which initialize all the map */
            create();

            /* Check of the forbidden position */
            if (check()) {
                /* Running of the backtracking algorithm to current map */
                Backtracking backtracking = new Backtracking(jack, jones, kraken, rock, heart, rum, perception);
                backtracking.run();

                /* Running of the A* algorithm to current map */
                AStar aStar = new AStar(jack, jones, kraken, rock, heart, rum, perception);
                aStar.run();
            } else {
                /* If any of the input positions is forbidden, then this map contradicts the rules of game */
                System.out.println("Your input is wrong!");
            }

        } catch (Exception exception) {
            /* If any error occurs during the program execution
            (Related to wrong input format) */
            System.out.println("Error during execution!");
        }
    }

    /**
     * Method reads and stores positions of Jack Sparrow, Davy Jones, Kraken, Rock, Dead Man's Chest and Tortuga,
     * type of input (from file or from console) and type of perception for Jack Sparrow
     *
     * @throws IOException as it input/output data from/in files
     */
    public static void create() throws IOException {
        System.out.println("Please choose type of input:\n1 - from file\n2 - from console");
        String line;
        int type;
        int spyglass;
        jack = new Cell();
        jones = new Cell();
        kraken = new Cell();
        rock = new Cell();
        heart = new Cell();
        rum = new Cell();

        while (true) {
            /* Reads the type of input
            1 - from file
            2 - from console */
            BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
            type = scanner.read() - 48;
            if (type == 1) {
                /* Reads input data from file 'input.txt' */
                BufferedReader scan = new BufferedReader(new FileReader("input.txt"));
                line = scan.readLine();
                spyglass = scan.read() - 48;
                scan.close();
                scanner.close();

                /* Fill objects' fields by corresponding positions and stores perception type */
                jack.x = Integer.parseInt(line.split("] \\[")[0].split(",")[0].split("\\[")[1]);
                jack.y = Integer.parseInt(line.split("] \\[")[0].split(",")[1]);

                jones.x = Integer.parseInt(line.split("] \\[")[1].split(",")[0]);
                jones.y = Integer.parseInt(line.split("] \\[")[1].split(",")[1]);

                kraken.x = Integer.parseInt(line.split("] \\[")[2].split(",")[0]);
                kraken.y = Integer.parseInt(line.split("] \\[")[2].split(",")[1]);

                rock.x = Integer.parseInt(line.split("] \\[")[3].split(",")[0]);
                rock.y = Integer.parseInt(line.split("] \\[")[3].split(",")[1]);

                heart.x = Integer.parseInt(line.split("] \\[")[4].split(",")[0]);
                heart.y = Integer.parseInt(line.split("] \\[")[4].split(",")[1]);

                rum.x = Integer.parseInt(line.split("] \\[")[5].split(",")[0]);
                rum.y = Integer.parseInt(line.split("] \\[")[5].split(",")[1].split("]")[0]);

                perception = spyglass;

                break;
            } else if (type == 2) {
                /* Generates new map according to rules of the game */
                Random rand = new Random();

                /* Choose the position of Jack Sparrow */
                jack.x = 0;
                jack.y = 0;

                /* Generates the position of Davy Jones */
                jones.x = rand.nextInt(100000) % 9;
                jones.y = rand.nextInt(100000) % 9;

                /* Check tha validity of Davy Jones' position */
                while (jones.x == jack.x && jones.y == jack.y) {
                    jones.x = rand.nextInt(100000) % 9;
                    jones.y = rand.nextInt(100000) % 9;
                }

                /* Generates the position of Kraken */
                kraken.x = rand.nextInt(100000) % 9;
                kraken.y = rand.nextInt(100000) % 9;

                /* Check tha validity of Kraken's position */
                while ((jones.x == kraken.x && jones.y == kraken.y) ||
                        (jack.x == kraken.x && jack.y == kraken.y)) {
                    kraken.x = rand.nextInt(100000) % 9;
                    kraken.y = rand.nextInt(100000) % 9;
                }

                /* Generates the position of Rock */
                rock.x = rand.nextInt(100000) % 9;
                rock.y = rand.nextInt(100000) % 9;

                /* Check tha validity of Rock's position */
                while ((jones.x == rock.x && jones.y == rock.y) ||
                        (jack.x == rock.x && jack.y == rock.y) ||
                        (kraken.x == rock.x && kraken.y == rock.y)) {
                    rock.x = rand.nextInt(100000) % 9;
                    rock.y = rand.nextInt(100000) % 9;
                }

                /* Generates the position of Dead Man's Chest */
                heart.x = rand.nextInt(100000) % 9;
                heart.y = rand.nextInt(100000) % 9;

                /* Check tha validity of Dead Man's Chest position */
                while ((jones.x == heart.x && jones.y == heart.y) ||
                        (jack.x == heart.x && jack.y == heart.y) ||
                        (kraken.x == heart.x && kraken.y == heart.y) ||
                        (rock.x == heart.x && rock.y == heart.y) ||
                        (kraken.x + 1 == heart.x && kraken.y == heart.y) ||
                        (kraken.x - 1 == heart.x && kraken.y == heart.y) ||
                        (kraken.x == heart.x && kraken.y + 1 == heart.y) ||
                        (kraken.x == heart.x && kraken.y - 1 == heart.y) ||
                        (jones.x - 1 == heart.x && jones.y - 1 == heart.y) ||
                        (jones.x == heart.x && jones.y - 1 == heart.y) ||
                        (jones.x + 1 == heart.x && jones.y - 1 == heart.y) ||
                        (jones.x + 1 == heart.x && jones.y == heart.y) ||
                        (jones.x - 1 == heart.x && jones.y == heart.y) ||
                        (jones.x == heart.x && jones.y + 1 == heart.y) ||
                        (jones.x + 1 == heart.x && jones.y + 1 == heart.y) ||
                        (jones.x - 1 == heart.x && jones.y + 1 == heart.y)) {
                    heart.x = rand.nextInt(100000) % 9;
                    heart.x = rand.nextInt(100000) % 9;
                }

                /* Generates the position of Tortuga */
                rum.x = rand.nextInt(100000) % 9;
                rum.y = rand.nextInt(100000) % 9;

                /* Check tha validity of Tortuga's position */
                while ((jones.x == rum.x && jones.y == rum.y) ||
                        (heart.x == rum.x && heart.y == rum.y) ||
                        (kraken.x == rum.x && kraken.y == rum.y) ||
                        (rock.x == rum.x && rock.y == rum.y) ||
                        (kraken.x + 1 == rum.x && kraken.y == rum.y) ||
                        (kraken.x - 1 == rum.x && kraken.y == rum.y) ||
                        (kraken.x == rum.x && kraken.y + 1 == rum.y) ||
                        (kraken.x == rum.x && kraken.y - 1 == rum.y) ||
                        (jones.x - 1 == rum.x && jones.y - 1 == rum.y) ||
                        (jones.x == rum.x && jones.y - 1 == rum.y) ||
                        (jones.x + 1 == rum.x && jones.y - 1 == rum.y) ||
                        (jones.x + 1 == rum.x && jones.y == rum.y) ||
                        (jones.x - 1 == rum.x && jones.y == rum.y) ||
                        (jones.x == rum.x && jones.y + 1 == rum.y) ||
                        (jones.x + 1 == rum.x && jones.y + 1 == rum.y) ||
                        (jones.x - 1 == rum.x && jones.y + 1 == rum.y)) {
                    rum.x = rand.nextInt(100000) % 9;
                    rum.y = rand.nextInt(100000) % 9;
                }

                System.out.println("Input perception scenario (1 or 2):");

                /* Input perception scenario from the Console */
                while (true) {
                    scanner = new BufferedReader(new InputStreamReader(System.in));
                    perception = scanner.read() - 48;
                    if (perception == 1 || perception == 2) {
                        scanner.close();
                        System.out.println("Wait! Process is executing!");
                        break;
                    }
                    System.out.println("Input correct perception scenario, please!");
                }
                break;
            } else {
                /* If user input not 1 or 2, asks to do it again */
                System.out.println("Input correct type, please!");
            }
        }

        System.out.println("• [" + jack.x + "," + jack.y + "] – Jack Sparrow\n" +
                "• [" + jones.x + "," + jones.y + "] – Davy Jones\n" +
                "• [" + kraken.x + "," + kraken.y + "] – Kraken\n" +
                "• [" + rock.x + "," + rock.y + "] – Rock\n" +
                "• [" + heart.x + "," + heart.y + "] – Dead Man’s Chest\n" +
                "• [" + rum.x + "," + rum.y + "] – Tortuga\n\n" +
                "• Perception scenario: " + perception);
    }

    /**
     * Check of the forbidden position:
     * Jack Sparrow - initially should be at [0,0]
     * Davy Jones - shouldn't be in position of Jack Sparrow, Kraken, Rock, Tortuga and Dead Man's Chest
     * Kraken - shouldn't be in position of Jack Sparrow, Davy Jones, Tortuga and Dead Man's Chest
     * Rock - shouldn't be in position of Jack Sparrow, Davy Jones, Tortuga and Dead Man's Chest
     * Tortuga - shouldn't be in position and danger zones of Davy Jones, Kraken and Rock and Dead Man's Chest
     * Dead Man's Chest - shouldn't be in position of Jack Sparrow, Davy Jones, Tortuga, Kraken and Rock
     * <p>
     * All positions should be on the map (x and y coordinate more or equal to 0 and less than 9)
     * Perception type - should be 1 or 2
     *
     * @return false if something forbidden, and true otherwise
     */
    static boolean check() {
        // Check the correctness of positions
        if (jack.x != 0 || jack.y != 0) return false;
        if (jones.x < 0 || jones.x > 8 || jones.y < 0 || jones.y > 8) return false;
        if (kraken.x < 0 || kraken.x > 8 || kraken.y < 0 || kraken.y > 8) return false;
        if (rock.x < 0 || rock.x > 8 || rock.y < 0 || rock.y > 8) return false;
        if (rum.x < 0 || rum.x > 8 || rum.y < 0 || rum.y > 8) return false;
        if (heart.x < 0 || heart.x > 8 || heart.y < 0 || heart.y > 8) return false;

        // Check position of Rock
        if (rock.x == jack.x && rock.y == jack.y) return false;
        if (rock.x == jones.x && rock.y == jones.y) return false;
        if (rock.x == heart.x && rock.y == heart.y) return false;
        if (rock.x == rum.x && rock.y == rum.y) return false;

        // Check position of Jack Sparrow
        if (jack.x == jones.x && jack.y == jones.y) return false;
        if (jack.x == kraken.x && jack.y == kraken.y) return false;
        if (jack.x == heart.x && jack.y == heart.y) return false;

        // Check position of Davy Jones
        if (jones.x == kraken.x && jones.y == kraken.y) return false;
        if (jones.x == rum.x && jones.y == rum.y) return false;
        if (jones.x == heart.x && jones.y == heart.y) return false;

        // Check position of Kraken
        if (kraken.x == rum.x && kraken.y == rum.y) return false;
        if (kraken.x == heart.x && kraken.y == heart.y) return false;

        // Check position of Dead Man's Chest
        if (heart.x == rum.x && heart.y == rum.y) return false;

        // Check position of Dead Man's Chest and Tortuga to be not in danger zones
        if (heart.x == jones.x + 1 && heart.y == jones.y) return false;
        if (heart.x == jones.x + 1 && heart.y == jones.y + 1) return false;
        if (heart.x == jones.x + 1 && heart.y == jones.y - 1) return false;
        if (heart.x == jones.x && heart.y == jones.y + 1) return false;
        if (heart.x == jones.x && heart.y == jones.y - 1) return false;
        if (heart.x == jones.x - 1 && heart.y == jones.y - 1) return false;
        if (heart.x == jones.x - 1 && heart.y == jones.y) return false;
        if (heart.x == jones.x - 1 && heart.y == jones.y + 1) return false;
        if (heart.x == kraken.x + 1 && heart.y == kraken.y) return false;
        if (heart.x == kraken.x - 1 && heart.y == kraken.y) return false;
        if (heart.x == kraken.x && heart.y == kraken.y + 1) return false;
        if (heart.x == kraken.x && heart.y == kraken.y - 1) return false;

        if (rum.x == jones.x + 1 && rum.y == jones.y) return false;
        if (rum.x == jones.x + 1 && rum.y == jones.y + 1) return false;
        if (rum.x == jones.x + 1 && rum.y == jones.y - 1) return false;
        if (rum.x == jones.x && rum.y == jones.y + 1) return false;
        if (rum.x == jones.x && rum.y == jones.y - 1) return false;
        if (rum.x == jones.x - 1 && rum.y == jones.y - 1) return false;
        if (rum.x == jones.x - 1 && rum.y == jones.y) return false;
        if (rum.x == jones.x - 1 && rum.y == jones.y + 1) return false;
        if (rum.x == kraken.x + 1 && rum.y == kraken.y) return false;
        if (rum.x == kraken.x - 1 && rum.y == kraken.y) return false;
        if (rum.x == kraken.x && rum.y == kraken.y + 1) return false;
        if (rum.x == kraken.x && rum.y == kraken.y - 1) return false;

        // Check perception type
        return perception == 1 || perception == 2;
    }
}

/**
 * Enumerator for priority direction
 * Used in Backtracking algorithm
 * 8 possible directions
 */
enum Priority {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    UP_LEFT,
    DOWN_LEFT,
    UP_RIGHT,
    DOWN_RIGHT
}

/**
 * Class represents type of return value for one of Backtracking algorithm methods
 */
class Outcome {

    int path;   // Value of found path length
    boolean flag;   // True in case of Win, false in case of Lose

    /**
     * @param path - path length
     * @param flag - True = Win, False = Lose
     */
    Outcome(int path, boolean flag) {
        this.path = path;
        this.flag = flag;
    }
}

/**
 * Class represents single cell
 * (X and Y coordinates of cell)
 */
class Cell {
    int x;
    int y;

    /**
     * By default x and y equal to 0
     */
    Cell() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor for creating object with defined coordinates
     *
     * @param x - first coordinate of the cell
     * @param y - second coordinate of the cell
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

/**
 * Class represents single node for A* algorithm
 */
class Node {
    /* Consists of its location (coordinates), functions g, h and f, indicator of the parent Node */
    Node parent;
    Cell location;
    int f;
    int g;
    int h;
    boolean rum;                                // True = Tortuga was visited, False = otherwise

    /* Special parameters, used for 2 type of perception
    True = danger zone was found near this Node horizontally or vertically, False = otherwise */
    boolean priority;

    Cell[] zone;                                // Current danger zone

    /**
     * Constructor for creating a single Node
     *
     * @param location - type of Cell (x and y coordinate of the cell)
     * @param g        - number of steps made
     * @param h        - distance to destination Cell (Chebyshev Distance)
     * @param parent   - parent Node
     */
    Node(Cell location, int g, int h, Node parent) {
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;                         // f function is sum of g and h
        this.location = location;
        rum = false;                            // Initially rum is false (Tortuga wasn't visited)
        priority = false;                       // Initially priority is false (Danger zone wasn't observed)
        this.zone = new Cell[15];               // Represents danger zone schema for each single Node
    }
}

/**
 * Class representing the backtracking algorithm
 */
class Backtracking {

    /* Cell objects for Jack Sparrow, Davy Jones, Kraken, Rock, Dead Man's Chest and Tortuga */
    private final Cell jack;
    private final Cell jones;
    private final Cell kraken;
    private final Cell rock;
    private final Cell heart;
    private final Cell rum;
    private int shortestPath;                   // Shortest path
    ArrayList<Cell> pathSequence;               // Path sequence
    int perception;                             // Type of perception

    /**
     * Constructor for Backtracking algorithm
     *
     * @param jack       - Cell for Jack Sparrow
     * @param jones      - Cell for Davy Jones
     * @param kraken     - Cell for Kraken
     * @param rock       - Cell for Rock
     * @param heart      - Cell for Dead Man's Chest
     * @param rum        - Cell for Tortuga
     * @param perception - perception type
     */
    Backtracking(Cell jack, Cell jones, Cell kraken, Cell rock, Cell heart, Cell rum, int perception) {
        this.jack = jack;
        this.jones = jones;
        this.kraken = kraken;
        this.rock = rock;
        this.rum = rum;
        this.heart = heart;
        this.perception = perception;
        this.shortestPath = 100;                // Initially the shortest path is 100
    }

    /**
     * Calculates prioritized direction for next step
     *
     * @param x           - current cell first coordinate
     * @param y           - current cell second coordinate
     * @param destination - Cell of destination
     * @return array of priorities (enum Priority) or null if the current cell's coordinate the same as destination's
     */
    private Priority[] direct(int x, int y, Cell destination) {
        Priority[] priority = new Priority[8];
        if ((destination.x - x) == 0) {
            /* If the first coordinate of the current cell is same
            as the first coordinate of the destination cell */
            if ((destination.y - y) == 0) {
                /* If the second coordinate of the current cell is same
                as the second coordinate of the destination cell */
                return null;
            } else if ((destination.y - y) > 0) {
                /* If the current cell is lefter then the destination cell */
                priority[0] = Priority.RIGHT;
                priority[1] = Priority.DOWN_RIGHT;
                priority[2] = Priority.UP_RIGHT;

                priority[3] = Priority.DOWN;
                priority[4] = Priority.UP;

                priority[5] = Priority.DOWN_LEFT;
                priority[6] = Priority.UP_LEFT;
                priority[7] = Priority.LEFT;
            } else {
                /* If the current cell is righter then the destination cell */
                priority[0] = Priority.LEFT;
                priority[1] = Priority.DOWN_LEFT;
                priority[2] = Priority.UP_LEFT;

                priority[3] = Priority.DOWN;
                priority[4] = Priority.UP;

                priority[5] = Priority.DOWN_RIGHT;
                priority[6] = Priority.UP_RIGHT;
                priority[7] = Priority.RIGHT;
            }
        } else if ((destination.x - x) > 0) {
            /* If the current cell is above the destination cell */
            if ((destination.y - y) == 0) {
                /* If the current cell is on the same vertical as the destination cell */
                priority[0] = Priority.DOWN;
                priority[1] = Priority.DOWN_RIGHT;
                priority[2] = Priority.DOWN_LEFT;

                priority[3] = Priority.RIGHT;
                priority[4] = Priority.LEFT;

                priority[5] = Priority.UP_RIGHT;
                priority[6] = Priority.UP_LEFT;
                priority[7] = Priority.UP;
            } else if ((destination.y - y) > 0) {
                /* If the current cell is on the up-left diagonal of the destination cell */
                priority[0] = Priority.DOWN_RIGHT;
                priority[1] = Priority.RIGHT;
                priority[2] = Priority.DOWN;

                priority[3] = Priority.DOWN_LEFT;
                priority[4] = Priority.UP_RIGHT;

                priority[5] = Priority.LEFT;
                priority[6] = Priority.UP;
                priority[7] = Priority.UP_LEFT;
            } else {
                /* If the current cell is on the up-right diagonal of the destination cell */
                priority[0] = Priority.DOWN_LEFT;
                priority[1] = Priority.DOWN;
                priority[2] = Priority.LEFT;

                priority[3] = Priority.DOWN_RIGHT;
                priority[4] = Priority.UP_LEFT;

                priority[5] = Priority.RIGHT;
                priority[6] = Priority.UP;
                priority[7] = Priority.UP_RIGHT;
            }
        } else {
            /* If the current cell is below the destination cell */
            if ((destination.y - y) == 0) {
                /* If the current cell is on the same vertical as the destination cell */
                priority[0] = Priority.UP;
                priority[1] = Priority.UP_RIGHT;
                priority[2] = Priority.UP_LEFT;

                priority[3] = Priority.RIGHT;
                priority[4] = Priority.LEFT;

                priority[5] = Priority.DOWN_RIGHT;
                priority[6] = Priority.DOWN_LEFT;
                priority[7] = Priority.DOWN;
            } else if ((destination.y - y) > 0) {
                /* If the current cell is on the down-left diagonal of the destination cell */
                priority[0] = Priority.UP_RIGHT;
                priority[1] = Priority.RIGHT;
                priority[2] = Priority.UP;

                priority[3] = Priority.DOWN_RIGHT;
                priority[4] = Priority.UP_LEFT;

                priority[5] = Priority.DOWN;
                priority[6] = Priority.LEFT;
                priority[7] = Priority.DOWN_LEFT;
            } else {
                /* If the current cell is on the down-right diagonal of the destination cell */
                priority[0] = Priority.UP_LEFT;
                priority[1] = Priority.LEFT;
                priority[2] = Priority.UP;

                priority[3] = Priority.DOWN_LEFT;
                priority[4] = Priority.UP_RIGHT;

                priority[5] = Priority.DOWN;
                priority[6] = Priority.RIGHT;
                priority[7] = Priority.DOWN_RIGHT;
            }
        }
        return priority;
    }

    /**
     * Algorithm calculating the shortest past
     *
     * @param actorPose   - starting position Cell
     * @param currentPath - length of the current path
     * @param dangerZones - array of Cells containing the enemies positions and their perception zones
     * @param barrel      - true if Tortuga was visited, false otherwise
     * @param path        - current path sequence
     * @param destination - Cell of destination
     * @param map         - two-dimensional array (first index - x coordinate, second - y coordinate) with current path length
     *                    to each single Cell
     * @return object of class Return
     */
    private Outcome calculatePath(Cell actorPose, int currentPath, Cell[] dangerZones,
                                  boolean barrel, ArrayList<Cell> path, Cell destination, int[][] map) {

        /* If Jack Sparrow was spawned inside the danger zone */
        if (isElement(dangerZones, actorPose.x, actorPose.y)) return new Outcome(shortestPath, false);

        /* Calculation of the priority list for current Cell */
        Priority[] priority = direct(actorPose.x, actorPose.y, destination);
        ArrayList<Cell> way;

        /* If the current Cell is not a destination Cell */
        if (priority != null) {
            /* If the current perception type is 2 */
            if (perception == 2) {
                /* Change the priority, if see the danger zone in 2 steps horizontally or vertically at any direction */
                if (priority[0] == Priority.DOWN) {
                    if (isElement(dangerZones, actorPose.x + 2, actorPose.y)) {
                        swap(priority);
                    }
                } else if (priority[0] == Priority.UP) {
                    if (isElement(dangerZones, actorPose.x - 2, actorPose.y)) {
                        swap(priority);
                    }
                } else if (priority[0] == Priority.RIGHT) {
                    if (isElement(dangerZones, actorPose.x, actorPose.y + 2)) {
                        swap(priority);
                    }
                } else if (priority[0] == Priority.LEFT) {
                    if (isElement(dangerZones, actorPose.x, actorPose.y - 2)) {
                        swap(priority);
                    }
                }
            }

            /* For each direction calculate parameters and make a step if possible */
            for (Priority element :
                    priority) {
                boolean flag = false;                   // Flag used to check whether the step was made
                int x = actorPose.x;                    // Current position first coordinate
                int y = actorPose.y;                    // Current position second coordinate

                /* Current danger zone and path sequence */
                Cell[] zones = new Cell[15];
                System.arraycopy(dangerZones, 0, zones, 0, 15);
                way = new ArrayList<>(path);
                boolean cask = barrel;                  // Was the Tortuga visited?

                if (x == rum.x && y == rum.y) {
                    cask = true;                        // If Tortuga in the current Cell
                }

                /* Change the danger zone, if the Kraken killed */
                neutralizeKraken(x, y, cask, zones);

                /* If current path length is more or equal to 100, than Lose case */
                if (currentPath >= 100) {
                    shortestPath = 100;
                    return new Outcome(shortestPath, false);
                }

                /* Stop moving in this way if current path already equal to found the shortest */
                if (currentPath >= shortestPath) return new Outcome(shortestPath, true);
                else {
                    /* Check direction
                    For each case next Cell should be inside the map, not in danger zone
                    and current path to this Cell should be less or equal to previous path
                    If step is done flag is true */
                    switch (element) {
                        case DOWN:
                            if ((x + 1 < 9) && !isElement(zones, x + 1, y) &&
                                    (map[x + 1][y] == -1 || map[x + 1][y] > currentPath + 1)) {
                                map[x + 1][y] = currentPath + 1;
                                x += 1;
                                flag = true;
                            }
                            break;
                        case UP:
                            if ((x - 1 >= 0) && !isElement(zones, x - 1, y) &&
                                    (map[x - 1][y] == -1 || map[x - 1][y] > currentPath + 1)) {
                                map[x - 1][y] = currentPath + 1;
                                x -= 1;
                                flag = true;
                            }
                            break;
                        case RIGHT:
                            if ((y + 1 < 9) && !isElement(zones, x, y + 1) &&
                                    (map[x][y + 1] == -1 || map[x][y + 1] > currentPath + 1)) {
                                map[x][y + 1] = currentPath + 1;
                                y += 1;
                                flag = true;
                            }
                            break;
                        case LEFT:
                            if ((y - 1 >= 0) && !isElement(zones, x, y - 1) &&
                                    (map[x][y - 1] == -1 || map[x][y - 1] > currentPath + 1)) {
                                map[x][y - 1] = currentPath + 1;
                                y -= 1;
                                flag = true;
                            }
                            break;
                        case DOWN_LEFT:
                            if ((x + 1 < 9 && y - 1 >= 0) && !isElement(zones, x + 1, y - 1) &&
                                    (map[x + 1][y - 1] == -1 || map[x + 1][y - 1] > currentPath + 1)) {
                                map[x + 1][y - 1] = currentPath + 1;
                                x += 1;
                                y -= 1;
                                flag = true;
                            }
                            break;
                        case DOWN_RIGHT:
                            if ((x + 1 < 9 && y + 1 < 9) && !isElement(zones, x + 1, y + 1) &&
                                    (map[x + 1][y + 1] == -1 || map[x + 1][y + 1] > currentPath + 1)) {
                                map[x + 1][y + 1] = currentPath + 1;
                                x += 1;
                                y += 1;
                                flag = true;
                            }
                            break;
                        case UP_LEFT:
                            if ((x - 1 >= 0 && y - 1 >= 0) && !isElement(zones, x - 1, y - 1) &&
                                    (map[x - 1][y - 1] == -1 || map[x - 1][y - 1] > currentPath + 1)) {
                                map[x - 1][y - 1] = currentPath + 1;
                                x -= 1;
                                y -= 1;
                                flag = true;
                            }
                            break;
                        case UP_RIGHT:
                            if ((x - 1 >= 0 && y + 1 < 9) && !isElement(zones, x - 1, y + 1) &&
                                    (map[x - 1][y + 1] == -1 || map[x - 1][y + 1] > currentPath + 1)) {
                                map[x - 1][y + 1] = currentPath + 1;
                                x -= 1;
                                y += 1;
                                flag = true;
                            }
                            break;
                    }
                    boolean t = true;

                    /* If step was done */
                    if (flag) {
                        /* Add new element to sequence of path */
                        way.add(new Cell(x, y));
                        /* Call new recursive call and get the returned flag value */
                        t = calculatePath(new Cell(x, y), currentPath + 1,
                                zones, cask, way, destination, map).flag;
                    }
                    /* If algorithm returned false, return false again (will act to finish all the recursion calls */
                    if (!t) return new Outcome(shortestPath, false);
                }
            }
        } else {
            /* If current Cell is the destination */
            if (currentPath < shortestPath) {
                /* Change the shortest path if the current is lesser in previous shortest */
                shortestPath = currentPath;
                pathSequence = path;
            }
        }
        return new Outcome(shortestPath, true);
    }

    /**
     * Initializing the initial schema of danger zones
     *
     * @return array of danger Cells
     */
    private Cell[] initializeZone() {
        Cell[] zones = new Cell[15];
        zones[0] = new Cell(kraken.x, kraken.y);
        zones[1] = new Cell(kraken.x + 1, kraken.y);
        zones[2] = new Cell(kraken.x - 1, kraken.y);
        zones[3] = new Cell(kraken.x, kraken.y + 1);
        zones[4] = new Cell(kraken.x, kraken.y - 1);

        zones[5] = new Cell(jones.x, jones.y);
        zones[6] = new Cell(jones.x + 1, jones.y);
        zones[7] = new Cell(jones.x - 1, jones.y);
        zones[8] = new Cell(jones.x, jones.y + 1);
        zones[9] = new Cell(jones.x, jones.y - 1);
        zones[10] = new Cell(jones.x + 1, jones.y + 1);
        zones[11] = new Cell(jones.x + 1, jones.y - 1);
        zones[12] = new Cell(jones.x - 1, jones.y - 1);
        zones[13] = new Cell(jones.x - 1, jones.y + 1);

        zones[14] = new Cell(rock.x, rock.y);

        return zones;
    }

    /**
     * Check if any Cell of array has such coordinates
     *
     * @param array - array to check
     * @param x     - first coordinate
     * @param y     - second coordinate
     * @return true if such coordinate exists, false oterwise
     */
    private boolean isElement(Cell[] array, int x, int y) {
        for (Cell cell :
                array) {
            if (cell.x == x && cell.y == y) return true;
        }
        return false;
    }

    /**
     * Check if any Cell of array list has such coordinates
     *
     * @param array - array list to check
     * @param x     - first coordinate
     * @param y     - second coordinate
     * @return true if such coordinate exists, false oterwise
     */
    private boolean isElement(ArrayList<Cell> array, int x, int y) {
        for (Cell cell :
                array) {
            if (cell.x == x && cell.y == y) return true;
        }
        return false;
    }

    /**
     * Check if Kraken would be killed from current Cell
     *
     * @param x     - first coordinate of current Cell
     * @param y     - second coordinate of current Cell
     * @param flag  - whether Tortuga was passed
     * @param zones - current schema of danger zones
     */
    private void neutralizeKraken(int x, int y, boolean flag, Cell[] zones) {
        if (((x + 1 == kraken.x && y + 1 == kraken.y) ||
                (x - 1 == kraken.x && y + 1 == kraken.y) ||
                (x + 1 == kraken.x && y - 1 == kraken.y) ||
                (x - 1 == kraken.x && y - 1 == kraken.y)) && flag) {
            /* If current Cell is diagonal and Tortuga was already passed
            than Kraken danger zones are no more exist */
            zones[0] = new Cell(-1, -1);
            zones[1] = new Cell(-1, -1);
            zones[2] = new Cell(-1, -1);
            zones[3] = new Cell(-1, -1);
            zones[4] = new Cell(-1, -1);
        }
    }

    /**
     * Prints result inside the file
     *
     * @param time   - time of algorithm execution
     * @param answer - the shortest path or 100 (in Lose case)
     * @param path   - sequence of path
     * @throws IOException as it prints result inside the file
     */
    private void print(long time, int answer, ArrayList<Cell> path) throws IOException {
        /* Open file */
        BufferedWriter writer = new BufferedWriter(new FileWriter("outputBacktracking.txt"));
        if (answer >= 100) {
            writer.write("Lose\n");                             // In case of Lose
        } else {
            writer.write("Win\n" + answer + "\n");              // In case of Win
            /* Prints path sequence */
            for (Cell cell :
                    path) {
                writer.write("[" + cell.x + "," + cell.y + "] ");
            }
            writer.write("\n-------------------\n  0 1 2 3 4 5 6 7 8\n");
            /* Prints map */
            for (int i = 0; i < 9; i++) {
                writer.write(i + " ");
                for (int j = 0; j < 9; j++) {
                    if (!isElement(path, i, j)) writer.write("- ");
                    else writer.write("* ");
                }
                writer.write("\n");
            }
            writer.write("-------------------\n" + time / 1000000.0 + " ms");
        }
        writer.close();
    }

    /**
     * 1. Running Backtracking algorithm from initial position to Tortuga then from Tortuga to Dead Man's Chest
     * 2. Running algorithm from initial position to Dead Man's Chest
     * Compares 1 and 2 paths and chooses the shortest one if it exists (Lose otherwise)
     *
     * @throws IOException as it prints result inside the file through resulting method
     */
    void run() throws IOException {

        long startTime = System.nanoTime();
        int[][] path = mapPath(jack);                        // Current configuration of map
        pathSequence = new ArrayList<>();
        pathSequence.add(jack);

        /* Running Backtracking algorithm from initial position to Tortuga */
        int with = calculatePath(new Cell(jack.x, jack.y), 0, initializeZone(),
                false, pathSequence, new Cell(rum.x, rum.y), path).path;

        shortestPath = 100;
        path = mapPath(rum);                                 // Current configuration of map

        /* Running Backtracking algorithm from Tortuga to Dead Man's Chest */
        with += calculatePath(new Cell(rum.x, rum.y), 0, initializeZone(),
                false, pathSequence, new Cell(heart.x, heart.y), path).path;

        shortestPath = with;
        path = mapPath(jack);                               // Current configuration of map
        ArrayList<Cell> path1 = new ArrayList<>(pathSequence);
        pathSequence = new ArrayList<>();
        pathSequence.add(jack);

        /* Running algorithm from initial position to Dead Man's Chest */
        int without = calculatePath(new Cell(jack.x, jack.y), 0, initializeZone(),
                false, pathSequence, new Cell(heart.x, heart.y), path).path;
        ArrayList<Cell> path2 = new ArrayList<>(pathSequence);

        /* Choose the shortest path */
        int result;
        ArrayList<Cell> resultPath;
        if (without >= with) {
            result = with;
            resultPath = path1;
        } else {
            result = without;
            resultPath = path2;
        }
        long finishTime = System.nanoTime();

        /* Call method to output result */
        print(finishTime - startTime, result, resultPath);
    }

    /**
     * Initialize map
     *
     * @return current map
     */
    private int[][] mapPath(Cell init) {
        int[][] path = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                path[i][j] = -1;
            }
        }
        path[init.x][init.y] = 0;
        return path;
    }

    /**
     * Place first priority to the end of the priority array
     * Used for 2 perception type
     *
     * @param priorities current array of priorities
     */
    private void swap(Priority[] priorities) {
        Priority element = priorities[0];
        System.arraycopy(priorities, 1, priorities, 0, 7);
        priorities[7] = element;
    }
}

/**
 * Class representing A* algorithm
 */
class AStar {
    /* Opened Node list and closed Node set */
    ArrayList<Node> opened;
    Set<Node> closed;
    Node initial;                                           // Initial Node

    /* Array list of array lists representing current map */
    ArrayList<ArrayList<Node>> map;

    /* Cell objects for Jack Sparrow, Davy Jones, Kraken, Rock, Dead Man's Chest and Tortuga */
    private final Cell jack;
    private final Cell jones;
    private final Cell kraken;
    private final Cell rock;
    private final Cell heart;
    private final Cell rum;
    Cell[] zones;                                           // Danger zones
    int perception;                                         // Type of perception

    /**
     * Constructor for A* algorithm
     *
     * @param jack       - Cell for Jack Sparrow
     * @param jones      - Cell for Davy Jones
     * @param kraken     - Cell for Kraken
     * @param rock       - Cell for Rock
     * @param heart      - Cell for Dead Man's Chest
     * @param rum        - Cell for Tortuga
     * @param perception - perception type
     */
    AStar(Cell jack, Cell jones, Cell kraken, Cell rock, Cell heart, Cell rum, int perception) {
        /* Initializing all the parameters */
        this.rum = rum;
        this.kraken = kraken;
        this.jack = jack;
        this.jones = jones;
        this.heart = heart;
        this.rock = rock;
        this.perception = perception;
        opened = new ArrayList<>();
        closed = new HashSet<>();
        initial = new Node(jack, 0, 0, null);
        initial.parent = initial;
        initial.zone = initializeZone();
        opened.add(initial);
        map = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                map.get(i).add(new Node(new Cell(i, j), -1, -1, null));
            }
        }
        map.get(0).set(0, initial);
    }

    /**
     * Algorithm calculating the shortest past
     *
     * @param dx   - destination first coordinate
     * @param dy   - destination second coordinate
     * @param zone - danger zones
     */
    private void calculatePath(int dx, int dy, Cell[] zone) {

        boolean flag = false;                               // Whether the shortest path is found
        /* If Jack Sparrow wasn't spawned inside the danger zone */
        if (!isElement(zone, jack.x, jack.y))
            /* While opened list is not empty */
            while (!opened.isEmpty()) {

                /* Choose node for current step */
                Node current = find(opened);
                opened.remove(current);

                /* Whether Tortuga was visited or it is in current Node */
                current.rum = current.location.x == rum.x && current.location.y == rum.y || current.parent.rum;

                /* Whether Kraken was killed or still not */
                System.arraycopy(current.parent.zone, 0, current.zone, 0, 15);

                /* Initialize neighbourhood of the current Node */
                ArrayList<Node> neighbourhood = initialize(current, current.zone);

                /* For each Node in neighbourhood */
                for (Node node :
                        neighbourhood) {
                    if (node.location.x == dx && node.location.y == dy) {
                        /* If the current location is the goal */
                        node.parent = current;
                        node.rum = current.rum;
                        node.g = current.g + 1;
                        node.h = measureDistance(node.location.x, node.location.y, dx, dy);
                        node.f = node.g + node.h;
                        flag = true;
                        break;
                    } else {
                        if (opened.contains(node)) {
                            /* If current Node inside the opened list */
                            if (node.g != -1) {
                                /* Path was calculated previously */
                                if (current.g + 1 < node.g) {
                                    /* Current path is lower than was previously */
                                    node.parent = current;
                                    node.rum = current.rum;
                                    node.g = current.g + 1;
                                }
                                node.h = measureDistance(node.location.x, node.location.y, dx, dy);
                                node.f = node.g + node.h;
                            } else {
                                /* Path wasn't calculated previously */
                                node.parent = current;
                                node.rum = current.rum;
                                node.g = current.g + 1;
                                node.h = measureDistance(node.location.x, node.location.y, dx, dy);
                                node.f = node.g + node.h;
                            }
                        } else if (closed.contains(node) && current.g + 1 >= node.g) {
                            /* Do nothing. In this case nothing should change */
                        } else {
                            node.parent = current;
                            node.rum = current.rum;
                            node.g = current.g + 1;
                            node.h = measureDistance(node.location.x, node.location.y, dx, dy);
                            node.f = node.g + node.h;
                            opened.add(node);
                        }
                    }
                }
                closed.add(current);                        // Add current Node to closed set
                if (flag) break;
            }
    }

    /**
     * Find next minimal Node
     *
     * @param opened list for already opened Nodes
     * @return next Node
     */
    private Node find(ArrayList<Node> opened) {
        Node least = opened.get(0);
        for (Node node :
                opened) {
            /* All the following step is checked in case of equality in previous
            Firstly, checks function f value */
            if (node.f < least.f) least = node;
            else if (node.f == least.f) {
                /* Secondly, checks function h value */
                if (node.h < least.h) least = node;
                else if (node.h == least.h) {
                    /* Thirdly, check priority parameter for 2 type of perception */
                    if (least.priority && !node.priority && perception == 2) {
                        least = node;
                    }
                }
            }
        }
        return least;
    }

    /**
     * Initializing neighbourhood around current Node
     *
     * @param current - Node for which neighbourhood is checked
     * @param zone    - danger zone
     * @return array list of neighbourhood Nodes
     */
    private ArrayList<Node> initialize(Node current, Cell[] zone) {

        ArrayList<Node> neighbourhood = new ArrayList<>();
        /* Renew the danger zone if Tortuga was visited */
        Cell[] zones = krakenChecker(current.location.x, current.location.y, current.rum, zone);

        /* For each case next Cell should be inside the map and not in danger zone
        Also for perception type 2 checking for horizontal and vertical cells second Node from current in any direction
        In case of danger, priority parameter of next Node in this direction will be True */
        if (current.location.x + 1 < 9 && !isElement(zones, current.location.x + 1, current.location.y)) {
            neighbourhood.add(map.get(current.location.x + 1).get(current.location.y));
            if (perception == 2 && current.location.x + 2 < 9 && isElement(zones, current.location.x + 2, current.location.y)) {
                map.get(current.location.x + 1).get(current.location.y).priority = true;
            }
        }
        if (current.location.x + 1 < 9 && current.location.y + 1 < 9 && !isElement(zones, current.location.x + 1, current.location.y + 1)) {
            neighbourhood.add(map.get(current.location.x + 1).get(current.location.y + 1));
        }
        if (current.location.y + 1 < 9 && !isElement(zones, current.location.x, current.location.y + 1)) {
            neighbourhood.add(map.get(current.location.x).get(current.location.y + 1));
            if (perception == 2 && current.location.y + 2 < 9 && isElement(zones, current.location.x, current.location.y + 2)) {
                map.get(current.location.x).get(current.location.y + 1).priority = true;
            }
        }
        if (current.location.x - 1 >= 0 && current.location.y + 1 < 9 && !isElement(zones, current.location.x - 1, current.location.y + 1)) {
            neighbourhood.add(map.get(current.location.x - 1).get(current.location.y + 1));
        }
        if (current.location.x - 1 >= 0 && !isElement(zones, current.location.x - 1, current.location.y)) {
            neighbourhood.add(map.get(current.location.x - 1).get(current.location.y));
            if (perception == 2 && current.location.x - 2 >= 0 && isElement(zones, current.location.x - 2, current.location.y)) {
                map.get(current.location.x - 1).get(current.location.y).priority = true;
            }
        }
        if (current.location.x - 1 >= 0 && current.location.y - 1 >= 0 && !isElement(zones, current.location.x - 1, current.location.y - 1)) {
            neighbourhood.add(map.get(current.location.x - 1).get(current.location.y - 1));
        }
        if (current.location.y - 1 >= 0 && !isElement(zones, current.location.x, current.location.y - 1)) {
            neighbourhood.add(map.get(current.location.x).get(current.location.y - 1));
            if (perception == 2 && current.location.y - 2 >= 0 && isElement(zones, current.location.x, current.location.y - 2)) {
                map.get(current.location.x).get(current.location.y - 1).priority = true;
            }
        }
        if (current.location.x + 1 < 9 && current.location.y - 1 >= 0 && !isElement(zones, current.location.x + 1, current.location.y - 1)) {
            neighbourhood.add(map.get(current.location.x + 1).get(current.location.y - 1));
        }

        return neighbourhood;
    }

    /**
     * Measure Chebyshev Distance
     *
     * @param x0 - first coordinate of the initial point
     * @param y0 - second coordinate of the initial point
     * @param x  - first coordinate of the destination point
     * @param y  - second coordinate of the destination point
     * @return Chebyshev Distance
     */
    private int measureDistance(int x0, int y0, int x, int y) {
        return max(abs(x - x0), abs(y - y0));
    }

    /**
     * Initializing the initial schema of danger zones
     *
     * @return array of danger Cells
     */
    private Cell[] initializeZone() {
        Cell[] zones = new Cell[15];
        zones[0] = new Cell(kraken.x, kraken.y);
        zones[1] = new Cell(kraken.x + 1, kraken.y);
        zones[2] = new Cell(kraken.x - 1, kraken.y);
        zones[3] = new Cell(kraken.x, kraken.y + 1);
        zones[4] = new Cell(kraken.x, kraken.y - 1);

        zones[5] = new Cell(jones.x, jones.y);
        zones[6] = new Cell(jones.x + 1, jones.y);
        zones[7] = new Cell(jones.x - 1, jones.y);
        zones[8] = new Cell(jones.x, jones.y + 1);
        zones[9] = new Cell(jones.x, jones.y - 1);
        zones[10] = new Cell(jones.x + 1, jones.y + 1);
        zones[11] = new Cell(jones.x + 1, jones.y - 1);
        zones[12] = new Cell(jones.x - 1, jones.y - 1);
        zones[13] = new Cell(jones.x - 1, jones.y + 1);

        zones[14] = new Cell(rock.x, rock.y);

        return zones;
    }

    /**
     * Check if Kraken would be killed from current Cell
     *
     * @param x     - first coordinate of current Cell
     * @param y     - second coordinate of current Cell
     * @param flag  - whether Tortuga was passed
     * @param zones - current schema of danger zones
     * @return new schema of danger zones
     */
    private Cell[] krakenChecker(int x, int y, boolean flag, Cell[] zones) {
        if (((x + 1 == kraken.x && y + 1 == kraken.y) ||
                (x - 1 == kraken.x && y + 1 == kraken.y) ||
                (x + 1 == kraken.x && y - 1 == kraken.y) ||
                (x - 1 == kraken.x && y - 1 == kraken.y)) && flag) {
            /* If current Cell is diagonal and Tortuga was already passed
            than Kraken danger zones are no more exist */
            zones[0] = new Cell(-1, -1);
            zones[1] = new Cell(-1, -1);
            zones[2] = new Cell(-1, -1);
            zones[3] = new Cell(-1, -1);
            zones[4] = new Cell(-1, -1);
        }
        return zones;
    }

    /**
     * Check if any Cell of array has such coordinates
     *
     * @param array - array to check
     * @param x     - first coordinate
     * @param y     - second coordinate
     * @return true if such coordinate exists, false oterwise
     */
    private boolean isElement(Cell[] array, int x, int y) {
        for (Cell cell :
                array) {
            if (cell.x == x && cell.y == y) return true;
        }
        return false;
    }

    /**
     * Check if any Cell of array list has such coordinates
     *
     * @param array - array list to check
     * @param x     - first coordinate
     * @param y     - second coordinate
     * @return true if such coordinate exists, false oterwise
     */
    private boolean isElement(ArrayList<Cell> array, int x, int y) {
        for (Cell cell :
                array) {
            if (cell.x == x && cell.y == y) return true;
        }
        return false;
    }

    /**
     * 1. Running Backtracking algorithm from initial position to Tortuga then from Tortuga to Dead Man's Chest
     * 2. Running algorithm from initial position to Dead Man's Chest
     * Compares 1 and 2 paths and chooses the shortest one if it exists (Lose otherwise)
     *
     * @throws IOException as it prints result inside the file through resulting method
     */
    void run() throws IOException {
        long startTime = System.nanoTime();

        /* Initialize all necessary parameters */
        initializeParameters(jack.x, jack.y);
        zones = initializeZone();

        /* Run algorithm from initial position to Tortuga and restore path if exists */
        calculatePath(rum.x, rum.y, zones);
        ArrayList<Cell> path1 = new ArrayList<>();
        int with = map.get(rum.x).get(rum.y).f;
        if (with >= 0) path1 = restorePath(jack.x, jack.y, map.get(rum.x).get(rum.y));

        /* Initialize all necessary parameters */
        initializeParameters(rum.x, rum.y);

        /* Run algorithm from Tortuga to Dead Man's Chest and restore path if exists */
        calculatePath(heart.x, heart.y, zones);
        ArrayList<Cell> path2 = new ArrayList<>();
        int with2 = map.get(heart.x).get(heart.y).f;
        if (with2 >= 0) {
            path2 = restorePath(rum.x, rum.y, map.get(heart.x).get(heart.y));
            path2.remove(path2.size() - 1);
        }
        path2.addAll(path1);
        Collections.reverse(path2);

        /* Initialize all necessary parameters */
        initializeParameters(jack.x, jack.y);
        zones = initializeZone();

        /* Run algorithm from initial position to Dead Man's Chest and restore path if exists */
        calculatePath(heart.x, heart.y, zones);
        ArrayList<Cell> path3 = new ArrayList<>();
        int without = map.get(heart.x).get(heart.y).f;
        if (without >= 0) path3 = restorePath(jack.x, jack.y, map.get(heart.x).get(heart.y));
        Collections.reverse(path3);

        /* Check witch path is shorter, if it exists */
        int result = 0;
        ArrayList<Cell> resultPath = new ArrayList<>();
        if (with < 0 || with2 < 0) {
            if (without > 0) {
                result = without;
                resultPath = path3;
            } else result = -1;
        } else if (with >= 0 && with2 >= 0) {
            if (without > 0) {
                if (without <= with + with2) {
                    result = without;
                    resultPath = path3;
                } else {
                    result = with + with2;
                    resultPath = path2;
                }
            } else {
                result = with + with2;
                resultPath = path2;
            }
        }
        long finishTime = System.nanoTime();
        print(finishTime - startTime, result, resultPath);
    }

    /**
     * Prints result inside the file
     *
     * @param time   - time of algorithm execution
     * @param answer - the shortest path or 100 (in Lose case)
     * @param path   - sequence of path
     * @throws IOException as it prints result inside the file
     */
    private void print(long time, int answer, ArrayList<Cell> path) throws IOException {
        /* Open file */
        BufferedWriter writer = new BufferedWriter(new FileWriter("outputAStar.txt"));
        if (answer < 0) {
            writer.write("Lose\n");                             // In case of Lose
        } else {
            writer.write("Win\n" + answer + "\n");              // In case of Win

            /* Prints path sequence */
            for (Cell cell :
                    path) {
                writer.write("[" + cell.x + "," + cell.y + "] ");
            }
            writer.write("\n-------------------\n  0 1 2 3 4 5 6 7 8\n");

            /* Prints map */
            for (int i = 0; i < 9; i++) {
                writer.write(i + " ");
                for (int j = 0; j < 9; j++) {
                    if (!isElement(path, i, j)) writer.write("- ");
                    else writer.write("* ");
                }
                writer.write("\n");
            }
            writer.write("-------------------\n" + time / 1000000.0 + " ms");
        }
        writer.close();
    }

    /**
     * Reinitialize opened list, closed set, map and initial Node
     *
     * @param x - first coordinate of initial Node
     * @param y - second coordinate of initial Node
     */
    private void initializeParameters(int x, int y) {
        opened = new ArrayList<>();
        closed = new HashSet<>();
        initial = new Node(new Cell(x, y), 0, 0, null);
        initial.parent = initial;
        initial.zone = initializeZone();
        opened.add(initial);
        map = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                map.get(i).add(new Node(new Cell(i, j), -1, -1, null));
            }
        }
        map.get(x).set(y, initial);
    }

    /**
     * Restore the path from destination to initial Node
     *
     * @param x0   - first coordinate of initial Node
     * @param y0   - second coordinate of initial Node
     * @param node - destination Node
     * @return path sequence
     */
    private ArrayList<Cell> restorePath(int x0, int y0, Node node) {
        int a = node.location.x;
        int b = node.location.y;
        ArrayList<Cell> path = new ArrayList<>();
        Node current = node;
        path.add(current.location);

        /* Run for parents starting from destination
         Store the coordinates and stop when initial Node reached */
        while (a != x0 || b != y0) {
            Node parent = current.parent;
            path.add(parent.location);
            a = parent.location.x;
            b = parent.location.y;
            current = parent;
        }
        return path;
    }

}