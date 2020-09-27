import java.util.ArrayList;
import java.util.Random;

//This class will implement the Java Runnable interface that will calculate col_idxth column of the result matrix

class ColumnCalculator implements Runnable
{
    Matrix m1;
    Matrix m2;
    Matrix r;
    int col_idx;

    ColumnCalculator(Matrix m1, Matrix m2, Matrix r, int col)
    {
        this.m1 = m1;
        this.m2 = m2;
        this.r = r;
        this.col_idx = col;
    }//end ColumnCalculator constructor

    @Override
    public void run()
    {
        for (int row = 0; row < r.rows; row++) {
            for (int col = 0; col < r.cols; col++) {
                r.values[row][col] = 0;
                for (int i = 0; i < col_idx; i++) {
                    r.values[row][col] += m1.values[row][i] * m2.values[i][col];
                }//end for loop with i
            }// end for loop with col
        }//end for loop with row
    }
}

public class Matrix {
    int rows;
    int cols;
    double[][] values;


    Matrix(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.values = init_matrix(r, c);
    }//end constructor matrix

    //
    Matrix(int r, int c, double v[][]) {
        //check size of array
        rows = v.length;
        cols = v[0].length;

        // 2D Array has less rows and cols than r and c
        // resize values to the new size
        this.values = new double[r][c];

        // copy old values to new values
        for (int i = 0; i < v.length; i++)
            for (int j = 0; j < v[0].length; j++)
                values[i][j] = v[i][j];

        // add 0.0 to additional rows and columns
        for (; rows < r; rows++) {
            for (; cols < c; cols++) {
                values[rows][cols] = 0.0;

            }// end for loop with x
        }//end for loop with y
    }//end matrix with three variables

    private double[][] init_matrix(int r, int c) {
        double[][] values = new double[r][c];
        Random rand = new Random();

        for (int y = 0; y < r; y++) {
            for (int x = 0; x < c; x++) {
                values[y][x] = rand.nextDouble() * 10.0;
            }// end for loop with x
        }//end for loop with y

        return values;
    }

    private void print() {
        if (values == null) {
            System.out.print("Matrix is null!/n");
        } else {
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    System.out.print(values[y][x] + " , ");
                }// end for loop with x
                System.out.println();
            }//end for loop with y
        }
    }//end print()

    Matrix MultiplyBy(Matrix m) {
        Matrix result = null;

        //Check to make sure # of columns equal # of rows
        if (this.rows != m.cols) {
            System.out.print("Rows of Matrix 1 do not match columns in Matrix 2.");
            return result;
        }//end if

        result = new Matrix(rows, m.cols);
        for (int y = 0; y < result.rows; y++) {
            for (int x = 0; x < result.cols; x++) {
                result.values[y][x] = 0;
                for (int i = 0; i < cols; i++) {
                    result.values[y][x] += values[y][i] * m.values[i][x];
                }//end for loop with i
            }// end for loop with x
        }//end for loop with y
        return result;

    }//end MultiplyBy()

    Matrix MultiplyByThreads(Matrix m) {
        Matrix result = null;

        //Check to make sure # of columns equal # of rows
        if (this.rows != m.cols) {
            System.out.print("Rows of Matrix 1 do not match columns in Matrix 2.");
            return result;
        }//end if

        result = new Matrix(rows, m.cols);
        ArrayList<Thread> threads = new ArrayList<>();
        int columns = this.values[0].length;
        for (int col = 0; col < columns; col++) {
            ColumnCalculator task = new ColumnCalculator(this, m, result, col);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);

            // wait for threads 10 at a time
            if (threads.size() % 10 == 0) {
                for (Thread t : threads){
                    try {
                        t.join();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    } //try...catch
                } //for
                threads.clear();
            }
        }
        return result;
    }//end MultiplyByThreads()

    public static void main(String[] args)
    {
        Matrix m1 = new Matrix(1000, 1000);
        //m1.print();

        Matrix m2 = new Matrix(1000, 1000);
        //m2.print();

        System.out.println("Begin Matrix product...");
        long startTime = System.currentTimeMillis();

        Matrix result = m1.MultiplyBy(m2);

        long endTime = System.currentTimeMillis();
        System.out.println("Ending Matrix product");

        long duration = (endTime - startTime);
        System.out.println("Time: " + duration + " milliseconds");
        //result.print();
//
//        int r = 2;
//        int c = 2;
//
//        double[][] values = new double[r][c];
//        Random rand = new Random();
//
//        for (int y = 0; y < r; y++) {
//            for (int x = 0; x < c; x++) {
//                values[y][x] = rand.nextDouble() * 10.0;
//            }// end for loop with x
//        }//end for loop with y
//
//        Matrix m3 = new Matrix(3, 3, values);
//        m3.print();

        //Implementation here
        // Test your MultiplyByThreads() in both accuracy and time performance
        System.out.println("Begin Thread Matrix product...");
        startTime = System.currentTimeMillis();

        Matrix result2 = m1.MultiplyByThreads(m2);

        endTime = System.currentTimeMillis();
        System.out.println("Ending Thread Matrix product");
        duration = (endTime - startTime);
        System.out.println("Time: " + duration + " milliseconds");
        //result2.print();

    }//end main

}//end class Matrix()
